package com.betterjr.modules.credit.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.credit.IScfCreditService;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.service.ScfCreditService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfCreditService.class)
public class ScfCreditDubboService implements IScfCreditService {

    @Autowired
    private ScfCreditService scfCreditService;

    @Override
    public String webQueryFactorCredit(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("授信额度信息查询成功", scfCreditService.queryCredit(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webAddCredit(Map<String, Object> anMap) {

        ScfCredit anCredit = (ScfCredit) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOk("授信额度录入成功", scfCreditService.addCredit(anCredit)).toJson();
    }

}
