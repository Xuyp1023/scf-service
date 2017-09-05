package com.betterjr.modules.supplieroffer.entity;

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
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.supplieroffer.data.AgreementConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_receivable_request_agreement")
public class ScfReceivableRequestAgreement implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 4652996115478216508L;
    
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;
    
    /**
     * 合同凭证编号
     */
    @Column(name = "C_Ref_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="合同凭证编号", comments = "合同凭证编号")
    private String refNo;
    
    /**
     * 合同编号
     */
    @Column(name = "C_AGREEMENT_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同编号", comments = "合同编号")
    private String agreementCode;
    
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
     * 合同名称
     */
    @Column(name = "C_AGREEMENT_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="合同名称", comments = "合同名称")
    private String agreementName;
    
    
    /**
     * 状态 0 未签署  1供应商已签 2采购商已签 3 作废
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态 0 未签署  1供应商已签 2采购商已签 3 作废", comments = "状态 0 未签署  1供应商已签 2采购商已签 3 作废")
    private String businStatus;
    
    /**
     * 供应商签署日期
     */
    @Column(name = "D_CUST_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="申请日期", comments = "申请日期")
    private String custDate;
    
    /**
     * 供应商签署时间
     */
    @Column(name = "T_CUST_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="供应商签署时间", comments = "供应商签署时间")
    private String custTime;
    
    /**
     * 供应商签署人名称
     */
    @Column(name = "L_CUST_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="供应商签署人名称", comments = "供应商签署人名称")
    private Long custOperId ;

    /**
     * 供应商签署人名称
     */
    @Column(name = "C_CUST_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="供应商签署人名称", comments = "供应商签署人名称")
    private String custOperName;
    
    /**
     * 核心企业签署日期
     */
    @Column(name = "D_CORE_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业申请日期", comments = "核心企业申请日期")
    private String coreDate;
    
    /**
     * 核心企业签署时间
     */
    @Column(name = "T_CORE_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业签署时间", comments = "核心企业签署时间")
    private String coreTime;
    
    /**
     * 核心企业签署人名称
     */
    @Column(name = "L_CORE_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业签署人名称", comments = "核心企业签署人名称")
    private Long coreOperId ;

    /**
     * 核心企业签署人名称
     */
    @Column(name = "C_CORE_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业签署人名称", comments = "核心企业签署人名称")
    private String coreOperName;
    
    /**
     * 金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="金额", comments = "金额")
    private BigDecimal balance;
    
    /**
     * 合同模版存放地址
     */
    @Column(name = "L_AGREEMENT_TEMPLATE_ID",  columnDefinition="INTEGER" )
    @MetaData( value="合同模版存放地址", comments = "合同模版存放地址")
    private Long agreementTemplateId ;
    
    /**
     * 融资申请编号
     */
    @Column(name = "L_RECEIVABLEREQUEST_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="融资申请编号", comments = "融资申请编号")
    private String receivableRequestNo;
    
    /**
     * 合同类型  0 企业金服平台  1 核心企业签订
     */
    @Column(name = "C_AGREEMENT_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同类型  0 企业金服平台  1 核心企业签订", comments = "合同类型  0 企业金服平台  1 核心企业签订")
    private String agreementType;
    
    
    /**
     * 合同最后签署日期
     */
    @Column(name = "D_SIGN_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="合同最后签署日期", comments = "合同最后签署日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String signDate;
    
    /**
     * 各方签署合同的文件上传到服务器存放id
     */
    @Column(name = "L_AGREEMENT_CONFIRM_FILE_ID",  columnDefinition="INTEGER" )
    @MetaData( value="各方签署合同的文件上传到服务器存放id", comments = "各方签署合同的文件上传到服务器存放id")
    private Long agreementConfirmFileId ;
    
    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORYNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="保理公司名称", comments = "保理公司名称")
    private String factoryName;
    
    /**
     * 保理公司Id
     */
    @Column(name = "L_FACTORYNO",  columnDefinition="INTEGER" )
    @MetaData( value="保理公司Id", comments = "保理公司Id")
    private Long factoryNo;
    
    public String getFactoryName() {
        return this.factoryName;
    }

    public void setFactoryName(String anFactoryName) {
        this.factoryName = anFactoryName;
    }

    public Long getFactoryNo() {
        return this.factoryNo;
    }

    public void setFactoryNo(Long anFactoryNo) {
        this.factoryNo = anFactoryNo;
    }

    public String getSignDate() {
        return this.signDate;
    }

    public void setSignDate(String anSignDate) {
        this.signDate = anSignDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String anRefNo) {
        this.refNo = anRefNo;
    }

    public String getAgreementCode() {
        return this.agreementCode;
    }

    public void setAgreementCode(String anAgreementCode) {
        this.agreementCode = anAgreementCode;
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

    public String getAgreementName() {
        return this.agreementName;
    }

    public void setAgreementName(String anAgreementName) {
        this.agreementName = anAgreementName;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getCustDate() {
        return this.custDate;
    }

    public void setCustDate(String anCustDate) {
        this.custDate = anCustDate;
    }

    public String getCustTime() {
        return this.custTime;
    }

    public void setCustTime(String anCustTime) {
        this.custTime = anCustTime;
    }

    public Long getCustOperId() {
        return this.custOperId;
    }

    public void setCustOperId(Long anCustOperId) {
        this.custOperId = anCustOperId;
    }

    public String getCustOperName() {
        return this.custOperName;
    }

    public void setCustOperName(String anCustOperName) {
        this.custOperName = anCustOperName;
    }

    public String getCoreDate() {
        return this.coreDate;
    }

    public void setCoreDate(String anCoreDate) {
        this.coreDate = anCoreDate;
    }

    public String getCoreTime() {
        return this.coreTime;
    }

    public void setCoreTime(String anCoreTime) {
        this.coreTime = anCoreTime;
    }

    public Long getCoreOperId() {
        return this.coreOperId;
    }

    public void setCoreOperId(Long anCoreOperId) {
        this.coreOperId = anCoreOperId;
    }

    public String getCoreOperName() {
        return this.coreOperName;
    }

    public void setCoreOperName(String anCoreOperName) {
        this.coreOperName = anCoreOperName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getAgreementTemplateId() {
        return this.agreementTemplateId;
    }

    public void setAgreementTemplateId(Long anAgreementTemplateId) {
        this.agreementTemplateId = anAgreementTemplateId;
    }

    public String getReceivableRequestNo() {
        return this.receivableRequestNo;
    }

    public void setReceivableRequestNo(String anReceivableRequestNo) {
        this.receivableRequestNo = anReceivableRequestNo;
    }

    public Long getAgreementConfirmFileId() {
        return this.agreementConfirmFileId;
    }

    public void setAgreementConfirmFileId(Long anAgreementConfirmFileId) {
        this.agreementConfirmFileId = anAgreementConfirmFileId;
    }

    public String getAgreementType() {
        return this.agreementType;
    }

    public void setAgreementType(String anAgreementType) {
        this.agreementType = anAgreementType;
    }

    @Override
    public String toString() {
        return "ScfReceivableRequestAgreement [id=" + this.id + ", refNo=" + this.refNo + ", agreementCode=" + this.agreementCode + ", custNo="
                + this.custNo + ", custName=" + this.custName + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName
                + ", agreementName=" + this.agreementName + ", businStatus=" + this.businStatus + ", custDate=" + this.custDate + ", custTime="
                + this.custTime + ", custOperId=" + this.custOperId + ", custOperName=" + this.custOperName + ", coreDate=" + this.coreDate
                + ", coreTime=" + this.coreTime + ", coreOperId=" + this.coreOperId + ", coreOperName=" + this.coreOperName + ", balance="
                + this.balance + ", agreementTemplateId=" + this.agreementTemplateId + ", receivableRequestNo=" + this.receivableRequestNo
                + ", agreementConfirmFileId=" + this.agreementConfirmFileId + "]";
    }

    public void saveAddValue(CustOperatorInfo anOperatorInfo) {
        
        this.setAgreementCode(SequenceFactory.generate("PLAT_ReceivableRequestAgreementCode", "TZBL-ZR#{Date:yy}#{Seq:8}"));
        
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_NOEFFECTIVE);
        this.setCustDate(BetterDateUtils.getNumDate());
        this.setCustOperId(anOperatorInfo.getId());
        this.setCustOperName(anOperatorInfo.getName());
        this.setCustTime(BetterDateUtils.getNumTime());
        this.setRefNo("");
        this.setId(SerialGenerator.getLongValue("ScfReceivableRequestAgreement.id"));
        //this.setSignDate(BetterDateUtils.getNumDate());
        
    }

    public void saveSignValue(CustOperatorInfo anOperatorInfo) {
       
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_SUPPLIER_SIGNED);
        this.setCustDate(BetterDateUtils.getNumDate());
        this.setCustTime(BetterDateUtils.getNumTime());
        this.setCustOperId(anOperatorInfo.getId());
        this.setCustOperName(anOperatorInfo.getName());
        this.setSignDate(BetterDateUtils.getNumDate());
        
    }
    
    public void saveCoreSignValue(CustOperatorInfo anOperatorInfo) {
        
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_CORE_SIGNED);
        this.setCoreDate(BetterDateUtils.getNumDate());
        this.setCoreTime(BetterDateUtils.getNumTime());
        this.setCoreOperId(anOperatorInfo.getId());
        this.setCoreOperName(anOperatorInfo.getName());
        this.setSignDate(BetterDateUtils.getNumDate());
        
    }
    public void saveFactorySignValue(CustOperatorInfo anOperatorInfo) {
        
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_CORE_SIGNED);
        this.setCoreDate(BetterDateUtils.getNumDate());
        this.setCoreTime(BetterDateUtils.getNumTime());
        this.setCoreOperId(anOperatorInfo.getId());
        this.setCoreOperName(anOperatorInfo.getName());
        this.setSignDate(BetterDateUtils.getNumDate());
        
    }
    
    

}
