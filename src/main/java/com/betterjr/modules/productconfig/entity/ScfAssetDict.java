package com.betterjr.modules.productconfig.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ASSET_DICT")
public class ScfAssetDict implements BetterjrEntity{
	
	@Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("asc")
	private Long id;

	@Column(name = "C_DICT_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="资产类型", comments = "资产类型")
    private String dictType;

	@Column(name = "C_DATA_SOURCE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据来源", comments = "数据来源，1：核心企业资金系统，2：供应商录入")
    private String dataSource;

	@Column(name = "C_FORM_DATA",  columnDefinition="VARCHAR" )
    @MetaData( value="表单数据要求", comments = "表单数据要求，0：无要求，1：有要求")
    private String formData;

	@Column(name = "C_ATTACH",  columnDefinition="VARCHAR" )
    @MetaData( value="附件要求", comments = "附件要求，0：无要求，1：有要求")
    private String attach;

    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态，0：不可用，1：可用")
    private String businStatus;
    
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;
    
    @Transient
    private String assetType;
	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setdataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	public String getBusinStatus() {
		return businStatus;
	}

	public void setBusinStatus(String businStatus) {
		this.businStatus = businStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attach == null) ? 0 : attach.hashCode());
		result = prime * result + ((businStatus == null) ? 0 : businStatus.hashCode());
		result = prime * result + ((dataSource == null) ? 0 : dataSource.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((dictType == null) ? 0 : dictType.hashCode());
		result = prime * result + ((formData == null) ? 0 : formData.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScfAssetDict other = (ScfAssetDict) obj;
		if (attach == null) {
			if (other.attach != null)
				return false;
		} else if (!attach.equals(other.attach))
			return false;
		if (businStatus == null) {
			if (other.businStatus != null)
				return false;
		} else if (!businStatus.equals(other.businStatus))
			return false;
		if (dataSource == null) {
			if (other.dataSource != null)
				return false;
		} else if (!dataSource.equals(other.dataSource))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (dictType == null) {
			if (other.dictType != null)
				return false;
		} else if (!dictType.equals(other.dictType))
			return false;
		if (formData == null) {
			if (other.formData != null)
				return false;
		} else if (!formData.equals(other.formData))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScfAssetDict [id=" + id + ", dictType=" + dictType + ", dataSource=" + dataSource + ", formData="
				+ formData + ", attach=" + attach + ", businStatus=" + businStatus + ", description=" + description
				+ ", assetType=" + assetType + "]";
	}

	public void init() {
		this.id = SerialGenerator.getLongValue("ScfAssetDict.id");
	}
}