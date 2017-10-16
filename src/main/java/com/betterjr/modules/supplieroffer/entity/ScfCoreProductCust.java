package com.betterjr.modules.supplieroffer.entity;

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
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.supplieroffer.data.CoreProductCustConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_coreproduct_cust")
public class ScfCoreProductCust implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -910310462856523101L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 产品编号
     */
    @Column(name = "C_PRODUCT_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "产品编号", comments = "产品编号")
    private String productCode;

    /**
     * 产品名称
     */
    @Column(name = "C_PRODUCT_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "产品名称", comments = "产品名称")
    private String productName;

    /**
     * 应收账款融资类型   1 采购方提前付款  2结算中心提前付款 3：内部保理付款  4 银行保理 5 外部保理付款
     */
    @Column(name = "C_RECEIVABLE_REQUEST_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "应收账款融资类型", comments = "应收账款融资类型")
    private String receivableRequestType;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORY_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "保理公司编号", comments = "保理公司编号")
    private Long factoryNo;

    /**
     * 保理公司名称
     */
    @Column(name = "C_FACTORY_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "保理公司名称", comments = "保理公司名称")
    private String factoryName;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 核心企业名称
     */
    @Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "核心企业名称", comments = "核心企业名称")
    private String coreCustName;

    /**
     * 供应商编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "供应商编号", comments = "供应商编号")
    private Long custNo;

    /**
     * 供应商名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "供应商名称", comments = "供应商名称")
    private String custName;

    /**
     * 申请日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "申请日期", comments = "申请日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 申请时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "申请时间", comments = "申请时间")
    private String regTime;

    /**
     * 注册人
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "注册人", comments = "注册人")
    private Long regOperId;

    /**
     * 注册人名称
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册人名称", comments = "注册人名称")
    private String regOperName;

    /**
     * 业务状态 0 不可用    1 未分配给供应商   2 已经分配给供应商
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务状态 0 不可用    1 未分配给供应商   2 已经分配给供应商", comments = "业务状态 0 不可用    1 未分配给供应商   2 已经分配给供应商")
    private String businStatus;

    /**
     * 保理产品Id
     */
    @Column(name = "L_PRODUCT_ID", columnDefinition = "INTEGER")
    @MetaData(value = "保理产品Id", comments = "保理产品Id")
    private Long productId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String anProductCode) {
        this.productCode = anProductCode;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String anProductName) {
        this.productName = anProductName;
    }

    public String getReceivableRequestType() {
        return this.receivableRequestType;
    }

    public void setReceivableRequestType(String anReceivableRequestType) {
        this.receivableRequestType = anReceivableRequestType;
    }

    public Long getFactoryNo() {
        return this.factoryNo;
    }

    public void setFactoryNo(Long anFactoryNo) {
        this.factoryNo = anFactoryNo;
    }

    public String getFactoryName() {
        return this.factoryName;
    }

    public void setFactoryName(String anFactoryName) {
        this.factoryName = anFactoryName;
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

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long anProductId) {
        this.productId = anProductId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.factoryName == null) ? 0 : this.factoryName.hashCode());
        result = prime * result + ((this.factoryNo == null) ? 0 : this.factoryNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.productCode == null) ? 0 : this.productCode.hashCode());
        result = prime * result + ((this.productId == null) ? 0 : this.productId.hashCode());
        result = prime * result + ((this.productName == null) ? 0 : this.productName.hashCode());
        result = prime * result + ((this.receivableRequestType == null) ? 0 : this.receivableRequestType.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfCoreProductCust other = (ScfCoreProductCust) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        } else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        } else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.factoryName == null) {
            if (other.factoryName != null) return false;
        } else if (!this.factoryName.equals(other.factoryName)) return false;
        if (this.factoryNo == null) {
            if (other.factoryNo != null) return false;
        } else if (!this.factoryNo.equals(other.factoryNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.productCode == null) {
            if (other.productCode != null) return false;
        } else if (!this.productCode.equals(other.productCode)) return false;
        if (this.productId == null) {
            if (other.productId != null) return false;
        } else if (!this.productId.equals(other.productId)) return false;
        if (this.productName == null) {
            if (other.productName != null) return false;
        } else if (!this.productName.equals(other.productName)) return false;
        if (this.receivableRequestType == null) {
            if (other.receivableRequestType != null) return false;
        } else if (!this.receivableRequestType.equals(other.receivableRequestType)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        } else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        } else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfCoreProductCust [id=" + this.id + ", productCode=" + this.productCode + ", productName="
                + this.productName + ", receivableRequestType=" + this.receivableRequestType + ", factoryNo="
                + this.factoryNo + ", factoryName=" + this.factoryName + ", coreCustNo=" + this.coreCustNo
                + ", coreCustName=" + this.coreCustName + ", custNo=" + this.custNo + ", custName=" + this.custName
                + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", regOperId=" + this.regOperId
                + ", regOperName=" + this.regOperName + ", businStatus=" + this.businStatus + ", productId="
                + this.productId + "]";
    }

    public void saveAddValue(CustOperatorInfo anOperatorInfo) {

        this.setBusinStatus(CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_EFFECTIVE);
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.regOperId = anOperatorInfo.getId();
        this.regOperName = anOperatorInfo.getName();
        this.setId(SerialGenerator.getLongValue("ScfCoreProductCust.id"));

    }

}
