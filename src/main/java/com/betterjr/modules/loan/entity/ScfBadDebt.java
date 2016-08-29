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

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_bad_debt")
public class ScfBadDebt implements BetterjrEntity {
    @Id
    private Long id;

    /**
     * 申请单号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请单号", comments = "申请单号")
    private String requestNo;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 剩余本金
     */
    @Column(name = "F_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="剩余本金", comments = "剩余本金")
    private BigDecimal principalBalance;

    /**
     * 剩余利息
     */
    @Column(name = "F_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="剩余利息", comments = "剩余利息")
    private BigDecimal interestBalance;

    /**
     * 剩余管理费
     */
    @Column(name = "F_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="剩余管理费", comments = "剩余管理费")
    private BigDecimal managementBalance;

    /**
     * 滞纳金
     */
    @Column(name = "F_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="滞纳金", comments = "滞纳金")
    private BigDecimal lateFeeBalance;

    /**
     * 手续费
     */
    @Column(name = "F_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="手续费", comments = "手续费")
    private BigDecimal servicefeeBalance;

    /**
     * 剩余其它费用
     */
    @Column(name = "F_OTHER_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="剩余其它费用", comments = "剩余其它费用")
    private BigDecimal otherBalance;

    /**
     * 合计
     */
    @Column(name = "F_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="合计", comments = "合计")
    private BigDecimal totalBalance;

    /**
     * 销核日期
     */
    @Column(name = "D_CONFIRM_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="销核日期", comments = "销核日期")
    private String confirmDate;

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

    private static final long serialVersionUID = 5434051810014303919L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
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

    public BigDecimal getLateFeeBalance() {
        return lateFeeBalance;
    }

    public void setLateFeeBalance(BigDecimal lateFeeBalance) {
        this.lateFeeBalance = lateFeeBalance;
    }

    public BigDecimal getServicefeeBalance() {
        return servicefeeBalance;
    }

    public void setServicefeeBalance(BigDecimal servicefeeBalance) {
        this.servicefeeBalance = servicefeeBalance;
    }

    public BigDecimal getOtherBalance() {
        return otherBalance;
    }

    public void setOtherBalance(BigDecimal otherBalance) {
        this.otherBalance = otherBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", principalBalance=").append(principalBalance);
        sb.append(", interestBalance=").append(interestBalance);
        sb.append(", managementBalance=").append(managementBalance);
        sb.append(", lateFeeBalance=").append(lateFeeBalance);
        sb.append(", servicefeeBalance=").append(servicefeeBalance);
        sb.append(", otherBalance=").append(otherBalance);
        sb.append(", totalBalance=").append(totalBalance);
        sb.append(", confirmDate=").append(confirmDate);
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
        ScfBadDebt other = (ScfBadDebt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getPrincipalBalance() == null ? other.getPrincipalBalance() == null : this.getPrincipalBalance().equals(other.getPrincipalBalance()))
            && (this.getInterestBalance() == null ? other.getInterestBalance() == null : this.getInterestBalance().equals(other.getInterestBalance()))
            && (this.getManagementBalance() == null ? other.getManagementBalance() == null : this.getManagementBalance().equals(other.getManagementBalance()))
            && (this.getLateFeeBalance() == null ? other.getLateFeeBalance() == null : this.getLateFeeBalance().equals(other.getLateFeeBalance()))
            && (this.getServicefeeBalance() == null ? other.getServicefeeBalance() == null : this.getServicefeeBalance().equals(other.getServicefeeBalance()))
            && (this.getOtherBalance() == null ? other.getOtherBalance() == null : this.getOtherBalance().equals(other.getOtherBalance()))
            && (this.getTotalBalance() == null ? other.getTotalBalance() == null : this.getTotalBalance().equals(other.getTotalBalance()))
            && (this.getConfirmDate() == null ? other.getConfirmDate() == null : this.getConfirmDate().equals(other.getConfirmDate()))
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
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getPrincipalBalance() == null) ? 0 : getPrincipalBalance().hashCode());
        result = prime * result + ((getInterestBalance() == null) ? 0 : getInterestBalance().hashCode());
        result = prime * result + ((getManagementBalance() == null) ? 0 : getManagementBalance().hashCode());
        result = prime * result + ((getLateFeeBalance() == null) ? 0 : getLateFeeBalance().hashCode());
        result = prime * result + ((getServicefeeBalance() == null) ? 0 : getServicefeeBalance().hashCode());
        result = prime * result + ((getOtherBalance() == null) ? 0 : getOtherBalance().hashCode());
        result = prime * result + ((getTotalBalance() == null) ? 0 : getTotalBalance().hashCode());
        result = prime * result + ((getConfirmDate() == null) ? 0 : getConfirmDate().hashCode());
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
}