package com.betterjr.modules.approval.seller;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.BaseNodeService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class SellerApplicationService extends BaseNodeService{
	public ScfRequest execute(ScfRequest request){
		request = requestService.addRequest(request);
        this.updateAndSendRequestStatus(request.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), "1");
        this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
		return request;
	}

}
