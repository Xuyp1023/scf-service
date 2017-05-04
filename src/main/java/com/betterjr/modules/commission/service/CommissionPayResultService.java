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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.constant.CommissionPayResultStatus;
import com.betterjr.modules.commission.dao.CommissionPayResultMapper;
import com.betterjr.modules.commission.entity.CommissionPayResult;
import com.betterjr.modules.commission.entity.CommissionPayResultRecord;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.entity.CustMechBase;

/**
 * @author liuwl
 *
 */
@Service
public class CommissionPayResultService extends BaseService<CommissionPayResultMapper, CommissionPayResult> {

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Resource
    private CommissionPayResultRecordService payResultRecordService;

    // 创建 日对账记录 所有日对账记录
    public CommissionPayResult saveCreatePayResult(final String anImportDate, final String anPayDate, final Long anCustNo) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        BTAssert.isTrue(BetterStringUtils.isNotBlank(anImportDate), "导入日期不允许为空！");
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anPayDate), "支付日期不允许为空！");

        BTAssert.notNull(anCustNo, "公司编号不允许为空！");

        final CustMechBase custMechBase = custMechBaseService.findBaseInfo(anCustNo);
        BTAssert.notNull(custMechBase, "没有找到该企业！");

        CommissionPayResult payResult = findByImportDateAndCustNo(anImportDate, anCustNo);
        BTAssert.isNull(payResult, "该企业当日记录已经创建对账记录：企业[" + custMechBase.getCustName() + "] 导入日期[" + anImportDate + "]");

        // TODO 检查是否有未审核数据

        final CustOperatorInfo operator = UserUtils.getOperatorInfo();

        payResult = new CommissionPayResult();

        payResult.setRefNo("");

        payResult.init(operator);

        final int result = this.insert(payResult);

        BTAssert.isTrue(result == 1, "数据保存失败！");

        final int resultCount = payResultRecordService.saveCreatePayResultRecord(anCustNo, custMechBase.getCustName(), custMechBase.getOperOrg(), payResult.getId(),
                anImportDate);

        return payResult;
    }

    /**
     *
     * @param anImportDate
     * @param anCustNo
     * @return
     */
    private CommissionPayResult findByImportDateAndCustNo(final String anImportDate, final Long anCustNo) {
        final Map<String, Object> conditionMap = new HashMap<>();

        conditionMap.put("importDate", anImportDate);
        conditionMap.put("ownCustNo", anCustNo);

        return Collections3.getFirst(this.selectByProperty(conditionMap));
    }

    /**
     *
     * @param anId
     * @param anString
     * @return
     */
    private CommissionPayResult findByIdAndCheckStatus(final Long anId, final String anStatus) {
        final CommissionPayResult commissionPayResult = this.selectByPrimaryKey(anId);

        BTAssert.notNull(commissionPayResult, "没有找到账单确认记录！");

        if (BetterStringUtils.isNotBlank(anStatus)) {
            BTAssert.isTrue(BetterStringUtils.equals("0", commissionPayResult.getBusinStatus()), "账单状态不正确！");
        }
        return commissionPayResult;
    }

    // 查询佣金记录
    public Page<Object> queryUncheckCommissionRecord(final Long anCustNo, final String anImportDate, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        return null;
    }

    // 查询日对账记录 未确认待确认
    public Page<CommissionPayResult> queryNormalPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("custNo", anCustNo);
        if (BetterStringUtils.isNotBlank(anPayDate)) {
            conditionMap.put("payDate", anPayDate);
        }
        conditionMap.put("businStatus", CommissionPayResultStatus.NORMAL);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    // 查询日对账记录 已确认待审核
    public Page<CommissionPayResult> queryConfirmPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("custNo", anCustNo);
        if (BetterStringUtils.isNotBlank(anPayDate)) {
            conditionMap.put("payDate", anPayDate);
        }
        conditionMap.put("businStatus", CommissionPayResultStatus.CONFIRM);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    // 查询日对账记录 已审核
    public Page<CommissionPayResult> queryAuditPayResultList(final Long anCustNo, final String anPayDate, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("custNo", anCustNo);
        if (BetterStringUtils.isNotBlank(anPayDate)) {
            conditionMap.put("payDate", anPayDate);
        }
        conditionMap.put("businStatus", CommissionPayResultStatus.AUDIT);

        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    // 未对账记录列表
    public Page<CommissionPayResultRecord> queryUncheckPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, null);

        return payResultRecordService.queryUncheckPayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize);
    }

    // 成功列表
    public Page<CommissionPayResultRecord> querySuccessPayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, null);

        return payResultRecordService.querySuccessPayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize);
    }

    // 失败列表
    public Page<CommissionPayResultRecord> queryFailurePayResultRecords(final Long anPayResultId, final int anFlag, final int anPageNum, final int anPageSize) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, null);

        return payResultRecordService.queryFailurePayResultRecords(anPayResultId, anFlag, anPageNum, anPageSize);
    }

    // 批量确认成功
    public Boolean saveConfirmSuccessPayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, CommissionPayResultStatus.NORMAL);

        payResultRecordService.saveConfirmSuccessPayResultRecords(anPayResultId, anPayResultRecords);

        return Boolean.TRUE;
    }

    // 批量确认失败
    public Boolean saveConfirmFailurePayResultRecords(final Long anPayResultId, final Long[] anPayResultRecords) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, CommissionPayResultStatus.NORMAL);

        payResultRecordService.saveConfirmFailurePayResultRecords(anPayResultId, anPayResultRecords);

        return Boolean.TRUE;
    }

    // 成功变失败
    public Boolean saveSuccessToFailurePayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, CommissionPayResultStatus.NORMAL);

        payResultRecordService.saveSuccessToFailurePayResultRecord(anPayResultId, anPayResultRecordId);

        return Boolean.TRUE;
    }

    // 失败变成功
    public Boolean saveFailureToSuccessPayResultRecord(final Long anPayResultId, final Long anPayResultRecordId) {
        BTAssert.isTrue(UserUtils.platformUser(), "操作失败！");

        findByIdAndCheckStatus(anPayResultId, CommissionPayResultStatus.NORMAL);

        payResultRecordService.saveFailureToSuccessPayResultRecord(anPayResultId, anPayResultRecordId);

        return Boolean.TRUE;
    }
}
