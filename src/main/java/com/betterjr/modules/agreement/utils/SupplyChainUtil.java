package com.betterjr.modules.agreement.utils;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.sys.entity.DictItemInfo;

public class SupplyChainUtil {
    public static final String FACTOR_CORE_CUSTINFO = "FactorCoreCustInfo";

    private static final Logger logger = LoggerFactory.getLogger(SupplyChainUtil.class);

    private static String[] queryCondition = new String[] { "GTEagreeStartDate", "LTEagreeStartDate", "GTErequestDate", "LTErequestDate",
            "GTEinvoiceDate", "LTEinvoiceDate", "buyer", "supplier", "buyerNo", "billNo", "factorRequestNo", "productId", "GTErepayDate",
            "LTErepayDate", "cashRequestNo","agreeNo","status" };

    private static String[] fuzzyQueryCondition = new String[] { "buyer", "supplier" };

    /**
     * 将前台传递过来的查询条件加入查询Map中
     * 
     * @param anParam
     * @param anCondition
     */
    public static void addCondition(Map<String, Object> anParam, Map<String, Object> anCondition) {

        for (String tmpKey : queryCondition) {
            Object tmpValue = anParam.get(tmpKey);
            if (null == tmpValue || tmpValue.toString().isEmpty()) {
                continue;
            }
            boolean isFuzzy = false;
            for (String fuzzyKey : fuzzyQueryCondition) {
                if (tmpKey.equals(fuzzyKey)) {
                    isFuzzy = true;
                    break;
                }
            }
            if (isFuzzy) {
                anCondition.put("LIKE" + tmpKey, "%" + tmpValue + "%");
            }
            else {
                anCondition.put(tmpKey, tmpValue);
            }
        }
        logger.info("Get query condition:" + anCondition.toString());
    }

    /**
     * 获得系统定义的保理公司信息，去除沃通数字签名公司
     * 
     * @return
     */
    public static Map<String, String> findFactorCorpInfo() {
        Map<String, String> agencyGroup = DictUtils.getDictMap("ScfAgencyGroup");
        agencyGroup.remove("wos");

        return agencyGroup;
    }

    public static String findFactorNameByNo(String anAgencyNo) {

        return DictUtils.getDictLabel("ScfAgencyGroup", anAgencyNo);
    }

    public static String findCoreNameByCustNo(Long anCustNo) {

        return DictUtils.getDictLabelByCode(FACTOR_CORE_CUSTINFO, Long.toString(anCustNo));
    }

    public static List<Long> findCoreCustNoList() {
        List<Long> result = new ArrayList();
        for (DictItemInfo item : DictUtils.getDictList(FACTOR_CORE_CUSTINFO)) {
            try {
                result.add(Long.parseLong(item.getItemCode()));
            }
            catch (Exception ex) {
                logger.error("findCoreCustNoList has error : " + item);
            }
        }
        return result;
    }
}
