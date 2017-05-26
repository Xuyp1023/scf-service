package com.betterjr.modules.asset.entity;

import java.util.HashMap;
import java.util.Map;

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
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_asset")
public class ScfAsset implements BetterjrEntity {

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
     * 资产状态 10：可用  20 ：不可用
     */
    @Column(name = "C_BUSIN_STATU",  columnDefinition="VARCHAR" )
    @MetaData( value="资产状态 10：可用  20 ：不可用", comments = "资产状态 10：可用  20 ：不可用")
    private String businStatus;

    /**
     * 资产产品名称
     */
    @Column(name = "C_ASSET_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="资产产品名称", comments = "资产产品名称")
    private String assetName;
    
    /**
     * 资产产品类型1:订单类资产 2:票据类资产3:应收账款资产
     */
    @Column(name = "C_ASSET_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="资产产品类型1:订单类资产 2:票据类资产3:应收账款资产", comments = "资产产品类型1:订单类资产 2:票据类资产3:应收账款资产")
    private String assetType;
    
    /**
     * 资产使用用途 1 询价 2 融资
     */
    @Column(name = "C_SOURCE_USE_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="资产使用用途 1 询价 2 融资", comments = "资产使用用途 1 询价 2 融资")
    private String sourceUseType;
    
    /**
     * 债权公司编号(资产所属公司)
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="债权公司编号(资产所属公司)", comments = "债权公司编号(资产所属公司)")
    private Long custNo;
    
    /**
     * 债权公司名称(资产所有者)
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="债权公司名称(资产所有者)", comments = "债权公司名称(资产所有者)")
    private String custName;
    
    /**
     * 债务公司编号(核心企业编号)
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="债务公司编号(核心企业编号)", comments = "债务公司编号(核心企业编号)")
    private Long coreCustNo;
    
    /**
     * 债务公司名称(核心企业名称)
     */
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="债务公司名称(核心企业名称)", comments = "债务公司名称(核心企业名称)")
    private String coreCustName;
    
    /**
     * 上一手资产编号
     */
    @Column(name = "L_PREFIX_ID",  columnDefinition="INTEGER" )
    @MetaData( value="上一手资产编号", comments = "上一手资产编号")
    private Long prefixId;
    
    /**
     * 下一手资产编号
     */
    @Column(name = "L_SUFFIX_ID",  columnDefinition="INTEGER" )
    @MetaData( value="下一手资产编号", comments = "下一手资产编号")
    private Long suffixId;
    
    /**
     * 资产所属方类型 1：供应商 2 经销商
     */
    @Column(name = "C_CUST_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="资产所属方类型 1：供应商 2 经销商", comments = "资产所属方类型 1：供应商 2 经销商")
    private String custType;

    /**
     * 资产公司集合
     */
    @Transient
    /*private List<ScfAssetCompany> companyList=new ArrayList<ScfAssetCompany>();
    private CustInfo custCompany=new CustInfo();//供应商
    private CustInfo coreCustCompany=new CustInfo();//核心企业
    private List<CustInfo> factoryCust*/
    /**
     * 存放资产公司的集合
     * 供应商  key: custInfo  value CustInfo类型
     * 供应商权限 key: custInfoStatus  value Integer类型
     * 核心企业 key : coreCustInfo  value CustInfo类型
     * 核心企业权限 key: coreCustInfoStatus  value Integer类型
     * 保理公司 key： factoryCustInfo  value List<CustInfo>类型
     * 保理公司权限 key: factoryCustInfoStatus  value Integer类型
     */
    private Map<String,Object> custMap=new HashMap<String,Object>();
    
    /**
     * 基础数据集合
     */
    @Transient
    //private List<ScfAssetBasedata> basedataList=new ArrayList<ScfAssetBasedata>();
    /**
     * 存放基础数据集合
     * 订单类型 key: orderList  value: List<ScfOrder>
     * 发票类型key: invoiceList  value:List<ScfInvoice>
     * 合同类型 key: agreementList   value:List<CustAgreement>
     * 应收账款类型 key: receivableList  value:List<ScfReceivable> 
     * 运输单据类型 key: transportList   value:List<ScfTransport> 
     * 汇票类型 key: acceptBillList   value:List<ScfAcceptBill> 
     */
    private Map<String,Object> basedataMap=new HashMap<String,Object>();
    
    /**
     * 此条记录是否权限
     */
    @Transient
    private Integer operationAuth;
    
    public Integer getOperationAuth() {
        return this.operationAuth;
    }

    public void setOperationAuth(Integer anOperationAuth) {
        this.operationAuth = anOperationAuth;
    }

    public Map<String, Object> getBasedataMap() {
        return this.basedataMap;
    }

    public void setBasedataMap(Map<String, Object> anBasedataMap) {
        this.basedataMap = anBasedataMap;
    }

    public Map<String, Object> getCustMap() {
        return this.custMap;
    }

    public void setCustMap(Map<String, Object> anCustMap) {
        this.custMap = anCustMap;
    }

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

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public void setAssetName(String anAssetName) {
        this.assetName = anAssetName;
    }

    public String getAssetType() {
        return this.assetType;
    }

    public void setAssetType(String anAssetType) {
        this.assetType = anAssetType;
    }

    public String getSourceUseType() {
        return this.sourceUseType;
    }

    public void setSourceUseType(String anSourceUseType) {
        this.sourceUseType = anSourceUseType;
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

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public Long getPrefixId() {
        return this.prefixId;
    }

    public void setPrefixId(Long anPrefixId) {
        this.prefixId = anPrefixId;
    }

    public Long getSuffixId() {
        return this.suffixId;
    }

    public void setSuffixId(Long anSuffixId) {
        this.suffixId = anSuffixId;
    }

    public String getCustType() {
        return this.custType;
    }



    public void setCustType(String anCustType) {
        this.custType = anCustType;
    }

    @Override
    public String toString() {
        return "ScfAsset [Hash = "+hashCode()+", id=" + this.id + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", businStatus=" + this.businStatus
                + ", assetName=" + this.assetName + ", assetType=" + this.assetType + ", sourceUseType=" + this.sourceUseType + ", custNo="
                + this.custNo + ", custName=" + this.custName + ", operationAuth=" + this.operationAuth + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName
                + ", prefixId=" + this.prefixId + ", suffixId=" + this.suffixId + ", serialVersionUID="+serialVersionUID+"]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.assetName == null) ? 0 : this.assetName.hashCode());
        result = prime * result + ((this.assetType == null) ? 0 : this.assetType.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.prefixId == null) ? 0 : this.prefixId.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.sourceUseType == null) ? 0 : this.sourceUseType.hashCode());
        result = prime * result + ((this.suffixId == null) ? 0 : this.suffixId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAsset other = (ScfAsset) obj;
        if (this.assetName == null) {
            if (other.assetName != null) return false;
        }
        else if (!this.assetName.equals(other.assetName)) return false;
        if (this.assetType == null) {
            if (other.assetType != null) return false;
        }
        else if (!this.assetType.equals(other.assetType)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
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
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.prefixId == null) {
            if (other.prefixId != null) return false;
        }
        else if (!this.prefixId.equals(other.prefixId)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.sourceUseType == null) {
            if (other.sourceUseType != null) return false;
        }
        else if (!this.sourceUseType.equals(other.sourceUseType)) return false;
        if (this.suffixId == null) {
            if (other.suffixId != null) return false;
        }
        else if (!this.suffixId.equals(other.suffixId)) return false;
        return true;
    }



    /**
     * 初始化资产信息，
     */
    public void initAdd() {

        this.id=SerialGenerator.getLongValue("ScfAsset.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.setBusinStatus(AssetConstantCollentions.ASSET_INFO_CAN_USE);
        this.assetName=this.custName+"的资产";
    }
    
    

}
