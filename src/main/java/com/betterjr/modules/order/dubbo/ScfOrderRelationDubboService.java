package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfOrderRelationService;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfOrderRelationService.class)
public class ScfOrderRelationDubboService implements IScfOrderRelationService{

    @Autowired
    private ScfOrderRelationService scfOrderRelationService;
    
    @Override
    public String webAddOrderRelation(Map<String, Object> anMap) {
        ScfOrderRelation anOrderRelation = (ScfOrderRelation) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOk("订单关联关系保存成功", scfOrderRelationService.addOrderRelation(anOrderRelation)).toJson();
    }

    @Override
    public String webSaveDeleteOrderRelation(Long anId) {
        return AjaxObject.newOk("订单关系删除成功", scfOrderRelationService.saveDeleteOrderRelation(anId)).toJson();
    }

}
