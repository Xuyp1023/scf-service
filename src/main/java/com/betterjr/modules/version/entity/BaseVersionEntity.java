package com.betterjr.modules.version.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
public class BaseVersionEntity implements BetterjrEntity{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;
    
    /**
     * 特性编码
     */
    @Column(name = "C_Ref_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="特性编码", comments = "单特性编码")
    @OrderBy("DESC")
    private String refNo;
    
    /**
     * 版本信息
     */
    @Column(name = "N_VERSION",  columnDefinition="VARCHAR" )
    @MetaData( value="版本", comments = "版本")
    @OrderBy("DESC")
    private String version;
    
    /**
     * 是否是最新编码
     */
    @Column(name = "C_IS_LATEST",  columnDefinition="VARCHAR" )
    @MetaData( value="最新", comments = "最新")
    private String isLatest;
    
    /**
     * 0 未核准 1：核准  2：已使用 3：转让 4废止 5 过期
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="0 未核准 1：核准  2：已使用 3：转让 4废止 5 过期", comments = "0 未核准 1：核准  2：已使用 3：转让 4废止 5 过期")
    private String businStatus;
    
    /**
     * 0 未锁定状态 1 锁定状态
     */
    @Column(name = "C_LOCKED_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="0 未锁定状态 1 锁定状态", comments = "0 未锁定状态 1 锁定状态")
    private String lockedStatus;
    
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
    
    
    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String anRefNo) {
        this.refNo = anRefNo;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    public String getIsLatest() {
        return this.isLatest;
    }

    public void setIsLatest(String anIsLatest) {
        this.isLatest = anIsLatest;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }


    public String getLockedStatus() {
        return this.lockedStatus;
    }

    public void setLockedStatus(String anLockedStatus) {
        this.lockedStatus = anLockedStatus==null ? null : anLockedStatus.trim();
    }

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }
    
    
}
