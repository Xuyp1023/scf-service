package com.betterjr.modules.remote.entity;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.modules.remote.data.FaceDataStyle;

public class WorkFaceFieldInfo extends FarFieldMapInfo {
    private static final long serialVersionUID = -4756273300648697457L;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private FaceFieldDictInfo fieldDictInfo;

    private DataTypeInfo typeInfo = null;
    private WorkFarFunction workFun;

    private FaceDataStyle dataStyle;

    public FaceFieldDictInfo getFieldDictInfo() {
        return this.fieldDictInfo;
    }
    public void setFieldDictInfo(FaceFieldDictInfo anFieldDictInfo) {
        this.fieldDictInfo = anFieldDictInfo;
    }
    private Class fieldType;
    
    public void setFieldType(Class anFieldType) {
        fieldType = anFieldType;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public FaceDataStyle getDataStyle() {
        return dataStyle;
    }

    public WorkFarFunction getWorkFun() {
        return workFun;
    }

    public DataTypeInfo getTypeInfo() {
        return typeInfo;
    }

    public RemoteDataFieldInfo createDataField(Object anValue) {
        RemoteDataFieldInfo fieldInfo = new RemoteDataFieldInfo(this.getFaceField(), anValue, this.getWorkLevel(), this.dataStyle);

        return fieldInfo;
    }

    public WorkFaceFieldInfo(FarFieldMapInfo anFieldMap, FaceFieldDictInfo anFieldDictInfo) {
        BeanMapper.copy(anFieldMap, this);

        this.fieldDictInfo = anFieldDictInfo;
    }

    public void init(WorkFarFunction anFunction) {
        this.workFun = anFunction;
        dataStyle = FaceDataStyle.checking(this.getWorkLevel(), this.workFun.getWorkFace().getDefStyle());
        // 初始化 字段 类型，如果找不到，抛出异常
        typeInfo = DataTypeInfo.checking(this.fieldDictInfo.getDataType());
        if (typeInfo == null) {
            throw new BytterValidException(25060, "remote invoke field type not find please checking:" + anFunction.getFunBeanName() + "."
                    + this.fieldDictInfo.getColumnName());
        }

    }

    /**
     * 
     * 校验数据项的有效性
     * 
     * @param 数据项内容
     * @param1 功能信息
     * @return 校验结果，如果数据类型和必填项不一致，则返回False，其他返回True；如果数据类型不一致，则抛出异常
     * @throws 异常情况
     */
    public boolean verify(Object anObj, WorkFarFunction anFunction) {
        if (anObj == null) {
            if (this.getMustItem() != null && this.getMustItem()) {
                logger.warn("remote invoke field must not null :" + anFunction.getFunBeanName() + "." + this.getBeanField());
            }

            return true;
        }

        return this.typeInfo.validData(anObj);
    }

    public String getValue(Object anObj) {
        Integer ddx = this.fieldDictInfo.getDataScale();
        int x;
        if (ddx == null) {
            x = 0;
        }
        else {
            x = ddx.intValue();
        }
        
        return DataTypeInfo.formatData(anObj, x);
    }

    public WorkFaceFieldInfo() {
    }
    public WorkFaceFieldInfo(WorkFaceFieldInfo anOther) {
        BeanMapper.copy(anOther, this);
    }
    public WorkFaceFieldInfo(int anIndex, Field anField, WorkFarFunction anFunc) {
        this.setFieldOrder(anIndex);
        this.setFaceNo(anFunc.getFaceNo());
        this.setFunCode(anFunc.getFunCode());
        this.setValueMode("0");
        this.setBeanField(anField.getName());
        this.setFaceField(anField.getName());
        this.workFun = anFunc;
        this.setDirection("2");
        this.setMustItem(Boolean.FALSE);
        this.dataStyle = FaceDataStyle.checking(this.getWorkLevel(), this.workFun.getWorkFace().getDefStyle());
        typeInfo = DataTypeInfo.findDataType(anField.getDeclaringClass());
        if (typeInfo == null) {
            throw new BytterValidException(25061, "remote invoke field type not find please checking:" + anFunc.getFunBeanName() + "."
                    + anField.getName());
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("  ").append(fieldDictInfo);

        return sb.toString();
    }
}
