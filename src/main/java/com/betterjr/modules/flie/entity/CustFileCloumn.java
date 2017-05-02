package com.betterjr.modules.flie.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_file_ordercloumn")
public class CustFileCloumn implements BetterjrEntity {

    
    /**
     * 
     */
    private static final long serialVersionUID = 133878753078982988L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号")
    private Long id;
    
    /**
     * 解析的文件类型1订单 2票据  3应收账款 4发票 5 合同
     */
    @Column(name = "C_INFO_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="解析的文件类型1订单 2票据  3应收账款 4发票 5 合同", comments = "解析的文件类型1订单 2票据  3应收账款 4发票 5 合同")
    private String infoType;
    
    /**
     * 当前列的类型0 字符串   1 数字
     */
    @Column(name = "C_CLOUMN_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="当前列的类型0 字符串   1 数字", comments = "当前列的类型0 字符串   1 数字")
    private String cloumn_type;
    
    /**
     * 标记上传下载   上传1  下载2
     */
    @Column(name = "C_UP_FLAG",  columnDefinition="VARCHAR" )
    @MetaData( value="标记上传下载   上传1  下载2", comments = "标记上传下载   上传1  下载2")
    private String upFlag;
    
    /**
     * 当前记录是否可用      0 可用  1过期
     */
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value=" 当前记录是否可用      0 可用  1过期", comments = " 当前记录是否可用      0 可用  1过期")
    private String businStatus;
    
    /**
     * 文件列排序序号
     */
    @Column(name = "L_CLOUMN_ORDER", columnDefinition = "INTEGER")
    @MetaData(value = "文件列排序序号", comments = "文件列排序序号")
    private Integer cloumnOrder;
    
    /**
     * 文件列对应的属性
     */
    @Column(name = "C_CLOUMN_PROPERTIES",  columnDefinition="VARCHAR" )
    @MetaData( value=" 文件列对应的属性", comments = "文件列对应的属性")
    private String cloumnProperties;
    
    /**
     * 文件列对应的属性名称
     */
    @Column(name = "C_CLOUMN_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value=" 文件列对应的属性", comments = "文件列对应的属性")
    private String cloumnName;
    
    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regTime;
    
    /**
     * 1 必须  0 不必须
     */
    @Column(name = "L_IS_MUST", columnDefinition = "INTEGER")
    @MetaData(value = "1 必须  0 不必须", comments = "1 必须  0 不必须")
    private Integer isMust;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getCloumn_type() {
        return this.cloumn_type;
    }

    public void setCloumn_type(String anCloumn_type) {
        this.cloumn_type = anCloumn_type;
    }

    public String getInfoType() {
        return this.infoType;
    }

    
    public String getCloumnName() {
        return this.cloumnName;
    }

    public void setCloumnName(String anCloumnName) {
        this.cloumnName = anCloumnName;
    }

    public void setInfoType(String anInfoType) {
        this.infoType = anInfoType;
    }

    public String getUpFlag() {
        return this.upFlag;
    }

    public void setUpFlag(String anUpFlag) {
        this.upFlag = anUpFlag;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public Integer getCloumnOrder() {
        return this.cloumnOrder;
    }

    public void setCloumnOrder(Integer anCloumnOrder) {
        this.cloumnOrder = anCloumnOrder;
    }

    public String getCloumnProperties() {
        return this.cloumnProperties;
    }

    public void setCloumnProperties(String anCloumnProperties) {
        this.cloumnProperties = anCloumnProperties;
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

    public Integer getIsMust() {
        return this.isMust;
    }

    public void setIsMust(Integer anIsMust) {
        this.isMust = anIsMust;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.cloumnOrder == null) ? 0 : this.cloumnOrder.hashCode());
        result = prime * result + ((this.cloumnProperties == null) ? 0 : this.cloumnProperties.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.isMust == null) ? 0 : this.isMust.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.upFlag == null) ? 0 : this.upFlag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CustFileCloumn other = (CustFileCloumn) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.cloumnOrder == null) {
            if (other.cloumnOrder != null) return false;
        }
        else if (!this.cloumnOrder.equals(other.cloumnOrder)) return false;
        if (this.cloumnProperties == null) {
            if (other.cloumnProperties != null) return false;
        }
        else if (!this.cloumnProperties.equals(other.cloumnProperties)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        }
        else if (!this.infoType.equals(other.infoType)) return false;
        if (this.isMust == null) {
            if (other.isMust != null) return false;
        }
        else if (!this.isMust.equals(other.isMust)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.upFlag == null) {
            if (other.upFlag != null) return false;
        }
        else if (!this.upFlag.equals(other.upFlag)) return false;
        return true;
    }


    @Override
    public String toString() {
        return "CustFileCloumn [id=" + this.id + ", infoType=" + this.infoType + ", upFlag=" + this.upFlag + ", businStatus=" + this.businStatus
                + ", cloumnOrder=" + this.cloumnOrder + ", cloumnProperties=" + this.cloumnProperties + ", cloumnName=" + this.cloumnName
                + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", isMust=" + this.isMust + "]";
    }

    public CustFileCloumn() {
        super();
    }
    
    
}
