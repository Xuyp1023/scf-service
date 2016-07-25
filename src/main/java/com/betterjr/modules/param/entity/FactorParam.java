package com.betterjr.modules.param.entity;

import java.math.BigDecimal;

public class FactorParam {
    
    //保理公司编号
    private Long custNo;
    
    //宽限期
    private int graceDays;
    
    //计算天数
    private int countDays;
    
    //罚息利率
    private BigDecimal penaltyRatio;
    
    //滞纳金利率
    private BigDecimal latefeeRatio;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public int getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(int graceDays) {
        this.graceDays = graceDays;
    }

    public int getCountDays() {
        return countDays;
    }

    public void setCountDays(int countDays) {
        this.countDays = countDays;
    }

    public BigDecimal getPenaltyRatio() {
        return penaltyRatio;
    }

    public void setPenaltyRatio(BigDecimal penaltyRatio) {
        this.penaltyRatio = penaltyRatio;
    }

    public BigDecimal getLatefeeRatio() {
        return latefeeRatio;
    }

    public void setLatefeeRatio(BigDecimal latefeeRatio) {
        this.latefeeRatio = latefeeRatio;
    }


    
    
}
