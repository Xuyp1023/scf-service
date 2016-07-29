package com.betterjr.modules.loan.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_pay_plan")
public class ScfPayPlan implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long id;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 申请编号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请编号", comments = "申请编号")
    private String requestNo;

    /**
     * 计划还款日期
     */
    @Column(name = "D_PLAN_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="计划还款日期", comments = "计划还款日期")
    private String planDate;

    /**
     * 应还本金
     */
    @Column(name = "F_SHOULD_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还本金", comments = "应还本金")
    private BigDecimal shouldPrincipalBalance;

    /**
     * 应还利息
     */
    @Column(name = "F_SHOULD_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还利息", comments = "应还利息")
    private BigDecimal shouldInterestBalance;

    /**
     * 应还手续费
     */
    @Column(name = "F_SHOULD_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还手续费", comments = "应还手续费")
    private BigDecimal shouldServicefeeBalance;

    /**
     * 应还管理费
     */
    @Column(name = "F_SHOULD_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还管理费", comments = "应还管理费")
    private BigDecimal shouldManagementBalance;

    /**
     * 应还罚息
     */
    @Column(name = "F_SHOULD_PENALTY_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还罚息", comments = "应还罚息")
    private BigDecimal shouldPenaltyBalance;

    /**
     * 应还滞纳金
     */
    @Column(name = "F_SHOULD_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还滞纳金", comments = "应还滞纳金")
    private BigDecimal shouldLatefeeBalance;

    /**
     * 应还其它费用
     */
    @Column(name = "F_SHOULD_OTHER_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还其它费用", comments = "应还其它费用")
    private BigDecimal shouldOtherBalance;

    /**
     * 应还总额
     */
    @Column(name = "F_SHOULD_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="应还总额", comments = "应还总额")
    private BigDecimal shouldTotalBalance;

    /**
     * 已还本金
     */
    @Column(name = "F_ALREADY_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还本金", comments = "已还本金")
    private BigDecimal alreadyPrincipalBalance;

    /**
     * 已还利息
     */
    @Column(name = "F_ALREADY_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还利息", comments = "已还利息")
    private BigDecimal alreadyInterestBalance;

    /**
     * 已还管理费
     */
    @Column(name = "F_ALREADY_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还管理费", comments = "已还管理费")
    private BigDecimal alreadyManagementBalance;

    /**
     * 已还罚息
     */
    @Column(name = "F_ALREADY_PENALTY_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还罚息", comments = "已还罚息")
    private BigDecimal alreadyPenaltyBalance;

    /**
     * 已还滞纳金
     */
    @Column(name = "F_ALREADY_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还滞纳金", comments = "已还滞纳金")
    private BigDecimal alreadyLatefeeBalance;

    /**
     * 已还手续费
     */
    @Column(name = "F_ALREADY_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还手续费", comments = "已还手续费")
    private BigDecimal alreadyServicefeeBalance;

    /**
     * 已还其它费用
     */
    @Column(name = "F_ALREADY_OTHER_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还其它费用", comments = "已还其它费用")
    private BigDecimal alreadyOtherBalance;

    /**
     * 已还合计
     */
    @Column(name = "F_ALREADY_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="已还合计", comments = "已还合计")
    private BigDecimal alreadyTotalBalance;

    /**
     * 期数
     */
    @Column(name = "N_TERM",  columnDefinition="INT" )
    @MetaData( value="期数", comments = "期数")
    private Integer term;

    /**
     * 状态：1：未还，2：结清，3：逾期，4：提前还款，5：展期，6：坏账
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态：1：未还", comments = "状态：1：未还，2：结清，3：逾期，4：提前还款，5：展期，6：坏账")
    private String businStatus;

    /**
     * 逾期天数
     */
    @Column(name = "L_OVERDUE_DAYS",  columnDefinition="INT" )
    @MetaData( value="逾期天数", comments = "逾期天数")
    private Integer overdueDays;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    @Column(name = "L_REG_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiOperName;

    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiDate;

    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiTime;

    @Column(name = "N_VERSION",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long version;

    /**
     * 未还本金
     */
    @Column(name = "F_SURPLUS_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还本金", comments = "未还本金")
    private BigDecimal surplusPrincipalBalance;

    /**
     * 未还利息
     */
    @Column(name = "F_SURPLUS_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还利息", comments = "未还利息")
    private BigDecimal surplusInterestBalance;

    /**
     * 未还管理费
     */
    @Column(name = "F_SURPLUS_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还管理费", comments = "未还管理费")
    private BigDecimal surplusManagementBalance;

    /**
     * 未还罚息
     */
    @Column(name = "F_SURPLUS_PENALTY_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还罚息", comments = "未还罚息")
    private BigDecimal surplusPenaltyBalance;

    /**
     * 未还滞纳金
     */
    @Column(name = "F_SURPLUS_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还滞纳金", comments = "未还滞纳金")
    private BigDecimal surplusLatefeeBalance;

    /**
     * 未还手续费
     */
    @Column(name = "F_SURPLUS_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还手续费", comments = "未还手续费")
    private BigDecimal surplusServicefeeBalance;

    /**
     * 未还其它费用
     */
    @Column(name = "F_SURPLUS_OTHER_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还其它费用", comments = "未还其它费用")
    private BigDecimal surplusOtherBalance;

    /**
     * 未还合计
     */
    @Column(name = "F_SURPLUS_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="未还合计", comments = "未还合计")
    private BigDecimal surplusTotalBalance;

    private static final long serialVersionUID = 1469504768790L;

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

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
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

    public BigDecimal getShouldOtherBalance() {
        return shouldOtherBalance;
    }

    public void setShouldOtherBalance(BigDecimal shouldOtherBalance) {
        this.shouldOtherBalance = shouldOtherBalance;
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

    public BigDecimal getAlreadyOtherBalance() {
        return alreadyOtherBalance;
    }

    public void setAlreadyOtherBalance(BigDecimal alreadyOtherBalance) {
        this.alreadyOtherBalance = alreadyOtherBalance;
    }

    public BigDecimal getAlreadyTotalBalance() {
        return alreadyTotalBalance;
    }

    public void setAlreadyTotalBalance(BigDecimal alreadyTotalBalance) {
        this.alreadyTotalBalance = alreadyTotalBalance;
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

    public BigDecimal getSurplusOtherBalance() {
        return surplusOtherBalance;
    }

    public void setSurplusOtherBalance(BigDecimal surplusOtherBalance) {
        this.surplusOtherBalance = surplusOtherBalance;
    }

    public BigDecimal getSurplusTotalBalance() {
        return surplusTotalBalance;
    }

    public void setSurplusTotalBalance(BigDecimal surplusTotalBalance) {
        this.surplusTotalBalance = surplusTotalBalance;
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
        sb.append(", planDate=").append(planDate);
        sb.append(", shouldPrincipalBalance=").append(shouldPrincipalBalance);
        sb.append(", shouldInterestBalance=").append(shouldInterestBalance);
        sb.append(", shouldServicefeeBalance=").append(shouldServicefeeBalance);
        sb.append(", shouldManagementBalance=").append(shouldManagementBalance);
        sb.append(", shouldPenaltyBalance=").append(shouldPenaltyBalance);
        sb.append(", shouldLatefeeBalance=").append(shouldLatefeeBalance);
        sb.append(", shouldOtherBalance=").append(shouldOtherBalance);
        sb.append(", shouldTotalBalance=").append(shouldTotalBalance);
        sb.append(", alreadyPrincipalBalance=").append(alreadyPrincipalBalance);
        sb.append(", alreadyInterestBalance=").append(alreadyInterestBalance);
        sb.append(", alreadyManagementBalance=").append(alreadyManagementBalance);
        sb.append(", alreadyPenaltyBalance=").append(alreadyPenaltyBalance);
        sb.append(", alreadyLatefeeBalance=").append(alreadyLatefeeBalance);
        sb.append(", alreadyServicefeeBalance=").append(alreadyServicefeeBalance);
        sb.append(", alreadyOtherBalance=").append(alreadyOtherBalance);
        sb.append(", alreadyTotalBalance=").append(alreadyTotalBalance);
        sb.append(", term=").append(term);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", overdueDays=").append(overdueDays);
        sb.append(", operOrg=").append(operOrg);
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
        sb.append(", surplusOtherBalance=").append(surplusOtherBalance);
        sb.append(", surplusTotalBalance=").append(surplusTotalBalance);
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
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getPlanDate() == null ? other.getPlanDate() == null : this.getPlanDate().equals(other.getPlanDate()))
            && (this.getShouldPrincipalBalance() == null ? other.getShouldPrincipalBalance() == null : this.getShouldPrincipalBalance().equals(other.getShouldPrincipalBalance()))
            && (this.getShouldInterestBalance() == null ? other.getShouldInterestBalance() == null : this.getShouldInterestBalance().equals(other.getShouldInterestBalance()))
            && (this.getShouldServicefeeBalance() == null ? other.getShouldServicefeeBalance() == null : this.getShouldServicefeeBalance().equals(other.getShouldServicefeeBalance()))
            && (this.getShouldManagementBalance() == null ? other.getShouldManagementBalance() == null : this.getShouldManagementBalance().equals(other.getShouldManagementBalance()))
            && (this.getShouldPenaltyBalance() == null ? other.getShouldPenaltyBalance() == null : this.getShouldPenaltyBalance().equals(other.getShouldPenaltyBalance()))
            && (this.getShouldLatefeeBalance() == null ? other.getShouldLatefeeBalance() == null : this.getShouldLatefeeBalance().equals(other.getShouldLatefeeBalance()))
            && (this.getShouldOtherBalance() == null ? other.getShouldOtherBalance() == null : this.getShouldOtherBalance().equals(other.getShouldOtherBalance()))
            && (this.getShouldTotalBalance() == null ? other.getShouldTotalBalance() == null : this.getShouldTotalBalance().equals(other.getShouldTotalBalance()))
            && (this.getAlreadyPrincipalBalance() == null ? other.getAlreadyPrincipalBalance() == null : this.getAlreadyPrincipalBalance().equals(other.getAlreadyPrincipalBalance()))
            && (this.getAlreadyInterestBalance() == null ? other.getAlreadyInterestBalance() == null : this.getAlreadyInterestBalance().equals(other.getAlreadyInterestBalance()))
            && (this.getAlreadyManagementBalance() == null ? other.getAlreadyManagementBalance() == null : this.getAlreadyManagementBalance().equals(other.getAlreadyManagementBalance()))
            && (this.getAlreadyPenaltyBalance() == null ? other.getAlreadyPenaltyBalance() == null : this.getAlreadyPenaltyBalance().equals(other.getAlreadyPenaltyBalance()))
            && (this.getAlreadyLatefeeBalance() == null ? other.getAlreadyLatefeeBalance() == null : this.getAlreadyLatefeeBalance().equals(other.getAlreadyLatefeeBalance()))
            && (this.getAlreadyServicefeeBalance() == null ? other.getAlreadyServicefeeBalance() == null : this.getAlreadyServicefeeBalance().equals(other.getAlreadyServicefeeBalance()))
            && (this.getAlreadyOtherBalance() == null ? other.getAlreadyOtherBalance() == null : this.getAlreadyOtherBalance().equals(other.getAlreadyOtherBalance()))
            && (this.getAlreadyTotalBalance() == null ? other.getAlreadyTotalBalance() == null : this.getAlreadyTotalBalance().equals(other.getAlreadyTotalBalance()))
            && (this.getTerm() == null ? other.getTerm() == null : this.getTerm().equals(other.getTerm()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getOverdueDays() == null ? other.getOverdueDays() == null : this.getOverdueDays().equals(other.getOverdueDays()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSurplusPrincipalBalance() == null ? other.getSurplusPrincipalBalance() == null : this.getSurplusPrincipalBalance().equals(other.getSurplusPrincipalBalance()))
            && (this.getSurplusInterestBalance() == null ? other.getSurplusInterestBalance() == null : this.getSurplusInterestBalance().equals(other.getSurplusInterestBalance()))
            && (this.getSurplusManagementBalance() == null ? other.getSurplusManagementBalance() == null : this.getSurplusManagementBalance().equals(other.getSurplusManagementBalance()))
            && (this.getSurplusPenaltyBalance() == null ? other.getSurplusPenaltyBalance() == null : this.getSurplusPenaltyBalance().equals(other.getSurplusPenaltyBalance()))
            && (this.getSurplusLatefeeBalance() == null ? other.getSurplusLatefeeBalance() == null : this.getSurplusLatefeeBalance().equals(other.getSurplusLatefeeBalance()))
            && (this.getSurplusServicefeeBalance() == null ? other.getSurplusServicefeeBalance() == null : this.getSurplusServicefeeBalance().equals(other.getSurplusServicefeeBalance()))
            && (this.getSurplusOtherBalance() == null ? other.getSurplusOtherBalance() == null : this.getSurplusOtherBalance().equals(other.getSurplusOtherBalance()))
            && (this.getSurplusTotalBalance() == null ? other.getSurplusTotalBalance() == null : this.getSurplusTotalBalance().equals(other.getSurplusTotalBalance()));
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
        result = prime * result + ((getPlanDate() == null) ? 0 : getPlanDate().hashCode());
        result = prime * result + ((getShouldPrincipalBalance() == null) ? 0 : getShouldPrincipalBalance().hashCode());
        result = prime * result + ((getShouldInterestBalance() == null) ? 0 : getShouldInterestBalance().hashCode());
        result = prime * result + ((getShouldServicefeeBalance() == null) ? 0 : getShouldServicefeeBalance().hashCode());
        result = prime * result + ((getShouldManagementBalance() == null) ? 0 : getShouldManagementBalance().hashCode());
        result = prime * result + ((getShouldPenaltyBalance() == null) ? 0 : getShouldPenaltyBalance().hashCode());
        result = prime * result + ((getShouldLatefeeBalance() == null) ? 0 : getShouldLatefeeBalance().hashCode());
        result = prime * result + ((getShouldOtherBalance() == null) ? 0 : getShouldOtherBalance().hashCode());
        result = prime * result + ((getShouldTotalBalance() == null) ? 0 : getShouldTotalBalance().hashCode());
        result = prime * result + ((getAlreadyPrincipalBalance() == null) ? 0 : getAlreadyPrincipalBalance().hashCode());
        result = prime * result + ((getAlreadyInterestBalance() == null) ? 0 : getAlreadyInterestBalance().hashCode());
        result = prime * result + ((getAlreadyManagementBalance() == null) ? 0 : getAlreadyManagementBalance().hashCode());
        result = prime * result + ((getAlreadyPenaltyBalance() == null) ? 0 : getAlreadyPenaltyBalance().hashCode());
        result = prime * result + ((getAlreadyLatefeeBalance() == null) ? 0 : getAlreadyLatefeeBalance().hashCode());
        result = prime * result + ((getAlreadyServicefeeBalance() == null) ? 0 : getAlreadyServicefeeBalance().hashCode());
        result = prime * result + ((getAlreadyOtherBalance() == null) ? 0 : getAlreadyOtherBalance().hashCode());
        result = prime * result + ((getAlreadyTotalBalance() == null) ? 0 : getAlreadyTotalBalance().hashCode());
        result = prime * result + ((getTerm() == null) ? 0 : getTerm().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getOverdueDays() == null) ? 0 : getOverdueDays().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSurplusPrincipalBalance() == null) ? 0 : getSurplusPrincipalBalance().hashCode());
        result = prime * result + ((getSurplusInterestBalance() == null) ? 0 : getSurplusInterestBalance().hashCode());
        result = prime * result + ((getSurplusManagementBalance() == null) ? 0 : getSurplusManagementBalance().hashCode());
        result = prime * result + ((getSurplusPenaltyBalance() == null) ? 0 : getSurplusPenaltyBalance().hashCode());
        result = prime * result + ((getSurplusLatefeeBalance() == null) ? 0 : getSurplusLatefeeBalance().hashCode());
        result = prime * result + ((getSurplusServicefeeBalance() == null) ? 0 : getSurplusServicefeeBalance().hashCode());
        result = prime * result + ((getSurplusOtherBalance() == null) ? 0 : getSurplusOtherBalance().hashCode());
        result = prime * result + ((getSurplusTotalBalance() == null) ? 0 : getSurplusTotalBalance().hashCode());
        return result;
    }

    public void init() {
        this.id = SerialGenerator.getLongValue("ScfPayPlan.id");
        this.businStatus = "0";
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
    }

    public void initModify() {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }
}