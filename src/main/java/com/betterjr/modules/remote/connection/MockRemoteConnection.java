package com.betterjr.modules.remote.connection;

import java.util.Map;

public class MockRemoteConnection extends RemoteConnection {

    private static final long serialVersionUID = 4963034736383808792L;

    public StringBuilder readStream() {

        return this.factory.readStream(this.workData, true);
    }
    
    public void sendRequest(Map<String, Object> queryParams, Map<String, Object> formParams) {
        this.statusCode = 200;
        this.statusMessage = "OK";
    }
}
