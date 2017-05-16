package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.JsonMapper;
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
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;
import com.betterjr.modules.commission.util.CommissionDateUtils;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.flie.service.FileDownService;
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
    @Autowired
    private CommissionMonthlyStatementService monthlyStatementService;
    @Autowired
    private FileDownService fileDownService;
    
    public Page<CommissionDailyStatement> queryDailyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
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
            paramMap.put("businStatus", new String[]{"0","1","2","3","9"});
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
    public List<CommissionDailyStatement> findCpsDailyStatementByMonth(String anMonth,Long anCustNo,String anBusinStatus) throws ParseException{
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        anMonth=anMonth.replaceAll("-", "")+"01";
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("GTEpayDate", CommissionDateUtils.getMinMonthDate(anMonth));
        monthMap.put("LTEpayDate", CommissionDateUtils.getMaxMonthDate(anMonth));
        if(BetterStringUtils.isNotBlank(anBusinStatus)){
            monthMap.put("businStatus", anBusinStatus);
        }else{
            monthMap.put("businStatus", new String[]{"0","1","2"});
        }
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
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        anMonth=anMonth.replaceAll("-", "")+"01";
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("payBeginDate", CommissionDateUtils.getMinMonthDate(anMonth));
        monthMap.put("payEndDate", CommissionDateUtils.getMaxMonthDate(anMonth));
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
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        String billMonth=anParam.get("billMonth").toString();
        Long anOwnCustNo=Long.parseLong(anParam.get("custNo").toString());
        String anEndInterestDate=(String)anParam.get("endInterestDate"); // 结息日期
        String month=billMonth.replaceAll("-", "")+"01";
        String startDate=CommissionDateUtils.getMinMonthDate(month);
        String endDate=CommissionDateUtils.getMaxMonthDate(month);
        // 根据对账月份查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("payBeginDate", startDate);
        monthMap.put("payEndDate", endDate);
        monthMap.put("ownCustNo", anOwnCustNo);

        long time = BetterDateUtils.parseDate(BetterDateUtils.getNumMonth()).getTime()-BetterDateUtils.parseDate(billMonth).getTime();
        if(time<=0){
            throw new BytterTradeException("对账月份必须小于当前月");
        }
        CalcPayResult payResult=this.mapper.selectDailyStatementCount(monthMap);
        
        Long failureTotalCount=payResult.getPayFailureAmount();
        if(failureTotalCount>0){
            throw new BytterTradeException("日账单存在未生效数据，不能生成月账单");
        }
        
        List<CommissionMonthlyStatement> monthlyList=monthlyStatementService.findMonthlyStatementByMonth(billMonth,anOwnCustNo);
        if(monthlyList!=null && monthlyList.size()>0){
            throw new BytterTradeException("该月对账单已经生成");
        }
        
        BigDecimal totalBalance=new BigDecimal(0.00);// 总金额
        BigDecimal totalPayBalance=new BigDecimal(0.00);// 总发生金额
        BigDecimal totalInterset=new BigDecimal(0.00);// 总利息
        BigDecimal totalTaxBalance=new BigDecimal(0.00);// 总税额
        BigDecimal interestBalance=new BigDecimal(0.00);// 结算金额
        BigDecimal paySuccessBalance=new BigDecimal(0.00);// 成功金额
        
        List<CommissionDailyStatement> resultDailyStatementList=new ArrayList<CommissionDailyStatement>();
        
        monthMap=getConfigData();
        
        BigDecimal rate=new BigDecimal(monthMap.get("interestRate").toString()); 
        BigDecimal taxRate=new BigDecimal(monthMap.get("taxRate").toString());
        
        
        
        // 获取日账单的列表，并计算好利息
        List<CommissionDailyStatement> dailyStatementList=findCpsDailyStatementByMonth(billMonth,anOwnCustNo,"2");
        for(CommissionDailyStatement dailyStatement:dailyStatementList){
            BigDecimal payTotalBalance= dailyStatement.getPayTotalBalance();
            String payDate=dailyStatement.getPayDate();
            long lTerm = BetterDateUtils.parseDate(anEndInterestDate).getTime()-BetterDateUtils.parseDate(payDate).getTime();
            lTerm=lTerm/(24*60*60*1000);
            BigDecimal term=new BigDecimal(lTerm);
            
            BigDecimal interset=getInterset(payTotalBalance,rate,term);
            BigDecimal taxBalance = getTaxBalance(payTotalBalance,interset,taxRate);
            
            logger.info("每日利息：dailyStatement refNo:"+dailyStatement.getRefNo()+"，interset:"+interset+"，每日税额："+taxBalance);
            totalBalance=MathExtend.add(totalBalance, dailyStatement.getTotalBalance());
            interestBalance=MathExtend.add(MathExtend.add(MathExtend.add(interestBalance, payTotalBalance), interset),taxBalance);
            totalPayBalance=MathExtend.add(totalPayBalance, payTotalBalance);
            totalInterset=MathExtend.add(totalInterset, interset);
            totalTaxBalance=MathExtend.add(totalTaxBalance, taxBalance);
            paySuccessBalance=MathExtend.add(dailyStatement.getPaySuccessBalance(), paySuccessBalance);
            // 更新日报表利息
            dailyStatement.setInterest(interset);
            dailyStatement.setInterestRate(rate);
            dailyStatement.setEndInterestDate(anEndInterestDate);
            this.updateByPrimaryKey(dailyStatement);
            resultDailyStatementList.add(dailyStatement);
        }
        
        monthMap.put("billMonth",billMonth);
        monthMap.put("payBeginDate", startDate);
        monthMap.put("payEndDate", endDate);
        monthMap.put("ownCustNo", anOwnCustNo);
        monthMap.put("ownCustName", custAccountService.queryCustName(anOwnCustNo));
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        monthMap.put("refNo", SequenceFactory.generate("PLAT_COMMISSION_MONTHLY_REFNO",custOperator.getOperOrg(), "MB#{Date:yyyyMM}#{Seq:10}", "M"));
        monthMap.put("totalBalance", totalBalance);
        monthMap.put("payTotalBalance", totalPayBalance);
        monthMap.put("interest", totalInterset);
        monthMap.put("taxBalance", totalTaxBalance);
        monthMap.put("dailyList", resultDailyStatementList);
        monthMap.put("makeDateTime", BetterDateUtils.getDateTime());
        monthMap.put("endInterestDate", anEndInterestDate);
        monthMap.put("interestBalance", interestBalance);
        monthMap.put("paySuccessBalance", paySuccessBalance);
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
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        CommissionDailyStatement dailyStatement=this.selectByPrimaryKey(anDailyStatementId);
        dailyStatement.setLastStatus(dailyStatement.getBusinStatus());
        dailyStatement.setBusinStatus(anBusinStatus);
        return this.updateByPrimaryKey(dailyStatement)>0;
    }
    
    public boolean delDailyStatement(Long anDailyStatementId){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        return this.deleteByPrimaryKey(anDailyStatementId)>0;
    }

    /***
     * 查询佣金记录数
     * @param anPayDate
     * @param anOwnCustNo
     * @return
     */
    public CalcPayResult findPayResultCount(String anPayDate,Long anOwnCustNo){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
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
    public Page<CommissionPayResultRecord> queryPayResultRecord(Long anOwnCustNo,String anPayDate,String anPayStatus,int anFlag, int anPageNum, int anPageSize){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        return payResultRecordService.queryAllPayResultRecords(anOwnCustNo, anPayDate,anPayStatus, anFlag, anPageNum, anPageSize);
    }
    
    /***
     * 新增日账单下一步操作
     * @param anPayDate
     * @param anOwnCustNo
     * @return
     */
    public Map<String, Object> findPayResultInfo(String anPayDate,Long anOwnCustNo){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
         if(findDailyStatementByPayDate(anPayDate, anOwnCustNo)){
             throw new BytterTradeException("当前日期已经生成日对账单");
         }
         
         CalcPayResult payResult = payResultRecordService.calcPayResultRecord(anOwnCustNo, anPayDate);
         Long unconfirmAmount=payResult.getUnconfirmAmount();
         BTAssert.isTrue(unconfirmAmount<=0, "佣金支付结果存在未确认数据，不能生成日账单");
         Long totalAmount=payResult.getTotalAmount();
         BTAssert.isTrue(totalAmount!=0, "没有查到佣金支付数据");
         String result=payResultRecordService.findAllAuditResult(anOwnCustNo, anPayDate);
         if(BetterStringUtils.equals("false", result)){
             throw new BytterTradeException("佣金数据有未复核数据");
         }

//         long time = BetterDateUtils.parseDate(BetterDateUtils.getNumDate()).getTime()-BetterDateUtils.parseDate(anPayDate).getTime();
//         BTAssert.isTrue(time>=0, "当前时间要小于对账月份时间");
         
         
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
         resultMp.put("paySuccessBalance", payResult.getPaySuccessBalance());
         resultMp.put("paySuccessAmount", payResult.getPaySuccessAmount());
         
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
        final String dailyTemplate = domainAttributeDubboClientService.findString("GLOBAL_COMMISSION_DAILY_TEMPLATE");

        map.put("makeCustName", cusrName);
        map.put("makeOperName", operator);
        map.put("interestRate", interestRate);
        map.put("taxRate", taxRate);
        map.put("dailyTemplate", dailyTemplate);
        
        return map;
    }

    public CommissionDailyStatement saveDailyStatement(String anDailyRefNo,String anPayDate,Long anOwnCustNo){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        CommissionDailyStatement dailyStatement=new CommissionDailyStatement();
        dailyStatement.setRefNo(anDailyRefNo);
        dailyStatement.setPayDate(anPayDate);
        dailyStatement.setOwnCustNo(anOwnCustNo);
        dailyStatement.setOwnCustName(custAccountService.queryCustName(anOwnCustNo));
        Map<String,Object> configMap=getConfigData();
        dailyStatement.setMakeCustName((String)configMap.get("makeCustName"));
        dailyStatement.setOperName((String)configMap.get("makeOperName"));
        
        CalcPayResult payResult= payResultRecordService.calcPayResultRecord(anOwnCustNo, anPayDate);
        logger.info("payResult:"+payResult);
        
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
        
        String dailyTemplate=(String)configMap.get("dailyTemplate");
        logger.info("dailyTemplate:"+dailyTemplate);
        Long fileId=0l;
        String fileType="";
        if(dailyTemplate!=null){
            Map<String, Object> templateMp = JsonMapper.parserJson(dailyTemplate);
            fileId=Long.parseLong(templateMp.get("id").toString());
            fileType=(String)templateMp.get("fileType");
            if(BetterStringUtils.isBlank(fileType)){
                fileType=".xlsx";
            }
        }
        
        List<CommissionDailyStatementRecord> recordList=dailyStatementRecordService.findDailyStatementRecord(dailyStatement.getId());
        CommissionDailyStatementRecord record=new CommissionDailyStatementRecord();
        recordList.add(record);
        
        Map<String, Object> fileMap=new HashMap<String, Object>();
        fileMap.put("daily", dailyStatement);
        fileMap.put("recordList", recordList);
        CustFileItem custFile = fileDownService.uploadCommissionRecordFileis(fileMap, fileId, BetterDateUtils.formatDispay(dailyStatement.getPayDate())+"-对账单"+fileType);
        logger.info("生成后的文件，custFile:"+custFile);
        dailyStatement.setFileId(custFile.getId());
        dailyStatement.setBatchNo(custFile.getBatchNo());
        this.updateByPrimaryKey(dailyStatement);
        
        // 回写生成日报表的记录状态        
        Map<String,Object> anMap=new HashMap<String, Object>();
        anMap.put("businStatus", "3");
        anMap.put("payDate", anPayDate);
        anMap.put("ownCustNo", anOwnCustNo);
        BTAssert.isTrue(payResultRecordService.saveRecordStatus(anMap)>0, "回写佣金状态记录异常");
        
        return dailyStatement;
    }
    
    public CommissionDailyStatement findDailyStatementById(Long anDailyStatementId){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        CommissionDailyStatement dailyStatement=this.selectByPrimaryKey(anDailyStatementId);
        dailyStatement.setMakeDateTime(BetterDateUtils.formatDispDate(dailyStatement.getMakeDate()) +" "+BetterDateUtils.formatDispTime(dailyStatement.getMakeTime()));
        return dailyStatement;
    }
    
    public Page<CommissionDailyStatementRecord> queryDailyStatementRecordByDailyId(Long anDailyStatementId, int anPageNum, int anPageSize,String anFlag){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        return dailyStatementRecordService.queryCommissionDailyStatementRecordByRefNo(anDailyStatementId, anPageNum, anPageSize, anFlag);
    }
    
    /***
     * 根据对账日期和对账企业查询日账单
     * @param anPayDate 对账日期
     * @param anOwnCustNo 对账企业客户号
     * @return 是否存在
     */
    public boolean findDailyStatementByPayDate(String anPayDate,Long anOwnCustNo){
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        Map<String,Object> anMap=new HashMap<String, Object>();
        anMap.put("payDate", anPayDate);
        anMap.put("ownCustNo", anOwnCustNo);
        
        List<CommissionDailyStatement> dailyList=this.selectByProperty(anMap);
        if(dailyList!=null && dailyList.size()>0){
           return true;
        }
        return false;
    }
    
    /***
     * 计算利息
     * @param payTotalBalance 金额
     * @param rate 利率
     * @param term 期限
     * @return
     */
    public BigDecimal getInterset(BigDecimal payTotalBalance,BigDecimal rate,BigDecimal term){
        BigDecimal interset=MathExtend.divide(MathExtend.divide(MathExtend.multiply(MathExtend.multiply(payTotalBalance,rate), term), new BigDecimal(100)),new BigDecimal(360));
        return interset;
    }
    
    /***
     * 计算税额
     * @param payTotalBalance
     * @param interset
     * @param taxAmount
     * @return
     */
    public BigDecimal getTaxBalance(BigDecimal payTotalBalance,BigDecimal interset,BigDecimal taxRate){
        return MathExtend.divide(MathExtend.multiply(MathExtend.add(payTotalBalance, interset), taxRate), new BigDecimal(100));
        
    }
    
    
}
