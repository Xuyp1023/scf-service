package com.betterjr.modules.agreement.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.IScfCustAgreementService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=IScfCustAgreementService.class)
public class ScfCustAgreementDubboService implements IScfCustAgreementService{

    @Autowired
    private ScfCustAgreementService scfCustAgreementService;
    
    @Override
    public String webQueryCustAgreementsByPage(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        return  AjaxObject.newOkWithPage("查询用户合同分页信息成功",scfCustAgreementService.queryCustAgreementsByPage(anParam, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webAddCustAgreement(Map<String, Object> anMap, String anFileList) {
        CustAgreement custAgreement=(CustAgreement)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("新增合同成功，请复核", scfCustAgreementService.addCustAgreement(custAgreement, anFileList)).toJson();
    }

    @Override
    public String webFindCustAgreementDetail(Long anAgreeId) {
        return AjaxObject.newOk("查询客户合同明细成功", scfCustAgreementService.findCustAgreementDetail(anAgreeId)).toJson();
    }

    @Override
    public String webModifyCustAgreement(Map<String, String> anParam, Long anId, String anFileList) {
        CustAgreement custAgreement=(CustAgreement)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("更新合同成功，请复核", scfCustAgreementService.modifyCustAgreement(custAgreement, anId, anFileList)).toJson();
    }

    @Override
    public String webFindCustFileItems(Long anId) {
        return AjaxObject.newOk("查询客户合同附件成功", scfCustAgreementService.findCustFileItems(anId)).toJson();
    }

    @Override
    public String webDeleteFileItem(Long anId, Long anAgreeId) {
        scfCustAgreementService.deleteFileItem(anId, anAgreeId);
        return AjaxObject.newOk("删除合同附件成功").toJson();
    }

    @Override
    public void webSaveCustAgreementStatus(Long anAgreeId, int anType) {
        scfCustAgreementService.saveCustAgreementStatus(anAgreeId, anType);
    }
    

}
