package com.betterjr.modules.remote.connection2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;


public class RemoteConnection implements Serializable {

    private static final long serialVersionUID = 8363663700059319920L;

    protected HttpRequestBase method;

    protected int statusCode;

    protected String statusMessage;

    protected StringBuilder orignData;

    protected RemoteConnectionFactory factory;

    protected String workUrl;

    protected InputStream workData;
    protected CloseableHttpResponse response;
    protected Charset ackEncoding;
    protected String contentType = null;

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Charset getAckEncoding() {
        return ackEncoding;
    }

    public InputStream getWorkData() {
        return workData;
    }

    public RemoteConnection() {

    }

    public void init(RemoteConnectionFactory anFactory, String anWorkUrl) {
        factory = anFactory;
        this.workUrl = anWorkUrl;
        ackEncoding = anFactory.getAckEncoding();
        this.contentType = anFactory.getContentType();
        this.method = new HttpPost();
       // this.method = new HttpGet();
    }

    public StringBuilder getOrignData() {
        return orignData;
    }

    /**
     * Adds a HTTP header.
     */
    public void addHeader(String key, String value) {
        method.addHeader(key, value);
    }

    protected void parseResponseHeaders(CloseableHttpResponse conn) throws IOException {

    }

    protected void beforePepair(Map<String, Object> queryParams, Map<String, Object> formParams) {

    }

    public StringBuilder readStream() {

        return this.factory.readStream(this.workData);
    }

    public void sendRequest(Map<String, Object> queryParams, Map<String, Object> formParams) {
        RuntimeException ee = null;
        try {
            beforePepair(queryParams, formParams);
            response = doPost(queryParams, formParams);
            this.statusCode = response.getStatusLine().getStatusCode();
            this.statusMessage = response.getStatusLine().getReasonPhrase();

            this.parseResponseHeaders(response);
            if (statusCode >= 200 && statusCode < 300) {
                try {
                    ContentType cType = ContentType.get(response.getEntity());
                    if (cType != null) {
                        this.ackEncoding = cType.getCharset();
                    }
                }
                catch (Exception ex) {

                }
                workData = response.getEntity().getContent();
                afterPepair();
            }
            else {
                HttpEntity entity = response.getEntity();
                this.method.abort();
                System.out.println(" 接收响应：url" + this.method.getURI() + " status=" + this.statusMessage + " resopnse="
                        + EntityUtils.toString(entity, "utf-8"));
                ee = new RuntimeException("Unexpected response status: " + statusCode);
            }
        }
        catch (URISyntaxException e) {
            this.method.abort();
            ee = new RuntimeException("URISyntaxException: ", e);
        }
        catch (UnsupportedEncodingException e) {
            this.method.abort();
            ee = new RuntimeException("UnsupportedEncodingException: ", e);
        }
        catch (ConnectTimeoutException e) {
            this.method.abort();
            ee = new RuntimeException("ConnectTimeoutException: ", e);
        }
        catch (ClientProtocolException e) {
            this.method.abort();
            ee = new RuntimeException("ClientProtocolException: ", e);
        }
        catch (InterruptedIOException e) {
            this.method.abort();
            ee = new RuntimeException("InterruptedIOException: ", e);
        }
        catch (UnknownHostException e) {
            this.method.abort();
            ee = new RuntimeException("UnknownHostException: ", e);
        }
        catch (IOException e) {
            this.method.abort();
            ee = new RuntimeException("IOException: ", e);
        }
        finally {
            try {
                afterComplete();
            }
            catch (Exception ex) {
                if (ee == null) {
                    ee = new RuntimeException("afterComplete Exception: ", ex);
                }
            }
            if (ee != null) {
                throw ee;
            }
        }

    }

    /**
     * 调用申请完毕后，对数据做处理
     */
    protected void afterPepair() {

    }

    /**
     * 调用完成后，对数据做处理，不受异常影响
     */
    protected void afterComplete() {

    }

    protected String findOneData(Map<String, Object> anFormParam) {
        for (Map.Entry<String, Object> ent : anFormParam.entrySet()) {
            return ent.getValue().toString();
        }
        throw new RuntimeException("none data for request");
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
    protected CloseableHttpResponse doPost(Map<String, Object> queryParams, Map<String, Object> formParams) throws URISyntaxException,
            ClientProtocolException, IOException {
        URIBuilder builder = new URIBuilder(this.workUrl);
        System.out.println("this request URL : " + workUrl);

        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(factory.paramsConverter(queryParams)).setCharset(factory.getEnConding());
        }
        method.setURI(builder.build());
        // 填入表单参数
        if (formParams != null && !formParams.isEmpty()) {
            if (method instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase workM = (HttpEntityEnclosingRequestBase) method;
                StringEntity se;
                if (this.factory.isNoneName()) {
                    se = new StringEntity(findOneData(formParams).toString(), factory.getEnConding());
                    workM.setEntity(se);
                    if (StringUtils.isNotBlank(this.contentType)) {
                        se.setContentType(contentType);
                    }
                    // se.setContentType("application/x-www-form-urlencoded");
                }
                else {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(factory.paramsConverter(formParams), factory.getEnConding());
                    workM.setEntity(entity);
                }
            }
        }
        return factory.getConnection().execute(method);
    }

    /**
     * Returns the status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the status string.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    public void close() {
        workData = null;
    }

    public void destroy() {
        close();
        HttpClientUtils.closeQuietly(response);

    }
}
