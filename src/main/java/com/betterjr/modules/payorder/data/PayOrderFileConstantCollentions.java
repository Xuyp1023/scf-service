package com.betterjr.modules.payorder.data;

public class PayOrderFileConstantCollentions {
    
    
    //文件状态 1 未确认 （包括生成的需要付款申请文件）  2已审核  9已删除
    
    public static final String PAY_FILE_BUSIN_STATUS_NOCONFIRM = "1"; //1 未确认（包括生成的需要付款申请文件）
    
    public static final String PAY_FILE_BUSIN_STATUS_CONFIRM = "2"; //2已审核（包括生成的需要付款申请文件）
   
    public static final String PAY_FILE_BUSIN_STATUS_DELETEED = "9"; // 9已删除
    
    //文件作用类型 0生成付款文件 1上传的付款结果文件
    
    public static final String PAY_FILE_INFO_TYPE_DOWNREQUESTFILE = "0"; //  0 生成付款文件
    
    public static final String PAY_FILE_INFO_TYPE_UPLOADPAYRESULT = "1"; //  1 上传的付款结果文件

}
