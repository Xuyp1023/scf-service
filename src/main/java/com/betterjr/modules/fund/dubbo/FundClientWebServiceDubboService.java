// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年3月14日, liuwl, creation
// ============================================================================
package com.betterjr.modules.fund.dubbo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.config.SpringPropertyResourceReader;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.service.CustCertService;
import com.betterjr.modules.fund.IFundClientWebService;
import com.betterjr.modules.supplychain.data.CoreCustInfo;
import com.betterjr.modules.supplychain.service.ScfClientService;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = IFundClientWebService.class)
public class FundClientWebServiceDubboService implements IFundClientWebService {
    private final static Logger logger = LoggerFactory.getLogger(FundClientWebServiceDubboService.class);

    protected static final String ATTACH_DATA = "attachData";

    @Autowired
    private CustCertService certService;

    @Autowired
    private CustKeyManager custKeyManager;

    private Map<String, Object> postForm(final String anURL, final Map<String, String> anParam) {
        // 创建默认的httpClient实例.
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        final HttpPost post = new HttpPost(anURL);
        // 创建参数队列
        final List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for (final Map.Entry<String, String> entry : anParam.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            post.setEntity(uefEntity);

            final CloseableHttpResponse response = httpclient.execute(post);
            try {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    final String resultJson = EntityUtils.toString(entity, "UTF-8");
                    final Map<String, Object> result = JsonMapper.parserJson(resultJson);
                    return result;
                }
            }
            finally {
                response.close();
            }
        }
        catch (final ClientProtocolException e) {
            logger.error("Client protocol exception", e);
        }
        catch (final UnsupportedEncodingException e) {
            logger.error("Client encoding exception", e);
        }
        catch (final IOException e) {
            logger.error("Client io exception", e);
        }
        finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            }
            catch (final IOException e) {
                logger.error("Client io exception", e);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.fund.IFundClientWebService#noticeAccoCheck(java.util.Map)
     */
    @Override
    public String noticeAccoCheck(final Map anMap) {

        final String anData = (String) anMap.get("data");
        final String anSign = (String) anMap.get("sign");
        final String certSerialNo = (String) anMap.get("cert");

        final CustCertInfo certInfo = certService.findBySerialNo(certSerialNo);
        final X509Certificate cert = (X509Certificate) KeyReader.fromCerBase64String(certInfo.getCertInfo());

        final String data = this.custKeyManager.decrypt(anData);

        final String operOrg = (String) anMap.get(ATTACH_DATA);
        final CoreCustInfo coreCustInfo = ScfClientService.findCoreCustInfoByOperOrg(operOrg);

        if (coreCustInfo != null && SignHelper.verifySign(data, anSign, cert)) {
            final Map<String, Object> param = JsonMapper.parserJson(data);

            final String requestNo = (String) param.get("requestNo");
            final String custNo = coreCustInfo.getCustNo().toString();

            final Map<String, String> anParam = new HashMap<String, String>();
            anParam.put("token", SpringPropertyResourceReader.getProperty("fund.token", "12345678"));
            anParam.put("requestNo", requestNo);
            anParam.put("custNo", custNo);
            final Map<String, Object> result = postForm(
                    SpringPropertyResourceReader.getProperty("fund.url.noticeAccoCheck", null), anParam);

            if (result != null) {
                final String code = (String) result.get("code");
                if (StringUtils.equals(code, "200")) {
                    final String resultData = JsonMapper.toJsonString(result.get("data"));
                    return SignHelper.encrypt(resultData, cert.getPublicKey());
                }
            }
        }
        return SignHelper.encrypt("failure", cert.getPublicKey());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.fund.IFundClientWebService#noticeTradeCheck(java.util.Map)
     */
    @Override
    public String noticeTradeCheck(final Map anMap) {

        final String anData = (String) anMap.get("data");
        final String anSign = (String) anMap.get("sign");
        final String certSerialNo = (String) anMap.get("cert");

        final CustCertInfo certInfo = certService.findBySerialNo(certSerialNo);
        final X509Certificate cert = (X509Certificate) KeyReader.fromCerBase64String(certInfo.getCertInfo());

        final String data = this.custKeyManager.decrypt(anData);

        final String operOrg = (String) anMap.get(ATTACH_DATA);
        final CoreCustInfo coreCustInfo = ScfClientService.findCoreCustInfoByOperOrg(operOrg);

        if (coreCustInfo != null && SignHelper.verifySign(data, anSign, cert)) {
            final Map<String, Object> param = JsonMapper.parserJson(data);

            final String requestNo = (String) param.get("requestNo");
            final String custNo = coreCustInfo.getCustNo().toString();

            final Map<String, String> anParam = new HashMap<String, String>();
            anParam.put("token", SpringPropertyResourceReader.getProperty("fund.token", "12345678"));
            anParam.put("requestNo", requestNo);
            anParam.put("custNo", custNo);
            final Map<String, Object> result = postForm(
                    SpringPropertyResourceReader.getProperty("fund.url.noticeToPlatForm", null), anParam);

            if (result != null) {
                final String code = (String) result.get("code");
                if (StringUtils.equals(code, "200")) {
                    return SignHelper.encrypt("success", cert.getPublicKey());
                }
            }
        }
        return SignHelper.encrypt("failure", cert.getPublicKey());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.fund.IFundClientWebService#noticeToPlatForm(java.util.Map)
     */
    @Override
    public String noticeToPlatForm(final Map anMap) {

        final String anData = (String) anMap.get("data");
        final String anSign = (String) anMap.get("sign");
        final String certSerialNo = (String) anMap.get("cert");

        final CustCertInfo certInfo = certService.findBySerialNo(certSerialNo);
        final X509Certificate cert = (X509Certificate) KeyReader.fromCerBase64String(certInfo.getCertInfo());

        final String data = this.custKeyManager.decrypt(anData);

        final String operOrg = (String) anMap.get(ATTACH_DATA);
        final CoreCustInfo coreCustInfo = ScfClientService.findCoreCustInfoByOperOrg(operOrg);

        if (coreCustInfo != null && SignHelper.verifySign(data, anSign, cert)) {
            final Map<String, Object> param = JsonMapper.parserJson(data);

            final String custNo = coreCustInfo.getCustNo().toString();

            final String requestNo = (String) param.get("requestNo");
            final String noticeType = (String) param.get("noticeType");
            final String aduitStatus = (String) param.get("aduitStatus");
            final String businFlag = (String) param.get("businFlag");
            final Map<String, String> anParam = new HashMap<String, String>();
            anParam.put("token", SpringPropertyResourceReader.getProperty("fund.token", "12345678"));
            anParam.put("requestNo", requestNo);
            anParam.put("noticeType", noticeType);
            anParam.put("aduitStatus", aduitStatus);
            anParam.put("businFlag", businFlag);
            anParam.put("custNo", custNo);
            final Map<String, Object> result = postForm(
                    SpringPropertyResourceReader.getProperty("fund.url.noticeToPlatForm", null), anParam);

            if (result != null) {
                final String code = (String) result.get("code");
                if (StringUtils.equals(code, "200")) {
                    final String resultData = JsonMapper.toJsonString(result.get("data"));
                    return SignHelper.encrypt(resultData, cert.getPublicKey());
                }
            }
        }

        return SignHelper.encrypt("failure", cert.getPublicKey());
    }

    public static void main(final String[] args) {
        final FundClientWebServiceDubboService x = new FundClientWebServiceDubboService();
        final Map<String, String> anParam = new HashMap<String, String>();
        anParam.put("requestNo", "2015112500000539010108");
        // x.noticeAccoCheck(anParam);

        final Map<String, String> anParam1 = new HashMap<String, String>();
        anParam1.put("requestNo", "2015120414416496010122");
        // x.noticeTradeCheck(anParam1);

        final Map<String, String> anParam2 = new HashMap<String, String>();
        anParam2.put("requestNo", "201512030000058801010811");
        anParam2.put("custNo", "100000162");
        anParam2.put("noticeType", "01");
        anParam2.put("aduitStatus", "01");
        anParam2.put("businFlag", "01");
        // x.noticeToPlatForm(anParam2);
    }

}
