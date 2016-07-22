package com.betterjr.modules.credit.service;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.credit.dao.ScfCreditDetailMapper;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.entity.ScfCreditDetail;

@Service
public class ScfCreditDetailService extends BaseService<ScfCreditDetailMapper, ScfCreditDetail> {

    /**
     * 授信录入变动记录
     * 
     * @param anCredit
     */
    public int addCreditDetail(ScfCredit anCredit) {
        logger.info("Begin to add Credit Detail");

        ScfCreditDetail anCreditDetail = new ScfCreditDetail();
        anCreditDetail.initAddValue(anCredit);
        anCreditDetail.setDirection("0");// 方向：收

        // 数据存盘
        return this.insert(anCreditDetail);
    }

}
