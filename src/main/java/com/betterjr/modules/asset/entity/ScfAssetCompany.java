package com.betterjr.modules.asset.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_asset_company")
public class ScfAssetCompany implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    private String regTime;

    /**
     * 资产表编号
     */
    @Column(name = "L_ASSET_ID", columnDefinition = "INTEGER")
    @MetaData(value = "资产表编号", comments = "资产表编号")
    private Long assetId;

    /**
     * 企业编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "企业编号", comments = "企业编号")
    private Long custNo;

    /**
     * 企业名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "企业名称", comments = "企业名称")
    private String custName;

    /**
     * 企业在资产中的角色 1 供应商 2 经销商 3保理公司 4 核心企业
     */
    @Column(name = "C_ASSET_ROLE", columnDefinition = "VARCHAR")
    @MetaData(value = "企业在资产中的角色 1 供应商 2 经销商 3保理公司 4 核心企业", comments = "企业在资产中的角色 1 供应商 2 经销商 3保理公司 4 核心企业")
    private String assetRole;

    /**
     * 企业对此资产的操作权限 
     */
    @Column(name = "N_OPERATOR_STATUS", columnDefinition = "INTEGER")
    @MetaData(value = "企业对此资产的操作权限 1 读 2 写 4 删除", comments = "企业对此资产的操作权限 1 读 2 写 4 删除")
    private Integer operatorStatus;

    /**
     * 是否关联资产 10 关联  20已经被中止关联
     */
    @Column(name = "C_BUSIN_STATU", columnDefinition = "VARCHAR")
    @MetaData(value = "是否关联资产 10 关联  20已经被中止关联", comments = "是否关联资产 10 关联  20已经被中止关联")
    private String businStatus;

    /**
     * 开户银行行名称
     */
    @Column(name = "c_bankName", columnDefinition = "VARCHAR")
    @MetaData(value = "开户银行行名称", comments = "开户银行行名称")
    private String bankName;

    /**
     * 银行帐号
     */
    @Column(name = "c_bankAccount", columnDefinition = "VARCHAR")
    @MetaData(value = "银行帐号", comments = "银行帐号")
    private String bankAccount;

    /**
     * 开户人户名名称
     */
    @Column(name = "c_bankAccountName", columnDefinition = "VARCHAR")
    @MetaData(value = "开户人户名名称", comments = "开户人户名名称")
    private String bankAccountName;

    @Transient
    private CustInfo custInfo;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
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

    public Long getAssetId() {
        return this.assetId;
    }

    public void setAssetId(Long anAssetId) {
        this.assetId = anAssetId;
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

    public String getAssetRole() {
        return this.assetRole;
    }

    public void setAssetRole(String anAssetRole) {
        this.assetRole = anAssetRole;
    }

    public Integer getOperatorStatus() {
        return this.operatorStatus;
    }

    public void setOperatorStatus(Integer anOperatorStatus) {
        this.operatorStatus = anOperatorStatus;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public CustInfo getCustInfo() {
        return this.custInfo;
    }

    public void setCustInfo(CustInfo anCustInfo) {
        this.custInfo = anCustInfo;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String anBankName) {
        this.bankName = anBankName;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(String anBankAccount) {
        this.bankAccount = anBankAccount;
    }

    public String getBankAccountName() {
        return this.bankAccountName;
    }

    public void setBankAccountName(String anBankAccountName) {
        this.bankAccountName = anBankAccountName;
    }

    @Override
    public String toString() {
        return "ScfAssetCompany [id=" + this.id + ", regDate=" + this.regDate + ", regTime=" + this.regTime
                + ", assetId=" + this.assetId + ", custNo=" + this.custNo + ", custName=" + this.custName
                + ", assetRole=" + this.assetRole + ", operatorStatus=" + this.operatorStatus + ", businStatus="
                + this.businStatus + ", bankName=" + this.bankName + ", bankAccount=" + this.bankAccount
                + ", bankAccountName=" + this.bankAccountName + ", custInfo=" + this.custInfo + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.assetId == null) ? 0 : this.assetId.hashCode());
        result = prime * result + ((this.assetRole == null) ? 0 : this.assetRole.hashCode());
        result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
        result = prime * result + ((this.bankAccountName == null) ? 0 : this.bankAccountName.hashCode());
        result = prime * result + ((this.bankName == null) ? 0 : this.bankName.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.custInfo == null) ? 0 : this.custInfo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.operatorStatus == null) ? 0 : this.operatorStatus.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAssetCompany other = (ScfAssetCompany) obj;
        if (this.assetId == null) {
            if (other.assetId != null) return false;
        } else if (!this.assetId.equals(other.assetId)) return false;
        if (this.assetRole == null) {
            if (other.assetRole != null) return false;
        } else if (!this.assetRole.equals(other.assetRole)) return false;
        if (this.bankAccount == null) {
            if (other.bankAccount != null) return false;
        } else if (!this.bankAccount.equals(other.bankAccount)) return false;
        if (this.bankAccountName == null) {
            if (other.bankAccountName != null) return false;
        } else if (!this.bankAccountName.equals(other.bankAccountName)) return false;
        if (this.bankName == null) {
            if (other.bankName != null) return false;
        } else if (!this.bankName.equals(other.bankName)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.custInfo == null) {
            if (other.custInfo != null) return false;
        } else if (!this.custInfo.equals(other.custInfo)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.operatorStatus == null) {
            if (other.operatorStatus != null) return false;
        } else if (!this.operatorStatus.equals(other.operatorStatus)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        return true;
    }

    public void initAdd() {

        this.id = SerialGenerator.getLongValue("ScfAssetCompany.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.businStatus = AssetConstantCollentions.ASSET_BUSIN_STATUS_OK;
    }

}
