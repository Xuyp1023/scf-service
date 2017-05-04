package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionDailyStatementRecordMapper;
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
    public Page<CommissionDailyStatementRecord> queryCommissionDailyStatementRecord(Map<String,Object> anMap,int anPageNum, int anPageSize){
        
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anMap.get("flag")));
    }
    
    /***
     * 根据日账单的凭证编号查询
     * @param anDailyStatementRefNo
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionDailyStatementRecord> queryCommissionDailyStatementRecordByRefNo(String anDailyStatementRefNo, int anPageNum, int anPageSize){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("dailyStatementRefNo", anDailyStatementRefNo);
        map.put("businStatus", "2");
        map.put("flag", "1");
        return queryCommissionDailyStatementRecord(map,anPageNum,anPageSize);
    }
    
}
