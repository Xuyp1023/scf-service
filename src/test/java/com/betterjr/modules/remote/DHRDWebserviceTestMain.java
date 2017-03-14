/**
 *
 */
package com.betterjr.modules.remote;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.modules.BasicServiceTest;
import com.betterjr.modules.remote.connection2.RemoteConnection;
import com.betterjr.modules.remote.connection2.RemoteConnectionFactory;

import junit.framework.Assert;

/**
 * @author hewei
 *
 */
public class DHRDWebserviceTestMain extends BasicServiceTest{

    protected CustKeyManager keyManager ;
    protected String bytterCode;
    protected String bytterToken;
    protected String custCode;
    protected String custToken;
    protected boolean isHttpCall;


    @Before
    public void beforeTest() throws Exception {
        // TODO Auto-generated method stub
        keyManager = new CustKeyManager("/keys/partner/private.pfx", "123456", "/keys/partner/public.cer");
        bytterCode = "bytter";
        bytterToken = "bytter123456789";
        custCode = "DHRD";
        custToken = "93bdcd8c-aaea-4684-9103-b6bb88e2dd91";
        isHttpCall = true;
    }

    public String getTargetUrl() {
        return "http://liuwl.qiejf.com:8081/better/forAgency/webservice/json";
        //        return "https://demo.qiejf.com:6443/better/forAgency/webservice/json";
    }

    protected String httpCall(final Map reqMap) {
        try{
            final RemoteConnectionFactory connFact = RemoteConnectionFactory.createInstance("UTF-8", "UTF-8", null);
            final RemoteConnection conn = connFact.create(getTargetUrl());
            conn.sendRequest(Collections.EMPTY_MAP, reqMap);
            final String response=conn.readStream().toString();
            return response;
        }catch(final Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    @Test
    public void testPushValidation() throws Exception{
        final String opType="PushValidation";
        final HashMap realData=new HashMap();
        realData.put("status", "1");
        realData.put("errorcode", "000");
        realData.put("remark", "test");
        realData.put("userID", "100000170");
        realData.put("actiontype", "PushValidation");
        final Map reqData=this.buildWosApiRequestMap(realData);
        final String response=this.httpCall(reqData);
        System.out.println("response:"+response);
        final Map remap=JsonMapper.parserJson(response);
        final String redata=keyManager.decrypt((String)remap.get("data"));
        System.out.println("response data:"+redata);
        Assert.assertEquals(0, 0);
    }

    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    @Test
    public void testPushSignedDoc() throws Exception{
        final String opType="PushSignedDoc";
        final HashMap realData=new HashMap();
        realData.put("status", "0");
        realData.put("errorcode", "000");

        final Map extendData=new HashMap();
        extendData.put("signid", "test");
        extendData.put("orderNumber", "10221121");
        extendData.put("content", "FDAFDAFDFKUJIOJKDJLFJDF");
        extendData.put("hash", "10221121");
        realData.put("extendData", extendData);

        realData.put("actiontype", "PushSignedDoc");
        final Map reqData=this.buildWosApiRequestMap(realData);
        final String response=this.httpCall(reqData);
        System.out.println("response:"+response);
        final Map remap=JsonMapper.parserJson(response);
        final String redata=keyManager.decrypt((String)remap.get("data"));
        System.out.println("response data:"+redata);
        Assert.assertEquals(0, 0);
    }


    @Test
    public void testQueryDownloadList() throws Exception {
        final String opType = "queryDownloadForSign";
        final HashMap realData = new HashMap();
        realData.put("custId", "100000170");
        realData.put("unitCode", "100000170");
        //        realData.put("custId", "102200308");
        //        realData.put("unitCode", "102200308");
        realData.put("businType", "01");
        realData.put("requestNo", "");
        final Map reqMap = buildWebServiceRequestMap(opType, realData);

        final String response=this.httpCall(reqMap);
        System.out.println("response:" + response);
        final Map remap = JsonMapper.parserJson(response);
        final String redata = keyManager.decrypt((String) remap.get("data"));
        System.out.println("response data:" + redata);
        Assert.assertEquals(0, 0);
    }


    protected Map buildWosApiRequestMap(final Map realData) throws Exception {
        final String reqData = JsonMapper.buildNormalMapper().toJson(realData);
        System.out.println("request real data:"+reqData);
        // 构造http request form data
        final Map reqMap = new HashMap();
        reqMap.put("data", keyManager.encrypt(reqData));
        reqMap.put("sign", keyManager.signData(reqData));
        reqMap.put("partnerCode", custCode);
        reqMap.put("partnerToken", custToken);
        System.out.println("request send data:"+reqMap);
        return reqMap;
    }

    protected Map buildWebServiceRequestMap(final String opType,final Object realData) throws Exception {
        final Map<String, Object> data = new HashMap();
        final String newToken=UUID.randomUUID().toString();
        data.put("token", newToken);
        data.put("opType", opType);
        data.put("data", realData);
        final String reqData = JsonMapper.buildNormalMapper().toJson(data);
        System.out.println("request real data:"+reqData);
        // 构造http request form data
        final Map reqMap = new HashMap();
        reqMap.put("data", keyManager.encrypt(reqData));
        reqMap.put("sign", keyManager.signData(reqData));
        reqMap.put("partnerCode", custCode);
        reqMap.put("partnerToken", custToken);
        System.out.println("request send data:"+reqMap);
        return reqMap;
    }
    protected Map buildWebServiceRequestMapToXml(final String reqData) throws Exception {
        final Map<String, Object> data = new HashMap();
        System.out.println("request real data:"+reqData);
        final Map reqMap = new HashMap();
        final String signStr="<sign>"+keyManager.signData(reqData)+"</sign>";
        reqMap.put("Grp", "<Grp>"+reqData+signStr+"</Grp>");
        System.out.println("request send data:"+reqMap);
        return reqMap;
    }

    @Override
    public Class getTargetServiceClass() {
        // TODO Auto-generated method stub
        return null;
    }

}
