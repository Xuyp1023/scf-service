package com.betterjr.modules.push.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.push.IScfSupplierPushService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

@Service(interfaceClass=IScfSupplierPushService.class)
public class ScfSupplierPushDubboService implements IScfSupplierPushService {

    @Autowired
    private ScfSupplierPushService scfSupplierPushService; 
    
    @Override
    public String webAddPushSupplier(Long anBillId) {
        if(scfSupplierPushService.pushSupplierInfo(anBillId)){
            return AjaxObject.newOk("推送成功").toJson();
        }else{
            return AjaxObject.newError("推送失败").toJson();
        }
    }
    
    public void webAddPushSign(String anAppNo){
        scfSupplierPushService.testPushSign(anAppNo);
    }

}
