package com.betterjr.modules.remote.serializer;

import java.util.Map;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.Collections3;

public class GffundsRemoteSerializer extends RemoteBaseSerializer {

    private static final long serialVersionUID = -9147331904244509304L;

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {

        return null;
    }

    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        String tmpData = JsonMapper.toJsonString(map);
        logger.debug("businessInput:"+tmpData);
        String encyData=null;
        if (this.getBoolean("encrypt_use", true)) {
            encyData = this.func.getWorkFace().getCryptService().encrypt(tmpData);
        }

        map.clear();
        map.put("encmsg", encyData);
        map.put("service", this.func.getFunCode());        
        Map tmpM = this.getMapValue("merchantVersion");
        map.putAll(tmpM);
        tmpData = Collections3.buildSignSourceString(map);
        logger.debug("need sign Data =" + tmpData);
        String signStr = SignHelper.signData(tmpData, this.keyManager.getPrivKey());
        logger.debug("mysignData =" + signStr);
        map.put("checkvalue", signStr);
        logger.debug("mysignData =" + map);
        
        return map;
    }

    
 
}
