package com.betterjr.modules.credit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.credit.dao.ScfCreditDetailMapper;
import com.betterjr.modules.credit.entity.ScfCreditDetail;

@Service
public class ScfCreditDetailService extends BaseService<ScfCreditDetailMapper, ScfCreditDetail> {

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

}
