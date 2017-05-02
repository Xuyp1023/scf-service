package com.betterjr.modules.productconfig.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_PRODUCT_CONFIG")
public class ScfProductConfig implements BetterjrEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6875911693357156893L;

	/**
	 * 产品流水号
	 */
	@Id
	@Column(name = "ID", columnDefinition = "BIGINT")
	@MetaData(value = "流水号", comments = "流水号")
	@OrderBy("desc")
	private Long id;

	/**
	 * 保理公司编号
	 */
	@Column(name = "L_FACTORNO", columnDefinition = "BIGINT")
	@MetaData(value = "保理公司编号", comments = "保理公司编号")
	private Long factorNo;

	/**
	 * 保理公司名称
	 */
	@Column(name = "C_FACTORNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "保理公司名称", comments = "保理公司名称")
	private String factorName;

	/**
	 * 产品编号
	 */
	@Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
	@MetaData(value = "产品编号", comments = "产品编号")
	private String productCode;

	/**
	 * 产品名称
	 */
	@Column(name = "C_PRODUCT_NAME", columnDefinition = "VARCHAR")
	@MetaData(value = "产品名称", comments = "产品名称")
	private String productName;

	/**
	 * 产品特征
	 */
	@Column(name = "C_FEATURE", columnDefinition = "VARCHAR")
	@MetaData(value = "产品特征", comments = "产品特征")
	private String features;

	/**
	 * 产品使用范围
	 */
	@Column(name = "C_FIT_CROWD", columnDefinition = "VARCHAR")
	@MetaData(value = "产品使用范围", comments = "产品使用范围")
	private String fitCrowd;

	/**
	 * 最低融资金额/单笔放款额度（起）
	 */
	@Column(name = "F_MIN_FACTOR_AMMOUNT", columnDefinition = "DOUBLE")
	@MetaData(value = "最低融资金额/单笔放款额度（起）", comments = "最低融资金额/单笔放款额度（起）")
	private BigDecimal minFactorAmmount;

	/**
	 * 最高融资金额/单笔放款额度（终）
	 */
	@Column(name = "F_MAX_FACTOR_AMMOUNT", columnDefinition = "DOUBLE")
	@MetaData(value = "最高融资金额/单笔放款额度（终）", comments = "最高融资金额/单笔放款额度（终）")
	private BigDecimal maxFactorAmmount;

	/**
	 * 通用利率
	 */
	@Column(name = "F_RATIO", columnDefinition = "DOUBLE")
	@MetaData(value = "通用利率", comments = "通用利率")
	private BigDecimal ratio;

	/**
	 * 最短放款天数
	 */
	@Column(name = "N_MINDAYS", columnDefinition = "INTEGER")
	@MetaData(value = "最短放款天数", comments = "最短放款天数")
	private Integer minDays;

	@Column(name = "N_MAXDAYS", columnDefinition = "INTEGER")
	@MetaData(value = "最长放款天数", comments = "最长放款天数")
	private Integer maxDays;
	
	@Column(name = "C_FACTOR_TYPE", columnDefinition = "VARCHAR")
	@MetaData(value = "保理类型", comments = "保理类型(1:国内有追索权保理  2:国内无追索权保理;")
	private String factorType;

	@Column(name = "C_ACTION_MODEL", columnDefinition = "VARCHAR")
	@MetaData(value = "操作方式", comments = "操作方式(1：正向保理 ，2： 反向保理)")
	private String actionModel;

	@Column(name = "C_FINANCE_TYPE", columnDefinition = "VARCHAR")
	@MetaData(value = "融资类型", comments = "融资类型：1应收账款保理融资,2商票保理")
	private String financeType;

	@Column(name = "C_FINANCE_MODEL", columnDefinition = "VARCHAR")
	@MetaData(value = "融资融资方式", comments = "融资方式(1:折扣方式;2:比例方式;)")
	private String financeModel;
	
	@Column(name = "F_FINANCE_PROPORTION", columnDefinition = "DOUBLE")
	@MetaData(value = "融资比例", comments = "融资比例")
	private BigDecimal financeProportion;

	@Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
	@MetaData(value = "状态(0:登记;1:上架;2:下架,3:草稿)", comments = "状态(0:登记;1:上架;2:下架,3:草稿)")
	private String businStatus;

	

	@Column(name = "L_BUSIN_ID", columnDefinition = "BIGINT")
	@MetaData(value = "融资业务类型id", comments = "融资业务类型id")
	private Long businId;

	@Column(name = "C_CORE_CREDICT", columnDefinition = "VARCHAR")
	@MetaData(value = "核心企业授信占用；0不占用， 1:占用", comments = "核心企业授信占用；0不占用， 1:占用")
	private String coreCredict;

	@Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
	@MetaData(value = "操作机构", comments = "操作机构")
	private String operOrg;

	@Column(name = "L_REG_OPERID", columnDefinition = "BIGINT")
	@MetaData(value = "", comments = "")
	private Long regOperId;

	@Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regOperName;

	@JsonSerialize(using = CustDateJsonSerializer.class)
	@Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regDate;

	@Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regTime;

	@Column(name = "L_MODI_OPERID", columnDefinition = "BIGINT")
	@MetaData(value = "", comments = "")
	private Long modiOperId;

	@Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiOperName;

	@JsonSerialize(using = CustDateJsonSerializer.class)
	@Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiDate;

	@Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiTime;

	@Column(name = "N_VERSION", columnDefinition = "BIGINT")
	@MetaData(value = "", comments = "")
	private Long version;

	/**
	 * 产品上架操作员ID号
	 */
	@JsonIgnore
	@Column(name = "L_SHELVES_OPERID", columnDefinition = "INTEGER")
	@MetaData(value = "产品上架操作员ID号", comments = "产品上架操作员ID号")
	private Long shelvesOperId;

	/**
	 * 产品上架操作员姓名
	 */
	@JsonIgnore
	@Column(name = "C_SHELVES_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "产品上架操作员姓名", comments = "产品上架操作员姓名")
	private String shelvesOperName;

	/**
	 * 产品上架日期
	 */
	@JsonSerialize(using = CustDateJsonSerializer.class)
	@Column(name = "C_SHELVES_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "产品上架日期", comments = "产品上架日期")
	private String shelvesDate;

	/**
	 * 产品上架时间
	 */
	@Column(name = "T_SHELVES_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "产品上架时间", comments = "产品上架时间")
	private String shelvesTime;

	/**
	 * 产品下架操作员ID号
	 */
	@JsonIgnore
	@Column(name = "L_OFFLINE_OPERID", columnDefinition = "INTEGER")
	@MetaData(value = "产品下架操作员ID号", comments = "产品下架操作员ID号")
	private Long offLineOperId;

	/**
	 * 产品下架操作员姓名
	 */
	@JsonIgnore
	@Column(name = "C_OFFLINE_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "产品下架操作员姓名", comments = "产品下架操作员姓名")
	private String offLineOperName;

	/**
	 * 产品下架日期
	 */
	@JsonSerialize(using = CustDateJsonSerializer.class)
	@Column(name = "D_OFFLINE_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "产品下架日期", comments = "产品下架日期")
	private String offLineDate;

	/**
	 * 产品下架时间
	 */
	@Column(name = "T_OFFLINE_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "产品下架时间", comments = "产品下架时间")
	private String offLineTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFactorNo() {
		return factorNo;
	}

	public void setFactorNo(Long factorNo) {
		this.factorNo = factorNo;
	}

	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getFitCrowd() {
		return fitCrowd;
	}

	public void setFitCrowd(String fitCrowd) {
		this.fitCrowd = fitCrowd;
	}

	public BigDecimal getMinFactorAmmount() {
		return minFactorAmmount;
	}

	public void setMinFactorAmmount(BigDecimal minFactorAmmount) {
		this.minFactorAmmount = minFactorAmmount;
	}

	public BigDecimal getMaxFactorAmmount() {
		return maxFactorAmmount;
	}

	public void setMaxFactorAmmount(BigDecimal maxFactorAmmount) {
		this.maxFactorAmmount = maxFactorAmmount;
	}

	public String getFinanceType() {
		return financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}

	public String getActionModel() {
		return actionModel;
	}

	public void setActionModel(String actionModel) {
		this.actionModel = actionModel;
	}

	public String getFinanceModel() {
		return financeModel;
	}

	public void setFinanceModel(String financeModel) {
		this.financeModel = financeModel;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public Integer getMinDays() {
		return minDays;
	}

	public void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}

	public Integer getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(Integer maxDays) {
		this.maxDays = maxDays;
	}

	public String getFactorType() {
		return factorType;
	}

	public void setFactorType(String factorType) {
		this.factorType = factorType;
	}

	public String getBusinStatus() {
		return businStatus;
	}

	public void setBusinStatus(String businStatus) {
		this.businStatus = businStatus;
	}

	public Long getBusinId() {
		return businId;
	}

	public void setBusinId(Long businId) {
		this.businId = businId;
	}

	public String getCoreCredict() {
		return coreCredict;
	}

	public void setCoreCredict(String coreCredict) {
		this.coreCredict = coreCredict;
	}

	public String getOperOrg() {
		return operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	public Long getRegOperId() {
		return regOperId;
	}

	public void setRegOperId(Long regOperId) {
		this.regOperId = regOperId;
	}

	public String getRegOperName() {
		return regOperName;
	}

	public void setRegOperName(String regOperName) {
		this.regOperName = regOperName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public Long getModiOperId() {
		return modiOperId;
	}

	public void setModiOperId(Long modiOperId) {
		this.modiOperId = modiOperId;
	}

	public String getModiOperName() {
		return modiOperName;
	}

	public void setModiOperName(String modiOperName) {
		this.modiOperName = modiOperName;
	}

	public String getModiDate() {
		return modiDate;
	}

	public void setModiDate(String modiDate) {
		this.modiDate = modiDate;
	}

	public String getModiTime() {
		return modiTime;
	}

	public void setModiTime(String modiTime) {
		this.modiTime = modiTime;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getShelvesOperId() {
		return shelvesOperId;
	}

	public void setShelvesOperId(Long shelvesOperId) {
		this.shelvesOperId = shelvesOperId;
	}

	public String getShelvesOperName() {
		return shelvesOperName;
	}

	public void setShelvesOperName(String shelvesOperName) {
		this.shelvesOperName = shelvesOperName;
	}

	public String getShelvesDate() {
		return shelvesDate;
	}

	public void setShelvesDate(String shelvesDate) {
		this.shelvesDate = shelvesDate;
	}

	public String getShelvesTime() {
		return shelvesTime;
	}

	public void setShelvesTime(String shelvesTime) {
		this.shelvesTime = shelvesTime;
	}

	public Long getOffLineOperId() {
		return offLineOperId;
	}

	public void setOffLineOperId(Long offLineOperId) {
		this.offLineOperId = offLineOperId;
	}

	public String getOffLineOperName() {
		return offLineOperName;
	}

	public void setOffLineOperName(String offLineOperName) {
		this.offLineOperName = offLineOperName;
	}

	public String getOffLineDate() {
		return offLineDate;
	}

	public void setOffLineDate(String offLineDate) {
		this.offLineDate = offLineDate;
	}

	public String getOffLineTime() {
		return offLineTime;
	}

	public void setOffLineTime(String offLineTime) {
		this.offLineTime = offLineTime;
	}

	public BigDecimal getFinanceProportion() {
		return financeProportion;
	}

	public void setFinanceProportion(BigDecimal financeProportion) {
		this.financeProportion = financeProportion;
	}

	@Override
	public String toString() {
		return "ScfProductConfig [id=" + id + ", factorNo=" + factorNo + ", factorName=" + factorName + ", productCode="
				+ productCode + ", productName=" + productName + ", features=" + features + ", fitCrowd=" + fitCrowd
				+ ", minFactorAmmount=" + minFactorAmmount + ", maxFactorAmmount=" + maxFactorAmmount + ", ratio="
				+ ratio + ", minDays=" + minDays + ", factorType=" + factorType + ", actionModel=" + actionModel
				+ ", financeType=" + financeType + ", financeModel=" + financeModel + ", financeProportion="
				+ financeProportion + ", businStatus=" + businStatus + ", maxDays=" + maxDays + ", businId="
				+ businId + ", coreCredict=" + coreCredict + ", operOrg=" + operOrg + ", regOperId=" + regOperId
				+ ", regOperName=" + regOperName + ", regDate=" + regDate + ", regTime=" + regTime + ", modiOperId="
				+ modiOperId + ", modiOperName=" + modiOperName + ", modiDate=" + modiDate + ", modiTime=" + modiTime
				+ ", version=" + version + ", shelvesOperId=" + shelvesOperId + ", shelvesOperName=" + shelvesOperName
				+ ", shelvesDate=" + shelvesDate + ", shelvesTime=" + shelvesTime + ", offLineOperId=" + offLineOperId
				+ ", offLineOperName=" + offLineOperName + ", offLineDate=" + offLineDate + ", offLineTime="
				+ offLineTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionModel == null) ? 0 : actionModel.hashCode());
		result = prime * result + ((businId == null) ? 0 : businId.hashCode());
		result = prime * result + ((businStatus == null) ? 0 : businStatus.hashCode());
		result = prime * result + ((coreCredict == null) ? 0 : coreCredict.hashCode());
		result = prime * result + ((factorName == null) ? 0 : factorName.hashCode());
		result = prime * result + ((factorNo == null) ? 0 : factorNo.hashCode());
		result = prime * result + ((factorType == null) ? 0 : factorType.hashCode());
		result = prime * result + ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((financeModel == null) ? 0 : financeModel.hashCode());
		result = prime * result + ((financeProportion == null) ? 0 : financeProportion.hashCode());
		result = prime * result + ((financeType == null) ? 0 : financeType.hashCode());
		result = prime * result + ((fitCrowd == null) ? 0 : fitCrowd.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((maxFactorAmmount == null) ? 0 : maxFactorAmmount.hashCode());
		result = prime * result + ((maxDays == null) ? 0 : maxDays.hashCode());
		result = prime * result + ((minFactorAmmount == null) ? 0 : minFactorAmmount.hashCode());
		result = prime * result + ((minDays == null) ? 0 : minDays.hashCode());
		result = prime * result + ((modiDate == null) ? 0 : modiDate.hashCode());
		result = prime * result + ((modiOperId == null) ? 0 : modiOperId.hashCode());
		result = prime * result + ((modiOperName == null) ? 0 : modiOperName.hashCode());
		result = prime * result + ((modiTime == null) ? 0 : modiTime.hashCode());
		result = prime * result + ((offLineDate == null) ? 0 : offLineDate.hashCode());
		result = prime * result + ((offLineOperId == null) ? 0 : offLineOperId.hashCode());
		result = prime * result + ((offLineOperName == null) ? 0 : offLineOperName.hashCode());
		result = prime * result + ((offLineTime == null) ? 0 : offLineTime.hashCode());
		result = prime * result + ((operOrg == null) ? 0 : operOrg.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((ratio == null) ? 0 : ratio.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((regOperId == null) ? 0 : regOperId.hashCode());
		result = prime * result + ((regOperName == null) ? 0 : regOperName.hashCode());
		result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
		result = prime * result + ((shelvesDate == null) ? 0 : shelvesDate.hashCode());
		result = prime * result + ((shelvesOperId == null) ? 0 : shelvesOperId.hashCode());
		result = prime * result + ((shelvesOperName == null) ? 0 : shelvesOperName.hashCode());
		result = prime * result + ((shelvesTime == null) ? 0 : shelvesTime.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ScfProductConfig other = (ScfProductConfig) obj;
		if (actionModel == null) {
			if (other.actionModel != null)
				return false;
		} else if (!actionModel.equals(other.actionModel))
			return false;
		if (businId == null) {
			if (other.businId != null)
				return false;
		} else if (!businId.equals(other.businId))
			return false;
		if (businStatus == null) {
			if (other.businStatus != null)
				return false;
		} else if (!businStatus.equals(other.businStatus))
			return false;
		if (coreCredict == null) {
			if (other.coreCredict != null)
				return false;
		} else if (!coreCredict.equals(other.coreCredict))
			return false;
		if (factorName == null) {
			if (other.factorName != null)
				return false;
		} else if (!factorName.equals(other.factorName))
			return false;
		if (factorNo == null) {
			if (other.factorNo != null)
				return false;
		} else if (!factorNo.equals(other.factorNo))
			return false;
		if (factorType == null) {
			if (other.factorType != null)
				return false;
		} else if (!factorType.equals(other.factorType))
			return false;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (financeModel == null) {
			if (other.financeModel != null)
				return false;
		} else if (!financeModel.equals(other.financeModel))
			return false;
		if (financeProportion == null) {
			if (other.financeProportion != null)
				return false;
		} else if (!financeProportion.equals(other.financeProportion))
			return false;
		if (financeType == null) {
			if (other.financeType != null)
				return false;
		} else if (!financeType.equals(other.financeType))
			return false;
		if (fitCrowd == null) {
			if (other.fitCrowd != null)
				return false;
		} else if (!fitCrowd.equals(other.fitCrowd))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maxFactorAmmount == null) {
			if (other.maxFactorAmmount != null)
				return false;
		} else if (!maxFactorAmmount.equals(other.maxFactorAmmount))
			return false;
		if (maxDays == null) {
			if (other.maxDays != null)
				return false;
		} else if (!maxDays.equals(other.maxDays))
			return false;
		if (minFactorAmmount == null) {
			if (other.minFactorAmmount != null)
				return false;
		} else if (!minFactorAmmount.equals(other.minFactorAmmount))
			return false;
		if (minDays == null) {
			if (other.minDays != null)
				return false;
		} else if (!minDays.equals(other.minDays))
			return false;
		if (modiDate == null) {
			if (other.modiDate != null)
				return false;
		} else if (!modiDate.equals(other.modiDate))
			return false;
		if (modiOperId == null) {
			if (other.modiOperId != null)
				return false;
		} else if (!modiOperId.equals(other.modiOperId))
			return false;
		if (modiOperName == null) {
			if (other.modiOperName != null)
				return false;
		} else if (!modiOperName.equals(other.modiOperName))
			return false;
		if (modiTime == null) {
			if (other.modiTime != null)
				return false;
		} else if (!modiTime.equals(other.modiTime))
			return false;
		if (offLineDate == null) {
			if (other.offLineDate != null)
				return false;
		} else if (!offLineDate.equals(other.offLineDate))
			return false;
		if (offLineOperId == null) {
			if (other.offLineOperId != null)
				return false;
		} else if (!offLineOperId.equals(other.offLineOperId))
			return false;
		if (offLineOperName == null) {
			if (other.offLineOperName != null)
				return false;
		} else if (!offLineOperName.equals(other.offLineOperName))
			return false;
		if (offLineTime == null) {
			if (other.offLineTime != null)
				return false;
		} else if (!offLineTime.equals(other.offLineTime))
			return false;
		if (operOrg == null) {
			if (other.operOrg != null)
				return false;
		} else if (!operOrg.equals(other.operOrg))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (ratio == null) {
			if (other.ratio != null)
				return false;
		} else if (!ratio.equals(other.ratio))
			return false;
		if (regDate == null) {
			if (other.regDate != null)
				return false;
		} else if (!regDate.equals(other.regDate))
			return false;
		if (regOperId == null) {
			if (other.regOperId != null)
				return false;
		} else if (!regOperId.equals(other.regOperId))
			return false;
		if (regOperName == null) {
			if (other.regOperName != null)
				return false;
		} else if (!regOperName.equals(other.regOperName))
			return false;
		if (regTime == null) {
			if (other.regTime != null)
				return false;
		} else if (!regTime.equals(other.regTime))
			return false;
		if (shelvesDate == null) {
			if (other.shelvesDate != null)
				return false;
		} else if (!shelvesDate.equals(other.shelvesDate))
			return false;
		if (shelvesOperId == null) {
			if (other.shelvesOperId != null)
				return false;
		} else if (!shelvesOperId.equals(other.shelvesOperId))
			return false;
		if (shelvesOperName == null) {
			if (other.shelvesOperName != null)
				return false;
		} else if (!shelvesOperName.equals(other.shelvesOperName))
			return false;
		if (shelvesTime == null) {
			if (other.shelvesTime != null)
				return false;
		} else if (!shelvesTime.equals(other.shelvesTime))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public void init() {
		this.productCode= SequenceFactory.generate("PLAT_FACTOR_PRODUCT", "FP#{Date:yy}#{Seq:14}");
		this.id = SerialGenerator.getLongValue("ScfProductConfig.id");
		this.businStatus = "3";
		this.regOperName = UserUtils.getUserName();
		this.regOperId = UserUtils.getOperatorInfo().getId();
		this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
		this.regDate = BetterDateUtils.getNumDate();
		this.regTime = BetterDateUtils.getNumTime();
	}

	public void initModify() {
		//上架
		if(BetterStringUtils.equals(this.businStatus, "1")){
			this.shelvesOperId = UserUtils.getOperatorInfo().getId();
			this.shelvesOperName = UserUtils.getUserName();
			this.shelvesDate = BetterDateUtils.getNumDate();
			this.shelvesTime = BetterDateUtils.getNumTime();
		}
		
		//下架
		else if(BetterStringUtils.equals(this.businStatus, "2")){
			this.offLineOperId = UserUtils.getOperatorInfo().getId();
			this.offLineOperName = UserUtils.getUserName();
			this.offLineDate = BetterDateUtils.getNumDate();
			this.offLineTime = BetterDateUtils.getNumTime();
		}
		
		else{
			this.modiOperId = UserUtils.getOperatorInfo().getId();
			this.modiOperName = UserUtils.getUserName();
			this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
			this.modiDate = BetterDateUtils.getNumDate();
			this.modiTime = BetterDateUtils.getNumTime();
		}
	}

}