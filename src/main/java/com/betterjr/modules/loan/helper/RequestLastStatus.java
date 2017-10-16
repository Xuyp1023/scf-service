package com.betterjr.modules.loan.helper;

public enum RequestLastStatus {

    REQUEST("1", "申请中"), APPROVE("2", "审批中"), LOAN("3", "放款中"), REPLAYMENT("4", "还款中"), OVERDUE("5",
            "逾期"), EXTENSION("6", "展期"), BADDEBT("7", "坏账"), CLEAN("8", "结清"), CLOSED("0", "交易终止");

    private String code;
    private String name;

    private RequestLastStatus(String code, String name) {
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
