package com.betterjr.modules.enquiry.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
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
        if(null == enquiry){
            AjaxObject.newError("addEnquiry service failed enquiry=null");
        }
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
        ScfOffer offer = (ScfOffer) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == offer){
            return AjaxObject.newError("webAddOffer service failed offer =null").toJson();
        }
        return AjaxObject.newOk(offerService.addOffer(offer)).toJson();
    }

    public String webFindOfferDetail(Long anId) {
        logger.debug("查询报价详情,入参：id："+ anId);
        return AjaxObject.newOk(offerService.findOfferDetail(anId)).toJson();
    }

    @Override
    public String webSaveModifyEnquiry(Map<String, Object> anMap, Long anId) {
        logger.debug("修改询价,入参：anId："+ anId + "/n" + anMap);
        
        ScfEnquiry enquiry = (ScfEnquiry) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == enquiry){
            return AjaxObject.newError("webUpdateEnquiry service failed Enquiry =null").toJson();
        }
        
        //检查该数据据是否合法
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("custNo", enquiry.getCustNo());
        anPropValue.put("id", anId);
        List list = enquiryService.selectByClassProperty(ScfEnquiry.class, anPropValue);
        if(CollectionUtils.isEmpty(list)){
            return AjaxObject.newError("webSaveModifyEnquiry service failed Enquiry error").toJson();
        }
        
        enquiry.setId(anId);
        return AjaxObject.newOk(enquiryService.saveModifyEnquiry(enquiry)).toJson();
    }

    @Override
    public String webSaveModifyOffer(Map<String, Object> anMap, Long anId) {
        logger.debug("修改报价,入参：anId："+ anId + "/n" + anMap);
        
        ScfOffer offer = (ScfOffer) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == offer){
            return AjaxObject.newError("webUpdateOffer service failed offer =null").toJson();
        }
        
        offer.setId(anId);
        return AjaxObject.newOk(offerService.saveModifyOffer(offer)).toJson();
    }
    
}
