package com.betterjr.modules.client.supplychian;

import java.io.Serializable;

//核心企业供应商信息
public class SupplyInfo implements Serializable {
    private static final long serialVersionUID = 3658291995241288050L;

    /**
     * 客户类型：0：机构；1：个人
     */
    private String custType;

    /**
     * 客户全称
     */
    private String custName;

    /**
     * 客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会
     */
    private String identType;

    /**
     * 客户证件号码
     */
    private String identNo;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 银行户名
     */
    private String bankAcountName;

    /**
     * 拜特客户编号
     */
    private String btCustId;

    /**
     * 联系人姓名
     */
    private String linkman;

    /**
     * 联系人证件类型
     */
    private String contIdentType;

    /**
     * 联系人证件号码
     */
    private String contIdentNo;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     * 联系人email地址
     */
    private String fax;

    /**
     * 联系人email地址
     */
    private String email;

    /**
     * 状态 0 未处理，1验证成功，2验证失败，9验证中
     */
    private String customerStatus;

    // 是否存在
    private boolean supplyChian;

    // 所属单位Id
    private String corpId;

    // 所属单位名称
    private String corpName;

    // 所属类别
    private String type;

    // 地址
    private String address;

    /**
     * 城市地区
     */
    private String cityName;

    /**
     * 登记日期
     */
    private String regDate;

    private String provinceCode;

    /**
     * 开证行
     */
    private String issuingBank;

    /**
     * 客户类型，0: 客户/供应商, 1:客户,2:供应商
     */
    private String fromStatus;

    public String getCustType() {
        return this.custType;
    }

    public void setCustType(String anCustType) {
        this.custType = anCustType;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getIdentType() {
        return this.identType;
    }

    public void setIdentType(String anIdentType) {
        this.identType = anIdentType;
    }

    public String getIdentNo() {
        return this.identNo;
    }

    public void setIdentNo(String anIdentNo) {
        this.identNo = anIdentNo;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(String anBankAccount) {
        this.bankAccount = anBankAccount;
    }

    public String getBankAcountName() {
        return this.bankAcountName;
    }

    public void setBankAcountName(String anBankAcountName) {
        this.bankAcountName = anBankAcountName;
    }
 

    public String getBtCustId() {
        return this.btCustId;
    }

    public void setBtCustId(String anBtCustId) {
        this.btCustId = anBtCustId;
    }

    public String getLinkman() {

        return this.linkman;
    }

    public void setLinkman(String anLinkman) {

        this.linkman = anLinkman;
    }

    public String getContIdentType() {
        return this.contIdentType;
    }

    public void setContIdentType(String anContIdentType) {
        this.contIdentType = anContIdentType;
    }

    public String getContIdentNo() {
        return this.contIdentNo;
    }

    public void setContIdentNo(String anContIdentNo) {
        this.contIdentNo = anContIdentNo;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String anTel) {
        this.tel = anTel;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNo(String anMobileNo) {
        this.mobileNo = anMobileNo;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String anFax) {
        this.fax = anFax;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String anEmail) {
        this.email = anEmail;
    }

    public String getCustomerStatus() {
        return this.customerStatus;
    }

    public void setCustomerStatus(String anCustomerStatus) {
        this.customerStatus = anCustomerStatus;
    }

    public boolean isSupplyChian() {
        return this.supplyChian;
    }

    public void setSupplyChian(boolean anSupplyChian) {
        this.supplyChian = anSupplyChian;
    }

    public String getCorpId() {
        return this.corpId;
    }

    public void setCorpId(String anCorpId) {
        this.corpId = anCorpId;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String anCorpName) {
        this.corpName = anCorpName;
    }
 
    public String getType() {
        return this.type;
    }

    public void setType(String anType) {
        this.type = anType;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String anAddress) {
        this.address = anAddress;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String anCityName) {
        this.cityName = anCityName;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(String anProvinceCode) {
        this.provinceCode = anProvinceCode;
    }

    public String getIssuingBank() {
        return this.issuingBank;
    }

    public void setIssuingBank(String anIssuingBank) {
        this.issuingBank = anIssuingBank;
    }

    public String getFromStatus() {
        return this.fromStatus;
    }

    public void setFromStatus(String anFromStatus) {
        this.fromStatus = anFromStatus;
    }

}
