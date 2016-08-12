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
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.agreement.IScfElecAgreementService;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.workflow.IFlowService;
import com.betterjr.modules.workflow.data.FlowCommand;
import com.betterjr.modules.workflow.data.FlowInput;
import com.betterjr.modules.workflow.data.FlowStatus;
import com.betterjr.modules.workflow.data.FlowType;

@Service(interfaceClass = IScfRequestService.class)
public class RequestDubboService implements IScfRequestService {
    protected final Logger logger = LoggerFactory.getLogger(RequestDubboService.class);

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfRequestSchemeService approvedService;
    @Autowired
    private ScfPayPlanService payPlanService;
    
    @Reference(interfaceClass=IFlowService.class )
    private IFlowService flowService;
    
    @Reference(interfaceClass=IScfElecAgreementService.class )
    private IScfElecAgreementService agreementService;

    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("新增融资申请，入参：" + anMap);
        ScfRequest request = requestService.addRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj());
        
        //启动流程
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(request.getRequestNo()));
        input.setMoney(request.getBalance());
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setType(FlowType.Trade);
        flowService.start(input );
        
        //修改融资状态
        FlowStatus search = new FlowStatus();
        search.setBusinessId(Long.parseLong(request.getRequestNo()));
        Page<FlowStatus> list = flowService.queryCurrentUserWorkTask(null, search);
        if(!Collections3.isEmpty(list)){
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
        return AjaxObject.newOkWithPage("融资申请查询成功",
                requestService.queryRequestList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
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
        return AjaxObject.newOk(approvedService.saveModifyScheme((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }

    @Override
    public String webQuerySchemeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资审批，入参：anRequestNo：" + anMap);
        return AjaxObject.newOkWithPage("查询成功",
                approvedService.querySchemeList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webApproveRequest(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("一般审批，入参approvalResult：" + anApprovalResult + "-returnNode:" + anReturnNode + "-description:" + anDescription);
        
        //执行流程
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        execFllow(anRequestNo, request.getBalance(), anApprovalResult, anReturnNode, anDescription);
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webOfferScheme(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("出具融资方案，入参：" + anMap);
        
        ScfRequestScheme scheme = new ScfRequestScheme();
        if(BetterStringUtils.equals(anApprovalResult, "0") == true){
            scheme = (ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj();
            scheme = approvedService.addScheme(scheme);
        }
        
        //执行流程
        execFllow(anMap.get("requestNo").toString(), scheme.getApprovedBalance(), anApprovalResult, anReturnNode, anDescription);
        
        return AjaxObject.newOk("操作成功", scheme).toJson();
    }

    @Override
    public String webConfirmScheme(String anRequestNo, String anAduitStatus) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        
        ScfRequestScheme scheme = new ScfRequestScheme();
        if(BetterStringUtils.equals(anAduitStatus, "0") == true){
            scheme = requestService.saveConfirmScheme(anRequestNo, anAduitStatus);
            
            //修改申请表中的信息
            ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
            request.setBalance(scheme.getApprovedBalance());
            request.setRatio(scheme.getApprovedRatio());
            request.setManagementRatio(scheme.getApprovedManagementRatio());
            requestService.saveModifyRequest(request, anRequestNo);
        }
        
        //执行流程
        execFllow(anRequestNo, scheme.getApprovedBalance(), anAduitStatus, "", "");
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webRequestTradingBackgrand(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("资方-发起贸易背景确认，入参：anRequestNo" + anRequestNo);
        
        ScfRequest request= new ScfRequest();
        if(BetterStringUtils.equals( "0", anApprovalResult) == true){
            //保存发起状态
            requestService.saveRequestTradingBackgrand(anRequestNo);
            request = requestService.findRequestDetail(anRequestNo);
            
            //1,订单，2:票据;3:应收款;4:经销商
            if(BetterStringUtils.equals("4", request.getRequestType()) == true){
                //三方协议
                
            }else{
                //发送转让通知书
                Map<String, Object> anMap =new HashMap<String, Object>();
                anMap.put("requestNo", anRequestNo);
                //TODO 合同名称、 银行账号， 保理公司详细地址
                String noticeNo = BetterDateUtils.getDate("yyyyMMdd")+anRequestNo;
                anMap.put("noticeNo", noticeNo);
                anMap.put("buyer", request.getFactorName());
                anMap.put("factorRequestNo", noticeNo);
                anMap.put("agreeName", request.getCustName() + "应收账款转让申请书");
                anMap.put("bankAccount", "6229887842567285");
                anMap.put("factorAddr", "保理公司详细地址");
                anMap.put("factorPost", "518100");
                anMap.put("factorLinkMan", "保理公司联系人姓名");
                if(agreementService.webTransNotice(anMap) == false){
                    logger.debug("转让通知书,发送失败！");
                    //TODO 失败了要怎么弄
                }
            }
            
        }
        
        //执行流程
        execFllow(anRequestNo, request.getBalance(), anApprovalResult, anReturnNode,  anDescription);
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        
        ScfRequest request = new ScfRequest();
        if(BetterStringUtils.equals(anAduitStatus, "0") == true){
            //保存确认状态
            request = requestService.confirmTradingBackgrand(anRequestNo, anAduitStatus);
            
            //1,订单，2:票据;3:应收款;4:经销商
            if(BetterStringUtils.equals( "4", request.getRequestType()) == true){
                //三方协议
                
            }else{
                //转让意见确认书
                Map<String, Object> anMap =new HashMap<String, Object>();
                String noticeNo = BetterDateUtils.getDate("yyyyMMdd")+anRequestNo;
                anMap.put("requestNo", anRequestNo);
                anMap.put("factorRequestNo", noticeNo);
                anMap.put("confirmNo", noticeNo);
                anMap.put("supplier", request.getCustName());
                
                if(agreementService.webTransOpinion(anMap) == false){
                    logger.debug("转让意见确认书,发送失败");
                    //TODO 失败了要怎么弄
                }
            }
        }
        
        //执行流程
        execFllow(anRequestNo, request.getBalance(), anAduitStatus, "", "");
       
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmLoan(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-放款确认：" + anMap);
        
        ScfRequest request = new ScfRequest();
        if (BetterStringUtils.equals(anApprovalResult, "0") == true) {
            request = requestService.saveConfirmLoan((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
        }
        
        //执行流程
        execFllow(request.getRequestNo(), request.getBalance(), anApprovalResult, anReturnNode, anDescription);
        
        return AjaxObject.newOk("操作成功").toJson();
    }
    
    @Override
    public String webCalculatServiecFee(String anRequestNo, BigDecimal anLoanBalance) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("servicefeeBalance", payPlanService.calculatFee(anRequestNo, anLoanBalance, 2));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }
    
    @Override
    public String webCalculatEndDate(String anRequestNo, String anStartDate) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("endDate", payPlanService.calculatEndDate(anRequestNo, anStartDate));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }
    
    @Override
    public String webCalculatInsterest(String anRequestNo, BigDecimal anLoanBalance) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("insterestBalance", payPlanService.calculatFee(anRequestNo, anLoanBalance, 3));
        anMap.put("managementBalance", payPlanService.calculatFee(anRequestNo, anLoanBalance, 1));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }
    
    @Override
    public String webQueryWorkTask(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize){
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        
        //查询当前用户任务列表
        Page<FlowStatus> page = new Page<FlowStatus>(anPageNum, anPageSize, 1 == anFlag);
        if(BetterStringUtils.equals("1", anMap.get("taskType").toString())){
            //待办
            page = flowService.queryCurrentUserWorkTask(null, null);
        }else{
            //已办
            page = flowService.queryCurrentUserHistoryWorkTask(null, null);
        }
        
        //获取任务中的requestNo
        List<Long> requestNos = new ArrayList<Long>();
        for (FlowStatus flowStatus : page) {
            requestNos.add(flowStatus.getBusinessId());
        }
        
        anMap.remove("taskType");
        return  AjaxObject.newOkWithPage("查询成功", requestService.queryRequestList(anMap , anFlag, anPageNum, anPageSize)).toJson();
    }
    
    /**
     * 执行流程
     * @param anRequestNo
     * @param money
     * @param anApprovalResult
     * @param anReturnNode
     * @param anDescription
     */
    private void execFllow(String anRequestNo,BigDecimal money, String anApprovalResult, String anReturnNode, String anDescription) {
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(anRequestNo));
        input.setMoney(money);
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setReason(anDescription);
        
        if (BetterStringUtils.equals(anApprovalResult, "0") == true) {
            // 下一步
            input.setCommand(FlowCommand.GoNext);
        } 
        else if (BetterStringUtils.equals(anApprovalResult, "1") == true) {
            //打回
            input.setRollbackNodeId(anReturnNode);
            input.setCommand(FlowCommand.Rollback);
        }
        else {
            // 拒绝
            input.setCommand(FlowCommand.Exit);
        }
       
        flowService.exec(input);

        //修改融资状态
        ScfRequest request= requestService.selectByPrimaryKey(anRequestNo);
        FlowStatus search = new FlowStatus();
        search.setBusinessId(Long.parseLong(request.getRequestNo()));
        Page<FlowStatus> list = flowService.queryCurrentUserWorkTask(null, search);
        if(!Collections3.isEmpty(list)){
            request.setTradeStatus(Collections3.getFirst(list).getCurrentNodeId().toString()); 
            request = requestService.saveModifyRequest(request, request.getRequestNo());
        }
    }
    
    @Override
    public String webQueryPendingRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询待批融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(requestService.queryPendingRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryRepaymentRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询还款融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(requestService.queryRepaymentRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryCompletedRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询历史融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(requestService.queryCompletedRequest(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryCoreEnterpriseRequest(Map<String, Object> anMap, String anRequestType, String anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页核心企业查询融资，入参：" + anMap);
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(requestService.queryCoreEnterpriseRequest(anQueryConditionMap, anRequestType, anFlag, anPageNum, anPageSize)).toJson();
    }
}
