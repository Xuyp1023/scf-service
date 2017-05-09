package com.betterjr.modules.commission.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.commission.data.CalcPayResult;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;

@BetterjrMapper
public interface CommissionPayResultRecordMapper extends Mapper<CommissionPayResultRecord> {

    @Insert("INSERT INTO t_cps_pay_result_record(ID, C_REFNO, L_PAY_RESULT_ID, L_RECORD_ID, C_RECORD_REFNO, "
            + "D_IMPORT_DATE, T_IMPORT_TIME, D_PAY_DATE, F_PAY_BALANCE, C_PAY_TARGET_BANK, C_PAY_TARGET_BANK_NAME, C_PAY_TARGET_BANK_ACCO, "
            + "C_PAY_TARGET_BANK_ACCO_NAME, C_PAY_TARGET_MOBILENO, L_CUSTNO, C_CUSTNAME, C_OPERORG, L_REG_OPERID, C_REG_OPERNAME, D_REG_DATE, T_REG_TIME, C_PAY_RESULT, C_BUSIN_STATUS)  "
            + "SELECT ID,C_REFNO,#{payResultId}, ID,C_REFNO, D_IMPORT_DATE, T_IMPORT_TIME, #{payDate}, F_BALANCE, C_BANK, C_BANK_NAME, C_BANK_ACCOUNT, C_BANK_ACCOUNT_NAME, C_CONTACTS_MOBILENO, "
            + "#{custNo}, #{custName}, #{operOrg}, #{regOperId}, #{regOperName}, #{regDate}, #{regTime}, '0', '0' FROM t_cps_record tcr "
            + "WHERE tcr.L_CUSTNO = #{custNo} AND tcr.C_OPERORG = #{operOrg} AND tcr.D_IMPORT_DATE = #{importDate}")
    public int createPayResultRecord(Map<String, Object> param);

    @Select("SELECT COUNT(ID) FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID = #{payResultId} AND C_BUSIN_STATUS = '0'")
    @ResultType(Long.class)
    public Long countUnconfirmPayResultRecord(@Param("payResultId") Long anPayResultId);

    @Select("SELECT COUNT(ID) FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID = #{payResultId} AND C_BUSIN_STATUS = '1' AND C_PAY_RESULT = '1'")
    @ResultType(Long.class)
    public Long countSuccessPayResultRecord(@Param("payResultId") Long anPayResultId);

    @Select("SELECT COUNT(ID) FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID = #{payResultId} AND C_BUSIN_STATUS = '1' AND C_PAY_RESULT = '2'")
    @ResultType(Long.class)
    public Long countFailurePayResultRecord(@Param("payResultId") Long anPayResultId);

    @Select("SELECT COUNT(ID) FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID = #{payResultId}")
    @ResultType(Long.class)
    public Long countAllPayResultRecord(@Param("payResultId") Long anPayResultId);

    @Select("SELECT t1.balance AS totalBalance, t1.amount AS totalAmount, t2.balance AS paySuccessBalance, t2.amount AS paySuccessAmount, "
            + "t3.balance AS payFailureBalance, t3.amount AS payFailureAmount, t4.balance AS unconfirmBalance, t4.amount AS unconfirmAmount FROM "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID=#{payResultId}) AS t1, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID=#{payResultId} AND C_PAY_RESULT='1' AND C_BUSIN_STATUS='1') AS t2, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID=#{payResultId} AND C_PAY_RESULT='2' AND C_BUSIN_STATUS='1') AS t3, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID=#{payResultId} AND C_PAY_RESULT='0' AND C_BUSIN_STATUS='0') AS t4 ")
    @Result(javaType=CalcPayResult.class)
    public CalcPayResult calcPayResultRecord(@Param("payResultId") Long anPayResultId);

    @Select("SELECT t1.balance AS totalBalance, t1.amount AS totalAmount, t2.balance AS paySuccessBalance, t2.amount AS paySuccessAmount, "
            + "t3.balance AS payFailureBalance, t3.amount AS payFailureAmount, t4.balance AS unconfirmBalance, t4.amount AS unconfirmAmount FROM "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE D_PAY_DATE=#{payDate} AND L_CUSTNO=#{custNo}) AS t1, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE D_PAY_DATE=#{payDate} AND C_PAY_RESULT='1' AND L_CUSTNO=#{custNo}) AS t2, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE D_PAY_DATE=#{payDate} AND C_PAY_RESULT='2' AND L_CUSTNO=#{custNo}) AS t3, "
            + "(SELECT SUM(F_PAY_BALANCE) AS balance, COUNT(ID) AS amount FROM t_cps_pay_result_record WHERE L_PAY_RESULT_ID=#{payResultId} AND C_PAY_RESULT='0' AND C_BUSIN_STATUS='0') AS t4 ")
    @Result(javaType=CalcPayResult.class)
    public CalcPayResult calcPayResultRecordByPayDate(@Param("custNo") Long anCustNo, @Param("payDate") String anPayDate);

    @Update("UPDATE t_cps_record cr INNER JOIN t_cps_pay_result_record cprr ON cr.ID = cprr.L_RECORD_ID "
            + "SET cr.C_PAY_STATUS = cprr.C_PAY_RESULT, cr.C_BUSIN_STATUS = '2' "
            + "WHERE cr.ID = cprr.L_RECORD_ID AND cprr.L_PAY_RESULT_ID = #{payResultId}")
    @ResultType(Long.class)
    public Long writebackRecordStatus(@Param("payResultId") Long anPayResultId);

    @Update("update t_cps_pay_result_record set C_BUSIN_STATUS=#{businStatus} where d_pay_date=#{payDate} and l_custno=#{ownCustNo}")
    @ResultType(Long.class)
    public Long saveRecordStatus(Map<String, Object> param);

    @Update("UPDATE t_cps_pay_result_record cprr "
            + "SET cprr.C_BUSIN_STATUS = '2' "
            + "WHERE cprr.L_PAY_RESULT_ID = #{payResultId}")
    @ResultType(Long.class)
    public void writebackPayResultRecordStatus(Long anPayResultId);
}