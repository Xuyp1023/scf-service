package com.betterjr.modules.credit.entity;

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
import com.betterjr.modules.credit.constant.CreditConstants;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_CREDIT_DETAIL")
public class ScfCreditDetail implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户名称", comments = "客户名称")
    private String custName;

    /**
     * 变动日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_OCCUPY_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "变动日期", comments = "变动日期")
    private String occupyDate;

    /**
     * 变动时间
     */
    @Column(name = "T_OCCUPY_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "变动时间", comments = "变动时间")
    private String occupyTime;

    /**
     * 变动金额
     */
    @Column(name = "F_BALANCE", columnDefinition = "DOUBLE")
    @MetaData(value = "变动金额", comments = "变动金额")
    private BigDecimal balance;

    /**
     * 方向(0:收;1:支;)
     */
    @Column(name = "C_DIRECTION", columnDefinition = "VARCHAR")
    @MetaData(value = "方向(0:收;1:支;)", comments = "方向(0:收;1:支;)")
    private String direction;

    /**
     * 业务类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)
     */
    @Column(name = "C_BUSIN_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "业务类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)", comments = "业务类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)")
    private String businFlag;

    /**
     * 业务流水号
     */
    @Column(name = "L_BUSINID", columnDefinition = "INTEGER")
    @MetaData(value = "业务流水号", comments = "业务流水号")
    private Long businId;

    /**
     * 业务单据号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "业务单据号", comments = "业务单据号")
    private String requestNo;

    /**
     * 状态(0:已完成;1:冻结中;)
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态(0:已完成;1:冻结中;)", comments = "状态(0:已完成;1:冻结中;)")
    private String businStatus;

    /**
     * 所属授信额度类型流水号
     */
    @Column(name = "L_CREDITID", columnDefinition = "INTEGER")
    @MetaData(value = "授信额度流水号", comments = "授信额度流水号")
    private Long creditId;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注")
    private String description;

    private static final long serialVersionUID = 1467703726396L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.custName = custName;
    }

    public String getOccupyDate() {
        return occupyDate;
    }

    public void setOccupyDate(String changeDate) {
        this.occupyDate = changeDate == null ? null : changeDate.trim();
    }

    public String getOccupyTime() {
        return occupyTime;
    }

    public void setOccupyTime(String occupyTime) {
        this.occupyTime = occupyTime == null ? null : occupyTime.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag == null ? null : businFlag.trim();
    }

    public Long getBusinId() {
        return businId;
    }

    public void setBusinId(Long businId) {
        this.businId = businId;
    }

    public String getRequestNo() {
        return this.requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public Long getCreditId() {
        return creditId;
    }

    public void setlCreditId(Long lCreditid) {
        this.creditId = lCreditid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", occupyDate=").append(occupyDate);
        sb.append(", occupyTime=").append(occupyTime);
        sb.append(", balance=").append(balance);
        sb.append(", direction=").append(direction);
        sb.append(", businFlag=").append(businFlag);
        sb.append(", businId=").append(businId);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", creditId=").append(creditId);
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
        ScfCreditDetail other = (ScfCreditDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getOccupyDate() == null ? other.getOccupyDate() == null : this.getOccupyDate().equals(other.getOccupyDate()))
                && (this.getOccupyTime() == null ? other.getOccupyTime() == null : this.getOccupyTime().equals(other.getOccupyTime()))
                && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
                && (this.getDirection() == null ? other.getDirection() == null : this.getDirection().equals(other.getDirection()))
                && (this.getBusinFlag() == null ? other.getBusinFlag() == null : this.getBusinFlag().equals(other.getBusinFlag()))
                && (this.getBusinId() == null ? other.getBusinId() == null : this.getBusinId().equals(other.getBusinId()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getCreditId() == null ? other.getCreditId() == null : this.getCreditId().equals(other.getCreditId()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getOccupyDate() == null) ? 0 : getOccupyDate().hashCode());
        result = prime * result + ((getOccupyTime() == null) ? 0 : getOccupyTime().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getBusinFlag() == null) ? 0 : getBusinFlag().hashCode());
        result = prime * result + ((getBusinId() == null) ? 0 : getBusinId().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getCreditId() == null) ? 0 : getCreditId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    private void init() {
        this.id = SerialGenerator.getLongValue("ScfCreditDetail.id");
        this.occupyDate = BetterDateUtils.getNumDate();
        this.occupyTime = BetterDateUtils.getNumTime();
    }

    public void initAddValue(Long anCustNo, BigDecimal anCreditLimit, Long anCreditId) {
        init();
        this.custNo = anCustNo;
        this.balance = anCreditLimit;
        this.businFlag = "";
        this.businId = 0l;
        this.businStatus = CreditConstants.CREDIT_CHANGE_STATUS_DONE;// 状态(0:已完成;1:冻结中;)
        this.creditId = anCreditId;
        this.description = "额度初始";
    }

    public void initModifyValue(Long anCustNo, String anCustName, Long anCreditId) {
        init();
        this.custNo = anCustNo;
        this.custName = anCustName;
        this.businFlag = "";
        this.businId = 0l;
        this.businStatus = CreditConstants.CREDIT_CHANGE_STATUS_DONE;// 状态(0:已完成;1:冻结中;)
        this.creditId = anCreditId;
        this.description = "额度调整";
    }

    public void initOccupyValue(ScfCreditInfo anCreditInfo, Long anCreditId) {
        init();
        initOccupyAndReleaseValue(anCreditInfo, anCreditId);
        this.direction = CreditConstants.CREDIT_DIRECTION_EXPEND;// 方向：0-收;1-支;
    }

    public void initReleaseValue(ScfCreditInfo anCreditInfo, Long anCreditId) {
        init();
        initOccupyAndReleaseValue(anCreditInfo, anCreditId);
        this.direction = CreditConstants.CREDIT_DIRECTION_INCOME;// 方向：0-收;1-支;
    }

    private void initOccupyAndReleaseValue(ScfCreditInfo anCreditInfo, Long anCreditId) {
        this.businStatus = CreditConstants.CREDIT_CHANGE_STATUS_DONE;// 状态(0:已完成;1:冻结中;)
        this.custNo = anCreditInfo.getCustNo();
        this.balance = anCreditInfo.getBalance();
        this.businFlag = anCreditInfo.getBusinFlag();
        this.businId = anCreditInfo.getBusinId();
        this.requestNo = anCreditInfo.getRequestNo();
        this.creditId = anCreditId;
    }

}