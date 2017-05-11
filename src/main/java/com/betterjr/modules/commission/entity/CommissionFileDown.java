package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CPS_FILE_DOWN")
public class CommissionFileDown implements BetterjrEntity {

    
    /**
     * 
     */
    private static final long serialVersionUID = -4607002834048195924L;

    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;
    
  //下载id
    @Column(name = "L_FILE_ID",  columnDefinition="INTEGER" )
    private Long fileId;
    
  //下载batchNo
    @Column(name = "N_BATCHNO",  columnDefinition="DECIMAL" )
    private Long batchNo;
    
  //文件导入日期
    @Column(name = "D_IMPORT_DATE",  columnDefinition="VARCHAR" )
    private String importDate;
    
  //文件导入时间
    @Column(name = "T_IMPORT_TIME",  columnDefinition="VARCHAR" )
    private String importTime;

    //企业id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    private Long custNo;

    //当前企业名称
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    private String custName;

    //操作机构
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    private String operOrg;
    
    
    //当前文件总金额
    @Column(name = "F_BALANCE",  columnDefinition="DECIMAL" )
    private BigDecimal blance;

    //当前解析文件总的行数
    @Column(name = "N_AMOUNT",  columnDefinition="INTEGER" )
    private Integer amount;
    
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    private String regDate;
    
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;

    public CommissionFileDown() {
        super();
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public Long getFileId() {
        return this.fileId;
    }

    public void setFileId(Long anFileId) {
        this.fileId = anFileId;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    public String getImportDate() {
        return this.importDate;
    }

    public void setImportDate(String anImportDate) {
        this.importDate = anImportDate;
    }

    public String getImportTime() {
        return this.importTime;
    }

    public void setImportTime(String anImportTime) {
        this.importTime = anImportTime;
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

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public BigDecimal getBlance() {
        return this.blance;
    }

    public void setBlance(BigDecimal anBlance) {
        this.blance = anBlance;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer anAmount) {
        this.amount = anAmount;
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
        return "CommissionFileDown [id=" + this.id + ", fileId=" + this.fileId + ", batchNo=" + this.batchNo + ", importDate=" + this.importDate
                + ", importTime=" + this.importTime + ", custNo=" + this.custNo + ", custName=" + this.custName + ", operOrg=" + this.operOrg
                + ", blance=" + this.blance + ", amount=" + this.amount + ", regDate=" + this.regDate + ", regTime=" + this.regTime + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.blance == null) ? 0 : this.blance.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.fileId == null) ? 0 : this.fileId.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.importDate == null) ? 0 : this.importDate.hashCode());
        result = prime * result + ((this.importTime == null) ? 0 : this.importTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionFileDown other = (CommissionFileDown) obj;
        if (this.amount == null) {
            if (other.amount != null) return false;
        }
        else if (!this.amount.equals(other.amount)) return false;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.blance == null) {
            if (other.blance != null) return false;
        }
        else if (!this.blance.equals(other.blance)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.fileId == null) {
            if (other.fileId != null) return false;
        }
        else if (!this.fileId.equals(other.fileId)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.importDate == null) {
            if (other.importDate != null) return false;
        }
        else if (!this.importDate.equals(other.importDate)) return false;
        if (this.importTime == null) {
            if (other.importTime != null) return false;
        }
        else if (!this.importTime.equals(other.importTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        return true;
    }


    public void saveAddInit(int anRecordAmount, BigDecimal anBlance, CommissionRecord anRecord) {
        
        this.setAmount(anRecordAmount);
        this.setBlance(anBlance);
        this.setCustName(anRecord.getCustName());
        this.setCustNo(anRecord.getCustNo());
        this.setOperOrg(anRecord.getOperOrg());
        this.setImportDate(anRecord.getImportDate());
        this.setImportTime(anRecord.getImportTime());
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        
    }
    
    

}
