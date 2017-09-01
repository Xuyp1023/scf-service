package com.betterjr.modules.supplieroffer.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.supplieroffer.IScfAgreementTemplateService;
import com.betterjr.modules.supplieroffer.entity.ScfAgreementTemplate;
import com.betterjr.modules.supplieroffer.service.ScfAgreementTemplateService;


@Service(interfaceClass=IScfAgreementTemplateService.class)
public class ScfAgreementTemplateDubboService implements IScfAgreementTemplateService {

    @Autowired
    private ScfAgreementTemplateService templateService;
    
    @Override
    public String webSaveAddTemplate(Map<String, Object> anMap, boolean anIsOperator) {
        
        ScfAgreementTemplate agreement=RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("合同模版新增成功", templateService.saveAddAgreementTemplate(agreement, anIsOperator)).toJson();
    }

    @Override
    public String webSaveDeleteTemplate(Long anId) {
        
        return AjaxObject.newOk("合同模版删除成功", templateService.saveDeleteAgreementTemplate(anId)).toJson();
    }

    @Override
    public String webSaveActiveTemplate(Long anId) {
        
        return AjaxObject.newOk("合同模版生效成功", templateService.saveActiveAgreementTemplate(anId)).toJson();
    }

    @Override
    public String webFindTemplateWithStatus(Long anCoreCustNo, String anBusinStatus) {
        
        return AjaxObject.newOk("合同模版查询成功", templateService.findAgreementTemplateWithStatus(anCoreCustNo, anBusinStatus)).toJson();
    }

    @Override
    public String webSaveUploadFtlAgreement(Long anId, Long anFileId) {
        
        return AjaxObject.newOk("FTL合同模版上传成功", templateService.saveUploadFtlAgreement(anId, anFileId)).toJson();
    }

    @Override
    public String webSaveDeleteFtlAgreement(Long anId) {
        
        return AjaxObject.newOk("FTL合同模版删除成功", templateService.saveDeleteFtlAgreement(anId)).toJson();
    }

    @Override
    public String webFindTemplate(Long anCoreCustNo) {
        
        return AjaxObject.newOk("合同模版查找成功", templateService.findAgreementTemplate(anCoreCustNo)).toJson();
    }

    @Override
    public String webQueryTemplatePage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        
        return AjaxObject.newOkWithPage("合同模版查询成功", templateService.queryAgreementTemplatePage(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

}
