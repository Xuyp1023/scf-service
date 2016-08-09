package com.betterjr.modules.agreement.dao;

import java.util.List;
import java.util.Map;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;

@BetterjrMapper
public interface ScfElecAgreementMapper extends Mapper<ScfElecAgreement> {
    
    public List<ScfElecAgreement> findElecAgreeByStub(Map<String,Object> anMap);
}