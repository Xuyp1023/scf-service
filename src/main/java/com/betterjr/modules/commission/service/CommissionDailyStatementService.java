package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionDailyStatementMapper;
import com.betterjr.modules.commission.data.CalcPayResult;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionDailyStatementRecord;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;
import com.betterjr.modules.commission.util.CommissionDateUtils;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.generator.SequenceFactory;
/***
 * 日账单服务类
 * @author hubl
 *
 */
@Service
public class CommissionDailyStatementService  extends BaseService<CommissionDailyStatementMapper, CommissionDailyStatement>{
    
    @Autowired
    private CommissionPayResultRecordService payResultRecordService;
//    @Resource
//    private DomainAttributeDubboClientService domainAttributeDubboClientService;
    @Reference(interfaceClass=IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeDubboClientService;
    @Autowired
    private CustAccountService custAccountService; 
    @Autowired
    private CommissionDailyStatementRecordService dailyStatementRecordService;
    
    public Page<CommissionDailyStatement> queryDailyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize){
        Map<String,Object> paramMap=new HashMap<String, Object>();
        
        if(BetterStringUtils.isNotBlank((String)anParam.get("GTEregDate"))){
            paramMap.put("GTEpayDate", anParam.get("GTEregDate"));
            paramMap.put("LTEpayDate", anParam.get("LTEregDate"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("custNo"))){
            paramMap.put("ownCustNo", anParam.get("custNo"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("businStatus"))){
            paramMap.put("businStatus", anParam.get("businStatus"));
        }else{
            paramMap.put("businStatus", new String[]{"0","1","2","9"});
        }
        Page<CommissionDailyStatement> monthlyStatement=this.selectPropertyByPage(paramMap, anPageNum, anPageSize, "1".equals(anParam.get("flag")),"id desc");
        return monthlyStatement;
    }
    
    /***
     * 根据先择的对账月份查询
     * @param anMonth
     * @return
     * @throws ParseException 
     */
    public List<CommissionDailyStatement> findCpsDailyStatementByMonth(String anMonth,Long anCustNo) throws ParseException{
        anMonth=anMonth.replaceAll("-", "")+"01";
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("GTEpayDate", CommissionDateUtils.getMinMonthDate(anMonth));
        monthMap.put("LTEpayDate", CommissionDateUtils.getMaxMonthDate(anMonth));
        monthMap.put("businStatus", "2");
        monthMap.put("ownCustNo", anCustNo);
        logger.debug("monthMap:"+monthMap);
        return findCpsDailyStatement(monthMap);
    }
    
    
    /***
     * 条件查询查询日对账单列表
     * @param anMap 查询条件
     * @return 账单列表
     */
    public List<CommissionDailyStatement> findCpsDailyStatement(Map<String, Object> anMap){
        return this.selectByProperty(anMap);
    }
    
    /***
     * 查询日报表的统计数据
     * @param anMonth
     * @return 统计结果
     * @throws ParseException 
     */
    public CalcPayResult findDailyStatementCount(String anMonth,Long anCustNo) throws ParseException{
        anMonth=anMonth.replaceAll("-", "")+"01";
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("startDate", CommissionDateUtils.getMinMonthDate(anMonth));
        monthMap.put("endDate", CommissionDateUtils.getMaxMonthDate(anMonth));
        monthMap.put("ownCustNo", anCustNo);
        CalcPayResult payResult=this.mapper.selectDailyStatementCount(monthMap); // 查询所有记录数
        logger.info("payResult:"+payResult);
        return payResult;
    }
    
    /***
     * 新增月账单时点击下一步时查询显示日账单的基础信息调用
     * @param anParam
     * @return
     * @throws ParseException 
     */
    public Map<String,Object> findDailyStatementBasicsInfo(Map<String,Object> anParam) throws ParseException{
        String anMonth=anParam.get("billMonth").toString();
        Long anOwnCustNo=Long.parseLong(anParam.get("custNo").toString());
        String anEndInterestDate=(String)anParam.get("endInterestDate"); // 结息日期
        String month=anMonth.replaceAll("-", "")+"01";
        String startDate=CommissionDateUtils.getMinMonthDate(month);
        String endDate=CommissionDateUtils.getMaxMonthDate(month);
        // 根据对账月份查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("startDate", startDate);
        monthMap.put("endDate", endDate);
        monthMap.put("ownCustNo", anOwnCustNo);
        /**
         *  检查条件
         *  1、判断当前时间要大于朋末时间
         *  2、未生效账单必须为0
         */
        long time = new Date().getTime()-BetterDateUtils.parseDate(CommissionDateUtils.getMaxMonthDate(month)).getTime();
        BTAssert.isTrue(time>0, "当前日期要大于对账月份的月末日期");
        CalcPayResult payResult=this.mapper.selectDailyStatementCount(monthMap);
        
        Long failureTotalCount=payResult.getPayFailureAmount();
        BTAssert.isTrue(failureTotalCount<0, "日账单存在未生效数据，不能生成月账单");
        
        BigDecimal totalBalance=new BigDecimal(0.00);// 总金额
        BigDecimal totalPayBalance=new BigDecimal(0.00);// 总发生金额
        BigDecimal totalInterset=new BigDecimal(0.00);// 总利息
        BigDecimal totalTaxBalance=new BigDecimal(0.00);// 总税额
        List<CommissionDailyStatement> resultDailyStatementList=new ArrayList<CommissionDailyStatement>();
        
        monthMap=getConfigData();
        
        BigDecimal rate=new BigDecimal((String)monthMap.get("interestRate")); 
        BigDecimal taxAmount=new BigDecimal((String)monthMap.get("taxRate"));
        
        // 获取日账单的列表，并计算好利息
        List<CommissionDailyStatement> dailyStatementList=findCpsDailyStatementByMonth(anMonth,anOwnCustNo);
        for(CommissionDailyStatement dailyStatement:dailyStatementList){
            BigDecimal payTotalBalance= dailyStatement.getPayTotalBalance();
            String payDate=dailyStatement.getPayDate();
            long lTerm = BetterDateUtils.parseDate(anEndInterestDate).getTime()-BetterDateUtils.parseDate(payDate).getTime();
            BigDecimal term=new BigDecimal(lTerm);
            BigDecimal d=term.divide(new BigDecimal(100)).divide(new BigDecimal(360));
            BigDecimal interset=MathExtend.multiply(MathExtend.multiply(payTotalBalance,rate), d) ;
            BigDecimal taxBalance=MathExtend.add(payTotalBalance, interset).multiply(taxAmount.divide(new BigDecimal(100)));
            logger.info("每日利息：dailyStatement refNo:"+dailyStatement.getRefNo()+"，interset:"+interset+"，每日税额："+taxBalance);
            totalBalance=MathExtend.add(totalBalance, payTotalBalance).add(interset).add(taxBalance);
            totalPayBalance=MathExtend.add(totalPayBalance, payTotalBalance);
            totalInterset=MathExtend.add(totalInterset, interset);
            totalTaxBalance=MathExtend.add(totalTaxBalance, taxBalance);
            // 更新日报表利息
            dailyStatement.setInterest(interset);
            dailyStatement.setInterestRate(rate);
            dailyStatement.setEndInterestDate(anEndInterestDate);
            this.updateByPrimaryKey(dailyStatement);
            resultDailyStatementList.add(dailyStatement);
        }
        
        monthMap.put("payBeginDate", startDate);
        monthMap.put("payEndDate", endDate);
        monthMap.put("ownCustNo", anOwnCustNo);
        monthMap.put("ownCustName", custAccountService.queryCustName(anOwnCustNo));
        monthMap.put("monthlyRefNo", SequenceFactory.generate("CommissionMonthlyStatement.refNo", "#{Date:yyyyMMdd}#{Seq:12}", "MB"));
        monthMap.put("totalBalance", totalBalance);
        monthMap.put("payTotalBalance", totalPayBalance);
        monthMap.put("totalInterset", totalInterset);
        monthMap.put("totalTaxBalance", totalTaxBalance);
        monthMap.put("dailyList", resultDailyStatementList);
        monthMap.put("makeDateTime", BetterDateUtils.getDateTime());
        monthMap.put("endInterestDate", anEndInterestDate);       
        
        return monthMap;
    }
    
    /***
     * 查询日账单统计的数据
     * @param anMap
     * @return
     */
    public CalcPayResult findDailyStatementCount(Map<String,Object> anMap){
        return this.mapper.selectDailyStatementCount(anMap);
    }
    
    /***
     * 审核/作废
     * @param andailyStatementId
     * @param anBusinStatus
     * @return
     */
    public boolean saveDailyStatementById(Long anDailyStatementId,String anBusinStatus){
        CommissionDailyStatement dailyStatement=this.selectByPrimaryKey(anDailyStatementId);
        dailyStatement.setLastStatus(dailyStatement.getBusinStatus());
        dailyStatement.setBusinStatus(anBusinStatus);
        return this.updateByPrimaryKey(dailyStatement)>0;
    }
    
    public boolean delDailyStatement(Long anDailyStatementId){
        return this.deleteByPrimaryKey(anDailyStatementId)>0;
    }

    /***
     * 查询佣金记录数
     * @param anPayDate
     * @param anOwnCustNo
     * @return
     */
    public CalcPayResult findPayResultCount(String anPayDate,Long anOwnCustNo){
        return payResultRecordService.calcPayResultRecord(anOwnCustNo, anPayDate);
    }
    
    /***
     * 查询支付佣金记录
     * @param anOwnCustNo
     * @param anPayDate
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionPayResultRecord> queryPayResultRecord(Long anOwnCustNo,String anPayDate,int anFlag, int anPageNum, int anPageSize){
        
        return payResultRecordService.queryAllPayResultRecords(anOwnCustNo, anPayDate, anFlag, anPageNum, anPageSize);
    }
    
    /***
     * 新增日账单下一步操作
     * @param anPayDate
     * @param anOwnCustNo
     * @return
     */
    public Map<String, Object> findPayResultInfo(String anPayDate,Long anOwnCustNo){
         CalcPayResult payResult = payResultRecordService.calcPayResultRecord(anOwnCustNo, anPayDate);
         Long payFailureAmount=payResult.getPayFailureAmount();
         BTAssert.isTrue(payFailureAmount<=0, "佣金支付结果存在未生效数据，不能生成日账单");
         
         Map<String, Object> resultMp=new HashMap<String, Object>();
         resultMp=getConfigData();
         final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
         resultMp.put("dailyRefNo",SequenceFactory.generate("PLAT_COMMISSION_DAILY_REFNO",custOperator.getOperOrg(), "DB#{Date:yyyyMMdd}#{Seq:8}", "D"));
         resultMp.put("totalBalance", payResult.getTotalBalance());
         resultMp.put("payDate", anPayDate);
         resultMp.put("ownCustNo", anOwnCustNo);
         resultMp.put("ownCustName", custAccountService.queryCustName(anOwnCustNo));
         resultMp.put("totalAmount", payResult.getTotalAmount());
         resultMp.put("makeDateTime", BetterDateUtils.getDateTime());
         return resultMp;
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

    public CommissionDailyStatement saveDailyStatement(String anDailyRefNo,String anPayDate,Long anOwnCustNo){
        CommissionDailyStatement dailyStatement=new CommissionDailyStatement();
        dailyStatement.setRefNo(anDailyRefNo);
        dailyStatement.setPayDate(anPayDate);
        dailyStatement.setOwnCustNo(anOwnCustNo);
        dailyStatement.setOwnCustName(custAccountService.queryCustName(anOwnCustNo));
        Map<String,Object> configMap=getConfigData();
        dailyStatement.setMakeCustName((String)configMap.get("makeCustName"));
        dailyStatement.setOperName((String)configMap.get("makeOperName"));
        
        CalcPayResult payResult= payResultRecordService.calcPayResultRecord(anOwnCustNo, anPayDate);
        
        dailyStatement.setTotalBalance(payResult.getTotalBalance()==null?new BigDecimal(0):payResult.getTotalBalance());
        dailyStatement.setTotalAmount(new BigDecimal(payResult.getTotalAmount()));
        dailyStatement.setPayTotalBalance(payResult.getTotalBalance()==null?new BigDecimal(0):payResult.getTotalBalance());
        dailyStatement.setPayTotalAmount(new BigDecimal(payResult.getTotalAmount()));
        dailyStatement.setPaySuccessAmount(new BigDecimal(payResult.getPaySuccessAmount()));
        dailyStatement.setPaySuccessBalance(payResult.getPaySuccessBalance()==null?new BigDecimal(0):payResult.getPaySuccessBalance());
        dailyStatement.setPayFailureBalance(payResult.getPayFailureBalance()==null?new BigDecimal(0):payResult.getPayFailureBalance());
        dailyStatement.setPayFailureAmount(new BigDecimal(payResult.getPayFailureAmount()));
        dailyStatement.initValue();
        
        this.insert(dailyStatement);
        
        // 添加日报表记录
        dailyStatementRecordService.addDailyStatementRecord(dailyStatement);
        
        // 回写生成日报表的记录状态        
        Map<String,Object> anMap=new HashMap<String, Object>();
        anMap.put("businStatus", "3");
        anMap.put("payDate", anPayDate);
        anMap.put("ownCustNo", anOwnCustNo);
        BTAssert.isTrue(payResultRecordService.saveRecordStatus(anMap)>0, "回写佣金状态记录异常");
        
        return dailyStatement;
    }
    
    public Map<String,Object> findDailyStatementById(Long anDailyStatementId){
        CommissionDailyStatement dailyStatement=this.selectByPrimaryKey(anDailyStatementId);
        Map<String,Object> infoMp=new HashMap<String, Object>();
        infoMp.put("ownCustNo", dailyStatement.getOwnCustNo());
        infoMp.put("ownCustName", dailyStatement.getOwnCustName());
        infoMp.put("dailyRefNo", dailyStatement.getRefNo());
        infoMp.put("totalBalance", dailyStatement.getTotalBalance());
        infoMp.put("totalAmount", dailyStatement.getTotalAmount());
        infoMp.put("payDate", dailyStatement.getPayDate());
        infoMp.put("id", anDailyStatementId);
        infoMp.put("makeCustName", dailyStatement.getMakeCustName());
        infoMp.put("operName", dailyStatement.getOperName());
        infoMp.put("makeDateTime", BetterDateUtils.formatDispDate(dailyStatement.getMakeDate())+" "+BetterDateUtils.formatDispTime(dailyStatement.getMakeTime()));
        return infoMp;
    }
    
    public Page<CommissionDailyStatementRecord> queryDailyStatementRecordByDailyId(Long anDailyStatementId, int anPageNum, int anPageSize,String anFlag){
        return dailyStatementRecordService.queryCommissionDailyStatementRecordByRefNo(anDailyStatementId, anPageNum, anPageSize, anFlag);
    }
    
}
