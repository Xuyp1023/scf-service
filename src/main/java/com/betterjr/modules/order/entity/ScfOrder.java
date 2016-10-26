package com.betterjr.modules.order.entity;

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
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ORDER")
public class ScfOrder implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "C_ORDERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="订单编号", comments = "订单编号")
    @OrderBy("ASC")
    private String orderNo;

    /**
     * 资金系统中订单编号
     */
    @Column(name = "C_BT_ORDERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="资金系统中订单编号", comments = "资金系统中订单编号")
    @JsonIgnore
    private String btOrderNo;

    /**
     * 客户资金系统编号
     */
    @Column(name = "C_BTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户资金系统编号", comments = "客户资金系统编号")
    @JsonIgnore
    private String btNo;

    /**
     * 客户号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户号", comments = "客户号")
    private Long custNo;
    
    @Transient
    private String custName;

    /**
     * 订单类型  0:供应商订单 1:经销商订单
     */
    @Column(name = "C_ORDER_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="订单类型  0:供应商订单 1:经销商订单", comments = "订单类型  0:供应商订单 1:经销商订单")
    private String orderType;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 商品名称
     */
    @Column(name = "C_GOODSNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="商品名称", comments = "商品名称")
    private String goodsName;

    /**
     * 订单日期
     */
    @Column(name = "D_ORDER_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="订单日期", comments = "订单日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    @OrderBy("ASC")
    private String orderDate;

    /**
     * 到期日期
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="到期日期", comments = "到期日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String endDate;

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
     * 订单总额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="订单总额", comments = "订单总额")
    private BigDecimal balance;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;
    
    @Transient
    private String coreCustName;

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
     * 结算方式 0:商业汇票 1:赊销
     */
    @Column(name = "C_SETTLEMENT",  columnDefinition="VARCHAR" )
    @MetaData( value="结算方式 0:商业汇票 1:赊销", comments = "结算方式 0:商业汇票 1:赊销")
    private String settlement;

    /**
     * 当前状态 0:可用 1:过期 2:冻结
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="当前状态 0:可用 1:过期 2:冻结", comments = "当前状态 0:可用 1:过期 2:冻结")
    private String businStatus;

    /**
     * 融资编号
     */
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="融资编号", comments = "融资编号")
    private String requestNo;

    /**
     * 订单附件，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="订单附件", comments = "订单附件，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 其他资料，对应fileinfo中的ID
     */
    @Column(name = "N_OTHERBATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="其他资料", comments = "其他资料，对应fileinfo中的ID")
    private Long otherBatchNo;

    /**
     * 融资占用金额
     */
    @Column(name = "F_BALANCE_USED",  columnDefinition="DOUBLE" )
    @MetaData( value="融资占用金额", comments = "融资占用金额")
    private BigDecimal balanceUsed;

    /**
     * 剩余金额
     */
    @Column(name = "F_BALANCE_REST",  columnDefinition="DOUBLE" )
    @MetaData( value="剩余金额", comments = "剩余金额")
    private BigDecimal balanceRest;
    
    /**
     * 数据来源, 0:关联单据默认生成, 1:用户录入
     */
    @Column(name = "C_DATASOURCE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据来源, 0:关联单据默认生成, 1:用户录入", comments = "数据来源, 0:关联单据默认生成, 1:用户录入")
    private String dataSource;

    /**
     * 合同列表
     */
    @Transient
    private List<CustAgreement> agreementList;
    
    /**
     * 发票列表
     */
    @Transient
    private List<ScfInvoice> invoiceList;
    /**
     * 应收账款列表
     */
    @Transient
    private List<ScfReceivable> receivableList;
    
    /**
     * 运输单据列表
     */
    @Transient
    private List<ScfTransport> transportList;
    
    /**
     * 汇票信息列表
     */
    @Transient
    private List<ScfAcceptBill> acceptBillList;
    
    
    private static final long serialVersionUID = 1469676107640L;
    
    public ScfOrder() {
        super();
    }

    /**
     * 根据汇票信息生成订单
     */
    public ScfOrder(ScfAcceptBill anAcceptBill) {
        super();
        this.initAddValue(UserUtils.getOperatorInfo());
        this.custNo = anAcceptBill.getHolderNo();
        //暂用开票日期作为订单日期
        this.orderDate = anAcceptBill.getInvoiceDate();
        this.orderNo = "此订单由票据编号" + anAcceptBill.getBillNo() + "的汇票默认生成";
        this.goodsName = "此订单由票据编号" + anAcceptBill.getBillNo() + "的汇票默认生成";
        this.balance = anAcceptBill.getBalance();
        this.coreCustNo = anAcceptBill.getCoreCustNo();
        this.settlement = "0";//结算方式 0-商业汇票
        this.businStatus = "0";
        this.dataSource = "0";
    }
    /**
     * 根据应收账款生成订单
     */
    public ScfOrder(ScfReceivable anReceivable) {
        super();
        this.initAddValue(UserUtils.getOperatorInfo());
        this.coreCustNo = anReceivable.getCustNo();
        this.custNo = anReceivable.getCustNo();
        //暂用数据生成日期作为订单日期
        this.orderDate = anReceivable.getRegDate();
        this.orderNo = "此订单由应收账款编号" + anReceivable.getReceivableNo() + "的应收账款默认生成";
        this.goodsName = anReceivable.getGoodsName();
        this.balance = anReceivable.getBalance();
        this.coreCustNo = anReceivable.getCoreCustNo();
        this.settlement = "1";//结算方式 1-赊销
        this.businStatus = "0";
        this.dataSource = "0";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getBtOrderNo() {
        return btOrderNo;
    }

    public void setBtOrderNo(String btOrderNo) {
        this.btOrderNo = btOrderNo == null ? null : btOrderNo.trim();
    }

    public String getBtNo() {
        return btNo;
    }

    public void setBtNo(String btNo) {
        this.btNo = btNo == null ? null : btNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate == null ? null : orderDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
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

    public Long getCoreCustNo() {
        return coreCustNo;
    }

    public void setCoreCustNo(Long coreCustNo) {
        this.coreCustNo = coreCustNo;
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

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement == null ? null : settlement.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Long getOtherBatchNo() {
        return otherBatchNo;
    }

    public void setOtherBatchNo(Long otherBatchNo) {
        this.otherBatchNo = otherBatchNo;
    }

    public BigDecimal getBalanceUsed() {
        return balanceUsed;
    }

    public void setBalanceUsed(BigDecimal balanceUsed) {
        this.balanceUsed = balanceUsed;
    }

    public BigDecimal getBalanceRest() {
        return balanceRest;
    }

    public void setBalanceRest(BigDecimal balanceRest) {
        this.balanceRest = balanceRest;
    }

    public List<ScfInvoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<ScfInvoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public List<ScfReceivable> getReceivableList() {
        return receivableList;
    }

    public void setReceivableList(List<ScfReceivable> receivableList) {
        this.receivableList = receivableList;
    }

    public List<ScfTransport> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<ScfTransport> transportList) {
        this.transportList = transportList;
    }

    public List<ScfAcceptBill> getAcceptBillList() {
        return acceptBillList;
    }

    public void setAcceptBillList(List<ScfAcceptBill> acceptBillList) {
        this.acceptBillList = acceptBillList;
    }

    public List<CustAgreement> getAgreementList() {
        return this.agreementList;
    }

    public void setAgreementList(List<CustAgreement> anAgreementList) {
        this.agreementList = anAgreementList;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String anDataSource) {
        this.dataSource = anDataSource;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", btOrderNo=").append(btOrderNo);
        sb.append(", btNo=").append(btNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", orderType=").append(orderType);
        sb.append(", regDate=").append(regDate);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", unit=").append(unit);
        sb.append(", amount=").append(amount);
        sb.append(", balance=").append(balance);
        sb.append(", coreCustNo=").append(coreCustNo);
        sb.append(", modiOperId =").append(modiOperId );
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", settlement=").append(settlement);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", otherBatchNo=").append(otherBatchNo);
        sb.append(", balanceUsed=").append(balanceUsed);
        sb.append(", balanceRest=").append(balanceRest);
        sb.append(", dataSource=").append(dataSource);
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
        ScfOrder other = (ScfOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getBtOrderNo() == null ? other.getBtOrderNo() == null : this.getBtOrderNo().equals(other.getBtOrderNo()))
            && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getOrderType() == null ? other.getOrderType() == null : this.getOrderType().equals(other.getOrderType()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
            && (this.getOrderDate() == null ? other.getOrderDate() == null : this.getOrderDate().equals(other.getOrderDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getCoreCustNo() == null ? other.getCoreCustNo() == null : this.getCoreCustNo().equals(other.getCoreCustNo()))
            && (this.getModiOperId () == null ? other.getModiOperId () == null : this.getModiOperId ().equals(other.getModiOperId ()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getSettlement() == null ? other.getSettlement() == null : this.getSettlement().equals(other.getSettlement()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getOtherBatchNo() == null ? other.getOtherBatchNo() == null : this.getOtherBatchNo().equals(other.getOtherBatchNo()))
            && (this.getBalanceUsed() == null ? other.getBalanceUsed() == null : this.getBalanceUsed().equals(other.getBalanceUsed()))
            && (this.getDataSource() == null ? other.getDataSource() == null : this.getDataSource().equals(other.getDataSource()))
            && (this.getBalanceRest() == null ? other.getBalanceRest() == null : this.getBalanceRest().equals(other.getBalanceRest()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getBtOrderNo() == null) ? 0 : getBtOrderNo().hashCode());
        result = prime * result + ((getBtNo() == null) ? 0 : getBtNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getOrderType() == null) ? 0 : getOrderType().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getOrderDate() == null) ? 0 : getOrderDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getCoreCustNo() == null) ? 0 : getCoreCustNo().hashCode());
        result = prime * result + ((getModiOperId () == null) ? 0 : getModiOperId ().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getSettlement() == null) ? 0 : getSettlement().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getOtherBatchNo() == null) ? 0 : getOtherBatchNo().hashCode());
        result = prime * result + ((getBalanceUsed() == null) ? 0 : getBalanceUsed().hashCode());
        result = prime * result + ((getDataSource() == null) ? 0 : getDataSource().hashCode());
        result = prime * result + ((getBalanceRest() == null) ? 0 : getBalanceRest().hashCode());
        return result;
    }
    
    /**
     * 订单信息编辑数据保存
     */
    public void initModifyValue(CustOperatorInfo anOperatorInfo) {
        if (null != anOperatorInfo) {
            this.modiOperId = anOperatorInfo.getId();
            this.modiOperName = anOperatorInfo.getName();
        }
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
    }
    
    /**
     * 清空订单关联信息
     */
    public void clearRelationInfo(){
        this.agreementList.clear();
        this.transportList.clear();
        this.invoiceList.clear();
        this.acceptBillList.clear();
        this.receivableList.clear();
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo) {
        this.id = SerialGenerator.getLongValue("ScfOrder.id");
        this.businStatus = "0";
        this.dataSource = "1";
        if (null != anOperatorInfo) {
            this.modiOperId = anOperatorInfo.getId();
            this.modiOperName = anOperatorInfo.getName();
            this.operOrg = anOperatorInfo.getOperOrg();
        }
        this.regDate = BetterDateUtils.getNumDate();
    }
}