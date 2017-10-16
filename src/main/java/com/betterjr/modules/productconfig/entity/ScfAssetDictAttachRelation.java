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
@Table(name = "T_SCF_ASSET_DICT_ATTACH_RELATION")
public class ScfAssetDictAttachRelation implements BetterjrEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 959749838225264604L;

    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    @Column(name = "L_ASSESTID", columnDefinition = "BIGINT")
    @MetaData(value = "资产类型id", comments = "资产类型id")
    private Long assestId;

    @Column(name = "C_ATTCH_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "附件类型名", comments = "附件类型名")
    private String attchName;

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

    public String getAttchName() {
        return attchName;
    }

    public void setAttchName(String attchName) {
        this.attchName = attchName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assestId == null) ? 0 : assestId.hashCode());
        result = prime * result + ((attchName == null) ? 0 : attchName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAssetDictAttachRelation other = (ScfAssetDictAttachRelation) obj;
        if (assestId == null) {
            if (other.assestId != null) return false;
        } else if (!assestId.equals(other.assestId)) return false;
        if (attchName == null) {
            if (other.attchName != null) return false;
        } else if (!attchName.equals(other.attchName)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfAssetDictAttachRelation [id=" + id + ", assestId=" + assestId + ", attchName=" + attchName + "]";
    }

    public void init() {
        this.id = SerialGenerator.getLongValue("ScfAssetDictAttachRelation.id");
    }

}