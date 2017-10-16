package com.betterjr.modules.remote.serializer;

import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.remote.entity.FaceHeaderInfo;
import com.betterjr.modules.remote.entity.FaceReturnCode;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.utils.XmlUtils;

public class HxFundXmlRemoteSerializer extends RemoteBaseSerializer implements RemoteSerializer {

    private static final long serialVersionUID = 2825783050403173818L;

    public HxFundXmlRemoteSerializer() {

    }

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {
        return null;
    }

    protected String buildReqParams(Map<String, Object> anParamsMap, String value, String node) {
        Document document = DocumentHelper.createDocument();
        List<String> headLst = new ArrayList<String>();
        headLst.add(value);
        String tmpStr = this.getString(node, null);
        SimpleDataEntity sde = new SimpleDataEntity(tmpStr, true);
        Element rootElement = document.addElement(sde.splitLastValue());
        XmlUtils.map2Xml(rootElement, (Map) anParamsMap.get(sde.splitFirstValue()));
        Element reqBody = XmlUtils.findElement(rootElement, headLst);
        return reqBody.asXML();
    }

    /***
     * 拼接请求报文
     */
    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        map = replaceHeadMap(map);
        String headData = buildReqParams(map, "GrpHead", XmlUtils.HEAD_NODE);
        String bodyData = buildReqParams(map, "GrpBody", XmlUtils.DATA_NODE);
        String tmpData = headData + bodyData;
        if (this.getBoolean("sign_data", true)) {
            tmpData = this.keyManager.signData(tmpData);
        }
        map.clear();
        StringBuffer reqData = new StringBuffer();
        reqData.append("<?xml version='1.0' encoding='utf-8'?>");
        tmpData = "";
        reqData.append("<Grp>").append(headData).append(bodyData).append("<Sign>").append(tmpData).append("</Sign>")
                .append("</Grp>");
        logger.info("reqData:" + reqData.toString());
        map.put(this.getString("req_requestName", null), reqData.toString());

        return map;
    }

    /***
     * 处理headMap下的发送日期属性
     * @param map
     * @return
     */
    public Map<String, Object> replaceHeadMap(Map<String, Object> map) {
        try {
            Map<String, Object> grpMap = (Map) map.get("Grp");
            Map<String, Object> grpHeadMap = (Map) grpMap.get("GrpHead");
            FaceHeaderInfo headerSendDateInfo = (FaceHeaderInfo) grpHeadMap.get("SendDate");
            headerSendDateInfo.setDefValue(BetterDateUtils.getNumDate());
            headerSendDateInfo.setHeadValue(BetterDateUtils.getNumDate());
            FaceHeaderInfo headerSendTimeInfo = (FaceHeaderInfo) grpHeadMap.get("SendTime");
            headerSendTimeInfo.setDefValue(BetterDateUtils.getNumTime());
            headerSendTimeInfo.setHeadValue(BetterDateUtils.getNumTime());
            grpHeadMap.put("SendDate", headerSendDateInfo);
            grpHeadMap.put("SendTime", headerSendTimeInfo);
            grpMap.put("GrpHead", grpHeadMap);
            map.put("Grp", grpMap);
        }
        catch (Exception e) {
            logger.info("转换异常： " + e.getMessage());
        }
        return map;
    }

    @Override
    public void writeOutput(WorkFarFunction anFun, WSInfo info, Object outObj, WebServiceErrorCode code) {
        WorkFarFaceInfo faceInfo = anFun.getWorkFace();
        FaceReturnCode faceCode = faceInfo.findReturnCode(code.getStrCode());

        Map objMap = prepareOutDataMap(anFun, info, outObj, code, faceCode);
        String respData = (String) objMap.get("Grp");
        logger.debug("response data souce:" + respData);

        String encryptedStr = "";
        if (this.getBoolean("encrypt_use", true)) {
            encryptedStr = this.keyManager.encrypt(respData);
        } else {
            encryptedStr = respData;
        }
        String signedStr = "";
        if (this.getBoolean("sign_data", true)) {
            signedStr = this.keyManager.signData(encryptedStr);
        }
        objMap.put("Sign", signedStr);
        String tmpData = toXmlString(objMap);
        logger.debug("response send data:" + tmpData);
        info.setData(tmpData);
    }

    /***
     * 拼装map对象
     */
    @Override
    protected Map prepareOutDataMap(WorkFarFunction anFun, WSInfo info, Object outObj, WebServiceErrorCode code,
            FaceReturnCode anReturnCode) {
        // 先获取 headMap
        Map resultMp = (Map) outObj;
        Map<String, Object> headMap = new HashMap<String, Object>();
        headMap.put("Version", "1.0");
        headMap.put("BizCode", "B012");
        headMap.put("MctCode", "bytter");
        headMap.put("SendDate", BetterDateUtils.getNumDate());
        headMap.put("SendTime", BetterDateUtils.getNumTime());
        headMap.put("SendNo", resultMp.get("requestNo"));

        logger.info("headMap:" + headMap);
        Map bodyMap = new HashMap();
        bodyMap.put("ErrorMsg", code.getMsg());
        bodyMap.put("ErrorCode", code.getStrCode());
        Map objMp = new HashMap();
        objMp.put("GrpHead", headMap);
        objMp.put("GrpBody", bodyMap);
        Map grpMp = new HashMap();
        grpMp.put("Grp", objMp);

        String headData = buildReqParams(grpMp, "GrpHead", XmlUtils.HEAD_NODE);
        String bodyData = buildReqParams(grpMp, "GrpBody", XmlUtils.DATA_NODE);
        grpMp.put("Grp", headData + bodyData);
        return grpMp;
    }

    /***
     * 将map 转成 xml
     * @param objMap
     * @return
     */
    private String toXmlString(Map objMap) {
        StringBuffer reqData = new StringBuffer();
        reqData.append("<?xml version='1.0' encoding='utf-8'?>");
        reqData.append("<Grp>").append(objMap.get("Grp")).append("<Sign>").append(objMap.get("Sign")).append("</Sign>")
                .append("</Grp>");
        return reqData.toString();
    }
}
