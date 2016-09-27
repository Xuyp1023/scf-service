package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_DEALSTATUS")
public class FaceDealStatus implements BetterjrEntity {
    /**
     * 路径代码
     */
    @Id
    @Column(name = "C_PATHCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "路径代码", comments = "路径代码")
    private String pathCode;

    /**
     * 分中心
     */
    @Column(name = "C_PAYCENTER", columnDefinition = "VARCHAR")
    @MetaData(value = "分中心", comments = "分中心")
    private String payCenterNo;

    /**
     * 接口
     */
    @Id
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 文件组代码
     */
    @Column(name = "C_GROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "文件组代码", comments = "文件组代码")
    private String groupNo;

    /**
     * 文件格式类型
     */
    @Column(name = "C_FMTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件格式类型", comments = "文件格式类型")
    private String fmtType;

    /**
     * 文件类型
     */
    @Column(name = "C_FILETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件类型", comments = "文件类型")
    private String fileType;

    /**
     * 处理状态
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "处理状态", comments = "处理状态")
    private String status;

    /**
     * 文件名称
     */
    @Column(name = "C_FILENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "文件名称", comments = "文件名称")
    private String fileName;

    /**
     * 记录数据
     */
    @Column(name = "N_COUNT", columnDefinition = "INTEGER")
    @MetaData(value = "记录数据", comments = "记录数据")
    private Integer count;

    /**
     * 处理记录数
     */
    @Column(name = "N_HANDLECOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "处理记录数", comments = "处理记录数")
    private Integer handleCount;

    private static final long serialVersionUID = 1439797394188L;

    public String getPathCode() {
        return pathCode;
    }

    public void setPathCode(String pathCode) {
        this.pathCode = pathCode == null ? null : pathCode.trim();
    }

    public String getPayCenterNo() {
        return payCenterNo;
    }

    public void setPayCenterNo(String payCenterNo) {
        this.payCenterNo = payCenterNo == null ? null : payCenterNo.trim();
    }

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo == null ? null : groupNo.trim();
    }

    public String getFmtType() {
        return fmtType;
    }

    public void setFmtType(String fmtType) {
        this.fmtType = fmtType == null ? null : fmtType.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getHandleCount() {
        return handleCount;
    }

    public void setHandleCount(Integer handleCount) {
        this.handleCount = handleCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pathCode=").append(pathCode);
        sb.append(", payCenterNo=").append(payCenterNo);
        sb.append(", faceNo=").append(faceNo);
        sb.append(", groupNo=").append(groupNo);
        sb.append(", fmtType=").append(fmtType);
        sb.append(", fileType=").append(fileType);
        sb.append(", status=").append(status);
        sb.append(", fileName=").append(fileName);
        sb.append(", count=").append(count);
        sb.append(", handleCount=").append(handleCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        FaceDealStatus other = (FaceDealStatus) that;
        return (this.getPathCode() == null ? other.getPathCode() == null : this.getPathCode().equals(other.getPathCode()))
                && (this.getPayCenterNo() == null ? other.getPayCenterNo() == null : this.getPayCenterNo().equals(other.getPayCenterNo()))
                && (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getGroupNo() == null ? other.getGroupNo() == null : this.getGroupNo().equals(other.getGroupNo()))
                && (this.getFmtType() == null ? other.getFmtType() == null : this.getFmtType().equals(other.getFmtType()))
                && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
                && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()))
                && (this.getHandleCount() == null ? other.getHandleCount() == null : this.getHandleCount().equals(other.getHandleCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPathCode() == null) ? 0 : getPathCode().hashCode());
        result = prime * result + ((getPayCenterNo() == null) ? 0 : getPayCenterNo().hashCode());
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getGroupNo() == null) ? 0 : getGroupNo().hashCode());
        result = prime * result + ((getFmtType() == null) ? 0 : getFmtType().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        result = prime * result + ((getHandleCount() == null) ? 0 : getHandleCount().hashCode());
        return result;
    }

    public FaceDealStatus() {

    }
}