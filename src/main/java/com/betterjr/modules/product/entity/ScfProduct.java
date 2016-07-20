package com.betterjr.modules.product.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_PRODUCT")
public class ScfProduct implements BetterjrEntity {
    /**
     * 产品流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 核心企业
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业", comments = "核心企业")
    private Long coreCustNo;

    @Transient
    private String coreName;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO", columnDefinition = "INTEGER")
    @MetaData(value = "保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    @Transient
    private String factorName;

    /**
     * 保理公司简称/名称
     */
    @Column(name = "C_FACTORCORP", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司简称/名称", comments = "保理公司简称/名称")
    private String factorCorp;

    /**
     * 产品编号
     */
    @Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品编号", comments = "产品编号")
    private String productCode;

    /**
     * 产品名称
     */
    @Column(name = "C_PRODUCT_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品名称", comments = "产品名称")
    private String productName;

    /**
     * 产品特征
     */
    @Column(name = "C_FEATURE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品特征", comments = "产品特征")
    private String features;

    /**
     * 产品使用范围
     */
    @Column(name = "C_FIT_CROWD", columnDefinition = "VARCHAR")
    @MetaData(value = "产品使用范围", comments = "产品使用范围")
    private String fitCrowd;

    /**
     * 最低融资金额/单笔放款额度（起）
     */
    @Column(name = "F_STARTMONEY", columnDefinition = "DOUBLE")
    @MetaData(value = "最低融资金额/单笔放款额度（起）", comments = "最低融资金额/单笔放款额度（起）")
    private BigDecimal minFactorAmmount;

    /**
     * 最高融资金额/单笔放款额度（终）
     */
    @Column(name = "F_ENDMONEY", columnDefinition = "DOUBLE")
    @MetaData(value = "最高融资金额/单笔放款额度（终）", comments = "最高融资金额/单笔放款额度（终）")
    private BigDecimal maxFactorAmmount;

    /**
     * 融资方式(1:折扣方式;2:比例方式;)
     */
    @Column(name = "C_FINANCTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "融资方式(1:折扣方式;2:比例方式;)", comments = "融资方式(1:折扣方式;2:比例方式;)")
    private String financeType;

    /**
     * 通用利率
     */
    @Column(name = "F_RATIO", columnDefinition = "DOUBLE")
    @MetaData(value = "通用利率", comments = "通用利率")
    private BigDecimal ratio;

    /**
     * 最短放款天数
     */
    @Column(name = "N_MINDAYS", columnDefinition = "INTEGER")
    @MetaData(value = "最短放款天数", comments = "最短放款天数")
    private Integer minLoanDays;

    /**
     * 最长放款天数
     */
    @Column(name = "N_MAXDAYS", columnDefinition = "INTEGER")
    @MetaData(value = "最长放款天数", comments = "最长放款天数")
    private Integer maxLoanDays;

    /**
     * 保理类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)
     */
    @Column(name = "C_FACTORTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "保理类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)", comments = "保理类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)")
    private String factorType;

    /**
     * 状态(0:登记;1:上架;2:下架;)
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态(0:登记;1:上架;2:下架;)", comments = "状态(0:登记;1:上架;2:下架;)")
    private String businStatus;

    /**
     * 产品录入操作员ID号
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "产品录入操作员ID号", comments = "产品录入操作员ID号")
    private Long regOperId;

    /**
     * 产品录入操作员姓名
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品录入操作员姓名", comments = "产品录入操作员姓名")
    private String regOperName;

    /**
     * 产品录入日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品录入日期", comments = "产品录入日期")
    private String regDate;

    /**
     * 产品录入时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品录入时间", comments = "产品录入时间")
    private String regTime;

    /**
     * 产品修改操作员ID号
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "产品修改操作员ID号", comments = "产品修改操作员ID号")
    private Long modiOperId;

    /**
     * 产品修改操作员姓名
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品修改操作员姓名", comments = "产品修改操作员姓名")
    private String modiOperName;

    /**
     * 产品修改日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品修改日期", comments = "产品修改日期")
    private String modiDate;

    /**
     * 产品修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品修改时间", comments = "产品修改时间")
    private String modiTime;

    /**
     * 产品上架操作员ID号
     */
    @Column(name = "L_SHELVES_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "产品上架操作员ID号", comments = "产品上架操作员ID号")
    private Long shelvesOperId;

    /**
     * 产品上架操作员姓名
     */
    @Column(name = "C_SHELVES_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品上架操作员姓名", comments = "产品上架操作员姓名")
    private String shelvesOperName;

    /**
     * 产品上架日期
     */
    @Column(name = "C_SHELVES_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品上架日期", comments = "产品上架日期")
    private String shelvesDate;

    /**
     * 产品上架时间
     */
    @Column(name = "T_SHELVES_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品上架时间", comments = "产品上架时间")
    private String shelvesTime;

    /**
     * 产品下架操作员ID号
     */
    @Column(name = "L_OFFLINE_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "产品下架操作员ID号", comments = "产品下架操作员ID号")
    private Long offLineOperId;

    /**
     * 产品下架操作员姓名
     */
    @Column(name = "C_OFFLINE_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品下架操作员姓名", comments = "产品下架操作员姓名")
    private String offLineOperName;

    /**
     * 产品下架日期
     */
    @Column(name = "D_OFFLINE_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品下架日期", comments = "产品下架日期")
    private String offLineDate;

    /**
     * 产品下架时间
     */
    @Column(name = "T_OFFLINE_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品下架时间", comments = "产品下架时间")
    private String offLineTime;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    private static final long serialVersionUID = 1467703726398L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public String getFactorCorp() {
        return factorCorp;
    }

    public void setFactorCorp(String factorCorp) {
        this.factorCorp = factorCorp;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features == null ? null : features.trim();
    }

    public String getFitCrowd() {
        return fitCrowd;
    }

    public void setFitCrowd(String fitCrowd) {
        this.fitCrowd = fitCrowd == null ? null : fitCrowd.trim();
    }

    public BigDecimal getMinFactorAmmount() {
        return minFactorAmmount;
    }

    public void setMinFactorAmmount(BigDecimal minFactorAmmount) {
        this.minFactorAmmount = minFactorAmmount;
    }

    public BigDecimal getMaxFactorAmmount() {
        return maxFactorAmmount;
    }

    public void setMaxFactorAmmount(BigDecimal maxFactorAmmount) {
        this.maxFactorAmmount = maxFactorAmmount;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType == null ? null : financeType.trim();
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getMinLoanDays() {
        return minLoanDays;
    }

    public void setMinLoanDays(Integer minLoanDays) {
        this.minLoanDays = minLoanDays;
    }

    public Integer getMaxLoanDays() {
        return maxLoanDays;
    }

    public void setMaxLoanDays(Integer maxLoanDays) {
        this.maxLoanDays = maxLoanDays;
    }

    public String getFactorType() {
        return factorType;
    }

    public void setFactorType(String factorType) {
        this.factorType = factorType == null ? null : factorType.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String status) {
        this.businStatus = status == null ? null : status.trim();
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public Long getShelvesOperId() {
        return shelvesOperId;
    }

    public void setShelvesOperId(Long shelvesOperId) {
        this.shelvesOperId = shelvesOperId;
    }

    public String getShelvesOperName() {
        return shelvesOperName;
    }

    public void setShelvesOperName(String shelvesOperName) {
        this.shelvesOperName = shelvesOperName == null ? null : shelvesOperName.trim();
    }

    public String getShelvesDate() {
        return shelvesDate;
    }

    public void setShelvesDate(String shelvesDate) {
        this.shelvesDate = shelvesDate == null ? null : shelvesDate.trim();
    }

    public String getShelvesTime() {
        return shelvesTime;
    }

    public void setShelvesTime(String shelvesTime) {
        this.shelvesTime = shelvesTime == null ? null : shelvesTime.trim();
    }

    public Long getOffLineOperId() {
        return offLineOperId;
    }

    public void setOffLineOperId(Long offLineOperId) {
        this.offLineOperId = offLineOperId;
    }

    public String getOffLineOperName() {
        return offLineOperName;
    }

    public void setOffLineOperName(String offLineOperName) {
        this.offLineOperName = offLineOperName == null ? null : offLineOperName.trim();
    }

    public String getOffLineDate() {
        return offLineDate;
    }

    public void setOffLineDate(String offLineDate) {
        this.offLineDate = offLineDate == null ? null : offLineDate.trim();
    }

    public String getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(String offLineTime) {
        this.offLineTime = offLineTime;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getCoreName() {
        return coreName;
    }

    public void setCoreName(String coreName) {
        this.coreName = coreName;
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", factorCorp=").append(factorCorp);
        sb.append(", productCode=").append(productCode);
        sb.append(", productName=").append(productName);
        sb.append(", features=").append(features);
        sb.append(", fitCrowd=").append(fitCrowd);
        sb.append(", minFactorAmmount=").append(minFactorAmmount);
        sb.append(", maxFactorAmmount=").append(maxFactorAmmount);
        sb.append(", financeType=").append(financeType);
        sb.append(", ratio=").append(ratio);
        sb.append(", minLoanDays=").append(minLoanDays);
        sb.append(", maxLoanDays=").append(maxLoanDays);
        sb.append(", factorType=").append(factorType);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", shelvesOperId=").append(shelvesOperId);
        sb.append(", shelvesOperName=").append(shelvesOperName);
        sb.append(", shelvesDate=").append(shelvesDate);
        sb.append(", shelvesTime=").append(shelvesTime);
        sb.append(", offLineOperId=").append(offLineOperId);
        sb.append(", offLineOperName=").append(offLineOperName);
        sb.append(", offLineDate=").append(offLineDate);
        sb.append(", offLineTime=").append(offLineTime);
        sb.append(", operOrg=").append(operOrg);
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
        ScfProduct other = (ScfProduct) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
                && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
                && (this.getFactorCorp() == null ? other.getFactorCorp() == null : this.getFactorCorp().equals(other.getFactorCorp()))
                && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
                && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
                && (this.getFeatures() == null ? other.getFeatures() == null : this.getFeatures().equals(other.getFeatures()))
                && (this.getFitCrowd() == null ? other.getFitCrowd() == null : this.getFitCrowd().equals(other.getFitCrowd()))
                && (this.getMinFactorAmmount() == null ? other.getMinFactorAmmount() == null
                        : this.getMinFactorAmmount().equals(other.getMinFactorAmmount()))
                && (this.getMaxFactorAmmount() == null ? other.getMaxFactorAmmount() == null
                        : this.getMaxFactorAmmount().equals(other.getMaxFactorAmmount()))
                && (this.getFinanceType() == null ? other.getFinanceType() == null : this.getFinanceType().equals(other.getFinanceType()))
                && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
                && (this.getMinLoanDays() == null ? other.getMinLoanDays() == null : this.getMinLoanDays().equals(other.getMinLoanDays()))
                && (this.getMaxLoanDays() == null ? other.getMaxLoanDays() == null : this.getMaxLoanDays().equals(other.getMaxLoanDays()))
                && (this.getFactorType() == null ? other.getFactorType() == null : this.getFactorType().equals(other.getFactorType()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getShelvesOperId() == null ? other.getShelvesOperId() == null : this.getShelvesOperId().equals(other.getShelvesOperId()))
                && (this.getShelvesOperName() == null ? other.getShelvesOperName() == null
                        : this.getShelvesOperName().equals(other.getShelvesOperName()))
                && (this.getShelvesDate() == null ? other.getShelvesDate() == null : this.getShelvesDate().equals(other.getShelvesDate()))
                && (this.getShelvesTime() == null ? other.getShelvesTime() == null : this.getShelvesTime().equals(other.getShelvesTime()))
                && (this.getOffLineOperId() == null ? other.getOffLineOperId() == null : this.getOffLineOperId().equals(other.getOffLineOperId()))
                && (this.getOffLineOperName() == null ? other.getOffLineOperName() == null
                        : this.getOffLineOperName().equals(other.getOffLineOperName()))
                && (this.getOffLineDate() == null ? other.getOffLineDate() == null : this.getOffLineDate().equals(other.getOffLineDate()))
                && (this.getOffLineTime() == null ? other.getOffLineTime() == null : this.getOffLineTime().equals(other.getOffLineTime()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getFactorCorp() == null) ? 0 : getFactorCorp().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getFeatures() == null) ? 0 : getFeatures().hashCode());
        result = prime * result + ((getFitCrowd() == null) ? 0 : getFitCrowd().hashCode());
        result = prime * result + ((getMinFactorAmmount() == null) ? 0 : getMinFactorAmmount().hashCode());
        result = prime * result + ((getMaxFactorAmmount() == null) ? 0 : getMaxFactorAmmount().hashCode());
        result = prime * result + ((getFinanceType() == null) ? 0 : getFinanceType().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getMinLoanDays() == null) ? 0 : getMinLoanDays().hashCode());
        result = prime * result + ((getMaxLoanDays() == null) ? 0 : getMaxLoanDays().hashCode());
        result = prime * result + ((getFactorType() == null) ? 0 : getFactorType().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getShelvesOperId() == null) ? 0 : getShelvesOperId().hashCode());
        result = prime * result + ((getShelvesOperName() == null) ? 0 : getShelvesOperName().hashCode());
        result = prime * result + ((getShelvesDate() == null) ? 0 : getShelvesDate().hashCode());
        result = prime * result + ((getShelvesTime() == null) ? 0 : getShelvesTime().hashCode());
        result = prime * result + ((getOffLineOperId() == null) ? 0 : getOffLineOperId().hashCode());
        result = prime * result + ((getOffLineOperName() == null) ? 0 : getOffLineOperName().hashCode());
        result = prime * result + ((getOffLineDate() == null) ? 0 : getOffLineDate().hashCode());
        result = prime * result + ((getOffLineTime() == null) ? 0 : getOffLineTime().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        return result;
    }

    public void initAddValue() {
        this.id = SerialGenerator.getLongValue("ScfProduct.id");
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.regOperName = UserUtils.getOperatorInfo().getName();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.businStatus = "0";
    }

    public void initModifyValue(ScfProduct anProduct) {
        this.id = anProduct.getId();
        this.businStatus = anProduct.getBusinStatus();
        this.operOrg = anProduct.getOperOrg();

        this.factorNo = anProduct.getFactorNo();
        this.factorCorp = anProduct.getFactorCorp();

        this.regOperId = anProduct.getRegOperId();
        this.regOperName = anProduct.getRegOperName();
        this.regDate = anProduct.getRegDate();
        this.regTime = anProduct.getRegTime();

        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getOperatorInfo().getName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }

}