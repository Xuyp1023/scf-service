package com.betterjr.modules.credit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.credit.dao.ScfCreditMapper;
import com.betterjr.modules.credit.entity.ScfCredit;

@Service
public class ScfCreditService extends BaseService<ScfCreditMapper, ScfCredit> {

    @Autowired
    private CustAccountService custAccountService;

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

}
