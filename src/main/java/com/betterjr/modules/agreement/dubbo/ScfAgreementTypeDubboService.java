package com.betterjr.modules.agreement.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.IScfAgreementTypeService;
import com.betterjr.modules.agreement.entity.ScfAgreementType;
import com.betterjr.modules.agreement.service.ScfAgreementTypeService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfAgreementTypeService.class)
public class ScfAgreementTypeDubboService implements IScfAgreementTypeService {

    @Autowired
    private ScfAgreementTypeService agreementTypeService;

    @Override
    public String webQueryRegisteredAgreementType(int anPageNum, int anPageSize, String anFlag) {
        return AjaxObject.newOkWithPage("查询登记合同成功！",
                agreementTypeService.queryRegisteredAgreementType(anPageNum, anPageSize, anFlag)).toJson();
    }

    @Override
    public String webAddAgreementType(Map<String, Object> anMap) {
        ScfAgreementType anAgreementType = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("登记合同类型成功！", agreementTypeService.addAgreementType(anAgreementType)).toJson();
    }

    @Override
    public String webSaveModifyAgreementType(Map<String, Object> anMap, Long anId) {
        ScfAgreementType anAgreementType = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("编辑合同成功！", agreementTypeService.saveModifyAgreementType(anAgreementType, anId))
                .toJson();
    }

    @Override
    public String webSaveDeleteAgreementType(Long anId) {
        return AjaxObject.newOk("删除成功！", agreementTypeService.saveDeleteAgreementType(anId)).toJson();
    }

    @Override
    public String webQueryUnEnableAgreementType(int anPageNum, int anPageSize, String anFlag) {
        return AjaxObject.newOkWithPage("查询待启用合同类型成功！",
                agreementTypeService.queryUnEnableAgreementType(anPageNum, anPageSize, anFlag)).toJson();
    }

    @Override
    public String webFindEnableAgreementType() {
        return AjaxObject.newOk("查询已启用合同类型成功！", agreementTypeService.findEnableAgreementType()).toJson();
    }

    @Override
    public String webSaveEnableAgreementType(Long anId) {
        return AjaxObject.newOk("启用合同类型成功！", agreementTypeService.saveEnableAgreementType(anId)).toJson();
    }

    @Override
    public String webQueryAgreementType(Map<String, Object> anMap, int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询合同类型成功",
                agreementTypeService.queryAgreementType(queryMap, anPageNum, anPageSize, anFlag)).toJson();
    }

}
