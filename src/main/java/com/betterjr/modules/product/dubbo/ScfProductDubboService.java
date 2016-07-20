package com.betterjr.modules.product.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.product.IScfProductService;
import com.betterjr.modules.product.service.ScfProductService;

@Service(interfaceClass = IScfProductService.class)
public class ScfProductDubboService implements IScfProductService {

    @Autowired
    private ScfProductService scfProductService;

    @Override
    public String webQueryProductKeyAndValue(Long anCoreCustNo, Long anFactorNo) {

        return AjaxObject.newOk("融资产品下拉列表查询成功", scfProductService.queryProductKeyAndValue(anCoreCustNo, anFactorNo)).toJson();
    }

}
