package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_PATHINFO")
public class FacePathInfo implements BetterjrEntity {
    /**
     * TA代码
     */
    @Column(name = "C_TANO", columnDefinition = "VARCHAR")
    @MetaData(value = "TA代码", comments = "TA代码")
    private String tano;

    /**
     * 路径代码
     */
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
     * 导入文件路径
     */
    @Column(name = "C_RECVPATH", columnDefinition = "VARCHAR")
    @MetaData(value = "导入文件路径", comments = "导入文件路径")
    private String recvPath;

    /**
     * 导出文件路径
     */
    @Column(name = "C_SENDPATH", columnDefinition = "VARCHAR")
    @MetaData(value = "导出文件路径", comments = "导出文件路径")
    private String sendPath;

    /**
     * 接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口")
    private String faceNo;

    /**
     * 远程接收文件路径；如果配置，文件将从该路径下载到本地导入文件路径
     */
    @Column(name = "C_REMOTE_RECV", columnDefinition = "VARCHAR")
    @MetaData(value = "远程接收文件路径", comments = "远程接收文件路径；如果配置，文件将从该路径下载到本地导入文件路径")
    private String remoteRecvPath;

    /**
     * 远程发送文件路径；如果配置，文件将从本地导出文件路径发送到远端。
     */
    @Column(name = "C_REMOTE_SEND", columnDefinition = "VARCHAR")
    @MetaData(value = "远程发送文件路径", comments = "远程发送文件路径；如果配置，文件将从本地导出文件路径发送到远端。")
    private String remoteSendPath;

    /**
     * 执行的CLASS，如果没有，使用默认
     */
    @Column(name = "C_WORKCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "执行的CLASS", comments = "执行CLASS，如果没有，使用默认")
    private String workClass;

    /**
     * 文件字符集
     */
    @Column(name = "C_CHARSET", columnDefinition = "VARCHAR")
    @MetaData(value = "文件字符集", comments = "文件字符集")
    private String workCharset;

    /**
     * 每个接口添加token，避免外部攻击。
     */
    @Column(name = "C_TOKEN", columnDefinition = "VARCHAR")
    @MetaData(value = "每个接口添加token，避免外部攻击。", comments = "每个接口添加token，避免外部攻击。")
    private String workToken;

    /**
     * 数据压缩类型
     */
    @Column(name = "C_PACK", columnDefinition = "VARCHAR")
    @MetaData(value = "数据压缩类型", comments = "数据压缩类型")
    private String packType;

    private static final long serialVersionUID = 1439797394191L;

    public String getTano() {
        return tano;
    }

    public void setTano(String tano) {
        this.tano = tano == null ? null : tano.trim();
    }

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

    public String getRecvPath() {
        return recvPath;
    }

    public void setRecvPath(String recvPath) {
        this.recvPath = recvPath == null ? null : recvPath.trim();
    }

    public String getSendPath() {
        return sendPath;
    }

    public void setSendPath(String sendPath) {
        this.sendPath = sendPath == null ? null : sendPath.trim();
    }

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getRemoteRecvPath() {
        return this.remoteRecvPath;
    }

    public void setRemoteRecvPath(String anRemoteRecvPath) {
        this.remoteRecvPath = anRemoteRecvPath;
    }

    public String getRemoteSendPath() {
        return this.remoteSendPath;
    }

    public void setRemoteSendPath(String anRemoteSendPath) {
        this.remoteSendPath = anRemoteSendPath;
    }

    public String getWorkClass() {
        return this.workClass;
    }

    public void setWorkClass(String anWorkClass) {
        this.workClass = anWorkClass;
    }

    public String getWorkCharset() {
        return this.workCharset;
    }

    public void setWorkCharset(String anWorkCharset) {
        this.workCharset = anWorkCharset;
    }

    public String getWorkToken() {
        return this.workToken;
    }

    public void setWorkToken(String anWorkToken) {
        this.workToken = anWorkToken;
    }

    public String getPackType() {
        return this.packType;
    }

    public void setPackType(String anPackType) {
        this.packType = anPackType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("remoteRecvPath = ").append(remoteRecvPath);
        sb.append(", tano=").append(tano);
        sb.append(", pathCode=").append(pathCode);
        sb.append(", payCenterNo=").append(payCenterNo);
        sb.append(", recvPath=").append(recvPath);
        sb.append(", sendPath=").append(sendPath);
        sb.append(", faceNo=").append(faceNo);
        sb.append(", remoteSendPath=").append(remoteSendPath);
        sb.append(", workToken=").append(workToken);
        sb.append(", workCharset=").append(workCharset);
        sb.append(", workClass=").append(workClass);
        sb.append(", packType=").append(packType);
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
        FacePathInfo other = (FacePathInfo) that;
        return (this.getTano() == null ? other.getTano() == null : this.getTano().equals(other.getTano()))
                && (this.getPathCode() == null ? other.getPathCode() == null
                        : this.getPathCode().equals(other.getPathCode()))
                && (this.getPayCenterNo() == null ? other.getPayCenterNo() == null
                        : this.getPayCenterNo().equals(other.getPayCenterNo()))
                && (this.getRecvPath() == null ? other.getRecvPath() == null
                        : this.getRecvPath().equals(other.getRecvPath()))
                && (this.getSendPath() == null ? other.getSendPath() == null
                        : this.getSendPath().equals(other.getSendPath()))
                && (this.getRemoteSendPath() == null ? other.getRemoteSendPath() == null
                        : this.getRemoteSendPath().equals(other.getRemoteSendPath()))
                && (this.getRemoteRecvPath() == null ? other.getRemoteRecvPath() == null
                        : this.getRemoteRecvPath().equals(other.getRemoteRecvPath()))
                && (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getWorkClass() == null ? other.getWorkClass() == null
                        : this.getWorkClass().equals(other.getWorkClass()))
                && (this.getWorkCharset() == null ? other.getWorkCharset() == null
                        : this.getWorkCharset().equals(other.getWorkCharset()))
                && (this.getWorkToken() == null ? other.getWorkToken() == null
                        : this.getWorkToken().equals(other.getWorkToken()))
                && (this.getPackType() == null ? other.getPackType() == null
                        : this.getPackType().equals(other.getPackType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTano() == null) ? 0 : getTano().hashCode());
        result = prime * result + ((getPathCode() == null) ? 0 : getPathCode().hashCode());
        result = prime * result + ((getPayCenterNo() == null) ? 0 : getPayCenterNo().hashCode());
        result = prime * result + ((getRecvPath() == null) ? 0 : getRecvPath().hashCode());
        result = prime * result + ((getSendPath() == null) ? 0 : getSendPath().hashCode());
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getRemoteSendPath() == null) ? 0 : getRemoteSendPath().hashCode());
        result = prime * result + ((getRemoteRecvPath() == null) ? 0 : getRemoteRecvPath().hashCode());
        result = prime * result + ((getWorkClass() == null) ? 0 : getWorkClass().hashCode());
        result = prime * result + ((getWorkCharset() == null) ? 0 : getWorkCharset().hashCode());
        result = prime * result + ((getWorkToken() == null) ? 0 : getWorkToken().hashCode());
        result = prime * result + ((getPackType() == null) ? 0 : getPackType().hashCode());
        return result;
    }
}