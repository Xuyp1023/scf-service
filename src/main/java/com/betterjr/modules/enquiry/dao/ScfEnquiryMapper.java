package com.betterjr.modules.enquiry.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;

@BetterjrMapper
public interface ScfEnquiryMapper extends Mapper<ScfEnquiry> {
    
    //查询业务过程定义的规则
    @Select("SELECT * FROM t_scf_enquiry  WHERE c_enquiryNo IN(SELECT C_ENQUIRYNO FROM t_scf_enquiry_object WHERE L_FACTORNO = #{anFactorNo})")
    @ResultType(ScfEnquiry.class)
    public List<ScfEnquiry> findEnquiryByfactorNo(Long anFactorNo);

}