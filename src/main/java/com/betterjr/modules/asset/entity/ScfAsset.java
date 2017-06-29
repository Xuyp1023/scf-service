package com.betterjr.modules.asset.entity;

import java.math.BigDecimal;
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
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
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
     * 资产状态 资产状态 0:未生效  1生效  2废止 3 转让 4不可用
     */
    @Column(name = "C_BUSIN_STATU",  columnDefinition="VARCHAR" )
    @MetaData( value="资产状态 0:未生效  1生效  2废止 3 转让 4不可用", comments = "资产状态 0:未生效  1生效  2废止 3 转让 4不可用")
    private String businStatus;

    /**
     * 资产类型
     */
    @Column(name = "C_ASSET_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="资产类型", comments = "资产类型")
    private String businTypeId;
    
    /**
     * 保理产品id
     */
    @Column(name = "C_ASSET_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="保理产品id", comments = "保理产品id")
    private String productCode;
    
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
     * 订单id集合以 ,分割
     */
    @Transient
    private String orderList;
    @Transient
    private String invoiceList;
    @Transient
    private String agreementList;
    @Transient
    private String receivableList;
    @Transient
    private String acceptBillList;
   
    /**
     * 此条记录是否权限
     */
    @Transient
    private Integer operationAuth;
    
    @Transient
    private Long factorNo;
    
    /**
     * 注册操作员编码
     */
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="注册操作员编码", comments = "注册操作员编码")
    private Long regOperId ;

    /**
     * 注册操作员名字
     */
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="注册操作员名字", comments = "注册操作员名字")
    private String regOperName;
    
    /**
     * 作废操作员编码
     */
    @Column(name = "L_ANNUL_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="作废操作员编码", comments = "作废操作员编码")
    private Long annulOperId ;

    /**
     * 作废操作员名字
     */
    @Column(name = "C_ANNUL_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="作废操作员名字", comments = "作废操作员名字")
    private String annulOperName;
    
    /**
     * 作废日期
     */
    @Column(name = "D_ANNUL_DATE",  columnDefinition="VARCHAR" )
    @OrderBy("ASC")
    @MetaData( value="作废日期", comments = "作废日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String annulDate;
    
    /**
     * 作废时间
     */
    @Column(name = "T_ANNUL_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="作废日期", comments = "作废日期")
    @JsonSerialize(using=CustTimeJsonSerializer.class)
    private String annulTime;
    
    /**
     * 上传的批次号，对账单附件
     */
    @Column(name = "N_STATEMENT_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "对账单附件的批次号", comments = "对账单附件的批次号")
    private Long statementBatchNo;
    
    /**
     * 商品出库单附件
     */
    @Column(name = "N_GOODS_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "商品出库单附件", comments = "商品出库单附件")
    private Long goodsBatchNo;
    
    /**
     * 其他附件
     */
    @Column(name = "N_OTHERS_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "其他附件", comments = "其他附件")
    private Long othersBatchNo;
    
    /**
     * 资产总额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="资产总额", comments = "资产总额")
    private BigDecimal balance;
    
    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getRegOperId() {
        return this.regOperId;
    }

    public void setRegOperId(Long anRegOperId) {
        this.regOperId = anRegOperId;
    }

    public String getRegOperName() {
        return this.regOperName;
    }

    public void setRegOperName(String anRegOperName) {
        this.regOperName = anRegOperName;
    }

    public Long getAnnulOperId() {
        return this.annulOperId;
    }

    public void setAnnulOperId(Long anAnnulOperId) {
        this.annulOperId = anAnnulOperId;
    }

    public String getAnnulOperName() {
        return this.annulOperName;
    }

    public void setAnnulOperName(String anAnnulOperName) {
        this.annulOperName = anAnnulOperName;
    }

    public String getAnnulDate() {
        return this.annulDate;
    }

    public void setAnnulDate(String anAnnulDate) {
        this.annulDate = anAnnulDate;
    }

    public String getAnnulTime() {
        return this.annulTime;
    }

    public void setAnnulTime(String anAnnulTime) {
        this.annulTime = anAnnulTime;
    }

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
    
    public Long getStatementBatchNo() {
        return this.statementBatchNo;
    }

    public void setStatementBatchNo(Long anStatementBatchNo) {
        this.statementBatchNo = anStatementBatchNo;
    }

    public Long getGoodsBatchNo() {
        return this.goodsBatchNo;
    }

    public void setGoodsBatchNo(Long anGoodsBatchNo) {
        this.goodsBatchNo = anGoodsBatchNo;
    }

    public Long getOthersBatchNo() {
        return this.othersBatchNo;
    }

    public void setOthersBatchNo(Long anOthersBatchNo) {
        this.othersBatchNo = anOthersBatchNo;
    }
    
    

    public String getBusinTypeId() {
        return this.businTypeId;
    }

    public void setBusinTypeId(String anBusinTypeId) {
        this.businTypeId = anBusinTypeId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String anProductCode) {
        this.productCode = anProductCode;
    }

    public Long getFactorNo() {
        return this.factorNo;
    }

    public void setFactorNo(Long anFactorNo) {
        this.factorNo = anFactorNo;
    }

    public String getOrderList() {
        return this.orderList;
    }

    public void setOrderList(String anOrderList) {
        this.orderList = anOrderList;
    }

    public String getInvoiceList() {
        return this.invoiceList;
    }

    public void setInvoiceList(String anInvoiceList) {
        this.invoiceList = anInvoiceList;
    }

    public String getAgreementList() {
        return this.agreementList;
    }

    public void setAgreementList(String anAgreementList) {
        this.agreementList = anAgreementList;
    }

    public String getReceivableList() {
        return this.receivableList;
    }

    public void setReceivableList(String anReceivableList) {
        this.receivableList = anReceivableList;
    }

    public String getAcceptBillList() {
        return this.acceptBillList;
    }

    public void setAcceptBillList(String anAcceptBillList) {
        this.acceptBillList = anAcceptBillList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.annulDate == null) ? 0 : this.annulDate.hashCode());
        result = prime * result + ((this.annulOperId == null) ? 0 : this.annulOperId.hashCode());
        result = prime * result + ((this.annulOperName == null) ? 0 : this.annulOperName.hashCode());
        result = prime * result + ((this.annulTime == null) ? 0 : this.annulTime.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.basedataMap == null) ? 0 : this.basedataMap.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.businTypeId == null) ? 0 : this.businTypeId.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custMap == null) ? 0 : this.custMap.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.custType == null) ? 0 : this.custType.hashCode());
        result = prime * result + ((this.factorNo == null) ? 0 : this.factorNo.hashCode());
        result = prime * result + ((this.goodsBatchNo == null) ? 0 : this.goodsBatchNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.operationAuth == null) ? 0 : this.operationAuth.hashCode());
        result = prime * result + ((this.othersBatchNo == null) ? 0 : this.othersBatchNo.hashCode());
        result = prime * result + ((this.prefixId == null) ? 0 : this.prefixId.hashCode());
        result = prime * result + ((this.productCode == null) ? 0 : this.productCode.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.sourceUseType == null) ? 0 : this.sourceUseType.hashCode());
        result = prime * result + ((this.statementBatchNo == null) ? 0 : this.statementBatchNo.hashCode());
        result = prime * result + ((this.suffixId == null) ? 0 : this.suffixId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAsset other = (ScfAsset) obj;
        if (this.annulDate == null) {
            if (other.annulDate != null) return false;
        }
        else if (!this.annulDate.equals(other.annulDate)) return false;
        if (this.annulOperId == null) {
            if (other.annulOperId != null) return false;
        }
        else if (!this.annulOperId.equals(other.annulOperId)) return false;
        if (this.annulOperName == null) {
            if (other.annulOperName != null) return false;
        }
        else if (!this.annulOperName.equals(other.annulOperName)) return false;
        if (this.annulTime == null) {
            if (other.annulTime != null) return false;
        }
        else if (!this.annulTime.equals(other.annulTime)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.basedataMap == null) {
            if (other.basedataMap != null) return false;
        }
        else if (!this.basedataMap.equals(other.basedataMap)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.businTypeId == null) {
            if (other.businTypeId != null) return false;
        }
        else if (!this.businTypeId.equals(other.businTypeId)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.custMap == null) {
            if (other.custMap != null) return false;
        }
        else if (!this.custMap.equals(other.custMap)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.custType == null) {
            if (other.custType != null) return false;
        }
        else if (!this.custType.equals(other.custType)) return false;
        if (this.factorNo == null) {
            if (other.factorNo != null) return false;
        }
        else if (!this.factorNo.equals(other.factorNo)) return false;
        if (this.goodsBatchNo == null) {
            if (other.goodsBatchNo != null) return false;
        }
        else if (!this.goodsBatchNo.equals(other.goodsBatchNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.operationAuth == null) {
            if (other.operationAuth != null) return false;
        }
        else if (!this.operationAuth.equals(other.operationAuth)) return false;
        if (this.othersBatchNo == null) {
            if (other.othersBatchNo != null) return false;
        }
        else if (!this.othersBatchNo.equals(other.othersBatchNo)) return false;
        if (this.prefixId == null) {
            if (other.prefixId != null) return false;
        }
        else if (!this.prefixId.equals(other.prefixId)) return false;
        if (this.productCode == null) {
            if (other.productCode != null) return false;
        }
        else if (!this.productCode.equals(other.productCode)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        }
        else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        }
        else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.sourceUseType == null) {
            if (other.sourceUseType != null) return false;
        }
        else if (!this.sourceUseType.equals(other.sourceUseType)) return false;
        if (this.statementBatchNo == null) {
            if (other.statementBatchNo != null) return false;
        }
        else if (!this.statementBatchNo.equals(other.statementBatchNo)) return false;
        if (this.suffixId == null) {
            if (other.suffixId != null) return false;
        }
        else if (!this.suffixId.equals(other.suffixId)) return false;
        return true;
    }


    @Override
    public String toString() {
        return "ScfAsset [id=" + this.id + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", businStatus=" + this.businStatus
                + ", businTypeId=" + this.businTypeId + ", productCode=" + this.productCode + ", sourceUseType=" + this.sourceUseType + ", custNo="
                + this.custNo + ", custName=" + this.custName + ", coreCustNo=" + this.coreCustNo + ", coreCustName=" + this.coreCustName
                + ", prefixId=" + this.prefixId + ", suffixId=" + this.suffixId + ", custType=" + this.custType + ", custMap=" + this.custMap
                + ", basedataMap=" + this.basedataMap + ", orderList=" + this.orderList + ", invoiceList=" + this.invoiceList + ", agreementList="
                + this.agreementList + ", receivableList=" + this.receivableList + ", acceptBillList=" + this.acceptBillList + ", operationAuth="
                + this.operationAuth + ", factorNo=" + this.factorNo + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName
                + ", annulOperId=" + this.annulOperId + ", annulOperName=" + this.annulOperName + ", annulDate=" + this.annulDate + ", annulTime="
                + this.annulTime + ", statementBatchNo=" + this.statementBatchNo + ", goodsBatchNo=" + this.goodsBatchNo + ", othersBatchNo="
                + this.othersBatchNo + ", balance=" + this.balance + "]";
    }

    /**
     * 初始化资产信息，
     */
    public void initAdd() {
        
        CustOperatorInfo operatorInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operatorInfo,"请先登录");
        this.id=SerialGenerator.getLongValue("ScfAsset.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOEFFECTIVE);
        this.regOperId=operatorInfo.getId();
        this.regOperName=operatorInfo.getName();
    }

    public void initModifyValue(CustOperatorInfo anOperatorInfo) {
        
        this.annulDate=BetterDateUtils.getNumDate();
        this.annulOperId=anOperatorInfo.getId();
        this.annulOperName=anOperatorInfo.getName();
        this.annulTime=BetterDateUtils.getNumTime();
        this.businStatus=AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE;
    }

    public void initAnnulAsset(CustOperatorInfo anOperatorInfo) {
        
        this.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL);
        this.annulDate=BetterDateUtils.getNumDate();
        this.annulOperId=anOperatorInfo.getId();
        this.annulOperName=anOperatorInfo.getName();
        this.annulTime=BetterDateUtils.getNumTime();
    }
    
    public void initRejectOrBreakAsset(CustOperatorInfo anOperatorInfo) {
        
        this.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE);
        this.annulDate=BetterDateUtils.getNumDate();
        this.annulOperId=anOperatorInfo.getId();
        this.annulOperName=anOperatorInfo.getName();
        this.annulTime=BetterDateUtils.getNumTime();
    }
    
    

}
