package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfPayPlanMapper;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.param.entity.FactorParam;

@Service
public class ScfPayPlanService extends BaseService<ScfPayPlanMapper, ScfPayPlan> {

    @Autowired
    private ScfPayRecordService payRecordService;
    @Autowired

    private ScfPayRecordDetailService payRecordDetailService;

    @Autowired
    private ScfRequestService requestService;

    @Autowired
    private CustAccountService custAccountService;

    @Autowired
    private ScfRequestSchemeService schemeService;
    
    @Autowired
    private ScfDeliveryNoticeService deliveryNotice;
    

    /**
     * 新增还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan addPayPlan(ScfPayPlan anPlan) {
        BTAssert.notNull(anPlan, "新增还款计划失败-anPlan不能为空");

        anPlan.init();
        this.insert(anPlan);
        return findPayPlanDetail(anPlan.getId());
    }

    /**
     * 修改还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan saveModifyPayPlan(ScfPayPlan anPlan, Long anId) {
        BTAssert.notNull(anPlan, "修改还款计划失败-anPlan不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anPlan.getFactorNo());
        map.put("id", anId);
        if (Collections3.isEmpty(selectByClassProperty(ScfPayPlan.class, map))) {
            throw new IllegalArgumentException("修改还款计划失败-找不到原数据");
        }

        anPlan.initModify();
        anPlan.setId(anId);
        this.updateByPrimaryKeySelective(anPlan);
        return findPayPlanDetail(anId);
    }

    /**
     * 查询还款计划列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfPayPlan> queryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfPayPlan> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        for (ScfPayPlan plan : page) {
            plan.setCustName(custAccountService.queryCustName(plan.getCustNo()));
            plan.setFactorName(custAccountService.queryCustName(plan.getFactorNo()));
        }
        return page;
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanDetail(Long anId) {
        BTAssert.notNull(anId, "查询还款计划失败-anId不能为空");

        ScfPayPlan plan = this.selectByPrimaryKey(anId);
        if (null == plan) {
           return new ScfPayPlan();
        }

        plan.setCustName(custAccountService.queryCustName(plan.getCustNo()));
        plan.setFactorName(custAccountService.queryCustName(plan.getFactorNo()));
        return plan;
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanByProperty(Map<String, Object> anPropValue) {
        @SuppressWarnings("unchecked")
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, anPropValue);
        if (Collections3.isEmpty(list)) {
            return new ScfPayPlan();
        }
        return Collections3.getFirst(list);
    }

    /**
     * 计算结束日期（用于放款时  填入放款日期后调用）
     * 
     * @param scheme
     * @return
     */
    public String calculatEndDate(String anRequestNo, String anLoanDate) {
        String endDate = "";
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        if (null == scheme) {
            return endDate;
        }

        // 1：日，1：月，
        if (1 == scheme.getApprovedPeriod()) {
            endDate = BetterDateUtils.addStrDays(anLoanDate, scheme.getApprovedPeriod());
        }
        else {
            endDate = BetterDateUtils.addStrMonths(anLoanDate, scheme.getApprovedPeriod());
        }

        // 算头不算尾，所以减掉一天
        return BetterDateUtils.addStrDays(endDate, -1);
    }

    /**
     * 计算利息（用于放款时 计息）
     * 
     * @param anRequestNo 申请编号
     * @param anLoanBalance 申请金额
     * @param anType 1：管理费，2：手续费，3：利息
     * @return
     */
    public BigDecimal calculatFee(String anRequestNo, BigDecimal anLoanBalance, int anType) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BigDecimal ratio = scheme.getApprovedRatio();
        BigDecimal scale = new BigDecimal(100);
        if (1 == anType) {
            ratio = scheme.getApprovedManagementRatio();
        }
        else if (2 == anType) {
            ratio = scheme.getServicefeeRatio();
            scale = new BigDecimal(1000);
        }

        if (null == ratio) {
            return new BigDecimal(0);
        }

        BigDecimal fee = anLoanBalance.multiply(ratio).multiply(new BigDecimal(scheme.getApprovedPeriod())).divide(scale);
        logger.debug("本金："+ anLoanBalance + "--利率："+ ratio + "--天数："+ scheme.getApprovedPeriod() + "--scale:"+ scale + "--费用="+ fee);
        return fee;
    }

    /**
     * 进入还款界面后，填充还款数据
     * @param anRequestNo
     * @return
     */
    public ScfPayRecord queryRepaymentFee(String anRequestNo, String anPayType, String anFactorNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requestNo", anRequestNo);
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, map);
        if (Collections3.isEmpty(list)) {
            throw new IllegalArgumentException("查询还款费用失败-找不到还款计划");
        }

        ScfPayRecord record = new ScfPayRecord();
        ScfPayPlan plan = Collections3.getFirst(list);
        record.setPrincipalBalance(plan.getSurplusPrincipalBalance());
        record.setInterestBalance(plan.getSurplusInterestBalance());
        record.setManagementBalance(plan.getSurplusManagementBalance());
        record.setPayPlanId(plan.getId());
        record.setPayDate(BetterDateUtils.getDate());
        record.setPlanPayDate(plan.getPlanDate());
        
        //还款类型：1：正常还款，2：提前还款，3：逾期还款，4：豁免，5：逾期豁免, 6:经销商还款，
        if("2".equals(anPayType)){
            FactorParam param = DictUtils.loadObject("FactorParam", anFactorNo, FactorParam.class);
            BTAssert.notNull(param, "查询还款费用失败-请先设置系统参数");
            
            //本次还款本金 * 利息   / 100
            BigDecimal servicefee = plan.getSurplusPrincipalBalance().multiply(param.getAdvanceRepaymentRatio()).divide(new BigDecimal(100));
            record.setServicefeeBalance(servicefee);
        }
        else if("3".equals(anPayType)){
            setOverDueFee(plan, anFactorNo, record);
        }
        return record;
    }

    
    /**
     * 经销商还款 -计算利息 -填入(本次还款额) 时  调用
     * @param anRecord
     * @return
     */
    public ScfPayRecord querySellerRepaymentFee(ScfPayRecord anRecord){
        ScfPayPlan plan = this.findPayPlanDetail(anRecord.getPayPlanId());
        Map<String, BigDecimal> map = getSellerPayPlanFee(anRecord, plan.getStartDate());
        BigDecimal managementBalance = map.get("mgrBalance");
        BigDecimal interestBalance = map.get("interestBalance");
        
        anRecord.setManagementBalance(managementBalance);
        anRecord.setInterestBalance(interestBalance);
        anRecord.setPrincipalBalance(plan.getSurplusPrincipalBalance());
        anRecord.setTotalBalance(map.get("totalBalance"));
        return anRecord;
    }
    
    /**
     * 计算还款方式-还款时，在选择还款日期后调用，用于填充还款方式
     * @param anRequestNo
     * @param anPayDate
     * @return
     */
    public Map<String, String> calculatPayType(String anRequestNo, String anPayDate){
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        Map<String, String> payType = new HashMap<String, String>();
       
        //融资方式 ：1,订单，2:票据;3:应收款;4:经销商
        if(BetterStringUtils.equals("4", request.getRequestType())){
            //经销商融资只能选经销商还款
            payType.put("4", "经销商还款");
        }else{
            Map<String, Object> propValue = new HashMap<String, Object>();
            propValue.put("requestNo", anRequestNo);
            ScfPayPlan plan = findPayPlanByProperty(propValue);
            BTAssert.notNull(plan, "还款计划为空");
            
            int overDays = getOverDueDays(plan.getPlanDate(), anPayDate, plan.getCustNo()+"");
            if(overDays > 0){
                //逾期可使用正常还款和逾期还款
                payType.put("3", "逾期还款");
            }else{
                //未到期可使用正常还款和提前还款
                payType.put("2", "提前还款");
            }
            payType.put("1", "正常还款");
        }
        
        return payType;
    }
    
    /**
     * 设置逾期相关数据
     * 
     * @param anPlan
     * @param anCustNo
     */
    private void setOverDueFee(ScfPayPlan anPlan, String anCustNo, ScfPayRecord anRecord) {
        // 逾期天数
        int overDays = getOverDueDays(anPlan.getPlanDate(), BetterDateUtils.getDate(), anCustNo);
        if(overDays < 0){
            return;
        }
        
        FactorParam param = DictUtils.loadObject("FactorParam", anCustNo, FactorParam.class);
        BTAssert.notNull(param, "获取逾期费用失败，请先设置系统参数");
        anRecord.setOverdueDays(overDays);

        // 本次还款本金 * 利息 * 天数 / 1000
        BigDecimal latefee = anPlan.getSurplusPrincipalBalance().multiply(param.getLatefeeRatio()).multiply(new BigDecimal(overDays)).divide(new BigDecimal(1000));
        anRecord.setLatefeeBalance(latefee);
        BigDecimal penaltyFee = anPlan.getSurplusPrincipalBalance().multiply(param.getPenaltyRatio()).multiply(new BigDecimal(overDays)).divide(new BigDecimal(1000));
        anRecord.setPenaltyBalance(penaltyFee);
    }
    
    private int getOverDueDays(String planDate, String payDate, String custNo){
        Calendar planCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        planCalendar.setTime(BetterDateUtils.parseDate(planDate));
        nowCalendar.setTime(BetterDateUtils.parseDate(payDate));
        //是滞逾期
        if(nowCalendar.compareTo(planCalendar) >= 0 ){
            //逾期天数
            int overDays = BetterDateUtils.getDaysBetween(planCalendar , nowCalendar);
            FactorParam param = DictUtils.loadObject("FactorParam", custNo, FactorParam.class);
            BTAssert.notNull(param, "获取逾期天数失败-请先设置系统参数");
            
            //是否大于宽限限
            if(overDays > param.getGraceDays()){
                return overDays;
            }
        }
        return -1;
    }
    
    public ScfPayPlan saveRepayment(ScfPayRecord anRecord) {
        ScfPayPlan plan = findPayPlanDetail(anRecord.getPayPlanId());
        BTAssert.notNull(plan, "保存还款失败-找不到对应还款计划");
        
        //新增还款记录
        anRecord.setCustNo(plan.getCustNo());
        anRecord.setFactorNo(plan.getFactorNo());
        anRecord.setPayCustNo(anRecord.getPayCustNo());
        anRecord.init();
        payRecordService.addPayRecord(anRecord);
        
        ScfPayRecord record = new ScfPayRecord();
        record.setPrincipalBalance(plan.getSurplusPrincipalBalance());
        record.setPayPlanId(anRecord.getPayPlanId());
        record.setPayDate(anRecord.getPayDate());
        Map<String, BigDecimal> map = getSellerPayPlanFee(record, plan.getStartDate());

        //新增还款详情
        saveRecordDetail(anRecord);
        
        //设置还款计划相关参数
        setPayPlanFee(anRecord, plan);
        
        //状态改为结清
        plan.setBusinStatus("2");
        plan = saveModifyPayPlan(plan, anRecord.getPayPlanId());
        
        //不是经销商还款 
        if(BetterStringUtils.equals("6", anRecord.getPayType()) == false){
            return plan;
        }
        
        //关联放款通知单
        deliveryNotice.saveRelationRequest(plan.getRequestNo(),  anRecord.getDeliverys());
        
        //是经销商，但本次已还款完成
        if(anRecord.getTotalBalance().compareTo(map.get("totalBalance")) < 0 == false){
            return plan;
        }
        
        ScfPayPlan newPlan = new ScfPayPlan();
        newPlan.setRequestNo(plan.getRequestNo());
        // 如果经销商还款 一次没有还款，则默认把还计算方式 改为按天计算
        // 这一次没有还完（还款金额 小于 剩余金额） 则 根据 剩余本金 重新计算 剩余利息
            
        //设置 新的 开始计息日（今天的利息已经收了，从明天开始 所以要往后推一天）
        newPlan.setStartDate(BetterDateUtils.addStrDays(anRecord.getPayDate(), 1));
        newPlan.setPlanDate(plan.getPayDate());
        newPlan.setRatio(anRecord.getRatio());
        newPlan.setManagementRatio(anRecord.getManagementRatio());
        newPlan.setCustNo(plan.getCustNo());
        newPlan.setCoreCustNo(plan.getCoreCustNo());
        newPlan.setFactorNo(plan.getFactorNo());
        //在原来的期数上加1
        newPlan.setTerm(plan.getTerm() +1);
        newPlan.setSurplusPrincipalBalance(plan.getSurplusPrincipalBalance().subtract(anRecord.getPrincipalBalance()));
        
        map = getSellerPayPlanFee(anRecord, plan.getStartDate());
        BigDecimal interestBalance = map.get("interestBalance");
        BigDecimal mgrBalance = map.get("mgrBalance");
        
        //新的应还
        newPlan.setShouldInterestBalance(plan.getAlreadyInterestBalance().add(interestBalance));
        newPlan.setShouldManagementBalance(plan.getAlreadyManagementBalance().add(mgrBalance));
        
        //新的剩余
        newPlan.setSurplusInterestBalance(interestBalance);
        newPlan.setSurplusManagementBalance(mgrBalance);
        
        return addPayPlan(newPlan);
    }

    /**
     * 计算剩余应还
     * @param anRecord
     * @param plan
     */
    private Map<String, BigDecimal> getSellerPayPlanFee(ScfPayRecord anRecord, String startDate) {
        ScfPayPlan plan = this.findPayPlanDetail(anRecord.getPayPlanId());
        
        //计算剩余天数
        Calendar startCalendar = Calendar.getInstance();
        Calendar payCalendar = Calendar.getInstance();
        startCalendar.setTime(BetterDateUtils.parseDate(startDate));
        payCalendar.setTime(BetterDateUtils.parseDate(plan.getPlanDate()));
        int surplusDays = BetterDateUtils.getDaysBetween(startCalendar, payCalendar);
        
        //计算利率(如果为月则换算成天=ratio*12/一年的天数)
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(plan.getRequestNo());
        BigDecimal ratio = anRecord.getRatio();
        BigDecimal mgrRatio = anRecord.getManagementRatio();
        if(BetterStringUtils.equals("2", scheme.getApprovedPeriodUnit().toString())){
            FactorParam param = DictUtils.loadObject("FactorParam", plan.getFactorNo().toString(), FactorParam.class);
            ratio = ratio.multiply(new BigDecimal(12)).divide(new BigDecimal(param.getCountDays())).setScale(2, BigDecimal.ROUND_HALF_UP);
            mgrRatio = mgrRatio.multiply(new BigDecimal(12)).divide(new BigDecimal(param.getCountDays())).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        
        //剩余本金
        BigDecimal surplusPrincipalBalance = plan.getSurplusPrincipalBalance();
        
        //计算利息(本金*利息*天数/100)
        BigDecimal interestBalance = surplusPrincipalBalance.multiply(ratio).multiply(new BigDecimal(surplusDays)).divide(new BigDecimal(100));
        BigDecimal mgrBalance = surplusPrincipalBalance.multiply(mgrRatio).multiply(new BigDecimal(surplusDays)).divide(new BigDecimal(100));
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("interestBalance", interestBalance);
        map.put("mgrBalance", mgrBalance);
        map.put("totalBalance", mgrBalance.add(interestBalance).add(surplusPrincipalBalance).add(surplusPrincipalBalance));
        return map;
    }

    /**
     * 还款明细
     * 
     * @param anRecord
     */
    public void saveRecordDetail(ScfPayRecord anRecord) {
        ScfPayRecordDetail recordDetail = new ScfPayRecordDetail();
        recordDetail.setPayRecordId(anRecord.getId());
        recordDetail.setCustNo(anRecord.getCustNo());
        recordDetail.setFactorNo(anRecord.getFactorNo());
        recordDetail.setPayDate(anRecord.getPayDate());
        recordDetail.setPayType(anRecord.getPayType());
        if (null != anRecord.getPrincipalBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPrincipalBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getInterestBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getInterestBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getManagementBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getManagementBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getPenaltyBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPenaltyBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getLatefeeBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getLatefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getServicefeeBalance()) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getServicefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }

    }

    private void setPayPlanFee(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 剩余金额
        anPlan.setSurplusPrincipalBalance(minusCalculat(anPlan.getSurplusPrincipalBalance(), anRecord.getPrincipalBalance()));
        anPlan.setSurplusInterestBalance(minusCalculat(anPlan.getSurplusInterestBalance(), anRecord.getInterestBalance()));
        anPlan.setSurplusManagementBalance(minusCalculat(anPlan.getSurplusManagementBalance(), anRecord.getManagementBalance()));
        anPlan.setSurplusPenaltyBalance(minusCalculat(anPlan.getSurplusPenaltyBalance(), anRecord.getPenaltyBalance()));
        anPlan.setSurplusLatefeeBalance(minusCalculat(anPlan.getSurplusLatefeeBalance(), anRecord.getLatefeeBalance()));

        // 已还金额
        anPlan.setAlreadyPrincipalBalance(plusCalculat(anPlan.getAlreadyPrincipalBalance(), anRecord.getPrincipalBalance()));
        anPlan.setAlreadyInterestBalance(plusCalculat(anPlan.getAlreadyInterestBalance(), anRecord.getInterestBalance()));
        anPlan.setAlreadyManagementBalance(plusCalculat(anPlan.getAlreadyManagementBalance(), anRecord.getManagementBalance()));
        anPlan.setAlreadyPenaltyBalance(plusCalculat(anPlan.getAlreadyPenaltyBalance(), anRecord.getPenaltyBalance()));
        anPlan.setAlreadyLatefeeBalance(plusCalculat(anPlan.getAlreadyLatefeeBalance(), anRecord.getLatefeeBalance()));
    }

    private BigDecimal minusCalculat(BigDecimal anMeiosis, BigDecimal anMinuend) {
        // 被减数为空或 小于等于0 直接返回减数
        if (null == anMinuend || anMinuend.compareTo(new BigDecimal(0)) < 1) {
            return anMeiosis;
        }

        return anMeiosis.subtract(anMinuend);
    }

    private BigDecimal plusCalculat(BigDecimal anBase, BigDecimal anPlus) {
        // 被减数为空或 小于等于0 直接返回减数
        if (null == anPlus || anPlus.compareTo(new BigDecimal(0)) < 1) {
            return anBase;
        }

        if (null == anBase) {
            return anPlus;
        }

        return anBase.add(anPlus);
    }

    
}
