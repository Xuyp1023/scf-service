package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfPayService;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfPayService.class)
public class PayDubboService implements IScfPayService {
    protected final Logger logger = LoggerFactory.getLogger(PayDubboService.class);

    @Autowired
    private ScfPayPlanService scfPayPlanService;

    @Override
    public String webQueryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询融资申请列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("融资申请查询成功",
                scfPayPlanService.queryPayPlanList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webAddPayPlan(Map<String, Object> anMap) {
        logger.debug("新增融资申请，入参：" + anMap);
        return AjaxObject.newOk(scfPayPlanService.addPayPlan((ScfPayPlan) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }

    @Override
    public String webSaveModifyPayPlan(Map<String, Object> anMap, Long id) {
        logger.debug("修改融资申请，入参：" + anMap);
        return AjaxObject.newOk(scfPayPlanService.saveModifyPayPlan((ScfPayPlan) RuleServiceDubboFilterInvoker.getInputObj(), id)).toJson();
    }
    
    @Override
    public String webFindPayPlanDetail(Map<String, Object> anMap, Long id) {
        logger.debug("查询融资申请详情，入参：" + anMap);
        return AjaxObject.newOk(scfPayPlanService.findPayPlanDetail(id)).toJson();
    }

}
