package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfPayRecordMapper;
import com.betterjr.modules.loan.entity.ScfPayRecord;

@Service
public class ScfPayDetailService extends BaseService<ScfPayRecordMapper, ScfPayRecord> {

    /**
     * 新增还款计划
     * @param anRecord
     * @return
     */
    public int addPayPlan(ScfPayRecord anRecord) {
        BTAssert.notNull(anRecord, "anPlan不能为空");
        anRecord.init();
        return this.insert(anRecord);
    }
    
    /**
     * 修改还款计划
     * 
     * @param anRecord
     * @return
     */
    public int saveModifyPayPlan(ScfPayRecord anRecord, Long anId) {
        BTAssert.notNull(anRecord, "anRecord不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anRecord.getCustNo());
        map.put("id", anId);
        BTAssert.notNull(selectByClassProperty(ScfPayRecord.class, map), "原始数据不存在");

        anRecord.initModify();
        anRecord.setId(anId);
        return this.updateByPrimaryKeySelective(anRecord);
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
    public Page<ScfPayRecord> queryPayPlanList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询还款计划详情
     * 
     * @param anId
     * @return
     */
    public ScfPayRecord findPayPlanDetail(Long anId) {
        BTAssert.notNull(anId, "anId不能为空");
        return this.selectByPrimaryKey(anId);
    }

}
