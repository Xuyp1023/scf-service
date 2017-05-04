// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年5月3日, liuwl, creation
// ============================================================================
package com.betterjr.modules.commission.service;

import java.util.HashMap;
import java.util.Map;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.dao.CommissionPayResultRecordMapper;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;

/**
 * @author liuwl
 *
 */
public class CommissionPayResultRecordService extends BaseService<CommissionPayResultRecordMapper, CommissionPayResultRecord> {

    protected int saveCreatePayResultRecord(final Long anCustNo, final String anCustName, final String anOperOrg, final Long anPayResultId, final String anImportDate) {
        final Map<String, Object> param = new HashMap<>();

        param.put("importDate", anImportDate);
        param.put("operOrg", anOperOrg);
        param.put("custNo", anCustNo);
        param.put("payResultId", anPayResultId);
        param.put("custName", anCustName);

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
    public void saveConfirmSuccessPayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        // TODO Auto-generated method stub
    }

    /**
     * @param anPayResultId
     * @param anPayResultRecords
     */
    public void saveConfirmFailurePayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        // TODO Auto-generated method stub

    }

    /**
     * @param anPayResultId
     * @param anPayResultRecordId
     */
    public void saveSuccessToFailurePayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param anPayResultId
     * @param anPayResultRecordId
     */
    public void saveFailureToSuccessPayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionPayResultRecord> queryUncheckPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionPayResultRecord> querySuccessPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param anPayResultId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionPayResultRecord> queryFailurePayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }
}
