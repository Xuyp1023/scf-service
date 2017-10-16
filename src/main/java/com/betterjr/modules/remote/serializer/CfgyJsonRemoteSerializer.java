package com.betterjr.modules.remote.serializer;

import java.util.Map;

public class CfgyJsonRemoteSerializer extends RemoteBaseSerializer {

    private static final long serialVersionUID = -6323273090244318514L;

    @Override
    protected Object buildReqParams(Map<String, Object> anParamsMap) {

        return null;
    }

    @Override
    public Map<String, Object> readResult() {
        Map<String, Object> map = mergeHeadInfo();
        logger.info("财富共盈请求参数cfgy request send data：" + map);
        return map;
    }
}
