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
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_monthly_statement_record")
public class CommissionMonthlyStatementRecord implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 月报表ID
     */
    @Column(name = "L_MONTHLY_STATEMENT_ID",  columnDefinition="INTEGER" )
    @MetaData( value="月报表ID", comments = "月报表ID")
    private Long monthlyStatementId;

    /**
     * 月报表凭证编号
     */
    @Column(name = "C_MONTHLY_STATEMENT_REFNO",  columnDefinition="VARCHAR" )
    @MetaData( value="月报表凭证编号", comments = "月报表凭证编号")
    private String monthlyStatementRefNo;

    /**
     * 日报表ID
     */
    @Column(name = "L_DAILY_STATEMENT_ID",  columnDefinition="INTEGER" )
    @MetaData( value="日报表ID", comments = "日报表ID")
    private Long dailyStatementId;

    /**
     * 日报表凭证编号
     */
    @Column(name = "C_DAILY_STATEMENT_REFNO",  columnDefinition="VARCHAR" )
    @MetaData( value="日报表凭证编号", comments = "日报表凭证编号")
    private String dailyStatementRefNo;

    /**
     * 支付日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_PAY_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="支付日期", comments = "支付日期")
    private String payDate;

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
     * 利息
     */
    @Column(name = "F_INTEREST",  columnDefinition="DECIMAL" )
    @MetaData( value="利息", comments = "利息")
    private BigDecimal interest;

    /**
     * 佣金年利率
     */
    @Column(name = "F_INTEREST_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="佣金年利率", comments = "佣金年利率")
    private BigDecimal interestRate;

    /**
     * 客户ID
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户ID", comments = "客户ID")
    private Long custNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户名称", comments = "客户名称")
    private String custName;

    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String operOrg;

    @Column(name = "L_REG_OPERID",  columnDefinition="Long" )
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

    @Column(name = "N_VERSION",  columnDefinition="INTEGER" )
    @MetaData( value="", comments = "")
    private Long version;

    @Column(name = "F_DAYS",  columnDefinition="INTEGER" )
    @MetaData( value="结算利息的天数", comments = "结算利息的天数")
    private Long days;

    private static final long serialVersionUID = 1493796206917L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMonthlyStatementId() {
        return monthlyStatementId;
    }

    public void setMonthlyStatementId(Long monthlyStatementId) {
        this.monthlyStatementId = monthlyStatementId;
    }

    public String getMonthlyStatementRefNo() {
        return monthlyStatementRefNo;
    }

    public void setMonthlyStatementRefNo(String monthlyStatementRefNo) {
        this.monthlyStatementRefNo = monthlyStatementRefNo;
    }

    public Long getDailyStatementId() {
        return dailyStatementId;
    }

    public void setDailyStatementId(Long dailyStatementId) {
        this.dailyStatementId = dailyStatementId;
    }

    public String getDailyStatementRefNo() {
        return dailyStatementRefNo;
    }

    public void setDailyStatementRefNo(String dailyStatementRefNo) {
        this.dailyStatementRefNo = dailyStatementRefNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
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

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getDays() {
        return this.days;
    }

    public void setDays(Long anDays) {
        this.days = anDays;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", monthlyStatementId=").append(monthlyStatementId);
        sb.append(", monthlyStatementRefNo=").append(monthlyStatementRefNo);
        sb.append(", dailyStatementId=").append(dailyStatementId);
        sb.append(", dailyStatementRefNo=").append(dailyStatementRefNo);
        sb.append(", payDate=").append(payDate);
        sb.append(", payTotalBalance=").append(payTotalBalance);
        sb.append(", payTotalAmount=").append(payTotalAmount);
        sb.append(", paySuccessBalance=").append(paySuccessBalance);
        sb.append(", paySuccessAmount=").append(paySuccessAmount);
        sb.append(", payFailureBalance=").append(payFailureBalance);
        sb.append(", payFailureAmount=").append(payFailureAmount);
        sb.append(", interest=").append(interest);
        sb.append(", interestRate=").append(interestRate);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", version=").append(version);
        sb.append(", days=").append(days);
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
        CommissionMonthlyStatementRecord other = (CommissionMonthlyStatementRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMonthlyStatementId() == null ? other.getMonthlyStatementId() == null : this.getMonthlyStatementId().equals(other.getMonthlyStatementId()))
            && (this.getMonthlyStatementRefNo() == null ? other.getMonthlyStatementRefNo() == null : this.getMonthlyStatementRefNo().equals(other.getMonthlyStatementRefNo()))
            && (this.getDailyStatementId() == null ? other.getDailyStatementId() == null : this.getDailyStatementId().equals(other.getDailyStatementId()))
            && (this.getDailyStatementRefNo() == null ? other.getDailyStatementRefNo() == null : this.getDailyStatementRefNo().equals(other.getDailyStatementRefNo()))
            && (this.getPayDate() == null ? other.getPayDate() == null : this.getPayDate().equals(other.getPayDate()))
            && (this.getPayTotalBalance() == null ? other.getPayTotalBalance() == null : this.getPayTotalBalance().equals(other.getPayTotalBalance()))
            && (this.getPayTotalAmount() == null ? other.getPayTotalAmount() == null : this.getPayTotalAmount().equals(other.getPayTotalAmount()))
            && (this.getPaySuccessBalance() == null ? other.getPaySuccessBalance() == null : this.getPaySuccessBalance().equals(other.getPaySuccessBalance()))
            && (this.getPaySuccessAmount() == null ? other.getPaySuccessAmount() == null : this.getPaySuccessAmount().equals(other.getPaySuccessAmount()))
            && (this.getPayFailureBalance() == null ? other.getPayFailureBalance() == null : this.getPayFailureBalance().equals(other.getPayFailureBalance()))
            && (this.getPayFailureAmount() == null ? other.getPayFailureAmount() == null : this.getPayFailureAmount().equals(other.getPayFailureAmount()))
            && (this.getInterest() == null ? other.getInterest() == null : this.getInterest().equals(other.getInterest()))
            && (this.getInterestRate() == null ? other.getInterestRate() == null : this.getInterestRate().equals(other.getInterestRate()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMonthlyStatementId() == null) ? 0 : getMonthlyStatementId().hashCode());
        result = prime * result + ((getMonthlyStatementRefNo() == null) ? 0 : getMonthlyStatementRefNo().hashCode());
        result = prime * result + ((getDailyStatementId() == null) ? 0 : getDailyStatementId().hashCode());
        result = prime * result + ((getDailyStatementRefNo() == null) ? 0 : getDailyStatementRefNo().hashCode());
        result = prime * result + ((getPayDate() == null) ? 0 : getPayDate().hashCode());
        result = prime * result + ((getPayTotalBalance() == null) ? 0 : getPayTotalBalance().hashCode());
        result = prime * result + ((getPayTotalAmount() == null) ? 0 : getPayTotalAmount().hashCode());
        result = prime * result + ((getPaySuccessBalance() == null) ? 0 : getPaySuccessBalance().hashCode());
        result = prime * result + ((getPaySuccessAmount() == null) ? 0 : getPaySuccessAmount().hashCode());
        result = prime * result + ((getPayFailureBalance() == null) ? 0 : getPayFailureBalance().hashCode());
        result = prime * result + ((getPayFailureAmount() == null) ? 0 : getPayFailureAmount().hashCode());
        result = prime * result + ((getInterest() == null) ? 0 : getInterest().hashCode());
        result = prime * result + ((getInterestRate() == null) ? 0 : getInterestRate().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
    
    public void initValue(CommissionDailyStatement anDailyStatement){
        this.id = SerialGenerator.getLongValue("CommissionMonthlyStatementRecord.id");
        if(anDailyStatement!=null){
            this.dailyStatementId=anDailyStatement.getId();
            this.dailyStatementRefNo=anDailyStatement.getRefNo();
            this.payDate=anDailyStatement.getPayDate();
            this.payTotalAmount=anDailyStatement.getPayTotalAmount();
            this.payTotalBalance=anDailyStatement.getPayTotalBalance();
            this.paySuccessAmount=anDailyStatement.getPaySuccessAmount();
            this.paySuccessBalance=anDailyStatement.getPaySuccessBalance();
            this.payFailureAmount=anDailyStatement.getPayFailureAmount();
            this.payFailureBalance=anDailyStatement.getPayFailureBalance();
            this.interest=anDailyStatement.getInterest();
            this.interestRate=anDailyStatement.getInterestRate();
            this.custName=anDailyStatement.getOwnCustName();
            this.custNo=anDailyStatement.getOwnCustNo();
            this.days=anDailyStatement.getDays();
        }
        this.regDate=BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        if(custOperator!=null){
            this.operOrg=custOperator.getOperOrg();
            this.regOperId=custOperator.getId();
            this.regOperName=custOperator.getName();
        }
    }
}