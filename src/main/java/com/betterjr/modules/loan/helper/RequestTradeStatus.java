package com.betterjr.modules.loan.helper;

public enum RequestTradeStatus {
    REQUEST("100","申请"),
    SCHEME("110","出具保理方案"),
    CONFIRM_SCHEME("120","融资方确认方案"),
    REQUEST_TRADING("130","发起融资背景确认"),
    CONFIRM_Trading("140","核心企业确认背景"),
    LOAN("150","放款确认"),
    FINISH("160","融资完成"),
    OVERDUE("170","逾期"),
    EXTENSION("180","展期"),
    PAYFINSH("190","还款完成");
    
    private String code;
    private String name;
    
    private RequestTradeStatus(String code, String name) {
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
