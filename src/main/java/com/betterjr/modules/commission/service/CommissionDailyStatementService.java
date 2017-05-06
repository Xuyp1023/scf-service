package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionDailyStatementMapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.util.CommissionDateUtils;
import com.betterjr.modules.generator.SequenceFactory;
/***
 * 日账单服务类
 * @author hubl
 *
 */
@Service
public class CommissionDailyStatementService  extends BaseService<CommissionDailyStatementMapper, CommissionDailyStatement>{
    
    public Page<CommissionDailyStatement> queryDailyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize){
        Map<String,Object> paramMap=new HashMap<String, Object>();
        
        if(BetterStringUtils.isNotBlank((String)paramMap.get("beginDate"))){
            paramMap.put("GTEpayDate", paramMap.get("beginDate"));
            paramMap.put("LTEpayDate", paramMap.get("endDate"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("ownCustNo"))){
            paramMap.put("ownCustNo", anParam.get("ownCustNo"));
        }
        if(BetterStringUtils.isNotBlank((String)anParam.get("businStatus"))){
            paramMap.put("businStatus", anParam.get("businStatus"));
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
    public Map<String,Object> findDailyStatementCount(String anMonth,Long anCustNo) throws ParseException{
        anMonth=anMonth.replaceAll("-", "")+"01";
        // 不管是几月在将月份改为1-31 号，作为条件查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("startDate", CommissionDateUtils.getMinMonthDate(anMonth));
        monthMap.put("endDate", CommissionDateUtils.getMaxMonthDate(anMonth));
        monthMap.put("ownCustNo", anCustNo);
        Map resultMap=this.mapper.selectDailyStatementCount(monthMap); // 查询所有记录数
        logger.info("resultMap:"+resultMap);
        return resultMap;
    }
    
    /***
     * 新增月账单时点击下一步时查询显示日账单的基础信息调用
     * @param anParam
     * @return
     * @throws ParseException 
     */
    public Map<String,Object> findDailyStatementBasicsInfo(Map<String,Object> anParam) throws ParseException{
        String anMonth=anParam.get("month").toString();
        Long anOwnCustNo=Long.parseLong(anParam.get("ownCustNo").toString());
        String anEndInterestDate=(String)anParam.get("endInterestDate"); // 结息日期
        String month=anMonth.replaceAll("-", "")+"01";
        
        // 根据对账月份查询
        Map<String,Object> monthMap=new HashMap<String, Object>();
        monthMap.put("startDate", CommissionDateUtils.getMinMonthDate(month));
        monthMap.put("endDate", CommissionDateUtils.getMaxMonthDate(month));
        monthMap.put("ownCustNo", anOwnCustNo);
        /**
         *  检查条件
         *  1、判断当前时间要大于朋末时间
         *  2、未生效账单必须为0
         */
        long time = new Date().getTime()-BetterDateUtils.parseDate(CommissionDateUtils.getMaxMonthDate(month)).getTime();
        BTAssert.isTrue(time>0, "当前日期要大于对账月份的月末日期");
        Map resultMap=this.mapper.selectDailyStatementCount(monthMap);
        
        Long failureTotalCount=Long.parseLong((String)resultMap.get("failureTotalCount"));
        BTAssert.isTrue(failureTotalCount<0, "日账单存在未生效数据，不能生成月账单");
        
        BigDecimal totalBalance=new BigDecimal(0.00);// 总金额
        BigDecimal totalPayBalance=new BigDecimal(0.00);// 总发生金额
        BigDecimal totalInterset=new BigDecimal(0.00);// 总利息
        BigDecimal totalTaxBalance=new BigDecimal(0.00);// 总税额
        List<CommissionDailyStatement> resultDailyStatementList=new ArrayList<CommissionDailyStatement>();
        
        BigDecimal rate=new BigDecimal(0.10); // 利率，从参数表中获得
        BigDecimal taxAmount=new BigDecimal(200); // 税额，从参数表中获得
        
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
        
        monthMap.put("ownCustName", anParam.get("ownCustName"));
        monthMap.put("monthlyRefNo", SequenceFactory.generate("CommissionDailyStatement.refNo", "#{Date:yyyyMMdd}#{Seq:12}", "MB"));
        monthMap.put("totalBalance", totalBalance);
        monthMap.put("payTotalBalance", totalPayBalance);
        monthMap.put("totalInterset", totalInterset);
        monthMap.put("totalTaxBalance", totalTaxBalance);
        monthMap.put("dailyList", resultDailyStatementList);
        monthMap.put("makeCustName", "深圳市拜特科技股份有限公司");// 参数表中获得
        monthMap.put("makeDateTime", BetterDateUtils.getDateTime());
        monthMap.put("endInterestDate", anEndInterestDate);       
        
        return monthMap;
    }
    
    /***
     * 查询日账单统计的数据
     * @param anMap
     * @return
     */
    public Map<String,Object> findDailyStatementCount(Map<String,Object> anMap){
        Map resultMap=this.mapper.selectDailyStatementCount(anMap);
        logger.info("findDailyStatementCount,resultMap:"+resultMap);
        return resultMap;
    }
    
    /***
     * 审核/作废
     * @param andailyStatementId
     * @param anBusinStatus
     * @return
     */
    public boolean updateDailyStatement(Long anDailyStatementId,String anBusinStatus){
        CommissionDailyStatement dailyStatement=this.selectByPrimaryKey(anDailyStatementId);
        dailyStatement.setLastStatus(dailyStatement.getBusinStatus());
        dailyStatement.setBusinStatus(anBusinStatus);
        return this.updateByPrimaryKey(dailyStatement)>0;
    }
    
    public boolean delDailyStatement(Long anDailyStatementId){
        return this.deleteByPrimaryKey(anDailyStatementId)>0;
    }

}
