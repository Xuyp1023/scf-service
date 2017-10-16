package com.betterjr.modules.loan.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_pay_plan")
public class ScfPayPlan implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO", columnDefinition = "BIGINT")
    @MetaData(value = "保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "BIGINT")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "BIGINT")
    @MetaData(value = "核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 申请编号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请编号", comments = "申请编号")
    private String requestNo;

    /**
     * 计息开始日期
     */
    @Column(name = "D_START_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "计息开始日期", comments = "计息开始日期")
    private String startDate;

    /**
     * 计划还款日期
     */
    @Column(name = "D_PLAN_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "计划还款日期", comments = "计划还款日期")
    private String planDate;

    /**
     * 实际还款日期
     */
    @Column(name = "D_PAY_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "实际还款日期", comments = "实际还款日期")
    private String payDate;

    /**
     * 应还本金
     */
    @Column(name = "F_SHOULD_PRINCIPAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还本金", comments = "应还本金")
    private BigDecimal shouldPrincipalBalance;

    /**
     * 应还利息
     */
    @Column(name = "F_SHOULD_INTEREST_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还利息", comments = "应还利息")
    private BigDecimal shouldInterestBalance;

    /**
     * 应还手续费
     */
    @Column(name = "F_SHOULD_SERVICEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还手续费", comments = "应还手续费")
    private BigDecimal shouldServicefeeBalance;

    /**
     * 应还管理费
     */
    @Column(name = "F_SHOULD_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还管理费", comments = "应还管理费")
    private BigDecimal shouldManagementBalance;

    /**
     * 应还罚息
     */
    @Column(name = "F_SHOULD_PENALTY_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还罚息", comments = "应还罚息")
    private BigDecimal shouldPenaltyBalance;

    /**
     * 应还滞纳金
     */
    @Column(name = "F_SHOULD_LATEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还滞纳金", comments = "应还滞纳金")
    private BigDecimal shouldLatefeeBalance;

    /**
     * 应还总额
     */
    @Column(name = "F_SHOULD_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还总额", comments = "应还总额")
    private BigDecimal shouldTotalBalance;

    /**
     * 已还本金
     */
    @Column(name = "F_ALREADY_PRINCIPAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还本金", comments = "已还本金")
    private BigDecimal alreadyPrincipalBalance;

    /**
     * 已还利息
     */
    @Column(name = "F_ALREADY_INTEREST_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还利息", comments = "已还利息")
    private BigDecimal alreadyInterestBalance;

    /**
     * 已还管理费
     */
    @Column(name = "F_ALREADY_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还管理费", comments = "已还管理费")
    private BigDecimal alreadyManagementBalance;

    /**
     * 已还罚息
     */
    @Column(name = "F_ALREADY_PENALTY_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还罚息", comments = "已还罚息")
    private BigDecimal alreadyPenaltyBalance;

    /**
     * 已还滞纳金
     */
    @Column(name = "F_ALREADY_LATEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还滞纳金", comments = "已还滞纳金")
    private BigDecimal alreadyLatefeeBalance;

    /**
     * 已还手续费
     */
    @Column(name = "F_ALREADY_SERVICEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还手续费", comments = "已还手续费")
    private BigDecimal alreadyServicefeeBalance;

    /**
     * 已还合计
     */
    @Column(name = "F_ALREADY_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "已还合计", comments = "已还合计")
    private BigDecimal alreadyTotalBalance;

    /**
     * 融资利率
     */
    @Column(name = "F_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "融资利率", comments = "融资利率")
    private BigDecimal ratio;

    /**
     * 管理费利率
     */
    @Column(name = "F_MANAGEMENT_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "管理费利率", comments = "管理费利率")
    private BigDecimal managementRatio;

    /**
     * 期数
     */
    @Column(name = "N_TERM", columnDefinition = "INT")
    @MetaData(value = "期数", comments = "期数")
    private Integer term;

    /**
     * 状态：1：未还，2：结清，3：逾期，4：提前还款，5：展期，6：坏账
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态：1：未还", comments = "状态：1：未还，2：结清，3：逾期，4：提前还款，5：展期，6：坏账")
    private String businStatus;

    /**
     * 逾期天数
     */
    @Column(name = "L_OVERDUE_DAYS", columnDefinition = "INT")
    @MetaData(value = "逾期天数", comments = "逾期天数")
    private Integer overdueDays;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 期限
     */
    @Column(name = "N_PERIOD", columnDefinition = "INT")
    @MetaData(value = "期限", comments = "期限")
    private Integer period;

    /**
     * 期限单位
     */
    @Column(name = "N_PERIOD_UNIT", columnDefinition = "INT")
    @MetaData(value = "期限单位", comments = "期限单位")
    private Integer periodUnit;

    @Column(name = "L_REG_OPERID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String modiOperName;

    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String modiDate;

    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String modiTime;

    @Column(name = "N_VERSION", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    private Long version;

    /**
     * 未还本金
     */
    @Column(name = "F_SURPLUS_PRINCIPAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还本金", comments = "未还本金")
    private BigDecimal surplusPrincipalBalance;

    /**
     * 未还利息
     */
    @Column(name = "F_SURPLUS_INTEREST_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还利息", comments = "未还利息")
    private BigDecimal surplusInterestBalance;

    /**
     * 未还管理费
     */
    @Column(name = "F_SURPLUS_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还管理费", comments = "未还管理费")
    private BigDecimal surplusManagementBalance;

    /**
     * 未还罚息
     */
    @Column(name = "F_SURPLUS_PENALTY_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还罚息", comments = "未还罚息")
    private BigDecimal surplusPenaltyBalance;

    /**
     * 未还滞纳金
     */
    @Column(name = "F_SURPLUS_LATEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还滞纳金", comments = "未还滞纳金")
    private BigDecimal surplusLatefeeBalance;

    /**
     * 未还手续费
     */
    @Column(name = "F_SURPLUS_SERVICEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还手续费", comments = "未还手续费")
    private BigDecimal surplusServicefeeBalance;

    /**
     * 未还合计
     */
    @Column(name = "F_SURPLUS_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "未还合计", comments = "未还合计")
    private BigDecimal surplusTotalBalance;

    /**
     * 豁免本金
     */
    @Column(name = "F_EXEMPT_PRINCIPAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免本金", comments = "豁免本金")
    private BigDecimal exemptPrincipalBalance;

    /**
     * 豁免利息
     */
    @Column(name = "F_EXEMPT_INTEREST_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免利息", comments = "豁免利息")
    private BigDecimal exemptInterestBalance;

    /**
     * 豁免管理费
     */
    @Column(name = "F_EXEMPT_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免管理费", comments = "豁免管理费")
    private BigDecimal exemptManagementBalance;

    /**
     * 豁免罚息
     */
    @Column(name = "F_EXEMPT_PENALTY_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免罚息", comments = "豁免罚息")
    private BigDecimal exemptPenaltyBalance;

    /**
     * 豁免滞纳金
     */
    @Column(name = "F_EXEMPT_LATEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免滞纳金", comments = "豁免滞纳金")
    private BigDecimal exemptLatefeeBalance;

    /**
     * 豁免手续费
     */
    @Column(name = "F_EXEMPT_SERVICEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免手续费", comments = "豁免手续费")
    private BigDecimal exemptServicefeeBalance;

    /**
     * 豁免合计
     */
    @Column(name = "F_EXEMPT_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "豁免合计", comments = "豁免合计")
    private BigDecimal exemptTotalBalance;

    private static final long serialVersionUID = 1474958522729L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getShouldPrincipalBalance() {
        return shouldPrincipalBalance;
    }

    public void setShouldPrincipalBalance(BigDecimal shouldPrincipalBalance) {
        this.shouldPrincipalBalance = shouldPrincipalBalance;
    }

    public BigDecimal getShouldInterestBalance() {
        return shouldInterestBalance;
    }

    public void setShouldInterestBalance(BigDecimal shouldInterestBalance) {
        this.shouldInterestBalance = shouldInterestBalance;
    }

    public BigDecimal getShouldServicefeeBalance() {
        return shouldServicefeeBalance;
    }

    public void setShouldServicefeeBalance(BigDecimal shouldServicefeeBalance) {
        this.shouldServicefeeBalance = shouldServicefeeBalance;
    }

    public BigDecimal getShouldManagementBalance() {
        return shouldManagementBalance;
    }

    public void setShouldManagementBalance(BigDecimal shouldManagementBalance) {
        this.shouldManagementBalance = shouldManagementBalance;
    }

    public BigDecimal getShouldPenaltyBalance() {
        return shouldPenaltyBalance;
    }

    public void setShouldPenaltyBalance(BigDecimal shouldPenaltyBalance) {
        this.shouldPenaltyBalance = shouldPenaltyBalance;
    }

    public BigDecimal getShouldLatefeeBalance() {
        return shouldLatefeeBalance;
    }

    public void setShouldLatefeeBalance(BigDecimal shouldLatefeeBalance) {
        this.shouldLatefeeBalance = shouldLatefeeBalance;
    }

    public BigDecimal getShouldTotalBalance() {
        return shouldTotalBalance;
    }

    public void setShouldTotalBalance(BigDecimal shouldTotalBalance) {
        this.shouldTotalBalance = shouldTotalBalance;
    }

    public BigDecimal getAlreadyPrincipalBalance() {
        return alreadyPrincipalBalance;
    }

    public void setAlreadyPrincipalBalance(BigDecimal alreadyPrincipalBalance) {
        this.alreadyPrincipalBalance = alreadyPrincipalBalance;
    }

    public BigDecimal getAlreadyInterestBalance() {
        return alreadyInterestBalance;
    }

    public void setAlreadyInterestBalance(BigDecimal alreadyInterestBalance) {
        this.alreadyInterestBalance = alreadyInterestBalance;
    }

    public BigDecimal getAlreadyManagementBalance() {
        return alreadyManagementBalance;
    }

    public void setAlreadyManagementBalance(BigDecimal alreadyManagementBalance) {
        this.alreadyManagementBalance = alreadyManagementBalance;
    }

    public BigDecimal getAlreadyPenaltyBalance() {
        return alreadyPenaltyBalance;
    }

    public void setAlreadyPenaltyBalance(BigDecimal alreadyPenaltyBalance) {
        this.alreadyPenaltyBalance = alreadyPenaltyBalance;
    }

    public BigDecimal getAlreadyLatefeeBalance() {
        return alreadyLatefeeBalance;
    }

    public void setAlreadyLatefeeBalance(BigDecimal alreadyLatefeeBalance) {
        this.alreadyLatefeeBalance = alreadyLatefeeBalance;
    }

    public BigDecimal getAlreadyServicefeeBalance() {
        return alreadyServicefeeBalance;
    }

    public void setAlreadyServicefeeBalance(BigDecimal alreadyServicefeeBalance) {
        this.alreadyServicefeeBalance = alreadyServicefeeBalance;
    }

    public BigDecimal getAlreadyTotalBalance() {
        return alreadyTotalBalance;
    }

    public void setAlreadyTotalBalance(BigDecimal alreadyTotalBalance) {
        this.alreadyTotalBalance = alreadyTotalBalance;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getManagementRatio() {
        return managementRatio;
    }

    public void setManagementRatio(BigDecimal managementRatio) {
        this.managementRatio = managementRatio;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(Integer periodUnit) {
        this.periodUnit = periodUnit;
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getSurplusPrincipalBalance() {
        return surplusPrincipalBalance;
    }

    public void setSurplusPrincipalBalance(BigDecimal surplusPrincipalBalance) {
        this.surplusPrincipalBalance = surplusPrincipalBalance;
    }

    public BigDecimal getSurplusInterestBalance() {
        return surplusInterestBalance;
    }

    public void setSurplusInterestBalance(BigDecimal surplusInterestBalance) {
        this.surplusInterestBalance = surplusInterestBalance;
    }

    public BigDecimal getSurplusManagementBalance() {
        return surplusManagementBalance;
    }

    public void setSurplusManagementBalance(BigDecimal surplusManagementBalance) {
        this.surplusManagementBalance = surplusManagementBalance;
    }

    public BigDecimal getSurplusPenaltyBalance() {
        return surplusPenaltyBalance;
    }

    public void setSurplusPenaltyBalance(BigDecimal surplusPenaltyBalance) {
        this.surplusPenaltyBalance = surplusPenaltyBalance;
    }

    public BigDecimal getSurplusLatefeeBalance() {
        return surplusLatefeeBalance;
    }

    public void setSurplusLatefeeBalance(BigDecimal surplusLatefeeBalance) {
        this.surplusLatefeeBalance = surplusLatefeeBalance;
    }

    public BigDecimal getSurplusServicefeeBalance() {
        return surplusServicefeeBalance;
    }

    public void setSurplusServicefeeBalance(BigDecimal surplusServicefeeBalance) {
        this.surplusServicefeeBalance = surplusServicefeeBalance;
    }

    public BigDecimal getSurplusTotalBalance() {
        return surplusTotalBalance;
    }

    public void setSurplusTotalBalance(BigDecimal surplusTotalBalance) {
        this.surplusTotalBalance = surplusTotalBalance;
    }

    public BigDecimal getExemptPrincipalBalance() {
        return exemptPrincipalBalance;
    }

    public void setExemptPrincipalBalance(BigDecimal exemptPrincipalBalance) {
        this.exemptPrincipalBalance = exemptPrincipalBalance;
    }

    public BigDecimal getExemptInterestBalance() {
        return exemptInterestBalance;
    }

    public void setExemptInterestBalance(BigDecimal exemptInterestBalance) {
        this.exemptInterestBalance = exemptInterestBalance;
    }

    public BigDecimal getExemptManagementBalance() {
        return exemptManagementBalance;
    }

    public void setExemptManagementBalance(BigDecimal exemptManagementBalance) {
        this.exemptManagementBalance = exemptManagementBalance;
    }

    public BigDecimal getExemptPenaltyBalance() {
        return exemptPenaltyBalance;
    }

    public void setExemptPenaltyBalance(BigDecimal exemptPenaltyBalance) {
        this.exemptPenaltyBalance = exemptPenaltyBalance;
    }

    public BigDecimal getExemptLatefeeBalance() {
        return exemptLatefeeBalance;
    }

    public void setExemptLatefeeBalance(BigDecimal exemptLatefeeBalance) {
        this.exemptLatefeeBalance = exemptLatefeeBalance;
    }

    public BigDecimal getExemptServicefeeBalance() {
        return exemptServicefeeBalance;
    }

    public void setExemptServicefeeBalance(BigDecimal exemptServicefeeBalance) {
        this.exemptServicefeeBalance = exemptServicefeeBalance;
    }

    public BigDecimal getExemptTotalBalance() {
        return exemptTotalBalance;
    }

    public void setExemptTotalBalance(BigDecimal exemptTotalBalance) {
        this.exemptTotalBalance = exemptTotalBalance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", startDate=").append(startDate);
        sb.append(", planDate=").append(planDate);
        sb.append(", payDate=").append(payDate);
        sb.append(", shouldPrincipalBalance=").append(shouldPrincipalBalance);
        sb.append(", shouldInterestBalance=").append(shouldInterestBalance);
        sb.append(", shouldServicefeeBalance=").append(shouldServicefeeBalance);
        sb.append(", shouldManagementBalance=").append(shouldManagementBalance);
        sb.append(", shouldPenaltyBalance=").append(shouldPenaltyBalance);
        sb.append(", shouldLatefeeBalance=").append(shouldLatefeeBalance);
        sb.append(", shouldTotalBalance=").append(shouldTotalBalance);
        sb.append(", alreadyPrincipalBalance=").append(alreadyPrincipalBalance);
        sb.append(", alreadyInterestBalance=").append(alreadyInterestBalance);
        sb.append(", alreadyManagementBalance=").append(alreadyManagementBalance);
        sb.append(", alreadyPenaltyBalance=").append(alreadyPenaltyBalance);
        sb.append(", alreadyLatefeeBalance=").append(alreadyLatefeeBalance);
        sb.append(", alreadyServicefeeBalance=").append(alreadyServicefeeBalance);
        sb.append(", alreadyTotalBalance=").append(alreadyTotalBalance);
        sb.append(", ratio=").append(ratio);
        sb.append(", managementRatio=").append(managementRatio);
        sb.append(", term=").append(term);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", overdueDays=").append(overdueDays);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", period=").append(period);
        sb.append(", periodUnit=").append(periodUnit);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", version=").append(version);
        sb.append(", surplusPrincipalBalance=").append(surplusPrincipalBalance);
        sb.append(", surplusInterestBalance=").append(surplusInterestBalance);
        sb.append(", surplusManagementBalance=").append(surplusManagementBalance);
        sb.append(", surplusPenaltyBalance=").append(surplusPenaltyBalance);
        sb.append(", surplusLatefeeBalance=").append(surplusLatefeeBalance);
        sb.append(", surplusServicefeeBalance=").append(surplusServicefeeBalance);
        sb.append(", surplusTotalBalance=").append(surplusTotalBalance);
        sb.append(", exemptPrincipalBalance=").append(exemptPrincipalBalance);
        sb.append(", exemptInterestBalance=").append(exemptInterestBalance);
        sb.append(", exemptManagementBalance=").append(exemptManagementBalance);
        sb.append(", exemptPenaltyBalance=").append(exemptPenaltyBalance);
        sb.append(", exemptLatefeeBalance=").append(exemptLatefeeBalance);
        sb.append(", exemptServicefeeBalance=").append(exemptServicefeeBalance);
        sb.append(", exemptTotalBalance=").append(exemptTotalBalance);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ScfPayPlan other = (ScfPayPlan) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null
                        : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null
                        : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null
                        : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getStartDate() == null ? other.getStartDate() == null
                        : this.getStartDate().equals(other.getStartDate()))
                && (this.getPlanDate() == null ? other.getPlanDate() == null
                        : this.getPlanDate().equals(other.getPlanDate()))
                && (this.getPayDate() == null ? other.getPayDate() == null
                        : this.getPayDate().equals(other.getPayDate()))
                && (this.getShouldPrincipalBalance() == null ? other.getShouldPrincipalBalance() == null
                        : this.getShouldPrincipalBalance().equals(other.getShouldPrincipalBalance()))
                && (this.getShouldInterestBalance() == null ? other.getShouldInterestBalance() == null
                        : this.getShouldInterestBalance().equals(other.getShouldInterestBalance()))
                && (this.getShouldServicefeeBalance() == null ? other.getShouldServicefeeBalance() == null
                        : this.getShouldServicefeeBalance().equals(other.getShouldServicefeeBalance()))
                && (this.getShouldManagementBalance() == null ? other.getShouldManagementBalance() == null
                        : this.getShouldManagementBalance().equals(other.getShouldManagementBalance()))
                && (this.getShouldPenaltyBalance() == null ? other.getShouldPenaltyBalance() == null
                        : this.getShouldPenaltyBalance().equals(other.getShouldPenaltyBalance()))
                && (this.getShouldLatefeeBalance() == null ? other.getShouldLatefeeBalance() == null
                        : this.getShouldLatefeeBalance().equals(other.getShouldLatefeeBalance()))
                && (this.getShouldTotalBalance() == null ? other.getShouldTotalBalance() == null
                        : this.getShouldTotalBalance().equals(other.getShouldTotalBalance()))
                && (this.getAlreadyPrincipalBalance() == null ? other.getAlreadyPrincipalBalance() == null
                        : this.getAlreadyPrincipalBalance().equals(other.getAlreadyPrincipalBalance()))
                && (this.getAlreadyInterestBalance() == null ? other.getAlreadyInterestBalance() == null
                        : this.getAlreadyInterestBalance().equals(other.getAlreadyInterestBalance()))
                && (this.getAlreadyManagementBalance() == null ? other.getAlreadyManagementBalance() == null
                        : this.getAlreadyManagementBalance().equals(other.getAlreadyManagementBalance()))
                && (this.getAlreadyPenaltyBalance() == null ? other.getAlreadyPenaltyBalance() == null
                        : this.getAlreadyPenaltyBalance().equals(other.getAlreadyPenaltyBalance()))
                && (this.getAlreadyLatefeeBalance() == null ? other.getAlreadyLatefeeBalance() == null
                        : this.getAlreadyLatefeeBalance().equals(other.getAlreadyLatefeeBalance()))
                && (this.getAlreadyServicefeeBalance() == null ? other.getAlreadyServicefeeBalance() == null
                        : this.getAlreadyServicefeeBalance().equals(other.getAlreadyServicefeeBalance()))
                && (this.getAlreadyTotalBalance() == null ? other.getAlreadyTotalBalance() == null
                        : this.getAlreadyTotalBalance().equals(other.getAlreadyTotalBalance()))
                && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
                && (this.getManagementRatio() == null ? other.getManagementRatio() == null
                        : this.getManagementRatio().equals(other.getManagementRatio()))
                && (this.getTerm() == null ? other.getTerm() == null : this.getTerm().equals(other.getTerm()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getOverdueDays() == null ? other.getOverdueDays() == null
                        : this.getOverdueDays().equals(other.getOverdueDays()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
                && (this.getPeriodUnit() == null ? other.getPeriodUnit() == null
                        : this.getPeriodUnit().equals(other.getPeriodUnit()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null
                        : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null
                        : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null
                        : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null
                        : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null
                        : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null
                        : this.getModiTime().equals(other.getModiTime()))
                && (this.getVersion() == null ? other.getVersion() == null
                        : this.getVersion().equals(other.getVersion()))
                && (this.getSurplusPrincipalBalance() == null ? other.getSurplusPrincipalBalance() == null
                        : this.getSurplusPrincipalBalance().equals(other.getSurplusPrincipalBalance()))
                && (this.getSurplusInterestBalance() == null ? other.getSurplusInterestBalance() == null
                        : this.getSurplusInterestBalance().equals(other.getSurplusInterestBalance()))
                && (this.getSurplusManagementBalance() == null ? other.getSurplusManagementBalance() == null
                        : this.getSurplusManagementBalance().equals(other.getSurplusManagementBalance()))
                && (this.getSurplusPenaltyBalance() == null ? other.getSurplusPenaltyBalance() == null
                        : this.getSurplusPenaltyBalance().equals(other.getSurplusPenaltyBalance()))
                && (this.getSurplusLatefeeBalance() == null ? other.getSurplusLatefeeBalance() == null
                        : this.getSurplusLatefeeBalance().equals(other.getSurplusLatefeeBalance()))
                && (this.getSurplusServicefeeBalance() == null ? other.getSurplusServicefeeBalance() == null
                        : this.getSurplusServicefeeBalance().equals(other.getSurplusServicefeeBalance()))
                && (this.getSurplusTotalBalance() == null ? other.getSurplusTotalBalance() == null
                        : this.getSurplusTotalBalance().equals(other.getSurplusTotalBalance()))
                && (this.getExemptPrincipalBalance() == null ? other.getExemptPrincipalBalance() == null
                        : this.getExemptPrincipalBalance().equals(other.getExemptPrincipalBalance()))
                && (this.getExemptInterestBalance() == null ? other.getExemptInterestBalance() == null
                        : this.getExemptInterestBalance().equals(other.getExemptInterestBalance()))
                && (this.getExemptManagementBalance() == null ? other.getExemptManagementBalance() == null
                        : this.getExemptManagementBalance().equals(other.getExemptManagementBalance()))
                && (this.getExemptPenaltyBalance() == null ? other.getExemptPenaltyBalance() == null
                        : this.getExemptPenaltyBalance().equals(other.getExemptPenaltyBalance()))
                && (this.getExemptLatefeeBalance() == null ? other.getExemptLatefeeBalance() == null
                        : this.getExemptLatefeeBalance().equals(other.getExemptLatefeeBalance()))
                && (this.getExemptServicefeeBalance() == null ? other.getExemptServicefeeBalance() == null
                        : this.getExemptServicefeeBalance().equals(other.getExemptServicefeeBalance()))
                && (this.getExemptTotalBalance() == null ? other.getExemptTotalBalance() == null
                        : this.getExemptTotalBalance().equals(other.getExemptTotalBalance()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getPlanDate() == null) ? 0 : getPlanDate().hashCode());
        result = prime * result + ((getPayDate() == null) ? 0 : getPayDate().hashCode());
        result = prime * result + ((getShouldPrincipalBalance() == null) ? 0 : getShouldPrincipalBalance().hashCode());
        result = prime * result + ((getShouldInterestBalance() == null) ? 0 : getShouldInterestBalance().hashCode());
        result = prime * result
                + ((getShouldServicefeeBalance() == null) ? 0 : getShouldServicefeeBalance().hashCode());
        result = prime * result
                + ((getShouldManagementBalance() == null) ? 0 : getShouldManagementBalance().hashCode());
        result = prime * result + ((getShouldPenaltyBalance() == null) ? 0 : getShouldPenaltyBalance().hashCode());
        result = prime * result + ((getShouldLatefeeBalance() == null) ? 0 : getShouldLatefeeBalance().hashCode());
        result = prime * result + ((getShouldTotalBalance() == null) ? 0 : getShouldTotalBalance().hashCode());
        result = prime * result
                + ((getAlreadyPrincipalBalance() == null) ? 0 : getAlreadyPrincipalBalance().hashCode());
        result = prime * result + ((getAlreadyInterestBalance() == null) ? 0 : getAlreadyInterestBalance().hashCode());
        result = prime * result
                + ((getAlreadyManagementBalance() == null) ? 0 : getAlreadyManagementBalance().hashCode());
        result = prime * result + ((getAlreadyPenaltyBalance() == null) ? 0 : getAlreadyPenaltyBalance().hashCode());
        result = prime * result + ((getAlreadyLatefeeBalance() == null) ? 0 : getAlreadyLatefeeBalance().hashCode());
        result = prime * result
                + ((getAlreadyServicefeeBalance() == null) ? 0 : getAlreadyServicefeeBalance().hashCode());
        result = prime * result + ((getAlreadyTotalBalance() == null) ? 0 : getAlreadyTotalBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getManagementRatio() == null) ? 0 : getManagementRatio().hashCode());
        result = prime * result + ((getTerm() == null) ? 0 : getTerm().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getOverdueDays() == null) ? 0 : getOverdueDays().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getPeriodUnit() == null) ? 0 : getPeriodUnit().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result
                + ((getSurplusPrincipalBalance() == null) ? 0 : getSurplusPrincipalBalance().hashCode());
        result = prime * result + ((getSurplusInterestBalance() == null) ? 0 : getSurplusInterestBalance().hashCode());
        result = prime * result
                + ((getSurplusManagementBalance() == null) ? 0 : getSurplusManagementBalance().hashCode());
        result = prime * result + ((getSurplusPenaltyBalance() == null) ? 0 : getSurplusPenaltyBalance().hashCode());
        result = prime * result + ((getSurplusLatefeeBalance() == null) ? 0 : getSurplusLatefeeBalance().hashCode());
        result = prime * result
                + ((getSurplusServicefeeBalance() == null) ? 0 : getSurplusServicefeeBalance().hashCode());
        result = prime * result + ((getSurplusTotalBalance() == null) ? 0 : getSurplusTotalBalance().hashCode());
        result = prime * result + ((getExemptPrincipalBalance() == null) ? 0 : getExemptPrincipalBalance().hashCode());
        result = prime * result + ((getExemptInterestBalance() == null) ? 0 : getExemptInterestBalance().hashCode());
        result = prime * result
                + ((getExemptManagementBalance() == null) ? 0 : getExemptManagementBalance().hashCode());
        result = prime * result + ((getExemptPenaltyBalance() == null) ? 0 : getExemptPenaltyBalance().hashCode());
        result = prime * result + ((getExemptLatefeeBalance() == null) ? 0 : getExemptLatefeeBalance().hashCode());
        result = prime * result
                + ((getExemptServicefeeBalance() == null) ? 0 : getExemptServicefeeBalance().hashCode());
        result = prime * result + ((getExemptTotalBalance() == null) ? 0 : getExemptTotalBalance().hashCode());
        return result;
    }

    public void init(ScfPayPlan anPlan) {
        this.id = SerialGenerator.getLongValue("ScfPayPlan.id");
        this.businStatus = "0";
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        fillBalance(anPlan);
    }

    public void initModify(ScfPayPlan anPlan, Long anId) {
        anPlan.id = anId;
        anPlan.modiOperId = UserUtils.getOperatorInfo().getId();
        anPlan.modiOperName = UserUtils.getUserName();
        anPlan.modiDate = BetterDateUtils.getNumDate();
        anPlan.modiTime = BetterDateUtils.getNumTime();
        // fillBalance(anPlan);
    }

    public void initAutoModify(ScfPayPlan anPlan, Long anId) {
        // 定时任务取不到 当前登录用户，故使用登记用户
        anPlan.id = anId;
        anPlan.modiOperId = this.regOperId;
        anPlan.modiOperName = this.regOperName;
        anPlan.modiDate = BetterDateUtils.getNumDate();
        anPlan.modiTime = BetterDateUtils.getNumTime();
        // fillBalance(anPlan);
    }

    private void fillBalance(ScfPayPlan anPlan) {
        anPlan.shouldInterestBalance = MathExtend.defaultValue(anPlan.shouldInterestBalance, BigDecimal.ZERO);
        anPlan.shouldLatefeeBalance = MathExtend.defaultValue(anPlan.shouldLatefeeBalance, BigDecimal.ZERO);
        anPlan.shouldManagementBalance = MathExtend.defaultValue(anPlan.shouldManagementBalance, BigDecimal.ZERO);
        anPlan.shouldPenaltyBalance = MathExtend.defaultValue(anPlan.shouldPenaltyBalance, BigDecimal.ZERO);
        anPlan.shouldPrincipalBalance = MathExtend.defaultValue(anPlan.shouldPrincipalBalance, BigDecimal.ZERO);
        anPlan.shouldServicefeeBalance = MathExtend.defaultValue(anPlan.shouldServicefeeBalance, BigDecimal.ZERO);
        anPlan.shouldTotalBalance = MathExtend.defaultValue(anPlan.shouldTotalBalance, BigDecimal.ZERO);

        anPlan.surplusInterestBalance = MathExtend.defaultValue(anPlan.surplusInterestBalance, BigDecimal.ZERO);
        anPlan.surplusLatefeeBalance = MathExtend.defaultValue(anPlan.surplusLatefeeBalance, BigDecimal.ZERO);
        anPlan.surplusManagementBalance = MathExtend.defaultValue(anPlan.surplusManagementBalance, BigDecimal.ZERO);
        anPlan.surplusPenaltyBalance = MathExtend.defaultValue(anPlan.surplusPenaltyBalance, BigDecimal.ZERO);
        anPlan.surplusPrincipalBalance = MathExtend.defaultValue(anPlan.surplusPrincipalBalance, BigDecimal.ZERO);
        anPlan.surplusServicefeeBalance = MathExtend.defaultValue(anPlan.surplusServicefeeBalance, BigDecimal.ZERO);
        anPlan.surplusTotalBalance = MathExtend.defaultValue(anPlan.surplusTotalBalance, BigDecimal.ZERO);

        anPlan.alreadyInterestBalance = MathExtend.defaultValue(anPlan.alreadyInterestBalance, BigDecimal.ZERO);
        anPlan.alreadyLatefeeBalance = MathExtend.defaultValue(anPlan.alreadyLatefeeBalance, BigDecimal.ZERO);
        anPlan.alreadyManagementBalance = MathExtend.defaultValue(anPlan.alreadyManagementBalance, BigDecimal.ZERO);
        anPlan.alreadyPenaltyBalance = MathExtend.defaultValue(anPlan.alreadyPenaltyBalance, BigDecimal.ZERO);
        anPlan.alreadyPrincipalBalance = MathExtend.defaultValue(anPlan.alreadyPrincipalBalance, BigDecimal.ZERO);
        anPlan.alreadyServicefeeBalance = MathExtend.defaultValue(anPlan.alreadyServicefeeBalance, BigDecimal.ZERO);
        anPlan.alreadyTotalBalance = MathExtend.defaultValue(anPlan.alreadyTotalBalance, BigDecimal.ZERO);

        anPlan.exemptInterestBalance = MathExtend.defaultValue(anPlan.exemptInterestBalance, BigDecimal.ZERO);
        anPlan.exemptLatefeeBalance = MathExtend.defaultValue(anPlan.exemptLatefeeBalance, BigDecimal.ZERO);
        anPlan.exemptManagementBalance = MathExtend.defaultValue(anPlan.exemptManagementBalance, BigDecimal.ZERO);
        anPlan.exemptPenaltyBalance = MathExtend.defaultValue(anPlan.exemptPenaltyBalance, BigDecimal.ZERO);
        anPlan.exemptPrincipalBalance = MathExtend.defaultValue(anPlan.exemptPrincipalBalance, BigDecimal.ZERO);
        anPlan.exemptServicefeeBalance = MathExtend.defaultValue(anPlan.exemptServicefeeBalance, BigDecimal.ZERO);
        anPlan.exemptTotalBalance = MathExtend.defaultValue(anPlan.exemptTotalBalance, BigDecimal.ZERO);

        anPlan.ratio = MathExtend.defaultValue(anPlan.ratio, BigDecimal.ZERO);
        anPlan.managementRatio = MathExtend.defaultValue(anPlan.managementRatio, BigDecimal.ZERO);
        anPlan.overdueDays = (null == anPlan.overdueDays) ? 0 : anPlan.overdueDays;
    }

    @Transient
    private String custName;
    @Transient
    private String factorName;
    @Transient
    private String actualDate;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

}