package com.betterjr.modules.flie.data;

import java.io.Serializable;

public class ExcelColumnCell implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8726284269985770511L;

	private String isMust; // 属性导入是否必须有值 1 必须  0 不必须
	
	private String cloumnType; //当前列的类型0 字符串   1 数字

	private String cloumnChineseName; //列的中文名称
	
	private Integer cloumnOrder; //当前属性在excel中排序的位置
	
	private String vailedRegular; //校验字符串正则表达式
	
	private String cloumnProperties; //列的属性pojo对应的属性

	public String getIsMust() {
		return isMust;
	}

	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}

	public String getCloumnType() {
		return cloumnType;
	}

	public void setCloumnType(String cloumnType) {
		this.cloumnType = cloumnType;
	}

	public String getCloumnChineseName() {
		return cloumnChineseName;
	}

	public void setCloumnChineseName(String cloumnChineseName) {
		this.cloumnChineseName = cloumnChineseName;
	}

	public Integer getCloumnOrder() {
		return cloumnOrder;
	}

	public void setCloumnOrder(Integer cloumnOrder) {
		this.cloumnOrder = cloumnOrder;
	}

	public String getVailedRegular() {
		return vailedRegular;
	}

	public void setVailedRegular(String vailedRegular) {
		this.vailedRegular = vailedRegular;
	}

	public String getCloumnProperties() {
		return cloumnProperties;
	}

	public void setCloumnProperties(String cloumnProperties) {
		this.cloumnProperties = cloumnProperties;
	}

	@Override
	public String toString() {
		return "ExcelColumnCell [isMust=" + isMust + ", cloumnType=" + cloumnType + ", cloumnChineseName="
				+ cloumnChineseName + ", cloumnOrder=" + cloumnOrder + ", vailedRegular=" + vailedRegular
				+ ", cloumnProperties=" + cloumnProperties + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cloumnChineseName == null) ? 0 : cloumnChineseName.hashCode());
		result = prime * result + ((cloumnOrder == null) ? 0 : cloumnOrder.hashCode());
		result = prime * result + ((cloumnProperties == null) ? 0 : cloumnProperties.hashCode());
		result = prime * result + ((cloumnType == null) ? 0 : cloumnType.hashCode());
		result = prime * result + ((isMust == null) ? 0 : isMust.hashCode());
		result = prime * result + ((vailedRegular == null) ? 0 : vailedRegular.hashCode());
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
		ExcelColumnCell other = (ExcelColumnCell) obj;
		if (cloumnChineseName == null) {
			if (other.cloumnChineseName != null)
				return false;
		} else if (!cloumnChineseName.equals(other.cloumnChineseName))
			return false;
		if (cloumnOrder == null) {
			if (other.cloumnOrder != null)
				return false;
		} else if (!cloumnOrder.equals(other.cloumnOrder))
			return false;
		if (cloumnProperties == null) {
			if (other.cloumnProperties != null)
				return false;
		} else if (!cloumnProperties.equals(other.cloumnProperties))
			return false;
		if (cloumnType == null) {
			if (other.cloumnType != null)
				return false;
		} else if (!cloumnType.equals(other.cloumnType))
			return false;
		if (isMust == null) {
			if (other.isMust != null)
				return false;
		} else if (!isMust.equals(other.isMust))
			return false;
		if (vailedRegular == null) {
			if (other.vailedRegular != null)
				return false;
		} else if (!vailedRegular.equals(other.vailedRegular))
			return false;
		return true;
	}
	
	
	
}
