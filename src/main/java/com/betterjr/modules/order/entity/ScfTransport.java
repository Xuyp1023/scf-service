package com.betterjr.modules.order.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_TRANSPORT")
public class ScfTransport implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;

    /**
     * 运输单据编号
     */
    @Column(name = "C_TRANSPORTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="运输单据编号", comments = "运输单据编号")
    private String transportNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 操作员编码
     */
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="操作员编码", comments = "操作员编码")
    private Long regOperId ;

    /**
     * 操作员名字
     */
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员名字", comments = "操作员名字")
    private String regOperName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="编辑操作员编码", comments = "编辑操作员编码")
    private Long modiOperId ;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑操作员名字", comments = "编辑操作员名字")
    private String modiOperName;

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
     * 发货人
     */
    @Column(name = "C_SUPPLIER",  columnDefinition="VARCHAR" )
    @MetaData( value="发货人", comments = "发货人")
    private String supplier;

    /**
     * 收货人
     */
    @Column(name = "C_BUYER",  columnDefinition="VARCHAR" )
    @MetaData( value="收货人", comments = "收货人")
    private String buyer;

    /**
     * 发货日期
     */
    @Column(name = "D_SENDDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="发货日期", comments = "发货日期")
    private String sendDate;

    /**
     * 收货日期
     */
    @Column(name = "D_RECEIVEDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="收货日期", comments = "收货日期")
    private String receiveDate;

    /**
     * 发货地址
     */
    @Column(name = "C_SENDADDRESS",  columnDefinition="VARCHAR" )
    @MetaData( value="发货地址", comments = "发货地址")
    private String sendAddress;

    /**
     * 收货地址
     */
    @Column(name = "C_REVEIVEADDRESS",  columnDefinition="VARCHAR" )
    @MetaData( value="收货地址", comments = "收货地址")
    private String receiveAddress;

    /**
     * 备注
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    /**
     * 上传的批次号，对应FILEINFO中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应FILEINFO中的ID")
    private Long batchNo;

    private static final long serialVersionUID = -4283600989266428293L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo == null ? null : transportNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getRegOperId () {
        return regOperId ;
    }

    public void setRegOperId (Long regOperId ) {
        this.regOperId  = regOperId ;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate == null ? null : sendDate.trim();
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate == null ? null : receiveDate.trim();
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress == null ? null : sendAddress.trim();
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress == null ? null : receiveAddress.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", transportNo=").append(transportNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", regOperId =").append(regOperId );
        sb.append(", regOperName=").append(regOperName);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId =").append(modiOperId );
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", supplier=").append(supplier);
        sb.append(", buyer=").append(buyer);
        sb.append(", sendDate=").append(sendDate);
        sb.append(", receiveDate=").append(receiveDate);
        sb.append(", sendAddress=").append(sendAddress);
        sb.append(", receiveAddress=").append(receiveAddress);
        sb.append(", description=").append(description);
        sb.append(", batchNo=").append(batchNo);
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
        ScfTransport other = (ScfTransport) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTransportNo() == null ? other.getTransportNo() == null : this.getTransportNo().equals(other.getTransportNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getRegOperId () == null ? other.getRegOperId () == null : this.getRegOperId ().equals(other.getRegOperId ()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId () == null ? other.getModiOperId () == null : this.getModiOperId ().equals(other.getModiOperId ()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getSupplier() == null ? other.getSupplier() == null : this.getSupplier().equals(other.getSupplier()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getSendDate() == null ? other.getSendDate() == null : this.getSendDate().equals(other.getSendDate()))
            && (this.getReceiveDate() == null ? other.getReceiveDate() == null : this.getReceiveDate().equals(other.getReceiveDate()))
            && (this.getSendAddress() == null ? other.getSendAddress() == null : this.getSendAddress().equals(other.getSendAddress()))
            && (this.getReceiveAddress() == null ? other.getReceiveAddress() == null : this.getReceiveAddress().equals(other.getReceiveAddress()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTransportNo() == null) ? 0 : getTransportNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getRegOperId () == null) ? 0 : getRegOperId ().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId () == null) ? 0 : getModiOperId ().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getSupplier() == null) ? 0 : getSupplier().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getSendDate() == null) ? 0 : getSendDate().hashCode());
        result = prime * result + ((getReceiveDate() == null) ? 0 : getReceiveDate().hashCode());
        result = prime * result + ((getSendAddress() == null) ? 0 : getSendAddress().hashCode());
        result = prime * result + ((getReceiveAddress() == null) ? 0 : getReceiveAddress().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        return result;
    }

    public void initAddValue() {
        this.id = SerialGenerator.getLongValue("ScfTransport.id");
        this.regOperId = UserUtils.getOperatorInfo().getId();
        this.regOperName = UserUtils.getOperatorInfo().getName();
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.operOrg = UserUtils.getOperatorInfo().getOperOrg();
        
    }
}