package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_monthly_statement")
public class CommissionMonthlyStatement implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 凭证号
     */
    @Column(name = "C_REFNO",  columnDefinition="VARCHAR" )
    @MetaData( value="凭证号", comments = "凭证号")
    private String refNo;

    /**
     * 金额单位（默认元）
     */
    @Column(name = "C_UNIT",  columnDefinition="VARCHAR" )
    @MetaData( value="金额单位（默认元）", comments = "金额单位（默认元）")
    private String unit;

    /**
     * 币种
     */
    @Column(name = "C_CURRENY",  columnDefinition="VARCHAR" )
    @MetaData( value="币种", comments = "币种")
    private String curreny;

    /**
     * 开始时间
     */
    @Column(name = "D_PAY_BEGIN_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="开始时间", comments = "开始时间")
    private String payBeginDate;

    /**
     * 结束时间
     */
    @Column(name = "D_PAY_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="结束时间", comments = "结束时间")
    private String payEndDate;

    /**
     * 总金额
     */
    @Column(name = "F_TOTAL_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="总金额", comments = "总金额")
    private BigDecimal totalBalance;

    /**
     * 总笔数
     */
    @Column(name = "N_TOTAL_AMOUNT",  columnDefinition="DECIMAL" )
    @MetaData( value="总笔数", comments = "总笔数")
    private BigDecimal totalAmount;

    /**
     * 支付总金额
     */
    @Column(name = "F_PAY_TOTAL_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付总金额", comments = "支付总金额")
    private BigDecimal payTotalBalance;

    /**
     * 支付总笔数
     */
    @Column(name = "N_PAY_TOTAL_AMOUNT",  columnDefinition="DECIMAL" )
    @MetaData( value="支付总笔数", comments = "支付总笔数")
    private BigDecimal payTotalAmount;

    /**
     * 支付成功金额
     */
    @Column(name = "F_PAY_SUCCESS_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付成功金额", comments = "支付成功金额")
    private BigDecimal paySuccessBalance;

    /**
     * 支付成功笔数
     */
    @Column(name = "N_PAY_SUCCESS_AMOUNT",  columnDefinition="DECIMAL" )
    @MetaData( value="支付成功笔数", comments = "支付成功笔数")
    private BigDecimal paySuccessAmount;

    /**
     * 支付失败金额
     */
    @Column(name = "F_PAY_FAILURE_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付失败金额", comments = "支付失败金额")
    private BigDecimal payFailureBalance;

    /**
     * 支付失败笔数
     */
    @Column(name = "N_PAY_FAILURE_AMOUNT",  columnDefinition="DECIMAL" )
    @MetaData( value="支付失败笔数", comments = "支付失败笔数")
    private BigDecimal payFailureAmount;

    /**
     * 企业客户号
     */
    @Column(name = "L_OWN_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="企业客户号", comments = "企业客户号")
    private Long ownCustNo;

    /**
     * 企业名称
     */
    @Column(name = "C_OWN_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="企业名称", comments = "企业名称")
    private String ownCustName;

    /**
     * 机构
     */
    @Column(name = "C_OWN_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="机构", comments = "机构")
    private String ownOperOrg;

    /**
     * 制表公司名称
     */
    @Column(name = "C_MAKE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="制表公司名称", comments = "制表公司名称")
    private String makeCustName;

    /**
     * 操作员名称
     */
    @Column(name = "C_MAKE_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员名称", comments = "操作员名称")
    private String makeOperName;

    /**
     * 制表日期
     */
    @Column(name = "D_MAKE_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="制表日期", comments = "制表日期")
    private String makeDate;

    /**
     * 制表时间
     */
    @Column(name = "T_MAKE_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="制表时间", comments = "制表时间")
    private String makeTime;

    /**
     * 业务状态，0未处理，1已确认，2已审核，3账单已投递
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="业务状态", comments = "业务状态，0未处理，1已确认，2已审核，3账单已投递")
    private String businStatus;

    @Column(name = "C_LAST_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String lastStatus;

    /**
     * 利息
     */
    @Column(name = "F_INTEREST",  columnDefinition="VARCHAR" )
    @MetaData( value="利息", comments = "利息")
    private String interest;

    /**
     * 利率
     */
    @Column(name = "F_INTEREST_RATE",  columnDefinition="VARCHAR" )
    @MetaData( value="利率", comments = "利率")
    private String interestRate;

    /**
     * 税额
     */
    @Column(name = "F_TAX_BALANCE",  columnDefinition="VARCHAR" )
    @MetaData( value="税额", comments = "税额")
    private String taxBalance;

    /**
     * 税利率
     */
    @Column(name = "F_TAX_RATE",  columnDefinition="VARCHAR" )
    @MetaData( value="税利率", comments = "税利率")
    private String taxRate;

    /**
     * 月报表文件
     */
    @Column(name = "N_BATCHNO",  columnDefinition="VARCHAR" )
    @MetaData( value="月报表文件", comments = "月报表文件")
    private String batchNo;

    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String operOrg;

    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
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

    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
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

    @Column(name = "N_VERSION",  columnDefinition="INTEGER" )
    @MetaData( value="", comments = "")
    private Long version;

    @Column(name = "D_END_INTEREST_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="结息日期", comments = "结息日期")
    private Long endInterestDate;
    
    private static final long serialVersionUID = 1493796206916L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurreny() {
        return curreny;
    }

    public void setCurreny(String curreny) {
        this.curreny = curreny;
    }

    public String getPayBeginDate() {
        return payBeginDate;
    }

    public void setPayBeginDate(String payBeginDate) {
        this.payBeginDate = payBeginDate;
    }

    public String getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(String payEndDate) {
        this.payEndDate = payEndDate;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayTotalBalance() {
        return payTotalBalance;
    }

    public void setPayTotalBalance(BigDecimal payTotalBalance) {
        this.payTotalBalance = payTotalBalance;
    }

    public BigDecimal getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(BigDecimal payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public BigDecimal getPaySuccessBalance() {
        return paySuccessBalance;
    }

    public void setPaySuccessBalance(BigDecimal paySuccessBalance) {
        this.paySuccessBalance = paySuccessBalance;
    }

    public BigDecimal getPaySuccessAmount() {
        return paySuccessAmount;
    }

    public void setPaySuccessAmount(BigDecimal paySuccessAmount) {
        this.paySuccessAmount = paySuccessAmount;
    }

    public BigDecimal getPayFailureBalance() {
        return payFailureBalance;
    }

    public void setPayFailureBalance(BigDecimal payFailureBalance) {
        this.payFailureBalance = payFailureBalance;
    }

    public BigDecimal getPayFailureAmount() {
        return payFailureAmount;
    }

    public void setPayFailureAmount(BigDecimal payFailureAmount) {
        this.payFailureAmount = payFailureAmount;
    }

    public Long getOwnCustNo() {
        return ownCustNo;
    }

    public void setOwnCustNo(Long ownCustNo) {
        this.ownCustNo = ownCustNo;
    }

    public String getOwnCustName() {
        return ownCustName;
    }

    public void setOwnCustName(String ownCustName) {
        this.ownCustName = ownCustName;
    }

    public String getOwnOperOrg() {
        return ownOperOrg;
    }

    public void setOwnOperOrg(String ownOperOrg) {
        this.ownOperOrg = ownOperOrg;
    }

    public String getMakeCustName() {
        return makeCustName;
    }

    public void setMakeCustName(String makeCustName) {
        this.makeCustName = makeCustName;
    }

    public String getMakeOperName() {
        return makeOperName;
    }

    public void setMakeOperName(String makeOperName) {
        this.makeOperName = makeOperName;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getTaxBalance() {
        return taxBalance;
    }

    public void setTaxBalance(String taxBalance) {
        this.taxBalance = taxBalance;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Long getEndInterestDate() {
        return this.endInterestDate;
    }

    public void setEndInterestDate(Long anEndInterestDate) {
        this.endInterestDate = anEndInterestDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", refNo=").append(refNo);
        sb.append(", unit=").append(unit);
        sb.append(", curreny=").append(curreny);
        sb.append(", payBeginDate=").append(payBeginDate);
        sb.append(", payEndDate=").append(payEndDate);
        sb.append(", totalBalance=").append(totalBalance);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", payTotalBalance=").append(payTotalBalance);
        sb.append(", payTotalAmount=").append(payTotalAmount);
        sb.append(", paySuccessBalance=").append(paySuccessBalance);
        sb.append(", paySuccessAmount=").append(paySuccessAmount);
        sb.append(", payFailureBalance=").append(payFailureBalance);
        sb.append(", payFailureAmount=").append(payFailureAmount);
        sb.append(", ownCustNo=").append(ownCustNo);
        sb.append(", ownCustName=").append(ownCustName);
        sb.append(", ownOperOrg=").append(ownOperOrg);
        sb.append(", makeCustName=").append(makeCustName);
        sb.append(", makeOperName=").append(makeOperName);
        sb.append(", makeDate=").append(makeDate);
        sb.append(", makeTime=").append(makeTime);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", interest=").append(interest);
        sb.append(", interestRate=").append(interestRate);
        sb.append(", taxBalance=").append(taxBalance);
        sb.append(", taxRate=").append(taxRate);
        sb.append(", batchNo=").append(batchNo);
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
        sb.append(", endInterestDate=").append(endInterestDate);
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
        CommissionMonthlyStatement other = (CommissionMonthlyStatement) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRefNo() == null ? other.getRefNo() == null : this.getRefNo().equals(other.getRefNo()))
            && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
            && (this.getCurreny() == null ? other.getCurreny() == null : this.getCurreny().equals(other.getCurreny()))
            && (this.getPayBeginDate() == null ? other.getPayBeginDate() == null : this.getPayBeginDate().equals(other.getPayBeginDate()))
            && (this.getPayEndDate() == null ? other.getPayEndDate() == null : this.getPayEndDate().equals(other.getPayEndDate()))
            && (this.getTotalBalance() == null ? other.getTotalBalance() == null : this.getTotalBalance().equals(other.getTotalBalance()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getPayTotalBalance() == null ? other.getPayTotalBalance() == null : this.getPayTotalBalance().equals(other.getPayTotalBalance()))
            && (this.getPayTotalAmount() == null ? other.getPayTotalAmount() == null : this.getPayTotalAmount().equals(other.getPayTotalAmount()))
            && (this.getPaySuccessBalance() == null ? other.getPaySuccessBalance() == null : this.getPaySuccessBalance().equals(other.getPaySuccessBalance()))
            && (this.getPaySuccessAmount() == null ? other.getPaySuccessAmount() == null : this.getPaySuccessAmount().equals(other.getPaySuccessAmount()))
            && (this.getPayFailureBalance() == null ? other.getPayFailureBalance() == null : this.getPayFailureBalance().equals(other.getPayFailureBalance()))
            && (this.getPayFailureAmount() == null ? other.getPayFailureAmount() == null : this.getPayFailureAmount().equals(other.getPayFailureAmount()))
            && (this.getOwnCustNo() == null ? other.getOwnCustNo() == null : this.getOwnCustNo().equals(other.getOwnCustNo()))
            && (this.getOwnCustName() == null ? other.getOwnCustName() == null : this.getOwnCustName().equals(other.getOwnCustName()))
            && (this.getOwnOperOrg() == null ? other.getOwnOperOrg() == null : this.getOwnOperOrg().equals(other.getOwnOperOrg()))
            && (this.getMakeCustName() == null ? other.getMakeCustName() == null : this.getMakeCustName().equals(other.getMakeCustName()))
            && (this.getMakeOperName() == null ? other.getMakeOperName() == null : this.getMakeOperName().equals(other.getMakeOperName()))
            && (this.getMakeDate() == null ? other.getMakeDate() == null : this.getMakeDate().equals(other.getMakeDate()))
            && (this.getMakeTime() == null ? other.getMakeTime() == null : this.getMakeTime().equals(other.getMakeTime()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(other.getLastStatus()))
            && (this.getInterest() == null ? other.getInterest() == null : this.getInterest().equals(other.getInterest()))
            && (this.getInterestRate() == null ? other.getInterestRate() == null : this.getInterestRate().equals(other.getInterestRate()))
            && (this.getTaxBalance() == null ? other.getTaxBalance() == null : this.getTaxBalance().equals(other.getTaxBalance()))
            && (this.getTaxRate() == null ? other.getTaxRate() == null : this.getTaxRate().equals(other.getTaxRate()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
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
        result = prime * result + ((getRefNo() == null) ? 0 : getRefNo().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getCurreny() == null) ? 0 : getCurreny().hashCode());
        result = prime * result + ((getPayBeginDate() == null) ? 0 : getPayBeginDate().hashCode());
        result = prime * result + ((getPayEndDate() == null) ? 0 : getPayEndDate().hashCode());
        result = prime * result + ((getTotalBalance() == null) ? 0 : getTotalBalance().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getPayTotalBalance() == null) ? 0 : getPayTotalBalance().hashCode());
        result = prime * result + ((getPayTotalAmount() == null) ? 0 : getPayTotalAmount().hashCode());
        result = prime * result + ((getPaySuccessBalance() == null) ? 0 : getPaySuccessBalance().hashCode());
        result = prime * result + ((getPaySuccessAmount() == null) ? 0 : getPaySuccessAmount().hashCode());
        result = prime * result + ((getPayFailureBalance() == null) ? 0 : getPayFailureBalance().hashCode());
        result = prime * result + ((getPayFailureAmount() == null) ? 0 : getPayFailureAmount().hashCode());
        result = prime * result + ((getOwnCustNo() == null) ? 0 : getOwnCustNo().hashCode());
        result = prime * result + ((getOwnCustName() == null) ? 0 : getOwnCustName().hashCode());
        result = prime * result + ((getOwnOperOrg() == null) ? 0 : getOwnOperOrg().hashCode());
        result = prime * result + ((getMakeCustName() == null) ? 0 : getMakeCustName().hashCode());
        result = prime * result + ((getMakeOperName() == null) ? 0 : getMakeOperName().hashCode());
        result = prime * result + ((getMakeDate() == null) ? 0 : getMakeDate().hashCode());
        result = prime * result + ((getMakeTime() == null) ? 0 : getMakeTime().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getInterest() == null) ? 0 : getInterest().hashCode());
        result = prime * result + ((getInterestRate() == null) ? 0 : getInterestRate().hashCode());
        result = prime * result + ((getTaxBalance() == null) ? 0 : getTaxBalance().hashCode());
        result = prime * result + ((getTaxRate() == null) ? 0 : getTaxRate().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
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
}