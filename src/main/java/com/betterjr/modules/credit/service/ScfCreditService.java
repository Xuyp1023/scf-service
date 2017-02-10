package com.betterjr.modules.credit.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.credit.constant.CreditConstants;
import com.betterjr.modules.credit.dao.ScfCreditMapper;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.entity.ScfCreditDetail;

@Service
public class ScfCreditService extends BaseService<ScfCreditMapper, ScfCredit> {

    @Autowired
    private CustAccountService custAccountService;

    @Autowired
    private ScfCreditDetailService scfCreditDetailService;

    @Autowired
    private CustAndOperatorRelaService custAndOperatorRelaService;

    /**
     * 授信额度分页查询
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfCredit> queryCredit(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        // 入参检查
        checkCreditCondition(anMap);

        // 查询授信记录
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "custNo,creditMode");
    }

    private void checkCreditCondition(Map<String, Object> anMap) {
        // 保理机构查询
        if (UserUtils.factorUser()) {
            // 使用operOrg过滤数据
            anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
            // 授信对象(1:供应商;2:经销商;3:核心企业;)
            Object anCreditType = anMap.get("creditType");
            if (null == anCreditType || anCreditType.toString().isEmpty()) {
                anMap.put("creditType",
                        new String[] { CreditConstants.CREDIT_TYPE_SUPPLIER, CreditConstants.CREDIT_TYPE_SELLER, CreditConstants.CREDIT_TYPE_CORE });
            }
        }
        else {
            BTAssert.notNull(anMap.get("custNo"), "客户编号不能为空");
            // 客户(核心企业、供应商、经销商)查询时,客户编号必填,核心企业和保理商为非比填项
            String[] sFilterKey = new String[] { "coreCustNo", "factorNo" };
            for (String anTempKey : sFilterKey) {
                Object anTempValue = anMap.get(anTempKey);
                if (null == anTempValue || anTempValue.toString().isEmpty()) {
                    anMap.remove(anTempKey);
                }
            }
            // 授信状态:0-未生效;1-已生效;2-已失效;
            anMap.put("businStatus", CreditConstants.CREDIT_STATUS_EFFECTIVE);
        }

    }

    /**
     * 授信额度录入
     * 
     * @param anCredit
     * @return
     */
    public ScfCredit addCredit(ScfCredit anCredit) {
        logger.info("Begin to add Credit");

        // 初始化参数
        anCredit.initAddValue();

        // 设置企业名称
        initName(anCredit);

        // 检查是否已授信
        checkCreditExists(anCredit);

        // 数据存盘-授信额度
        this.insert(anCredit);

        // 数据存盘-授信额度变动
        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initAddValue(anCredit.getCustNo(), anCredit.getCreditLimit(), anCredit.getId());
        anCreditDetail.setCustName(custAccountService.queryCustName(anCredit.getCustNo()));
        anCreditDetail.setDirection(CreditConstants.CREDIT_DIRECTION_INCOME);// 方向：0-收;1-支;
        scfCreditDetailService.insert(anCreditDetail);

        return anCredit;
    }

    private void initName(ScfCredit anCredit) {
        // 设置操作员所属保理公司客户号
        Long anFactorNo = Collections3.getFirst(custAndOperatorRelaService.findCustNoList(anCredit.getRegOperId(), anCredit.getOperOrg()));
        anCredit.setFactorNo(anFactorNo);
        anCredit.setFactorName(custAccountService.queryCustName(anCredit.getFactorNo()));
        // 设置核心企业名称
        anCredit.setCoreName(custAccountService.queryCustName(anCredit.getCoreCustNo()));
        // 设置客户名称
        anCredit.setCustName(custAccountService.queryCustName(anCredit.getCustNo()));
    }

    private void checkCreditExists(ScfCredit anCredit) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCredit.getCustNo());
        anMap.put("factorNo", anCredit.getFactorNo());
        anMap.put("coreCustNo", anCredit.getCoreCustNo());
        anMap.put("creditMode", anCredit.getCreditMode());
        // 授信状态:0-未生效;1-已生效;2-已失效;
        anMap.put("businStatus", new String[] { CreditConstants.CREDIT_STATUS_INEFFECTIVE, CreditConstants.CREDIT_STATUS_EFFECTIVE });
        if (Collections3.isEmpty(this.selectByProperty(anMap)) == false) {
            logger.info("当前客户已存在该授信类型的记录,不允许重复授信");
            throw new BytterTradeException(40001, "当前客户已存在该授信类型的记录,不允许重复授信");
        }
    }

    /**
     * 授信记录修改
     * 
     * @param anModiCredit
     * @param anId
     * @return
     */
    public ScfCredit saveModifyCredit(ScfCredit anModiCredit, Long anId) {
        logger.info("Begin to modify Credit");

        // 获取授信记录
        ScfCredit anCredit = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anCredit, "无法获取授信记录");

        // 检查当前操作员是否能修改该授信记录
        checkOperator(anCredit.getOperOrg(), "当前操作员不能修改该授信记录");

        // 检查授信状态,不允许修改已生效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_EFFECTIVE, true, "授信记录已生效,不允许修改");

        // 检查授信状态,不允许修改已失效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_INVALID, true, "授信记录已失效,不允许修改");

        // 设置修改信息
        anModiCredit.initModifyValue(anCredit);

        // 数据存盘-授信额度修改
        this.updateByPrimaryKeySelective(anModiCredit);

        // 检查授信额度是否调整
        BigDecimal balance = MathExtend.subtract(anCredit.getCreditLimit(), anModiCredit.getCreditLimit());

        // 授信额度发生改变
        if (balance.longValue() != 0) {
            ScfCreditDetail anCreditDetail = new ScfCreditDetail();
            anCreditDetail.initModifyValue(anCredit.getCustNo(), anCredit.getCustName(), anCredit.getId());
            if (balance.longValue() < 0) {// 授信额度增加
                anCreditDetail.setDirection(CreditConstants.CREDIT_DIRECTION_INCOME);// 方向：0-收;1-支;
            }
            if (balance.longValue() > 0) {// 授信额度减少
                anCreditDetail.setDirection(CreditConstants.CREDIT_DIRECTION_EXPEND);// 方向：0-收;1-支;
            }
            anCreditDetail.setBalance(balance.abs());// 变动额度
            // 数据存盘-授信额度调整明细
            scfCreditDetailService.insert(anCreditDetail);
        }

        return anModiCredit;
    }

    /**
     * 授信额度激活
     * 
     * @param anId
     * @return
     */
    public ScfCredit saveActivateCredit(Long anId) {
        logger.info("Begin to activate Credit");

        // 获取授信记录
        ScfCredit anCredit = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anCredit, "无法获取授信记录");

        // 检查当前操作员是否能激活该授信记录
        checkOperator(anCredit.getOperOrg(), "当前操作员不能激活该授信记录");

        // 检查授信状态,不允许激活已生效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_EFFECTIVE, true, "授信记录已生效");

        // 检查授信状态,不允许激活已失效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_INVALID, true, "授信记录已失效");

        // 检查授信合同是否存在
        BTAssert.notNull(anCredit.getAgreeId(), "请选择保理授信合同,不能激活授信记录");

        // 设置激活信息
        anCredit.initActivateValue();

        // 数据存盘
        this.updateByPrimaryKey(anCredit);

        return anCredit;
    }

    /**
     * 授信终止
     * 
     * @param anId
     * @return
     */
    public ScfCredit saveTerminatCredit(Long anId) {
        logger.info("Begin to terminat Credit");

        // 获取授信记录
        ScfCredit anCredit = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anCredit, "无法获取授信记录");

        // 检查当前操作员是否能终止该授信记录
        checkOperator(anCredit.getOperOrg(), "当前操作员不能终止该授信记录");

        // 检查授信状态,不允许终止未生效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_INEFFECTIVE, true, "授信记录未生效");

        // 检查授信状态,不允许终止已失效的授信记录
        checkStatus(anCredit.getBusinStatus(), CreditConstants.CREDIT_STATUS_INVALID, true, "授信记录已失效");

        // 设置授信终止信息
        anCredit.initTerminatValue();

        // 数据存盘
        this.updateByPrimaryKey(anCredit);

        return anCredit;
    }

    public ScfCredit findCredit(Long anCustNo, Long anCoreCustNo, Long anFactorNo, String anCreditMode) {
        BTAssert.notNull(anCustNo, "请选择客户!");
        BTAssert.notNull(anCoreCustNo, "请选择核心企业!");
        BTAssert.notNull(anFactorNo, "请选择保理公司!");
        BTAssert.notNull(anCreditMode, "请选择授信类型!");
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("coreCustNo", anCoreCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("creditMode", anCreditMode);
        anMap.put("businStatus", CreditConstants.CREDIT_STATUS_EFFECTIVE);// 授信状态:0-未生效;1-已生效;2-已失效;

        return Collections3.getFirst(this.selectByProperty(anMap));
    }
    
    public ScfCredit findCreditList(Long anCustNo, Long anCoreCustNo, Long anFactorNo) {
        BTAssert.notNull(anCustNo, "请选择客户!");
        BTAssert.notNull(anCoreCustNo, "请选择核心企业!");
        BTAssert.notNull(anFactorNo, "请选择保理公司!");
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("coreCustNo", anCoreCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("businStatus", CreditConstants.CREDIT_STATUS_EFFECTIVE);// 授信状态:0-未生效;1-已生效;2-已失效;

        return Collections3.getFirst(this.selectByProperty(anMap));
    }
    
    public List<SimpleDataEntity> findCreditSimpleData(Long anCustNo, Long anCoreCustNo, Long anFactorNo) {
        BTAssert.notNull(anCustNo, "请选择客户!");
        BTAssert.notNull(anCoreCustNo, "请选择核心企业!");
        BTAssert.notNull(anFactorNo, "请选择保理公司!");
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("coreCustNo", anCoreCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("businStatus", CreditConstants.CREDIT_STATUS_EFFECTIVE);// 授信状态:0-未生效;1-已生效;2-已失效;
        
        List<SimpleDataEntity> dataList = new ArrayList<SimpleDataEntity>();
        HashMap<String, String> creditType = new HashMap<String, String>();
        creditType.put("1", "信用授信(循环)");
        creditType.put("2", "信用授信(一次性)");
        creditType.put("3", "担保信用(循环)");
        creditType.put("4", "担保授信(循环)");
        
        for (Map.Entry<String, String> entry : creditType.entrySet()) { 
        	anMap.put("creditMode", entry.getKey());
        	List<ScfCredit> list = this.selectByProperty(anMap);
        	String value = entry.getValue();
        	if(!Collections3.isEmpty(list)){
        		value = entry.getValue() + "/余额"+ list.get(0).getCreditBalance();
        	}
        	dataList.add(new SimpleDataEntity(value, entry.getKey()));
        }
        
        return dataList;
    }

    public Map<String, Object> findCreditSumByCustNo(Long anCustNo) {
        BTAssert.notNull(anCustNo, "客户编号不能为空");
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("businStatus", CreditConstants.CREDIT_STATUS_EFFECTIVE);// 授信状态:0-未生效;1-已生效;2-已失效;
        // 授信额度总和 + 累计已使用额度 + 授信余额总和
        String[] anSumFields = new String[] { "creditLimit", "creditUsed", "creditBalance", };
        return findCreditSum(anMap, anSumFields);
    }

    private Map<String, Object> findCreditSum(Map<String, Object> anConditionMap, String[] anSumFields) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        for (String anSumField : anSumFields) {
            anMap.put(anSumField, this.selectSumByProperty(anConditionMap, anSumField));
        }
        return anMap;
    }

    private void checkOperator(String anOperOrg, String anMessage) {
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    private void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

}
