package com.betterjr.modules.loan.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_extension")
public class ScfExtension implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    private Long id;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO", columnDefinition = "BIGINT")
    @MetaData(value = "保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 展期企业编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "BIGINT")
    @MetaData(value = "展期企业编号", comments = "展期企业编号")
    private Long custNo;

    /**
     * 还款计划id
     */
    @Column(name = "L_PAY_PLAN_ID", columnDefinition = "BIGINT")
    @MetaData(value = "还款计划id", comments = "还款计划id")
    private Long payPlanId;

    /**
     * 新的还款计划id
     */
    @Column(name = "L_NEW_PAY_PLAN_ID", columnDefinition = "BIGINT")
    @MetaData(value = "新的还款计划id", comments = "新的还款计划id")
    private Long newPayPlanId;

    /**
     * 申请单编号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请单编号", comments = "申请单编号")
    private String requestNo;

    /**
     * 贷款余额
     */
    @Column(name = "F_LOAN_BALANCE", columnDefinition = "BIGINT")
    @MetaData(value = "贷款余额", comments = "贷款余额")
    private Long loanBalance;

    /**
     * 展期金额
     */
    @Column(name = "F_EXTENSION_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期金额", comments = "展期金额")
    private BigDecimal extensionBalance;

    /**
     * 展期时还款总额
     */
    @Column(name = "F_PAY_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款总额", comments = "展期时还款总额")
    private BigDecimal payTotalBalance;

    /**
     * 展期时还款本金
     */
    @Column(name = "F_PAY_PRINCIPAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款本金", comments = "展期时还款本金")
    private BigDecimal payPrincipalBalance;

    /**
     * 展期时还款利息
     */
    @Column(name = "F_PAY_INTEREST_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款利息", comments = "展期时还款利息")
    private BigDecimal payInterestBalance;

    /**
     * 展期时还款管理费
     */
    @Column(name = "F_PAY_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款管理费", comments = "展期时还款管理费")
    private BigDecimal payManagementBalance;

    /**
     * 展期时还款罚息
     */
    @Column(name = "F_PAY_PENALTY_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款罚息", comments = "展期时还款罚息")
    private BigDecimal payPenaltyBalance;

    /**
     * 展期时还款滞纳金
     */
    @Column(name = "F_PAY_LATEFEE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "展期时还款滞纳金", comments = "展期时还款滞纳金")
    private BigDecimal payLatefeeBalance;

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
     * 应还管理费
     */
    @Column(name = "F_SHOULD_MANAGEMENT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还管理费", comments = "应还管理费")
    private BigDecimal shouldManagementBalance;

    /**
     * 应还总额
     */
    @Column(name = "F_SHOULD_TOTAL_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "应还总额", comments = "应还总额")
    private BigDecimal shouldTotalBalance;

    /**
     * 期限
     */
    @Column(name = "N_PERIOD", columnDefinition = "INT")
    @MetaData(value = "期限", comments = "期限")
    private Integer period;

    /**
     * 期限单位：1：日，2月
     */
    @Column(name = "N_PERIOD_UNIT", columnDefinition = "INT")
    @MetaData(value = "期限单位：1：日", comments = "期限单位：1：日，2月")
    private Integer periodUnit;

    /**
     * 展期开始日期
     */
    @Column(name = "D_START_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "展期开始日期", comments = "展期开始日期")
    private String startDate;

    /**
     * 展期结束日期
     */
    @Column(name = "D_END_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "展期结束日期", comments = "展期结束日期")
    private String endDate;

    /**
     * 展期利率
     */
    @Column(name = "F_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "展期利率", comments = "展期利率")
    private BigDecimal ratio;

    /**
     * 管理费利率
     */
    @Column(name = "F_MANAGEMENT_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "管理费利率", comments = "管理费利率")
    private BigDecimal managementRatio;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

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

    private static final long serialVersionUID = 1472112316913L;

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

    public Long getPayPlanId() {
        return payPlanId;
    }

    public void setPayPlanId(Long payPlanId) {
        this.payPlanId = payPlanId;
    }

    public Long getNewPayPlanId() {
        return newPayPlanId;
    }

    public void setNewPayPlanId(Long newPayPlanId) {
        this.newPayPlanId = newPayPlanId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Long getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(Long loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getExtensionBalance() {
        return extensionBalance;
    }

    public void setExtensionBalance(BigDecimal extensionBalance) {
        this.extensionBalance = extensionBalance;
    }

    public BigDecimal getPayTotalBalance() {
        return payTotalBalance;
    }

    public void setPayTotalBalance(BigDecimal payTotalBalance) {
        this.payTotalBalance = payTotalBalance;
    }

    public BigDecimal getPayPrincipalBalance() {
        return payPrincipalBalance;
    }

    public void setPayPrincipalBalance(BigDecimal payPrincipalBalance) {
        this.payPrincipalBalance = payPrincipalBalance;
    }

    public BigDecimal getPayInterestBalance() {
        return payInterestBalance;
    }

    public void setPayInterestBalance(BigDecimal payInterestBalance) {
        this.payInterestBalance = payInterestBalance;
    }

    public BigDecimal getPayManagementBalance() {
        return payManagementBalance;
    }

    public void setPayManagementBalance(BigDecimal payManagementBalance) {
        this.payManagementBalance = payManagementBalance;
    }

    public BigDecimal getPayPenaltyBalance() {
        return payPenaltyBalance;
    }

    public void setPayPenaltyBalance(BigDecimal payPenaltyBalance) {
        this.payPenaltyBalance = payPenaltyBalance;
    }

    public BigDecimal getPayLatefeeBalance() {
        return payLatefeeBalance;
    }

    public void setPayLatefeeBalance(BigDecimal payLatefeeBalance) {
        this.payLatefeeBalance = payLatefeeBalance;
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

    public BigDecimal getShouldManagementBalance() {
        return shouldManagementBalance;
    }

    public void setShouldManagementBalance(BigDecimal shouldManagementBalance) {
        this.shouldManagementBalance = shouldManagementBalance;
    }

    public BigDecimal getShouldTotalBalance() {
        return shouldTotalBalance;
    }

    public void setShouldTotalBalance(BigDecimal shouldTotalBalance) {
        this.shouldTotalBalance = shouldTotalBalance;
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

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", payPlanId=").append(payPlanId);
        sb.append(", newPayPlanId=").append(newPayPlanId);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", loanBalance=").append(loanBalance);
        sb.append(", extensionBalance=").append(extensionBalance);
        sb.append(", payTotalBalance=").append(payTotalBalance);
        sb.append(", payPrincipalBalance=").append(payPrincipalBalance);
        sb.append(", payInterestBalance=").append(payInterestBalance);
        sb.append(", payManagementBalance=").append(payManagementBalance);
        sb.append(", payPenaltyBalance=").append(payPenaltyBalance);
        sb.append(", payLatefeeBalance=").append(payLatefeeBalance);
        sb.append(", shouldPrincipalBalance=").append(shouldPrincipalBalance);
        sb.append(", shouldInterestBalance=").append(shouldInterestBalance);
        sb.append(", shouldManagementBalance=").append(shouldManagementBalance);
        sb.append(", shouldTotalBalance=").append(shouldTotalBalance);
        sb.append(", period=").append(period);
        sb.append(", periodUnit=").append(periodUnit);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", ratio=").append(ratio);
        sb.append(", managementRatio=").append(managementRatio);
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
        ScfExtension other = (ScfExtension) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null
                        : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getPayPlanId() == null ? other.getPayPlanId() == null
                        : this.getPayPlanId().equals(other.getPayPlanId()))
                && (this.getNewPayPlanId() == null ? other.getNewPayPlanId() == null
                        : this.getNewPayPlanId().equals(other.getNewPayPlanId()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null
                        : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getLoanBalance() == null ? other.getLoanBalance() == null
                        : this.getLoanBalance().equals(other.getLoanBalance()))
                && (this.getExtensionBalance() == null ? other.getExtensionBalance() == null
                        : this.getExtensionBalance().equals(other.getExtensionBalance()))
                && (this.getPayTotalBalance() == null ? other.getPayTotalBalance() == null
                        : this.getPayTotalBalance().equals(other.getPayTotalBalance()))
                && (this.getPayPrincipalBalance() == null ? other.getPayPrincipalBalance() == null
                        : this.getPayPrincipalBalance().equals(other.getPayPrincipalBalance()))
                && (this.getPayInterestBalance() == null ? other.getPayInterestBalance() == null
                        : this.getPayInterestBalance().equals(other.getPayInterestBalance()))
                && (this.getPayManagementBalance() == null ? other.getPayManagementBalance() == null
                        : this.getPayManagementBalance().equals(other.getPayManagementBalance()))
                && (this.getPayPenaltyBalance() == null ? other.getPayPenaltyBalance() == null
                        : this.getPayPenaltyBalance().equals(other.getPayPenaltyBalance()))
                && (this.getPayLatefeeBalance() == null ? other.getPayLatefeeBalance() == null
                        : this.getPayLatefeeBalance().equals(other.getPayLatefeeBalance()))
                && (this.getShouldPrincipalBalance() == null ? other.getShouldPrincipalBalance() == null
                        : this.getShouldPrincipalBalance().equals(other.getShouldPrincipalBalance()))
                && (this.getShouldInterestBalance() == null ? other.getShouldInterestBalance() == null
                        : this.getShouldInterestBalance().equals(other.getShouldInterestBalance()))
                && (this.getShouldManagementBalance() == null ? other.getShouldManagementBalance() == null
                        : this.getShouldManagementBalance().equals(other.getShouldManagementBalance()))
                && (this.getShouldTotalBalance() == null ? other.getShouldTotalBalance() == null
                        : this.getShouldTotalBalance().equals(other.getShouldTotalBalance()))
                && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
                && (this.getPeriodUnit() == null ? other.getPeriodUnit() == null
                        : this.getPeriodUnit().equals(other.getPeriodUnit()))
                && (this.getStartDate() == null ? other.getStartDate() == null
                        : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null
                        : this.getEndDate().equals(other.getEndDate()))
                && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
                && (this.getManagementRatio() == null ? other.getManagementRatio() == null
                        : this.getManagementRatio().equals(other.getManagementRatio()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
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
                        : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getPayPlanId() == null) ? 0 : getPayPlanId().hashCode());
        result = prime * result + ((getNewPayPlanId() == null) ? 0 : getNewPayPlanId().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getLoanBalance() == null) ? 0 : getLoanBalance().hashCode());
        result = prime * result + ((getExtensionBalance() == null) ? 0 : getExtensionBalance().hashCode());
        result = prime * result + ((getPayTotalBalance() == null) ? 0 : getPayTotalBalance().hashCode());
        result = prime * result + ((getPayPrincipalBalance() == null) ? 0 : getPayPrincipalBalance().hashCode());
        result = prime * result + ((getPayInterestBalance() == null) ? 0 : getPayInterestBalance().hashCode());
        result = prime * result + ((getPayManagementBalance() == null) ? 0 : getPayManagementBalance().hashCode());
        result = prime * result + ((getPayPenaltyBalance() == null) ? 0 : getPayPenaltyBalance().hashCode());
        result = prime * result + ((getPayLatefeeBalance() == null) ? 0 : getPayLatefeeBalance().hashCode());
        result = prime * result + ((getShouldPrincipalBalance() == null) ? 0 : getShouldPrincipalBalance().hashCode());
        result = prime * result + ((getShouldInterestBalance() == null) ? 0 : getShouldInterestBalance().hashCode());
        result = prime * result
                + ((getShouldManagementBalance() == null) ? 0 : getShouldManagementBalance().hashCode());
        result = prime * result + ((getShouldTotalBalance() == null) ? 0 : getShouldTotalBalance().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getPeriodUnit() == null) ? 0 : getPeriodUnit().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getManagementRatio() == null) ? 0 : getManagementRatio().hashCode());
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
        return result;
    }

    @Transient
    public String custName;
    @Transient
    public String coreCustName;
    @Transient
    public String factorName;

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

    public void init() {
        this.id = SerialGenerator.getLongValue("ScfServiceFee.id");
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