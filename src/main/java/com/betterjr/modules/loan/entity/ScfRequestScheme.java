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
    @MetaData( value="申请企业确认状态", comments = "申请企业确认状态，-1初始值 ，0确认，1否决，2作废")
    private String custAduit;

    /**
     * 核心企业确认状态，-1初始值，0未确认，1已确认，2否决,3作废
     */
    @Column(name = "C_CORECUST_ADUIT",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业确认状态", comments = "核心企业确认状态，-1初始值 ，0确认，1否决，2作废")
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", custAduit=").append(custAduit);
        sb.append(", coreCustAduit=").append(coreCustAduit);
        sb.append(", requestBalance=").append(requestBalance);
        sb.append(", approvedBalance=").append(approvedBalance);
        sb.append(", approvedRatio=").append(approvedRatio);
        sb.append(", approvedManagementRatio=").append(approvedManagementRatio);
        sb.append(", servicefeeRatio=").append(servicefeeRatio);
        sb.append(", period=").append(period);
        sb.append(", periodUnit=").append(periodUnit);
        sb.append(", approvedPeriod=").append(approvedPeriod);
        sb.append(", approvedPeriodUnit=").append(approvedPeriodUnit);
        sb.append(", creditMode=").append(creditMode);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        ScfRequestScheme other = (ScfRequestScheme) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getCustAduit() == null ? other.getCustAduit() == null : this.getCustAduit().equals(other.getCustAduit()))
            && (this.getCoreCustAduit() == null ? other.getCoreCustAduit() == null : this.getCoreCustAduit().equals(other.getCoreCustAduit()))
            && (this.getRequestBalance() == null ? other.getRequestBalance() == null : this.getRequestBalance().equals(other.getRequestBalance()))
            && (this.getApprovedBalance() == null ? other.getApprovedBalance() == null : this.getApprovedBalance().equals(other.getApprovedBalance()))
            && (this.getApprovedRatio() == null ? other.getApprovedRatio() == null : this.getApprovedRatio().equals(other.getApprovedRatio()))
            && (this.getApprovedManagementRatio() == null ? other.getApprovedManagementRatio() == null : this.getApprovedManagementRatio().equals(other.getApprovedManagementRatio()))
            && (this.getServicefeeRatio() == null ? other.getServicefeeRatio() == null : this.getServicefeeRatio().equals(other.getServicefeeRatio()))
            && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
            && (this.getPeriodUnit() == null ? other.getPeriodUnit() == null : this.getPeriodUnit().equals(other.getPeriodUnit()))
            && (this.getApprovedPeriod() == null ? other.getApprovedPeriod() == null : this.getApprovedPeriod().equals(other.getApprovedPeriod()))
            && (this.getApprovedPeriodUnit() == null ? other.getApprovedPeriodUnit() == null : this.getApprovedPeriodUnit().equals(other.getApprovedPeriodUnit()))
            && (this.getCreditMode() == null ? other.getCreditMode() == null : this.getCreditMode().equals(other.getCreditMode()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCustAduit() == null) ? 0 : getCustAduit().hashCode());
        result = prime * result + ((getCoreCustAduit() == null) ? 0 : getCoreCustAduit().hashCode());
        result = prime * result + ((getRequestBalance() == null) ? 0 : getRequestBalance().hashCode());
        result = prime * result + ((getApprovedBalance() == null) ? 0 : getApprovedBalance().hashCode());
        result = prime * result + ((getApprovedRatio() == null) ? 0 : getApprovedRatio().hashCode());
        result = prime * result + ((getApprovedManagementRatio() == null) ? 0 : getApprovedManagementRatio().hashCode());
        result = prime * result + ((getServicefeeRatio() == null) ? 0 : getServicefeeRatio().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getPeriodUnit() == null) ? 0 : getPeriodUnit().hashCode());
        result = prime * result + ((getApprovedPeriod() == null) ? 0 : getApprovedPeriod().hashCode());
        result = prime * result + ((getApprovedPeriodUnit() == null) ? 0 : getApprovedPeriodUnit().hashCode());
        result = prime * result + ((getCreditMode() == null) ? 0 : getCreditMode().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
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