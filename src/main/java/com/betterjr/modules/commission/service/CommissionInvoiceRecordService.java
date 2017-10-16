package com.betterjr.modules.commission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionInvoiceRecordMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionInvoice;
import com.betterjr.modules.commission.entity.CommissionInvoiceRecord;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;

@Service
public class CommissionInvoiceRecordService
        extends BaseService<CommissionInvoiceRecordMapper, CommissionInvoiceRecord> {

    @Autowired
    private CommissionMonthlyStatementService monthlyService;

    /**
     * 查询用于普通发票和专用发票的月账单信息
     * 
     * coreCustNo 为核心企业id invoiceType 发票类型 0 普通发票 1 专用发票 GTEbillMonth 对账月份 LTEbillMonth 对账月份
     * 
     * @param anParam
     * @param anPageNum
     * @param anPageSize
     * @return
     * @throws Exception
     */
    public Page<CommissionMonthlyStatement> queryCanInvoiceMonthlyList(Map<String, Object> anParam, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anParam, "查询月账单失败！条件为空");
        // 去除空白字符串的查询条件
        anParam = Collections3.filterMapEmptyObject(anParam);
        BTAssert.notNull(anParam.get("coreCustNo"), "查询月账单失败！条件为空");
        BTAssert.notNull(anParam.get("invoiceType"), "查询月账单失败！条件为空");
        // 指定查询普发票还是专用发票
        if (CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN
                .equals(anParam.get("invoiceType").toString())) {

            anParam.put("plainInvoiceFlag", "0");
        } else {

            anParam.put("specialInvoiceFlag", "0");
        }
        anParam.put("businStatus", new String[] { "2", "3", "4" });
        anParam.put("ownCustNo", anParam.get("coreCustNo"));
        anParam.remove("coreCustNo");
        anParam.remove("invoiceType");
        Page<CommissionMonthlyStatement> page = monthlyService.selectPropertyByPage(anParam, anPageNum, anPageSize,
                true, "id desc");

        return page;

    }

    /**
     * 将月账单封装进发票中，填充相关信息
     * 
     * @param anInvoice
     *            发票对象
     * @param anMonthlyIds
     *            月账单集合
     */
    public void fillDataToInvoice(CommissionInvoice anInvoice, String anMonthlyIds) {

        BTAssert.notNull(anMonthlyIds, "请选择月账单");
        // 将月账单id集合转换成月账单集合
        List<CommissionMonthlyStatement> recordList = convertInvoiceRecord(anMonthlyIds);
        BTAssert.notEmpty(recordList, "请选择月账单,未查询到月账单信息");
        // 遍历月账单集合 将数据封装到发票中，并生成明细记录信息
        for (CommissionMonthlyStatement monthly : recordList) {

            // 如果月账单的企业和发票中的核心企业不对应不能发到当前发票中
            if (!anInvoice.getCoreCustNo().equals(monthly.getOwnCustNo())) {
                BTAssert.notNull(null, "此账单不是当前企业对应的账单，账单编号为：" + monthly.getRefNo());
            }

            // 封装总金额
            if (anInvoice.getInvoiceType()
                    .equals(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN)) {
                // 普通发票的处理 发票总金额=月账单的付款成功金额
                anInvoice.setBalance(MathExtend.add(anInvoice.getBalance(), monthly.getPaySuccessBalance()));

            } else if (anInvoice.getInvoiceType()
                    .equals(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_SPECIAL)) {
                // 专用发票的处理 发票总金额=手续费+利息
                anInvoice.setBalance(MathExtend.add(anInvoice.getBalance(),
                        MathExtend.add(monthly.getInterest(), monthly.getTaxBalance())));

            } else {

                BTAssert.notNull(null, "暂时只支持普通发票和专用发票，创建失败");
            }

            // 封装税率
            if (anInvoice.getTaxRate() == null) {

                anInvoice.setTaxRate(monthly.getTaxRate());

            } else {

                if (anInvoice.getTaxRate().compareTo(monthly.getTaxRate()) != 0) {
                    BTAssert.notNull(null, "选择的账单税率不一致，请重新选择");
                }

            }

            // 将月账单转成发票明细存下来
            saveAddRecord(anInvoice, monthly);

        }

    }

    /**
     * 通过月账单新增发票记录信息
     * 
     * @param anInvoice
     * @param anMonthly
     */
    private CommissionInvoiceRecord saveAddRecord(CommissionInvoice anInvoice, CommissionMonthlyStatement anMonthly) {

        BTAssert.notNull(anInvoice, "新增明细失败！发票不存在");
        BTAssert.notNull(anInvoice.getId(), "新增明细失败！发票不存在");
        BTAssert.notNull(anMonthly, "新增明细失败！月账单为空");
        CommissionInvoiceRecord record = new CommissionInvoiceRecord();
        record.initAddValue(UserUtils.getOperatorInfo());
        record.setInvoiceType(anInvoice.getInvoiceType());
        if (record.getInvoiceType().equals(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN)) {

            record.setBalance(anMonthly.getPaySuccessBalance());
        } else {

            record.setBalance(MathExtend.add(anMonthly.getInterest(), anMonthly.getTaxBalance()));
        }

        record.setInvoiceId(anInvoice.getId());
        record.setMonthlyId(anMonthly.getId());
        record.setTaxRate(anMonthly.getTaxRate());
        this.insert(record);
        return record;
    }

    private List<CommissionMonthlyStatement> convertInvoiceRecord(String anMonthlyIds) {

        if (StringUtils.isNoneBlank(anMonthlyIds)) {

            List<CommissionMonthlyStatement> recordList = new ArrayList<>();
            if (anMonthlyIds.contains(",")) {

                String[] split = anMonthlyIds.split(",");
                for (String string : split) {
                    try {
                        Long.parseLong(string);
                        CommissionMonthlyStatement monthly = monthlyService.selectByPrimaryKey(Long.parseLong(string));
                        recordList.add(monthly);
                    }
                    catch (Exception e) {
                        logger.info("查询月账单出错 数据为：monthlyid=" + string + " 错误信息为：" + e.getMessage());
                    }
                }

            } else {

                try {
                    Long.parseLong(anMonthlyIds);
                    CommissionMonthlyStatement monthly = monthlyService
                            .selectByPrimaryKey(Long.parseLong(anMonthlyIds));
                    recordList.add(monthly);
                }
                catch (Exception e) {
                    logger.info("查询月账单出错 数据为：monthlyid=" + anMonthlyIds + " 错误信息为：" + e.getMessage());
                }

            }

            return recordList;
        }

        return null;
    }

    /**
     * 更新月账单的状态
     * 
     * @param anInvoiceId
     * @param anInvoiceType
     */
    public void saveUpdateRecord(Long anInvoiceId, String anInvoiceType) {

        BTAssert.notNull(anInvoiceId, "提交发票失败！发票不存在");
        Map map = QueryTermBuilder.newInstance().put("invoiceId", anInvoiceId).build();
        List<CommissionInvoiceRecord> list = this.selectByProperty(map);
        BTAssert.notNull(list, "提交发票失败！请选择月账单");
        for (CommissionInvoiceRecord record : list) {

            if (!anInvoiceType.equals(record.getInvoiceType())) {
                BTAssert.notNull(null, "提交发票失败！当前发票数据已经缺失");
            }
            CommissionMonthlyStatement monthly = monthlyService.selectByPrimaryKey(record.getMonthlyId());
            if (anInvoiceType.equals(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN)) {

                if ("1".equals(monthly.getPlainInvoiceFlag())) {
                    BTAssert.notNull(null, "提交发票失败！当前发票对应的月账单已经生成了发票");
                }
                monthly.setPlainInvoiceFlag("1");
            } else {

                if ("1".equals(monthly.getSpecialInvoiceFlag())) {
                    BTAssert.notNull(null, "提交发票失败！当前发票对应的月账单已经生成了发票");
                }
                monthly.setSpecialInvoiceFlag("1");
            }

            monthlyService.updateByPrimaryKeySelective(monthly);
        }

    }

    /**
     * 为确认的发票废止，释放月账单消息
     * 
     * @param anInvoiceId
     */
    public void saveAnnulRecord(Long anInvoiceId) {

        BTAssert.notNull(anInvoiceId, "提交发票失败！发票不存在");
        Map map = QueryTermBuilder.newInstance().put("invoiceId", anInvoiceId).build();
        List<CommissionInvoiceRecord> list = this.selectByProperty(map);

        for (CommissionInvoiceRecord record : list) {

            CommissionMonthlyStatement monthly = monthlyService.selectByPrimaryKey(record.getMonthlyId());
            if (record.getInvoiceType()
                    .equals(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN)) {

                monthly.setPlainInvoiceFlag("0");
            } else {

                monthly.setSpecialInvoiceFlag("0");
            }

            monthlyService.updateByPrimaryKeySelective(monthly);
        }

    }

}
