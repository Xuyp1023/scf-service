package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionMonthlyStatementMapper;
import com.betterjr.modules.commission.data.CalcPayResult;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatementRecord;
import com.betterjr.modules.commission.entity.CommissionParam;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.flie.service.JxlsFileService;

/***
 * 月报表服务类型
 * @author hubl
 *
 */
@Service
public class CommissionMonthlyStatementService
        extends BaseService<CommissionMonthlyStatementMapper, CommissionMonthlyStatement> {

    @Autowired
    private CommissionDailyStatementService dailyStatementService;
    @Autowired
    private CommissionMonthlyStatementRecordService monthlyRecordService;
    @Reference(interfaceClass = IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeDubboClientService;
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private JxlsFileService jxlsFileService;

    @Autowired
    private CommissionParamService paramService;
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    /***
     * 保存月报表记录
     * @param anParam
     * @return
     * @throws ParseException 
     */
    public CommissionMonthlyStatement saveComissionMonthlyStatement(Map<String, Object> anParam) throws ParseException {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        // 添加月账单主表信息
        Map<String, Object> monthMap = new HashMap<String, Object>();
        monthMap.put("GTEpayDate", anParam.get("payBeginDate"));
        monthMap.put("LTEpayDate", anParam.get("payEndDate"));
        monthMap.put("businStatus", "2");
        monthMap.put("ownCustNo", anParam.get("ownCustNo"));
        CommissionMonthlyStatement monthlyStatement = new CommissionMonthlyStatement();
        monthlyStatement.initMonthlyStatement(anParam);
        CustInfo custInfo = custAccountService.findCustInfo(monthlyStatement.getOwnCustNo());// 查询企业基本信息
        if (custInfo != null) {
            monthlyStatement.setOwnOperOrg(custInfo.getOperOrg());
        }

        CalcPayResult payResult = dailyStatementService.findDailyStatementCount(anParam);

        logger.info("saveComissionMonthlyStatement payResult:" + payResult);
        monthlyStatement.setTotalAmount(new BigDecimal(payResult.getTotalAmount()));
        monthlyStatement.setPayTotalAmount(new BigDecimal(payResult.getPaySuccessAmount()));
        monthlyStatement.setPaySuccessAmount(new BigDecimal(payResult.getPaySuccessAmount()));
        monthlyStatement.setPaySuccessBalance(
                payResult.getPaySuccessBalance() == null ? new BigDecimal(0) : payResult.getPaySuccessBalance());
        monthlyStatement.setPayFailureAmount(new BigDecimal(payResult.getPayFailureAmount()));
        monthlyStatement.setPayFailureBalance(
                payResult.getPayFailureBalance() == null ? new BigDecimal(0) : payResult.getPayFailureBalance());

        Map<String, Object> configMap = getConfigData(monthlyStatement.getOwnCustNo());
        monthlyStatement.setMakeCustName(configMap.get("makeCustName").toString());
        monthlyStatement.setMakeOperName(configMap.get("makeOperName").toString());
        monthlyStatement.setInterestRate(new BigDecimal(configMap.get("interestRate").toString()));
        monthlyStatement.setTaxRate(new BigDecimal(configMap.get("taxRate").toString()));

        this.insert(monthlyStatement);

        List<CommissionDailyStatement> dailyStatementList = dailyStatementService.findCpsDailyStatement(monthMap);
        for (CommissionDailyStatement dailyStatement : dailyStatementList) {
            if (!monthlyRecordService.addMonthlyRecord(dailyStatement, monthlyStatement.getId(),
                    monthlyStatement.getRefNo())) {
                throw new BytterTradeException("月记录添加失败");
            }
            // 更新日报表状态
            dailyStatement.setLastStatus(dailyStatement.getBusinStatus());
            dailyStatement.setBusinStatus("3");
            dailyStatementService.updateByPrimaryKey(dailyStatement);
        }

        String monthlyTemplate = (String) configMap.get("monthlyTemplate");
        logger.info("dailyTemplate:" + monthlyTemplate);
        Long fileId = 0l;
        String fileType = "";
        if (monthlyTemplate != null) {
            Map<String, Object> templateMp = JsonMapper.parserJson(monthlyTemplate);
            fileId = Long.parseLong(templateMp.get("id").toString());
            fileType = (String) templateMp.get("fileType");
            if (StringUtils.isBlank(fileType)) {
                fileType = ".xlsx";
            }
        }

        List<CommissionMonthlyStatementRecord> monthlyRecordList = monthlyRecordService
                .findMonthlyStatementRecord(monthlyStatement.getId(), monthlyStatement.getRefNo());
        CommissionMonthlyStatementRecord monthlyRecord = new CommissionMonthlyStatementRecord();
        monthlyRecordList.add(monthlyRecord);

        // 生成文件
        Map<String, Object> fileMap = new HashMap<String, Object>();
        fileMap.put("monthly", monthlyStatement);
        try {
            CustFileItem custFile = jxlsFileService.transformXLSFile(monthlyRecordList, fileMap, fileId, 22, 24,
                    BetterDateUtils.formatMonthDispay(monthlyStatement.getBillMonth()) + "-对账单" + fileType);
            logger.info("生成后的文件，custFile:" + custFile);
            monthlyStatement.setFileId(custFile.getId());
            monthlyStatement.setBatchNo(custFile.getBatchNo());
            this.updateByPrimaryKey(monthlyStatement);
        }
        catch (Exception e) {
            logger.error("异常：" + e);
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
    public Page<CommissionMonthlyStatement> queryMonthlyStatement(Map<String, Object> anParam, int anPageNum,
            int anPageSize) throws ParseException {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (StringUtils.isNotBlank((String) anParam.get("billMonth"))) {
            paramMap.put("billMonth", anParam.get("billMonth"));
        }
        if (StringUtils.isNotBlank((String) anParam.get("custNo"))) {
            paramMap.put("ownCustNo", anParam.get("custNo"));
        }
        if (StringUtils.isNotBlank((String) anParam.get("businStatus"))) {
            paramMap.put("businStatus", anParam.get("businStatus"));
        } else {
            paramMap.put("businStatus", new String[] { "0", "1", "2", "3", "4", "9" });
        }
        Page<CommissionMonthlyStatement> monthlyStatement = this.selectPropertyByPage(paramMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")), "id desc");

        return monthlyStatement;
    }

    /**
     * 查询需要添加到快递中的月账单，并且修改月账单状态
     * @param anParam
     * @return
     */
    public List<CommissionMonthlyStatement> saveQueryMonthlyStatement(Map<String, Object> anParam) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        List<CommissionMonthlyStatement> monthlyStatement = this.selectByProperty(anParam);
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
    public CommissionMonthlyStatement findMonthlyStatementById(Long anMonthlyId) {
        CommissionMonthlyStatement monthlyStatement = this.selectByPrimaryKey(anMonthlyId);
        // 查询日记录列表
        List<CommissionMonthlyStatementRecord> monthlyRecordList = monthlyRecordService
                .findMonthlyStatementRecord(monthlyStatement.getId(), monthlyStatement.getRefNo());
        monthlyStatement.setDailyList(monthlyRecordList);
        monthlyStatement.setMakeDateTime(BetterDateUtils.formatDispDate(monthlyStatement.getMakeDate()) + " "
                + BetterDateUtils.formatDispTime(monthlyStatement.getMakeTime()));
        return monthlyStatement;
    }

    /***
     * 审核/作废账单
     * @param anMonthlyId
     * @param anBusinStatus
     * @return
     */
    public boolean saveMonthlyStatement(Long anMonthlyId, String anBusinStatus) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        CommissionMonthlyStatement monthlyStatement = this.selectByPrimaryKey(anMonthlyId);
        monthlyStatement.setLastStatus(monthlyStatement.getBusinStatus());
        monthlyStatement.setBusinStatus(anBusinStatus);

        if (StringUtils.equalsIgnoreCase("9", anBusinStatus)) {
            List<CommissionMonthlyStatementRecord> recordList = monthlyRecordService
                    .findMonthlyStatementRecord(monthlyStatement.getId(), monthlyStatement.getRefNo());
            for (CommissionMonthlyStatementRecord record : recordList) {
                dailyStatementService.saveDailyStatementById(record.getDailyStatementId(), "2");
            }
        }

        return this.updateByPrimaryKey(monthlyStatement) > 0;
    }

    public boolean delMonthlyStatement(Long anMonthlyId) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        CommissionMonthlyStatement monthlyStatement = this.selectByPrimaryKey(anMonthlyId);
        List<CommissionMonthlyStatementRecord> recordList = monthlyRecordService
                .findMonthlyStatementRecord(monthlyStatement.getId(), monthlyStatement.getRefNo());
        for (CommissionMonthlyStatementRecord record : recordList) {
            // 删除之前先回写日账单状态
            dailyStatementService.saveDailyStatementById(record.getDailyStatementId(), "2");
            monthlyRecordService.delete(record);
        }
        return this.deleteByPrimaryKey(anMonthlyId) > 0;
    }

    /***
     * 根据对账月份查询
     * @param anBillMonth
     * @param ownCustNo
     * @return
     */
    public List<CommissionMonthlyStatement> findMonthlyStatementByMonth(String anBillMonth, Long ownCustNo) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("billMonth", anBillMonth);
        anMap.put("ownCustNo", ownCustNo);
        anMap.put("businStatus", new String[] { "0", "1", "2", "3", "4" });
        return this.selectByProperty(anMap);
    }

    /***
    * 获取参数表里面的信息
    * @return
    */
    public Map<String, Object> getConfigData(Long anCoreCustNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String cusrName = domainAttributeDubboClientService.findString(custOperator.getOperOrg(),
                "PLAT_COMMISSION_MAKE_CUSTNAME");
        final String operator = domainAttributeDubboClientService.findString(custOperator.getOperOrg(),
                "PLAT_COMMISSION_MAKE_OPERATOR");
        // final BigDecimal interestRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(),
        // "PLAT_COMMISSION_INTEREST_RATE");
        // final BigDecimal taxRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(),
        // "PLAT_COMMISSION_TAX_RATE");
        final String monthlyTemplate = domainAttributeDubboClientService
                .findString("GLOBAL_COMMISSION_MONTHLY_TEMPLATE");
        CommissionParam param = paramService.findParamByCustNo(Collections3.getFirst(getCurrentUserCustNos()),
                anCoreCustNo);
        BTAssert.notNull(param, "请配置企业的默认利率，税率");
        // final BigDecimal interestRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(),
        // "PLAT_COMMISSION_INTEREST_RATE");
        // final BigDecimal taxRate = domainAttributeDubboClientService.findMoney(custOperator.getOperOrg(),
        // "PLAT_COMMISSION_TAX_RATE");
        final BigDecimal interestRate = param.getInterestRate();
        final BigDecimal taxRate = param.getTaxRate();
        map.put("makeCustName", cusrName);
        map.put("makeOperName", operator);
        map.put("interestRate", interestRate);
        map.put("taxRate", taxRate);
        map.put("monthlyTemplate", monthlyTemplate);

        return map;
    }

    /**
    * 获取当前登录用户所在的所有公司id集合
    * @return
    */
    private Collection<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询参数配置失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询参数配置失败!获取当前企业失败");
        Collection<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

}
