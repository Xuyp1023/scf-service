package com.betterjr.modules.version.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
     * 特性编码
     */
    @Column(name = "Ref_NO",  columnDefinition="VARCHAR" )
    @MetaData( value="特性编码", comments = "单特性编码")
    private String refNo;
    
    /**
     * 版本信息
     */
    @Column(name = "Version",  columnDefinition="VARCHAR" )
    @MetaData( value="版本", comments = "版本")
    private String version;
    
    /**
     * 是否是最新编码
     */
    @Column(name = "Is_Latest",  columnDefinition="VARCHAR" )
    @MetaData( value="版本", comments = "版本")
    private String isLatest;

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

    
}
