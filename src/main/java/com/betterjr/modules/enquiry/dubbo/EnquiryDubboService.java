package com.betterjr.modules.enquiry.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.enquiry.IScfEnquiryService;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfEnquiryOfferReply;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.ScfEnquiryObjectService;
import com.betterjr.modules.enquiry.service.ScfEnquiryOfferReplyService;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfEnquiryService.class)
public class EnquiryDubboService implements IScfEnquiryService {
    @Autowired
    private ScfEnquiryService enquiryService;
    
    @Autowired
    private ScfOfferService offerService;
    
    @Autowired
    private ScfEnquiryOfferReplyService offerReplyService;
    
    @Autowired
    private ScfEnquiryObjectService enquiryObjectService;
    
    protected final Logger logger = LoggerFactory.getLogger(EnquiryDubboService.class);
    
    public String webQueryEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询询价列表,入参："+ anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功", enquiryService.queryEnquiryList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    public String webQuerySingleOrderEnquiryList(Long custNo, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询询价列表,入参："+ custNo);
        return AjaxObject.newOkWithPage("查询成功", enquiryService.querySingleOrderEnquiryList(custNo, anFlag, anPageNum, anPageSize)).toJson();
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
    
    public String webFindSingleOrderEnquiryDetail(Long anId) {
        logger.debug("查询询价详情,入参：anId"+ anId);
        return AjaxObject.newOk(enquiryService.findSingleOrderEnquiryDetail(anId)).toJson();
    }

    public String webQueryOfferList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        logger.debug("分页查询报价列表,入参："+ anMap);
        return AjaxObject.newOkWithPage("查询成功", offerService.queryOfferList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    public String webQueryOfferByFactor(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        logger.debug("查看有哪些公司报了价,入参："+ anMap);
        return AjaxObject.newOkWithPage("查询成功", offerService.queryOfferByFactor(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    public String webAddOffer(Map<String, Object> anMap) {
        logger.debug("新增报价,入参："+ anMap);
        return AjaxObject.newOk(offerService.addOffer((ScfOffer) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }
    
    public String webFindOfferDetail(Long factorNo, String enquiryNo) {
        logger.debug("查询报价详情,入参：factorNo："+ factorNo + "  enquiryNo:" + enquiryNo);
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
        return AjaxObject.newOkWithPage("查询成功", enquiryService.queryEnquiryByfactorNo(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    
    @Override
    public String webAddOfferReply(Map<String, Object> anMap) {
        logger.debug("新增报价回复,入参："+ anMap);
        return AjaxObject.newOk(offerReplyService.add((ScfEnquiryOfferReply) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }
    
    @Override
    public String webSaveModifyOfferReply(Map<String, Object> anMap, Long anId) {
        logger.debug("修改报价回复,入参：anId：" + anMap);
        return AjaxObject.newOk(offerReplyService.saveModify((ScfEnquiryOfferReply) RuleServiceDubboFilterInvoker.getInputObj(), anId)).toJson();
    }
    
    @Override
    public String webQueryOfferReply(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询报价回复,入参："+ anMap);
        anMap = (Map)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功", offerReplyService.queryList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFactorDropOffer(String anEnquiryNo, Long factorNo) {
        logger.debug("资金方放弃报价,入参：factorNo:"+ anEnquiryNo + " -factorNo:"+factorNo);
        ScfEnquiryObject object = new ScfEnquiryObject();
        object.setEnquiryNo(anEnquiryNo);
        object.setFactorNo(factorNo);
        object.setBusinStatus("-1");
        return AjaxObject.newOk("保存成功", enquiryObjectService.saveModify(object)).toJson();
    }
    
    @Override
    public String webCustDropOffer(String anEnquiryNo, Long offerId) {
        logger.debug("询价企业放弃某个资金方的报价,入参：factorNo:"+ anEnquiryNo + " -offerId:"+offerId);
        ScfOffer object = new ScfOffer();
        object.setEnquiryNo(anEnquiryNo);
        object.setId(offerId);
        object.setBusinStatus("-1");
        return AjaxObject.newOk("保存成功", offerService.saveModifyOffer(object, offerId)).toJson();
    }
    
    
}
