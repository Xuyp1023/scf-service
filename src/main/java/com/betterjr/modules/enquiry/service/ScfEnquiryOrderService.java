package com.betterjr.modules.enquiry.service;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.enquiry.dao.ScfEnquiryOrderMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiryOrder;

@Service
public class ScfEnquiryOrderService extends BaseService<ScfEnquiryOrderMapper, ScfEnquiryOrder> {

    /**
     * 新增
     * 
     * @param anEnquiry
     * @return
     */
    public ScfEnquiryOrder add(ScfEnquiryOrder anEnquiryOrder) {
        BTAssert.notNull(anEnquiryOrder, "保存询价与订单关系 失败-anEnquiryOrder is null");
        anEnquiryOrder.init();
        this.insert(anEnquiryOrder);
        return anEnquiryOrder;
    }

   
}
