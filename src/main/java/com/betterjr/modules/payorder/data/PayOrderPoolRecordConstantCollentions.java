package com.betterjr.modules.payorder.data;

public class PayOrderPoolRecordConstantCollentions {

    // 状态：0 未处理 1付款中 2 复核中 3付款失败 4 付款成功 5 解析生成 6审核生效 9数据失效

    public static final String PAY_RECORD_BUSIN_STATUS_NOHANDLE = "0"; // 0 未处理

    public static final String PAY_RECORD_BUSIN_STATUS_PAYING = "1"; // 1 付款中

    public static final String PAY_RECORD_BUSIN_STATUS_AUDITING = "2"; // 2 复核中

    public static final String PAY_RECORD_BUSIN_STATUS_PAYFAILURE = "3"; // 3付款失败

    public static final String PAY_RECORD_BUSIN_STATUS_PAYSUCCESS = "4"; // 4 付款成功

    public static final String PAY_RECORD_BUSIN_STATUS_NOEFFECTIVE = "9"; // 9数据失效
    
    //数据来源 0 融资申请  1上传解析
    
    public static final String PAY_RECORD_INFO_TYPE_REQUEST = "0"; //  0生成付款文件
    
    public static final String PAY_RECORD_INFO_TYPE_UPLOADRESOLVE = "1"; //  1上传解析

}
