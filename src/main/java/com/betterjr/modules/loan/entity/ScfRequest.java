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
@Table(name = "t_scf_request")
public class ScfRequest implements BetterjrEntity {
    @Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    @OrderBy("desc")
    private String requestNo;

    /**
     * 保理产品编号
     */
    @Column(name = "C_PRODUCT_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="保理产品编号", comments = "保理产品编号")
    private String productCode;

    @Column(name = "L_BUYER_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String buyerNo;

    @Column(name = "L_SUPPLIER_NO",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long supplierNo;

    @Column(name = "C_BILLNO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String billNo;

    /**
     * 申请金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="申请金额", comments = "申请金额")
    private BigDecimal balance;

    /**
     * 审批金额
     */
    @Column(name = "F_APPROVED_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="审批金额", comments = "审批金额")
    private BigDecimal approvedBalance;

    /**
     * 实际融资金额
     */
    @Column(name = "F_CONFIRMBALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="实际融资金额", comments = "实际融资金额")
    private BigDecimal confirmBalance;

    /**
     * 实际申请日期
     */
    @Column(name = "D_REQUEST_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="实际申请日期", comments = "实际申请日期")
    private String requestDate;

    /**
     * 申请时间
     */
    @Column(name = "T_REQUEST_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="申请时间", comments = "申请时间")
    private String requestTime;

    /**
     * 保理公司业务单号
     */
    @Column(name = "C_FACTOR_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司业务单号", comments = "保理公司业务单号")
    private String factorRequestNo;

    /**
     * 下单日期
     */
    @Column(name = "D_OPERDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="下单日期", comments = "下单日期")
    private String operDate;

    /**
     * 下单时间
     */
    @Column(name = "T_OPERTIME",  columnDefinition="VARCHAR" )
    @MetaData( value="下单时间", comments = "下单时间")
    private String operTime;

    /**
     * 机构客户的复核人员名字
     */
    @Column(name = "C_CHECKER",  columnDefinition="VARCHAR" )
    @MetaData( value="机构客户的复核人员名字", comments = "机构客户的复核人员名字")
    private String checker;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员编码", comments = "操作员编码")
    private String operCode;

    @Column(name = "C_CHECKNO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String checkerNo;

    /**
     * 经办人姓名
     */
    @Column(name = "C_CONTACT_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="经办人姓名", comments = "经办人姓名")
    private String contactName;

    /**
     * 核心企业确认状态，0未确认，1已确认，2否决
     */
    @Column(name = "C_ADUIT",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业确认状态", comments = "核心企业确认状态，0未确认，1已确认，2否决")
    private String aduit;

    /**
     * 申请状态（100 申请，110 出具保理方案 ，120 融资方确认方案 ，130 融资背景确认，140 核心企业确认背景，150 放款确认 ，160 放款完成，170 逾期，180 展期，190 还款完成）
     */
    @Column(name = "C_TRADE_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="申请状态（100 申请", comments = "申请状态（100 申请，110 出具保理方案 ，120 融资方确认方案 ，130 融资背景确认，140 核心企业确认背景，150 放款确认 ，160 放款完成，170 逾期，180 展期，190 还款完成）")
    private String tradeStatus;

    @Column(name = "C_LAST_STATUS",  columnDefinition="VARCHAR" )//0中止，1 申请中，2审批中，3放款中，4还款中， 5逾期，6 展期，7坏帐，8 结清,
    @MetaData( value="", comments = "")
    private String lastStatus;

    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String operOrg;

    /**
     * 放款金额
     */
    @Column(name = "F_LOAN_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="放款金额", comments = "放款金额")
    private BigDecimal loanBalance;

    /**
     * 实际放款日期
     */
    @Column(name = "D_ACTUAL_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="实际放款日期", comments = "实际放款日期")
    private String actualDate;

    /**
     * 还款截止日期
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="还款截止日期", comments = "还款截止日期")
    private String endDate;

    /**
     * 结清日期
     */
    @Column(name = "D_CLEAN_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="结清日期", comments = "结清日期")
    private String cleanDate;

    /**
     * 申请利率
     */
    @Column(name = "F_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="申请利率", comments = "申请利率")
    private BigDecimal ratio;

    /**
     * 审批利率
     */
    @Column(name = "F_APPROVED_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="审批利率", comments = "审批利率")
    private BigDecimal approvedRatio;

    @Column(name = "C_CASH_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String cashRequestNo;

    @Column(name = "F_CREDITMONEY",  columnDefinition="DOUBLE" )
    @MetaData( value="", comments = "")
    private BigDecimal creditMoney;

    @Column(name = "F_REMAINMONEY",  columnDefinition="DOUBLE" )
    @MetaData( value="", comments = "")
    private BigDecimal remainMoney;

    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String description;

    /**
     * 保理公司编码
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编码", comments = "保理公司编码")
    private Long factorNo;

    @Column(name = "N_BILLID",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String billId;

    @Column(name = "C_CONFIRM_CAUSE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String confirmCause;

    /**
     * 申请企业名称
     */
    @Column(name = "C_CUST_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="申请企业名称", comments = "申请企业名称")
    private String custName;

    @Column(name = "F_INVOICEBALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="", comments = "")
    private BigDecimal invoiceBalance;

    /**
     * 产品id
     */
    @Column(name = "L_PRODUCT_ID",  columnDefinition="BIGINT" )
    @MetaData( value="产品id", comments = "产品id")
    private Long productId;

    /**
     * 申请期限
     */
    @Column(name = "N_PERIOD",  columnDefinition="INT" )
    @MetaData( value="申请期限", comments = "申请期限")
    private Integer period;

    /**
     * 期限单位：1：日，2月
     */
    @Column(name = "N_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="期限单位：1：日", comments = "期限单位：1：日，2月")
    private Integer periodUnit;

    /**
     * 申请审批期限
     */
    @Column(name = "N_APPROVED_PERIOD",  columnDefinition="INT" )
    @MetaData( value="申请审批期限", comments = "申请审批期限")
    private Integer approvedPeriod;

    /**
     * 审批期限单位： 1日，2月
     */
    @Column(name = "N_APPROVED_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="审批期限单位： 1日", comments = "审批期限单位： 1日，2月")
    private Integer approvedPeriodUnit;

    /**
     * 申请企业编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="申请企业编号", comments = "申请企业编号")
    private Long custNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 客户类型 1:供应商，2：经销商
     */
    @Column(name = "C_CUST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="客户类型 1:供应商", comments = "客户类型 1:供应商，2：经销商")
    private String custType;

    /**
     * 管理费利率
     */
    @Column(name = "F_MANAGEMENT_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="管理费利率", comments = "管理费利率")
    private BigDecimal managementRatio;

    /**
     * 一次性放款手续费利率
     */
    @Column(name = "F_SERVICEFEE_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="一次性放款手续费利率", comments = "一次性放款手续费利率")
    private BigDecimal servicefeeRatio;

    /**
     * 1,s订单，2:票据;3:应收款;4:经销商
     */
    @Column(name = "C_REQUEST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="1,s订单", comments = "1,s订单，2:票据;3:应收款;4:经销商")
    private String requestType;

    /**
     * 保证金
     */
    @Column(name = "F_BOND_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="保证金", comments = "保证金")
    private BigDecimal bondBalance;

    /**
     * 授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
     */
    @Column(name = "C_CREDIT_MODE",  columnDefinition="VARCHAR" )
    @MetaData( value="授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)", comments = "授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)")
    private String creditMode;

    /**
     * 申请流程状态：1：进行中，2：中止，3：结束
     */
    @Column(name = "C_FLOW_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="申请流程状态：1：进行中", comments = "申请流程状态：1：进行中，2：中止，3：结束")
    private String flowStatus;

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

    /**
     * 关联订单编号
     */
    @Column(name = "C_ORDERS",  columnDefinition="VARCHAR" )
    @MetaData( value="关联订单编号", comments = "关联订单编号")
    private String orders;
    
    /**
     * 发送的审批状态
     */
    @Transient
    private String outStatus;

    /**
     * 1:2.0, 2:微信票据
     */
    @Column(name = "C_REQUEST_FROM",  columnDefinition="CHAR" )
    @MetaData( value="1:2.0, 2:微信票据", comments = "1:2.0, 2:微信票据")
    private String requestFrom;

    /**
     * 报价ID
     */
    @Column(name = "L_OFFERID",  columnDefinition="BIGINT" )
    @MetaData( value="报价ID", comments = "报价ID")
    private Long offerId;
    

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INT" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Integer batchNo;
    
    @Column(name = "C_LOANNO",  columnDefinition="VARCHAR" )
    @MetaData( value="融资编号", comments = "融资编号")
    private String loanNo;
    
    @Column(name = "C_SUPP_BANK_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="收款方银行账户", comments = "收款方银行账户")
    private String suppBankAccount;
    
    private static final long serialVersionUID = 1474419650663L;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(String buyerNo) {
        this.buyerNo = buyerNo;
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getApprovedBalance() {
        return approvedBalance;
    }

    public void setApprovedBalance(BigDecimal approvedBalance) {
        this.approvedBalance = approvedBalance;
    }

    public BigDecimal getConfirmBalance() {
        return confirmBalance;
    }

    public void setConfirmBalance(BigDecimal confirmBalance) {
        this.confirmBalance = confirmBalance;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getFactorRequestNo() {
        return factorRequestNo;
    }

    public void setFactorRequestNo(String factorRequestNo) {
        this.factorRequestNo = factorRequestNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getCheckerNo() {
        return checkerNo;
    }

    public void setCheckerNo(String checkerNo) {
        this.checkerNo = checkerNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAduit() {
        return aduit;
    }

    public void setAduit(String aduit) {
        this.aduit = aduit;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
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

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getApprovedRatio() {
        return approvedRatio;
    }

    public void setApprovedRatio(BigDecimal approvedRatio) {
        this.approvedRatio = approvedRatio;
    }

    public String getCashRequestNo() {
        return cashRequestNo;
    }

    public void setCashRequestNo(String cashRequestNo) {
        this.cashRequestNo = cashRequestNo;
    }

    public BigDecimal getCreditMoney() {
        return creditMoney;
    }

    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }

    public BigDecimal getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getConfirmCause() {
        return confirmCause;
    }

    public void setConfirmCause(String confirmCause) {
        this.confirmCause = confirmCause;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public BigDecimal getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(BigDecimal invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public BigDecimal getManagementRatio() {
        return managementRatio;
    }

    public void setManagementRatio(BigDecimal managementRatio) {
        this.managementRatio = managementRatio;
    }

    public BigDecimal getServicefeeRatio() {
        return servicefeeRatio;
    }

    public void setServicefeeRatio(BigDecimal servicefeeRatio) {
        this.servicefeeRatio = servicefeeRatio;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public BigDecimal getBondBalance() {
        return bondBalance;
    }

    public void setBondBalance(BigDecimal bondBalance) {
        this.bondBalance = bondBalance;
    }

    public String getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(String creditMode) {
        this.creditMode = creditMode;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
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

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Integer getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Integer batchNo) {
        this.batchNo = batchNo;
    }

    public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getSuppBankAccount() {
		return suppBankAccount;
	}

	public void setSuppBankAccount(String suppBankAccount) {
		this.suppBankAccount = suppBankAccount;
	}

    @Override
	public String toString() {
		return "ScfRequest [requestNo=" + requestNo + ", productCode=" + productCode + ", buyerNo=" + buyerNo
				+ ", supplierNo=" + supplierNo + ", billNo=" + billNo + ", balance=" + balance + ", approvedBalance="
				+ approvedBalance + ", confirmBalance=" + confirmBalance + ", requestDate=" + requestDate
				+ ", requestTime=" + requestTime + ", factorRequestNo=" + factorRequestNo + ", operDate=" + operDate
				+ ", operTime=" + operTime + ", checker=" + checker + ", operCode=" + operCode + ", checkerNo="
				+ checkerNo + ", contactName=" + contactName + ", aduit=" + aduit + ", tradeStatus=" + tradeStatus
				+ ", lastStatus=" + lastStatus + ", operOrg=" + operOrg + ", loanBalance=" + loanBalance
				+ ", actualDate=" + actualDate + ", endDate=" + endDate + ", cleanDate=" + cleanDate + ", ratio="
				+ ratio + ", approvedRatio=" + approvedRatio + ", cashRequestNo=" + cashRequestNo + ", creditMoney="
				+ creditMoney + ", remainMoney=" + remainMoney + ", description=" + description + ", factorNo="
				+ factorNo + ", billId=" + billId + ", confirmCause=" + confirmCause + ", custName=" + custName
				+ ", invoiceBalance=" + invoiceBalance + ", productId=" + productId + ", period=" + period
				+ ", periodUnit=" + periodUnit + ", approvedPeriod=" + approvedPeriod + ", approvedPeriodUnit="
				+ approvedPeriodUnit + ", custNo=" + custNo + ", coreCustNo=" + coreCustNo + ", custType=" + custType
				+ ", managementRatio=" + managementRatio + ", servicefeeRatio=" + servicefeeRatio + ", requestType="
				+ requestType + ", bondBalance=" + bondBalance + ", creditMode=" + creditMode + ", flowStatus="
				+ flowStatus + ", regOperId=" + regOperId + ", modiOperId=" + modiOperId + ", modiOperName="
				+ modiOperName + ", modiDate=" + modiDate + ", modiTime=" + modiTime + ", version=" + version
				+ ", regOperName=" + regOperName + ", regDate=" + regDate + ", regTime=" + regTime + ", orders="
				+ orders + ", outStatus=" + outStatus + ", requestFrom=" + requestFrom + ", offerId=" + offerId
				+ ", batchNo=" + batchNo + ", loanNo=" + loanNo + ", suppBankAccount=" + suppBankAccount
				+ ", coreCustName=" + coreCustName + ", factorName=" + factorName + ", payPlan=" + payPlan
				+ ", orderBalance=" + orderBalance + ", productName=" + productName + ", servicefeeBalance="
				+ servicefeeBalance + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualDate == null) ? 0 : actualDate.hashCode());
		result = prime * result + ((aduit == null) ? 0 : aduit.hashCode());
		result = prime * result + ((approvedBalance == null) ? 0 : approvedBalance.hashCode());
		result = prime * result + ((approvedPeriod == null) ? 0 : approvedPeriod.hashCode());
		result = prime * result + ((approvedPeriodUnit == null) ? 0 : approvedPeriodUnit.hashCode());
		result = prime * result + ((approvedRatio == null) ? 0 : approvedRatio.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((batchNo == null) ? 0 : batchNo.hashCode());
		result = prime * result + ((billId == null) ? 0 : billId.hashCode());
		result = prime * result + ((billNo == null) ? 0 : billNo.hashCode());
		result = prime * result + ((bondBalance == null) ? 0 : bondBalance.hashCode());
		result = prime * result + ((buyerNo == null) ? 0 : buyerNo.hashCode());
		result = prime * result + ((cashRequestNo == null) ? 0 : cashRequestNo.hashCode());
		result = prime * result + ((checker == null) ? 0 : checker.hashCode());
		result = prime * result + ((checkerNo == null) ? 0 : checkerNo.hashCode());
		result = prime * result + ((cleanDate == null) ? 0 : cleanDate.hashCode());
		result = prime * result + ((confirmBalance == null) ? 0 : confirmBalance.hashCode());
		result = prime * result + ((confirmCause == null) ? 0 : confirmCause.hashCode());
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		result = prime * result + ((coreCustName == null) ? 0 : coreCustName.hashCode());
		result = prime * result + ((coreCustNo == null) ? 0 : coreCustNo.hashCode());
		result = prime * result + ((creditMode == null) ? 0 : creditMode.hashCode());
		result = prime * result + ((creditMoney == null) ? 0 : creditMoney.hashCode());
		result = prime * result + ((custName == null) ? 0 : custName.hashCode());
		result = prime * result + ((custNo == null) ? 0 : custNo.hashCode());
		result = prime * result + ((custType == null) ? 0 : custType.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((factorName == null) ? 0 : factorName.hashCode());
		result = prime * result + ((factorNo == null) ? 0 : factorNo.hashCode());
		result = prime * result + ((factorRequestNo == null) ? 0 : factorRequestNo.hashCode());
		result = prime * result + ((flowStatus == null) ? 0 : flowStatus.hashCode());
		result = prime * result + ((invoiceBalance == null) ? 0 : invoiceBalance.hashCode());
		result = prime * result + ((lastStatus == null) ? 0 : lastStatus.hashCode());
		result = prime * result + ((loanBalance == null) ? 0 : loanBalance.hashCode());
		result = prime * result + ((loanNo == null) ? 0 : loanNo.hashCode());
		result = prime * result + ((managementRatio == null) ? 0 : managementRatio.hashCode());
		result = prime * result + ((modiDate == null) ? 0 : modiDate.hashCode());
		result = prime * result + ((modiOperId == null) ? 0 : modiOperId.hashCode());
		result = prime * result + ((modiOperName == null) ? 0 : modiOperName.hashCode());
		result = prime * result + ((modiTime == null) ? 0 : modiTime.hashCode());
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + ((operCode == null) ? 0 : operCode.hashCode());
		result = prime * result + ((operDate == null) ? 0 : operDate.hashCode());
		result = prime * result + ((operOrg == null) ? 0 : operOrg.hashCode());
		result = prime * result + ((operTime == null) ? 0 : operTime.hashCode());
		result = prime * result + ((orderBalance == null) ? 0 : orderBalance.hashCode());
		result = prime * result + ((orders == null) ? 0 : orders.hashCode());
		result = prime * result + ((outStatus == null) ? 0 : outStatus.hashCode());
		result = prime * result + ((payPlan == null) ? 0 : payPlan.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((periodUnit == null) ? 0 : periodUnit.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((ratio == null) ? 0 : ratio.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((regOperId == null) ? 0 : regOperId.hashCode());
		result = prime * result + ((regOperName == null) ? 0 : regOperName.hashCode());
		result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
		result = prime * result + ((remainMoney == null) ? 0 : remainMoney.hashCode());
		result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
		result = prime * result + ((requestFrom == null) ? 0 : requestFrom.hashCode());
		result = prime * result + ((requestNo == null) ? 0 : requestNo.hashCode());
		result = prime * result + ((requestTime == null) ? 0 : requestTime.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((servicefeeBalance == null) ? 0 : servicefeeBalance.hashCode());
		result = prime * result + ((servicefeeRatio == null) ? 0 : servicefeeRatio.hashCode());
		result = prime * result + ((suppBankAccount == null) ? 0 : suppBankAccount.hashCode());
		result = prime * result + ((supplierNo == null) ? 0 : supplierNo.hashCode());
		result = prime * result + ((tradeStatus == null) ? 0 : tradeStatus.hashCode());
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
		ScfRequest other = (ScfRequest) obj;
		if (actualDate == null) {
			if (other.actualDate != null)
				return false;
		} else if (!actualDate.equals(other.actualDate))
			return false;
		if (aduit == null) {
			if (other.aduit != null)
				return false;
		} else if (!aduit.equals(other.aduit))
			return false;
		if (approvedBalance == null) {
			if (other.approvedBalance != null)
				return false;
		} else if (!approvedBalance.equals(other.approvedBalance))
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
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (batchNo == null) {
			if (other.batchNo != null)
				return false;
		} else if (!batchNo.equals(other.batchNo))
			return false;
		if (billId == null) {
			if (other.billId != null)
				return false;
		} else if (!billId.equals(other.billId))
			return false;
		if (billNo == null) {
			if (other.billNo != null)
				return false;
		} else if (!billNo.equals(other.billNo))
			return false;
		if (bondBalance == null) {
			if (other.bondBalance != null)
				return false;
		} else if (!bondBalance.equals(other.bondBalance))
			return false;
		if (buyerNo == null) {
			if (other.buyerNo != null)
				return false;
		} else if (!buyerNo.equals(other.buyerNo))
			return false;
		if (cashRequestNo == null) {
			if (other.cashRequestNo != null)
				return false;
		} else if (!cashRequestNo.equals(other.cashRequestNo))
			return false;
		if (checker == null) {
			if (other.checker != null)
				return false;
		} else if (!checker.equals(other.checker))
			return false;
		if (checkerNo == null) {
			if (other.checkerNo != null)
				return false;
		} else if (!checkerNo.equals(other.checkerNo))
			return false;
		if (cleanDate == null) {
			if (other.cleanDate != null)
				return false;
		} else if (!cleanDate.equals(other.cleanDate))
			return false;
		if (confirmBalance == null) {
			if (other.confirmBalance != null)
				return false;
		} else if (!confirmBalance.equals(other.confirmBalance))
			return false;
		if (confirmCause == null) {
			if (other.confirmCause != null)
				return false;
		} else if (!confirmCause.equals(other.confirmCause))
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
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
		if (creditMoney == null) {
			if (other.creditMoney != null)
				return false;
		} else if (!creditMoney.equals(other.creditMoney))
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
		if (custType == null) {
			if (other.custType != null)
				return false;
		} else if (!custType.equals(other.custType))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
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
		if (factorRequestNo == null) {
			if (other.factorRequestNo != null)
				return false;
		} else if (!factorRequestNo.equals(other.factorRequestNo))
			return false;
		if (flowStatus == null) {
			if (other.flowStatus != null)
				return false;
		} else if (!flowStatus.equals(other.flowStatus))
			return false;
		if (invoiceBalance == null) {
			if (other.invoiceBalance != null)
				return false;
		} else if (!invoiceBalance.equals(other.invoiceBalance))
			return false;
		if (lastStatus == null) {
			if (other.lastStatus != null)
				return false;
		} else if (!lastStatus.equals(other.lastStatus))
			return false;
		if (loanBalance == null) {
			if (other.loanBalance != null)
				return false;
		} else if (!loanBalance.equals(other.loanBalance))
			return false;
		if (loanNo == null) {
			if (other.loanNo != null)
				return false;
		} else if (!loanNo.equals(other.loanNo))
			return false;
		if (managementRatio == null) {
			if (other.managementRatio != null)
				return false;
		} else if (!managementRatio.equals(other.managementRatio))
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
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		if (operCode == null) {
			if (other.operCode != null)
				return false;
		} else if (!operCode.equals(other.operCode))
			return false;
		if (operDate == null) {
			if (other.operDate != null)
				return false;
		} else if (!operDate.equals(other.operDate))
			return false;
		if (operOrg == null) {
			if (other.operOrg != null)
				return false;
		} else if (!operOrg.equals(other.operOrg))
			return false;
		if (operTime == null) {
			if (other.operTime != null)
				return false;
		} else if (!operTime.equals(other.operTime))
			return false;
		if (orderBalance == null) {
			if (other.orderBalance != null)
				return false;
		} else if (!orderBalance.equals(other.orderBalance))
			return false;
		if (orders == null) {
			if (other.orders != null)
				return false;
		} else if (!orders.equals(other.orders))
			return false;
		if (outStatus == null) {
			if (other.outStatus != null)
				return false;
		} else if (!outStatus.equals(other.outStatus))
			return false;
		if (payPlan == null) {
			if (other.payPlan != null)
				return false;
		} else if (!payPlan.equals(other.payPlan))
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
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
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
		if (remainMoney == null) {
			if (other.remainMoney != null)
				return false;
		} else if (!remainMoney.equals(other.remainMoney))
			return false;
		if (requestDate == null) {
			if (other.requestDate != null)
				return false;
		} else if (!requestDate.equals(other.requestDate))
			return false;
		if (requestFrom == null) {
			if (other.requestFrom != null)
				return false;
		} else if (!requestFrom.equals(other.requestFrom))
			return false;
		if (requestNo == null) {
			if (other.requestNo != null)
				return false;
		} else if (!requestNo.equals(other.requestNo))
			return false;
		if (requestTime == null) {
			if (other.requestTime != null)
				return false;
		} else if (!requestTime.equals(other.requestTime))
			return false;
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType))
			return false;
		if (servicefeeBalance == null) {
			if (other.servicefeeBalance != null)
				return false;
		} else if (!servicefeeBalance.equals(other.servicefeeBalance))
			return false;
		if (servicefeeRatio == null) {
			if (other.servicefeeRatio != null)
				return false;
		} else if (!servicefeeRatio.equals(other.servicefeeRatio))
			return false;
		if (suppBankAccount == null) {
			if (other.suppBankAccount != null)
				return false;
		} else if (!suppBankAccount.equals(other.suppBankAccount))
			return false;
		if (supplierNo == null) {
			if (other.supplierNo != null)
				return false;
		} else if (!supplierNo.equals(other.supplierNo))
			return false;
		if (tradeStatus == null) {
			if (other.tradeStatus != null)
				return false;
		} else if (!tradeStatus.equals(other.tradeStatus))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}



	@Transient
    private String coreCustName;
    @Transient
    private String factorName;
    @Transient
    private ScfPayPlan payPlan;
    
    /*用于保存票据金额，如果有多张票则相加 (为满足票据版需求)*/
    @Transient 
    private BigDecimal orderBalance;
    @Transient
    private String productName;
    //手续费
    @Transient
    private BigDecimal servicefeeBalance;
    
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

    public ScfPayPlan getPayPlan() {
        return payPlan;
    }

    public void setPayPlan(ScfPayPlan payPlan) {
        this.payPlan = payPlan;
    }
    
    public BigDecimal getOrderBalance() {
        return orderBalance;
    }

    public void setOrderBalance(BigDecimal orderBalance) {
        this.orderBalance = orderBalance;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String anOutStatus) {
        outStatus = anOutStatus;
    }
    
    

    public BigDecimal getServicefeeBalance() {
        return servicefeeBalance;
    }

    public void setServicefeeBalance(BigDecimal servicefeeBalance) {
        this.servicefeeBalance = servicefeeBalance;
    }

    public void init() {
        this.requestNo = SerialGenerator.getLongValue("ScfRequest.id")+"";
        this.tradeStatus = "0";
        this.lastStatus = "0";
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();

    }

    public void init(ScfRequest request) {
        request.requestNo = SerialGenerator.getLongValue("ScfRequest.id")+"";
        request.tradeStatus = "0";
        request.lastStatus = "0";
        request.regOperName = UserUtils.getUserName();
        request.regOperId = UserUtils.getOperatorInfo().getId();
        request.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        request.regDate = BetterDateUtils.getNumDate();
        request.regTime = BetterDateUtils.getNumTime();
        fillBalance(request);
    }

    public void initModify(ScfRequest request) {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        //fillBalance(request);
    }
    
    public void initAutoModify(ScfRequest request) {
        this.modiOperId = this.regOperId;
        this.modiOperName = this.getRegOperName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        //fillBalance(request);
    }
    
    private void fillBalance(ScfRequest request){
        request.period = (null ==request.period)?0:request.period;
        request.confirmBalance = MathExtend.defaultValue(request.confirmBalance, BigDecimal.ZERO);
        request.loanBalance = MathExtend.defaultValue(request.loanBalance, BigDecimal.ZERO);
        request.creditMoney = MathExtend.defaultValue(request.creditMoney, BigDecimal.ZERO);
        request.ratio =MathExtend.defaultValue(request.ratio, BigDecimal.ZERO);
        request.approvedBalance = MathExtend.defaultValue(request.approvedBalance, BigDecimal.ZERO);
        request.remainMoney =  MathExtend.defaultValue(request.remainMoney, BigDecimal.ZERO);
        request.invoiceBalance =MathExtend.defaultValue(request.invoiceBalance, BigDecimal.ZERO);
        request.bondBalance = MathExtend.defaultValue(request.bondBalance, BigDecimal.ZERO);
        request.managementRatio = MathExtend.defaultValue(request.managementRatio, BigDecimal.ZERO);
        request.servicefeeRatio = MathExtend.defaultValue(request.servicefeeRatio, BigDecimal.ZERO);
        request.approvedRatio = MathExtend.defaultValue(request.approvedRatio, BigDecimal.ZERO);
        request.periodUnit = (null ==request.periodUnit)?approvedPeriodUnit:request.periodUnit;
        request.approvedPeriodUnit = (null ==request.approvedPeriodUnit)?1:request.approvedPeriodUnit;
    }
}