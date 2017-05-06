package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionMonthlyStatementMapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatementRecord;
import com.betterjr.modules.commission.util.CommissionDateUtils;
import com.betterjr.modules.generator.SequenceFactory;

/***
 * 月报表服务类型
 * @author hubl
 *
 */
@Service
public class CommissionMonthlyStatementService extends BaseService<CommissionMonthlyStatementMapper, CommissionMonthlyStatement> {

    @Autowired
    private CommissionDailyStatementService dailyStatementService;
    @Autowired
    private CommissionMonthlyStatementRecordService monthlyRecordService;
    
    /***
     * 保存月报表记录
     * @param anParam
     * @return
     * @throws ParseException 
     */
    public CommissionMonthlyStatement saveComissionMonthlyStatement(Map<String,Object> anParam) throws ParseException{
        // 添加月账单主表信息
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("GTEpayDate", anParam.get("startDate"));
        monthMap.put("LTEpayDate", anParam.get("endDate"));
        monthMap.put("businStatus", "2");
        monthMap.put("ownCustNo", anParam.get("ownCustNo"));
        CommissionMonthlyStatement monthlyStatement=new CommissionMonthlyStatement();
        monthlyStatement.initMonthlyStatement(anParam);
        Map countMap=dailyStatementService.findDailyStatementCount(anParam);
        monthlyStatement.setTotalAmount(new BigDecimal((String)countMap.get("allTotalCount")));
        monthlyStatement.setPayTotalAmount(new BigDecimal((String)countMap.get("allTotalCount")));
        monthlyStatement.setPaySuccessAmount(new BigDecimal((String)countMap.get("successTotalCount")));
        monthlyStatement.setPaySuccessBalance(new BigDecimal((String)countMap.get("successTotalBalance")));
        monthlyStatement.setPayFailureAmount(new BigDecimal((String)countMap.get("failureTotalCount")));
        monthlyStatement.setPayFailureBalance(new BigDecimal((String)countMap.get("failureTotalBalance")));
        monthlyStatement.setMakeCustName("");// 参数表中获取
        monthlyStatement.setMakeOperName("");// 参数表中获取
        monthlyStatement.setInterestRate(new BigDecimal("0.00"));// 参数表中获取
        monthlyStatement.setTaxRate(new BigDecimal("0.00"));// 参数表中获取
        
        this.insert(monthlyStatement);
        
        List<CommissionDailyStatement> dailyStatementList=dailyStatementService.findCpsDailyStatement(monthMap);
        for(CommissionDailyStatement dailyStatement:dailyStatementList){
            if(!monthlyRecordService.addMonthlyRecord(dailyStatement, monthlyStatement.getId(), monthlyStatement.getRefNo())){
                throw new BytterTradeException("月记录添加失败");
            }
            // 更新日报表状态
            dailyStatement.setLastStatus(dailyStatement.getBusinStatus());
            dailyStatement.setBusinStatus("3");
            dailyStatementService.updateByPrimaryKey(dailyStatement);
        }
        return monthlyStatement;
    }

    /***
     * 分页查询月报表信息
     * @param anParam
     * @param anPageNum
     * @param anPageSize
     * @return
     * @throws ParseException
     */
    public Page<CommissionMonthlyStatement> queryMonthlyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize) throws ParseException{
        Map<String,Object> paramMap=new HashMap<String, Object>();
        String anMonth=(String)anParam.get("month");
        if(BetterStringUtils.isNotBlank(anMonth)){
            anMonth=anMonth.replaceAll("-", "")+"01";
            paramMap.put("payBeginDate", CommissionDateUtils.getMinMonthDate(anMonth));
            paramMap.put("payEndDate", CommissionDateUtils.getMaxMonthDate(anMonth));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("ownCustNo"))){
            paramMap.put("ownCustNo", anParam.get("ownCustNo"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("businStatus"))){
            paramMap.put("businStatus", anParam.get("businStatus"));
        }
        Page<CommissionMonthlyStatement> monthlyStatement=this.selectPropertyByPage(paramMap, anPageNum, anPageSize, "1".equals(anParam.get("flag")),"id desc");

        return monthlyStatement;
    }
    
    /**
     * 查询需要添加到快递中的月账单，并且修改月账单状态
     * @param anParam
     * @return
     */
    public List<CommissionMonthlyStatement> saveQueryMonthlyStatement(Map<String, Object> anParam){
        
        List<CommissionMonthlyStatement> monthlyStatement=this.selectByProperty(anParam);
        for (CommissionMonthlyStatement statement : monthlyStatement) {
            statement.setBusinStatus("3");
            this.updateByPrimaryKeySelective(statement);
        }
        return monthlyStatement;
    }
    
    /***
     * 根据id查询月报表信息
     * @param anMonthlyId
     * @return
     */
    public Map<String,Object> findMonthlyStatementById(Long anMonthlyId){
        Map<String,Object> monthMap=new HashMap<String, Object>();
        CommissionMonthlyStatement monthlyStatement=this.selectByPrimaryKey(anMonthlyId);
        
        // 查询日记录列表
        List<CommissionMonthlyStatementRecord> monthlyRecordList=monthlyRecordService.findMonthlyStatementRecord(monthlyStatement.getId(), monthlyStatement.getRefNo());
        monthMap.put("ownCustName", monthlyStatement.getOwnCustName());
        monthMap.put("monthlyRefNo", monthlyStatement.getRefNo());
        monthMap.put("totalBalance", monthlyStatement.getTotalBalance());
        monthMap.put("payTotalBalance", monthlyStatement.getPayTotalBalance());
        monthMap.put("totalInterset", monthlyStatement.getInterest());
        monthMap.put("totalTaxBalance", monthlyStatement.getTaxBalance());
        monthMap.put("dailyList", monthlyRecordList);
        monthMap.put("makeCustName", monthlyStatement.getMakeCustName());// 参数表中获得
        monthMap.put("makeDateTime", BetterDateUtils.formatDispDate(monthlyStatement.getMakeDate())+BetterDateUtils.formatDispTime(monthlyStatement.getMakeTime()));
        monthMap.put("endInterestDate", monthlyStatement.getEndInterestDate());  
        return monthMap;
    }
    
    /***
     * 审核/作废账单
     * @param anMonthlyId
     * @param anBusinStatus
     * @return
     */
    public boolean saveMonthlyStatement(Long anMonthlyId,String anBusinStatus){
        CommissionMonthlyStatement monthlyStatement=this.selectByPrimaryKey(anMonthlyId);
        monthlyStatement.setLastStatus(monthlyStatement.getBusinStatus());
        monthlyStatement.setBusinStatus(anBusinStatus);
        return this.updateByPrimaryKey(monthlyStatement)>0;
    }
    
    public boolean delMonthlyStatement(Long anMonthlyId){
        return this.deleteByPrimaryKey(anMonthlyId)>0;
    }
    
}
