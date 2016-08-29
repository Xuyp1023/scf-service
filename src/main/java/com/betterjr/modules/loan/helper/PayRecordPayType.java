package com.betterjr.modules.loan.helper;

public enum PayRecordPayType {
    NORMAL("1", "正常还款"),
    CLEAN("2", "提前还款"),
    OVERDUE("3", "逾期还款"),
    EXPMPT("4", "豁免"),
    OVERDUE_EXPMPT("5", "逾期豁免"),
    BADBIT("6", "坏账"),
    EXTENSION("7", "展期");
    
    private PayRecordPayType(String name, String code) {
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
