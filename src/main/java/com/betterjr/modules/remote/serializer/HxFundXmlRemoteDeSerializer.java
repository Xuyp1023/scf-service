package com.betterjr.modules.remote.serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.config.ConfigFace;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.TripleDesUtil;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.utils.ParamValueHelper;
import com.betterjr.modules.remote.utils.XmlUtils;

public class HxFundXmlRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {
    private static final Logger logger = LoggerFactory.getLogger(HxFundXmlRemoteDeSerializer.class);
    private static final long serialVersionUID = 2825783050403173818L;
    private String localPath;
    private String remotePath;

    @Override
    public void init(WorkFarFunction anFunc, RemoteConnection anConn, Map<String, FarConfigInfo> anConfigInfo,
            CustKeyManager anKeyManager) {
        initParameter(anConfigInfo, anKeyManager);
        this.func = anFunc;
        if (anFunc == null) {
            return;
        }
        this.enconding = this.func.getWorkFace().getRespCharset();
        this.data = anConn.readStream().toString();
        this.localPath = anConn.getLocalPath();
        this.remotePath = anConn.getRemotePath();
    }

    public HxFundXmlRemoteDeSerializer() {

    }

    /**
     * 根据定义的实际数据节点来处理，如果没有定义或者没有取得数据则返回原始数据.
     * 
     * @param anData
     * @return
     */
    protected List<Map<String, Object>> prepareData(Map<String, Object> anData) {
        String dataNode = this.getString("resp_realDataNode", null);
        Object obj;
        if (StringUtils.isNotBlank(dataNode)) {
            SimpleDataEntity sde = new SimpleDataEntity(dataNode, true);

            // 查询到的数据是包括了最后节点的那个MAP，获取数据，需要根据最后节点来取值
            Map tmpMap = XmlUtils.findMap(anData, sde.getValue());
            if (tmpMap == null) {
                obj = anData;
            } else {
                obj = tmpMap.get(sde.splitLastValue());
            }
        } else {
            if (anData.containsKey(JsonMapper.WORK_DATANODE)) {
                obj = anData.get(JsonMapper.WORK_DATANODE);
            } else {
                obj = anData;
            }
        }

        if (obj instanceof List) {
            return (List) obj;
        } else {
            return (List) Arrays.asList(obj);
        }
    }

    @Override
    public List<Map<String, Object>> readResult() {
        logger.info("this.data:" + this.data);
        if ("URL_EXP".equalsIgnoreCase(this.func.getResultCode())) {
            logger.info("localPath:" + localPath + ",remotePath:" + remotePath);
            orignMap = new HashMap<String, Object>();
            // 返回如果是异常情况，则代表是文件类型处理
            ConfigFace value = configInfo.get("encrypt_key");
            boolean bool = uploadFile(this.data, value.getItemValue(), this.localPath);
            if (bool) {
                orignMap.put("code", "0000");
                orignMap.put("msg", "文件下载成功");
            } else {
                orignMap.put("code", "9999");
                orignMap.put("msg", "文件下载失败");
            }
        } else {
            String tmpData = this.data;
            if ("uploadFile".equalsIgnoreCase(this.func.getFunCode())) {
                // 将json 格式转化成xml
                try {
                    Map<String, Object> objMap = JsonMapper.parserJson(this.data);
                    tmpData = buildReqParams(objMap);
                    logger.info("文件下载返回 json 转成 xml 后的值 ：" + tmpData);
                }
                catch (Exception e) {
                    tmpData = this.data;
                }
            }
            if (this.getBoolean("fileEncrypt", true)) {
                tmpData = this.func.getWorkFace().getCryptService().decrypt(data);
            }
            if (this.getBoolean("sign_Data", true)) {
                if (SignHelper.verifyXmlSign(tmpData, this.keyManager.getMatchKey()) == false) {
                    throw new BytterSecurityException(25701, "sign Data verifyXmlSign false");
                }
            }
            orignMap = parseData(tmpData);
        }
        return prepareData(orignMap);
    }

    public boolean uploadFile(String anTmpData, String anKey, String anLocalPath) {
        try {
            byte[] b = TripleDesUtil.decryptBy3DesInHex(anTmpData, anKey);
            return TripleDesUtil.persistFile(b, new File(anLocalPath));
        }
        catch (Exception e) {
            logger.error("文件解密异常：" + e.getMessage());
            return false;
        }
    }

    /**
     * 解析XML数据，如果混合了JSON数据，也一起处理
     * 
     * @param anObj
     * @return
     */
    protected Map<String, Object> parseData(Object anObj) {
        Document doc;
        Map<String, Object> map = new LinkedHashMap();
        try {
            doc = DocumentHelper.parseText(anObj.toString());
            Element root = doc.getRootElement();
            // 根据定义的状态节点列表获取状态数据
            Element baseNode = XmlUtils.findElement(root, this.getListValue("resp_StatePath"));
            // 获取应用返回的状态信息
            if (baseNode != null) {
                this.state = baseNode.elementText(this.getString("resp_State", "code"));
                this.message = baseNode.elementText(this.getString("resp_Msg", "msg"));
            }
            // 根据定义的数据节点，获取数据节点的数据
            baseNode = XmlUtils.findElement(root, this.getListValue("resp_DataPath"));
            if (baseNode != null) {
                map = XmlUtils.dom2Map(baseNode);
            }
            baseNode = XmlUtils.findElement(root, this.getListValue("resp_StatePath"));
            if (baseNode != null) {
                Map<String, Object> tmpMap = XmlUtils.dom2Map(baseNode);
                mergeMap(tmpMap, map);// 将head,body 合并成一个map
            }
        }
        catch (DocumentException e) {
            throw new BytterException(205068,
                    "XmlRemoteDeSerializer.parseData Parse Document Error, DocumentException Please Check", e);

        }
        return map;
    }

    @Override
    public WSInfo readInput(Map<String, String> formPara, WorkFarFaceInfo faceInfo) {
        try {
            String sign = formPara.get("Sign");
            String reqData = formPara.get("Grp");
            boolean verifyResult = true;
            if (this.getBoolean("sign_Data", true)) {
                verifyResult = this.keyManager.verifySign(reqData, sign);
            }
            if (verifyResult == false) {
                return WSInfo.buildErrOutput(WebServiceErrorCode.E1002, faceInfo);
            }
            logger.debug("reqData=" + reqData);
            Map<String, Object> data = grpXmlParseObject(reqData);
            String opType = (String) data.get("BizCode");
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

            return WSInfo.buildInput(null, null, data, opType, null, inputObj);
        }
        catch (Exception ex) {
            logger.error("not declare error ", ex);
            return WSInfo.buildErrOutput(WebServiceErrorCode.E9999, faceInfo);
        }
    }

    public Map<String, Object> grpXmlParseObject(String reqData) {
        Document doc;
        Map<String, Object> map = new LinkedHashMap();
        try {
            doc = DocumentHelper.parseText(reqData);
            Element root = doc.getRootElement();
            // 根据定义的状态节点列表获取状态数据
            Element baseNode = XmlUtils.findElement(root, this.getListValue("resp_StatePath"));
            // 获取应用返回的状态信息
            if (baseNode != null) {
                this.state = baseNode.elementText(this.getString("resp_State", "code"));
                this.message = baseNode.elementText(this.getString("resp_Msg", "msg"));
            }
            // 根据定义的数据节点，获取数据节点的数据
            baseNode = XmlUtils.findElement(root, this.getListValue("resp_DataPath"));
            map = XmlUtils.dom2Map(baseNode);
            baseNode = XmlUtils.findElement(root, this.getListValue("resp_StatePath"));
            Map<String, Object> tmpMap = XmlUtils.dom2Map(baseNode);
            mergeMap(tmpMap, map);// 将head,body 合并成一个map
        }
        catch (DocumentException e) {
            throw new BytterException(205068,
                    "XmlRemoteDeSerializer.parseData Parse Document Error, DocumentException Please Check", e);

        }
        return map;
    }

    protected String buildReqParams(Map<String, Object> anParamsMap) {
        Map<String, Object> grpMap = new HashMap<String, Object>();
        Map<String, Object> grpBody = new HashMap<String, Object>();
        Map<String, Object> dataBody = (Map<String, Object>) anParamsMap.get("data");
        dataBody.put("ErrorMsg", anParamsMap.get("msg").toString());
        dataBody.put("ErrorCode", anParamsMap.get("status"));
        grpBody.put("GrpBody", dataBody);
        grpMap.put("Grp", grpBody);
        Map<String, Object> objBody = new HashMap<String, Object>();
        objBody.put("Grp", grpMap);

        Document document = DocumentHelper.createDocument();
        List<String> headLst = new ArrayList<String>();
        headLst.add("Grp");
        Element rootElement = document.addElement("Grp");
        XmlUtils.map2Xml(rootElement, (Map) objBody.get("Grp"));
        Element reqBody = XmlUtils.findElement(rootElement, headLst);
        return reqBody.asXML();
    }
}
