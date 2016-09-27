package com.betterjr.modules.remote.helper;

import java.util.Map;

import com.betterjr.modules.remote.entity.FaceConvertInfo;
import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;

public class DataConverterService {
    private final Map<String, FaceConvertInfo> convertMap;

    public DataConverterService(Map<String, FaceConvertInfo> anConvert) {
        this.convertMap = anConvert;
    }
    
    public String convert(WorkFaceFieldInfo fieldInfo, String anFaceFieldName, String anValue){
        FaceConvertInfo convert = convertMap.get(anFaceFieldName);
        if (convert == null){
            return anValue;
        }
        else{
            return convert.convert(fieldInfo, anFaceFieldName, anValue);
        }
    }
}
