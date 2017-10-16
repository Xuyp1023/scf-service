package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.config.ConfigFace;
import com.betterjr.common.config.ConfigItemOperatorImpl;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.modules.remote.utils.ParamValueHelper;
import com.betterjr.modules.remote.utils.XmlUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FAR_CONFIG")
public class FarConfigInfo extends ConfigItemOperatorImpl implements BetterjrEntity, ConfigFace {
    /**
     * 接口名称
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口名称", comments = "接口名称")
    private String faceNo;

    /**
     * 配置名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "配置名称", comments = "配置名称")
    private String itemName;

    /**
     * 配置内容
     */
    @Column(name = "C_VALUE", columnDefinition = "VARCHAR")
    @MetaData(value = "配置内容", comments = "配置内容")
    private String itemValue;

    /**
     * 配置类型
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "配置类型", comments = "配置类型")
    private String itemType;

    /**
     * 是否分割为,0不分割，1分割为数组，2分割为有序的MAP
     */
    @Column(name = "C_SPLIT", columnDefinition = "VARCHAR")
    @MetaData(value = "是否分割为,0不分割", comments = "是否分割为,0不分割，1分割为数组，2分割为有序的MAP")
    private String split;

    /**
     * 应用范围，0无；1接口申请，2接口返回，3页面调用申请，4页面调用返回
     */
    @Column(name = "C_SCOPE", columnDefinition = "VARCHAR")
    @MetaData(value = "应用范围", comments = "应用范围，0无；1接口申请，2接口返回，3页面调用申请，4页面调用返回")
    private String scope;

    /**
     * 配置说明
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "配置说明", comments = "配置说明")
    private String description;

    /**
     * 参数顺序，如果需要可以配置
     */
    @Column(name = "N_ORDER", columnDefinition = "INTEGER")
    @MetaData(value = "参数顺序", comments = "参数顺序，如果需要可以配置")
    private Integer configOrder;

    /**
     * 编号
     */
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Integer id;

    private static final long serialVersionUID = 1440667936360L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    @Override
    public String getItemValue() {
        if ("1".equals(this.split)) {
            StringBuilder sb = new StringBuilder();
            for (String tmpStr : XmlUtils.split(this.itemValue, ";")) {
                sb.append(ParamValueHelper.getValue(tmpStr)).append(";");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        } else {
            return ParamValueHelper.getValue(itemValue);
        }
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue == null ? null : itemValue.trim();
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
    }

    @Override
    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split == null ? null : split.trim();
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public Integer getConfigOrder() {
        return configOrder;
    }

    public void setConfigOrder(Integer configOrder) {
        this.configOrder = configOrder;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", itemName=").append(itemName);
        sb.append(", itemValue=").append(itemValue);
        sb.append(", itemType=").append(itemType);
        sb.append(", split=").append(split);
        sb.append(", scope=").append(scope);
        sb.append(", description=").append(description);
        sb.append(", configOrder=").append(configOrder);
        sb.append(", id=").append(id);
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
        FarConfigInfo other = (FarConfigInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getItemName() == null ? other.getItemName() == null
                        : this.getItemName().equals(other.getItemName()))
                && (this.getItemValue() == null ? other.getItemValue() == null
                        : this.getItemValue().equals(other.getItemValue()))
                && (this.getItemType() == null ? other.getItemType() == null
                        : this.getItemType().equals(other.getItemType()))
                && (this.getSplit() == null ? other.getSplit() == null : this.getSplit().equals(other.getSplit()))
                && (this.getScope() == null ? other.getScope() == null : this.getScope().equals(other.getScope()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()))
                && (this.getConfigOrder() == null ? other.getConfigOrder() == null
                        : this.getConfigOrder().equals(other.getConfigOrder()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getItemName() == null) ? 0 : getItemName().hashCode());
        result = prime * result + ((getItemValue() == null) ? 0 : getItemValue().hashCode());
        result = prime * result + ((getItemType() == null) ? 0 : getItemType().hashCode());
        result = prime * result + ((getSplit() == null) ? 0 : getSplit().hashCode());
        result = prime * result + ((getScope() == null) ? 0 : getScope().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getConfigOrder() == null) ? 0 : getConfigOrder().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public String getDictItem() {

        return this.itemName;
    }

    @Override
    public String getDataType() {

        return "C";
    }

    @Override
    public String getClassType() {

        return null;
    }
}