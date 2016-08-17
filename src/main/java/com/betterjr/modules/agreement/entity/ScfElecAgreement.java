package com.betterjr.modules.agreement.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.data.NormalStatus;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_AGREEMENT")
public class ScfElecAgreement implements BetterjrEntity,BaseRemoteEntity {
    /**
     * 电子签署合同的申请单号，日期加流水号的形式
     */
    @Id
    @Column(name = "C_APPLICATIONNO",  columnDefinition="VARCHAR" )
    @MetaData( value="电子签署合同的申请单号", comments = "电子签署合同的申请单号，日期加流水号的形式")
    private String appNo;

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
     * 订单申请单号；对应T_SCF_REQUEST中的申请单号；或者业务系统中的订单信息
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="订单申请单号", comments = "订单申请单号；对应T_SCF_REQUEST中的申请单号；或者业务系统中的订单信息")
    private String requestNo;

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
     * 保理公司编码
     */
    @Column(name = "C_FACTORNO",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司编码", comments = "保理公司编码")
    private String factorNo;

    /**
     * 合同正式签署时间
     */
    @Column(name = "D_SIGN_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同正式签署时间", comments = "合同正式签署时间")
    private String signDate;

    /**
     * 未签署的合同的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="未签署的合同的批次号", comments = "未签署的合同的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 已签署的合同的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_SIGN_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="已签署的合同的批次号", comments = "已签署的合同的批次号，对应fileinfo中的ID")
    private Long signBatchNo;

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
     * 状态；0草稿，1签署完毕，2签署中，9作废
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态；0草稿，1签署完毕，2签署中，9作废")
    private String signStatus;

    /**
     * 电子合同类型，0：应收账款债权转让通知书，1：买方确认意见
     */
    @Column(name = "C_AGREETYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="电子合同类型", comments = "电子合同类型，0：应收账款债权转让通知书，1：买方确认意见")
    private String agreeType;

    /**
     * 合同金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="合同金额", comments = "合同金额")
    private BigDecimal balance;
    
    /**
     * 在电子合同签署服务方的编号
     */
    @Column(name = "C_SIGNID", columnDefinition = "VARCHAR")
    @MetaData(value = "在电子合同签署服务方的编号", comments = "在电子合同签署服务方的编号")
    private String signId;
    

    /**
     * 处理状态，0未 处理，1已处理；指是否发送到基金公司，2发送失败
     */
    @Column(name = "C_DEAL", columnDefinition = "VARCHAR")
    @MetaData(value = "处理状态", comments = "数据发送处理状态")
    private String dealFlag;
    
    /**
     * 供应商名称（卖方）
     */
    @Column(name = "C_SUPPLIER", columnDefinition = "VARCHAR")
    @MetaData(value = "供应商名称（卖方）", comments = "供应商名称（卖方）")
    private String supplier;
 
    private static final long serialVersionUID = 1461746080033L;

    @Transient
    private List<Map> signerlist;
    
    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
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

    public String getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(String factorNo) {
        this.factorNo = factorNo == null ? null : factorNo.trim();
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate == null ? null : signDate.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Long getSignBatchNo() {
        return signBatchNo;
    }

    public void setSignBatchNo(Long signBatchNo) {
        this.signBatchNo = signBatchNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }
 

    public String getSignStatus() {
        return this.signStatus;
    }

    public void setSignStatus(String anSignStatus) {
        this.signStatus = anSignStatus;
    }

    public String getAgreeType() {
        return agreeType;
    }

    public void setAgreeType(String agreeType) {
        this.agreeType = agreeType == null ? null : agreeType.trim();
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }
 

    public List<Map> getSignerlist() {
        return this.signerlist;
    }

    public void setSignerlist(List<Map> anSignerlist) {
        this.signerlist = anSignerlist;
    }

    public String getSignId() {
        return this.signId;
    }

    public void setSignId(String anSignId) {
        this.signId = anSignId;
    }

    public String getDealFlag() {
        return this.dealFlag;
    }

    public void setDealFlag(String anDealFlag) {
        this.dealFlag = anDealFlag;
    }

    public boolean hasSendSignOrder(){

        return "1".equals(this.getDealFlag()) == false;
    }
    
    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String anSupplier) {
        this.supplier = anSupplier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" appNo=").append(appNo);
        sb.append(", agreeName=").append(agreeName);
        sb.append(", agreeNo=").append(agreeNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", signDate=").append(signDate);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", signBatchNo=").append(signBatchNo);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", signStatus=").append(signStatus);
        sb.append(", agreeType=").append(agreeType);
        sb.append(", balance=").append(balance);
        sb.append(", signId=").append(signId);
        sb.append(", signerlist=").append(signerlist);
        sb.append(", dealFlag=").append(dealFlag);
        sb.append(", supplier=").append(supplier);
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
        ScfElecAgreement other = (ScfElecAgreement) that;
        return (this.getAppNo() == null ? other.getAppNo() == null : this.getAppNo().equals(other.getAppNo()))
            && (this.getAgreeName() == null ? other.getAgreeName() == null : this.getAgreeName().equals(other.getAgreeName()))
            && (this.getAgreeNo() == null ? other.getAgreeNo() == null : this.getAgreeNo().equals(other.getAgreeNo()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getSignDate() == null ? other.getSignDate() == null : this.getSignDate().equals(other.getSignDate()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getSignBatchNo() == null ? other.getSignBatchNo() == null : this.getSignBatchNo().equals(other.getSignBatchNo()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getSignStatus() == null ? other.getSignStatus() == null : this.getSignStatus().equals(other.getSignStatus()))
            && (this.getDealFlag() == null ? other.getDealFlag() == null : this.getDealFlag().equals(other.getDealFlag()))
            && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
            && (this.getAgreeType() == null ? other.getAgreeType() == null : this.getAgreeType().equals(other.getAgreeType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAppNo() == null) ? 0 : getAppNo().hashCode());
        result = prime * result + ((getAgreeName() == null) ? 0 : getAgreeName().hashCode());
        result = prime * result + ((getAgreeNo() == null) ? 0 : getAgreeNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getSignDate() == null) ? 0 : getSignDate().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getSignBatchNo() == null) ? 0 : getSignBatchNo().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getSignStatus() == null) ? 0 : getSignStatus().hashCode());
        result = prime * result + ((getAgreeType() == null) ? 0 : getAgreeType().hashCode());
        result = prime * result + ((getDealFlag() == null) ? 0 : getDealFlag().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
       return result;
    }
    
    /**
     * 更新未签署的电子合同的信息
     * @param anElecAgree
     */
    public void updateInfo(ScfElecAgreement anElecAgree){
        this.agreeName = anElecAgree.agreeName;
        this.agreeNo = anElecAgree.agreeNo;
        this.buyerNo = anElecAgree.buyerNo;
        this.supplierNo = anElecAgree.supplierNo;
    }
    
    /**
     * 更新电子合同状态，如果是成功签署；则更新合同签署日期，否则之更新修改时间。
     * @param anStatus
     */
    public void fillElecAgreeStatus(String anStatus){
        this.signStatus = anStatus;
        if (NormalStatus.VALID_STATUS.value.equals(anStatus)) {
            this.setSignDate(BetterDateUtils.getNumDate());
        }
        this.setModiDate(BetterDateUtils.getNumDate());
    }

    /**
     * 根据转让申请书，产生转让电子合同信息
     * @param anRequest 申请单
     * @param anAgreeName 电子合同名称
     * @param anAgreeNo 电子合同编号
     * @param anBalance 电子合同金额
     * @return
     */
    public static ScfElecAgreement createByNotice(String anAgreeName, String anAgreeNo, BigDecimal anBalance) {
        ScfElecAgreement elecAgreement = new ScfElecAgreement();
        elecAgreement.fillBaseInfo(anAgreeName, anAgreeNo, "0", anBalance);

        return elecAgreement;
    }

    /**
     * 根据核心企业确认书，产生企业确认电子合同信息
     * @param anRequest 申请单
     * @param anAgreeName 电子合同名称
     * @param anAgreeNo 电子合同编号
     * @param anBalance 电子合同金额
     * @return
     */
    public static ScfElecAgreement createByOpinion(String anAgreeName, String anAgreeNo,  BigDecimal anBalance) {
        ScfElecAgreement elecAgreement = new ScfElecAgreement();
        elecAgreement.fillBaseInfo(anAgreeName, anAgreeNo, "1", anBalance);
        
        return elecAgreement; 
    }
    

    /**
     * 添加三方协议
     * @param anRequest 申请单
     * @param anAgreeName 电子合同名称
     * @param anAgreeNo 电子合同编号
     * @param anBalance 电子合同金额
     */
    public static ScfElecAgreement createByProtacal(String anAgreeName, String anAgreeNo,  BigDecimal anBalance) {
        ScfElecAgreement elecAgreement = new ScfElecAgreement();
        elecAgreement.fillBaseInfo(anAgreeName, anAgreeNo, "2", anBalance);
        
        return elecAgreement; 
    }
    
    private void fillBaseInfo(String anAgreeName, String anAgreeNo, String anAgreeType, BigDecimal anBalance){
        this.appNo = BetterDateUtils.getNumDate().concat(Long.toHexString(SerialGenerator.getLongValue("ScfElecAgreement.id")));
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = this.regDate;
        this.agreeName = anAgreeName;
        this.agreeNo = anAgreeNo;
        this.signStatus = "0";
        this.agreeType = anAgreeType;
        this.balance = anBalance;
        this.batchNo = 0L;
        this.signBatchNo = 0L;
        this.dealFlag ="0";
    }
}