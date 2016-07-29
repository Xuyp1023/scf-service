package com.betterjr.modules.enquiry.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.enquiry.IScfEnquiryService;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.EnquiryService;
import com.betterjr.modules.enquiry.service.OfferService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfEnquiryService.class)
public class EnquiryDubboService implements IScfEnquiryService {
    @Autowired
    private EnquiryService enquiryService;
    
    @Autowired
    private OfferService offerService;
    
    protected final Logger logger = LoggerFactory.getLogger(EnquiryDubboService.class);
    public String webQueryEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询询价列表,入参："+ anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功", enquiryService.queryEnquiryList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    public String webAddEnquiry(Map<String, Object> anMap) {
        logger.debug("新增询价,入参："+ anMap);
        ScfEnquiry enquiry = (ScfEnquiry) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk(enquiryService.addEnquiry(enquiry)).toJson();
    }

    public String webFindEnquiryDetail(Long anId) {
        logger.debug("查询询价详情,入参：anId"+ anId);
        return AjaxObject.newOk(enquiryService.findEnquiryDetail(anId)).toJson();
    }

    public String webQueryOfferList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询报价列表,入参："+ anMap);
        anMap = (Map)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功", offerService.queryOfferList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    public String webAddOffer(Map<String, Object> anMap) {
        logger.debug("新增报价,入参："+ anMap);
        return AjaxObject.newOk(offerService.addOffer((ScfOffer) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }

    public String webFindOfferDetail(Long factorNo, String enquiryNo) {
        logger.debug("查询报价详情,入参：factorNo："+ factorNo + "  factorNo:" + enquiryNo);
        return AjaxObject.newOk(offerService.findOfferDetail(factorNo, enquiryNo)).toJson();
    }

    @Override
    public String webSaveModifyEnquiry(Map<String, Object> anMap, Long anId) {
        logger.debug("修改询价,入参：anId："+ anId + "/n" + anMap);
        return AjaxObject.newOk(enquiryService.saveModifyEnquiry(anMap, anId)).toJson();
    }

    @Override
    public String webSaveModifyOffer(Map<String, Object> anMap, Long anId) {
        logger.debug("修改报价,入参：anId："+ anId + "/n" + anMap);
        return AjaxObject.newOk(offerService.saveModifyOffer((ScfOffer) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webQueryEnquiryByfactorNo(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        logger.debug("查询保理公司收到的询价,入参： " + anMap);
        return AjaxObject.newOk(enquiryService.queryEnquiryByfactorNo(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
}
