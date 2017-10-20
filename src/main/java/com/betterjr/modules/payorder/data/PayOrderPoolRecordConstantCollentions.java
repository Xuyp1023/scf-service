package com.betterjr.modules.payorder.data;

/**
 * 
 * @ClassName: PayOrderPoolRecordConstantCollentions 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:34:35 
 *
 */
public class PayOrderPoolRecordConstantCollentions {

    // 状态：0 未处理 1付款中 2 复核中 3付款失败 4 付款成功 5 解析生成 6审核生效 9数据失效
    /**
     * 0 未处理
     */
    public static final String PAY_RECORD_BUSIN_STATUS_NOHANDLE = "0";

    /**
     * 1 付款中
     */
    public static final String PAY_RECORD_BUSIN_STATUS_PAYING = "1";

    /**
     * 2 复核中
     */
    public static final String PAY_RECORD_BUSIN_STATUS_AUDITING = "2";

    /**
     * 3付款失败
     */
    public static final String PAY_RECORD_BUSIN_STATUS_PAYFAILURE = "3";

    /**
     * 4 付款成功
     */
    public static final String PAY_RECORD_BUSIN_STATUS_PAYSUCCESS = "4";

    /**
     * 9数据失效
     */
    public static final String PAY_RECORD_BUSIN_STATUS_NOEFFECTIVE = "9";

    // 数据来源 0 融资申请 1上传解析

    /**
     * 0生成付款文件
     */
    public static final String PAY_RECORD_INFO_TYPE_REQUEST = "0";

    /**
     * 1上传解析
     */
    public static final String PAY_RECORD_INFO_TYPE_UPLOADRESOLVE = "1";

    // 解析结果

    /**
     * 付款成功
     */
    public static final String PAY_RECORD_BUSIN_STATUS_CHINESS_PAYSUCCESS = "付款成功";

    /**
     * 付款失败
     */
    public static final String PAY_RECORD_BUSIN_STATUS_CHINESS_PAYFAILURE = "付款失败";
    
    /**
     * 符号分隔符
     */
    public static final String MARK_SEPARATOR = ",";
    
    /**
     * map 包含属性 状态
     */
    public static final String MAP_CONTAIN_PROPERTIES_BUSIN_STATUS = "businStatus";
    
    /**
     * map 包含属性 状态
     */
    public static final String MAP_CONTAIN_PROPERTIES_FACTORY_NO = "factoryNo";
}
