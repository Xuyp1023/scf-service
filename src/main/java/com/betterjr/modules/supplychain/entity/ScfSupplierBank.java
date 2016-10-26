package com.betterjr.modules.supplychain.entity;

import java.util.Map;

import com.betterjr.common.annotation.*;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.supplychain.data.ScfClientDataParentFace;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_BANK")
public class ScfSupplierBank implements ScfClientDataParentFace {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户全称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户全称", comments = "客户全称")
    private String custName;

    /**
     * 核心企额客户编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企额客户编号", comments = "核心企额客户编号")
    private Long coreCustNo;

    /**
     * 银行账户
     */
    @Column(name = "C_BANKACCO",  columnDefinition="VARCHAR" )
    @MetaData( value="银行账户", comments = "银行账户")
    private String bankAccount;

    /**
     * 银行账户
     */
    @Column(name = "C_BANKNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="开户银行", comments = "开户银行")
    private String bankName;

    /**
     * 银行户名
     */
    @Column(name = "C_BANKACCONAME",  columnDefinition="VARCHAR" )
    @MetaData( value="银行户名", comments = "银行户名")
    private String bankAccountName;   
    

    /**
     * 联行号
     */
    @Column(name = "C_BANKSOURCECODE",  columnDefinition="VARCHAR" )
    @MetaData( value="联行号", comments = "联行号")
    private String bankSourceCode;

    /**
     * 银行类别
     */
    @Column(name = "C_BANKTYPECODE",  columnDefinition="VARCHAR" )
    @MetaData( value="银行类别", comments = "银行类别")
    private String bankTypeCode;

    /**
     * 账号所在省
     */
    @Column(name = "C_ACCOUNTPROVINCE",  columnDefinition="VARCHAR" )
    @MetaData( value="账号所在省", comments = "账号所在省")
    private String provinceName;

    /**
     * 账号所在市
     */
    @Column(name = "C_ACCOUNTCITY",  columnDefinition="VARCHAR" )
    @MetaData( value="账号所在市", comments = "账号所在市")
    private String cityName;

    /**
     * 签约账号
     */
    @Column(name = "C_SIGNACCOUNT",  columnDefinition="VARCHAR" )
    @MetaData( value="签约账号", comments = "签约账号")
    private Boolean signAccount;

    /**
     * 首选账号
     */
    @Column(name = "C_DEFAULT",  columnDefinition="VARCHAR" )
    @MetaData( value="首选账号", comments = "首选账号")
    private Boolean defaultAccount;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 状态，0未处理，1正常，2申请中， 3取消中，4取消
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态，0未处理，1正常，2申请中， 3取消中，4取消")
    private String accountStatus;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 客户类型：0：机构；1：个人
     */
    @Column(name = "C_CUSTTYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="客户类型：0：机构", comments = "客户类型：0：机构；1：个人")
    private String custType;

    /**
     * 操作员所在机构，证书登录，则是证书的企业名称O字段
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员所在机构", comments = "操作员所在机构，证书登录，则是证书的企业名称O字段")
    private String operOrg;

    /**
     * 客户在资金管理系统中的客户号
     */
    @Column(name = "C_BTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户在资金管理系统中的客户号", comments = "客户在资金管理系统中的客户号")
    private String btNo;

    private static final long serialVersionUID = 5567237665659725591L;

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
        this.custName = custName == null ? null : custName.trim();
    }
 
    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    public String getBankSourceCode() {
        return bankSourceCode;
    }

    public void setBankSourceCode(String bankSourceCode) {
        this.bankSourceCode = bankSourceCode == null ? null : bankSourceCode.trim();
    }

    public String getBankTypeCode() {
        return bankTypeCode;
    }

    public void setBankTypeCode(String bankTypeCode) {
        this.bankTypeCode = bankTypeCode == null ? null : bankTypeCode.trim();
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public Boolean getSignAccount() {
        return signAccount;
    }

    public void setSignAccount(Boolean signAccount) {
        this.signAccount = signAccount;
    }

    public Boolean getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus == null ? null : accountStatus.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType == null ? null : custType.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getBtNo() {
        return btNo;
    }

    public void setBtNo(String btNo) {
        this.btNo = btNo == null ? null : btNo.trim();
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String anBankName) {
        this.bankName = anBankName;
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
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", bankAccountName=").append(bankAccountName);
        sb.append(", bankSourceCode=").append(bankSourceCode);
        sb.append(", bankTypeCode=").append(bankTypeCode);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", cityName=").append(cityName);
        sb.append(", signAccount=").append(signAccount);
        sb.append(", defaultAccount=").append(defaultAccount);
        sb.append(", regDate=").append(regDate);
        sb.append(", accountStatus=").append(accountStatus);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", custType=").append(custType);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", btNo=").append(btNo);
        sb.append(", bankName=").append(bankName);
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
        ScfSupplierBank other = (ScfSupplierBank) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
            && (this.getBankAccountName() == null ? other.getBankAccountName() == null : this.getBankAccountName().equals(other.getBankAccountName()))
            && (this.getBankSourceCode() == null ? other.getBankSourceCode() == null : this.getBankSourceCode().equals(other.getBankSourceCode()))
            && (this.getBankTypeCode() == null ? other.getBankTypeCode() == null : this.getBankTypeCode().equals(other.getBankTypeCode()))
            && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
            && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
            && (this.getSignAccount() == null ? other.getSignAccount() == null : this.getSignAccount().equals(other.getSignAccount()))
            && (this.getDefaultAccount() == null ? other.getDefaultAccount() == null : this.getDefaultAccount().equals(other.getDefaultAccount()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getAccountStatus() == null ? other.getAccountStatus() == null : this.getAccountStatus().equals(other.getAccountStatus()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getCustType() == null ? other.getCustType() == null : this.getCustType().equals(other.getCustType()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getBankAccountName() == null) ? 0 : getBankAccountName().hashCode());
        result = prime * result + ((getBankSourceCode() == null) ? 0 : getBankSourceCode().hashCode());
        result = prime * result + ((getBankTypeCode() == null) ? 0 : getBankTypeCode().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getSignAccount() == null) ? 0 : getSignAccount().hashCode());
        result = prime * result + ((getDefaultAccount() == null) ? 0 : getDefaultAccount().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getAccountStatus() == null) ? 0 : getAccountStatus().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBtNo() == null) ? 0 : getBtNo().hashCode());
       return result;
    }
    
    public void fillDefaultValue(){
       this.id = SerialGenerator.getLongValue("ScfSupplierBank.id");
       this.custType = "0";
       this.regDate = BetterDateUtils.getNumDate();
       this.modiDate = BetterDateUtils.getNumDateTime();
       this.accountStatus = "1";
       this.custNo = MathExtend.defaultLongZero(custNo);
       this.operOrg = BetterStringUtils.defaultIfEmpty(this.operOrg, " ");
    }

    @Override
    public void modifytValue(){
        
       this.modiDate = BetterDateUtils.getNumDateTime();        
    }
    
    public Map<String, Object> converToTmpDataMap(){
        Map<String, Object> tmpMap = BeanMapper.map(this, Map.class);
        
        return tmpMap;
    }

    @Override
    public String findBankAccountName() {
        return this.bankAccountName;
    }
}