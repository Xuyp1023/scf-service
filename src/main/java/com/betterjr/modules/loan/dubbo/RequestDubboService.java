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
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.push.service.ScfSupplierPushService;
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
    private ScfOfferService offerService;
    @Autowired
    private ScfAgreementService agreementService;
    @Autowired
    private ScfRequestSchemeService schemeService;
    @Autowired
    private ScfCreditDetailService  creditDetailService;
    @Autowired
    private ScfSupplierPushService supplierPushService;
    @Autowired
    private ScfElecAgreementService elecAgreementService;

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
        logger.debug("一般审批，入参anRequestNo:" + anRequestNo + "approvalResult：" + anApprovalResult  + "-returnNode:" + anReturnNode  + "-description:" + anDescription);
        //
        BigDecimal balance = requestService.selectByPrimaryKey(anRequestNo).getBalance();
        
        // 执行流程
        execFlow(anRequestNo, balance, anApprovalResult, anReturnNode, anDescription);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webOfferScheme(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-出具融资方案，入参：" + anMap);

        ScfRequestScheme scheme = (ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj();
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            //保存融资方案
            scheme = requestService.saveOfferScheme(scheme);
            
            //如果是 微信端的申请 并为 票据融资 则需要发送消息
            ScfRequest request = requestService.selectByPrimaryKey(scheme.getRequestNo());
            if (BetterStringUtils.equals("2", request.getRequestFrom()) && BetterStringUtils.equals(RequestType.BILL.getCode(), request.getRequestType())) {
                List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(scheme.getRequestNo(), "0");
                if(false == Collections3.isEmpty(list)){
                    supplierPushService.pushSignInfo(Collections3.getFirst(list));
                }
            }
        }

        // 执行流程
        execFlow(anMap.get("requestNo").toString(), scheme.getApprovedBalance(), anApprovalResult, anReturnNode, anDescription);
        return AjaxObject.newOk("操作成功", scheme).toJson();
    }

    @Override
    public String webConfirmScheme(String anRequestNo, String anApprovalResult, String anSmsCode) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo + "-  anApprovalResult:" + anApprovalResult);
        
        //修改融资方案确认状态
        ScfRequestScheme scheme = requestService.saveConfirmScheme(anRequestNo, anApprovalResult, anSmsCode);
        
        // 执行流程
        execFlow(anRequestNo, scheme.getApprovedBalance(), anApprovalResult, "", "");
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webRequestTradingBackgrand(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription, String anSmsCode) {
        logger.debug("保理公司-发起贸易背景确认，入参：anRequestNo" + anRequestNo);
       
        // 保存发起状态
        ScfRequestScheme scheme =requestService.saveRequestTradingBackgrand(anRequestNo, anApprovalResult, anSmsCode);
        
        //如果是 微信端的申请 并为 票据融资 则需要发送消息
        ScfRequest request = requestService.selectByPrimaryKey(scheme.getRequestNo());
        if (BetterStringUtils.equals("2", request.getRequestFrom()) && BetterStringUtils.equals(RequestType.BILL.getCode(), request.getRequestType())) {
            List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(scheme.getRequestNo(), "1");
            if(false == Collections3.isEmpty(list)){
                supplierPushService.pushSignInfo(Collections3.getFirst(list));
            }
        }
        
        // 执行流程
        execFlow(anRequestNo, scheme.getApprovedBalance(), anApprovalResult, anReturnNode, anDescription);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anApprovalResult, String smsCode) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo + "-  anApprovalResult:" + anApprovalResult);
        
        //确认背景
        ScfRequestScheme scheme = requestService.confirmTradingBackgrand(anRequestNo, anApprovalResult, smsCode);
        
        // 执行流程
        execFlow(anRequestNo, scheme.getApprovedBalance(), anApprovalResult, "", "");
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmLoan(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-放款确认：" + anMap);

        ScfLoan loan = (ScfLoan) RuleServiceDubboFilterInvoker.getInputObj();
        ScfRequest request = new ScfRequest();
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            request = requestService.saveConfirmLoan(loan);
        }else{
            //此处要贺伟提供一个获取当前流程的上一步 no 的接口
            //flowService.webFindExecutedNodes(Long.parseLong(loan.getRequestNo()));
            
            //目前的业务：如果放款失败则会打回，现在的流程是如果打回就到了 核心企业放款确认，这样是不对的。
            anReturnNode = "140";
        }

        // 执行流程
        execFlow(loan.getRequestNo(), request.getBalance(), anApprovalResult, anReturnNode, anDescription);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webGetServiecFee(String anRequestNo, BigDecimal anLoanBalance) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        anMap.put("servicefeeBalance",anLoanBalance.multiply(request.getServicefeeRatio()).divide(new BigDecimal(1000)));
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
    public String webGetInsterest(String anRequestNo, BigDecimal anLoanBalance, String loanDate, String endDate) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        anMap.put("interestBalance", payPlanService.getFee(request.getFactorNo(), anLoanBalance, request.getApprovedRatio(), loanDate, endDate));
        anMap.put("managementBalance", payPlanService.getFee(request.getFactorNo(), anLoanBalance, request.getManagementRatio(), loanDate, endDate));
        anMap.put("serviceBalance", MathExtend.divide(MathExtend.multiply(anLoanBalance, request.getServicefeeRatio()), new BigDecimal(1000)));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }

    @Override
    public String webQueryWorkTask(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        String[] queryTerm = new String[] {"taskType","tradeStatus", "GTErequestDate", "LTErequestDate", "requestNo", "requestType"};
        anMap = Collections3.filterMap(anMap, queryTerm);
        Map<String, Object> qyRequestMap = new HashMap<>();
        
        // 查询当前用户任务列表
        Page<FlowStatus> page = new Page<FlowStatus>(anPageNum, anPageSize, 1 == anFlag);
        if (BetterStringUtils.equals("1", anMap.get("taskType").toString())) {
            // 待办
            page = flowService.queryCurrentUserWorkTask(null, null);
            qyRequestMap.put("LTtradeStatus", "160");
        }
        else
        {
            // 已办
            page = flowService.queryCurrentUserHistoryWorkTask(null, null);
        }

        // 无数据
        if (0 == page.size()) {
            return AjaxObject.newOkWithPage("分页查询用户任务列表，无数据", new Page<ScfRequest>()).toJson();
        }

        // 获取任务中的requestNo
        List<Long> requestsNo = new ArrayList<Long>();
        for (FlowStatus flowStatus : page) {
            requestsNo.add(flowStatus.getBusinessId());
        }
        
        List<Long> queryRequestsNo = new ArrayList<Long>();
        //如果输入中的值有requestNo 且 在任务列表中 则以输入的为准
        if((null != anMap.get("requestNo") && BetterStringUtils.isNotBlank(anMap.get("requestNo").toString()))){
            Long inputRequestNo = Long.parseLong(anMap.get("requestNo").toString());
            for (Long no : requestsNo) {
                if(null!=no && no.equals(inputRequestNo)){
                    queryRequestsNo.add(inputRequestNo);
                    break;
                }
            }
        }else{
            queryRequestsNo.addAll(requestsNo);
        }
        
        if(0 == queryRequestsNo.size()){
            return AjaxObject.newOkWithPage("查询成功", new Page(anPageNum, anPageSize, 1==anFlag)).toJson();
        }
        
        anMap.remove("taskType");
        anMap.remove("requestNo");
        qyRequestMap.putAll(anMap);
        qyRequestMap.put("requestNo", queryRequestsNo); 
        return AjaxObject.newOkWithPage("查询成功", requestService.queryRequestList(qyRequestMap, anFlag, anPageNum, anPageSize)).toJson();
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
    private void execFlow(String anRequestNo, BigDecimal money, String anApprovalResult, String anReturnNode, String anDescription) {
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(anRequestNo));
        input.setMoney(money);
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setReason(anDescription);

        // 当前融资申请单
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        if (BetterStringUtils.equals(anApprovalResult, APPROVALRESULT_0)) {
            // 下一步
            input.setCommand(FlowCommand.GoNext);
        }
        else if (BetterStringUtils.equals(anApprovalResult, "1")) {
            // 打回
            input.setRollbackNodeId(anReturnNode);
            input.setCommand(FlowCommand.Rollback);
            
            //打回到此区间内需要修改融资方案的确认值
            int returnNode = Integer.parseInt(anReturnNode);
            if(returnNode>120 && returnNode <= 140){
                updateScheme(anRequestNo, anApprovalResult, anReturnNode);
            }
        }
        else {
            // 拒绝
            input.setCommand(FlowCommand.Exit);

            // 解除订单关联
            orderService.unForzenInfoes(anRequestNo, null);
            
            //改为可用状态
            if(null != request.getOfferId()){
                offerService.saveUpdateTradeStatus(request.getOfferId(), "1");
            }
            
            request.setTradeStatus(RequestTradeStatus.CLOSED.getCode());
            
            // 当融资流程终止时,对冻结的授信额度进行解冻 add by Liusq 2016-10-20
            ScfCreditInfo anCreditInfo = new ScfCreditInfo();
            anCreditInfo.setBusinFlag(request.getRequestType());
            anCreditInfo.setBalance(request.getBalance());
            anCreditInfo.setBusinId(Long.valueOf(request.getRequestNo()));
            anCreditInfo.setCoreCustNo(request.getCoreCustNo());
            anCreditInfo.setCustNo(request.getCustNo());
            anCreditInfo.setFactorNo(request.getFactorNo());
            anCreditInfo.setCreditMode(request.getCreditMode());
            anCreditInfo.setRequestNo(request.getRequestNo());
            anCreditInfo.setDescription(request.getDescription());
            creditDetailService.saveUnfreezeCredit(anCreditInfo);
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
                request.setTradeStatus(RequestTradeStatus.CLOSED.getCode());
            }
            
        }
        else {
            // 流程-结束
            request.setFlowStatus("3");
        }

        try {
            // 修改-当前融资申请单（融资审批状态，流程状态）
            request = requestService.saveModifyRequest(request, request.getRequestNo());
            
            // 如果是微信发起的流程---发送微信提醒
            if(BetterStringUtils.equals("2", request.getRequestFrom())){
                supplierPushService.pushOrderInfo(request);
            }
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

    @Override
    public String webQuerySupplierRequestByCore(Map<String, Object> anMap, String anBusinStatus, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询融资成功",
                requestService.querySupplierRequestByCore(anQueryConditionMap, anBusinStatus, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQuerySellerRequestByCore(Map<String, Object> anMap, String anBusinStatus, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询融资成功",
                requestService.querySellerRequestByCore(anQueryConditionMap, anBusinStatus, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    public List<Long> findVoucherBatchNo(String anRequest) {
        // TODO Auto-generated method stub
        return this.requestService.findVoucherBatchNo(anRequest);
    }

    @Override
    public void updateAndSendRequestStatus(String anRequestNo, String anStatus) {
        // TODO Auto-generated method stub
        this.requestService.updateAndSendRequestStatus(anRequestNo, anStatus);

    }
    
    private void updateScheme(String anRequestNo, String anApprovalResult, String anReturnNode) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail(anRequestNo);
        
        int returnNode = Integer.parseInt(anReturnNode);
        if(returnNode>120 && returnNode <= 130){
            scheme.setCoreCustAduit("-1");
        }
        else if(returnNode>130 && returnNode <= 140){
            scheme.setCoreCustAduit("0");
        }
        schemeService.saveModifyScheme(scheme);
    }

}
