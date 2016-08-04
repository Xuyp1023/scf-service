package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfOrderService;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfOrderService.class)
public class ScfOrderDubboService implements IScfOrderService{
    
    @Autowired
    private ScfOrderService scfOrderService;

    @Override
    public String webSaveModifyOrder(Map<String, Object> anMap, Long anId, String anFileList) {
        ScfOrder anOrder = (ScfOrder) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息编辑成功", scfOrderService.saveModifyOrder(anOrder, anId, anFileList)).toJson();
    }

    @Override
    public String webQueryOrder(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息查询成功", scfOrderService.queryOrder(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

}
