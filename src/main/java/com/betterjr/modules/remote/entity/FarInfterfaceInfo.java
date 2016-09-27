package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FAR_INTERFACE")
public class FarInfterfaceInfo implements BetterjrEntity {
    /**
     * 接口简称
     */
    @Column(name = "C_FACE",  columnDefinition="VARCHAR" )
    @MetaData( value="接口简称", comments = "接口简称")
    private String faceNo;

    /**
     * 接口名称
     */
    @Column(name = "C_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="接口名称", comments = "接口名称")
    private String faceName;

    /**
     * 内容签名方式；
     */
    @Column(name = "C_SIGN",  columnDefinition="VARCHAR" )
    @MetaData( value="内容签名方式", comments = "内容签名方式；")
    private String signMode;

    /**
     * 内容格式，XML，JSON，TXT
     */
    @Column(name = "C_FORMAT",  columnDefinition="VARCHAR" )
    @MetaData( value="内容格式", comments = "内容格式，XML，JSON，TXT")
    private String dataFormat;

    /**
     * 内容加密，0不加密，1申请加密内容，2返回加密所有，3申请加密所有，4返回加密所有。
     */
    @Column(name = "C_ENCRYPT",  columnDefinition="VARCHAR" )
    @MetaData( value="内容加密", comments = "内容加密，0不加密，1申请加密内容，2返回加密所有，3申请加密所有，4返回加密所有。")
    private String encrypt;

    /**
     * 头部签名模式；有值使用头部签名模式
     */
    @Column(name = "C_HEADSIGN",  columnDefinition="VARCHAR" )
    @MetaData( value="头部签名模式", comments = "头部签名模式；有值使用头部签名模式")
    private String headSign;

    /**
     * 使用申请字段排序，0不排序，1按字段名字排序
     */
    @Column(name = "C_FIELDORDER",  columnDefinition="VARCHAR" )
    @MetaData( value="使用申请字段排序", comments = "使用申请字段排序，0不排序，1按字段名字排序")
    private String fieldOrder;

    /**
     * FTP地址信息，包括端口号，默认是22号端口，例如：sftp.alipay.net:25
     */
    @Column(name = "C_FTP",  columnDefinition="VARCHAR" )
    @MetaData( value="FTP地址信息", comments = "FTP地址信息，包括端口号，默认是22号端口，例如：sftp.alipay.net:25")
    private String ftpUrl;

    /**
     * 远程调用的基础路径，单路径的接口不用定义每个功能的URL地址
     */
    @Column(name = "C_URL",  columnDefinition="VARCHAR" )
    @MetaData( value="远程调用的基础路径", comments = "远程调用的基础路径，单路径的接口不用定义每个功能的URL地址")
    private String url;

    /**
     * 字符集，如果多字符集，使用逗号区分，第一个是申请的字符集，第二个是返回的字符集，第三个是页面调用申请字符集，第四个是页面调用返回的字符集
     */
    @Column(name = "C_CHARSET",  columnDefinition="VARCHAR" )
    @MetaData( value="字符集", comments = "字符集，如果多字符集，使用逗号区分，第一个是申请的字符集，第二个是返回的字符集，第三个是页面调用申请字符集，第四个是页面调用返回的字符集")
    private String charset;

    /**
     * 秘钥的保存路径，私钥文件使用private.pfx，公钥使用public.cer；FTP服务器增加相应的ftp前缀；使用server.jks保存所有的服务器证书
     */
    @Column(name = "C_KEYPATH",  columnDefinition="VARCHAR" )
    @MetaData( value="秘钥的保存路径", comments = "秘钥的保存路径，私钥文件使用private.pfx，公钥使用public.cer；FTP服务器增加相应的ftp前缀；使用server.jks保存所有的服务器证书")
    private String keyPath;

    /**
     * 设置各证书的密码；使用冒号加分号分隔；priv：aaaa；ftppriv:aa
     */
    @Column(name = "C_KEYPASS",  columnDefinition="VARCHAR" )
    @MetaData( value="设置各证书的密码", comments = "设置各证书的密码；使用冒号加分号分隔；priv：aaaa；ftppriv:aa")
    private String keyPass;

    /**
     * 作者
     */
    @Column(name = "C_OWNER",  columnDefinition="VARCHAR" )
    @MetaData( value="作者", comments = "作者")
    private String owner;

    /**
     * 接口分类，FUND：基金调用接口，BANK银行调用接口
     */
    @Column(name = "C_GROUP",  columnDefinition="VARCHAR" )
    @MetaData( value="接口分类", comments = "接口分类，FUND：基金调用接口，BANK银行调用接口")
    private String faceGroup;

    /**
     * 接口实现JAVA类
     */
    @Column(name = "C_PROVIDER",  columnDefinition="VARCHAR" )
    @MetaData( value="接口实现JAVA类", comments = "接口实现JAVA类")
    private String provider;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 空内容必须填写字段；例如XML的空内容，必须填写XML标签
     */
    @Column(name = "C_MUSTITEM",  columnDefinition="VARCHAR" )
    @MetaData( value="空内容必须填写字段", comments = "空内容必须填写字段；例如XML的空内容，必须填写XML标签")
    private Boolean mustItem;


    /**
     * 异常状态模板
     */
    @Column(name = "C_ERR_MODE",  columnDefinition="VARCHAR" )
    @MetaData( value="异常状态模板", comments = "异常状态模板")
    private String errCodeMode;
    
   public String getErrCodeMode() {
        return errCodeMode;
    }

    public void setErrCodeMode(String errCodeMode) {
        this.errCodeMode = errCodeMode;
    }

private static final long serialVersionUID = 1440666748882L;

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName == null ? null : faceName.trim();
    }

    public String getSignMode() {
        return signMode;
    }

    public void setSignMode(String signMode) {
        this.signMode = signMode == null ? null : signMode.trim();
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat == null ? null : dataFormat.trim();
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt == null ? null : encrypt.trim();
    }

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign == null ? null : headSign.trim();
    }

    public String getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(String fieldOrder) {
        this.fieldOrder = fieldOrder == null ? null : fieldOrder.trim();
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl == null ? null : ftpUrl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset == null ? null : charset.trim();
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath == null ? null : keyPath.trim();
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass == null ? null : keyPass.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getFaceGroup() {
        return faceGroup;
    }

    public void setFaceGroup(String faceGroup) {
        this.faceGroup = faceGroup == null ? null : faceGroup.trim();
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider == null ? null : provider.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public Boolean getMustItem() {
        return mustItem;
    }

    public void setMustItem(Boolean mustItem) {
        this.mustItem = mustItem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", faceName=").append(faceName);
        sb.append(", signMode=").append(signMode);
        sb.append(", dataFormat=").append(dataFormat);
        sb.append(", encrypt=").append(encrypt);
        sb.append(", headSign=").append(headSign);
        sb.append(", fieldOrder=").append(fieldOrder);
        sb.append(", ftpUrl=").append(ftpUrl);
        sb.append(", url=").append(url);
        sb.append(", charset=").append(charset);
        sb.append(", keyPath=").append(keyPath);
        sb.append(", keyPass=").append(keyPass);
        sb.append(", owner=").append(owner);
        sb.append(", faceGroup=").append(faceGroup);
        sb.append(", provider=").append(provider);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", mustItem=").append(mustItem);
        sb.append(", errCodeMode=").append(errCodeMode);
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
        FarInfterfaceInfo other = (FarInfterfaceInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
            && (this.getFaceName() == null ? other.getFaceName() == null : this.getFaceName().equals(other.getFaceName()))
            && (this.getSignMode() == null ? other.getSignMode() == null : this.getSignMode().equals(other.getSignMode()))
            && (this.getDataFormat() == null ? other.getDataFormat() == null : this.getDataFormat().equals(other.getDataFormat()))
            && (this.getEncrypt() == null ? other.getEncrypt() == null : this.getEncrypt().equals(other.getEncrypt()))
            && (this.getHeadSign() == null ? other.getHeadSign() == null : this.getHeadSign().equals(other.getHeadSign()))
            && (this.getFieldOrder() == null ? other.getFieldOrder() == null : this.getFieldOrder().equals(other.getFieldOrder()))
            && (this.getFtpUrl() == null ? other.getFtpUrl() == null : this.getFtpUrl().equals(other.getFtpUrl()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getCharset() == null ? other.getCharset() == null : this.getCharset().equals(other.getCharset()))
            && (this.getKeyPath() == null ? other.getKeyPath() == null : this.getKeyPath().equals(other.getKeyPath()))
            && (this.getKeyPass() == null ? other.getKeyPass() == null : this.getKeyPass().equals(other.getKeyPass()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()))
            && (this.getFaceGroup() == null ? other.getFaceGroup() == null : this.getFaceGroup().equals(other.getFaceGroup()))
            && (this.getProvider() == null ? other.getProvider() == null : this.getProvider().equals(other.getProvider()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getMustItem() == null ? other.getMustItem() == null : this.getMustItem().equals(other.getMustItem()))
            && (this.getErrCodeMode() == null ? other.getErrCodeMode() == null : this.getErrCodeMode().equals(other.getErrCodeMode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFaceName() == null) ? 0 : getFaceName().hashCode());
        result = prime * result + ((getSignMode() == null) ? 0 : getSignMode().hashCode());
        result = prime * result + ((getDataFormat() == null) ? 0 : getDataFormat().hashCode());
        result = prime * result + ((getEncrypt() == null) ? 0 : getEncrypt().hashCode());
        result = prime * result + ((getHeadSign() == null) ? 0 : getHeadSign().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getFtpUrl() == null) ? 0 : getFtpUrl().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getCharset() == null) ? 0 : getCharset().hashCode());
        result = prime * result + ((getKeyPath() == null) ? 0 : getKeyPath().hashCode());
        result = prime * result + ((getKeyPass() == null) ? 0 : getKeyPass().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        result = prime * result + ((getFaceGroup() == null) ? 0 : getFaceGroup().hashCode());
        result = prime * result + ((getProvider() == null) ? 0 : getProvider().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getMustItem() == null) ? 0 : getMustItem().hashCode());
        result = prime * result + ((getErrCodeMode() == null) ? 0 : getErrCodeMode().hashCode());
        return result;
    }
}