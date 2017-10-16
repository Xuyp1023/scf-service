package com.betterjr.modules.delivery.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.delivery.data.DeliveryConstantCollentions;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_delivery")
public class DeliveryRecord implements BetterjrEntity {

    // 编号
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    // 凭证编号
    @Column(name = "C_REFNO", columnDefinition = "VARCHAR")
    private String refNo;

    // 总金额
    @Column(name = "F_TOTAL_BLANCE", columnDefinition = "DECIMAL")
    private BigDecimal totalBlance;

    // 总笔数
    @Column(name = "N_TOTAL_AMOUNT", columnDefinition = "DECIMAL")
    private BigDecimal totalAmount;

    // 投递日期
    @Column(name = "D_POST_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String postDate;

    // 投递时间
    @Column(name = "T_POST_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String postTime;

    // 接收公司(青海移动)
    @Column(name = "L_POST_CUSTNO", columnDefinition = "INTEGER")
    private Long postCustNo;

    // 接收公司名称(青海移动)
    @Column(name = "C_POST_CUSTNAME", columnDefinition = "VARCHAR")
    private String postCustName;

    // 接收公司机构(青海移动)
    @Column(name = "C_POST_OPERORG", columnDefinition = "VARCHAR")
    private String postOperOrg;

    // 操作公司
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    // 操作公司名称
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    private String custName;

    // 操作公司机构
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    // 业务状态 0未处理 1已投递 2确认
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    private String businStatus;

    // 确认日期
    @Column(name = "D_CONFIRM_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String confirmDate;

    // 确认时间
    @Column(name = "T_CONFIRM_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String confirmTime;

    // 确认操作员
    @Column(name = "L_CONFIRM_OPERID", columnDefinition = "INTEGER")
    private Long confirmOperId;

    // 确认操作员名称
    @Column(name = "L_CONFIRM_OPERNAME", columnDefinition = "VARCHAR")
    private String confirmOperName;

    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    private Long regOperId;

    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    private String regOperName;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String regTime;

    @Column(name = "N_VERSION", columnDefinition = "VARCHAR")
    private String version;

    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    private String description;

    @Transient
    private List<DeliveryRecordStatement> recordStatementList = new ArrayList<>();

    private static final long serialVersionUID = -189024275871128671L;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo == null ? null : refNo.trim();
    }

    public BigDecimal getTotalBlance() {
        return totalBlance;
    }

    public void setTotalBlance(BigDecimal totalBlance) {
        this.totalBlance = totalBlance;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate == null ? null : postDate.trim();
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime == null ? null : postTime.trim();
    }

    public Long getPostCustNo() {
        return postCustNo;
    }

    public void setPostCustNo(Long postCustNo) {
        this.postCustNo = postCustNo;
    }

    public String getPostCustName() {
        return postCustName;
    }

    public void setPostCustName(String postCustName) {
        this.postCustName = postCustName == null ? null : postCustName.trim();
    }

    public String getPostOperOrg() {
        return postOperOrg;
    }

    public void setPostOperOrg(String postOperOrg) {
        this.postOperOrg = postOperOrg == null ? null : postOperOrg.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate == null ? null : confirmDate.trim();
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime == null ? null : confirmTime.trim();
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal anTotalAmount) {
        this.totalAmount = anTotalAmount;
    }

    public Long getConfirmOperId() {
        return this.confirmOperId;
    }

    public void setConfirmOperId(Long anConfirmOperId) {
        this.confirmOperId = anConfirmOperId;
    }

    public String getConfirmOperName() {
        return this.confirmOperName;
    }

    public void setConfirmOperName(String anConfirmOperName) {
        this.confirmOperName = anConfirmOperName;
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

    public List<DeliveryRecordStatement> getRecordStatementList() {
        return this.recordStatementList;
    }

    public void setRecordStatementList(List<DeliveryRecordStatement> anRecordStatementList) {
        this.recordStatementList = anRecordStatementList;
    }

    public DeliveryRecord(String anRefNo) {
        super();
        this.refNo = anRefNo;
    }

    public DeliveryRecord(Long anId) {
        super();
        this.id = anId;
    }

    public DeliveryRecord() {
        super();
    }

    @Override
    public String toString() {
        return "DeliveryRecord [id=" + this.id + ", refNo=" + this.refNo + ", totalBlance=" + this.totalBlance
                + ", totalAmount=" + this.totalAmount + ", postDate=" + this.postDate + ", postTime=" + this.postTime
                + ", postCustNo=" + this.postCustNo + ", postCustName=" + this.postCustName + ", postOperOrg="
                + this.postOperOrg + ", custNo=" + this.custNo + ", custName=" + this.custName + ", operOrg="
                + this.operOrg + ", businStatus=" + this.businStatus + ", confirmDate=" + this.confirmDate
                + ", confirmTime=" + this.confirmTime + ", confirmOperId=" + this.confirmOperId + ", confirmOperName="
                + this.confirmOperName + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName
                + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", version=" + this.version
                + ", description=" + this.description + ", recordStatementList=" + this.recordStatementList + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.confirmDate == null) ? 0 : this.confirmDate.hashCode());
        result = prime * result + ((this.confirmOperId == null) ? 0 : this.confirmOperId.hashCode());
        result = prime * result + ((this.confirmOperName == null) ? 0 : this.confirmOperName.hashCode());
        result = prime * result + ((this.confirmTime == null) ? 0 : this.confirmTime.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.postCustName == null) ? 0 : this.postCustName.hashCode());
        result = prime * result + ((this.postCustNo == null) ? 0 : this.postCustNo.hashCode());
        result = prime * result + ((this.postDate == null) ? 0 : this.postDate.hashCode());
        result = prime * result + ((this.postOperOrg == null) ? 0 : this.postOperOrg.hashCode());
        result = prime * result + ((this.postTime == null) ? 0 : this.postTime.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
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
        DeliveryRecord other = (DeliveryRecord) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.confirmDate == null) {
            if (other.confirmDate != null) return false;
        } else if (!this.confirmDate.equals(other.confirmDate)) return false;
        if (this.confirmOperId == null) {
            if (other.confirmOperId != null) return false;
        } else if (!this.confirmOperId.equals(other.confirmOperId)) return false;
        if (this.confirmOperName == null) {
            if (other.confirmOperName != null) return false;
        } else if (!this.confirmOperName.equals(other.confirmOperName)) return false;
        if (this.confirmTime == null) {
            if (other.confirmTime != null) return false;
        } else if (!this.confirmTime.equals(other.confirmTime)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        } else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.postCustName == null) {
            if (other.postCustName != null) return false;
        } else if (!this.postCustName.equals(other.postCustName)) return false;
        if (this.postCustNo == null) {
            if (other.postCustNo != null) return false;
        } else if (!this.postCustNo.equals(other.postCustNo)) return false;
        if (this.postDate == null) {
            if (other.postDate != null) return false;
        } else if (!this.postDate.equals(other.postDate)) return false;
        if (this.postOperOrg == null) {
            if (other.postOperOrg != null) return false;
        } else if (!this.postOperOrg.equals(other.postOperOrg)) return false;
        if (this.postTime == null) {
            if (other.postTime != null) return false;
        } else if (!this.postTime.equals(other.postTime)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        } else if (!this.refNo.equals(other.refNo)) return false;
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
        if (this.totalAmount == null) {
            if (other.totalAmount != null) return false;
        } else if (!this.totalAmount.equals(other.totalAmount)) return false;
        if (this.totalBlance == null) {
            if (other.totalBlance != null) return false;
        } else if (!this.totalBlance.equals(other.totalBlance)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        } else if (!this.version.equals(other.version)) return false;
        return true;
    }

    public void saveAddInit(CustOperatorInfo anOperatorInfo, CustInfo anDefCustInfo,
            CommissionMonthlyStatement anStatement) {

        this.setBusinStatus(DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_NO_HANDEL);
        this.setCustName(anDefCustInfo.getCustName());
        this.setCustNo(anDefCustInfo.getCustNo());
        this.setOperOrg(anOperatorInfo.getOperOrg());
        this.setRefNo(SequenceFactory.generate("PLAT_COMMON", "#{Date:yyyyMMdd}#{Seq:12}", "D"));
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setVersion("1");
        this.setPostDate(BetterDateUtils.getNumDate());
        this.setPostTime(BetterDateUtils.getNumTime());
        this.setPostCustName(anStatement.getOwnCustName());
        this.setPostCustNo(anStatement.getOwnCustNo());
        this.setPostOperOrg(anStatement.getOwnOperOrg());
        this.setId(SerialGenerator.getLongValue("DeliveryRecord.id"));
    }

    public void saveExpressInit(CustOperatorInfo anOperatorInfo) {

        this.setBusinStatus(DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_EXPRESS);
        this.setPostDate(BetterDateUtils.getNumDate());
        this.setPostTime(BetterDateUtils.getNumTime());

    }

    public void saveConfirmInit(CustOperatorInfo anOperatorInfo) {

        this.setBusinStatus(DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_CONFIRM);
        this.confirmDate = BetterDateUtils.getNumDate();
        this.confirmTime = BetterDateUtils.getNumTime();
        this.confirmOperId = anOperatorInfo.getId();
        this.confirmOperName = anOperatorInfo.getName();

    }

}