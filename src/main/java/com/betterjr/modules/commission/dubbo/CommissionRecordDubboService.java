package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.service.CommissionRecordService;
import com.betterjr.modules.commissionfile.ICommissionRecordService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = ICommissionRecordService.class)
public class CommissionRecordDubboService implements ICommissionRecordService {

    @Autowired
    private CommissionRecordService recordService;

    @Override
    public String webQueryRecordList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("佣金记录查询成功", recordService.queryRecordList(queryMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryCanAuditRecordList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("佣金审核全部记录查询成功", recordService.queryCanAuditRecordList(queryMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveAuditRecordList(Long anCustNo, String anImportDate) {

        Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("custNo", anCustNo).put("importDate", anImportDate).build();
        return AjaxObject.newOk("佣金记录审核成功", recordService.saveAuditRecordList(queryMap)).toJson();
    }

}
