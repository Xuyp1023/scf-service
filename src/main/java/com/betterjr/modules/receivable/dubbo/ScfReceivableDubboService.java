package com.betterjr.modules.receivable.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.receivable.IScfReceivableService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.receivable.service.ScfReceivableService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfReceivableService.class)
public class ScfReceivableDubboService implements IScfReceivableService {

    @Autowired
    private ScfReceivableService scfReceivableService;

    @Override
    public String webSaveModifyReceivable(Map<String, Object> anMap, Long anId) {
        ScfReceivable anReceivable = (ScfReceivable) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOk("应收账款信息编辑成功", scfReceivableService.saveModifyReceivable(anReceivable, anId)).toJson();
    }

    @Override
    public String webQueryReceivable(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOkWithPage("应收账款查询成功", scfReceivableService.queryReceivable(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

}
