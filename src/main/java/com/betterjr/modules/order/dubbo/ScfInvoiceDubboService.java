package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfInvoiceService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfInvoiceService.class)
public class ScfInvoiceDubboService implements IScfInvoiceService {

    @Autowired
    private ScfInvoiceService scfInvoiceService;
    
    @Override
    public String webAddInvoice(Map<String, Object> anMap, String anFileList) {
        ScfInvoice anInvoice = (ScfInvoice) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单发票信息录入成功", scfInvoiceService.addInvoice(anInvoice, anFileList)).toJson();
    }

}
