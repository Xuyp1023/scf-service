package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

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
    @Column(name = "F_INTEREST",  columnDefinition="DECIMAL" )
    @MetaData( value="利息", comments = "利息")
    private BigDecimal interest;

    /**
     * 利率
     */
    @Column(name = "F_INTEREST_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="利率", comments = "利率")
    private BigDecimal interestRate;

    /**
     * 税额
     */
    @Column(name = "F_TAX_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="税额", comments = "税额")
    private BigDecimal taxBalance;

    /**
     * 税利率
     */
    @Column(name = "F_TAX_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="税利率", comments = "税利率")
    private BigDecimal taxRate;

    /**
     * 月报表文件
     */
    @Column(name = "N_BATCHNO",  columnDefinition="Long" )
    @MetaData( value="月报表文件", comments = "月报表文件")
    private Long batchNo;

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
    private String endInterestDate;
    
    //对账月份yyyyMM
    @Column(name = "D_BILL_MONTH",  columnDefinition="VARCHAR" )
    private String billMonth;
    
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

    public String getEndInterestDate() {
        return this.endInterestDate;
    }

    public void setEndInterestDate(String anEndInterestDate) {
        this.endInterestDate = anEndInterestDate;
    }

    public BigDecimal getInterest() {
        return this.interest;
    }

    public void setInterest(BigDecimal anInterest) {
        this.interest = anInterest;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal anInterestRate) {
        this.interestRate = anInterestRate;
    }

    public BigDecimal getTaxBalance() {
        return this.taxBalance;
    }

    public void setTaxBalance(BigDecimal anTaxBalance) {
        this.taxBalance = anTaxBalance;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal anTaxRate) {
        this.taxRate = anTaxRate;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public String getBillMonth() {
        return this.billMonth;
    }

    public void setBillMonth(String anBillMonth) {
        this.billMonth = anBillMonth;
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

    public void initMonthlyStatement(Map<String,Object> anMap) {
        this.id = SerialGenerator.getLongValue("CommissionDailyStatement.id");         
        this.refNo = (String)anMap.get("monthlyRefNo");
        this.ownCustName= (String)anMap.get("monthlyRefNo");
        this.totalBalance=new BigDecimal((String)anMap.get("totalBalance"));
        this.payTotalBalance = new BigDecimal((String)anMap.get("payTotalBalance"));
        this.ownCustNo=Long.parseLong((String)anMap.get("monthlyRefNo"));
        this.endInterestDate=(String)anMap.get("endInterestDate");
        this.interest=new BigDecimal((String)anMap.get("interest"));
        this.taxBalance=new BigDecimal((String)anMap.get("totalTaxBalance"));
        this.payBeginDate=(String)anMap.get("startDate");
        this.payEndDate=(String)anMap.get("endDate");
        this.makeDate=BetterDateUtils.getNumDate();
        this.makeTime= BetterDateUtils.getNumTime();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        this.businStatus="0";
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        if(custOperator!=null){
            this.operOrg=custOperator.getOperOrg();
            this.regOperId=custOperator.getId();
            this.regOperName=custOperator.getName();
            this.modiOperId=custOperator.getId();
            this.modiOperName=custOperator.getName();
        }
    }
}