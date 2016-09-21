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
        
        //将原有的报价改为历史
        this.saveModifyToHistory(anOffer);
        enquiryService.saveUpdateOfferCount(anOffer.getEnquiryNo(), 1);
        enquiryService.saveUpdateBusinStatus(anOffer.getEnquiryNo(), "1");
        
        //修改询价意向企业关系表中的状 为 已报价
        ScfEnquiryObject object = scfEnquiryObjectService.findByEnquiryNoAndObject(anOffer.getEnquiryNo(), anOffer.getFactorNo());
        object.setBusinStatus("1");
        scfEnquiryObjectService.saveModify(object);
        
        anOffer.init();
        this.insert(anOffer);
        return anOffer;
    }

    public ScfOffer saveUpdateTradeStatus(Long anId, String tradeStatus){
        ScfOffer offer = new ScfOffer();
        if(null == anId){
            return offer;
        }
        
        offer.setBusinStatus(tradeStatus);
        return this.saveModifyOffer(offer , anId);
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
           anMap.put("businStatus", new String[]{"1", "2"});
       }
        
       Page<ScfOffer> offerList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
       //设置保理公司名称
       for (ScfOffer offer : offerList) {
           offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
           offer.setCustName(custAccountService.queryCustName(offer.getCustNo()));
           offer.setEnquiry(enquiryService.findEnquiryByNo(offer.getEnquiryNo()));
       }
       return offerList;
    }
    
    /**
     * 根据意向企业分组查询报价
     * @return
     */
    public Page<ScfEnquiryObject> queryOfferByFactor(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize){
        Map<String, Object> qyObjectMap = new HashMap<String, Object>();
        qyObjectMap.put("enquiryNo", anMap.get("enquiryNo").toString());
        
        Page<ScfEnquiryObject> list = null;
        Map<String, Object> qyOfferMap = null;
        ScfEnquiry enquiry = enquiryService.findEnquiryByNo(anMap.get("enquiryNo").toString());
                
        if(BetterStringUtils.equals("2", enquiry.getEnquiryMethod())){
            //主动报价
            list = scfEnquiryObjectService.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
        }else{
            //自动报价(只查有报价的企业)
            qyObjectMap.put("businStatus", new String[]{"1", "-3"});
            list = scfEnquiryObjectService.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
        }
        
        for (ScfEnquiryObject object : list) {
            qyOfferMap = new HashMap<String, Object>();
            qyOfferMap.put("enquiryNo", anMap.get("enquiryNo").toString());
            qyOfferMap.put("factorNo", object.getFactorNo());
            List<ScfOffer> offerList = this.findOfferList(qyOfferMap);
           
            //如果查到了给出最近一次报价的信息
            if(false == Collections3.isEmpty(offerList)){
                object.setOffer(Collections3.getFirst(offerList));
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
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("factorNo", anFactorNo);
        anMap.put("enquiryNo", anEnquiryNo);
        anMap.put("businStatus", "1");
        List<ScfOffer> offerList =  this.selectByProperty(anMap);
        if(Collections3.isEmpty(offerList) || offerList.size() == 0){
            new ScfOffer();
        }
        
        ScfOffer offer = Collections3.getFirst(offerList);
        offer.setCustName(custAccountService.queryCustName(offer.getCustNo()));
        offer.setFactorName(custAccountService.queryCustName(offer.getFactorNo()));
        offer.setEnquiry(enquiryService.findEnquiryByNo(offer.getEnquiryNo()));
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
