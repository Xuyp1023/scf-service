package com.betterjr.modules.remote.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.utils.ParamValueHelper;

public class CfgyJsonRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 4975873332459749906L;

    @Override
    public List<Map<String, Object>> readResult() {
        if (this.func.isHttp() == false) {
            Map<String, Object> result = JsonMapper.parserJson(this.data);
            this.state = (String) result.get("ResultType");
            this.message = (String) result.get("LogMessage");
            return Arrays.asList(result);
        }

        Map<String, Object> objMap = JsonMapper.parserJson(this.data);
        logger.info("response parsed json data:" + objMap);

        this.state = objMap.get("ResultType").toString();
        this.message = "".equals(objMap.get("Message")) ? "成功" : (String) objMap.get("Message");
        if ("0".equals(this.state)) {
            Object obj = objMap.get("Result");
            logger.info("return data :" + obj.toString());
            if ("1".equals(this.func.getOutputMode())) {
                return (List) obj;
            } else {
                return Arrays.asList((Map<String, Object>) obj);
            }
        } else {
            return new ArrayList(0);
        }

    }

    @Override
    public WSInfo readInput(Map<String, String> formPara, WorkFarFaceInfo faceInfo) {
        try {
            String globalToken = formPara.get(WSInfo.RequestFieldTokenString);

            String configGlobalToken = this.getString("partnerToken");
            if (!globalToken.equalsIgnoreCase(configGlobalToken)) {
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1001, faceInfo);
            }
            // 先根据partnerCode定义合作伙伴，token可以定时更换，大家约定一致，避免外部攻击。

            String partnerCode = formPara.get(WSInfo.RequestFieldPartnerCodeString);
            String sign = formPara.get(WSInfo.RequestFieldSignString);
            String reqData = formPara.get(WSInfo.RequestFieldDataString);

            String decodedData = null;
            try {
                decodedData = this.keyManager.decrypt(reqData);
            }
            catch (Exception ex) {
                logger.error("decrypt data has error");
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1007, faceInfo);
            }
            if (sign != null) {
                boolean verifyResult = this.keyManager.verifySign(decodedData, sign);
                if (verifyResult == false) {
                    return WSInfo.buildErrOutput(WebServiceErrorCode.E1002, faceInfo);
                }
            }
            logger.debug("reqData=" + decodedData);

            Map<String, Object> objMap = prepareInputDataMap(decodedData);
            String opType = (String) objMap.get(WSInfo.RequestDataOptypeString);
            Object data = objMap.get(WSInfo.RequestDataDataString);
            String token = (String) objMap.get(WSInfo.RequestDataTokenString);
            try {
                this.func = faceInfo.findFuncWithFace(opType);
            }
            catch (Exception ex) {
                logger.error("get function failed!:", ex);
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1003, faceInfo);
            }
            // 如果功能不存在，直接返回
            if (this.func == null) {

                return WSInfo.buildErrOutput(WebServiceErrorCode.E1003, faceInfo);
            }
            ParamValueHelper.init(faceInfo, func, data);

            Object inputObj = null;
            if (Collection.class.isInstance(data)) {
                List list = new ArrayList();
                for (Object obj : ((Collection) data)) {
                    list.add(this.processInput(obj));
                }
                inputObj = list;
            } else {
                inputObj = this.processInput(data);
            }

            return WSInfo.buildInput(globalToken, partnerCode, objMap, opType, token, inputObj);
        }
        catch (Exception ex) {
            logger.error("not declare error ", ex);
            return WSInfo.buildErrOutput(WebServiceErrorCode.E9999, faceInfo);
        }
    }
}