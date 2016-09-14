package com.betterjr.modules.push.entity;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_SUPPLIER_PUSH")
public class ScfSupplierPush implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;

    @Column(name = "C_CUSTCORENO",  columnDefinition="INTEGER" )
    private Long custCoreNo;

    @Column(name = "C_SUPPLIERNO",  columnDefinition="INTEGER" )
    private Long supplierNo;

    @Column(name = "C_PUSH_DETAILID",  columnDefinition="INTEGER" )
    private Long pushDetailId;

    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;

    @Column(name = "C_REMARK",  columnDefinition="VARCHAR" )
    private String remark;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;

    @Column(name = "D_MODIFY_DATE",  columnDefinition="VARCHAR" )
    private String modifyDate;

    @Column(name = "T_MODIFY_TIME",  columnDefinition="VARCHAR" )
    private String modifyTime;
    
    @Column(name = "C_REMARKID",  columnDefinition="VARCHAR" )
    private String remarkId;

    private static final long serialVersionUID = 8094687535079426738L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustCoreNo() {
        return custCoreNo;
    }

    public void setCustCoreNo(Long custCoreNo) {
        this.custCoreNo = custCoreNo;
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public Long getPushDetailId() {
        return pushDetailId;
    }

    public void setPushDetailId(Long pushDetailId) {
        this.pushDetailId = pushDetailId;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate == null ? null : modifyDate.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }

    public String getRemarkId() {
        return this.remarkId;
    }

    public void setRemarkId(String anRemarkId) {
        this.remarkId = anRemarkId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", custCoreNo=").append(custCoreNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", pushDetailId=").append(pushDetailId);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", remark=").append(remark);
        sb.append(", remarkId=").append(remarkId);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        ScfSupplierPush other = (ScfSupplierPush) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustCoreNo() == null ? other.getCustCoreNo() == null : this.getCustCoreNo().equals(other.getCustCoreNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getPushDetailId() == null ? other.getPushDetailId() == null : this.getPushDetailId().equals(other.getPushDetailId()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustCoreNo() == null) ? 0 : getCustCoreNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getPushDetailId() == null) ? 0 : getPushDetailId().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        return result;
    }
    
    public void initDefValue(Long custCoreNo,Long supplierNo,Long detailId){
        this.id=SerialGenerator.getLongValue("push.id");
        this.custCoreNo=custCoreNo;
        this.supplierNo=supplierNo;
        this.pushDetailId=detailId;
        this.businStatus="1";
        this.regDate=BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
        this.modifyDate=BetterDateUtils.getNumDate();
        this.modifyTime=BetterDateUtils.getNumTime();
    }
}