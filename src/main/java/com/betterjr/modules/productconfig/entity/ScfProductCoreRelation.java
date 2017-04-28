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
@Table(name = "T_SCF_PRODUCT_CORE_RELATION")
public class ScfProductCoreRelation implements BetterjrEntity {
	@Id
	@Column(name = "ID", columnDefinition = "BIGINT")
	@OrderBy("desc")
	private Long id;

	@Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
	@MetaData(value = "产品编号", comments = "产品编号")
	private String productCode;

	@Column(name = "L_CORE_CUSTNO", columnDefinition = "BIGINT")
	@MetaData(value = "核心企业编号", comments = "核心企业编号")
	private Long coreCustNo;

	@Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "核心企业", comments = "核心企业")
	private String coreCustName;
	
	@Transient
	private String credict = "未授信";//未授信，已授信

	public Long getCoreCustNo() {
		return coreCustNo;
	}

	public void setCoreCustNo(Long coreCustNo) {
		this.coreCustNo = coreCustNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCoreCustName() {
		return coreCustName;
	}

	public void setCoreCustName(String coreCustName) {
		this.coreCustName = coreCustName;
	}
	
	public String getCredict() {
		return credict;
	}

	public void setCredict(String credict) {
		this.credict = credict;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coreCustName == null) ? 0 : coreCustName.hashCode());
		result = prime * result + ((coreCustNo == null) ? 0 : coreCustNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
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
		ScfProductCoreRelation other = (ScfProductCoreRelation) obj;
		if (coreCustName == null) {
			if (other.coreCustName != null)
				return false;
		} else if (!coreCustName.equals(other.coreCustName))
			return false;
		if (coreCustNo == null) {
			if (other.coreCustNo != null)
				return false;
		} else if (!coreCustNo.equals(other.coreCustNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScfProductCoreRelation [id=" + id + ", productCode=" + productCode + ", coreCustNo=" + coreCustNo
				+ ", coreCustName=" + coreCustName + ", credict=" + credict + "]";
	}

	public void init() {
		this.id = SerialGenerator.getLongValue("ScfProductCoreRelation.id");
	}
}