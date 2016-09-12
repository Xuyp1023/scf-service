package com.betterjr.modules.loan.helper;

public enum RequestType {
    ORDER("1","申请"),
    BILL("2","出具保理方案"),
    RECEIVABLE("3","融资方确认方案"),
    SELLER("4","发起融资背景确认");
    
    private String code;
    private String name;
    
    private RequestType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
