/**
 * 
 */
package com.betterjr.modules.remote.serializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.entity.WSInfo;

/**
 * @author hewei
 *
 */
public class WosJsonRemoteDeSerializer extends RemoteBaseDeSerializer {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final String statusPara="status";
    private final String errorcodePara="errorcode";
    
    private boolean status=false;
    
    @Override
    public List<Map<String, Object>> readResult() {
        //parse response string to data&sign 
        Map<String,String> paras=BetterStringUtils.parseParamsMap(this.data);

//        List<NameValuePair> list=URLEncodedUtils.parse(this.data, Charset.forName("UTF8"));
//        Map<String,String> paras=new HashMap<String,String>();
//        for(NameValuePair pair:list){
//            paras.put(pair.getName(), pair.getValue());
//        }
        
        String dataStr=paras.get("data");
        String signStr=paras.get("sign");
        logger.debug("data:"+dataStr);
        logger.debug("sign:"+signStr);
        String decodedData=this.func.getWorkFace().getCryptService().decrypt(dataStr);
        logger.debug("decoded data:"+decodedData);
        boolean verified=this.func.getWorkFace().getSignService().verifySign(decodedData, signStr);
        if(!verified){
            logger.error("response verify sign failed!!");
            return Collections.EMPTY_LIST;
        }
        
        Map<String,Object> dataMap=JsonMapper.parserJson(decodedData);
        String statusStr=dataMap.get(statusPara).toString();
        if(StringUtils.equals("1", statusStr)){
            this.status=true;
        }else{
            this.status=false;
        }
        this.state=dataMap.get(errorcodePara).toString();
        
        return Collections.singletonList(dataMap);
    } 
    
    
    @Override
    public boolean getStatus() {
        return this.status;
    }
    
    protected Map<String, Object> prepareInputDataMap(String decodedData){

        Map<String, Object> tmpMap = super.prepareInputDataMap(decodedData);
        Object opType=tmpMap.get("actiontype");
        tmpMap.remove("actiontype");
        if(opType==null){
            return tmpMap;
        }
        if(!"PushSignedDoc".equalsIgnoreCase(opType.toString()) && !"PushValidation".equalsIgnoreCase(opType.toString())){
            return tmpMap;
        }
        
        if("PushSignedDoc".equalsIgnoreCase(opType.toString())){
            Object extendData=tmpMap.get("extendData");
            tmpMap.remove("extendData");
            if(extendData!=null & extendData instanceof Map){
                tmpMap.putAll((Map)extendData);
            }
        }
        Map result = new HashMap();
        result.put(WSInfo.RequestDataOptypeString, opType);
        result.put(WSInfo.RequestDataTokenString, UUID.randomUUID().toString());
        result.put(WSInfo.RequestDataDataString, tmpMap);
        return result;
    }
}
