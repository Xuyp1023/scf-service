package com.betterjr.modules.agreement.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.loan.entity.ScfRequest;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_REQUEST_NOTICE")
public class ScfRequestNotice implements BetterjrEntity,BaseRemoteEntity {
    /**
     * 申请单号
     */
    @Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请单号", comments = "申请单号")
    private String requestNo;

    /**
     * 保理产品编号
     */
    @Column(name = "C_PRODUCT_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="保理产品编号", comments = "保理产品编号")
    private String productCode;

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
     * 转让通知书编号
     */
    @Column(name = "C_NOTICENO",  columnDefinition="VARCHAR" )
    @MetaData( value="转让通知书编号", comments = "转让通知书编号")
    private String noticeNo;

    /**
     * 买方（甲方）
     */
    @Column(name = "C_BUYER",  columnDefinition="VARCHAR" )
    @MetaData( value="买方（甲方）", comments = "买方（甲方）")
    private String buyer;

    /**
     * 合同名称
     */
    @Column(name = "C_AGREENAME",  columnDefinition="VARCHAR" )
    @MetaData( value="合同名称", comments = "合同名称")
    private String agreeName;

    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司名称", comments = "保理公司名称")
    private String factorName;

    /**
     * 保理公司编码
     */
    @Column(name = "C_FACTORNO",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司编码", comments = "保理公司编码")
    private String factorNo;

    /**
     * 保理公司详细地址
     */
    @Column(name = "C_FACTORADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司详细地址", comments = "保理公司详细地址")
    private String factorAddr;

    /**
     * 保理公司邮编
     */
    @Column(name = "C_FACTORAPOST",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司邮编", comments = "保理公司邮编")
    private String factorPost;

    /**
     * 保理公司联系人
     */
    @Column(name = "C_FACTORA_LINKMAN",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司联系人", comments = "保理公司联系人")
    private String factorLinkMan;

    /**
     * 实际申请日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="实际申请日期", comments = "实际申请日期")
    private String modiDate;

    /**
     * 申请时间
     */
    @Column(name = "T_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="申请时间", comments = "申请时间")
    private String noticeTime;

    /**
     * 保理公司业务单号
     */
    @Column(name = "C_FACTOR_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司业务单号", comments = "保理公司业务单号")
    private String factorRequestNo;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 申请状态；0未处理，1已经签署，2供应商签署，3保理公司签署
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="申请状态", comments = "申请状态；0未处理，1已经签署，2供应商签署，3保理公司签署")
    private String transStatus;

    /**
     * 保理公司银行账户
     */
    @Column(name = "C_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司银行账户", comments = "保理公司银行账户")
    private String bankAccount;
    
    private static final long serialVersionUID = 1459952422046L;

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

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo == null ? null : noticeNo.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public String getAgreeName() {
        return agreeName;
    }

    public void setAgreeName(String agreeName) {
        this.agreeName = agreeName == null ? null : agreeName.trim();
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName == null ? null : factorName.trim();
    }

    public String getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(String factorNo) {
        this.factorNo = factorNo == null ? null : factorNo.trim();
    }

    public String getFactorAddr() {
        return factorAddr;
    }

    public void setFactorAddr(String factorAddr) {
        this.factorAddr = factorAddr == null ? null : factorAddr.trim();
    }

    public String getFactorPost() {
        return factorPost;
    }

    public void setFactorPost(String factorPost) {
        this.factorPost = factorPost == null ? null : factorPost.trim();
    }

    public String getFactorLinkMan() {
        return factorLinkMan;
    }

    public void setFactorLinkMan(String factorLinkMan) {
        this.factorLinkMan = factorLinkMan == null ? null : factorLinkMan.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime == null ? null : noticeTime.trim();
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

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(String anBankAccount) {
        this.bankAccount = anBankAccount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", requestNo=").append(requestNo);
        sb.append(", productCode=").append(productCode);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", noticeNo=").append(noticeNo);
        sb.append(", buyer=").append(buyer);
        sb.append(", agreeName=").append(agreeName);
        sb.append(", factorName=").append(factorName);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", factorAddr=").append(factorAddr);
        sb.append(", factorPost=").append(factorPost);
        sb.append(", factorLinkMan=").append(factorLinkMan);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", noticeTime=").append(noticeTime);
        sb.append(", factorRequestNo=").append(factorRequestNo);
        sb.append(", regDate=").append(regDate);
        sb.append(", transStatus=").append(transStatus);
        sb.append(", bankAccount=").append(bankAccount);
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
        ScfRequestNotice other = (ScfRequestNotice) that;
        return (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getNoticeNo() == null ? other.getNoticeNo() == null : this.getNoticeNo().equals(other.getNoticeNo()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getAgreeName() == null ? other.getAgreeName() == null : this.getAgreeName().equals(other.getAgreeName()))
            && (this.getFactorName() == null ? other.getFactorName() == null : this.getFactorName().equals(other.getFactorName()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getFactorAddr() == null ? other.getFactorAddr() == null : this.getFactorAddr().equals(other.getFactorAddr()))
            && (this.getFactorPost() == null ? other.getFactorPost() == null : this.getFactorPost().equals(other.getFactorPost()))
            && (this.getFactorLinkMan() == null ? other.getFactorLinkMan() == null : this.getFactorLinkMan().equals(other.getFactorLinkMan()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getNoticeTime() == null ? other.getNoticeTime() == null : this.getNoticeTime().equals(other.getNoticeTime()))
            && (this.getFactorRequestNo() == null ? other.getFactorRequestNo() == null : this.getFactorRequestNo().equals(other.getFactorRequestNo()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getTransStatus() == null ? other.getTransStatus() == null : this.getTransStatus().equals(other.getTransStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getNoticeNo() == null) ? 0 : getNoticeNo().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getAgreeName() == null) ? 0 : getAgreeName().hashCode());
        result = prime * result + ((getFactorName() == null) ? 0 : getFactorName().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getFactorAddr() == null) ? 0 : getFactorAddr().hashCode());
        result = prime * result + ((getFactorPost() == null) ? 0 : getFactorPost().hashCode());
        result = prime * result + ((getFactorLinkMan() == null) ? 0 : getFactorLinkMan().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getNoticeTime() == null) ? 0 : getNoticeTime().hashCode());
        result = prime * result + ((getFactorRequestNo() == null) ? 0 : getFactorRequestNo().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getTransStatus() == null) ? 0 : getTransStatus().hashCode());
        return result;
    }
    
    public void fillInfo(ScfRequest anRequest){
        this.productCode = anRequest.getProductCode();
        this.buyerNo = anRequest.getBuyerNo()==null?0:Long.parseLong(anRequest.getBuyerNo());
        this.supplierNo = anRequest.getSupplierNo();
        this.factorNo = anRequest.getFactorNo()==null?null:anRequest.getFactorNo().toString();
        this.transStatus ="0";
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
     }
}