package com.betterjr.modules.joblog.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_joblog")
public class ScfJoblog implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 4547028333602136374L;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 日志记录信息
     */
    @Column(name = "C_SHOW_MESSAGE", columnDefinition = "VARCHAR")
    @MetaData(value = "日志记录信息", comments = "日志记录信息")
    private String showMessage;

    /**
     * 业务状态 0 设置失败 1 设置成功
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务状态 0 设置失败       1 设置成功", comments = "业务状态 0 设置失败       1 设置成功")
    private String businStatus;

    /**
     * 多条排序序号
     */
    @Column(name = "C_ORDER_BY", columnDefinition = "VARCHAR")
    @MetaData(value = "多条排序序号", comments = "多条排序序号")
    private String orderBy;

    /**
     * 数据类型：1订单2票据3应收账款4发票5贸易合同  6应收账款申请
     */
    @Column(name = "C_DATAINFO_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "数据类型：1订单2票据3应收账款4发票5贸易合同", comments = "数据类型：1订单2票据3应收账款4发票5贸易合同")
    private String dataType;

    /**
     * 业务模型 1:过期日志
     */
    @Column(name = "C_BUSIN_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "业务模型  1:过期日志", comments = "业务模型  1:过期日志")
    private String businType;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

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

    public String getShowMessage() {
        return this.showMessage;
    }

    public void setShowMessage(String anShowMessage) {
        this.showMessage = anShowMessage;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String anOrderBy) {
        this.orderBy = anOrderBy;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String anDataType) {
        this.dataType = anDataType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getBusinType() {
        return this.businType;
    }

    public void setBusinType(String anBusinType) {
        this.businType = anBusinType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.dataType == null) ? 0 : this.dataType.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.orderBy == null) ? 0 : this.orderBy.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.showMessage == null) ? 0 : this.showMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfJoblog other = (ScfJoblog) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.dataType == null) {
            if (other.dataType != null) return false;
        }
        else if (!this.dataType.equals(other.dataType)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.orderBy == null) {
            if (other.orderBy != null) return false;
        }
        else if (!this.orderBy.equals(other.orderBy)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.showMessage == null) {
            if (other.showMessage != null) return false;
        }
        else if (!this.showMessage.equals(other.showMessage)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfJoblog [regDate=" + this.regDate + ", regTime=" + this.regTime + ", showMessage=" + this.showMessage + ", businStatus="
                + this.businStatus + ", orderBy=" + this.orderBy + ", dataType=" + this.dataType + ", businType=" + this.businType + ", id=" + this.id
                + "]";
    }

    public void initAddValue() {

        this.setId(SerialGenerator.getLongValue("ScfJoblog.id"));
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());

    }

}
