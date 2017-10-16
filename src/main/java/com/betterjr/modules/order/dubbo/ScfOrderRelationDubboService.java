package com.betterjr.modules.order.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfOrderRelationService;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfOrderRelationService.class)
public class ScfOrderRelationDubboService implements IScfOrderRelationService {

    @Autowired
    private ScfOrderRelationService scfOrderRelationService;

    @Override
    public String webAddOrderRelation(String anEnterType, Long anEnterId, String anInfoType, String anInfoIdList) {
        ScfOrderRelation anOrderRelation = (ScfOrderRelation) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject
                .newOk("订单关联关系保存成功",
                        scfOrderRelationService.addOrderRelation(anEnterType, anEnterId, anInfoType, anInfoIdList))
                .toJson();
    }

    @Override
    public String webSaveDeleteOrderRelation(String anEnterType, Long anEnterId, String anInfoType, Long anInfoId) {
        return AjaxObject
                .newOk("订单关系删除成功",
                        scfOrderRelationService.saveDeleteOrderRelation(anEnterType, anEnterId, anInfoType, anInfoId))
                .toJson();
    }

}
