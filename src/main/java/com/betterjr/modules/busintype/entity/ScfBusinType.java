package com.betterjr.modules.busintype.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_busintype")
public class ScfBusinType implements BetterjrEntity{
    
    
        /**
         * 
         */
        private static final long serialVersionUID = -636117315262619493L;
    
        /**
         * 流水号
         */
        @Id
        @Column(name = "ID",  columnDefinition="INTEGER" )
        @MetaData( value="流水号", comments = "流水号")
        private Long id;
        
        /**
         * 创建日期
         */
        @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
        @OrderBy("ASC")
        @MetaData( value="创建日期", comments = "创建日期")
        @JsonSerialize(using = CustDateJsonSerializer.class)
        private String regDate;
        
        /**
         * 创建时间
         */
        @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
        @MetaData( value="创建日期", comments = "创建日期")
        private String regTime;
        
        /**
         * 业务状态 0 不可用  1 生效
         */
        @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
        @MetaData( value="业务状态 0 不可用  1 生效", comments = "业务状态 0 不可用  1 生效")
        private String businStatus;
        
        /**
        * 注册操作员编码
        */
       @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
       @MetaData( value="注册操作员编码", comments = "注册操作员编码")
       private Long regOperId ;
    
       /**
        * 注册操作员名字
        */
       @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
       @MetaData( value="注册操作员名字", comments = "注册操作员名字")
       private String regOperName;
       
       /**
        * 业务类型名称
        */
       @Column(name = "C_BUSIN_TYPE_NAME",  columnDefinition="VARCHAR" )
       @MetaData( value="业务类型名称", comments = "业务类型名称")
       private String businTypeName;
       
       
       /**
        * 授信标识
        */
       @Column(name = "C_CREDIT_FLAG",  columnDefinition="VARCHAR" )
       @MetaData( value="授信标识", comments = "授信标识")
       private String creditFlag;
       
       
       /**
        * 备注
        */
       @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
       @MetaData( value="备注", comments = "备注")
       private String description;


        public Long getId() {
            return this.id;
        }
        
        
        public void setId(Long anId) {
            this.id = anId;
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
        
        
        public String getBusinStatus() {
            return this.businStatus;
        }
        
        
        public void setBusinStatus(String anBusinStatus) {
            this.businStatus = anBusinStatus;
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
        
        
        public String getBusinTypeName() {
            return this.businTypeName;
        }
        
        
        public void setBusinTypeName(String anBusinTypeName) {
            this.businTypeName = anBusinTypeName;
        }
        
        
        public String getCreditFlag() {
            return this.creditFlag;
        }
        
        
        public void setCreditFlag(String anCreditFlag) {
            this.creditFlag = anCreditFlag;
        }
        
        
        public String getDescription() {
            return this.description;
        }
        
        
        public void setDescription(String anDescription) {
            this.description = anDescription;
        }
        
        
        @Override
        public String toString() {
            return "ScfBusinType [id=" + this.id + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", businStatus=" + this.businStatus
                    + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName + ", businTypeName=" + this.businTypeName + ", creditFlag="
                    + this.creditFlag + ", description=" + this.description + "]";
        }
        
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
            result = prime * result + ((this.businTypeName == null) ? 0 : this.businTypeName.hashCode());
            result = prime * result + ((this.creditFlag == null) ? 0 : this.creditFlag.hashCode());
            result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
            result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
            result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
            result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
            result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
            result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
            return result;
        }
        
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ScfBusinType other = (ScfBusinType) obj;
            if (this.businStatus == null) {
                if (other.businStatus != null) return false;
            }
            else if (!this.businStatus.equals(other.businStatus)) return false;
            if (this.businTypeName == null) {
                if (other.businTypeName != null) return false;
            }
            else if (!this.businTypeName.equals(other.businTypeName)) return false;
            if (this.creditFlag == null) {
                if (other.creditFlag != null) return false;
            }
            else if (!this.creditFlag.equals(other.creditFlag)) return false;
            if (this.description == null) {
                if (other.description != null) return false;
            }
            else if (!this.description.equals(other.description)) return false;
            if (this.id == null) {
                if (other.id != null) return false;
            }
            else if (!this.id.equals(other.id)) return false;
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
            return true;
        }
           
   

}
