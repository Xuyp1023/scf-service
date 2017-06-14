package com.betterjr.modules.ledger.entity;

import java.util.ArrayList;
import java.util.List;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_contract_ledger")
public class ContractLedger  implements BetterjrEntity {
    @Id
    @Column(name = "id",  columnDefinition="INTEGER" )
    private Long id;

    @Column(name = "c_agreename",  columnDefinition="VARCHAR" )
    private String agreeName;

    @Column(name = "c_agreeno",  columnDefinition="VARCHAR" )
    private String agreeNo;

    @Column(name = "c_supplier",  columnDefinition="VARCHAR" )
    private String supplier;

    @Column(name = "c_buyer",  columnDefinition="VARCHAR" )
    private String buyer;

    @Column(name = "f_balance",  columnDefinition="DECIMAL" )
    private double balance;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_delivery_date",  columnDefinition="VARCHAR" )
    private String deliveryDate;

    @Column(name = "c_delivery_addr",  columnDefinition="VARCHAR" )
    private String deliveryAddr;

    @Column(name = "c_check_accept",  columnDefinition="VARCHAR" )
    private String checkAccept;

    @Column(name = "c_objection_period",  columnDefinition="VARCHAR" )
    private String objectionPeriod;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_agree_start_date",  columnDefinition="VARCHAR" )
    private String agreeStartDate;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_agree_end_date",  columnDefinition="VARCHAR" )
    private String agreeEndDate;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_regdate",  columnDefinition="VARCHAR" )
    private String regDate;

    @Column(name = "d_regtime",  columnDefinition="VARCHAR" )
    private String regTime;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_modidate",  columnDefinition="VARCHAR" )
    private String modiDate;

    @Column(name = "d_moditime",  columnDefinition="VARCHAR" )
    private String modiTime;

    @Column(name = "c_busin_status",  columnDefinition="VARCHAR" )
    private String businStatus;

    @Column(name = "l_buyer_no",  columnDefinition="INTEGER" )
    private Long buyerNo;

    @Column(name = "l_supplier_no",  columnDefinition="INTEGER" )
    private Long supplierNo;

    @Column(name = "l_operId",  columnDefinition="INTEGER" )
    private Long operId;

    @Column(name = "c_opername",  columnDefinition="VARCHAR" )
    private String operName;

    @Column(name = "c_operorg",  columnDefinition="VARCHAR" )
    private String operOrg;

    @Column(name = "n_batchno",  columnDefinition="INTEGER" )
    private Long batchNo;

    @Column(name = "c_default",  columnDefinition="VARCHAR" )
    private String defaultFlag;

    @Column(name = "c_describe",  columnDefinition="VARCHAR" )
    private String des;
    
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "d_signdate",  columnDefinition="VARCHAR" )
    private String signDate;
    
    @Column(name = "c_sign_addr",  columnDefinition="VARCHAR" )
    private String signAddr;
    
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "供应商客户号", comments = "供应商客户号")
    private Long custNo;
    
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "供应商名称", comments = "供应商名称")
    private String custName;
    
    
    @Column(name = "C_LOCKED_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "0 未锁定状态 1 锁定状态", comments = "0 未锁定状态 1 锁定状态")
    private String lockedStatus;
    
    @Column(name = "C_Ref_NO", columnDefinition = "VARCHAR")
    @MetaData(value = "凭证编号", comments = "凭证编号")
    private String refNo;
    
    
    @Column(name = "N_VERSION", columnDefinition = "VARCHAR")
    @MetaData(value = "版本", comments = "版本")
    private String version;
    
    @Column(name = "C_IS_LATEST", columnDefinition = "VARCHAR")
    @MetaData(value = "是否是最新", comments = "是否是最新")
    private String isLatest;
    
    //0 未核准 1：核准  2：已使用 3：转让 4废止 5 过期
    @Column(name = "c_busin_version_status",  columnDefinition="VARCHAR" )
    private String businVersionStatus;
    
    private List<CustFileItem> custFileList=new ArrayList<CustFileItem>();

    private static final long serialVersionUID = -2337760108713768519L;

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
        this.agreeName = agreeName;
    }

    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

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
        this.deliveryAddr = deliveryAddr;
    }

    public String getCheckAccept() {
        return checkAccept;
    }

    public void setCheckAccept(String checkAccept) {
        this.checkAccept = checkAccept;
    }

    public String getObjectionPeriod() {
        return objectionPeriod;
    }

    public void setObjectionPeriod(String objectionPeriod) {
        this.objectionPeriod = objectionPeriod;
    }

    public String getAgreeStartDate() {
        return agreeStartDate;
    }

    public void setAgreeStartDate(String agreeStartDate) {
        this.agreeStartDate = agreeStartDate == null ? null : agreeStartDate.trim();
    }

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
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }
    
    public String getBusinVersionStatus() {
        return this.businVersionStatus;
    }

    public void setBusinVersionStatus(String anBusinVersionStatus) {
        this.businVersionStatus = anBusinVersionStatus;
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

    public Long getOperId() {
        return this.operId;
    }

    public void setOperId(Long anOperId) {
        this.operId = anOperId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSignDate() {
        return this.signDate;
    }

    public void setSignDate(String anSignDate) {
        this.signDate = anSignDate == null ? null : anSignDate.trim();
    }

    public String getSignAddr() {
        return this.signAddr;
    }

    public void setSignAddr(String anSignAddr) {
        this.signAddr = anSignAddr;
    }

    public List<CustFileItem> getCustFileList() {
        return this.custFileList;
    }

    public void setCustFileList(List<CustFileItem> anCustFileList) {
        this.custFileList = anCustFileList;
    }
    
    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getLockedStatus() {
        return this.lockedStatus;
    }

    public void setLockedStatus(String anLockedStatus) {
        this.lockedStatus = anLockedStatus;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String anRefNo) {
        this.refNo = anRefNo;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    public String getIsLatest() {
        return this.isLatest;
    }

    public void setIsLatest(String anIsLatest) {
        this.isLatest = anIsLatest;
    }


    @Override
    public String toString() {
        return "ContractLedger [id=" + this.id + ", agreeName=" + this.agreeName + ", agreeNo=" + this.agreeNo + ", supplier=" + this.supplier
                + ", buyer=" + this.buyer + ", balance=" + this.balance + ", deliveryDate=" + this.deliveryDate + ", deliveryAddr="
                + this.deliveryAddr + ", checkAccept=" + this.checkAccept + ", objectionPeriod=" + this.objectionPeriod + ", agreeStartDate="
                + this.agreeStartDate + ", agreeEndDate=" + this.agreeEndDate + ", regDate=" + this.regDate + ", regTime=" + this.regTime
                + ", modiDate=" + this.modiDate + ", modiTime=" + this.modiTime + ", businStatus=" + this.businStatus + ", buyerNo=" + this.buyerNo
                + ", supplierNo=" + this.supplierNo + ", operId=" + this.operId + ", operName=" + this.operName + ", operOrg=" + this.operOrg
                + ", batchNo=" + this.batchNo + ", defaultFlag=" + this.defaultFlag + ", des=" + this.des + ", signDate=" + this.signDate
                + ", signAddr=" + this.signAddr + ", custNo=" + this.custNo + ", custName=" + this.custName + ", lockedStatus=" + this.lockedStatus
                + ", refNo=" + this.refNo + ", version=" + this.version + ", isLatest=" + this.isLatest + ", businVersionStatus="
                + this.businVersionStatus + ", custFileList=" + this.custFileList + "]";
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
        ContractLedger other = (ContractLedger) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgreeName() == null ? other.getAgreeName() == null : this.getAgreeName().equals(other.getAgreeName()))
            && (this.getAgreeNo() == null ? other.getAgreeNo() == null : this.getAgreeNo().equals(other.getAgreeNo()))
            && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getBalance() == other.getBalance())
            && (this.getDeliveryDate() == null ? other.getDeliveryDate() == null : this.getDeliveryDate().equals(other.getDeliveryDate()))
            && (this.getDeliveryAddr() == null ? other.getDeliveryAddr() == null : this.getDeliveryAddr().equals(other.getDeliveryAddr()))
            && (this.getCheckAccept() == null ? other.getCheckAccept() == null : this.getCheckAccept().equals(other.getCheckAccept()))
            && (this.getObjectionPeriod() == null ? other.getObjectionPeriod() == null : this.getObjectionPeriod().equals(other.getObjectionPeriod()))
            && (this.getAgreeStartDate() == null ? other.getAgreeStartDate() == null : this.getAgreeStartDate().equals(other.getAgreeStartDate()))
            && (this.getAgreeEndDate() == null ? other.getAgreeEndDate() == null : this.getAgreeEndDate().equals(other.getAgreeEndDate()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getDefaultFlag() == null ? other.getDefaultFlag() == null : this.getDefaultFlag().equals(other.getDefaultFlag()))
            && (this.getDes() == null ? other.getDes() == null : this.getDes().equals(other.getDes()));
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
        long temp;
        temp = Double.doubleToLongBits(getBalance());
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((getDeliveryDate() == null) ? 0 : getDeliveryDate().hashCode());
        result = prime * result + ((getDeliveryAddr() == null) ? 0 : getDeliveryAddr().hashCode());
        result = prime * result + ((getCheckAccept() == null) ? 0 : getCheckAccept().hashCode());
        result = prime * result + ((getObjectionPeriod() == null) ? 0 : getObjectionPeriod().hashCode());
        result = prime * result + ((getAgreeStartDate() == null) ? 0 : getAgreeStartDate().hashCode());
        result = prime * result + ((getAgreeEndDate() == null) ? 0 : getAgreeEndDate().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getDefaultFlag() == null) ? 0 : getDefaultFlag().hashCode());
        result = prime * result + ((getDes() == null) ? 0 : getDes().hashCode());
        return result;
    }
    
    public void initValue(){
        this.id = SerialGenerator.getLongValue("ContractLedger.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
        this.defaultFlag="1";
        this.businStatus="0";
        CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (operator != null){
            this.operId = operator.getId();
            this.operName = operator.getName();
            this.operOrg = operator.getOperOrg();
        }
        this.regTime = BetterDateUtils.getNumTime();
        this.modiTime= BetterDateUtils.getNumTime();
        this.custNo=this.supplierNo;
        this.custName=this.supplier;
        this.lockedStatus=VersionConstantCollentions.LOCKED_STATUS_INlOCKED;
        this.isLatest=VersionConstantCollentions.IS_LATEST;
        String pattern="TC#{Date:yy}#{Seq:14}";
        this.refNo=SequenceFactory.generate("PLAT_"+this.getClass().getSimpleName(), pattern);
        this.version="1";
        this.businVersionStatus=this.businStatus;
    }
    
    public void modifyContractLedger(ContractLedger anContractLedger){
        this.id=SerialGenerator.getLongValue("ContractLedger.id");
        this.modiTime= BetterDateUtils.getNumTime();
        this.modiDate = BetterDateUtils.getNumDate();
        this.defaultFlag=anContractLedger.getDefaultFlag();
        this.businStatus=anContractLedger.getBusinStatus();
        this.regDate=anContractLedger.getRegDate();
        this.regTime=anContractLedger.getRegTime();
        this.operId=anContractLedger.getOperId();
        this.operName = anContractLedger.getOperName();
        this.operOrg = anContractLedger.getOperOrg();
        this.batchNo=anContractLedger.getBatchNo();
        this.custNo=this.supplierNo;
        this.custName=this.supplier;
        this.lockedStatus=VersionConstantCollentions.LOCKED_STATUS_INlOCKED;
        this.isLatest=VersionConstantCollentions.IS_LATEST;
        this.refNo=anContractLedger.getRefNo();
        this.version=(Integer.parseInt(anContractLedger.getVersion())+1)+"";
        this.businVersionStatus=anContractLedger.getBusinStatus();
    }
    
    public void initModiDateValue(){
        CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (operator != null){
            this.operId = operator.getId();
            this.operName = operator.getName();
            this.operOrg = operator.getOperOrg();
        }
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime= BetterDateUtils.getNumTime();
    }
}