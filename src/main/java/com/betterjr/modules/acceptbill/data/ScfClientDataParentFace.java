package com.betterjr.modules.acceptbill.data;

public interface ScfClientDataParentFace {
public String getOperOrg();
    
    public void setOperOrg(String operOrg);

    public void setCustNo(Long anCustNo);
    
    public Long getCustNo();
    
    public void setCoreCustNo(Long anCoreCustNo);

    public void setModiDate(String anModiDate);
    
    public String getBtNo();
    
    public String getBankAccount();
    
    public String findBankAccountName();
    
    public void fillDefaultValue();

    public void modifytValue();

}
