package com.betterjr.modules.supplieroffer.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequestAgreement;

@BetterjrMapper
public interface ScfReceivableRequestAgreementMapper extends Mapper<ScfReceivableRequestAgreement> {

    @Select("select DISTINCT L_FACTORYNO,C_FACTORYNAME  from t_scf_receivable_request_agreement where C_BUSIN_STATUS !='3'")
    @ResultType(List.class)
    public List<ScfReceivableRequestAgreement> queryDictFactory();
}
