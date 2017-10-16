package com.betterjr.modules.commission.entity;

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
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_invoice")
public class CommissionInvoice implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 9096698491446660763L;

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    // 申请编号
    @Column(name = "C_REFNO", columnDefinition = "VARCHAR")
    private String refNo;

    // 发票代码
    @Column(name = "C_INVOICE_CODE", columnDefinition = "VARCHAR")
    private String invoiceCode;

    // 发票号码
    @Column(name = "C_INVOICE_NO", columnDefinition = "VARCHAR")
    private String invoiceNo;

    // 客户编号
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    // 客户名称
    @Column(name = "L_CUSTNAME", columnDefinition = "VARCHAR")
    private String custName;

    // 核心企业Id
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    private Long coreCustNo;

    // 核心企业名称
    @Column(name = "L_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    private String coreCustName;

    // 操作机构
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    // 确认人名字
    @Column(name = "C_CONFIRM_OPERNAME", columnDefinition = "VARCHAR")
    private String confirmOperName;

    // 确认日期
    @Column(name = "D_CONFIRM_DATE", columnDefinition = "VARCHAR")
    private String confirmDate;

    //// 确认时间
    @Column(name = "T_CONFIRM_TIME", columnDefinition = "VARCHAR")
    private String confirmTime;

    // 确认人Id
    @Column(name = "L_CONFIRM_OPERID", columnDefinition = "INTEGER")
    private Long confirmOperId;

    // 开票日期
    @Column(name = "D_INVOICE_DATE", columnDefinition = "VARCHAR")
    private String invoiceDate;

    /**
     * 发票总金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "总金额", comments = "总金额")
    private BigDecimal balance;

    // 开票人
    @Column(name = "C_DRAWER", columnDefinition = "VARCHAR")
    private String drawer;

    // 上传的批次号
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    private Long batchNo;

    // 状态 0 初始化状态 1 待开票 2 开票中 3 已开票 4 作废
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    private String businStatus;

    // 备注
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    private String description;

    // 注册人名字
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    private String regOperName;

    // 注册日期
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    //// 注册时间
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String regTime;

    // 注册人Id
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    private Long regOperId;

    // 提交开票人名字
    @Column(name = "C_AUDIT_OPERNAME", columnDefinition = "VARCHAR")
    private String auditOperName;

    // 提交开票日期
    @Column(name = "D_AUDIT_DATE", columnDefinition = "VARCHAR")
    private String auditDate;

    //// 提交开票时间
    @Column(name = "T_AUDIT_TIME", columnDefinition = "VARCHAR")
    private String auditTime;

    // 提交开票人Id
    @Column(name = "L_AUDIT_OPERID", columnDefinition = "INTEGER")
    private Long auditOperId;

    /**
     * 平台发票抬头Id
     */
    @Column(name = "L_CUSTINVOICE_ID", columnDefinition = "INTEGER")
    @MetaData(value = "平台发票抬头Id", comments = "平台发票抬头Id")
    private Long custInvoiceId;

    @Transient
    private CommissionInvoiceCustInfo custInvoiceInfo;

    @Column(name = "L_CORECUSTINVOICE_ID", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业发票抬头id", comments = "核心企业发票抬头id")
    private Long coreCustInvoiceId;

    @Transient
    private CommissionInvoiceCustInfo coreCustInvoiceInfo;

    // 发票种类 0 普通发票 1 专用发票
    @Column(name = "C_INVOICE_TYPE", columnDefinition = "VARCHAR")
    private String invoiceType;

    // 发票内容
    @Column(name = "C_INVOICE_CONTENT", columnDefinition = "VARCHAR")
    private String invoiceContent;

    @Column(name = "F_TAX_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "税额", comments = "税额")
    private BigDecimal taxBalance;

    @Column(name = "F_INTEREST_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "发票金额", comments = "发票金额")
    private BigDecimal interestBalance;

    @Column(name = "F_TAX_RATE", columnDefinition = "DECIMAL")
    @MetaData(value = "税率", comments = "税率")
    private BigDecimal taxRate;

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

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getConfirmOperName() {
        return this.confirmOperName;
    }

    public void setConfirmOperName(String anConfirmOperName) {
        this.confirmOperName = anConfirmOperName;
    }

    public String getConfirmDate() {
        return this.confirmDate;
    }

    public void setConfirmDate(String anConfirmDate) {
        this.confirmDate = anConfirmDate;
    }

    public String getConfirmTime() {
        return this.confirmTime;
    }

    public void setConfirmTime(String anConfirmTime) {
        this.confirmTime = anConfirmTime;
    }

    public Long getConfirmOperId() {
        return this.confirmOperId;
    }

    public void setConfirmOperId(Long anConfirmOperId) {
        this.confirmOperId = anConfirmOperId;
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

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public String getRegOperName() {
        return this.regOperName;
    }

    public void setRegOperName(String anRegOperName) {
        this.regOperName = anRegOperName;
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

    public Long getRegOperId() {
        return this.regOperId;
    }

    public void setRegOperId(Long anRegOperId) {
        this.regOperId = anRegOperId;
    }

    public String getAuditOperName() {
        return this.auditOperName;
    }

    public void setAuditOperName(String anAuditOperName) {
        this.auditOperName = anAuditOperName;
    }

    public String getAuditDate() {
        return this.auditDate;
    }

    public void setAuditDate(String anAuditDate) {
        this.auditDate = anAuditDate;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String anAuditTime) {
        this.auditTime = anAuditTime;
    }

    public Long getAuditOperId() {
        return this.auditOperId;
    }

    public void setAuditOperId(Long anAuditOperId) {
        this.auditOperId = anAuditOperId;
    }

    public Long getCustInvoiceId() {
        return this.custInvoiceId;
    }

    public void setCustInvoiceId(Long anCustInvoiceId) {
        this.custInvoiceId = anCustInvoiceId;
    }

    public CommissionInvoiceCustInfo getCustInvoiceInfo() {
        return this.custInvoiceInfo;
    }

    public void setCustInvoiceInfo(CommissionInvoiceCustInfo anCustInvoiceInfo) {
        this.custInvoiceInfo = anCustInvoiceInfo;
    }

    public Long getCoreCustInvoiceId() {
        return this.coreCustInvoiceId;
    }

    public void setCoreCustInvoiceId(Long anCoreCustInvoiceId) {
        this.coreCustInvoiceId = anCoreCustInvoiceId;
    }

    public CommissionInvoiceCustInfo getCoreCustInvoiceInfo() {
        return this.coreCustInvoiceInfo;
    }

    public void setCoreCustInvoiceInfo(CommissionInvoiceCustInfo anCoreCustInvoiceInfo) {
        this.coreCustInvoiceInfo = anCoreCustInvoiceInfo;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(String anInvoiceType) {
        this.invoiceType = anInvoiceType;
    }

    public String getInvoiceContent() {
        return this.invoiceContent;
    }

    public void setInvoiceContent(String anInvoiceContent) {
        this.invoiceContent = anInvoiceContent;
    }

    public BigDecimal getTaxBalance() {
        return this.taxBalance;
    }

    public void setTaxBalance(BigDecimal anTaxBalance) {
        this.taxBalance = anTaxBalance;
    }

    public BigDecimal getInterestBalance() {
        return this.interestBalance;
    }

    public void setInterestBalance(BigDecimal anInterestBalance) {
        this.interestBalance = anInterestBalance;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal anTaxRate) {
        this.taxRate = anTaxRate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.auditDate == null) ? 0 : this.auditDate.hashCode());
        result = prime * result + ((this.auditOperId == null) ? 0 : this.auditOperId.hashCode());
        result = prime * result + ((this.auditOperName == null) ? 0 : this.auditOperName.hashCode());
        result = prime * result + ((this.auditTime == null) ? 0 : this.auditTime.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.confirmDate == null) ? 0 : this.confirmDate.hashCode());
        result = prime * result + ((this.confirmOperId == null) ? 0 : this.confirmOperId.hashCode());
        result = prime * result + ((this.confirmOperName == null) ? 0 : this.confirmOperName.hashCode());
        result = prime * result + ((this.confirmTime == null) ? 0 : this.confirmTime.hashCode());
        result = prime * result + ((this.coreCustInvoiceId == null) ? 0 : this.coreCustInvoiceId.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custInvoiceId == null) ? 0 : this.custInvoiceId.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.drawer == null) ? 0 : this.drawer.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.interestBalance == null) ? 0 : this.interestBalance.hashCode());
        result = prime * result + ((this.invoiceCode == null) ? 0 : this.invoiceCode.hashCode());
        result = prime * result + ((this.invoiceContent == null) ? 0 : this.invoiceContent.hashCode());
        result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
        result = prime * result + ((this.invoiceNo == null) ? 0 : this.invoiceNo.hashCode());
        result = prime * result + ((this.invoiceType == null) ? 0 : this.invoiceType.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.taxBalance == null) ? 0 : this.taxBalance.hashCode());
        result = prime * result + ((this.taxRate == null) ? 0 : this.taxRate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionInvoice other = (CommissionInvoice) obj;
        if (this.auditDate == null) {
            if (other.auditDate != null) return false;
        } else if (!this.auditDate.equals(other.auditDate)) return false;
        if (this.auditOperId == null) {
            if (other.auditOperId != null) return false;
        } else if (!this.auditOperId.equals(other.auditOperId)) return false;
        if (this.auditOperName == null) {
            if (other.auditOperName != null) return false;
        } else if (!this.auditOperName.equals(other.auditOperName)) return false;
        if (this.auditTime == null) {
            if (other.auditTime != null) return false;
        } else if (!this.auditTime.equals(other.auditTime)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        } else if (!this.balance.equals(other.balance)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        } else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.confirmDate == null) {
            if (other.confirmDate != null) return false;
        } else if (!this.confirmDate.equals(other.confirmDate)) return false;
        if (this.confirmOperId == null) {
            if (other.confirmOperId != null) return false;
        } else if (!this.confirmOperId.equals(other.confirmOperId)) return false;
        if (this.confirmOperName == null) {
            if (other.confirmOperName != null) return false;
        } else if (!this.confirmOperName.equals(other.confirmOperName)) return false;
        if (this.confirmTime == null) {
            if (other.confirmTime != null) return false;
        } else if (!this.confirmTime.equals(other.confirmTime)) return false;
        if (this.coreCustInvoiceId == null) {
            if (other.coreCustInvoiceId != null) return false;
        } else if (!this.coreCustInvoiceId.equals(other.coreCustInvoiceId)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        } else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        } else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.custInvoiceId == null) {
            if (other.custInvoiceId != null) return false;
        } else if (!this.custInvoiceId.equals(other.custInvoiceId)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        } else if (!this.description.equals(other.description)) return false;
        if (this.drawer == null) {
            if (other.drawer != null) return false;
        } else if (!this.drawer.equals(other.drawer)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.interestBalance == null) {
            if (other.interestBalance != null) return false;
        } else if (!this.interestBalance.equals(other.interestBalance)) return false;
        if (this.invoiceCode == null) {
            if (other.invoiceCode != null) return false;
        } else if (!this.invoiceCode.equals(other.invoiceCode)) return false;
        if (this.invoiceContent == null) {
            if (other.invoiceContent != null) return false;
        } else if (!this.invoiceContent.equals(other.invoiceContent)) return false;
        if (this.invoiceDate == null) {
            if (other.invoiceDate != null) return false;
        } else if (!this.invoiceDate.equals(other.invoiceDate)) return false;
        if (this.invoiceNo == null) {
            if (other.invoiceNo != null) return false;
        } else if (!this.invoiceNo.equals(other.invoiceNo)) return false;
        if (this.invoiceType == null) {
            if (other.invoiceType != null) return false;
        } else if (!this.invoiceType.equals(other.invoiceType)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        } else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        } else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        } else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        if (this.taxBalance == null) {
            if (other.taxBalance != null) return false;
        } else if (!this.taxBalance.equals(other.taxBalance)) return false;
        if (this.taxRate == null) {
            if (other.taxRate != null) return false;
        } else if (!this.taxRate.equals(other.taxRate)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommissionInvoice [id=" + this.id + ", refNo=" + this.refNo + ", invoiceCode=" + this.invoiceCode
                + ", invoiceNo=" + this.invoiceNo + ", custNo=" + this.custNo + ", custName=" + this.custName
                + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", operOrg="
                + this.operOrg + ", confirmOperName=" + this.confirmOperName + ", confirmDate=" + this.confirmDate
                + ", confirmTime=" + this.confirmTime + ", confirmOperId=" + this.confirmOperId + ", invoiceDate="
                + this.invoiceDate + ", balance=" + this.balance + ", drawer=" + this.drawer + ", batchNo="
                + this.batchNo + ", businStatus=" + this.businStatus + ", description=" + this.description
                + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime=" + this.regTime
                + ", regOperId=" + this.regOperId + ", auditOperName=" + this.auditOperName + ", auditDate="
                + this.auditDate + ", auditTime=" + this.auditTime + ", auditOperId=" + this.auditOperId
                + ", custInvoiceId=" + this.custInvoiceId + ", custInvoiceInfo=" + this.custInvoiceInfo
                + ", coreCustInvoiceId=" + this.coreCustInvoiceId + ", coreCustInvoiceInfo=" + this.coreCustInvoiceInfo
                + ", invoiceType=" + this.invoiceType + ", invoiceContent=" + this.invoiceContent + ", taxBalance="
                + this.taxBalance + ", interestBalance=" + this.interestBalance + ", taxRate=" + this.taxRate + "]";
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo) {

        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setId(SerialGenerator.getLongValue("CommissionInvoice.id"));
        this.setBusinStatus("0");
        this.setRefNo(SequenceFactory.generate("PLAT_COMMON", "#{Date:yyyyMMdd}#{Seq:12}", "D"));
    }

    public void initConfirmValue(CustOperatorInfo anOperatorInfo) {

        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setConfirmDate(BetterDateUtils.getNumDate());
        this.setConfirmTime(BetterDateUtils.getNumTime());
        this.setConfirmOperId(anOperatorInfo.getId());
        this.setConfirmOperName(anOperatorInfo.getName());
        this.setBusinStatus("2");

    }

    public void initAuditValue(CustOperatorInfo anOperatorInfo, CommissionInvoice anInvoice) {

        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setAuditDate(BetterDateUtils.getNumDate());
        this.setAuditTime(BetterDateUtils.getNumTime());
        this.setAuditOperId(anOperatorInfo.getId());
        this.setAuditOperName(anOperatorInfo.getName());
        this.setBusinStatus("3");
        this.setInvoiceCode(anInvoice.getInvoiceCode());
        this.setInvoiceNo(anInvoice.getInvoiceNo());
        this.setDescription(anInvoice.getDescription());
        this.setInvoiceDate(anInvoice.getInvoiceDate());
        this.setDrawer(anInvoice.getDrawer());
    }

}
