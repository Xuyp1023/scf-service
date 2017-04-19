package com.betterjr.modules.order.entity;

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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_invoice_detail_v3")
public class ScfInvoiceDODetail implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1555495118926092387L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;
    
    /**
     * 发票号
     */
    @Id
    @Column(name = "L_INVOICE_ID",  columnDefinition="INTEGER" )
    @MetaData( value="发票号", comments = "发票号")
    private Long invoiceId;
    
    /**
     * 货物/应税劳务,服务名称
     */
    @Column(name = "C_SERVICE_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="货物/应税劳务,服务名称", comments = "货物/应税劳务,服务名称")
    private String serviceName;
    
    /**
     * 规格型号
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="规格型号", comments = "规格型号")
    private String description;

    /**
     * 商品价格
     */
    @Column(name = "F_UNIT",  columnDefinition="DOUBLE" )
    @MetaData( value="商品价格", comments = "商品价格")
    private BigDecimal unit;

    /**
     * 数量
     */
    @Column(name = "N_AMOUNT",  columnDefinition="DOUBLE" )
    @MetaData( value="数量", comments = "数量")
    private Integer amount;

    /**
     * 总额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="总额", comments = "总额")
    private BigDecimal balance;
    
    /**
     * 单位
     */
    @Column(name = "C_COMPANY",  columnDefinition="VARCHAR" )
    @MetaData( value="单位", comments = "单位")
    private String company;
    
    /**
     * 税率
     */
    @Column(name = "F_INVOICE_TAX",  columnDefinition="DOUBLE" )
    @MetaData( value="税率", comments = "税率")
    private BigDecimal invoiceTax;
    
    /**
     * 税金
     */
    @Column(name = "F_INVOICE_TAX_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="税金", comments = "税金")
    private BigDecimal invoiceTaxBalance;

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

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String anServiceName) {
        this.serviceName = anServiceName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
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

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String anCompany) {
        this.company = anCompany;
    }

    public BigDecimal getInvoiceTax() {
        return this.invoiceTax;
    }

    public void setInvoiceTax(BigDecimal anInvoiceTax) {
        this.invoiceTax = anInvoiceTax;
    }

    public BigDecimal getInvoiceTaxBalance() {
        return this.invoiceTaxBalance;
    }

    public void setInvoiceTaxBalance(BigDecimal anInvoiceTaxBalance) {
        this.invoiceTaxBalance = anInvoiceTaxBalance;
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

    @Override
    public String toString() {
        return "ScfInvoiceDODetail [id=" + this.id + ", invoiceId=" + this.invoiceId + ", serviceName=" + this.serviceName + ", description="
                + this.description + ", unit=" + this.unit + ", amount=" + this.amount + ", balance=" + this.balance + ", company=" + this.company
                + ", invoiceTax=" + this.invoiceTax + ", invoiceTaxBalance=" + this.invoiceTaxBalance + ", regDate=" + this.regDate + ", regTime="
                + this.regTime + "]";
    }

    public ScfInvoiceDODetail() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.company == null) ? 0 : this.company.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.invoiceId == null) ? 0 : this.invoiceId.hashCode());
        result = prime * result + ((this.invoiceTax == null) ? 0 : this.invoiceTax.hashCode());
        result = prime * result + ((this.invoiceTaxBalance == null) ? 0 : this.invoiceTaxBalance.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.serviceName == null) ? 0 : this.serviceName.hashCode());
        result = prime * result + ((this.unit == null) ? 0 : this.unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfInvoiceDODetail other = (ScfInvoiceDODetail) obj;
        if (this.amount == null) {
            if (other.amount != null) return false;
        }
        else if (!this.amount.equals(other.amount)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.company == null) {
            if (other.company != null) return false;
        }
        else if (!this.company.equals(other.company)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.invoiceId == null) {
            if (other.invoiceId != null) return false;
        }
        else if (!this.invoiceId.equals(other.invoiceId)) return false;
        if (this.invoiceTax == null) {
            if (other.invoiceTax != null) return false;
        }
        else if (!this.invoiceTax.equals(other.invoiceTax)) return false;
        if (this.invoiceTaxBalance == null) {
            if (other.invoiceTaxBalance != null) return false;
        }
        else if (!this.invoiceTaxBalance.equals(other.invoiceTaxBalance)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.serviceName == null) {
            if (other.serviceName != null) return false;
        }
        else if (!this.serviceName.equals(other.serviceName)) return false;
        if (this.unit == null) {
            if (other.unit != null) return false;
        }
        else if (!this.unit.equals(other.unit)) return false;
        return true;
    }

}
