package com.betterjr.modules.remote.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;

public class YqrJsonRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 4975873332459749906L;

    public List<Map<String, Object>> readResult() {
        if (this.func.isHttp() == false) {
            Map<String, Object> result = (Map) JsonMapper.parserJson(this.data);
            this.state = (String) result.get("status");
            this.message = (String) result.get("msg");
            return Arrays.asList(result);
        }

        String tmpData = null;
        Map<String, Object> objMap = JsonMapper.parserJson(this.data);
        logger.debug("response parsed json data:"+objMap);
        Object obj = objMap.get("partnerToken");
        tmpData = this.getString("partnerToken");
        if (tmpData.equals(obj) == false) {
           // throw new BytterValidException(1231, "Global token not equals Service define, please Check");
        }
        obj = objMap.get("sign");
        if (obj == null) {
            throw new BytterValidException(1231, "return not define signData, please Check");
        }
        String signData = obj.toString();
        obj = objMap.get("data");
        if (obj == null) {
            throw new BytterValidException(1231, "return not define ReturnData, please Check");
        }

        tmpData = obj.toString();
        if (this.getBoolean("encrypt_Use", true)) {
            tmpData = this.func.getWorkFace().getCryptService().decrypt(tmpData);
        }

        if (this.keyManager.verifySign(tmpData, signData)  == false) {

            throw new BytterSecurityException(25701, "sign Data verifySign false");
        }
        logger.info("return source data :" + tmpData);
        objMap = JsonMapper.parserJson(tmpData);
        
        this.state = objMap.get("status").toString();
        this.message = objMap.get("msg").toString();
        String requestToken=objMap.get("token").toString();
        String opType=objMap.get("opType").toString();
        String workToken =(String) RemoteBaseSerializer.getRequestToken();        
        if (workToken.equals(requestToken) && "0000".equals(this.state)) {
            obj = objMap.get("data");
            logger.info("return data :" + obj.toString());
            if ("1".equals(this.func.getOutputMode())){
                return (List) obj;
            }
            else {
                logger.info("opType is " + opType);
                if ("openAcco".equalsIgnoreCase(opType)){
                   Map<String, Object> mm = (Map)obj;
                  String businStatus = mm.get("status").toString();
                  logger.info("businStatus is " + businStatus);
                  if ("0".equals(businStatus) || "1".equals(businStatus) || "2".equals(businStatus)){
                      mm.put("status", "1");
                  }
                  else{
                      mm.put("status", "0"); 
                  }
                }
                logger.info("return end data is " + obj);
                return Arrays.asList((Map<String,Object>)obj);
            }
        }
        else {
            return new ArrayList(0);
        }

    }
}