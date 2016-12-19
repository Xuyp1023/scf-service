package com.betterjr.modules.approval.seller;

import com.betterjr.modules.approval.BaseNode;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

public class Application extends BaseNode{
    
	public ScfRequest execute(ScfRequest request){
		request = requestService.addRequest(request);
        this.updateAndSendRequestStatus(request.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
		return request;
	}

}
