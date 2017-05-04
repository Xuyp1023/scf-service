package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.commission.dao.CommissionDailyStatementMapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
/***
 * 日账单服务类
 * @author hubl
 *
 */
@Service
public class CommissionDailyStatementService  extends BaseService<CommissionDailyStatementMapper, CommissionDailyStatement>{

    /***
     * 根据先择的对账月份查询
     * @param anMonth
     * @return
     */
    public List<CommissionDailyStatement> findCpsDailyStatementByMonth(String anMonth){
        anMonth=anMonth.replaceAll("-", "");
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("GTEpayDate", anMonth+"01");
        monthMap.put("LTEpayDate", anMonth+"31");
        monthMap.put("businStatus", "2");
        logger.debug("monthMap:"+monthMap);
        return findCpsDailyStatement(monthMap);
    }
    
    
    /***
     * 查询日对账单列表
     * @param anMap 查询条件
     * @return 账单列表
     */
    public List<CommissionDailyStatement> findCpsDailyStatement(Map<String, Object> anMap){
        return this.selectByProperty(anMap);
    }
    
    /***
     * 根据凭证编号查询日账单信息
     * @param anRefNo
     * @return
     */
    public CommissionDailyStatement findDailyStatement(String anRefNo){
        List<CommissionDailyStatement> list=this.selectByProperty("refNo", anRefNo);
        return Collections3.getFirst(list);
    }

}
