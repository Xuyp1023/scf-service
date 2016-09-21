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
import com.betterjr.common.utils.UserUtils;
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
public class ScfAcceptBill implements BetterjrEntity {
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
     * 上传的批次号
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号")
    private Long batchNo;

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
     * 核心企业
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业", comments = "核心企业")
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

    private static final long serialVersionUID = -8124643903288982684L;

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
        this.invoiceDate = invoiceDate;
    }
    
    public String getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(String drawerId) {
        this.drawerId = drawerId == null ? null : drawerId.trim();
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

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
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
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
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
        return operId;
    }

    public void setOperId(Long operId) {
        this.operId = operId;
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

    public Long getAgreeId() {
        return agreeId;
    }

    public void setAgreeId(Long agreeId) {
        this.agreeId = agreeId;
    }

    public BigDecimal getConfirmBalance() {
        return confirmBalance;
    }

    public void setConfirmBalance(BigDecimal confirmBalance) {
        this.confirmBalance = confirmBalance;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

/*    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }*/


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

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime;
    }

    public String getInvoiceCorp() {
        return this.invoiceCorp;
    }

    public void setInvoiceCorp(String anInvoiceCorp) {
        this.invoiceCorp = anInvoiceCorp;
    }

    public String getHolder() {
        return this.holder;
    }
    
    public Long getHolderNo() {
        return holderNo;
    }

    public void setHolderNo(Long holderNo) {
        this.holderNo = holderNo;
    }

    public void setHolder(String anHolder) {
        this.holder = anHolder;
    }
    
    public String getHolderBankAccount() {
        return holderBankAccount;
    }

    public void setHolderBankAccount(String holderBankAccount) {
        this.holderBankAccount = holderBankAccount == null ? null : holderBankAccount.trim();
    }

    public String getAcceptor() {
        return this.acceptor;
    }

    public void setAcceptor(String anAcceptor) {
        this.acceptor = anAcceptor;
    }
    
    public String getAcceptorBankAccount() {
        return acceptorBankAccount;
    }

    public void setAcceptorBankAccount(String acceptorBankAccount) {
        this.acceptorBankAccount = acceptorBankAccount == null ? null : acceptorBankAccount.trim();
    }

    public String getBillFrom() {
        return this.billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
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

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
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
        sb.append(", agreeId=").append(agreeId);
        sb.append(", confirmBalance=").append(confirmBalance);
        sb.append(", loanBalance=").append(loanBalance);
        sb.append(", ratio=").append(ratio);
        sb.append(", description=").append(description);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiTime=").append(modiTime);
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
            && (this.getAgreeId() == null ? other.getAgreeId() == null : this.getAgreeId().equals(other.getAgreeId()))
            && (this.getConfirmBalance() == null ? other.getConfirmBalance() == null : this.getConfirmBalance().equals(other.getConfirmBalance()))
            && (this.getLoanBalance() == null ? other.getLoanBalance() == null : this.getLoanBalance().equals(other.getLoanBalance()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()));
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
        result = prime * result + ((getAgreeId() == null) ? 0 : getAgreeId().hashCode());
        result = prime * result + ((getConfirmBalance() == null) ? 0 : getConfirmBalance().hashCode());
        result = prime * result + ((getLoanBalance() == null) ? 0 : getLoanBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        return result;
    }
    
    /**
     * 汇票信息添加
     */
    public void initAddValue(ScfAcceptBill anAcceptBill) {
        this.id = SerialGenerator.getLongValue("ScfAcceptBill.id");
        this.businStatus = "0";
        this.financeFlag = "0";
        this.regDate = BetterDateUtils.getNumDate();
        this.operId = UserUtils.getOperatorInfo().getId();
        this.operName = UserUtils.getOperatorInfo().getName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        
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
    public void initModifyValue(ScfAcceptBill anModiAcceptBill) {
        this.buyer = anModiAcceptBill.getBuyer();
        this.buyerBankAccount = anModiAcceptBill.getBuyerBankAccount();
        this.buyerBankName = anModiAcceptBill.getBuyerBankName();
        this.buyerNo = anModiAcceptBill.getBuyerNo();
        this.supplier = anModiAcceptBill.getSupplier();
        this.suppBankAccount = anModiAcceptBill.getSuppBankAccount();
        this.suppBankName = anModiAcceptBill.getSuppBankName();
        this.supplierNo = anModiAcceptBill.getSupplierNo();
        this.billType = anModiAcceptBill.getBillType();
        this.billMode = anModiAcceptBill.getBillMode();
        this.billNo = anModiAcceptBill.getBillNo();
        this.balance = anModiAcceptBill.getBalance();
        this.invoiceDate = anModiAcceptBill.getInvoiceDate();
        this.endDate = anModiAcceptBill.getEndDate();
        
        this.acceptor =  anModiAcceptBill.getAcceptor();
        this.holder = anModiAcceptBill.getHolder();
        this.invoiceCorp = anModiAcceptBill.getInvoiceCorp();
        
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getOperatorInfo().getName();
        this.modiTime = BetterDateUtils.getNumTime();
    }

    public void initTransferValue() {
        this.id = SerialGenerator.getLongValue("ScfAcceptBill.id");
        //先手为之前持票人
        this.preHand = this.holder;
        //后手为空
        this.nextHand = null;
        //来源改为转让
        this.billFrom = "1";
        //业务状态初始化
        this.businStatus = "0";
        this.financeFlag = "0";
    }
}