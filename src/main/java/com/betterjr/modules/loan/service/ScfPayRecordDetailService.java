package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfPayRecordDetailMapper;
import com.betterjr.modules.loan.entity.ScfPayRecordDetail;

@Service
public class ScfPayRecordDetailService extends BaseService<ScfPayRecordDetailMapper, ScfPayRecordDetail> {

    /**
     * 新增还款明细记录详情
     * @param anRecord
     * @return
     */
    public int addRecordDetail(ScfPayRecordDetail anRecord) {
        BTAssert.notNull(anRecord, "新增还款明细记录详情失败-anRecord不能为空");
        anRecord.init();
        return this.insert(anRecord);
    }

    /**
     * 修改还款明细记录详情
     * 
     * @param anRecord
     * @return
     */
    public int saveModifyRecordDetail(ScfPayRecordDetail anRecord, Long anId) {
        BTAssert.notNull(anRecord, "修改还款明细记录详情失败-anRecord不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anRecord.getCustNo());
        map.put("id", anId);
        BTAssert.notNull(selectByClassProperty(ScfPayRecordDetail.class, map), "修改还款明细记录详情-原始数据不存在");

        anRecord.initModify();
        anRecord.setId(anId);
        return this.updateByPrimaryKeySelective(anRecord);
    }

    /**
     * 分页查询还款明细记录
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfPayRecordDetail> queryRecordDetailList(Map<String, Object> anMap, int anFlag, int anPageNum,
            int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询还款明细记录
     * 
     * @param anId
     * @return
     */
    public ScfPayRecordDetail findRecordDetail(Long anId) {
        BTAssert.notNull(anId, "查询还款明细记录失败-anId不能为空");
        return this.selectByPrimaryKey(anId);
    }

}
