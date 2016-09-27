package com.betterjr.modules.remote.serializer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SignHelper;

public class JsonRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 4975873332459749906L;
    public static final String DIGEST_ALGORITHM = "SHA-512";

    public List<Map<String, Object>> readResult() {
        if (this.func.isHttp() == false) {
            Map<String, Object> result = (Map) JsonMapper.parserJson(this.data);
            this.state = (String) result.get("status");
            this.message = (String) result.get("msg");
            return Arrays.asList(result);
        }

        String tmpData = null;
        Map<String, Object> objMap = JsonMapper.parserJson(this.data);
        Object obj = objMap.get("channelcode");
        tmpData = this.getString("channelcode");
        if (tmpData.equals(obj) == false) {
            throw new BytterValidException(1231, "Declare channelcode not equals Service define, please Check");
        }
        obj = objMap.get(this.getString("sign_node"));
        if (obj == null) {
            throw new BytterValidException(1231, "return not define signData, please Check");
        }
        String signData = obj.toString();
        obj = objMap.get("respdata");
        if (obj == null) {
            throw new BytterValidException(1231, "return not define ReturnData, please Check");
        }

        tmpData = obj.toString();
        if (this.getBoolean("encrypt_Use", true)) {
            tmpData = new String(this.func.getWorkFace().getCryptService().bosDecode(tmpData, this.keyManager.getPrivKey()));
        }

        String tmpStr = SignHelper.digest(tmpData.getBytes(), DIGEST_ALGORITHM);
        if (this.keyManager.verifySign(tmpStr, signData ) == false) {

            throw new BytterSecurityException(25701, "sign Data verifyXmlSign false");
        }
        logger.info("return source data :" + tmpData);
        objMap = JsonMapper.parserJson(tmpData);
        this.state = objMap.get("status").toString();
        this.message = objMap.get("msg").toString();
        objMap = (Map) objMap.get("data");
        logger.info("return data :" + objMap.toString());
        if ("1".equals(this.func.getOutputMode())) {
            return (List) objMap.get(this.getString("resp_realDataNode"));
        }
        else {
            return Arrays.asList(objMap);
        }
    }
}
