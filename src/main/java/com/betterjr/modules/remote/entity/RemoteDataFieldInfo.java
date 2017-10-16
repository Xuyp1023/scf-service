package com.betterjr.modules.remote.entity;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.modules.remote.data.FaceDataStyle;

public class RemoteDataFieldInfo implements RemoteFieldInfo {

    private static final long serialVersionUID = 7567079231695537428L;

    private final String name;
    private final Object value;
    private final String userStyle;
    private final FaceDataStyle dataStyle;

    public RemoteDataFieldInfo(String anName, Object anValue, String anUserStyle, FaceDataStyle anDataStyle) {
        this.name = anName;
        this.value = anValue;
        this.userStyle = anUserStyle;
        this.dataStyle = anDataStyle;
    }

    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public Object getValue() {

        return this.value;
    }

    @Override
    public boolean custStyle() {

        return StringUtils.isNotBlank(this.userStyle);
    }

    @Override
    public FaceDataStyle getDataStyle() {

        return dataStyle;
    }

    @Override
    public String getWorkStyle() {

        return userStyle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append(", userStyle=").append(userStyle);
        sb.append(", dataStyle=").append(dataStyle);
        sb.append("]");

        return sb.toString();
    }

}
