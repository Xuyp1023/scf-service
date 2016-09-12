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
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfEnquiryMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfEnquiryOrder;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.order.service.ScfOrderService;
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
    private ScfOrderService scfOrderService;

    @Autowired
    private ScfEnquiryOrderService scfEnquiryOrderService;

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

        // 保存询价所关联的订单关系
        List<String> orderList = BetterStringUtils.splitTrim(anEnquiry.getOrders());
        for (String orderId : orderList) {
            ScfEnquiryOrder scfEnquiryOrder = new ScfEnquiryOrder();
            scfEnquiryOrder.setEnquiryNo(anEnquiry.getEnquiryNo());
            scfEnquiryOrder.setOrderId(Long.parseLong(orderId));
            scfEnquiryOrder.setOrderType(anEnquiry.getRequestType());
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
            scfEnquiry.setOrder(scfOrderService.findInfoListByRequest(scfEnquiry.getEnquiryNo(), scfEnquiry.getRequestType()));
            scfEnquiry.setCustName(custAccountService.queryCustName(scfEnquiry.getCustNo()));
        }
        return page;
    }

    /**
     * 查询询价详情
     * 
     * @param anId
     * @return
     */
    public ScfEnquiry findEnquiryDetail(Long anId) {
        ScfEnquiry enquiry = this.selectByPrimaryKey(anId);
        enquiry.setCustName(custAccountService.queryCustName(enquiry.getCustNo()));
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
        return Collections3.getFirst(list);
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

        // 如果原意向订单ids与修改后的值不同则要修改
        if (BetterStringUtils.equals(sourceEnquiry.getOrders(), enquiry.getOrders()) == false) {
            updateOrderRelation(enquiry, sourceEnquiry);
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
     * 修改询价与相关订单关系
     * @param anEnquiry
     * @param sourceEnquiry
     * @return
     */
    private void updateOrderRelation(ScfEnquiry anEnquiry, ScfEnquiry sourceEnquiry) {
        // 删除原有的关联订单
        Map<String, Object> qyOrderCondition = new HashMap<String, Object>();
        qyOrderCondition.put("enquiryNo", sourceEnquiry.getEnquiryNo());
        List<ScfEnquiryOrder> orderList = scfEnquiryOrderService.selectByClassProperty(ScfEnquiryOrder.class, qyOrderCondition);
        for (ScfEnquiryOrder object : orderList) {
            scfEnquiryOrderService.delete(object);
        }

        // 保存新选的关联订单
        List<String> orders = BetterStringUtils.splitTrim(anEnquiry.getFactors());
        for (String orderId : orders) {
            ScfEnquiryOrder order = new ScfEnquiryOrder();
            order.setOrderId(Long.parseLong(orderId));
            order.setEnquiryNo(sourceEnquiry.getEnquiryNo());
            order.setOrderType(anEnquiry.getRequestType());
            scfEnquiryOrderService.add(order);
        }
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
            qyOfferMap.put("businStatus", 1);
            qyOfferMap.put("enquiryNo", scfEnquiry.getEnquiryNo());
            List<ScfOffer> offerList = offerService.selectByClassProperty(ScfOffer.class, qyOfferMap);
            scfEnquiry.setOfferList(offerList);
            scfEnquiry.setOfferCount(offerList.size());
        }

        return enquiryList;
    }
}
