package com.betterjr.modules.receivable.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.receivable.IScfReceivableService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.receivable.service.ScfReceivableService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfReceivableService.class)
public class ScfReceivableDubboService implements IScfReceivableService {

    @Autowired
    private ScfReceivableService scfReceivableService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Override
    public String webSaveModifyReceivable(Map<String, Object> anMap, Long anId, String anFileList, String anOtherFileList) {
        ScfReceivable anReceivable = (ScfReceivable) RuleServiceDubboFilterInvoker.getInputObj();
        
        //保存附件信息
        anReceivable.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anReceivable.getBatchNo()));
        anReceivable.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anReceivable.getOtherBatchNo()));
        return AjaxObject.newOk("应收账款信息编辑成功", scfReceivableService.saveModifyReceivable(anReceivable, anId, anFileList, anOtherFileList)).toJson();
    }

    @Override
    public String webQueryReceivable(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOkWithPage("应收账款查询成功", scfReceivableService.queryReceivable(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindReceivableList(String anCustNo, String anIsOnlyNormal) {
        return AjaxObject.newOk("应收账款查询成功", scfReceivableService.findReceivableList(anCustNo, anIsOnlyNormal)).toJson();
    }

}
