package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_INFO")
public class FaceInfo implements BetterjrEntity {
    /**
     * 接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 文件组代码
     */
    @Column(name = "C_GROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "文件组代码", comments = "文件组代码")
    private String faceGroup;

    /**
     * 文件组描述
     */
    @Column(name = "C_GROUPDSC", columnDefinition = "VARCHAR")
    @MetaData(value = "文件组描述", comments = "文件组描述")
    private String groupDesc;

    /**
     * 文件格式类型
     */
    @Column(name = "C_FMTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件格式类型", comments = "文件格式类型")
    private String formatType;

    /**
     * 文件格式类型描述
     */
    @Column(name = "C_FMTTYPEDSC", columnDefinition = "VARCHAR")
    @MetaData(value = "文件格式类型描述", comments = "文件格式类型描述")
    private String formatDesc;

    /**
     * 文件类型
     */
    @Column(name = "C_FILETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件类型", comments = "文件类型")
    private String fileType;

    /**
     * 文件类型描述
     */
    @Column(name = "C_FILETYPEDSC", columnDefinition = "VARCHAR")
    @MetaData(value = "文件类型描述", comments = "文件类型描述")
    private String fileDesc;

    /**
     * 文件名称
     */
    @Column(name = "C_FILENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "文件名称", comments = "文件名称")
    private String fileName;

    /**
     * 指定的IO表
     */
    @Column(name = "C_TABLENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "指定的IO表", comments = "指定的IO表")
    private String tableName;

    /**
     * 入出SQL条件
     */
    @Column(name = "C_SQLWHERE", columnDefinition = "VARCHAR")
    @MetaData(value = "入出SQL条件", comments = "入出SQL条件")
    private String sqlWhere;

    /**
     * 数据项读写方向0转出，1转入，2转入转出相互映
     */
    @Column(name = "C_IO", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项读写方向0转出", comments = "数据项读写方向0转出，1转入，2转入转出相互映")
    private String direction;

    /**
     * 文件处理日期变量
     */
    @Column(name = "C_DEFDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件处理日期变量", comments = "文件处理日期变量")
    private String defValue;

    /**
     * 导入数据涉及更新的表
     */
    @Column(name = "C_TABLEUPDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "导入数据涉及更新的表", comments = "导入数据涉及更新的表")
    private String tableUpdate;

    /**
     * 异常状态模板；即接口返回码字典
     */
    @Column(name = "C_ERR_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "异常状态模板", comments = "异常状态模板；即接口返回码字典")
    private String errCodeMode;

    /**
     * 临时数据预处理类
     */
    @Column(name = "C_PREPARE", columnDefinition = "VARCHAR")
    @MetaData(value = "临时数据预处理类", comments = "临时数据预处理类")
    private String workPrepare;

    /**
     * 数据项顺序号
     */
    @Column(name = "N_ORDER", columnDefinition = "INTEGER")
    @MetaData(value = "数据项顺序号", comments = "数据项顺序号")
    @OrderBy("ASC")
    private Integer fieldOrder;

    private static final long serialVersionUID = 1440666748879L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFaceGroup() {
        return faceGroup;
    }

    public void setFaceGroup(String faceGroup) {
        this.faceGroup = faceGroup == null ? null : faceGroup.trim();
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc == null ? null : groupDesc.trim();
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType == null ? null : formatType.trim();
    }

    public String getFormatDesc() {
        return formatDesc;
    }

    public void setFormatDesc(String formatDesc) {
        this.formatDesc = formatDesc == null ? null : formatDesc.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc == null ? null : fileDesc.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getSqlWhere() {
        return sqlWhere;
    }

    public void setSqlWhere(String sqlWhere) {
        this.sqlWhere = sqlWhere == null ? null : sqlWhere.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue == null ? null : defValue.trim();
    }

    public String getTableUpdate() {
        return tableUpdate;
    }

    public void setTableUpdate(String tableUpdate) {
        this.tableUpdate = tableUpdate == null ? null : tableUpdate.trim();
    }

    public String getErrCodeMode() {
        return errCodeMode;
    }

    public void setErrCodeMode(String errCodeMode) {
        this.errCodeMode = errCodeMode == null ? null : errCodeMode.trim();
    }

    public String getWorkPrepare() {
        return this.workPrepare;
    }

    public void setWorkPrepare(String anWorkPrepare) {
        this.workPrepare = anWorkPrepare;
    }

    public Integer getFieldOrder() {
        return this.fieldOrder;
    }

    public void setFieldOrder(Integer anFieldOrder) {
        this.fieldOrder = anFieldOrder;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", faceGroup=").append(faceGroup);
        sb.append(", groupDesc=").append(groupDesc);
        sb.append(", formatType=").append(formatType);
        sb.append(", formatDesc=").append(formatDesc);
        sb.append(", fileType=").append(fileType);
        sb.append(", fileDesc=").append(fileDesc);
        sb.append(", fileName=").append(fileName);
        sb.append(", tableName=").append(tableName);
        sb.append(", sqlWhere=").append(sqlWhere);
        sb.append(", direction=").append(direction);
        sb.append(", defValue=").append(defValue);
        sb.append(", tableUpdate=").append(tableUpdate);
        sb.append(", errCodeMode=").append(errCodeMode);
        sb.append(", workPrepare=").append(workPrepare);
        sb.append(", fieldOrder=").append(fieldOrder);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FaceInfo other = (FaceInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getFaceGroup() == null ? other.getFaceGroup() == null
                        : this.getFaceGroup().equals(other.getFaceGroup()))
                && (this.getGroupDesc() == null ? other.getGroupDesc() == null
                        : this.getGroupDesc().equals(other.getGroupDesc()))
                && (this.getFormatType() == null ? other.getFormatType() == null
                        : this.getFormatType().equals(other.getFormatType()))
                && (this.getFormatDesc() == null ? other.getFormatDesc() == null
                        : this.getFormatDesc().equals(other.getFormatDesc()))
                && (this.getFileType() == null ? other.getFileType() == null
                        : this.getFileType().equals(other.getFileType()))
                && (this.getFileDesc() == null ? other.getFileDesc() == null
                        : this.getFileDesc().equals(other.getFileDesc()))
                && (this.getFileName() == null ? other.getFileName() == null
                        : this.getFileName().equals(other.getFileName()))
                && (this.getTableName() == null ? other.getTableName() == null
                        : this.getTableName().equals(other.getTableName()))
                && (this.getSqlWhere() == null ? other.getSqlWhere() == null
                        : this.getSqlWhere().equals(other.getSqlWhere()))
                && (this.getDirection() == null ? other.getDirection() == null
                        : this.getDirection().equals(other.getDirection()))
                && (this.getDefValue() == null ? other.getDefValue() == null
                        : this.getDefValue().equals(other.getDefValue()))
                && (this.getTableUpdate() == null ? other.getTableUpdate() == null
                        : this.getTableUpdate().equals(other.getTableUpdate()))
                && (this.getErrCodeMode() == null ? other.getErrCodeMode() == null
                        : this.getErrCodeMode().equals(other.getErrCodeMode()))
                && (this.getFieldOrder() == null ? other.getFieldOrder() == null
                        : this.getFieldOrder().equals(other.getFieldOrder()))
                && (this.getWorkPrepare() == null ? other.getWorkPrepare() == null
                        : this.getWorkPrepare().equals(other.getWorkPrepare()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFaceGroup() == null) ? 0 : getFaceGroup().hashCode());
        result = prime * result + ((getGroupDesc() == null) ? 0 : getGroupDesc().hashCode());
        result = prime * result + ((getFormatType() == null) ? 0 : getFormatType().hashCode());
        result = prime * result + ((getFormatDesc() == null) ? 0 : getFormatDesc().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getFileDesc() == null) ? 0 : getFileDesc().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getSqlWhere() == null) ? 0 : getSqlWhere().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getDefValue() == null) ? 0 : getDefValue().hashCode());
        result = prime * result + ((getTableUpdate() == null) ? 0 : getTableUpdate().hashCode());
        result = prime * result + ((getErrCodeMode() == null) ? 0 : getErrCodeMode().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getWorkPrepare() == null) ? 0 : getWorkPrepare().hashCode());
        return result;
    }
}