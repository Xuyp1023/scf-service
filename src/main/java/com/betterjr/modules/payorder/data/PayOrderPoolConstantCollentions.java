package com.betterjr.modules.payorder.data;

/**
 * 
 * @ClassName: PayOrderPoolConstantCollentions 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:34:22 
 *
 */
public class PayOrderPoolConstantCollentions {
    
    
    /**
     *  @param @param anType 
     *                      1: 从未付款到付款中
     *                      2： 付款中到复核中
     *                      3： 复核中失败
     *                      4：复核中成功
     *                      5: 从复核中到付款中
     */
    
    public static final String PAYPOOL_SAVEUPDATEAMOUNT_TYPE_NOPAYTOPAYING = "1";
    
    /**
     * 2： 付款中到复核中
     */
    public static final String PAYPOOL_SAVEUPDATEAMOUNT_TYPE_PAYINGTOAUDIT = "2";
    
    /**
     *  3： 复核中失败
     */
    public static final String PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYFUILURE = "3";
    
    /**
     * 4：复核中成功
     */
    public static final String PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYSUCCESS = "4";
    
    /**
     * 5: 从复核中到付款中
     */
    public static final String PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYING = "5";

}
