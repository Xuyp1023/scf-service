package com.betterjr.modules.supplychain.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.client.data.ScfClientDataParentFace;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_BANK_FLOW")
public class ScfBankPaymentFlow implements ScfClientDataParentFace {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 收款人名称
     */
    @Column(name = "C_SUPPLIER", columnDefinition = "VARCHAR")
    @MetaData(value = "收款人名称", comments = "收款人名称")
    private String supplier;

    /**
     * 买方客户号
     */
    @Column(name = "L_BUYER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "买方客户号", comments = "买方客户号")
    private Long buyerNo;

    /**
     * 收款方银行账户
     */
    @Column(name = "C_SUPPLIER_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "收款方银行账户", comments = "收款方银行账户")
    private String suppBankAccount;

    /**
     * 收款方银行全称
     */
    @Column(name = "C_SUPPLIER_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "收款方银行全称", comments = "收款方银行全称")
    private String suppBankName;

    /**
     * 付款人名称
     */
    @Column(name = "C_BUYER", columnDefinition = "VARCHAR")
    @MetaData(value = "付款人名称", comments = "付款人名称")
    private String buyer;

    /**
     * 卖方客户号
     */
    @Column(name = "L_SUPPLIER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "卖方客户号", comments = "卖方客户号")
    private Long supplierNo;

    /**
     * 付款方银行账户
     */
    @Column(name = "C_BUYER_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "付款方银行账户", comments = "付款方银行账户")
    private String buyerBankAccount;

    /**
     * 付款方银行全称
     */
    @Column(name = "C_BUYER_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "付款方银行全称", comments = "付款方银行全称")
    private String buyerBankName;

    /**
     * 拜特内部编号
     */
    @Column(name = "C_BTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "拜特内部编号", comments = "拜特内部编号")
    private String btInnerId;

    /**
     * 金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "金额", comments = "金额")
    private BigDecimal balance;

    /**
     * 交易日期
     */
    @Column(name = "D_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "交易日期", comments = "交易日期")
    private String requestDate;

    /**
     * 交易时间
     */
    @Column(name = "T_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "交易时间", comments = "交易时间")
    private String requestTime;

    /**
     * 摘要
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "摘要", comments = "摘要")
    private String description;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 银行流水号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "银行流水号", comments = "银行流水号")
    private String requestNo;

    /**
     * 核心企额客户编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企额客户编号", comments = "核心企额客户编号")
    private Long coreCustNo;

    /**
     * 操作员所在机构，证书登录，则是证书的企业名称O字段
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构，证书登录，则是证书的企业名称O字段")
    private String operOrg;

    @JsonIgnore
    @Column(name = "C_CORE_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "数据所属核心企业")
    private String coreOperOrg;

    private static final long serialVersionUID = 1459331162472L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public Long getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(final Long buyerNo) {
        this.buyerNo = buyerNo;
    }

    public String getSuppBankAccount() {
        return suppBankAccount;
    }

    public void setSuppBankAccount(final String suppBankAccount) {
        this.suppBankAccount = suppBankAccount == null ? null : suppBankAccount.trim();
    }

    public String getSuppBankName() {
        return suppBankName;
    }

    public void setSuppBankName(final String suppBankName) {
        this.suppBankName = suppBankName == null ? null : suppBankName.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(final String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(final Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(final String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount == null ? null : buyerBankAccount.trim();
    }

    public String getBuyerBankName() {
        return buyerBankName;
    }

    public void setBuyerBankName(final String buyerBankName) {
        this.buyerBankName = buyerBankName == null ? null : buyerBankName.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(final String requestDate) {
        this.requestDate = requestDate == null ? null : requestDate.trim();
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(final String requestTime) {
        this.requestTime = requestTime == null ? null : requestTime.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(final String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    @Override
    public void setCoreCustNo(final Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getBtInnerId() {
        return this.btInnerId;
    }

    public void setBtInnerId(final String anBtInnerId) {
        this.btInnerId = anBtInnerId;
    }

    @Override
    public String getCoreOperOrg() {
        return coreOperOrg;
    }

    @Override
    public void setCoreOperOrg(final String coreOperOrg) {
        this.coreOperOrg = coreOperOrg == null ? null : coreOperOrg.trim();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" id=").append(id);
        sb.append(", supplier=").append(supplier);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", suppBankAccount=").append(suppBankAccount);
        sb.append(", suppBankName=").append(suppBankName);
        sb.append(", buyer=").append(buyer);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", buyerBankAccount=").append(buyerBankAccount);
        sb.append(", buyerBankName=").append(buyerBankName);
        sb.append(", btInnerId=").append(btInnerId);
        sb.append(", balance=").append(balance);
        sb.append(", requestDate=").append(requestDate);
        sb.append(", requestTime=").append(requestTime);
        sb.append(", description=").append(description);
        sb.append(", regDate=").append(regDate);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", coreOperOrg=").append(coreOperOrg);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        final ScfBankPaymentFlow other = (ScfBankPaymentFlow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSupplier() == null ? other.getSupplier() == null
                        : this.getSupplier().equals(other.getSupplier()))
                && (this.getBuyerNo() == null ? other.getBuyerNo() == null
                        : this.getBuyerNo().equals(other.getBuyerNo()))
                && (this.getSuppBankAccount() == null ? other.getSuppBankAccount() == null
                        : this.getSuppBankAccount().equals(other.getSuppBankAccount()))
                && (this.getSuppBankName() == null ? other.getSuppBankName() == null
                        : this.getSuppBankName().equals(other.getSuppBankName()))
                && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
                && (this.getSupplierNo() == null ? other.getSupplierNo() == null
                        : this.getSupplierNo().equals(other.getSupplierNo()))
                && (this.getBuyerBankAccount() == null ? other.getBuyerBankAccount() == null
                        : this.getBuyerBankAccount().equals(other.getBuyerBankAccount()))
                && (this.getBuyerBankName() == null ? other.getBuyerBankName() == null
                        : this.getBuyerBankName().equals(other.getBuyerBankName()))
                && (this.getBtInnerId() == null ? other.getBtInnerId() == null
                        : this.getBtInnerId().equals(other.getBtInnerId()))
                && (this.getBalance() == null ? other.getBalance() == null
                        : this.getBalance().equals(other.getBalance()))
                && (this.getRequestDate() == null ? other.getRequestDate() == null
                        : this.getRequestDate().equals(other.getRequestDate()))
                && (this.getRequestTime() == null ? other.getRequestTime() == null
                        : this.getRequestTime().equals(other.getRequestTime()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null
                        : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getCoreOperOrg() == null ? other.getCoreOperOrg() == null
                        : this.getCoreOperOrg().equals(other.getCoreOperOrg()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null
                        : this.getCoreCustNo().equals(other.getCoreCustNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSuppBankAccount() == null) ? 0 : getSuppBankAccount().hashCode());
        result = prime * result + ((getSuppBankName() == null) ? 0 : getSuppBankName().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getBuyerBankAccount() == null) ? 0 : getBuyerBankAccount().hashCode());
        result = prime * result + ((getBuyerBankName() == null) ? 0 : getBuyerBankName().hashCode());
        result = prime * result + ((getBtInnerId() == null) ? 0 : getBtInnerId().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getRequestDate() == null) ? 0 : getRequestDate().hashCode());
        result = prime * result + ((getRequestTime() == null) ? 0 : getRequestTime().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCoreOperOrg() == null) ? 0 : getCoreOperOrg().hashCode());
        return result;
    }

    @Override
    public void fillDefaultValue() {
        this.id = SerialGenerator.getLongValue("ScfBankPaymentFlow.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.buyerNo = this.coreCustNo;
        if (BetterStringUtils.shortString(this.requestDate, 12) == false) {
            this.requestTime = this.requestDate.substring(8).trim();
            this.requestDate = this.requestDate.substring(0, 8);
        }
        this.supplierNo = MathExtend.defaultLongZero(this.supplierNo);
    }

    @Override
    public String getOperOrg() {
        return this.operOrg;
    }

    @Override
    public void setOperOrg(final String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    @Override
    public void setCustNo(final Long anCustNo) {

        this.supplierNo = anCustNo;
    }

    @Override
    public void setModiDate(final String anModiDate) {

    }

    @Override
    public String getBankAccount() {

        return this.suppBankAccount;
    }

    @Override
    public String getBtNo() {

        return null;
    }

    @Override
    public Long getCustNo() {

        return this.supplierNo;
    }

    @Override
    public void modifytValue() {}

    @Override
    public String findBankAccountName() {

        return this.supplier;
    }

}