package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfPayService;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfPayRecordDetailService;
import com.betterjr.modules.loan.service.ScfPayRecordService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfPayService.class)
public class PayDubboService implements IScfPayService {
    protected final Logger logger = LoggerFactory.getLogger(PayDubboService.class);

    @Autowired
    private ScfPayPlanService payPlanService;
    
    @Autowired
    private ScfPayRecordService payRecordService;
    
    @Autowired
    private ScfPayRecordDetailService payRecordDetailService;

    @Override
    public String webQueryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询还款列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("还款查询成功",
                payPlanService.queryPayPlanList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyPayPlan(Map<String, Object> anMap, Long anId) {
        logger.debug("修改还款，入参：" + anMap);
        return AjaxObject.newOk(payPlanService.saveModifyPayPlan((ScfPayPlan) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webFindPayPlanDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("查询还款详情，入参：" + anMap);
        return AjaxObject.newOk(payPlanService.findPayPlanDetail(anId)).toJson();
    }
    
    @Override
    public String webQueryPayRecordList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询还款列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("还款查询成功",
                payRecordService.queryPayRecordList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyPayRecord(Map<String, Object> anMap, Long anId) {
        logger.debug("修改还款，入参：" + anMap);
        return AjaxObject.newOk(payRecordService.saveModifyPayRecord((ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webFindPayRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("查询还款详情，入参：" + anMap);
        return AjaxObject.newOk(payRecordService.findPayRecordDetail(anId)).toJson();
    }
    
    @Override
    public String webQueryRecordDetailList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询还款列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("还款查询成功",
                payRecordDetailService.queryRecordDetailList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("修改还款，入参：" + anMap);
        return AjaxObject.newOk(payRecordDetailService.saveModifyRecordDetail((ScfPayRecordDetail) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webFindRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("查询还款详情，入参：" + anMap);
        return AjaxObject.newOk(payRecordDetailService.findRecordDetail(anId)).toJson();
    }

}
