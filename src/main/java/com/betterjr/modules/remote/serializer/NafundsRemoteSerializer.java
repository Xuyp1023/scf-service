package com.betterjr.modules.remote.serializer;

import java.util.Map;

import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.Collections3;

public class NafundsRemoteSerializer extends RemoteBaseSerializer {

    private static final long serialVersionUID = 4026262112926018056L;

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        String signSourceString = Collections3.buildSignSourceString(map);
        logger.debug(signSourceString);
        String signStr = SignHelper.signData(signSourceString, this.keyManager.getPrivKey());
        logger.debug("mysignData =" + signStr);

        if (this.getBoolean("encrypt_use", true)) {
            signSourceString = this.func.getWorkFace().getCryptService().encrypt(signSourceString);
        }

        map.clear();
        map.put("sign", signSourceString);

        return map;
    }

}
