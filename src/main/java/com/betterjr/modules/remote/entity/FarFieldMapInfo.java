package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FAR_FIELDMAP")
public class FarFieldMapInfo implements BetterjrEntity {
    /**
     * 接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 功能代码
     */
    @Column(name = "C_FUNCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "功能代码", comments = "功能代码")
    private String funCode;

    /**
     * 数据项顺序号
     */
    @Column(name = "N_ORDER", columnDefinition = "INTEGER")
    @MetaData(value = "数据项顺序号", comments = "数据项顺序号")
    @OrderBy
    private Integer fieldOrder;

    /**
     * 数据项TXT名称；接口文件中的字段名称
     */
    @Column(name = "C_FACEFIELD", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项TXT名称", comments = "数据项TXT名称；接口文件中的字段名称")
    private String faceField;

    /**
     * 数据项JavaBean中Field名称；我们系统中使用的名称
     */
    @Column(name = "C_BEANFIELD", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项JavaBean中Field名称", comments = "数据项JavaBean中Field名称；我们系统中使用的名称")
    private String beanField;

    /**
     * 用于数据转换用的JAVA CLASS全称
     */
    @Column(name = "C_CLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "用于数据转换用的JAVA CLASS全称", comments = "用于数据转换用的JAVA CLASS全称")
    private String javaClass;

    /**
     * 0无方向性，1输出 即将JAVABEAN转换为入参信息，2输入即将返回结果转换为JAVABEAN
     */
    @Column(name = "C_IO", columnDefinition = "VARCHAR")
    @MetaData(value = "0无方向性", comments = "0无方向性，1输出 即将JAVABEAN转换为入参信息，2输入即将返回结果转换为JAVABEAN")
    private String direction;

    /**
     * 0不是必须内容，1必须内容
     */
    @Column(name = "C_MUSTITEM", columnDefinition = "VARCHAR")
    @MetaData(value = "0不是必须内容", comments = "0不是必须内容，1必须内容")
    private Boolean mustItem;

    /**
     * 数据项所在的位置，ROOT位于根节点 , HEAD 位于Head节点的头节点 , DATA位于数据Data节点
     */
    @Column(name = "C_LEVEL", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项所在的位置", comments = "数据项所在的位置，ROOT位于根节点 , HEAD 位于Head节点的头节点 , DATA位于数据Data节点")
    private String workLevel;

    @Column(name = "C_VALUE_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "值的处理模式", comments = "值的处理模式，0不处理，1默认值，2表达式")
    private String valueMode;

    @Column(name = "C_EXP", columnDefinition = "VARCHAR")
    @MetaData(value = "处理的表达式", comments = "如果不为空，则按表达式处理，为空默认处理；对于输出，可以将表达式定义在BeanField,输入出现表达式必须在此处")
    private String workExpress;

    private static final long serialVersionUID = 1439797394192L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode == null ? null : funCode.trim();
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFaceField() {
        return faceField;
    }

    public void setFaceField(String faceField) {
        this.faceField = faceField == null ? null : faceField.trim();
    }

    public String getBeanField() {
        return beanField;
    }

    public void setBeanField(String beanField) {
        this.beanField = beanField == null ? null : beanField.trim();
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass == null ? null : javaClass.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public Boolean getMustItem() {
        return mustItem;
    }

    public void setMustItem(Boolean mustItem) {
        this.mustItem = mustItem;
    }

    protected String getWorkLevel() {
        return workLevel;
    }

    public void setWorkLevel(String workLevel) {
        this.workLevel = workLevel;
    }

    public String getValueMode() {
        return this.valueMode;
    }

    public void setValueMode(String anValueMode) {
        this.valueMode = anValueMode;
    }

    public String getWorkExpress() {
        return this.workExpress;
    }

    public void setWorkExpress(String anWorkExpress) {
        this.workExpress = anWorkExpress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", funCode=").append(funCode);
        sb.append(", fieldOrder=").append(fieldOrder);
        sb.append(", faceField=").append(faceField);
        sb.append(", beanField=").append(beanField);
        sb.append(", javaClass=").append(javaClass);
        sb.append(", direction=").append(direction);
        sb.append(", mustItem=").append(mustItem);
        sb.append(", workLevel=").append(workLevel);
        sb.append(", valueMode=").append(valueMode);
        sb.append(", workExpress=").append(workExpress);
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
        FarFieldMapInfo other = (FarFieldMapInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getFunCode() == null ? other.getFunCode() == null
                        : this.getFunCode().equals(other.getFunCode()))
                && (this.getFieldOrder() == null ? other.getFieldOrder() == null
                        : this.getFieldOrder().equals(other.getFieldOrder()))
                && (this.getFaceField() == null ? other.getFaceField() == null
                        : this.getFaceField().equals(other.getFaceField()))
                && (this.getBeanField() == null ? other.getBeanField() == null
                        : this.getBeanField().equals(other.getBeanField()))
                && (this.getJavaClass() == null ? other.getJavaClass() == null
                        : this.getJavaClass().equals(other.getJavaClass()))
                && (this.getDirection() == null ? other.getDirection() == null
                        : this.getDirection().equals(other.getDirection()))
                && (this.getWorkLevel() == null ? other.getWorkLevel() == null
                        : this.getWorkLevel().equals(other.getWorkLevel()))
                && (this.getValueMode() == null ? other.getValueMode() == null
                        : this.getValueMode().equals(other.getValueMode()))
                && (this.getMustItem() == null ? other.getMustItem() == null
                        : this.getMustItem().equals(other.getMustItem()))
                && (this.getWorkExpress() == null ? other.getWorkExpress() == null
                        : this.getWorkExpress().equals(other.getWorkExpress()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFunCode() == null) ? 0 : getFunCode().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getFaceField() == null) ? 0 : getFaceField().hashCode());
        result = prime * result + ((getBeanField() == null) ? 0 : getBeanField().hashCode());
        result = prime * result + ((getJavaClass() == null) ? 0 : getJavaClass().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getMustItem() == null) ? 0 : getMustItem().hashCode());
        result = prime * result + ((getWorkLevel() == null) ? 0 : getWorkLevel().hashCode());
        result = prime * result + ((getValueMode() == null) ? 0 : getValueMode().hashCode());
        result = prime * result + ((getWorkExpress() == null) ? 0 : getWorkExpress().hashCode());
        return result;
    }
}