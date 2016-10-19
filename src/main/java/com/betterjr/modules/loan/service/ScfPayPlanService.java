package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.loan.dao.ScfPayPlanMapper;
import com.betterjr.modules.loan.entity.ScfExempt;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfServiceFee;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;
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
    private CustOperatorService custOperatorService;
    @Autowired
    private ScfDeliveryNoticeService deliveryNotice;
    @Autowired
    private ScfExemptService exemptService;
    @Autowired
    private ScfServiceFeeService serviceFeeService;
    @Autowired
    private ScfCreditDetailService creditDetailService;
    
    @Reference(interfaceClass = INotificationSendService.class)
    private INotificationSendService notificationSendService;
    
    /**
     * 新增还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan addPayPlan(ScfPayPlan anPlan) {
        BTAssert.notNull(anPlan, "新增还款计划失败-anPlan不能为空");

        anPlan.init(anPlan);
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
            throw new BytterTradeException(40001, "修改还款计划失败-找不到原数据");
        }

        anPlan.initModify(anPlan, anId);
        this.updateByPrimaryKeySelective(anPlan);
        return findPayPlanDetail(anId);
    }

    /**
     * 定时任务 自动修改还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan saveAutoModifyPayPlan(ScfPayPlan anPlan, Long anId) {
        BTAssert.notNull(anPlan, "修改还款计划失败-anPlan不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anPlan.getFactorNo());
        map.put("id", anId);
        if (Collections3.isEmpty(selectByClassProperty(ScfPayPlan.class, map))) {
            throw new BytterTradeException(40001, "修改还款计划失败-找不到原数据");
        }

        anPlan.initAutoModify(anPlan, anId);
        this.updateByPrimaryKeySelective(anPlan);
        return findPayPlanDetail(anId);
    }

    /**
     * 分页查询还款计划列表
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
     * 查询还款计划列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<ScfPayPlan> findPlanList(Map<String, Object> anMap) {
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, anMap);
        for (ScfPayPlan plan : list) {
            plan.setCustName(custAccountService.queryCustName(plan.getCustNo()));
            plan.setFactorName(custAccountService.queryCustName(plan.getFactorNo()));
        }
        return list;
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
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanByRequest(String anRequestNo) {
        Map<String, Object> propValue = new HashMap<String, Object>();
        propValue.put("requestNo", anRequestNo);
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, "requestNo", anRequestNo);
        if (Collections3.isEmpty(list)) {
            return new ScfPayPlan();
        }
        return Collections3.getFirst(list);
    }

    /**
     * 计算结束日期（用于放款时 填入放款日期后调用）
     * 
     * @param scheme
     * @return
     */
    public String getEndDate(String anLoanDate, Integer anPeriod, Integer anPeriodUnit) {
        String endDate = "";

        // 1：日，2：月，
        if (1 == anPeriodUnit) {
            endDate = BetterDateUtils.addStrDays(anLoanDate, anPeriod);
        }
        else {
            endDate = BetterDateUtils.addStrMonths(anLoanDate, anPeriod);
        }

        // 算头不算尾，所以减掉一天,
        endDate = BetterDateUtils.addStrDays(endDate, -1);
        return BetterDateUtils.formatDate(BetterDateUtils.parseDate(endDate), "yyyy-MM-dd");
    }

    /**
     * 计算利息（用于放款时 计息）
     * 
     * @param anRequestNo
     *            申请编号
     * @param anLoanBalance
     *            申请金额
     * @param anType
     *            1：管理费，2：手续费，3：利息
     * @param endDate 
     * @param loanDate 
     * @return
     */
    public BigDecimal getFee(Long factorNo, BigDecimal anBalance, BigDecimal ratio, String loanDate, String endDate) {
        int days = getDaysBetween(loanDate, endDate);
        FactorParam param = DictUtils.loadObject("FactorParam", factorNo.toString(), FactorParam.class);
        Assert.notNull(param, "获取费用失败-请先设置系统参数");
        
        //金额 X 年利率 / 一年的天数  X 实现融资天数  / 100
        BigDecimal fee = anBalance.multiply(ratio).multiply(new BigDecimal(days)).divide(new BigDecimal(100));
        return MathExtend.divide(fee, new BigDecimal(param.getCountDays()));
    }
    
    /**
     * 进入还款界面后，填充还款数据
     * 
     * @param anRequestNo
     * @param anPayDate
     * @return
     */
    public ScfPayRecord queryRepaymentFee(String anRequestNo, String anPayType, String anFactorNo, String anPayDate, BigDecimal anTotalBalance) {
        ScfPayRecord record = new ScfPayRecord();
        ScfPayPlan plan = findPlanByRequestNo(anRequestNo);
        record.setPrincipalBalance(plan.getSurplusPrincipalBalance());
        record.setInterestBalance(plan.getSurplusInterestBalance());
        record.setManagementBalance(plan.getSurplusManagementBalance());
        
        FactorParam param = DictUtils.loadObject("FactorParam", anFactorNo, FactorParam.class);
        BTAssert.notNull(param, "查询还款费用失败-请先设置系统参数");

        // 还款类型：1：正常还款，2：提前还款，3：逾期还款
        if ("2".equals(anPayType)) {
            // 提前还款手续费= 本次还款本金 * 提前还款手续费利率 / 100
            BigDecimal servicefee = MathExtend.multiply(plan.getSurplusPrincipalBalance().divide(new BigDecimal(100)), param.getAdvanceRepaymentRatio());
            record.setServicefeeBalance(servicefee);
            
            //提前还款时-按天计算利息
            BigDecimal managementBalance = getFee(plan.getFactorNo(), plan.getSurplusPrincipalBalance(),  plan.getManagementRatio(), plan.getStartDate(), anPayDate);
            BigDecimal interestBalance = getFee(plan.getFactorNo(), plan.getSurplusPrincipalBalance(),  plan.getRatio(), plan.getStartDate(), anPayDate);
            record.setInterestBalance(interestBalance);
            record.setManagementBalance(managementBalance);
            record.setTotalBalance(record.getPrincipalBalance().add(record.getInterestBalance()).add(record.getManagementBalance()).add(servicefee));
        }
        else if ("3".equals(anPayType)) {
            // 逾期还款 需要计算滞纳金
            record.setLatefeeBalance(plan.getSurplusLatefeeBalance());
            record.setPenaltyBalance(plan.getSurplusPenaltyBalance());
            record.setTotalBalance(plan.getSurplusTotalBalance());
            record.setOverdueDays(plan.getOverdueDays());
            
        }
        else{
            //即使逾期了，选择正常还款 也是不收罚息的，所以要把罚息去掉
            record.setTotalBalance(MathExtend.subtract(MathExtend.subtract(plan.getSurplusTotalBalance(), plan.getSurplusLatefeeBalance()),plan.getSurplusPenaltyBalance()));
        }

        record.setPayPlanId(plan.getId());
        record.setPayDate(anPayDate);
        record.setPlanPayDate(plan.getPlanDate());
        return record;
    }

    public ScfPayRecord queryDistributeFee(String anRequestNo, String anPayType, String anPayDate, BigDecimal anTotalBalance) {
        ScfPayPlan plan = this.findPlanByRequestNo(anRequestNo);
        BigDecimal managementBalance = plan.getSurplusManagementBalance();
        BigDecimal interestBalance = plan.getSurplusInterestBalance();
        return fp(anPayDate, anTotalBalance, plan, managementBalance, interestBalance);
    }
    
    /**
     * 经销商还款 -计算利息 -填入(本次还款额) 时 调用
     * 
     * @param anRecord
     * @return
     */
    public ScfPayRecord querySellerRepaymentFee(String anRequestNo, String anPayType, String anPayDate, BigDecimal anTotalBalance) {
        ScfPayPlan plan = this.findPlanByRequestNo(anRequestNo);
        BigDecimal managementBalance = getFee(plan.getFactorNo(), plan.getSurplusPrincipalBalance(), plan.getManagementRatio(), plan.getStartDate(), anPayDate);
        BigDecimal interestBalance = getFee(plan.getFactorNo(), plan.getSurplusPrincipalBalance(), plan.getRatio(), plan.getStartDate(), anPayDate);
        return fp(anPayDate, anTotalBalance, plan, managementBalance, interestBalance);
    }

    private ScfPayRecord fp(String anPayDate, BigDecimal anTotalBalance, ScfPayPlan plan, BigDecimal managementBalance,
            BigDecimal interestBalance) {
        ScfPayRecord record = new ScfPayRecord();
        if (false == MathExtend.compareToZero(anTotalBalance)) {
            anTotalBalance = plan.getSurplusPrincipalBalance().add(interestBalance).add(managementBalance);
        }
        
        //将还款金额  分配到各项费用
        if (anTotalBalance.compareTo(managementBalance) <= 0) {
            record.setManagementBalance(anTotalBalance);
            record.setInterestBalance(BigDecimal.ZERO);
            record.setPrincipalBalance(BigDecimal.ZERO);
        }
        else if (anTotalBalance.compareTo(managementBalance.add(interestBalance)) <= 0) {
            record.setManagementBalance(managementBalance);
            record.setInterestBalance(MathExtend.subtract(anTotalBalance, managementBalance));
            record.setPrincipalBalance(BigDecimal.ZERO);
        }
        else {
            record.setManagementBalance(managementBalance);
            record.setInterestBalance(interestBalance);
            record.setPrincipalBalance(anTotalBalance.subtract(managementBalance).subtract(interestBalance));
        }

        record.setTotalBalance(anTotalBalance);
        record.setPayDate(anPayDate);
        record.setPlanPayDate(plan.getPlanDate());
        record.setPayPlanId(plan.getId());
        return record;
    }

    /**
     * 计算还款方式-还款时，在选择还款日期后调用，用于填充还款方式
     * 
     * @param anRequestNo
     * @param anPayDate
     * @return
     */
    public ArrayList<Map<String, String>> calculatPayType(String anRequestNo, String anPayDate) {
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> payType = new HashMap<String, String>();

        // 融资方式 ：1,订单，2:票据;3:应收款;4:经销商
        if (BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType())) {
            // 经销商融资只能选经销商还款
            payType = QueryTermBuilder.newInstance().put("value", "6").put("name", "经销商还款").build();
            list.add(payType);
        }
        else 
        {
            payType = QueryTermBuilder.newInstance().put("value", "1").put("name", "正常还款").build();
            list.add(payType);

            ScfPayPlan plan = findPayPlanByRequest(request.getRequestNo());
            BTAssert.notNull(plan, "还款计划为空");
            int overDays = getOverDueDays(plan.getPlanDate(), anPayDate);
            if (overDays > 0) {
                // 逾期可使用正常还款和逾期还款
                payType = QueryTermBuilder.newInstance().put("value", "3").put("name", "逾期还款").build();
                list.add(payType);
            }
            else {
                // 未到期可使用正常还款和提前还款
                payType = QueryTermBuilder.newInstance().put("value", "2").put("name", "提前还款").build();
                list.add(payType);
            }
        }

        return list;
    }

    /**
     * 获取逾期相关数据
     * 
     * @param anPlan
     * @param anCustNo
     */
    public Map<String, BigDecimal> getOverDueFee(String anStartDate, String anEndDate, BigDecimal anPrincipalBalance, String anCustNo) {
        Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
        // 计算逾期天数
        int overDays = getOverDueDays(anStartDate, anEndDate);
        FactorParam param = DictUtils.loadObject("FactorParam", anCustNo, FactorParam.class);
        BTAssert.notNull(param, "获取逾期天数失败-请先设置系统参数");

        // 小于宽限期不计算罚息
        if (overDays <= param.getGraceDays()) {
            feeMap.put("latefeeBalance", BigDecimal.ZERO);
            feeMap.put("penaltyBalance", BigDecimal.ZERO);
            return feeMap;
        }

        // 计算罚息
        BigDecimal baseBalance = MathExtend.multiply(anPrincipalBalance, new BigDecimal(overDays)).divide(new BigDecimal(1000));
        feeMap.put("latefeeBalance", MathExtend.multiply(baseBalance, param.getLatefeeRatio()));
        feeMap.put("penaltyBalance", MathExtend.multiply(baseBalance, param.getPenaltyRatio()));
        return feeMap;
    }

    public int getDaysBetween(String startDate, String endDate) {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(BetterDateUtils.parseDate(startDate));
        endCalendar.setTime(BetterDateUtils.parseDate(endDate));
        return BetterDateUtils.getDaysBetween(startCalendar, endCalendar);
    }
    
    public int getOverDueDays(String planDate, String payDate) {
        Calendar planCalendar = Calendar.getInstance();
        Calendar payCalendar = Calendar.getInstance();
        planCalendar.setTime(BetterDateUtils.parseDate(planDate));
        payCalendar.setTime(BetterDateUtils.parseDate(payDate));
        // 是滞逾期
        if (payCalendar.compareTo(planCalendar) > 0) {
            // 逾期天数
            return BetterDateUtils.getDaysBetween(planCalendar, payCalendar);
            
        }
        return 0;
    }

    /**
     * 保存还款（修改还款计划，添加还款记录，还款明细）
     * 
     * @param anRecord
     * @return
     */
    public ScfPayPlan saveRepayment(ScfPayRecord anRecord) {
        ScfPayPlan plan = findPayPlanDetail(anRecord.getPayPlanId());
        ScfRequest request = requestService.findRequestDetail(anRecord.getRequestNo());
        BTAssert.notNull(plan, "保存还款失败-找不到对应还款计划");

        // 新增还款记录
        anRecord.setCustNo(plan.getCustNo());
        anRecord.setFactorNo(plan.getFactorNo());
        anRecord.setPayCustNo(anRecord.getPayCustNo());
        payRecordService.addPayRecord(anRecord);

        // 如果实际还款日期 在 计划还款日期之后 并且 没有收罚息和滞纳金则要进行豁免
        this.addExempt(anRecord, plan);

        // 新增还款详情
        this.saveRecordDetail(anRecord);

        // 设置还款计划相关参数
        this.fillPayPlan(anRecord, plan);
        this.saveModifyPayPlan(plan, anRecord.getPayPlanId());
        
        //保存手续费
        this.saveServicefee(anRecord, plan);
        
        //释放授信额度(释放还款本金的信息额度)
        this.saveReleaseCredit(request, anRecord.getPrincipalBalance());
        
        // 结清
        if (false == MathExtend.compareToZero(plan.getSurplusPrincipalBalance())) {
            request.setCleanDate(plan.getPayDate());
            request.setTradeStatus(RequestTradeStatus.CLEAN.getCode());
            requestService.saveModifyRequest(request, plan.getRequestNo());
            return plan;
        }

        // 非经销商还款
        if (BetterStringUtils.equals("6", anRecord.getPayType()) == false) {
            return plan;
        }

        // ******************************以下为经销商特殊操作******************************************
        // 1.关联放款通知单
        // 2.新生成一还款计划
        // ******************************以下为经销商特殊操作******************************************

        // 关联放款通知单
        deliveryNotice.saveRelationRequest(plan.getRequestNo(), anRecord.getDeliverys());
        return createNewPlan(anRecord, plan);
    }

    //释放授信额度
    private void saveReleaseCredit(ScfRequest anRequest, BigDecimal anReleaseBalance){
        if(false == MathExtend.compareToZero(anReleaseBalance)){
            return;
        }
        
        ScfCreditInfo anCreditInfo = new ScfCreditInfo();
        anCreditInfo.setBusinFlag(anRequest.getRequestType());
        anCreditInfo.setBalance(anReleaseBalance);
        anCreditInfo.setBusinId(Long.parseLong(anRequest.getRequestNo()));
        anCreditInfo.setCoreCustNo(anRequest.getCoreCustNo());
        anCreditInfo.setCustNo(anRequest.getCustNo());
        anCreditInfo.setFactorNo(anRequest.getFactorNo());
        anCreditInfo.setCreditMode(anRequest.getCreditMode());
        anCreditInfo.setRequestNo(anRequest.getRequestNo());
        anCreditInfo.setDescription(anRequest.getDescription());
        creditDetailService.saveReleaseCredit(anCreditInfo);
    }
    
    private void saveServicefee(ScfPayRecord anRecord, ScfPayPlan plan) {
        if(false == MathExtend.compareToZero(anRecord.getServicefeeBalance())){
            return;
        }
        
        ScfServiceFee serviceFee = new ScfServiceFee();
        serviceFee.setBalance(anRecord.getServicefeeBalance());
        serviceFee.setRequestNo(plan.getRequestNo());
        serviceFee.setCustNo(anRecord.getPayCustNo());
        serviceFee.setFactorNo(plan.getFactorNo());
        serviceFee.setFeeType("2");
        serviceFee.setPayDate(anRecord.getPayDate());
        serviceFeeService.addServiceFee(serviceFee);
    }

    private ScfPayPlan createNewPlan(ScfPayRecord anRecord, ScfPayPlan anOldPlan) {
        // 这一次没有还完（还款金额 小于 剩余金额） 则 根据 剩余本金 重新计算 剩余利息
        // 如果经销商还款 一次没有还款，则默认把还计算方式 改为按天计算
        ScfPayPlan newPlan = anOldPlan;

        // 设置 新的 开始计息日（今天的利息已经收了，从明天开始 所以要往后推一天）
        newPlan.setStartDate(BetterDateUtils.addStrDays(anRecord.getPayDate(), 1));
        newPlan.setRatio(anRecord.getRatio());
        newPlan.setManagementRatio(anRecord.getManagementRatio());

        // 在原来的期数上加1
        newPlan.setTerm(anOldPlan.getTerm() + 1);
        
        //利息
        BigDecimal managementBalance = getFee(newPlan.getFactorNo(), newPlan.getSurplusPrincipalBalance(),  newPlan.getManagementRatio(), newPlan.getStartDate(), newPlan.getPlanDate());
        BigDecimal interestBalance = getFee(newPlan.getFactorNo(), newPlan.getSurplusPrincipalBalance(),  newPlan.getRatio(), newPlan.getStartDate(), newPlan.getPlanDate());

        // 新的应还=原计划已还部分 + 剩余本金*新利率*（本次还款日到截止日期的天数）
        newPlan.setShouldInterestBalance(MathExtend.add(newPlan.getAlreadyInterestBalance(), interestBalance));
        newPlan.setShouldManagementBalance(MathExtend.add(newPlan.getAlreadyManagementBalance(), managementBalance));
        newPlan.setShouldTotalBalance(newPlan.getAlreadyTotalBalance().add(newPlan.getShouldPrincipalBalance()).add(newPlan.getShouldInterestBalance()).add(newPlan.getShouldManagementBalance()));

        // 新的未还
        newPlan.setSurplusInterestBalance(interestBalance);
        newPlan.setSurplusManagementBalance(managementBalance);
        newPlan.setSurplusTotalBalance(newPlan.getSurplusPrincipalBalance().add(newPlan.getSurplusInterestBalance()).add(newPlan.getSurplusManagementBalance()));
        return addPayPlan(newPlan);
    }

    private void addExempt(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 为经销商还款时暂时不豁免
        if (BetterStringUtils.equals("6", anRecord.getPayType())) {
            return;
        }

        // 已逾期并且没有收逾期相关费用，则豁免相关费用。
        if (BetterDateUtils.parseDate(anRecord.getPayDate()).after(BetterDateUtils.parseDate(anRecord.getPlanPayDate()))
                && (false == MathExtend.compareToZero(anRecord.getLatefeeBalance()) || false == MathExtend.compareToZero(anRecord.getPenaltyBalance()))) {
            ScfExempt anExempt = new ScfExempt();
            anExempt.setRequestNo(anPlan.getRequestNo());
            anExempt.setExemptDate(anRecord.getPayDate());
            anExempt.setFactorNo(anRecord.getFactorNo());
            anExempt.setCustNo(anRecord.getCustNo());
            anExempt.setLatefeeBalance(anPlan.getSurplusLatefeeBalance());
            anExempt.setPenaltyBalance(anPlan.getSurplusPenaltyBalance());
            exemptService.addExempt(anExempt);
        }
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
        if (MathExtend.compareToZero(anRecord.getPrincipalBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPrincipalBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (MathExtend.compareToZero(anRecord.getInterestBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getInterestBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (MathExtend.compareToZero(anRecord.getManagementBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getManagementBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (MathExtend.compareToZero(anRecord.getPenaltyBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPenaltyBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (MathExtend.compareToZero(anRecord.getLatefeeBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getLatefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (MathExtend.compareToZero(anRecord.getServicefeeBalance())) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getServicefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }

    }

    public void fillPayPlan(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 剩余金额
        anPlan.setSurplusTotalBalance(MathExtend.subtract(anPlan.getSurplusTotalBalance(), anRecord.getTotalBalance()));
        anPlan.setSurplusPrincipalBalance(MathExtend.subtract(anPlan.getSurplusPrincipalBalance(), anRecord.getPrincipalBalance()));
        anPlan.setSurplusInterestBalance(MathExtend.subtract(anPlan.getSurplusInterestBalance(), anRecord.getInterestBalance()));
        anPlan.setSurplusManagementBalance(MathExtend.subtract(anPlan.getSurplusManagementBalance(), anRecord.getManagementBalance()));
        anPlan.setSurplusPenaltyBalance(MathExtend.subtract(anPlan.getSurplusPenaltyBalance(), anRecord.getPenaltyBalance()));
        anPlan.setSurplusLatefeeBalance(MathExtend.subtract(anPlan.getSurplusLatefeeBalance(), anRecord.getLatefeeBalance()));

        // 已还金额
        anPlan.setAlreadyPrincipalBalance(MathExtend.add(anPlan.getAlreadyPrincipalBalance(), anRecord.getPrincipalBalance()));
        anPlan.setAlreadyInterestBalance(MathExtend.add(anPlan.getAlreadyInterestBalance(), anRecord.getInterestBalance()));
        anPlan.setAlreadyManagementBalance(MathExtend.add(anPlan.getAlreadyManagementBalance(), anRecord.getManagementBalance()));
        anPlan.setAlreadyPenaltyBalance(MathExtend.add(anPlan.getAlreadyPenaltyBalance(), anRecord.getPenaltyBalance()));
        anPlan.setAlreadyLatefeeBalance(MathExtend.add(anPlan.getAlreadyLatefeeBalance(), anRecord.getLatefeeBalance()));
        anPlan.setAlreadyTotalBalance(MathExtend.add(anPlan.getAlreadyTotalBalance(), anRecord.getTotalBalance()));
        
        anPlan.setPayDate(anRecord.getPayDate());
        
        // 状态改为结清
        anPlan.setBusinStatus("2");
    }

    /**
     * 自动更新逾期数据
     */
    public void saveAutoUpdateOverDue() {
        // 查询已标记为逾期 和 正常状态的还款计划
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        String[] status = new String[] { "1", "3" };
        anPropValue.put("businStatus", status);
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, anPropValue);

        String todayDate = BetterDateUtils.getDate();
        for (ScfPayPlan plan : list) {
            // 计算到今天的逾期天数
            int dueDays = getOverDueDays(plan.getPlanDate(), todayDate);
            if (dueDays <= 0) {
                continue;
            }

            plan.setOverdueDays(dueDays);
            String factorNo = plan.getFactorNo().toString();
            FactorParam param = DictUtils.loadObject("FactorParam", factorNo, FactorParam.class);
            if (null == param) {
                saveAutoModifyPayPlan(plan, plan.getId());
                continue;
            }

            if (dueDays < param.getGraceDays()) {
                continue;
            }

            // 过了宽限期需要计算费用
            Map<String, BigDecimal> feeMap = getOverDueFee(plan.getPlanDate(), todayDate, plan.getSurplusPrincipalBalance(), factorNo);

            // 应还
            BigDecimal shouldTotalBalance = plan.getShouldTotalBalance().subtract(plan.getShouldLatefeeBalance())
                    .subtract(plan.getShouldPenaltyBalance()).add(feeMap.get("latefeeBalance")).add(feeMap.get("penaltyBalance"));
            plan.setShouldTotalBalance(shouldTotalBalance);
            plan.setShouldLatefeeBalance(feeMap.get("latefeeBalance"));
            plan.setShouldPenaltyBalance(feeMap.get("penaltyBalance"));

            // 剩余未还
            BigDecimal surplusTotalBalance = plan.getSurplusTotalBalance().subtract(plan.getSurplusLatefeeBalance())
                    .subtract(plan.getSurplusPenaltyBalance()).add(feeMap.get("latefeeBalance")).add(feeMap.get("penaltyBalance"));
            plan.setSurplusTotalBalance(surplusTotalBalance);
            plan.setSurplusLatefeeBalance(feeMap.get("latefeeBalance").subtract(plan.getAlreadyLatefeeBalance()));
            plan.setSurplusPenaltyBalance(feeMap.get("penaltyBalance").subtract(plan.getAlreadyPenaltyBalance()));

            // 修改状态为逾期
            saveAutoModifyPayPlan(plan, plan.getId());
            saveAutoUpdateRequest(plan.getRequestNo());
        }
    }

    /**
     * 自动更新逾期数据
     */
    public void saveAutoUpdateRequest(String anRequestNo) {
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        request.setTradeStatus(RequestTradeStatus.OVERDUE.getCode());
        requestService.saveAutoModifyRequest(request, request.getRequestNo());
    }

    private ScfPayPlan findPlanByRequestNo(String anRequestNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requestNo", anRequestNo);
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, map);
        if (Collections3.isEmpty(list)) {
            throw new BytterTradeException(40001, "查询还款费用失败-找不到还款计划");
        }

        return Collections3.getFirst(list);
    }
    
    /**
     * 还款提醒
     */
    public boolean notifyPay(String anRequestNo){
        try {
            ScfRequest request = requestService.findRequestDetail(anRequestNo);
            ScfPayPlan plan = this.findPlanByRequestNo(anRequestNo);
            
            //供应商融资
            String profileName = "供应商提醒核心企业还款";
            CustInfo sendCustomer = custAccountService.findCustInfo(request.getCustNo());
            CustInfo accCustomer = custAccountService.findCustInfo(request.getCoreCustNo());
            
            //经销融资（核心企业提醒经销商还款）
            if(BetterStringUtils.equals(request.getRequestType(), RequestType.SELLER.getCode())){
                profileName = "核心企业提醒经销商还款";
                sendCustomer = custAccountService.findCustInfo(request.getCoreCustNo());
                accCustomer = custAccountService.findCustInfo(request.getCustNo());
            }
            
            CustOperatorInfo sendOperator = custOperatorService.findCustOperatorInfo(sendCustomer.getRegOperId());// 
            CustOperatorInfo accOperator = custOperatorService.findCustOperatorInfo(accCustomer.getRegOperId());// 
            
            Builder builder = NotificationModel.newBuilder(profileName, sendCustomer, sendOperator);
            builder.addParam("custName", request.getCustName());
            builder.addParam("coreCustName", request.getCoreCustName());
            builder.addParam("planDate", plan.getPlanDate());
            builder.addParam("requestNo", plan.getRequestNo());
            builder.addReceiver(accCustomer.getCustNo(), null);
            return notificationSendService.sendNotification(builder.build());
        }
        catch (final Exception e) {
            logger.error("还款提醒消息发送失败！", e);
        }
        return false;
    }
}
