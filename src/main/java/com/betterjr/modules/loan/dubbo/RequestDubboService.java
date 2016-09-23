package com.betterjr.modules.loan.dubbo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfRequestNoticeService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.workflow.IFlowService;
import com.betterjr.modules.workflow.data.CustFlowNodeData;
import com.betterjr.modules.workflow.data.FlowCommand;
import com.betterjr.modules.workflow.data.FlowInput;
import com.betterjr.modules.workflow.data.FlowStatus;
import com.betterjr.modules.workflow.data.FlowType;

@Service(interfaceClass = IScfRequestService.class)
public class RequestDubboService implements IScfRequestService {
    private static final String APPROVALRESULT_0 = "0";

    protected final Logger logger = LoggerFactory.getLogger(RequestDubboService.class);

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfRequestSchemeService approvedService;
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private ScfRequestNoticeService requestNoticeService;
    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfAgreementService agreementService;

    @Reference(interfaceClass = IFlowService.class)
    private IFlowService flowService;
    
    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("2.0版-新增融资申请，入参：" + anMap);
        ScfRequest request = requestService.saveStartRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj());
        
        // 启动流程
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(request.getRequestNo()));
        input.setMoney(request.getBalance());
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setFinancerOperOrg(UserUtils.getOperatorInfo().getOperOrg());
        input.setType(FlowType.Trade);

        CustInfo coreCustInfo = custAccountService.selectByPrimaryKey(request.getCoreCustNo());
        input.setCoreOperOrg(coreCustInfo.getOperOrg());
        flowService.start(input);
        
        // 修改融资状态
        FlowStatus search = new FlowStatus();
        search.setBusinessId(Long.parseLong(request.getRequestNo()));
        Page<FlowStatus> list = flowService.queryCurrentWorkTask(null, search);
        if (!Collections3.isEmpty(list)) {
            request.setTradeStatus(Collections3.getFirst(list).getCurrentNodeId().toString());
            request = requestService.saveModifyRequest(request, request.getRequestNo());
        }
        return AjaxObject.newOk(request).toJson();
    }

    @Override
    public String webSaveModifyRequest(Map<String, Object> anMap, String anRequestNo) {
        logger.debug("修改融资申请，入参：" + anMap);
        return AjaxObject.newOk(requestService.saveModifyRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj(), anRequestNo)).toJson();
    }

    @Override
    public String webQueryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资申请，入参：" + anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("融资申请查询成功", requestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindRequestDetail(Map<String, Object> anMap, String anRequestNo) {
        logger.debug("查询融资申请详情，入参：" + anMap);
        return AjaxObject.newOk(requestService.findRequestDetail(anRequestNo)).toJson();
    }

    @Override
    public String webFindSchemeDetail(String anRequestNo) {
        logger.debug("获取融资审批详情，入参：anRequestNo" + anRequestNo);
        return AjaxObject.newOk(approvedService.findSchemeDetail(anRequestNo)).toJson();
    }

    @Override
    public String webSaveModifyScheme(Map<String, Object> anMap) {
        logger.debug("修改融资审批详情，入参：" + anMap);
        ScfRequestScheme scheme = (ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(approvedService.saveModifyScheme(scheme)).toJson();
    }

    @Override
    public String webQuerySchemeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资审批，入参：anRequestNo：" + anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功",
                approvedService.querySchemeList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webApproveRequest(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("一般审批，入参anRequestNo:" + anRequestNo 
                + "approvalResult：" + anApprovalResult 
                + "-returnNode:" + anReturnNode 
                + "-description:" + anDescription);
        
        //
        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        
        // 执行流程
        execFllow(anRequestNo, request.getBalance(), anApprovalResult, anReturnNode, anDescription);

        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webOfferScheme(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-出具融资方案，入参：" + anMap);

        ScfRequestScheme scheme = new ScfRequestScheme();
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            scheme = (ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj();
            requestService.saveOfferScheme(scheme);
        }

        // 执行流程
        execFllow(anMap.get("requestNo").toString(), scheme.getApprovedBalance(), anApprovalResult, anReturnNode, anDescription);

        return AjaxObject.newOk("操作成功", scheme).toJson();
    }

    @Override
    public String webConfirmScheme(String anRequestNo, String anApprovalResult, String smsCode) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo + "-  anApprovalResult:" + anApprovalResult);
        
        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        ScfRequestScheme scheme = new ScfRequestScheme();
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {

            //经销商融资的时候 不需要发送
            if(false == BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType())){
               
                //电子合同类型，0：应收账款债权转让通知书，1：买方确认意见，2三方协议书
                if(false == agreementService.sendValidCodeByRequestNo(anRequestNo, "0", smsCode)){
                    //return AjaxObject.newError("操作失败：短信验证码错误").toJson();
                }
            }
            
            //修改融资方案确认状态
            scheme = requestService.saveConfirmScheme(anRequestNo, "1");
        }else{
            //取消签约
            agreementService.cancelElecAgreement(anRequestNo, "");
        }
        
        // 执行流程
        execFllow(anRequestNo, scheme.getApprovedBalance(), anApprovalResult, "", "");
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webRequestTradingBackgrand(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-发起贸易背景确认，入参：anRequestNo" + anRequestNo);

        ScfRequest request = new ScfRequest();
        if (BetterStringUtils.equals(APPROVALRESULT_0, anApprovalResult) == true) {
            //查看 应收帐款转让通知书 是否存在，如果不存在则操作失败
            if(null == requestNoticeService.findTransNotice(anRequestNo)){
                return AjaxObject.newError("操作失败,没有找到相应的-应收账款转让通知书").toJson();
            }
            
            // 保存发起状态
            requestService.saveRequestTradingBackgrand(anRequestNo);
        }

        // 执行流程
        execFllow(anRequestNo, request.getBalance(), anApprovalResult, anReturnNode, anDescription);

        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anApprovalResult, String smsCode) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo + "-  anApprovalResult:" + anApprovalResult);

        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            // 经销商签署的是 三方协议
            String  agreeType = BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType()) ? "2" :"1";
           
            //签署协议
            if(false == agreementService.sendValidCodeByRequestNo(anRequestNo, agreeType, smsCode)){
               // return AjaxObject.newError("操作失败：短信验证码错误").toJson();
            }
            
            // 保存确认贸易背景确认状态
            request = requestService.confirmTradingBackgrand(anRequestNo, "1");
        }
        else{
            //取消签约
            agreementService.cancelElecAgreement(anRequestNo, "");
        }

        // 执行流程
        execFllow(anRequestNo, request.getBalance(), anApprovalResult, "", "");

        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmLoan(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-放款确认：" + anMap);

        ScfRequest request = new ScfRequest();
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            request = requestService.saveConfirmLoan((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
        }

        // 执行流程
        execFllow(request.getRequestNo(), request.getBalance(), anApprovalResult, anReturnNode, anDescription);

        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webGetServiecFee(String anRequestNo, BigDecimal anLoanBalance) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("servicefeeBalance", payPlanService.getFee(anRequestNo, anLoanBalance, 2));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }

    @Override
    public String webGetEndDate(String anRequestNo, String anStartDate) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        anMap.put("endDate", payPlanService.getEndDate(anStartDate, request.getApprovedPeriod(), request.getApprovedPeriodUnit()));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }

    @Override
    public String webGetInsterest(String anRequestNo, BigDecimal anLoanBalance) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("interestBalance", payPlanService.getFee(anRequestNo, anLoanBalance, 3));
        anMap.put("managementBalance", payPlanService.getFee(anRequestNo, anLoanBalance, 1));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }

    @Override
    public String webQueryWorkTask(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        // 查询当前用户任务列表
        Page<FlowStatus> page = new Page<FlowStatus>(anPageNum, anPageSize, 1 == anFlag);
        if (BetterStringUtils.equals("1", anMap.get("taskType").toString())) {
            // 待办
            page = flowService.queryCurrentUserWorkTask(null, null);
        }
        else {
            // 已办
            page = flowService.queryCurrentUserHistoryWorkTask(null, null);
        }

        // 无数据
        if (0 == page.size()) {
            return AjaxObject.newOkWithPage("分页查询用户任务列表，无数据", new Page<ScfRequest>()).toJson();
        }

        List<Long> requestNos = new ArrayList<Long>();
        if(null != anMap.get("requestNo") && BetterStringUtils.isNotBlank(anMap.get("requestNo").toString())){
            requestNos.add(Long.parseLong(anMap.get("requestNo").toString()));
        }else{
            // 获取任务中的requestNo
           
            for (FlowStatus flowStatus : page) {
                requestNos.add(flowStatus.getBusinessId());
            }
        }
       
        anMap.remove("taskType");
        anMap.put("requestNo", requestNos);
        return AjaxObject.newOkWithPage("查询成功", requestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    /**
     * 执行流程
     * 
     * @param anRequestNo
     * @param money
     * @param anApprovalResult
     * @param anReturnNode
     * @param anDescription
     */
    private void execFllow(String anRequestNo, BigDecimal money, String anApprovalResult, String anReturnNode, String anDescription) {
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(anRequestNo));
        input.setMoney(money);
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setReason(anDescription);

        // 当前融资申请单
        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            // 下一步
            input.setCommand(FlowCommand.GoNext);
        }
        else if (BetterStringUtils.equals(anApprovalResult, "1")) {
            // 打回
            input.setRollbackNodeId(anReturnNode);
            input.setCommand(FlowCommand.Rollback);
        }
        else {
            // 拒绝
            input.setCommand(FlowCommand.Exit);

            // 解除订单关联
            orderService.unForzenInfoes(anRequestNo, null);
            
            //改为可用状态
            offerService.saveUpdateTradeStatus(request.getOfferId(), "1");
        }

        // 执行流程
        flowService.exec(input);

        // 查询-当前流程所在节点
        FlowStatus search = new FlowStatus();
        search.setBusinessId(Long.parseLong(request.getRequestNo()));
        Page<FlowStatus> list = flowService.queryCurrentWorkTask(null, search);

        // 设置申请表中的-融资审批状态-为当前流程所在节点
        if (!Collections3.isEmpty(list)) {
            request.setTradeStatus(Collections3.getFirst(list).getCurrentNodeId().toString());

            if (BetterStringUtils.equals(anApprovalResult, "2")) {
                // 流程-中止
                request.setFlowStatus("2");
            }
            else {
                // 流程-进行中
                request.setFlowStatus("1");
            }
        }
        else {
            // 流程-结束
            request.setFlowStatus("3");
        }

        // 修改-当前融资申请单（融资审批状态，流程状态）
        try {
            request = requestService.saveModifyRequest(request, request.getRequestNo());
        }
        catch (Exception e) {
            input.setRollbackNodeId(anReturnNode);
            input.setCommand(FlowCommand.Rollback);
            flowService.exec(input);
        }
    }

    public String webQueryTradeStatus() {
        List<CustFlowNodeData> list = flowService.findFlowNodesByType("Trade");
        requestService.getDefaultNode(list);
        return AjaxObject.newOk("查询成功", list).toJson();
    }

    @Override
    public String webQueryPendingRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询待批融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询待批融资成功", requestService.queryPendingRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryRepaymentRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询还款融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询还款融资成功", requestService.queryRepaymentRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webQueryCompletedRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询历史融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询历史融资成功", requestService.queryCompletedRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webQueryCoreEnterpriseRequest(Map<String, Object> anMap, String anRequestType, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询融资成功",
                requestService.queryCoreEnterpriseRequest(anQueryConditionMap, anRequestType, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFindPayPlan(Long anId) {
        logger.debug("查询还款计划详情，入参：" + anId);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("查询还款计划详情", payPlanService.findPayPlanDetail(anId)).toJson();
    }

}
