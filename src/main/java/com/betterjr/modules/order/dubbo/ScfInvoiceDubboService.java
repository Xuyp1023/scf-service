package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.IScfInvoiceService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfInvoiceService.class)
public class ScfInvoiceDubboService implements IScfInvoiceService {

    @Autowired
    private ScfInvoiceService scfInvoiceService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;
    
    @Override
    public String webAddInvoice(Map<String, Object> anMap, String anFileList) {
        ScfInvoice anInvoice = (ScfInvoice) RuleServiceDubboFilterInvoker.getInputObj();
        //保存附件信息
        anInvoice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anInvoice.getBatchNo()));
        return AjaxObject.newOk("订单发票信息录入成功", scfInvoiceService.addInvoice(anInvoice, anFileList)).toJson();
    }
    
    @Override
    public String webQueryInvoiceList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOkWithPage("订单发票信息查询成功", scfInvoiceService.queryInvoice(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    

}
