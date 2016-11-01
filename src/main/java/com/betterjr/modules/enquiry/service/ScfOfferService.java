package com.betterjr.modules.enquiry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfOfferMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfOffer;

@Service
public class ScfOfferService extends BaseService<ScfOfferMapper, ScfOffer> {
    
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private ScfEnquiryService enquiryService;
    @Autowired
    private ScfEnquiryObjectService scfEnquiryObjectService;
    @Autowired
    private CustAccountService accountService;
    
    
    /**
     * 保存报价
     * @param id
     * @return
     */
    public ScfOffer addOffer(ScfOffer anOffer) {
        BTAssert.notNull(anOffer, "增加报价失败addOffer service failed offer =null");
        
        ScfEnquiry enquiry = enquiryService.findEnquiryByNo(anOffer.getEnquiryNo());
        if(BetterStringUtils.equals("1", enquiry.getEnquiryMethod())){
            anOffer.setCustNo(enquiry.getCustNo());
        }
        
        //保存报价
        anOffer.init();
        this.insert(anOffer);
        
        //将原有的报价改为历史
        this.saveModifyToHistory(anOffer);
        enquiryService.saveUpdateOfferCount(anOffer.getEnquiryNo(), 1);
        enquiryService.saveUpdateBusinStatus(anOffer.getEnquiryNo(), "1");
        
        //修改询价意向企业关系表中的状 为 已报价
        ScfEnquiryObject object = scfEnquiryObjectService.findByEnquiryNoAndObject(anOffer.getEnquiryNo(), anOffer.getFactorNo());
        object.setOfferId(anOffer.getId());
        object.setBusinStatus("1");
        scfEnquiryObjectService.saveModify(object);
        
        return anOffer;
    }

    /**
     * 修改报价状态
     * @param anId
     * @param tradeStatus
     * @return
     */
    public ScfOffer saveUpdateTradeStatus(Long anId, String tradeStatus){
        ScfOffer offer = selectByPrimaryKey(anId);
        BTAssert.notNull(offer, "修改报价失败，没有找到该报价！");
        offer.setBusinStatus(tradeStatus);
        return this.saveModifyOffer(offer, anId);
    }
    
    
    public ScfOffer saveModifyOffer(ScfOffer anOffer, Long anId) {
        //取消报价:1.要修改报价次数, 2.修改报价与意向企业关联表中的报价状态
        if (BetterStringUtils.equals("0", anOffer.getBusinStatus())) {
            enquiryService.saveUpdateOfferCount(anOffer.getEnquiryNo(), -1);
            ScfEnquiryObject object = scfEnquiryObjectService.findByEnquiryNoAndObject(anOffer.getEnquiryNo(), anOffer.getFactorNo());
            object.setBusinStatus("-3");
            scfEnquiryObjectService.saveModify(object);
        }
        
        BTAssert.notNull(anOffer);
        anOffer.setId(anId);
        anOffer.initModify();
        this.updateByPrimaryKeySelective(anOffer);
        return anOffer;
    }
    
    /**
     * 该公司之前 对本询价 发出的报价状态 改为历史
     * @param anOffer
     */
    private void saveModifyToHistory(ScfOffer anOffer) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("factorNo", anOffer.getFactorNo());
        anMap.put("enquiryNo", anOffer.getEnquiryNo());
        Map<String, Object> propValue = new HashMap<String, Object>();
        List<ScfOffer> list = this.selectByClassProperty(ScfOffer.class, propValue);
        for (ScfOffer scfOffer : list) {
            scfOffer.setBusinStatus("2");//历史报价
            this.updateByPrimaryKeySelective(scfOffer);
        }
    }

    /**
     * 查询报价列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfOffer> queryOfferList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
       if(anMap.get("businStatus") == null || BetterStringUtils.isEmpty(anMap.get("businStatus").toString())){
           anMap.put("businStatus", new String[]{"1", "3"});
       }
        
       Page<ScfOffer> offerList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
       for (ScfOffer offer : offerList) {
           //设置保理公司名称
           fillInfo(offer);
       }
       return offerList;
    }

    private void fillInfo(ScfOffer anOffer) {
        anOffer.setFactorName(custAccountService.queryCustName(anOffer.getFactorNo()));
        anOffer.setCustName(custAccountService.queryCustName(anOffer.getCustNo()));
        anOffer.setEnquiry(enquiryService.findEnquiryByNo(anOffer.getEnquiryNo()));
    }
    
    /**
     * 无分页查询报价列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<ScfOffer> searchOfferList(Map<String, Object> anMap) {
       if(anMap.get("businStatus") == null || BetterStringUtils.isEmpty(anMap.get("businStatus").toString())){
           anMap.put("businStatus", new String[]{"1", "3"});
       }
        
       List<ScfOffer> offerList = this.selectByClassProperty(ScfOffer.class, anMap);
       for (ScfOffer offer : offerList) {
           //设置保理公司名称
           fillInfo(offer);
           offer.setEnquiry(enquiryService.findEnquiryByNo(offer.getEnquiryNo()));
       }
       return offerList;
    }
    
    /**
     * 根据意向企业分组查询报价
     * @return
     */
    public List<ScfEnquiryObject> queryOfferByEnquiryObject(String enquriyNo){
        ScfEnquiry enquiry = enquiryService.findSingleOrderEnquiryDetail(enquriyNo);
        Map<String, Object> qyObjectMap = QueryTermBuilder.newInstance().put("enquiryNo", enquiry.getEnquiryNo()).build();
        List<ScfEnquiryObject> list = null;
        if(BetterStringUtils.equals("2", enquiry.getEnquiryMethod())){
            //主动报价
            list = scfEnquiryObjectService.selectByClassProperty(ScfEnquiryObject.class, qyObjectMap);
        }
        else{
            //自动报价(只查有报价的企业)
            qyObjectMap.put("businStatus", new String[]{"1", "-3"});
            list = scfEnquiryObjectService.selectByClassProperty(ScfEnquiryObject.class, qyObjectMap);
        }
        
        for (ScfEnquiryObject object : list) {
            if(null != object.getOfferId()){
                ScfOffer offer = this.selectByPrimaryKey(object.getOfferId());
                //if(null != offer && BetterStringUtils.equals("1", offer.getBusinStatus())){
                    object.setOffer(offer);
                //}
            }
            
            object.setFactorName(accountService.queryCustName(object.getFactorNo()));
        }
        return list;
    }
    
    /**
     * 查询报价详情
     * @param anFactorNo
     * @param anEnquiryNo
     * @return
     */
    public ScfOffer findOfferDetail(Long anFactorNo, String anEnquiryNo) {
        Map<String, Object> qyMap = QueryTermBuilder.newInstance().put("factorNo", anFactorNo).put("enquiryNo", anEnquiryNo).put("businStatus", new String[]{"1", "3"}).build();
        List<ScfOffer> offerList =  this.selectByProperty(qyMap);
        if(Collections3.isEmpty(offerList)){
           return new ScfOffer();
        }
        
        ScfOffer offer = Collections3.getFirst(offerList);
        fillInfo(offer);
        return offer;
    }
    
    /**
     * 查询报价(无分页)
     * @param anMap
     * @return
     */
    public List<ScfOffer> findOfferList(Map<String, Object> anMap) {
        anMap.put("businStatus", "1");
        List<ScfOffer> offerList = this.selectByClassProperty(ScfOffer.class, anMap);
        
        //设置保理公司名称
        for (ScfOffer offer : offerList) {
            offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
            offer.setEnquiry(enquiryService.findEnquiryByNo(offer.getEnquiryNo()));
        }
        return offerList;
    }

}
