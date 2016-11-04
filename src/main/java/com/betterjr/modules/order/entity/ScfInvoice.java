package com.betterjr.modules.order.entity;

import java.math.BigDecimal;
import java.util.List;

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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_INVOICE")
public class ScfInvoice implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;

    /**
     * 发票代码
     */
    @Column(name = "C_INVOICE_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="发票代码", comments = "发票代码")
    private String invoiceCode;

    /**
     * 发票号码
     */
    @Column(name = "C_INVOICE_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="发票号码", comments = "发票号码")
    private String invoiceNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 新增操作员编码
     */
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="新增操作员编码", comments = "新增操作员编码")
    @JsonIgnore
    private Long regOperId ;

    /**
     * 新增操作员名字
     */
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="新增操作员名字", comments = "新增操作员名字")
    private String regOperName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="编辑操作员编码", comments = "编辑操作员编码")
    @JsonIgnore
    private Long modiOperId ;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑操作员名字", comments = "编辑操作员名字")
    private String modiOperName;

    /**
     * 编辑日期
     */
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑日期", comments = "编辑日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 编辑时间
     */
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑时间", comments = "编辑时间")
    private String modiTime;

    /**
     * 纳税人识别号
     */
    @Column(name = "C_TAXPAYERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="纳税人识别号", comments = "纳税人识别号")
    private String taxpayerNo;

    /**
     * 行业类别
     */
    @Column(name = "C_CORPVOCATE",  columnDefinition="VARCHAR" )
    @MetaData( value="行业类别", comments = "行业类别")
    private String corpVocate;

    /**
     * 开票日期
     */
    @Column(name = "D_INVOICE_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="开票日期", comments = "开票日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String invoiceDate;

    /**
     * 发票金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="发票金额", comments = "发票金额")
    private BigDecimal balance;

    /**
     * 开票人
     */
    @Column(name = "C_DRAWER",  columnDefinition="VARCHAR" )
    @MetaData( value="开票人", comments = "开票人")
    private String drawer;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="发票状态", comments = "发票状态，0失效，1正常，2过期，3冻结")
    private String businStatus;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;
    
    /**
     * 发票项目详情
     */
    @Transient
    private List<ScfInvoiceItem> invoiceItemList;
    
    /**
     * 客户名称
     */
    @Transient
    private String custName;

    private static final long serialVersionUID = 1469428023697L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode == null ? null : invoiceCode.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getRegOperId () {
        return regOperId ;
    }

    public void setRegOperId (Long regOperId ) {
        this.regOperId  = regOperId ;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getModiOperId () {
        return modiOperId ;
    }

    public void setModiOperId (Long modiOperId ) {
        this.modiOperId  = modiOperId ;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public String getTaxpayerNo() {
        return taxpayerNo;
    }

    public void setTaxpayerNo(String taxpayerNo) {
        this.taxpayerNo = taxpayerNo == null ? null : taxpayerNo.trim();
    }

    public String getCorpVocate() {
        return corpVocate;
    }

    public void setCorpVocate(String corpVocate) {
        this.corpVocate = corpVocate == null ? null : corpVocate.trim();
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate == null ? null : invoiceDate.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer == null ? null : drawer.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public List<ScfInvoiceItem> getInvoiceItemList() {
        return this.invoiceItemList;
    }

    public void setInvoiceItemList(List<ScfInvoiceItem> anInvoiceItemList) {
        this.invoiceItemList = anInvoiceItemList;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", invoiceCode=").append(invoiceCode);
        sb.append(", invoiceNo=").append(invoiceNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", regOperId =").append(regOperId );
        sb.append(", regOperName=").append(regOperName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId =").append(modiOperId );
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", taxpayerNo=").append(taxpayerNo);
        sb.append(", corpVocate=").append(corpVocate);
        sb.append(", invoiceDate=").append(invoiceDate);
        sb.append(", balance=").append(balance);
        sb.append(", drawer=").append(drawer);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", description=").append(description);
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
        ScfInvoice other = (ScfInvoice) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInvoiceCode() == null ? other.getInvoiceCode() == null : this.getInvoiceCode().equals(other.getInvoiceCode()))
            && (this.getInvoiceNo() == null ? other.getInvoiceNo() == null : this.getInvoiceNo().equals(other.getInvoiceNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getRegOperId () == null ? other.getRegOperId () == null : this.getRegOperId ().equals(other.getRegOperId ()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId () == null ? other.getModiOperId () == null : this.getModiOperId ().equals(other.getModiOperId ()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getTaxpayerNo() == null ? other.getTaxpayerNo() == null : this.getTaxpayerNo().equals(other.getTaxpayerNo()))
            && (this.getCorpVocate() == null ? other.getCorpVocate() == null : this.getCorpVocate().equals(other.getCorpVocate()))
            && (this.getInvoiceDate() == null ? other.getInvoiceDate() == null : this.getInvoiceDate().equals(other.getInvoiceDate()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getDrawer() == null ? other.getDrawer() == null : this.getDrawer().equals(other.getDrawer()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInvoiceCode() == null) ? 0 : getInvoiceCode().hashCode());
        result = prime * result + ((getInvoiceNo() == null) ? 0 : getInvoiceNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getRegOperId () == null) ? 0 : getRegOperId ().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId () == null) ? 0 : getModiOperId ().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getTaxpayerNo() == null) ? 0 : getTaxpayerNo().hashCode());
        result = prime * result + ((getCorpVocate() == null) ? 0 : getCorpVocate().hashCode());
        result = prime * result + ((getInvoiceDate() == null) ? 0 : getInvoiceDate().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getDrawer() == null) ? 0 : getDrawer().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
    
    public void initAddValue() {
        this.id = SerialGenerator.getLongValue("ScfInvoice.id");
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.regOperName = UserUtils.getOperatorInfo().getName();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
    }
    
    public void initModifyValue(CustOperatorInfo anOperator) {
        this.modiDate = BetterDateUtils.getNumDate();
        if (null != anOperator) {
            this.modiOperId = anOperator.getId();
            this.modiOperName = anOperator.getName();
            this.operOrg = anOperator.getOperOrg();
        }
        this.modiTime = BetterDateUtils.getNumTime();
    }
}