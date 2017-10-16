package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.entity.CommissionInvoiceCustInfo;
import com.betterjr.modules.commission.service.CommissionInvoiceCustInfoService;
import com.betterjr.modules.commissionfile.ICommissionInvoiceCustInfoService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = ICommissionInvoiceCustInfoService.class)
public class CommissionInvoiceCustInfoDubboService implements ICommissionInvoiceCustInfoService {

    @Autowired
    private CommissionInvoiceCustInfoService custInfoService;

    @Override
    public String webSaveAddInvoiceCustInfo(Map anParamMap) {

        CommissionInvoiceCustInfo custInfo = RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("发票抬头新增成功", custInfoService.saveAddInvoiceCustInfo(custInfo)).toJson();
    }

    @Override
    public String webQueryInvoiceCustInfoList(Map<String, Object> anAnMap, String anFlag, int anPageNum,
            int anPageSize) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("发票抬头查询成功",
                custInfoService.queryInvoiceCustInfoList(queryMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveUpdateInvoiceCustInfo(Map anParamMap) {

        CommissionInvoiceCustInfo custInfo = RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("发票抬头修改成功", custInfoService.saveUpdateInvoiceCustInfo(custInfo)).toJson();

    }

    @Override
    public String webFindInvoiceCustInfoEffectiveByCustNo(Long anCustNo, Long anCoreCustNo) {

        return AjaxObject
                .newOk("发票抬头查询成功", custInfoService.findInvoiceCustInfoEffectiveByCustNo(anCustNo, anCoreCustNo))
                .toJson();
    }

    @Override
    public String webSaveDeleteCustInfo(Long anCustInfoId) {

        return AjaxObject.newOk("发票抬头删除成功", custInfoService.saveDeleteCustInfo(anCustInfoId)).toJson();
    }

}
