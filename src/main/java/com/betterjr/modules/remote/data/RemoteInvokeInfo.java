package com.betterjr.modules.remote.data;

import java.lang.reflect.Method;

import com.betterjr.common.annotation.AnnonRemoteMethod;
import com.betterjr.common.data.BusinClassType;

public class RemoteInvokeInfo {
    private String businFlag;
    private String custType;
    private BusinClassType businClass;
    private Method workMethod;

    public RemoteInvokeInfo() {

    }

    public RemoteInvokeInfo(AnnonRemoteMethod anM, Method anMM) {
        if (anM == null) {
            this.businFlag = anMM.getName();
            this.custType = "X";
            this.businClass = BusinClassType.SALE_QUERY;
        }
        else {
            this.businFlag = anM.businFlag();
            this.custType = anM.custType();
            this.businClass = anM.businClass();
        }
        this.workMethod = anMM;
    }

    public String getBusinFlag() {
        return this.businFlag;
    }

    public void setBusinFlag(String anBusinFlag) {
        this.businFlag = anBusinFlag;
    }

    public String getCustType() {
        return this.custType;
    }

    public void setCustType(String anCustType) {
        this.custType = anCustType;
    }

    public BusinClassType getBusinClass() {
        return this.businClass;
    }

    public void setBusinClass(BusinClassType anBusinClass) {
        this.businClass = anBusinClass;
    }

    public Method getWorkMethod() {
        return this.workMethod;
    }

    public void setWorkMethod(Method anWorkMethod) {
        this.workMethod = anWorkMethod;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("businFlag=").append(businFlag);
        sb.append(", custType=").append(custType);
        sb.append(", businClass=").append(businClass);
        sb.append(", workMethod=").append(workMethod);
        sb.append("]");
        return sb.toString();
    }
}
