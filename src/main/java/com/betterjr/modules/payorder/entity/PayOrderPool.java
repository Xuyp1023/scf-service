package com.betterjr.modules.payorder.entity;

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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_pos_source_pay_pool")
public class PayOrderPool implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4254211803448055510L;

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    /**
     * 付款日期
     */
    @Column(name = "D_REQUEST_PAY_DATE", columnDefinition = "varchar")
    @MetaData(value = "付款日期", comments = "付款日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String requestPayDate;

    /**
     * 资金方企业编号
     */
    @Column(name = "L_FACTORY_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "资金方企业编号", comments = "资金方企业编号")
    private Long factoryNo;

    /**
     * 资金方企业名称
     */
    @Column(name = "C_FACTORY_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "资金方企业名称", comments = "资金方企业名称")
    private String factoryName;

    /**
     * 所属机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "所属机构", comments = "所属机构")
    private String operOrg;

    /**
     * 总金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "总金额", comments = "申请总金额")
    private BigDecimal balance;

    /**
     * 付款总笔数
     */
    @Column(name = "N_PAY_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "付款总笔数", comments = "付款总笔数")
    private Long payAmount;

    /**
     * 未付款总笔数
     */
    @Column(name = "N_NOPAY_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "未付款总笔数", comments = "未付款总笔数")
    private Long noPayAmount;

    /**
     * 付款中笔数
     */
    @Column(name = "N_PAYING_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "未付款总笔数", comments = "付款中笔数")
    private Long payingAmount;

    /**
     * 复核中笔数
     */
    @Column(name = "N_AUDIT_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "未付款总笔数", comments = "复核中笔数")
    private Long auditAmount;

    /**
     * 付款失败笔数
     */
    @Column(name = "N_PAYFAILURE_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "付款失败笔数", comments = "付款失败笔数")
    private Long payFailureAmount;

    /**
     * 付款成功笔数
     */
    @Column(name = "N_PAYSUCCESS_AMOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "付款成功笔数", comments = "付款成功笔数")
    private Long paySuccessAmount;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getRequestPayDate() {
        return this.requestPayDate;
    }

    public void setRequestPayDate(String anRequestPayDate) {
        this.requestPayDate = anRequestPayDate;
    }

    public Long getFactoryNo() {
        return this.factoryNo;
    }

    public void setFactoryNo(Long anFactoryNo) {
        this.factoryNo = anFactoryNo;
    }

    public String getFactoryName() {
        return this.factoryName;
    }

    public void setFactoryName(String anFactoryName) {
        this.factoryName = anFactoryName;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getPayAmount() {
        return this.payAmount;
    }

    public void setPayAmount(Long anPayAmount) {
        this.payAmount = anPayAmount;
    }

    public Long getNoPayAmount() {
        return this.noPayAmount;
    }

    public void setNoPayAmount(Long anNoPayAmount) {
        this.noPayAmount = anNoPayAmount;
    }

    public Long getPayingAmount() {
        return this.payingAmount;
    }

    public void setPayingAmount(Long anPayingAmount) {
        this.payingAmount = anPayingAmount;
    }

    public Long getAuditAmount() {
        return this.auditAmount;
    }

    public void setAuditAmount(Long anAuditAmount) {
        this.auditAmount = anAuditAmount;
    }

    public Long getPayFailureAmount() {
        return this.payFailureAmount;
    }

    public void setPayFailureAmount(Long anPayFailureAmount) {
        this.payFailureAmount = anPayFailureAmount;
    }

    public Long getPaySuccessAmount() {
        return this.paySuccessAmount;
    }

    public void setPaySuccessAmount(Long anPaySuccessAmount) {
        this.paySuccessAmount = anPaySuccessAmount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.auditAmount == null) ? 0 : this.auditAmount.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.factoryName == null) ? 0 : this.factoryName.hashCode());
        result = prime * result + ((this.factoryNo == null) ? 0 : this.factoryNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.noPayAmount == null) ? 0 : this.noPayAmount.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.payAmount == null) ? 0 : this.payAmount.hashCode());
        result = prime * result + ((this.payFailureAmount == null) ? 0 : this.payFailureAmount.hashCode());
        result = prime * result + ((this.paySuccessAmount == null) ? 0 : this.paySuccessAmount.hashCode());
        result = prime * result + ((this.payingAmount == null) ? 0 : this.payingAmount.hashCode());
        result = prime * result + ((this.requestPayDate == null) ? 0 : this.requestPayDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PayOrderPool other = (PayOrderPool) obj;
        if (this.auditAmount == null) {
            if (other.auditAmount != null) return false;
        }
        else if (!this.auditAmount.equals(other.auditAmount)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.factoryName == null) {
            if (other.factoryName != null) return false;
        }
        else if (!this.factoryName.equals(other.factoryName)) return false;
        if (this.factoryNo == null) {
            if (other.factoryNo != null) return false;
        }
        else if (!this.factoryNo.equals(other.factoryNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.noPayAmount == null) {
            if (other.noPayAmount != null) return false;
        }
        else if (!this.noPayAmount.equals(other.noPayAmount)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.payAmount == null) {
            if (other.payAmount != null) return false;
        }
        else if (!this.payAmount.equals(other.payAmount)) return false;
        if (this.payFailureAmount == null) {
            if (other.payFailureAmount != null) return false;
        }
        else if (!this.payFailureAmount.equals(other.payFailureAmount)) return false;
        if (this.paySuccessAmount == null) {
            if (other.paySuccessAmount != null) return false;
        }
        else if (!this.paySuccessAmount.equals(other.paySuccessAmount)) return false;
        if (this.payingAmount == null) {
            if (other.payingAmount != null) return false;
        }
        else if (!this.payingAmount.equals(other.payingAmount)) return false;
        if (this.requestPayDate == null) {
            if (other.requestPayDate != null) return false;
        }
        else if (!this.requestPayDate.equals(other.requestPayDate)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "PayOrderPool [id=" + this.id + ", requestPayDate=" + this.requestPayDate + ", factoryNo=" + this.factoryNo + ", factoryName="
                + this.factoryName + ", operOrg=" + this.operOrg + ", balance=" + this.balance + ", payAmount=" + this.payAmount + ", noPayAmount="
                + this.noPayAmount + ", payingAmount=" + this.payingAmount + ", auditAmount=" + this.auditAmount + ", payFailureAmount="
                + this.payFailureAmount + ", paySuccessAmount=" + this.paySuccessAmount + "]";
    }

    public PayOrderPool saveAddInitValue() {
        
        this.setId(SerialGenerator.getLongValue("PayOrderPool.id"));
        this.setAuditAmount(0L);
        this.setPayAmount(1L);
        this.setNoPayAmount(1L);
        this.setPayFailureAmount(0L);
        this.setPayingAmount(0L);
        this.setPaySuccessAmount(0L);
        return this;
    }

}
