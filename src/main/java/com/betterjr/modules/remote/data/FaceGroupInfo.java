package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;

public enum FaceGroupInfo {
    FUND, BANK, TICKET, P2P;
    public static FaceGroupInfo checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return FaceGroupInfo.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }
}
