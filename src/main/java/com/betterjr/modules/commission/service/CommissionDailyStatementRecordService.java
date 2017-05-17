package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionDailyStatementRecordMapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionDailyStatementRecord;

/***
 * 日账单记录表
 * @author hubl
 *
 */
@Service
public class CommissionDailyStatementRecordService extends BaseService<CommissionDailyStatementRecordMapper, CommissionDailyStatementRecord> {

    /***
     * 条件分页查询
     * @return
     */
    public Page<CommissionDailyStatementRecord> queryCommissionDailyStatementRecord(Map<String,Object> anMap,int anPageNum, int anPageSize,String anFlag){
        
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
    }
    
    /***
     * 根据日账单编号查询
     * @param anDailyStatementRefNo
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionDailyStatementRecord> queryCommissionDailyStatementRecordByRefNo(Long anDailyStatementId, int anPageNum, int anPageSize,String anFlag){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("dailyStatementId", anDailyStatementId);
        return queryCommissionDailyStatementRecord(map,anPageNum,anPageSize,anFlag);
    }
    
    /***
     * 保存日记录
     * @return
     */
    public boolean addDailyStatementRecord(CommissionDailyStatement anDailyStatement){
        anDailyStatement.setBusinStatus("1");
        return this.mapper.addDailyStatementRecord(anDailyStatement)>0;
    }
    
    public List<CommissionDailyStatementRecord> findDailyStatementRecord(Long anDailyStatementId){
        return this.selectByProperty("dailyStatementId", anDailyStatementId);
    }
    
    public boolean delDailyStatementRecord(Long anDailyStatementId,String anDailyStatementRefNo){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("dailyStatementId", anDailyStatementId);
        map.put("dailyStatementRefNo", anDailyStatementRefNo);
        return this.deleteByExample(map)>0;
    }
    
}
