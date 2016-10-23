package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfInvoiceService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfInvoiceItem;
import com.betterjr.modules.order.service.ScfInvoiceItemService;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfInvoiceService.class)
public class ScfInvoiceDubboService implements IScfInvoiceService {

    @Autowired
    private ScfInvoiceService scfInvoiceService;
    @Autowired
    private ScfInvoiceItemService scfInvoiceItemService;

    @Override
    public String webAddInvoice(Map<String, Object> anMap, String anInvoiceItemIds, String anFileList) {
        ScfInvoice anInvoice = (ScfInvoice) RuleServiceDubboFilterInvoker.getInputObj();
        // 保存附件信息
        return AjaxObject.newOk("订单发票信息录入成功", scfInvoiceService.addInvoice(anInvoice, anInvoiceItemIds, anFileList)).toJson();
    }

    @Override
    public String webQueryInvoiceList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("订单发票信息查询成功", scfInvoiceService.queryInvoice(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyInvoice(Map<String, Object> anMap, String anInvoiceItemIds, String anFileList) {
        ScfInvoice anInvoice = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单发票信息编辑成功", scfInvoiceService.saveModifyInvoice(anInvoice,anFileList, anInvoiceItemIds)).toJson();
    }
    
    @Override
    public String webAddInvoiceItem(Map<String, Object> anMap) {
        ScfInvoiceItem anInvoiceItem = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票详情录入成功", scfInvoiceItemService.addInvoiceItem(anInvoiceItem)).toJson();
    }
    
    @Override
    public String webSaveDeleteInvoice(Long anId) {
        return AjaxObject.newOk("删除发票信息成功", scfInvoiceService.saveDeleteInvoice(anId)).toJson();
    }

}
