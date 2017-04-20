package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfInvoiceService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.entity.ScfInvoiceItem;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.order.service.ScfInvoiceItemService;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfInvoiceService.class)
public class ScfInvoiceDubboService implements IScfInvoiceService {

    @Autowired
    private ScfInvoiceService scfInvoiceService;
    @Autowired
    private ScfInvoiceItemService scfInvoiceItemService;
    @Autowired
    private ScfInvoiceDOService invoiceService;

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

    @Override
    public String webQueryIncompletedInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询补录发票成功", scfInvoiceService.queryIncompletedInvoice(queryMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webAddInvoiceDO(Map<String, Object> anAnMap, String anFileList, boolean anConfirmFlag) {
        
        ScfInvoiceDO invoice = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票信息添加成功", invoiceService.addInvoice(invoice,anFileList, anConfirmFlag)).toJson();
        
    }

    @Override
    public String webSaveModifyInvoiceDO(Map<String, Object> anAnMap, String anFileList, boolean anConfirmFlag) {
        
        ScfInvoiceDO invoice = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票信息编辑成功", invoiceService.saveModifyInvoice(invoice,anFileList, anConfirmFlag)).toJson();
    
    }

    @Override
    public String webSaveAnnulInvoice(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("发票废止成功", invoiceService.saveAnnulInvoice(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webSaveAuditInvoiceByRefNoVersion(String anRefNo, String anVersion) {
       
        return AjaxObject.newOk("发票审核成功", invoiceService.saveAuditInvoice(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webQueryIneffectiveInvoice(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize, boolean anIsAudit) {
       
        Map<String, Object> conditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票查询成功", invoiceService.queryIneffectiveInvoice(conditionMap, anFlag, anPageNum, anPageSize, anIsAudit)).toJson();
 
    }

    @Override
    public String webQueryEffectiveInvoice(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize, boolean anIsCust) {
      
        Map<String, Object> conditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票查询成功", invoiceService.queryEffectiveInvoice(conditionMap, anFlag, anPageNum, anPageSize, anIsCust)).toJson();
 
    }

    @Override
    public String webFindInvoiceDO(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("发票查询成功", invoiceService.findInvoice(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webQueryRecycleInvoice(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {
       
        Map<String, Object> conditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票查询成功", invoiceService.queryRecycleInvoice(conditionMap, anFlag, anPageNum, anPageSize)).toJson();
 
    }

    @Override
    public String webSaveAnnulEffectiveInvoice(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("发票废止成功", invoiceService.saveCustAnnulInvoice(anRefNo, anVersion)).toJson();
    }

}
