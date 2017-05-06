// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年5月6日, liuwl, creation
// ============================================================================
package com.betterjr.modules.commission.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuwl
 *
 */
public class CalcPayResult implements Serializable {
    private BigDecimal totalBalance;
    private Long totalAmount;
    private BigDecimal paySuccessBalance;
    private Long paySuccessAmount;
    private BigDecimal payFailureBalance;
    private Long payFailureAmount;
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }
    public void setTotalBalance(final BigDecimal anTotalBalance) {
        totalBalance = anTotalBalance;
    }
    public Long getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(final Long anTotalAmount) {
        totalAmount = anTotalAmount;
    }
    public BigDecimal getPaySuccessBalance() {
        return paySuccessBalance;
    }
    public void setPaySuccessBalance(final BigDecimal anPaySuccessBalance) {
        paySuccessBalance = anPaySuccessBalance;
    }
    public Long getPaySuccessAmount() {
        return paySuccessAmount;
    }
    public void setPaySuccessAmount(final Long anPaySuccessAmount) {
        paySuccessAmount = anPaySuccessAmount;
    }
    public BigDecimal getPayFailureBalance() {
        return payFailureBalance;
    }
    public void setPayFailureBalance(final BigDecimal anPayFailureBalance) {
        payFailureBalance = anPayFailureBalance;
    }
    public Long getPayFailureAmount() {
        return payFailureAmount;
    }
    public void setPayFailureAmount(final Long anPayFailureAmount) {
        payFailureAmount = anPayFailureAmount;
    }
}
