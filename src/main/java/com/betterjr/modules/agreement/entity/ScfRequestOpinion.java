package com.betterjr.modules.agreement.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.loan.entity.ScfRequest;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_REQUEST_OPINION")
public class ScfRequestOpinion implements BetterjrEntity, BaseRemoteEntity {
    /**
     * 申请单号
     */
    @Id
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
     * 确认书编号
     */
    @Column(name = "C_CONFIRMNO", columnDefinition = "VARCHAR")
    @MetaData(value = "确认书编号", comments = "确认书编号")
    private String confirmNo;

    /**
     * 买方（甲方）
     */
    @Column(name = "C_SUPPLIER", columnDefinition = "VARCHAR")
    @MetaData(value = "买方（甲方）", comments = "买方（甲方）")
    private String supplier;

    /**
     * 合同名称
     */
    @Column(name = "C_AGREENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "合同名称", comments = "合同名称")
    private String agreeName;

    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司名称", comments = "保理公司名称")
    private String factorName;

    /**
     * 保理公司编码
     */
    @Column(name = "C_FACTORNO", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司编码", comments = "保理公司编码")
    private String factorNo;

    /**
     * 实际申请日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "实际申请日期", comments = "实际申请日期")
    private String modiDate;

    /**
     * 确认书状态；0未处理，1已经签署，2拒绝
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "确认书状态", comments = "确认书状态；0未处理，1已经签署，2拒绝")
    private String opinionStatus;

    /**
     * 申请时间
     */
    @Column(name = "T_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "申请时间", comments = "申请时间")
    private String opinionTime;

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

    /**
     * 转让通知书编号
     */
    @Column(name = "C_NOTICENO", columnDefinition = "VARCHAR")
    @MetaData(value = "转让通知书编号", comments = "转让通知书编号")
    private String noticeNo;

    /**
     * 联系人
     */
    @Column(name = "C_LINKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人", comments = "联系人")
    private String linkName;

    /**
     * 电子邮箱
     */
    @Column(name = "C_EMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "电子邮箱", comments = "电子邮箱")
    private String email;

    /**
     * 联系电话
     */
    @Column(name = "C_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "联系电话", comments = "联系电话")
    private String phone;

    /***
     * 买方名称
     */
    @Transient
    private String buyerName;

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

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo == null ? null : confirmNo.trim();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
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

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getOpinionStatus() {
        return opinionStatus;
    }

    public void setOpinionStatus(String opinionStatus) {
        this.opinionStatus = opinionStatus == null ? null : opinionStatus.trim();
    }

    public String getOpinionTime() {
        return opinionTime;
    }

    public void setOpinionTime(String opinionTime) {
        this.opinionTime = opinionTime == null ? null : opinionTime.trim();
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

    public String getNoticeNo() {
        return this.noticeNo;
    }

    public void setNoticeNo(String anNoticeNo) {
        this.noticeNo = anNoticeNo;
    }

    public String getLinkName() {
        return this.linkName;
    }

    public void setLinkName(String anLinkName) {
        this.linkName = anLinkName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String anEmail) {
        this.email = anEmail;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String anPhone) {
        this.phone = anPhone;
    }

    public String getBuyerName() {
        return this.buyerName;
    }

    public void setBuyerName(String anBuyerName) {
        this.buyerName = anBuyerName;
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
        sb.append(", confirmNo=").append(confirmNo);
        sb.append(", supplier=").append(supplier);
        sb.append(", agreeName=").append(agreeName);
        sb.append(", factorName=").append(factorName);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", opinionStatus=").append(opinionStatus);
        sb.append(", opinionTime=").append(opinionTime);
        sb.append(", factorRequestNo=").append(factorRequestNo);
        sb.append(", regDate=").append(regDate);
        sb.append(", noticeNo=").append(noticeNo);
        sb.append(", linkName=").append(linkName);
        sb.append(", email=").append(email);
        sb.append(", phone=").append(phone);
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
        ScfRequestOpinion other = (ScfRequestOpinion) that;
        return (this.getRequestNo() == null ? other.getRequestNo() == null
                : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getProductCode() == null ? other.getProductCode() == null
                        : this.getProductCode().equals(other.getProductCode()))
                && (this.getBuyerNo() == null ? other.getBuyerNo() == null
                        : this.getBuyerNo().equals(other.getBuyerNo()))
                && (this.getSupplierNo() == null ? other.getSupplierNo() == null
                        : this.getSupplierNo().equals(other.getSupplierNo()))
                && (this.getConfirmNo() == null ? other.getConfirmNo() == null
                        : this.getConfirmNo().equals(other.getConfirmNo()))
                && (this.getSupplier() == null ? other.getSupplier() == null
                        : this.getSupplier().equals(other.getSupplier()))
                && (this.getAgreeName() == null ? other.getAgreeName() == null
                        : this.getAgreeName().equals(other.getAgreeName()))
                && (this.getFactorName() == null ? other.getFactorName() == null
                        : this.getFactorName().equals(other.getFactorName()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null
                        : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getOpinionStatus() == null ? other.getOpinionStatus() == null
                        : this.getOpinionStatus().equals(other.getOpinionStatus()))
                && (this.getOpinionTime() == null ? other.getOpinionTime() == null
                        : this.getOpinionTime().equals(other.getOpinionTime()))
                && (this.getFactorRequestNo() == null ? other.getFactorRequestNo() == null
                        : this.getFactorRequestNo().equals(other.getFactorRequestNo()))
                && (this.getNoticeNo() == null ? other.getNoticeNo() == null
                        : this.getNoticeNo().equals(other.getNoticeNo()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getConfirmNo() == null) ? 0 : getConfirmNo().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
        result = prime * result + ((getAgreeName() == null) ? 0 : getAgreeName().hashCode());
        result = prime * result + ((getFactorName() == null) ? 0 : getFactorName().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getOpinionStatus() == null) ? 0 : getOpinionStatus().hashCode());
        result = prime * result + ((getOpinionTime() == null) ? 0 : getOpinionTime().hashCode());
        result = prime * result + ((getFactorRequestNo() == null) ? 0 : getFactorRequestNo().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getNoticeNo() == null) ? 0 : getNoticeNo().hashCode());
        return result;
    }

    public void fillInfo(ScfRequest anRequest) {
        this.confirmNo = String.valueOf(SerialGenerator.findAppNoWithDayAndType(anRequest.getFactorNo(), "QEQHTZQR"));
        this.productCode = anRequest.getProductCode();
        this.buyerNo = anRequest.getCoreCustNo() == null ? 0 : anRequest.getCoreCustNo();
        this.supplierNo = anRequest.getCustNo();
        this.factorNo = anRequest.getFactorNo() == null ? null : anRequest.getFactorNo().toString();
        this.opinionStatus = "0";
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
        this.requestNo = anRequest.getRequestNo();
    }
}