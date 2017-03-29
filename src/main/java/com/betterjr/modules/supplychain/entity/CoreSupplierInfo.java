package com.betterjr.modules.supplychain.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.client.data.ScfClientDataParentFace;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_SUPPLIER")
public class CoreSupplierInfo implements ScfClientDataParentFace {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 核心企额客户编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企额客户编号", comments = "核心企额客户编号")
    private Long coreCustNo;

    /**
     * 客户在资金管理系统中的客户号
     */
    @Column(name = "C_BTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "客户在资金管理系统中的客户号", comments = "客户在资金管理系统中的客户号")
    private String btNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户全称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户全称", comments = "客户全称")
    private String custName;

    /**
     * 所属单位Id
     */
    @Column(name = "C_CORPID", columnDefinition = "VARCHAR")
    @MetaData(value = "所属单位Id", comments = "所属单位Id")
    private String corpNo;

    /**
     * 所属单位名称
     */
    @Column(name = "C_CORPNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "所属单位名称", comments = "所属单位名称")
    private String corpName;

    /**
     * 所属类别
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "所属类别", comments = "所属类别")
    private String serviceClass;

    /**
     * 账号所在省
     */
    @Column(name = "C_ACCOUNTPROVINCE", columnDefinition = "VARCHAR")
    @MetaData(value = "账号所在省", comments = "账号所在省")
    private String provinceName;

    /**
     * 账号所在市
     */
    @Column(name = "C_ACCOUNTCITY", columnDefinition = "VARCHAR")
    @MetaData(value = "账号所在市", comments = "账号所在市")
    private String cityName;

    /**
     * 联系人姓名
     */
    @Column(name = "C_CONTACT", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人姓名", comments = "联系人姓名")
    private String contName;

    /**
     * 联系人证件类型
     */
    @Column(name = "C_CONTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人证件类型", comments = "联系人证件类型")
    private String contIdentType;

    /**
     * 联系人证件号码
     */
    @Column(name = "C_CONTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人证件号码", comments = "联系人证件号码")
    private String contIdentNo;

    /**
     * 联系人联系电话
     */
    @Column(name = "C_CONTPHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人联系电话", comments = "联系人联系电话")
    private String contPhone;

    /**
     * 传真号码
     */
    @Column(name = "C_FAXNO", columnDefinition = "VARCHAR")
    @MetaData(value = "传真号码", comments = "传真号码")
    private String faxNo;

    /**
     * 联系人手机号码
     */
    @Column(name = "C_CONTMOBILE", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人手机号码", comments = "联系人手机号码")
    private String contMobileNo;

    /**
     * 联系人email地址
     */
    @Column(name = "C_CONTEMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "联系人email地址", comments = "联系人email地址")
    private String contEmail;

    /**
     * 通讯地址
     */
    @Column(name = "C_ADDRESS", columnDefinition = "VARCHAR")
    @MetaData(value = "通讯地址", comments = "通讯地址")
    private String address;

    /**
     * 0:无效,1: 有效,2:冻结
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "0:无效,1: 有效,2:冻结", comments = "0:无效,1: 有效,2:冻结")
    private String userStatus;

    /**
     * 0: 客户/供应商, 1:客户,2:供应商
     */
    @Column(name = "C_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "0: 客户/供应商, 1:客户,2:供应商", comments = "0: 客户/供应商, 1:客户,2:供应商")
    private String userFlag;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 操作员所在机构，证书登录，则是证书的企业名称O字段
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员所在机构", comments = "操作员所在机构，证书登录，则是证书的企业名称O字段")
    private String operOrg;

    @JsonIgnore
    @Column(name = "C_CORE_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "数据所属核心企业")
    private String coreOperOrg;

    @Transient
    private String bankAccount;

    @Transient
    private String bankAccountName;

    private static final long serialVersionUID = 1459331162474L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getBtNo() {
        return btNo;
    }

    public void setBtNo(final String btNo) {
        this.btNo = btNo == null ? null : btNo.trim();
    }

    @Override
    public Long getCustNo() {
        return custNo;
    }

    @Override
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

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(final String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(final String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getContName() {
        return contName;
    }

    public void setContName(final String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(final String contIdentType) {
        this.contIdentType = contIdentType == null ? null : contIdentType.trim();
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(final String contIdentNo) {
        this.contIdentNo = contIdentNo == null ? null : contIdentNo.trim();
    }

    public String getContPhone() {
        return contPhone;
    }

    public void setContPhone(final String contPhone) {
        this.contPhone = contPhone == null ? null : contPhone.trim();
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(final String faxNo) {
        this.faxNo = faxNo == null ? null : faxNo.trim();
    }

    public String getContMobileNo() {
        return contMobileNo;
    }

    public void setContMobileNo(final String contMobileNo) {
        this.contMobileNo = contMobileNo == null ? null : contMobileNo.trim();
    }

    public String getContEmail() {
        return contEmail;
    }

    public void setContEmail(final String contEmail) {
        this.contEmail = contEmail == null ? null : contEmail.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(final String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(final String userFlag) {
        this.userFlag = userFlag == null ? null : userFlag.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    @Override
    public void setCoreCustNo(final Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    @Override
    public String getOperOrg() {
        return this.operOrg;
    }

    @Override
    public void setOperOrg(final String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    @Override
    public void setModiDate(final String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getServiceClass() {
        return this.serviceClass;
    }

    public void setServiceClass(final String anServiceClass) {
        this.serviceClass = anServiceClass;
    }

    public String getBankAccountName() {
        return this.bankAccountName;
    }

    public void setBankAccountName(final String anBankAccountName) {
        this.bankAccountName = anBankAccountName;
    }

    public void setBankAccount(final String anBankAccount) {
        this.bankAccount = anBankAccount;
    }

    @Override
    public String getCoreOperOrg() {
        return coreOperOrg;
    }

    @Override
    public void setCoreOperOrg(final String coreOperOrg) {
        this.coreOperOrg = coreOperOrg == null ? null : coreOperOrg.trim();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", btNo=").append(btNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", corpNo=").append(corpNo);
        sb.append(", corpName=").append(corpName);
        sb.append(", serviceClass=").append(serviceClass);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", cityName=").append(cityName);
        sb.append(", contName=").append(contName);
        sb.append(", contIdentType=").append(contIdentType);
        sb.append(", contIdentNo=").append(contIdentNo);
        sb.append(", contPhone=").append(contPhone);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", contMobileNo=").append(contMobileNo);
        sb.append(", contEmail=").append(contEmail);
        sb.append(", address=").append(address);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", userFlag=").append(userFlag);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", bankAccountName=").append(bankAccountName);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", coreOperOrg=").append(coreOperOrg);
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
        final CoreSupplierInfo other = (CoreSupplierInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getCorpNo() == null ? other.getCorpNo() == null : this.getCorpNo().equals(other.getCorpNo()))
                && (this.getCorpName() == null ? other.getCorpName() == null : this.getCorpName().equals(other.getCorpName()))
                && (this.getServiceClass() == null ? other.getServiceClass() == null : this.getServiceClass().equals(other.getServiceClass()))
                && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
                && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
                && (this.getContName() == null ? other.getContName() == null : this.getContName().equals(other.getContName()))
                && (this.getContIdentType() == null ? other.getContIdentType() == null : this.getContIdentType().equals(other.getContIdentType()))
                && (this.getContIdentNo() == null ? other.getContIdentNo() == null : this.getContIdentNo().equals(other.getContIdentNo()))
                && (this.getContPhone() == null ? other.getContPhone() == null : this.getContPhone().equals(other.getContPhone()))
                && (this.getFaxNo() == null ? other.getFaxNo() == null : this.getFaxNo().equals(other.getFaxNo()))
                && (this.getContMobileNo() == null ? other.getContMobileNo() == null : this.getContMobileNo().equals(other.getContMobileNo()))
                && (this.getContEmail() == null ? other.getContEmail() == null : this.getContEmail().equals(other.getContEmail()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getUserStatus() == null ? other.getUserStatus() == null : this.getUserStatus().equals(other.getUserStatus()))
                && (this.getUserFlag() == null ? other.getUserFlag() == null : this.getUserFlag().equals(other.getUserFlag()))
                && (this.getCoreOperOrg() == null ? other.getCoreOperOrg() == null : this.getCoreOperOrg().equals(other.getCoreOperOrg()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getBtNo() == null) ? 0 : getBtNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getCorpNo() == null) ? 0 : getCorpNo().hashCode());
        result = prime * result + ((getCorpName() == null) ? 0 : getCorpName().hashCode());
        result = prime * result + ((getServiceClass() == null) ? 0 : getServiceClass().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getContName() == null) ? 0 : getContName().hashCode());
        result = prime * result + ((getContIdentType() == null) ? 0 : getContIdentType().hashCode());
        result = prime * result + ((getContIdentNo() == null) ? 0 : getContIdentNo().hashCode());
        result = prime * result + ((getContPhone() == null) ? 0 : getContPhone().hashCode());
        result = prime * result + ((getFaxNo() == null) ? 0 : getFaxNo().hashCode());
        result = prime * result + ((getContMobileNo() == null) ? 0 : getContMobileNo().hashCode());
        result = prime * result + ((getContEmail() == null) ? 0 : getContEmail().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getUserStatus() == null) ? 0 : getUserStatus().hashCode());
        result = prime * result + ((getUserFlag() == null) ? 0 : getUserFlag().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getCoreOperOrg() == null) ? 0 : getCoreOperOrg().hashCode());
        return result;
    }

    public void modifyDefaultValue(final CoreSupplierInfo anSupplier) {
        this.id = anSupplier.id;
        this.modiDate = BetterDateUtils.getNumDateTime();
    }

    @Override
    public void fillDefaultValue() {
        this.id = SerialGenerator.getLongValue("CoreSupplierInfo.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDateTime();
        this.custNo = MathExtend.defaultLongZero(this.custNo);
        this.userStatus="1";
    }

    @Override
    public String getBankAccount() {

        return this.bankAccount;
    }

    @Override
    public String findBankAccountName() {

        return this.bankAccountName;
    }

    @Override
    public void modifytValue() {

        this.modiDate = BetterDateUtils.getNumDate();
    }
}