package com.betterjr.modules.remote.entity;

import java.io.File;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.helper.RemoteProxyFactory;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_FILEUPLOAD")
public class CustFileUpload implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 交易账户
     */
    @Column(name = "C_TRADEACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "交易账户", comments = "交易账户")
    private String tradeAccount;

    /**
     * 销售人代码
     */
    @Column(name = "C_AGENCYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "销售人代码", comments = "销售人代码")
    private String agencyNo;

    /**
     * 申请单号
     */
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请单号", comments = "申请单号")
    private String requestNo;

    /**
     * 基金公司申请单号
     */
    @Column(name = "C_SALE_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "基金公司申请单号", comments = "基金公司申请单号")
    private String saleRequestNo;

    /**
     * 业务代码
     */
    @Column(name = "C_BUSINFLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "业务代码", comments = "业务代码")
    private String businFlag;

    /**
     * 文件名称
     */
    @Column(name = "C_FILENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "文件名称", comments = "文件名称")
    private String fileName;

    /**
     * 文件类型
     */
    @Column(name = "C_FILETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件类型", comments = "文件类型")
    private String fileType;

    /**
     * 保存文件路径，上传到对方的路径
     */
    @Column(name = "C_FILEPATH", columnDefinition = "VARCHAR")
    @MetaData(value = "保存文件路径", comments = "保存文件路径，上传到对方的路径")
    private String filePath;

    /**
     * 文件大小
     */
    @Column(name = "N_LENGTH", columnDefinition = "INTEGER")
    @MetaData(value = "文件大小", comments = "文件大小")
    private Long fileLength;

    /**
     * 处理状态
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "处理状态", comments = "处理状态")
    private String status;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 注册时间
     */
    @Column(name = "D_REGTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册时间", comments = "注册时间")
    private String regTime;

    /**
     * 失败次数
     */
    @Column(name = "N_FAILS", columnDefinition = "INTEGER")
    @MetaData(value = "失败次数", comments = "失败次数")
    private Integer failTimes;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 
     */
    @Column(name = "C_FILEINFOTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件信息类型", comments = "文件信息类")
    private String fileInfoType;

    /**
     * 远程发送文件路径
     */
    @Column(name = "C_REMOTE_SEND", columnDefinition = "VARCHAR")
    @MetaData(value = "远程发送文件路径", comments = "文件需要发送的路径")
    private String remoteSendPath;

    private static final long serialVersionUID = 1440653438750L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount == null ? null : tradeAccount.trim();
    }

    public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo == null ? null : agencyNo.trim();
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public String getSaleRequestNo() {
        return saleRequestNo;
    }

    public void setSaleRequestNo(String saleRequestNo) {
        this.saleRequestNo = saleRequestNo == null ? null : saleRequestNo.trim();
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag == null ? null : businFlag.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getFileInfoType() {
        return this.fileInfoType;
    }

    public void setFileInfoType(String anFileInfoType) {
        this.fileInfoType = anFileInfoType;
    }

    public String getRemoteSendPath() {
        return this.remoteSendPath;
    }

    public void setRemoteSendPath(String anRemoteSendPath) {
        this.remoteSendPath = anRemoteSendPath;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id=").append(id);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", tradeAccount=").append(tradeAccount);
        sb.append(", agencyNo=").append(agencyNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", saleRequestNo=").append(saleRequestNo);
        sb.append(", businFlag=").append(businFlag);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileType=").append(fileType);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileLength=").append(fileLength);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", failTimes=").append(failTimes);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", fileInfoType=").append(fileInfoType);
        sb.append(", remoteSendPath=").append(remoteSendPath);
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
        CustFileUpload other = (CustFileUpload) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getTradeAccount() == null ? other.getTradeAccount() == null : this.getTradeAccount().equals(other.getTradeAccount()))
                && (this.getAgencyNo() == null ? other.getAgencyNo() == null : this.getAgencyNo().equals(other.getAgencyNo()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getSaleRequestNo() == null ? other.getSaleRequestNo() == null : this.getSaleRequestNo().equals(other.getSaleRequestNo()))
                && (this.getBusinFlag() == null ? other.getBusinFlag() == null : this.getBusinFlag().equals(other.getBusinFlag()))
                && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
                && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
                && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
                && (this.getFileLength() == null ? other.getFileLength() == null : this.getFileLength().equals(other.getFileLength()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getFailTimes() == null ? other.getFailTimes() == null : this.getFailTimes().equals(other.getFailTimes()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getFileInfoType() == null ? other.getFileInfoType() == null : this.getFileInfoType().equals(other.getFileInfoType()))
                && (this.getRemoteSendPath() == null ? other.getRemoteSendPath() == null : this.getRemoteSendPath().equals(other.getRemoteSendPath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getTradeAccount() == null) ? 0 : getTradeAccount().hashCode());
        result = prime * result + ((getAgencyNo() == null) ? 0 : getAgencyNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getSaleRequestNo() == null) ? 0 : getSaleRequestNo().hashCode());
        result = prime * result + ((getBusinFlag() == null) ? 0 : getBusinFlag().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getFileLength() == null) ? 0 : getFileLength().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getFailTimes() == null) ? 0 : getFailTimes().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getFileInfoType() == null) ? 0 : getFileInfoType().hashCode());
        result = prime * result + ((getRemoteSendPath() == null) ? 0 : getRemoteSendPath().hashCode());
        return result;
    }

    public void updateStatus(String anStatus) {
        if ("0".equalsIgnoreCase(anStatus)) {
            this.failTimes++;
        }
        this.status = anStatus;
        this.modiDate = BetterDateUtils.getNumDateTime();
    }

    public String findFileName() {
        if (BetterStringUtils.isNotBlank(fileInfoType)) {
            String tmpName = RemoteProxyFactory.findFileName(this.agencyNo, this.fileInfoType);
            return tmpName.concat(".").concat(this.fileType);
        }
        else {
            File tmpFile = new File(this.filePath);
            return tmpFile.getName().concat(".").concat(this.fileType);
        }
    }

    public boolean needUpload() {

        return "0".equals(this.status) && this.failTimes.intValue() < 10;
    }

    public CustFileUpload() {

    }
}