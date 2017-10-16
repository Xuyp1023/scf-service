package com.betterjr.modules.supplieroffer.entity;

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
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_receivable_request_log")
public class ScfReceivableRequestLog implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4319952225167483544L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 融资编号
     */
    @Column(name = "C_EQUITYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "融资编号", comments = "融资编号")
    private String equityNo;

    /**
     * 操作日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "操作日期", comments = "操作日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 操作时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作时间", comments = "操作时间")
    private String modiTime;

    /**
     * 操作员Id
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "操作员Id", comments = "操作员Id")
    private Long modiOperId;

    /**
     * 操作员名称
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员名称", comments = "操作员名称")
    private String modiOperName;

    /**
     * 操作步骤信息
     */
    @Column(name = "C_MODI_MESSAGE", columnDefinition = "VARCHAR")
    @MetaData(value = "操作步骤信息", comments = "操作步骤信息")
    private String modiMessage;

    /**
     * 操作员所属公司
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "操作员所属公司", comments = "操作员所属公司")
    private Long custNo;

    /**
     * 所属公司名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "所属公司名称", comments = "所属公司名称")
    private String custName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getEquityNo() {
        return this.equityNo;
    }

    public void setEquityNo(String anEquityNo) {
        this.equityNo = anEquityNo;
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

    public String getModiMessage() {
        return this.modiMessage;
    }

    public void setModiMessage(String anModiMessage) {
        this.modiMessage = anModiMessage;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.equityNo == null) ? 0 : this.equityNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiMessage == null) ? 0 : this.modiMessage.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfReceivableRequestLog other = (ScfReceivableRequestLog) obj;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.equityNo == null) {
            if (other.equityNo != null) return false;
        } else if (!this.equityNo.equals(other.equityNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        } else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiMessage == null) {
            if (other.modiMessage != null) return false;
        } else if (!this.modiMessage.equals(other.modiMessage)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        } else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        } else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        } else if (!this.modiTime.equals(other.modiTime)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfReceivableRequestLog [id=" + this.id + ", equityNo=" + this.equityNo + ", modiDate=" + this.modiDate
                + ", modiTime=" + this.modiTime + ", modiOperId=" + this.modiOperId + ", modiOperName="
                + this.modiOperName + ", modiMessage=" + this.modiMessage + ", custNo=" + this.custNo + ", custName="
                + this.custName + "]";
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo) {

        BTAssert.notNull(anOperatorInfo, "无法获取登录信息,操作失败");
        this.setId(SerialGenerator.getLongValue("ScfReceivableRequestLog.id"));
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        this.modiOperId = anOperatorInfo.getId();
        this.modiOperName = anOperatorInfo.getName();

    }

}
