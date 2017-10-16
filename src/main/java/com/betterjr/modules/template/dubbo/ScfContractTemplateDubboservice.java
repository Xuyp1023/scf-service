package com.betterjr.modules.template.dubbo;

import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.contract.IScfContractTemplateService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.template.service.ScfContractTemplateService;

@Service(interfaceClass = IScfContractTemplateService.class)
public class ScfContractTemplateDubboservice implements IScfContractTemplateService {

    @Resource
    private ScfContractTemplateService contractTemplateService;

    @Override
    public String webFindTemplateByType(Long anFactorNo, String anType) {
        return AjaxObject.newOk("合同模板-查询 成功", contractTemplateService.findTemplateByType(anFactorNo, anType, "1"))
                .toJson();
    }

    @Override
    public String webFindTemplate(Long anId) {
        return AjaxObject.newOk("合同模板-查询 成功", contractTemplateService.findTemplate(anId)).toJson();
    }

    @Override
    public String webQueryTemplate(Map<String, Object> anParam, int anFlag, int anPageNum, int anPageSize) {
        Map map = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("合同模板-列表查询 成功",
                contractTemplateService.queryTemplate(anParam, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveTemplate(Map<String, Object> anParam) {
        // ScfContractTemplate template = (ScfContractTemplate)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("合同模板-保存成功", contractTemplateService.addTemplate(anParam, anParam.get("fileList").toString()))
                .toJson();
    }

    @Override
    public String webSaveModifyTemplate(Map<String, Object> anParam, Long id) {
        // ScfContractTemplate template = (ScfContractTemplate)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("合同模板-修改成功",
                        contractTemplateService.saveUpdateTemplate(anParam, id, anParam.get("fileList").toString()))
                .toJson();
    }

}
