package com.betterjr.modules.version.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.version.constant.VersionConstantCollentions;

@Access(AccessType.FIELD)
@Entity
public class BaseVersionEntity extends BetterjrBaseEntity{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * 0：草稿 1：确认 2废止
     */
    @Column(name = "C_DOC_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="0：草稿 1：确认 2废止", comments = "0：草稿 1：确认 2废止")
    private String docStatus;

    /**
     * 修改操作员ID号
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "修改操作员ID号", comments = "修改操作员ID号")
    private Long modiOperId;
    
    /**
     * 审核操作员ID号
     */
    @Column(name = "L_AUDIT_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "审核操作员ID号", comments = "审核操作员ID号")
    
    private Long auditOperId;
    /**
     * 审核操作员名字
     */
    @Column(name = "C_AUDIT_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "审核操作员名字", comments = "审核操作员名字")
    private String auditOperName;
    
    /**
     * 审核日期
     */
    @Column(name = "D_AUDIT_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "审核日期", comments = "审核日期")
    @OrderBy("DESC")
    private String auditData;
    
    /**
     * 审核时间
     */
    @Column(name = "T_AUDIT_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "审核时间", comments = "审核时间")
    private String auditTime;
    
    /**
     * 0: 未生效单据自动任务过期来源  1：已生效的单据自动任务过期来源
     */
    @Column(name = "C_EXPIRE_FLAG_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "0: 未生效单据自动任务过期来源  1：已生效的单据自动任务过期来源", comments = "0: 未生效单据自动任务过期来源  1：已生效的单据自动任务过期来源")
    private String expireFlagStatus;
    
    
   

    public String getDocStatus() {
        return this.docStatus;
    }

    public void setDocStatus(String anDocStatus) {
        this.docStatus = anDocStatus==null ? null : anDocStatus.trim();
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public Long getAuditOperId() {
        return this.auditOperId;
    }

    public void setAuditOperId(Long anAuditOperId) {
        this.auditOperId = anAuditOperId;
    }

    public String getAuditOperName() {
        return this.auditOperName;
    }

    public void setAuditOperName(String anAuditOperName) {
        this.auditOperName = anAuditOperName;
    }

    public String getAuditData() {
        return this.auditData;
    }

    public void setAuditData(String anAuditData) {
        this.auditData = anAuditData;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String anAuditTime) {
        this.auditTime = anAuditTime;
    }

    
    public String getExpireFlagStatus() {
        return this.expireFlagStatus;
    }

    public void setExpireFlagStatus(String anExpireFlagStatus) {
        this.expireFlagStatus = anExpireFlagStatus;
    }

    public void checkFinanceStatus(){
      
        checkStatus(this.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_ANNUL, true, "当前单据已经废止,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EXPIRE, true, "当前单据已经过期,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE, true, "当前单据还未生效,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_TRANSFER, true, "当前单据已经转让,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_USED, true, "当前单据已经进行融资,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getDocStatus(), VersionConstantCollentions.DOC_STATUS_ANNUL, true, "当前单据已经废止,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "当前单据已经进行融资,无法进行融资,凭证编号为："+this.getRefNo());
        checkStatus(this.getIsLatest(), VersionConstantCollentions.IS_NOT_LATEST, true, "当前单据已经进行修改,无法对旧版本资产进行融资,凭证编号为："+this.getRefNo());
        
    }
    
    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            
            throw new BytterTradeException(40001, anMessage);
        }
    }
}
