package com.betterjr.modules.credit.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
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
        // 授信对象(1:供应商;2:经销商;3:核心企业;)
        Object anCreditType = anMap.get("creditType");
        if (null == anCreditType || anCreditType.toString().isEmpty()) {
            anMap.put("creditType", new String[] { "1", "2", "3" });
        }
        // 查询授信记录
        Page<ScfCredit> anCreditList = this.selectPropertyByPage(ScfCredit.class, anMap, anPageNum, anPageSize, "1".equals(anFlag));

        // 设置企业名称
        for (ScfCredit anCredit : anCreditList) {
            // 核心企业名称
            anCredit.setCoreName(custAccountService.queryCustName(anCredit.getCoreCustNo()));
            // 保理商名称
            anCredit.setFactorName(custAccountService.queryCustName(anCredit.getFactorNo()));
            // 企业名称
            anCredit.setCustName(custAccountService.queryCustName(anCredit.getCustNo()));
            anCredit.setStartDate(BetterDateUtils.formatDispDate(anCredit.getStartDate()));
            anCredit.setEndDate(BetterDateUtils.formatDispDate(anCredit.getEndDate()));
            anCredit.setRegDate(BetterDateUtils.formatDispDate(anCredit.getRegDate()));
            anCredit.setModiDate(BetterDateUtils.formatDispDate(anCredit.getModiDate()));
            anCredit.setActivateDate(BetterDateUtils.formatDispDate(anCredit.getActivateDate()));
            anCredit.setTerminatDate(BetterDateUtils.formatDispDate(anCredit.getTerminatDate()));
        }

        return anCreditList;
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

        // 设置操作员所属保理公司客户号
        anCredit.setFactorNo(Collections3.getFirst(custAndOperatorRelaService.findCustNoList(anCredit.getRegOperId(), anCredit.getOperOrg())));

        // 检查是否已授信
        checkCreditExists(anCredit);

        // 数据存盘-授信额度
        this.insert(anCredit);

        // 数据存盘-授信额度变动
        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initAddValue(anCredit.getCustNo(), anCredit.getCreditLimit(), anCredit.getId());
        anCreditDetail.setDirection("0");// 方向：0-收;1-支;
        scfCreditDetailService.insert(anCreditDetail);

        return anCredit;
    }

    private void checkCreditExists(ScfCredit anCredit) {
        // 授信额度入参
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCredit.getCustNo());
        anMap.put("factorNo", anCredit.getFactorNo());
        anMap.put("creditMode", anCredit.getCreditMode());
        anMap.put("businStatus", new String[] { "0", "1" });// 授信状态:0-未生效;1-已生效;2-已过期;
        // 查询结果
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
        checkStatus(anCredit.getBusinStatus(), "1", true, "授信记录已生效,不允许修改");

        // 检查授信状态,不允许修改已过期的授信记录
        checkStatus(anCredit.getBusinStatus(), "2", true, "授信记录已过期,不允许修改");

        // 设置修改信息
        anModiCredit.initModifyValue(anCredit);

        // 数据存盘-授信额度修改
        this.updateByPrimaryKeySelective(anModiCredit);

        // 检查授信额度是否调整
        BigDecimal balance = MathExtend.subtract(anCredit.getCreditLimit(), anModiCredit.getCreditLimit());

        // 授信额度发生改变
        if (balance.longValue() != 0) {
            ScfCreditDetail anCreditDetail = new ScfCreditDetail();
            anCreditDetail.initModifyValue(anCredit.getCustNo(), anCredit.getId());
            // 授信额度增加
            if (balance.longValue() < 0) {
                anCreditDetail.setDirection("0");// 方向：0-收;1-支;
            }
            // 授信额度减少
            if (balance.longValue() > 0) {
                anCreditDetail.setDirection("1");// 方向：0-收;1-支;
            }
            // 变动额度
            anCreditDetail.setBalance(balance.abs());
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
        checkStatus(anCredit.getBusinStatus(), "1", true, "授信记录已生效");

        // 检查授信状态,不允许激活已过期的授信记录
        checkStatus(anCredit.getBusinStatus(), "2", true, "授信记录已过期");

        // 检查授信合同是否存在
        BTAssert.notNull(anCredit.getAgreeId(), "未关联授信合同,不能激活授信记录");

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
        checkStatus(anCredit.getBusinStatus(), "0", true, "授信记录未生效");

        // 检查授信状态,不允许终止已过期的授信记录
        checkStatus(anCredit.getBusinStatus(), "2", true, "授信记录已过期");

        // 设置授信终止信息
        anCredit.initTerminatValue();

        // 数据存盘
        this.updateByPrimaryKey(anCredit);

        return anCredit;
    }

    public ScfCredit findCredit(Long anCustNo, Long anCoreNo, Long anFactorNo, String anCreditMode) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("coreCustNo", anCoreNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("creditMode", anCreditMode);
        anMap.put("businStatus", "1");// 授信状态:0-未生效;1-已生效;2-已过期;

        return Collections3.getFirst(this.selectByProperty(anMap));
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
