package com.betterjr.modules.agreement.entity;

import java.math.BigDecimal;

import com.betterjr.common.data.BaseRemoteEntity;

public class FaceTradeResult implements BaseRemoteEntity {

    // 基金公司客户代码
    private String saleCustNo;

    // 基金公司申请单号
    private String saleRequestNo;

    // 基金公司交易账户
    private String saleTradeAccount;

    // 我方申请单号
    private String requestNo;

    // 交易账户
    private String tradeAccount;

    // 基金账户
    private String fundAccount;

    // 基金代码
    private String fundCode;

    // 份额类别
    private String shareType;

    // 交易申请日期
    private String tradeDate;

    // 申请时间
    private String tradeTime;

    // 申请份额
    private BigDecimal shares;

    // 申请金额
    private BigDecimal balance;

    // 下单日期
    private String operDate;

    // 下单时间
    private String operTime;

    // 下单时间
    private String messageID;

    // 确认日期
    private String confirmDate;

    // 冻结份额
    private BigDecimal frozenShare;

    // 总份额
    private BigDecimal totalShare;

    // 有效份额
    private BigDecimal validShare;

    // 基金公司资金账户
    private String saleMoneyAccount;

    // 基金公司网点号
    private String saleNetNo;

    // 分红方式
    private String bonusType;
    
    //基金经办人流水号
    private Long contactorSerial;
    
    //状态
    private String status;
    //申请单状态
    private String applayStatus;
    //附件url
    private String url;
    // 描述
    private String describe;

    public Long getContactorSerial() {
        return this.contactorSerial;
    }

    public void setContactorSerial(Long anContactorSerial) {
        this.contactorSerial = anContactorSerial;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String anBonusType) {
        bonusType = anBonusType;
    }

    public String getSaleNetNo() {
        return saleNetNo;
    }

    public void setSaleNetNo(String anSaleNetNo) {
        saleNetNo = anSaleNetNo;
    }

    public String getSaleMoneyAccount() {
        return saleMoneyAccount;
    }

    public void setSaleMoneyAccount(String anSaleMoneyAccount) {
        saleMoneyAccount = anSaleMoneyAccount;
    }

    public BigDecimal getFrozenShare() {
        return frozenShare;
    }

    public void setFrozenShare(BigDecimal anFrozenShare) {
        frozenShare = anFrozenShare;
    }

    public BigDecimal getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(BigDecimal anTotalShare) {
        totalShare = anTotalShare;
    }

    public BigDecimal getValidShare() {
        return validShare;
    }

    public void setValidShare(BigDecimal anValidShare) {
        validShare = anValidShare;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String anConfirmDate) {
        confirmDate = anConfirmDate;
    }

    public String getSaleCustNo() {
        return saleCustNo;
    }

    public void setSaleCustNo(String anSaleCustNo) {
        saleCustNo = anSaleCustNo;
    }

    public String getSaleRequestNo() {
        return saleRequestNo;
    }

    public void setSaleRequestNo(String anSaleRequestNo) {
        saleRequestNo = anSaleRequestNo;
    }

    public String getSaleTradeAccount() {
        return saleTradeAccount;
    }

    public void setSaleTradeAccount(String anSaleTradeAccount) {
        saleTradeAccount = anSaleTradeAccount;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String anRequestNo) {
        requestNo = anRequestNo;
    }

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String anTradeAccount) {
        tradeAccount = anTradeAccount;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String anFundAccount) {
        fundAccount = anFundAccount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String anFundCode) {
        fundCode = anFundCode;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String anShareType) {
        shareType = anShareType;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String anTradeDate) {
        tradeDate = anTradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String anTradeTime) {
        tradeTime = anTradeTime;
    }
 

    public BigDecimal getShares() {
        return this.shares;
    }

    public void setShares(BigDecimal anShares) {
        this.shares = anShares;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal anBalance) {
        balance = anBalance;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String anOperDate) {
        operDate = anOperDate;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String anOperTime) {
        operTime = anOperTime;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String anMessageID) {
        messageID = anMessageID;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String anStatus) {
        this.status = anStatus;
    }

    public String getApplayStatus() {
        return this.applayStatus;
    }

    public void setApplayStatus(String anApplayStatus) {
        this.applayStatus = anApplayStatus;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String anUrl) {
        this.url = anUrl;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String anDescribe) {
        this.describe = anDescribe;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("saleCustNo = ").append(saleCustNo);
        sb.append(", saleRequestNo=").append(saleRequestNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append("\r\n\t\t\t");
        sb.append(", fundCode=").append(fundCode);
        sb.append(", shareType=").append(shareType);
        sb.append(", saleTradeAccount=").append(saleTradeAccount);
        sb.append("\r\n\t\t\t");
        sb.append(", tradeAccount=").append(tradeAccount);
        sb.append(", fundAccount=").append(fundAccount);
        sb.append(", tradeDate=").append(tradeDate);
        sb.append(", tradeTime=").append(tradeTime);
        sb.append(", operDate=").append(operDate);
        sb.append(", operTime=").append(operTime);
        sb.append("\r\n\t\t\t");
        sb.append(", shares=").append(shares);
        sb.append(", balance=").append(balance);
        sb.append(", messageID=").append(messageID);
        sb.append(", confirmDate=").append(confirmDate);
        sb.append("\r\n\t\t\t");
        sb.append(", frozenShare=").append(frozenShare);
        sb.append(", totalShare=").append(totalShare);
        sb.append(", validShare=").append(validShare);
        sb.append(",saleMoneyAccount=").append(saleMoneyAccount);
        sb.append(",saleNetNo=").append(saleNetNo);
        sb.append(",bonusType=").append(bonusType);
        sb.append(",status=").append(status);
        sb.append(",describe=").append(describe);
        sb.append("]");
        return sb.toString();
    }

}
