package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_RETURNCODE")
public class FaceReturnCode implements BetterjrEntity {
    /**
     * 返回码模板名称
     */
    @Column(name = "C_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "返回码模板名称", comments = "返回码模板名称")
    private String modeNo;

    /**
     * 返回状态
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "返回状态", comments = "返回状态")
    private Boolean status;

    /**
     * 返回码
     */
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "返回码", comments = "返回码")
    private String returnCode;

    /**
     * 返回码名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "返回码名称", comments = "返回码名称")
    private String returnName;

    /**
     * 描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "描述", comments = "描述")
    private String description;

    private static final long serialVersionUID = 1441285430960L;

    public static FaceReturnCode createNull() {
        FaceReturnCode rr = new FaceReturnCode();
        rr.setStatus(Boolean.FALSE);
        rr.setReturnCode("9990");
        return rr;
    }

    public String getModeNo() {
        return modeNo;
    }

    public void setModeNo(String modeNo) {
        this.modeNo = modeNo == null ? null : modeNo.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode == null ? null : returnCode.trim();
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName == null ? null : returnName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modeNo=").append(modeNo);
        sb.append(", status=").append(status);
        sb.append(", returnCode=").append(returnCode);
        sb.append(", returnName=").append(returnName);
        sb.append(", description=").append(description);
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
        FaceReturnCode other = (FaceReturnCode) that;
        return (this.getModeNo() == null ? other.getModeNo() == null : this.getModeNo().equals(other.getModeNo()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getReturnCode() == null ? other.getReturnCode() == null : this.getReturnCode().equals(other.getReturnCode()))
                && (this.getReturnName() == null ? other.getReturnName() == null : this.getReturnName().equals(other.getReturnName()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getModeNo() == null) ? 0 : getModeNo().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getReturnCode() == null) ? 0 : getReturnCode().hashCode());
        result = prime * result + ((getReturnName() == null) ? 0 : getReturnName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
}