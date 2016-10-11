package com.betterjr.modules.loan.helper;

public enum RequestTradeStatus {
    REQUEST("100","申请"),
    OFFER_SCHEME("110","出具保理方案"),
    CONFIRM_SCHEME("120","确认融资方案"),
    REQUEST_TRADING("130","发起贸易背景确认"),
    CONFIRM_TRADING("140","确认贸易背景"),
    CONFIRM_LOAN("150","放款确认"),
    FINISH_LOAN("160","还款中"),
    OVERDUE("170","逾期"),
    EXTENSION("180","展期"),
    CLEAN("190","结清"),
    CLOSED("-100","交易终止");
    
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
