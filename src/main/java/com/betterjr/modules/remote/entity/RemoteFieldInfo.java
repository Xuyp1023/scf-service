package com.betterjr.modules.remote.entity;

import com.betterjr.modules.remote.data.FaceDataStyle;

/**
 * 数据处理的接口，用于定义数据的样式！
 * 
 * @author henry
 *
 */
public interface RemoteFieldInfo extends java.io.Serializable {

    public String getName();

    public Object getValue();

    public boolean custStyle();

    public String getWorkStyle();

    public FaceDataStyle getDataStyle();
}
