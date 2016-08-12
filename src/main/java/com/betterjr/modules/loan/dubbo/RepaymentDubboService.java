package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRepaymentService;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfPayRecordDetailService;
import com.betterjr.modules.loan.service.ScfPayRecordService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRepaymentService.class)
public class RepaymentDubboService implements IScfRepaymentService {
    protected final Logger logger = LoggerFactory.getLogger(RepaymentDubboService.class);

    @Autowired
    private ScfPayPlanService payPlanService;
    
    
    @Autowired
    private ScfPayRecordService payRecordService;
    
    @Autowired
    private ScfPayRecordDetailService payRecordDetailService;

    
    @Override
    public String webSaveRepayment(Map<String, Object> anMap) {
        ScfPayRecord record = (ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("操作成功", payPlanService.saveRepayment(record)).toJson();
    }
    
    @Override
    public String webQueryRepaymentFee(String anRequestNo, String anPayType, String anFactorNo) {
        ScfPayRecord record = payPlanService.queryRepaymentFee(anRequestNo, anPayType, anFactorNo);
        return AjaxObject.newOk("操作成功", record).toJson();
    }
    
    @Override
    public String webQuerySellerRepaymentFee(Map<String, Object> anMap) {
        ScfPayRecord record = (ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("操作成功", payPlanService.querySellerRepaymentFee(record)).toJson();
    }
    
    @Override
    public String webCalculatPayType(String anRequestNo, String anPayDate) {
        return AjaxObject.newOk("操作成功", payPlanService.calculatPayType(anRequestNo, anPayDate)).toJson();
    }

    @Override
    public String webQueryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询还款列表，入参：" + anMap);
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
        logger.debug("分页查询还款记录列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("查询成功",
                payRecordService.queryPayRecordList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyPayRecord(Map<String, Object> anMap, Long anId) {
        logger.debug("修改还款记录，入参：" + anMap);
        return AjaxObject.newOk(payRecordService.saveModifyPayRecord((ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webFindPayRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("查询还款记录详情，入参：" + anMap);
        return AjaxObject.newOk(payRecordService.findPayRecordDetail(anId)).toJson();
    }
    
    @Override
    public String webQueryRecordDetailList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询还款列表记录详情列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("查询成功",
                payRecordDetailService.queryRecordDetailList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("修改还款记录详情，入参：" + anMap);
        return AjaxObject.newOk(payRecordDetailService.saveModifyRecordDetail((ScfPayRecordDetail) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webFindRecordDetail(Map<String, Object> anMap, Long anId) {
        logger.debug("查询还款记录详情，入参：" + anMap);
        return AjaxObject.newOk(payRecordDetailService.findRecordDetail(anId)).toJson();
    }

}
