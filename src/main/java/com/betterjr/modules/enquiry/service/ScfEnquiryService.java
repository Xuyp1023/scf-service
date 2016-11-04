package com.betterjr.modules.enquiry.service;

import java.util.ArrayList;
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
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfEnquiryMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.receivable.service.ScfReceivableService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service
public class ScfEnquiryService extends BaseService<ScfEnquiryMapper, ScfEnquiry> {

    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfEnquiryObjectService enquiryObjectService;
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private ScfAcceptBillService billService;
    @Autowired
    private ScfReceivableService receivableService;
    @Autowired
    private ScfOrderService scfOrderService;

    /**
     * 新增询价
     * 
     * @param anEnquiry
     * @return
     */
    public ScfEnquiry addEnquiry(ScfEnquiry anEnquiry) {
        BTAssert.notNull(anEnquiry, "新增询价 失败-anEnquiry is null");
        anEnquiry.init();
        this.insert(anEnquiry);

        // 保存多选的询价对象关系
        List<String> factorList = BetterStringUtils.splitTrim(anEnquiry.getFactors());
        for (String factorId : factorList) {
            ScfEnquiryObject object = new ScfEnquiryObject();
            object.setFactorNo(Long.parseLong(factorId));
            object.setEnquiryNo(anEnquiry.getEnquiryNo());
            object.setCustNo(anEnquiry.getCustNo());
            enquiryObjectService.add(object);
        }

        return anEnquiry;
    }

    /**
     * 查询询价列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfEnquiry> queryEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfEnquiry> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        for (ScfEnquiry scfEnquiry : page) {
            this.fillOrder(scfEnquiry, 1);
            this.fillBusinStatus(scfEnquiry);
        }
        return page;
    }
    
    /**
     * 查询询价列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfEnquiry> querySingleOrderEnquiryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfEnquiry> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        for (ScfEnquiry enquiry :page) {
            this.fillOrder(enquiry, 2);
            //设置报价状态
            this.fillBusinStatus(enquiry);
        }
        return page;
    }

    /**
     * 
     * @param enquiry
     * @param type 1：2.0版本一个询价有多个订单，2：一个询价只有一个订单
     */
    private void fillOrder(ScfEnquiry enquiry, int type) {
        List idList = BetterStringUtils.splitTrim(enquiry.getOrders());
        enquiry.setCustName(custAccountService.queryCustName(enquiry.getCustNo()));
        
        if(1 == type){
            if(BetterStringUtils.equals("1", enquiry.getRequestType())){
                enquiry.setOrder(scfOrderService.selectByListProperty("id", idList));
            }else if(BetterStringUtils.equals("2", enquiry.getRequestType())){
                enquiry.setOrder(billService.selectByListProperty("id", idList));
            }else{
                enquiry.setOrder(receivableService.selectByListProperty("id", idList));
            }
            return ;
        }
        
        if(BetterStringUtils.equals("1", enquiry.getRequestType())){
            enquiry.setOrder(Collections3.getFirst(scfOrderService.selectByListProperty("id", idList)));
        }else if(BetterStringUtils.equals("2", enquiry.getRequestType())){
            enquiry.setOrder(Collections3.getFirst(billService.selectByListProperty("id", idList)));
        }else{
            enquiry.setOrder(Collections3.getFirst(receivableService.selectByListProperty("id", idList)));
        }
    }

    private void fillBusinStatus(ScfEnquiry enquiry) {
        //状态：-2：已融资，-1：放弃，0：未报价，1：已报价
        switch (enquiry.getBusinStatus()) {
            case "1":
                enquiry.setBusinStatusText(enquiry.getOfferCount() + "个报价");
                break;
            case "0":
                enquiry.setBusinStatusText("未报价");
                break;
             case "-1":
                 enquiry.setBusinStatusText("已放弃");
                break;
            default:
                enquiry.setBusinStatusText("已融资");
                break;
        }
    }

    /**
     * 查询询价详情
     * 
     * @param anId
     * @return
     */
    public ScfEnquiry findEnquiryDetail(Long anId) {
        ScfEnquiry enquiry = this.selectByPrimaryKey(anId);
        fillOrder(enquiry, 1);
        return enquiry;
    }
    
    /**
     * 查询询价详情
     * 
     * @param anId
     * @return
     */
    public ScfEnquiry findSingleOrderEnquiryDetail(String anEnquiryNo) {
        ScfEnquiry enquiry = this.findEnquiryByNo(anEnquiryNo);
        this.fillOrder(enquiry, 2);
        this.fillBusinStatus(enquiry);
        return enquiry;
    }
    
    /**
     * 查询询价详情
     * 
     * @param anId
     * @return
     */
    public ScfEnquiry findEnquiryByNo(String anEnquiryNo) {
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("enquiryNo", anEnquiryNo);
        List<ScfEnquiry> list = this.selectByClassProperty(ScfEnquiry.class, anPropValue);
        ScfEnquiry enquiry = Collections3.getFirst(list);
        if(null != enquiry){
            enquiry.setCustName(custAccountService.queryCustName(enquiry.getCustNo()));
            if(false == BetterStringUtils.isBlank(enquiry.getOrders())){
                enquiry.setOrder(billService.findAcceptBill(Long.parseLong(enquiry.getOrders())));
            }
        }
        return enquiry;
    }

    /**
     * 修改询价
     * 
     * @param anEnquiry
     * @return
     */
    public ScfEnquiry saveModifyEnquiry(Map<String, Object> anMap, Long anId) {
        ScfEnquiry enquiry = (ScfEnquiry) RuleServiceDubboFilterInvoker.getInputObj();
        BTAssert.notNull(enquiry, "修改询价 失败 saveModifyEnquiry service failed Enquiry =null");
        enquiry.setId(anId);
        
        //放弃融资
        if(BetterStringUtils.isNotBlank(enquiry.getBusinStatus()) && BetterStringUtils.equals("-1", enquiry.getBusinStatus())){
            return saveUpdate(enquiry);
        }
        
        // 原询价
        ScfEnquiry sourceEnquiry = this.selectByPrimaryKey(enquiry.getId());

        // 如果原意向企业ids与修改后的值不同则要修改
        if (BetterStringUtils.equals(sourceEnquiry.getFactors(), enquiry.getFactors()) == false) {
            updateObjectRelation(enquiry, sourceEnquiry);
        }

        return saveUpdate(enquiry);
    }

    public ScfEnquiry saveUpdate(ScfEnquiry anEnquiry) {
        anEnquiry.initModify();
        this.updateByPrimaryKeySelective(anEnquiry);
        return anEnquiry;
    }

    /**
     * 修改报价次数
     * @param anEnquiryNo
     * @param anCount
     */
    public void saveUpdateOfferCount(String anEnquiryNo, int anCount) {
        ScfEnquiry enquiry = this.findEnquiryByNo(anEnquiryNo);
        enquiry.setOfferCount(enquiry.getOfferCount() + anCount);
        this.updateByPrimaryKeySelective(enquiry);
    }

    /**
     * 修改询价状态
     * @param anEnquiryNo
     * @param businStatus
     */
    public void saveUpdateBusinStatus(String anEnquiryNo, String businStatus) {
        ScfEnquiry enquiry = this.findEnquiryByNo(anEnquiryNo);
        enquiry.setBusinStatus(businStatus);
        this.updateByPrimaryKeySelective(enquiry);
    }

    /**
     * 修改询价与意向企业关系
     * @param anEnquiry
     * @param sourceEnquiry
     * @return
     */
    private Map<String, Object> updateObjectRelation(ScfEnquiry anEnquiry, ScfEnquiry sourceEnquiry) {
        // 删除原有的询问对象
        Map<String, Object> qyObjectCondition = new HashMap<String, Object>();
        qyObjectCondition.put("enquiryNo", sourceEnquiry.getEnquiryNo());
        List<ScfEnquiryObject> objectList = enquiryObjectService.findList(qyObjectCondition);
        for (ScfEnquiryObject object : objectList) {
            enquiryObjectService.delete(object);
        }

        // 保存新选的询问对象
        List<String> factors = BetterStringUtils.splitTrim(anEnquiry.getFactors());
        for (String factorId : factors) {
            ScfEnquiryObject object = new ScfEnquiryObject();
            object.setFactorNo(Long.parseLong(factorId));
            object.setEnquiryNo(sourceEnquiry.getEnquiryNo());
            object.setCustNo(anEnquiry.getCustNo());
            enquiryObjectService.add(object);
        }
        return qyObjectCondition;
    }

    /**
     * 查看保理公司收到的询价
     * 
     * @param anFactorNo
     * @return
     */
    public Page<ScfEnquiry> queryEnquiryByfactorNo(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        // 根据保理公司编号获取询价关系
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anMap.get("factorNo"));
        List<ScfEnquiryObject> list = enquiryObjectService.findList(map);
        if (Collections3.isEmpty(list)) {
            return new Page<>(anPageNum, anPageSize, 1 == anFlag);
        }

        // 从询价关系表中获取询价编号
        List<String> enquiryNoList = new ArrayList<String>();
        for (ScfEnquiryObject scfEnquiryObject : list) {
            enquiryNoList.add(scfEnquiryObject.getEnquiryNo());
        }

        // 批量指获取询
        Map<String, Object> qyEnquiryMap = new HashMap<String, Object>();
        qyEnquiryMap.put("enquiryNo", enquiryNoList);
        Page<ScfEnquiry> enquiryList = this.selectPropertyByPage(qyEnquiryMap, anPageNum, anPageSize, 1 == anFlag);

        // 设置询价公司名称
        for (ScfEnquiry scfEnquiry : enquiryList) {
            scfEnquiry.setCustName(custAccountService.queryCustName(scfEnquiry.getCustNo()));

            // 查询我的报价
            Map<String, Object> qyOfferMap = new HashMap<String, Object>();
            qyOfferMap.put("factorNo", anMap.get("factorNo"));
            qyOfferMap.put("businStatus", new String[]{"1", "3"});
            qyOfferMap.put("enquiryNo", scfEnquiry.getEnquiryNo());
            List<ScfOffer> offerList = offerService.selectByClassProperty(ScfOffer.class, qyOfferMap);
            scfEnquiry.setOfferList(offerList);
            scfEnquiry.setOfferCount(offerList.size());
        }

        return enquiryList;
    }
    
}
