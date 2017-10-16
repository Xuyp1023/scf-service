package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;
import java.util.*;

public enum DataFormatInfo {
    JSON, XML, HX_XML, FIX, TEXT, FORM, MULTIPART, STREAM;
    private static Map<DataFormatInfo, String> map = new HashMap();

    static {
        map.put(JSON, "applicaton/json");
        map.put(XML, "applicaton/xml");
        map.put(TEXT, "text/plain");
        map.put(FORM, "application/x-www-form-urlencoded");
        map.put(MULTIPART, "multipart/form-data");
        map.put(STREAM, "application/octet-stream");
        map.put(FIX, "text/plain");
        map.put(HX_XML, "text/xml;charset=UTF-8");
    }

    public static DataFormatInfo checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return DataFormatInfo.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return XML;
    }

    public String createContentType() {
        return map.get(this);
    }
}
