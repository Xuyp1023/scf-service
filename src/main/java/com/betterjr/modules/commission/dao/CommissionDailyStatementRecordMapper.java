package com.betterjr.modules.commission.dao;

import org.apache.ibatis.annotations.Insert;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.commission.entity.CommissionDailyStatement;
import com.betterjr.modules.commission.entity.CommissionDailyStatementRecord;

@BetterjrMapper
public interface CommissionDailyStatementRecordMapper extends Mapper<CommissionDailyStatementRecord> {
    
    @Insert("INSERT INTO t_cps_daily_statement_record(ID,L_DAILY_STATEMENT_ID,C_DAILY_STATEMENT_REFNO,L_PAY_RECORD_ID,C_PAY_RECORD_REFNO,L_RECORD_ID,C_RECORD_REFNO,"
            + "D_IMPORT_DATE,T_IMPORT_TIME,C_PAYNO,D_PAY_DATE,T_PAY_TIME,F_PAY_BALANCE,C_PAY_TARGET_BANK,C_PAY_TARGET_BANKNAME,C_PAY_TARGET_BANK_FULLNAME,"
            + "C_PAY_TARGET_BANK_ACCO,C_PAY_TARGET_BANK_ACCO_NAME,C_PAY_BANK,C_PAY_BANKNAME,C_PAY_BANK_FULLNAME,C_PAY_BANK_ACCO,C_PAY_BANK_ACCO_NAME,C_PAY_RESULT,"
            + "C_PAY_COMMENT,C_BUSIN_STATUS,C_LAST_STATUS,L_CUSTNO,C_CUSTNAME,C_OPERORG,L_REG_OPERID,C_REG_OPERNAME,D_REG_DATE,T_REG_TIME)"
            + " SELECT ID,#{id},#{refNo},L_PAY_RESULT_ID,C_REFNO,L_RECORD_ID,C_RECORD_REFNO,D_IMPORT_DATE,T_IMPORT_TIME,C_PAYNO,D_PAY_DATE,T_PAY_TIME"
            + ",F_PAY_BALANCE,C_PAY_TARGET_BANK,C_PAY_TARGET_BANK_NAME,C_PAY_TARGET_BANK_FULLNAME,C_PAY_TARGET_BANK_ACCO,C_PAY_TARGET_BANK_ACCO_NAME,C_PAY_BANK,"
            + "C_PAY_BANK_NAME,C_PAY_BANK_FULLNAME,C_PAY_BANK_ACCO,C_PAY_BANK_ACCO_NAME,C_PAY_RESULT,C_PAY_COMMENT,#{businStatus},"
            + "#{businStatus},#{ownCustNo},#{ownCustName},#{operOrg},#{regOperId},"
            + "#{regOperName},#{regDate},#{regTime} FROM t_cps_pay_result_record"
            + " WHERE d_pay_date=#{payDate} and l_custno=#{ownCustNo} and C_PAY_RESULT=#{businStatus}")
    public int addDailyStatementRecord(CommissionDailyStatement dailyStatement);
}