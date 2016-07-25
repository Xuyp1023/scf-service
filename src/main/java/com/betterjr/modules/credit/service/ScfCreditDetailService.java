package com.betterjr.modules.credit.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
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
        Page<ScfCreditDetail> anCreditDetailList = this.selectPropertyByPage(ScfCreditDetail.class, anMap, anPageNum, anPageSize, "1".equals(anFlag));

        // 设置企业个名称
        for (ScfCreditDetail anCreditDetail : anCreditDetailList) {
            anCreditDetail.setCustName(custAccountService.queryCustName(anCreditDetail.getCustNo()));
            anCreditDetail.setOccupyDate(BetterDateUtils.formatDispDate(anCreditDetail.getOccupyDate()));
        }

        return anCreditDetailList;
    }

    /**
     * 授信额度占用,供融资放款业务调用
     * 
     * @param anCreditInfo
     */
    public int saveOccupyCredit(ScfCreditInfo anCreditInfo) {
        logger.info("Begin to Occupy Credit");
        try {
            // 检查入参
            checkValue(anCreditInfo);

            // 授信额度变动金额
            BigDecimal anOccupyBalance = anCreditInfo.getBalance();

            // 获取客户授信记录
            ScfCredit anCustCredit = scfCreditService.findCredit(anCreditInfo.getCustNo(), anCreditInfo.getCoreCustNo(), anCreditInfo.getFactorNo(),
                    anCreditInfo.getCreditMode());
            BTAssert.notNull(anCustCredit, "无法获取客户授信记录");

            // 获取核心企业授信记录
            ScfCredit anCoreCredit = scfCreditService.findCredit(anCreditInfo.getCoreCustNo(), anCreditInfo.getCoreCustNo(),
                    anCreditInfo.getFactorNo(), anCreditInfo.getCreditMode());
            BTAssert.notNull(anCoreCredit, "无法获取核心企业授信记录");

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
        catch (Exception e) {
            return -1;
        }
        return 1;
    }

    private void saveOccupyData(ScfCreditInfo anCreditInfo, Long anCreditId) {
        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initOccupyValue(anCreditInfo, anCreditId);
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
        BTAssert.notNull(anCreditInfo.getDescription(), "业务描述信息不能为空");
        if (MathExtend.compareToZero(anCreditInfo.getBalance()) == false) {
            logger.warn("业务单据金额不能小于等于零");
            throw new BytterTradeException(40001, "业务单据金额不能小于等于零");
        }
    }

}
