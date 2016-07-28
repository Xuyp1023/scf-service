package com.betterjr.modules.enquiry.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfOfferMapper;
import com.betterjr.modules.enquiry.entity.ScfOffer;

@Service
public class OfferService extends BaseService<ScfOfferMapper, ScfOffer> {
    @Autowired
    private CustAccountService custAccountService;
    
    /**
     * 查询报价列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfOffer> queryOfferList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
       boolean flag = 1==anFlag;
       Page<ScfOffer> offerList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, flag);
       //设置保理公司名称
       for (ScfOffer offer : offerList) {
           offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
       }
       return offerList;
    }

    /**
     * 增加报价
     * @param id
     * @return
     */
    public ScfOffer addOffer(ScfOffer anOffer) {
        BTAssert.notNull(anOffer, "webAddOffer service failed offer =null");
        anOffer.init();
        this.insert(anOffer);
        return anOffer;

    }

    /**
     * 查询报价详情
     * @param anId
     * @return
     */
    public ScfOffer findOfferDetail(Long anId) {
        ScfOffer offer = this.selectByPrimaryKey(anId);
        offer.setCustName(custAccountService.queryCustName(offer.getCustNo()));
        offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
        return this.selectByPrimaryKey(anId);
    }
    
    /**
     * 查询报价(无分页)
     * @param anMap
     * @return
     */
    public List<ScfOffer> findOfferList(Map<String, Object> anMap) {
        List<ScfOffer> offerList = this.selectByClassProperty(ScfOffer.class, anMap);
        
        //设置保理公司名称
        for (ScfOffer offer : offerList) {
            offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
        }
        return offerList;
    }

    public ScfOffer saveModifyOffer(ScfOffer anOffer, Long anId) {
        BTAssert.notNull(anOffer);
        anOffer.setId(anId);
        anOffer.setUpdateBaseInfo();
        this.updateByPrimaryKeySelective(anOffer);
        return anOffer;
    }
    
}
