package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfPayPlanMapper;
import com.betterjr.modules.loan.entity.ScfPayPlan;

@Service
public class ScfPayPlanService extends BaseService<ScfPayPlanMapper, ScfPayPlan> {

    /**
     * 新增还款计划
     * @param anPlan
     * @return
     */
    public int addPayPlan(ScfPayPlan anPlan) {
        BTAssert.notNull(anPlan, "anPlan不能为空");
        anPlan.init();
        return this.insert(anPlan);
    }
    
    /**
     * 修改还款计划
     * 
     * @param anPlan
     * @return
     */
    public int saveModifyPayPlan(ScfPayPlan anPlan, Long anId) {
        BTAssert.notNull(anPlan, "anPlan不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anPlan.getCustNo());
        map.put("id", anId);
        BTAssert.notNull(selectByClassProperty(ScfPayPlan.class, map), "原始数据不存在");

        anPlan.initModify();
        anPlan.setId(anId);
        return this.updateByPrimaryKeySelective(anPlan);
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
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayPlan findPayPlanDetail(Long anId) {
        BTAssert.notNull(anId, "anId不能为空");
        return this.selectByPrimaryKey(anId);
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
        if(!Collections3.isEmpty(list)){
            return list.get(0);
        }
        return new ScfPayPlan();
    }

}
