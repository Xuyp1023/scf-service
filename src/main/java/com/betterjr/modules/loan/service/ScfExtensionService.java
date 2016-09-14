package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfExtensionMapper;
import com.betterjr.modules.loan.entity.ScfExtension;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.PayPlanBusinStatus;
import com.betterjr.modules.loan.helper.PayRecordPayType;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.param.entity.FactorParam;

@Service
public class ScfExtensionService extends BaseService<ScfExtensionMapper, ScfExtension> {
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private ScfPayRecordService payRecordService;
    
    /**
     * 计算到某天的 剩余总金额  如果展期开始日大于今天则  总额=（今天的总额  + 明天至展期开始日的 前日的罚息金额）
     * @param anRequestNo
     * @param anStartDate
     * @return
     */
    public BigDecimal getLoanBalance(String anRequestNo, String anStartDate){
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("requestNo", anRequestNo);
        ScfPayPlan payPlan = payPlanService.findPayPlanByRequest(anRequestNo);
        
        //计算明天 到 展期开始前一日 的罚息金额
        String tomorrowDate = BetterDateUtils.addStrDays(BetterDateUtils.getDate(), 1);
        if(payPlanService.getOverDueDays(tomorrowDate, anStartDate) < 0){
            return payPlan.getShouldTotalBalance();
        }
        
        //总额
        Map<String, BigDecimal> feeMap = payPlanService.getOverDueFee(tomorrowDate, anStartDate, payPlan.getShouldPrincipalBalance(), payPlan.getFactorNo().toString());
        return payPlan.getSurplusTotalBalance().add(feeMap.get("latefeeBalance")).add(feeMap.get("penaltyBalance"));
    }
    
    
    /**
     * 还款分配（顺序是 滞纳金-》罚息-》管理费-》利息-》本金）
     * @param anRequestNo
     * @param anStartDate
     * @param payBalance
     * @return
     */
    public Map<String, Object> payAssigned(String anRequestNo, String anStartDate, BigDecimal payBalance){
        ScfPayPlan payPlan = payPlanService.findPayPlanByRequest(anRequestNo);
        String tomorrowDate = BetterDateUtils.addStrDays(BetterDateUtils.getDate(), 1);
        
        Map<String, Object> payMap = new HashMap<String, Object>();
        payMap.put("payPenaltyBalance", 0);
        payMap.put("payManagementBalance", 0);
        payMap.put("payInterestBalance", 0);
        payMap.put("payPrincipalBalance", 0);
        
        //
        String custNo = payPlan.getFactorNo().toString();
        FactorParam param = DictUtils.loadObject("FactorParam", custNo, FactorParam.class);
        
        //有逾期且超过宽限限的情况下要计算逾期（如今天是10号系统定时已算了逾期费用，那如果用户选择的是 15号开始展期 那么就要计算11号到14号这4天的逾期费用）
        int overDueDays = payPlanService.getOverDueDays(tomorrowDate, anStartDate);
        if(overDueDays > 0 && overDueDays > param.getGraceDays()){
            Map<String, BigDecimal> feeMap = payPlanService.getOverDueFee(tomorrowDate, anStartDate, payPlan.getSurplusPrincipalBalance(), custNo);
            
            //分配滞纳金（如果还款金额小于滞纳金，则本次还款滞纳金则为本次的还款总额，将不再往下走。下面的程序也是这种判断）
            BigDecimal lateFee = payPlan.getSurplusLatefeeBalance().add(feeMap.get("latefeeBalance"));
            if(payBalance.compareTo(lateFee) == 1){
                payBalance = payBalance.subtract(lateFee);
                payMap.put("payLatefeeBalance", lateFee);
            }else{
                payMap.put("payLatefeeBalance", payBalance);
                return payMap;
            }
            
            //分配罚息
            BigDecimal penaltyBalance =  payPlan.getSurplusPenaltyBalance().add(feeMap.get("penaltyBalance"));
            if(payBalance.compareTo(penaltyBalance) == 1){
                payBalance = payBalance.subtract(penaltyBalance);
                payMap.put("payPenaltyBalance", penaltyBalance);
            }else{
                payMap.put("payPenaltyBalance", payBalance);
                return payMap;
            }
        }else{
            payMap.put("payLatefeeBalance", 0);
            payMap.put("payPenaltyBalance", 0);
        }
        
        //项目逾期，但是在宽限期内，宽限限内 按天利息计算，不用算罚息
        BigDecimal overManagementBalance = new BigDecimal(0);
        BigDecimal overInterestBalance = new BigDecimal(0);
        if(overDueDays > 0 && overDueDays < param.getGraceDays()){
            Map<String, BigDecimal> interestMap = payPlanService.getInterestByDays(anRequestNo, payPlan.getSurplusPrincipalBalance(), custNo, overDueDays);
            overManagementBalance = interestMap.get("managementBalance");
            overInterestBalance = interestMap.get("interestBalance");
        }
        
        //分配管理费
        if(payBalance.compareTo(payPlan.getSurplusManagementBalance()) == 1){
            payMap.put("payManagementBalance", payPlan.getSurplusManagementBalance().add(overManagementBalance));
            payBalance = payBalance.subtract(payPlan.getSurplusManagementBalance());
        }else{
            payMap.put("payManagementBalance", payBalance);
            return payMap;
        }
      
        //分配利息
        if(payBalance.compareTo(payPlan.getSurplusInterestBalance()) == 1){
            payMap.put("payInterestBalance", payPlan.getSurplusInterestBalance().add(overInterestBalance));
            payBalance = payBalance.subtract(payPlan.getSurplusInterestBalance());
        }else{
            payMap.put("payInterestBalance", payBalance);
            return payMap;
        }
        
        //分配本金
        if(payBalance.compareTo(payPlan.getSurplusPrincipalBalance()) == 1){
            payMap.put("payPrincipalBalance", payBalance.subtract(payPlan.getSurplusPrincipalBalance()));
        }else{
            payMap.put("payPrincipalBalance", payBalance);
        }
        
        return payMap;
    }
    
    /**
     * 计算利息
     * @param anRequestNo
     * @param anStartDate
     * @param payBalance
     * @return
     */
    public Map<String, Object> getInterest(BigDecimal ratio, BigDecimal managementRatio, BigDecimal extensionBalance){
        Map<String, Object> payMap = new HashMap<String, Object>();
        BigDecimal interestBalance = extensionBalance.multiply(ratio).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal managementBalance = extensionBalance.multiply(managementRatio).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        payMap.put("shouldInterestBalance", interestBalance);
        payMap.put("shouldManagementBalance", managementBalance);
        payMap.put("shouldTotalBalance", extensionBalance.add(interestBalance).add(managementBalance));
        return payMap;
    }
    
    /**
     * 新增展期记录
     * @param anExtension
     * @return
     */
    public ScfExtension addExtension(ScfExtension anExtension) {
        BTAssert.notNull(anExtension, "新增展期记录失败-anExtension不能为空");
        
        //获取申请单信息
        ScfRequest request = requestService.selectByPrimaryKey(anExtension.getRequestNo());
        
        //获取还款计划信息
        ScfPayPlan plan = payPlanService.findPayPlanByRequest(anExtension.getRequestNo());
       
        //添加还款记录
        ScfPayRecord record = new ScfPayRecord();
        record.setPayPlanId(plan.getId());
        record.setPayCustNo(anExtension.getCustNo());
        record.setPayDate(anExtension.getStartDate());
        record.setPayType(PayRecordPayType.EXTENSION.getCode());
        record.setPrincipalBalance(anExtension.getShouldPrincipalBalance());
        record.setInterestBalance(anExtension.getShouldInterestBalance());
        record.setManagementBalance(anExtension.getShouldManagementBalance());
        record.setTotalBalance(anExtension.getShouldTotalBalance());
        payRecordService.addPayRecord(record);
        
        // 新增还款详情
        payPlanService.saveRecordDetail(record);

        //修改原还款计划 
        plan.setBusinStatus(PayPlanBusinStatus.EXTENSION.getCode());
        payPlanService.setPayPlanFee(record, plan);
        payPlanService.saveModifyPayPlan(plan, plan.getId());
        
        //修改申请的状态为展期 
        request.setTradeStatus(RequestTradeStatus.EXTENSION.getCode());
        
        //生成新的还款计划
        ScfPayPlan newPlan =new ScfPayPlan();
        newPlan.setRequestNo(plan.getRequestNo());
        newPlan.setCustNo(request.getCustNo());
        newPlan.setCoreCustNo(request.getCoreCustNo());
        newPlan.setFactorNo(request.getFactorNo());
        newPlan.setStartDate(anExtension.getStartDate());
        newPlan.setPlanDate(anExtension.getEndDate());
        newPlan.setRatio(anExtension.getRatio());
        newPlan.setManagementRatio(anExtension.getManagementRatio());
        newPlan.setShouldInterestBalance(anExtension.getShouldInterestBalance());
        newPlan.setShouldManagementBalance(anExtension.getShouldManagementBalance());
        newPlan.setShouldPrincipalBalance(anExtension.getShouldPrincipalBalance());
        newPlan.setShouldTotalBalance(anExtension.getShouldTotalBalance());
        
        newPlan.setSurplusInterestBalance(anExtension.getShouldInterestBalance());
        newPlan.setSurplusManagementBalance(anExtension.getShouldManagementBalance());
        newPlan.setSurplusPrincipalBalance(anExtension.getShouldPrincipalBalance());
        newPlan.setSurplusTotalBalance(anExtension.getShouldTotalBalance());
        
        //期数加1
        newPlan.setTerm(plan.getTerm()+1);
        payPlanService.addPayPlan(newPlan);
        
        //保存展期
        anExtension.setCustNo(request.getCustNo());
        anExtension.setPayPlanId(plan.getId());
        anExtension.setNewPayPlanId(newPlan.getId());
        anExtension.init();
        this.insert(anExtension);
        
        setCustName(anExtension);
        return anExtension;
    }
    
    /**
     * 修改展期记录
     * 
     * @param anExtension
     * @return
     */
    public ScfExtension saveModifyExtension(ScfExtension anExtension, Long anId) {
        BTAssert.notNull(anExtension, "修改展期记录失败-anExtension不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anExtension.getFactorNo());
        map.put("id", anId);
        if(Collections3.isEmpty(selectByClassProperty(ScfExtension.class, map))){
            throw new IllegalArgumentException("修改展期记录失败-找不到原数据");
        }

        anExtension.initModify();
        anExtension.setId(anId);
        this.updateByPrimaryKeySelective(anExtension);
        
        setCustName(anExtension);
        return anExtension;
    }

    /**
     * 分页查询展期记录列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfExtension> queryExtensionList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }
    
    /**
     * 无分页查询展期记录列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<ScfExtension> queryExtensionList(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfExtension.class, anMap);
    }

    /**
     * 查询展期记录详情
     * 
     * @param anId
     * @return
     */
    public ScfExtension findExtensionDetail(Long anId) {
        BTAssert.notNull(anId, "查询展期记录详情失败-anId不能为空");
        
        ScfExtension extension =  this.selectByPrimaryKey(anId);
        if(null == extension){
            return new ScfExtension();
        }
        
        setCustName(extension);
        return extension;
    }
    
    private void setCustName(ScfExtension anExtension){
        anExtension.setCustName(custAccountService.queryCustName(anExtension.getCustNo()));
        anExtension.setFactorName(custAccountService.queryCustName(anExtension.getFactorNo()));
    }
}
