package com.betterjr.modules.supplieroffer.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.supplieroffer.IScfSupplierOfferService;
import com.betterjr.modules.supplieroffer.entity.ScfSupplierOffer;
import com.betterjr.modules.supplieroffer.service.ScfSupplierOfferService;

@Service(interfaceClass=IScfSupplierOfferService.class)
public class ScfSupplierOfferDubboService implements IScfSupplierOfferService {

    @Autowired
    private ScfSupplierOfferService offerService;
    
    @Override
    public String webSaveAddOffer(Map<String, Object> anMap) {
        
        ScfSupplierOffer offer=RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("供应商利率新增成功", offerService.saveAddSupplierOffer(offer)).toJson();
    }

    @Override
    public String webSaveUpdateOffer(Long anCustNo, Long anCoreCustNo, double anCoreCustRate) {
        
        return AjaxObject.newOk("供应商利率修改成功", offerService.saveUpdateSupplierOffer(anCustNo, anCoreCustNo, anCoreCustRate)).toJson();
    }

    @Override
    public String webQueryOfferPage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("供应商利率查询成功", offerService.queryScfSupplierOfferPage(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFindOffer(Long anCustNo, Long anCoreCustNo) {
        
        return AjaxObject.newOk("供应商利率查询成功", offerService.findOffer(anCustNo, anCoreCustNo)).toJson();
    }

    @Override
    public String webFindOffer(Long anId) {
        
        return AjaxObject.newOk("供应商利率查询成功", offerService.selectByPrimaryKey(anId)).toJson();
    }

    @Override
    public String webQueryAllCust(Long anCoreCustNo) {
        
        return AjaxObject.newOk("查询供应商成功", offerService.queryAllCust(anCoreCustNo)).toJson();
    }

}
