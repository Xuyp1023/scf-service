package com.betterjr.modules.commission.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.commission.entity.CommissionRecord;

@BetterjrMapper
public interface CommissionRecordMapper extends Mapper<CommissionRecord> {

    @Select("SELECT COUNT(ID) as amount, SUM(F_BALANCE) as balance FROM t_cps_record cr WHERE cr.L_CUSTNO = #{custNo} AND cr.D_IMPORT_DATE = #{importDate} AND C_BUSIN_STATUS = '1'")
    @ResultType(Map.class)
    Map<String, Object> countRecordList(@Param("custNo")Long anCustNo, @Param("importDate")String anImportDate);
}