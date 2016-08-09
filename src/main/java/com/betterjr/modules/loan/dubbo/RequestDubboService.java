package com.betterjr.modules.loan.dubbo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRequestService.class)
public class RequestDubboService implements IScfRequestService {
    protected final Logger logger = LoggerFactory.getLogger(RequestDubboService.class);

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfRequestSchemeService approvedService;
    @Autowired
    private ScfPayPlanService payPlanService;

    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("新增融资申请，入参：" + anMap);
        return AjaxObject.newOk(requestService.addRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
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
        
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        request.setLastStatus(tradeStatus);
        if (BetterStringUtils.equals(anApprovalResult, "0") == true) {
            // TODO 调用流程 下一步
        }
        else if (BetterStringUtils.equals(anApprovalResult, "1") == true) {
            // 打回
        }
        else {
            // 拒绝
        }
        requestService.saveModifyRequest(request, anRequestNo);
        return "";
    }

    @Override
    public String webOfferScheme(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("出具融资方案，入参：approvalResult：" + anApprovalResult + "-returnNode:" + anReturnNode + "-description:" + anDescription);
        logger.debug("出具融资方案，入参：" + anMap);
        ScfRequestScheme scheme = new ScfRequestScheme();
        if (BetterStringUtils.equals(anApprovalResult, "0") == true) {
            // 下一步
            scheme = requestService.saveOfferScheme((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj());
        }
        else if (BetterStringUtils.equals(anApprovalResult, "1") == true) {
            // TODO 调用流程 打回
        }
        else {
            // 拒绝
        }
        return AjaxObject.newOk(scheme).toJson();
    }

    @Override
    public String webConfirmScheme(String anRequestNo, String anAduitStatus) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        requestService.saveConfirmScheme(anRequestNo, anAduitStatus);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webRequestTradingBackgrand(String anRequestNo) {
        logger.debug("资方-发起贸易背景确认，入参：anRequestNo" + anRequestNo);
        requestService.saveRequestTradingBackgrand(anRequestNo);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo + "-  anAduitStatus:" + anAduitStatus);
        requestService.confirmTradingBackgrand(anRequestNo, anAduitStatus);
        return AjaxObject.newOk("操作成功").toJson();
    }

    @Override
    public String webConfirmLoan(Map<String, Object> anMap, String anApprovalResult, String anReturnNode, String anDescription) {
        logger.debug("保理公司-放款确认：approvalResult：" + anApprovalResult + "-returnNode:" + anReturnNode + "-description:" + anDescription);
        logger.debug("保理公司-放款确认，入参：anMap：" + anMap);
        if (BetterStringUtils.equals(anApprovalResult, "0") == true) {
            // 下一步
            requestService.saveConfirmLoan((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
        }
        else if (BetterStringUtils.equals(anApprovalResult, "1") == true) {
            // TODO 调用流程 打回
        }
        else {
            // 拒绝
        }
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
    

}
