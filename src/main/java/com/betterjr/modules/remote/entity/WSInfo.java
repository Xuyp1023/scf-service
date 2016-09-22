/**
 * 
 */
package com.betterjr.modules.remote.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.mapper.JsonMapper;

/**
 * @author hewei
 * @param <T>
 *
 */
public class WSInfo<T>  implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -406893397117100240L;
    public static final String SingleNoBeanParameterKey="singleNoBeanParameter";
    public static final String SingleNoBeanReturnKey="singleNoBeanReturn";
    
    /**
     * request fields
     */
    public static final String RequestFieldTokenString="partnerToken";
    public static final String RequestFieldSignString="sign";
    public static final String RequestFieldDataString="data";
    public static final String RequestFieldPartnerCodeString="partnerCode";
    
    /**
     *request data fields
     */
    public static final String RequestDataOptypeString="opType";
    public static final String RequestDataTokenString="token";
    public static final String RequestDataDataString="data";
    
    /**
     * response fields
     */
    public static final String ResponseFieldTokenString="partnerToken";
    public static final String ResponseFieldSignString="sign";
    public static final String ResponseFieldDataString="data";
    public static final String ResponseFieldPartnerCodeString="partnerCode";
    
    /**
     *response data fields
     */
    public static final String ResponseDataOptypeString="opType";
    public static final String ResponseDataTokenString="token";
    public static final String ResponseDataDataString="data";
    public static final String ResponseDataStatusString="status";
    public static final String ResponseDataMessageString="msg";
    
    
    /**
     * 返回状态，成功，失败
     */
    private boolean sucess=true;
    /**
     * 返回状态码
     */
    private String returnCode;
    /**
     * 返回错误信息，成功无错误信息，对应状态码
     */
    private String message;

    /**
     * 返回的数据
     */
    private String data;
    private List<Map<String, Object>> orignData;
    
    /**
     *接收到的数据
     */
    private Object input;
    private Map<String, Object> orignInput;
    private String globalToken;
    private String token;
    private String partnerCode;
    private String opType;
   
    private WebServiceErrorCode errorCode;
    
    public WebServiceErrorCode getErrorCode() {
        
        return this.errorCode;
    }

    public Object getInput() {
        return this.input;
    }

    public void setInput(Object anInput) {
        this.input = anInput;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String anReturnCode) {
        this.returnCode = anReturnCode;
    }

    public Map<String, Object> getOrignInput() {
        return this.orignInput;
    }

    public void setOrignInput(Map<String, Object> anOrignInput) {
        this.orignInput = anOrignInput;
    }



    public String getMessage() {
        return this.message;
    }

    public String getData() {
        return this.data;
    }

    public List<Map<String, Object>> getOrignData() {
        return this.orignData;
    }



    public void setMessage(String anMessage) {
        this.message = anMessage;
    }

    public void setData(String anData) {
        this.data = anData;
    }

    public void setOrignData(List<Map<String, Object>> anOrignData) {
        this.orignData = anOrignData;
    }

    public String getGlobalToken() {
        return this.globalToken;
    }

    public void setGlobalToken(String anGlobalToken) {
        this.globalToken = anGlobalToken;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String anToken) {
        this.token = anToken;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public void setPartnerCode(String anPartnerCode) {
        this.partnerCode = anPartnerCode;
    }

    public String getOpType() {
        return this.opType;
    }

    public void setOpType(String anOpType) {
        this.opType = anOpType;
    }
    
    public boolean isSucess() {
        return this.sucess;
    }

    public void setSucess(boolean anSucess) {
        this.sucess = anSucess;
    }

    public String toString(){
        return JsonMapper.toJsonString(this);
    }

    public static WSInfo buildInput(String globalToken, String partnerCode, Map<String, Object> objMap, String opType, String token, Object inputObj) {
        WSInfo info=new WSInfo();
        info.setGlobalToken(globalToken);
        info.setToken(token);
        info.setOpType(opType);
        info.setPartnerCode(partnerCode);
        info.setInput(inputObj);
        info.setOrignInput(objMap);
        return info;
    }

    public static WSInfo buildErrOutput(WebServiceErrorCode anCode, WorkFarFaceInfo face){
/*        String codeString=anCode.getStrCode();
        String defaultCodeMessage=anCode.getMsg();
        FaceReturnCode faceCode=face.findReturnCode(codeString);
*/       
        WSInfo info=new WSInfo();
        info.setReturnCode(anCode.getStrCode());
        info.setSucess(false);
        info.errorCode = anCode;

        return info;
    }

}
