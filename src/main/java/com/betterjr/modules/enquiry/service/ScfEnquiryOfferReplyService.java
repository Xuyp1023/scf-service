package com.betterjr.modules.enquiry.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.enquiry.dao.ScfEnquiryOfferReplyMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiryOfferReply;

@Service
public class ScfEnquiryOfferReplyService extends BaseService<ScfEnquiryOfferReplyMapper, ScfEnquiryOfferReply> {

    /**
     * 新增
     * 
     * @param offerReply
     * @return
     */
    public ScfEnquiryOfferReply add(ScfEnquiryOfferReply offerReply) {
        BTAssert.notNull(offerReply, "保存报价回复 失败-enquiryOfferReply is null");
        offerReply.init();
        this.insert(offerReply);
        return offerReply;
    }
    
    /**
     * 分页查询
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfEnquiryOfferReply> queryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
     }

    /**
     * 修改
     * @param offerReply
     * @return
     */
    public ScfEnquiryOfferReply saveModify(ScfEnquiryOfferReply offerReply, Long anId) {
        offerReply.setId(anId);
        offerReply.initModify();
        this.updateByPrimaryKeySelective(offerReply);
        return offerReply;
    }

   
}
