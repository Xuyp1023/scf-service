package com.betterjr.modules.loan.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_pay_record")
public class ScfPayRecord implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="BIGINT" )
    @MetaData( value="", comments = "")
    @OrderBy("desc")
    private Long id;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO",  columnDefinition="BIGINT" )
    @MetaData( value="保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 还款人编号
     */
    @Column(name = "L_PAY_CUSTNO",  columnDefinition="BIGINT" )
    @MetaData( value="还款人编号", comments = "还款人编号")
    private Long payCustNo;

    /**
     * 申请单号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请单号", comments = "申请单号")
    private String requestNo;

    /**
     * 还款计划id
     */
    @Column(name = "L_PAYPLAN_ID",  columnDefinition="BIGINT" )
    @MetaData( value="还款计划id", comments = "还款计划id")
    private Long payPlanId;

    /**
     * 还款日期
     */
    @Column(name = "D_PAY_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="还款日期", comments = "还款日期")
    private String payDate;

    /**
     * 计划还款日期
     */
    @Column(name = "D_PLAN_PAY_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="计划还款日期", comments = "计划还款日期")
    private String planPayDate;

    /**
     * 计算开始日期
     */
    @Column(name = "D_START_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="计算开始日期", comments = "计算开始日期")
    private String dStartDate;

    /**
     * 还款本金
     */
    @Column(name = "F_PRINCIPAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款本金", comments = "还款本金")
    private BigDecimal principalBalance;

    /**
     * 还款利息
     */
    @Column(name = "F_INTEREST_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款利息", comments = "还款利息")
    private BigDecimal interestBalance;

    /**
     * 逾期天数
     */
    @Column(name = "L_OVERDUE_DAYS",  columnDefinition="INT" )
    @MetaData( value="逾期天数", comments = "逾期天数")
    private Integer overdueDays;

    /**
     * 还款管理费
     */
    @Column(name = "F_MANAGEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款管理费", comments = "还款管理费")
    private BigDecimal managementBalance;

    /**
     * 还款罚息
     */
    @Column(name = "F_PENALTY_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款罚息", comments = "还款罚息")
    private BigDecimal penaltyBalance;

    /**
     * 还款滞纳金
     */
    @Column(name = "F_LATEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款滞纳金", comments = "还款滞纳金")
    private BigDecimal latefeeBalance;

    /**
     * 还款提前还款手续费
     */
    @Column(name = "F_SERVICEFEE_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="还款提前还款手续费", comments = "还款提前还款手续费")
    private BigDecimal servicefeeBalance;

    /**
     * 合计
     */
    @Column(name = "F_TOTAL_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="合计", comments = "合计")
    private BigDecimal totalBalance;

    /**
     * 融资利率
     */
    @Column(name = "F_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="融资利率", comments = "融资利率")
    private BigDecimal ratio;

    /**
     * 管理费利率
     */
    @Column(name = "F_MANAGEMENT_RATIO",  columnDefinition="DOUBLE" )
    @MetaData( value="管理费利率", comments = "管理费利率")
    private BigDecimal managementRatio;

    /**
     * 还款类型：1：正常还款，2：提前还款，3：逾期还款，4：豁免，5：逾期豁免，6.经销商还款 7展期
     */
    @Column(name = "C_PAY_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="还款类型：1：正常还款", comments = "还款类型：1：正常还款，2：提前还款，3：逾期还款，4：豁免，5：逾期豁免，6.经销商还款, 7展期")
    private String payType;

    /**
     * 发货通知单id,多个以逗号隔开
     */
    @Column(name = "C_DELIVERYS",  columnDefinition="VARCHAR" )
    @MetaData( value="发货通知单id,多个以逗号隔开", comments = "发货通知单id,多个以逗号隔开")
    private String deliverys;

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

    private static final long serialVersionUID = 1471350583041L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public Long getPayCustNo() {
        return payCustNo;
    }

    public void setPayCustNo(Long payCustNo) {
        this.payCustNo = payCustNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Long getPayPlanId() {
        return payPlanId;
    }

    public void setPayPlanId(Long payPlanId) {
        this.payPlanId = payPlanId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPlanPayDate() {
        return planPayDate;
    }

    public void setPlanPayDate(String planPayDate) {
        this.planPayDate = planPayDate;
    }

    public String getdStartDate() {
        return dStartDate;
    }

    public void setdStartDate(String dStartDate) {
        this.dStartDate = dStartDate == null ? null : dStartDate.trim();
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getManagementBalance() {
        return managementBalance;
    }

    public void setManagementBalance(BigDecimal managementBalance) {
        this.managementBalance = managementBalance;
    }

    public BigDecimal getPenaltyBalance() {
        return penaltyBalance;
    }

    public void setPenaltyBalance(BigDecimal penaltyBalance) {
        this.penaltyBalance = penaltyBalance;
    }

    public BigDecimal getLatefeeBalance() {
        return latefeeBalance;
    }

    public void setLatefeeBalance(BigDecimal latefeeBalance) {
        this.latefeeBalance = latefeeBalance;
    }

    public BigDecimal getServicefeeBalance() {
        return servicefeeBalance;
    }

    public void setServicefeeBalance(BigDecimal servicefeeBalance) {
        this.servicefeeBalance = servicefeeBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getManagementRatio() {
        return managementRatio;
    }

    public void setManagementRatio(BigDecimal managementRatio) {
        this.managementRatio = managementRatio;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(String deliverys) {
        this.deliverys = deliverys;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", factorNo=").append(factorNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", payCustNo=").append(payCustNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", payPlanId=").append(payPlanId);
        sb.append(", payDate=").append(payDate);
        sb.append(", planPayDate=").append(planPayDate);
        sb.append(", dStartDate=").append(dStartDate);
        sb.append(", principalBalance=").append(principalBalance);
        sb.append(", interestBalance=").append(interestBalance);
        sb.append(", overdueDays=").append(overdueDays);
        sb.append(", managementBalance=").append(managementBalance);
        sb.append(", penaltyBalance=").append(penaltyBalance);
        sb.append(", latefeeBalance=").append(latefeeBalance);
        sb.append(", servicefeeBalance=").append(servicefeeBalance);
        sb.append(", totalBalance=").append(totalBalance);
        sb.append(", ratio=").append(ratio);
        sb.append(", managementRatio=").append(managementRatio);
        sb.append(", payType=").append(payType);
        sb.append(", deliverys=").append(deliverys);
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
        ScfPayRecord other = (ScfPayRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFactorNo() == null ? other.getFactorNo() == null : this.getFactorNo().equals(other.getFactorNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getPayCustNo() == null ? other.getPayCustNo() == null : this.getPayCustNo().equals(other.getPayCustNo()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getPayPlanId() == null ? other.getPayPlanId() == null : this.getPayPlanId().equals(other.getPayPlanId()))
            && (this.getPayDate() == null ? other.getPayDate() == null : this.getPayDate().equals(other.getPayDate()))
            && (this.getPlanPayDate() == null ? other.getPlanPayDate() == null : this.getPlanPayDate().equals(other.getPlanPayDate()))
            && (this.getdStartDate() == null ? other.getdStartDate() == null : this.getdStartDate().equals(other.getdStartDate()))
            && (this.getPrincipalBalance() == null ? other.getPrincipalBalance() == null : this.getPrincipalBalance().equals(other.getPrincipalBalance()))
            && (this.getInterestBalance() == null ? other.getInterestBalance() == null : this.getInterestBalance().equals(other.getInterestBalance()))
            && (this.getOverdueDays() == null ? other.getOverdueDays() == null : this.getOverdueDays().equals(other.getOverdueDays()))
            && (this.getManagementBalance() == null ? other.getManagementBalance() == null : this.getManagementBalance().equals(other.getManagementBalance()))
            && (this.getPenaltyBalance() == null ? other.getPenaltyBalance() == null : this.getPenaltyBalance().equals(other.getPenaltyBalance()))
            && (this.getLatefeeBalance() == null ? other.getLatefeeBalance() == null : this.getLatefeeBalance().equals(other.getLatefeeBalance()))
            && (this.getServicefeeBalance() == null ? other.getServicefeeBalance() == null : this.getServicefeeBalance().equals(other.getServicefeeBalance()))
            && (this.getTotalBalance() == null ? other.getTotalBalance() == null : this.getTotalBalance().equals(other.getTotalBalance()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getManagementRatio() == null ? other.getManagementRatio() == null : this.getManagementRatio().equals(other.getManagementRatio()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getDeliverys() == null ? other.getDeliverys() == null : this.getDeliverys().equals(other.getDeliverys()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFactorNo() == null) ? 0 : getFactorNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getPayCustNo() == null) ? 0 : getPayCustNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getPayPlanId() == null) ? 0 : getPayPlanId().hashCode());
        result = prime * result + ((getPayDate() == null) ? 0 : getPayDate().hashCode());
        result = prime * result + ((getPlanPayDate() == null) ? 0 : getPlanPayDate().hashCode());
        result = prime * result + ((getdStartDate() == null) ? 0 : getdStartDate().hashCode());
        result = prime * result + ((getPrincipalBalance() == null) ? 0 : getPrincipalBalance().hashCode());
        result = prime * result + ((getInterestBalance() == null) ? 0 : getInterestBalance().hashCode());
        result = prime * result + ((getOverdueDays() == null) ? 0 : getOverdueDays().hashCode());
        result = prime * result + ((getManagementBalance() == null) ? 0 : getManagementBalance().hashCode());
        result = prime * result + ((getPenaltyBalance() == null) ? 0 : getPenaltyBalance().hashCode());
        result = prime * result + ((getLatefeeBalance() == null) ? 0 : getLatefeeBalance().hashCode());
        result = prime * result + ((getServicefeeBalance() == null) ? 0 : getServicefeeBalance().hashCode());
        result = prime * result + ((getTotalBalance() == null) ? 0 : getTotalBalance().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getManagementRatio() == null) ? 0 : getManagementRatio().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getDeliverys() == null) ? 0 : getDeliverys().hashCode());
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
        return result;
    }
    
    public void init() {
        this.id = SerialGenerator.getLongValue("ScfPayRecord.id");
        this.regOperName = UserUtils.getUserName();
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
    }

    public void initModify() {
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getUserName();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }
    
}