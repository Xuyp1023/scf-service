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
import com.betterjr.modules.flie.annotation.ExcelImportAnno;
import com.betterjr.modules.flie.annotation.ExcelImportTypeAnno;
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * t_pos_source_pay_pool_record
 * @ClassName: PayOrderPoolRecord 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午3:11:27 
 *
 */

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_pos_source_pay_pool_record")
@ExcelImportTypeAnno(excelBeginRow = 5)
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
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "账款编号", cloumnOrder = 0, cloumnType = "0", vailedRegular = "")
    private String requestNo;
    
    /**
     * 收款银行
     */
    @Column(name = "c_cust_bankName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款银行", comments = "收款银行")
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "收款人开户银行", cloumnOrder = 2, cloumnType = "0", vailedRegular = "")
    private String custBankName;
    
    /**
     * 收款帐号
     */
    @Column(name = "c_cust_bankAccount", columnDefinition = "VARCHAR")
    @MetaData(value = "收款帐号", comments = "收款帐号")
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "收款人开户银行", cloumnOrder = 3, cloumnType = "0", vailedRegular = "^\\d+$")
    private String custBankAccount;
    
    /**
     * 收款开户名称
     */
    @Column(name = "c_cust_bankAccountName", columnDefinition = "VARCHAR")
    @MetaData(value = "收款开户名称", comments = "收款开户名称")
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "收款人名称", cloumnOrder = 1, cloumnType = "0", vailedRegular = "")
    private String custBankAccountName;
    
    /**
     * 申请总金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请总金额", comments = "申请总金额")
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "金额", cloumnOrder = 4, cloumnType = "1", vailedRegular = "")
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
     * 付款成功|付款失败
     */
    @Column(name = "C_BUSIN_STATUS_CHINESE",  columnDefinition="VARCHAR" )
    @ExcelImportAnno(isMust = "1", cloumnChineseName = "付款状态", cloumnOrder = 5, cloumnType = "0", vailedRegular = "", requireContainsValues="付款成功|付款失败")
    private String businStatusChinese;
    
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
    @ExcelImportAnno(isMust = "0", cloumnChineseName = "处理结果", cloumnOrder = 6, cloumnType = "0", vailedRegular = "")
    private String description;
    
    /**
     * 付款日期
     */
    @Column(name = "D_REQUEST_PAY_DATE", columnDefinition = "varchar")
    @MetaData(value = "付款日期", comments = "付款日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String requestPayDate;
    
    /**
     * 资金方企业编号
     */
    @Column(name = "L_FACTORY_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "资金方企业编号", comments = "资金方企业编号")
    private Long factoryNo;

    /**
     * 资金方企业名称
     */
    @Column(name = "C_FACTORY_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "资金方企业名称", comments = "资金方企业名称")
    private String factoryName;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long anId) {
        this.id = anId;
    }

    public String getRequestNo() {
        return this.requestNo;
    }

    public void setRequestNo(final String anRequestNo) {
        this.requestNo = anRequestNo;
    }

    public String getCustBankName() {
        return this.custBankName;
    }

    public void setCustBankName(final String anCustBankName) {
        this.custBankName = anCustBankName;
    }

    public String getCustBankAccount() {
        return this.custBankAccount;
    }

    public void setCustBankAccount(final String anCustBankAccount) {
        this.custBankAccount = anCustBankAccount;
    }

    public String getCustBankAccountName() {
        return this.custBankAccountName;
    }

    public void setCustBankAccountName(final String anCustBankAccountName) {
        this.custBankAccountName = anCustBankAccountName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(final BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public Long getPoolId() {
        return this.poolId;
    }

    public void setPoolId(final Long anPoolId) {
        this.poolId = anPoolId;
    }

    public Long getPayFileId() {
        return this.payFileId;
    }

    public void setPayFileId(final Long anPayFileId) {
        this.payFileId = anPayFileId;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(final String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(final String anInfoType) {
        this.infoType = anInfoType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String anDescription) {
        this.description = anDescription;
    }
    
    public String getRequestPayDate() {
        return this.requestPayDate;
    }

    public void setRequestPayDate(final String anRequestPayDate) {
        this.requestPayDate = anRequestPayDate;
    }
    
    public Long getFactoryNo() {
        return this.factoryNo;
    }

    public void setFactoryNo(final Long anFactoryNo) {
        this.factoryNo = anFactoryNo;
    }

    public String getFactoryName() {
        return this.factoryName;
    }

    public void setFactoryName(final String anFactoryName) {
        this.factoryName = anFactoryName;
    }

    public String getBusinStatusChinese() {
        return this.businStatusChinese;
    }

    public void setBusinStatusChinese(final String anBusinStatusChinese) {
        this.businStatusChinese = anBusinStatusChinese;
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PayOrderPoolRecord other = (PayOrderPoolRecord) obj;
        if (this.balance == null) {
            if (other.balance != null) {
                return false;
            }
        }
        else if (!this.balance.equals(other.balance)) {
            return false;
        }
        if (this.businStatus == null) {
            if (other.businStatus != null) {
                return false;
            }
        }
        else if (!this.businStatus.equals(other.businStatus)) {
            return false;
        }
        if (this.custBankAccount == null) {
            if (other.custBankAccount != null) {
                return false;
            }
        }
        else if (!this.custBankAccount.equals(other.custBankAccount)) {
            return false;
        }
        if (this.custBankAccountName == null) {
            if (other.custBankAccountName != null) {
                return false;
            }
        }
        else if (!this.custBankAccountName.equals(other.custBankAccountName)) {
            return false;
        }
        if (this.custBankName == null) {
            if (other.custBankName != null) {
                return false;
            }
        }
        else if (!this.custBankName.equals(other.custBankName)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        }
        else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.infoType == null) {
            if (other.infoType != null) {
                return false;
            }
        }
        else if (!this.infoType.equals(other.infoType)) {
            return false;
        }
        if (this.payFileId == null) {
            if (other.payFileId != null) {
                return false;
            }
        }
        else if (!this.payFileId.equals(other.payFileId)) {
            return false;
        }
        if (this.poolId == null) {
            if (other.poolId != null) {
                return false;
            }
        }
        else if (!this.poolId.equals(other.poolId)) {
            return false;
        }
        if (this.requestNo == null) {
            if (other.requestNo != null) {
                return false;
            }
        }
        else if (!this.requestNo.equals(other.requestNo)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "PayOrderPoolRecord [id=" + this.id + ", requestNo=" + this.requestNo + ", custBankName="
                + this.custBankName + ", custBankAccount=" + this.custBankAccount + ", custBankAccountName="
                + this.custBankAccountName + ", balance=" + this.balance + ", poolId=" + this.poolId + ", payFileId="
                + this.payFileId + ", businStatus=" + this.businStatus + ", infoType=" + this.infoType
                + ", description=" + this.description + ", requestPayDate=" + this.requestPayDate + ", factoryNo="
                + this.factoryNo + ", factoryName=" + this.factoryName + "]";
    }

    public  PayOrderPoolRecord saveAddInitValue(final String anInfoType) {
        
        this.setId(SerialGenerator.getLongValue("PayOrderPoolRecord.id"));
        this.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE);
        this.setInfoType(anInfoType);
        return this;
    }
    
    


}
