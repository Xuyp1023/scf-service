package com.betterjr.modules.enquiry.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.enquiry.dao.ScfOfferMapper;
import com.betterjr.modules.enquiry.entity.ScfOffer;

@Service
public class OfferService extends BaseService<ScfOfferMapper, ScfOffer> {
    Logger logger = LoggerFactory.getLogger(OfferService.class);

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
       return this.selectPropertyByPage(anMap, anPageNum, anPageSize, flag);
    }

    /**
     * 增加报价
     * @param id
     * @return
     */
    public int addOffer(ScfOffer offer) {
        //TODO 生成询价编号
        offer.init();
        return this.insert(offer);

    }

    /**
     * 查询报价详情
     * @param id
     * @return
     */
    public ScfOffer findOfferDetail(Long id) {
        return this.selectByPrimaryKey(id);
    }
    
    /**
     * 查询报价(无分页)
     * @param anMap
     * @return
     */
    public List<ScfOffer> findOfferList(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfOffer.class, anMap);
    }

    public int saveModifyOffer(ScfOffer offer) {
        offer.setUpdateBaseInfo();
        return this.updateByPrimaryKey(offer);
    }
    
}
