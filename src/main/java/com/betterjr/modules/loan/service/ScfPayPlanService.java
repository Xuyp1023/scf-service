package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfPayPlanMapper;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequestScheme;

@Service
public class ScfPayPlanService extends BaseService<ScfPayPlanMapper, ScfPayPlan> {

    @Autowired
    private CustAccountService custAccountService;
    
    @Autowired
    private ScfRequestSchemeService schemeService;

    /**
     * 新增还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan addPayPlan(ScfPayPlan anPlan) {
        BTAssert.notNull(anPlan, "anPlan不能为空");

        anPlan.init();
        this.insert(anPlan);
        return findPayPlanDetail(anPlan.getId());
    }

    /**
     * 修改还款计划
     * 
     * @param anPlan
     * @return
     */
    public ScfPayPlan saveModifyPayPlan(ScfPayPlan anPlan, Long anId) {
        BTAssert.notNull(anPlan, "anPlan不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anPlan.getFactorNo());
        map.put("id", anId);
        if (Collections3.isEmpty(selectByClassProperty(ScfPayPlan.class, map))) {
            throw new IllegalArgumentException("找不到原数据");
        }

        anPlan.initModify();
        anPlan.setId(anId);
        this.updateByPrimaryKeySelective(anPlan);
        return findPayPlanDetail(anId);
    }

    /**
     * 查询还款计划列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfPayPlan> queryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfPayPlan> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        for (ScfPayPlan plan : page) {
            plan.setCustName(custAccountService.queryCustName(plan.getCustNo()));
            plan.setFactorName(custAccountService.queryCustName(plan.getFactorNo()));
        }
        return page;
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanDetail(Long anId) {
        BTAssert.notNull(anId, "anId不能为空");

        ScfPayPlan plan = this.selectByPrimaryKey(anId);
        if (null == plan) {
            new ScfPayPlan();
        }

        plan.setCustName(custAccountService.queryCustName(plan.getCustNo()));
        plan.setFactorName(custAccountService.queryCustName(plan.getFactorNo()));
        return plan;
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanByProperty(Map<String, Object> anPropValue) {
        @SuppressWarnings("unchecked")
        List<ScfPayPlan> list = this.selectByClassProperty(ScfPayPlan.class, anPropValue);
        if (!Collections3.isEmpty(list)) {
            return list.get(0);
        }
        return new ScfPayPlan();
    }

    /**
     * 计算结束日期
     * @param scheme
     * @return 
     */
    public String calculatEndDate(String anRequestNo, String anLoanDate) {
        String endDate = "";
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        if(null == scheme){
            return endDate;
        }
        
        //1：日，1：月，
        if(1 == scheme.getPeriodUnit()){
            endDate = BetterDateUtils.addStrDays(anLoanDate, scheme.getPeriod());
        }
        else {
            endDate = BetterDateUtils.addStrMonths(anLoanDate, scheme.getPeriod());
        }
        
        //算头不算尾，所以减掉一天
        return BetterDateUtils.addStrDays(endDate, -1);
    }

    /**
     * 计算费用
     * @param anRequestNo 申请编号
     * @param anLoanBalance 申请金额
     * @param anType        1：管理费，2：手续费，3：利息
     * @return
     */
    public BigDecimal calculatFee(String anRequestNo, BigDecimal anLoanBalance, int anType) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BigDecimal ratio = scheme.getApprovedRatio();
        BigDecimal scale = new BigDecimal(100);
        if(1 == anType){
            ratio = scheme.getApprovedManagementRatio();
        }else if(2 == anType){
            ratio = scheme.getServicefeeRatio();
            scale = new BigDecimal(1000);
        }
        
        if(null == ratio){
            return new BigDecimal(0);
        }
        
        return anLoanBalance.multiply(ratio).multiply(new BigDecimal(scheme.getPeriod())).divide(scale);
    }
    

}
