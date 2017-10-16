package com.betterjr.modules.payorder.entity;

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

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.payorder.data.PayOrderFileConstantCollentions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_pos_source_pay_file")
public class PayOrderFile implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -6645045911621523446L;
    
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;
    
    /**
     * 提前付款日期
     */
    @Column(name = "D_REQUEST_PAY_DATE",  columnDefinition="varchar" )
    @MetaData( value="提前付款日期", comments = "提前付款日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String requestPayDate;

    /**
     * 付款总笔数
     */
    @Column(name = "N_PAY_AMOUNT", columnDefinition = "INTEGER")
    @MetaData( value="付款总笔数", comments = "付款总笔数")
    private Long payAmount;
    
    /**
     * 申请总金额
     */
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    @MetaData( value="申请总金额", comments = "申请总金额")
    private BigDecimal balance;
    
    /**
     * 文件名称
     */
    @Id
    @Column(name = "C_FILE_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="文件名称", comments = "文件名称")
    private String fileName;
    
    /**
     * 凭证编号
     */
    @Id
    @Column(name = "C_REF_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="凭证编号", comments = "凭证编号")
    private String refNo;
    
    /**
     * 生成文件上传的文件Id
     */
    @Column(name = "L_FILEITEM_ID", columnDefinition = "INTEGER")
    @MetaData( value="生成文件上传的文件Id", comments = "生成文件上传的文件Id")
    private Long fileItemId;
    
    /**
     * 记录生成人
     */
    @Column(name = "L_ORG_OPERID", columnDefinition = "INTEGER")
    @MetaData( value="记录生成人", comments = "记录生成人")
    private Long orgOperId;
    
    /**
     * 记录人名称
     */
    @Column(name = "C_ORG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData( value="记录人名称", comments = "记录人名称")
    private String orgOperName;
    
    /**
     * 申请日期
     */
    @Column(name = "D_ORG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="申请日期", comments = "申请日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    /**
     * 生成时间
     */
    @Column(name = "T_ORG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="生成时间", comments = "生成时间")
    private String regTime;
    
    /**
     * 生成ip
     */
    @Column(name = "C_ORG_IP",  columnDefinition="VARCHAR" )
    @MetaData( value="生成ip", comments = "生成ip")
    private String regIp;
    
    /**
     * 付款池id
     */
    @Column(name = "N_POOL_ID", columnDefinition = "INTEGER")
    @MetaData( value="付款池id", comments = "付款池id")
    private Long poolId;
    
    /**
     * 文件作用类型 0生成付款文件 1上传的付款结果文件
     */
    @Column(name = "C_INFO_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="文件作用类型 0生成付款文件 1上传的付款结果文件", comments = "文件作用类型 0生成付款文件 1上传的付款结果文件")
    private String infoType;
    
    /**
     * 文件状态 1 未确认 （包括生成的需要付款申请文件）  2已审核  9已删除
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="文件状态 1 未确认 （包括生成的需要付款申请文件）  2已审核  9已删除", comments = "文件状态 1 未确认 （包括生成的需要付款申请文件）  2已审核  9已删除")
    private String businStatus;
    
    /**
     * 生成的需付款文件Id
     */
    @Column(name = "N_SOURCE_FILE_ID", columnDefinition = "INTEGER")
    @MetaData( value="生成的需付款文件Id", comments = "生成的需付款文件Id")
    private Long sourceFileId;
    
    /**
     * 审核人Id
     */
    @Column(name = "L_AUDIT_OPERID", columnDefinition = "INTEGER")
    @MetaData( value="审核人Id", comments = "审核人Id")
    private Long auditOperId;
    
    /**
     * 审核人名称
     */
    @Column(name = "C_AUDIT_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData( value="审核人名称", comments = "审核人名称")
    private Long auditOperName;
    
    /**
     * 审核日期
     */
    @Column(name = "D_AUDIT_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="审核日期", comments = "审核日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String auditDate;
    
    /**
     * 审核时间
     */
    @Column(name = "T_AUDIT_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="审核时间", comments = "审核时间")
    private String auditTime;
    
    @Transient
    private List<PayOrderPoolRecord> recordList = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getRequestPayDate() {
        return this.requestPayDate;
    }

    public void setRequestPayDate(String anRequestPayDate) {
        this.requestPayDate = anRequestPayDate;
    }

    public Long getPayAmount() {
        return this.payAmount;
    }

    public void setPayAmount(Long anPayAmount) {
        this.payAmount = anPayAmount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal anBalance) {
        this.balance = anBalance;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String anFileName) {
        this.fileName = anFileName;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String anRefNo) {
        this.refNo = anRefNo;
    }

    public Long getFileItemId() {
        return this.fileItemId;
    }

    public void setFileItemId(Long anFileItemId) {
        this.fileItemId = anFileItemId;
    }

    public Long getOrgOperId() {
        return this.orgOperId;
    }

    public void setOrgOperId(Long anOrgOperId) {
        this.orgOperId = anOrgOperId;
    }

    public String getOrgOperName() {
        return this.orgOperName;
    }

    public void setOrgOperName(String anOrgOperName) {
        this.orgOperName = anOrgOperName;
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

    public String getRegIp() {
        return this.regIp;
    }

    public void setRegIp(String anRegIp) {
        this.regIp = anRegIp;
    }

    public Long getPoolId() {
        return this.poolId;
    }

    public void setPoolId(Long anPoolId) {
        this.poolId = anPoolId;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String anInfoType) {
        this.infoType = anInfoType;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public Long getSourceFileId() {
        return this.sourceFileId;
    }

    public void setSourceFileId(Long anSourceFileId) {
        this.sourceFileId = anSourceFileId;
    }

    public Long getAuditOperId() {
        return this.auditOperId;
    }

    public void setAuditOperId(Long anAuditOperId) {
        this.auditOperId = anAuditOperId;
    }

    public Long getAuditOperName() {
        return this.auditOperName;
    }

    public void setAuditOperName(Long anAuditOperName) {
        this.auditOperName = anAuditOperName;
    }

    public String getAuditDate() {
        return this.auditDate;
    }

    public void setAuditDate(String anAuditDate) {
        this.auditDate = anAuditDate;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String anAuditTime) {
        this.auditTime = anAuditTime;
    }
    
    public List<PayOrderPoolRecord> getRecordList() {
        return this.recordList;
    }

    public void setRecordList(List<PayOrderPoolRecord> anRecordList) {
        this.recordList = anRecordList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.auditDate == null) ? 0 : this.auditDate.hashCode());
        result = prime * result + ((this.auditOperId == null) ? 0 : this.auditOperId.hashCode());
        result = prime * result + ((this.auditOperName == null) ? 0 : this.auditOperName.hashCode());
        result = prime * result + ((this.auditTime == null) ? 0 : this.auditTime.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.fileItemId == null) ? 0 : this.fileItemId.hashCode());
        result = prime * result + ((this.fileName == null) ? 0 : this.fileName.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.orgOperId == null) ? 0 : this.orgOperId.hashCode());
        result = prime * result + ((this.orgOperName == null) ? 0 : this.orgOperName.hashCode());
        result = prime * result + ((this.payAmount == null) ? 0 : this.payAmount.hashCode());
        result = prime * result + ((this.poolId == null) ? 0 : this.poolId.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regIp == null) ? 0 : this.regIp.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.requestPayDate == null) ? 0 : this.requestPayDate.hashCode());
        result = prime * result + ((this.sourceFileId == null) ? 0 : this.sourceFileId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PayOrderFile other = (PayOrderFile) obj;
        if (this.auditDate == null) {
            if (other.auditDate != null) return false;
        }
        else if (!this.auditDate.equals(other.auditDate)) return false;
        if (this.auditOperId == null) {
            if (other.auditOperId != null) return false;
        }
        else if (!this.auditOperId.equals(other.auditOperId)) return false;
        if (this.auditOperName == null) {
            if (other.auditOperName != null) return false;
        }
        else if (!this.auditOperName.equals(other.auditOperName)) return false;
        if (this.auditTime == null) {
            if (other.auditTime != null) return false;
        }
        else if (!this.auditTime.equals(other.auditTime)) return false;
        if (this.balance == null) {
            if (other.balance != null) return false;
        }
        else if (!this.balance.equals(other.balance)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.fileItemId == null) {
            if (other.fileItemId != null) return false;
        }
        else if (!this.fileItemId.equals(other.fileItemId)) return false;
        if (this.fileName == null) {
            if (other.fileName != null) return false;
        }
        else if (!this.fileName.equals(other.fileName)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        }
        else if (!this.infoType.equals(other.infoType)) return false;
        if (this.orgOperId == null) {
            if (other.orgOperId != null) return false;
        }
        else if (!this.orgOperId.equals(other.orgOperId)) return false;
        if (this.orgOperName == null) {
            if (other.orgOperName != null) return false;
        }
        else if (!this.orgOperName.equals(other.orgOperName)) return false;
        if (this.payAmount == null) {
            if (other.payAmount != null) return false;
        }
        else if (!this.payAmount.equals(other.payAmount)) return false;
        if (this.poolId == null) {
            if (other.poolId != null) return false;
        }
        else if (!this.poolId.equals(other.poolId)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        }
        else if (!this.refNo.equals(other.refNo)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regIp == null) {
            if (other.regIp != null) return false;
        }
        else if (!this.regIp.equals(other.regIp)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.requestPayDate == null) {
            if (other.requestPayDate != null) return false;
        }
        else if (!this.requestPayDate.equals(other.requestPayDate)) return false;
        if (this.sourceFileId == null) {
            if (other.sourceFileId != null) return false;
        }
        else if (!this.sourceFileId.equals(other.sourceFileId)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "PayOrderFile [id=" + this.id + ", requestPayDate=" + this.requestPayDate + ", payAmount=" + this.payAmount + ", balance="
                + this.balance + ", fileName=" + this.fileName + ", refNo=" + this.refNo + ", fileItemId=" + this.fileItemId + ", orgOperId="
                + this.orgOperId + ", orgOperName=" + this.orgOperName + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", regIp="
                + this.regIp + ", poolId=" + this.poolId + ", infoType=" + this.infoType + ", businStatus=" + this.businStatus + ", sourceFileId="
                + this.sourceFileId + ", auditOperId=" + this.auditOperId + ", auditOperName=" + this.auditOperName + ", auditDate=" + this.auditDate
                + ", auditTime=" + this.auditTime + ", recordList=" + this.recordList + "]";
    }

    public PayOrderFile saveAddInitValue(String anInfoType) {
        
        CustOperatorInfo info = UserUtils.getOperatorInfo();
        BTAssert.notNull(info,"请登录,操作失败");
        
        this.setBusinStatus(PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM);
        this.setId(SerialGenerator.getLongValue("PayOrderFile.id"));
        this.setInfoType(anInfoType);
        this.setOrgOperId(info.getId());
        this.setOrgOperName(info.getName());
        this.setRefNo(SequenceFactory.generate("PLAT_COMMON", "#{Date:yyyyMMdd}#{Seq:12}", "D"));
        this.setRegDate("");
        this.setRegTime("");
        this.setRegIp(UserUtils.getRequestIp());
        
        return this;
    }

    
    
    
}
