package com.betterjr.modules.remote.helper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.exception.BytterWebServiceException;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.data.FaceDataStyle;
import com.betterjr.modules.remote.entity.RemoteDataFieldInfo;
import com.betterjr.modules.remote.entity.RemoteFieldInfo;
import com.betterjr.modules.remote.entity.RemoteResultInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.serializer.RemoteBaseSerializer;
import com.betterjr.modules.remote.serializer.RemoteDeSerializer;
import com.betterjr.modules.remote.serializer.RemoteSerializer;
import com.betterjr.modules.remote.utils.BetterjrBaseInputHelper;
import com.betterjr.modules.remote.utils.BetterjrBaseOutputHelper;
import com.betterjr.modules.remote.utils.ParamValueHelper;
import com.betterjr.modules.rule.service.QlExpressUtil;
import com.betterjr.modules.sys.service.SysConfigService;

/**
 * 接口实现类
 * 
 * @author zhoucy
 *
 */
public class RemoteProxyService implements InvocationHandler {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WorkFarFaceInfo faceInfo;
    @Resource
    protected RemoteProxyFactory proxyFactory;

    public RemoteProxyService() {

    }

    public void init(RemoteProxyFactory anFactory, WorkFarFaceInfo anFaceInfo) {
        this.proxyFactory = anFactory;
        this.faceInfo = anFaceInfo;
    }

    protected RemoteResultInfo process(RemoteConnection anConn, String anMethodName, Object[] anArgs) {
        WorkFarFunction func = this.faceInfo.findFunction(anMethodName);
        RemoteDeSerializer deSerialize = func.getDeSerializer();

        deSerialize.init(func, anConn, this.faceInfo.getConfig(), this.faceInfo.getKeyManager());
        List<Map<String, Object>> mapData = deSerialize.readResult();
        RemoteResultInfo resultInfo = null;
        if (func.isHttp()) {
            BetterjrBaseInputHelper inputHelper = null;
            DataConverterService converter = new DataConverterService(this.faceInfo.getInConvertMap());
            List list = new LinkedList();
            for (Map<String, Object> ent : mapData) {
                inputHelper = new BetterjrBaseInputHelper(ent, converter);
                list.add(inputHelper.readObject(func.getWorkReturnClass(), func.getPropMap()));
            }
            resultInfo = new RemoteResultInfo(mapData, list, deSerialize.getStatus(), deSerialize.getReturnMsg());
        }
        else {
            resultInfo = new RemoteResultInfo(mapData, mapData, deSerialize.getStatus(), deSerialize.getReturnMsg());
        }

        return resultInfo;
    }

    /**
     * 
     * @param anMethod
     * @param anArgs
     * @return
     */
    private Object[] prepareParam(Method anMethod, Object[] anArgs) {
        Map<String, Object> map = new LinkedCaseInsensitiveMap();
        Parameter[] params = anMethod.getParameters();
        boolean useMap = false;
        for (int i = 0; i < anArgs.length; i++) {
            Parameter pp = params[i];
            map.put(pp.getName(), anArgs[i]);
            if ((anArgs[i] instanceof Map || anArgs[i] instanceof List || anArgs[i] instanceof BaseRemoteEntity) == false) {
                useMap = true;
            }
        }
        if (useMap) {
            return new Object[] { map };
        }
        else {
            return anArgs;
        }
    }

    public Object invoke(Object anProxy, Method anMethod, Object[] anArgs) throws Throwable {
        String methodName = anMethod.getName();
        anArgs = prepareParam(anMethod, anArgs);
        ParamValueHelper.init(this.faceInfo, this.faceInfo.findFunction(methodName), anArgs[0]);
        Class<?>[] params = anMethod.getParameterTypes();

        // equals and hashCode are special cased
        if (methodName.equals("equals") && params.length == 1 && params[0].equals(Object.class)) {
            Object value = anArgs[0];
            if (value == null || !Proxy.isProxyClass(value.getClass())) {
                return Boolean.FALSE;
            }

            Object proxyHandler = Proxy.getInvocationHandler(value);

            if (!(proxyHandler instanceof RemoteProxyService)) {
                return Boolean.FALSE;
            }

            return false;
        }
        else if (methodName.equals("hashCode") && params.length == 0) {
            return new Integer(this.hashCode());
        }
        else if (methodName.equals("getHessianType")) {
            return anProxy.getClass().getInterfaces()[0].getName();
        }
        else if (methodName.equals("toString") && params.length == 0) {
            return "RemoteProxyService ";
        }
        RemoteConnection conn = null;
        conn = sendRequest(anMethod, methodName, anArgs);
        RemoteResultInfo resultInfo = process(conn, methodName, anArgs);

        resultInfo = afterProcess(resultInfo, conn, methodName, anArgs);
        closeConn(conn);

        return resultInfo;
    }

    protected RemoteResultInfo afterProcess(RemoteResultInfo anResultInfo, RemoteConnection anConn, String anMethodName, Object[] anArgs) {

        return anResultInfo;
    }

    protected void closeConn(RemoteConnection conn) {
        if (conn != null) {
            conn.destroy();
        }
    }

    /**
     * Method that allows subclasses to add request headers. Default implementation is usually use header.
     */
    protected void addRequestHeaders(RemoteConnection conn) {
        // conn.addHeader("Content-Type",
        // this.faceInfo.getWorkFormat().createContentType());
        conn.addHeader("Accept-Encoding", "deflate");
    }

    /*
     * 用于子类发送数据时对数据做预处理和检查
     */
    protected Object[] prepareSendData(String methodName, Map<String, RemoteFieldInfo> anHeaderInfo, Object[] args) {

        return args;
    }

    /*
     * 用于子类发送数据时对数据做预处理和检查；并产生需要的数据
     */
    protected Map<String, Object> prepareSendMap(WorkFarFunction anFunc, Map<String, RemoteFieldInfo> anHeaderInfo,
            Map<String, RemoteFieldInfo> sendParams, RemoteConnection conn) {
        RemoteBaseSerializer xmlSerializer = anFunc.getRemoteSerializer();
        xmlSerializer.init(anFunc, anHeaderInfo, sendParams, this.faceInfo.getConfig(), this.faceInfo.getKeyManager(), conn);

        return xmlSerializer.readResult();
    }

    /**
     * 允许自己 定义Head的值，主要的方式：属性@范围（Face,Func, Config, outConver, inConver, Class, SelectKey对于 有Map的处理，增加一个@来处理；如果无参数，则是默认值，如果没有取到值，则为空）
     * 
     * @param anFunc
     *            当前使用的功能
     * @param args
     *            当前参数信息
     * @return 通过Clone获得的修正过的头信息
     */
    protected Map<String, RemoteFieldInfo> prepareHeadMap(WorkFarFunction anFunc, Object[] args) {
        Map<String, RemoteFieldInfo> headMap = ParamValueHelper.prepareHeadValues(this.faceInfo.cloneHeadMap());

        return headMap;
    }

    private static Class findReturnClass(Object anParam) {
        if (anParam instanceof Class) {
            return (Class) anParam;
        }
        else if (anParam instanceof ParameterizedType) {
            ParameterizedType param = (ParameterizedType) anParam;
            Type tt = param.getRawType();
            if (tt instanceof Class && (Map.class.isAssignableFrom((Class) tt))) {
                return (Class) tt;
            }

            if (param.getActualTypeArguments().length > 0) {
                Type txt = param.getActualTypeArguments()[0];
                if (txt instanceof ParameterizedType) {
                    return findReturnClass((ParameterizedType) txt);
                }
                else if (txt instanceof Class) {
                    return (Class) txt;
                }
            }
        }

        return null;
    }

    /**
     * Sends the HTTP request to the RemoteConnection connection.
     */
    protected RemoteConnection sendRequest(Method anMethod, String methodName, Object[] args) throws IOException {
        RemoteConnection conn = null;
        WorkFarFunction func = this.faceInfo.findFunction(methodName);
        if (func == null) {
            logger.warn("request function not fund, must declare! " + methodName);
            throw new BytterDeclareException(23456, "request function not fund, must declare! " + methodName);
        }
        // ParameterizedType tt = ;
        Class cc = findReturnClass(anMethod.getGenericReturnType());
        func.setWorkReturnClass(cc);

        conn = this.proxyFactory.createConnFactory(this.faceInfo.getFaceNo()).create(func, args);
        boolean isValid = false;
        try {
            Map<String, Object> queryParams = new LinkedHashMap();
            Map<String, Object> formParams;
            if (func.isHttp()) {
                conn.createMethod(func.getMethod());

                addRequestHeaders(conn);
                Map<String, RemoteFieldInfo> headMap = prepareHeadMap(func, args);

                DataConverterService converter = new DataConverterService(this.faceInfo.getOutConverMap());
                args = prepareSendData(methodName, headMap, args);
                BetterjrBaseOutputHelper outputHelper;
                Map<String, RemoteFieldInfo> paramList;
                if (args[0] instanceof Collection || args[0].getClass().isArray()) {
                    Collection dataList;
                    if (args[0] instanceof Collection) {
                        dataList = (Collection) args[0];
                    }
                    else {
                        dataList = Collections3.arrayToList(args[0]);
                    }
                    List<Map> result = new ArrayList();
                    for (Object obj : dataList) {
                        outputHelper = new BetterjrBaseOutputHelper(obj, converter);
                        result.add(outputHelper.outPutData(func, this.faceInfo.getMustItem() == Boolean.TRUE));
                    }
                    paramList = new HashMap();
                    RemoteDataFieldInfo fieldInfo = new RemoteDataFieldInfo("listData", result, "Data", FaceDataStyle.DATA);
                    paramList.put("listData", fieldInfo);
                }
                else {
                    outputHelper = new BetterjrBaseOutputHelper(args[0], converter);
                    paramList = outputHelper.outPutData(func, this.faceInfo.getMustItem() == Boolean.TRUE);
                }
                formParams = prepareSendMap(func, headMap, paramList, conn);
            }
            else {
                if (args[0] instanceof Map) {
                    formParams = (Map) args[0];
                }
                else {
                    formParams = new LinkedHashMap();
                    formParams.put("data", args[0]);
                }
            }
            conn.sendRequest(queryParams, formParams);

            isValid = true;

            return conn;
        }
        catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }
        finally {
            if (!isValid && conn != null) conn.destroy();
        }
    }

    public String process(Map<String, String> formPara) {
        try {
            WebServiceErrorCode resultCode = WebServiceErrorCode.E0000;
            Object result = null;
            WSInfo response = null;
            RemoteDeSerializer deSerializer = this.faceInfo.getDeSerializer();
            deSerializer.initParameter(this.faceInfo.getConfig(), this.faceInfo.getKeyManager());
            response = deSerializer.readInput(formPara, this.faceInfo);
            WorkFarFunction func = null;
            if (response.isSucess()) {
                func = this.faceInfo.findFuncWithFace(response.getOpType());
                String workExp = func.buildExeExp();
                try {
                    List errList = new ArrayList();
                    result = QlExpressUtil.invoke(workExp, func.getInputMode(), response.getInput(), errList);
                    logger.debug("expression result:" + result);
                }
                catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause != null && cause instanceof BytterWebServiceException) {
                       // logger.error("WebServiceException ", cause);
                        resultCode = ((BytterWebServiceException) cause).getErrorCode();
                    }
                    else {
                        logger.error("QlExpress error ", e);
                        resultCode = WebServiceErrorCode.E9999;
                    }
                }
                catch (Exception e) {
                    logger.error("QlExpress error ", e);
                    resultCode = WebServiceErrorCode.E9999;
                }
            }
            else {
                result = "";
                resultCode = response.getErrorCode();
                func = this.faceInfo.findFistFunction();
            }
            RemoteSerializer serializer = this.faceInfo.getRemoteSerializer();
            serializer.initParameter(this.faceInfo.getConfig(), this.faceInfo.getKeyManager());
            serializer.writeOutput(func, response, result, resultCode);
            return response.getData();
        }
        catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
        return "";
    }

    /**
     * 签名文件
     * @param anFileItem 文件信息
     * @return
     */
    public String signFile(String filePath) {
        String basePath = (String) SysConfigService.getObject(ParamNames.OPENACCO_FILE_DOWNLOAD_PATH);
        String path = basePath + filePath;
        String msg = null;
        File file = FileUtils.getRealFile(path);
        if (file != null) {
            msg = signFileWithAbsoPath(file);
        }
        else {
            msg = "file document not exists!";
        }
        
        return msg;
    }

    /**
     * 对绝对路径的文件签名
     * @param anFile 具体的文件。
     * @return
     */
    public String signFileWithAbsoPath(File anFile) {
        
        return SignHelper.signFile(anFile, faceInfo.getKeyManager().getPrivKey());
    }
    
    public void initFaceInfo(String face){
        Map<String, WorkFarFaceInfo> mapFaceInfo=this.proxyFactory.getFarMapInfo();
        this.faceInfo=mapFaceInfo.get(face);
    }
}
