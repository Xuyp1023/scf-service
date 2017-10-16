package com.betterjr.modules.payorder.entity;

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
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_pos_source_pay_pool_record")
public class PayOrderPoolRecord implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -7319936895368148412L;
    
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;
    
    /**
     * 申请编号
     */
    @Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="申请编号", comments = "申请编号")
    private String requestNo;
    
    /**
     * 收款银行
     */
    @Column(name = "c_cust_bankName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行", comments = "收款银行")
    private String custBankName;
    
    /**
     * 收款帐号
     */
    @Column(name = "c_cust_bankAccount", columnDefinition = "VARCHAR")
    @MetaData(value = "收款帐号", comments = "收款帐号")
    private String custBankAccount;
    
    /**
     * 收款开户名称
     */
    @Column(name = "c_cust_bankAccountName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款开户名称", comments = "收款开户名称")
    private String custBankAccountName;
    
    /**
     * 申请总金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请总金额", comments = "申请总金额")
    private BigDecimal balance;
    
    /**
     * 付款池id
     */
    @Column(name = "N_POOL_ID", columnDefinition = "INTEGER")
    @MetaData( value="付款池id", comments = "付款池id")
    private Long poolId;
    
    /**
     * 付款文件id
     */
    @Column(name = "N_PAYFILE_ID", columnDefinition = "INTEGER")
    @MetaData( value="付款池id", comments = "付款池id")
    private Long payFileId;
    
    /**
     * 状态：0 未处理 1付款中 2 复核中 3付款失败 4 付款成功 5 解析生成 6审核生效  9数据失效
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态：0 未处理 1付款中 2 复核中 3付款失败 4 付款成功 5 解析生成 6审核生效  9数据失效", comments = "状态：0 未处理 1付款中 2 复核中 3付款失败 4 付款成功 5 解析生成 6审核生效  9数据失效")
    private String businStatus;
    
    /**
     * 数据来源 0 融资申请  1上传解析
     */
    @Column(name = "C_INFO_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据来源 0 融资申请  1上传解析", comments = "数据来源 0 融资申请  1上传解析")
    private String infoType;
    
    /**
     * 付款结果信息
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="付款结果信息", comments = "付款结果信息")
    private String description;
    
    /**
     * 付款日期
     */
    @Column(name = "D_REQUEST_PAY_DATE", columnDefinition = "varchar")
    @MetaData(value = "付款日期", comments = "付款日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String requestPayDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getRequestNo() {
        return this.requestNo;
    }

    public void setRequestNo(String anRequestNo) {
        this.requestNo = anRequestNo;
    }

    public String getCustBankName() {
        return this.custBankName;
    }

    public void setCustBankName(String anCustBankName) {
        this.custBankName = anCustBankName;
    }

    public String getCustBankAccount() {
        return this.custBankAccount;
    }

    public void setCustBankAccount(String anCustBankAccount) {
        this.custBankAccount = anCustBankAccount;
    }

    public String getCustBankAccountName() {
        return this.custBankAccountName;
    }

    public void setCustBankAccountName(String anCustBankAccountName) {
        this.custBankAccountName = anCustBankAccountName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getPoolId() {
        return this.poolId;
    }

    public void setPoolId(Long anPoolId) {
        this.poolId = anPoolId;
    }

    public Long getPayFileId() {
        return this.payFileId;
    }

    public void setPayFileId(Long anPayFileId) {
        this.payFileId = anPayFileId;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String anInfoType) {
        this.infoType = anInfoType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }
    
    public String getRequestPayDate() {
        return this.requestPayDate;
    }

    public void setRequestPayDate(String anRequestPayDate) {
        this.requestPayDate = anRequestPayDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.custBankAccount == null) ? 0 : this.custBankAccount.hashCode());
        result = prime * result + ((this.custBankAccountName == null) ? 0 : this.custBankAccountName.hashCode());
        result = prime * result + ((this.custBankName == null) ? 0 : this.custBankName.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.payFileId == null) ? 0 : this.payFileId.hashCode());
        result = prime * result + ((this.poolId == null) ? 0 : this.poolId.hashCode());
        result = prime * result + ((this.requestNo == null) ? 0 : this.requestNo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PayOrderPoolRecord other = (PayOrderPoolRecord) obj;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.custBankAccount == null) {
            if (other.custBankAccount != null) return false;
        }
        else if (!this.custBankAccount.equals(other.custBankAccount)) return false;
        if (this.custBankAccountName == null) {
            if (other.custBankAccountName != null) return false;
        }
        else if (!this.custBankAccountName.equals(other.custBankAccountName)) return false;
        if (this.custBankName == null) {
            if (other.custBankName != null) return false;
        }
        else if (!this.custBankName.equals(other.custBankName)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        }
        else if (!this.infoType.equals(other.infoType)) return false;
        if (this.payFileId == null) {
            if (other.payFileId != null) return false;
        }
        else if (!this.payFileId.equals(other.payFileId)) return false;
        if (this.poolId == null) {
            if (other.poolId != null) return false;
        }
        else if (!this.poolId.equals(other.poolId)) return false;
        if (this.requestNo == null) {
            if (other.requestNo != null) return false;
        }
        else if (!this.requestNo.equals(other.requestNo)) return false;
        return true;
    }


    @Override
    public String toString() {
        return "PayOrderPoolRecord [id=" + this.id + ", requestNo=" + this.requestNo + ", custBankName=" + this.custBankName + ", custBankAccount="
                + this.custBankAccount + ", custBankAccountName=" + this.custBankAccountName + ", balance=" + this.balance + ", poolId=" + this.poolId
                + ", payFileId=" + this.payFileId + ", businStatus=" + this.businStatus + ", infoType=" + this.infoType + ", description="
                + this.description + ", requestPayDate=" + this.requestPayDate + "]";
    }

    public  PayOrderPoolRecord saveAddInitValue(String anInfoType) {
        
        this.setId(SerialGenerator.getLongValue("PayOrderPoolRecord.id"));
        this.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE);
        this.setInfoType(anInfoType);
        return this;
    }
    
    


}
