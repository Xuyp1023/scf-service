package com.betterjr.modules.remote.serializer;

import java.util.List;
import java.util.Map;

import com.betterjr.common.security.CustKeyManager;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFaceInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;

public interface RemoteDeSerializer {

    public Map<String, Object> getOrignMap();

    public boolean getStatus();

    public String getReturnMsg();

    public void init(WorkFarFunction anFunc, RemoteConnection anConn , Map<String, FarConfigInfo> anConfigInfo, CustKeyManager anKeyManager);

    public void initParameter(Map anConfigInfo, CustKeyManager anKeyManager);
    /**
     * 获取处理结果
     * 
     * @return
     */
    public List<Map<String, Object>> readResult();
    public   WSInfo readInput(Map<String, String> formPara, WorkFarFaceInfo faceInfo);

}