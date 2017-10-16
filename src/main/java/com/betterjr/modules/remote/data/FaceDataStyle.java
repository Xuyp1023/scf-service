package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.modules.remote.entity.RemoteFieldInfo;
import com.betterjr.modules.remote.utils.XmlUtils;

import java.util.*;

public enum FaceDataStyle {
    ROOT, HEAD, DATA, NONE, ATTRIB;
    public static FaceDataStyle checking(String anWorkType) {

        // 默认使用根路径比较合理
        return checking(anWorkType, DATA);
    }

    public static FaceDataStyle checking(String anWorkType, FaceDataStyle defStyle) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return FaceDataStyle.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            if (StringUtils.isNotBlank(anWorkType)) {
                return NONE;
            }
        }
        return defStyle;
    }

    public static void findStyle(Map<String, Object> anMap, String anKey, String anValue, String anTk) {
        StringTokenizer tokenizer = new StringTokenizer(anTk, ";");
        Map<String, Object> dataMap;
        dataMap = anMap;
        Map<String, Object> tmpMap = null;
        Object obj = null;
        int ncount = tokenizer.countTokens();
        int k = 0;
        while (tokenizer.hasMoreTokens()) {
            String tmpKey = tokenizer.nextToken();
            obj = dataMap.get(tmpKey);
            if (obj == null) {
                tmpMap = new HashMap();
                dataMap.put(tmpKey, tmpMap);
                dataMap = tmpMap;
            } else if (obj instanceof Map) {
                dataMap = (Map) obj;
            } else if (obj instanceof Collection) {
                tmpMap = new HashMap();
                ((Collection) obj).add(tmpMap);
                dataMap = tmpMap;
            }
            k++;
            if (ncount == k) {
                dataMap.put(anKey, anValue);
            }
        }
    }

    public Map<String, Object> dataStyle(Map<String, Object> anMap, RemoteFieldInfo anHeadInfo) {
        String key = anHeadInfo.getName();
        Map<String, Object> dataMap;
        switch (this) {
        case ROOT:
            dataMap = (Map) anMap.get(XmlUtils.ROOT_NODE);
            if (dataMap == null) {
                dataMap = new LinkedHashMap();
                anMap.put(XmlUtils.ROOT_NODE, dataMap);
            }
            dataMap.put(key, anHeadInfo);
            break;
        case HEAD:
            dataMap = (Map) anMap.get(XmlUtils.HEAD_NODE);
            if (dataMap == null) {
                dataMap = new LinkedHashMap();
                anMap.put(XmlUtils.HEAD_NODE, dataMap);
            }
            dataMap.put(key, anHeadInfo);

            break;
        case DATA:
            dataMap = (Map) anMap.get(XmlUtils.DATA_NODE);
            if (dataMap == null) {
                dataMap = new LinkedHashMap();
                anMap.put(XmlUtils.DATA_NODE, dataMap);
            }
            dataMap.put(key, anHeadInfo);

            break;

        // 如果没有指定，则使用默认的处理，如果自己定义了数据的路径，则使用自己定义的数据路径
        case NONE:

            break;

        // 表示该值是其他节点的属性，在这里不做处理！
        case ATTRIB:

            break;
        default:

            break;
        }

        return anMap;
    }
}
