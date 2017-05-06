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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_pay_result")
public class CommissionPayResult implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 凭证号
     */
    @Column(name = "C_REFNO",  columnDefinition="VARCHAR" )
    @MetaData( value="凭证号", comments = "凭证号")
    private String refNo;

    /**
     * 支付日期
     */
    @Column(name = "D_PAY_DATE",  columnDefinition="CHAR" )
    @MetaData( value="支付日期", comments = "支付日期")
    private String payDate;

    /**
     * 支付时间
     */
    @Column(name = "T_PAY_TIME",  columnDefinition="CHAR" )
    @MetaData( value="支付时间", comments = "支付时间")
    private String payTime;

    /**
     * 导入日期
     */
    @Column(name = "D_IMPORT_DATE",  columnDefinition="CHAR" )
    @MetaData( value="导入日期", comments = "导入日期")
    private String importDate;

    /**
     * 导入时间
     */
    @Column(name = "T_IMPORT_TIME",  columnDefinition="CHAR" )
    @MetaData( value="导入时间", comments = "导入时间")
    private String importTime;

    /**
     * 总金额
     */
    @Column(name = "F_TOTAL_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="总金额", comments = "总金额")
    private BigDecimal totalBalance;

    /**
     * 总笔数
     */
    @Column(name = "N_TOTAL_AMOUNT",  columnDefinition="INTEGER" )
    @MetaData( value="总笔数", comments = "总笔数")
    private Long totalAmount;

    /**
     * 支付总金额
     */
    @Column(name = "F_PAY_TOTAL_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付总金额", comments = "支付总金额")
    private BigDecimal payTotalBalance;

    /**
     * 支付总笔数
     */
    @Column(name = "N_PAY_TOTAL_AMOUNT",  columnDefinition="INTEGER" )
    @MetaData( value="支付总笔数", comments = "支付总笔数")
    private Long payTotalAmount;

    /**
     * 支付成功金额
     */
    @Column(name = "F_PAY_SUCCESS_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付成功金额", comments = "支付成功金额")
    private BigDecimal paySuccessBalance;

    /**
     * 支付成功笔数
     */
    @Column(name = "N_PAY_SUCCESS_AMOUNT",  columnDefinition="INTEGER" )
    @MetaData( value="支付成功笔数", comments = "支付成功笔数")
    private Long paySuccessAmount;

    /**
     * 支付失败金额
     */
    @Column(name = "F_PAY_FAILURE_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="支付失败金额", comments = "支付失败金额")
    private BigDecimal payFailureBalance;

    /**
     * 支付失败笔数
     */
    @Column(name = "N_PAY_FAILURE_AMOUNT",  columnDefinition="INTEGER" )
    @MetaData( value="支付失败笔数", comments = "支付失败笔数")
    private Long payFailureAmount;

    /**
     * 所属公司
     */
    @Column(name = "L_OWN_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="所属公司", comments = "所属公司")
    private Long ownCustNo;

    /**
     * 所属公司名称
     */
    @Column(name = "C_OWN_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="所属公司名称", comments = "所属公司名称")
    private String ownCustName;

    /**
     * 所属机构
     */
    @Column(name = "C_OWN_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="所属机构", comments = "所属机构")
    private String ownOperOrg;

    /**
     * 客户ID
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户ID", comments = "客户ID")
    private Long custNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户名称", comments = "客户名称")
    private String custName;

    /**
     * 机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="机构", comments = "机构")
    private String operOrg;

    /**
     * 业务状态
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="业务状态", comments = "业务状态")
    private String businStatus;

    /**
     * 业务状态
     */
    @Column(name = "C_LAST_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="业务状态", comments = "业务状态")
    private String lastStatus;

    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="", comments = "")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiOperName;

    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiDate;

    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiTime;

    @Column(name = "N_VERSION",  columnDefinition="INTEGER" )
    @MetaData( value="", comments = "")
    private Long version;

    private static final long serialVersionUID = 1119685691057575307L;

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

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(final BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayTotalBalance() {
        return payTotalBalance;
    }

    public void setPayTotalBalance(final BigDecimal payTotalBalance) {
        this.payTotalBalance = payTotalBalance;
    }

    public Long getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(final Long payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public BigDecimal getPaySuccessBalance() {
        return paySuccessBalance;
    }

    public void setPaySuccessBalance(final BigDecimal paySuccessBalance) {
        this.paySuccessBalance = paySuccessBalance;
    }

    public Long getPaySuccessAmount() {
        return paySuccessAmount;
    }

    public void setPaySuccessAmount(final Long paySuccessAmount) {
        this.paySuccessAmount = paySuccessAmount;
    }

    public BigDecimal getPayFailureBalance() {
        return payFailureBalance;
    }

    public void setPayFailureBalance(final BigDecimal payFailureBalance) {
        this.payFailureBalance = payFailureBalance;
    }

    public Long getPayFailureAmount() {
        return payFailureAmount;
    }

    public void setPayFailureAmount(final Long payFailureAmount) {
        this.payFailureAmount = payFailureAmount;
    }

    public Long getOwnCustNo() {
        return ownCustNo;
    }

    public void setOwnCustNo(final Long ownCustNo) {
        this.ownCustNo = ownCustNo;
    }

    public String getOwnCustName() {
        return ownCustName;
    }

    public void setOwnCustName(final String ownCustName) {
        this.ownCustName = ownCustName;
    }

    public String getOwnOperOrg() {
        return ownOperOrg;
    }

    public void setOwnOperOrg(final String ownOperOrg) {
        this.ownOperOrg = ownOperOrg;
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

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(final Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(final String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(final String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
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
        sb.append(", payDate=").append(payDate);
        sb.append(", payTime=").append(payTime);
        sb.append(", importDate=").append(importDate);
        sb.append(", importTime=").append(importTime);
        sb.append(", totalBalance=").append(totalBalance);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", payTotalBalance=").append(payTotalBalance);
        sb.append(", payTotalAmount=").append(payTotalAmount);
        sb.append(", paySuccessBalance=").append(paySuccessBalance);
        sb.append(", paySuccessAmount=").append(paySuccessAmount);
        sb.append(", payFailureBalance=").append(payFailureBalance);
        sb.append(", payFailureAmount=").append(payFailureAmount);
        sb.append(", ownCustNo=").append(ownCustNo);
        sb.append(", ownCustName=").append(ownCustName);
        sb.append(", ownOperOrg=").append(ownOperOrg);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
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
        final CommissionPayResult other = (CommissionPayResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRefNo() == null ? other.getRefNo() == null : this.getRefNo().equals(other.getRefNo()))
                && (this.getPayDate() == null ? other.getPayDate() == null : this.getPayDate().equals(other.getPayDate()))
                && (this.getPayTime() == null ? other.getPayTime() == null : this.getPayTime().equals(other.getPayTime()))
                && (this.getImportDate() == null ? other.getImportDate() == null : this.getImportDate().equals(other.getImportDate()))
                && (this.getImportTime() == null ? other.getImportTime() == null : this.getImportTime().equals(other.getImportTime()))
                && (this.getTotalBalance() == null ? other.getTotalBalance() == null : this.getTotalBalance().equals(other.getTotalBalance()))
                && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
                && (this.getPayTotalBalance() == null ? other.getPayTotalBalance() == null : this.getPayTotalBalance().equals(other.getPayTotalBalance()))
                && (this.getPayTotalAmount() == null ? other.getPayTotalAmount() == null : this.getPayTotalAmount().equals(other.getPayTotalAmount()))
                && (this.getPaySuccessBalance() == null ? other.getPaySuccessBalance() == null : this.getPaySuccessBalance().equals(other.getPaySuccessBalance()))
                && (this.getPaySuccessAmount() == null ? other.getPaySuccessAmount() == null : this.getPaySuccessAmount().equals(other.getPaySuccessAmount()))
                && (this.getPayFailureBalance() == null ? other.getPayFailureBalance() == null : this.getPayFailureBalance().equals(other.getPayFailureBalance()))
                && (this.getPayFailureAmount() == null ? other.getPayFailureAmount() == null : this.getPayFailureAmount().equals(other.getPayFailureAmount()))
                && (this.getOwnCustNo() == null ? other.getOwnCustNo() == null : this.getOwnCustNo().equals(other.getOwnCustNo()))
                && (this.getOwnCustName() == null ? other.getOwnCustName() == null : this.getOwnCustName().equals(other.getOwnCustName()))
                && (this.getOwnOperOrg() == null ? other.getOwnOperOrg() == null : this.getOwnOperOrg().equals(other.getOwnOperOrg()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(other.getLastStatus()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRefNo() == null) ? 0 : getRefNo().hashCode());
        result = prime * result + ((getPayDate() == null) ? 0 : getPayDate().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getImportDate() == null) ? 0 : getImportDate().hashCode());
        result = prime * result + ((getImportTime() == null) ? 0 : getImportTime().hashCode());
        result = prime * result + ((getTotalBalance() == null) ? 0 : getTotalBalance().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getPayTotalBalance() == null) ? 0 : getPayTotalBalance().hashCode());
        result = prime * result + ((getPayTotalAmount() == null) ? 0 : getPayTotalAmount().hashCode());
        result = prime * result + ((getPaySuccessBalance() == null) ? 0 : getPaySuccessBalance().hashCode());
        result = prime * result + ((getPaySuccessAmount() == null) ? 0 : getPaySuccessAmount().hashCode());
        result = prime * result + ((getPayFailureBalance() == null) ? 0 : getPayFailureBalance().hashCode());
        result = prime * result + ((getPayFailureAmount() == null) ? 0 : getPayFailureAmount().hashCode());
        result = prime * result + ((getOwnCustNo() == null) ? 0 : getOwnCustNo().hashCode());
        result = prime * result + ((getOwnCustName() == null) ? 0 : getOwnCustName().hashCode());
        result = prime * result + ((getOwnOperOrg() == null) ? 0 : getOwnOperOrg().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    /**
     * @param anOperator
     */
    public void init(final CustOperatorInfo anOperator) {
        this.setId(SerialGenerator.getLongValue("CommissionPayResult.id"));

        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());

        this.setModiDate(BetterDateUtils.getNumDate());
        this.setModiTime(BetterDateUtils.getNumTime());

        if (anOperator != null) {
            this.setRegOperId(anOperator.getId());
            this.setRegOperName(anOperator.getName());

            this.setModiOperId(anOperator.getId());
            this.setModiOperName(anOperator.getName());
        }
    }

    /**
     * @param anOperator
     */
    public void modify(final CustOperatorInfo anOperator) {
        this.setModiDate(BetterDateUtils.getNumDate());
        this.setModiTime(BetterDateUtils.getNumTime());
        if (anOperator != null) {
            this.setModiOperId(anOperator.getId());
            this.setModiOperName(anOperator.getName());
        }
    }
}