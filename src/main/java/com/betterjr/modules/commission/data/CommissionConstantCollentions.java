package com.betterjr.modules.commission.data;

public class CommissionConstantCollentions {

    public static final String COMMISSION_BUSIN_STATUS_NO_HANDLE = "0"; // 佣金业务状态未处理

    public static final String COMMISSION_BUSIN_STATUS_IS_HANDLE = "1"; // 佣金业务状态已处理

    public static final String COMMISSION_BUSIN_STATUS_AUDIT = "2"; // 佣金业务状态已审核

    public static final String COMMISSION_BUSIN_STATUS_DELETE = "3"; // 佣金业务状态已删除

    public static final String COMMISSION_BUSIN_STATUS_CANNUL = "4"; // 佣金业务状态已作废

    public static final String COMMISSION_PAY_STATUS_NO_HANDLE = "0"; // 佣金付款状态未处理

    public static final String COMMISSION_PAY_STATUS_SUCCESS = "2"; // 佣金付款状态付款成功

    public static final String COMMISSION_PAY_STATUS_FAILURE = "1"; // 佣金付款状态付款失败

    public static final String COMMISSION_RESOLVE_STATUS_SUCCESS = "1"; // 佣金解析状态已解析成功

    public static final String COMMISSION_RESOLVE_STATUS_FAILURE = "0"; // 佣金解析状态已解析失败

    public static final String COMMISSION_FILE_INFO_TYPE = "6"; // 佣金解析状态未处理

    public static final String COMMISSION_RECORD_BUSIN_STATUS_AUDIT = "1"; // 佣金记录审核状态

    public static final String COMMISSION_RECORD_BUSIN_STATUS_PAY = "2"; // 佣金记录付款状态

    public static final String COMMISSION_RECORD_BUSIN_STATUS_DELETE = "3"; // 佣金记录删除状态

    public static final Integer COMMISSION_FILE_BEGIN_ROW = 1; // 佣金文件解析的开始行

    public static final Long COMMISSION_FILE_DOWN_FILEITEM_FILEID = 1005043l;// 文件下载模版

    public static final Integer COMMISSION_FILE_RESOLVE_SLEEP_TIME = 60;// 文件解析睡眠时间

    public static final String COMMISSION_FILE_RESOLVE_SUFFIX_KEY = "COMMISSIONFILERESOLVE";// 佣金文件解析前缀

    public static final String COMMISSION_FILE_CONFIRM_STATUS_EFFECTIVE = "2"; // 确认合规(确认通过)

    public static final String COMMISSION_FILE_CONFIRM_STATUS_INEFFECTIVE = "1";// 确认不合规(确认未通过)

    public static final String COMMISSION_FILE_CONFIRM_STATUS_UNCONFIRMED = "0";// 还没有确认

    public static final String COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK = "1"; // 发票参数状态为可用 1

    public static final String COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER = "0"; // 发票参数状态为不可用 0

    public static final String COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_ENTERPRISE = "1"; // 发票抬头的类型 1 企业

    public static final String COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PERSION = "0"; // 发票抬头的类型 0个人

    public static final String COMMISSION_INVOICE_CUSTINFO_IS_LAEST_OK = "1"; // 是否默认 1是默认

    public static final String COMMISSION_INVOICE_CUSTINFO_IS_LAEST_FAILER = "0"; // 是否默认 0 不是默认

    public static final String COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_PLAIN = "0"; // 发票类型 0 普通发票

    public static final String COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_SPECIAL = "1"; // 发票类型 1 专用发票
}
