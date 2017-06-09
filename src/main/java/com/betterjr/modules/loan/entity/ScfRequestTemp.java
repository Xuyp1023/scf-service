package com.betterjr.modules.loan.entity;

import java.math.BigDecimal;

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
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_REQUEST_TEMP")
public class ScfRequestTemp implements BetterjrEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -339025043698285653L;

	@Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    @OrderBy("desc")
    private String requestNo;

	@Column(name = "L_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="申请金额", comments = "申请金额")
    private BigDecimal balance;

	@Column(name = "N_PERIOD",  columnDefinition="INT" )
    @MetaData( value="申请期限", comments = "申请期限")
    private Integer period;
	
	@Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
	@MetaData( value="保理公司编号", comments = "保理公司编号")
	private Long factorNo;
	
	@Column(name = "N_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="期限单位：1：日", comments = "期限单位：1：日，2月")
    private Integer periodUnit;
	
	@Column(name = "C_PRODUCT_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="保理产品编号", comments = "保理产品编号")
    private String productCode;

	@Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="申请企业编号", comments = "申请企业编号")
    private Long custNo;
	
	@Column(name = "L_CORE_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;
	
	@Column(name = "C_SUPP_BANK_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="收款方银行账户", comments = "收款方银行账户")
    private String suppBankAccount;
	               
	@Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "1：草稿，2.已确认,3：作废")
    private String businStatus;
	
    @Column(name = "L_REG_OPERID",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private Long regOperId;

    @Column(name = "L_MODI_OPERID",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiOperName;

    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiDate;

    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiTime;

    @Column(name = "N_VERSION",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long version;

    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regTime;
    
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String operOrg;

    @Column(name = "C_ORDERS",  columnDefinition="VARCHAR" )
    @MetaData( value="联资产编号", comments = "关联资产编号")
    private String orders;

    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String description;

	public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

	public Long getFactorNo() {
		return factorNo;
	}

	public void setFactorNo(Long factorNo) {
		this.factorNo = factorNo;
	}

	public Integer getPeriodUnit() {
		return periodUnit;
	}

	public void setPeriodUnit(Integer periodUnit) {
		this.periodUnit = periodUnit;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Long getCustNo() {
		return custNo;
	}

	public void setCustNo(Long custNo) {
		this.custNo = custNo;
	}

	public Long getCoreCustNo() {
		return coreCustNo;
	}

	public void setCoreCustNo(Long coreCustNo) {
		this.coreCustNo = coreCustNo;
	}

	public String getSuppBankAccount() {
		return suppBankAccount;
	}

	public void setSuppBankAccount(String suppBankAccount) {
		this.suppBankAccount = suppBankAccount;
	}

	public String getBusinStatus() {
		return businStatus;
	}

	public void setBusinStatus(String businStatus) {
		this.businStatus = businStatus;
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
	
	@JsonSerialize(using = CustDateJsonSerializer.class)
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

	@JsonSerialize(using = CustDateJsonSerializer.class)
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

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getOperOrg() {
		return operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((businStatus == null) ? 0 : businStatus.hashCode());
		result = prime * result + ((coreCustNo == null) ? 0 : coreCustNo.hashCode());
		result = prime * result + ((custNo == null) ? 0 : custNo.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((factorNo == null) ? 0 : factorNo.hashCode());
		result = prime * result + ((modiDate == null) ? 0 : modiDate.hashCode());
		result = prime * result + ((modiOperId == null) ? 0 : modiOperId.hashCode());
		result = prime * result + ((modiOperName == null) ? 0 : modiOperName.hashCode());
		result = prime * result + ((modiTime == null) ? 0 : modiTime.hashCode());
		result = prime * result + ((operOrg == null) ? 0 : operOrg.hashCode());
		result = prime * result + ((orders == null) ? 0 : orders.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((periodUnit == null) ? 0 : periodUnit.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((regOperId == null) ? 0 : regOperId.hashCode());
		result = prime * result + ((regOperName == null) ? 0 : regOperName.hashCode());
		result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
		result = prime * result + ((requestNo == null) ? 0 : requestNo.hashCode());
		result = prime * result + ((suppBankAccount == null) ? 0 : suppBankAccount.hashCode());
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
		ScfRequestTemp other = (ScfRequestTemp) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (businStatus == null) {
			if (other.businStatus != null)
				return false;
		} else if (!businStatus.equals(other.businStatus))
			return false;
		if (coreCustNo == null) {
			if (other.coreCustNo != null)
				return false;
		} else if (!coreCustNo.equals(other.coreCustNo))
			return false;
		if (custNo == null) {
			if (other.custNo != null)
				return false;
		} else if (!custNo.equals(other.custNo))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (factorNo == null) {
			if (other.factorNo != null)
				return false;
		} else if (!factorNo.equals(other.factorNo))
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
		if (orders == null) {
			if (other.orders != null)
				return false;
		} else if (!orders.equals(other.orders))
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		if (periodUnit == null) {
			if (other.periodUnit != null)
				return false;
		} else if (!periodUnit.equals(other.periodUnit))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
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
		if (requestNo == null) {
			if (other.requestNo != null)
				return false;
		} else if (!requestNo.equals(other.requestNo))
			return false;
		if (suppBankAccount == null) {
			if (other.suppBankAccount != null)
				return false;
		} else if (!suppBankAccount.equals(other.suppBankAccount))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ScfRequestTemp [requestNo=" + requestNo + ", balance=" + balance + ", period=" + period + ", factorNo="
				+ factorNo + ", periodUnit=" + periodUnit + ", productCode=" + productCode + ", custNo=" + custNo
				+ ", coreCustNo=" + coreCustNo + ", suppBankAccount=" + suppBankAccount + ", businStatus=" + businStatus
				+ ", regOperId=" + regOperId + ", modiOperId=" + modiOperId + ", modiOperName=" + modiOperName
				+ ", modiDate=" + modiDate + ", modiTime=" + modiTime + ", version=" + version + ", regOperName="
				+ regOperName + ", regDate=" + regDate + ", regTime=" + regTime + ", operOrg=" + operOrg + ", orders="
				+ orders + ", description=" + description + ", factorName=" + factorName + ", productName="
				+ productName + ", custName=" + custName + ", coreCustName=" + coreCustName + "]";
	}

	public void init() {
        this.businStatus = "1";
        this.requestNo = SequenceFactory.generate("PLAT_FACTOR_PRODUCT", "SQ#{Date:yy}#{Seq:14}");
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.businStatus = "1";
    }

	public void initModify() {
		this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
	}
	
	@Transient
    private String factorName;
	@Transient
    private String productName;
	@Transient
    private String custName;
	@Transient
    private String coreCustName;


	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCoreCustName() {
		return coreCustName;
	}

	public void setCoreCustName(String coreCustName) {
		this.coreCustName = coreCustName;
	}
	
	 
}