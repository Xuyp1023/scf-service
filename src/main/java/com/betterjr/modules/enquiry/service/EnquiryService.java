package com.betterjr.modules.enquiry.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfEnquiryMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service
public class EnquiryService extends BaseService<ScfEnquiryMapper, ScfEnquiry> {

    @Autowired
    private OfferService offerService;

    @Autowired
    private EnquiryObjectService enquiryObjectService;
    
    @Autowired
    private CustAccountService custAccountService;

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
            if (BetterStringUtils.isBlank(scfEnquiry.getEnquiryNo())) {
                continue;
            }
            scfEnquiry.setCustName(custAccountService.queryCustName(scfEnquiry.getCustNo()));

            // 根据enquiryNo查询出该询价的报价（未取消的）
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("enquiryNo", scfEnquiry.getEnquiryNo());
            map.put("businStatus", 1);
            scfEnquiry.setOfferCount(offerService.selectCountByProperty(map));
        }
        return page;
    }

    /**
     * 新增询价
     * 
     * @param anEnquiry
     * @return
     */
    public ScfEnquiry addEnquiry(ScfEnquiry anEnquiry) {
        BTAssert.notNull(anEnquiry, "anEnquiry is null");
        
        //保存多选的询价对象
        String[] factors =  anEnquiry.getFactors().split(",");
        for (int i = 0; i < factors.length; i++) {
            ScfEnquiryObject object = new ScfEnquiryObject();
            object.setFactorNo(Long.parseLong(factors[i]));
            object.setCustNo(anEnquiry.getCustNo());
            object.setEnquiryNo(anEnquiry.getEnquiryNo());
            enquiryObjectService.add(object);
        }
        
        anEnquiry.init();
        this.insert(anEnquiry);
        return anEnquiry;
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
     * 新增
     * @param anEnquiry
     * @return
     */
    public ScfEnquiry saveModifyEnquiry(Map<String, Object> anMap, Long anId) {
        ScfEnquiry enquiry = (ScfEnquiry) RuleServiceDubboFilterInvoker.getInputObj();
        BTAssert.notNull(enquiry, "saveModifyEnquiry service failed Enquiry =null");
        
        //检查该数据据是否合法
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("custNo", enquiry.getCustNo());
        anPropValue.put("id", anId);
        List<ScfEnquiry> list = selectByClassProperty(ScfEnquiry.class, anPropValue);
        if(CollectionUtils.isEmpty(list) || list.size() == 0){
            new ScfEnquiry();
        }
        
        enquiry.setId(anId);
        enquiry.setModiBaseInfo();
        this.updateByPrimaryKeySelective(enquiry);
        return enquiry;
    }

    /**
     * 查看保理公司收到的询价
     * @param anFactorNo
     * @return
     */
    public Page<ScfEnquiry> queryEnquiryByfactorNo(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        //根据保理公司编号获取询价关系
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anMap.get("factorNo"));
        List<ScfEnquiryObject> list = enquiryObjectService.findList(map);
        
        //从询价关系表中获取询价编号
        List<String> enquiryNoList = new ArrayList<String>();
        for (ScfEnquiryObject scfEnquiryObject : list) {
            enquiryNoList.add(scfEnquiryObject.getEnquiryNo());
        }
        
        //去除保理公司编号
        anMap.remove("factorNo");
        
        //批量指获取询
        anMap.put("enquiryNo", enquiryNoList);
        Page<ScfEnquiry> enquiryList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        
        //设置询价公司名称
        for (ScfEnquiry scfEnquiry : enquiryList) {
            scfEnquiry.setCustName(custAccountService.queryCustName(scfEnquiry.getCustNo()));
        }
        
        return enquiryList;
    }
}
