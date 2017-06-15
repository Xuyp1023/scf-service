package com.betterjr.modules.asset.data;

public class AssetConstantCollentions {
    
    //资产状态 0:未生效  1生效  2废止 3 转让
    public static final String ASSET_INFO_BUSIN_STATUS_NOEFFECTIVE="0"; //资产状态 0:未生效
    
    public static final String ASSET_INFO_BUSIN_STATUS_EFFECTIVE="1";  //资产状态 1生效
    
    public static final String ASSET_INFO_BUSIN_STATUS_ANNUL="2";   //资产状态  2废止 
    
    public static final String ASSET_INFO_BUSIN_STATUS_ASSIGNMENT="3"; //资产状态 3 转让
    
    public static final String ASSET_INFO_CAN_USE="10"; //资产状态可用
    
    public static final String ASSET_INFO_CAN_NO_USE="20"; //资产状态不可用
    
    public static final String ASSET_USE_TYPE_ENQUIRY="1";  //资产用于询价
    
    public static final String ASSET_USE_TYPE_REQUEST="2";  //资产用于融资

    public static final String CUST_INFO_KEY="custInfo";   //供应商/经销商在资产表的map中的key
    
    public static final String CUST_INFO_STATUS="custInfoStatus";   //供应商/经销商在资产表的权限中的key
    
    public static final String CORE_CUST_INFO_KEY="coreCustInfo";   //核心企业在资产表的map中的key
    
    public static final String CORE_CUST_INFO_STATUS="coreCustInfoStatus";   //核心企业在资产表的权限中的key
    
    public static final String FACTORY_CUST_INFO_KEY="factoryCustInfo";   //保理公司在资产表的map中的key
    
    public static final String FACTORY_CUST_INFO_STATUS="factoryCustInfoStatus";   //保理公司在资产表的权限中的key
   
    public static final String SCF_ORDER_LIST_KEY="orderList";   //订单在资产表的map中的key
    
    public static final String SCF_INVOICE_LIST_KEY="invoiceList";   //发票在资产表的map中的key
    
    public static final String CUST_AGREEMENT_LIST_KEY="agreementList";   //合同在资产表的map中的key
    
    public static final String SCF_RECEICEABLE_LIST_KEY="receivableList";   //应收账款在资产表的map中的key
    
    public static final String SCF_TRANSPORT_LIST_KEY="transportList";   //运输单据在资产表的map中的key
    
    public static final String SCF_BILL_LIST_KEY="acceptBillList";   //票据在资产表的map中的key
    
    public static final String SCF_ASSET_ROLE_SUPPLY="1";   //供应商角色
    
    public static final String  SCF_ASSET_ROLE_CORE="4";    //核心企业角色
    
    public static final String SCF_ASSET_ROLE_FACTORY="3";  //保理公司角色
    
    public static final String SCF_ASSET_ROLE_DEALER="2";   //经销商角色
    
    public static final String ASSET_BASEDATA_INFO_TYPE_ORDER="1";  //基础数据类型是订单类型
    
    public static final String ASSET_BASEDATA_INFO_TYPE_BILL="2";  //基础数据类型是票据类型
    
    public static final String ASSET_BASEDATA_INFO_TYPE_RECEIVABLE="3";  //基础数据类型是应收账款类型
   
    public static final String ASSET_BASEDATA_INFO_TYPE_INVOICE="4";  //基础数据类型是发票类型
    
    public static final String ASSET_BASEDATA_INFO_TYPE_AGREEMENT="5";  //基础数据类型是合同类型
    
    public static final String ASSET_BASEDATA_INFO_TYPE_TRANSPORT="6";  //基础数据类型是运输单据类型

    public static final Integer ASSET_OPERATOR_AUTH_MAX = 4;    //资产操作的最大权限

    public static final String ASSET_BUSIN_STATUS_OK = "10";    //资产企业，详情可用
    
    public static final String ASSET_BUSIN_STATUS_NO = "20";    //资产企业详情不可用
    
}
