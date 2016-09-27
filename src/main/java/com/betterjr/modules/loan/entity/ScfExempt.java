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
@Table(name = "t_scf_exempt")
public class ScfExempt implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    @OrderBy("desc")
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
     * 申请编号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请编号", comments = "申请编号")
    private String requestNo;

    /**
     * 还款计划ID
     */
    @Column(name = "L_PAY_PLAN_ID",  columnDefinition="BIGINT" )
    @MetaData( value="还款计划ID", comments = "还款计划ID")
    private Long payPlanId;

    /**
     * 豁免时间
     */
    @Column(name = "EXEMPT_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="豁免时间", comments = "豁免时间")
    private String exemptDate;

    /**
     * 本金
     */
    @Column(name = "F_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="本金", comments = "本金")
    private BigDecimal principalBalance;

    /**
     * 利息
     */
    @Column(name = "F_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="利息", comments = "利息")
    private BigDecimal interestBalance;

    /**
     * 管理费
     */
    @Column(name = "F_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="管理费", comments = "管理费")
    private BigDecimal managementBalance;

    /**
     * 罚息
     */
    @Column(name = "F_PENALTY_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="罚息", comments = "罚息")
    private BigDecimal penaltyBalance;

    /**
     * 滞纳金
     */
    @Column(name = "F_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="滞纳金", comments = "滞纳金")
    private BigDecimal latefeeBalance;

    /**
     * 手续费
     */
    @Column(name = "F_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="手续费", comments = "手续费")
    private BigDecimal servicefeeBalance;

    /**
     * 合计
     */
    @Column(name = "F_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="合计", comments = "合计")
    private BigDecimal totalBalance;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

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

    private static final long serialVersionUID = 1471942242070L;

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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Long getPayPlanId() {
        return payPlanId;
    }

    public void setPayPlanId(Long payPlanId) {
        this.payPlanId = payPlanId;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getExemptDate() {
        return exemptDate;
    }

    public void setExemptDate(String exemptDate) {
        this.exemptDate = exemptDate == null ? null : exemptDate.trim();
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public BigDecimal getManagementBalance() {
        return managementBalance;
    }

    public void setManagementBalance(BigDecimal managementBalance) {
        this.managementBalance = managementBalance;
    }

    public BigDecimal getPenaltyBalance() {
        return penaltyBalance;
    }

    public void setPenaltyBalance(BigDecimal penaltyBalance) {
        this.penaltyBalance = penaltyBalance;
    }

    public BigDecimal getLatefeeBalance() {
        return latefeeBalance;
    }

    public void setLatefeeBalance(BigDecimal latefeeBalance) {
        this.latefeeBalance = latefeeBalance;
    }

    public BigDecimal getServicefeeBalance() {
        return servicefeeBalance;
    }

    public void setServicefeeBalance(BigDecimal servicefeeBalance) {
        this.servicefeeBalance = servicefeeBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        sb.append(", requestNo=").append(requestNo);
        sb.append(", payPlanId=").append(payPlanId);
        sb.append(", exemptDate=").append(exemptDate);
        sb.append(", principalBalance=").append(principalBalance);
        sb.append(", interestBalance=").append(interestBalance);
        sb.append(", managementBalance=").append(managementBalance);
        sb.append(", penaltyBalance=").append(penaltyBalance);
        sb.append(", latefeeBalance=").append(latefeeBalance);
        sb.append(", servicefeeBalance=").append(servicefeeBalance);
        sb.append(", totalBalance=").append(totalBalance);
        sb.append(", description=").append(description);
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
        ScfExempt other = (ScfExempt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getPayPlanId() == null ? other.getPayPlanId() == null : this.getPayPlanId().equals(other.getPayPlanId()))
            && (this.getExemptDate() == null ? other.getExemptDate() == null : this.getExemptDate().equals(other.getExemptDate()))
            && (this.getPrincipalBalance() == null ? other.getPrincipalBalance() == null : this.getPrincipalBalance().equals(other.getPrincipalBalance()))
            && (this.getInterestBalance() == null ? other.getInterestBalance() == null : this.getInterestBalance().equals(other.getInterestBalance()))
            && (this.getManagementBalance() == null ? other.getManagementBalance() == null : this.getManagementBalance().equals(other.getManagementBalance()))
            && (this.getPenaltyBalance() == null ? other.getPenaltyBalance() == null : this.getPenaltyBalance().equals(other.getPenaltyBalance()))
            && (this.getLatefeeBalance() == null ? other.getLatefeeBalance() == null : this.getLatefeeBalance().equals(other.getLatefeeBalance()))
            && (this.getServicefeeBalance() == null ? other.getServicefeeBalance() == null : this.getServicefeeBalance().equals(other.getServicefeeBalance()))
            && (this.getTotalBalance() == null ? other.getTotalBalance() == null : this.getTotalBalance().equals(other.getTotalBalance()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getPayPlanId() == null) ? 0 : getPayPlanId().hashCode());
        result = prime * result + ((getExemptDate() == null) ? 0 : getExemptDate().hashCode());
        result = prime * result + ((getPrincipalBalance() == null) ? 0 : getPrincipalBalance().hashCode());
        result = prime * result + ((getInterestBalance() == null) ? 0 : getInterestBalance().hashCode());
        result = prime * result + ((getManagementBalance() == null) ? 0 : getManagementBalance().hashCode());
        result = prime * result + ((getPenaltyBalance() == null) ? 0 : getPenaltyBalance().hashCode());
        result = prime * result + ((getLatefeeBalance() == null) ? 0 : getLatefeeBalance().hashCode());
        result = prime * result + ((getServicefeeBalance() == null) ? 0 : getServicefeeBalance().hashCode());
        result = prime * result + ((getTotalBalance() == null) ? 0 : getTotalBalance().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
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
    public String factorName;
    @Transient
    private String custName;
    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void init(ScfExempt amExempt) {
        this.id = SerialGenerator.getLongValue("ScfServiceFee.id");
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        fillDefaultValue(amExempt);
    }

    public void initModify() {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }
    
    private void fillDefaultValue(ScfExempt amExempt){
        amExempt.interestBalance = MathExtend.defaultValue(amExempt.interestBalance, BigDecimal.ZERO);
        amExempt.latefeeBalance = MathExtend.defaultValue(amExempt.latefeeBalance, BigDecimal.ZERO);
        amExempt.managementBalance = MathExtend.defaultValue(amExempt.managementBalance, BigDecimal.ZERO);
        amExempt.penaltyBalance = MathExtend.defaultValue(amExempt.penaltyBalance, BigDecimal.ZERO);
        amExempt.principalBalance = MathExtend.defaultValue(amExempt.principalBalance, BigDecimal.ZERO);
        amExempt.servicefeeBalance = MathExtend.defaultValue(amExempt.servicefeeBalance, BigDecimal.ZERO);
        amExempt.totalBalance = MathExtend.defaultValue(amExempt.totalBalance, BigDecimal.ZERO);
    }
}