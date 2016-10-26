package com.betterjr.modules.supplychain.entity;

import java.math.BigDecimal;

import com.betterjr.common.annotation.*;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.supplychain.data.ScfClientDataParentFace;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_CAPITAL_FLOW")
public class ScfCapitalFlow implements ScfClientDataParentFace {
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
     * 实际申请日期
     */
    @Column(name = "D_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "实际申请日期", comments = "实际申请日期")
    private String requestDate;

    /**
     * 申请时间
     */
    @Column(name = "T_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "申请时间", comments = "申请时间")
    private String requestTime;

    /**
     * 用途
     */
    @Column(name = "C_PURPOSE", columnDefinition = "VARCHAR")
    @MetaData(value = "用途", comments = "用途")
    private String purpose;

    /**
     * 摘要
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "摘要", comments = "摘要")
    private String description;

    /**
     * 付款状态；0未付款，1已付款
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "付款状态", comments = "付款状态；0未付款，1已付款")
    private String status;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 0银行付款，1票据付款
     */
    @Column(name = "C_PAY_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "0银行付款", comments = "0银行付款，1票据付款")
    private String payMode;

    /**
     * 付款流水号；如果票据付款，对应票据号，如果银行付款，对应银行流水号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "付款流水号", comments = "付款流水号；如果票据付款，对应票据号，如果银行付款，对应银行流水号")
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

    private static final long serialVersionUID = -4262616032648201451L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public Long getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(Long buyerNo) {
        this.buyerNo = buyerNo;
    }

    public String getSuppBankAccount() {
        return suppBankAccount;
    }

    public void setSuppBankAccount(String suppBankAccount) {
        this.suppBankAccount = suppBankAccount == null ? null : suppBankAccount.trim();
    }

    public String getSuppBankName() {
        return suppBankName;
    }

    public void setSuppBankName(String suppBankName) {
        this.suppBankName = suppBankName == null ? null : suppBankName.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount == null ? null : buyerBankAccount.trim();
    }

    public String getBuyerBankName() {
        return buyerBankName;
    }

    public void setBuyerBankName(String buyerBankName) {
        this.buyerBankName = buyerBankName == null ? null : buyerBankName.trim();
    }

    public String getBtInnerId() {
        return this.btInnerId;
    }

    public void setBtInnerId(String anBtInnerId) {
        this.btInnerId = anBtInnerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate == null ? null : requestDate.trim();
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime == null ? null : requestTime.trim();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose == null ? null : purpose.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        sb.append(", getBtInnerId=").append(btInnerId);
        sb.append(", balance=").append(balance);
        sb.append(", requestDate=").append(requestDate);
        sb.append(", requestTime=").append(requestTime);
        sb.append(", purpose=").append(purpose);
        sb.append(", description=").append(description);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", payMode=").append(payMode);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", operOrg=").append(operOrg);
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
        ScfCapitalFlow other = (ScfCapitalFlow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
                && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
                && (this.getSuppBankAccount() == null ? other.getSuppBankAccount() == null : this.getSuppBankAccount().equals(
                        other.getSuppBankAccount()))
                && (this.getSuppBankName() == null ? other.getSuppBankName() == null : this.getSuppBankName().equals(other.getSuppBankName()))
                && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
                && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
                && (this.getBuyerBankAccount() == null ? other.getBuyerBankAccount() == null : this.getBuyerBankAccount().equals(
                        other.getBuyerBankAccount()))
                && (this.getBuyerBankName() == null ? other.getBuyerBankName() == null : this.getBuyerBankName().equals(other.getBuyerBankName()))
                && (this.getBtInnerId() == null ? other.getBtInnerId() == null : this.getBtInnerId().equals(other.getBtInnerId()))
                && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
                && (this.getRequestDate() == null ? other.getRequestDate() == null : this.getRequestDate().equals(other.getRequestDate()))
                && (this.getRequestTime() == null ? other.getRequestTime() == null : this.getRequestTime().equals(other.getRequestTime()))
                && (this.getPurpose() == null ? other.getPurpose() == null : this.getPurpose().equals(other.getPurpose()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getPayMode() == null ? other.getPayMode() == null : this.getPayMode().equals(other.getPayMode()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()));
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
        result = prime * result + ((getPurpose() == null) ? 0 : getPurpose().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getPayMode() == null) ? 0 : getPayMode().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        return result;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {

        this.operOrg = anOperOrg;
    }

    @Override
    public void setCustNo(Long anCustNo) {

        this.supplierNo = anCustNo;
    }

    public void fillDefaultValue() {
        this.id = SerialGenerator.getLongValue("ScfCapitalFlow.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.buyerNo = this.coreCustNo;
        if (BetterStringUtils.shortString(this.requestDate, 12) == false) {
            this.requestTime = this.requestDate.substring(8).trim();
            this.requestDate = this.requestDate.substring(0, 8);
        }
        this.modiDate = BetterDateUtils.getNumDate();
        this.status="1";
        if ("01".equals(this.payMode)){
            this.payMode = "0";
        }
        else{
            this.payMode ="1";
        }
        this.supplierNo = MathExtend.defaultLongZero(this.supplierNo);
        this.requestNo = BetterStringUtils.defaultString(this.requestNo, " ");
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
    public void modifytValue() {
        
        this.modiDate = BetterDateUtils.getNumDate(); 
    }
    
    @Override
    public String findBankAccountName() {
        
        return this.supplier;
    }

}