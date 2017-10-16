package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.IScfTransportService;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.service.ScfTransportService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfTransportService.class)
public class ScfTransportDubboService implements IScfTransportService {

    @Autowired
    private ScfTransportService scfTransportService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Override
    public String webAddTransport(Map<String, Object> anMap, String anFileList) {
        ScfTransport anTransport = (ScfTransport) RuleServiceDubboFilterInvoker.getInputObj();
        // 保存附件信息
        anTransport.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anTransport.getBatchNo()));
        return AjaxObject.newOk("订单运输单据录入成功", scfTransportService.addTransport(anTransport, anFileList)).toJson();
    }

    @Override
    public String webQueryTransportList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("订单运输单据查询成功",
                        scfTransportService.queryTransport(anQueryConditionMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webSaveModifyTransport(Map<String, Object> anMap, Long anId, String anFileList) {
        ScfTransport anTransport = (ScfTransport) RuleServiceDubboFilterInvoker.getInputObj();
        // 保存附件信息
        anTransport.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anTransport.getBatchNo()));
        return AjaxObject.newOk("订单运输单据编辑成功", scfTransportService.saveModifyTransport(anTransport, anId, anFileList))
                .toJson();
    }

    @Override
    public String webSaveDeleteTransport(Long anId) {
        return AjaxObject.newOk("订单运输单据删除成功", scfTransportService.saveDeleteTransport(anId)).toJson();
    }

}
