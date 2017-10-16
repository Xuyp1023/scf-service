package com.betterjr.modules.remote.serializer;

import java.security.PrivateKey;
import java.util.Map;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SignHelper;

public class JsonRemoteSerializer extends RemoteBaseSerializer {

    private static final long serialVersionUID = 1L;

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {

        return null;
    }

    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        String tmpData = JsonMapper.toJsonString(map);
        logger.warn(tmpData);
        String signData = SignHelper.digest(tmpData.getBytes(), JsonRemoteDeSerializer.DIGEST_ALGORITHM);
        String signStr = SignHelper.signData(signData, this.keyManager.getPrivKey());
        logger.warn("mysignData =" + signStr);
        if (this.getBoolean("encrypt_use", true)) {
            tmpData = this.func.getWorkFace().getCryptService().encrypt(tmpData);
        }

        map.clear();
        map.put(this.getString("req_requestName", null), tmpData);
        map.put(this.getString("sign_node", null), signStr);
        map.put("channelcode", this.getString("channelcode"));

        return map;
    }

    public static String bosSignData(String anData, PrivateKey anPrivateKey) {
        String signData = SignHelper.digest(anData.getBytes(), JsonRemoteDeSerializer.DIGEST_ALGORITHM);
        return SignHelper.signData(signData, anPrivateKey);
    }
}
