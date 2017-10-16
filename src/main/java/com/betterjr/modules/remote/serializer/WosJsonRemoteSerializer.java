/**
 * 
 */
package com.betterjr.modules.remote.serializer;

import java.util.LinkedHashMap;
import java.util.Map;

import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.modules.remote.entity.FaceReturnCode;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;

/**
 * @author hewei
 *
 */
public class WosJsonRemoteSerializer extends RemoteBaseSerializer {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String dataPara = "data";
    private final String signPara = "sign";
    private final String tokenidPara = "tokenid";
    private final String actiontypePara = "actiontype";

    /* (non-Javadoc)
     * @see com.betterjr.modules.remote.service.RemoteBaseSerializer#buildReqParams(java.util.Map)
     */
    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        map.put(tokenidPara, this.getString("token"));
        map.put(actiontypePara, this.func.getFunCode());

        String tmpData = JsonMapper.toJsonString(map);
        logger.info("wos request real data =" + tmpData);

        String encryptedData = tmpData;
        if (this.getBoolean("encrypt_use", true)) {
            encryptedData = this.func.getWorkFace().getCryptService().encrypt(tmpData);
        }
        String signStr = this.func.getWorkFace().getSignService().signData(tmpData);

        map.clear();
        map.put(dataPara, encryptedData);
        map.put(signPara, signStr);

        logger.debug("wos request sent data =" + map);
        return map;
    }

    @Override
    protected Map prepareOutDataMap(WorkFarFunction anFun, WSInfo info, Object outObj, WebServiceErrorCode code,
            FaceReturnCode ReturnCode) {
        String opType = info.getOpType();
        if (!"PushSignedDoc".equalsIgnoreCase(opType) && !"PushValidation".equalsIgnoreCase(opType)) {
            return super.prepareOutDataMap(anFun, info, outObj, code, ReturnCode);
        }
        Map objMap = new LinkedHashMap<String, Object>();
        if (WebServiceErrorCode.E0000.equals(code)) {
            objMap.put("status", outObj.toString());
        } else {
            objMap.put("status", "0");
        }
        return objMap;
    }

}
