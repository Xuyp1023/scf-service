package com.betterjr.modules.supplychain.data;

public class CoreCustInfo implements java.io.Serializable {
    private static final long serialVersionUID = 7914384563295474255L;
    private String custName;
    private Long custNo;
    private String operOrg;    
    private String partnerCode;

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public void setPartnerCode(String anPartnerCode) {
        this.partnerCode = anPartnerCode;
    }

}
