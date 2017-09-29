package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_record")
public class CommissionRecord implements BetterjrEntity {

    // 编号
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    // 佣金文件id
    @Column(name = "L_CPS_FILE_ID", columnDefinition = "INTEGER")
    private Long fileId;

    // 凭证编号
    @Column(name = "C_REFNO", columnDefinition = "VARCHAR")
    private String refNo;

    // 导入日期
    @Column(name = "D_IMPORT_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String importDate;

    // 导入时间
    @Column(name = "T_IMPORT_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String importTime;

    // 片区
    @Column(name = "C_AREA", columnDefinition = "VARCHAR")
    private String area;

    // 客户名称
    @Column(name = "C_CUSTOMER_NAME", columnDefinition = "VARCHAR")
    private String customerName;

    // 数量
    @Column(name = "N_AMOUNT", columnDefinition = "INTEGER")
    private Long amount;

    // 单价
    @Column(name = "F_UNIT", columnDefinition = "DECIMAL")
    private BigDecimal unit;

    // 金额
    @Column(name = "F_BALANCE", columnDefinition = "DECIMAL")
    private BigDecimal balance;

    // 银行账户
    @Column(name = "C_BANK_ACCOUNT", columnDefinition = "VARCHAR")
    private String bankAccount;

    // 银行账户名
    @Column(name = "C_BANK_ACCOUNT_NAME", columnDefinition = "VARCHAR")
    private String bankAccountName;

    // 银行
    @Column(name = "C_BANK", columnDefinition = "VARCHAR")
    private String bank;

    // 银行全称
    @Column(name = "C_BANK_NAME", columnDefinition = "VARCHAR")
    private String bankName;

    // 银行地址
    @Column(name = "C_BANK_ADDRESS", columnDefinition = "VARCHAR")
    private String bankAddress;

    // 联系人
    @Column(name = "C_CONTACTS", columnDefinition = "VARCHAR")
    private String contacts;

    // 联系人手机号码
    @Column(name = "C_CONTACTS_MOBILENO", columnDefinition = "VARCHAR")
    private String contactsMobileNo;

    // 客户ID
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    // 客户名称
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    private String custName;

    // 操作机构
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    // 支付状态 0 未处理 2 支付成功 1 支付失败
    @Column(name = "C_PAY_STATUS", columnDefinition = "VARCHAR")
    private String payStatus;

    // 业务状态 0 未处理 1 已审核(最终可供 拜特支付状态) 2已支付 3删除
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    private String businStatus;

    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    private String regOperName;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String regTime;

    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    private String modiOperName;

    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String modiTime;

    @Column(name = "N_VERSION", columnDefinition = "VARCHAR")
    private String version;

    // 拜特确认当前记录是否合规 0 未确认 1 不合规 2 合规
    @Column(name = "C_CONFIRM_STATUS", columnDefinition = "VARCHAR")
    private String confirmStatus;

    private static final long serialVersionUID = 1493715864481L;

    public String getConfirmStatus() {
        return this.confirmStatus;
    }

    public void setConfirmStatus(String anConfirmStatus) {
        this.confirmStatus = anConfirmStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo == null ? null : refNo.trim();
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate == null ? null : importDate.trim();
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime == null ? null : importTime.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public BigDecimal getUnit() {
        return unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress == null ? null : bankAddress.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactsMobileNo() {
        return contactsMobileNo;
    }

    public void setContactsMobileNo(String contactsMobileNo) {
        this.contactsMobileNo = contactsMobileNo == null ? null : contactsMobileNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public Long getFileId() {
        return this.fileId;
    }

    public void setFileId(Long anFileId) {
        this.fileId = anFileId;
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

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.area == null) ? 0 : this.area.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.bank == null) ? 0 : this.bank.hashCode());
        result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
        result = prime * result + ((this.bankAccountName == null) ? 0 : this.bankAccountName.hashCode());
        result = prime * result + ((this.bankAddress == null) ? 0 : this.bankAddress.hashCode());
        result = prime * result + ((this.bankName == null) ? 0 : this.bankName.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.confirmStatus == null) ? 0 : this.confirmStatus.hashCode());
        result = prime * result + ((this.contacts == null) ? 0 : this.contacts.hashCode());
        result = prime * result + ((this.contactsMobileNo == null) ? 0 : this.contactsMobileNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.customerName == null) ? 0 : this.customerName.hashCode());
        result = prime * result + ((this.fileId == null) ? 0 : this.fileId.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.importDate == null) ? 0 : this.importDate.hashCode());
        result = prime * result + ((this.importTime == null) ? 0 : this.importTime.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.payStatus == null) ? 0 : this.payStatus.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.unit == null) ? 0 : this.unit.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionRecord other = (CommissionRecord) obj;
        if (this.amount == null) {
            if (other.amount != null) return false;
        }
        else if (!this.amount.equals(other.amount)) return false;
        if (this.area == null) {
            if (other.area != null) return false;
        }
        else if (!this.area.equals(other.area)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.bank == null) {
            if (other.bank != null) return false;
        }
        else if (!this.bank.equals(other.bank)) return false;
        if (this.bankAccount == null) {
            if (other.bankAccount != null) return false;
        }
        else if (!this.bankAccount.equals(other.bankAccount)) return false;
        if (this.bankAccountName == null) {
            if (other.bankAccountName != null) return false;
        }
        else if (!this.bankAccountName.equals(other.bankAccountName)) return false;
        if (this.bankAddress == null) {
            if (other.bankAddress != null) return false;
        }
        else if (!this.bankAddress.equals(other.bankAddress)) return false;
        if (this.bankName == null) {
            if (other.bankName != null) return false;
        }
        else if (!this.bankName.equals(other.bankName)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.confirmStatus == null) {
            if (other.confirmStatus != null) return false;
        }
        else if (!this.confirmStatus.equals(other.confirmStatus)) return false;
        if (this.contacts == null) {
            if (other.contacts != null) return false;
        }
        else if (!this.contacts.equals(other.contacts)) return false;
        if (this.contactsMobileNo == null) {
            if (other.contactsMobileNo != null) return false;
        }
        else if (!this.contactsMobileNo.equals(other.contactsMobileNo)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.customerName == null) {
            if (other.customerName != null) return false;
        }
        else if (!this.customerName.equals(other.customerName)) return false;
        if (this.fileId == null) {
            if (other.fileId != null) return false;
        }
        else if (!this.fileId.equals(other.fileId)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.importDate == null) {
            if (other.importDate != null) return false;
        }
        else if (!this.importDate.equals(other.importDate)) return false;
        if (this.importTime == null) {
            if (other.importTime != null) return false;
        }
        else if (!this.importTime.equals(other.importTime)) return false;
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
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.payStatus == null) {
            if (other.payStatus != null) return false;
        }
        else if (!this.payStatus.equals(other.payStatus)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        }
        else if (!this.refNo.equals(other.refNo)) return false;
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
        if (this.unit == null) {
            if (other.unit != null) return false;
        }
        else if (!this.unit.equals(other.unit)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        }
        else if (!this.version.equals(other.version)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommissionRecord [id=" + this.id + ", fileId=" + this.fileId + ", refNo=" + this.refNo + ", importDate=" + this.importDate
                + ", importTime=" + this.importTime + ", area=" + this.area + ", customerName=" + this.customerName + ", amount=" + this.amount
                + ", unit=" + this.unit + ", balance=" + this.balance + ", bankAccount=" + this.bankAccount + ", bankAccountName="
                + this.bankAccountName + ", bank=" + this.bank + ", bankName=" + this.bankName + ", bankAddress=" + this.bankAddress + ", contacts="
                + this.contacts + ", contactsMobileNo=" + this.contactsMobileNo + ", custNo=" + this.custNo + ", custName=" + this.custName
                + ", operOrg=" + this.operOrg + ", payStatus=" + this.payStatus + ", businStatus=" + this.businStatus + ", regOperId="
                + this.regOperId + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", modiOperId="
                + this.modiOperId + ", modiOperName=" + this.modiOperName + ", modiDate=" + this.modiDate + ", modiTime=" + this.modiTime
                + ", version=" + this.version + ", confirmStatus=" + this.confirmStatus + "]";
    }

    public CommissionRecord() {
        super();
    }

    public CommissionRecord(String anRefNo) {
        super();
        this.refNo = anRefNo;
    }

    public void initResolveValue(Map<String, Object> anDataMap, int anI, CustOperatorInfo anOperatorInfo) {

        try {
            BeanUtils.populate(this, anDataMap);
            this.setPayStatus(CommissionConstantCollentions.COMMISSION_PAY_STATUS_NO_HANDLE);
            this.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE);
            this.setRegDate(BetterDateUtils.getNumDate());
            this.setRegTime(BetterDateUtils.getNumTime());
            this.setRegOperId(anOperatorInfo.getId());
            this.setRegOperName(anOperatorInfo.getName());
            this.setVersion("1");
            this.confirmStatus = CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_UNCONFIRMED;
            this.refNo = SequenceFactory.generate("PLAT_COMMISSION_RECORD", this.getOperOrg(), "CP#{Date:yyMMdd}#{Seq:8}", "D");
            this.id = SerialGenerator.getLongValue("CommissionRecord.id");
        }
        catch (Exception e) {

            BTAssert.notNull(null, "第" + anI + "行记录解析失败" + e.getMessage());
        }

    }

    public CommissionRecord saveAuditInit(CustOperatorInfo anOperatorInfo) {

        this.setBusinStatus(CommissionConstantCollentions.COMMISSION_RECORD_BUSIN_STATUS_AUDIT);
        this.setModiDate(BetterDateUtils.getNumDate());
        this.setModiTime(BetterDateUtils.getNumTime());
        this.setModiOperId(anOperatorInfo.getId());
        this.setModiOperName(anOperatorInfo.getName());
        return this;

    }

}