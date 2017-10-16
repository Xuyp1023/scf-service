package com.betterjr.modules.ledger.entity;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_contract_ledger_recode")
public class ContractLedgerRecode implements BetterjrEntity {
    @Id
    @Column(name = "id", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "l_contractId", columnDefinition = "INTEGER")
    private Long contractId;

    @Column(name = "L_OPERID", columnDefinition = "INTEGER")
    private Long operId;

    @Column(name = "C_OPERNAME", columnDefinition = "VARCHAR")
    private String operName;

    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_OPER_DATE", columnDefinition = "VARCHAR")
    private String operDate;

    @Column(name = "D_OPER_TIME", columnDefinition = "VARCHAR")
    private String operTime;

    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    private String businStatus;

    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    private static final long serialVersionUID = 1482311479890L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getOperId() {
        return operId;
    }

    public void setOperId(Long operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", contractId=").append(contractId);
        sb.append(", operId=").append(operId);
        sb.append(", operName=").append(operName);
        sb.append(", operDate=").append(operDate);
        sb.append(", operTime=").append(operTime);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", operOrg=").append(operOrg);
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
        ContractLedgerRecode other = (ContractLedgerRecode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getContractId() == null ? other.getContractId() == null
                        : this.getContractId().equals(other.getContractId()))
                && (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
                && (this.getOperName() == null ? other.getOperName() == null
                        : this.getOperName().equals(other.getOperName()))
                && (this.getOperDate() == null ? other.getOperDate() == null
                        : this.getOperDate().equals(other.getOperDate()))
                && (this.getOperTime() == null ? other.getOperTime() == null
                        : this.getOperTime().equals(other.getOperTime()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getOperOrg() == other.getOperOrg());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperDate() == null) ? 0 : getOperDate().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        return result;
    }

    public void initValue(Long anContractId, String anBusinStatus) {
        this.id = SerialGenerator.getLongValue("ContractLedgerRecode.id");
        this.contractId = anContractId;
        CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (operator != null) {
            this.operId = operator.getId();
            this.operName = operator.getName();
            this.operOrg = operator.getOperOrg();
        }
        this.operDate = BetterDateUtils.getNumDate();
        this.operTime = BetterDateUtils.getNumTime();
        this.businStatus = anBusinStatus;
    }
}