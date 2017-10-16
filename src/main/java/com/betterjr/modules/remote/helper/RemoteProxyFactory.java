package com.betterjr.modules.remote.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.betterjr.common.exception.BetterjrClientProtocolException;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.modules.remote.connection.RemoteConnectionFactory;
import com.betterjr.modules.remote.entity.*;
import com.betterjr.modules.remote.service.FarFaceService;

/**
 * 实现创建具体服务类和参数信息加载
 * 
 * @author zhoucy
 *
 */

public class RemoteProxyFactory implements Serializable {

    private static final long serialVersionUID = 236065580519259663L;
    private static final Logger logger = LoggerFactory.getLogger(RemoteProxyFactory.class);

    private Map<String, WorkFarFaceInfo> farMapInfo = new ConcurrentHashMap<String, WorkFarFaceInfo>();
    private Map<String, RemoteConnectionFactory> remoteFactory = new ConcurrentHashMap<String, RemoteConnectionFactory>();
    private Map<String, Map<String, FaceReturnCode>> faceReturnMap = null;

    @Autowired
    private CustKeyManager keyManager;

    @Autowired
    private FarFaceService faceService;

    private KeyStore trustStore = null;

    private String trustStoreFile;

    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }

    public KeyStore getTrustStore() {
        return trustStore;
    }

    public Map<String, WorkFarFaceInfo> getFarMapInfo() {
        return this.farMapInfo;
    }

    private String trustStorePass;

    private static RemoteProxyFactory instance = null;

    public static <T> T createService(String anFaceNo, Class<T> anClass) {

        return instance.create(anFaceNo, anClass);
    }

    public static String findFileName(String anAgencyNo, String anFileInfoType) {
        WorkFarFaceInfo faceInfo = instance.farMapInfo.get(anAgencyNo);
        return faceInfo.findFileName(anFileInfoType);
    }

    public void setKeyManager(CustKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public static String findReturnStatus(String anAgencyNo, String anReturnCode) {
        FaceReturnCode retCode = instance.findFaceReturnMap(anAgencyNo, anReturnCode);
        if (retCode.getStatus()) {
            return "1";
        } else {
            return "0";
        }
    }

    public FaceReturnCode findFaceReturnMap(String anAgencyNo, String anReturnCode) {
        WorkFarFaceInfo faceInfo = this.farMapInfo.get(anAgencyNo);
        FaceReturnCode retCode = null;
        if (faceInfo != null) {
            Map<String, FaceReturnCode> mmMap = this.faceReturnMap.get(faceInfo.getErrCodeMode());
            if (mmMap != null) {
                retCode = mmMap.get(anReturnCode);
            }
        }
        if (retCode == null) {
            retCode = FaceReturnCode.createNull();
        }

        return retCode;
    }

    public Map<String, FaceReturnCode> getFaceReturnMap(String anModeNo) {

        return faceReturnMap.get(anModeNo);
    }

    public WorkFarFaceInfo findWorkFarFaceInfo(String anFaceNo) {

        return this.farMapInfo.get(anFaceNo);
    }

    public CustKeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * 
     * 创建连接工厂类，并缓存
     * 
     * @param 接口简称
     * @return 连接工厂
     * @throws 异常情况
     */
    public RemoteConnectionFactory createConnFactory(String anFace) {
        RemoteConnectionFactory factory = this.remoteFactory.get(anFace);
        if (factory == null) {
            logger.info("createConnFactory doing, face:" + anFace);
            factory = new RemoteConnectionFactory();
            WorkFarFaceInfo faceInfo = farMapInfo.get(anFace);
            factory.init(faceInfo.getConfig(), faceInfo.getCharset(), faceInfo.getRespCharset(),
                    faceInfo.getWorkFormat(), trustStore, faceInfo.getPrivKeyStore());
            this.remoteFactory.put(anFace, factory);
            logger.info("createConnFactory finished, face:" + anFace);
        }

        return factory;
    }

    public static RemoteProxyService createProxyService(String anFace) {

        try {
            return instance.createProxy(anFace);
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new BetterjrClientProtocolException(25201, "Create RemoteProxyService Class InstantiationException ",
                    e);
        }
    }

    /**
     * 获取接口服务实现
     * 
     * @param 接口名称
     * @return 接口具体实现类
     * @throws 异常情况
     */
    protected <T> T create(String anFace, Class<T> anClass) {

        RemoteProxyService handler;
        try {
            handler = createProxy(anFace);
            return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { anClass }, handler);
        }
        catch (InstantiationException | IllegalAccessException e) {
            String className = anClass.getSimpleName();
            throw new BetterjrClientProtocolException(25201, "Create " + className + " Class InstantiationException ",
                    e);
        }
    }

    /**
     * 初始化代理类
     * @param anFace
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private RemoteProxyService createProxy(String anFace) throws InstantiationException, IllegalAccessException {
        RemoteProxyService handler;
        WorkFarFaceInfo faceInfo = farMapInfo.get(anFace);
        if (faceInfo == null) {
            return null;
        }

        handler = (RemoteProxyService) faceInfo.getProviderClass().newInstance();
        handler.init(this, faceInfo);

        return handler;
    }

    private void createTrustStore() {
        InputStream in = null;
        try {
            KeyStore trustKeyStore = KeyStore.getInstance("JKS");
            ClassPathResource cpr = new ClassPathResource(trustStoreFile);
            in = cpr.getInputStream();
            trustKeyStore.load(in, this.trustStorePass.toCharArray());
            Enumeration<String> eee = trustKeyStore.aliases();
            while (eee.hasMoreElements()) {
                System.out.println(eee.nextElement());
            }
            this.trustStore = trustKeyStore;
        }
        catch (CertificateException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 
     * 根据系统配置信息，初始化接口实现
     */
    public void init() {
        try {
            this.faceReturnMap = faceService.findAllReturnCode();

            this.farMapInfo = faceService.findFarFaceList();
            createTrustStore();

            // 初始化接口参数和连接工厂等信息
            for (Map.Entry<String, WorkFarFaceInfo> ent : this.farMapInfo.entrySet()) {
                ent.getValue().init(this);
                createConnFactory(ent.getValue().getFaceNo());
            }

            if (instance == null) {
                instance = this;
            }
        }
        catch (Exception ex) {
            logger.error("init remote invoke failed !", ex);
        }
    }

}