package com.betterjr.modules.enquiry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.enquiry.dao.ScfEnquiryMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfOffer;

@Service
public class EnquiryService extends BaseService<ScfEnquiryMapper, ScfEnquiry> {

    @Autowired
    private OfferService offerService;
    
    /**
     * 查询询价列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfEnquiry> queryEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfEnquiry> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
        for (ScfEnquiry scfEnquiry : page) {
            if(BetterStringUtils.isBlank(scfEnquiry.getEnquiryNo())){
                continue;
            }
            
            //根据enquiryNo查询出该询价的报价（未取消的）
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("enquiryNo", scfEnquiry.getEnquiryNo());
            map.put("businStatus", 1);
            List<ScfOffer> offerList =  offerService.findOfferList(map);
            scfEnquiry.setOfferList(offerList);
            scfEnquiry.setOfferCount(offerList.size());
        }
        return page;
    }

    /**
     * 新增询价
     * @param anEnquiry
     * @return
     */
    public int addEnquiry(ScfEnquiry anEnquiry) {
        anEnquiry.init();
        return this.insert(anEnquiry);

    }

    /**
     * 查询询价详情
     * @param anId
     * @return
     */
    public ScfEnquiry findEnquiryDetail(Long anId) {
        return this.selectByPrimaryKey(anId);
    }

    public int saveModifyEnquiry(ScfEnquiry anEnquiry) {
        anEnquiry.setModiBaseInfo();
        return this.updateByPrimaryKeySelective(anEnquiry);
    }
}
