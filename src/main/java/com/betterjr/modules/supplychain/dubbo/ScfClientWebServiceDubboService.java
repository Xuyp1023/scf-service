package com.betterjr.modules.supplychain.dubbo;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.supplychain.IScfClientWebService;
import com.betterjr.modules.supplychain.data.ScfClientDataDetail;
import com.betterjr.modules.supplychain.data.ScfClientDataProcess;
import com.betterjr.modules.supplychain.service.ScfClientService;

@Service(interfaceClass = IScfClientWebService.class)
public class ScfClientWebServiceDubboService implements IScfClientWebService {

    @Autowired
    private ScfClientService clientService;

    @Override
    public List<ScfClientDataDetail> findUnCheckAccount(Map anMap) {

        return clientService.findUnCheckAccount(anMap);
    }

    @Override
    public List<ScfClientDataDetail> findWorkAccount(Map anMap) {

        return clientService.findWorkAccount(anMap);
    }

    @Override
    public boolean saveProcessPushData(Map anMap) {

        return clientService.saveProcessPushData(anMap);
    }

    @Override
    public boolean saveProcessCoreCorp(Map anMap) {

        return clientService.saveProcessCoreCorp(anMap);
    }

    @Override
    public boolean saveProcessData(Map anMap) {

        return clientService.saveProcessData(anMap);
    }

    @Override
    public boolean saveBatchProcessData(Map anMap) {

        return clientService.saveBatchProcessData(anMap);
    }

    @Override
    public boolean noticeProcess(Map anMap) {

        return clientService.noticeProcess(anMap);
    }

    @Override
    public List<ScfClientDataProcess> findDataProcess(Map anMap) {

        return clientService.findDataProcess(anMap);
    }

}
