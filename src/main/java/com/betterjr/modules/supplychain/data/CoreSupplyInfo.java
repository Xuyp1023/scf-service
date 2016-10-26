package com.betterjr.modules.supplychain.data;

import java.math.BigDecimal;

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.modules.account.entity.CustContactInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CoreSupplyInfo implements BaseRemoteEntity {

    /**
     * 供应商客户编号
     */
    private Long custNo;
    /**
     * 供应商企业名称
     */
    private String custName;
    /**
     * 供应商法人代表名称
     */
    private String lawName;
    /**
     * 供应商联系电话
     */
    private String phone;
    /**
     * 供应商地址
     */
    private String address;
    /**
     * 创建时间
     */
    private String regDate;
    /**
     * 传真号码
     */
    private String faxNo;
    /**
     * 核心企业编号
     */
    private Long buyerNo;
    /**
     * 核心企业名称
     */
    private String buyer;
    /**
     * 供应商证件类型
     */
    private String identType;
    /**
     * 供应商营业执照号码
     */
    private String identNo;
    /**
     * 供应商法人代表手机
     */
    private String lawPhone;
    /**
     * 供应商法人代表证件号码
     */
    private String lawIdentNo;
    /**
     * 营业执照登记日期
     */
    private String businLicRegDate;
    /**
     * 营业执照截止日期
     */
    private String validDate;
    /**
     * 企业注册地址
     */
    private String regAddr;
    /**
     * 成立日期
     */
    private String setupDate;
    /**
     * 行业
     */
    private String corpVocate;
    /**
     * 注册资本金额
     */
    private BigDecimal regBalance;
    /**
     * 实收资本
     */
    private BigDecimal paidCapital;
    /**
     * 公司从业人数
     */
    private Integer personCount;
    /**
     * 经营面积（平方米）
     */
    private BigDecimal operateArea;
    /**
     * 经营场地所有权年限(年)
     */
    private Integer ownerShipYear;
    /**
     * 经营范围
     */
    private String busiScope;

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

    public String getLawName() {
        return this.lawName;
    }

    public void setLawName(String anLawName) {
        this.lawName = anLawName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String anPhone) {
        this.phone = anPhone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String anAddress) {
        this.address = anAddress;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getFaxNo() {
        return this.faxNo;
    }

    public void setFaxNo(String anFaxNo) {
        this.faxNo = anFaxNo;
    }

    public Long getBuyerNo() {
        return this.buyerNo;
    }

    public void setBuyerNo(Long anBuyerNo) {
        this.buyerNo = anBuyerNo;
    }

    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String anBuyer) {
        this.buyer = anBuyer;
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

    public String getLawPhone() {
        return this.lawPhone;
    }

    public void setLawPhone(String anLawPhone) {
        this.lawPhone = anLawPhone;
    }

    public String getLawIdentNo() {
        return this.lawIdentNo;
    }

    public void setLawIdentNo(String anLawIdentNo) {
        this.lawIdentNo = anLawIdentNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getBusinLicRegDate() {
        return this.businLicRegDate;
    }

    public void setBusinLicRegDate(String anBusinLicRegDate) {
        this.businLicRegDate = anBusinLicRegDate;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getValidDate() {
        return this.validDate;
    }

    public void setValidDate(String anValidDate) {
        this.validDate = anValidDate;
    }

    public String getRegAddr() {
        return this.regAddr;
    }

    public void setRegAddr(String anRegAddr) {
        this.regAddr = anRegAddr;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getSetupDate() {
        return this.setupDate;
    }

    public void setSetupDate(String anSetupDate) {
        this.setupDate = anSetupDate;
    }

    public String getCorpVocate() {
        return this.corpVocate;
    }

    public void setCorpVocate(String anCorpVocate) {
        this.corpVocate = anCorpVocate;
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getRegBalance() {
        return this.regBalance;
    }

    public void setRegBalance(BigDecimal anRegBalance) {
        this.regBalance = anRegBalance;
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getPaidCapital() {
        return this.paidCapital;
    }

    public void setPaidCapital(BigDecimal anPaidCapital) {
        this.paidCapital = anPaidCapital;
    }

    public Integer getPersonCount() {
        return this.personCount;
    }

    public void setPersonCount(Integer anPersonCount) {
        this.personCount = anPersonCount;
    }

    // @JsonSerialize(using = CustDecimalJsonSerializer.class)
    public BigDecimal getOperateArea() {
        return this.operateArea;
    }

    public void setOperateArea(BigDecimal anOperateArea) {
        this.operateArea = anOperateArea;
    }

    public Integer getOwnerShipYear() {
        return this.ownerShipYear;
    }

    public void setOwnerShipYear(Integer anOwnerShipYear) {
        this.ownerShipYear = anOwnerShipYear;
    }

    public String getBusiScope() {
        return this.busiScope;
    }

    public void setBusiScope(String anBusiScope) {
        this.busiScope = anBusiScope;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", custNo=").append(custNo);
        sb.append(", custName=").append(custName);
        sb.append(", lawName=").append(lawName);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", regDate=").append(regDate);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", buyerNo=").append(buyerNo);
        sb.append(", buyer=").append(buyer);
        sb.append(", identType=").append(identType);
        sb.append(", identNo=").append(identNo);
        sb.append(", lawPhone=").append(lawPhone);
        sb.append(", lawIdentNo=").append(lawIdentNo);
        sb.append(", businLicRegDate=").append(businLicRegDate);
        sb.append(", validDate=").append(validDate);
        sb.append(", regAddr=").append(regAddr);
        sb.append(", setupDate=").append(setupDate);
        sb.append(", corpVocate=").append(corpVocate);
        sb.append(", regBalance=").append(regBalance);
        sb.append(", paidCapital=").append(paidCapital);
        sb.append(", personCount=").append(personCount);
        sb.append(", operateArea=").append(operateArea);
        sb.append(", ownerShipYear=").append(ownerShipYear);
        sb.append(", busiScope=").append(busiScope);
        sb.append("]");
        return sb.toString();
    }

    public CoreSupplyInfo() {

    }

    public CoreSupplyInfo(CustInfo anCustInfo,  CustContactInfo anContactInfo, Long anCoreCustNo, String anCoreCustName) {
        initCustInfo(anCustInfo);
        initContactInfo(anContactInfo);
        this.buyerNo = anCoreCustNo;
        this.buyer = anCoreCustName;
    }

    private void initCustInfo(CustInfo anCustInfo) {
        if (anCustInfo != null) {
            this.custNo = anCustInfo.getCustNo();
            this.custName = anCustInfo.getCustName();
            this.regDate = anCustInfo.getRegDate();
            this.identType = anCustInfo.getIdentType();
            this.identNo = anCustInfo.getIdentNo();
            this.validDate = anCustInfo.getValidDate();
        }
    }
 

    private void initContactInfo(CustContactInfo anContactInfo) {
        if (anContactInfo != null) {
            this.phone = anContactInfo.getPhone();
            this.address = anContactInfo.getAddress();
            this.faxNo = anContactInfo.getFaxNo();
        }
    }

}
