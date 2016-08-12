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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

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
     * 融资金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="融资金额", comments = "融资金额")
    private BigDecimal balance;

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
     * 申请状态
     */
    @Column(name = "C_TRADE_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="申请状态", comments = "申请状态")
    private String tradeStatus;

    @Column(name = "C_LAST_STATUS",  columnDefinition="VARCHAR" )
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
     * 利率
     */
    @Column(name = "F_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="利率", comments = "利率")
    private BigDecimal ratio;

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

    @Column(name = "L_PRODUCT_ID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long productId;

    /**
     * 申请期限
     */
    @Column(name = "N_PERIOD",  columnDefinition="INT" )
    @MetaData( value="申请期限", comments = "申请期限")
    private Integer period;

    /**
     * 期限单位：1：日，2月，3日
     */
    @Column(name = "N_PERIOD_UNIT",  columnDefinition="INT" )
    @MetaData( value="期限单位：1：日，2月，3日", comments = "期限单位：1：日，2月，3日")
    private Integer periodUnit;

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
     * 1,订单，2:票据;3:应收款;4:经销商
     */
    @Column(name = "C_REQUEST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="1,订单，2:票据;3:应收款;4:经销商", comments = "1,订单，2:票据;3:应收款;4:经销商")
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

    private static final long serialVersionUID = 1470300619165L;

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

    public BigDecimal getConfirmBalance() {
        return confirmBalance;
    }

    public void setConfirmBalance(BigDecimal confirmBalance) {
        this.confirmBalance = confirmBalance;
    }

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

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

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

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", requestNo=").append(requestNo);
        sb.append(", productCode=").append(productCode);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", billNo=").append(billNo);
        sb.append(", balance=").append(balance);
        sb.append(", confirmBalance=").append(confirmBalance);
        sb.append(", requestDate=").append(requestDate);
        sb.append(", requestTime=").append(requestTime);
        sb.append(", factorRequestNo=").append(factorRequestNo);
        sb.append(", operDate=").append(operDate);
        sb.append(", operTime=").append(operTime);
        sb.append(", checker=").append(checker);
        sb.append(", operCode=").append(operCode);
        sb.append(", checkerNo=").append(checkerNo);
        sb.append(", contactName=").append(contactName);
        sb.append(", aduit=").append(aduit);
        sb.append(", tradeStatus=").append(tradeStatus);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", loanBalance=").append(loanBalance);
        sb.append(", actualDate=").append(actualDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", cleanDate=").append(cleanDate);
        sb.append(", ratio=").append(ratio);
        sb.append(", cashRequestNo=").append(cashRequestNo);
        sb.append(", creditMoney=").append(creditMoney);
        sb.append(", remainMoney=").append(remainMoney);
        sb.append(", description=").append(description);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", billId=").append(billId);
        sb.append(", confirmCause=").append(confirmCause);
        sb.append(", custName=").append(custName);
        sb.append(", invoiceBalance=").append(invoiceBalance);
        sb.append(", productId=").append(productId);
        sb.append(", period=").append(period);
        sb.append(", periodUnit=").append(periodUnit);
        sb.append(", custNo=").append(custNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", custType=").append(custType);
        sb.append(", managementRatio=").append(managementRatio);
        sb.append(", servicefeeRatio=").append(servicefeeRatio);
        sb.append(", requestType=").append(requestType);
        sb.append(", bondBalance=").append(bondBalance);
        sb.append(", creditMode=").append(creditMode);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", version=").append(version);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", orders=").append(orders);
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
        ScfRequest other = (ScfRequest) that;
        return (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getBillNo() == null ? other.getBillNo() == null : this.getBillNo().equals(other.getBillNo()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getConfirmBalance() == null ? other.getConfirmBalance() == null : this.getConfirmBalance().equals(other.getConfirmBalance()))
            && (this.getRequestDate() == null ? other.getRequestDate() == null : this.getRequestDate().equals(other.getRequestDate()))
            && (this.getRequestTime() == null ? other.getRequestTime() == null : this.getRequestTime().equals(other.getRequestTime()))
            && (this.getFactorRequestNo() == null ? other.getFactorRequestNo() == null : this.getFactorRequestNo().equals(other.getFactorRequestNo()))
            && (this.getOperDate() == null ? other.getOperDate() == null : this.getOperDate().equals(other.getOperDate()))
            && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()))
            && (this.getChecker() == null ? other.getChecker() == null : this.getChecker().equals(other.getChecker()))
            && (this.getOperCode() == null ? other.getOperCode() == null : this.getOperCode().equals(other.getOperCode()))
            && (this.getCheckerNo() == null ? other.getCheckerNo() == null : this.getCheckerNo().equals(other.getCheckerNo()))
            && (this.getContactName() == null ? other.getContactName() == null : this.getContactName().equals(other.getContactName()))
            && (this.getAduit() == null ? other.getAduit() == null : this.getAduit().equals(other.getAduit()))
            && (this.getTradeStatus() == null ? other.getTradeStatus() == null : this.getTradeStatus().equals(other.getTradeStatus()))
            && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(other.getLastStatus()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getLoanBalance() == null ? other.getLoanBalance() == null : this.getLoanBalance().equals(other.getLoanBalance()))
            && (this.getActualDate() == null ? other.getActualDate() == null : this.getActualDate().equals(other.getActualDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getCleanDate() == null ? other.getCleanDate() == null : this.getCleanDate().equals(other.getCleanDate()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getCashRequestNo() == null ? other.getCashRequestNo() == null : this.getCashRequestNo().equals(other.getCashRequestNo()))
            && (this.getCreditMoney() == null ? other.getCreditMoney() == null : this.getCreditMoney().equals(other.getCreditMoney()))
            && (this.getRemainMoney() == null ? other.getRemainMoney() == null : this.getRemainMoney().equals(other.getRemainMoney()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getBillId() == null ? other.getBillId() == null : this.getBillId().equals(other.getBillId()))
            && (this.getConfirmCause() == null ? other.getConfirmCause() == null : this.getConfirmCause().equals(other.getConfirmCause()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getInvoiceBalance() == null ? other.getInvoiceBalance() == null : this.getInvoiceBalance().equals(other.getInvoiceBalance()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
            && (this.getPeriodUnit() == null ? other.getPeriodUnit() == null : this.getPeriodUnit().equals(other.getPeriodUnit()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getCustType() == null ? other.getCustType() == null : this.getCustType().equals(other.getCustType()))
            && (this.getManagementRatio() == null ? other.getManagementRatio() == null : this.getManagementRatio().equals(other.getManagementRatio()))
            && (this.getServicefeeRatio() == null ? other.getServicefeeRatio() == null : this.getServicefeeRatio().equals(other.getServicefeeRatio()))
            && (this.getRequestType() == null ? other.getRequestType() == null : this.getRequestType().equals(other.getRequestType()))
            && (this.getBondBalance() == null ? other.getBondBalance() == null : this.getBondBalance().equals(other.getBondBalance()))
            && (this.getCreditMode() == null ? other.getCreditMode() == null : this.getCreditMode().equals(other.getCreditMode()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getOrders() == null ? other.getOrders() == null : this.getOrders().equals(other.getOrders()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getBillNo() == null) ? 0 : getBillNo().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getConfirmBalance() == null) ? 0 : getConfirmBalance().hashCode());
        result = prime * result + ((getRequestDate() == null) ? 0 : getRequestDate().hashCode());
        result = prime * result + ((getRequestTime() == null) ? 0 : getRequestTime().hashCode());
        result = prime * result + ((getFactorRequestNo() == null) ? 0 : getFactorRequestNo().hashCode());
        result = prime * result + ((getOperDate() == null) ? 0 : getOperDate().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getChecker() == null) ? 0 : getChecker().hashCode());
        result = prime * result + ((getOperCode() == null) ? 0 : getOperCode().hashCode());
        result = prime * result + ((getCheckerNo() == null) ? 0 : getCheckerNo().hashCode());
        result = prime * result + ((getContactName() == null) ? 0 : getContactName().hashCode());
        result = prime * result + ((getAduit() == null) ? 0 : getAduit().hashCode());
        result = prime * result + ((getTradeStatus() == null) ? 0 : getTradeStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getLoanBalance() == null) ? 0 : getLoanBalance().hashCode());
        result = prime * result + ((getActualDate() == null) ? 0 : getActualDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getCleanDate() == null) ? 0 : getCleanDate().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getCashRequestNo() == null) ? 0 : getCashRequestNo().hashCode());
        result = prime * result + ((getCreditMoney() == null) ? 0 : getCreditMoney().hashCode());
        result = prime * result + ((getRemainMoney() == null) ? 0 : getRemainMoney().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getBillId() == null) ? 0 : getBillId().hashCode());
        result = prime * result + ((getConfirmCause() == null) ? 0 : getConfirmCause().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getInvoiceBalance() == null) ? 0 : getInvoiceBalance().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getPeriodUnit() == null) ? 0 : getPeriodUnit().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getManagementRatio() == null) ? 0 : getManagementRatio().hashCode());
        result = prime * result + ((getServicefeeRatio() == null) ? 0 : getServicefeeRatio().hashCode());
        result = prime * result + ((getRequestType() == null) ? 0 : getRequestType().hashCode());
        result = prime * result + ((getBondBalance() == null) ? 0 : getBondBalance().hashCode());
        result = prime * result + ((getCreditMode() == null) ? 0 : getCreditMode().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getOrders() == null) ? 0 : getOrders().hashCode());
        return result;
    }
    
    @Transient
    private String coreCustName;
    @Transient
    private String factorName;
    @Transient
    private ScfPayPlan payPlan;
    @Transient
    private BigDecimal approvedRatio;
    @Transient
    private Integer approvedPeriod;
    @Transient
    private Integer approvedPeriodUnit;
    @Transient
    private BigDecimal approvedBalance;
    
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
    
    public BigDecimal getApprovedRatio() {
        return approvedRatio;
    }

    public void setApprovedRatio(BigDecimal approvedRatio) {
        this.approvedRatio = approvedRatio;
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

    public BigDecimal getApprovedBalance() {
        return approvedBalance;
    }

    public void setApprovedBalance(BigDecimal approvedBalance) {
        this.approvedBalance = approvedBalance;
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

    public void initModify() {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }
}