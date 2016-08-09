package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRepaymentService;
import com.betterjr.modules.loan.entity.ScfPayRecord;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRepaymentService.class)
public class RepaymentDubboService implements IScfRepaymentService {
    protected final Logger logger = LoggerFactory.getLogger(RepaymentDubboService.class);

    @Autowired
    private ScfPayPlanService payPlanService;
    
    @Override
    public String webSaveRepayment(Map<String, Object> anMap) {
        ScfPayRecord record = (ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == anMap.get("payPlanId") || BetterStringUtils.isBlank(anMap.get("payPlanId").toString())){
            AjaxObject.newError("payPlanId不能为空");
        }
        
        record.setPayPlanId(Long.parseLong(anMap.get("payPlanId").toString()));
        return AjaxObject.newOk("操作成功", payPlanService.saveRepayment(record)).toJson();
    }
    
    @Override
    public String webQueryRepaymentFee(String anRequestNo, String anPayType, String factorNo) {
        ScfPayRecord record = payPlanService.queryRepaymentFee(anRequestNo, anPayType, factorNo);
        return AjaxObject.newOk("操作成功", record).toJson();
    }
    
    @Override
    public String webQuerySellerRepaymentFee(Map<String, Object> anMap) {
        ScfPayRecord record = (ScfPayRecord) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("操作成功", payPlanService.querySellerRepaymentFee(record)).toJson();
    }
    
    @Override
    public String webCalculatPayType(String anRequestNo, String payDate) {
        return AjaxObject.newOk("操作成功", payPlanService.calculatPayType(anRequestNo, payDate)).toJson();
    }
    

}
