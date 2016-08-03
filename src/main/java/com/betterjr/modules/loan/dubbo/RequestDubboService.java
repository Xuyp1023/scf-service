package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestApproved;
import com.betterjr.modules.loan.service.ScfRequestApprovedService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRequestService.class)
public class RequestDubboService implements IScfRequestService {
    protected final Logger logger = LoggerFactory.getLogger(RequestDubboService.class);

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfRequestApprovedService approvedService;

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
    public String webApproveRequest(Map<String, Object> anMap) {
        logger.debug("融资审批，入参：" + anMap);
        return AjaxObject.newOk(requestService.approveRequest((Map) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }
    
    @Override
    public String webOfferScheme(Map<String, Object> anMap) {
        logger.debug("出具融资方案，入参：" + anMap);
        return AjaxObject.newOk(requestService.offerScheme((ScfRequestApproved) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }
    
    
    @Override
    public String webFindApprovedDetail(String anRequestNo) {
        logger.debug("获取融资审批详情，入参：anRequestNo" + anRequestNo);
        return AjaxObject.newOk(approvedService.findApprovedDetail(anRequestNo)).toJson();
    }
    
    @Override
    public String webSaveModifyApproved(Map<String, Object> anMap) {
        logger.debug("修改融资审批详情，入参：" + anMap);
        return AjaxObject.newOk(approvedService.saveModifyApproved((ScfRequestApproved)RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }
    
    @Override
    public String webQueryApprovedList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询融资审批，入参：anRequestNo：" + anMap);
        return AjaxObject.newOkWithPage("查询成功", approvedService.queryApprovedList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webConfirmScheme(String anRequestNo, String anBusinStatus) {
        logger.debug("申请企业-确认融资方案，入参：anRequestNo" + anRequestNo +"  anAduit:"+ anBusinStatus);
        return AjaxObject.newOk(requestService.confirmScheme(anRequestNo, anBusinStatus)).toJson();
    }
    
    @Override
    public String webRequestTradingBackgrand(String anRequestNo) {
        logger.debug("资方-发起贸易背景确认，入参：anRequestNo" + anRequestNo);
        return AjaxObject.newOk(requestService.webRequestTradingBackgrand(anRequestNo)).toJson();
    }
    
    @Override
    public String webConfirmTradingBackgrand(String anRequestNo, String anBusinStatus) {
        logger.debug("核心企业-确认贸易背景，入参：anRequestNo" + anRequestNo +"  anAduit:"+ anBusinStatus);
        return AjaxObject.newOk(requestService.confirmTradingBackgrand(anRequestNo, anBusinStatus)).toJson();
    }
    

}
