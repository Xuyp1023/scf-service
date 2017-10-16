package com.betterjr.modules.supplychain.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.BetterBaseEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_core_corp")
public class CustCoreCorpInfo extends BetterBaseEntity implements BetterjrEntity {
    /**
     * ID
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "ID", comments = "ID")
    private Long id;

    /**
     * 核心企业客户号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业客户号", comments = "核心企业客户号")
    private Long coreCustNo;

    /**
     * 下属分公司在平台的客户号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "下属分公司在平台的客户号", comments = "下属分公司在平台的客户号")
    private Long custNo;

    /**
     * 下属企业在平台的名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "下属企业在平台的名称", comments = "下属企业在平台的名称")
    private String custName;

    /**
     * 下属企业在资金管理系统中的ID
     */
    @Column(name = "C_CORPID", columnDefinition = "VARCHAR")
    @MetaData(value = "下属企业在资金管理系统中的ID", comments = "下属企业在资金管理系统中的ID")
    private String corpNo;

    /**
     * 下属企业在资金管理系统中编号
     */
    @Column(name = "C_CORPNO", columnDefinition = "VARCHAR")
    @MetaData(value = "下属企业在资金管理系统中编号", comments = "下属企业在资金管理系统中编号")
    private String corpCode;

    /**
     * 下属企业在资金管理系统中的名称
     */
    @Column(name = "C_CORPNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "下属企业在资金管理系统中的名称", comments = "下属企业在资金管理系统中的名称")
    private String corpName;

    /**
     * 下属企业在资金管理系统中的组织机构代码
     */
    @Column(name = "C_CORP_BUSI_LICENCE", columnDefinition = "VARCHAR")
    @MetaData(value = "下属企业在资金管理系统中的组织机构代码", comments = "下属企业在资金管理系统中的组织机构代码")
    private String busiLicence;

    /**
     * 上级机构ID
     */
    @Column(name = "C_PARENT_CORPID", columnDefinition = "VARCHAR")
    @MetaData(value = "上级机构ID", comments = "上级机构ID")
    private String parentCorpNo;

    /**
     * 登记的操作员ID
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "登记的操作员ID", comments = "登记的操作员ID")
    private Long regOperId;

    /**
     * 状态，0删除，1正常
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0删除，1正常")
    private String businStatus;

    /**
     * 登记的操作员名称
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "登记的操作员名称", comments = "登记的操作员名称")
    private String regOperName;

    /**
     * 登记日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 登记时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "登记时间", comments = "登记时间")
    private String regTime;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "编辑操作员编码", comments = "编辑操作员编码")
    private Long modiOperId;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "编辑操作员名字", comments = "编辑操作员名字")
    private String modiOperName;

    /**
     * 修改日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 资金系统内部id
     */
    @JsonIgnore
    @Column(name = "C_CORPID", columnDefinition = "VARCHAR")
    @MetaData(value = "资金系统内部id", comments = "资金系统内部id")
    private String corpId;

    private static final long serialVersionUID = 8300178519489076446L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(final Long coreCustNo) {
        this.coreCustNo = coreCustNo;
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
        this.custName = custName == null ? null : custName.trim();
    }

    public String getCorpNo() {
        return corpNo;
    }

    public void setCorpNo(final String corpNo) {
        this.corpNo = corpNo == null ? null : corpNo.trim();
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(final String corpCode) {
        this.corpCode = corpCode == null ? null : corpCode.trim();
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(final String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }

    public String getBusiLicence() {
        return busiLicence;
    }

    public void setBusiLicence(final String busiLicence) {
        this.busiLicence = busiLicence == null ? null : busiLicence.trim();
    }

    public String getParentCorpNo() {
        return parentCorpNo;
    }

    public void setParentCorpNo(final String parentCorpNo) {
        this.parentCorpNo = parentCorpNo == null ? null : parentCorpNo.trim();
    }

    @Override
    public Long getRegOperId() {
        return regOperId;
    }

    @Override
    public void setRegOperId(final Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(final String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    @Override
    public String getRegOperName() {
        return regOperName;
    }

    @Override
    public void setRegOperName(final String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    @Override
    public String getRegDate() {
        return regDate;
    }

    @Override
    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    @Override
    public String getRegTime() {
        return regTime;
    }

    @Override
    public void setRegTime(final String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    @Override
    public void setModiOperId(final Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    @Override
    public void setModiOperName(final String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    @Override
    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    @Override
    public void setModiTime(final String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(final String corpId) {
        this.corpId = corpId == null ? null : corpId.trim();
    }

    public void initDefValue(final String anOperOrg) {
        this.id = SerialGenerator.getLongValue("CustCoreCorpInfo.id");
        this.businStatus = "1";
        this.operOrg = anOperOrg;
    }

    public void modifyDefValue(final CustCoreCorpInfo anCoreCorp) {
        this.id = anCoreCorp.getId();
        this.custNo = anCoreCorp.getCustNo();
        this.custName = anCoreCorp.getCustName();
        this.coreCustNo = anCoreCorp.getCoreCustNo();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", corpNo=").append(corpNo);
        sb.append(", corpCode=").append(corpCode);
        sb.append(", corpName=").append(corpName);
        sb.append(", busiLicence=").append(busiLicence);
        sb.append(", parentCorpNo=").append(parentCorpNo);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", corpId=").append(corpId);
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
        final CustCoreCorpInfo other = (CustCoreCorpInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null
                        : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null
                        : this.getCustName().equals(other.getCustName()))
                && (this.getCorpNo() == null ? other.getCorpNo() == null : this.getCorpNo().equals(other.getCorpNo()))
                && (this.getCorpCode() == null ? other.getCorpCode() == null
                        : this.getCorpCode().equals(other.getCorpCode()))
                && (this.getCorpName() == null ? other.getCorpName() == null
                        : this.getCorpName().equals(other.getCorpName()))
                && (this.getBusiLicence() == null ? other.getBusiLicence() == null
                        : this.getBusiLicence().equals(other.getBusiLicence()))
                && (this.getParentCorpNo() == null ? other.getParentCorpNo() == null
                        : this.getParentCorpNo().equals(other.getParentCorpNo()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null
                        : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null
                        : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null
                        : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null
                        : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null
                        : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getCorpId() == null ? other.getCorpId() == null : this.getCorpId().equals(other.getCorpId()))
                && (this.getModiTime() == null ? other.getModiTime() == null
                        : this.getModiTime().equals(other.getModiTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getCorpNo() == null) ? 0 : getCorpNo().hashCode());
        result = prime * result + ((getCorpCode() == null) ? 0 : getCorpCode().hashCode());
        result = prime * result + ((getCorpName() == null) ? 0 : getCorpName().hashCode());
        result = prime * result + ((getBusiLicence() == null) ? 0 : getBusiLicence().hashCode());
        result = prime * result + ((getParentCorpNo() == null) ? 0 : getParentCorpNo().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getCorpId() == null) ? 0 : getCorpId().hashCode());
        return result;
    }
}