package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;
import java.util.*;

public enum DataOutModeStyle {
    JSON, XML, XML_JSONLIST, XML_JSONMAP;
    private static Map<DataOutModeStyle, String> map = new HashMap();

    public static DataOutModeStyle checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return DataOutModeStyle.valueOf(anWorkType.trim().toUpperCase());
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
