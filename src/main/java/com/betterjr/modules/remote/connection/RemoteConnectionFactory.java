package com.betterjr.modules.remote.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.config.ConfigBaseService;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BetterjrClientProtocolException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.remote.data.DataFormatInfo;
import com.betterjr.modules.remote.data.RemoteInvokeMode;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.utils.BetterJrRetryHandler;
import com.betterjr.modules.remote.utils.XmlUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.*;

public class RemoteConnectionFactory extends ConfigBaseService {

    private static final long serialVersionUID = -5234902862209209686L;
    private static final Logger logger = LoggerFactory.getLogger(RemoteConnectionFactory.class);

    private volatile BasicCookieStore cookieStore;

    private PoolingHttpClientConnectionManager poolConnManager;

    private static RequestConfig requestConfig = null;

    private boolean useCookies = false;

    private Charset enConding;

    private Charset ackEncoding;
    private boolean noneName;
    private String contentType = null;
    private Class connClass = RemoteConnection.class;

    // 仅用于测试数据回放
    private String testFile;

    private Class ftpClass = null;

    public String getContentType() {
        return contentType;
    }

    public RemoteConnection create(WorkFarFunction func, Object[] args) {
        RemoteConnection conn;
        try {
            RemoteInvokeMode invokeMode = RemoteInvokeMode.checking(func.getInvokeMode());
            Class tmpClass = invokeMode.findWorkClass(this.connClass, this.ftpClass);
            conn = (RemoteConnection) tmpClass.newInstance();
            conn.init(this, func, args);

            return conn;
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new BetterjrClientProtocolException(25201, "create connection has error", e);
        }
    }

    public Charset getEnConding() {
        return enConding;
    }

    public Charset getAckEncoding() {
        return ackEncoding;
    }

    public CloseableHttpClient getConnection() {
        CloseableHttpClient httpClient = null;
        if (this.useCookies) {
            httpClient = HttpClients.custom().setConnectionManager(poolConnManager).setDefaultCookieStore(cookieStore)
                    .setDefaultRequestConfig(requestConfig).setRetryHandler(new BetterJrRetryHandler()).build();
        } else {
            httpClient = HttpClients.custom().setConnectionManager(poolConnManager)
                    .setDefaultRequestConfig(requestConfig).setRetryHandler(new BetterJrRetryHandler()).build();

        }

        return httpClient;
    }

    public RemoteConnectionFactory() {

    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public void init(Map anConfigInfo, String anEnCoding, String ackEncoding, DataFormatInfo anDataFormat,
            KeyStore trustStore, KeyAndValueObject anPrivKeyStore) {
        try {
            enConding = Charset.forName(anEnCoding);
            if (StringUtils.isNotBlank(ackEncoding)) {
                this.ackEncoding = Charset.forName(ackEncoding);
            } else {
                this.ackEncoding = enConding;
            }
            // 需要通过以下代码声明对https连接支持
            this.addAll(anConfigInfo);
            // this.configInfo = anConfigInfo;

            // 测试文件，如果使用，则会写入
            this.testFile = getString("conn_testFile", null);
            System.out.println(this.testFile);

            // 连接超时时间，默认30秒
            int timeOut = getInt("conn_connectTimeout".toLowerCase(), 30);
            timeOut = timeOut * 1000;
            requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut)
                    .setConnectionRequestTimeout(timeOut).build();
            if (trustStore == null) {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            }

            // 开阶段，信任所有的服务器，实施后，需要修改服务证书
            SSLContext sslcontext = null;
            if (this.getBoolean("conn_bothAuth", false)) {
                try {
                    sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy())
                            .loadKeyMaterial((KeyStore) anPrivKeyStore.getValue(),
                                    anPrivKeyStore.getStrKey().toCharArray())
                            .build();
                }
                catch (Exception ex) {
                    logger.error("Create sslcontext has error", ex);
                    sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
                }
            } else {
                sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
            }
            HostnameVerifier hostnameVerifier = new TrustAnyHostnameVerifier();
            // SSLConnectionSocketFactory.getDefaultHostnameVerifier();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();

            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // max total connection to 200
            int maxTotalPool = getInt("conn_maxTotalPool".toLowerCase(), 100);
            poolConnManager.setMaxTotal(maxTotalPool);

            // default max connection per route to 20
            int maxConPerRoute = getInt("conn_maxConPerRoute".toLowerCase(), 20);
            poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);
            poolConnManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(enConding).build());
            poolConnManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeOut).build());
            this.useCookies = getBoolean("conn_useCookies".toLowerCase(), false);
            this.noneName = getBoolean("conn_noneParamName".toLowerCase(), false);
            this.contentType = anDataFormat.createContentType();

            String tmpClassName = this.getString("conn_RemoteConnection", "RemoteConnection");

            // 如果没有包体名称，则认为和他工厂在一个目录
            if (XmlUtils.split(tmpClassName, ".").size() == 1) {
                tmpClassName = this.getClass().getPackage().getName().concat(".").concat(tmpClassName);
            }
            this.connClass = Class.forName(tmpClassName);

            // 如果存在FTP处理的配置，则初始化
            tmpClassName = this.getString("conn_FtpConnection");
            if (StringUtils.isNotBlank(tmpClassName)) {
                tmpClassName = this.getClass().getPackage().getName().concat(".").concat(tmpClassName);
                this.ftpClass = Class.forName(tmpClassName);
            }
        }
        catch (KeyManagementException e) {
            throw new BetterjrClientProtocolException(25204, "Create KeyManagementException  ", e);
        }
        catch (NoSuchAlgorithmException e) {

            throw new BetterjrClientProtocolException(25203, "Create NoSuchAlgorithmException  ", e);
        }
        catch (KeyStoreException e) {

            throw new BetterjrClientProtocolException(25202, "Create Trust KeyStoreException", e);
        }
        catch (ClassNotFoundException e) {
            throw new BetterjrClientProtocolException(25201, "Create Connection Class NotFund", e);
        }
    }

    public boolean isNoneName() {
        return noneName;
    }

    public List<NameValuePair> paramsConverter(Map<String, Object> params) {
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Entry<String, Object>> paramsSet = params.entrySet();
        for (Entry<String, Object> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue().toString()));
        }
        return nvps;
    }

    public StringBuilder readStream(InputStream in) {

        return readStream(in, false);
    }

    private void writeTest(StringBuilder anSB, boolean anMock) {
        if (anMock == false && StringUtils.isNotBlank(testFile)) {
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(new FileOutputStream(this.testFile), this.ackEncoding);
                writer.write(anSB.toString());
                writer.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                IOUtils.closeQuietly(writer);
            }
        }
    }

    public StringBuilder readStream(InputStream in, boolean anMock) {
        StringBuilder sb = new StringBuilder(4096);
        if (anMock) {
            try {
                in = new FileInputStream(this.testFile);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (in == null) {
            return sb;
        }

        InputStreamReader inReader = null;
        try {
            inReader = new InputStreamReader(in, this.ackEncoding);
            char[] buffer = new char[4096];
            int readLen = 0;
            while ((readLen = inReader.read(buffer)) != -1) {
                sb.append(buffer, 0, readLen);
            }

            // 生产上需要去掉！
            writeTest(sb, anMock);

            return sb;
        }
        catch (IOException e) {
            logger.error("读取返回内容出错", e);
            throw new BetterjrClientProtocolException(25010, "readStream IOException: ", e);
        }
        finally {
            IOUtils.closeQuietly(inReader);
        }
    }

    public InputStream doGet(String url) throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponse response = this.doGet(url, null);

        return response != null ? response.getEntity().getContent() : null;
    }

    public StringBuilder doGetForString(String url) throws URISyntaxException, ClientProtocolException, IOException {

        return readStream(this.doGet(url));
    }

    public InputStream doGetForStream(String url, Map<String, Object> queryParams)
            throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponse response = this.doGet(url, queryParams);

        return response != null ? response.getEntity().getContent() : null;
    }

    public StringBuilder doGetForString(String url, Map<String, Object> queryParams)
            throws URISyntaxException, ClientProtocolException, IOException {

        return readStream(this.doGetForStream(url, queryParams));
    }

    /**
     * 基本的Get请求
     * 
     * @param url
     *            请求url
     * @param queryParams
     *            请求头的查询参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public HttpResponse doGet(String url, Map<String, Object> queryParams)
            throws URISyntaxException, ClientProtocolException, IOException {
        HttpGet gm = new HttpGet();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams)).setCharset(enConding);
        }
        gm.setURI(builder.build());

        return getConnection().execute(gm);
    }

    /**
     * 基本的Post请求
     * 
     * @param url
     *            请求url
     * @param queryParams
     *            请求头的查询参数
     * @param formParams
     *            post表单的参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public HttpResponse doPost(String url, Map<String, Object> queryParams, Map<String, Object> formParams)
            throws URISyntaxException, ClientProtocolException, IOException {
        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams)).setCharset(enConding);
        }
        pm.setURI(builder.build());
        // 填入表单参数
        if (formParams != null && !formParams.isEmpty()) {
            pm.setEntity(new UrlEncodedFormEntity(paramsConverter(formParams), enConding));
        }

        return getConnection().execute(pm);
    }

    /**
     * 多块Post请求
     * 
     * @param url
     *            请求url
     * @param queryParams
     *            请求头的查询参数
     * @param formParts
     *            post表单的参数,支持字符串-文件(FilePart)和字符串-字符串(StringPart)形式的参数
     * @param maxCount
     *            最多尝试请求的次数
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws HttpException
     * @throws IOException
     */
    public HttpResponse multipartPost(String url, Map<String, Object> queryParams, List<FormBodyPart> formParts)
            throws URISyntaxException, ClientProtocolException, IOException {

        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        pm.setURI(builder.build());
        // 填入表单参数
        if (Collections3.isEmpty(formParts) == false) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (FormBodyPart formPart : formParts) {
                entityBuilder = entityBuilder.addPart(formPart.getName(), formPart.getBody());
            }
            pm.setEntity(entityBuilder.build());
        }
        return getConnection().execute(pm);
    }

    /**
     * 获取当前Http客户端状态中的Cookie
     * 
     * @param domain
     *            作用域
     * @param port
     *            端口 传null 默认80
     * @param path
     *            Cookie路径 传null 默认"/"
     * @param useSecure
     *            Cookie是否采用安全机制 传null 默认false
     * @return
     */
    public Map<String, Cookie> getCookie(String domain, Integer port, String path, Boolean useSecure) {
        if (domain == null) {
            return null;
        }
        if (port == null) {
            port = 80;
        }
        if (path == null) {
            path = "/";
        }
        if (useSecure == null) {
            useSecure = false;
        }
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }

        CookieOrigin origin = new CookieOrigin(domain, port, path, useSecure);
        DefaultCookieSpec cookieSpec = new DefaultCookieSpec();
        Map<String, Cookie> retVal = new HashMap<String, Cookie>();
        for (Cookie cookie : cookies) {
            if (cookieSpec.match(cookie, origin)) {
                retVal.put(cookie.getName(), cookie);
            }
        }
        return retVal;
    }

    /**
     * 批量设置Cookie
     * 
     * @param cookies
     *            cookie键值对图
     * @param domain
     *            作用域 不可为空
     * @param path
     *            路径 传null默认为"/"
     * @param useSecure
     *            是否使用安全机制 传null 默认为false
     * @return 是否成功设置cookie
     */
    public boolean setCookie(Map<String, String> cookies, String domain, String path, Boolean useSecure) {
        synchronized (cookieStore) {
            if (domain == null) {
                return false;
            }
            if (path == null) {
                path = "/";
            }
            if (useSecure == null) {
                useSecure = false;
            }
            if (cookies == null || cookies.isEmpty()) {
                return true;
            }
            Set<Entry<String, String>> set = cookies.entrySet();
            String key = null;
            String value = null;
            for (Entry<String, String> entry : set) {
                key = entry.getKey();
                if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                    throw new IllegalArgumentException("cookies key and value both can not be empty");
                }
                BasicClientCookie cookie = new BasicClientCookie(key, value);
                cookie.setDomain(domain);
                cookie.setPath(path);
                cookie.setSecure(useSecure);
                cookieStore.addCookie(cookie);
            }
            return true;
        }
    }

    /**
     * 设置单个Cookie
     * 
     * @param key
     *            Cookie键
     * @param value
     *            Cookie值
     * @param domain
     *            作用域 不可为空
     * @param path
     *            路径 传null默认为"/"
     * @param useSecure
     *            是否使用安全机制 传null 默认为false
     * @return 是否成功设置cookie
     */
    public boolean setCookie(String key, String value, String domain, String path, Boolean useSecure) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put(key, value);
        return setCookie(cookies, domain, path, useSecure);
    }

    class AnyTrustStrategy implements TrustStrategy {

        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }

}
