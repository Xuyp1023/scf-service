package com.betterjr.modules.agreement.entity;

import java.math.BigDecimal;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_REQUEST_CREDIT")
public class ScfRequestCredit implements BetterjrEntity, BaseRemoteEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Integer id;

    /**
     * 申请单号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请单号", comments = "申请单号")
    private String requestNo;

    /**
     * 保理产品编号
     */
    @Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "保理产品编号", comments = "保理产品编号")
    private String productCode;

    /**
     * 买方客户号
     */
    @Column(name = "L_BUYER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "买方客户号", comments = "买方客户号")
    private Long buyerNo;

    /**
     * 卖方客户号
     */
    @Column(name = "L_SUPPLIER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "卖方客户号", comments = "卖方客户号")
    private Long supplierNo;

    /**
     * 转让通知书编号
     */
    @Column(name = "C_NOTICENO", columnDefinition = "VARCHAR")
    @MetaData(value = "转让通知书编号", comments = "转让通知书编号")
    private String confirmNo;

    /**
     * 应收账款转让编号
     */
    @Column(name = "C_TRANSNO", columnDefinition = "VARCHAR")
    @MetaData(value = "应收账款转让编号", comments = "应收账款转让编号")
    private String transNo;

    /**
     * 融资金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "融资金额", comments = "融资金额")
    private BigDecimal balance;

    /**
     * 到期日期
     */
    @Column(name = "D_END_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "到期日期", comments = "到期日期")
    private String endDate;

    /**
     * 合同编号
     */
    @Column(name = "C_AGREENO", columnDefinition = "VARCHAR")
    @MetaData(value = "合同编号", comments = "合同编号")
    private String agreeNo;

    /**
     * 发票号
     */
    @Column(name = "C_INVOICE_NO", columnDefinition = "VARCHAR")
    @MetaData(value = "发票号", comments = "发票号")
    private String invoiceNo;

    /**
     * 发票金额
     */
    @Column(name = "F_INVOICE_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "发票金额", comments = "发票金额")
    private BigDecimal invoiceBalance;

    /**
     * 保理公司业务单号
     */
    @Column(name = "C_FACTOR_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司业务单号", comments = "保理公司业务单号")
    private String factorRequestNo;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    private static final long serialVersionUID = 1459952422046L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
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

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo == null ? null : confirmNo.trim();
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo == null ? null : agreeNo.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(BigDecimal invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    public String getFactorRequestNo() {
        return factorRequestNo;
    }

    public void setFactorRequestNo(String factorRequestNo) {
        this.factorRequestNo = factorRequestNo == null ? null : factorRequestNo.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getTransNo() {

        return this.transNo;
    }

    public void setTransNo(String anTransNo) {
        this.transNo = anTransNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" id=").append(id);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", productCode=").append(productCode);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", confirmNo=").append(confirmNo);
        sb.append(", balance=").append(balance);
        sb.append(", endDate=").append(endDate);
        sb.append(", agreeNo=").append(agreeNo);
        sb.append(", invoiceNo=").append(invoiceNo);
        sb.append(", invoiceBalance=").append(invoiceBalance);
        sb.append(", factorRequestNo=").append(factorRequestNo);
        sb.append(", regDate=").append(regDate);
        sb.append(", transNo=").append(transNo);
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
        ScfRequestCredit other = (ScfRequestCredit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null
                        : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getProductCode() == null ? other.getProductCode() == null
                        : this.getProductCode().equals(other.getProductCode()))
                && (this.getBuyerNo() == null ? other.getBuyerNo() == null
                        : this.getBuyerNo().equals(other.getBuyerNo()))
                && (this.getSupplierNo() == null ? other.getSupplierNo() == null
                        : this.getSupplierNo().equals(other.getSupplierNo()))
                && (this.getConfirmNo() == null ? other.getConfirmNo() == null
                        : this.getConfirmNo().equals(other.getConfirmNo()))
                && (this.getBalance() == null ? other.getBalance() == null
                        : this.getBalance().equals(other.getBalance()))
                && (this.getEndDate() == null ? other.getEndDate() == null
                        : this.getEndDate().equals(other.getEndDate()))
                && (this.getAgreeNo() == null ? other.getAgreeNo() == null
                        : this.getAgreeNo().equals(other.getAgreeNo()))
                && (this.getInvoiceNo() == null ? other.getInvoiceNo() == null
                        : this.getInvoiceNo().equals(other.getInvoiceNo()))
                && (this.getInvoiceBalance() == null ? other.getInvoiceBalance() == null
                        : this.getInvoiceBalance().equals(other.getInvoiceBalance()))
                && (this.getFactorRequestNo() == null ? other.getFactorRequestNo() == null
                        : this.getFactorRequestNo().equals(other.getFactorRequestNo()))
                && (this.getTransNo() == null ? other.getTransNo() == null
                        : this.getTransNo().equals(other.getTransNo()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getConfirmNo() == null) ? 0 : getConfirmNo().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getAgreeNo() == null) ? 0 : getAgreeNo().hashCode());
        result = prime * result + ((getInvoiceNo() == null) ? 0 : getInvoiceNo().hashCode());
        result = prime * result + ((getInvoiceBalance() == null) ? 0 : getInvoiceBalance().hashCode());
        result = prime * result + ((getFactorRequestNo() == null) ? 0 : getFactorRequestNo().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getTransNo() == null) ? 0 : getTransNo().hashCode());
        return result;
    }

    public void fillInfo(ScfRequest anRequest) {
        this.id = SerialGenerator.getIntValue("ScfRequestCredit.id");
        this.productCode = anRequest.getProductCode();
        this.buyerNo = anRequest.getCoreCustNo() == null ? 0 : anRequest.getCoreCustNo();
        this.supplierNo = anRequest.getCustNo();
        this.regDate = BetterDateUtils.getNumDate();
    }
}