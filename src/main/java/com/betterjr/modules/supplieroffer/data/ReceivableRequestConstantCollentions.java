package com.betterjr.modules.supplieroffer.data;

public class ReceivableRequestConstantCollentions {

    
    //状态 0 未生效 1供应商提交申请 2供应商签署合同 3 供应商转让合同给核心企业签署 4核心企业确认并签署合同 5资金方付款 6完结 7废止
    public static final String OFFER_BUSIN_STATUS_NOEFFECTIVE="0"; //0： 不可用
    
    public static final String OFFER_BUSIN_STATUS_SUBMIT_REQUEST="1"; //1 供应商提交申请
    
    public static final String OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT="2"; //2供应商签署合同
    
    public static final String OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE="3"; //3 供应商转让合同给核心企业签署
    
    public static final String OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT="4"; //4 核心企业确认并签署合同
    
    public static final String OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM="5"; //5资金方付款
    
    public static final String OFFER_BUSIN_STATUS_REQUEST_END="6"; //6完结
    
    public static final String OFFER_BUSIN_STATUS_REQUEST_ANNUL="7"; //7废止
    
    public static final Long PLATFORM_CUST_NO=123456L; //6完结
}
