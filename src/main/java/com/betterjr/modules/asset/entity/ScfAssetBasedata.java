package com.betterjr.modules.asset.entity;

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
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_asset_dasedata")
public class ScfAssetBasedata implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;
    
    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @OrderBy("ASC")
    @MetaData( value="创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    private String regTime;
    
    /**
     * 资产表编号
     */
    @Column(name = "L_ASSET_ID",  columnDefinition="INTEGER" )
    @MetaData( value="资产表编号", comments = "资产表编号")
    private Long assetId;
    
    /**
     * 资产基础数据主键id
     */
    @Column(name = "L_RELATION_INFO_ID",  columnDefinition="INTEGER" )
    @MetaData( value="资产基础数据主键id", comments = "资产基础数据主键id")
    private Long infoId;
    
    /**
     * 关联的基础的版本
     */
    @Column(name = "C_RELATION_INFO_VERSION",  columnDefinition="VARCHAR" )
    @MetaData( value="关联的基础的版本", comments = "关联的基础的版本")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String version;
    
    /**
     *基础数据的唯一标识
     */
    @Column(name = "C_RELATION_INFO_REFNO",  columnDefinition="VARCHAR" )
    @MetaData( value="基础数据的唯一标识", comments = "基础数据的唯一标识")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String refNo;
    
    /**
     * 关联的基础数据的类型1订单2票据3应收账款4发票5贸易合同6运输单单据类型
     */
    @Column(name = "C_RELATION_INFO_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="关联的基础数据的类型1订单2票据3应收账款4发票5贸易合同6运输单单据类型", comments = "关联的基础数据的类型1订单2票据3应收账款4发票5贸易合同6运输单单据类型")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String infoType;

    /**
     * 是否关联资产 10 关联  20已经被中止关联
     */
    @Column(name = "C_BUSIN_STATU",  columnDefinition="VARCHAR" )
    @MetaData( value="是否关联资产 10 关联  20已经被中止关联", comments = "是否关联资产 10 关联  20已经被中止关联")
    private String businStatus;
    
    /**
     * 存放订单数据
     */
    @Transient
    private ScfOrder scfOrder=new ScfOrder();
    
    /**
     * 存放发票数据
     */
    @Transient
    private ScfInvoice scfInvoice=new ScfInvoice();
    
    /**
     * 存放运输单数据
     */
    @Transient
    private ScfTransport scfTransport=new ScfTransport();
    
    /**
     * 存放票据单数据
     */
    @Transient
    private ScfAcceptBill scfAcceptBill=new ScfAcceptBill();
    
    /**
     * 存放应收单据数据
     */
    @Transient
    private ScfReceivable scfReceivable=new ScfReceivable();
    
    /**
     * 存放贸易合同数据
     */
    @Transient
    private CustAgreement custAgreement=new CustAgreement();
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public void setAssetId(Long anAssetId) {
        this.assetId = anAssetId;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String anRefNo) {
        this.refNo = anRefNo;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String anInfoType) {
        this.infoType = anInfoType;
    }

    public ScfOrder getScfOrder() {
        return this.scfOrder;
    }

    public void setScfOrder(ScfOrder anScfOrder) {
        this.scfOrder = anScfOrder;
    }

    public ScfInvoice getScfInvoice() {
        return this.scfInvoice;
    }

    public void setScfInvoice(ScfInvoice anScfInvoice) {
        this.scfInvoice = anScfInvoice;
    }

    public ScfTransport getScfTransport() {
        return this.scfTransport;
    }

    public void setScfTransport(ScfTransport anScfTransport) {
        this.scfTransport = anScfTransport;
    }

    public ScfAcceptBill getScfAcceptBill() {
        return this.scfAcceptBill;
    }

    public void setScfAcceptBill(ScfAcceptBill anScfAcceptBill) {
        this.scfAcceptBill = anScfAcceptBill;
    }

    public ScfReceivable getScfReceivable() {
        return this.scfReceivable;
    }

    public void setScfReceivable(ScfReceivable anScfReceivable) {
        this.scfReceivable = anScfReceivable;
    }

    public CustAgreement getCustAgreement() {
        return this.custAgreement;
    }

    public void setCustAgreement(CustAgreement anCustAgreement) {
        this.custAgreement = anCustAgreement;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public Long getInfoId() {
        return this.infoId;
    }

    public void setInfoId(Long anInfoId) {
        this.infoId = anInfoId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.assetId == null) ? 0 : this.assetId.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAssetBasedata other = (ScfAssetBasedata) obj;
        if (this.assetId == null) {
            if (other.assetId != null) return false;
        }
        else if (!this.assetId.equals(other.assetId)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        }
        else if (!this.infoType.equals(other.infoType)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        }
        else if (!this.refNo.equals(other.refNo)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        }
        else if (!this.version.equals(other.version)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfAssetBasedata [Hash = "+hashCode()+", id=" + this.id + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", assetId=" + this.assetId
                + ", version=" + this.version + ", refNo=" + this.refNo + ", businStatus=" + this.businStatus + ", infoType=" + this.infoType + ", serialVersionUID="+serialVersionUID+"]";
    }

    /**
     * 新增资产初始化id 创建时间日期
     */
    public void initAdd() {
        
        this.id=SerialGenerator.getLongValue("ScfAssetBasedata.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.businStatus=AssetConstantCollentions.ASSET_BUSIN_STATUS_OK;
        
    }
    
    


}
