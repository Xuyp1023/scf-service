package com.betterjr.modules.remote.serializer;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.Collections3;

public class GffundsRemoteDeSerializer extends XmlRemoteDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 4975873332459749906L;

    public List<Map<String, Object>> readResult() {
        if (this.func.isHttp() == false) {
            Map<String, Object> result = (Map) JsonMapper.parserJson(this.data);
            this.state = (String) result.get("status");
            this.message = (String) result.get("msg");
            return Arrays.asList(result);
        }
        String tmpData = null;
        logger.warn("DeseriInput:"+this.data);
        Map<String, Object> objMap = JsonMapper.parserJson(this.data);
        String signData = (String) objMap.remove("checkvalue");
        tmpData = Collections3.buildSignSourceString(objMap);
        
        if (this.keyManager.verifySign(tmpData, signData) == false) {

            throw new BytterSecurityException(25701, "sign Data verifyXmlSign false");
        }

        String obj = (String) objMap.get("merchant");
        tmpData = (String) objMap.get("service");
        Map tmpM = this.getMapValue("merchantVersion");
        if ((tmpM.get("merchant").equals(obj) && func.getFunCode().equals(tmpData)) == false) {
            
            throw new BytterValidException(25701, "sign Data verifyXmlSign false");
        }

        tmpData = (String) objMap.get("encmsg");
        if (this.getBoolean("encrypt_Use", true)) {
            tmpData = new String(this.func.getWorkFace().getCryptService().decrypt(tmpData));
        }

        logger.debug("return source data :" + tmpData);
        objMap = JsonMapper.parserJson(tmpData);
        this.state = objMap.get("retcode").toString();
        this.message = objMap.get("retmsg").toString();
        
        //get real data 
        List<String> dataNodeList = this.getListValue("resp_realDataNode");
        logger.debug("read_data_config:"+dataNodeList);
        for(String dataNodeItem:dataNodeList){
            Object objtmp=objMap.get(dataNodeItem);
            if(objtmp!=null)
            logger.debug("node type:"+objtmp.getClass());
            if(objtmp!=null && objtmp instanceof List){
                try{
                    List<Map<String, Object>> realDataList= (List<Map<String, Object>>)objtmp;
                    return realDataList;
                }catch(Exception ex){
                    logger.error("parse resonse data error:"+ex.getLocalizedMessage());
                }
            }
        }
        
        return Arrays.asList(objMap);
    }

    protected void parseString(Object objtmp, List<Map<String, Object>> parsedList) {
        // parse [{aa=bb,cf=dd},{}]
        String objTmpStr=((String)objtmp).trim();
        Map<String,Object> objTmpMap=null;
        CharBuffer builder=CharBuffer.allocate(objTmpStr.length());
        boolean nodeStart=false;
        for(char ch:objtmp.toString().toCharArray()){
            if(ch=='[' || ch==' ' ){
                continue;
            }
            if(ch=='{'){
                objTmpMap=new HashMap<String,Object>();
                nodeStart=true;
                continue;
            }
            if(ch=='}'){
                parsedList.add(objTmpMap);
                nodeStart=false;
                continue;
            }
            if(ch==','){
                if(nodeStart){
                    String keyValue=builder.toString();
                    String[] kvArray=keyValue.split("=");
                    objTmpMap.put(kvArray[0], kvArray[1]);
                    builder.clear();
                }else{
                    continue;
                }
            }
            builder.append(ch);
        }
    }
}
