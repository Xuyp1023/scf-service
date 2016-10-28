package com.betterjr.modules.push.entity;

import java.math.BigDecimal;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDecimalJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_SUPPLIER_PUSH_DETAIL")
public class ScfSupplierPushDetail implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;

    @Column(name = "C_ORDERID",  columnDefinition="VARCHAR" )
    private String orderId;

    @Column(name = "C_BUSIN_TYPE",  columnDefinition="VARCHAR" )
    private String businType;

    @Column(name = "D_BEGIN_DATE",  columnDefinition="VARCHAR" )
    private String beginDate;

    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    private String endDate;

    @Column(name = "C_AGENCYNO",  columnDefinition="VARCHAR" )
    private String agencyNo;

    @Column(name = "F_MONEY",  columnDefinition="DOUBLE" )
    private BigDecimal money;

    @Column(name = "F_COSTRATE",  columnDefinition="VARCHAR" )
    private String costRate;

    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;

    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    private String regDate;

    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;

    @Column(name = "D_MODIFY_DATE",  columnDefinition="VARCHAR" )
    private String modifyDate;

    @Column(name = "T_MODIFY_TIME",  columnDefinition="VARCHAR" )
    private String modifyTime;
    
    @Column(name = "C_SENDNO",  columnDefinition="VARCHAR" )
    private String sendNo;
    
    @Column(name = "C_RECEIVENO",  columnDefinition="VARCHAR" )
    private String receiveNo;
    
    @Column(name = "C_REMARK",  columnDefinition="VARCHAR" )
    private String remark;    

    @Transient
    private String custName;
    @Transient
    private String tragetCustName;
    @Transient
    private String acceptor;
    @Transient
    private String disDate;
    @Transient
    private String disMoney;
    @Transient
    private String billNo;
    
    private static final long serialVersionUID = 1473315525514L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBusinType() {
        return businType;
    }

    public void setBusinType(String businType) {
        this.businType = businType == null ? null : businType.trim();
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate == null ? null : beginDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo == null ? null : agencyNo.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCostRate() {
        return costRate;
    }

    public void setCostRate(String costRate) {
        this.costRate = costRate == null ? null : costRate.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
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

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate == null ? null : modifyDate.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getTragetCustName() {
        return this.tragetCustName;
    }

    public void setTragetCustName(String anTragetCustName) {
        this.tragetCustName = anTragetCustName;
    }

    public String getAcceptor() {
        return this.acceptor;
    }

    public void setAcceptor(String anAcceptor) {
        this.acceptor = anAcceptor;
    }

    public String getDisDate() {
        return BetterDateUtils.formatDate(BetterDateUtils.parseDate(this.beginDate), "yyyy年MM月dd日")+"至"+BetterDateUtils.formatDate(BetterDateUtils.parseDate(this.endDate), "yyyy年MM月dd日");
    }

    public void setDisDate(String anDisDate) {
        this.disDate = anDisDate;
    }

    public String getDisMoney() {
        return "￥"+CustDecimalJsonSerializer.format(this.money);
    }

    public void setDisMoney(String anDisMoney) {
        this.disMoney = anDisMoney;
    }

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String anBillNo) {
        this.billNo = anBillNo;
    }

    public String getSendNo() {
        return this.sendNo;
    }

    public void setSendNo(String anSendNo) {
        this.sendNo = anSendNo;
    }

    public String getReceiveNo() {
        return this.receiveNo;
    }

    public void setReceiveNo(String anReceiveNo) {
        this.receiveNo = anReceiveNo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String anRemark) {
        this.remark = anRemark;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", businType=").append(businType);
        sb.append(", beginDate=").append(beginDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", agencyNo=").append(agencyNo);
        sb.append(", money=").append(money);
        sb.append(", costRate=").append(costRate);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", modifyTime=").append(modifyTime);
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
        ScfSupplierPushDetail other = (ScfSupplierPushDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getBusinType() == null ? other.getBusinType() == null : this.getBusinType().equals(other.getBusinType()))
            && (this.getBeginDate() == null ? other.getBeginDate() == null : this.getBeginDate().equals(other.getBeginDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getAgencyNo() == null ? other.getAgencyNo() == null : this.getAgencyNo().equals(other.getAgencyNo()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getCostRate() == null ? other.getCostRate() == null : this.getCostRate().equals(other.getCostRate()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getBusinType() == null) ? 0 : getBusinType().hashCode());
        result = prime * result + ((getBeginDate() == null) ? 0 : getBeginDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getAgencyNo() == null) ? 0 : getAgencyNo().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getCostRate() == null) ? 0 : getCostRate().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        return result;
    }
    

    public void initDefValue(ScfAcceptBill scfAcceptBill){
        this.id=SerialGenerator.getLongValue("pushDetail.id");
        this.orderId=scfAcceptBill.getId()+"";
        this.businType="1";// 0:订单，1票据，2应收账款，3签约提醒
        this.beginDate=scfAcceptBill.getInvoiceDate();
        this.endDate=scfAcceptBill.getEndDate();
        this.money=scfAcceptBill.getBalance();
        this.costRate=scfAcceptBill.getRatio().toString();
        this.businStatus="1";
        this.regDate=BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
        this.modifyDate=BetterDateUtils.getNumDate();
        this.modifyTime=BetterDateUtils.getNumTime();
    }
    
    public void initValue(String anOrderId,String anBusinType){
        this.id=SerialGenerator.getLongValue("pushDetail.id");
        this.orderId=anOrderId;
        this.businType=anBusinType;
        this.businStatus="1";
        this.regDate=BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
        this.modifyDate=BetterDateUtils.getNumDate();
        this.modifyTime=BetterDateUtils.getNumTime();
    }
}