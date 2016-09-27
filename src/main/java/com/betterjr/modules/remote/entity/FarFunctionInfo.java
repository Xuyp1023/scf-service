package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FAR_FUNCTION")
public class FarFunctionInfo implements BetterjrEntity {
    /**
     * 接口编码
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口编码", comments = "接口编码")
    private String faceNo;

    /**
     * 对应外部服务名
     */
    @Column(name = "C_FUN", columnDefinition = "VARCHAR")
    @MetaData(value = "对应外部服务名", comments = "对应外部服务名")
    private String funBeanName;

    /**
     * 功能代码
     */
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "功能代码", comments = "功能代码")
    private String funCode;

    /**
     * 功能名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "功能名称", comments = "功能名称")
    private String funName;

    /**
     * 功能描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "功能描述", comments = "功能描述")
    private String description;

    /**
     * 调用URL地址
     */
    @Column(name = "C_URL", columnDefinition = "VARCHAR")
    @MetaData(value = "调用URL地址", comments = "调用URL地址")
    private String url;

    /**
     * 页面调用URL地址
     */
    @Column(name = "C_FACEURL", columnDefinition = "VARCHAR")
    @MetaData(value = "页面调用URL地址", comments = "页面调用URL地址")
    private String faceUrl;

    /**
     * 接口输入类信息
     */
    @Column(name = "C_INCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "接口输入类信息", comments = "接口输入类信息")
    private String invokeCLass;

    /**
     * 接口输出类内容
     */
    @Column(name = "C_OUTCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "接口输出类内容", comments = "接口输出类内容")
    private String returnClass;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 输入数据模式；0单记录，1多记录
     */
    @Column(name = "C_INPUT_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "输入数据模式", comments = "输入数据模式；0单记录，1多记录，2无参数，3多参数")
    private String inputMode;

    /**
     * 输出数据模式；0单记录，1多记录
     */
    @Column(name = "C_OUTPUT_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "输出数据模式", comments = "输出数据模式；0单记录，1多记录")
    private String outputMode;

    /**
     * 调用方式，0；使用普通http，1使用http的 MuiltPart；2使用ftp，3使用sftp;9使用文件系统
     */
    @Column(name = "C_INVOKE_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "调用方式", comments = "调用方式，0；使用普通http，1使用http的 MuiltPart，2使用ftp，3使用sftp， 4使用ftps；9使用文件系统")
    private String invokeMode = "0";

    /**
     * 编码器，即定义数据发送时候的序列化处理
     */
    @Column(name = "C_DATA_ENCODER", columnDefinition = "VARCHAR")
    @MetaData(value = "编码器", comments = "编码器，即定义数据发送时候的序列化处理")
    private String dataSerializer;

    /**
     * 解码器，即从远程返回数据的反序列化处理
     */
    @Column(name = "C_DATA_DECODER", columnDefinition = "VARCHAR")
    @MetaData(value = "解码器", comments = "解码器，即从远程返回数据的反序列化处理")
    private String dataDeSerializer;

    /**
     * 返回的功能代码
     */
    @Column(name = "C_RESULTCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "解码器", comments = "返回的功能代码")
    private String resultCode;

    private static final long serialVersionUID = 1441077082484L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFunBeanName() {
        return funBeanName;
    }

    public void setFunBeanName(String funBeanName) {
        this.funBeanName = funBeanName == null ? null : funBeanName.trim();
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode == null ? null : funCode.trim();
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName == null ? null : funName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl == null ? null : faceUrl.trim();
    }

    public String getInvokeCLass() {
        return invokeCLass;
    }

    public void setInvokeCLass(String invokeCLass) {
        this.invokeCLass = invokeCLass == null ? null : invokeCLass.trim();
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass == null ? null : returnClass.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode == null ? null : inputMode.trim();
    }

    public String getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(String outputMode) {
        this.outputMode = outputMode == null ? null : outputMode.trim();
    }

    public String getInvokeMode() {
        return this.invokeMode;
    }

    public void setInvokeMode(String anInvokeMode) {
        this.invokeMode = anInvokeMode;
    }

    public String getDataSerializer() {
        return this.dataSerializer;
    }

    public void setDataSerializer(String anDataSerializer) {
        this.dataSerializer = anDataSerializer;
    }

    public String getDataDeSerializer() {
        return this.dataDeSerializer;
    }

    public void setDataDeSerializer(String anDataDeSerializer) {
        this.dataDeSerializer = anDataDeSerializer;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(String anResultCode) {
        this.resultCode = anResultCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" faceNo=").append(faceNo);
        sb.append(", funBeanName=").append(funBeanName);
        sb.append(", funCode=").append(funCode);
        sb.append(", funName=").append(funName);
        sb.append(", description=").append(description);
        sb.append(", url=").append(url);
        sb.append(", faceUrl=").append(faceUrl);
        sb.append(", invokeCLass=").append(invokeCLass);
        sb.append(", returnClass=").append(returnClass);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", inputMode=").append(inputMode);
        sb.append(", outputMode=").append(outputMode);
        sb.append(", invokeMode=").append(invokeMode);
        sb.append(", dataSerializer=").append(dataSerializer);
        sb.append(", dataDeSerializer=").append(dataDeSerializer);
        sb.append(", resultCode=").append(resultCode);
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
        FarFunctionInfo other = (FarFunctionInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getFunBeanName() == null ? other.getFunBeanName() == null : this.getFunBeanName().equals(other.getFunBeanName()))
                && (this.getFunCode() == null ? other.getFunCode() == null : this.getFunCode().equals(other.getFunCode()))
                && (this.getFunName() == null ? other.getFunName() == null : this.getFunName().equals(other.getFunName()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getFaceUrl() == null ? other.getFaceUrl() == null : this.getFaceUrl().equals(other.getFaceUrl()))
                && (this.getInvokeCLass() == null ? other.getInvokeCLass() == null : this.getInvokeCLass().equals(other.getInvokeCLass()))
                && (this.getReturnClass() == null ? other.getReturnClass() == null : this.getReturnClass().equals(other.getReturnClass()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getInputMode() == null ? other.getInputMode() == null : this.getInputMode().equals(other.getInputMode()))
                && (this.getOutputMode() == null ? other.getOutputMode() == null : this.getOutputMode().equals(other.getOutputMode()))
                && (this.getDataSerializer() == null ? other.getDataSerializer() == null : this.getDataSerializer().equals(other.getDataSerializer()))
                && (this.getDataDeSerializer() == null ? other.getDataDeSerializer() == null : this.getDataDeSerializer().equals(
                        other.getDataDeSerializer()))
                && (this.getResultCode() == null ? other.getResultCode() == null : this.getResultCode().equals(other.getResultCode()))
                && (this.getInvokeMode() == null ? other.getInvokeMode() == null : this.getInvokeMode().equals(other.getInvokeMode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFunBeanName() == null) ? 0 : getFunBeanName().hashCode());
        result = prime * result + ((getFunCode() == null) ? 0 : getFunCode().hashCode());
        result = prime * result + ((getFunName() == null) ? 0 : getFunName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getFaceUrl() == null) ? 0 : getFaceUrl().hashCode());
        result = prime * result + ((getInvokeCLass() == null) ? 0 : getInvokeCLass().hashCode());
        result = prime * result + ((getReturnClass() == null) ? 0 : getReturnClass().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getInputMode() == null) ? 0 : getInputMode().hashCode());
        result = prime * result + ((getOutputMode() == null) ? 0 : getOutputMode().hashCode());
        result = prime * result + ((getInvokeMode() == null) ? 0 : getInvokeMode().hashCode());
        result = prime * result + ((getDataSerializer() == null) ? 0 : getDataSerializer().hashCode());
        result = prime * result + ((getDataDeSerializer() == null) ? 0 : getDataDeSerializer().hashCode());
        result = prime * result + ((getResultCode() == null) ? 0 : getResultCode().hashCode());
        return result;
    }
}