package com.betterjr.modules.order.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.entity.BaseVersionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_ORDER_V3")
public class ScfOrderDO extends BaseVersionEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 8629619120582427923L;
    
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
    
    /**
     * 客户企业名称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户企业名称", comments = "客户企业名称")
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
    
    /**
     * 核心企业名称
     */
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业名称", comments = "核心企业名称")
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
     * 订单附件，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="订单附件", comments = "订单附件，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 数据来源, 0:关联单据默认生成, 1:用户录入
     */
    @Column(name = "C_DATASOURCE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据来源, 0:关联单据默认生成, 1:用户录入", comments = "数据来源, 0:关联单据默认生成, 1:用户录入")
    private String dataSource;
    
    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String anOrderNo) {
        this.orderNo = anOrderNo;
    }

    public String getBtOrderNo() {
        return this.btOrderNo;
    }

    public void setBtOrderNo(String anBtOrderNo) {
        this.btOrderNo = anBtOrderNo;
    }

    public String getBtNo() {
        return this.btNo;
    }

    public void setBtNo(String anBtNo) {
        this.btNo = anBtNo;
    }

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public void setOrderType(String anOrderType) {
        this.orderType = anOrderType;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String anGoodsName) {
        this.goodsName = anGoodsName;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(String anOrderDate) {
        this.orderDate = anOrderDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    public BigDecimal getUnit() {
        return this.unit;
    }

    public void setUnit(BigDecimal anUnit) {
        this.unit = anUnit;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer anAmount) {
        this.amount = anAmount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }

    public String getSettlement() {
        return this.settlement;
    }

    public void setSettlement(String anSettlement) {
        this.settlement = anSettlement;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String anDataSource) {
        this.dataSource = anDataSource;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public ScfOrderDO() {
        super();
    }

    @Override
    public String toString() {
        return "ScfOrderDO [orderNo=" + this.orderNo + ", btOrderNo=" + this.btOrderNo + ", btNo=" + this.btNo + ", custNo=" + this.custNo
                + ", custName=" + this.custName + ", orderType=" + this.orderType + ", regDate=" + this.regDate + ", goodsName=" + this.goodsName
                + ", orderDate=" + this.orderDate + ", endDate=" + this.endDate + ", unit=" + this.unit + ", amount=" + this.amount + ", balance="
                + this.balance + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", modiOperId=" + this.modiOperId
                + ", modiOperName=" + this.modiOperName + ", operOrg=" + this.operOrg + ", modiDate=" + this.modiDate + ", modiTime=" + this.modiTime
                + ", settlement=" + this.settlement + ", batchNo=" + this.batchNo + ", dataSource=" + this.dataSource + ", description="
                + this.description + ", getRefNo()=" + this.getRefNo() + ", getVersion()=" + this.getVersion() + ", getIsLatest()="
                + this.getIsLatest() + ", getBusinStatus()=" + this.getBusinStatus() + ", getLockedStatus()=" + this.getLockedStatus()
                + ", getDocStatus()=" + this.getDocStatus() + ", getAuditOperId()=" + this.getAuditOperId() + ", getAuditOperName()="
                + this.getAuditOperName() + ", getAuditData()=" + this.getAuditData() + ", getAuditTime()=" + this.getAuditTime() + ", getId()="
                + this.getId() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.btNo == null) ? 0 : this.btNo.hashCode());
        result = prime * result + ((this.btOrderNo == null) ? 0 : this.btOrderNo.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.dataSource == null) ? 0 : this.dataSource.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.goodsName == null) ? 0 : this.goodsName.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.orderDate == null) ? 0 : this.orderDate.hashCode());
        result = prime * result + ((this.orderNo == null) ? 0 : this.orderNo.hashCode());
        result = prime * result + ((this.orderType == null) ? 0 : this.orderType.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.settlement == null) ? 0 : this.settlement.hashCode());
        result = prime * result + ((this.unit == null) ? 0 : this.unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfOrderDO other = (ScfOrderDO) obj;
        if (this.amount == null) {
            if (other.amount != null) return false;
        }
        else if (!this.amount.equals(other.amount)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.btNo == null) {
            if (other.btNo != null) return false;
        }
        else if (!this.btNo.equals(other.btNo)) return false;
        if (this.btOrderNo == null) {
            if (other.btOrderNo != null) return false;
        }
        else if (!this.btOrderNo.equals(other.btOrderNo)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.dataSource == null) {
            if (other.dataSource != null) return false;
        }
        else if (!this.dataSource.equals(other.dataSource)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.endDate == null) {
            if (other.endDate != null) return false;
        }
        else if (!this.endDate.equals(other.endDate)) return false;
        if (this.goodsName == null) {
            if (other.goodsName != null) return false;
        }
        else if (!this.goodsName.equals(other.goodsName)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        }
        else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        }
        else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        }
        else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        }
        else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.orderDate == null) {
            if (other.orderDate != null) return false;
        }
        else if (!this.orderDate.equals(other.orderDate)) return false;
        if (this.orderNo == null) {
            if (other.orderNo != null) return false;
        }
        else if (!this.orderNo.equals(other.orderNo)) return false;
        if (this.orderType == null) {
            if (other.orderType != null) return false;
        }
        else if (!this.orderType.equals(other.orderType)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.settlement == null) {
            if (other.settlement != null) return false;
        }
        else if (!this.settlement.equals(other.settlement)) return false;
        if (this.unit == null) {
            if (other.unit != null) return false;
        }
        else if (!this.unit.equals(other.unit)) return false;
        return true;
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo,boolean confirmFlag) {
        
        BTAssert.notNull(anOperatorInfo,"无法获取登录信息,操作失败");
        this.setId(SerialGenerator.getLongValue("ScfOrderDO.id"));
        this.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        this.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        this.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        if(confirmFlag){
            this.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM);
        }
        this.dataSource = "1";//订单的数据来源是新增
        if (null != anOperatorInfo) {
            this.setModiOperId(anOperatorInfo.getId());
            this.modiOperName = anOperatorInfo.getName();
            this.operOrg = anOperatorInfo.getOperOrg();
        }
        this.regDate = BetterDateUtils.getNumDate();
    }
    
    /**
     * 订单信息编辑数据保存
     */
    public ScfOrderDO initModifyValue(ScfOrderDO anOrder) {
        
        anOrder.setModiDate(BetterDateUtils.getNumDate());
        anOrder.setModiTime(BetterDateUtils.getNumTime());
        anOrder.setOrderNo(this.orderNo);
        anOrder.setOrderDate(this.orderDate);
        anOrder.setGoodsName(this.goodsName);
        anOrder.setBalance(this.balance);
        anOrder.setUnit(this.unit);
        anOrder.setAmount(this.amount);
        anOrder.setCustNo(this.custNo);
        anOrder.setCoreCustNo(this.coreCustNo);
        anOrder.setDescription(this.description);
        return anOrder;
        
    }
    
    

}
