package com.betterjr.modules.acceptbill.entity;

import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.modules.version.entity.BaseVersionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ACCEPT_BILL_V3")
public class ScfAcceptBillDO extends BaseVersionEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -1955186203837626292L;
    
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
     * 持票人 (债权人)供应商
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
     * 承兑人 (债券人)核心企业
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
     * 票据来源，0库存票据，1背书转让
     */
    @Column(name = "C_BILLFROM",  columnDefinition="VARCHAR" )
    @MetaData( value="票据来源", comments = "票据来源，0库存票据，1背书转让")
    private String billFrom;

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
     * 买方客户名称
     */
    @Column(name = "C_BUYER_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "买方客户名称", comments = "买方客户名称")
    private String buyerName;

    /**
     * 卖方客户号
     */
    @Column(name = "L_SUPPLIER_NO", columnDefinition = "INTEGER")
    @MetaData(value = "卖方客户号", comments = "卖方客户号")
    private Long supplierNo;

    /*@Transient
    private String supplierName;*/
    /**
     * 卖方客户名称
     */
    @Column(name = "C_SUPPLIER_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "卖方客户名称", comments = "卖方客户名称")
    private String supplierName;
    
    

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
     * 合同ID号
     */
    @Column(name = "L_AGREEID", columnDefinition = "INTEGER")
    @MetaData(value = "合同ID号", comments = "合同ID号")
    private Long agreeId;

    /**
     * 备注
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
     * 背书转让来源票据流水号
     */
    @Column(name = "L_TRANSFERID",  columnDefinition="INTEGER" )
    @MetaData( value="背书转让来源票据流水号", comments = "背书转让来源票据流水号")
    private Long transferId;


    @JsonIgnore
    @Column(name = "C_CORE_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "数据所属核心企业")
    private String coreOperOrg;

    /**
     * 操作员所在企业名称
     */
    @Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在企业名称", comments = "操作员所在企业名称")
    private String coreCustName;

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String anBillNo) {
        this.billNo = anBillNo;
    }

    public String getBillType() {
        return this.billType;
    }

    public void setBillType(String anBillType) {
        this.billType = anBillType;
    }

    public String getBillMode() {
        return this.billMode;
    }

    public void setBillMode(String anBillMode) {
        this.billMode = anBillMode;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(String anInvoiceDate) {
        this.invoiceDate = anInvoiceDate;
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

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    public String getCashDate() {
        return this.cashDate;
    }

    public void setCashDate(String anCashDate) {
        this.cashDate = anCashDate;
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

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String anSupplier) {
        this.supplier = anSupplier;
    }

    public String getSuppBankAccount() {
        return this.suppBankAccount;
    }

    public void setSuppBankAccount(String anSuppBankAccount) {
        this.suppBankAccount = anSuppBankAccount;
    }

    public String getSuppBankName() {
        return this.suppBankName;
    }

    public void setSuppBankName(String anSuppBankName) {
        this.suppBankName = anSuppBankName;
    }

    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String anBuyer) {
        this.buyer = anBuyer;
    }

    public String getRealBuyer() {
        return this.realBuyer;
    }

    public void setRealBuyer(String anRealBuyer) {
        this.realBuyer = anRealBuyer;
    }

    public String getBuyerBankAccount() {
        return this.buyerBankAccount;
    }

    public void setBuyerBankAccount(String anBuyerBankAccount) {
        this.buyerBankAccount = anBuyerBankAccount;
    }

    public String getBuyerBankName() {
        return this.buyerBankName;
    }

    public void setBuyerBankName(String anBuyerBankName) {
        this.buyerBankName = anBuyerBankName;
    }

    public String getBillFrom() {
        return this.billFrom;
    }

    public void setBillFrom(String anBillFrom) {
        this.billFrom = anBillFrom;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getBtBillNo() {
        return this.btBillNo;
    }

    public void setBtBillNo(String anBtBillNo) {
        this.btBillNo = anBtBillNo;
    }

    public Long getBuyerNo() {
        return this.buyerNo;
    }

    public void setBuyerNo(Long anBuyerNo) {
        this.buyerNo = anBuyerNo;
    }

    public String getBuyerName() {
        return this.buyerName;
    }

    public void setBuyerName(String anBuyerName) {
        this.buyerName = anBuyerName;
    }

    public Long getSupplierNo() {
        return this.supplierNo;
    }

    public void setSupplierNo(Long anSupplierNo) {
        this.supplierNo = anSupplierNo;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(String anSupplierName) {
        this.supplierName = anSupplierName;
    }

    public String getAgreeNo() {
        return this.agreeNo;
    }

    public void setAgreeNo(String anAgreeNo) {
        this.agreeNo = anAgreeNo;
    }

    public Long getOperId() {
        return this.operId;
    }

    public void setOperId(Long anOperId) {
        this.operId = anOperId;
    }

    public String getOperName() {
        return this.operName;
    }

    public void setOperName(String anOperName) {
        this.operName = anOperName;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public Long getAgreeId() {
        return this.agreeId;
    }

    public void setAgreeId(Long anAgreeId) {
        this.agreeId = anAgreeId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
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

    public Long getTransferId() {
        return this.transferId;
    }

    public void setTransferId(Long anTransferId) {
        this.transferId = anTransferId;
    }

    public String getCoreOperOrg() {
        return this.coreOperOrg;
    }

    public void setCoreOperOrg(String anCoreOperOrg) {
        this.coreOperOrg = anCoreOperOrg;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    @Override
    public String toString() {
        return "ScfAcceptBillDO [billNo=" + this.billNo + ", billType=" + this.billType + ", billMode=" + this.billMode + ", balance=" + this.balance
                + ", invoiceDate=" + this.invoiceDate + ", invoiceCorp=" + this.invoiceCorp + ", drawerId=" + this.drawerId + ", endDate="
                + this.endDate + ", cashDate=" + this.cashDate + ", holder=" + this.holder + ", holderNo=" + this.holderNo + ", holderBankAccount="
                + this.holderBankAccount + ", acceptor=" + this.acceptor + ", acceptorBankAccount=" + this.acceptorBankAccount + ", supplier="
                + this.supplier + ", suppBankAccount=" + this.suppBankAccount + ", suppBankName=" + this.suppBankName + ", buyer=" + this.buyer
                + ", realBuyer=" + this.realBuyer + ", buyerBankAccount=" + this.buyerBankAccount + ", buyerBankName=" + this.buyerBankName
                + ", billFrom=" + this.billFrom + ", regDate=" + this.regDate + ", modiDate=" + this.modiDate + ", btBillNo=" + this.btBillNo
                + ", buyerNo=" + this.buyerNo + ", buyerName=" + this.buyerName + ", supplierNo=" + this.supplierNo + ", supplierName="
                + this.supplierName + ", agreeNo=" + this.agreeNo + ", operId=" + this.operId + ", operName=" + this.operName + ", operOrg="
                + this.operOrg + ", batchNo=" + this.batchNo + ", agreeId=" + this.agreeId + ", description=" + this.description + ", coreCustNo="
                + this.coreCustNo + ", modiOperId=" + this.modiOperId + ", modiOperName=" + this.modiOperName + ", modiTime=" + this.modiTime
                + ", dataSource=" + this.dataSource + ", transferId=" + this.transferId + ", coreOperOrg=" + this.coreOperOrg + ", coreCustName="
                + this.coreCustName + ", getRefNo()=" + this.getRefNo() + ", getVersion()=" + this.getVersion() + ", getIsLatest()="
                + this.getIsLatest() + ", getBusinStatus()=" + this.getBusinStatus() + ", getLockedStatus()=" + this.getLockedStatus()
                + ", getDocStatus()=" + this.getDocStatus() + ", getAuditOperId()=" + this.getAuditOperId() + ", getAuditOperName()="
                + this.getAuditOperName() + ", getAuditData()=" + this.getAuditData() + ", getAuditTime()=" + this.getAuditTime() + ", getId()="
                + this.getId() + "]";
    }

    public ScfAcceptBillDO() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.acceptor == null) ? 0 : this.acceptor.hashCode());
        result = prime * result + ((this.acceptorBankAccount == null) ? 0 : this.acceptorBankAccount.hashCode());
        result = prime * result + ((this.agreeId == null) ? 0 : this.agreeId.hashCode());
        result = prime * result + ((this.agreeNo == null) ? 0 : this.agreeNo.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.billFrom == null) ? 0 : this.billFrom.hashCode());
        result = prime * result + ((this.billMode == null) ? 0 : this.billMode.hashCode());
        result = prime * result + ((this.billNo == null) ? 0 : this.billNo.hashCode());
        result = prime * result + ((this.billType == null) ? 0 : this.billType.hashCode());
        result = prime * result + ((this.btBillNo == null) ? 0 : this.btBillNo.hashCode());
        result = prime * result + ((this.buyer == null) ? 0 : this.buyer.hashCode());
        result = prime * result + ((this.buyerBankAccount == null) ? 0 : this.buyerBankAccount.hashCode());
        result = prime * result + ((this.buyerBankName == null) ? 0 : this.buyerBankName.hashCode());
        result = prime * result + ((this.buyerName == null) ? 0 : this.buyerName.hashCode());
        result = prime * result + ((this.buyerNo == null) ? 0 : this.buyerNo.hashCode());
        result = prime * result + ((this.cashDate == null) ? 0 : this.cashDate.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.coreOperOrg == null) ? 0 : this.coreOperOrg.hashCode());
        result = prime * result + ((this.dataSource == null) ? 0 : this.dataSource.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.drawerId == null) ? 0 : this.drawerId.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.holder == null) ? 0 : this.holder.hashCode());
        result = prime * result + ((this.holderBankAccount == null) ? 0 : this.holderBankAccount.hashCode());
        result = prime * result + ((this.holderNo == null) ? 0 : this.holderNo.hashCode());
        result = prime * result + ((this.invoiceCorp == null) ? 0 : this.invoiceCorp.hashCode());
        result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operId == null) ? 0 : this.operId.hashCode());
        result = prime * result + ((this.operName == null) ? 0 : this.operName.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.realBuyer == null) ? 0 : this.realBuyer.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.suppBankAccount == null) ? 0 : this.suppBankAccount.hashCode());
        result = prime * result + ((this.suppBankName == null) ? 0 : this.suppBankName.hashCode());
        result = prime * result + ((this.supplier == null) ? 0 : this.supplier.hashCode());
        result = prime * result + ((this.supplierName == null) ? 0 : this.supplierName.hashCode());
        result = prime * result + ((this.supplierNo == null) ? 0 : this.supplierNo.hashCode());
        result = prime * result + ((this.transferId == null) ? 0 : this.transferId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAcceptBillDO other = (ScfAcceptBillDO) obj;
        if (this.acceptor == null) {
            if (other.acceptor != null) return false;
        }
        else if (!this.acceptor.equals(other.acceptor)) return false;
        if (this.acceptorBankAccount == null) {
            if (other.acceptorBankAccount != null) return false;
        }
        else if (!this.acceptorBankAccount.equals(other.acceptorBankAccount)) return false;
        if (this.agreeId == null) {
            if (other.agreeId != null) return false;
        }
        else if (!this.agreeId.equals(other.agreeId)) return false;
        if (this.agreeNo == null) {
            if (other.agreeNo != null) return false;
        }
        else if (!this.agreeNo.equals(other.agreeNo)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.billFrom == null) {
            if (other.billFrom != null) return false;
        }
        else if (!this.billFrom.equals(other.billFrom)) return false;
        if (this.billMode == null) {
            if (other.billMode != null) return false;
        }
        else if (!this.billMode.equals(other.billMode)) return false;
        if (this.billNo == null) {
            if (other.billNo != null) return false;
        }
        else if (!this.billNo.equals(other.billNo)) return false;
        if (this.billType == null) {
            if (other.billType != null) return false;
        }
        else if (!this.billType.equals(other.billType)) return false;
        if (this.btBillNo == null) {
            if (other.btBillNo != null) return false;
        }
        else if (!this.btBillNo.equals(other.btBillNo)) return false;
        if (this.buyer == null) {
            if (other.buyer != null) return false;
        }
        else if (!this.buyer.equals(other.buyer)) return false;
        if (this.buyerBankAccount == null) {
            if (other.buyerBankAccount != null) return false;
        }
        else if (!this.buyerBankAccount.equals(other.buyerBankAccount)) return false;
        if (this.buyerBankName == null) {
            if (other.buyerBankName != null) return false;
        }
        else if (!this.buyerBankName.equals(other.buyerBankName)) return false;
        if (this.buyerName == null) {
            if (other.buyerName != null) return false;
        }
        else if (!this.buyerName.equals(other.buyerName)) return false;
        if (this.buyerNo == null) {
            if (other.buyerNo != null) return false;
        }
        else if (!this.buyerNo.equals(other.buyerNo)) return false;
        if (this.cashDate == null) {
            if (other.cashDate != null) return false;
        }
        else if (!this.cashDate.equals(other.cashDate)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.coreOperOrg == null) {
            if (other.coreOperOrg != null) return false;
        }
        else if (!this.coreOperOrg.equals(other.coreOperOrg)) return false;
        if (this.dataSource == null) {
            if (other.dataSource != null) return false;
        }
        else if (!this.dataSource.equals(other.dataSource)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.drawerId == null) {
            if (other.drawerId != null) return false;
        }
        else if (!this.drawerId.equals(other.drawerId)) return false;
        if (this.endDate == null) {
            if (other.endDate != null) return false;
        }
        else if (!this.endDate.equals(other.endDate)) return false;
        if (this.holder == null) {
            if (other.holder != null) return false;
        }
        else if (!this.holder.equals(other.holder)) return false;
        if (this.holderBankAccount == null) {
            if (other.holderBankAccount != null) return false;
        }
        else if (!this.holderBankAccount.equals(other.holderBankAccount)) return false;
        if (this.holderNo == null) {
            if (other.holderNo != null) return false;
        }
        else if (!this.holderNo.equals(other.holderNo)) return false;
        if (this.invoiceCorp == null) {
            if (other.invoiceCorp != null) return false;
        }
        else if (!this.invoiceCorp.equals(other.invoiceCorp)) return false;
        if (this.invoiceDate == null) {
            if (other.invoiceDate != null) return false;
        }
        else if (!this.invoiceDate.equals(other.invoiceDate)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        }
        else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        }
        else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        }
        else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        }
        else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operId == null) {
            if (other.operId != null) return false;
        }
        else if (!this.operId.equals(other.operId)) return false;
        if (this.operName == null) {
            if (other.operName != null) return false;
        }
        else if (!this.operName.equals(other.operName)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.realBuyer == null) {
            if (other.realBuyer != null) return false;
        }
        else if (!this.realBuyer.equals(other.realBuyer)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.suppBankAccount == null) {
            if (other.suppBankAccount != null) return false;
        }
        else if (!this.suppBankAccount.equals(other.suppBankAccount)) return false;
        if (this.suppBankName == null) {
            if (other.suppBankName != null) return false;
        }
        else if (!this.suppBankName.equals(other.suppBankName)) return false;
        if (this.supplier == null) {
            if (other.supplier != null) return false;
        }
        else if (!this.supplier.equals(other.supplier)) return false;
        if (this.supplierName == null) {
            if (other.supplierName != null) return false;
        }
        else if (!this.supplierName.equals(other.supplierName)) return false;
        if (this.supplierNo == null) {
            if (other.supplierNo != null) return false;
        }
        else if (!this.supplierNo.equals(other.supplierNo)) return false;
        if (this.transferId == null) {
            if (other.transferId != null) return false;
        }
        else if (!this.transferId.equals(other.transferId)) return false;
        return true;
    }

    
}
