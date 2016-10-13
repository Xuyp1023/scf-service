package com.betterjr.modules.credit.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
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
     * 授信额度占用,供融资放款业务调用
     * 
     * @param anCreditInfo
     */
    public void saveOccupyCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Occupy Credit");
        // 检查入参
        checkValue(anCreditInfo);

        // 授信额度变动金额
        BigDecimal anOccupyBalance = anCreditInfo.getBalance();

        // 获取客户授信记录
        ScfCredit anCustCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(anCustCredit, "无法获取客户授信额度信息");

        // 获取核心企业授信记录
        ScfCredit anCoreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(anCoreCredit, "无法获取核心企业授信额度信息");

        // 检查当前授信变动额度是否超过客户授信余额
        checkCreditBalance(anCustCredit.getCreditBalance(), anOccupyBalance,
                "当前业务发生额: " + anOccupyBalance + "超过客户授信余额: " + anCustCredit.getCreditBalance());

        // 检查当前授信变动额度是否超过核心企业授信余额
        checkCreditBalance(anCoreCredit.getCreditBalance(), anOccupyBalance,
                "当前业务发生额: " + anOccupyBalance + "超过核心企业授信余额: " + anCoreCredit.getCreditBalance());

        // 更新客户授信额度累计使用,授信余额
        anCustCredit.occupyCreditBalance(anCustCredit.getCreditUsed(), anCustCredit.getCreditBalance(), anOccupyBalance);

        // 更新核心企业授信额度累计使用,授信余额
        anCoreCredit.occupyCreditBalance(anCoreCredit.getCreditUsed(), anCoreCredit.getCreditBalance(), anOccupyBalance);

        // 数据存盘,客户授信额度变动
        saveOccupyData(anCreditInfo, anCustCredit.getId());

        // 数据存盘,核心企业授信额度变动
        saveOccupyData(anCreditInfo, anCoreCredit.getId());

        // 数据存盘-回写客户授信余额
        scfCreditService.updateByPrimaryKeySelective(anCustCredit);

        // 数据存盘-回写核心企业授信余额
        scfCreditService.updateByPrimaryKeySelective(anCoreCredit);
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

        // 客户当前释放的授信额度
        BigDecimal anReleaseBalance = anCreditInfo.getBalance();

        // 获取客户授信记录
        ScfCredit anCustCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(anCustCredit, "无法获取客户授信额度信息");

        // 获取核心企业授信记录
        ScfCredit anCoreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                anCreditInfo.getCreditMode());
        BTAssert.notNull(anCoreCredit, "无法获取核心企业授信额度信息");

        // 数据存盘,客户授信额度变动
        saveReleaseData(anCreditInfo, anCustCredit.getId());

        // 数据存盘,核心企业授信额度变动
        saveReleaseData(anCreditInfo, anCoreCredit.getId());

        // 一次性授信不释放额度,授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
        if (BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GENERAL) == true
                || BetterStringUtils.equals(anCreditInfo.getCreditMode(), CreditConstants.CREDIT_MODE_CYCLE_GUARANTEE) == true) {
            // 更新客户授信额度累计使用,授信余额
            anCustCredit.releaseCreditBalance(anCustCredit.getCreditUsed(), anCustCredit.getCreditBalance(), anReleaseBalance);

            // 更新核心企业授信额度累计使用,授信余额
            anCoreCredit.releaseCreditBalance(anCoreCredit.getCreditUsed(), anCoreCredit.getCreditBalance(), anReleaseBalance);

            // 数据存盘-回写客户授信余额
            scfCreditService.updateByPrimaryKeySelective(anCustCredit);

            // 数据存盘-回写核心企业授信余额
            scfCreditService.updateByPrimaryKeySelective(anCoreCredit);
        }
    }

    private void saveOccupyData(ScfCreditInfo anCreditInfo, Long anCreditId) {
        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initOccupyValue(anCreditInfo, anCreditId);
        // 设置客户名称
        anCreditDetail.setCustName(custAccountService.queryCustName(anCreditInfo.getCustNo()));
        // 业务描述信息
        if (BetterStringUtils.isBlank(anCreditInfo.getDescription())) {
            Long factorNo = anCreditInfo.getFactorNo();
            String factorName = custAccountService.queryCustName(factorNo);
            anCreditDetail.setDescription(factorName + "放款,业务单据号：" + anCreditInfo.getRequestNo() + ",金额:￥" + anCreditInfo.getBalance());
        }

        this.insert(anCreditDetail);
    }

    private void saveReleaseData(ScfCreditInfo anCreditInfo, Long anCreditId) {
        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initReleaseValue(anCreditInfo, anCreditId);
        // 设置客户名称
        anCreditDetail.setCustName(custAccountService.queryCustName(anCreditInfo.getCustNo()));
        // 业务描述信息
        if (BetterStringUtils.isBlank(anCreditInfo.getDescription())) {
            Long custNo = anCreditInfo.getCustNo();
            String custName = custAccountService.queryCustName(custNo);
            anCreditDetail.setDescription(custName + "还款,业务单据号：" + anCreditInfo.getRequestNo() + ",金额:￥" + anCreditInfo.getBalance());
        }

        this.insert(anCreditDetail);
    }

    private void checkCreditBalance(BigDecimal anCreditBalance, BigDecimal anOccupyBalance, String anMessage) {
        if (MathExtend.subtract(anCreditBalance, anOccupyBalance).longValue() < 0) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
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
        // BTAssert.notNull(anCreditInfo.getDescription(), "业务描述信息不能为空");
        if (MathExtend.compareToZero(anCreditInfo.getBalance()) == false) {
            logger.warn("业务单据金额不能小于零或等于零");
            throw new BytterTradeException(40001, "业务单据金额不能小于零或等于零");
        }
    }

}
