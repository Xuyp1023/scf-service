package com.betterjr.modules.client.supplychian;

import java.io.Serializable;

public class CustCoreCorp implements Serializable{
    private static final long serialVersionUID = -5026298710559976370L;
    private String corpId;
    private String corpCode;
    private String corpName;
    private String superId;
    private String occ;
    
    public String getCorpId() {
        return this.corpId;
    }
    public void setCorpId(String anCorpId) {
        this.corpId = anCorpId;
    }
    public String getCorpCode() {
        return this.corpCode;
    }
    public void setCorpCode(String anCorpCode) {
        this.corpCode = anCorpCode;
    }
    public String getCorpName() {
        return this.corpName;
    }
    public void setCorpName(String anCorpName) {
        this.corpName = anCorpName;
    }
    public String getSuperId() {
        return this.superId;
    }
    public void setSuperId(String anSuperId) {
        this.superId = anSuperId;
    }
    public String getOcc() {
        return this.occ;
    }
    public void setOcc(String anOcc) {
        this.occ = anOcc;
    }
    
}
