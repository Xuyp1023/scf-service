package com.betterjr.modules.commission.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_invoice_custinfo")
public class CommissionInvoiceCustInfo implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -6224014799685775186L;
  
    //`ID` bigint(18) NOT NULL COMMENT '发票主键',
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;
    
    //企业id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    private Long custNo;

    //当前企业名称
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    private String custName;
    
    //核心企业id
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    private Long coreCustNo;
    
    //核心企业名称
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    private String coreCustName;
    
    //银行s
    @Column(name = "C_CORE_BANK",  columnDefinition="VARCHAR" )
    private String coreBank;
    
    // 银行帐号',
    @Column(name = "C_CORE_BANK_ACCOUNT",  columnDefinition="VARCHAR" )
    private String coreBankAccount;
    
    //纳税人识别号
    @Column(name = "C_CORE_TAXPAYERNO",  columnDefinition="VARCHAR" )
    private String coreTaxPayerNo;
    
    //电话
    @Column(name = "C_CORE_PHONE",  columnDefinition="VARCHAR" )
    private String corePhone;
    
    //地址
    @Column(name = "C_CORE_ADDRESS",  columnDefinition="VARCHAR" )
    private String coreAddress;
    
    //状态  0 不可用   1 可用
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
    
    //是否默认  0 不是默认   1是默认
    @Column(name = "C_IS_LATEST",  columnDefinition="VARCHAR" )
    private String isLatest;
    
    //发票抬头的类型  发票抬头的类型  1  企业     0个人
    @Column(name = "C_COREINFO_TYPE",  columnDefinition="VARCHAR" )
    private String coreInfoType;
    
    //注册人
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

    public String getCoreBank() {
        return this.coreBank;
    }

    public void setCoreBank(String anCoreBank) {
        this.coreBank = anCoreBank;
    }

    public String getCoreBankAccount() {
        return this.coreBankAccount;
    }

    public void setCoreBankAccount(String anCoreBankAccount) {
        this.coreBankAccount = anCoreBankAccount;
    }

    public String getCoreTaxPayerNo() {
        return this.coreTaxPayerNo;
    }

    public void setCoreTaxPayerNo(String anCoreTaxPayerNo) {
        this.coreTaxPayerNo = anCoreTaxPayerNo;
    }

    public String getCorePhone() {
        return this.corePhone;
    }

    public void setCorePhone(String anCorePhone) {
        this.corePhone = anCorePhone;
    }

    public String getCoreAddress() {
        return this.coreAddress;
    }

    public void setCoreAddress(String anCoreAddress) {
        this.coreAddress = anCoreAddress;
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

    public String getIsLatest() {
        return this.isLatest;
    }

    public void setIsLatest(String anIsLatest) {
        this.isLatest = anIsLatest;
    }

    public String getCoreInfoType() {
        return this.coreInfoType;
    }

    public void setCoreInfoType(String anCoreInfoType) {
        this.coreInfoType = anCoreInfoType;
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
        result = prime * result + ((this.coreAddress == null) ? 0 : this.coreAddress.hashCode());
        result = prime * result + ((this.coreBank == null) ? 0 : this.coreBank.hashCode());
        result = prime * result + ((this.coreBankAccount == null) ? 0 : this.coreBankAccount.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.coreInfoType == null) ? 0 : this.coreInfoType.hashCode());
        result = prime * result + ((this.corePhone == null) ? 0 : this.corePhone.hashCode());
        result = prime * result + ((this.coreTaxPayerNo == null) ? 0 : this.coreTaxPayerNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.isLatest == null) ? 0 : this.isLatest.hashCode());
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
        CommissionInvoiceCustInfo other = (CommissionInvoiceCustInfo) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.coreAddress == null) {
            if (other.coreAddress != null) return false;
        }
        else if (!this.coreAddress.equals(other.coreAddress)) return false;
        if (this.coreBank == null) {
            if (other.coreBank != null) return false;
        }
        else if (!this.coreBank.equals(other.coreBank)) return false;
        if (this.coreBankAccount == null) {
            if (other.coreBankAccount != null) return false;
        }
        else if (!this.coreBankAccount.equals(other.coreBankAccount)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.coreInfoType == null) {
            if (other.coreInfoType != null) return false;
        }
        else if (!this.coreInfoType.equals(other.coreInfoType)) return false;
        if (this.corePhone == null) {
            if (other.corePhone != null) return false;
        }
        else if (!this.corePhone.equals(other.corePhone)) return false;
        if (this.coreTaxPayerNo == null) {
            if (other.coreTaxPayerNo != null) return false;
        }
        else if (!this.coreTaxPayerNo.equals(other.coreTaxPayerNo)) return false;
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
        if (this.isLatest == null) {
            if (other.isLatest != null) return false;
        }
        else if (!this.isLatest.equals(other.isLatest)) return false;
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
        return "CommissionInvoiceCustInfo [id=" + this.id + ", custNo=" + this.custNo + ", custName=" + this.custName + ", coreCustNo="
                + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", coreBank=" + this.coreBank + ", coreBankAccount="
                + this.coreBankAccount + ", coreTaxPayerNo=" + this.coreTaxPayerNo + ", corePhone=" + this.corePhone + ", coreAddress="
                + this.coreAddress + ", businStatus=" + this.businStatus + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate
                + ", regTime=" + this.regTime + ", isLatest=" + this.isLatest + ", coreInfoType=" + this.coreInfoType + ", regOperId=" + this.regOperId + "]";
    }

    
    public void initAddValue(CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo, "新增佣金参数失败！请先登录");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setBusinStatus(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK);
        this.setId(SerialGenerator.getLongValue("CommissionInvoiceCustInfo.id"));
        if(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PERSION.equals(this.getCoreInfoType())){
            
            this.setCoreAddress("");
            this.setCoreBank("");
            this.setCoreTaxPayerNo("");
            this.setCoreBankAccount("");
        }
    }

    

   
    
    
}
