package com.betterjr.modules.receivable.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_RECEIVABLE")
public class ScfReceivable implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;

    /**
     * 应收账款编号
     */
    @Column(name = "C_RECEIVABLE_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="应收账款编号", comments = "应收账款编号")
    private String receivableNo;

    /**
     * 客户资金系统编号
     */
    @Column(name = "C_BTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户资金系统编号", comments = "客户资金系统编号")
    @JsonIgnore
    private String btNo;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    @JsonIgnore
    private Long coreCustNo;

    /**
     * 客户号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户号", comments = "客户号")
    private Long custNo;

    /**
     * 债权人
     */
    @Column(name = "C_CREDITOR",  columnDefinition="VARCHAR" )
    @MetaData( value="债权人", comments = "债权人")
    private String creditor;

    /**
     * 债务人
     */
    @Column(name = "C_DEBTOR",  columnDefinition="VARCHAR" )
    @MetaData( value="债务人", comments = "债务人")
    private String debtor;

    /**
     * 商品名称
     */
    @Column(name = "C_GOODSNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="商品名称", comments = "商品名称")
    private String goodsName;

    /**
     * 商品价格
     */
    @Column(name = "F_UNIT",  columnDefinition="DOUBLE" )
    @MetaData( value="商品价格", comments = "商品价格")
    private BigDecimal unit;

    /**
     * 采购数量
     */
    @Column(name = "N_AMOUNT",  columnDefinition="DOUBLE" )
    @MetaData( value="采购数量", comments = "采购数量")
    private Integer amount;

    /**
     * 金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="金额", comments = "金额")
    private BigDecimal balance;

    /**
     * 付款到期日期
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="付款到期日期", comments = "付款到期日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String endDate;

    /**
     * 数据创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据创建日期", comments = "数据创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="编辑操作员编码", comments = "编辑操作员编码")
    @JsonIgnore
    private Long modiOperId ;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑操作员名字", comments = "编辑操作员名字")
    @JsonIgnore
    private String modiOperName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 编辑日期
     */
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑日期", comments = "编辑日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 编辑时间
     */
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑时间", comments = "编辑时间")
    private String modiTime;

    /**
     * 当前状态 0:可用 1:过期 2:冻结
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="当前状态 0:可用 1:过期 2:冻结", comments = "当前状态 0:可用 1:过期 2:冻结")
    private String businStatus;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    private static final long serialVersionUID = 1469676107642L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceivableNo() {
        return receivableNo;
    }

    public void setReceivableNo(String receivableNo) {
        this.receivableNo = receivableNo == null ? null : receivableNo.trim();
    }

    public String getBtNo() {
        return btNo;
    }

    public void setBtNo(String btNo) {
        this.btNo = btNo == null ? null : btNo.trim();
    }

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor == null ? null : creditor.trim();
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor == null ? null : debtor.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public BigDecimal getUnit() {
        return unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public Long getModiOperId () {
        return modiOperId ;
    }

    public void setModiOperId (Long modiOperId ) {
        this.modiOperId  = modiOperId ;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
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

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", receivableNo=").append(receivableNo);
        sb.append(", btNo=").append(btNo);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", creditor=").append(creditor);
        sb.append(", debtor=").append(debtor);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", unit=").append(unit);
        sb.append(", amount=").append(amount);
        sb.append(", balance=").append(balance);
        sb.append(", endDate=").append(endDate);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiOperId =").append(modiOperId );
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", description=").append(description);
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
        ScfReceivable other = (ScfReceivable) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getReceivableNo() == null ? other.getReceivableNo() == null : this.getReceivableNo().equals(other.getReceivableNo()))
            && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getCreditor() == null ? other.getCreditor() == null : this.getCreditor().equals(other.getCreditor()))
            && (this.getDebtor() == null ? other.getDebtor() == null : this.getDebtor().equals(other.getDebtor()))
            && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
            && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiOperId () == null ? other.getModiOperId () == null : this.getModiOperId ().equals(other.getModiOperId ()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getReceivableNo() == null) ? 0 : getReceivableNo().hashCode());
        result = prime * result + ((getBtNo() == null) ? 0 : getBtNo().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCreditor() == null) ? 0 : getCreditor().hashCode());
        result = prime * result + ((getDebtor() == null) ? 0 : getDebtor().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiOperId () == null) ? 0 : getModiOperId ().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    /**
     * 应收账款信息变更迁移初始化
     */
    public void initModifyValue(ScfReceivable anReceivable) {
        this.id = anReceivable.getId();
        this.btNo = anReceivable.getBtNo();
        this.businStatus = anReceivable.getBusinStatus();
        this.regDate = anReceivable.getRegDate();
        this.custNo = anReceivable.getCustNo();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        
        this.coreCustNo = anReceivable.getCoreCustNo();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiOperId = UserUtils.getOperatorInfo().getId();
        this.modiOperName = UserUtils.getOperatorInfo().getName();
        this.modiTime = BetterDateUtils.getNumTime();
    }
}