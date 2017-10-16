package com.betterjr.modules.supplychain.entity;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.client.data.ScfClientDataParentFace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_TEMP_CUST_ENROLL")
public class CustTempEnrollInfo implements ScfClientDataParentFace {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 客户类型：0：机构；1：个人
     */
    @Column(name = "C_CUSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户类型：0：机构", comments = "客户类型：0：机构；1：个人")
    private String custType;

    /**
     * 客户全称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户全称", comments = "客户全称")
    private String custName;

    /**
     * 客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会
     */
    @Column(name = "C_IDENTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户证件类型", comments = "客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会")
    private String identType;

    /**
     * 客户证件号码
     */
    @Column(name = "C_IDENTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "客户证件号码", comments = "客户证件号码")
    private String identNo;

    /**
     * 客户简称
     */
    @Column(name = "C_SHORTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户简称", comments = "客户简称")
    private String shortName;

    /**
     * 客户昵称
     */
    @Column(name = "C_NICKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户昵称", comments = "客户昵称")
    private String nickName;

    /**
     * 银行全称
     */
    @Column(name = "C_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "银行全称", comments = "银行全称")
    private String bankName;

    /**
     * 银行账户
     */
    @Column(name = "C_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "银行账户", comments = "银行账户")
    private String bankAccount;

    /**
     * 银行户名
     */
    @Column(name = "C_BANKACCONAME", columnDefinition = "VARCHAR")
    @MetaData(value = "银行户名", comments = "银行户名")
    private String bankAcountName;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 经办人姓名
     */
    @Column(name = "C_CONTACT", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人姓名", comments = "经办人姓名")
    private String contName;

    /**
     * 经办人证件类型
     */
    @Column(name = "C_CONTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件类型", comments = "经办人证件类型")
    private String contIdentType;

    /**
     * 经办人证件号码
     */
    @Column(name = "C_CONTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件号码", comments = "经办人证件号码")
    private String contIdentNo;

    /**
     * 经办人联系电话
     */
    @Column(name = "C_CONTPHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人联系电话", comments = "经办人联系电话")
    private String contPhone;

    /**
     * 经办人手机号码
     */
    @Column(name = "C_CONTMOBILE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人手机号码", comments = "经办人手机号码")
    private String contMobileNo;

    /**
     * 经办人email地址
     */
    @Column(name = "C_CONTEMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人email地址", comments = "经办人email地址")
    private String contEmail;

    /**
     * 状态 0 未处理，1验证成功，2验证失败，9验证中
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态 0 未处理", comments = "状态 0 未处理，1验证成功，2验证失败，9验证中")
    private String status;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注")
    private String description;

    /**
     * 登记日期
     */
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构，证书登录，则是证书的企业名称字段")
    private String operOrg;

    /**
     * 客户在资金管理系统中的客户号
     */
    @Column(name = "C_BTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "客户在资金管理系统中的客户号", comments = "客户在资金管理系统中的客户号")
    private String btNo;

    /**
     * 核心企额客户编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企额客户编号", comments = "核心企额客户编号")
    private Long coreCustNo;

    /**
     * 核心企业名称
     */
    @Transient
    private String coreCustName;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 经营场地地址
     */
    @Column(name = "C_PREMISES_ADDRESS", columnDefinition = "VARCHAR")
    @MetaData(value = "经营场地地址", comments = "经营场地地址")
    private String premisesAddress;

    private static final long serialVersionUID = -7979691284028963513L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(final String custType) {
        this.custType = custType == null ? null : custType.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(final String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(final String identType) {
        this.identType = identType == null ? null : identType.trim();
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(final String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    @Override
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankAcountName() {
        return this.bankAcountName;
    }

    public void setBankAcountName(final String anBankAcountName) {
        this.bankAcountName = anBankAcountName;
    }

    @Override
    public Long getCustNo() {
        return custNo;
    }

    @Override
    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    @Override
    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    @Override
    public String getOperOrg() {
        return operOrg;
    }

    @Override
    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    @Override
    public String getCoreOperOrg() {
        return null;
    }

    @Override
    public void setCoreOperOrg(final String coreOperOrg) {

    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(final Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getPremisesAddress() {
        return premisesAddress;
    }

    public void setPremisesAddress(final String premisesAddress) {
        this.premisesAddress = premisesAddress == null ? null : premisesAddress.trim();
    }

    public String getCoreCustName() {
        return coreCustName;
    }

    public void setCoreCustName(final String anCoreCustName) {
        coreCustName = anCoreCustName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id=").append(id);
        sb.append(", custType=").append(custType);
        sb.append(", custName=").append(custName);
        sb.append(", identType=").append(identType);
        sb.append(", identNo=").append(identNo);
        sb.append(", shortName=").append(shortName);
        sb.append(", nickName=").append(nickName);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", bankAcountName=").append(bankAcountName);
        sb.append(", custNo=").append(custNo);
        sb.append(", contName=").append(contName);
        sb.append(", contIdentType=").append(contIdentType);
        sb.append(", contIdentNo=").append(contIdentNo);
        sb.append(", contPhone=").append(contPhone);
        sb.append(", contMobileNo=").append(contMobileNo);
        sb.append(", contEmail=").append(contEmail);
        sb.append(", status=").append(status);
        sb.append(", description=").append(description);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", btNo=").append(btNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", coreCustName=").append(coreCustName);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", premisesAddress=").append(premisesAddress);
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
        final CustTempEnrollInfo other = (CustTempEnrollInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCustType() == null ? other.getCustType() == null
                        : this.getCustType().equals(other.getCustType()))
                && (this.getCustName() == null ? other.getCustName() == null
                        : this.getCustName().equals(other.getCustName()))
                && (this.getIdentType() == null ? other.getIdentType() == null
                        : this.getIdentType().equals(other.getIdentType()))
                && (this.getIdentNo() == null ? other.getIdentNo() == null
                        : this.getIdentNo().equals(other.getIdentNo()))
                && (this.getShortName() == null ? other.getShortName() == null
                        : this.getShortName().equals(other.getShortName()))
                && (this.getNickName() == null ? other.getNickName() == null
                        : this.getNickName().equals(other.getNickName()))
                && (this.getBankName() == null ? other.getBankName() == null
                        : this.getBankName().equals(other.getBankName()))
                && (this.getBankAccount() == null ? other.getBankAccount() == null
                        : this.getBankAccount().equals(other.getBankAccount()))
                && (this.getBankAcountName() == null ? other.getBankAcountName() == null
                        : this.getBankAcountName().equals(other.getBankAcountName()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getContName() == null ? other.getContName() == null
                        : this.getContName().equals(other.getContName()))
                && (this.getContIdentType() == null ? other.getContIdentType() == null
                        : this.getContIdentType().equals(other.getContIdentType()))
                && (this.getContIdentNo() == null ? other.getContIdentNo() == null
                        : this.getContIdentNo().equals(other.getContIdentNo()))
                && (this.getContPhone() == null ? other.getContPhone() == null
                        : this.getContPhone().equals(other.getContPhone()))
                && (this.getContMobileNo() == null ? other.getContMobileNo() == null
                        : this.getContMobileNo().equals(other.getContMobileNo()))
                && (this.getContEmail() == null ? other.getContEmail() == null
                        : this.getContEmail().equals(other.getContEmail()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null
                        : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getCoreOperOrg() == null ? other.getCoreOperOrg() == null
                        : this.getCoreOperOrg().equals(other.getCoreOperOrg()))
                && (this.getBatchNo() == null ? other.getBatchNo() == null
                        : this.getBatchNo().equals(other.getBatchNo()))
                && (this.getCoreCustName() == null ? other.getCoreCustName() == null
                        : this.getCoreCustName().equals(other.getCoreCustName()))
                && (this.getPremisesAddress() == null ? other.getPremisesAddress() == null
                        : this.getPremisesAddress().equals(other.getPremisesAddress()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getIdentType() == null) ? 0 : getIdentType().hashCode());
        result = prime * result + ((getIdentNo() == null) ? 0 : getIdentNo().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getBankAcountName() == null) ? 0 : getBankAcountName().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getContName() == null) ? 0 : getContName().hashCode());
        result = prime * result + ((getContIdentType() == null) ? 0 : getContIdentType().hashCode());
        result = prime * result + ((getContIdentNo() == null) ? 0 : getContIdentNo().hashCode());
        result = prime * result + ((getContPhone() == null) ? 0 : getContPhone().hashCode());
        result = prime * result + ((getContMobileNo() == null) ? 0 : getContMobileNo().hashCode());
        result = prime * result + ((getContEmail() == null) ? 0 : getContEmail().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getCoreOperOrg() == null) ? 0 : getCoreOperOrg().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getCoreCustName() == null) ? 0 : getCoreCustName().hashCode());
        result = prime * result + ((getPremisesAddress() == null) ? 0 : getPremisesAddress().hashCode());
        return result;
    }

    public CustTempEnrollInfo() {

    }

    public void initWeChatAddValue() {
        this.custType = "0";
        this.identType = "1";
        this.status = "0";
        this.id = SerialGenerator.getLongValue("TempCustEnroll.id");
        this.regDate = BetterDateUtils.getNumDate();
    }

    /**
     * 初始化默认值
     */
    public void initdefValue() {
        this.custType = "0";
        this.identType = "1";
        this.custNo = MathExtend.defaultLongZero(this.custNo);
        this.operOrg = " ";
        initDefOpenAccoValue();
    }

    /**
     * 初始化开户时的默认值
     */
    public void initDefOpenAccoValue() {
        this.coreCustNo = 0L;
        this.regDate = BetterDateUtils.getNumDate();
        this.modiDate = BetterDateUtils.getNumDate();
        this.id = SerialGenerator.getLongValue("TempCustEnroll.id");
        this.status = "0";
    }

    public Long getCoreCustNo() {

        return this.coreCustNo;
    }

    public void setBtNo(final String anBtNo) {

        this.btNo = anBtNo;
    }

    @Override
    public void setCoreCustNo(final Long anCoreCustNo) {

        this.coreCustNo = anCoreCustNo;
    }

    @Override
    public String getBtNo() {

        return this.btNo;
    }

    public void modifyStatus(final String anOperOrg, final Long anCoreCustNo, final String anBtNo,
            final Long anCustNo) {
        this.operOrg = anOperOrg;
        this.coreCustNo = anCoreCustNo;
        this.btNo = anBtNo;
        this.custNo = anCustNo;
        this.status = "1";
        this.modiDate = BetterDateUtils.getNumDate();
    }

    @Override
    public void fillDefaultValue() {

    }

    @Override
    public void modifytValue() {

        this.modiDate = BetterDateUtils.getNumDate();
    }

    public Map<String, Object> converToTmpDataMap() {
        final Map<String, Object> tmpMap = BeanMapper.map(this, Map.class);

        return tmpMap;
    }

    public boolean unChecked() {

        return "0".equals(this.status) || "9".equals(this.status);
    }

    @Override
    public String findBankAccountName() {

        return this.bankAcountName;
    }
}