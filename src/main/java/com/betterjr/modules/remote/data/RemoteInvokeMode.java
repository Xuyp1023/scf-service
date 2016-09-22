package com.betterjr.modules.remote.data;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.connection.LocateConnection;
import com.betterjr.modules.remote.connection.RemoteMultiPartConnection;

public enum RemoteInvokeMode {
    HTTP("0"), HTTP_MUILT("1"), FTP("2"), SFTP("3"), FTPS("4"), LOCALE("9");
    private final String value;

    RemoteInvokeMode(String anValue) {
        this.value = anValue;
    }

    public String getValue() {

        return this.value;
    }

    public Class findWorkClass(Class anConn, Class anFtp) {
        Class tmpClass;
        if (this == RemoteInvokeMode.HTTP) {
            tmpClass = anConn;
        }
        else if (this == RemoteInvokeMode.HTTP_MUILT) {
            tmpClass = RemoteMultiPartConnection.class;
        }

        else if (this == RemoteInvokeMode.LOCALE) {
            tmpClass = LocateConnection.class;
        }
        else {
            tmpClass = anFtp;
        }

        return tmpClass;
    }

    public static RemoteInvokeMode checking(String anWorkType) {
        try {
            if (BetterStringUtils.isNotBlank(anWorkType)) {
                for (RemoteInvokeMode statusType : RemoteInvokeMode.values()) {
                    if (statusType.value.equals(anWorkType)) {

                        return statusType;
                    }
                }
                return RemoteInvokeMode.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return RemoteInvokeMode.HTTP;
    }
}
