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
    public String webQueryProduct(Long anCoreCustNo, String anFlag, int anPageNum, int anPageSize) {

        return AjaxObject.newOkWithPage("融资产品信息查询成功", scfProductService.queryProduct(anCoreCustNo, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryProductKeyAndValue(Long anCoreCustNo, Long anFactorNo) {

        return AjaxObject.newOk("融资产品下拉列表查询成功", scfProductService.queryProductKeyAndValue(anCoreCustNo, anFactorNo)).toJson();
    }

    @Override
    public String webFindProductByCode(Long anCoreCustNo, Long anFactorNo, String anProductCode) {

        return AjaxObject.newOk("融资产品信息查询成功", scfProductService.findProductByCode(anCoreCustNo, anFactorNo, anProductCode)).toJson();
    }

    @Override
    public String webFindProductById(Long anProductId) {

        return AjaxObject.newOk("融资产品信息查询成功", scfProductService.findProductById(anProductId)).toJson();
    }

    @Override
    public String webAddProduct(Map<String, Object> anMap) {

        ScfProduct anProduct = (ScfProduct) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("融资产品录入成功", scfProductService.addProduct(anProduct)).toJson();
    }

    @Override
    public String webSaveModifyProduct(Map<String, Object> anMap, Long anId) {

        ScfProduct anProduct = (ScfProduct) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("融资产品信息修改成功", scfProductService.saveModifyProduct(anProduct, anId)).toJson();
    }

    @Override
    public String webSaveDeleteProduct(Long anId) {

        return AjaxObject.newOk("融资产品删除成功", scfProductService.saveDeleteProduct(anId)).toJson();
    }

    @Override
    public String webSaveShelvesProduct(Long anId) {

        return AjaxObject.newOk("融资产品上架成功", scfProductService.saveShelvesProduct(anId)).toJson();
    }

    @Override
    public String webSaveOfflineProduct(Long anId) {

        return AjaxObject.newOk("融资产品下架成功", scfProductService.saveOfflineProduct(anId)).toJson();
    }

}
