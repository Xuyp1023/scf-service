package com.betterjr.modules.agreement.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_AGREEMENT_STANDARD")
public class ScfAgreementStandard implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="BIGINT" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 标准合同编号
     */
    @Column(name = "C_AGREEMENT_STANDARD_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="标准合同编号", comments = "标准合同编号")
    private String agreementStandardNo;

    /**
     * 合同类型id
     */
    @Column(name = "L_AGREEMENT_TYPE_ID",  columnDefinition="BIGINT" )
    @MetaData( value="合同类型id", comments = "合同类型id")
    private Long agreementTypeId;

    /**
     * 标准合同名称
     */
    @Column(name = "C_AGREEMENT_STANDARD_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="标准合同名称", comments = "标准合同名称")
    private String agreementStandardName;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 状态  0登记 1生效
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="CHAR" )
    @MetaData( value="状态  0登记 1生效", comments = "状态  0登记 1生效")
    private String businStatus;

    /**
     * 创建人(操作员)ID号
     */
    @Column(name = "L_REG_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    private Long regOperId;

    /**
     * 创建人(操作员)姓名
     */
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 修改人(操作员)ID号
     */
    @Column(name = "L_MODI_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="修改人(操作员)ID号", comments = "修改人(操作员)ID号")
    private Long modiOperId;

    /**
     * 修改人(操作员)姓名
     */
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="修改人(操作员)姓名", comments = "修改人(操作员)姓名")
    private String modiOperName;

    /**
     * 修改日期
     */
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 审核人(操作员)ID号
     */
    @Column(name = "L_AUDIT_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="审核人(操作员)ID号", comments = "审核人(操作员)ID号")
    private Long auditOperId;

    /**
     * 审核人(操作员)姓名
     */
    @Column(name = "C_AUDIT_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="审核人(操作员)姓名", comments = "审核人(操作员)姓名")
    private String auditOperName;

    /**
     * 审核日期
     */
    @Column(name = "D_AUDIT_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="审核日期", comments = "审核日期")
    private String auditDate;

    /**
     * 审核时间
     */
    @Column(name = "T_AUDIT_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="审核时间", comments = "审核时间")
    private String auditTime;

    @Transient
    private String agreementTypeName;
    
    private static final long serialVersionUID = 1482304589011L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementStandardNo() {
        return agreementStandardNo;
    }

    public void setAgreementStandardNo(String agreementStandardNo) {
        this.agreementStandardNo = agreementStandardNo;
    }

    public Long getAgreementTypeId() {
        return agreementTypeId;
    }

    public void setAgreementTypeId(Long agreementTypeId) {
        this.agreementTypeId = agreementTypeId;
    }

    public String getAgreementStandardName() {
        return agreementStandardName;
    }

    public void setAgreementStandardName(String agreementStandardName) {
        this.agreementStandardName = agreementStandardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime;
    }

    public Long getAuditOperId() {
        return auditOperId;
    }

    public void setAuditOperId(Long auditOperId) {
        this.auditOperId = auditOperId;
    }

    public String getAuditOperName() {
        return auditOperName;
    }

    public void setAuditOperName(String auditOperName) {
        this.auditOperName = auditOperName;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAgreementTypeName() {
        return this.agreementTypeName;
    }

    public void setAgreementTypeName(String anAgreementTypeName) {
        this.agreementTypeName = anAgreementTypeName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", agreementStandardNo=").append(agreementStandardNo);
        sb.append(", agreementTypeId=").append(agreementTypeId);
        sb.append(", agreementStandardName=").append(agreementStandardName);
        sb.append(", description=").append(description);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", auditOperId=").append(auditOperId);
        sb.append(", auditOperName=").append(auditOperName);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", auditTime=").append(auditTime);
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
        ScfAgreementStandard other = (ScfAgreementStandard) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgreementStandardNo() == null ? other.getAgreementStandardNo() == null : this.getAgreementStandardNo().equals(other.getAgreementStandardNo()))
            && (this.getAgreementTypeId() == null ? other.getAgreementTypeId() == null : this.getAgreementTypeId().equals(other.getAgreementTypeId()))
            && (this.getAgreementStandardName() == null ? other.getAgreementStandardName() == null : this.getAgreementStandardName().equals(other.getAgreementStandardName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getAuditOperId() == null ? other.getAuditOperId() == null : this.getAuditOperId().equals(other.getAuditOperId()))
            && (this.getAuditOperName() == null ? other.getAuditOperName() == null : this.getAuditOperName().equals(other.getAuditOperName()))
            && (this.getAuditDate() == null ? other.getAuditDate() == null : this.getAuditDate().equals(other.getAuditDate()))
            && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgreementStandardNo() == null) ? 0 : getAgreementStandardNo().hashCode());
        result = prime * result + ((getAgreementTypeId() == null) ? 0 : getAgreementTypeId().hashCode());
        result = prime * result + ((getAgreementStandardName() == null) ? 0 : getAgreementStandardName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getAuditOperId() == null) ? 0 : getAuditOperId().hashCode());
        result = prime * result + ((getAuditOperName() == null) ? 0 : getAuditOperName().hashCode());
        result = prime * result + ((getAuditDate() == null) ? 0 : getAuditDate().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
        return result;
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo) {
        this.id = SerialGenerator.getLongValue("ScfAgreementStandard.id");
        this.agreementStandardNo = this.id.toString();
        //状态  0登记 1生效 2停用
        this.businStatus = "0";
        if (null != anOperatorInfo) {
            this.regOperId = anOperatorInfo.getId();
            this.regOperName = anOperatorInfo.getName();
            this.operOrg = anOperatorInfo.getOperOrg();
        }
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getTime();
    }
    
    public void initModifyValue(CustOperatorInfo anOperatorInfo) {
        if (null != anOperatorInfo) {
            this.modiOperId = anOperatorInfo.getId();
            this.modiOperName = anOperatorInfo.getName();
        }
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }

    public void initAuditValue(CustOperatorInfo anOperatorInfo) {
        this.businStatus = "1";
        this.auditDate = BetterDateUtils.getNumDate();
        this.auditTime = BetterDateUtils.getNumTime();
        if (null != anOperatorInfo) {
            this.auditOperId = anOperatorInfo.getId();
            this.auditOperName = anOperatorInfo.getName();
        }
    }
    
    public void initDisableValue(CustOperatorInfo anOperatorInfo) {
        this.businStatus = "2";
        this.auditDate = "";
        this.auditTime = "";
        if (null != anOperatorInfo) {
            this.auditOperId = anOperatorInfo.getId();
            this.auditOperName = anOperatorInfo.getName();
        }
    }
}