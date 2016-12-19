package com.betterjr.modules.approval;

import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

public class BaseNode {

	/**
	 * 应收账款转让通知书  应收账款转让确认书 三方协议
	 */
	public static final String AGREEMENT_TYPE_NOTICE = "0";
	public static final String AGREEMENT_TYPE_CONFIRMATION = "1";
	public static final String AGREEMENT_TYPE_PROTOCOL = "2";
	@Autowired
	public ScfRequestService requestService;
	@Autowired
	private ScfOrderService orderService;
	@Autowired
	private ScfOfferService offerService;
	@Autowired
	private ScfCreditDetailService creditDetailService;
	@Autowired
    private ScfSupplierPushService supplierPushService;
	
	/**
	 * 修改项目状态
	 * 
	 * @param anContext
	 * @return
	 */
	public void updateAndSendRequestStatus(String anRequestNo, String anStatus) {
		requestService.updateAndSendRequestStatus(anRequestNo, anStatus);
	}

	/**
	 * 释放资源
	 * @param anRequest
	 */
	public void releaseSource(ScfRequest anRequest) {
		// 当融资流程终止时 ，解除订单关联
        orderService.unForzenInfoes(anRequest.getRequestNo(), null);
        
		//当融资流程终止时 ，将使用的报价改为可用状态
        if(null != anRequest .getOfferId()){
            offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "1");
        }
        
        //修申请单状态为关闭
        anRequest.setTradeStatus(RequestTradeStatus.CLOSED.getCode());
        
        //当融资流程终止时,对冻结的授信额度进行解冻 
        ScfCreditInfo anCreditInfo = new ScfCreditInfo();
        anCreditInfo.setBusinFlag(anRequest.getRequestType());
        anCreditInfo.setBalance(anRequest.getBalance());
        anCreditInfo.setBusinId(Long.valueOf(anRequest.getRequestNo()));
        anCreditInfo.setCoreCustNo(anRequest.getCoreCustNo());
        anCreditInfo.setCustNo(anRequest.getCustNo());
        anCreditInfo.setFactorNo(anRequest.getFactorNo());
        anCreditInfo.setCreditMode(anRequest.getCreditMode());
        anCreditInfo.setRequestNo(anRequest.getRequestNo());
        anCreditInfo.setDescription(anRequest.getDescription());
        creditDetailService.saveUnfreezeCredit(anCreditInfo);
	}
	
	/**
	 *  融资申请进度推送-发送微信提醒
	 * @param anRequest
	 */
	public void pushOrderInfo(ScfRequest anRequest){
		 // 如果是微信发起的流程---发送微信提醒
        if(BetterStringUtils.equals("2", anRequest.getRequestFrom())){
            supplierPushService.pushOrderInfo(anRequest);
        }
	}
	
	
}
