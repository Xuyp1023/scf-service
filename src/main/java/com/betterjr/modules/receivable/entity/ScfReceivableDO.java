package com.betterjr.modules.receivable.entity;

import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.entity.BaseVersionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_RECEIVABLE_V3")
public class ScfReceivableDO extends BaseVersionEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 1047920079499004982L;
    
    /**
     * 应收账款编号(账款编号)
     */
    @Column(name = "C_RECEIVABLE_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="应收账款编号", comments = "应收账款编号")
    private String receivableNo;

    /**
     * 客户资金系统编号
     */
    @Column(name = "C_BTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户资金系统编号", comments = "客户资金系统编号")
    @JsonIgnore
    private String btNo;

    /**
     * 核心企业编号(债务公司)
     */
    @Column(name = "L_CORE_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;
    
    /**
     * 核心企业名称(债务公司名称)
     */
    @Column(name = "C_CORE_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="核心企业名称", comments = "核心企业名称")
    private String coreCustName;  

    /**
     * 客户号(债权公司)
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户号", comments = "客户号")
    private Long custNo;

    /**
     * 客户名称(债权公司名称)
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户名称", comments = "客户名称")
    private String custName;
    
    /**
     * 债权人
     */
    @Column(name = "C_CREDITOR",  columnDefinition="VARCHAR" )
    @MetaData( value="债权人", comments = "债权人")
    private String creditor;

    /**
     * 债务人
     */
    @Column(name = "C_DEBTOR",  columnDefinition="VARCHAR" )
    @MetaData( value="债务人", comments = "债务人")
    private String debtor;

    /**
     * 金额（应付账款金额）
     */
    @Column(name = "F_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="金额", comments = "金额")
    private BigDecimal balance;
    
    /**
     * 余额(应付账款余额)
     */
    @Column(name = "F_SURPLUS_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="余额", comments = "余额")
    private BigDecimal surplusBalance;
    
    /**
     * 抵扣金额
     */
    @Column(name = "F_DEDUCTION_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="抵扣金额", comments = "抵扣金额")
    private BigDecimal deductionBalance ;
    
    /**
     * 结算金额(已结算金额)
     */
    @Column(name = "F_STATEMENT_BALANCE",  columnDefinition="DOUBLE" )
    @MetaData( value="结算金额", comments = "结算金额")
    private BigDecimal statementBalance ;

    /**
     * 贸易合同号
     */
    @Column(name = "C_AGREENO",  columnDefinition="VARCHAR" )
    @MetaData( value="贸易合同号", comments = "贸易合同号")
    private String agreeNo;
    
    /**
     * 发票编号
     */
    @Column(name = "C_INVOICENO",  columnDefinition="VARCHAR" )
    @MetaData( value="发票编号", comments = "发票编号")
    private String invoiceNo;
    
    /**
     * 付款到期日期（应付账款结算日）
     */
    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="付款到期日期", comments = "付款到期日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String endDate;

    /**
     * 数据创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据创建日期", comments = "数据创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    /**
     * 数据创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="数据创建时间", comments = "数据创建时间")
    private String regTime;

    /**
     * 编辑操作员编码
     */
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="编辑操作员编码", comments = "编辑操作员编码")
    @JsonIgnore
    private Long modiOperId ;

    /**
     * 编辑操作员名字
     */
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑操作员名字", comments = "编辑操作员名字")
    private String modiOperName;

    /**
     * 操作机构(拥有机构)
     */
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 编辑日期
     */
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑日期", comments = "编辑日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;

    /**
     * 编辑时间
     */
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="编辑时间", comments = "编辑时间")
    private String modiTime;


    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;
    
    /**
     * 备注(描述)
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="备注", comments = "备注")
    private String description;

    public String getReceivableNo() {
        return this.receivableNo;
    }

    public void setReceivableNo(String anReceivableNo) {
        this.receivableNo = anReceivableNo;
    }

    public String getBtNo() {
        return this.btNo;
    }

    public void setBtNo(String anBtNo) {
        this.btNo = anBtNo;
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

    public String getCreditor() {
        return this.creditor;
    }

    public void setCreditor(String anCreditor) {
        this.creditor = anCreditor;
    }

    public String getDebtor() {
        return this.debtor;
    }

    public void setDebtor(String anDebtor) {
        this.debtor = anDebtor;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public BigDecimal getSurplusBalance() {
        return this.surplusBalance;
    }

    public void setSurplusBalance(BigDecimal anSurplusBalance) {
        this.surplusBalance = anSurplusBalance;
    }

    public BigDecimal getDeductionBalance() {
        return this.deductionBalance;
    }

    public void setDeductionBalance(BigDecimal anDeductionBalance) {
        this.deductionBalance = anDeductionBalance;
    }

    public BigDecimal getStatementBalance() {
        return this.statementBalance;
    }

    public void setStatementBalance(BigDecimal anStatementBalance) {
        this.statementBalance = anStatementBalance;
    }

    public String getAgreeNo() {
        return this.agreeNo;
    }

    public void setAgreeNo(String anAgreeNo) {
        this.agreeNo = anAgreeNo;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String anInvoiceNo) {
        this.invoiceNo = anInvoiceNo;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
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

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }


    @Override
    public String toString() {
        return "ScfReceivableDO [receivableNo=" + this.receivableNo + ", btNo=" + this.btNo + ", coreCustNo=" + this.coreCustNo + ", coreCustName="
                + this.coreCustName + ", custNo=" + this.custNo + ", custName=" + this.custName + ", creditor=" + this.creditor + ", debtor="
                + this.debtor + ", balance=" + this.balance + ", surplusBalance=" + this.surplusBalance + ", deductionBalance="
                + this.deductionBalance + ", statementBalance=" + this.statementBalance + ", agreeNo=" + this.agreeNo + ", invoiceNo="
                + this.invoiceNo + ", endDate=" + this.endDate + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", modiOperId="
                + this.modiOperId + ", modiOperName=" + this.modiOperName + ", operOrg=" + this.operOrg + ", modiDate=" + this.modiDate
                + ", modiTime=" + this.modiTime + ", batchNo=" + this.batchNo + ", description=" + this.description + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.agreeNo == null) ? 0 : this.agreeNo.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.btNo == null) ? 0 : this.btNo.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.creditor == null) ? 0 : this.creditor.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.debtor == null) ? 0 : this.debtor.hashCode());
        result = prime * result + ((this.deductionBalance == null) ? 0 : this.deductionBalance.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.invoiceNo == null) ? 0 : this.invoiceNo.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.receivableNo == null) ? 0 : this.receivableNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.statementBalance == null) ? 0 : this.statementBalance.hashCode());
        result = prime * result + ((this.surplusBalance == null) ? 0 : this.surplusBalance.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfReceivableDO other = (ScfReceivableDO) obj;
        if (this.agreeNo == null) {
            if (other.agreeNo != null) return false;
        }
        else if (!this.agreeNo.equals(other.agreeNo)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.btNo == null) {
            if (other.btNo != null) return false;
        }
        else if (!this.btNo.equals(other.btNo)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        }
        else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        }
        else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.creditor == null) {
            if (other.creditor != null) return false;
        }
        else if (!this.creditor.equals(other.creditor)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.debtor == null) {
            if (other.debtor != null) return false;
        }
        else if (!this.debtor.equals(other.debtor)) return false;
        if (this.deductionBalance == null) {
            if (other.deductionBalance != null) return false;
        }
        else if (!this.deductionBalance.equals(other.deductionBalance)) return false;
        if (this.description == null) {
            if (other.description != null) return false;
        }
        else if (!this.description.equals(other.description)) return false;
        if (this.endDate == null) {
            if (other.endDate != null) return false;
        }
        else if (!this.endDate.equals(other.endDate)) return false;
        if (this.invoiceNo == null) {
            if (other.invoiceNo != null) return false;
        }
        else if (!this.invoiceNo.equals(other.invoiceNo)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        }
        else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        }
        else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        }
        else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        }
        else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.receivableNo == null) {
            if (other.receivableNo != null) return false;
        }
        else if (!this.receivableNo.equals(other.receivableNo)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.statementBalance == null) {
            if (other.statementBalance != null) return false;
        }
        else if (!this.statementBalance.equals(other.statementBalance)) return false;
        if (this.surplusBalance == null) {
            if (other.surplusBalance != null) return false;
        }
        else if (!this.surplusBalance.equals(other.surplusBalance)) return false;
        return true;
    }

    public ScfReceivableDO() {
        super();
    }

    public void initAddValue(CustOperatorInfo anOperatorInfo, boolean anConfirmFlag) {
        
        BTAssert.notNull(anOperatorInfo,"无法获取登录信息,操作失败");
        double operValue = this.surplusBalance.add(this.deductionBalance).add(this.statementBalance).doubleValue();
        if(this.balance.doubleValue()!=operValue){
            BTAssert.notNull(null,"应付账款金额 = 应付账款余额 + 抵扣金额 + 已结算金额!保存失败"); 
        }
        this.setId(SerialGenerator.getLongValue("ScfReceivableDO.id"));
        this.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        this.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        this.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        if(anConfirmFlag){
            this.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM);
        }
        this.creditor=this.custNo+"";
        this.debtor=this.coreCustNo+"";
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        if (null != anOperatorInfo) {
            this.setModiOperId(anOperatorInfo.getId());
            this.modiOperName = anOperatorInfo.getName();
            this.operOrg = anOperatorInfo.getOperOrg();
        }
        
    }

    public ScfReceivableDO initModifyValue(ScfReceivableDO anReceivable) {
        
        double operValue = this.surplusBalance.add(this.deductionBalance).add(this.statementBalance).doubleValue();
        if(this.balance.doubleValue()!=operValue){
            BTAssert.notNull(null,"应付账款金额 = 应付账款余额 + 抵扣金额 + 已结算金额!保存失败"); 
        }
        this.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        this.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        this.creditor=this.custNo+"";
        this.debtor=this.coreCustNo+"";
        this.setModiOperId(anReceivable.getModiOperId());
        this.modiOperName = anReceivable.getModiOperName();
        this.operOrg = anReceivable.getOperOrg();
        this.btNo=anReceivable.getBtNo();
        this.modiDate=BetterDateUtils.getNumDate();
        this.modiTime=BetterDateUtils.getNumTime();
        this.setId(anReceivable.getId());
        this.regDate=anReceivable.getRegDate();
        this.regTime=anReceivable.getRegTime();
        return this;
    }
    

    
}
