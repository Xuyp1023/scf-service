package com.betterjr.modules.commission.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;

@BetterjrMapper
public interface CommissionPayResultRecordMapper extends Mapper<CommissionPayResultRecord> {

    @Insert("INSERT INTO  t_cps_pay_result_record(ID, C_REFNO, L_PAY_RESULT_ID, L_RECORD_ID, C_RECORD_REFNO, "
            + "D_IMPORT_DATE, T_IMPORT_TIME, C_PAY_TARGET_BANK, C_PAY_TARGET_BANK_NAME, C_PAY_TARGET_BANK_ACCO, "
            + "C_PAY_TARGET_BANK_ACCO_NAME, L_CUSTNO, C_CUSTNAME, C_OPERORG, L_REG_OPERID, C_REG_OPERNAME, D_REG_DATE, T_REG_TIME)  "
            + "SELECT ID,C_REFNO,#{payResultId}, ID,C_REFNO, D_IMPORT_DATE, T_IMPORT_TIME, C_BANK, C_BANK_NAME, C_BANK_ACCOUNT, C_BANK_ACCOUNT_NAME, "
            + "#{custNo}, #{custName}, #{operOrg}, #{regOperId}, #{regOperName}, #{regDate}, #{regTime} FROM t_cps_record tcr "
            + "WHERE tcr.L_CUSTNO = #{custNo} AND tcr.C_OPERORG = #{operOrg} AND tcr.D_IMPORT_DATE = #{importDate}")
    public int createPayResultRecord(Map<String, Object> param);
}