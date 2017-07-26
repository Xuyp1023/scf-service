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

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_invoice_record")
public class CommissionInvoiceRecord implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 3365818383644393988L;
    
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;
    
    //佣金发票主键Id发票Id
    @Column(name = "L_INVOICE_ID",  columnDefinition="INTEGER" )
    private Long invoiceId;
    
    //月账单Id
    @Column(name = "L_MONTHLY_STATEMENT_ID",  columnDefinition="INTEGER" )
    private Long monthlyId;

    
    /**
     * 总金额
     */
    @Column(name = "F_BLANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="总金额", comments = "总金额")
    private BigDecimal balance;
    
    /**
     * 商业发票税率
     */
    @Column(name = "F_TAX_RATE",  columnDefinition="DECIMAL" )
    @MetaData( value="商业发票税率", comments = "商业发票税率")
    private BigDecimal taxRate;
    
    //发票种类  0 普通发票  1 专用发票
    @Column(name = "C_INVOICE_TYPE",  columnDefinition="VARCHAR" )
    private String invoiceType;
    
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

    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(Long anInvoiceId) {
        this.invoiceId = anInvoiceId;
    }

    public Long getMonthlyId() {
        return this.monthlyId;
    }

    public void setMonthlyId(Long anMonthlyId) {
        this.monthlyId = anMonthlyId;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal anTaxRate) {
        this.taxRate = anTaxRate;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(String anInvoiceType) {
        this.invoiceType = anInvoiceType;
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
    public String toString() {
        return "CommissionInvoiceRecord [id=" + this.id + ", invoiceId=" + this.invoiceId + ", monthlyId=" + this.monthlyId + ", balance="
                + this.balance + ", taxRate=" + this.taxRate + ", invoiceType=" + this.invoiceType + ", regOperName=" + this.regOperName
                + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", regOperId=" + this.regOperId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.invoiceId == null) ? 0 : this.invoiceId.hashCode());
        result = prime * result + ((this.invoiceType == null) ? 0 : this.invoiceType.hashCode());
        result = prime * result + ((this.monthlyId == null) ? 0 : this.monthlyId.hashCode());
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
        CommissionInvoiceRecord other = (CommissionInvoiceRecord) obj;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.invoiceId == null) {
            if (other.invoiceId != null) return false;
        }
        else if (!this.invoiceId.equals(other.invoiceId)) return false;
        if (this.invoiceType == null) {
            if (other.invoiceType != null) return false;
        }
        else if (!this.invoiceType.equals(other.invoiceType)) return false;
        if (this.monthlyId == null) {
            if (other.monthlyId != null) return false;
        }
        else if (!this.monthlyId.equals(other.monthlyId)) return false;
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

    public void initAddValue(CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setId(SerialGenerator.getLongValue("CommissionInvoiceRecord.id"));
    }
    
    

}
