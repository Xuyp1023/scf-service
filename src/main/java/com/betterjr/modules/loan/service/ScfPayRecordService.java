package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfPayRecordMapper;
import com.betterjr.modules.loan.entity.ScfPayRecord;

@Service
public class ScfPayRecordService extends BaseService<ScfPayRecordMapper, ScfPayRecord> {

    /**
     * 新增还款记录
     * @param anRecord
     * @return
     */
    public int addPayRecord(ScfPayRecord anRecord) {
        BTAssert.notNull(anRecord, "新增还款记录失败-anRecord不能为空");
        anRecord.init();
        return this.insert(anRecord);
    }
    
    /**
     * 修改还款记录
     * 
     * @param anRecord
     * @return
     */
    public int saveModifyPayRecord(ScfPayRecord anRecord, Long anId) {
        BTAssert.notNull(anRecord, "修改还款记录失败-anRecord不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anRecord.getCustNo());
        map.put("id", anId);
        BTAssert.notNull(selectByClassProperty(ScfPayRecord.class, map), "修改还款记录失败-原始数据不存在");

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
    public Page<ScfPayRecord> queryPayRecordList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
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
    public List<ScfPayRecord> findPayRecordList(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfPayRecord.class, anMap);
    }

    /**
     * 查询还款记录详情
     * 
     * @param anId
     * @return
     */
    public ScfPayRecord findPayRecordDetail(Long anId) {
        BTAssert.notNull(anId, "查询还款记录详情失败-anId不能为空");
        return this.selectByPrimaryKey(anId);
    }

}
