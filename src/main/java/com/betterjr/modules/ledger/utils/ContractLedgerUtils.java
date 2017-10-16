package com.betterjr.modules.ledger.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;

/***
 * 合同台账一些工具类
 * @author hubl
 *
 */
public class ContractLedgerUtils {

    public static Map<String, Object> convertDateStr(Map<String, Object> anParams) {
        Iterator<Entry<String, Object>> it = anParams.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String paramName = entry.getKey();
            String value = "";
            if (paramName.toUpperCase().contains("DATE")) {
                String tmpStr = (String) entry.getValue();
                if (StringUtils.isNotBlank(tmpStr)) {
                    value = tmpStr.replace("-", "");
                }
                anParams.put(paramName, value);
            }
        }
        return anParams;
    }

}
