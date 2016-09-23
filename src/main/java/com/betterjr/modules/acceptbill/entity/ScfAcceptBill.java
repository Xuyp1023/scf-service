package com.betterjr.modules.acceptbill.entity;

import java.math.BigDecimal;
import java.util.List;

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
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.acceptbill.data.ScfClientDataParentFace;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ACCEPT_BILL")
public class ScfAcceptBill implements BetterjrEntity,ScfClientDataParentFace {
    
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 票据编号
     */
    @Column(name = "C_BILLNO", columnDefinition = "VARCHAR")
    @MetaData(value = "票据编号", comments = "票据编号")
    private String billNo;

    /**
     * 票据类型;0：商业票据，1：银行票据
     */
    @Column(name = "C_BILL_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "票据类型;0：商业票据", comments = "票据类型;0：商业票据，1：银行票据")
    private String billType;

    /**
     * 票据流通方式;0：纸票，1：电票
     */
    @Column(name = "C_BILL_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "票据流通方式;0：纸票", comments = "票据流通方式;0：纸票，1：电票")
    private String billMode;

    /**
     * 确定的金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "确定的金额", comments = "确定的金额")
    private BigDecimal balance;

    /**
     * 开票日期
     */
    @Column(name = "D_INVOICE_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "开票日期", comments = "开票日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @OrderBy("DESC")
    private String invoiceDate;


    /**
     * 开票单位
     */
    @Column(name = "C_INVOICE_CORP",  columnDefinition="VARCHAR" )
    @MetaData( value="开票单位", comments = "开票单位")
    private String invoiceCorp;
    
    /**
     * 开票人在资金管理系统中的企业编号
     */
    @Column(name = "C_DRAWERID",  columnDefinition="VARCHAR" )
    @MetaData( value="开票人在资金管理系统中的企业编号", comments = "开票人在资金管理系统中的企业编号")
    private String drawerId;
    
    
    /**
     * 到期日期
     */
    @Column(name = "D_END_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "到期日期", comments = "到期日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String endDate;

    /**
     * 兑付日期
     */
    @Column(name = "D_CASH_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "兑付日期", comments = "兑付日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String cashDate;

    /**
     * 持票人
     */
    @Column(name = "C_HOLDER",  columnDefinition="VARCHAR" )
    @MetaData( value="持票人", comments = "持票人")
    private String holder ;
    
    /**
     * 持票人客户号
     */
    @Column(name = "L_HOLDER_NO",  columnDefinition="BIGINT" )
    @MetaData( value="持票人客户号", comments = "持票人客户号")
    private Long holderNo;
    
    /**
     * 持票人帐号
     */
    @Column(name = "C_HOLDER_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="持票人帐号", comments = "持票人帐号")
    private String holderBankAccount;
    
    /**
     * 承兑人
     */
    @Column(name = "C_ACCEPTOR",  columnDefinition="VARCHAR" )
    @MetaData( value="承兑人", comments = "承兑人")
    private String acceptor;
    
    /**
     * 承兑人银行账户
     */
    @Column(name = "C_ACCEPTOR_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="承兑人银行账户", comments = "承兑人银行账户")
    private String acceptorBankAccount;
    
    /**
     * 收款人名称
     */
    @Column(name = "C_SUPPLIER", columnDefinition = "VARCHAR")
    @MetaData(value = "收款人名称", comments = "收款人名称")
    private String supplier;

    /**
     * 收款方银行账户
     */
    @Column(name = "C_SUPPLIER_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "收款方银行账户", comments = "收款方银行账户")
    private String suppBankAccount;

    /**
     * 收款方银行全称
     */
    @Column(name = "C_SUPPLIER_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "收款方银行全称", comments = "收款方银行全称")
    private String suppBankName;

    /**
     * 付款人名称
     */
    @Column(name = "C_BUYER", columnDefinition = "VARCHAR")
    @MetaData(value = "付款人名称", comments = "付款人名称")
    private String buyer;

    /**
     * 实际付款人名称
     */
    @Column(name = "C_BUYER_REAL", columnDefinition = "VARCHAR")
    @MetaData(value = "实际付款人名称", comments = "实际付款人名称")
    private String realBuyer;

    /**
     * 付款方银行账户
     */
    @Column(name = "C_BUYER_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "付款方银行账户", comments = "付款方银行账户")
    private String buyerBankAccount;

    /**
     * 付款方银行全称
     */
    @Column(name = "C_BUYER_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "付款方银行全称", comments = "付款方银行全称")
    private String buyerBankName;

    /**
     * 状态，0未处理，1完善资料，2已融资，3已过期
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0未处理，1完善资料，2已融资，3已过期")
    private String businStatus;

    /**
     * 融资标志，0未融资，1已融资，2收款，3已还款
     */
    @Column(name = "C_FINANCE_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "融资标志", comments = "融资标志，0未融资，1已融资，2收款，3已还款，4融资失败")
    private String financeFlag;
    
    /**
     * 票据来源，0库存票据，1背书转让
     */
    @Column(name = "C_BILLFROM",  columnDefinition="VARCHAR" )
    @MetaData( value="票据来源", comments = "票据来源，0库存票据，1背书转让")
    private String billFrom;

    /**
     * 前手
     */
    @Column(name = "C_PRE_HAND",  columnDefinition="VARCHAR" )
    @MetaData( value="前手", comments = "前手")
    private String preHand;

    /**
     * 后手
     */
    @Column(name = "C_NEXT_HAND",  columnDefinition="VARCHAR" )
    @MetaData( value="后手", comments = "后手")
    private String nextHand;

    /**
     * 是否审核，0未审核，1已审核
     */
    @Column(name = "C_ADUIT",  columnDefinition="VARCHAR" )
    @MetaData( value="是否审核", comments = "是否审核，0未审核，1已审核")
    private String aduit;


    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 拜特内部编号
     */
    @Column(name = "C_BTBILLNO", columnDefinition = "VARCHAR")
    @MetaData(value = "拜特内部编号", comments = "拜特内部编号")
    @JsonIgnore
    private String btBillNo;

    /**
     * 买方客户号
     */
    @Column(name = "L_BUYER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "买方客户号", comments = "买方客户号")
    private Long buyerNo;

/*    @Transient
    private String buyerName;*/

    /**
     * 卖方客户号
     */
    @Column(name = "L_SUPPLIER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "卖方客户号", comments = "卖方客户号")
    private Long supplierNo;

    /*@Transient
    private String supplierName;*/

    /**
     * 合同编号
     */
    @Column(name = "C_AGREENO", columnDefinition = "VARCHAR")
    @MetaData(value = "合同编号", comments = "合同编号")
    private String agreeNo;

    /**
     * 操作员编码
     */
    @Column(name = "L_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "操作员编码", comments = "操作员编码")
    @JsonIgnore
    private Long operId;

    /**
     * 操作员名字
     */
    @Column(name = "C_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员名字", comments = "操作员名字")
    @JsonIgnore
    private String operName;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;


    /**
     * 其他资料，对应fileinfo中的ID
     */
    @Column(name = "N_OTHERBATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="其他资料", comments = "其他资料，对应fileinfo中的ID")
    private Long otherBatchNo;

    /**
     * 合同ID号
     */
    @Column(name = "L_AGREEID", columnDefinition = "INTEGER")
    @MetaData(value = "合同ID号", comments = "合同ID号")
    private Long agreeId;
    
    /**
     * 实际融资金额
     */
    @Column(name = "F_CONFIRM_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "实际融资金额", comments = "实际融资金额")
    private BigDecimal confirmBalance;

    /**
     * 实际放款金额
     */
    @Column(name = "F_LOAN_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "实际放款金额", comments = "实际放款金额")
    private BigDecimal loanBalance;
    
    /**
     * 利率
     */
    @Column(name = "F_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "利率", comments = "利率")
    private BigDecimal ratio;

    /**
     * 保理用途
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "保理用途", comments = "保理用途")
    private String description;

    
    /**
     * 核心企业客户号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业客户号", comments = "核心企业客户号")
    private Long coreCustNo;

    /**
     * 修改操作员ID号
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "修改操作员ID号", comments = "修改操作员ID号")
    private Long modiOperId;

    /**
     * 修改操作员姓名
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改操作员姓名", comments = "修改操作员姓名")
    private String modiOperName;

    /**
     * 修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;
    
    /**
     * 数据来源；00：核心企业自动，01：核心企业手工，10：供应商手工，11：供应商自动
     */
    @Column(name = "C_SOURCE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据来源", comments = "数据来源；00：核心企业自动，01：核心企业手工，10：供应商手工，11：供应商自动")
    private String dataSource;

    /**
     * 前手客户号
     */
    @Column(name = "C_PRE_HAND_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="前手客户号", comments = "前手客户号")
    private Long preHandNo;

    /**
     * 后手客户号
     */
    @Column(name = "C_NEXT_HAND_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="后手客户号", comments = "后手客户号")
    private Long nextHandNo;
    
    /**
     * 背书转让来源票据流水号
     */
    @Column(name = "L_TRANSFERID",  columnDefinition="INTEGER" )
    @MetaData( value="背书转让来源票据流水号", comments = "背书转让来源票据流水号")
    private Long transferId;

    @Transient
    private String coreCustName;
    
    /**
     * 合同列表
     */
    @Transient
    private List<CustAgreement> agreementList;
    
    @Transient
    private List<ScfOrder> orderList;
    
    @Transient
    private List<ScfInvoice> invoiceList;
    
    @Transient
    private List<ScfTransport> transportList;
    
    @Transient
    private List<ScfReceivable> receivableList;
  
    /**
     * 发票总金额
     */
    @Transient
    private BigDecimal invoiceBalanceSum;
    
    @Transient
    private String coreOperOrg;
     
     
    private static final long serialVersionUID = 1458262605550L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType == null ? null : billType.trim();
    }

    public String getBillMode() {
        return billMode;
    }

    public void setBillMode(String billMode) {
        this.billMode = billMode == null ? null : billMode.trim();
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate == null ? null : invoiceDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getCashDate() {
        return cashDate;
    }

    public void setCashDate(String cashDate) {
        this.cashDate = cashDate == null ? null : cashDate.trim();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public String getSuppBankAccount() {
        return suppBankAccount;
    }

    public void setSuppBankAccount(String suppBankAccount) {
        this.suppBankAccount = suppBankAccount == null ? null : suppBankAccount.trim();
    }

    public String getSuppBankName() {
        return suppBankName;
    }

    public void setSuppBankName(String suppBankName) {
        this.suppBankName = suppBankName == null ? null : suppBankName.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String anBuyer) {
        this.buyer = anBuyer == null ? null : anBuyer.trim();
    }

    public String getRealBuyer() {
        return realBuyer;
    }

    public void setRealBuyer(String realBuyer) {
        this.realBuyer = realBuyer == null ? null : realBuyer.trim();
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount == null ? null : buyerBankAccount.trim();
    }

    public String getBuyerBankName() {
        return buyerBankName;
    }

    public void setBuyerBankName(String buyerBankName) {
        this.buyerBankName = buyerBankName == null ? null : buyerBankName.trim();
    }
 
    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getFinanceFlag() {
        return financeFlag;
    }

    public void setFinanceFlag(String financeFlag) {
        this.financeFlag = financeFlag == null ? null : financeFlag.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getBtBillNo() {
        return btBillNo;
    }

    public void setBtBillNo(String btBillNo) {
        this.btBillNo = btBillNo == null ? null : btBillNo.trim();
    }

    public Long getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(Long buyerNo) {
        this.buyerNo = buyerNo;
    }

    public Long getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(Long supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo == null ? null : agreeNo.trim();
    }
 
    public Long getOperId() {
        return this.operId;
    }

    public void setOperId(Long anOperId) {
        this.operId = anOperId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public BigDecimal getFactorPercent() {

        return MathExtend.divide(this.getConfirmBalance(), this.getBalance(), 4);
    }

    public void setFactorPercent(BigDecimal anFactorPercent) {

    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getConfirmBalance() {
        return this.confirmBalance;
    }

    public void setConfirmBalance(BigDecimal anConfirmBalance) {
        this.confirmBalance = anConfirmBalance;
    }

    public Long getAgreeId() {
        return this.agreeId;
    }

    public void setAgreeId(Long anAgreeId) {
        this.agreeId = anAgreeId;
    }

    public BigDecimal getInvoiceBalanceSum() {
        return this.invoiceBalanceSum;
    }

    public void setInvoiceBalanceSum(BigDecimal anInvoiceBalanceSum) {
        this.invoiceBalanceSum = anInvoiceBalanceSum;
    }
 
    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public BigDecimal getLoanBalance() {
        return this.loanBalance;
    }

    public void setLoanBalance(BigDecimal anLoanBalance) {
        this.loanBalance = anLoanBalance;
    }

    public String getCoreOperOrg() {
        return this.coreOperOrg;
    }

    public void setCoreOperOrg(String anCoreOperOrg) {
        this.coreOperOrg = anCoreOperOrg;
    }
    
    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
    
    public List<ScfOrder> getOrderList() {
        return this.orderList;
    }

    public void setOrderList(List<ScfOrder> anOrderList) {
        this.orderList = anOrderList;
    }

    public List<ScfInvoice> getInvoiceList() {
        return this.invoiceList;
    }

    public void setInvoiceList(List<ScfInvoice> anInvoiceList) {
        this.invoiceList = anInvoiceList;
    }

    public List<ScfTransport> getTransportList() {
        return this.transportList;
    }

    public void setTransportList(List<ScfTransport> anTransportList) {
        this.transportList = anTransportList;
    }

    public List<ScfReceivable> getReceivableList() {
        return this.receivableList;
    }

    public void setReceivableList(List<ScfReceivable> anReceivableList) {
        this.receivableList = anReceivableList;
    }

    public List<CustAgreement> getAgreementList() {
        return this.agreementList;
    }

    public void setAgreementList(List<CustAgreement> anAgreementList) {
        this.agreementList = anAgreementList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", billNo=").append(billNo);
        sb.append(", billType=").append(billType);
        sb.append(", billMode=").append(billMode);
        sb.append(", balance=").append(balance);
        sb.append(", invoiceDate=").append(invoiceDate);
        sb.append(", invoiceCorp=").append(invoiceCorp);
        sb.append(", drawerId=").append(drawerId);
        sb.append(", endDate=").append(endDate);
        sb.append(", cashDate=").append(cashDate);
        sb.append(", holder =").append(holder );
        sb.append(", holderNo=").append(holderNo);
        sb.append(", holderBankAccount=").append(holderBankAccount);
        sb.append(", acceptor=").append(acceptor);
        sb.append(", acceptorBankAccount=").append(acceptorBankAccount);
        sb.append(", supplier=").append(supplier);
        sb.append(", suppBankAccount=").append(suppBankAccount);
        sb.append(", suppBankName=").append(suppBankName);
        sb.append(", buyer=").append(buyer);
        sb.append(", realBuyer=").append(realBuyer);
        sb.append(", buyerBankAccount=").append(buyerBankAccount);
        sb.append(", buyerBankName=").append(buyerBankName);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", financeFlag=").append(financeFlag);
        sb.append(", billFrom=").append(billFrom);
        sb.append(", preHand=").append(preHand);
        sb.append(", nextHand=").append(nextHand);
        sb.append(", aduit=").append(aduit);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", btBillNo=").append(btBillNo);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", agreeNo=").append(agreeNo);
        sb.append(", operId=").append(operId);
        sb.append(", operName=").append(operName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", otherBatchNo=").append(otherBatchNo);
        sb.append(", agreeId=").append(agreeId);
        sb.append(", confirmBalance=").append(confirmBalance);
        sb.append(", loanBalance=").append(loanBalance);
        sb.append(", ratio=").append(ratio);
        sb.append(", description=").append(description);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", dataSource=").append(dataSource);
        sb.append(", preHandNo=").append(preHandNo);
        sb.append(", nextHandNo=").append(nextHandNo);
        sb.append(", transferId=").append(transferId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public String getBillFrom() {
        return this.billFrom;
    }

    public void setBillFrom(String anBillFrom) {
        this.billFrom = anBillFrom;
    }

    public String getPreHand() {
        return this.preHand;
    }

    public void setPreHand(String anPreHand) {
        this.preHand = anPreHand;
    }

    public String getNextHand() {
        return this.nextHand;
    }

    public void setNextHand(String anNextHand) {
        this.nextHand = anNextHand;
    }

    public String getAduit() {
        return this.aduit;
    }

    public void setAduit(String anAduit) {
        this.aduit = anAduit;
    }

    public Long getOtherBatchNo() {
        return this.otherBatchNo;
    }

    public void setOtherBatchNo(Long anOtherBatchNo) {
        this.otherBatchNo = anOtherBatchNo;
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }
 

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String anDataSource) {
        this.dataSource = anDataSource;
    }

    public Long getPreHandNo() {
        return this.preHandNo;
    }

    public void setPreHandNo(Long anPreHandNo) {
        this.preHandNo = anPreHandNo;
    }

    public Long getNextHandNo() {
        return this.nextHandNo;
    }

    public void setNextHandNo(Long anNextHandNo) {
        this.nextHandNo = anNextHandNo;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }
 
    public String getInvoiceCorp() {
        return this.invoiceCorp;
    }

    public void setInvoiceCorp(String anInvoiceCorp) {
        this.invoiceCorp = anInvoiceCorp;
    }

    public String getDrawerId() {
        return this.drawerId;
    }

    public void setDrawerId(String anDrawerId) {
        this.drawerId = anDrawerId;
    }

    public String getHolder() {
        return this.holder;
    }

    public void setHolder(String anHolder) {
        this.holder = anHolder;
    }

    public Long getHolderNo() {
        return this.holderNo;
    }

    public void setHolderNo(Long anHolderNo) {
        this.holderNo = anHolderNo;
    }

    public String getHolderBankAccount() {
        return this.holderBankAccount;
    }

    public void setHolderBankAccount(String anHolderBankAccount) {
        this.holderBankAccount = anHolderBankAccount;
    }

    public String getAcceptor() {
        return this.acceptor;
    }

    public void setAcceptor(String anAcceptor) {
        this.acceptor = anAcceptor;
    }

    public String getAcceptorBankAccount() {
        return this.acceptorBankAccount;
    }

    public void setAcceptorBankAccount(String anAcceptorBankAccount) {
        this.acceptorBankAccount = anAcceptorBankAccount;
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
        ScfAcceptBill other = (ScfAcceptBill) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBillNo() == null ? other.getBillNo() == null : this.getBillNo().equals(other.getBillNo()))
            && (this.getBillType() == null ? other.getBillType() == null : this.getBillType().equals(other.getBillType()))
            && (this.getBillMode() == null ? other.getBillMode() == null : this.getBillMode().equals(other.getBillMode()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getInvoiceDate() == null ? other.getInvoiceDate() == null : this.getInvoiceDate().equals(other.getInvoiceDate()))
            && (this.getInvoiceCorp() == null ? other.getInvoiceCorp() == null : this.getInvoiceCorp().equals(other.getInvoiceCorp()))
            && (this.getDrawerId() == null ? other.getDrawerId() == null : this.getDrawerId().equals(other.getDrawerId()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getCashDate() == null ? other.getCashDate() == null : this.getCashDate().equals(other.getCashDate()))
            && (this.getHolder () == null ? other.getHolder () == null : this.getHolder ().equals(other.getHolder ()))
            && (this.getHolderNo() == null ? other.getHolderNo() == null : this.getHolderNo().equals(other.getHolderNo()))
            && (this.getHolderBankAccount() == null ? other.getHolderBankAccount() == null : this.getHolderBankAccount().equals(other.getHolderBankAccount()))
            && (this.getAcceptor() == null ? other.getAcceptor() == null : this.getAcceptor().equals(other.getAcceptor()))
            && (this.getAcceptorBankAccount() == null ? other.getAcceptorBankAccount() == null : this.getAcceptorBankAccount().equals(other.getAcceptorBankAccount()))
            && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
            && (this.getSuppBankAccount() == null ? other.getSuppBankAccount() == null : this.getSuppBankAccount().equals(other.getSuppBankAccount()))
            && (this.getSuppBankName() == null ? other.getSuppBankName() == null : this.getSuppBankName().equals(other.getSuppBankName()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getRealBuyer() == null ? other.getRealBuyer() == null : this.getRealBuyer().equals(other.getRealBuyer()))
            && (this.getBuyerBankAccount() == null ? other.getBuyerBankAccount() == null : this.getBuyerBankAccount().equals(other.getBuyerBankAccount()))
            && (this.getBuyerBankName() == null ? other.getBuyerBankName() == null : this.getBuyerBankName().equals(other.getBuyerBankName()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getFinanceFlag() == null ? other.getFinanceFlag() == null : this.getFinanceFlag().equals(other.getFinanceFlag()))
            && (this.getBillFrom() == null ? other.getBillFrom() == null : this.getBillFrom().equals(other.getBillFrom()))
            && (this.getPreHand() == null ? other.getPreHand() == null : this.getPreHand().equals(other.getPreHand()))
            && (this.getNextHand() == null ? other.getNextHand() == null : this.getNextHand().equals(other.getNextHand()))
            && (this.getAduit() == null ? other.getAduit() == null : this.getAduit().equals(other.getAduit()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getBtBillNo() == null ? other.getBtBillNo() == null : this.getBtBillNo().equals(other.getBtBillNo()))
            && (this.getBuyerNo() == null ? other.getBuyerNo() == null : this.getBuyerNo().equals(other.getBuyerNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getAgreeNo() == null ? other.getAgreeNo() == null : this.getAgreeNo().equals(other.getAgreeNo()))
            && (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getOtherBatchNo() == null ? other.getOtherBatchNo() == null : this.getOtherBatchNo().equals(other.getOtherBatchNo()))
            && (this.getAgreeId() == null ? other.getAgreeId() == null : this.getAgreeId().equals(other.getAgreeId()))
            && (this.getConfirmBalance() == null ? other.getConfirmBalance() == null : this.getConfirmBalance().equals(other.getConfirmBalance()))
            && (this.getLoanBalance() == null ? other.getLoanBalance() == null : this.getLoanBalance().equals(other.getLoanBalance()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getDataSource() == null ? other.getDataSource() == null : this.getDataSource().equals(other.getDataSource()))
            && (this.getPreHandNo() == null ? other.getPreHandNo() == null : this.getPreHandNo().equals(other.getPreHandNo()))
            && (this.getNextHandNo() == null ? other.getNextHandNo() == null : this.getNextHandNo().equals(other.getNextHandNo()))
            && (this.getTransferId() == null ? other.getTransferId() == null : this.getTransferId().equals(other.getTransferId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBillNo() == null) ? 0 : getBillNo().hashCode());
        result = prime * result + ((getBillType() == null) ? 0 : getBillType().hashCode());
        result = prime * result + ((getBillMode() == null) ? 0 : getBillMode().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getInvoiceDate() == null) ? 0 : getInvoiceDate().hashCode());
        result = prime * result + ((getInvoiceCorp() == null) ? 0 : getInvoiceCorp().hashCode());
        result = prime * result + ((getDrawerId() == null) ? 0 : getDrawerId().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getCashDate() == null) ? 0 : getCashDate().hashCode());
        result = prime * result + ((getHolder () == null) ? 0 : getHolder ().hashCode());
        result = prime * result + ((getHolderNo() == null) ? 0 : getHolderNo().hashCode());
        result = prime * result + ((getHolderBankAccount() == null) ? 0 : getHolderBankAccount().hashCode());
        result = prime * result + ((getAcceptor() == null) ? 0 : getAcceptor().hashCode());
        result = prime * result + ((getAcceptorBankAccount() == null) ? 0 : getAcceptorBankAccount().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
        result = prime * result + ((getSuppBankAccount() == null) ? 0 : getSuppBankAccount().hashCode());
        result = prime * result + ((getSuppBankName() == null) ? 0 : getSuppBankName().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getRealBuyer() == null) ? 0 : getRealBuyer().hashCode());
        result = prime * result + ((getBuyerBankAccount() == null) ? 0 : getBuyerBankAccount().hashCode());
        result = prime * result + ((getBuyerBankName() == null) ? 0 : getBuyerBankName().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getFinanceFlag() == null) ? 0 : getFinanceFlag().hashCode());
        result = prime * result + ((getBillFrom() == null) ? 0 : getBillFrom().hashCode());
        result = prime * result + ((getPreHand() == null) ? 0 : getPreHand().hashCode());
        result = prime * result + ((getNextHand() == null) ? 0 : getNextHand().hashCode());
        result = prime * result + ((getAduit() == null) ? 0 : getAduit().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getBtBillNo() == null) ? 0 : getBtBillNo().hashCode());
        result = prime * result + ((getBuyerNo() == null) ? 0 : getBuyerNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getAgreeNo() == null) ? 0 : getAgreeNo().hashCode());
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getOtherBatchNo() == null) ? 0 : getOtherBatchNo().hashCode());
        result = prime * result + ((getAgreeId() == null) ? 0 : getAgreeId().hashCode());
        result = prime * result + ((getConfirmBalance() == null) ? 0 : getConfirmBalance().hashCode());
        result = prime * result + ((getLoanBalance() == null) ? 0 : getLoanBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getDataSource() == null) ? 0 : getDataSource().hashCode());
        result = prime * result + ((getPreHandNo() == null) ? 0 : getPreHandNo().hashCode());
        result = prime * result + ((getNextHandNo() == null) ? 0 : getNextHandNo().hashCode());
        result = prime * result + ((getTransferId() == null) ? 0 : getTransferId().hashCode());
        return result;
    }


    public void initFactorInfo(ScfAcceptBill anBill) {
        this.description = anBill.getDescription();
        this.confirmBalance = anBill.getConfirmBalance();
        this.ratio = anBill.getRatio();
        this.modiDate = BetterDateUtils.getNumDate();
        if (BetterStringUtils.isNotBlank(anBill.getBusinStatus())) {
            this.businStatus = anBill.getBusinStatus();
        }
        if (BetterStringUtils.isNotBlank(anBill.getFinanceFlag())) {
            this.financeFlag = anBill.getFinanceFlag();
        }
    }

    public BigDecimal getRatio() {
        return this.ratio;
    }

    public void setRatio(BigDecimal anRatio) {
        this.ratio = anRatio;
    }

    public ScfAcceptBill() {

    }

    public void modifyValue(ScfAcceptBill anBill){
        this.id = anBill.id;
        this.regDate = anBill.regDate;
    }
    
    public void initDefOperInfo(Long anOperId, String anName) {
        this.operId = anOperId;
        this.operName = anName;
    }

    public void initModifyValue(String anAgreeNo, String anAgreeId) {
        if (BetterStringUtils.isNotBlank(anAgreeNo)) {
            this.agreeId = Long.parseLong(anAgreeId);
            this.agreeNo = anAgreeNo;
        }
        this.modiDate = BetterDateUtils.getNumDate();
    }

    public void initStatus(Long anBillId, String anStatus, String anFinanceFlag){
        this.id = anBillId;
        this.businStatus = anStatus;
        this.financeFlag  = anFinanceFlag;        
    }
    
    /**
     * 更新票据的基本信息
     * 
     * @param anBill
     */
    public void modifyDfaultValue(ScfAcceptBill anBill) {
        this.id = anBill.id;
        this.regDate = anBill.regDate;
        this.modiDate = BetterDateUtils.getNumDate();
    }

    public void fillAddDefaultValue() {
        this.id = SerialGenerator.getLongValue("ScfAcceptBill.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
        this.businStatus = "0";
        this.buyerNo = this.coreCustNo;
        this.confirmBalance = BigDecimal.ZERO;
        this.ratio = BigDecimal.ZERO;
        
        //如果实际支付人信息为null，则给出核心企业开发方信息
        if (BetterStringUtils.isBlank(this.realBuyer) || (this.realBuyer.trim().length() < 3)){
           this.realBuyer = this.buyer; 
        }
        
        this.financeFlag="0";
    }

    public void fillDefaultValue(){
        String tmpStatus = this.businStatus;
        fillAddDefaultValue();
        if ("0".equals(tmpStatus)){
            this.businStatus = "9";
        }
        else if ("1".equals(tmpStatus)){
            this.businStatus = "0";
        }
        else{
            this.businStatus = "9"; 
        }
    }

    /**
     * 汇票信息添加
     */
    public void initAddValue(CustOperatorInfo anOperInfo, ScfAcceptBill anAcceptBill) {
        this.id = SerialGenerator.getLongValue("ScfAcceptBill.id");
        this.businStatus = "0";
        this.financeFlag = "0";
        this.regDate = BetterDateUtils.getNumDate();
        if (anOperInfo != null){
            this.operId = anOperInfo.getId();
            this.operName = anOperInfo.getName();
            this.operOrg = anOperInfo.getOperOrg();
        }
        //默认自开库存
        this.billFrom = "0";
        //未审核
        this.aduit = "0";
        //默认前手为付款方
        this.preHand = anAcceptBill.getBuyer();
    }
    
    /**
     * 汇票信息变更初始化
     */
    public void initModifyValue(CustOperatorInfo anOperInfo) {
        this.modiDate = BetterDateUtils.getNumDate();
        if (anOperInfo != null){
            this.modiOperId = anOperInfo.getId();
            this.modiOperName = anOperInfo.getName();
        }
        this.modiTime = BetterDateUtils.getNumTime();
    }

   public void initTransferValue() {
        //背书来源id
        this.transferId = this.id;
        //生成新的流水号
        this.id = SerialGenerator.getLongValue("ScfAcceptBill.id");
        //先手为之前持票人
        this.preHand = this.holder;
        this.preHandNo = this.holderNo;
        //后手为空
        this.nextHand = null;
        this.nextHandNo = null;
        //来源改为转让
        this.billFrom = "1";
        //业务状态初始化
        this.businStatus = "0";
        this.financeFlag = "0";
    }
    
    /**
     * 检查票据是否过期
     * 
     * @return
     */
    public boolean hasInvalid() {

        return BetterDateUtils.getNumDate().compareTo(this.endDate) >= 0;
    }

    /**
     * 检查是否可以修改，在已融资或已过期的情况下，不能修改。
     * 
     * @return
     */
    public boolean permitModify() {

        return "0".equals(this.businStatus) || "1".equals(this.businStatus) || "X".equalsIgnoreCase(this.businStatus);
    }

    @Override
    public void setCustNo(Long anCustNo) {
        
        this.supplierNo = anCustNo;        
    }

    @Override
    public String getBtNo(){
        
        return null;
    }

    @Override
    public String getBankAccount() {
        
        return this.suppBankAccount;
    }

    @Override
    public Long getCustNo() {
        
        return this.supplierNo;
    }

    @Override
    public void modifytValue() {
        
        this.modiDate = BetterDateUtils.getNumDate(); 
    }

    @Override
    public String findBankAccountName() {
        
        return this.supplier;
    }
}