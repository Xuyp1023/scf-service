// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年5月3日, liuwl, creation
// ============================================================================
package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.constant.CommissionPayResultRecordStatus;
import com.betterjr.modules.commission.dao.CommissionPayResultRecordMapper;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;

/**
 * @author liuwl
 *
 */
@Service
public class CommissionPayResultRecordService extends BaseService<CommissionPayResultRecordMapper, CommissionPayResultRecord> {

    /**
     *
     * @param anCustNo
     * @param anCustName
     * @param anOperOrg
     * @param anPayResultId
     * @param anImportDate
     * @param anPayDate
     * @return
     */
    protected int saveCreatePayResultRecord(final Long anCustNo, final String anCustName, final String anOperOrg, final Long anPayResultId, final String anImportDate, final String anPayDate) {
        final Map<String, Object> param = new HashMap<>();

        param.put("importDate", anImportDate);
        param.put("operOrg", anOperOrg);
        param.put("custNo", anCustNo);
        param.put("payResultId", anPayResultId);
        param.put("custName", anCustName);
        param.put("payDate", anPayDate);

        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        param.put("regOperId", operator.getId());
        param.put("regOperName", operator.getName());
        param.put("regDate", BetterDateUtils.getNumDate());
        param.put("regTime", BetterDateUtils.getNumTime());

        return this.mapper.createPayResultRecord(param);
    }

    /**
     * @param anPayResultId
     * @param anPayResultRecords
     */
    protected void saveConfirmSuccessPayResultRecords(final Long anPayResultId, final List<Long> anPayResultRecords) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payResultId", anPayResultId);
        conditionMap.put("id", anPayResultRecords);

        final List<CommissionPayResultRecord> payResultRecords = this.selectByProperty(conditionMap);

        BTAssert.isTrue(anPayResultRecords.size() == payResultRecords.size(), "数据不匹配！");

        for (final CommissionPayResultRecord payResultRecord: payResultRecords) {
            BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.NORMAL, payResultRecord.getBusinStatus()), "数据状态不正确:[" + payResultRecord.getRecordRefNo() + "]");

            payResultRecord.setBusinStatus(CommissionPayResultRecordStatus.CONFIRM);
            payResultRecord.setPayResult(CommissionPayResultRecordStatus.PAY_SUCCESS);

            final int result = this.updateByPrimaryKeySelective(payResultRecord);

            BTAssert.isTrue(result == 1, "数据确认失败！:[" + payResultRecord.getRecordRefNo() + "]");
        }
    }

    /**
     * @param anPayResultId
     * @param anPayResultRecords
     */
    protected void saveConfirmFailurePayResultRecords(final Long anPayResultId, final List<Long> anPayResultRecords) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payResultId", anPayResultId);
        conditionMap.put("id", anPayResultRecords);

        final List<CommissionPayResultRecord> payResultRecords = this.selectByProperty(conditionMap);

        BTAssert.isTrue(anPayResultRecords.size() == payResultRecords.size(), "数据不匹配！");

        for (final CommissionPayResultRecord payResultRecord: payResultRecords) {
            BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.NORMAL, payResultRecord.getBusinStatus()), "数据状态不正确:[" + payResultRecord.getRecordRefNo() + "]");

            payResultRecord.setBusinStatus(CommissionPayResultRecordStatus.CONFIRM);
            payResultRecord.setPayResult(CommissionPayResultRecordStatus.PAY_FAILURE);

            final int result = this.updateByPrimaryKeySelective(payResultRecord);

            BTAssert.isTrue(result == 1, "数据确认失败！:[" + payResultRecord.getRecordRefNo() + "]");
        }
    }

    /**
     * @param anPayResultId
     * @param anPayResultRecordId
     */
    protected void saveSuccessToFailurePayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        final CommissionPayResultRecord payResultRecord = this.selectByPrimaryKey(anPayResultRecordId);
        BTAssert.notNull(payResultRecord, "数据未找到！");

        BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.CONFIRM, payResultRecord.getBusinStatus()), "数据状态不正确:[" + payResultRecord.getRecordRefNo() + "]");
        BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.PAY_SUCCESS, payResultRecord.getPayResult()), "数据支付状态不正确:[" + payResultRecord.getRecordRefNo() + "]");

        payResultRecord.setPayResult(CommissionPayResultRecordStatus.PAY_FAILURE);

        final int result = this.updateByPrimaryKeySelective(payResultRecord);
        BTAssert.isTrue(result == 1, "数据确认失败！:[" + payResultRecord.getRecordRefNo() + "]");
    }

    /**
     * @param anPayResultId
     * @param anPayResultRecordId
     */
    protected void saveFailureToSuccessPayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        final CommissionPayResultRecord payResultRecord = this.selectByPrimaryKey(anPayResultRecordId);
        BTAssert.notNull(payResultRecord, "数据未找到！");

        BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.CONFIRM, payResultRecord.getBusinStatus()), "数据状态不正确:[" + payResultRecord.getRecordRefNo() + "]");
        BTAssert.isTrue(BetterStringUtils.equals(CommissionPayResultRecordStatus.PAY_FAILURE, payResultRecord.getPayResult()), "数据支付状态不正确:[" + payResultRecord.getRecordRefNo() + "]");

        payResultRecord.setPayResult(CommissionPayResultRecordStatus.PAY_SUCCESS);

        final int result = this.updateByPrimaryKeySelective(payResultRecord);
        BTAssert.isTrue(result == 1, "数据确认失败！:[" + payResultRecord.getRecordRefNo() + "]");
    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> queryUncheckPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payResultId", anPayResultId);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.NORMAL);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> querySuccessPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payResultId", anPayResultId);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.CONFIRM);
        conditionMap.put("payResult", CommissionPayResultRecordStatus.PAY_SUCCESS);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> queryFailurePayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payResultId", anPayResultId);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.CONFIRM);
        conditionMap.put("payResult", CommissionPayResultRecordStatus.PAY_FAILURE);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayDate
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> queryAllPayResultRecords(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payDate", anPayDate);
        conditionMap.put("custNo", anCustNo);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }
    
    /**
     * @param anPayDate
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> queryUncheckPayResultRecords(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payDate", anPayDate);
        conditionMap.put("custNo", anCustNo);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.NORMAL);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayDate
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> querySuccessPayResultRecords(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payDate", anPayDate);
        conditionMap.put("custNo", anCustNo);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.CONFIRM);
        conditionMap.put("payResult", CommissionPayResultRecordStatus.PAY_SUCCESS);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayDate
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    protected Page<CommissionPayResultRecord> queryFailurePayResultRecords(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("payDate", anPayDate);
        conditionMap.put("custNo", anCustNo);
        conditionMap.put("businStatus", CommissionPayResultRecordStatus.CONFIRM);
        conditionMap.put("payResult", CommissionPayResultRecordStatus.PAY_FAILURE);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anPayResultId
     */
    public void checkConfirmStatus(final Long anPayResultId) {
        final Long unconfirmCount = this.mapper.countUnconfirmPayResultRecord(anPayResultId);

        BTAssert.isTrue(unconfirmCount != null && unconfirmCount <= 0L, "还有未确认交易数据！");
    }

    /**
     * @param anPayResultId
     * @return
     */
    public Map<String, Object> calcPayResultRecord(final Long anPayResultId) {
        return this.mapper.calcPayResultRecord(anPayResultId);
    }

    /**
     * @param anPayDate
     * @return
     */
    public Map<String, Object> calcPayResultRecord(final Long anCustNo, final String anPayDate) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");
        return this.mapper.calcPayResultRecordByPayDate(anCustNo, anPayDate);
    }

    /**
     * @param anPayResultId
     */
    public void saveWritebackRecordStatus(final Long anPayResultId) {
        this.mapper.writebackRecordStatus(anPayResultId);
    }
}
