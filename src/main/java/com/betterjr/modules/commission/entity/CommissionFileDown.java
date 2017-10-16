package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CPS_FILE_DOWN")
public class CommissionFileDown implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4607002834048195924L;

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    // 下载id
    @Column(name = "L_FILE_ID", columnDefinition = "INTEGER")
    private Long fileId;

    // 下载batchNo
    @Column(name = "N_BATCHNO", columnDefinition = "DECIMAL")
    private Long batchNo;

    // 文件导入日期
    @Column(name = "D_IMPORT_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String importDate;

    // 文件导入时间
    @Column(name = "T_IMPORT_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String importTime;

    // 企业id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    // 当前企业名称
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    private String custName;

    // 操作机构
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    // 当前文件总金额
    @Column(name = "F_BALANCE", columnDefinition = "DECIMAL")
    private BigDecimal blance;

    // 当前解析文件总的行数
    @Column(name = "N_AMOUNT", columnDefinition = "INTEGER")
    private Integer amount;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    private String regTime;

    // 拜特确认当前记录是否合规 未确认 1 确认未通过 2 确认通过
    @Column(name = "C_CONFIRM_STATUS", columnDefinition = "VARCHAR")
    private String confirmStatus;

    // 确认日期
    @Column(name = "D_CONFIRM_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String confirmDate;

    // 确认时间
    @Column(name = "T_CONFIRM_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String confirmTime;

    // 确认信息
    @Column(name = "C_CONFRIM_MESSAGE", columnDefinition = "VARCHAR")
    private String confirmMessage;

    // 当前文件总金额
    @Column(name = "F_BALANCE_CONFIRM_FAILURE", columnDefinition = "DECIMAL")
    private BigDecimal confirmFailureBalance;

    // 当前解析文件总的行数
    @Column(name = "N_AMOUNT_CONFIRM_FAILURE", columnDefinition = "INTEGER")
    private Integer confirmFailureAmount;

    // 当前文件总金额
    @Column(name = "F_BALANCE_CONFIRM_SUCCESS", columnDefinition = "DECIMAL")
    private BigDecimal confirmSuccessBalance;

    // 当前解析文件总的行数
    @Column(name = "N_AMOUNT_CONFIRM_SUCCESS", columnDefinition = "INTEGER")
    private Integer confirmSuccessAmount;

    @Column(name = "L_AUDIT_OPERID", columnDefinition = "INTEGER")
    private Long auditOperId;

    @Column(name = "C_AUDIT_OPERNAME", columnDefinition = "VARCHAR")
    private String auditOperName;

    public CommissionFileDown() {
        super();
    }

    public String getConfirmStatus() {
        return this.confirmStatus;
    }

    public Long getAuditOperId() {
        return this.auditOperId;
    }

    public void setAuditOperId(Long anAuditOperId) {
        this.auditOperId = anAuditOperId;
    }

    public String getAuditOperName() {
        return this.auditOperName;
    }

    public void setAuditOperName(String anAuditOperName) {
        this.auditOperName = anAuditOperName;
    }

    public void setConfirmStatus(String anConfirmStatus) {
        this.confirmStatus = anConfirmStatus;
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

    public String getConfirmMessage() {
        return this.confirmMessage;
    }

    public void setConfirmMessage(String anConfirmMessage) {
        this.confirmMessage = anConfirmMessage;
    }

    public BigDecimal getConfirmFailureBalance() {
        return this.confirmFailureBalance;
    }

    public void setConfirmFailureBalance(BigDecimal anConfirmFailureBalance) {
        this.confirmFailureBalance = anConfirmFailureBalance;
    }

    public Integer getConfirmFailureAmount() {
        return this.confirmFailureAmount;
    }

    public void setConfirmFailureAmount(Integer anConfirmFailureAmount) {
        this.confirmFailureAmount = anConfirmFailureAmount;
    }

    public BigDecimal getConfirmSuccessBalance() {
        return this.confirmSuccessBalance;
    }

    public void setConfirmSuccessBalance(BigDecimal anConfirmSuccessBalance) {
        this.confirmSuccessBalance = anConfirmSuccessBalance;
    }

    public Integer getConfirmSuccessAmount() {
        return this.confirmSuccessAmount;
    }

    public void setConfirmSuccessAmount(Integer anConfirmSuccessAmount) {
        this.confirmSuccessAmount = anConfirmSuccessAmount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public Long getFileId() {
        return this.fileId;
    }

    public void setFileId(Long anFileId) {
        this.fileId = anFileId;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public String getImportDate() {
        return this.importDate;
    }

    public void setImportDate(String anImportDate) {
        this.importDate = anImportDate;
    }

    public String getImportTime() {
        return this.importTime;
    }

    public void setImportTime(String anImportTime) {
        this.importTime = anImportTime;
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

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public BigDecimal getBlance() {
        return this.blance;
    }

    public void setBlance(BigDecimal anBlance) {
        this.blance = anBlance;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer anAmount) {
        this.amount = anAmount;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.blance == null) ? 0 : this.blance.hashCode());
        result = prime * result + ((this.confirmDate == null) ? 0 : this.confirmDate.hashCode());
        result = prime * result + ((this.confirmFailureAmount == null) ? 0 : this.confirmFailureAmount.hashCode());
        result = prime * result + ((this.confirmFailureBalance == null) ? 0 : this.confirmFailureBalance.hashCode());
        result = prime * result + ((this.confirmMessage == null) ? 0 : this.confirmMessage.hashCode());
        result = prime * result + ((this.confirmStatus == null) ? 0 : this.confirmStatus.hashCode());
        result = prime * result + ((this.confirmSuccessAmount == null) ? 0 : this.confirmSuccessAmount.hashCode());
        result = prime * result + ((this.confirmSuccessBalance == null) ? 0 : this.confirmSuccessBalance.hashCode());
        result = prime * result + ((this.confirmTime == null) ? 0 : this.confirmTime.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.fileId == null) ? 0 : this.fileId.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.importDate == null) ? 0 : this.importDate.hashCode());
        result = prime * result + ((this.importTime == null) ? 0 : this.importTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionFileDown other = (CommissionFileDown) obj;
        if (this.amount == null) {
            if (other.amount != null) return false;
        } else if (!this.amount.equals(other.amount)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        } else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.blance == null) {
            if (other.blance != null) return false;
        } else if (!this.blance.equals(other.blance)) return false;
        if (this.confirmDate == null) {
            if (other.confirmDate != null) return false;
        } else if (!this.confirmDate.equals(other.confirmDate)) return false;
        if (this.confirmFailureAmount == null) {
            if (other.confirmFailureAmount != null) return false;
        } else if (!this.confirmFailureAmount.equals(other.confirmFailureAmount)) return false;
        if (this.confirmFailureBalance == null) {
            if (other.confirmFailureBalance != null) return false;
        } else if (!this.confirmFailureBalance.equals(other.confirmFailureBalance)) return false;
        if (this.confirmMessage == null) {
            if (other.confirmMessage != null) return false;
        } else if (!this.confirmMessage.equals(other.confirmMessage)) return false;
        if (this.confirmStatus == null) {
            if (other.confirmStatus != null) return false;
        } else if (!this.confirmStatus.equals(other.confirmStatus)) return false;
        if (this.confirmSuccessAmount == null) {
            if (other.confirmSuccessAmount != null) return false;
        } else if (!this.confirmSuccessAmount.equals(other.confirmSuccessAmount)) return false;
        if (this.confirmSuccessBalance == null) {
            if (other.confirmSuccessBalance != null) return false;
        } else if (!this.confirmSuccessBalance.equals(other.confirmSuccessBalance)) return false;
        if (this.confirmTime == null) {
            if (other.confirmTime != null) return false;
        } else if (!this.confirmTime.equals(other.confirmTime)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.fileId == null) {
            if (other.fileId != null) return false;
        } else if (!this.fileId.equals(other.fileId)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.importDate == null) {
            if (other.importDate != null) return false;
        } else if (!this.importDate.equals(other.importDate)) return false;
        if (this.importTime == null) {
            if (other.importTime != null) return false;
        } else if (!this.importTime.equals(other.importTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        } else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommissionFileDown [id=" + this.id + ", fileId=" + this.fileId + ", batchNo=" + this.batchNo
                + ", importDate=" + this.importDate + ", importTime=" + this.importTime + ", custNo=" + this.custNo
                + ", custName=" + this.custName + ", operOrg=" + this.operOrg + ", blance=" + this.blance + ", amount="
                + this.amount + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", confirmStatus="
                + this.confirmStatus + ", confirmDate=" + this.confirmDate + ", confirmTime=" + this.confirmTime
                + ", confirmMessage=" + this.confirmMessage + ", confirmFailureBalance=" + this.confirmFailureBalance
                + ", confirmFailureAmount=" + this.confirmFailureAmount + ", confirmSuccessBalance="
                + this.confirmSuccessBalance + ", confirmSuccessAmount=" + this.confirmSuccessAmount + ", auditOperId="
                + this.auditOperId + ", auditOperName=" + this.auditOperName + "]";
    }

    public void saveAddInit(int anRecordAmount, BigDecimal anBlance, CommissionRecord anRecord) {

        this.setAmount(anRecordAmount);
        this.setBlance(anBlance);
        this.setCustName(anRecord.getCustName());
        this.setCustNo(anRecord.getCustNo());
        this.setOperOrg(anRecord.getOperOrg());
        this.setImportDate(anRecord.getImportDate());
        this.setImportTime(anRecord.getImportTime());
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.confirmStatus = CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_UNCONFIRMED;
        this.id = SerialGenerator.getLongValue("CommissionFileDown.id");

    }

    public void saveAuditInit(String anConfirmStatus, String anConfirmMessage, CustOperatorInfo anOperatorInfo) {

        this.auditOperId = anOperatorInfo.getId();
        this.auditOperName = anOperatorInfo.getName();
        this.confirmDate = BetterDateUtils.getNumDate();
        this.confirmMessage = anConfirmMessage;
        this.confirmStatus = anConfirmStatus;
        this.confirmTime = BetterDateUtils.getNumTime();

    }

}
