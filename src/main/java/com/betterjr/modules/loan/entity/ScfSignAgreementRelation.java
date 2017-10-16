package com.betterjr.modules.loan.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_SIGN_AGREEMENT_RELATION")
public class ScfSignAgreementRelation implements BetterjrEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 3999847880679327457L;

    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    @Column(name = "C_REQUEST_NO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请编号", comments = "申请编号")
    private String requestNo;

    @Column(name = "L_AGREEMENT_ID", columnDefinition = "VARCHAR")
    @MetaData(value = "合同id", comments = "合同id")
    private Long agreementId;

    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "合同类型", comments = "合同类型")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((agreementId == null) ? 0 : agreementId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((requestNo == null) ? 0 : requestNo.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfSignAgreementRelation other = (ScfSignAgreementRelation) obj;
        if (agreementId == null) {
            if (other.agreementId != null) return false;
        } else if (!agreementId.equals(other.agreementId)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (requestNo == null) {
            if (other.requestNo != null) return false;
        } else if (!requestNo.equals(other.requestNo)) return false;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfSignAgreementRelation [id=" + id + ", requestNo=" + requestNo + ", agreementId=" + agreementId
                + ", type=" + type + "]";
    }

    public void init() {
        this.id = SerialGenerator.getLongValue("ScfSignAgreementRelation.id");
    }
}