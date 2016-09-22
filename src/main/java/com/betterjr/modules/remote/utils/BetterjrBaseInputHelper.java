package com.betterjr.modules.remote.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;
import com.betterjr.modules.remote.helper.DataConverterService;


/**
 * 将远程返回的结构化数据序列化为一个对象
 * 
 * @author zhoucy
 *
 */
public class BetterjrBaseInputHelper {
    private Map<String, Object> map;
    private final DataConverterService convertService;
    private static final Logger logger = LoggerFactory.getLogger(BetterjrBaseInputHelper.class);

    public BetterjrBaseInputHelper(Map<String, Object> anMap, DataConverterService anConvert) {
        this.map = anMap;
        this.convertService = anConvert;
    }

    private <T> T readValue(WorkFaceFieldInfo fieldInfo, String anKey, Class<T> anClass) {
        if (anClass == null) {
            logger.warn("field " + fieldInfo + ", don't declare class");
            anClass = DataTypeInfo.getClass(fieldInfo.getFieldDictInfo().getDataType());
            if (anClass == null) {
                logger.warn("field " + fieldInfo + ", don't find declare class");
                return null;
            }
            else{
                logger.warn("this null field type is " + anClass);
            }
        }
        // String valueMode = fieldInfo.getValueMode();
        // char valueChar = '0';
        Object obj = BetterjrBaseOutputHelper.defConvertValue(fieldInfo.getValueMode(), this.map, fieldInfo.getFaceField(), this.map,
                fieldInfo.getWorkExpress());
        if (DataTypeInfo.simpleObject(obj) == false){
        
            return (T)obj;
        }
        
        String tmpStr;
        if (obj == null || "Void".equalsIgnoreCase(fieldInfo.getBeanField())) {

            tmpStr = "";
        }
        else {

            tmpStr = obj.toString();
        }
        tmpStr = convertService.convert(fieldInfo, anKey, tmpStr);

        if (StringUtils.isNotBlank(tmpStr)) {
            if (Date.class.isAssignableFrom(anClass)) {

                return readDate(fieldInfo, anKey, anClass);
            }
            else if (String.class.isAssignableFrom(anClass)) {

                return (T) tmpStr;
            }
            String mName = "parse" + anClass.getSimpleName();
            Method method;
            try {
                if (anClass == Integer.class) {
                    mName = "parseInt";
                }
                else if (anClass == BigDecimal.class) {

                    return (T) new BigDecimal(tmpStr);
                }
                method = anClass.getMethod(mName, String.class);
                return (T) method.invoke(null, tmpStr);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    private String readValue1(WorkFaceFieldInfo fieldInfo, String anKey) {
        // Object obj = this.map.get(anKey);
        Object obj = BetterjrBaseOutputHelper.defConvertValue(fieldInfo.getValueMode(), this.map, fieldInfo.getFaceField(), this.map,
                fieldInfo.getWorkExpress());
        String tmpStr;
        if (obj == null) {

            tmpStr = "";
        }
        else {

            tmpStr = obj.toString();
        }
        return convertService.convert(fieldInfo, anKey, tmpStr);
    }

    private Map<String, Object> workForOrignMap(Class anClass, Map<String, WorkFaceFieldInfo> anPropMap) {
        Map<String, Object> map = new HashMap();

        Object obj;
        List dataList = new LinkedList();
        for (WorkFaceFieldInfo fieldInfo : anPropMap.values()) {
            obj = readValue(fieldInfo, fieldInfo.getFaceField(), fieldInfo.getFieldType());
            if (obj != null) {
                if (Map.class.isAssignableFrom(anClass)) {
                    map.put(fieldInfo.getBeanField(), obj);
                }
                else {
                    dataList.add(obj);
                }
            }
        }
        if (Collection.class.isAssignableFrom(anClass)) {
            map.put("dataList", dataList);
        }

        return map;
    }

    private Map<String, Object> readData(Class anClass, Map<String, WorkFaceFieldInfo> anPropMap) {
        // 如果是Map 或者集合，特殊处理
        if (Map.class.isAssignableFrom(anClass) || Collection.class.isAssignableFrom(anClass)) {

            return workForOrignMap(anClass, anPropMap);
        }

        Map<String, Object> map = new HashMap();
        Object obj;
        for (WorkFaceFieldInfo fieldInfo : anPropMap.values()) {
            obj = readValue(fieldInfo, fieldInfo.getFaceField(), fieldInfo.getFieldType());
            if (obj != null) {
                map.put(fieldInfo.getBeanField(), obj);
            }
        }

        return map;
    }

    public Map<String, String> readData1(Class anClass, Map<String, WorkFaceFieldInfo> anPropMap) {

        Map<String, String> map = new HashMap();
        String obj;
        for (WorkFaceFieldInfo fieldInfo : anPropMap.values()) {
            obj = readValue1(fieldInfo, fieldInfo.getFaceField());
            if (BetterStringUtils.isNotBlank(obj)) {

                map.put(fieldInfo.getBeanField(), obj);
            }
        }
        return map;
    }

    public <T> T readObject(Class<T> anClass, Map<String, WorkFaceFieldInfo> anPropMap) {
        if (Map.class.isAssignableFrom(anClass) || Collection.class.isAssignableFrom(anClass)) {

            return (T) readData(anClass, anPropMap);
        }
        else if (BaseRemoteEntity.class.isAssignableFrom(anClass)) {

            return BeanMapper.map(readData(anClass, anPropMap), anClass);
        }

        throw new BytterException(205068, "BetterjrBaseInputHelper only support  javaBean and Map Class, Please Check");
    }

    public Boolean readBoolean(String anKey) {

        return readValue(null, anKey, Boolean.class);
    }

    public Byte readByte(String anKey) {

        return readValue(null, anKey, Byte.class);
    }

    public Short readShort(String anKey) {

        return readValue(null, anKey, Short.class);
    }

    public Integer readInteger(String anKey) {

        return readValue(null, anKey, Integer.class);
    }

    public Long readLong(String anKey) {
        return readValue(null, anKey, Long.class);
    }

    public Float readFloat(String anKey) {
        return readValue(null, anKey, Float.class);
    }

    public Double readDouble(String anKey) {
        return readValue(null, anKey, Double.class);
    }

    public String readString(String anKey) {
        String tmpStr = this.map.get(anKey).toString();
        return convertService.convert(null, anKey, tmpStr);
    }

    public <T> T readDate(WorkFaceFieldInfo fieldInfo, String anKey, Class<T> anClass) {
        java.util.Date dd = readDate(anKey);
        if (dd == null) {
            return null;
        }
        try {
            Constructor<T> cc = anClass.getConstructor(long.class);
            return cc.newInstance(dd.getTime());
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Date readDate(String anKey) {
        String tmpStr = this.map.get(anKey).toString();

        return BetterDateUtils.parseDate(convertService.convert(null, anKey, tmpStr));
    }

    public Character readCharacter(String anKey) {
        String tmpStr = this.map.get(anKey).toString();
        tmpStr = convertService.convert(null, anKey, tmpStr);
        if (StringUtils.isNotBlank(tmpStr)) {
            return tmpStr.charAt(0);
        }
        else {
            return null;
        }
    }
}
