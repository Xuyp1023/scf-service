package com.betterjr.modules.delivery.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.delivery.IDeliveryService;
import com.betterjr.modules.delivery.service.DeliveryRecordService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=IDeliveryService.class)
public class DeliveryDubboService  implements IDeliveryService{

    @Autowired
    private DeliveryRecordService recordService;
    
    @Override
    public String webQueryFileList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize, boolean anIsPostCust) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("快递账单查询成功", recordService.queryDeliveryRecordList(queryMap,anFlag, anPageNum, anPageSize,anIsPostCust))
                .toJson();
    }

    @Override
    public String webFindDeliveryRecord(String anRefNo) {
        
        
        return AjaxObject
                .newOk("快递账单查询成功", recordService.findDeliveryRecord(anRefNo))
                .toJson();
    }

    @Override
    public String webAddDeliveryRecord(Map anParamMap) {
        
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("快递账单查询成功", recordService.saveAddDeliveryRecord(queryMap))
                .toJson();
    }

    @Override
    public String webSaveDeleteStatement(Long anStatementId) {
        
        return AjaxObject
                .newOk("快递账单修改成功", recordService.saveModifyRecordByStatementId(anStatementId))
                .toJson();
    }

    @Override
    public String webSaveExpressDelivery(String anRefNo) {
        
        return AjaxObject
                .newOk("快递账单投递成功", recordService.saveExpressRecordById(anRefNo))
                .toJson();
    }

    @Override
    public String webSaveConfirmDelivery(String anRefNo) {
        
        return AjaxObject
                .newOk("快递账单确认成功", recordService.saveConfirmRecordByRefNo(anRefNo))
                .toJson();
    }
    

}
