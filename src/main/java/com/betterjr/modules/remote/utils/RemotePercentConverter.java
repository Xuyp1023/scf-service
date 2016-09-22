package com.betterjr.modules.remote.utils;

import java.math.BigDecimal;

import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;

/**
 * 将数字转换为剩以100的数字。 入参的值必须是一个数字
 * 
 * @author henry
 *
 */
public class RemotePercentConverter implements RemoteBaseConverter {

    private static BigDecimal value100 = new BigDecimal(100);

    @Override
    public String convert(WorkFaceFieldInfo anFieldInfo, String anPropName, String anValue) {
        try {
            BigDecimal bb = new BigDecimal(anValue);
            if (bb != null) {
                return bb.multiply(value100).setScale(anFieldInfo.getFieldDictInfo().getDataScale()).toPlainString();
            }
        }
        catch (Exception ex) {

        }
        return anValue;
    }

}
