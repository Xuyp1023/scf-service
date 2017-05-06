package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.commission.dao.CommissionMonthlyStatementRecordMapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatementRecord;
/**
 * 月报表记录
 * @author hubl
 *
 */

@Service
public class CommissionMonthlyStatementRecordService extends BaseService<CommissionMonthlyStatementRecordMapper, CommissionMonthlyStatementRecord> {

    /***
     * 添加月记录
     * @return
     */
    public boolean addMonthlyRecord(CommissionDailyStatement anDailyStatement,Long anMonthId,String anMonthlyRefNo){
        CommissionMonthlyStatementRecord monthlyStatementRecord=new CommissionMonthlyStatementRecord();
        monthlyStatementRecord.initValue(anDailyStatement);
        monthlyStatementRecord.setMonthlyStatementId(anMonthId);
        monthlyStatementRecord.setMonthlyStatementRefNo(anMonthlyRefNo);
        return this.insert(monthlyStatementRecord)>0;
    }
    
    public List<CommissionMonthlyStatementRecord> findMonthlyStatementRecord(Long anMonthlyStatementId,String anMonthlyStatementRefNo){
        Map<String, Object> anMap=new HashMap<String, Object>();
        anMap.put("monthlyStatementId", anMonthlyStatementId);
        anMap.put("monthlyStatementRefNo", anMonthlyStatementRefNo);
        return this.selectByProperty(anMap);
    }
}
