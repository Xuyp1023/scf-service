package com.betterjr.modules.loan.helper;

public enum PayPlanBusinStatus {
    NORMAL("1", "正常"),
    CLEAN("2", "结清"),
    OVERDUE("3", "逾期"),
    REPAYMENT("4", "提前结清"),
    EXTENSION("5", "展期"),
    BADBIT("6", "坏账");
    
    private PayPlanBusinStatus(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    private String name;
    private String code;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    
    
}
