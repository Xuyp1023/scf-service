package com.betterjr.modules.remote.connection2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;


public class RemoteConnectionFactory {

    private volatile BasicCookieStore cookieStore;

    private PoolingHttpClientConnectionManager poolConnManager;

    private static RequestConfig requestConfig = null;

    private boolean useCookies = false;

    private Charset enConding;

    private Charset ackEncoding;
    private boolean noneName;
    private String contentType = null;

    public String getContentType() {
        return contentType;
    }

    public RemoteConnection create(String anWorkUrl) {
        RemoteConnection conn;
        try {
            conn = new RemoteConnection();
            conn.init(this, anWorkUrl);

            return conn;
        }
        catch (Exception e) {
            throw new RuntimeException("create connection has error", e);
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
                    .setDefaultRequestConfig(requestConfig).build();
        }
        else {
            httpClient = HttpClients.custom().setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();

        }

        return httpClient;
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static RemoteConnectionFactory createInstance(String anEnCoding, String anAckEncoding, KeyStore anTrustStore) {
     
        return new RemoteConnectionFactory(anEnCoding, anAckEncoding, anTrustStore);
    }

    private RemoteConnectionFactory() {

    }

    private RemoteConnectionFactory(String anEnCoding, String ackEncoding, KeyStore trustStore) {
        try {
            enConding = Charset.forName(anEnCoding);
            if (StringUtils.isNotBlank(ackEncoding)) {
                this.ackEncoding = Charset.forName(ackEncoding);
            }
            else {
                this.ackEncoding = enConding;
            }

            // 连接超时时间，默认60秒
            int timeOut = 60;
            timeOut = timeOut * 1000;
            requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut).build();
            if (trustStore == null) {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            }

            // 开阶段，信任所有的服务器，实施后，需要修改服务证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
            HostnameVerifier hostnameVerifier = new TrustAnyHostnameVerifier();
            // SSLConnectionSocketFactory.getDefaultHostnameVerifier();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();

            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // max total connection to 200
            int maxTotalPool = 200;
            poolConnManager.setMaxTotal(maxTotalPool);

            // default max connection per route to 20
            int maxConPerRoute = 20;
            poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);
            poolConnManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(enConding).build());
            poolConnManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeOut).build());
            this.useCookies = false;
            this.noneName = false;
            this.contentType = "application/x-www-form-urlencoded";
        }
        catch (KeyManagementException e) {
            throw new RuntimeException("Create KeyManagementException  ", e);
        }
        catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Create NoSuchAlgorithmException  ", e);
        }
        catch (KeyStoreException e) {

            throw new RuntimeException("Create Trust KeyStoreException", e);
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

    public StringBuilder readStream(InputStream in, boolean anMock) {
        StringBuilder sb = new StringBuilder(4096);
        if (anMock) {
            try {
                in = new FileInputStream("testFile.txt");
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
            // writeTest(sb, anMock);

            return sb;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("readStream IOException: ", e);
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

    public InputStream doGetForStream(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponse response = this.doGet(url, queryParams);

        return response != null ? response.getEntity().getContent() : null;
    }

    public StringBuilder doGetForString(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {

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
    public HttpResponse doGet(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
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
    public HttpResponse doPost(String url, Map<String, Object> queryParams, Map<String, Object> formParams) throws URISyntaxException,
            ClientProtocolException, IOException {
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
    public HttpResponse multipartPost(String url, Map<String, Object> queryParams, List<FormBodyPart> formParts) throws URISyntaxException,
            ClientProtocolException, IOException {

        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        pm.setURI(builder.build());
        // 填入表单参数
        if (formParts != null && formParts.size() > 0) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
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

        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }

    public static void main(String[] anArg){
       RemoteConnectionFactory connFact = createInstance("UTF-8", "UTF-8", null);
       RemoteConnection conn = connFact.create("http://www.163.com/");
       Map map = new HashMap();
       conn.sendRequest(map, map);
    }
}
