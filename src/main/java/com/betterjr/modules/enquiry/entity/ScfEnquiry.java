package com.betterjr.modules.enquiry.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_enquiry")
public class ScfEnquiry implements BetterjrEntity {
   
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="BIGINT" )
    @MetaData( value="流水号", comments = "流水号")
    @OrderBy("desc")
    private Long id;

    /**
     * 询价公司编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="询价公司编号", comments = "询价公司编号")
    private Long custNo;

    /**
     * 发布日期
     */
    @Column(name = "D_ACTUAL_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="发布日期", comments = "发布日期")
    private String actualDate;

    /**
     * 询价编号
     */
    @Column(name = "C_ENQUIRYNO",  columnDefinition="VARCHAR" )
    @MetaData( value="询价编号", comments = "询价编号")
    private String enquiryNo;

    /**
     * 截止日期
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="截止日期", comments = "截止日期")
    private String endDate;

    /**
     * 相关订单,多个以“，”隔开
     */
    @Column(name = "C_ORDERS",  columnDefinition="VARCHAR" )
    @MetaData( value="相关订单,多个以“", comments = "相关订单,多个以“，”隔开")
    private String orders;

    /**
     * 意向企业编号多个以“，”隔开
     */
    @Column(name = "C_FACTORS",  columnDefinition="VARCHAR" )
    @MetaData( value="意向企业编号多个以“", comments = "意向企业编号多个以“，”隔开")
    private String factors;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    /**
     * 1,订单，2:票据;3:应收款
     */
    @Column(name = "C_REQUEST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="1,订单", comments = "1,订单，2:票据;3:应收款")
    private String requestType;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    @Column(name = "L_REG_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regOperName;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String regTime;

    @Column(name = "L_MODI_OPERID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long modiOperId;

    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiOperName;

    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiDate;

    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="", comments = "")
    private String modiTime;

    @Column(name = "N_VERSION",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    private Long version;

    /**
     * 期望金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="期望金额", comments = "期望金额")
    private BigDecimal balance;

    /**
     * 利率范围
     */
    @Column(name = "C_RATIO",  columnDefinition="VARCHAR" )
    @MetaData( value="利率范围", comments = "利率范围")
    private String ratio;

    /**
     * 融资策略 1：时间优先，2：成本优先
     */
    @Column(name = "C_STRATEGY",  columnDefinition="CHAR" )
    @MetaData( value="融资策略 1：时间优先", comments = "融资策略 1：时间优先，2：成本优先")
    private String strategy;

    /**
     * 状态：-2：已融资，-1：放弃，0：未报价，1：已报价
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="CHAR" )
    @MetaData( value="状态：-2：已融资", comments = "状态：-2：已融资，-1：放弃，0：未报价，1：已报价")
    private String businStatus;

    /**
     * 报价次数
     */
    @Column(name = "N_OFFER_COUNT",  columnDefinition="INT" )
    @MetaData( value="报价次数", comments = "报价次数")
    private Integer offerCount;

    /**
     * 询价方式 1：自动，2，主动
     */
    @Column(name = "C_ENQUIRY_METHOD",  columnDefinition="CHAR" )
    @MetaData( value="询价方式 1：自动", comments = "询价方式 1：自动，2，主动")
    private String enquiryMethod;

    /**
     * 放弃理由：1融资成本太高,2融资时间太长,3无资金需求,4操作太繁琐
     */
    @Column(name = "C_DROP_REASON",  columnDefinition="CHAR" )
    @MetaData( value="放弃理由：1融资成本太高,2融资时间太长,3无资金需求,4操作太繁琐", comments = "放弃理由：1融资成本太高,2融资时间太长,3无资金需求,4操作太繁琐")
    private String dropReason;

    private static final long serialVersionUID = 1474169781476L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getEnquiryNo() {
        return enquiryNo;
    }

    public void setEnquiryNo(String enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
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

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
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
        this.modiOperName = modiOperName;
    }

    @JsonSerialize(using = CustDateJsonSerializer.class)
    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public Integer getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(Integer offerCount) {
        this.offerCount = offerCount;
    }

    public String getEnquiryMethod() {
        return enquiryMethod;
    }

    public void setEnquiryMethod(String enquiryMethod) {
        this.enquiryMethod = enquiryMethod;
    }

    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", custNo=").append(custNo);
        sb.append(", actualDate=").append(actualDate);
        sb.append(", enquiryNo=").append(enquiryNo);
        sb.append(", endDate=").append(endDate);
        sb.append(", orders=").append(orders);
        sb.append(", factors=").append(factors);
        sb.append(", description=").append(description);
        sb.append(", requestType=").append(requestType);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", version=").append(version);
        sb.append(", balance=").append(balance);
        sb.append(", ratio=").append(ratio);
        sb.append(", strategy=").append(strategy);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", offerCount=").append(offerCount);
        sb.append(", enquiryMethod=").append(enquiryMethod);
        sb.append(", dropReason=").append(dropReason);
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
        ScfEnquiry other = (ScfEnquiry) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getActualDate() == null ? other.getActualDate() == null : this.getActualDate().equals(other.getActualDate()))
            && (this.getEnquiryNo() == null ? other.getEnquiryNo() == null : this.getEnquiryNo().equals(other.getEnquiryNo()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getOrders() == null ? other.getOrders() == null : this.getOrders().equals(other.getOrders()))
            && (this.getFactors() == null ? other.getFactors() == null : this.getFactors().equals(other.getFactors()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getRequestType() == null ? other.getRequestType() == null : this.getRequestType().equals(other.getRequestType()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getStrategy() == null ? other.getStrategy() == null : this.getStrategy().equals(other.getStrategy()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getOfferCount() == null ? other.getOfferCount() == null : this.getOfferCount().equals(other.getOfferCount()))
            && (this.getEnquiryMethod() == null ? other.getEnquiryMethod() == null : this.getEnquiryMethod().equals(other.getEnquiryMethod()))
            && (this.getDropReason() == null ? other.getDropReason() == null : this.getDropReason().equals(other.getDropReason()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getActualDate() == null) ? 0 : getActualDate().hashCode());
        result = prime * result + ((getEnquiryNo() == null) ? 0 : getEnquiryNo().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getOrders() == null) ? 0 : getOrders().hashCode());
        result = prime * result + ((getFactors() == null) ? 0 : getFactors().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRequestType() == null) ? 0 : getRequestType().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getStrategy() == null) ? 0 : getStrategy().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getOfferCount() == null) ? 0 : getOfferCount().hashCode());
        result = prime * result + ((getEnquiryMethod() == null) ? 0 : getEnquiryMethod().hashCode());
        result = prime * result + ((getDropReason() == null) ? 0 : getDropReason().hashCode());
        return result;
    }
    
    @Transient
    private List offerList;
    
    @Transient
    private String custName;
    
    @Transient
    private String factorName;
    
    @Transient
    private String businStatusText;
    
    /*为适应票据版询价价列表查询，**/
    @Transient
    private Object order;

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public List getOfferList() {
        return offerList;
    }

    public void setOfferList(List offerList) {
        this.offerList = offerList;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public String getBusinStatusText() {
        return businStatusText;
    }

    public void setBusinStatusText(String businStatusText) {
        this.businStatusText = businStatusText;
    }

    public void init() {
        this.id=SerialGenerator.getLongValue("ScfEnquiry.id");
        this.enquiryNo = BetterDateUtils.getNumDate()+getId().toString();
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.offerCount = 0;
        this.businStatus = "0";
        if(BetterStringUtils.isBlank(this.actualDate)){
            this.actualDate = BetterDateUtils.getNumDate();
        }
    }

    public void initModify() {
        WorkUserInfo user = UserUtils.getUser();
        this.modiOperId = user.getId();
        this.modiOperName = UserUtils.getUserName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }

}