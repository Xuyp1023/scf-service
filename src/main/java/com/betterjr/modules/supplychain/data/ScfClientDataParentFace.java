package com.betterjr.modules.supplychain.data;

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.entity.BetterjrEntity;
 
public interface ScfClientDataParentFace extends BetterjrEntity, BaseRemoteEntity {

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
