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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_request_scheme")
public class ScfRequestScheme implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    /**
     * 申请编号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请编号", comments = "申请编号")
    private String requestNo;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 申请企业编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="申请企业编号", comments = "申请企业编号")
    private Long custNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORECUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 申请企业确认状态，-1初始值 ，0未确认，1已确认，2否决,3作废
     */
    @Column(name = "C_CUST_ADUIT",  columnDefinition="VARCHAR" )
    @MetaData( value="申请企业确认状态", comments = "申请企业确认状态，-1初始值 ，0未确认，1已确认，2否决,3作废")
    private String custAduit;

    /**
     * 核心企业确认状态，-1初始值，0未确认，1已确认，2否决,3作废
     */
    @Column(name = "C_CORECUST_ADUIT",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业确认状态", comments = "核心企业确认状态，-1初始值，0未确认，1已确认，2否决,3作废")
    private String coreCustAduit;

    /**
     * 申请金额
     */
    @Column(name = "F_REQUEST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="申请金额", comments = "申请金额")
    private BigDecimal requestBalance;

    /**
     * 审批金额
     */
    @Column(name = "F_APPROVED_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="审批金额", comments = "审批金额")
    private BigDecimal approvedBalance;

    /**
     * 审批利率
     */
    @Column(name = "F_APPROVED_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="审批利率", comments = "审批利率")
    private BigDecimal approvedRatio;

    /**
     * 审批管理费
     */
    @Column(name = "F_APPROVED_MANAGEMENT_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="审批管理费", comments = "审批管理费")
    private BigDecimal approvedManagementRatio;

    /**
     * 手续费率
     */
    @Column(name = "F_SERVICEFEE_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="手续费率", comments = "手续费率")
    private BigDecimal servicefeeRatio;

    /**
     * 期限
     */
    @Column(name = "N_PERIOD",  columnDefinition="INT" )
    @MetaData( value="期限", comments = "期限")
    private Integer period;

    /**
     * 期限单位： 1日，2月
     */
    @Column(name = "N_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="期限单位： 1日", comments = "期限单位： 1日，2月")
    private Integer periodUnit;

    /**
     * 审批期限
     */
    @Column(name = "N_APPROVED_PERIOD",  columnDefinition="INT" )
    @MetaData( value="审批期限", comments = "审批期限")
    private Integer approvedPeriod;

    /**
     * 审批期限单位： 1日，2月
     */
    @Column(name = "N_APPROVED_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="审批期限单位： 1日", comments = "审批期限单位： 1日，2月")
    private Integer approvedPeriodUnit;

    /**
     * 授信类型
     */
    @Column(name = "C_CREDIT_MODE",  columnDefinition="VARCHAR" )
    @MetaData( value="授信类型", comments = "授信类型")
    private String creditMode;
    
    @Column(name = "F_EXTENSION_RATIO",  columnDefinition="VARCHAR" )
    @MetaData( value="展期利率", comments = "展期利率")
    private BigDecimal extensionRatio;    

    
    @Column(name = "F_OVERDUE_RATIO",  columnDefinition="VARCHAR" )
    @MetaData( value="逾期利率", comments = "逾期利率")
    private BigDecimal overdueRatio;
    
    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    @Column(name = "L_REG_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID",  columnDefinition="BIGINT" )
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

    private static final long serialVersionUID = 1470898776193L;

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

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
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

    public String getCustAduit() {
        return custAduit;
    }

    public void setCustAduit(String custAduit) {
        this.custAduit = custAduit;
    }

    public String getCoreCustAduit() {
        return coreCustAduit;
    }

    public void setCoreCustAduit(String coreCustAduit) {
        this.coreCustAduit = coreCustAduit;
    }

    public BigDecimal getRequestBalance() {
        return requestBalance;
    }

    public void setRequestBalance(BigDecimal requestBalance) {
        this.requestBalance = requestBalance;
    }

    public BigDecimal getApprovedBalance() {
        return approvedBalance;
    }

    public void setApprovedBalance(BigDecimal approvedBalance) {
        this.approvedBalance = approvedBalance;
    }

    public BigDecimal getApprovedRatio() {
        return approvedRatio;
    }

    public void setApprovedRatio(BigDecimal approvedRatio) {
        this.approvedRatio = approvedRatio;
    }

    public BigDecimal getApprovedManagementRatio() {
        return approvedManagementRatio;
    }

    public void setApprovedManagementRatio(BigDecimal approvedManagementRatio) {
        this.approvedManagementRatio = approvedManagementRatio;
    }

    public BigDecimal getServicefeeRatio() {
        return servicefeeRatio;
    }

    public void setServicefeeRatio(BigDecimal servicefeeRatio) {
        this.servicefeeRatio = servicefeeRatio;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(Integer periodUnit) {
        this.periodUnit = periodUnit;
    }

    public Integer getApprovedPeriod() {
        return approvedPeriod;
    }

    public void setApprovedPeriod(Integer approvedPeriod) {
        this.approvedPeriod = approvedPeriod;
    }

    public Integer getApprovedPeriodUnit() {
        return approvedPeriodUnit;
    }

    public void setApprovedPeriodUnit(Integer approvedPeriodUnit) {
        this.approvedPeriodUnit = approvedPeriodUnit;
    }

    public String getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(String creditMode) {
        this.creditMode = creditMode;
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
    
    public BigDecimal getExtensionRatio() {
		return extensionRatio;
	}

	public void setExtensionRatio(BigDecimal extensionRatio) {
		this.extensionRatio = extensionRatio;
	}

	public BigDecimal getOverdueRatio() {
		return overdueRatio;
	}

	public void setOverdueRatio(BigDecimal overdueRatio) {
		this.overdueRatio = overdueRatio;
	}
    
    @Override
	public String toString() {
		return "ScfRequestScheme [id=" + id + ", requestNo=" + requestNo + ", factorNo=" + factorNo + ", custNo="
				+ custNo + ", coreCustNo=" + coreCustNo + ", custAduit=" + custAduit + ", coreCustAduit="
				+ coreCustAduit + ", requestBalance=" + requestBalance + ", approvedBalance=" + approvedBalance
				+ ", approvedRatio=" + approvedRatio + ", approvedManagementRatio=" + approvedManagementRatio
				+ ", servicefeeRatio=" + servicefeeRatio + ", period=" + period + ", periodUnit=" + periodUnit
				+ ", approvedPeriod=" + approvedPeriod + ", approvedPeriodUnit=" + approvedPeriodUnit + ", creditMode="
				+ creditMode + ", extensionRatio=" + extensionRatio + ", overdueRatio=" + overdueRatio + ", operOrg="
				+ operOrg + ", regOperId=" + regOperId + ", regOperName=" + regOperName + ", regDate=" + regDate
				+ ", regTime=" + regTime + ", modiOperId=" + modiOperId + ", modiOperName=" + modiOperName
				+ ", modiDate=" + modiDate + ", modiTime=" + modiTime + ", version=" + version + ", custName="
				+ custName + ", coreCustName=" + coreCustName + ", factorName=" + factorName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvedBalance == null) ? 0 : approvedBalance.hashCode());
		result = prime * result + ((approvedManagementRatio == null) ? 0 : approvedManagementRatio.hashCode());
		result = prime * result + ((approvedPeriod == null) ? 0 : approvedPeriod.hashCode());
		result = prime * result + ((approvedPeriodUnit == null) ? 0 : approvedPeriodUnit.hashCode());
		result = prime * result + ((approvedRatio == null) ? 0 : approvedRatio.hashCode());
		result = prime * result + ((coreCustAduit == null) ? 0 : coreCustAduit.hashCode());
		result = prime * result + ((coreCustName == null) ? 0 : coreCustName.hashCode());
		result = prime * result + ((coreCustNo == null) ? 0 : coreCustNo.hashCode());
		result = prime * result + ((creditMode == null) ? 0 : creditMode.hashCode());
		result = prime * result + ((custAduit == null) ? 0 : custAduit.hashCode());
		result = prime * result + ((custName == null) ? 0 : custName.hashCode());
		result = prime * result + ((custNo == null) ? 0 : custNo.hashCode());
		result = prime * result + ((extensionRatio == null) ? 0 : extensionRatio.hashCode());
		result = prime * result + ((factorName == null) ? 0 : factorName.hashCode());
		result = prime * result + ((factorNo == null) ? 0 : factorNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modiDate == null) ? 0 : modiDate.hashCode());
		result = prime * result + ((modiOperId == null) ? 0 : modiOperId.hashCode());
		result = prime * result + ((modiOperName == null) ? 0 : modiOperName.hashCode());
		result = prime * result + ((modiTime == null) ? 0 : modiTime.hashCode());
		result = prime * result + ((operOrg == null) ? 0 : operOrg.hashCode());
		result = prime * result + ((overdueRatio == null) ? 0 : overdueRatio.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((periodUnit == null) ? 0 : periodUnit.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((regOperId == null) ? 0 : regOperId.hashCode());
		result = prime * result + ((regOperName == null) ? 0 : regOperName.hashCode());
		result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
		result = prime * result + ((requestBalance == null) ? 0 : requestBalance.hashCode());
		result = prime * result + ((requestNo == null) ? 0 : requestNo.hashCode());
		result = prime * result + ((servicefeeRatio == null) ? 0 : servicefeeRatio.hashCode());
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
		ScfRequestScheme other = (ScfRequestScheme) obj;
		if (approvedBalance == null) {
			if (other.approvedBalance != null)
				return false;
		} else if (!approvedBalance.equals(other.approvedBalance))
			return false;
		if (approvedManagementRatio == null) {
			if (other.approvedManagementRatio != null)
				return false;
		} else if (!approvedManagementRatio.equals(other.approvedManagementRatio))
			return false;
		if (approvedPeriod == null) {
			if (other.approvedPeriod != null)
				return false;
		} else if (!approvedPeriod.equals(other.approvedPeriod))
			return false;
		if (approvedPeriodUnit == null) {
			if (other.approvedPeriodUnit != null)
				return false;
		} else if (!approvedPeriodUnit.equals(other.approvedPeriodUnit))
			return false;
		if (approvedRatio == null) {
			if (other.approvedRatio != null)
				return false;
		} else if (!approvedRatio.equals(other.approvedRatio))
			return false;
		if (coreCustAduit == null) {
			if (other.coreCustAduit != null)
				return false;
		} else if (!coreCustAduit.equals(other.coreCustAduit))
			return false;
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
		if (creditMode == null) {
			if (other.creditMode != null)
				return false;
		} else if (!creditMode.equals(other.creditMode))
			return false;
		if (custAduit == null) {
			if (other.custAduit != null)
				return false;
		} else if (!custAduit.equals(other.custAduit))
			return false;
		if (custName == null) {
			if (other.custName != null)
				return false;
		} else if (!custName.equals(other.custName))
			return false;
		if (custNo == null) {
			if (other.custNo != null)
				return false;
		} else if (!custNo.equals(other.custNo))
			return false;
		if (extensionRatio == null) {
			if (other.extensionRatio != null)
				return false;
		} else if (!extensionRatio.equals(other.extensionRatio))
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
		if (overdueRatio == null) {
			if (other.overdueRatio != null)
				return false;
		} else if (!overdueRatio.equals(other.overdueRatio))
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
		if (requestBalance == null) {
			if (other.requestBalance != null)
				return false;
		} else if (!requestBalance.equals(other.requestBalance))
			return false;
		if (requestNo == null) {
			if (other.requestNo != null)
				return false;
		} else if (!requestNo.equals(other.requestNo))
			return false;
		if (servicefeeRatio == null) {
			if (other.servicefeeRatio != null)
				return false;
		} else if (!servicefeeRatio.equals(other.servicefeeRatio))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}



	@Transient
    private String custName;
    @Transient
    private String coreCustName;
    @Transient
    private String factorName;
    
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

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }
    
    public void init(ScfRequestScheme anScheme) {
        this.id = SerialGenerator.getLongValue("ScfRequestApproved.id");
        this.custAduit = "0";
        this.coreCustAduit = "-1";
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        fillDefault(anScheme);
    }

    public void initModify(ScfRequestScheme anScheme) {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        fillDefault(anScheme);
    }
    
    private void fillDefault(ScfRequestScheme anScheme){
        anScheme.approvedBalance = MathExtend.defaultValue(anScheme.approvedBalance, BigDecimal.ZERO);
        anScheme.approvedManagementRatio = MathExtend.defaultValue(anScheme.approvedManagementRatio, BigDecimal.ZERO);
        anScheme.approvedRatio = MathExtend.defaultValue(anScheme.approvedRatio, BigDecimal.ZERO);
        anScheme.servicefeeRatio = MathExtend.defaultValue(anScheme.servicefeeRatio, BigDecimal.ZERO);
        anScheme.requestBalance = MathExtend.defaultValue(anScheme.requestBalance, BigDecimal.ZERO);
        anScheme.approvedPeriodUnit = (null == anScheme.approvedPeriodUnit) ? 1 :anScheme.approvedPeriodUnit;
    }
}