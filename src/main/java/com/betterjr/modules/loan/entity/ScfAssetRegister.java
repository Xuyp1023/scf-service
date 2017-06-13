package com.betterjr.modules.loan.entity;

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
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ASSET_REGISTER")
public class ScfAssetRegister implements BetterjrEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2560620952647431168L;

	@Id
	@Column(name = "ID", columnDefinition = "BIGINT")
	@MetaData(value = "", comments = "")
	@OrderBy("desc")
	private Long id;

	@Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String requestNo;

	@Column(name = "C_ASSET_CODE", columnDefinition = "VARCHAR")
	@MetaData(value = "资产编号", comments = "资产编号")
	private String assetCode;

	@Column(name = "C_REGISTER_CUSTNO", columnDefinition = "VARCHAR")
	@MetaData(value = "登记企业编号", comments = "登记企业编号")
	private Long registerCustNo;

	@Column(name = "C_REGISTER_CUSTNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "登记企业名称", comments = "登记企业名称")
	private String registerCustName;

	@Column(name = "C_REGISTRANT", columnDefinition = "VARCHAR")
	@MetaData(value = "登记人", comments = "登记人")
	private String registrant;

	@Column(name = "C_ZDW_REG_CODE", columnDefinition = "VARCHAR")
	@MetaData(value = "中征登记编号", comments = "中征登记编号")
	private String zdwRegCode;

	@Column(name = "D_ZDW_REG_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "中征登记日期", comments = "中征登记日期")
	private String zdwRegDate;

	@Column(name = "L_BATCHNO", columnDefinition = "VARCHAR")
	@MetaData(value = "中征登记文件", comments = "中征登记文件")
	private Long batchNo;
	
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

	@Column(name = "L_REG_OPERID", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private Long regOperId;

	@Column(name = "L_MODI_OPERID", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private Long modiOperId;

	@Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiOperName;

	@Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiDate;

	@Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String modiTime;

	@Column(name = "N_VERSION", columnDefinition = "BIGINT")
	@MetaData(value = "", comments = "")
	private Long version;

	@Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regOperName;

	@Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regDate;

	@Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
	@MetaData(value = "", comments = "")
	private String regTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public Long getRegisterCustNo() {
		return registerCustNo;
	}

	public void setRegisterCustNo(Long registerCustNo) {
		this.registerCustNo = registerCustNo;
	}

	public String getOperOrg() {
		return operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	public String getRegisterCustName() {
		return registerCustName;
	}

	public void setRegisterCustName(String registerCustName) {
		this.registerCustName = registerCustName;
	}

	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}

	public String getZdwRegCode() {
		return zdwRegCode;
	}

	public void setZdwRegCode(String zdwRegCode) {
		this.zdwRegCode = zdwRegCode;
	}

	public String getZdwRegDate() {
		return zdwRegDate;
	}

	public void setZdwRegDate(String zdwRegDate) {
		this.zdwRegDate = zdwRegDate;
	}

	public Long getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
	}

	public Long getRegOperId() {
		return regOperId;
	}

	public void setRegOperId(Long regOperId) {
		this.regOperId = regOperId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetCode == null) ? 0 : assetCode.hashCode());
		result = prime * result + ((batchNo == null) ? 0 : batchNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modiDate == null) ? 0 : modiDate.hashCode());
		result = prime * result + ((modiOperId == null) ? 0 : modiOperId.hashCode());
		result = prime * result + ((modiOperName == null) ? 0 : modiOperName.hashCode());
		result = prime * result + ((modiTime == null) ? 0 : modiTime.hashCode());
		result = prime * result + ((operOrg == null) ? 0 : operOrg.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((regOperId == null) ? 0 : regOperId.hashCode());
		result = prime * result + ((regOperName == null) ? 0 : regOperName.hashCode());
		result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
		result = prime * result + ((registerCustName == null) ? 0 : registerCustName.hashCode());
		result = prime * result + ((registerCustNo == null) ? 0 : registerCustNo.hashCode());
		result = prime * result + ((registrant == null) ? 0 : registrant.hashCode());
		result = prime * result + ((requestNo == null) ? 0 : requestNo.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((zdwRegCode == null) ? 0 : zdwRegCode.hashCode());
		result = prime * result + ((zdwRegDate == null) ? 0 : zdwRegDate.hashCode());
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
		ScfAssetRegister other = (ScfAssetRegister) obj;
		if (assetCode == null) {
			if (other.assetCode != null)
				return false;
		} else if (!assetCode.equals(other.assetCode))
			return false;
		if (batchNo == null) {
			if (other.batchNo != null)
				return false;
		} else if (!batchNo.equals(other.batchNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (operOrg == null) {
			if (other.operOrg != null)
				return false;
		} else if (!operOrg.equals(other.operOrg))
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
		if (registerCustName == null) {
			if (other.registerCustName != null)
				return false;
		} else if (!registerCustName.equals(other.registerCustName))
			return false;
		if (registerCustNo == null) {
			if (other.registerCustNo != null)
				return false;
		} else if (!registerCustNo.equals(other.registerCustNo))
			return false;
		if (registrant == null) {
			if (other.registrant != null)
				return false;
		} else if (!registrant.equals(other.registrant))
			return false;
		if (requestNo == null) {
			if (other.requestNo != null)
				return false;
		} else if (!requestNo.equals(other.requestNo))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (zdwRegCode == null) {
			if (other.zdwRegCode != null)
				return false;
		} else if (!zdwRegCode.equals(other.zdwRegCode))
			return false;
		if (zdwRegDate == null) {
			if (other.zdwRegDate != null)
				return false;
		} else if (!zdwRegDate.equals(other.zdwRegDate))
			return false;
		return true;
	}

	public void init() {
		this.id = SerialGenerator.getLongValue("ScfAssetRegister.id");
		this.regOperName = UserUtils.getUserName();
		this.regOperId = UserUtils.getOperatorInfo().getId();
		this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
		this.regDate = BetterDateUtils.getNumDate();
		this.regTime = BetterDateUtils.getNumTime();
	}

	public void initModify() {
		this.modiOperId = UserUtils.getOperatorInfo().getId();
		this.modiOperName = UserUtils.getUserName();
		this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
		this.modiDate = BetterDateUtils.getNumDate();
		this.modiTime = BetterDateUtils.getNumTime();
	}

}