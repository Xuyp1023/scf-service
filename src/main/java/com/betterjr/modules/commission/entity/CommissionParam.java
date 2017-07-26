package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_commission_param")
public class CommissionParam implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -4805417451205960858L;
    
    //`ID` bigint(18) NOT NULL COMMENT '发票主键',
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;
    
   //平台id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    private Long custNo;

    //平台名称
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    private String custName;
    
    //核心企业对账企业Id
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    private Long coreCustNo;
    
    //对账企业名称
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    private String coreCustName;
    
    /**
     * 佣金年利率
     */
    @Column(name = "F_INTEREST_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="佣金年利率", comments = "佣金年利率")
    private BigDecimal interestRate;
    
    /**
     * 商业发票税率
     */
    @Column(name = "F_TAX_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="商业发票税率", comments = "商业发票税率")
    private BigDecimal taxRate;
    
    //状态: 0 不可用   1 可用
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;
    
    //注册人名字
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    private String regOperName;
    
    //注册日期
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    private String regDate;
    
    ////注册时间
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;

    //注册人Id
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    private Long regOperId;

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

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal anInterestRate) {
        this.interestRate = anInterestRate;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal anTaxRate) {
        this.taxRate = anTaxRate;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getRegOperName() {
        return this.regOperName;
    }

    public void setRegOperName(String anRegOperName) {
        this.regOperName = anRegOperName;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.interestRate == null) ? 0 : this.interestRate.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.taxRate == null) ? 0 : this.taxRate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionParam other = (CommissionParam) obj;
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
        if (this.interestRate == null) {
            if (other.interestRate != null) return false;
        }
        else if (!this.interestRate.equals(other.interestRate)) return false;
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
        if (this.taxRate == null) {
            if (other.taxRate != null) return false;
        }
        else if (!this.taxRate.equals(other.taxRate)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommissionParam [id=" + this.id + ", custNo=" + this.custNo + ", custName=" + this.custName + ", coreCustNo=" + this.coreCustNo
                + ", coreCustName=" + this.coreCustName + ", interestRate=" + this.interestRate + ", taxRate=" + this.taxRate + ", businStatus="
                + this.businStatus + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", regOperId="
                + this.regOperId + "]";
    }

    
    public void initAddValue(CustOperatorInfo anOperatorInfo) {
        
        saveUpdateValue(anOperatorInfo);
        this.setId(SerialGenerator.getLongValue("CommissionParam.id"));
        
    }

    public void saveUpdateValue(CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setBusinStatus(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK);
        
    }
    
    
    

}
