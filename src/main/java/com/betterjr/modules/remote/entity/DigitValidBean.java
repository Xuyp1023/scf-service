package com.betterjr.modules.remote.entity;

import javax.validation.constraints.Digits;

public class DigitValidBean implements java.io.Serializable {

    private static final long serialVersionUID = 7684483859666570526L;

    @Digits(message="", fraction = 0, integer = 0)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
