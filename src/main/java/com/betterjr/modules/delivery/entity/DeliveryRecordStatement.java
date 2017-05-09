package com.betterjr.modules.delivery.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.delivery.data.DeliveryConstantCollentions;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_delivery_record")
public class DeliveryRecordStatement implements BetterjrEntity {
    
    //编号
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;

    //投递记录主表编号
    @Column(name = "L_DELIVERY_ID",  columnDefinition="INTEGER" )
    private Long deliverId;

    //投递记录主表凭证号
    @Column(name = "C_DELIVERY_REFNO",  columnDefinition="VARCHAR" )
    private String deliverRefNo;

    //月账单编号
    @Column(name = "L_MONTHLY_STATEMENT_ID",  columnDefinition="INTEGER" )
    private Long monthlyStatementId;

    //月账单凭证号
    @Column(name = "C_MONTHLY_STATEMENT_REFNO",  columnDefinition="VARCHAR" )
    private String monthlyStatementRefNo;

    //开始时间
    @Column(name = "D_PAY_BEGIN_DATE",  columnDefinition="VARCHAR" )
    private String payBeginDate;

    //结束时间
    @Column(name = "D_PAY_END_DATE",  columnDefinition="VARCHAR" )
    private String payEndDate;

    //总金额
    @Column(name = "F_TOTAL_BLANCE",  columnDefinition="DECIMAL" )
    private BigDecimal totalBlance;

    //总的佣金笔数
    @Column(name = "N_TOTAL_AMOUNT",  columnDefinition="INTEGER" )
    private BigDecimal totalAmount;

    //总的付成功的金额
    @Column(name = "F_PAY_TOTAL_SUCCESS_BALANCE",  columnDefinition="DECIMAL" )
    private BigDecimal payTotalSuccessBlance;

    //总的付成功的项
    @Column(name = "N_PAY_TOTAL_SUCCESS_AMOUNT",  columnDefinition="INTEGER" )
    private BigDecimal payTotalSuccessitems;

    //总的付失败的金额
    @Column(name = "F_PAY_TOTAL_FAILURE_BALANCE",  columnDefinition="DECIMAL" )
    private BigDecimal payTotalFailureBlance;

    //总的付失败的项数
    @Column(name = "N_PAY_TOTAL_FAILURE_AMOUNT",  columnDefinition="INTEGER" )
    private BigDecimal payTotalFailureitems;

    //拥有公司ID
    @Column(name = "L_OWN_CUSTNO",  columnDefinition="INTEGER" )
    private Long ownCustNo;

    //拥有公司名称
    @Column(name = "C_OWN_CUSTNAME",  columnDefinition="VARCHAR" )
    private String ownCustName;

    //拥有公司操作结构
    @Column(name = "C_OWN_OPERORG",  columnDefinition="VARCHAR" )
    private String ownOperOrg;
    
    //0 可用   1 不可用，已删除状态
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;
    
    //传递状态  0 未投递  1 已投递   2已确认
    @Column(name = "C_EXPRESS_STATUS",  columnDefinition="VARCHAR" )
    private String expressStatus;
    
    //对账月份yyyyMM
    @Column(name = "D_BILL_MONTH",  columnDefinition="VARCHAR" )
    private String billMonth;
    

    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    private String operOrg;
    
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    private Long regOperId;
    
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    private String regOperName;
    
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    private String regDate;
    
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;
    
    @Column(name = "N_VERSION",  columnDefinition="VARCHAR" )
    private String version;

    private static final long serialVersionUID = 1493717117087L;
    
    
    public String getBillMonth() {
        return this.billMonth;
    }

    public void setBillMonth(String anBillMonth) {
        this.billMonth = anBillMonth;
    }

    public String getExpressStatus() {
        return this.expressStatus;
    }

    public void setExpressStatus(String anExpressStatus) {
        this.expressStatus = anExpressStatus;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public Long getMonthlyStatementId() {
        return monthlyStatementId;
    }

    public void setMonthlyStatementId(Long monthlyStatementId) {
        this.monthlyStatementId = monthlyStatementId;
    }

    public String getMonthlyStatementRefNo() {
        return monthlyStatementRefNo;
    }

    public void setMonthlyStatementRefNo(String monthlyStatementRefNo) {
        this.monthlyStatementRefNo = monthlyStatementRefNo == null ? null : monthlyStatementRefNo.trim();
    }

    public String getPayBeginDate() {
        return payBeginDate;
    }

    public void setPayBeginDate(String payBeginDate) {
        this.payBeginDate = payBeginDate == null ? null : payBeginDate.trim();
    }

    public String getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(String payEndDate) {
        this.payEndDate = payEndDate == null ? null : payEndDate.trim();
    }

    public BigDecimal getTotalBlance() {
        return totalBlance;
    }

    public void setTotalBlance(BigDecimal totalBlance) {
        this.totalBlance = totalBlance;
    }

    

    public BigDecimal getPayTotalSuccessBlance() {
        return payTotalSuccessBlance;
    }

    public void setPayTotalSuccessBlance(BigDecimal payTotalSuccessBlance) {
        this.payTotalSuccessBlance = payTotalSuccessBlance;
    }

    public BigDecimal getPayTotalSuccessitems() {
        return payTotalSuccessitems;
    }

    public void setPayTotalSuccessitems(BigDecimal payTotalSuccessitems) {
        this.payTotalSuccessitems = payTotalSuccessitems;
    }

    public BigDecimal getPayTotalFailureBlance() {
        return payTotalFailureBlance;
    }

    public void setPayTotalFailureBlance(BigDecimal payTotalFailureBlance) {
        this.payTotalFailureBlance = payTotalFailureBlance;
    }

    public BigDecimal getPayTotalFailureitems() {
        return payTotalFailureitems;
    }

    public void setPayTotalFailureitems(BigDecimal payTotalFailureitems) {
        this.payTotalFailureitems = payTotalFailureitems;
    }

    public Long getOwnCustNo() {
        return ownCustNo;
    }

    public void setOwnCustNo(Long ownCustNo) {
        this.ownCustNo = ownCustNo;
    }

    public String getOwnCustName() {
        return ownCustName;
    }

    public void setOwnCustName(String ownCustName) {
        this.ownCustName = ownCustName == null ? null : ownCustName.trim();
    }

    public String getOwnOperOrg() {
        return ownOperOrg;
    }

    public void setOwnOperOrg(String ownOperOrg) {
        this.ownOperOrg = ownOperOrg == null ? null : ownOperOrg.trim();
    }

    public Long getDeliverId() {
        return this.deliverId;
    }

    public void setDeliverId(Long anDeliverId) {
        this.deliverId = anDeliverId;
    }

    public String getDeliverRefNo() {
        return this.deliverRefNo;
    }

    public void setDeliverRefNo(String anDeliverRefNo) {
        this.deliverRefNo = anDeliverRefNo;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal anTotalAmount) {
        this.totalAmount = anTotalAmount;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
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

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.deliverId == null) ? 0 : this.deliverId.hashCode());
        result = prime * result + ((this.deliverRefNo == null) ? 0 : this.deliverRefNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.monthlyStatementId == null) ? 0 : this.monthlyStatementId.hashCode());
        result = prime * result + ((this.monthlyStatementRefNo == null) ? 0 : this.monthlyStatementRefNo.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.ownCustName == null) ? 0 : this.ownCustName.hashCode());
        result = prime * result + ((this.ownCustNo == null) ? 0 : this.ownCustNo.hashCode());
        result = prime * result + ((this.ownOperOrg == null) ? 0 : this.ownOperOrg.hashCode());
        result = prime * result + ((this.payBeginDate == null) ? 0 : this.payBeginDate.hashCode());
        result = prime * result + ((this.payEndDate == null) ? 0 : this.payEndDate.hashCode());
        result = prime * result + ((this.payTotalFailureBlance == null) ? 0 : this.payTotalFailureBlance.hashCode());
        result = prime * result + ((this.payTotalFailureitems == null) ? 0 : this.payTotalFailureitems.hashCode());
        result = prime * result + ((this.payTotalSuccessBlance == null) ? 0 : this.payTotalSuccessBlance.hashCode());
        result = prime * result + ((this.payTotalSuccessitems == null) ? 0 : this.payTotalSuccessitems.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.totalAmount == null) ? 0 : this.totalAmount.hashCode());
        result = prime * result + ((this.totalBlance == null) ? 0 : this.totalBlance.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DeliveryRecordStatement other = (DeliveryRecordStatement) obj;
        if (this.deliverId == null) {
            if (other.deliverId != null) return false;
        }
        else if (!this.deliverId.equals(other.deliverId)) return false;
        if (this.deliverRefNo == null) {
            if (other.deliverRefNo != null) return false;
        }
        else if (!this.deliverRefNo.equals(other.deliverRefNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.monthlyStatementId == null) {
            if (other.monthlyStatementId != null) return false;
        }
        else if (!this.monthlyStatementId.equals(other.monthlyStatementId)) return false;
        if (this.monthlyStatementRefNo == null) {
            if (other.monthlyStatementRefNo != null) return false;
        }
        else if (!this.monthlyStatementRefNo.equals(other.monthlyStatementRefNo)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.ownCustName == null) {
            if (other.ownCustName != null) return false;
        }
        else if (!this.ownCustName.equals(other.ownCustName)) return false;
        if (this.ownCustNo == null) {
            if (other.ownCustNo != null) return false;
        }
        else if (!this.ownCustNo.equals(other.ownCustNo)) return false;
        if (this.ownOperOrg == null) {
            if (other.ownOperOrg != null) return false;
        }
        else if (!this.ownOperOrg.equals(other.ownOperOrg)) return false;
        if (this.payBeginDate == null) {
            if (other.payBeginDate != null) return false;
        }
        else if (!this.payBeginDate.equals(other.payBeginDate)) return false;
        if (this.payEndDate == null) {
            if (other.payEndDate != null) return false;
        }
        else if (!this.payEndDate.equals(other.payEndDate)) return false;
        if (this.payTotalFailureBlance == null) {
            if (other.payTotalFailureBlance != null) return false;
        }
        else if (!this.payTotalFailureBlance.equals(other.payTotalFailureBlance)) return false;
        if (this.payTotalFailureitems == null) {
            if (other.payTotalFailureitems != null) return false;
        }
        else if (!this.payTotalFailureitems.equals(other.payTotalFailureitems)) return false;
        if (this.payTotalSuccessBlance == null) {
            if (other.payTotalSuccessBlance != null) return false;
        }
        else if (!this.payTotalSuccessBlance.equals(other.payTotalSuccessBlance)) return false;
        if (this.payTotalSuccessitems == null) {
            if (other.payTotalSuccessitems != null) return false;
        }
        else if (!this.payTotalSuccessitems.equals(other.payTotalSuccessitems)) return false;
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
        if (this.totalAmount == null) {
            if (other.totalAmount != null) return false;
        }
        else if (!this.totalAmount.equals(other.totalAmount)) return false;
        if (this.totalBlance == null) {
            if (other.totalBlance != null) return false;
        }
        else if (!this.totalBlance.equals(other.totalBlance)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        }
        else if (!this.version.equals(other.version)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "DeliveryRecordStatement [id=" + this.id + ", deliverId=" + this.deliverId + ", deliverRefNo=" + this.deliverRefNo
                + ", monthlyStatementId=" + this.monthlyStatementId + ", monthlyStatementRefNo=" + this.monthlyStatementRefNo + ", payBeginDate="
                + this.payBeginDate + ", payEndDate=" + this.payEndDate + ", totalBlance=" + this.totalBlance + ", totalAmount=" + this.totalAmount
                + ", payTotalSuccessBlance=" + this.payTotalSuccessBlance + ", payTotalSuccessitems=" + this.payTotalSuccessitems
                + ", payTotalFailureBlance=" + this.payTotalFailureBlance + ", payTotalFailureitems=" + this.payTotalFailureitems + ", ownCustNo="
                + this.ownCustNo + ", ownCustName=" + this.ownCustName + ", ownOperOrg=" + this.ownOperOrg + ", businStatus=" + this.businStatus
                + ", expressStatus=" + this.expressStatus + ", billMonth=" + this.billMonth + ", operOrg=" + this.operOrg + ", regOperId="
                + this.regOperId + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", version="
                + this.version + "]";
    }

    public void saveAddInit() {
       
        this.setId(SerialGenerator.getLongValue("CommissionMonthlyStatement.id"));
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegOperId(UserUtils.getOperatorInfo().getId());
        this.setRegOperName(UserUtils.getOperatorInfo().getName());
        this.setOperOrg(UserUtils.getOperatorInfo().getOperOrg());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setVersion("1");
        this.businStatus=DeliveryConstantCollentions.DELIVERY_STATEMENT_BUSIN_STATUS_CANUSERD;
        this.expressStatus=DeliveryConstantCollentions.DELIVERY_STATEMENT_EXPRESS_STATUS_NOEXPRESS;
    }

    public DeliveryRecordStatement(Long anId) {
        super();
        this.id = anId;
    }

    public DeliveryRecordStatement() {
        super();
    }
    
    

}