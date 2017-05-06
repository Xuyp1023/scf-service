// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年5月4日, liuwl, creation
// ============================================================================
package com.betterjr.modules.commission.dubbo;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    @Resource
    private CommissionPayResultService commissionPayResultService;

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webCreatePayResult(java.lang.String, java.lang.String, java.lang.Long)
     */
    @Override
    public String webCreatePayResult(final String anImportDate, final String anPayDate, final Long anCustNo) {
        return AjaxObject.newOk("日对账单创建成功！", commissionPayResultService.saveCreatePayResult(anImportDate, anPayDate, anCustNo)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryUncheckCommissionRecord(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryUncheckCommissionRecord(final Long anCustNo, final String anImportDate, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("未确认的佣金记录查询成功！", commissionPayResultService.queryUncheckCommissionRecord(anCustNo, anImportDate, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryNormalPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryNormalPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("未确认的账单记录查询成功！", commissionPayResultService.queryNormalPayResultList(anCustNo, anPayDate, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryConfirmPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryConfirmPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("已确认的账单记录查询成功！", commissionPayResultService.queryConfirmPayResultList(anCustNo, anPayDate, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryAuditPayResultList(java.lang.Long, java.lang.String, int, int, int)
     */
    @Override
    public String webQueryAuditPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("已审核的账单记录查询成功！", commissionPayResultService.queryAuditPayResultList(anCustNo, anPayDate, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryUncheckPayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQueryUncheckPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("未处理的账单支付记录查询成功！", commissionPayResultService.queryUncheckPayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQuerySuccessPayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQuerySuccessPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("支付成功的账单支付记录查询成功！", commissionPayResultService.querySuccessPayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webQueryFailurePayResultRecords(java.lang.Long, int, int, int)
     */
    @Override
    public String webQueryFailurePayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        return AjaxObject.newOkWithPage("支付失败的账单支付记录查询成功！", commissionPayResultService.queryFailurePayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmSuccessPayResultRecords(java.lang.Long, java.lang.Long[])
     */
    @Override
    public String webConfirmSuccessPayResultRecords(final Long anPayResultId, final String anPayResultRecords) {
        final List<Long> payResultRecords = COMMA_PATTERN.splitAsStream(anPayResultRecords).map(Long::valueOf).collect(Collectors.toList());
        return AjaxObject.newOk("设置账单支付成功成功！", commissionPayResultService.saveConfirmSuccessPayResultRecords(anPayResultId, payResultRecords)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmFailurePayResultRecords(java.lang.Long, java.lang.Long[])
     */
    @Override
    public String webConfirmFailurePayResultRecords(final Long anPayResultId, final String anPayResultRecords) {
        final List<Long> payResultRecords = COMMA_PATTERN.splitAsStream(anPayResultRecords).map(Long::valueOf).collect(Collectors.toList());
        return AjaxObject.newOk("设置账单支付失败成功！", commissionPayResultService.saveConfirmFailurePayResultRecords(anPayResultId, payResultRecords)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webSuccessToFailurePayResultRecord(java.lang.Long, java.lang.Long)
     */
    @Override
    public String webSuccessToFailurePayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        return AjaxObject.newOk("支付成功转支付失败设置成功！", commissionPayResultService.saveSuccessToFailurePayResultRecord(anPayResultId, anPayResultRecordId)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webFailureToSuccessPayResultRecord(java.lang.Long, java.lang.Long)
     */
    @Override
    public String webFailureToSuccessPayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        return AjaxObject.newOk("支付失败转支付成功设置成功！", commissionPayResultService.saveFailureToSuccessPayResultRecord(anPayResultId, anPayResultRecordId)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webConfirmPayResult(java.lang.Long)
     */
    @Override
    public String webConfirmPayResult(final Long anPayResultId) {
        return AjaxObject.newOk("确认日对账单成功！", commissionPayResultService.saveConfirmPayResult(anPayResultId)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.commission.ICommissionPayResultService#webAuditPayResult(java.lang.Long)
     */
    @Override
    public String webAuditPayResult(final Long anPayResultId) {
        return AjaxObject.newOk("审核日对账单成功！", commissionPayResultService.saveConfirmPayResult(anPayResultId)).toJson();
    }

}
