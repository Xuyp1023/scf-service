package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_monthly_statement")
public class CommissionMonthlyStatement implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 凭证号
     */
    @Column(name = "C_REFNO", columnDefinition = "VARCHAR")
    @MetaData(value = "凭证号", comments = "凭证号")
    private String refNo;

    /**
     * 金额单位（默认元）
     */
    @Column(name = "C_UNIT", columnDefinition = "VARCHAR")
    @MetaData(value = "金额单位（默认元）", comments = "金额单位（默认元）")
    private String unit;

    /**
     * 币种
     */
    @Column(name = "C_CURRENY", columnDefinition = "VARCHAR")
    @MetaData(value = "币种", comments = "币种")
    private String curreny;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_PAY_BEGIN_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "开始时间", comments = "开始时间")
    private String payBeginDate;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_PAY_END_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "结束时间", comments = "结束时间")
    private String payEndDate;

    /**
     * 总金额
     */
    @Column(name = "F_TOTAL_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "总金额", comments = "总金额")
    private BigDecimal totalBalance;

    /**
     * 总笔数
     */
    @Column(name = "N_TOTAL_AMOUNT", columnDefinition = "DECIMAL")
    @MetaData(value = "总笔数", comments = "总笔数")
    private BigDecimal totalAmount;

    /**
     * 支付总金额
     */
    @Column(name = "F_PAY_TOTAL_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "支付总金额", comments = "支付总金额")
    private BigDecimal payTotalBalance;

    /**
     * 支付总笔数
     */
    @Column(name = "N_PAY_TOTAL_AMOUNT", columnDefinition = "DECIMAL")
    @MetaData(value = "支付总笔数", comments = "支付总笔数")
    private BigDecimal payTotalAmount;

    /**
     * 支付成功金额
     */
    @Column(name = "F_PAY_SUCCESS_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "支付成功金额", comments = "支付成功金额")
    private BigDecimal paySuccessBalance;

    /**
     * 支付成功笔数
     */
    @Column(name = "N_PAY_SUCCESS_AMOUNT", columnDefinition = "DECIMAL")
    @MetaData(value = "支付成功笔数", comments = "支付成功笔数")
    private BigDecimal paySuccessAmount;

    /**
     * 支付失败金额
     */
    @Column(name = "F_PAY_FAILURE_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "支付失败金额", comments = "支付失败金额")
    private BigDecimal payFailureBalance;

    /**
     * 支付失败笔数
     */
    @Column(name = "N_PAY_FAILURE_AMOUNT", columnDefinition = "DECIMAL")
    @MetaData(value = "支付失败笔数", comments = "支付失败笔数")
    private BigDecimal payFailureAmount;

    /**
     * 企业客户号
     */
    @Column(name = "L_OWN_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "企业客户号", comments = "企业客户号")
    private Long ownCustNo;

    /**
     * 企业名称
     */
    @Column(name = "C_OWN_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "企业名称", comments = "企业名称")
    private String ownCustName;

    /**
     * 机构
     */
    @Column(name = "C_OWN_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "机构", comments = "机构")
    private String ownOperOrg;

    /**
     * 制表公司名称
     */
    @Column(name = "C_MAKE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "制表公司名称", comments = "制表公司名称")
    private String makeCustName;

    /**
     * 操作员名称
     */
    @Column(name = "C_MAKE_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员名称", comments = "操作员名称")
    private String makeOperName;

    /**
     * 制表日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_MAKE_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "制表日期", comments = "制表日期")
    private String makeDate;

    /**
     * 制表时间
     */
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    @Column(name = "T_MAKE_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "制表时间", comments = "制表时间")
    private String makeTime;

    /**
     * 业务状态，0未处理，1已确认，2已审核，3账单已投递
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务状态", comments = "业务状态，0未处理，1已确认，2已审核，3账单已投递")
    private String businStatus;

    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String lastStatus;

    /**
     * 利息
     */
    @Column(name = "F_INTEREST", columnDefinition = "DECIMAL")
    @MetaData(value = "利息", comments = "利息")
    private BigDecimal interest;

    /**
     * 利率
     */
    @Column(name = "F_INTEREST_RATE", columnDefinition = "DECIMAL")
    @MetaData(value = "利率", comments = "利率")
    private BigDecimal interestRate;

    /**
     * 税额
     */
    @Column(name = "F_TAX_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "税额", comments = "税额")
    private BigDecimal taxBalance;

    /**
     * 税利率
     */
    @Column(name = "F_TAX_RATE", columnDefinition = "DECIMAL")
    @MetaData(value = "税利率", comments = "税利率")
    private BigDecimal taxRate;

    /**
     * 月报表文件
     */
    @Column(name = "N_BATCHNO", columnDefinition = "Long")
    @MetaData(value = "月报表文件", comments = "月报表文件")
    private Long batchNo;

    /**
     * 月报表文件id
     */
    @Column(name = "L_FILE_ID", columnDefinition = "Long")
    @MetaData(value = "月报表文件", comments = "月报表文件")
    private Long fileId;

    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String operOrg;

    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
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

    @Column(name = "N_VERSION", columnDefinition = "INTEGER")
    @MetaData(value = "", comments = "")
    private Long version;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_END_INTEREST_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "结息日期", comments = "结息日期")
    private String endInterestDate;

    // 对账月份yyyyMM
    @Column(name = "D_BILL_MONTH", columnDefinition = "VARCHAR")
    private String billMonth;

    // 是否用于普通发票 默认为0 未使用 1已经使用
    @Column(name = "C_PLAININVOICE_FLAG", columnDefinition = "CHAR")
    private String plainInvoiceFlag;

    // 是否用于专用发票 默认为0 未使用 1已经使用
    @Column(name = "C_SPECIALINVOICE_FLAG", columnDefinition = "CHAR")
    private String specialInvoiceFlag;

    /**
     * 结算金额
     */
    @Column(name = "F_INTEREST_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "结算金额", comments = "结算金额")
    private BigDecimal interestBalance;

    @Transient
    private String makeDateTime;

    private List<CommissionMonthlyStatementRecord> dailyList = new ArrayList<CommissionMonthlyStatementRecord>();

    private static final long serialVersionUID = 1493796206916L;

    public List<CommissionMonthlyStatementRecord> getDailyList() {
        return this.dailyList;
    }

    public void setDailyList(List<CommissionMonthlyStatementRecord> anDailyList) {
        this.dailyList = anDailyList;
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return this.fileId;
    }

    public void setFileId(Long anFileId) {
        this.fileId = anFileId;
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

    public String getMakeDateTime() {
        return this.makeDateTime;
    }

    public void setMakeDateTime(String anMakeDateTime) {
        this.makeDateTime = anMakeDateTime;
    }

    public BigDecimal getInterestBalance() {
        return this.interestBalance;
    }

    public void setInterestBalance(BigDecimal anInterestBalance) {
        this.interestBalance = anInterestBalance;
    }

    public String getPlainInvoiceFlag() {
        return this.plainInvoiceFlag;
    }

    public void setPlainInvoiceFlag(String anPlainInvoiceFlag) {
        this.plainInvoiceFlag = anPlainInvoiceFlag;
    }

    public String getSpecialInvoiceFlag() {
        return this.specialInvoiceFlag;
    }

    public void setSpecialInvoiceFlag(String anSpecialInvoiceFlag) {
        this.specialInvoiceFlag = anSpecialInvoiceFlag;
    }

    @Override
    public String toString() {
        return "CommissionMonthlyStatement [id=" + this.id + ", refNo=" + this.refNo + ", unit=" + this.unit
                + ", curreny=" + this.curreny + ", payBeginDate=" + this.payBeginDate + ", payEndDate="
                + this.payEndDate + ", totalBalance=" + this.totalBalance + ", totalAmount=" + this.totalAmount
                + ", payTotalBalance=" + this.payTotalBalance + ", payTotalAmount=" + this.payTotalAmount
                + ", paySuccessBalance=" + this.paySuccessBalance + ", paySuccessAmount=" + this.paySuccessAmount
                + ", payFailureBalance=" + this.payFailureBalance + ", payFailureAmount=" + this.payFailureAmount
                + ", ownCustNo=" + this.ownCustNo + ", ownCustName=" + this.ownCustName + ", ownOperOrg="
                + this.ownOperOrg + ", makeCustName=" + this.makeCustName + ", makeOperName=" + this.makeOperName
                + ", makeDate=" + this.makeDate + ", makeTime=" + this.makeTime + ", businStatus=" + this.businStatus
                + ", lastStatus=" + this.lastStatus + ", interest=" + this.interest + ", interestRate="
                + this.interestRate + ", taxBalance=" + this.taxBalance + ", taxRate=" + this.taxRate + ", batchNo="
                + this.batchNo + ", fileId=" + this.fileId + ", operOrg=" + this.operOrg + ", regOperId="
                + this.regOperId + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime="
                + this.regTime + ", modiOperId=" + this.modiOperId + ", modiOperName=" + this.modiOperName
                + ", modiDate=" + this.modiDate + ", modiTime=" + this.modiTime + ", version=" + this.version
                + ", endInterestDate=" + this.endInterestDate + ", billMonth=" + this.billMonth + ", plainInvoiceFlag="
                + this.plainInvoiceFlag + ", specialInvoiceFlag=" + this.specialInvoiceFlag + ", interestBalance="
                + this.interestBalance + ", makeDateTime=" + this.makeDateTime + ", dailyList=" + this.dailyList + "]";
    }

    public void initMonthlyStatement(Map<String, Object> anMap) {
        this.id = SerialGenerator.getLongValue("CommissionMonthlyStatement.id");
        this.refNo = (String) anMap.get("refNo");
        this.ownCustName = (String) anMap.get("ownCustName");
        this.billMonth = (String) anMap.get("billMonth");
        this.totalBalance = new BigDecimal((String) anMap.get("totalBalance"));
        this.payTotalBalance = new BigDecimal((String) anMap.get("paySuccessBalance"));
        this.ownCustNo = Long.parseLong((String) anMap.get("ownCustNo"));
        this.endInterestDate = (String) anMap.get("endInterestDate");
        String totalInterset = (String) anMap.get("interest") == "" ? "0" : (String) anMap.get("interest");
        this.interest = new BigDecimal(totalInterset);
        String tax = (String) anMap.get("taxBalance") == "" ? "0" : (String) anMap.get("taxBalance");
        this.taxBalance = new BigDecimal(tax);
        this.payBeginDate = (String) anMap.get("payBeginDate");
        this.payEndDate = (String) anMap.get("payEndDate");
        String interestBalance = (String) anMap.get("interestBalance") == "" ? "0"
                : (String) anMap.get("interestBalance");
        this.interestBalance = new BigDecimal(interestBalance);
        this.makeDate = BetterDateUtils.getNumDate();
        this.makeTime = BetterDateUtils.getNumTime();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        this.businStatus = "1";
        this.unit = "元";
        this.curreny = "RMB";
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        if (custOperator != null) {
            this.operOrg = custOperator.getOperOrg();
            this.regOperId = custOperator.getId();
            this.regOperName = custOperator.getName();
            this.modiOperId = custOperator.getId();
            this.modiOperName = custOperator.getName();
        }
        this.setPlainInvoiceFlag("0");
        this.setSpecialInvoiceFlag("0");
    }
}