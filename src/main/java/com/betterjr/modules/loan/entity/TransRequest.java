package com.betterjr.modules.loan.entity;

import de.ruedigermoeller.serialization.annotations.Transient;

@Transient
public class TransRequest {

    private ScfRequest request;
    private Object order;
    public ScfRequest getRequest() {
        return request;
    }
    public void setRequest(ScfRequest request) {
        this.request = request;
    }
    public Object getOrder() {
        return order;
    }
    public void setOrder(Object order) {
        this.order = order;
    }
    
}
