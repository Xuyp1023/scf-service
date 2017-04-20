package com.betterjr.modules.order.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.entity.BaseVersionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_INVOICE_V3")
public class ScfInvoiceDO extends BaseVersionEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 2781626846765308979L;
    
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
     * 客户编号（销售方id）
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户名称（销售方id）
     */
    @Column(name = "L_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户名称", comments = "客户名称")
    private String custName;
    
    /**
     * 纳税人识别号（销售方id）
     */
    @Column(name = "C_CUST_TAXPAYERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方纳税人识别号", comments = "销售方纳税人识别号")
    private String custTaxpayerNo;
    
    /**
     * 方手机号码
     */
    @Column(name = "C_CUST_PHONE",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方手机号码", comments = "销售方手机号码")
    private String custPhone;
    
    /**
     * 开户行及账号
     */
    @Column(name = "C_CUST_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方开户行及账号", comments = "销售方开户行及账号")
    private String custAccount;
    
    
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
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;
    

    /**
     * 核心企业编号(购买方id)
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 核心企业名称(购买方企业名称)
     */
    @Column(name = "L_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业名称", comments = "核心企业名称")
    private String coreCustName;
    
    /**
     * (购买方企业)纳税人识别号
     */
    @Column(name = "C_CORECUST_TAXPAYERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方纳税人识别号", comments = "销售方纳税人识别号")
    private String coreCustTaxpayerNo;
    
    /**
     * (购买方企业)手机号码
     */
    @Column(name = "C_CORECUST_PHONE",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方手机号码", comments = "销售方手机号码")
    private String coreCustPhone;
    
    /**
     * (购买方企业)开户行及帐号
     */
    @Column(name = "C_CORECUST_ACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="销售方开户行及帐号", comments = "销售方开户行及帐号")
    private String coreCustAccount;

    @Transient
    private List<ScfInvoiceDODetail> InvoiceDetailList=new ArrayList<>();
    
    public String getInvoiceCode() {
        return this.invoiceCode;
    }

    public void setInvoiceCode(String anInvoiceCode) {
        this.invoiceCode = anInvoiceCode;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String anInvoiceNo) {
        this.invoiceNo = anInvoiceNo;
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

    public String getCustTaxpayerNo() {
        return this.custTaxpayerNo;
    }

    public void setCustTaxpayerNo(String anCustTaxpayerNo) {
        this.custTaxpayerNo = anCustTaxpayerNo;
    }

    public String getCustPhone() {
        return this.custPhone;
    }

    public void setCustPhone(String anCustPhone) {
        this.custPhone = anCustPhone;
    }

    public String getCustAccount() {
        return this.custAccount;
    }

    public void setCustAccount(String anCustAccount) {
        this.custAccount = anCustAccount;
    }

    public Long getRegOperId() {
        return this.regOperId;
    }

    public void setRegOperId(Long anRegOperId) {
        this.regOperId = anRegOperId;
    }

    public String getRegOperName() {
        return this.regOperName;
    }

    public void setRegOperName(String anRegOperName) {
        this.regOperName = anRegOperName;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
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

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }

    public String getCorpVocate() {
        return this.corpVocate;
    }

    public void setCorpVocate(String anCorpVocate) {
        this.corpVocate = anCorpVocate;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(String anInvoiceDate) {
        this.invoiceDate = anInvoiceDate;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public String getDrawer() {
        return this.drawer;
    }

    public void setDrawer(String anDrawer) {
        this.drawer = anDrawer;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
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

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public String getCoreCustTaxpayerNo() {
        return this.coreCustTaxpayerNo;
    }

    public void setCoreCustTaxpayerNo(String anCoreCustTaxpayerNo) {
        this.coreCustTaxpayerNo = anCoreCustTaxpayerNo;
    }

    public String getCoreCustPhone() {
        return this.coreCustPhone;
    }

    public void setCoreCustPhone(String anCoreCustPhone) {
        this.coreCustPhone = anCoreCustPhone;
    }

    public String getCoreCustAccount() {
        return this.coreCustAccount;
    }

    public void setCoreCustAccount(String anCoreCustAccount) {
        this.coreCustAccount = anCoreCustAccount;
    }

    public List<ScfInvoiceDODetail> getInvoiceDetailList() {
        return this.InvoiceDetailList;
    }

    public void setInvoiceDetailList(List<ScfInvoiceDODetail> anInvoiceDetailList) {
        this.InvoiceDetailList = anInvoiceDetailList;
    }

    public ScfInvoiceDO() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.coreCustAccount == null) ? 0 : this.coreCustAccount.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.coreCustPhone == null) ? 0 : this.coreCustPhone.hashCode());
        result = prime * result + ((this.coreCustTaxpayerNo == null) ? 0 : this.coreCustTaxpayerNo.hashCode());
        result = prime * result + ((this.corpVocate == null) ? 0 : this.corpVocate.hashCode());
        result = prime * result + ((this.custAccount == null) ? 0 : this.custAccount.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.custPhone == null) ? 0 : this.custPhone.hashCode());
        result = prime * result + ((this.custTaxpayerNo == null) ? 0 : this.custTaxpayerNo.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.drawer == null) ? 0 : this.drawer.hashCode());
        result = prime * result + ((this.invoiceCode == null) ? 0 : this.invoiceCode.hashCode());
        result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
        result = prime * result + ((this.invoiceNo == null) ? 0 : this.invoiceNo.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfInvoiceDO other = (ScfInvoiceDO) obj;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.coreCustAccount == null) {
            if (other.coreCustAccount != null) return false;
        }
        else if (!this.coreCustAccount.equals(other.coreCustAccount)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.coreCustPhone == null) {
            if (other.coreCustPhone != null) return false;
        }
        else if (!this.coreCustPhone.equals(other.coreCustPhone)) return false;
        if (this.coreCustTaxpayerNo == null) {
            if (other.coreCustTaxpayerNo != null) return false;
        }
        else if (!this.coreCustTaxpayerNo.equals(other.coreCustTaxpayerNo)) return false;
        if (this.corpVocate == null) {
            if (other.corpVocate != null) return false;
        }
        else if (!this.corpVocate.equals(other.corpVocate)) return false;
        if (this.custAccount == null) {
            if (other.custAccount != null) return false;
        }
        else if (!this.custAccount.equals(other.custAccount)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.custPhone == null) {
            if (other.custPhone != null) return false;
        }
        else if (!this.custPhone.equals(other.custPhone)) return false;
        if (this.custTaxpayerNo == null) {
            if (other.custTaxpayerNo != null) return false;
        }
        else if (!this.custTaxpayerNo.equals(other.custTaxpayerNo)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.drawer == null) {
            if (other.drawer != null) return false;
        }
        else if (!this.drawer.equals(other.drawer)) return false;
        if (this.invoiceCode == null) {
            if (other.invoiceCode != null) return false;
        }
        else if (!this.invoiceCode.equals(other.invoiceCode)) return false;
        if (this.invoiceDate == null) {
            if (other.invoiceDate != null) return false;
        }
        else if (!this.invoiceDate.equals(other.invoiceDate)) return false;
        if (this.invoiceNo == null) {
            if (other.invoiceNo != null) return false;
        }
        else if (!this.invoiceNo.equals(other.invoiceNo)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        }
        else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        }
        else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        }
        else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        }
        else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        }
        else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfInvoiceDO [invoiceCode=" + this.invoiceCode + ", invoiceNo=" + this.invoiceNo + ", custNo=" + this.custNo + ", custName="
                + this.custName + ", custTaxpayerNo=" + this.custTaxpayerNo + ", custPhone=" + this.custPhone + ", custAccount=" + this.custAccount
                + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName + ", operOrg=" + this.operOrg + ", regDate=" + this.regDate
                + ", regTime=" + this.regTime + ", modiOperId=" + this.getModiOperId() + ", modiOperName=" + this.modiOperName + ", modiDate="
                + this.modiDate + ", modiTime=" + this.modiTime + ", corpVocate=" + this.corpVocate + ", invoiceDate=" + this.invoiceDate
                + ", balance=" + this.balance + ", drawer=" + this.drawer + ", batchNo=" + this.batchNo + ", description=" + this.description
                + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", coreCustTaxpayerNo=" + this.coreCustTaxpayerNo
                + ", coreCustPhone=" + this.coreCustPhone + ", coreCustAccount=" + this.coreCustAccount + ", getRefNo()=" + this.getRefNo()
                + ", getVersion()=" + this.getVersion() + ", getIsLatest()=" + this.getIsLatest() + ", getBusinStatus()=" + this.getBusinStatus()
                + ", getLockedStatus()=" + this.getLockedStatus() + ", getDocStatus()=" + this.getDocStatus() + ", getAuditOperId()="
                + this.getAuditOperId() + ", getAuditOperName()=" + this.getAuditOperName() + ", getAuditData()=" + this.getAuditData()
                + ", getAuditTime()=" + this.getAuditTime() + ", getId()=" + this.getId() + "]";
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo, boolean anConfirmFlag) {
        
        BTAssert.notNull(anOperatorInfo,"无法获取登录信息,操作失败");
        this.setId(SerialGenerator.getLongValue("ScfInvoiceDO.id"));
        this.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        this.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        this.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        if(anConfirmFlag){
            this.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM);
        }
        if (null != anOperatorInfo) {
            this.setModiOperId(anOperatorInfo.getId());
            this.modiOperName = anOperatorInfo.getName();
            this.operOrg = anOperatorInfo.getOperOrg();
            this.regOperId=anOperatorInfo.getId();
            this.regOperName=anOperatorInfo.getName();
        }
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
    }

    public void initModifyValue(ScfInvoiceDO anInvoice) {
        
        this.setId(anInvoice.getId());
        this.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        this.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        this.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        this.regDate=anInvoice.getRegDate();
        this.regTime=anInvoice.getRegTime();
        this.modiDate=BetterDateUtils.getNumDate();
        this.modiTime=BetterDateUtils.getNumTime();
        this.operOrg=anInvoice.getOperOrg();
        this.regOperId=anInvoice.getRegOperId();
        this.regOperName=anInvoice.getRegOperName();
        this.modiOperName=anInvoice.getModiOperName();
        this.setModiOperId(anInvoice.getModiOperId());
        this.custPhone=anInvoice.getCustPhone();
        this.custAccount=anInvoice.getCustAccount();
        this.custName=anInvoice.getCustName();
        this.custTaxpayerNo=anInvoice.getCustTaxpayerNo();
        this.coreCustAccount=anInvoice.getCoreCustAccount();
        this.coreCustName=anInvoice.getCoreCustName();
        this.coreCustPhone=anInvoice.getCoreCustPhone();
        this.coreCustTaxpayerNo=anInvoice.getCoreCustTaxpayerNo();
        this.corpVocate=anInvoice.getCorpVocate();
    }

}
