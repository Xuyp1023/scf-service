package com.betterjr.modules.remote.serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.utils.BTObjectUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.helper.DataConverterService;
import com.betterjr.modules.remote.helper.RemoteAlgorithmService;
import com.betterjr.modules.remote.utils.BetterjrBaseInputHelper;
import com.betterjr.modules.remote.utils.ParamValueHelper;

public class RemoteBaseDeSerializer extends RemoteAlgorithmService implements RemoteDeSerializer {
    private static final long serialVersionUID = 3707251982865328989L;
    protected static final String ATTACH_DATA = "attachData";
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String enconding;
    protected WorkFarFunction func;
    protected List<Map<String, String>> sendParams;
    protected String data;
    protected String state;
    protected String message;
    protected Map<String, Object> orignMap;

    @Override
    public Map<String, Object> getOrignMap() {
        if (orignMap == null) {
            return new HashMap();
        }
        else {
            return orignMap;
        }
    }

    @Override
    public boolean getStatus() {
        return func.getReturnState(state);
    }

    @Override
    public String getReturnMsg() {
        if (StringUtils.isNoneBlank(this.message)) {
            return this.message;
        }
        else {
            return this.func.getReturnMsg(state);
        }
    }

    /**
     * 将列表中的子元素合并到一个MAP中
     * 
     * @param 处理
     *            的列表信息
     * @param 返回的结果
     */
    public static void mergeMap(Collection<Object> anData, Map<String, Object> anResult) {
        for (Object obj : anData) {
            if (obj instanceof Map) {
                mergeMap((Map) obj, anResult);
            }
            else if (obj instanceof Collection) {
                mergeMap((Collection) obj, anResult);
            }
        }
    }

    /**
     * 将Map中的子元素合并到一个Map中
     * 
     * @param 需要处理的Map
     * @param 返回的结果
     */
    public static void mergeMap(Map<String, Object> anData, Map<String, Object> anResult) {
        for (Map.Entry<String, Object> ent : anData.entrySet()) {
            Object obj = ent.getValue();
            if (obj instanceof Map) {

                mergeMap((Map) obj, anResult);
            }
            else if (obj instanceof Collection) {
                mergeMap((Collection) obj, anResult);
            }
            else {
                anResult.put(ent.getKey(), ent.getValue());
            }

        }
    }

    @Override
    public void init(WorkFarFunction anFunc, RemoteConnection anConn, Map<String, FarConfigInfo> anConfigInfo, CustKeyManager anKeyManager) {
        initParameter(anConfigInfo, anKeyManager);
        this.func = anFunc;
        if (anFunc == null) {
            return;
        }
        this.enconding = this.func.getWorkFace().getRespCharset();
        this.data = anConn.readStream().toString();
    }

    public List<Map<String, Object>> readResult() {

        return null;
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
            if (this.getBoolean("encrypt_use", true)) {
                try {
                    decodedData = this.keyManager.decrypt(reqData);
                }
                catch (Exception ex) {
                    logger.error("decrypt data has error");
                    return WSInfo.buildErrOutput(WebServiceErrorCode.E1007, faceInfo);
                }
            }
            else {
                decodedData = reqData;
            }
            if (this.getBoolean("sign_Data", true)) {
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
            
            if (data instanceof Map) {
                String attachData = this.getString(ATTACH_DATA, null);
                if (BetterStringUtils.isNotBlank(attachData)){
                    ((Map) data).put(ATTACH_DATA, attachData);
                }
            }
            ParamValueHelper.init(faceInfo, func, data);

            Object inputObj = null;
            if (this.getBoolean("process_Input", true)) {
                if (data instanceof Collection) {
                    List list = new ArrayList();
                    for (Object obj : ((Collection) data)) {
                        list.add(this.processInput(obj));
                    }
                    inputObj = list;
                }
                else {
                    inputObj = this.processInput(data);
                }
            }
            else {
                inputObj = data;
            }

            return WSInfo.buildInput(globalToken, partnerCode, objMap, opType, token, inputObj);
        }
        catch (Exception ex) {
            logger.error("not declare error ", ex);
            return WSInfo.buildErrOutput(WebServiceErrorCode.E9999, faceInfo);
        }
    }

    protected Map<String, Object> prepareInputDataMap(String decodedData) {
        Map<String, Object> objMap = JsonMapper.parserJson(decodedData);
        return objMap;
    }

    protected Object processInput(Object input) {

        if (!Map.class.isInstance(input)) {
            throw new BytterException(305068, "json invalid, Please Check");
        }

        DataConverterService converter = new DataConverterService(this.func.getWorkFace().getInConvertMap());
        BetterjrBaseInputHelper inputHelper = new BetterjrBaseInputHelper((Map) input, converter);

        Class inputCls = func.getWorkReturnClass();
        inputCls = BTObjectUtils.defaultIfNull(inputCls, Map.class);

        Object obj = inputHelper.readObject(inputCls, func.getPropMap());
        logger.info("processInput " + obj);
        return obj;
    }
}
