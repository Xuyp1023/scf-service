package com.betterjr.modules.remote.data;

import java.util.*;

import org.dom4j.Element;

import com.betterjr.modules.remote.entity.RemoteFieldInfo;

/**
 * 数据节点属性信息
 * 
 * @author zhoucy
 *
 */
public class DataAttribNode implements java.io.Serializable {

    private static final long serialVersionUID = 1461701081405022163L;
    private Map<String, RemoteFieldInfo> attrib = new LinkedHashMap();
    private final Object nodeValue;

    public static DataAttribNode create(Object anNodeValue) {

        return new DataAttribNode(anNodeValue);
    }

    public DataAttribNode(Object anNodeValue) {
        nodeValue = anNodeValue;
    }

    public void addAttrib(Element anEle) {
        if (anEle != null) {
            for (Map.Entry<String, RemoteFieldInfo> ent : this.attrib.entrySet()) {
                anEle.addAttribute(ent.getKey(), (String) ent.getValue().getValue());
            }
        }
    }

    public DataAttribNode addAttribData(String anKeyName, RemoteFieldInfo anValue) {

        this.attrib.put(anKeyName, anValue);

        return this;
    }

    public Object getNodeValue() {

        return nodeValue;
    }

    public RemoteFieldInfo remove(String anKeyName) {

        return attrib.remove(anKeyName);
    }
}