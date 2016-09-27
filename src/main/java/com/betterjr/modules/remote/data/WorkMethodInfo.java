package com.betterjr.modules.remote.data;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.*;

public enum WorkMethodInfo {
    POST, GET, HEAD, PUT, DELETE;
    private static Map<WorkMethodInfo, Class> map = new HashMap();

    static {
        map.put(POST, HttpPost.class);
        map.put(GET, HttpGet.class);
        map.put(HEAD, HttpHead.class);
        map.put(PUT, HttpPut.class);
        map.put(DELETE, HttpDelete.class);
    }

    public HttpRequestBase createMethod() {
        Class cc = map.get(this);

        try {
            return (HttpRequestBase) cc.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WorkMethodInfo checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return WorkMethodInfo.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

}
