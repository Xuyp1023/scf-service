package com.betterjr.modules.credit.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.credit.constant.CreditConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_CREDIT")
public class ScfCredit implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 核心企业
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业", comments = "核心企业")
    private Long coreCustNo;

    /**
     * 核心企业名称
     */
    @Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "核心企业名称", comments = "核心企业名称")
    private String coreName;

    /**
     * 保理公司
     */
    @Column(name = "L_FACTORNO", columnDefinition = "INTEGER")
    @MetaData(value = "保理公司", comments = "保理公司")
    private Long factorNo;

    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司名称", comments = "保理公司名称")
    private String factorName;

    /**
     * 授信对象(1:供应商;2:经销商;3:核心企业;)
     */
    @Column(name = "C_CREDIT_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信对象(1:供应商;2:经销商;3:核心企业;)", comments = "授信对象(1:供应商;2:经销商;3:核心企业;)")
    private String creditType;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户名称", comments = "客户名称")
    private String custName;

    /**
     * 授信额度
     */
    @Column(name = "F_CREDIT_LIMIT", columnDefinition = "DOUBLE")
    @MetaData(value = "授信额度", comments = "授信额度")
    private BigDecimal creditLimit;

    /**
     * 已使用授信
     */
    @Column(name = "F_CREDIT_USED", columnDefinition = "DOUBLE")
    @MetaData(value = "已使用授信", comments = "已使用授信")
    private BigDecimal creditUsed;

    /**
     * 授信余额
     */
    @Column(name = "F_CREDIT_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "授信余额", comments = "授信余额")
    private BigDecimal creditBalance;

    /**
     * 开始日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_START_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "开始日期", comments = "开始日期")
    private String startDate;

    /**
     * 有效期至
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_END_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "有效期至", comments = "有效期至")
    private String endDate;

    /**
     * 授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
     */
    @Column(name = "C_CREDIT_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)", comments = "授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)")
    private String creditMode;

    /**
     * 保理合同
     */
    @Column(name = "L_AGREEID", columnDefinition = "INTEGER")
    @MetaData(value = "保理合同", comments = "保理合同")
    private Long agreeId;

    /**
     * 授信录入操作员ID号
     */
    @JsonIgnore
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "授信录入操作员ID号", comments = "授信录入操作员ID号")
    private Long regOperId;

    /**
     * 授信录入操作员姓名
     */
    @JsonIgnore
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信录入操作员姓名", comments = "授信录入操作员姓名")
    private String regOperName;

    /**
     * 授信录入日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信录入日期", comments = "授信录入日期")
    private String regDate;

    /**
     * 授信录入时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信录入时间", comments = "授信录入时间")
    private String regTime;

    /**
     * 授信修改操作员ID号
     */
    @JsonIgnore
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "授信修改操作员ID号", comments = "授信修改操作员ID号")
    private Long modiOperId;

    /**
     * 授信修改操作员姓名
     */
    @JsonIgnore
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信修改操作员姓名", comments = "授信修改操作员姓名")
    private String modiOperName;

    /**
     * 授信修改日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信修改日期", comments = "授信修改日期")
    private String modiDate;

    /**
     * 授信修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信修改时间", comments = "授信修改时间")
    private String modiTime;

    /**
     * 授信激活操作员ID号
     */
    @JsonIgnore
    @Column(name = "L_ACTIVATE_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "授信激活操作员ID号", comments = "授信激活操作员ID号")
    private Long activateOperId;

    /**
     * 授信激活操作员姓名
     */
    @JsonIgnore
    @Column(name = "C_ACTIVATE_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信激活操作员姓名", comments = "授信激活操作员姓名")
    private String activateOperName;

    /**
     * 授信激活日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_ACTIVATE_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信激活日期", comments = "授信激活日期")
    private String activateDate;

    /**
     * 授信激活时间
     */
    @Column(name = "T_ACTIVATE_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信激活时间", comments = "授信激活时间")
    private String activateTime;

    /**
     * 授信终止操作员ID号
     */
    @JsonIgnore
    @Column(name = "L_TERMINAT_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "授信终止操作员ID号", comments = "授信终止操作员ID号")
    private Long terminatOperId;

    /**
     * 授信终止操作员姓名
     */
    @JsonIgnore
    @Column(name = "C_TERMINAT_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信终止操作员姓名", comments = "授信终止操作员姓名")
    private String terminatOperName;

    /**
     * 授信终止日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_TERMINAT_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "授信终止日期", comments = "授信终止日期")
    private String terminatDate;

    /**
     * 授信终止时间
     */
    @Column(name = "T_TERMINAT_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "授信终止时间", comments = "授信终止时间")
    private String terminatTime;

    /**
     * 状态(0:未生效;1:已生效;2:已失效;)
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态(0:未生效;1:已生效;2:已失效;)", comments = "状态(0:未生效;1:已生效;2:已失效;)")
    private String businStatus;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注")
    private String description;

    private static final long serialVersionUID = 1467703726395L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public String getCoreName() {
        return this.coreName;
    }

    public void setCoreName(String anCoreName) {
        this.coreName = anCoreName;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public String getFactorName() {
        return this.factorName;
    }

    public void setFactorName(String anFactorName) {
        this.factorName = anFactorName;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType == null ? null : creditType.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(BigDecimal creditUsed) {
        this.creditUsed = creditUsed;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(String creditMode) {
        this.creditMode = creditMode == null ? null : creditMode.trim();
    }

    public Long getAgreeId() {
        return agreeId;
    }

    public void setAgreeId(Long agreeId) {
        this.agreeId = agreeId;
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
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
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
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public Long getActivateOperId() {
        return activateOperId;
    }

    public void setActivateOperId(Long activateOperId) {
        this.activateOperId = activateOperId;
    }

    public String getActivateOperName() {
        return activateOperName;
    }

    public void setActivateOperName(String activateOperName) {
        this.activateOperName = activateOperName == null ? null : activateOperName.trim();
    }

    public String getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(String activateDate) {
        this.activateDate = activateDate == null ? null : activateDate.trim();
    }

    public String getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(String activateTime) {
        this.activateTime = activateTime == null ? null : activateTime.trim();
    }

    public Long getTerminatOperId() {
        return terminatOperId;
    }

    public void setTerminatOperId(Long terminatOperId) {
        this.terminatOperId = terminatOperId;
    }

    public String getTerminatOperName() {
        return terminatOperName;
    }

    public void setTerminatOperName(String terminatOperName) {
        this.terminatOperName = terminatOperName == null ? null : terminatOperName.trim();
    }

    public String getTerminatDate() {
        return terminatDate;
    }

    public void setTerminatDate(String terminatDate) {
        this.terminatDate = terminatDate == null ? null : terminatDate.trim();
    }

    public String getTerminatTime() {
        return terminatTime;
    }

    public void setTerminatTime(String terminatTime) {
        this.terminatTime = terminatTime == null ? null : terminatTime.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", coreName=").append(coreName);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", factorName=").append(factorName);
        sb.append(", creditType=").append(creditType);
        sb.append(", custNo=").append(custNo);
        sb.append(", creditLimit=").append(creditLimit);
        sb.append(", creditUsed=").append(creditUsed);
        sb.append(", creditBalance=").append(creditBalance);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", creditMode=").append(creditMode);
        sb.append(", agreeId=").append(agreeId);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", activateOperId=").append(activateOperId);
        sb.append(", activateOperName=").append(activateOperName);
        sb.append(", activateDate=").append(activateDate);
        sb.append(", activateTime=").append(activateTime);
        sb.append(", terminatOperId=").append(terminatOperId);
        sb.append(", terminatOperName=").append(terminatOperName);
        sb.append(", terminatDate=").append(terminatDate);
        sb.append(", terminatTime=").append(terminatTime);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", description=").append(description);
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
        ScfCredit other = (ScfCredit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getCoreName() == null ? other.getCoreName() == null : this.getCoreName().equals(other.getCoreName()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getFactorName() == null ? other.getFactorName() == null : this.getFactorName().equals(other.getFactorName()))
                && (this.getCreditType() == null ? other.getCreditType() == null : this.getCreditType().equals(other.getCreditType()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getCreditLimit() == null ? other.getCreditLimit() == null : this.getCreditLimit().equals(other.getCreditLimit()))
                && (this.getCreditUsed() == null ? other.getCreditUsed() == null : this.getCreditUsed().equals(other.getCreditUsed()))
                && (this.getCreditBalance() == null ? other.getCreditBalance() == null : this.getCreditBalance().equals(other.getCreditBalance()))
                && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getCreditMode() == null ? other.getCreditMode() == null : this.getCreditMode().equals(other.getCreditMode()))
                && (this.getAgreeId() == null ? other.getAgreeId() == null : this.getAgreeId().equals(other.getAgreeId()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getActivateOperId() == null ? other.getActivateOperId() == null : this.getActivateOperId().equals(other.getActivateOperId()))
                && (this.getActivateOperName() == null ? other.getActivateOperName() == null
                        : this.getActivateOperName().equals(other.getActivateOperName()))
                && (this.getActivateDate() == null ? other.getActivateDate() == null : this.getActivateDate().equals(other.getActivateDate()))
                && (this.getActivateTime() == null ? other.getActivateTime() == null : this.getActivateTime().equals(other.getActivateTime()))
                && (this.getTerminatOperId() == null ? other.getTerminatOperId() == null : this.getTerminatOperId().equals(other.getTerminatOperId()))
                && (this.getTerminatOperName() == null ? other.getTerminatOperName() == null
                        : this.getTerminatOperName().equals(other.getTerminatOperName()))
                && (this.getTerminatDate() == null ? other.getTerminatDate() == null : this.getTerminatDate().equals(other.getTerminatDate()))
                && (this.getTerminatTime() == null ? other.getTerminatTime() == null : this.getTerminatTime().equals(other.getTerminatTime()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCoreName() == null) ? 0 : getCoreName().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getFactorName() == null) ? 0 : getFactorName().hashCode());
        result = prime * result + ((getCreditType() == null) ? 0 : getCreditType().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getCreditLimit() == null) ? 0 : getCreditLimit().hashCode());
        result = prime * result + ((getCreditUsed() == null) ? 0 : getCreditUsed().hashCode());
        result = prime * result + ((getCreditBalance() == null) ? 0 : getCreditBalance().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getCreditMode() == null) ? 0 : getCreditMode().hashCode());
        result = prime * result + ((getAgreeId() == null) ? 0 : getAgreeId().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getActivateOperId() == null) ? 0 : getActivateOperId().hashCode());
        result = prime * result + ((getActivateOperName() == null) ? 0 : getActivateOperName().hashCode());
        result = prime * result + ((getActivateDate() == null) ? 0 : getActivateDate().hashCode());
        result = prime * result + ((getActivateTime() == null) ? 0 : getActivateTime().hashCode());
        result = prime * result + ((getTerminatOperId() == null) ? 0 : getTerminatOperId().hashCode());
        result = prime * result + ((getTerminatOperName() == null) ? 0 : getTerminatOperName().hashCode());
        result = prime * result + ((getTerminatDate() == null) ? 0 : getTerminatDate().hashCode());
        result = prime * result + ((getTerminatTime() == null) ? 0 : getTerminatTime().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    private void init() {
        this.id = SerialGenerator.getLongValue("ScfCredit.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.regOperName = UserUtils.getOperatorInfo().getName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
    }

    private void calculate() {
        // 授信余额 = 授信额度 - 授信占用
        this.creditBalance = MathExtend.subtract(this.creditLimit, this.creditUsed);
    }

    public void initAddValue() {
        init();
        this.creditUsed = BigDecimal.ZERO;
        calculate();
        this.businStatus = CreditConstants.CREDIT_STATUS_INEFFECTIVE;// 授信状态:0-未生效;1-已生效;2-已失效;
    }

    public void initModifyValue(ScfCredit anCredit) {
        calculate();
        this.id = anCredit.getId();

        this.regOperId = anCredit.getRegOperId();
        this.regOperName = anCredit.getRegOperName();
        this.regDate = anCredit.getRegDate();
        this.regTime = anCredit.getRegTime();

        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getOperatorInfo().getName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();

        this.businStatus = anCredit.getBusinStatus();
        this.operOrg = anCredit.getOperOrg();
    }

    public void initActivateValue() {
        this.activateOperId = UserUtils.getOperatorInfo().getId();
        this.activateOperName = UserUtils.getOperatorInfo().getName();
        this.activateDate = BetterDateUtils.getNumDate();
        this.activateTime = BetterDateUtils.getNumTime();
        this.businStatus = CreditConstants.CREDIT_STATUS_EFFECTIVE;// 授信状态:0-未生效;1-已生效;2-已失效;
    }

    public void initTerminatValue() {
        this.terminatOperId = UserUtils.getOperatorInfo().getId();
        this.terminatOperName = UserUtils.getOperatorInfo().getName();
        this.terminatDate = BetterDateUtils.getNumDate();
        this.terminatTime = BetterDateUtils.getNumTime();
        this.businStatus = CreditConstants.CREDIT_STATUS_INVALID;// 授信状态:0-未生效;1-已生效;2-已失效;
    }

    public void occupyCreditBalance(BigDecimal anCreditUsed, BigDecimal anCreditBalance, BigDecimal anOccupyBalance) {
        // 更新授信额度累计使用
        this.creditUsed = MathExtend.add(anCreditUsed, anOccupyBalance);
        // 更新授信余额
        this.creditBalance = MathExtend.subtract(anCreditBalance, anOccupyBalance);
    }

    public void releaseCreditBalance(BigDecimal anCreditUsed, BigDecimal anCreditBalance, BigDecimal anOccupyBalance) {
        // 更新授信额度累计使用
        this.creditUsed = MathExtend.subtract(anCreditUsed, anOccupyBalance);
        // 更新授信余额
        this.creditBalance = MathExtend.add(anCreditBalance, anOccupyBalance);
    }

}