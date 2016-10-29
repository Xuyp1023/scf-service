package com.betterjr.modules.enquiry.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.enquiry.IScfEnquiryService;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfEnquiryOfferReply;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.ScfEnquiryObjectService;
import com.betterjr.modules.enquiry.service.ScfEnquiryOfferReplyService;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.push.service.ScfSupplierPushService;
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
    @Autowired
    private ScfAcceptBillService acceptBillService;
    @Autowired
    private ScfSupplierPushService supplierPushService;
    
    protected final Logger logger = LoggerFactory.getLogger(EnquiryDubboService.class);
    
    public String webQueryEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询询价列表,入参："+ anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询成功", enquiryService.queryEnquiryList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    public String webQuerySingleOrderEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询询价列表,入参："+ anMap);
        return AjaxObject.newOkWithPage("查询成功", enquiryService.querySingleOrderEnquiryList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    public String webAddEnquiry(Map<String, Object> anMap) {
        logger.debug("新增询价,入参："+ anMap);
        return AjaxObject.newOk(enquiryService.addEnquiry((ScfEnquiry) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }

    public String webFindEnquiryDetail(Long anId) {
        logger.debug("查询询价详情,入参：anId"+ anId);
        return AjaxObject.newOk(enquiryService.findEnquiryDetail(anId)).toJson();
    }
    
    public String webFindSingleOrderEnquiryDetail(String enquiryNo) {
        logger.debug("查询询价详情,入参：enquiryNo"+ enquiryNo);
        return AjaxObject.newOk(enquiryService.findSingleOrderEnquiryDetail(enquiryNo)).toJson();
    }

    public String webQueryOfferList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        logger.debug("分页查询报价列表,入参："+ anMap);
        return AjaxObject.newOkWithPage("查询成功", offerService.queryOfferList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    public String webSearchOfferList(Map<String, Object> anMap) {
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        logger.debug("分页查询报价列表,入参："+ anMap);
        return AjaxObject.newOk("查询成功", offerService.searchOfferList(anMap)).toJson();
    }
    
    public String webQueryOfferByEnquiryObject(String enquriyNo) {
        logger.debug("查看有哪些公司报了价,入参："+ enquriyNo);
        return AjaxObject.newOk("查询成功", offerService.queryOfferByEnquiryObject(enquriyNo)).toJson();
    }

    public String webQueryEnquiryObject(String enquiryNo, Long factorNo) {
        logger.debug("查看有哪些公司报了价,入参："+ enquiryNo);
        return AjaxObject.newOk("查询成功", enquiryObjectService.findByEnquiryNoAndObject(enquiryNo, factorNo)).toJson();
    }
    
    public String webAddOffer(Map<String, Object> anMap) {
        logger.debug("新增报价,入参："+ anMap);
        ScfOffer offer = offerService.addOffer((ScfOffer) RuleServiceDubboFilterInvoker.getInputObj());
       // sentOfferMsg(offer.getId());
        return AjaxObject.newOk(offer).toJson();
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
    public String webCustDropOffer(Long enquiryNo, Long offerId, String dropReason, String description) {
        logger.debug("询价企业放弃某个资金方的报价,入参：offerId:"+ offerId);
        ScfOffer offer = offerService.selectByPrimaryKey(offerId);
        offer.setId(offerId);
        offer.setDescription(description);
        offer.setBusinStatus("-1");
        offer.setDropReason(dropReason);
        
        //同时将询价对象中的状态也改为放弃
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("offerId", offerId);
        anMap.put("enquiryNo", enquiryNo);
        ScfEnquiryObject object = enquiryObjectService.find(anMap );
        object.setBusinStatus("-1");
        enquiryObjectService.saveModify(object);
        
        //报价数量减少
        ScfEnquiry enquiry = enquiryService.findEnquiryByNo(enquiryNo.toString());
        enquiry.setOfferCount(enquiry.getOfferCount()-1);
        if(null == enquiry.getOfferCount() || 0== enquiry.getOfferCount()){
            enquiry.setBusinStatus("0");
        }
        enquiryService.saveUpdate(enquiry);
        
        return AjaxObject.newOk(offerService.saveModifyOffer(offer, offerId)).toJson();
    }

    @Override
    public String webFindOfferList(String enquiryNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("enquiryNo", enquiryNo);
        return AjaxObject.newOk(offerService.findOfferList(map)).toJson();
    }
    
    /**
     * 发送报价消息
     * @param offer
     */
    @Override
    public void sentOfferMsg(Long offerId){
        ScfOffer offer = offerService.selectByPrimaryKey(offerId);
        Map<String, String> msgMap = new HashMap<String, String>();
        ScfEnquiry enquiry = enquiryService.findEnquiryByNo(offer.getEnquiryNo());
        ScfAcceptBill acceptBill = acceptBillService.findAcceptBill(Long.parseLong(enquiry.getOrders()));
        if(null != acceptBill){
            List<ScfOrder> orderList = acceptBill.getOrderList();
            if(!Collections3.isEmpty(orderList)){
                ScfOrder order = Collections3.getFirst(orderList);
                msgMap.put("productName", order.getGoodsName()+ "-" + order.getUnit());
            }
        }
        
        msgMap.put("enquiryNo", offer.getEnquiryNo());
        msgMap.put("offerTime", offer.getRegDate() + " " +offer.getRegTime());
        msgMap.put("balance", offer.getBalance().toString());
        msgMap.put("description", offer.getDescription());
        msgMap.put("sendCustNo", offer.getFactorNo().toString());
        msgMap.put("accCustNo", offer.getCustNo().toString());
        supplierPushService.pushScfEnquiryInfo(msgMap);
    }
    
}
