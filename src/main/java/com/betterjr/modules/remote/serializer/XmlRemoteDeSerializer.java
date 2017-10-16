package com.betterjr.modules.remote.serializer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SignHelper;
import com.betterjr.modules.remote.utils.XmlUtils;

public class XmlRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 2825783050403173818L;

    public XmlRemoteDeSerializer() {

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
        String tmpData = null;
        if (this.getBoolean("encrypt_Use", true)) {
            tmpData = this.func.getWorkFace().getCryptService().decrypt(data);
        } else {
            tmpData = this.data;
        }
        if (this.getBoolean("sign_Data", true)) {
            if (SignHelper.verifyXmlSign(tmpData, this.keyManager.getMatchKey()) == false) {
                throw new BytterSecurityException(25701, "sign Data verifyXmlSign false");
            }
        }
        orignMap = parseData(tmpData);

        return prepareData(orignMap);
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
            if (this.getBoolean("resp_JsonData", false)) {
                Map<String, Object> tmpMap = JsonMapper.parserJson(baseNode.getTextTrim());
                // 如果函数的返回是单行模式，则将数据做合并处理。及将子MAP合并到根上来.
                if ("0".equals(this.func.getOutputMode())) {
                    mergeMap(tmpMap, map);
                } else {
                    map = tmpMap;
                }
            } else {
                map = XmlUtils.dom2Map(baseNode);
            }
        }
        catch (DocumentException e) {
            throw new BytterException(205068,
                    "XmlRemoteDeSerializer.parseData Parse Document Error, DocumentException Please Check", e);

        } /*
           * catch (JsonParseException e) {
           * 
           * throw new BytterException(205068, "XmlRemoteDeSerializer.parseData Parse Document Error, JsonParseException Please Check", e);
           * 
           * } catch (JsonMappingException e) { throw new BytterException(205068,
           * "XmlRemoteDeSerializer.parseData Parse Document Error, JsonMappingException Please Check", e);
           * 
           * } catch (IOException e) { throw new BytterException(205068,
           * "XmlRemoteDeSerializer.parseData Parse Document Error, IOException Please Check", e);
           * 
           * }
           */

        return map;
    }
}
