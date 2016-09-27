package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.modules.remote.data.FaceDataStyle;
import com.betterjr.modules.remote.utils.ParamValueHelper;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_HEADER")
public class FaceHeaderInfo implements BetterjrEntity, RemoteFieldInfo, Cloneable {
    private static final long serialVersionUID = 1440667936358L;

    /**
     * 接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 数据项方向；0产生内容，1解析内容
     */
    @Column(name = "C_IO", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项方向", comments = "数据项方向；0产生内容，1解析内容")
    private String direction;

    /**
     * 序号
     */
    @Column(name = "N_ORDER", columnDefinition = "INTEGER")
    @MetaData(value = "序号", comments = "序号")
    private Integer fieldOrder;

    /**
     * 数据项TXT名称
     */
    @Column(name = "C_COLUMN_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项TXT名称", comments = "数据项TXT名称")
    private String columnName;

    /**
     * 数据项描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项描述", comments = "数据项描述")
    private String description;

    /**
     * 数据项类型
     */
    @Column(name = "C_DATA_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项类型", comments = "数据项类型")
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
     * 数据项转换标志Y转换N不转换
     */
    @Column(name = "C_CONVERT_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项转换标志Y转换N不转换", comments = "数据项转换标志Y转换N不转换")
    private Boolean convertFlag;

    /**
     * 数据项分隔符
     */
    @Column(name = "N_SPLTASCII", columnDefinition = "INTEGER")
    @MetaData(value = "数据项分隔符", comments = "数据项分隔符")
    private Integer splitAscii;

    /**
     * 默认值，可以用通配符使用系统上下文的值
     */
    @Column(name = "C_DEFVALUE", columnDefinition = "VARCHAR")
    @MetaData(value = "默认值", comments = "默认值，可以用通配符使用系统上下文的值；属性@范围（Face, Func, Config, outConver, inConver, Class,SelectKey,Dict)对于有Map的处理，增加一个@来处理")
    private String defValue;

    /**
     * 数据项所在的位置，ROOT位于根节点 , HEAD 位于Head节点的头节点 , DATA位于数据Data节点
     */
    @Column(name = "C_LEVEL", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项所在的位置", comments = "数据项所在的位置，ROOT位于根节点 , HEAD 位于Head节点的头节点 , DATA位于数据Data节点")
    private String workLevel;

    private String headValue;

    private FaceDataStyle dataStyle;

    public FaceDataStyle getDataStyle() {
        return dataStyle;
    }

    public void init(WorkFarFaceInfo anFaceInfo) {

        dataStyle = FaceDataStyle.checking(workLevel, anFaceInfo.getDefStyle());
    }

    public String getHeadValue() {
        return headValue;
    }

    public void setHeadValue(String anHeadValue) {
        headValue = anHeadValue;
    }

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    /**
     * 如果字段名称出现分号拆分，则会将所有的数据通过ParamValueHelper做过滤、计算处理。
     * @return
     */
    public String getColumnName() {
        String[] arrStr = this.columnName.split(";");
        if (arrStr.length > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(arrStr[0]);
            String tmpStr;
            for(int i = 1, k = arrStr.length; i < k; i++){
               tmpStr = arrStr[i];
               sb.append(ParamValueHelper.getValue(tmpStr)).append(";"); 
            }
            sb.setLength(sb.length()-1);
            return sb.toString();
        }
        else {
            return ParamValueHelper.getValue(columnName);
        }
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue == null ? null : defValue.trim();
    }

    public String getWorkLevel() {
        return workLevel;
    }

    public void setWorkLevel(String workLevel) {
        this.workLevel = workLevel == null ? null : workLevel.trim();
    }

    @Override
    public FaceHeaderInfo clone() {
        try {
            return (FaceHeaderInfo) super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" faceNo=").append(faceNo);
        sb.append(", direction=").append(direction);
        sb.append(", fieldOrder=").append(fieldOrder);
        sb.append(", columnName=").append(columnName);
        sb.append(", description=").append(description);
        sb.append(", dataType=").append(dataType);
        sb.append(", dataLen=").append(dataLen);
        sb.append(", dataScale=").append(dataScale);
        sb.append(", dataFlag=").append(dataFlag);
        sb.append(", convertFlag=").append(convertFlag);
        sb.append(", splitAscii=").append(splitAscii);
        sb.append(", defValue=").append(defValue);
        sb.append(", headValue=").append(headValue);
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
        FaceHeaderInfo other = (FaceHeaderInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getDirection() == null ? other.getDirection() == null : this.getDirection().equals(other.getDirection()))
                && (this.getFieldOrder() == null ? other.getFieldOrder() == null : this.getFieldOrder().equals(other.getFieldOrder()))
                && (this.getColumnName() == null ? other.getColumnName() == null : this.getColumnName().equals(other.getColumnName()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
                && (this.getDataLen() == null ? other.getDataLen() == null : this.getDataLen().equals(other.getDataLen()))
                && (this.getDataScale() == null ? other.getDataScale() == null : this.getDataScale().equals(other.getDataScale()))
                && (this.getDataFlag() == null ? other.getDataFlag() == null : this.getDataFlag().equals(other.getDataFlag()))
                && (this.getConvertFlag() == null ? other.getConvertFlag() == null : this.getConvertFlag().equals(other.getConvertFlag()))
                && (this.getSplitAscii() == null ? other.getSplitAscii() == null : this.getSplitAscii().equals(other.getSplitAscii()))
                && (this.getDefValue() == null ? other.getDefValue() == null : this.getDefValue().equals(other.getDefValue()))
                && (this.getWorkLevel() == null ? other.getWorkLevel() == null : this.getWorkLevel().equals(other.getWorkLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getColumnName() == null) ? 0 : getColumnName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        result = prime * result + ((getDataScale() == null) ? 0 : getDataScale().hashCode());
        result = prime * result + ((getDataFlag() == null) ? 0 : getDataFlag().hashCode());
        result = prime * result + ((getConvertFlag() == null) ? 0 : getConvertFlag().hashCode());
        result = prime * result + ((getSplitAscii() == null) ? 0 : getSplitAscii().hashCode());
        result = prime * result + ((getDefValue() == null) ? 0 : getDefValue().hashCode());
        result = prime * result + ((getWorkLevel() == null) ? 0 : getWorkLevel().hashCode());
        return result;
    }

    @Override
    public String getName() {

        return this.columnName;
    }

    @Override
    public String getValue() {

        return this.headValue;
    }

    @Override
    public boolean custStyle() {

        return StringUtils.isNotBlank(this.getWorkLevel());
    }

    @Override
    public String getWorkStyle() {

        return workLevel;
    }
}