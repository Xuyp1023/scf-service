package com.betterjr.modules.agreement.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.IScfAgreementStandardService;
import com.betterjr.modules.agreement.entity.ScfAgreementStandard;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfAgreementStandardService.class)
public class ScfAgreementStandardDubboService implements IScfAgreementStandardService{

    @Autowired
    private ScfAgreementStandardService agreementStandardService;
    
    @Override
    public String webAddAgreementStandard(Map<String, Object> anMap) {
        ScfAgreementStandard anAgreementStandard = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("标准合同登记成功！", agreementStandardService.addAgreementStandard(anAgreementStandard)).toJson();
    }

    @Override
    public String webQueryRegisteredAgreementStandard(int anPageNum, int anPageSize, String anFlag) {
        return AjaxObject.newOkWithPage("登记标准合同查询成功！", agreementStandardService.queryRegisteredAgreementStandard(anPageNum, anPageSize, anFlag)).toJson();
    }

    @Override
    public String webSaveModifyAgreementStandard(Map<String, Object> anMap, Long anId) {
        ScfAgreementStandard anAgreementStandard = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("标准合同编辑成功！", agreementStandardService.saveModifyAgreementStandard(anAgreementStandard, anId)).toJson();
    }

    @Override
    public String webSaveDeleteAgreementStandard(Long anId) {
        return AjaxObject.newOk("标准合同删除成功！", agreementStandardService.saveDeleteAgreementStandard(anId)).toJson();
    }

    @Override
    public String webQueryAgreementStandardByStatus(String anBusinStatus, int anPageNum, int anPageSize, String anFlag) {
        return AjaxObject.newOkWithPage("审核标准合同查询成功！", agreementStandardService.queryAgreementStandardByStatus(anBusinStatus,anPageNum, anPageSize, anFlag)).toJson();
    }

    @Override
    public String webSaveEnableAgreementStandard(Long anId) {
        return AjaxObject.newOk("标准合同启用成功！", agreementStandardService.saveEnableAgreementStandard(anId)).toJson();
    }

    @Override
    public String webSaveDisableAgreementStandard(Long anId) {
        return AjaxObject.newOk("标准合同停用成功！", agreementStandardService.saveDisableAgreementStandard(anId)).toJson();
    }

    @Override
    public String webQueryAgreementStandard(Map<String, Object> anMap, int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("标准合同查询成功！", agreementStandardService.queryAgreementStandard(queryMap, anPageNum, anPageSize, anFlag)).toJson();
    }

}
