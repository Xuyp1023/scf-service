package com.betterjr.modules.remote.connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.utils.URIBuilder;

import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.remote.entity.MultiPartDataInfo;

public class RemoteMultiPartConnection extends RemoteConnection {

    private static final long serialVersionUID = 4963034736383808792L;

    protected CloseableHttpResponse doPost(Map<String, Object> queryParams, Map<String, Object> formParams) throws URISyntaxException,
            ClientProtocolException, IOException {
        URIBuilder builder = new URIBuilder(this.workUrl);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(factory.paramsConverter(queryParams));
        }
        this.method.setURI(builder.build());
        HttpEntityEnclosingRequestBase workM = (HttpEntityEnclosingRequestBase) method;

        // 填入表单参数
        if (Collections3.isEmpty(formParams)) {
            MultiPartDataInfo data = (MultiPartDataInfo) formParams.get("data");
            if (data != null) {
                workM.setEntity(data.build());
            }
        }
        return factory.getConnection().execute(this.method);
    }
}
