package com.betterjr.modules.remote.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.remote.dao.FarInfterfaceInfoMapper;
import com.betterjr.modules.remote.entity.*;

@Service
public class FarFaceService extends BaseService<FarInfterfaceInfoMapper, FarInfterfaceInfo> {
    private static final Logger logger = LoggerFactory.getLogger(FarFaceService.class);

    /**
     * 
     * 获取接口信息
     * 
     * @param 入参说明及前提条件
     * @return 所有接口的完整参数
     * @throws 异常情况
     */
    public Map<String, WorkFarFaceInfo> findFarFaceList() {
        Map<String, WorkFarFaceInfo> faceMap = new HashMap();
        String faceNo = null;
        // for (FarInfterfaceInfo faceInfo : this.selectByProperty("faceGroup", "FUND")) {
        // 暂时只支持供应链金融，基金业务分布式后再取全部数据 by zhoucy 20161025
        for (FarInfterfaceInfo faceInfo : this.selectByProperty("faceGroup", "SCF")) {
            faceNo = faceInfo.getFaceNo();
            Map<String, FaceFieldDictInfo> fieldMap = findFaceDict(faceNo);
            Map<String, WorkFarFunction> funMap = findFarFunList(faceNo, fieldMap);
            WorkFarFaceInfo farFaceInfo = new WorkFarFaceInfo(faceInfo, funMap, findConfigInfo(faceNo),
                    findConverInfo(faceNo, "1"), findConverInfo(faceNo, "2"), findHeadInfo(faceNo));

            faceMap.put(faceInfo.getFaceNo(), farFaceInfo);
        }

        return faceMap;
    }

    /**
     * 
     * 接口的具体功能信息
     * 
     * @param 接口简称
     * @param1 接口对应的数据字典
     * @return 功能涉及到的全部信息
     * @throws 异常情况
     */
    public Map<String, WorkFarFunction> findFarFunList(String anFaceNo, Map<String, FaceFieldDictInfo> anFieldMap) {
        Map<String, WorkFarFunction> mapFun = new LinkedCaseInsensitiveMap();
        for (FarFunctionInfo funcInfo : mapper.findFunctionByFaceNo(anFaceNo)) {
            List fieldMap = findFieldMap(anFaceNo, funcInfo.getFunCode(), anFieldMap);
            logger.info("work funcInfo :" + funcInfo);
            mapFun.put(funcInfo.getFunBeanName(), new WorkFarFunction(funcInfo, fieldMap));
        }

        return mapFun;
    }

    /**
     * 
     * 功能每个数据项的映射关系
     * 
     * @param 接口简称
     * @param1 功能编码
     * @param2 数据 字典
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public List<WorkFaceFieldInfo> findFieldMap(String anFaceNo, String anFunNo,
            Map<String, FaceFieldDictInfo> anFieldMap) {
        List<WorkFaceFieldInfo> fieldMap = new LinkedList();
        FaceFieldDictInfo dictField;
        for (FarFieldMapInfo fieldInfo : mapper.findFieldMapByFaceNo(anFaceNo, anFunNo)) {
            dictField = anFieldMap.get(fieldInfo.getFaceField());
            if (dictField == null && fieldInfo.getFieldOrder() > -1) {
                logger.warn("work func :" + anFaceNo + "." + anFunNo + ", field :" + fieldInfo.getFaceField() + " = "
                        + fieldInfo.getBeanField() + ", not find in T_FACE_FIELDDICT ");
            } else {
                if (fieldInfo.getFieldOrder() < 0 && dictField == null) {
                    dictField = new FaceFieldDictInfo(fieldInfo);
                }
                WorkFaceFieldInfo workField = new WorkFaceFieldInfo(fieldInfo, dictField);
                fieldMap.add(workField);
            }
        }

        return fieldMap;
    }

    /**
     * 
     * 根据接口获取全部数据字典信息
     * 
     * @param 接口简称
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public Map<String, FaceFieldDictInfo> findFaceDict(String anFaceNo) {
        Map<String, FaceFieldDictInfo> fieldMap = new LinkedCaseInsensitiveMap();
        for (FaceFieldDictInfo fieldDict : mapper.findFaceDictByFaceNo(anFaceNo)) {
            fieldMap.put(fieldDict.getColumnName(), fieldDict);
        }

        return fieldMap;
    }

    /**
     * 根據接口獲取接口的配置參數信息
     */
    public Map<String, FarConfigInfo> findConfigInfo(String anFaceNo) {
        Map<String, FarConfigInfo> configMap = new LinkedCaseInsensitiveMap();
        for (FarConfigInfo configInfo : this.mapper.findConfigByFaceNo(anFaceNo)) {
            configMap.put(configInfo.getItemName(), configInfo);
        }

        return configMap;
    }

    /**
     * 根據接口獲取接口的配置參數信息
     */
    public Map<String, FaceHeaderInfo> findHeadInfo(String anFaceNo) {
        Map<String, FaceHeaderInfo> headMap = new LinkedCaseInsensitiveMap();
        for (FaceHeaderInfo headInfo : this.mapper.findFaceHeadByFaceNo(anFaceNo)) {
            headMap.put(headInfo.getColumnName(), headInfo);
        }

        return headMap;
    }

    /**
     * 获取接口定义的返回码信息
     */
    public Map<String, FaceReturnCode> findReturnCodeByFaceMode(String anModeNo) {
        Map<String, FaceReturnCode> headMap = new LinkedCaseInsensitiveMap();
        for (FaceReturnCode headInfo : this.mapper.findReturnCodeByFaceMode(anModeNo)) {
            headMap.put(headInfo.getReturnCode(), headInfo);
        }

        return headMap;
    }

    /**
     * 获取接口定义的返回码信息
     */
    public Map<String, Map<String, FaceReturnCode>> findAllReturnCode() {
        Map<String, Map<String, FaceReturnCode>> returnMap = new LinkedCaseInsensitiveMap();
        for (FaceReturnCode returnInfo : this.mapper.findAllReturnCode()) {
            Map<String, FaceReturnCode> headMap = returnMap.get(returnInfo.getModeNo());
            if (headMap == null) {
                headMap = new LinkedCaseInsensitiveMap();
                returnMap.put(returnInfo.getModeNo(), headMap);
            }

            headMap.put(returnInfo.getReturnCode(), returnInfo);
        }

        return returnMap;
    }

    /**
     * 获取接口转换信息
     * 
     * @param 接口简称
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public Map<String, FaceConvertInfo> findConverInfo(String anFaceNo, String anInutDirect) {

        Map<String, FaceConvertInfo> converMap = new LinkedCaseInsensitiveMap();
        for (FaceConvertInfo faceC : this.mapper.findFaceConvertByFaceNo(anFaceNo, "0")) {
            faceC.swapSource(anInutDirect);
            faceC.mergeConvert(converMap);
        }

        for (FaceConvertInfo faceC : this.mapper.findFaceConvertByFaceNo(anFaceNo, anInutDirect)) {

            faceC.mergeConvert(converMap);
        }

        return converMap;
    }
}