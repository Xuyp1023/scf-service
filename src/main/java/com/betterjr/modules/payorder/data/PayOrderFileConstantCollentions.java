package com.betterjr.modules.payorder.data;

/**
 * 
 * @ClassName: PayOrderFileConstantCollentions 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:33:58 
 *
 */
public class PayOrderFileConstantCollentions {

    // 文件状态 1 未确认 （包括生成的需要付款申请文件） 2已审核 9已删除

    /**
     * 1 未确认（包括生成的需要付款申请文件）
     */
    public static final String PAY_FILE_BUSIN_STATUS_NOCONFIRM = "1";

    /**
     * 2已审核（包括生成的需要付款申请文件）
     */
    public static final String PAY_FILE_BUSIN_STATUS_CONFIRM = "2";

    /**
     * 9已删除
     */
    public static final String PAY_FILE_BUSIN_STATUS_DELETEED = "9";

   
    /**
     * 文件作用类型 0生成付款文件 1上传的付款结果文件
     */
    public static final String PAY_FILE_INFO_TYPE_DOWNREQUESTFILE = "0";

    /**
     * 文件作用类型 0生成付款文件 1上传的付款结果文件
     */
    public static final String PAY_FILE_INFO_TYPE_UPLOADPAYRESULT = "1";

     
    /**
     * 文件使用状态 0 : 可以上传解析 1 已经上传解析
     */
    public static final String PAY_FILE_LOCKED_STATUS_CANUPLOAD = "0";

    /**
     * 文件使用状态 0 : 可以上传解析 1 已经上传解析
     */
    public static final String PAY_FILE_LOCKED_STATUS_UPLOADED = "1";
    
    /**
     *  新增导出文件是否持久化  0 不需要持久化   1 需要持久化
     *  
     */
    public static final String PAY_FILE_REPOSITORY_FLAG_OK = "1";
    
    /**
     * 不需要持久化
     */
    public static final String PAY_FILE_REPOSITORY_FLAG_NO = "0";
    

}
