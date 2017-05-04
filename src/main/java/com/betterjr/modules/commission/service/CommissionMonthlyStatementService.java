package com.betterjr.modules.commission.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.commission.dao.CommissionMonthlyStatementMapper;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;

/***
 * 月报表服务类型
 * @author hubl
 *
 */
@Service
public class CommissionMonthlyStatementService extends BaseService<CommissionMonthlyStatementMapper, CommissionMonthlyStatement> {

    public CommissionMonthlyStatement saveComissionMonthlyStatement(Map<String,Object> anMap){
        
        return null;
    }
    
}
