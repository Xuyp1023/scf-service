package com.betterjr.modules.remote.serializer;

import java.io.Serializable;
import java.util.Map;

import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.RemoteFieldInfo;
import com.betterjr.modules.remote.entity.WSInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;

/**
 * 远程调用的编码处理；产生调用需要的数据
 * 
 * @author zhoucy
 *
 */
public interface RemoteSerializer extends Serializable {

    public Map<String, Object> readResult();
    public void writeOutput(WorkFarFunction  anFun, WSInfo info,Object outObj, WebServiceErrorCode code);
    public void initParameter(Map anConfigInfo, CustKeyManager anKeyManager);
    public void init(WorkFarFunction anFunc, Map<String, RemoteFieldInfo> anHeaderInfo, Map<String, RemoteFieldInfo> anSendParams,
            Map<String, FarConfigInfo> anConfigInfo, CustKeyManager anKeyManager,RemoteConnection conn);
}
