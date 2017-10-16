package com.betterjr.modules.client.supplychian;

import java.io.Serializable;

//供应商银行信息
public class SupplyBankInfo implements Serializable {

    private static final long serialVersionUID = -3925582010928880465L;

    /**
     * 拜特客户编号
     */
    private String btCustId;

    /**
     * 客户全称
     */
    private String custName;

    /**
     * 银行账户
     */
    private String bankAcco;

    /**
     * 银行户名
     */
    private String bankAccountName;

    /**
     * 银行全称
     */
    private String bankName;

    // 联行号
    private String bankSourcecode;

    // 银行类别
    private String bankTypeCode;

    // 账号所在市
    private String accProvince;

    /**
     * 城市地区
     */
    private String accCity;

    // 首选账号
    private String isDefualtAcc;

    // 签约账号
    private String isSignAcc;

    public String getBtCustId() {
        return this.btCustId;
    }

    public void setBtCustId(String anBtCustId) {
        this.btCustId = anBtCustId;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getBankAcco() {
        return this.bankAcco;
    }

    public void setBankAcco(String anBankAcco) {
        this.bankAcco = anBankAcco;
    }

    public String getBankAccountName() {
        return this.bankAccountName;
    }

    public void setBankAccountName(String anBankAccountName) {
        this.bankAccountName = anBankAccountName;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String anBankName) {
        this.bankName = anBankName;
    }

    public String getBankSourcecode() {
        return this.bankSourcecode;
    }

    public void setBankSourcecode(String anBankSourcecode) {
        this.bankSourcecode = anBankSourcecode;
    }

    public String getBankTypeCode() {
        return this.bankTypeCode;
    }

    public void setBankTypeCode(String anBankTypeCode) {
        this.bankTypeCode = anBankTypeCode;
    }

    public String getAccProvince() {
        return this.accProvince;
    }

    public void setAccProvince(String anAccProvince) {
        this.accProvince = anAccProvince;
    }

    public String getAccCity() {
        return this.accCity;
    }

    public void setAccCity(String anAccCity) {
        this.accCity = anAccCity;
    }

    public String getIsDefualtAcc() {
        return this.isDefualtAcc;
    }

    public void setIsDefualtAcc(String anIsDefualtAcc) {
        this.isDefualtAcc = anIsDefualtAcc;
    }

    public String getIsSignAcc() {
        return this.isSignAcc;
    }

    public void setIsSignAcc(String anIsSignAcc) {
        this.isSignAcc = anIsSignAcc;
    }
}