package com.betterjr.modules.delivery.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.delivery.IDeliveryStatementService;
import com.betterjr.modules.delivery.service.DeliveryRecordStatementService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IDeliveryStatementService.class)
public class DeliveryStatementDubboService implements IDeliveryStatementService {

    @Autowired
    private DeliveryRecordStatementService statementService;

    @Override
    public String webQueryStatementList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("投递账单查询成功", statementService.queryDeliveryStatementList(queryMap, anFlag, anPageNum, anPageSize)).toJson();
    }

}
