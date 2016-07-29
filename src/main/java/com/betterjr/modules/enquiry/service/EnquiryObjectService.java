package com.betterjr.modules.enquiry.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.enquiry.dao.ScfEnquiryObjectMapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;

@Service
public class EnquiryObjectService extends BaseService<ScfEnquiryObjectMapper, ScfEnquiryObject> {

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
       boolean flag = 1==anFlag;
       return this.selectPropertyByPage(anMap, anPageNum, anPageSize, flag);
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
     * 查询(无分页)
     * @param anMap
     * @return
     */
    public List<ScfEnquiryObject> findList(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfEnquiryObject.class, anMap);
    }

}
