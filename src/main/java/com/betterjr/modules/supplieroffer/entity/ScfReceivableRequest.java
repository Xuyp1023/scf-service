package com.betterjr.modules.supplieroffer.entity;

import java.math.BigDecimal;

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
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.supplieroffer.data.ReceivableRequestConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_receivable_request")
public class ScfReceivableRequest implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -4458624164579492390L;

    /**
     * 申请编号
     */
    @Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请编号", comments = "申请编号")
    private String requestNo;
    
    /**
     * 融资编号
     */
    @Column(name = "C_EQUITYNO",  columnDefinition="VARCHAR" )
    @MetaData( value="融资编号", comments = "融资编号")
    private String equityNo;
    
    /**
     * 供应商编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="供应商编号", comments = "供应商编号")
    private Long custNo;
    
    /**
     * 供应商名称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="供应商名称", comments = "供应商名称")
    private String custName;
    
    /**
     * 收款银行
     */
    @Column(name = "c_cust_bankName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行", comments = "收款银行")
    private String custBankName;
    
    /**
     * 收款帐号
     */
    @Column(name = "c_cust_bankAccount", columnDefinition = "VARCHAR")
    @MetaData(value = "收款帐号", comments = "收款帐号")
    private String custBankAccount;
    
    /**
     * 收款开户名称
     */
    @Column(name = "c_cust_bankAccountName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款开户名称", comments = "收款开户名称")
    private String custBankAccountName;
    
    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;
    
    /**
     * 核心企业名称
     */
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业名称", comments = "核心企业名称")
    private String coreCustName;
    
    
    /**
     * 业务类型编号
     */
    @Column(name = "N_BUSINTYPE_NO",  columnDefinition="INTEGER" )
    @MetaData( value="业务类型编号", comments = "业务类型编号")
    private int businTypeNo;
    
    
    /**
     * 保理产品编号
     */
    @Column(name = "N_REQUEST_PRODUCT_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="保理产品Id", comments = "保理产品Id")
    private String requestProductCode;
    
    /**
     * 资产Id
     */
    @Column(name = "L_ASSET_ID",  columnDefinition="INTEGER" )
    @MetaData( value="资产Id", comments = "资产Id")
    private Long assetId;
    
    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORY_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="保理公司编号", comments = "保理公司编号")
    private Long factoryNo;
    
    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORY_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司名称", comments = "保理公司名称")
    private String factoryName;
    
    /**
     * 申请总金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请总金额", comments = "申请总金额")
    private BigDecimal balance;
    
    /**
     * 申请金额
     */
    @Column(name = "N_REQUEST_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请金额", comments = "申请金额")
    private BigDecimal requestBalance;
    
    /**
     * 核心企业提供的折扣率
     */
    @Column(name = "F_CUST_CORE_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="核心企业提供的折扣率", comments = "核心企业提供的折扣率")
    private BigDecimal custCoreRate;
    
    /**
     * 供应商折扣率
     */
    @Column(name = "F_DEPOSIT_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="供应商折扣率", comments = "供应商折扣率")
    private BigDecimal depositRate;
    
    /**
     * 申请提前付款金额
     */
    @Column(name = "F_REQUEST_PAY_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请提前付款金额", comments = "申请提前付款金额")
    private BigDecimal requestPayBalance;
    
    /**
     * 付款给平台的钱
     */
    @Column(name = "F_REQUEST_PAY_PLAT_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="付款给平台的钱", comments = "付款给平台的钱")
    private BigDecimal requestPayPlatBalance;
    
    /**
     * 平台提供利率
     */
    @Column(name = "F_CUST_OPAT_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="平台提供利率", comments = "平台提供利率")
    private BigDecimal custOpatRate;
    
    /**
     * 提前付款日期
     */
    @Column(name = "C_REQUEST_PAY_DATE",  columnDefinition="varchar" )
    @MetaData( value="提前付款日期", comments = "提前付款日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String requestPayDate;
    
    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;
    
    /**
     * 状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 4核心企业确认并签署合同 5资金方付款 6完结
     */
    @Column(name = "C_BUSINSTATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 4核心企业确认并签署合同 5资金方付款 6完结", comments = "状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 4核心企业确认并签署合同 5资金方付款 6完结")
    private String businStatus;
    
    /**
     * 所属机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="所属机构", comments = "所属机构")
    private String operOrg;
    
    
    /**
     * 当前流程所属公司
     */
    @Column(name = "L_OWN_COMPANY",  columnDefinition="INTEGER" )
    @MetaData( value="当前流程所属公司", comments = "当前流程所属公司")
    private Long ownCompany;
    
    /**
     * 申请日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="申请日期", comments = "申请日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    /**
     * 申请时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="申请时间", comments = "申请时间")
    private String regTime;
    
    /**
     * 到期时间
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="到期时间", comments = "到期时间")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String endDate;
    
    /**
     * 应收账款融资类型
     */
    @Column(name = "C_RECEIVABLE_REQUEST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="应收账款融资类型", comments = "应收账款融资类型")
    private String receivableRequestType;
    
    @Transient
    private ScfAsset asset=new ScfAsset();
    
    /**
     * 核心企业与供应商签订的合同id
     */
    @Column(name = "N_CORE_AGREEMENT_ID",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业与供应商签订的合同id", comments = "核心企业与供应商签订的合同id")
    private Long coreAgreementId;
    @Transient
    private ScfReceivableRequestAgreement coreAgreement=new ScfReceivableRequestAgreement();
    
    /**
     * 平台和供应商签订的合同id
     */
    @Column(name = "N_PLAT_AGREEMENT_ID",  columnDefinition="INTEGER" )
    @MetaData( value="平台和供应商签订的合同id", comments = "平台和供应商签订的合同id")
    private Long platAgreementId;
    @Transient
    private ScfReceivableRequestAgreement platAgreement=new ScfReceivableRequestAgreement();
    
    public Long getCoreAgreementId() {
        return this.coreAgreementId;
    }

    public void setCoreAgreementId(Long anCoreAgreementId) {
        this.coreAgreementId = anCoreAgreementId;
    }

    public ScfReceivableRequestAgreement getCoreAgreement() {
        return this.coreAgreement;
    }

    public String getReceivableRequestType() {
        return this.receivableRequestType;
    }

    public void setReceivableRequestType(String anReceivableRequestType) {
        this.receivableRequestType = anReceivableRequestType;
    }

    public Long getFactoryNo() {
        return this.factoryNo;
    }

    public BigDecimal getDepositRate() {
        return this.depositRate;
    }

    public void setDepositRate(BigDecimal anDepositRate) {
        this.depositRate = anDepositRate;
    }

    public void setFactoryNo(Long anFactoryNo) {
        this.factoryNo = anFactoryNo;
    }

    public String getFactoryName() {
        return this.factoryName;
    }

    public BigDecimal getRequestBalance() {
        return this.requestBalance;
    }

    public void setRequestBalance(BigDecimal anRequestBalance) {
        this.requestBalance = anRequestBalance;
    }

    public void setFactoryName(String anFactoryName) {
        this.factoryName = anFactoryName;
    }

    public void setCoreAgreement(ScfReceivableRequestAgreement anCoreAgreement) {
        this.coreAgreement = anCoreAgreement;
    }

    public Long getPlatAgreementId() {
        return this.platAgreementId;
    }

    public void setPlatAgreementId(Long anPlatAgreementId) {
        this.platAgreementId = anPlatAgreementId;
    }

    public ScfReceivableRequestAgreement getPlatAgreement() {
        return this.platAgreement;
    }

    public void setPlatAgreement(ScfReceivableRequestAgreement anPlatAgreement) {
        this.platAgreement = anPlatAgreement;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    public ScfAsset getAsset() {
        return this.asset;
    }

    public void setAsset(ScfAsset anAsset) {
        this.asset = anAsset;
    }

    public String getRequestNo() {
        return this.requestNo;
    }

    public void setRequestNo(String anRequestNo) {
        this.requestNo = anRequestNo;
    }

    public String getEquityNo() {
        return this.equityNo;
    }

    public void setEquityNo(String anEquityNo) {
        this.equityNo = anEquityNo;
    }

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getCustBankName() {
        return this.custBankName;
    }

    public void setCustBankName(String anCustBankName) {
        this.custBankName = anCustBankName;
    }

    public String getCustBankAccount() {
        return this.custBankAccount;
    }

    public void setCustBankAccount(String anCustBankAccount) {
        this.custBankAccount = anCustBankAccount;
    }

    public String getCustBankAccountName() {
        return this.custBankAccountName;
    }

    public void setCustBankAccountName(String anCustBankAccountName) {
        this.custBankAccountName = anCustBankAccountName;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public int getBusinTypeNo() {
        return this.businTypeNo;
    }

    public void setBusinTypeNo(int anBusinTypeNo) {
        this.businTypeNo = anBusinTypeNo;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public void setAssetId(Long anAssetId) {
        this.assetId = anAssetId;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public BigDecimal getCustCoreRate() {
        return this.custCoreRate;
    }

    public void setCustCoreRate(BigDecimal anCustCoreRate) {
        this.custCoreRate = anCustCoreRate;
    }

    public BigDecimal getRequestPayBalance() {
        return this.requestPayBalance;
    }

    public void setRequestPayBalance(BigDecimal anRequestPayBalance) {
        this.requestPayBalance = anRequestPayBalance;
    }

    public BigDecimal getCustOpatRate() {
        return this.custOpatRate;
    }

    public String getRequestProductCode() {
        return this.requestProductCode;
    }

    public void setRequestProductCode(String anRequestProductCode) {
        this.requestProductCode = anRequestProductCode;
    }

    public void setCustOpatRate(BigDecimal anCustOpatRate) {
        this.custOpatRate = anCustOpatRate;
    }

    public String getRequestPayDate() {
        return this.requestPayDate;
    }

    public void setRequestPayDate(String anRequestPayDate) {
        this.requestPayDate = anRequestPayDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public Long getOwnCompany() {
        return this.ownCompany;
    }

    public void setOwnCompany(Long anOwnCompany) {
        this.ownCompany = anOwnCompany;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

   

    public BigDecimal getRequestPayPlatBalance() {
        return this.requestPayPlatBalance;
    }

    public void setRequestPayPlatBalance(BigDecimal anRequestPayPlatBalance) {
        this.requestPayPlatBalance = anRequestPayPlatBalance;
    }


    @Override
    public String toString() {
        return "ScfReceivableRequest [requestNo=" + this.requestNo + ", equityNo=" + this.equityNo + ", custNo=" + this.custNo + ", custName="
                + this.custName + ", custBankName=" + this.custBankName + ", custBankAccount=" + this.custBankAccount + ", custBankAccountName="
                + this.custBankAccountName + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", businTypeNo="
                + this.businTypeNo + ", assetId=" + this.assetId + ", factoryNo=" + this.factoryNo + ", factoryName=" + this.factoryName
                + ", balance=" + this.balance + ", requestBalance=" + this.requestBalance + ", custCoreRate=" + this.custCoreRate + ", depositRate="
                + this.depositRate + ", requestPayBalance=" + this.requestPayBalance + ", requestPayPlatBalance=" + this.requestPayPlatBalance
                + ", custOpatRate=" + this.custOpatRate + ", requestPayDate=" + this.requestPayDate + ", description=" + this.description
                + ", businStatus=" + this.businStatus + ", operOrg=" + this.operOrg + ", ownCompany=" + this.ownCompany + ", regDate=" + this.regDate
                + ", regTime=" + this.regTime + ", endDate=" + this.endDate + ", receivableRequestType=" + this.receivableRequestType + ", asset="
                + this.asset + ", coreAgreementId=" + this.coreAgreementId + ", coreAgreement=" + this.coreAgreement + ", platAgreementId="
                + this.platAgreementId + ", platAgreement=" + this.platAgreement + "]";
    }

    public void saveAddValue() {
        
        this.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE);
        this.setEquityNo(SequenceFactory.generate("PLAT_SCFRECEIVABLErEQUESTLOGID", "RZ#{Date:yy}#{Seq:14}"));
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRequestNo(SequenceFactory.generate("PLAT_SCFRECEIVABLErEQUESTID", "SQ#{Date:yy}#{Seq:14}"));
        
    }
    
    
    
}
