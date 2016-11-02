package com.betterjr.modules.credit.service;

import java.math.BigDecimal;
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
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.credit.constant.CreditConstants;
import com.betterjr.modules.credit.dao.ScfCreditDetailMapper;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.entity.ScfCreditDetail;
import com.betterjr.modules.credit.entity.ScfCreditInfo;

@Service
public class ScfCreditDetailService extends BaseService<ScfCreditDetailMapper, ScfCreditDetail> {

    @Autowired
    private ScfCreditService scfCreditService;

    @Autowired
    private CustAccountService custAccountService;

    /**
     * 授信额度变动信息查询
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfCreditDetail> queryCreditDetail(Map<String, Object> anMap, Long anCreditId, String anFlag, int anPageNum, int anPageSize) {
        // 授信来源ID
        anMap.put("creditId", anCreditId);

        // 获取查询结果
        return this.selectPropertyByPage(ScfCreditDetail.class, anMap, anPageNum, anPageSize, "1".equals(anFlag));
    }

    /**
     * 授信额度解冻,供融资终止业务调用
     * 
     * @param anCreditInfo
     */
    public void saveUnfreezeCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Unfreeze Credit");
        // 检查入参
        checkValue(anCreditInfo);

        // 解冻额度
        BigDecimal unfreezeBalance = anCreditInfo.getBalance();

        // 获取客户授信记录
        ScfCredit custCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        if (custCredit != null) {
            // 获取客户授信额度冻结记录
            ScfCreditDetail custFreezeCreditDetail = findCreditDetail(anCreditInfo, custCredit);
            if (custFreezeCreditDetail != null) {
                // 更新客户授信额度累计使用,授信余额
                custCredit.releaseCreditBalance(custCredit.getCreditUsed(), custCredit.getCreditBalance(), unfreezeBalance);

                // 数据存盘-回写客户授信余额
                scfCreditService.updateByPrimaryKeySelective(custCredit);

                // 数据存盘,客户授信额度变动
                ScfCreditDetail custCreditDetail = createCreditDetail(anCreditInfo, custCredit.getId(), CreditConstants.CREDIT_DIRECTION_INCOME);
                custCreditDetail.setBalance(unfreezeBalance);
                custCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);// 状态(0:已完成;1:冻结中;)
                custCreditDetail.setDescription("交易终止,业务单据号：" + anCreditInfo.getRequestNo() + ",解冻额度:￥" + unfreezeBalance);
                this.insert(custCreditDetail);
            }
        }

        // 获取核心企业授信记录
        ScfCredit coreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        // 获取核心企业授信额度冻结记录
        ScfCreditDetail coreFreezeCreditDetail = findCreditDetail(anCreditInfo, coreCredit);
        if (coreFreezeCreditDetail != null) {
            // 更新核心企业授信额度累计使用,授信余额
            coreCredit.releaseCreditBalance(coreCredit.getCreditUsed(), coreCredit.getCreditBalance(), unfreezeBalance);

            // 数据存盘-回写核心企业授信余额
            scfCreditService.updateByPrimaryKeySelective(coreCredit);

            // 数据存盘,核心企业授信额度变动
            ScfCreditDetail coreCreditDetail = createCreditDetail(anCreditInfo, coreCredit.getId(), CreditConstants.CREDIT_DIRECTION_INCOME);
            coreCreditDetail.setBalance(unfreezeBalance);
            coreCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);// 状态(0:已完成;1:冻结中;)
            coreCreditDetail.setDescription("交易终止,业务单据号：" + anCreditInfo.getRequestNo() + ",解冻额度:￥" + unfreezeBalance);
            this.insert(coreCreditDetail);
        }
    }

    /**
     * 授信额度冻结,供融资申请业务调用
     * 
     * @param anCreditInfo
     */
    public void saveFreezeCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Freeze Credit");
        // 检查入参
        checkValue(anCreditInfo);

        // 冻结额度
        BigDecimal freezeBalance = anCreditInfo.getBalance();

        //
        String custName = custAccountService.queryCustName(anCreditInfo.getCustNo());
        String coreCustName = custAccountService.queryCustName(anCreditInfo.getCoreCustNo());
        String factorName = custAccountService.queryCustName(anCreditInfo.getFactorNo());

        // 获取客户授信记录
        ScfCredit custCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        if (custCredit != null) {
            // 检查授信有效期
            checkCreditValidDate(custCredit, custName);
            
            // 检查客户授信余额是否充足
            checkCreditBalance(custCredit.getCreditBalance(), freezeBalance,
                    "业务发生额: " + freezeBalance + "超过" + custName + "授信余额: " + custCredit.getCreditBalance());

            // 更新客户授信额度累计使用,授信余额
            custCredit.occupyCreditBalance(custCredit.getCreditUsed(), custCredit.getCreditBalance(), freezeBalance);

            // 数据存盘,客户授信额度变动
            ScfCreditDetail custCreditDetail = createCreditDetail(anCreditInfo, custCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
            custCreditDetail.setBalance(freezeBalance);
            custCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_FREEZE);// 状态(0:已完成;1:冻结中;)
            custCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",冻结额度:￥" + freezeBalance);
            this.insert(custCreditDetail);

            // 数据存盘-回写客户授信余额
            scfCreditService.updateByPrimaryKeySelective(custCredit);
        }

        // 获取核心企业授信记录
        ScfCredit coreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(coreCredit, "当前业务将占用" + coreCustName + "该类型的授信额度，该企业未获得" + factorName + "该类型的授信，请等待授信结束后继续办理业务！");

        // 检查授信有效期
        checkCreditValidDate(coreCredit, coreCustName);

        // 检查核心企业授信余额是否充足
        checkCreditBalance(coreCredit.getCreditBalance(), freezeBalance,
                "业务发生额: " + freezeBalance + "超过" + coreCustName + "授信余额: " + coreCredit.getCreditBalance());

        // 更新核心企业授信额度累计使用,授信余额
        coreCredit.occupyCreditBalance(coreCredit.getCreditUsed(), coreCredit.getCreditBalance(), freezeBalance);

        // 数据存盘,核心企业授信额度变动
        ScfCreditDetail coreCreditDetail = createCreditDetail(anCreditInfo, coreCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
        coreCreditDetail.setBalance(freezeBalance);
        coreCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_FREEZE);// 状态(0:已完成;1:冻结中;)
        coreCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",冻结额度:￥" + freezeBalance);
        this.insert(coreCreditDetail);

        // 数据存盘-回写核心企业授信余额
        scfCreditService.updateByPrimaryKeySelective(coreCredit);
    }

    /**
     * 授信额度占用,供融资放款业务调用
     * 
     * @param anCreditInfo
     */
    public void saveOccupyCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Occupy Credit");

        // 检查入参
        checkValue(anCreditInfo);

        // 占用额度
        BigDecimal occupyBalance = anCreditInfo.getBalance();
        String custName = custAccountService.queryCustName(anCreditInfo.getCustNo());
        String coreCustName = custAccountService.queryCustName(anCreditInfo.getCoreCustNo());
        String factorName = custAccountService.queryCustName(anCreditInfo.getFactorNo());

        // 获取客户授信记录
        ScfCredit custCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        if (custCredit != null) {
            // 检查授信有效期
            checkCreditValidDate(custCredit, custName);
            
            // 检查客户授信余额是否充足
            checkCreditBalance(custCredit.getCreditBalance(), occupyBalance,
                    "业务发生额: " + occupyBalance + "超过" + custName + "授信余额: " + custCredit.getCreditBalance());
            // 处理客户冻结和占用的授信额度
            saveFreezeAndOccupyData(anCreditInfo, custCredit, occupyBalance);
        }

        // 获取核心企业授信记录
        ScfCredit coreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(coreCredit, "当前业务将占用" + coreCustName + "该类型的授信额度，该企业未获得" + factorName + "该类型的授信，请等待授信结束后继续办理业务！");

        // 检查授信有效期
        checkCreditValidDate(coreCredit, coreCustName);

        // 检查核心企业授信余额是否充足
        checkCreditBalance(coreCredit.getCreditBalance(), occupyBalance,
                "业务发生额: " + occupyBalance + "超过" + coreCustName + "授信余额: " + coreCredit.getCreditBalance());

        // 处理核心企业冻结和占用的授信额度
        saveFreezeAndOccupyData(anCreditInfo, coreCredit, occupyBalance);
    }

    private void saveFreezeAndOccupyData(ScfCreditInfo anCreditInfo, ScfCredit anCredit, BigDecimal anOccupyBalance) {
        // 获取授信额度冻结记录
        ScfCreditDetail freezeCreditDetail = findCreditDetail(anCreditInfo, anCredit);

        if (freezeCreditDetail != null) {
            // 冻结金额
            BigDecimal freezeBalance = freezeCreditDetail.getBalance();

            // 实际占用额度与冻结额度的差额
            BigDecimal balance = MathExtend.subtract(freezeBalance, anOccupyBalance);
            if (balance.longValue() != 0) {
                if (balance.longValue() < 0) {// 冻结额度小于当前占用额度,需要额外占用多出来的额度
                    // 更新授信额度累计使用,授信余额
                    anCredit.occupyCreditBalance(anCredit.getCreditUsed(), anCredit.getCreditBalance(), balance.abs());

                    // 数据存盘,回写授信余额信息
                    scfCreditService.updateByPrimaryKeySelective(anCredit);

                    // 生成本次授信额度占用的记录
                    ScfCreditDetail creditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
                    creditDetail.setBalance(freezeBalance);
                    creditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
                    creditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",占用额度:￥" + freezeBalance);
                    this.insert(creditDetail);

                    // 生成本次授信额度额外占用的记录
                    ScfCreditDetail extraCreditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
                    extraCreditDetail.setBalance(balance.abs());
                    extraCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
                    extraCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",追加占用:￥" + balance.abs());
                    this.insert(extraCreditDetail);
                }
                if (balance.longValue() > 0) {// 冻结额度大于当前占用额度,需要释放多出来的额度
                    // 更新授信额度累计使用,授信余额
                    anCredit.releaseCreditBalance(anCredit.getCreditUsed(), anCredit.getCreditBalance(), balance.abs());

                    // 数据存盘,回写授信余额信息
                    scfCreditService.updateByPrimaryKeySelective(anCredit);

                    // 生成本次授信额度占用的记录
                    ScfCreditDetail creditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
                    creditDetail.setBalance(anOccupyBalance);
                    creditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
                    creditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",占用额度:￥" + anOccupyBalance);
                    this.insert(creditDetail);

                    // 生成本次授信额度释放的记录
                    ScfCreditDetail extraCreditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_INCOME);
                    extraCreditDetail.setBalance(balance.abs());
                    extraCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
                    extraCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",释放额度:￥" + balance.abs());
                    this.insert(extraCreditDetail);
                }
            }
            else {
                // 数据存盘,授信额度变动信息
                ScfCreditDetail creditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
                creditDetail.setBalance(anOccupyBalance);
                creditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
                creditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",占用额度:￥" + anOccupyBalance);
                this.insert(creditDetail);
            }

            // 删除冻结记录
            this.delete(freezeCreditDetail);

        }
        else {
            // 更新授信额度累计使用,授信余额
            anCredit.occupyCreditBalance(anCredit.getCreditUsed(), anCredit.getCreditBalance(), anOccupyBalance);

            // 数据存盘,回写授信余额信息
            scfCreditService.updateByPrimaryKeySelective(anCredit);

            // 数据存盘,授信额度变动信息
            ScfCreditDetail creditDetail = createCreditDetail(anCreditInfo, anCredit.getId(), CreditConstants.CREDIT_DIRECTION_EXPEND);
            creditDetail.setBalance(anOccupyBalance);
            creditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
            creditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",占用额度:￥" + anOccupyBalance);
            this.insert(creditDetail);
        }
    }

    /**
     * 授信额度释放,供融资还款业务调用,一次性授信不释放额度
     * 
     * @param anCreditInfo
     */
    public void saveReleaseCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Release Credit");
        // 检查入参
        checkValue(anCreditInfo);

        // 释放额度
        BigDecimal releaseBalance = anCreditInfo.getBalance();

        // 获取客户授信记录
        ScfCredit custCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        if (custCredit != null) {
            // 数据存盘,客户授信额度变动
            ScfCreditDetail custCreditDetail = createCreditDetail(anCreditInfo, custCredit.getId(), CreditConstants.CREDIT_DIRECTION_INCOME);
            custCreditDetail.setBalance(releaseBalance);
            custCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
            custCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",释放额度:￥" + releaseBalance);
            this.insert(custCreditDetail);

            // 一次性授信不回写余额,授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
            if (BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GENERAL) == true
                    || BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GUARANTEE) == true) {
                // 更新客户授信额度累计使用,授信余额
                custCredit.releaseCreditBalance(custCredit.getCreditUsed(), custCredit.getCreditBalance(), releaseBalance);

                // 数据存盘,回写客户授信余额
                scfCreditService.updateByPrimaryKeySelective(custCredit);
            }
        }

        // 获取核心企业授信记录
        ScfCredit coreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());

        // 数据存盘,核心企业授信额度变动
        ScfCreditDetail coreCreditDetail = createCreditDetail(anCreditInfo, coreCredit.getId(), CreditConstants.CREDIT_DIRECTION_INCOME);
        coreCreditDetail.setBalance(releaseBalance);
        coreCreditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);
        coreCreditDetail.setDescription("业务单据号：" + anCreditInfo.getRequestNo() + ",释放额度:￥" + releaseBalance);
        this.insert(coreCreditDetail);

        // 一次性授信不回写余额,授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
        if (BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GENERAL) == true
                || BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GUARANTEE) == true) {
            // 更新核心企业授信额度累计使用,授信余额
            coreCredit.releaseCreditBalance(coreCredit.getCreditUsed(), coreCredit.getCreditBalance(), releaseBalance);

            // 数据存盘,回写核心企业授信余额
            scfCreditService.updateByPrimaryKeySelective(coreCredit);
        }
    }

    private ScfCreditDetail createCreditDetail(ScfCreditInfo anCreditInfo, Long anCreditId, String anDirection) {
        ScfCreditDetail creditDetail = new ScfCreditDetail();
        creditDetail.init();
        creditDetail.setCustNo(anCreditInfo.getCustNo());
        creditDetail.setCustName(custAccountService.queryCustName(anCreditInfo.getCustNo()));
        creditDetail.setBusinFlag(anCreditInfo.getBusinFlag());
        creditDetail.setBusinId(anCreditInfo.getBusinId());
        creditDetail.setRequestNo(anCreditInfo.getRequestNo());
        creditDetail.setlCreditId(anCreditId);
        creditDetail.setBusinStatus(CreditConstants.CREDIT_CHANGE_STATUS_DONE);// 状态(0:已完成;1:冻结中;)
        creditDetail.setDirection(anDirection);// 方向：0-收;1-支;
        return creditDetail;
    }

    private ScfCreditDetail findCreditDetail(ScfCreditInfo anCreditInfo, ScfCredit anCredit) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().put("creditId", anCredit.getId()).put("custNo", anCreditInfo.getCustNo())
                .put("requestNo", anCreditInfo.getRequestNo()).put("businFlag", anCreditInfo.getBusinFlag())
                .put("businStatus", CreditConstants.CREDIT_CHANGE_STATUS_FREEZE).build();

        return Collections3.getFirst(this.selectByProperty(anMap));
    }

    private void checkCreditBalance(BigDecimal anCreditBalance, BigDecimal anOccupyBalance, String anMessage) {
        if (MathExtend.subtract(anCreditBalance, anOccupyBalance).longValue() < 0) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    private void checkCreditValidDate(ScfCredit anCredit, String anCustName) {
        if (BetterDateUtils.getDistanceOfTwoDay(anCredit.getEndDate(), BetterDateUtils.getNumDate()) > 0) {
            logger.warn(anCustName + "授信已过期,无法继续办理业务!");
            throw new BytterTradeException(40001, anCustName + "授信已过期,无法继续办理业务!");
        }
    }

    private void checkValue(ScfCreditInfo anCreditInfo) {
        BTAssert.notNull(anCreditInfo.getCustNo(), "客户编号不能为空");
        BTAssert.notNull(anCreditInfo.getCoreCustNo(), "核心企业编号不能为空");
        BTAssert.notNull(anCreditInfo.getFactorNo(), "保理商编号不能为空");
        BTAssert.notNull(anCreditInfo.getRequestNo(), "业务单据申请号不能为空");
        BTAssert.notNull(anCreditInfo.getBusinId(), "业务单据流水号不能为空");
        BTAssert.notNull(anCreditInfo.getBusinFlag(), "业务类型不能为空");
        BTAssert.notNull(anCreditInfo.getCreditMode(), "授信方式不能为空");
        if (MathExtend.compareToZero(anCreditInfo.getBalance()) == false) {
            logger.warn("业务单据金额不能小于零或等于零");
            throw new BytterTradeException(40001, "业务单据金额不能小于零或等于零");
        }
    }

}
