package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_FIELDMAPING")
public class FaceFieldMapInfo implements BetterjrEntity {
    /**
     * 接口
     */
    @Column(name = "C_FACE",  columnDefinition="VARCHAR" )
    @MetaData( value="接口", comments = "接口")
    private String faceNo;

    /**
     * 文件类型
     */
    @Column(name = "C_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="文件类型", comments = "文件类型")
    private String fileType;

    /**
     * 数据项顺序号
     */
    @Column(name = "N_ORDER",  columnDefinition="INTEGER" )
    @MetaData( value="数据项顺序号", comments = "数据项顺序号")
    private Integer fieldOrder;

    /**
     * 数据项TXT名称；接口文件中的字段名称
     */
    @Column(name = "C_SRCNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="数据项TXT名称", comments = "数据项TXT名称；接口文件中的字段名称")
    private String sourceName;

    /**
     * 数据项TAB名称；我们系统中使用的名称
     */
    @Column(name = "C_DESTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="数据项TAB名称", comments = "数据项TAB名称；我们系统中使用的名称")
    private String targetName;

    /**
     * 用于数据转换用的JAVA CLASS全称
     */
    @Column(name = "C_CLASS",  columnDefinition="VARCHAR" )
    @MetaData( value="用于数据转换用的JAVA CLASS全称", comments = "用于数据转换用的JAVA CLASS全称")
    private String javaClass;

    /**
     * 默认值；如果没有字段给值，使用默认值，允许使用系统变量
     */
    @Column(name = "C_DEFVALUE",  columnDefinition="VARCHAR" )
    @MetaData( value="默认值", comments = "默认值；如果没有字段给值，使用默认值，允许使用系统变量")
    private String defValue;

    private static final long serialVersionUID = 1440667936357L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName == null ? null : targetName.trim();
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass == null ? null : javaClass.trim();
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue == null ? null : defValue.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", fileType=").append(fileType);
        sb.append(", fieldOrder=").append(fieldOrder);
        sb.append(", sourceName=").append(sourceName);
        sb.append(", targetName=").append(targetName);
        sb.append(", javaClass=").append(javaClass);
        sb.append(", defValue=").append(defValue);
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
        FaceFieldMapInfo other = (FaceFieldMapInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
            && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
            && (this.getFieldOrder() == null ? other.getFieldOrder() == null : this.getFieldOrder().equals(other.getFieldOrder()))
            && (this.getSourceName() == null ? other.getSourceName() == null : this.getSourceName().equals(other.getSourceName()))
            && (this.getTargetName() == null ? other.getTargetName() == null : this.getTargetName().equals(other.getTargetName()))
            && (this.getJavaClass() == null ? other.getJavaClass() == null : this.getJavaClass().equals(other.getJavaClass()))
            && (this.getDefValue() == null ? other.getDefValue() == null : this.getDefValue().equals(other.getDefValue()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getSourceName() == null) ? 0 : getSourceName().hashCode());
        result = prime * result + ((getTargetName() == null) ? 0 : getTargetName().hashCode());
        result = prime * result + ((getJavaClass() == null) ? 0 : getJavaClass().hashCode());
        result = prime * result + ((getDefValue() == null) ? 0 : getDefValue().hashCode());
        return result;
    }
}