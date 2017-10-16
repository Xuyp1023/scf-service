package com.betterjr.modules.remote.serializer;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UUIDUtils;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.data.DataAttribNode;
import com.betterjr.modules.remote.data.FaceDataStyle;
import com.betterjr.modules.remote.entity.FaceReturnCode;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.RemoteFieldInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.helper.DataConverterService;
import com.betterjr.modules.remote.helper.RemoteAlgorithmService;
import com.betterjr.modules.remote.utils.BetterjrBaseOutputHelper;
import com.betterjr.modules.remote.utils.XmlUtils;

public abstract class RemoteBaseSerializer extends RemoteAlgorithmService implements RemoteSerializer {

    private static final long serialVersionUID = 2825783050403173818L;
    protected String enconding;
    protected WorkFarFunction func;
    protected Map<String, RemoteFieldInfo> headerInfo;
    protected Map<String, RemoteFieldInfo> sendParams;
    // added for process http header
    protected RemoteConnection conn;
    public static String[] CONFIG_NODE_NAME = new String[] { XmlUtils.DATA_NODE, XmlUtils.HEAD_NODE,
            XmlUtils.ROOT_NODE };
    protected static Logger logger = LoggerFactory.getLogger(RemoteBaseSerializer.class);
    private static ThreadLocal<Object> tokenContext = new ThreadLocal();

    public static Object getRequestToken() {
        Object obj = tokenContext.get();
        if (obj == null) {
            obj = UUIDUtils.uuid();
        }
        return obj;
    }

    protected static void putRequestToken(Object anObj) {
        tokenContext.set(anObj);
    }

    public RemoteBaseSerializer() {

    }

    @Override
    public void init(WorkFarFunction anFunc, Map<String, RemoteFieldInfo> anHeaderInfo,
            Map<String, RemoteFieldInfo> anSendParams, Map<String, FarConfigInfo> anConfigInfo,
            CustKeyManager anKeyManager, RemoteConnection conn) {
        super.initParameter(anConfigInfo, anKeyManager);
        this.func = anFunc;
        this.headerInfo = anHeaderInfo;
        this.sendParams = anSendParams;
        this.enconding = this.func.getWorkFace().getReqCharset();
        this.conn = conn;
    }

    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        String tmpData = buildReqParams(map).toString();
        logger.warn(tmpData);
        if (this.getBoolean("encrypt_use", true)) {
            tmpData = this.func.getWorkFace().getCryptService().encrypt(tmpData);
        }

        map.clear();
        map.put(this.getString("req_requestName", null), tmpData);

        return map;
    }

    /**
     * 处理定义的 属性,定义采取属性名称@元素的方式；元素可以采取路径方式；如果没有使用路径，则从根遍历属性；如果不存在，则抛出异常。
     * 
     * @param 数据
     * @param anHeadInfo
     */
    public void workForAttrib(Map<String, Object> anDataMap, RemoteFieldInfo anHeadInfo) {
        SimpleDataEntity dataEntity = new SimpleDataEntity(anHeadInfo.getName(), true);
        List<String> splitData = dataEntity.findSplitValueData();
        Map map;
        if (splitData.size() == 1) {
            map = XmlUtils.findMap(anDataMap, splitData.get(0));
        } else {
            map = XmlUtils.findMap(anDataMap, splitData);
        }

        if (map != null) {
            Object obj = map.get(dataEntity.splitLastValue());
            DataAttribNode attribNode;
            if (obj instanceof DataAttribNode) {
                attribNode = (DataAttribNode) obj;
            } else {
                attribNode = new DataAttribNode(obj);
                map.put(dataEntity.splitLastValue(), attribNode);
            }

            attribNode.addAttribData(dataEntity.getName(), anHeadInfo);
        } else {
            logger.warn("workForAttrib declare attrib must in workData Map");
            // throw new BytterDeclareException(39002, "workForAttrib declare attrib must in workData Map");
        }
    }

    /**
     * 
     * 接口自己定义的数据项的处理，需要注意的是，该处理方法可能会覆盖已经存在的数据；因此如果键值存在，则需要将数据追加到列表中
     * 
     * @param 入参说明及前提条件
     * @param1 入参说明及前提条件
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public Map<String, Object> dataNoneStyle(Map<String, Object> anMap, Map<String, RemoteFieldInfo> anHeadMap) {
        Map<String, Object> dataMap;
        RemoteFieldInfo headInfo = null;
        for (Map.Entry<String, RemoteFieldInfo> ent : anHeadMap.entrySet()) {
            headInfo = ent.getValue();
            if (headInfo.getDataStyle() == FaceDataStyle.NONE && headInfo.custStyle()) {
                String key = headInfo.getName();
                dataMap = XmlUtils.findMap(anMap, headInfo.getWorkStyle());
                mergeNode(dataMap, key, headInfo);
            }
        }

        return anMap;
    }

    /**
     * 
     * 合并节点数据
     * 
     * @param 需要合并数据的映射
     * @param1 需要合并数据的Key值
     * @return 需要合并的对象
     * @throws 异常情况
     */
    protected void mergeNode(Map<String, Object> dataMap, String key, Object anHeadInfo) {
        if (dataMap == null) {
            return;
        }
        Object obj = dataMap.get(key);
        if (obj == null) {
            dataMap.put(key, anHeadInfo);
        } else if (obj instanceof Map) {
            Map tmpMap = (Map) obj;
            if (tmpMap.size() == 0) {
                dataMap.put(key, anHeadInfo);
            } else {
                List tmpList = new LinkedList<>();
                tmpList.add(obj);
                tmpList.add(anHeadInfo);
                dataMap.put(key, tmpList);
            }
        } else if (obj instanceof Collection) {
            Collection tmpList = (Collection) obj;
            if (tmpList.size() == 0) {
                dataMap.put(key, anHeadInfo);
            } else {
                tmpList.add(anHeadInfo);
            }
        } else {
            List tmpList = new LinkedList<>();
            tmpList.add(obj);
            tmpList.add(anHeadInfo);
            dataMap.put(key, tmpList);
        }
    }

    // 初始化化节点的结构
    protected Map<String, Object> initDataStruct() {
        Map<String, Object> map = new LinkedHashMap();
        for (String tmpNode : CONFIG_NODE_NAME) {
            String tmpStr = this.getString(tmpNode, null);
            if (StringUtils.isNotBlank(tmpStr)) {
                map = XmlUtils.createTreeMap(tmpStr, map);
            }
        }

        return map;
    }

    protected Map<String, Object> mgereWorkData(Map<String, Object> rootMap, Map<String, Object> dataMap) {
        Map<String, Object> tmpMap;
        SimpleDataEntity de;
        for (String tmpNode : CONFIG_NODE_NAME) {
            // 有数据才处理
            if (dataMap.containsKey(tmpNode)) {
                String tmpStr = this.getString(tmpNode, null);
                // 有配置信息才处理
                if (StringUtils.isNotBlank(tmpStr)) {
                    de = new SimpleDataEntity(tmpStr, true);
                    tmpMap = XmlUtils.findMap(rootMap, de.findSplitValueData());
                    mergeNode(tmpMap, de.splitLastValue(), dataMap.get(tmpNode));
                }
            }
        }

        return rootMap;
    }

    protected Map<String, Object> simpleMerge(Map<String, Object> rootMap, Map<String, RemoteFieldInfo> dataMap) {

        for (Map.Entry<String, RemoteFieldInfo> ent : dataMap.entrySet()) {
            rootMap.put(ent.getKey(), ent.getValue().getValue());
        }
        return rootMap;
    }

    public Map<String, Object> dataNodeStyle(Map<String, Object> anMap, Map<String, RemoteFieldInfo> anHeadMap,
            Map<String, RemoteFieldInfo> anAttrib) {

        for (RemoteFieldInfo head : anHeadMap.values()) {
            if (head.getDataStyle() == FaceDataStyle.ATTRIB) {
                anAttrib.put(head.getName(), head);
            } else if (head.getDataStyle() == FaceDataStyle.NONE) {
                continue;
            } else {
                head.getDataStyle().dataStyle(anMap, head);
            }
        }

        return anMap;
    }

    protected Map<String, Object> mergeHeadInfo() {
        Map<String, Object> map = new LinkedHashMap();

        // 处理定义的头的位置
        Map<String, RemoteFieldInfo> attribMap = new LinkedHashMap();
        Map<String, Object> rootMap = initDataStruct();

        // 如果都没有数据，表示没有使用结构化，则将head和Params的数据直接合并！
        if (rootMap.isEmpty()) {
            simpleMerge(rootMap, headerInfo);
            simpleMerge(rootMap, sendParams);
        } else {
            // 处理数据节点的样式
            map = dataNodeStyle(map, this.headerInfo, attribMap);
            map = dataNodeStyle(map, this.sendParams, attribMap);

            // 将数据节点和结构节点合并
            mgereWorkData(rootMap, map);

            System.out.println(rootMap);
            // 处理自己定义样式
            rootMap = dataNoneStyle(rootMap, headerInfo);
            rootMap = dataNoneStyle(rootMap, sendParams);

            // 处理数据属性信息；属性信息样式为，属性的名称@元素信息
            for (RemoteFieldInfo head : attribMap.values()) {
                workForAttrib(rootMap, head);
            }
        }

        System.out.println(rootMap);

        return rootMap;
    }

    protected abstract Object buildReqParams(Map<String, Object> anParamsMap);

    /**
     * 数据处理，将返回的对象处理为字符串
     * @param anMap
     * @return
     */
    protected String prepareOutData(Map<String, Object> anMap) {
        return JsonMapper.toJsonString(anMap);
    }

    @Override
    public void writeOutput(WorkFarFunction anFun, WSInfo info, Object outObj, WebServiceErrorCode code) {
        // prepare result json
        WorkFarFaceInfo faceInfo = anFun.getWorkFace();
        FaceReturnCode faceCode = faceInfo.findReturnCode(code.getStrCode());

        Map objMap = prepareOutDataMap(anFun, info, outObj, code, faceCode);

        String respData = prepareOutData(objMap);
        logger.debug("response data souce:" + respData);
        objMap.clear();

        String encryptedStr;
        if (StringUtils.isBlank(respData)) {
            respData = " ";
        }
        if (this.getBoolean("encrypt_use", true) && (this.keyManager != null)) {
            encryptedStr = this.keyManager.encrypt(respData);
        } else {
            encryptedStr = respData;
        }

        String signedStr = "";
        if (this.getBoolean("sign_Data", true) && (this.keyManager != null)) {
            signedStr = this.keyManager.signData(respData);
        }
        // eqrbank
        String localPartner = this.getString("channelcode");
        String localToken = this.getString("token");
        objMap.put(WSInfo.ResponseFieldDataString, encryptedStr);
        objMap.put(WSInfo.ResponseFieldSignString, signedStr);
        objMap.put(WSInfo.ResponseFieldPartnerCodeString, localPartner);
        objMap.put(WSInfo.ResponseFieldTokenString, localToken);

        String tmpData = prepareOutProtocol(objMap);
        logger.debug("response send data:" + tmpData);
        info.setData(tmpData);
    }

    protected String prepareOutProtocol(Map anMap) {

        return JsonMapper.toJsonString(anMap);
    }

    protected Map prepareOutDataMap(WorkFarFunction anFun, WSInfo info, Object outObj, WebServiceErrorCode code,
            FaceReturnCode anReturnCode) {
        Map objMap = new LinkedHashMap<String, Object>();
        objMap.put(WSInfo.ResponseDataStatusString, code.getStrCode());
        if (anReturnCode != null) {
            objMap.put(WSInfo.ResponseDataMessageString, anReturnCode.getReturnName());
        } else {
            objMap.put(WSInfo.ResponseDataMessageString, code.getMsg());
        }

        if (info.isSucess()) {
            String opType = info.getOpType();
            String requestRessionToken = info.getToken();
            if (this.getBoolean("process_Input", true)) {
                if (outObj != null) {
                    try {
                        if (outObj instanceof List) {
                            List outList = new ArrayList();
                            // boolean isBean = false;
                            for (Object obj : (List) outObj) {
                                if (obj == null) {
                                    logger.info("this is obj is null");
                                    continue;
                                }
                                outList.add(this.processSingleOutPut(anFun, obj));
                            }
                            objMap.put(WSInfo.ResponseDataDataString, outList);
                        } else {
                            objMap.put(WSInfo.ResponseDataDataString, this.processSingleOutPut(anFun, outObj));
                        }
                    }
                    catch (BytterValidException ex) {
                        code = WebServiceErrorCode.E1005;
                        ex.printStackTrace();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        code = WebServiceErrorCode.E9999;
                    }
                }
            } else {
                objMap.put(WSInfo.ResponseDataDataString, outObj);
            }

            objMap.put(WSInfo.ResponseDataOptypeString, opType);
            objMap.put(WSInfo.ResponseDataTokenString, requestRessionToken);
        }
        return objMap;
    }

    private Map<String, Object> processSingleOutPut(WorkFarFunction anFunc, Object outObj) {
        Map<String, Object> map = new LinkedHashMap();
        DataConverterService converter = new DataConverterService(anFunc.getWorkFace().getOutConverMap());

        // if (!(outObj instanceof Map) && !(outObj instanceof BetterjrEntity) && !(outObj instanceof BaseRemoteEntity))
        // {

        if (DataTypeInfo.simpleObject(outObj)) {
            // 如果配置了返回值，则构建一个map，否则返回空map
            String returnKey = anFunc.findSingleFieldFaceId();
            if (returnKey == null) {

                return map;
            } else {
                outObj = Collections.singletonMap(returnKey, outObj);
            }
        }
        BetterjrBaseOutputHelper outputHelper = new BetterjrBaseOutputHelper(outObj, converter);

        Map<String, RemoteFieldInfo> dataMap = outputHelper.outPutData(anFunc,
                anFunc.getWorkFace().getMustItem() == Boolean.TRUE);
        for (Map.Entry<String, RemoteFieldInfo> ent : dataMap.entrySet()) {

            map.put(ent.getKey(), ent.getValue().getValue());
        }

        return map;
    }
}