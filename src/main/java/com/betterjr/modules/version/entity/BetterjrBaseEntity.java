package com.betterjr.modules.version.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
public class BetterjrBaseEntity implements BetterjrEntity{

    /**
     * 
     */
    private static final long serialVersionUID = 4834094556743191421L;
    
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
    private String refNo;
    
    /**
     * 版本信息
     */
    @Column(name = "N_VERSION",  columnDefinition="VARCHAR" )
    @MetaData( value="版本", comments = "版本")
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

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
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getLockedStatus() {
        return this.lockedStatus;
    }

    public void setLockedStatus(String anLockedStatus) {
        this.lockedStatus = anLockedStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.isLatest == null) ? 0 : this.isLatest.hashCode());
        result = prime * result + ((this.lockedStatus == null) ? 0 : this.lockedStatus.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BetterjrBaseEntity other = (BetterjrBaseEntity) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.isLatest == null) {
            if (other.isLatest != null) return false;
        }
        else if (!this.isLatest.equals(other.isLatest)) return false;
        if (this.lockedStatus == null) {
            if (other.lockedStatus != null) return false;
        }
        else if (!this.lockedStatus.equals(other.lockedStatus)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        }
        else if (!this.refNo.equals(other.refNo)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        }
        else if (!this.version.equals(other.version)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "BetterjrBaseEntity [id=" + this.id + ", refNo=" + this.refNo + ", version=" + this.version + ", isLatest=" + this.isLatest
                + ", businStatus=" + this.businStatus + ", lockedStatus=" + this.lockedStatus + "]";
    }
    
    

}
