package com.betterjr.modules.discount.entity;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;

import java.math.BigDecimal;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_discount")
public class ScfDiscount implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "L_FACTORNO", columnDefinition = "INTEGER")
    private Integer factorNo;

    @Column(name = "c_discountType", columnDefinition = "VARCHAR")
    private String discountType;

    @Column(name = "c_interestType", columnDefinition = "VARCHAR")
    private String interestType;

    @Column(name = "c_busin_status", columnDefinition = "VARCHAR")
    private String businStatus;

    @Column(name = "c_discountDay", columnDefinition = "DOUBLE")
    private BigDecimal discountDay;

    @Column(name = "c_discountRate", columnDefinition = "DOUBLE")
    private BigDecimal discountRate;

    @Column(name = "c_billCount", columnDefinition = "INTEGER")
    private Long billCount;

    @Column(name = "c_billMoneyCount", columnDefinition = "DOUBLE")
    private BigDecimal billMoneyCount;

    @Column(name = "c_billsNo", columnDefinition = "VARCHAR")
    private String billsNo;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    private String regDate;

    @Column(name = "D_REG_TIME", columnDefinition = "VARCHAR")
    private String regTime;

    private static final long serialVersionUID = 2554720553861111663L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Integer factorNo) {
        this.factorNo = factorNo;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType == null ? null : discountType.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public BigDecimal getDiscountDay() {
        return discountDay;
    }

    public void setDiscountDay(BigDecimal discountDay) {
        this.discountDay = discountDay;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public Long getBillCount() {
        return billCount;
    }

    public void setBillCount(Long billCount) {
        this.billCount = billCount;
    }

    public BigDecimal getBillMoneyCount() {
        return billMoneyCount;
    }

    public void setBillMoneyCount(BigDecimal billMoneyCount) {
        this.billMoneyCount = billMoneyCount;
    }

    public String getBillsNo() {
        return billsNo;
    }

    public void setBillsNo(String billsNo) {
        this.billsNo = billsNo == null ? null : billsNo.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getInterestType() {
        return this.interestType;
    }

    public void setInterestType(String anInterestType) {
        this.interestType = anInterestType;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", discountType=").append(discountType);
        sb.append(", interestType=").append(interestType);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", discountDay=").append(discountDay);
        sb.append(", discountRate=").append(discountRate);
        sb.append(", billCount=").append(billCount);
        sb.append(", billMoneyCount=").append(billMoneyCount);
        sb.append(", billsNo=").append(billsNo);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ScfDiscount other = (ScfDiscount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null
                        : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getDiscountType() == null ? other.getDiscountType() == null
                        : this.getDiscountType().equals(other.getDiscountType()))
                && (this.getInterestType() == null ? other.getInterestType() == null
                        : this.getInterestType().equals(other.getInterestType()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getDiscountDay() == null ? other.getDiscountDay() == null
                        : this.getDiscountDay().equals(other.getDiscountDay()))
                && (this.getDiscountRate() == null ? other.getDiscountRate() == null
                        : this.getDiscountRate().equals(other.getDiscountRate()))
                && (this.getBillCount() == null ? other.getBillCount() == null
                        : this.getBillCount().equals(other.getBillCount()))
                && (this.getBillMoneyCount() == null ? other.getBillMoneyCount() == null
                        : this.getBillMoneyCount().equals(other.getBillMoneyCount()))
                && (this.getBillsNo() == null ? other.getBillsNo() == null
                        : this.getBillsNo().equals(other.getBillsNo()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null
                        : this.getRegTime().equals(other.getRegTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getDiscountType() == null) ? 0 : getDiscountType().hashCode());
        result = prime * result + ((getInterestType() == null) ? 0 : getInterestType().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getDiscountDay() == null) ? 0 : getDiscountDay().hashCode());
        result = prime * result + ((getDiscountRate() == null) ? 0 : getDiscountRate().hashCode());
        result = prime * result + ((getBillCount() == null) ? 0 : getBillCount().hashCode());
        result = prime * result + ((getBillMoneyCount() == null) ? 0 : getBillMoneyCount().hashCode());
        result = prime * result + ((getBillsNo() == null) ? 0 : getBillsNo().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        return result;
    }

    public void initDefValue(ScfDiscount scfDiscount) {
        BeanMapper.copy(scfDiscount, this);
        this.id = SerialGenerator.getIntValue("discount.id");
        this.businStatus = "1";
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
    }
}