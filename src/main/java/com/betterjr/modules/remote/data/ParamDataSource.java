package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;

public enum ParamDataSource {
    FACE, FUNC, CONFIG, OUTCONVER, INCONVER, CLASS, SELECTKEY, DICT, CUSTOM;
    public static ParamDataSource checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return ParamDataSource.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }
}
