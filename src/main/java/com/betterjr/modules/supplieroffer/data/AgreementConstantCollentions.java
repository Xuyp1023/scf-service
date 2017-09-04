package com.betterjr.modules.supplieroffer.data;

public class AgreementConstantCollentions {

    
    //模版的状态 0 已删除 1 刚上传模版  2 平台已经制作ftl 3已经激活
    public static final String AGREMENT_TEMPLATE_BUSIN_STATUS_DELETE="0"; //0 已删除 
    
    public static final String AGREMENT_TEMPLATE_BUSIN_STATUS_NOEFFECTIVE="1"; //1 刚上传模版 
    
    public static final String AGREMENT_TEMPLATE_BUSIN_STATUS_EFFECTIVE="2"; //2 平台已经制作ftl
    
    public static final String AGREMENT_TEMPLATE_BUSIN_STATUS_ACTIVATE="3"; //3已经激活
    
    
    //状态 0 未签署  1供应商已签 2采购商已签 3 作废
    public static final String AGREMENT_BUSIN_STATUS_NOEFFECTIVE="0"; //0 未签署
    
    public static final String AGREMENT_BUSIN_STATUS_SUPPLIER_SIGNED="1"; //1供应商已签
    
    public static final String AGREMENT_BUSIN_STATUS_CORE_SIGNED="2"; //2采购商已签
    
    public static final String AGREMENT_BUSIN_STATUS_ANNUL="3"; //3 作废
    
    public static final String AGREMENT_BUSIN_STATUS_FACTORY_SIGNED="4"; //4保理公司已签

}
