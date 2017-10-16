package com.betterjr.modules.client.supplychian;

import java.io.Serializable;
import java.math.BigDecimal;

//核心企业对供应商付款流水记录
public class ScfCapitalFlowInfo implements Serializable {
    private static final long serialVersionUID = -81733220396442149L;

    /**
     * 拜特客户编号
     */
    private String btCustno;

    // 付款类型，01银行， 02票据
    private String payType;

    // 收款人名称
    private String supplier;

    // 收款方银行账户
    private String supplierBankAcco;

    // 收款方银行全称
    private String supplierBankName;

    // 付款人名称
    private String buyer;

    // 付款方银行账户
    private String buyerBankAcco;

    // 付款方银行全称
    private String buyerBankName;

    // 拜特内部编号
    private String btId;

    // 银行流水号
    private String applicationNo;

    // 付款金额
    private BigDecimal balance;

    // 支付时间
    private String payDate;

    // 摘要
    private String description;

    public String getBtCustno() {
        return this.btCustno;
    }

    public void setBtCustno(String anBtCustno) {
        this.btCustno = anBtCustno;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String anPayType) {
        this.payType = anPayType;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String anSupplier) {
        this.supplier = anSupplier;
    }

    public String getSupplierBankAcco() {
        return this.supplierBankAcco;
    }

    public void setSupplierBankAcco(String anSupplierBankAcco) {
        this.supplierBankAcco = anSupplierBankAcco;
    }

    public String getSupplierBankName() {
        return this.supplierBankName;
    }

    public void setSupplierBankName(String anSupplierBankName) {
        this.supplierBankName = anSupplierBankName;
    }

    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String anBuyer) {
        this.buyer = anBuyer;
    }

    public String getBuyerBankAcco() {
        return this.buyerBankAcco;
    }

    public void setBuyerBankAcco(String anBuyerBankAcco) {
        this.buyerBankAcco = anBuyerBankAcco;
    }

    public String getBuyerBankName() {
        return this.buyerBankName;
    }

    public void setBuyerBankName(String anBuyerBankName) {
        this.buyerBankName = anBuyerBankName;
    }

    public String getBtId() {
        return this.btId;
    }

    public void setBtId(String anBtId) {
        this.btId = anBtId;
    }

    public String getApplicationNo() {
        return this.applicationNo;
    }

    public void setApplicationNo(String anApplicationNo) {
        this.applicationNo = anApplicationNo;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public String getPayDate() {
        return this.payDate;
    }

    public void setPayDate(String anPayDate) {
        this.payDate = anPayDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

}
