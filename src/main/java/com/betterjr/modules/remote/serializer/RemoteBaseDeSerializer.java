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
    public static void mergeMap(final Collection<Object> anData, final Map<String, Object> anResult) {
        for (final Object obj : anData) {
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
    public static void mergeMap(final Map<String, Object> anData, final Map<String, Object> anResult) {
        for (final Map.Entry<String, Object> ent : anData.entrySet()) {
            final Object obj = ent.getValue();
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
    public void init(final WorkFarFunction anFunc, final RemoteConnection anConn, final Map<String, FarConfigInfo> anConfigInfo, final CustKeyManager anKeyManager) {
        initParameter(anConfigInfo, anKeyManager);
        this.func = anFunc;
        if (anFunc == null) {
            return;
        }
        this.enconding = this.func.getWorkFace().getRespCharset();
        this.data = anConn.readStream().toString();
    }

    @Override
    public List<Map<String, Object>> readResult() {

        return null;
    }

    @Override
    public WSInfo readInput(final Map<String, String> formPara, final WorkFarFaceInfo faceInfo) {
        try {
            final String globalToken = formPara.get(WSInfo.RequestFieldTokenString);

            final String configGlobalToken = this.getString("partnerToken");
            if (!globalToken.equalsIgnoreCase(configGlobalToken)) {
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1001, faceInfo);
            }
            // 先根据partnerCode定义合作伙伴，token可以定时更换，大家约定一致，避免外部攻击。

            final String partnerCode = formPara.get(WSInfo.RequestFieldPartnerCodeString);
            final String sign = formPara.get(WSInfo.RequestFieldSignString);
            final String reqData = formPara.get(WSInfo.RequestFieldDataString);
            final String cert = formPara.get(WSInfo.RequestFieldCertString);

            String decodedData = null;
            if (this.getBoolean("encrypt_use", true)) {
                try {
                    decodedData = this.keyManager.decrypt(reqData);
                }
                catch (final Exception ex) {
                    logger.error("decrypt data has error");
                    return WSInfo.buildErrOutput(WebServiceErrorCode.E1007, faceInfo);
                }
            }
            else {
                decodedData = reqData;
            }
            if (this.getBoolean("sign_Data", true)) {
                final boolean verifyResult = this.keyManager.verifySign(decodedData, sign);
                if (verifyResult == false) {
                    return WSInfo.buildErrOutput(WebServiceErrorCode.E1002, faceInfo);
                }
            }
            logger.debug("reqData=" + decodedData);

            final Map<String, Object> objMap = prepareInputDataMap(decodedData);
            final String opType = (String) objMap.get(WSInfo.RequestDataOptypeString);
            final Object data = objMap.get(WSInfo.RequestDataDataString);
            final String token = (String) objMap.get(WSInfo.RequestDataTokenString);
            try {
                this.func = faceInfo.findFuncWithFace(opType);
            }
            catch (final Exception ex) {
                logger.error("get function failed!:", ex);
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1003, faceInfo);
            }
            // 如果功能不存在，直接返回
            if (this.func == null) {

                return WSInfo.buildErrOutput(WebServiceErrorCode.E1003, faceInfo);
            }

            if (data instanceof Map) {
                final String attachData = this.getString(ATTACH_DATA, null);
                if (BetterStringUtils.isNotBlank(attachData)){
                    ((Map) data).put(ATTACH_DATA, attachData);
                }
                if (BetterStringUtils.isNotBlank(cert)) {
                    ((Map) data).put("cert", cert);
                }
            }
            ParamValueHelper.init(faceInfo, func, data);

            Object inputObj = null;
            if (this.getBoolean("process_Input", true)) {
                if (data instanceof Collection) {
                    final List list = new ArrayList();
                    for (final Object obj : ((Collection) data)) {
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
        catch (final Exception ex) {
            logger.error("not declare error ", ex);
            return WSInfo.buildErrOutput(WebServiceErrorCode.E9999, faceInfo);
        }
    }

    protected Map<String, Object> prepareInputDataMap(final String decodedData) {
        final Map<String, Object> objMap = JsonMapper.parserJson(decodedData);
        return objMap;
    }

    protected Object processInput(final Object input) {

        if (!Map.class.isInstance(input)) {
            throw new BytterException(305068, "json invalid, Please Check");
        }

        final DataConverterService converter = new DataConverterService(this.func.getWorkFace().getInConvertMap());
        final BetterjrBaseInputHelper inputHelper = new BetterjrBaseInputHelper((Map) input, converter);

        Class inputCls = func.getWorkReturnClass();
        inputCls = BTObjectUtils.defaultIfNull(inputCls, Map.class);

        final Object obj = inputHelper.readObject(inputCls, func.getPropMap());
        logger.info("processInput " + obj);
        return obj;
    }
}
