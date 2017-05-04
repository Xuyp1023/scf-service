// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年5月4日, liuwl, creation
// ============================================================================
package com.betterjr.modules.commission.dubbo;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.ICommissionPayResultService;
import com.betterjr.modules.commission.service.CommissionPayResultService;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = ICommissionPayResultService.class)
public class CommissionPayResultDubboService implements ICommissionPayResultService {
    @Resource
    private CommissionPayResultService commissionPayResultService;

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webCreatePayResult(java.lang.String, java.lang.String, java.lang.Long)
     */
    @Override
    public String webCreatePayResult(final String anImportDate, final String anPayDate, final Long anCustNo) {
        return AjaxObject.newOk("", commissionPayResultService.saveCreatePayResult(anImportDate, anPayDate, anCustNo)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryUncheckCommissionRecord(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryUncheckCommissionRecord(final Long anCustNo, final String anImportDate, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryNormalPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryNormalPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryConfirmPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryConfirmPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryAuditPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryAuditPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryUncheckPayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQueryUncheckPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQuerySuccessPayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQuerySuccessPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryFailurePayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQueryFailurePayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmSuccessPayResultRecords(java.lang.Long, java.lang.Long[])
     */
    @Override
    public String webConfirmSuccessPayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmFailurePayResultRecords(java.lang.Long, java.lang.Long[])
     */
    @Override
    public String webConfirmFailurePayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webSuccessToFailurePayResultRecord(java.lang.Long, java.lang.Long)
     */
    @Override
    public String webSuccessToFailurePayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webFailureToSuccessPayResultRecord(java.lang.Long, java.lang.Long)
     */
    @Override
    public String webFailureToSuccessPayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmPayResult(java.lang.Long)
     */
    @Override
    public String webConfirmPayResult(final Long anPayResultId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webAuditPayResult(java.lang.Long)
     */
    @Override
    public String webAuditPayResult(final Long anPayResultId) {
        // TODO Auto-generated method stub
        return null;
    }

}
