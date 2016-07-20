package com.betterjr.modules.product.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.product.IScfProductService;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfProductService.class)
public class ScfProductDubboService implements IScfProductService {

    @Autowired
    private ScfProductService scfProductService;

    @Override
    public String webQueryProduct(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("融资产品信息查询成功", scfProductService.queryProduct(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryProductKeyAndValue(Long anCoreCustNo, Long anFactorNo) {

        return AjaxObject.newOk("融资产品下拉列表查询成功", scfProductService.queryProductKeyAndValue(anCoreCustNo, anFactorNo)).toJson();
    }

    @Override
    public String webAddProduct(Map<String, Object> anMap) {

        ScfProduct anProduct = (ScfProduct) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("融资产品录入成功", scfProductService.addProduct(anProduct)).toJson();
    }

}
