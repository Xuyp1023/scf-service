package com.betterjr.modules.remote.serializer;

import java.util.HashMap;
import java.util.Map;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.UUIDUtils;

public class YqrJsonRemoteSerializer extends RemoteBaseSerializer {
 
    private static final long serialVersionUID = -6323273090244318514L;
    
    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {

        return null;
    }

    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        
        Map<String, Object> dataMap=new HashMap<String, Object>();
        dataMap.put("data", map);
        String workToken = UUIDUtils.uuid();
        RemoteBaseSerializer.putRequestToken(workToken);
        dataMap.put("token", workToken);
        dataMap.put("opType", this.func.getFunCode());
        
        String souceData = JsonMapper.toJsonString(dataMap);
        logger.info("yqr request real data =" + souceData);
        String signStr = SignHelper.signData(souceData, this.keyManager.getPrivKey());
        if (this.getBoolean("encrypt_use", true)) {
            souceData = this.func.getWorkFace().getCryptService().encrypt(souceData);
        }

        map.clear();
        map.put("data", souceData);
        map.put("sign", signStr);
        map.put("partnerCode", this.getString("channelcode"));
        map.put("partnerToken", this.getString("token"));
        logger.debug("yqr request sent data =" + map);
        return map;
    } 
}
