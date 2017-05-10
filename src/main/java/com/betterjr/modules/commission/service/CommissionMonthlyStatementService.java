package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.dao.CommissionMonthlyStatementMapper;
import com.betterjr.modules.commission.data.CalcPayResult;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatementRecord;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;

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
//    @Resource
//    private DomainAttributeDubboClientService domainAttributeDubboClientService;
    @Reference(interfaceClass=IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeDubboClientService;
    
    /***
     * 保存月报表记录
     * @param anParam
     * @return
     * @throws ParseException 
     */
    public CommissionMonthlyStatement saveComissionMonthlyStatement(Map<String,Object> anParam) throws ParseException{
        // 添加月账单主表信息
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("GTEpayDate", anParam.get("payBeginDate"));
        monthMap.put("LTEpayDate", anParam.get("payEndDate"));
        monthMap.put("businStatus", "2");
        monthMap.put("ownCustNo", anParam.get("ownCustNo"));
        CommissionMonthlyStatement monthlyStatement=new CommissionMonthlyStatement();
        monthlyStatement.initMonthlyStatement(anParam);
        CalcPayResult payResult=dailyStatementService.findDailyStatementCount(anParam);
        monthlyStatement.setTotalAmount(new BigDecimal(payResult.getTotalAmount()));
        monthlyStatement.setPayTotalAmount(new BigDecimal(payResult.getTotalAmount()));
        monthlyStatement.setPaySuccessAmount(new BigDecimal(payResult.getPaySuccessAmount()));
        monthlyStatement.setPaySuccessBalance(payResult.getPaySuccessBalance()==null?new BigDecimal(0):payResult.getPaySuccessBalance());
        monthlyStatement.setPayFailureAmount(new BigDecimal(payResult.getPayFailureAmount()));
        monthlyStatement.setPayFailureBalance(payResult.getPayFailureBalance()==null?new BigDecimal(0):payResult.getPayFailureBalance());
        
        Map<String,Object> configMap=getConfigData();
        monthlyStatement.setMakeCustName(configMap.get("makeCustName").toString());
        monthlyStatement.setMakeOperName(configMap.get("makeOperName").toString());
        monthlyStatement.setInterestRate(new BigDecimal(configMap.get("interestRate").toString()));
        monthlyStatement.setTaxRate(new BigDecimal(configMap.get("taxRate").toString()));
        
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

        if(BetterStringUtils.isNotBlank((String)anParam.get("billMonth"))){
            paramMap.put("billMonth", anParam.get("billMonth"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("custNo"))){
            paramMap.put("ownCustNo", anParam.get("custNo"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("businStatus"))){
            paramMap.put("businStatus", anParam.get("businStatus"));
        }else{
            paramMap.put("businStatus", new String[]{"0","1","2","9"});
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
        monthMap.put("payBeginDate", monthlyStatement.getPayBeginDate());
        monthMap.put("payEndDate", monthlyStatement.getPayEndDate());
        monthMap.put("totalBalance", monthlyStatement.getTotalBalance());
        monthMap.put("payTotalBalance", monthlyStatement.getPayTotalBalance());
        monthMap.put("totalInterset", monthlyStatement.getInterest());
        monthMap.put("totalTaxBalance", monthlyStatement.getTaxBalance());
        monthMap.put("dailyList", monthlyRecordList);
        monthMap.put("makeCustName", monthlyStatement.getMakeCustName());
        monthMap.put("operName", monthlyStatement.getMakeOperName());
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
    
    /***
     * 根据对账月份查询
     * @param anBillMonth
     * @param ownCustNo
     * @return
     */
    public List<CommissionMonthlyStatement> findMonthlyStatementByMonth(String anBillMonth,Long ownCustNo){
        Map<String,Object> anMap=new HashMap<String, Object>();
        anMap.put("billMonth", anBillMonth);
        anMap.put("ownCustNo", ownCustNo);
        return this.selectByProperty(anMap);
    }
    
    /***
    * 获取参数表里面的信息
    * @return
    */
   public Map<String,Object> getConfigData(){
       Map<String, Object> map=new HashMap<String, Object>();
       final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
       final String cusrName = domainAttributeDubboClientService.findString(custOperator.getOperOrg(), "PLAT_COMMISSION_MAKE_CUSTNAME");
       final String operator = domainAttributeDubboClientService.findString(custOperator.getOperOrg(), "PLAT_COMMISSION_MAKE_OPERATOR");
       final BigDecimal interestRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(), "PLAT_COMMISSION_INTEREST_RATE");
       final BigDecimal taxRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(), "PLAT_COMMISSION_TAX_RATE");

       map.put("makeCustName", cusrName);
       map.put("makeOperName", operator);
       map.put("interestRate", interestRate);
       map.put("taxRate", taxRate);
       
       return map;
   }
    
}
