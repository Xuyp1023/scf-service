package com.betterjr.modules.credit.entity;

import java.math.BigDecimal;

public class ScfCreditInfo {

    /**
     * 客户编号
     */
    private Long custNo;

    /**
     * 核心企业编号
     */
    private Long coreCustNo;

    /**
     * 保理公司编号
     */
    private Long factorNo;

    /**
     * 授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
     */
    private String creditMode;

    /**
     * 授信额度变动金额
     */
    private BigDecimal balance;

    /**
     * 业务类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)
     */
    private String businFlag;

    /**
     * 业务流水号
     */
    private Long businId;

    /**
     * 业务单据号
     */
    private String requestNo;

    /**
     * 备注
     */
    private String description;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public String getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(String creditMode) {
        this.creditMode = creditMode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag;
    }

    public Long getBusinId() {
        return businId;
    }

    public void setBusinId(Long businId) {
        this.businId = businId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
