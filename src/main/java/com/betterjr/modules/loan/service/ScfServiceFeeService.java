package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfServiceFeeMapper;
import com.betterjr.modules.loan.entity.ScfServiceFee;

@Service
public class ScfServiceFeeService extends BaseService<ScfServiceFeeMapper, ScfServiceFee> {

    /**
     * 新增手续费记录
     * @param anServiceFee
     * @return
     */
    public ScfServiceFee addServiceFee(ScfServiceFee anServiceFee) {
        BTAssert.notNull(anServiceFee, "新增手续费记录失败-anServiceFee不能为空");
        anServiceFee.init();
        this.insert(anServiceFee);
        return findServiceFeeDetail(anServiceFee.getId());
    }
    
    /**
     * 修改手续费记录
     * 
     * @param anServiceFee
     * @return
     */
    public ScfServiceFee saveModifyServiceFee(ScfServiceFee anServiceFee, Long anId) {
        BTAssert.notNull(anServiceFee, "修改手续费记录失败-anServiceFee不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anServiceFee.getFactorNo());
        map.put("id", anId);
        if(Collections3.isEmpty(selectByClassProperty(ScfServiceFee.class, map))){
            throw new BytterTradeException(40001, "修改手续费记录失败-找不到原数据");
        }

        anServiceFee.initModify();
        anServiceFee.setId(anId);
        this.updateByPrimaryKeySelective(anServiceFee);
        return findServiceFeeDetail(anId);
    }

    /**
     * 查询手续费记录列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfServiceFee> queryServiceFeeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询手续费记录详情
     * 
     * @param anId
     * @return
     */
    public ScfServiceFee findServiceFeeDetail(Long anId) {
        BTAssert.notNull(anId, "查询手续费记录详情失败-anId不能为空");
        ScfServiceFee fee =  this.selectByPrimaryKey(anId);
        if(null == fee){
            return new ScfServiceFee();
        }
        return fee;
    }
    
    /**
     * 查询手续费记录详情
     * 
     * @param anId
     * @return
     */
    public ScfServiceFee findServiceFeeByType(String anRequestNo, String feeType) {
        Map<String, Object> qyMap = new HashMap<String, Object>();
        qyMap.put("requestNo", anRequestNo);
        qyMap.put("feeType", feeType);
        List<ScfServiceFee> list = this.selectByClassProperty(ScfServiceFee.class, qyMap);
        if(Collections3.isEmpty(list)){
            return null;
        }
        return Collections3.getFirst(list);
    }
    

}
