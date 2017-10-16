package com.betterjr.modules.agreement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.agreement.dao.ScfRequestCreditMapper;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.loan.entity.ScfRequest;

import java.util.*;

/**
 * 应收账款转让通知书-应收账款管理
 * @author zhoucy
 *
 */
@Service
public class ScfRequestCreditService extends BaseService<ScfRequestCreditMapper, ScfRequestCredit> {

    private static final Logger logger = LoggerFactory.getLogger(ScfRequestCreditService.class);

    /**
     * 根据订单号，查询转让协议中的明细内容
     * @param anRequestNo
     * @return
     */
    public Map<String, Object> findByRequestNo(String anRequestNo) {
        logger.info("findByRequestNo，anRequestNo：" + anRequestNo);
        Map<String, Object> creditMap = new HashMap<String, Object>();
        List<ScfRequestCredit> creditList = this.selectByProperty("requestNo", anRequestNo);
        creditMap.put("total", creditList.size());
        creditMap.put("requestNo", anRequestNo);
        List<Map<String, Object>> creditBillList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < creditList.size();) {
            ScfRequestCredit credit = creditList.get(i);
            Map<String, Object> creditBillListMap = new HashMap<String, Object>();
            Map<String, Object> anMap = new HashMap<String, Object>();
            anMap.put("requestNo", credit.getRequestNo());
            anMap.put("transNo", credit.getTransNo());
            List<ScfRequestCredit> billList = this.selectByProperty(anMap);
            creditList.removeAll(billList); // 移除列表
            billList = formatList(billList);
            creditBillListMap.put("billList", billList);
            creditBillListMap.put("total", billList.size());
            creditBillList.add(creditBillListMap);
            if (billList.size() <= 0) { // 避免没找到数据的情况
                creditList.remove(credit);
            }
        }
        creditMap.put("creditBillList", creditBillList);
        logger.info("creditMap :" + creditMap);
        return creditMap;
    }

    public List<ScfRequestCredit> formatList(List<ScfRequestCredit> anBillList) {
        List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
        for (ScfRequestCredit credit : anBillList) {
            credit.setEndDate(
                    BetterDateUtils.formatDate(BetterDateUtils.parseDate(credit.getEndDate()), "yyyy年MM月dd日"));
            creditList.add(credit);
        }
        return creditList;
    }

    public boolean updateCreditList(ScfRequest anRequest, String anNoticNo, List<ScfRequestCredit> anList,
            String anAgreeNo) {
        this.deleteByProperty("requestNo", anRequest.getRequestNo());
        for (ScfRequestCredit credit : anList) {
            BTAssert.notNull(credit.getInvoiceNo(), "请完善发票信息");
            credit.fillInfo(anRequest);
            credit.setConfirmNo(anNoticNo);
            // credit.setAgreeNo(anAgreeNo);
            this.insert(credit);
        }
        return true;
    }
}
