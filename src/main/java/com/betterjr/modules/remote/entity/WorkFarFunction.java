package com.betterjr.modules.remote.entity;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.exception.BetterjrClientProtocolException;
import com.betterjr.common.exception.BytterClassNotFoundException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterClassUtils;
import com.betterjr.mapper.mapperhelper.EntityHelper;
import com.betterjr.modules.remote.data.RemoteInvokeMode;
import com.betterjr.modules.remote.data.WorkMethodInfo;
import com.betterjr.modules.remote.serializer.RemoteBaseSerializer;
import com.betterjr.modules.remote.serializer.RemoteDeSerializer;
import com.betterjr.modules.sys.service.SysConfigService;

/**
 * 定义功能信息，功能定义的各参数都会在事先定义完毕后使用
 * 
 * @author henry
 *
 */
public class WorkFarFunction extends FarFunctionInfo {

    private static final long serialVersionUID = -53052510005258529L;
    private final Map<String, WorkFaceFieldInfo> fieldMap = new LinkedHashMap();
    private final Map<String, WorkFaceFieldInfo> propMap = new LinkedHashMap();
    private WorkMethodInfo method = WorkMethodInfo.POST;
    private String workUrl;
    private WorkFarFaceInfo workFace;
    private Class workReturnClass;
    private Class deSerializerClass = null;
    private Class serializerClass = null;

    public void setWorkReturnClass(Class anWorkReturnClass) {
        initReturnClass(anWorkReturnClass);
    }

    public RemoteBaseSerializer getRemoteSerializer() {
        if (this.serializerClass == null) {

            return this.workFace.getRemoteSerializer();
        }
        else {
            try {
                return (RemoteBaseSerializer) this.serializerClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e) {

                throw new BytterClassNotFoundException(123465, "getSerializer class not find", e);
            }
        }
    }


    public String buildExeExp(){
        StringBuilder sb = new StringBuilder();
        sb.append( this.getFunBeanName());
        //表示基本类型参数（>=1个参数，只支持基本类型），需从FieldMap中获得需要的字段信息
        if ("3".equals(this.getInputMode())){
            sb.append("(").append(this.findOrderParamStr()).append(")");
        }//表示没有参数
        else if ("2".equals(this.getInputMode())){
            sb.append("()");
        }
        else{//单个参数(bean 或者 map，或者List<Bean>,List<Map>)
           sb.append("(inputParam)"); 
        }
        
        return sb.toString();
    }
    
    
    private String findOrderParamStr(){
       StringBuilder sb = new StringBuilder();
       for (String tmpKey : this.propMap.keySet()){
          sb.append(tmpKey).append(", "); 
       }
       sb.setLength(sb.length() -2);
       
       return sb.toString();
    }
    
    public String findSingleFieldFaceId(){
        Iterator<String> it=this.fieldMap.keySet().iterator();
        if(it.hasNext()){
            return it.next();
        }else{
            return null;
        }
    }
    
    public RemoteDeSerializer getDeSerializer() {
        if (this.deSerializerClass == null) {
            return this.workFace.getDeSerializer();
        }
        else {
            try {
                return (RemoteDeSerializer) this.deSerializerClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e) {

                throw new BytterClassNotFoundException(123465, "RemoteDeSerializer class not find", e);
            }
        }
    }

    public Class getWorkReturnClass() {
        return workReturnClass;

    }

    public String getReturnMsg(String anReturnCode) {
        FaceReturnCode returnCode = this.workFace.findReturnCode(anReturnCode);
        if (returnCode == null) {
            return "";
        }
        else {
            return returnCode.getReturnName();
        }
    }

    public boolean getReturnState(String anReturnCode) {
        FaceReturnCode returnCode = this.workFace.findReturnCode(anReturnCode);
        if (returnCode == null) {
            return false;
        }
        else {
            return returnCode.getStatus();
        }
    }

    public WorkFarFaceInfo getWorkFace() {
        return workFace;
    }

    public String getWorkUrl() {
        return workUrl;
    }

    public WorkMethodInfo getMethod() {
        return method;
    }

    public Map<String, WorkFaceFieldInfo> getFieldMap() {
        return fieldMap;
    }

    public Map<String, WorkFaceFieldInfo> getPropMap() {
        return propMap;
    }

    public WorkFarFunction(FarFunctionInfo anFuncInfo, List<WorkFaceFieldInfo> anFieldMap) {
        BeanMapper.copy(anFuncInfo, this);

        for (WorkFaceFieldInfo fieldInfo : anFieldMap) {
            if ("0".equalsIgnoreCase(fieldInfo.getDirection())) {
                this.propMap.put(fieldInfo.getBeanField(), fieldInfo);
                this.fieldMap.put(fieldInfo.getFaceField(), new WorkFaceFieldInfo(fieldInfo));
            }
            else if ("1".equalsIgnoreCase(fieldInfo.getDirection())) {
                this.fieldMap.put(fieldInfo.getFaceField(), fieldInfo);
            }
            else if ("2".equalsIgnoreCase(fieldInfo.getDirection())) {
                this.propMap.put(fieldInfo.getBeanField(), fieldInfo);
            }
        }
    }

    public boolean isHttp() {
        RemoteInvokeMode tmpMode = RemoteInvokeMode.checking(this.getInvokeMode());
        return (tmpMode == RemoteInvokeMode.HTTP) || (tmpMode == RemoteInvokeMode.HTTP_MUILT);
    }

    private void initReturnClass(Class anClass) {
        if (anClass == null) {
            return;
        }
        if (this.workReturnClass == null) {
            if (Map.class.isAssignableFrom(anClass) || Collection.class.isAssignableFrom(anClass)) {

            }
            else {
                List<Field> fields = EntityHelper.getAllField(anClass);
                WorkFaceFieldInfo fieldInfo;
                for (Field ff : fields) {
                    fieldInfo = this.propMap.get(ff.getName());
                    if (fieldInfo != null) {
                        fieldInfo.setFieldType(ff.getType());
                    }
                }
            }
            this.workReturnClass = anClass;
    

        }
    }

    public void init(WorkFarFaceInfo anFaceInfo) {
        this.workFace = anFaceInfo;
        String tmpUrl = this.getUrl();

        // 为URL请求增加请求方式，POST，GET等
        if (StringUtils.isNotBlank(tmpUrl)) {
            String[] args = tmpUrl.split(",");
            WorkMethodInfo tmpM = WorkMethodInfo.checking(args[0]);
            if (tmpM != null) {
                this.method = tmpM;
            }
            if (args.length > 1) {
                int pos = tmpUrl.indexOf(",");
                this.setUrl(tmpUrl.substring(pos + 1));
            }
        }
        tmpUrl = this.getUrl();
        if (StringUtils.isNotBlank(tmpUrl)) {
            this.workUrl = anFaceInfo.getUrl().concat(tmpUrl);
        }
        else {
            this.workUrl = anFaceInfo.getUrl();
        }

        // 初始化 字段信息
        for (Map.Entry<String, WorkFaceFieldInfo> ent : fieldMap.entrySet()) {
            ent.getValue().init(this);
        }
        
        initReturnClass(BetterClassUtils.findClassByName(this.getReturnClass(), SysConfigService.getList("ClassRecursivePathList")));
        
        try {
            this.deSerializerClass = WorkFarFaceInfo.findDeclareClass(this.getDataDeSerializer());
            this.serializerClass = WorkFarFaceInfo.findDeclareClass(this.getDataSerializer());
        }
        catch (ClassNotFoundException e) {
            throw new BetterjrClientProtocolException(25201, "WorkFarFunction.init Serializer Class NotFund : ", e);
        }
        /*
         * // 初始化输入和输出类信息 String packPath = BaseRemoteEntity.class.getPackage().getName().concat("."); String tmpClassName = this.getReturnClass();
         * try { if (StringUtils.isNotBlank(tmpClassName)) { if (XmlUtils.split(tmpClassName, ".").size() == 1) { tmpClassName =
         * packPath.concat(tmpClassName); } initReturnClass(Class.forName(tmpClassName)); } this.deSerializerClass =
         * WorkFarFaceInfo.findDeclareClass(this.getDataDeSerializer()); this.serializerClass =
         * WorkFarFaceInfo.findDeclareClass(this.getDataSerializer()); } catch (ClassNotFoundException e) { throw new
         * BetterjrClientProtocolException(25201, "WorkFarFunction.init Return Class NotFund : " + tmpClassName, e); }
         */
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\r\n");

        sb.append("this is WorkFaceFieldInfo \r\n");
        for (WorkFaceFieldInfo obj : (Collection<WorkFaceFieldInfo>) (fieldMap.values())) {
            sb.append(obj).append("\r\n");
        }

        return sb.toString();
    }
}
