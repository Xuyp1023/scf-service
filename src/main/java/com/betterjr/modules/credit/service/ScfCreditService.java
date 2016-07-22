package com.betterjr.modules.credit.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.credit.dao.ScfCreditMapper;
import com.betterjr.modules.credit.entity.ScfCredit;

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
        scfCreditDetailService.addCreditDetail(anCredit);

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

}
