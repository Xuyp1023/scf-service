package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfPayPlanMapper;
import com.betterjr.modules.loan.entity.ScfExempt;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
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
    private ScfDeliveryNoticeService deliveryNotice;
    @Autowired
    private ScfExemptService exemptService;

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

        // 1：日，1：月，
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
     * @return
     */
    public BigDecimal getFee(String anRequestNo, BigDecimal anLoanBalance, int anType) {
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        BigDecimal ratio = request.getApprovedRatio();
        BigDecimal scale = new BigDecimal(100);
        if (1 == anType) {
            ratio = request.getManagementRatio();
        }
        else if (2 == anType) {
            ratio = request.getServicefeeRatio();
            scale = new BigDecimal(1000);
        }

        if (null == ratio) {
            return BigDecimal.ZERO;
        }

        BigDecimal fee = anLoanBalance.multiply(ratio).multiply(new BigDecimal(request.getApprovedPeriod())).divide(scale);
        logger.debug("本金：" + anLoanBalance + "--利率：" + ratio + "--天数：" + request.getApprovedPeriod() + "--scale:" + scale + "--费用=" + fee);
        return fee;
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
            BigDecimal servicefee = plan.getSurplusPrincipalBalance().multiply(param.getAdvanceRepaymentRatio()).divide(new BigDecimal(100));
            record.setServicefeeBalance(servicefee);
            
            //按天计算利息
            Map<String, BigDecimal> feeMap = getInterestByDays(plan, anPayDate);
            record.setInterestBalance(feeMap.get("interestBalance"));
            record.setManagementBalance(feeMap.get("managementBalance"));
            record.setTotalBalance(record.getPrincipalBalance().add(record.getInterestBalance()).add(record.getManagementBalance()).add(servicefee));
        }
        else if ("3".equals(anPayType)) {
            // 逾期还款 需要计算滞纳金
            record.setLatefeeBalance(plan.getSurplusLatefeeBalance());
            record.setPenaltyBalance(plan.getSurplusPenaltyBalance());
            record.setTotalBalance(plan.getSurplusTotalBalance());
            record.setOverdueDays(plan.getOverdueDays());
            
        }else{
            //即使逾期了，选择正常还款 也是不收罚息的，所以要把罚息去掉
            record.setTotalBalance(plan.getSurplusTotalBalance().subtract(plan.getSurplusLatefeeBalance()).subtract(plan.getSurplusPenaltyBalance()));
        }

        record.setPayPlanId(plan.getId());
        record.setPayDate(anPayDate);
        record.setPlanPayDate(plan.getPlanDate());
        return record;
    }

    /**
     * 经销商还款 -计算利息 -填入(本次还款额) 时 调用
     * 
     * @param anRecord
     * @return
     */
    public ScfPayRecord querySellerRepaymentFee(String anRequestNo, String anPayType, String anPayDate, BigDecimal anTotalBalance) {
        ScfPayPlan plan = this.findPlanByRequestNo(anRequestNo);
        Map<String, BigDecimal> map = getInterestByDays(plan, anPayDate);
        BigDecimal managementBalance = map.get("managementBalance");
        BigDecimal interestBalance = map.get("interestBalance");
        if (null == anTotalBalance) {
            anTotalBalance = plan.getSurplusPrincipalBalance().add(interestBalance).add(managementBalance);
        }
        
        ScfPayRecord record = new ScfPayRecord();
        //将还款金额  分配到各项费用
        if (anTotalBalance.compareTo(managementBalance) <= 0) {
            record.setManagementBalance(anTotalBalance);
            record.setInterestBalance(BigDecimal.ZERO);
            record.setPrincipalBalance(BigDecimal.ZERO);
        }
        else if (anTotalBalance.compareTo(managementBalance.add(interestBalance)) <= 0) {
            record.setManagementBalance(managementBalance);
            record.setInterestBalance(anTotalBalance.subtract(managementBalance));
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
        else {

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
        BigDecimal latefeeBalance = anPrincipalBalance.multiply(param.getLatefeeRatio()).multiply(new BigDecimal(overDays)).divide(new BigDecimal(1000))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal penaltyBalance = anPrincipalBalance.multiply(param.getPenaltyRatio()).multiply(new BigDecimal(overDays)).divide(new BigDecimal(1000))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        feeMap.put("latefeeBalance", latefeeBalance);
        feeMap.put("penaltyBalance", penaltyBalance);
        return feeMap;
    }

    /**
     * 按天计算利息
     * 
     * @param anRequestNo
     * @param anPrincipalBalance
     * @param anCustNo
     * @param anDays
     * @return
     */
    public Map<String, BigDecimal> getInterestByDays(ScfPayPlan plan, String anPayDate) {
        Calendar startCalendar = Calendar.getInstance();
        Calendar payCalendar = Calendar.getInstance();
        startCalendar.setTime(BetterDateUtils.parseDate(plan.getStartDate()));
        payCalendar.setTime(BetterDateUtils.parseDate(anPayDate));
        int surplusDays = BetterDateUtils.getDaysBetween(startCalendar, payCalendar);

        Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
        FactorParam param = DictUtils.loadObject("FactorParam", plan.getFactorNo().toString(), FactorParam.class);

        BigDecimal dayManagementRatio = plan.getManagementRatio();
        BigDecimal dayRatio = plan.getRatio();

        // 以月为单位，需要换算成天
        ScfRequest request = requestService.findRequestDetail(plan.getRequestNo());
        if (request.getPeriodUnit() == 2) {
            dayManagementRatio = plan.getManagementRatio().multiply(new BigDecimal(12)).divide(new BigDecimal(param.getCountDays()));
            dayRatio = plan.getRatio().multiply(new BigDecimal(12)).divide(new BigDecimal(param.getCountDays()));
        }

        // 计算利息
        BigDecimal managementBalance = plan.getSurplusPrincipalBalance().multiply(dayManagementRatio).multiply(new BigDecimal(surplusDays))
                .divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal interestBalance = plan.getSurplusPrincipalBalance().multiply(dayRatio).multiply(new BigDecimal(surplusDays))
                .divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

        feeMap.put("managementBalance", managementBalance);
        feeMap.put("interestBalance", interestBalance);
        return feeMap;
    }

    public int getOverDueDays(String planDate, String payDate) {
        Calendar planCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        planCalendar.setTime(BetterDateUtils.parseDate(planDate));
        nowCalendar.setTime(BetterDateUtils.parseDate(payDate));
        // 是滞逾期
        if (nowCalendar.compareTo(planCalendar) > 0) {
            // 逾期天数
            return BetterDateUtils.getDaysBetween(planCalendar, nowCalendar);

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

        // 状态改为结清
        plan.setBusinStatus("2");
        plan = saveModifyPayPlan(plan, anRecord.getPayPlanId());

        // 结清
        if (plan.getSurplusPrincipalBalance().compareTo(BigDecimal.ZERO) <= 0) {
            ScfRequest request = requestService.findRequestDetail(plan.getRequestNo());
            request.setTradeStatus(RequestTradeStatus.PAYFINSH.getCode());
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

    private ScfPayPlan createNewPlan(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 这一次没有还完（还款金额 小于 剩余金额） 则 根据 剩余本金 重新计算 剩余利息
        // 如果经销商还款 一次没有还款，则默认把还计算方式 改为按天计算
        ScfPayPlan newPlan = new ScfPayPlan();
        newPlan.setRequestNo(anPlan.getRequestNo());

        // 设置 新的 开始计息日（今天的利息已经收了，从明天开始 所以要往后推一天）
        newPlan.setStartDate(BetterDateUtils.addStrDays(anRecord.getPayDate(), 1));
        newPlan.setPlanDate(anPlan.getPlanDate());
        newPlan.setRatio(anRecord.getRatio());
        newPlan.setManagementRatio(anRecord.getManagementRatio());

        newPlan.setCustNo(anPlan.getCustNo());
        newPlan.setCoreCustNo(anPlan.getCoreCustNo());
        newPlan.setFactorNo(anPlan.getFactorNo());

        // 在原来的期数上加1
        newPlan.setTerm(anPlan.getTerm() + 1);
        
        //应还本金
        newPlan.setShouldPrincipalBalance(anPlan.getSurplusPrincipalBalance().subtract(anRecord.getPrincipalBalance()));
        newPlan.setSurplusPrincipalBalance(newPlan.getShouldPrincipalBalance());
        
        Map<String, BigDecimal> map = getInterestByDays(newPlan, newPlan.getPlanDate());
        BigDecimal interestBalance = map.get("interestBalance");
        BigDecimal mgrBalance = map.get("managementBalance");

        // 新的应还
        newPlan.setShouldInterestBalance(interestBalance);
        newPlan.setShouldManagementBalance(mgrBalance);
        newPlan.setShouldTotalBalance(newPlan.getShouldPrincipalBalance().add(newPlan.getShouldInterestBalance()).add(newPlan.getShouldManagementBalance()));

        // 新的未还
        newPlan.setSurplusInterestBalance(newPlan.getShouldInterestBalance());
        newPlan.setSurplusManagementBalance(newPlan.getShouldManagementBalance());
        newPlan.setSurplusTotalBalance(newPlan.getShouldTotalBalance());
        return addPayPlan(newPlan);
    }

    private void addExempt(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 为经销商还款时暂时不豁免
        if ("6".equals(anRecord.getPayType())) {
            return;
        }

        // 已逾期并且没有收逾期相关费用，则豁免相关费用。
        if (BetterDateUtils.parseDate(anRecord.getPayDate()).after(BetterDateUtils.parseDate(anRecord.getPlanPayDate()))
                && (anRecord.getLatefeeBalance().compareTo(BigDecimal.ZERO) == 0 && anRecord.getLatefeeBalance().compareTo(BigDecimal.ZERO) == 0)) {
            ScfExempt anExempt = new ScfExempt();
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
        if (null != anRecord.getPrincipalBalance() && anRecord.getPrincipalBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPrincipalBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getInterestBalance() && anRecord.getInterestBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getInterestBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getManagementBalance() && anRecord.getManagementBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getManagementBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getPenaltyBalance() && anRecord.getPenaltyBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getPenaltyBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getLatefeeBalance() && anRecord.getLatefeeBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getLatefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }
        if (null != anRecord.getServicefeeBalance() && anRecord.getServicefeeBalance().compareTo(BigDecimal.ZERO) == 1) {
            recordDetail.init();
            recordDetail.setPayBalance(anRecord.getServicefeeBalance());
            payRecordDetailService.addRecordDetail(recordDetail);
        }

    }

    public void fillPayPlan(ScfPayRecord anRecord, ScfPayPlan anPlan) {
        // 剩余金额
        anPlan.setSurplusTotalBalance(minusCalculat(anPlan.getSurplusTotalBalance(), anRecord.getTotalBalance()));
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
        anPlan.setAlreadyTotalBalance(plusCalculat(anPlan.getAlreadyTotalBalance(), anRecord.getTotalBalance()));
        
        anPlan.setPayDate(anRecord.getPayDate());
    }

    private BigDecimal minusCalculat(BigDecimal anMeiosis, BigDecimal anMinuend) {
        // 被减数为空或 小于等于0 直接返回减数
        if (null == anMinuend || anMinuend.compareTo(BigDecimal.ZERO) < 1) {
            return anMeiosis;
        }

        return anMeiosis.subtract(anMinuend);
    }

    private BigDecimal plusCalculat(BigDecimal anBase, BigDecimal addend) {
        // 被减数为空或 小于等于0 直接返回减数
        if (null == addend || addend.compareTo(BigDecimal.ZERO) < 1) {
            return anBase;
        }

        if (null == anBase) {
            return addend;
        }

        return anBase.add(addend);
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

        ScfPayPlan plan = Collections3.getFirst(list);
        return plan;
    }
}
