package com.betterjr.modules.credit.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.credit.IScfCreditService;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.credit.service.ScfCreditService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfCreditService.class)
public class ScfCreditDubboService implements IScfCreditService {

    @Autowired
    private ScfCreditService scfCreditService;

    @Autowired
    private ScfCreditDetailService scfCreditDetailService;

    @Override
    public String webQueryFactorCredit(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("授信额度信息查询成功", scfCreditService.queryCredit(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryCustCredit(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("授信余额信息查询成功", scfCreditService.queryCredit(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryCreditDetail(Map<String, Object> anMap, Long anCreditId, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("授信额度变动信息查询成功",
                scfCreditDetailService.queryCreditDetail(anQueryConditionMap, anCreditId, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFindCredit(Long anCustNo, Long anCoreCustNo, Long anFactorNo, String anCreditMode) {
    	
    	return AjaxObject.newOk("授信额度信息查询成功", scfCreditService.findCredit(anCustNo, anCoreCustNo, anFactorNo, anCreditMode)).toJson();
    }
    
    @Override
    public String webFindCreditSimpleData(Long anCustNo, Long anCoreCustNo, Long anFactorNo) {
    	
    	return AjaxObject.newOk("授信额度信息查询成功", scfCreditService.findCreditSimpleData(anCustNo, anCoreCustNo, anFactorNo)).toJson();
    }
    
    @Override
    public String webFindCreditList(Long anCustNo, Long anCoreCustNo, Long anFactorNo) {
        return AjaxObject.newOk("授信额度信息查询成功", scfCreditService.findCreditList(anCustNo, anCoreCustNo, anFactorNo)).toJson();
    }

    @Override
    public String webFindCreditSumByCustNo(Long anCustNo) {

        return AjaxObject.newOk("授信额度信息查询成功", scfCreditService.findCreditSumByCustNo(anCustNo)).toJson();
    }

    @Override
    public String webAddCredit(Map<String, Object> anMap) {

        ScfCredit anCredit = (ScfCredit) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("授信额度录入成功", scfCreditService.addCredit(anCredit)).toJson();
    }

    @Override
    public String webModifyCredit(Map<String, Object> anMap, Long anId) {

        ScfCredit anCredit = (ScfCredit) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("授信记录修改成功", scfCreditService.saveModifyCredit(anCredit, anId)).toJson();
    }

    @Override
    public String webActivateCredit(Long anId) {

        return AjaxObject.newOk("授信额度激活成功", scfCreditService.saveActivateCredit(anId)).toJson();
    }

    @Override
    public String webTerminatCredit(Long anId) {

        return AjaxObject.newOk("授信终止成功", scfCreditService.saveTerminatCredit(anId)).toJson();
    }

    public void saveOccupyCredit(ScfCreditInfo anOccupyCredit) {

        scfCreditDetailService.saveOccupyCredit(anOccupyCredit);
    }

    public void saveReleaseCredit(ScfCreditInfo anOccupyCredit) {

        scfCreditDetailService.saveReleaseCredit(anOccupyCredit);
    }

}
