package com.betterjr.modules.param.entity;

public class SupplierParam {

    // 供应商客户号
    private String custNo;
    // 授信优先标识.1已办理，0未办理
    private String creditFirst;
    // 业务优先标识.1已办理，0未办理
    private String businessFirst;
    // 是否接收资金方信息推送.1接收，0不接收
    private String isPush;
    
    public String getCustNo() {
        return this.custNo;
    }
    public void setCustNo(String anCustNo) {
        this.custNo = anCustNo;
    }
    public String getCreditFirst() {
        return this.creditFirst;
    }
    public void setCreditFirst(String anCreditFirst) {
        this.creditFirst = anCreditFirst;
    }
    public String getBusinessFirst() {
        return this.businessFirst;
    }
    public void setBusinessFirst(String anBusinessFirst) {
        this.businessFirst = anBusinessFirst;
    }
    public String getIsPush() {
        return this.isPush;
    }
    public void setIsPush(String anIsPush) {
        this.isPush = anIsPush;
    }
    
}
