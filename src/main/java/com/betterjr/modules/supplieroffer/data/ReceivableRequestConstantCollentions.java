package com.betterjr.modules.supplieroffer.data;

public class ReceivableRequestConstantCollentions {

    
    //模式整合   0 未生效 1供应商提交申请 2资金方签署合同    3 资金方确认付款  6完结  7废止     10.10
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_NOEFFECTIVE = "0"; // 0 未生效
    
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT = "1"; // 1供应商提交申请
    
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN = "2"; // 2资金方签署合同
    
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_CONFIRM_PAY = "3"; // 3 资金方确认付款
    
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED = "6"; // 6完结
    
    public static final String RECEIVABLE_REQUEST_BUSIN_STATUS_ANNUL = "7"; //  7废止
    
    
    // 模式一状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 4核心企业确认并签署合同 5资金方付款 6完结 7废止
    public static final String OFFER_BUSIN_STATUS_NOEFFECTIVE = "0"; // 0： 不可用

    public static final String OFFER_BUSIN_STATUS_SUBMIT_REQUEST = "1"; // 1 供应商提交申请

    public static final String OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT = "2"; // 2供应商签署合同

    public static final String OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE = "3"; // 3 供应商转让合同给核心企业签署

    public static final String OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT = "4"; // 4 核心企业确认并签署合同

    public static final String OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM = "5"; // 5资金方付款

    // public static final String OFFER_BUSIN_STATUS_REQUEST_END="6"; //6完结

    public static final String OFFER_BUSIN_STATUS_REQUEST_ANNUL = "7"; // 7废止

   
    
    // 模式二 状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 5核心企业确认  6：结算中心签署合同    8结算中心资金方付款 9完结 7废止

    public static final String OFFER_BUSIN_STATUS_TWO_CORE_CONFIRM = "5"; // 核心企业确认申请

    public static final String OFFER_BUSIN_STATUS_TWO_FACTORY_SIGN_AGREEMENT = "6"; // 结算中心签署合同

    public static final String OFFER_BUSIN_STATUS_TWO_FACTORY_PAY_CONFIRM = "8"; // 结算中心确认付款

    public static final String OFFER_BUSIN_STATUS_TWO_REQUEST_END = "9"; // 模式2完成申请
    
    public static final String SIGN_AGREEMENT_FLAG_NO = "0"; // 0： 没有签署合同

    public static final String SIGN_AGREEMENT_FLAG_YES = "1"; // 1:已经签署合同

}
