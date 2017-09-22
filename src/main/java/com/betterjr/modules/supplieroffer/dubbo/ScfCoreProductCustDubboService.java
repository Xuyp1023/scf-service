package com.betterjr.modules.supplieroffer.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.supplieroffer.IScfCoreProductCustService;
import com.betterjr.modules.supplieroffer.service.ScfCoreProductCustService;


@Service(interfaceClass=IScfCoreProductCustService.class)
public class ScfCoreProductCustDubboService implements IScfCoreProductCustService {

    
    @Autowired
    private ScfCoreProductCustService productService;
    @Override
    public String webQueryProductConfigByCore(Long anCustNo, Long anCoreCustNo) {
        
        return AjaxObject.newOk("查询成功", productService.queryProductConfigByCore(anCustNo, anCoreCustNo)).toJson();
    }

    @Override
    public String webSaveAddAndUpdateProduct(Long anCustNo, Long anCoreCustNo, String anProductCodes) {
        
        return AjaxObject.newOk("查询成功", productService.saveAddAndUpdateProduct(anCustNo, anCoreCustNo,anProductCodes)).toJson();
    }

    @Override
    public String webQueryCanUseProduct(Long anCustNo, Long anCoreCustNo) {
        
        return AjaxObject.newOk("查询成功", productService.queryCanUseProduct(anCustNo, anCoreCustNo)).toJson();
    }

}
