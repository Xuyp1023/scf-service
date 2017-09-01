package com.betterjr.modules.supplieroffer.entity;

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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.supplieroffer.data.OfferConstantCollentions;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_supplier_offer")
public class ScfSupplierOffer implements BetterjrEntity{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8108164153039362088L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;
    
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
     * 核心企业给供应商的利率
     */
    @Column(name = "L_CORECUST_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="核心企业给供应商的利率", comments = "核心企业给供应商的利率")
    private BigDecimal coreCustRate;
    
    /**
     * 状态  0： 不可用  1 正常使用
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态  0： 不可用  1 正常使用", comments = "状态  0： 不可用  1 正常使用")
    private String businStatus;
    
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
     * 注册人
     */
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="注册人", comments = "注册人")
    private Long regOperId ;

    /**
     * 注册人名称
     */
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="注册人名称", comments = "注册人名称")
    private String regOperName;
    
    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
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

    public BigDecimal getCoreCustRate() {
        return this.coreCustRate;
    }

    public void setCoreCustRate(BigDecimal anCoreCustRate) {
        this.coreCustRate = anCoreCustRate;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
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

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.coreCustRate == null) ? 0 : this.coreCustRate.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
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
        ScfSupplierOffer other = (ScfSupplierOffer) obj;
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
        if (this.coreCustRate == null) {
            if (other.coreCustRate != null) return false;
        }
        else if (!this.coreCustRate.equals(other.coreCustRate)) return false;
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
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
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
        return true;
    }

    @Override
    public String toString() {
        return "ScfSupplierOffer [id=" + this.id + ", custNo=" + this.custNo + ", custName=" + this.custName + ", coreCustNo=" + this.coreCustNo
                + ", coreCustName=" + this.coreCustName + ", coreCustRate=" + this.coreCustRate + ", businStatus=" + this.businStatus + ", regDate="
                + this.regDate + ", regTime=" + this.regTime + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName + ", operOrg="
                + this.operOrg + "]";
    }

    public void saveAddValue(CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo,"无法获取登录信息,操作失败");
        this.setId(SerialGenerator.getLongValue("ScfSupplierOffer.id"));
        this.setBusinStatus(OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE);
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
        this.regOperId=anOperatorInfo.getId();
        this.regOperName = anOperatorInfo.getName();
        
    }

    
    

}
