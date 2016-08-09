package com.betterjr.modules.agreement.entity;

import java.math.BigDecimal;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
//import com.betterjr.common.mapper.CustDecimalJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_AGREEMENT")
public class CustAgreement implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;

    /**
     * 合同名称
     */
    @Column(name = "C_AGREENAME",  columnDefinition="VARCHAR" )
    @MetaData( value="合同名称", comments = "合同名称")
    private String agreeName;

    /**
     * 合同编号
     */
    @Column(name = "C_AGREENO",  columnDefinition="VARCHAR" )
    @MetaData( value="合同编号", comments = "合同编号")
    private String agreeNo;

    /**
     * 供应商（乙方）
     */
    @Column(name = "C_SUPPLIER",  columnDefinition="VARCHAR" )
    @MetaData( value="供应商（乙方）", comments = "供应商（乙方）")
    private String supplier;

    /**
     * 买方（甲方）
     */
    @Column(name = "C_BUYER",  columnDefinition="VARCHAR" )
    @MetaData( value="买方（甲方）", comments = "买方（甲方）")
    private String buyer;

    /**
     * 乙方联系人
     */
    @Column(name = "C_SUPPLIER_LINKMAN",  columnDefinition="VARCHAR" )
    @MetaData( value="乙方联系人", comments = "乙方联系人")
    private String supplierLinkman;

    /**
     * 甲方联系人
     */
    @Column(name = "C_BUYER_LINKMAN",  columnDefinition="VARCHAR" )
    @MetaData( value="甲方联系人", comments = "甲方联系人")
    private String buyerLinkman;

    /**
     * 合同金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="合同金额", comments = "合同金额")
    private BigDecimal balance;

    /**
     * 交付日期
     */
    @Column(name = "D_DELIVERY_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="交付日期", comments = "交付日期")
    private String deliveryDate;

    /**
     * 交付地点
     */
    @Column(name = "C_DELIVERY_ADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="交付地点", comments = "交付地点")
    private String deliveryAddr;

    /**
     * 验收方式
     */
    @Column(name = "C_CHECK_ACCEPT",  columnDefinition="VARCHAR" )
    @MetaData( value="验收方式", comments = "验收方式")
    private String checkAccept;

    /**
     * 提出异议期限
     */
    @Column(name = "C_OBJECTION_PERIOD",  columnDefinition="VARCHAR" )
    @MetaData( value="提出异议期限", comments = "提出异议期限")
    private String objectionPeriod;

    /**
     * 收款方银行账户
     */
    @Column(name = "C_BANKACCO",  columnDefinition="VARCHAR" )
    @MetaData( value="收款方银行账户", comments = "收款方银行账户")
    private String bankAccount;

    /**
     * 收款方银行全称
     */
    @Column(name = "C_BANKNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="收款方银行全称", comments = "收款方银行全称")
    private String bankName;

    /**
     * 收款方银行户名
     */
    @Column(name = "C_BANKACCONAME",  columnDefinition="VARCHAR" )
    @MetaData( value="收款方银行户名", comments = "收款方银行户名")
    private String bankAccountName;

    /**
     * 合同起始日期
     */
    @Column(name = "D_AGREE_START_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同起始日期", comments = "合同起始日期")
    private String agreeStartDate;

    /**
     * 合同截止日期
     */
    @Column(name = "D_AGREE_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同截止日期", comments = "合同截止日期")
    private String agreeEndDate;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 状态；0草稿，1启用，2终止
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态；0草稿，1启用，2终止")
    private String status;

    /**
     * 买方客户号
     */
    @Column(name = "L_BUYER_NO",  columnDefinition="INTEGER" )
    @MetaData( value="买方客户号", comments = "买方客户号")
    private Long buyerNo;

    /**
     * 卖方客户号
     */
    @Column(name = "L_SUPPLIER_NO",  columnDefinition="INTEGER" )
    @MetaData( value="卖方客户号", comments = "卖方客户号")
    private Long supplierNo;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员编码", comments = "操作员编码")
    private String operCode;

    /**
     * 操作员名字
     */
    @Column(name = "C_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员名字", comments = "操作员名字")
    private String operName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    private static final long serialVersionUID = 1458113450523L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreeName() {
        return agreeName;
    }

    public void setAgreeName(String agreeName) {
        this.agreeName = agreeName == null ? null : agreeName.trim();
    }

    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo == null ? null : agreeNo.trim();
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

    public String getSupplierLinkman() {
        return supplierLinkman;
    }

    public void setSupplierLinkman(String supplierLinkman) {
        this.supplierLinkman = supplierLinkman == null ? null : supplierLinkman.trim();
    }

    public String getBuyerLinkman() {
        return buyerLinkman;
    }

    public void setBuyerLinkman(String buyerLinkman) {
        this.buyerLinkman = buyerLinkman == null ? null : buyerLinkman.trim();
    }

//    @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate == null ? null : deliveryDate.trim();
    }

    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr == null ? null : deliveryAddr.trim();
    }

    public String getCheckAccept() {
        return checkAccept;
    }

    public void setCheckAccept(String checkAccept) {
        this.checkAccept = checkAccept == null ? null : checkAccept.trim();
    }

    public String getObjectionPeriod() {
        return objectionPeriod;
    }

    public void setObjectionPeriod(String objectionPeriod) {
        this.objectionPeriod = objectionPeriod == null ? null : objectionPeriod.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getAgreeStartDate() {
        return agreeStartDate;
    }

    public void setAgreeStartDate(String agreeStartDate) {
        this.agreeStartDate = agreeStartDate == null ? null : agreeStartDate.trim();
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getAgreeEndDate() {
        return agreeEndDate;
    }

    public void setAgreeEndDate(String agreeEndDate) {
        this.agreeEndDate = agreeEndDate == null ? null : agreeEndDate.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode == null ? null : operCode.trim();
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", agreeName=").append(agreeName);
        sb.append(", agreeNo=").append(agreeNo);
        sb.append(", supplier=").append(supplier);
        sb.append(", buyer=").append(buyer);
        sb.append(", supplierLinkman=").append(supplierLinkman);
        sb.append(", buyerLinkman=").append(buyerLinkman);
        sb.append(", balance=").append(balance);
        sb.append(", deliveryDate=").append(deliveryDate);
        sb.append(", deliveryAddr=").append(deliveryAddr);
        sb.append(", checkAccept=").append(checkAccept);
        sb.append(", objectionPeriod=").append(objectionPeriod);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankAccountName=").append(bankAccountName);
        sb.append(", agreeStartDate=").append(agreeStartDate);
        sb.append(", agreeEndDate=").append(agreeEndDate);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", status=").append(status);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", operCode=").append(operCode);
        sb.append(", operName=").append(operName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", batchNo=").append(batchNo);
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
        CustAgreement other = (CustAgreement) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgreeName() == null ? other.getAgreeName() == null : this.getAgreeName().equals(other.getAgreeName()))
            && (this.getAgreeNo() == null ? other.getAgreeNo() == null : this.getAgreeNo().equals(other.getAgreeNo()))
            && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getSupplierLinkman() == null ? other.getSupplierLinkman() == null : this.getSupplierLinkman().equals(other.getSupplierLinkman()))
            && (this.getBuyerLinkman() == null ? other.getBuyerLinkman() == null : this.getBuyerLinkman().equals(other.getBuyerLinkman()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getDeliveryDate() == null ? other.getDeliveryDate() == null : this.getDeliveryDate().equals(other.getDeliveryDate()))
            && (this.getDeliveryAddr() == null ? other.getDeliveryAddr() == null : this.getDeliveryAddr().equals(other.getDeliveryAddr()))
            && (this.getCheckAccept() == null ? other.getCheckAccept() == null : this.getCheckAccept().equals(other.getCheckAccept()))
            && (this.getObjectionPeriod() == null ? other.getObjectionPeriod() == null : this.getObjectionPeriod().equals(other.getObjectionPeriod()))
            && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankAccountName() == null ? other.getBankAccountName() == null : this.getBankAccountName().equals(other.getBankAccountName()))
            && (this.getAgreeStartDate() == null ? other.getAgreeStartDate() == null : this.getAgreeStartDate().equals(other.getAgreeStartDate()))
            && (this.getAgreeEndDate() == null ? other.getAgreeEndDate() == null : this.getAgreeEndDate().equals(other.getAgreeEndDate()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getOperCode() == null ? other.getOperCode() == null : this.getOperCode().equals(other.getOperCode()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgreeName() == null) ? 0 : getAgreeName().hashCode());
        result = prime * result + ((getAgreeNo() == null) ? 0 : getAgreeNo().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getSupplierLinkman() == null) ? 0 : getSupplierLinkman().hashCode());
        result = prime * result + ((getBuyerLinkman() == null) ? 0 : getBuyerLinkman().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getDeliveryDate() == null) ? 0 : getDeliveryDate().hashCode());
        result = prime * result + ((getDeliveryAddr() == null) ? 0 : getDeliveryAddr().hashCode());
        result = prime * result + ((getCheckAccept() == null) ? 0 : getCheckAccept().hashCode());
        result = prime * result + ((getObjectionPeriod() == null) ? 0 : getObjectionPeriod().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankAccountName() == null) ? 0 : getBankAccountName().hashCode());
        result = prime * result + ((getAgreeStartDate() == null) ? 0 : getAgreeStartDate().hashCode());
        result = prime * result + ((getAgreeEndDate() == null) ? 0 : getAgreeEndDate().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getOperCode() == null) ? 0 : getOperCode().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        return result;
    }
    
    public CustAgreement() {
        
    }

    public void modifyAgreement(CustAgreement anAgree){
        this.id = anAgree.getId();
        this.status = anAgree.getStatus();
        this.modiDate = BetterDateUtils.getNumDate();
        this.batchNo = anAgree.getBatchNo();
    }
    
    public void updateStatus(String anStatus){
       this.status = anStatus;
       this.modiDate = BetterDateUtils.getNumDate();
    }
    
    public void initDefValue(CustOperatorInfo anCustOperInfo, String anBuyer, Long anSupplierNo, String anSupplier) {
        this.id = SerialGenerator.getLongValue("CustAgreement.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
        this.status = "0";
        this.operOrg = anCustOperInfo.getOperOrg();
        this.operCode = anCustOperInfo.getOperCode();
        this.operName = anCustOperInfo.getName();
        this.buyer = anBuyer;
        this.supplierNo = anSupplierNo;
        this.supplier = anSupplier;
    }
    
}