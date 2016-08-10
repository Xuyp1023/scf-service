package com.betterjr.modules.loan.dubbo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
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
        //TODO 设置状态 等贺伟的接口  request.setTradeStatus(tradeStatus); 
        request = requestService.saveModifyRequest(request, request.getRequestNo());
        
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
    public String webApproveRequest(String anRequestNo, String anApprovalResult, String anReturnNode, String anDescription,String tradeStatus) {
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
            approvedService.addScheme(scheme);
        }
        
        //执行流程
        execFllow(scheme.getRequestNo(), scheme.getApprovedBalance(), anApprovalResult, anReturnNode, anDescription);
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmScheme(String anRequestNo, String anAduitStatus) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        
        ScfRequestScheme scheme = new ScfRequestScheme();
        if(BetterStringUtils.equals(anAduitStatus, "0") == true){
            scheme = requestService.saveConfirmScheme(anRequestNo, anAduitStatus);
        }
        
        //执行流程
        execFllow(anRequestNo, scheme.getApprovedBalance(), anAduitStatus, "", "");
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webRequestTradingBackgrand(String anRequestNo, String anAduitStatus, String anReturnNode, String anDescription) {
        logger.debug("资方-发起贸易背景确认，入参：anRequestNo" + anRequestNo);
        
        ScfRequest request= new ScfRequest();
        if(BetterStringUtils.equals(anAduitStatus, "0") == true){
            request = requestService.saveRequestTradingBackgrand(anRequestNo);
        }
        
        //执行流程
        execFllow(anRequestNo, request.getBalance(), anAduitStatus, "", "");
        
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        
        ScfRequest request = new ScfRequest();
        if(BetterStringUtils.equals(anAduitStatus, "0") == true){
            request = requestService.confirmTradingBackgrand(anRequestNo, anAduitStatus);
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
    
    public String queryWorkTask(String type, int anFlag, int anPageNum, int anPageSize){
        Page<FlowStatus> page = new Page<FlowStatus>(anPageNum, anPageSize, 1 == anFlag);
        if(BetterStringUtils.equals("1", type)){
            //待办
            page = flowService.queryCurrentUserWorkTask(page);
        }else{
            //已办
            page = flowService.queryCurrentUserHistoryWorkTask(page);
        }
        
        StringBuffer bufRequestNo = new StringBuffer();
        for (FlowStatus flowStatus : page) {
            bufRequestNo.append(",");
            //TODO 等贺伟 的接口 目前没有getbussId
            //requestNos.append(flowStatus.getbussId);
        }
        
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("requestNo", bufRequestNo.toString().replaceFirst(",", ""));
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
        //TODO 设置状态 等贺伟的接口  request.setTradeStatus(tradeStatus); 
        requestService.saveModifyRequest(request, anRequestNo);
    }
}
