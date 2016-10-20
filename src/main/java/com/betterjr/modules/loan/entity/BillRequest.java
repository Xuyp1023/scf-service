package com.betterjr.modules.loan.entity;

import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;

import de.ruedigermoeller.serialization.annotations.Transient;

@Transient
public class BillRequest {
    public BillRequest(){}
    public BillRequest(ScfRequest request, ScfAcceptBill bill,ScfPayPlan plan) {
        super();
        this.request = request;
        this.bill = bill;
        this.plan = plan;
    }
    private ScfRequest request;
    private ScfAcceptBill bill;
    private ScfPayPlan plan;
    public ScfRequest getRequest() {
        return request;
    }
    public void setRequest(ScfRequest request) {
        this.request = request;
    }
    public ScfAcceptBill getBill() {
        return bill;
    }
    public void setBill(ScfAcceptBill bill) {
        this.bill = bill;
    }
    public ScfPayPlan getPlan() {
        return plan;
    }
    public void setPlan(ScfPayPlan plan) {
        this.plan = plan;
    }
    
}
