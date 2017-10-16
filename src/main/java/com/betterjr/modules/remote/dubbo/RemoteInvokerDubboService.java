package com.betterjr.modules.remote.dubbo;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.remote.IRemoteInvoker;
import com.betterjr.modules.remote.entity.RemoteResultInfo;
import com.betterjr.modules.remote.helper.RemoteProxyFactory;
import com.betterjr.modules.remote.helper.RemoteProxyService;

@Service(interfaceClass = IRemoteInvoker.class)
public class RemoteInvokerDubboService implements IRemoteInvoker {
    private static final Logger logger = LoggerFactory.getLogger(RemoteInvokerDubboService.class);

    @SuppressWarnings("rawtypes")
    @Override
    public RemoteResultInfo invoke(String anFaceNo, Method anMethod, Object[] anArgs) {
        try {
            RemoteProxyService proxy = RemoteProxyFactory.createProxyService(anFaceNo);
            RemoteResultInfo result = (RemoteResultInfo) proxy.invoke(null, anMethod, anArgs);
            return result;
        }
        catch (Throwable e) {
            logger.error("remote invoker provider side:method=" + anMethod.getName(), e);
            return null;
        }
    }

    @Override
    public String process(Map<String, String> anMap) {

        String partnerCode = anMap.get("partnerCode");

        RemoteProxyService service = RemoteProxyFactory.createProxyService(partnerCode);
        if (service != null) {
            return service.process(anMap);
        } else {
            return "not find partnerCode !";
        }
    }

    @Override
    public String signFile(String partnerCode, String anFileToken) {
        try {
            RemoteProxyService proxy = RemoteProxyFactory.createProxyService(partnerCode);
            return proxy.signFile(anFileToken);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "sign file failed!";
        }
    }

}
