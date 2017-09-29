package com.betterjr.modules.commission.dubbo;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.entity.CommissionInvoice;
import com.betterjr.modules.commission.service.CommissionInvoiceRecordService;
import com.betterjr.modules.commission.service.CommissionInvoiceService;
import com.betterjr.modules.commissionfile.ICommissionInvoiceService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = ICommissionInvoiceService.class)
public class CommissionInvoiceDubboService implements ICommissionInvoiceService {

    @Autowired
    private CommissionInvoiceService invoiceService;

    @Autowired
    private CommissionInvoiceRecordService recordService;

    @Override
    public String webSaveDemandInvoice(Long anCustNo, Long anCoreCustNo, String anMonthlyIds, String anInvoiceType) {

        return AjaxObject.newOk("发票索取成功", invoiceService.saveDemandInvoice(anCustNo, anCoreCustNo, anMonthlyIds, anInvoiceType)).toJson();
    }

    @Override
    public String webSaveInvoiceEffective(Long anInvoiceId, String anInvoiceContent, String anDescription) {

        return AjaxObject.newOk("发票确认成功", invoiceService.saveInvoiceEffective(anInvoiceId, anInvoiceContent, anDescription)).toJson();
    }

    @Override
    public String webSaveAnnulInvoice(Long anInvoiceId) {

        return AjaxObject.newOk("发票作废成功", invoiceService.saveAnnulInvoice(anInvoiceId)).toJson();
    }

    @Override
    public String webSaveConfirmInvoice(Long anInvoiceId) {

        return AjaxObject.newOk("发票确认成功", invoiceService.saveConfirmInvoice(anInvoiceId)).toJson();
    }

    @Override
    public String webSaveAuditInvoice(Map anParamMap, String anFileList) {

        CommissionInvoice invoice = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("发票提交成功", invoiceService.saveAuditInvoice(invoice, anFileList)).toJson();
    }

    @Override
    public String webQueryCommissionInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize, boolean anIsConfirm) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("发票查询成功", invoiceService.queryCommissionInvoice(queryMap, anFlag, anPageNum, anPageSize, anIsConfirm))
                .toJson();
    }

    @Override
    public String webFindCommissionInvoiceById(Long anInvoiceId) {

        return AjaxObject.newOk("发票查询成功", invoiceService.findCommissionInvoiceById(anInvoiceId)).toJson();
    }

    @Override
    public String webQueryCanUseMonthly(Map<String, Object> anMap, int anPageNum, int anPageSize) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("发票查询成功", recordService.queryCanInvoiceMonthlyList(queryMap, anPageNum, anPageSize)).toJson();

    }

}
