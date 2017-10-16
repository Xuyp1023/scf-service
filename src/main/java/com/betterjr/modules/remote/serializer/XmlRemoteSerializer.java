package com.betterjr.modules.remote.serializer;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.security.DocumentUtil;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.modules.remote.utils.XmlUtils;

public class XmlRemoteSerializer extends RemoteBaseSerializer implements RemoteSerializer {

    private static final long serialVersionUID = 2825783050403173818L;

    public XmlRemoteSerializer() {

    }

    protected StringBuilder findElementText(Element reqBody) {
        StringBuilder sb = new StringBuilder();
        List list = reqBody.elements();
        if (list.size() > 0) {
            reqBody = (Element) list.get(0);
        }
        sb.append(reqBody.asXML().replaceAll("\t", "").replaceAll("\n", ""));

        return sb;
    }

    protected String signData(Element reqBody, String anSignNode) {
        Element root = reqBody.getParent();
        String signStr = findElementText(reqBody).toString();
        signStr = SignHelper.signData(signStr, this.keyManager.getPrivKey());
        root.addElement(anSignNode).setText(signStr);

        return reqBody.asXML();
    }

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {
        Document document = DocumentHelper.createDocument();

        String tmpStr = this.getString(XmlUtils.DATA_NODE, null);
        if (StringUtils.isNotBlank(tmpStr) == false) {
            tmpStr = this.getString(XmlUtils.ROOT_NODE, null);
        }
        SimpleDataEntity sde = new SimpleDataEntity(tmpStr, true);
        Element rootElement = document.addElement(sde.splitFirstValue());
        XmlUtils.map2Xml(rootElement, (Map) anParamsMap.get(sde.splitFirstValue()));

        boolean useSign = this.getBoolean("signData", true);
        if (useSign) {
            String signNode = this.getString("signNode", null);
            if (StringUtils.isNotBlank(signNode)) {
                Element reqBody = XmlUtils.findElement(rootElement, sde.findSplitValueData());
                return signData(reqBody, signNode);
            } else {
                logger.error(document.asXML());
                return SignHelper.signXml(DocumentUtil.getDocFromString(document.asXML()), this.keyManager.getPrivKey(),
                        sde.splitLastValue(), this.enconding);
            }
        } else {
            return document.asXML();
        }
    }

    protected String buildReqParams_old(Map<String, Object> anParamsMap) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement(this.getString("rootElement", "Finance"));
        String serialNo = SerialGenerator.getAppSerialNo(this.getInt("serialNoSize", 19));
        Element msgElement = null;
        String msgNode = this.getString("MessageNode", null);
        if (StringUtils.isNotBlank(msgNode)) {
            msgElement = rootElement.addElement("Message").addAttribute("id", serialNo);
        } else {
            msgElement = rootElement;
        }
        String REQ_BODY = this.getString("ReqBody", "ReqBody");
        Element reqBody = msgElement.addElement(REQ_BODY).addAttribute("Id", REQ_BODY);
        int workSize = anParamsMap.size();
        Element workNode = reqBody;
        for (Map.Entry<String, Object> ent : anParamsMap.entrySet()) {
            if (workSize > 1) {
                workNode = reqBody.addElement(ent.getKey()).addAttribute("Id", ent.getKey());
            }
            if (ent.getValue() instanceof Map) {
                Map<String, Object> tmpMap = (Map) ent.getValue();
                for (Map.Entry<String, Object> subEnt : tmpMap.entrySet()) {

                    Element childrenElement = workNode.addElement(subEnt.getKey());
                    childrenElement.setText("" + subEnt.getValue() + "");
                }
            }
        }

        boolean useSign = this.getBoolean("sign_data", true);
        if (useSign) {
            String signNode = this.getString("sign_node", null);
            if (StringUtils.isNotBlank(signNode)) {

                return signData(reqBody, signNode);
            } else {
                return SignHelper.signXml(DocumentUtil.getDocFromString(document.asXML()), this.keyManager.getPrivKey(),
                        REQ_BODY, this.enconding);
            }
        } else {
            return document.asXML();
        }
    }
}
