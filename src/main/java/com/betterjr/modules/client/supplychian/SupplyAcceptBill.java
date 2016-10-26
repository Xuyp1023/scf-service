package com.betterjr.modules.client.supplychian;

import java.math.BigDecimal;

public class SupplyAcceptBill implements java.io.Serializable {

    /**
     * 拜特客户编号
     */
    private String btCustno;

    /**
     * 票据编号
     */
    private String billno;

    /**
     * 票据类型;0：商业票据，1：银行票据
     */
    private String billType;

    /**
     * 票据流通方式;0：纸票，1：电票
     */
    private String billMode;

    /**
     * 确定的金额
     */
    private BigDecimal balance;

    /**
     * 开票日期
     */
    private String invoiceDate;

    /**
     * 到期日期
     */
    private String endDate;

    /**
     * 兑付日期
     */
    private String cashDate;

    /**
     * 收款人名称
     */
    private String supplier;

    /**
     * 收款方银行账户
     */
    private String supplierBankacco;

    /**
     * 收款方银行全称
     */
    private String supplierBankname;

    /**
     * 付款人名称
     */
    private String buyer;

    /**
     * 实际付款人名称
     */
    private String buyerReal;

    /**
     * 付款方银行账户
     */
    private String buyerBankacco;

    /**
     * 付款方银行全称
     */
    private String buyerBankname;

    /**
     * 登记日期
     */
    private String regDate;

    /**
     * 拜特内部编号
     */
    private String btId;

    /**
     * 买方客户号
     */
    private Long buyerNo;

    /**
     * 卖方客户号
     */
    private Long supplierNo;

    // 票据状态，1表示新开出票据，0表示未用退票
    private String status;

    // 开票人ID
    private String drawerId;

    // 开票人名称
    private String drawerName;

    // 承兑人名称
    private String acceptor;

    // 承兑人账户
    private String acceptorAcc;

    // 持票人名称
    private String holder;

    // 持票人账户
    private String holderAcc;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String anStatus) {
        this.status = anStatus;
    }

    public String getBtCustno() {
        return this.btCustno;
    }

    public void setBtCustno(String anBtCustno) {
        this.btCustno = anBtCustno;
    }

    private static final long serialVersionUID = 1458262605550L;

    public String getBillno() {
        return this.billno;
    }

    public void setBillno(String anBillno) {
        this.billno = anBillno;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType == null ? null : billType.trim();
    }

    public String getBillMode() {
        return billMode;
    }

    public void setBillMode(String billMode) {
        this.billMode = billMode == null ? null : billMode.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate == null ? null : invoiceDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getCashDate() {
        return cashDate;
    }

    public void setCashDate(String cashDate) {
        this.cashDate = cashDate == null ? null : cashDate.trim();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public Long getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(Long buyerNo) {
        this.buyerNo = buyerNo;
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getBuyerReal() {
        return this.buyerReal;
    }

    public void setBuyerReal(String anBuyerReal) {
        this.buyerReal = anBuyerReal;
    }

    public String getSupplierBankacco() {
        return this.supplierBankacco;
    }

    public void setSupplierBankacco(String anSupplierBankacco) {
        this.supplierBankacco = anSupplierBankacco;
    }

    public String getSupplierBankname() {
        return this.supplierBankname;
    }

    public void setSupplierBankname(String anSupplierBankname) {
        this.supplierBankname = anSupplierBankname;
    }

    public String getBuyerBankacco() {
        return this.buyerBankacco;
    }

    public void setBuyerBankacco(String anBuyerBankacco) {
        this.buyerBankacco = anBuyerBankacco;
    }

    public String getBuyerBankname() {
        return this.buyerBankname;
    }

    public void setBuyerBankname(String anBuyerBankname) {
        this.buyerBankname = anBuyerBankname;
    }

    public String getBtId() {
        return this.btId;
    }

    public void setBtId(String anBtId) {
        this.btId = anBtId;
    }

    public String getDrawerId() {
        return this.drawerId;
    }

    public void setDrawerId(String anDrawerId) {
        this.drawerId = anDrawerId;
    }

    public String getDrawerName() {
        return this.drawerName;
    }

    public void setDrawerName(String anDrawerName) {
        this.drawerName = anDrawerName;
    }

    public String getAcceptor() {
        return this.acceptor;
    }

    public void setAcceptor(String anAcceptor) {
        this.acceptor = anAcceptor;
    }

    public String getAcceptorAcc() {
        return this.acceptorAcc;
    }

    public void setAcceptorAcc(String anAcceptorAcc) {
        this.acceptorAcc = anAcceptorAcc;
    }

    public String getHolder() {
        return this.holder;
    }

    public void setHolder(String anHolder) {
        this.holder = anHolder;
    }

    public String getHolderAcc() {
        return this.holderAcc;
    }

    public void setHolderAcc(String anHolderAcc) {
        this.holderAcc = anHolderAcc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", billno=").append(billno);
        sb.append(", billType=").append(billType);
        sb.append(", billMode=").append(billMode);
        sb.append(", balance=").append(balance);
        sb.append(", invoiceDate=").append(invoiceDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", cashDate=").append(cashDate);
        sb.append(", supplier=").append(supplier);
        sb.append(", supplierBankacco=").append(supplierBankacco);
        sb.append(", supplierBankname=").append(supplierBankname);
        sb.append(", buyer=").append(buyer);
        sb.append(", buyerReal=").append(buyerReal);
        sb.append(", buyerBankacco=").append(buyerBankacco);
        sb.append(", buyerBankname=").append(buyerBankname);
        sb.append(", regDate=").append(regDate);
        sb.append(", btId=").append(btId);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", status=").append(status);
        sb.append(", drawerId=").append(drawerId);
        sb.append(", drawerName=").append(drawerName);
        sb.append(", acceptor=").append(acceptor);
        sb.append(", acceptorAcc=").append(acceptorAcc);
        sb.append(", holder=").append(holder);
        sb.append(", holderAcc=").append(holderAcc);
        sb.append("]");
        return sb.toString();
    }

}