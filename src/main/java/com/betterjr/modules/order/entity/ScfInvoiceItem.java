package com.betterjr.modules.order.entity;

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
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_INVOICE_ITEM")
public class ScfInvoiceItem implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 发票流水号
     */
    @Column(name = "L_INVOICE_ID", columnDefinition = "BIGINT")
    @MetaData(value = "发票流水号", comments = "发票流水号")
    private Long invoiceId;

    /**
     * 项目
     */
    @Column(name = "C_SUBJECT", columnDefinition = "VARCHAR")
    @MetaData(value = "项目", comments = "项目")
    private String subject;

    /**
     * 单价
     */
    @Column(name = "F_UNIT", columnDefinition = "DOUBLE")
    @MetaData(value = "单位", comments = "单位")
    private String unit;

    /**
     * 数量
     */
    @Column(name = "N_AMOUNT", columnDefinition = "DOUBLE")
    @MetaData(value = "数量", comments = "数量")
    private Integer amount;

    /**
     * 金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "金额", comments = "金额")
    private BigDecimal balance;

    /**
     * 新增操作员编码
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "新增操作员编码", comments = "新增操作员编码")
    @JsonIgnore
    private Long regOperId;

    /**
     * 新增操作员名字
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "新增操作员名字", comments = "新增操作员名字")
    private String regOperName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "编辑操作员编码", comments = "编辑操作员编码")
    @JsonIgnore
    private Long modiOperId;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "编辑操作员名字", comments = "编辑操作员名字")
    private String modiOperName;

    /**
     * 编辑日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "编辑日期", comments = "编辑日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 编辑时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "编辑时间", comments = "编辑时间")
    private String modiTime;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注")
    private String description;

    private static final long serialVersionUID = 1473152368168L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
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
        sb.append(", invoiceId=").append(invoiceId);
        sb.append(", subject=").append(subject);
        sb.append(", unit=").append(unit);
        sb.append(", amount=").append(amount);
        sb.append(", balance=").append(balance);
        sb.append(", regOperId =").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId =").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
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
        ScfInvoiceItem other = (ScfInvoiceItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getInvoiceId() == null ? other.getInvoiceId() == null
                        : this.getInvoiceId().equals(other.getInvoiceId()))
                && (this.getSubject() == null ? other.getSubject() == null
                        : this.getSubject().equals(other.getSubject()))
                && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getBalance() == null ? other.getBalance() == null
                        : this.getBalance().equals(other.getBalance()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null
                        : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null
                        : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
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
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInvoiceId() == null) ? 0 : getInvoiceId().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    public void initAddValue() {
        this.id = SerialGenerator.getLongValue("ScfInvoiceItem.id");
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.regOperName = UserUtils.getOperatorInfo().getName();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
    }
}