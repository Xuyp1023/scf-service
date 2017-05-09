package com.betterjr.modules.commission.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommissionRecordAuditResult implements Serializable{
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -216061789242915421L;

    //审核的状态 200  成功     300失败
    private Integer code;
    
    private String auditMessage;//审核的信息
    
    private Integer noAuditFile; //未审核的文件记录数
    
    private Integer auditFile;//已审核的文件记录数
    
    private Integer auditRecord;//已审核 的记录数
    
    private BigDecimal auditTotalBalance;//审核的所有金额

    public static CommissionRecordAuditResult ok(Integer auditFile,Integer auditRecord,BigDecimal auditTotalBalance){
        
        CommissionRecordAuditResult result=new CommissionRecordAuditResult();
        result.code=200;
        result.auditMessage="审核成功";
        result.auditFile=auditFile;
        result.auditRecord=auditRecord;
        result.auditTotalBalance=auditTotalBalance;
        return result;
    }
    
    public static CommissionRecordAuditResult fail (Integer noAuditFile){
        
        CommissionRecordAuditResult result=new CommissionRecordAuditResult();
        result.code=300;
        result.auditMessage="审核失败";
        result.noAuditFile=noAuditFile;
        return result;
    }
    public static CommissionRecordAuditResult fail (String message){
        
        CommissionRecordAuditResult result=new CommissionRecordAuditResult();
        result.code=300;
        result.auditMessage=message;
        result.noAuditFile=0;
        return result;
    }
    
    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer anCode) {
        this.code = anCode;
    }

    public String getAuditMessage() {
        return this.auditMessage;
    }

    public void setAuditMessage(String anAuditMessage) {
        this.auditMessage = anAuditMessage;
    }

    public Integer getNoAuditFile() {
        return this.noAuditFile;
    }

    public void setNoAuditFile(Integer anNoAuditFile) {
        this.noAuditFile = anNoAuditFile;
    }

    public Integer getAuditFile() {
        return this.auditFile;
    }

    public void setAuditFile(Integer anAuditFile) {
        this.auditFile = anAuditFile;
    }

    public Integer getAuditRecord() {
        return this.auditRecord;
    }

    public void setAuditRecord(Integer anAuditRecord) {
        this.auditRecord = anAuditRecord;
    }

    public BigDecimal getAuditTotalBalance() {
        return this.auditTotalBalance;
    }

    public void setAuditTotalBalance(BigDecimal anAuditTotalBalance) {
        this.auditTotalBalance = anAuditTotalBalance;
    }

    @Override
    public String toString() {
        return "CommissionRecordAuditResult [code=" + this.code + ", auditMessage=" + this.auditMessage + ", noAuditFile=" + this.noAuditFile
                + ", auditFile=" + this.auditFile + ", auditRecord=" + this.auditRecord + ", auditTotalBalance=" + this.auditTotalBalance + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.auditFile == null) ? 0 : this.auditFile.hashCode());
        result = prime * result + ((this.auditMessage == null) ? 0 : this.auditMessage.hashCode());
        result = prime * result + ((this.auditRecord == null) ? 0 : this.auditRecord.hashCode());
        result = prime * result + ((this.auditTotalBalance == null) ? 0 : this.auditTotalBalance.hashCode());
        result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
        result = prime * result + ((this.noAuditFile == null) ? 0 : this.noAuditFile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionRecordAuditResult other = (CommissionRecordAuditResult) obj;
        if (this.auditFile == null) {
            if (other.auditFile != null) return false;
        }
        else if (!this.auditFile.equals(other.auditFile)) return false;
        if (this.auditMessage == null) {
            if (other.auditMessage != null) return false;
        }
        else if (!this.auditMessage.equals(other.auditMessage)) return false;
        if (this.auditRecord == null) {
            if (other.auditRecord != null) return false;
        }
        else if (!this.auditRecord.equals(other.auditRecord)) return false;
        if (this.auditTotalBalance == null) {
            if (other.auditTotalBalance != null) return false;
        }
        else if (!this.auditTotalBalance.equals(other.auditTotalBalance)) return false;
        if (this.code == null) {
            if (other.code != null) return false;
        }
        else if (!this.code.equals(other.code)) return false;
        if (this.noAuditFile == null) {
            if (other.noAuditFile != null) return false;
        }
        else if (!this.noAuditFile.equals(other.noAuditFile)) return false;
        return true;
    }
    

}
