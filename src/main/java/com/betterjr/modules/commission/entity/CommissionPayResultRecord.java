package com.betterjr.modules.commission.entity;

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
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_pay_result_record")
public class CommissionPayResultRecord implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 凭证号
     */
    @Column(name = "C_REFNO", columnDefinition = "VARCHAR")
    @MetaData(value = "凭证号", comments = "凭证号")
    private String refNo;

    /**
     * 支付结果编号
     */
    @Column(name = "L_PAY_RESULT_ID", columnDefinition = "INTEGER")
    @MetaData(value = "支付结果编号", comments = "支付结果编号")
    private Long payResultId;

    /**
     * 支付结果凭证号
     */
    @Column(name = "C_PAY_RESULT_REFNO", columnDefinition = "VARCHAR")
    @MetaData(value = "支付结果凭证号", comments = "支付结果凭证号")
    private String payResultRefNo;

    /**
     * 支付流水编号
     */
    @Column(name = "L_PAY_LOG_ID", columnDefinition = "INTEGER")
    @MetaData(value = "支付流水编号", comments = "支付流水编号")
    private Long payLogId;

    /**
     * 佣金记录编号
     */
    @Column(name = "L_RECORD_ID", columnDefinition = "INTEGER")
    @MetaData(value = "佣金记录编号", comments = "佣金记录编号")
    private Long recordId;

    /**
     * 佣金记录凭证号
     */
    @Column(name = "C_RECORD_REFNO", columnDefinition = "VARCHAR")
    @MetaData(value = "佣金记录凭证号", comments = "佣金记录凭证号")
    private String recordRefNo;

    /**
     * 佣金记录导入日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_IMPORT_DATE", columnDefinition = "CHAR")
    @MetaData(value = "佣金记录导入日期", comments = "佣金记录导入日期")
    private String importDate;

    /**
     * 佣金记录导入时间
     */
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    @Column(name = "T_IMPORT_TIME", columnDefinition = "CHAR")
    @MetaData(value = "佣金记录导入时间", comments = "佣金记录导入时间")
    private String importTime;

    /**
     * 支付流水号
     */
    @Column(name = "C_PAYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "支付流水号", comments = "支付流水号")
    private String payNo;

    /**
     * 支付日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_PAY_DATE", columnDefinition = "CHAR")
    @MetaData(value = "支付日期", comments = "支付日期")
    private String payDate;

    /**
     * 支付时间
     */
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    @Column(name = "T_PAY_TIME", columnDefinition = "CHAR")
    @MetaData(value = "支付时间", comments = "支付时间")
    private String payTime;

    /**
     * 支付金额
     */
    @Column(name = "F_PAY_BALANCE", columnDefinition = "DECIMAL")
    @MetaData(value = "支付金额", comments = "支付金额")
    private BigDecimal payBalance;

    /**
     * 收款银行
     */
    @Column(name = "C_PAY_TARGET_BANK", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行", comments = "收款银行")
    private String payTargetBank;

    /**
     * 收款银行名称
     */
    @Column(name = "C_PAY_TARGET_BANK_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行名称", comments = "收款银行名称")
    private String payTargetBankName;

    /**
     * 收款银行全称
     */
    @Column(name = "C_PAY_TARGET_BANK_FULLNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行全称", comments = "收款银行全称")
    private String payTargetBankFullName;

    /**
     * 收款银行账号
     */
    @Column(name = "C_PAY_TARGET_BANK_ACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行账号", comments = "收款银行账号")
    private String payTargetBankAccount;

    /**
     * 收款银行账户名
     */
    @Column(name = "C_PAY_TARGET_BANK_ACCO_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行账户名", comments = "收款银行账户名")
    private String payTargetBankAccountName;

    /**
     * 联系人手机号码
     */
    @Column(name = "C_PAY_TARGET_MOBILENO", columnDefinition = "VARCHAR")
    private String payTargetMobileNo;

    /**
     * 支付银行
     */
    @Column(name = "C_PAY_BANK", columnDefinition = "VARCHAR")
    @MetaData(value = "支付银行", comments = "支付银行")
    private String payBank;

    /**
     * 支付银行名称
     */
    @Column(name = "C_PAY_BANK_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "支付银行名称", comments = "支付银行名称")
    private String payBankName;

    /**
     * 支付银行全称
     */
    @Column(name = "C_PAY_BANK_FULLNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "支付银行全称", comments = "支付银行全称")
    private String payBankFullName;

    /**
     * 支付银行账号
     */
    @Column(name = "C_PAY_BANK_ACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "支付银行账号", comments = "支付银行账号")
    private String payBankAccount;

    /**
     * 支付银行账户名
     */
    @Column(name = "C_PAY_BANK_ACCO_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "支付银行账户名", comments = "支付银行账户名")
    private String payBankAccountName;

    /**
     * 支付结果
     */
    @Column(name = "C_PAY_RESULT", columnDefinition = "VARCHAR")
    @MetaData(value = "支付结果", comments = "支付结果")
    private String payResult;

    /**
     * 备注
     */
    @Column(name = "C_PAY_COMMENT", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注")
    private String payComment;

    /**
     * 业务状态
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务状态", comments = "业务状态")
    private String businStatus;

    /**
     * 业务状态
     */
    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务状态", comments = "业务状态")
    private String lastStatus;

    /**
     * 客户ID
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户ID", comments = "客户ID")
    private Long custNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户名称", comments = "客户名称")
    private String custName;

    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String operOrg;

    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regTime;

    @Column(name = "N_VERSION", columnDefinition = "INTEGER")
    @MetaData(value = "", comments = "")
    private Long version;

    private static final long serialVersionUID = 1493800886851L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(final String refNo) {
        this.refNo = refNo;
    }

    public Long getPayResultId() {
        return payResultId;
    }

    public void setPayResultId(final Long payResultId) {
        this.payResultId = payResultId;
    }

    public String getPayResultRefNo() {
        return payResultRefNo;
    }

    public void setPayResultRefNo(final String payResultRefNo) {
        this.payResultRefNo = payResultRefNo;
    }

    public Long getPayLogId() {
        return payLogId;
    }

    public void setPayLogId(final Long payLogId) {
        this.payLogId = payLogId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(final Long recordId) {
        this.recordId = recordId;
    }

    public String getRecordRefNo() {
        return recordRefNo;
    }

    public void setRecordRefNo(final String recordRefNo) {
        this.recordRefNo = recordRefNo;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(final String importDate) {
        this.importDate = importDate == null ? null : importDate.trim();
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(final String importTime) {
        this.importTime = importTime == null ? null : importTime.trim();
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(final String payNo) {
        this.payNo = payNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(final String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(final String payTime) {
        this.payTime = payTime == null ? null : payTime.trim();
    }

    public BigDecimal getPayBalance() {
        return payBalance;
    }

    public void setPayBalance(final BigDecimal payBalance) {
        this.payBalance = payBalance;
    }

    public String getPayTargetBank() {
        return payTargetBank;
    }

    public void setPayTargetBank(final String payTargetBank) {
        this.payTargetBank = payTargetBank;
    }

    public String getPayTargetBankName() {
        return payTargetBankName;
    }

    public void setPayTargetBankName(final String payTargetBankName) {
        this.payTargetBankName = payTargetBankName;
    }

    public String getPayTargetBankFullName() {
        return payTargetBankFullName;
    }

    public void setPayTargetBankFullName(final String payTargetBankFullName) {
        this.payTargetBankFullName = payTargetBankFullName;
    }

    public String getPayTargetBankAccount() {
        return payTargetBankAccount;
    }

    public void setPayTargetBankAccount(final String payTargetBankAccount) {
        this.payTargetBankAccount = payTargetBankAccount;
    }

    public String getPayTargetBankAccountName() {
        return payTargetBankAccountName;
    }

    public void setPayTargetBankAccountName(final String payTargetBankAccountName) {
        this.payTargetBankAccountName = payTargetBankAccountName;
    }

    public String getPayTargetMobileNo() {
        return payTargetMobileNo;
    }

    public void setPayTargetMobileNo(final String anPayTargetMobileNo) {
        payTargetMobileNo = anPayTargetMobileNo;
    }

    public String getPayBank() {
        return payBank;
    }

    public void setPayBank(final String payBank) {
        this.payBank = payBank;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(final String payBankName) {
        this.payBankName = payBankName;
    }

    public String getPayBankFullName() {
        return payBankFullName;
    }

    public void setPayBankFullName(final String payBankFullName) {
        this.payBankFullName = payBankFullName;
    }

    public String getPayBankAccount() {
        return payBankAccount;
    }

    public void setPayBankAccount(final String payBankAccount) {
        this.payBankAccount = payBankAccount;
    }

    public String getPayBankAccountName() {
        return payBankAccountName;
    }

    public void setPayBankAccountName(final String payBankAccountName) {
        this.payBankAccountName = payBankAccountName;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(final String payResult) {
        this.payResult = payResult;
    }

    public String getPayComment() {
        return payComment;
    }

    public void setPayComment(final String payComment) {
        this.payComment = payComment;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(final String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(final String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(final String custName) {
        this.custName = custName;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg;
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(final Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(final String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(final String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", refNo=").append(refNo);
        sb.append(", payResultId=").append(payResultId);
        sb.append(", payResultRefNo=").append(payResultRefNo);
        sb.append(", payLogId=").append(payLogId);
        sb.append(", recordId=").append(recordId);
        sb.append(", recordRefNo=").append(recordRefNo);
        sb.append(", importDate=").append(importDate);
        sb.append(", importTime=").append(importTime);
        sb.append(", payNo=").append(payNo);
        sb.append(", payDate=").append(payDate);
        sb.append(", payTime=").append(payTime);
        sb.append(", payBalance=").append(payBalance);
        sb.append(", payTargetBank=").append(payTargetBank);
        sb.append(", payTargetBankName=").append(payTargetBankName);
        sb.append(", payTargetBankFullName=").append(payTargetBankFullName);
        sb.append(", payTargetBankAccount=").append(payTargetBankAccount);
        sb.append(", payTargetBankAccountName=").append(payTargetBankAccountName);
        sb.append(", payTargetMobileNo=").append(payTargetMobileNo);
        sb.append(", payBank=").append(payBank);
        sb.append(", payBankName=").append(payBankName);
        sb.append(", payBankFullName=").append(payBankFullName);
        sb.append(", payBankAccount=").append(payBankAccount);
        sb.append(", payBankAccountName=").append(payBankAccountName);
        sb.append(", payResult=").append(payResult);
        sb.append(", payComment=").append(payComment);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        final CommissionPayResultRecord other = (CommissionPayResultRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRefNo() == null ? other.getRefNo() == null : this.getRefNo().equals(other.getRefNo()))
                && (this.getPayResultId() == null ? other.getPayResultId() == null
                        : this.getPayResultId().equals(other.getPayResultId()))
                && (this.getPayResultRefNo() == null ? other.getPayResultRefNo() == null
                        : this.getPayResultRefNo().equals(other.getPayResultRefNo()))
                && (this.getPayLogId() == null ? other.getPayLogId() == null
                        : this.getPayLogId().equals(other.getPayLogId()))
                && (this.getRecordId() == null ? other.getRecordId() == null
                        : this.getRecordId().equals(other.getRecordId()))
                && (this.getRecordRefNo() == null ? other.getRecordRefNo() == null
                        : this.getRecordRefNo().equals(other.getRecordRefNo()))
                && (this.getImportDate() == null ? other.getImportDate() == null
                        : this.getImportDate().equals(other.getImportDate()))
                && (this.getImportTime() == null ? other.getImportTime() == null
                        : this.getImportTime().equals(other.getImportTime()))
                && (this.getPayNo() == null ? other.getPayNo() == null : this.getPayNo().equals(other.getPayNo()))
                && (this.getPayDate() == null ? other.getPayDate() == null
                        : this.getPayDate().equals(other.getPayDate()))
                && (this.getPayTime() == null ? other.getPayTime() == null
                        : this.getPayTime().equals(other.getPayTime()))
                && (this.getPayBalance() == null ? other.getPayBalance() == null
                        : this.getPayBalance().equals(other.getPayBalance()))
                && (this.getPayTargetBank() == null ? other.getPayTargetBank() == null
                        : this.getPayTargetBank().equals(other.getPayTargetBank()))
                && (this.getPayTargetBankName() == null ? other.getPayTargetBankName() == null
                        : this.getPayTargetBankName().equals(other.getPayTargetBankName()))
                && (this.getPayTargetBankFullName() == null ? other.getPayTargetBankFullName() == null
                        : this.getPayTargetBankFullName().equals(other.getPayTargetBankFullName()))
                && (this.getPayTargetBankAccount() == null ? other.getPayTargetBankAccount() == null
                        : this.getPayTargetBankAccount().equals(other.getPayTargetBankAccount()))
                && (this.getPayTargetBankAccountName() == null ? other.getPayTargetBankAccountName() == null
                        : this.getPayTargetBankAccountName().equals(other.getPayTargetBankAccountName()))
                && (this.getPayTargetMobileNo() == null ? other.getPayTargetMobileNo() == null
                        : this.getPayTargetMobileNo().equals(other.getPayTargetMobileNo()))
                && (this.getPayBank() == null ? other.getPayBank() == null
                        : this.getPayBank().equals(other.getPayBank()))
                && (this.getPayBankName() == null ? other.getPayBankName() == null
                        : this.getPayBankName().equals(other.getPayBankName()))
                && (this.getPayBankFullName() == null ? other.getPayBankFullName() == null
                        : this.getPayBankFullName().equals(other.getPayBankFullName()))
                && (this.getPayBankAccount() == null ? other.getPayBankAccount() == null
                        : this.getPayBankAccount().equals(other.getPayBankAccount()))
                && (this.getPayBankAccountName() == null ? other.getPayBankAccountName() == null
                        : this.getPayBankAccountName().equals(other.getPayBankAccountName()))
                && (this.getPayResult() == null ? other.getPayResult() == null
                        : this.getPayResult().equals(other.getPayResult()))
                && (this.getPayComment() == null ? other.getPayComment() == null
                        : this.getPayComment().equals(other.getPayComment()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null
                        : this.getLastStatus().equals(other.getLastStatus()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null
                        : this.getCustName().equals(other.getCustName()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null
                        : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null
                        : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null
                        : this.getRegTime().equals(other.getRegTime()))
                && (this.getVersion() == null ? other.getVersion() == null
                        : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRefNo() == null) ? 0 : getRefNo().hashCode());
        result = prime * result + ((getPayResultId() == null) ? 0 : getPayResultId().hashCode());
        result = prime * result + ((getPayResultRefNo() == null) ? 0 : getPayResultRefNo().hashCode());
        result = prime * result + ((getPayLogId() == null) ? 0 : getPayLogId().hashCode());
        result = prime * result + ((getRecordId() == null) ? 0 : getRecordId().hashCode());
        result = prime * result + ((getRecordRefNo() == null) ? 0 : getRecordRefNo().hashCode());
        result = prime * result + ((getImportDate() == null) ? 0 : getImportDate().hashCode());
        result = prime * result + ((getImportTime() == null) ? 0 : getImportTime().hashCode());
        result = prime * result + ((getPayNo() == null) ? 0 : getPayNo().hashCode());
        result = prime * result + ((getPayDate() == null) ? 0 : getPayDate().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getPayBalance() == null) ? 0 : getPayBalance().hashCode());
        result = prime * result + ((getPayTargetBank() == null) ? 0 : getPayTargetBank().hashCode());
        result = prime * result + ((getPayTargetBankName() == null) ? 0 : getPayTargetBankName().hashCode());
        result = prime * result + ((getPayTargetBankFullName() == null) ? 0 : getPayTargetBankFullName().hashCode());
        result = prime * result + ((getPayTargetBankAccount() == null) ? 0 : getPayTargetBankAccount().hashCode());
        result = prime * result
                + ((getPayTargetBankAccountName() == null) ? 0 : getPayTargetBankAccountName().hashCode());
        result = prime * result + ((getPayTargetMobileNo() == null) ? 0 : getPayTargetMobileNo().hashCode());
        result = prime * result + ((getPayBank() == null) ? 0 : getPayBank().hashCode());
        result = prime * result + ((getPayBankName() == null) ? 0 : getPayBankName().hashCode());
        result = prime * result + ((getPayBankFullName() == null) ? 0 : getPayBankFullName().hashCode());
        result = prime * result + ((getPayBankAccount() == null) ? 0 : getPayBankAccount().hashCode());
        result = prime * result + ((getPayBankAccountName() == null) ? 0 : getPayBankAccountName().hashCode());
        result = prime * result + ((getPayResult() == null) ? 0 : getPayResult().hashCode());
        result = prime * result + ((getPayComment() == null) ? 0 : getPayComment().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    /**
     * @param anOperator
     */
    public void init(final CustOperatorInfo anOperator) {
        this.setId(SerialGenerator.getLongValue("CommissionPayResultRecord.id"));

        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());

        if (anOperator != null) {
            this.setRegOperId(anOperator.getId());
            this.setRegOperName(anOperator.getName());
        }
    }
}