package com.betterjr.modules.loan.dubbo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRepaymentService;
import com.betterjr.modules.loan.entity.ScfExempt;
import com.betterjr.modules.loan.entity.ScfExtension;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.entity.ScfPressMoney;
import com.betterjr.modules.loan.service.ScfExemptService;
import com.betterjr.modules.loan.service.ScfExtensionService;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.loan.service.ScfPayRecordDetailService;
import com.betterjr.modules.loan.service.ScfPayRecordService;
import com.betterjr.modules.loan.service.ScfPressMoneyService;
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
    @Autowired
    private ScfExemptService exemptService;
    @Autowired
    private ScfPressMoneyService pressMoneyService;
    @Autowired
    private ScfExtensionService extensionService;
    
    @Override
    public String webSaveRepayment(Map<String, Object> anMap) {
        logger.debug("保存还款，入参：" + anMap);
        ScfPayRecord record = (ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("操作成功", payPlanService.saveRepayment(record)).toJson();
    }
    
    @Override
    public String webQueryRepaymentFee(String anRequestNo, String anPayType, String anFactorNo, String anPayDate, BigDecimal anTotalBalance) {
        ScfPayRecord record = payPlanService.queryRepaymentFee(anRequestNo, anPayType, anFactorNo, anPayDate, anTotalBalance);
        return AjaxObject.newOk("操作成功", record).toJson();
    }
    
    @Override
    public String webQueryDistributeFee(String anRequestNo, String anPayType, String anPayDate, BigDecimal anTotalBalance) {
        return AjaxObject.newOk("操作成功", payPlanService.queryDistributeFee(anRequestNo,anPayType, anPayDate, anTotalBalance)).toJson();
    }
    
    @Override
    public String webQuerySellerRepaymentFee(String anRequestNo, String anPayType, String anPayDate, BigDecimal anTotalBalance) {
        return AjaxObject.newOk("操作成功", payPlanService.querySellerRepaymentFee(anRequestNo,anPayType, anPayDate, anTotalBalance)).toJson();
    }
    
    @Override
    public String webCalculatPayType(String anRequestNo, String anPayDate) {
        logger.debug("计算还款方式，入参：" + anPayDate);
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
    public String webFindPayPlanDetail(String anRequestNo) {
        logger.debug("查询还款详情，入参：" + anRequestNo);
        List<ScfPayPlan> list = payPlanService.selectByClassProperty(ScfPayPlan.class, "requestNo", anRequestNo);
        return AjaxObject.newOk(Collections3.getFirst(list)).toJson();
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
    
    @Override
    public String webQueryExemptList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询豁免列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("分页查询豁免列表成功", exemptService.queryExemptList(qyConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webAddExempt(Map<String, Object> anMap) {
        logger.debug("新增豁免，入参：" + anMap);
        ScfExempt anExempt = (ScfExempt) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("新增催收成功", exemptService.addExempt(anExempt)).toJson();
    }
    
    @Override
    public String webQueryPressMoneyList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询催收列表，入参：" + anMap);
        String[] queryTerm = new String[] {"requestNo","factorNo"};
        anMap = Collections3.filterMap(anMap, queryTerm);
        return AjaxObject.newOkWithPage("分页查询催收列表成功", pressMoneyService.queryPressMoneyList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webAddPressMoney(Map<String, Object> anMap) {
        logger.debug("新增催收，入参：" + anMap);
        ScfPressMoney anPress = (ScfPressMoney) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("新增催收成功", pressMoneyService.addPressMoney(anPress)).toJson();
    }
    
    @Override
    public String webSaveModifyPressMoney(Map<String, Object> anMap, Long id) {
        logger.debug("编辑催收，入参：" + anMap);
        ScfPressMoney anPress = (ScfPressMoney) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("编辑催收成功", pressMoneyService.saveModifyPressMoney(anPress, id)).toJson();
    }
    
    @Override
    public String webSaveDelPressMoney(Map<String, Object> anMap, Long id) {
        logger.debug("删除催收，入参：" + anMap);
        ScfPressMoney anPress = (ScfPressMoney) RuleServiceDubboFilterInvoker.getInputObj();
        if(1 == pressMoneyService.delete(anPress)){
            return AjaxObject.newOk("操作成功").toJson();
        }
        return AjaxObject.newOk("操作失败").toJson();
    }

    @Override
    public String webCalculatExtensionEndDate(String anStartDate, Integer anPeriod, Integer anPeriodUnit) {
        logger.debug("计算展期结束日期，入参：" + anStartDate);
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("endDate", payPlanService.getEndDate(anStartDate, anPeriod, anPeriodUnit));
        return AjaxObject.newOk("操作成功", anMap).toJson();
    }
    
    @Override
    public String webCalculatExtensionFee(BigDecimal anRatio, BigDecimal anManagementRatio, BigDecimal anExtensionBalance) {
        logger.debug("计算展期费用，入参：" + anExtensionBalance);
        return AjaxObject.newOk("操作成功", extensionService.getInterest(anRatio, anManagementRatio, anExtensionBalance)).toJson();
    }
    
    @Override
    public String webPayAssigned(String anRequestNo, String anStartDate, BigDecimal anPayBalance) {
        logger.debug("计算还款分配，入参：" + anRequestNo);
        return AjaxObject.newOk("操作成功",  extensionService.payAssigned(anRequestNo, anStartDate, anPayBalance)).toJson();
    }

    @Override
    public String webCalculatLoanBalance(String anRequestNo, String anStartDate) {
        logger.debug("计算放款金额，入参：" + anRequestNo);
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("loanBalance", extensionService.getLoanBalance(anRequestNo, anStartDate));
        return AjaxObject.newOk("操作成功", map).toJson();
    }
    
    @Override
    public String webAddExtension(Map<String, Object> anMap) {
        logger.debug("新增展期记录，入参：" + anMap);
        ScfExtension anExtension = (ScfExtension) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("操作成功", extensionService.addExtension(anExtension)).toJson();
    }
    
    @Override
    public String webQueryExtensionList(Map<String, Object> anMap, String requestNo, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查展期列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", requestNo);
        return AjaxObject.newOkWithPage("分页查询催收列表成功", extensionService.queryExtensionList(qyConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindExtensionList(Map<String, Object> anMap) {
        logger.debug("无分页查展期列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", anMap.get("requestNo").toString());
        return AjaxObject.newOk("无分页查询展期列表成功", extensionService.queryExtensionList(anMap)).toJson();
    }
    
    @Override
    public String webFindExemptList(Map<String, Object> anMap) {
        logger.debug("无分页查询豁免列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", anMap.get("requestNo").toString());
        return AjaxObject.newOk("分页查询豁免列表成功", exemptService.findExemptList(anMap)).toJson();
    }
    
    @Override
    public String webFindPressMoneyList(Map<String, Object> anMap) {
        logger.debug("无分页查催收免列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", anMap.get("requestNo").toString());
        return AjaxObject.newOk("分页查询催收列表成功", pressMoneyService.findPresMoneyist(anMap)).toJson();
    }
    
    @Override
    public String webFindPlanList(Map<String, Object> anMap) {
        logger.debug("无分页查询还款计划列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", anMap.get("requestNo").toString());
        return AjaxObject.newOk("无分页查还款计划列表成功", payPlanService.findPlanList(anMap)).toJson();
    }
    
    @Override
    public String webFindPayRecordList(Map<String, Object> anMap) {
        logger.debug("无分页查询还款记录列表，入参：" + anMap);
        Map<String, Object> qyConditionMap = new HashMap<String, Object>();
        qyConditionMap.put("requestNo", anMap.get("requestNo").toString());
        return AjaxObject.newOk("无分页查还款记录列表成功", payRecordService.findPayRecordList(anMap)).toJson();
    }
    
    @Override
    public String webNotifyPay(Map<String, Object> anMap, String requestNo) {
        logger.debug("还款提醒发送消息发送，入参：" + anMap);
        if(payPlanService.notifyPay(requestNo)){
            return AjaxObject.newOk("还款提醒发送成功！").toJson();
        }
        return AjaxObject.newError("还款提醒发送失败！").toJson();
    }
    

}
