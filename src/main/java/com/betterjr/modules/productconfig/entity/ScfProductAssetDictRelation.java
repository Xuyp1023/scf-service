package com.betterjr.modules.productconfig.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_PRODUCT_ASSET_DICT_RELATION")
public class ScfProductAssetDictRelation implements BetterjrEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 7414534785625558438L;

    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @OrderBy("desc")
    private Long id;

    @Column(name = "L_ASSEST_ID", columnDefinition = "BIGINT")
    @MetaData(value = "资产类型id", comments = "资产类型id")
    private Long assestId;

    @Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品编号", comments = "产品编号")
    private String productCode;

    @Column(name = "C_ASSEST_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "资产类型", comments = "资产类型(1主体资产/2辅助材料)")
    private String assestType;

    public void init() {
        this.id = SerialGenerator.getLongValue("ScfProductAssetDictRelation.id");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssestId() {
        return assestId;
    }

    public void setAssestId(Long assestId) {
        this.assestId = assestId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getAssestType() {
        return assestType;
    }

    public void setAssestType(String assestType) {
        this.assestType = assestType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assestId == null) ? 0 : assestId.hashCode());
        result = prime * result + ((assestType == null) ? 0 : assestType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfProductAssetDictRelation other = (ScfProductAssetDictRelation) obj;
        if (assestId == null) {
            if (other.assestId != null) return false;
        } else if (!assestId.equals(other.assestId)) return false;
        if (assestType == null) {
            if (other.assestType != null) return false;
        } else if (!assestType.equals(other.assestType)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (productCode == null) {
            if (other.productCode != null) return false;
        } else if (!productCode.equals(other.productCode)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfProductAssetDictRelation [id=" + id + ", assestId=" + assestId + ", productCode=" + productCode
                + ", assestType=" + assestType + "]";
    }

}