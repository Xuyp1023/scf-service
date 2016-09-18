package com.betterjr.modules.enquiry.entity;

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
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_offer")
public class ScfOffer implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    /**
     * 询价编号
     */
    @Column(name = "C_ENQUIRYNO",  columnDefinition="VARCHAR" )
    @MetaData( value="询价编号", comments = "询价编号")
    private String enquiryNo;

    /**
     * 询价企业编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="询价企业编号", comments = "询价企业编号")
    private Long custNo;

    /**
     * 保理公司编号(报价企业)
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编号(报价企业)", comments = "保理公司编号(报价企业)")
    private Long factorNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 报价金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="报价金额", comments = "报价金额")
    private BigDecimal balance;

    /**
     * 报价利率
     */
    @Column(name = "F_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="报价利率", comments = "报价利率")
    private BigDecimal ratio;

    /**
     * 报价期限
     */
    @Column(name = "N_PERIOD",  columnDefinition="INT" )
    @MetaData( value="报价期限", comments = "报价期限")
    private Integer period;

    /**
     * 期限单位：1：日，2月
     */
    @Column(name = "N_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="期限单位：1：日", comments = "期限单位：1：日，2月")
    private Integer periodUnit;

    /**
     * 最小放款周期
     */
    @Column(name = "N_MIN_CYCLE",  columnDefinition="INT" )
    @MetaData( value="最小放款周期", comments = "最小放款周期")
    private Integer minCycle;

    /**
     * 最大放款周期
     */
    @Column(name = "N_MAX_CYCLE",  columnDefinition="INT" )
    @MetaData( value="最大放款周期", comments = "最大放款周期")
    private Integer maxCycle;

    /**
     * 2：历史，1：报价，0：取消
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="CHAR" )
    @MetaData( value="2：历史", comments = "2：历史，1：报价，0：取消")
    private String businStatus;

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

    /**
     * 1:报价;2:最终报价
     */
    @Column(name = "C_QUOTE_TYPE",  columnDefinition="CHAR" )
    @MetaData( value="1:报价;2:最终报价", comments = "1:报价;2:最终报价")
    private String quoteType;

    private static final long serialVersionUID = 1473324455148L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnquiryNo() {
        return enquiryNo;
    }

    public void setEnquiryNo(String enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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

    public Integer getMinCycle() {
        return minCycle;
    }

    public void setMinCycle(Integer minCycle) {
        this.minCycle = minCycle;
    }

    public Integer getMaxCycle() {
        return maxCycle;
    }

    public void setMaxCycle(Integer maxCycle) {
        this.maxCycle = maxCycle;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
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

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", enquiryNo=").append(enquiryNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", balance=").append(balance);
        sb.append(", ratio=").append(ratio);
        sb.append(", period=").append(period);
        sb.append(", periodUnit=").append(periodUnit);
        sb.append(", minCycle=").append(minCycle);
        sb.append(", maxCycle=").append(maxCycle);
        sb.append(", businStatus=").append(businStatus);
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
        sb.append(", quoteType=").append(quoteType);
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
        ScfOffer other = (ScfOffer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEnquiryNo() == null ? other.getEnquiryNo() == null : this.getEnquiryNo().equals(other.getEnquiryNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
            && (this.getPeriodUnit() == null ? other.getPeriodUnit() == null : this.getPeriodUnit().equals(other.getPeriodUnit()))
            && (this.getMinCycle() == null ? other.getMinCycle() == null : this.getMinCycle().equals(other.getMinCycle()))
            && (this.getMaxCycle() == null ? other.getMaxCycle() == null : this.getMaxCycle().equals(other.getMaxCycle()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getQuoteType() == null ? other.getQuoteType() == null : this.getQuoteType().equals(other.getQuoteType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEnquiryNo() == null) ? 0 : getEnquiryNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getPeriodUnit() == null) ? 0 : getPeriodUnit().hashCode());
        result = prime * result + ((getMinCycle() == null) ? 0 : getMinCycle().hashCode());
        result = prime * result + ((getMaxCycle() == null) ? 0 : getMaxCycle().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
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
        result = prime * result + ((getQuoteType() == null) ? 0 : getQuoteType().hashCode());
        return result;
    }
    
    @Transient
    public String custName;
    @Transient
    public String factorName;
    
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }
    
    public void init() {
        this.id=SerialGenerator.getLongValue("ScfOffer.id");
        this.businStatus = "1";
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