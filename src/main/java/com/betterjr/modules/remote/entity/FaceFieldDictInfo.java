package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_FIELDDICT")
public class FaceFieldDictInfo implements BetterjrEntity {
    /**
     * 接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 数据项TXT名称
     */
    @Column(name = "C_COLUMN_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项TXT名称", comments = "数据项TXT名称")
    private String columnName;

    /**
     * 数据项类型;C字符型，A数字的字符型，N数值型，T文本型；BINARY二进制，TEXT大文本
     */
    @Column(name = "C_DATA_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项类型;C字符型", comments = "数据项类型;C字符型，A数字的字符型，N数值型，T文本型；BINARY二进制，TEXT大文本")
    private String dataType;

    /**
     * 数据项长度
     */
    @Column(name = "N_DATA_LEN", columnDefinition = "INTEGER")
    @MetaData(value = "数据项长度", comments = "数据项长度")
    private Integer dataLen;

    /**
     * 数据项小数位
     */
    @Column(name = "N_DATA_SCALE", columnDefinition = "INTEGER")
    @MetaData(value = "数据项小数位", comments = "数据项小数位")
    private Integer dataScale;

    /**
     * 数据项状态Y文件定义N自定义
     */
    @Column(name = "C_DATA_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项状态Y文件定义N自定义", comments = "数据项状态Y文件定义N自定义")
    private String dataFlag;

    /**
     * 数据项转换标志，0不转换，1转换
     */
    @Column(name = "C_CONVERT_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项转换标志", comments = "数据项转换标志，0不转换，1转换")
    private Boolean convertFlag;

    /**
     * 数据项分隔长度
     */
    @Column(name = "N_SPLTASCII", columnDefinition = "INTEGER")
    @MetaData(value = "数据项分隔长度", comments = "数据项分隔长度")
    private Integer splitAscii;

    /**
     * 数据项描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项描述", comments = "数据项描述")
    private String description;

    private static final long serialVersionUID = 1440667936357L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public Integer getDataLen() {
        return dataLen;
    }

    public void setDataLen(Integer dataLen) {
        this.dataLen = dataLen;
    }

    public Integer getDataScale() {
        return dataScale;
    }

    public void setDataScale(Integer dataScale) {
        this.dataScale = dataScale;
    }

    public String getDataFlag() {
        return dataFlag;
    }

    public void setDataFlag(String dataFlag) {
        this.dataFlag = dataFlag == null ? null : dataFlag.trim();
    }

    public Boolean getConvertFlag() {
        return this.convertFlag;
    }

    public void setConvertFlag(Boolean anConvertFlag) {
        this.convertFlag = anConvertFlag;
    }

    public Integer getSplitAscii() {
        return splitAscii;
    }

    public void setSplitAscii(Integer splitAscii) {
        this.splitAscii = splitAscii;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public FaceFieldDictInfo() {

    }

    public FaceFieldDictInfo(FarFieldMapInfo anFieldMap) {
        this.columnName = anFieldMap.getFaceField();
        this.faceNo = anFieldMap.getFaceNo();
        this.convertFlag = Boolean.FALSE;
        this.dataType = "C";
        this.dataLen = 100;
        this.dataScale = 0;
        this.splitAscii = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" faceNo=").append(faceNo);
        sb.append(", columnName=").append(columnName);
        sb.append(", dataType=").append(dataType);
        sb.append(", dataLen=").append(dataLen);
        sb.append(", dataScale=").append(dataScale);
        sb.append(", dataFlag=").append(dataFlag);
        sb.append(", convertFlag=").append(convertFlag);
        sb.append(", splitAscii=").append(splitAscii);
        sb.append(", description=").append(description);
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
        FaceFieldDictInfo other = (FaceFieldDictInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getColumnName() == null ? other.getColumnName() == null
                        : this.getColumnName().equals(other.getColumnName()))
                && (this.getDataType() == null ? other.getDataType() == null
                        : this.getDataType().equals(other.getDataType()))
                && (this.getDataLen() == null ? other.getDataLen() == null
                        : this.getDataLen().equals(other.getDataLen()))
                && (this.getDataScale() == null ? other.getDataScale() == null
                        : this.getDataScale().equals(other.getDataScale()))
                && (this.getDataFlag() == null ? other.getDataFlag() == null
                        : this.getDataFlag().equals(other.getDataFlag()))
                && (this.getConvertFlag() == null ? other.getConvertFlag() == null
                        : this.getConvertFlag().equals(other.getConvertFlag()))
                && (this.getSplitAscii() == null ? other.getSplitAscii() == null
                        : this.getSplitAscii().equals(other.getSplitAscii()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getColumnName() == null) ? 0 : getColumnName().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        result = prime * result + ((getDataScale() == null) ? 0 : getDataScale().hashCode());
        result = prime * result + ((getDataFlag() == null) ? 0 : getDataFlag().hashCode());
        result = prime * result + ((getConvertFlag() == null) ? 0 : getConvertFlag().hashCode());
        result = prime * result + ((getSplitAscii() == null) ? 0 : getSplitAscii().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
}