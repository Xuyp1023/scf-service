package com.betterjr.modules.remote.utils;

import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;

/**
 * 自定义的数据转换
 * 
 * @author zhoucy
 *
 */
public interface RemoteBaseConverter {

    /**
     * 
     * 数据转换到相应的结果
     * 
     * @param 接口字段名称
     * @param1 需要转换的数据
     * @return 转换后需要的结果
     * @throws 异常情况
     */
    public String convert(WorkFaceFieldInfo fieldInfo, String anPropName, String anValue);

}
