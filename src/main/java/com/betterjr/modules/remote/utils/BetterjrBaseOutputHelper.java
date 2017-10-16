package com.betterjr.modules.remote.utils;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.data.AttachDataFace;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.entity.RemoteFieldInfo;
import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.remote.helper.DataConverterService;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.service.QlExpressUtil;

public class BetterjrBaseOutputHelper {

    private final Object objBig;
    private final DataConverterService convertService;

    public BetterjrBaseOutputHelper(Object anObj, DataConverterService anConvert) {
        this.objBig = anObj;
        this.convertService = anConvert;
    }

    public static Object defConvertValue(String valueMode, Map dataMap, String anField, Object anDataObj,
            String anWorkExp) {
        char valueChar = '0';
        Object obj = null;

        if (StringUtils.isNotBlank(anWorkExp)) {
            anField = anWorkExp;
        }
        if (StringUtils.isNotBlank(valueMode)) {
            valueChar = valueMode.trim().charAt(0);
        }
        if ('0' == valueChar) {
            obj = dataMap.get(anField);
        } else if ('1' == valueChar) {
            obj = anField;
        } else if ('2' == valueChar) {
            obj = ParamValueHelper.getValue(anField);
        } else if ('3' == valueChar) {
            RuleContext ruleC = RuleContext.create("obj", anDataObj);
            try {
                obj = QlExpressUtil.simpleInvoke(anField, ruleC);
            }
            catch (Exception e) {
                throw new BytterValidException(58971, "BetterjrBaseOutputHelper use QlExpressUtil.getRunner has error",
                        e);
            }
        }
        return obj;
    }

    public Map<String, RemoteFieldInfo> outPutData(WorkFarFunction anFunction, boolean anMust) {
        Map<String, RemoteFieldInfo> map = new LinkedHashMap();
        Map<String, Object> dataMap = null;
        if (objBig instanceof Map) {

            dataMap = (Map) objBig;
        } else if (objBig instanceof BetterjrEntity || objBig instanceof BaseRemoteEntity) {

            dataMap = BeanMapper.map(objBig, Map.class);
        } else {

            throw new BytterException(205068,
                    "BetterjrBaseOutputHelper only support javaBean and Map Class, Please Check");
        }
        if (objBig instanceof AttachDataFace) {
            Map tmpAttach = ((AttachDataFace) objBig).getAttach();
            if (tmpAttach != null) {

                dataMap.putAll(tmpAttach);
            }
        }

        String tmpStr = null;
        for (Map.Entry<String, WorkFaceFieldInfo> ent : anFunction.getFieldMap().entrySet()) {
            WorkFaceFieldInfo fieldInfo = ent.getValue();
            Object obj = defConvertValue(fieldInfo.getValueMode(), dataMap, fieldInfo.getBeanField(), this.objBig,
                    fieldInfo.getWorkExpress());
            if (fieldInfo.verify(obj, anFunction)) {
                if (DataTypeInfo.simpleObject(obj)) {
                    tmpStr = fieldInfo.getValue(obj);
                    tmpStr = this.convertService.convert(fieldInfo, fieldInfo.getFaceField(), tmpStr);
                    if (StringUtils.isBlank(tmpStr)) {
                        if (anMust) {
                            if ((fieldInfo.getFieldType() != null)
                                    && Number.class.isAssignableFrom(fieldInfo.getFieldType())) {
                                tmpStr = "0";
                            } else {
                                tmpStr = " ";
                            }
                        } else {
                            continue;
                        }
                    }
                    obj = tmpStr;
                }
                map.put(fieldInfo.getFaceField(), fieldInfo.createDataField(obj));
            }
        }

        return map;
    }

}
