package com.betterjr.modules.loan.helper;

public enum RequestType {

    /**
     * 订单融资
     */
    ORDER("1", "订单融资"),

    /**
     * 票据融资
     */
    BILL("2", "票据融资"),

    /**
     * 应收账款融资
     */
    RECEIVABLE("3", "应收账款融资"),

    /**
     * 经销商融资
     */
    SELLER("4", "经销商融资");

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
