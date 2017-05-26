package com.betterjr.modules.asset.data;

import com.betterjr.modules.account.entity.CustInfo;

public class CustInfoRole extends CustInfo {

    private Integer operatorStatus;

    public Integer getOperatorStatus() {
        return this.operatorStatus;
    }

    public void setOperatorStatus(Integer anOperatorStatus) {
        this.operatorStatus = anOperatorStatus;
    }
    
    
}
