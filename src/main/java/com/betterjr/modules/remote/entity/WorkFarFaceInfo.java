package com.betterjr.modules.remote.entity;

import java.io.File;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.betterjr.common.config.ConfigBaseService;
import com.betterjr.common.config.ConfigItemFace;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BetterjrClientProtocolException;
import com.betterjr.common.exception.BytterClassNotFoundException;
import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.ClassLoaderUtil;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.remote.RemoteModule;
import com.betterjr.modules.remote.data.DataFormatInfo;
import com.betterjr.modules.remote.data.FaceDataStyle;
import com.betterjr.modules.remote.helper.RemoteCryptService;
import com.betterjr.modules.remote.helper.RemoteProxyFactory;
import com.betterjr.modules.remote.helper.RemoteProxyService;
import com.betterjr.modules.remote.helper.RemoteSignService;
import com.betterjr.modules.remote.serializer.RemoteBaseSerializer;
import com.betterjr.modules.remote.serializer.RemoteDeSerializer;
import com.betterjr.modules.remote.serializer.XmlRemoteDeSerializer;
import com.betterjr.modules.remote.serializer.XmlRemoteSerializer;

public class WorkFarFaceInfo extends FarInfterfaceInfo {
    private static final long serialVersionUID = -7318664081247509692L;
    private static Logger logger = LoggerFactory.getLogger(WorkFarFaceInfo.class);
    private static String DEF_CHARSET = "UTF-8";
    private final Map<String, WorkFarFunction> workFunMap;
    private final Map<String, FarConfigInfo> config;
    private final Map<String, FaceConvertInfo> outConverMap;
    private final Map<String, FaceConvertInfo> inConvertMap;
    private final Map<String, FaceHeaderInfo> headerMap;
    private Map<String, FaceReturnCode> faceReturnMap = null;
    private CustKeyManager keyManager;
    private final String PRIV_KEY = "private.pfx";
    private final String MATCH_KEY = "public.cer";
    private final String SFTP_KEY = "ftpprivate.pfx";
    private String sftpPass;

    private String reqCharset;
    private String respCharset;
    private String faceReqCharset;
    private String faceRespCharset;
    private DataFormatInfo workFormat;
    private Class providerClass;
    private FaceDataStyle defStyle;
    private Class deSerializerClass = XmlRemoteDeSerializer.class;
    private Class serializerClass = XmlRemoteSerializer.class;

    private KeyAndValueObject privKeyStore = null;

    private RemoteCryptService cryptService = null;

    private RemoteSignService signService = null;

    public WorkFarFunction findFunction(String anFunName) {

        return this.workFunMap.get(anFunName);
    }

    public WorkFarFunction findFistFunction() {
        return Collections3.getFirst(this.workFunMap.values());
    }

    public WorkFarFunction findFuncWithFace(String anFunName) {
        for (WorkFarFunction func : this.workFunMap.values()) {
            if (func.getFunCode().equalsIgnoreCase(anFunName)) {
                return func;
            }
        }

        throw new BytterValidException("not declare function " + anFunName);
    }

    public RemoteBaseSerializer getRemoteSerializer() {
        try {
            return (RemoteBaseSerializer) this.serializerClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {

            throw new BytterClassNotFoundException(123465, "getSerializer class not find", e);
        }
    }

    public RemoteDeSerializer getDeSerializer() {
        try {
            return (RemoteDeSerializer) this.deSerializerClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {

            throw new BytterClassNotFoundException(123465, "RemoteDeSerializer class not find", e);
        }
    }

    public Map<String, FaceHeaderInfo> findHeadMap() {

        return cloneSubHeadMap(false);
    }

    public Map<String, FaceHeaderInfo> cloneHeadMap() {

        return cloneSubHeadMap(true);
    }

    private Map<String, FaceHeaderInfo> cloneSubHeadMap(boolean anClone) {
        Map<String, FaceHeaderInfo> tmpHead = new HashMap();
        for (Map.Entry<String, FaceHeaderInfo> ent : this.headerMap.entrySet()) {
            FaceHeaderInfo headInfo = ent.getValue().clone();
            if (anClone) {
                headInfo.setColumnName(headInfo.getColumnName());
                tmpHead.put(headInfo.getColumnName(), headInfo);
            } else {
                tmpHead.put(ent.getKey(), headInfo);
            }
        }

        return tmpHead;
    }

    public WorkFarFaceInfo(FarInfterfaceInfo anFaceInfo, Map<String, WorkFarFunction> anFunMap,
            Map<String, FarConfigInfo> anConfig, Map<String, FaceConvertInfo> anOutConver,
            Map<String, FaceConvertInfo> anInConver, Map<String, FaceHeaderInfo> anHeader) {
        BeanMapper.copy(anFaceInfo, this);
        this.workFunMap = anFunMap;
        this.config = anConfig;
        this.outConverMap = anOutConver;
        this.inConvertMap = anInConver;
        this.headerMap = anHeader;
    }

    public FaceReturnCode findReturnCode(String anReturnCode) {

        return this.faceReturnMap.get(anReturnCode);
    }

    public String findFileName(String anFileInfoType) {
        ConfigBaseService bs = new ConfigBaseService(this.config);
        Map<String, String> map = bs.getMapValue("convertFileNames");
        String tmpValue = map.get(anFileInfoType);

        return StringUtils.defaultString(tmpValue, anFileInfoType);
    }

    public KeyAndValueObject getPrivKeyStore() {

        return this.privKeyStore;
    }

    /**
     * 
     * 初始化化接口信息，并检查接口相关参数
     * 
     * @throws 异常情况
     */
    public void init(RemoteProxyFactory anFactory) {

        this.faceReturnMap = anFactory.getFaceReturnMap(this.getErrCodeMode());
        if (this.faceReturnMap == null) {
            throw new BytterDeclareException(39001, "Interface Return Info Must Declare");
        }
        // 初始化加密和签名服务
        ConfigBaseService bs = new ConfigBaseService(this.config);
        this.defStyle = FaceDataStyle.checking(bs.getString("req_defStyle", null));

        // 初始化证书管理
        ClassPathResource privKeyPath = new ClassPathResource(getPrivateKeyPath());
        ClassPathResource matchKeyPath = new ClassPathResource(getMatchKeyPath());
        Map<String, String> keyPassMap = findKeyPass();
        if (matchKeyPath.exists()) {
            if (privKeyPath.exists()) {
                this.keyManager = new CustKeyManager(getPrivateKeyPath(), keyPassMap.get("priv"), getMatchKeyPath());
                this.privKeyStore = new KeyAndValueObject(keyPassMap.get("priv"),
                        KeyReader.readKeyStoreFromPKCS12ClassPath(getPrivateKeyPath(), keyPassMap.get("priv")));
            } else if (matchKeyPath.exists()) {
                Certificate matchKey = KeyReader.fromCerStoredClassPath(getMatchKeyPath());
                this.keyManager = new CustKeyManager(anFactory.getKeyManager(), matchKey);
            }
        } else {
            logger.warn("sale Face matchKeyPath not exists please checking");
        }

        // 使用简单的公钥和私钥的方式；公和私必須同步出現
        if (this.keyManager == null) {
            privKeyPath = new ClassPathResource(getSimplePrivateKey());
            matchKeyPath = new ClassPathResource(getSimpleMatchKey());
            if (privKeyPath.exists() && matchKeyPath.exists()) {
                logger.warn("sale Face use Simple Key for remote");
                this.keyManager = new CustKeyManager(getSimplePrivateKey(), getSimpleMatchKey());
            }
            if (this.keyManager != null) {
                this.keyManager.setWorkCharSet(bs.getString("encrypt_charset".toLowerCase(), DEF_CHARSET));
            }
        }

        this.sftpPass = keyPassMap.get("ftppriv");

        // 处理字符集
        if (StringUtils.isBlank(this.getCharset())) {
            this.setCharset(DEF_CHARSET);
        }
        String[] arrStr = this.getCharset().split(",");
        this.reqCharset = arrStr[0];

        if (arrStr.length > 1) {
            this.respCharset = arrStr[1];
        } else {
            this.respCharset = DEF_CHARSET;
        }
        if (arrStr.length > 2) {
            this.faceReqCharset = arrStr[1];
        } else {
            this.faceReqCharset = DEF_CHARSET;
        }
        if (arrStr.length > 3) {
            this.faceRespCharset = arrStr[1];
        } else {
            this.faceRespCharset = DEF_CHARSET;
        }

        // 初始化功能信息
        for (Map.Entry<String, WorkFarFunction> ent : this.workFunMap.entrySet()) {
            ent.getValue().init(this);
        }

        // 初始化功能信息
        for (Map.Entry<String, FaceHeaderInfo> ent : this.headerMap.entrySet()) {
            ent.getValue().init(this);
        }

        this.workFormat = DataFormatInfo.checking(this.getDataFormat());

        String tmpClassName = "";
        try {
            // 初始化化服务提供类
            this.providerClass = findDeclareClass(this.getProvider(), RemoteProxyService.class);

            tmpClassName = bs.getString("cryptService", "RemoteCryptService");
            this.cryptService = (RemoteCryptService) RemoteModule.findClassInModule(tmpClassName).newInstance();
            this.cryptService.initParameter(this.config, this.keyManager);

            tmpClassName = bs.getString("signService", "RemoteSignService");
            this.signService = (RemoteSignService) RemoteModule.findClassInModule(tmpClassName).newInstance();
            this.signService.initParameter(this.config, this.keyManager);

            try {
                this.deSerializerClass = findDeclareClass(bs.getString("deSerializerClass"), this.deSerializerClass);
                this.serializerClass = findDeclareClass(bs.getString("dataSerializerClass"), this.serializerClass);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        catch (ClassNotFoundException e) {
            throw new BetterjrClientProtocolException(25201, "Create " + tmpClassName + " Service Class NotFund", e);
        }
        catch (InstantiationException e) {
            throw new BetterjrClientProtocolException(25201,
                    "Create  " + tmpClassName + " Service InstantiationException", e);
        }
        catch (IllegalAccessException e) {
            throw new BetterjrClientProtocolException(25201,
                    "Create  " + tmpClassName + " Service IllegalAccessException", e);
        }

    }

    public static Class findDeclareClass(String tmpClassName) throws ClassNotFoundException {
        return findDeclareClass(tmpClassName, null);
    }

    public static Class findDeclareClass(String tmpClassName, Class anDefValue) throws ClassNotFoundException {
        if (StringUtils.isNotBlank(tmpClassName)) {
            return RemoteModule.findClassInModule(tmpClassName);
        }
        return anDefValue;
    }

    public FaceDataStyle getDefStyle() {
        return defStyle;
    }

    public RemoteCryptService getCryptService() {
        return cryptService;
    }

    public RemoteSignService getSignService() {
        return signService;
    }

    public Map<String, FaceConvertInfo> getOutConverMap() {
        return outConverMap;
    }

    public Map<String, FaceConvertInfo> getInConvertMap() {
        return inConvertMap;
    }

    public Map<String, WorkFarFunction> getWorkFunMap() {
        return workFunMap;
    }

    public Map<String, FarConfigInfo> getConfig() {
        return config;
    }

    public DataFormatInfo getWorkFormat() {
        return workFormat;
    }

    public CustKeyManager getKeyManager() {
        return keyManager;
    }

    public String getSftpPass() {
        return sftpPass;
    }

    public Class getProviderClass() {
        return providerClass;
    }

    public String getReqCharset() {
        return reqCharset;
    }

    public String getRespCharset() {
        return respCharset;
    }

    public String getFaceReqCharset() {
        return faceReqCharset;
    }

    public String getFaceRespCharset() {
        return faceRespCharset;
    }

    private Map<String, String> findKeyPass() {
        String arrStr[] = this.getKeyPass().split(";");
        Map<String, String> map = new LinkedHashMap<>();
        for (String tmpStr : arrStr) {
            String[] subArr = tmpStr.split(":");
            if (subArr.length == 2) {
                map.put(subArr[0], subArr[1]);
            } else {
                map.put("priv", subArr[0]);
            }
        }
        return map;
    }

    public String getSimplePrivateKey() {

        return this.getKeyPath().concat(File.separator).concat("private.pem");
    }

    public String getSimpleMatchKey() {

        return this.getKeyPath().concat(File.separator).concat("public.key");
    }

    public String getPrivateKeyPath() {
        return this.getKeyPath().concat(File.separator).concat(this.PRIV_KEY);
    }

    public String getMatchKeyPath() {

        return this.getKeyPath().concat(File.separator).concat(this.MATCH_KEY);
    }

    public String getSftpKeyPath() {

        return this.getKeyPath().concat(File.separator).concat(this.SFTP_KEY);
    }

    public String getRealFtpKeyPath() {
        String tmpStr = ClassLoaderUtil.getAppPath(WorkFarFunction.class);
        return tmpStr.concat(File.separator).concat(getSftpKeyPath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\r\n");
        sb.append("this is WorkFarFunction \r\n");

        for (WorkFarFunction obj : (workFunMap.values())) {
            sb.append(obj).append("\r\n");
        }

        sb.append("this is FarConfigInfo \r\n");
        for (ConfigItemFace obj : (config.values())) {
            sb.append(obj).append("\r\n");
        }

        sb.append("this is OutFaceConvertInfo \r\n");
        for (FaceConvertInfo obj : (outConverMap.values())) {
            sb.append(obj).append("\r\n");
        }

        sb.append("this is InFaceConvertInfo \r\n");
        for (FaceConvertInfo obj : (inConvertMap.values())) {
            sb.append(obj).append("\r\n");
        }

        return sb.toString();
    }
}
