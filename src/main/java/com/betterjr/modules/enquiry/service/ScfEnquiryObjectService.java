package com.betterjr.modules.enquiry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.enquiry.dao.ScfEnquiryObjectMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;

@Service
public class ScfEnquiryObjectService extends BaseService<ScfEnquiryObjectMapper, ScfEnquiryObject> {

    @Autowired
    private CustAccountService custAccountService;
    /**
     * 新增
     * @param id
     * @return
     */
    public ScfEnquiryObject add(ScfEnquiryObject object) {
        object.init();
        this.insert(object);
        return object;

    }

    /**
     * 修改
     * @param object
     * @return
     */
    public ScfEnquiryObject saveModify(ScfEnquiryObject object) {
        object.initModify();
        this.updateByPrimaryKeySelective(object);
        return object;
    }
    
    /**
     * 分页查询
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfEnquiryObject> queryList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
       return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public ScfEnquiryObject findDetail(Long id) {
        return this.selectByPrimaryKey(id);
    }
    
    /**
     * 根据询价编号和意向企业获取关系信息
     * @param id
     * @return
     */
    public ScfEnquiryObject findByEnquiryNoAndObject(String enquiryNo, Long factorNo) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("enquiryNo", enquiryNo);
        anMap.put("factorNo", factorNo);
        ScfEnquiryObject object = Collections3.getFirst(this.findList(anMap));
        object.setFactorName(custAccountService.queryCustName(factorNo));
        return object;
    }
    
    /**
     * 查询(无分页)
     * @param anMap
     * @return
     */
    public List<ScfEnquiryObject> findList(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfEnquiryObject.class, anMap);
    }

}
