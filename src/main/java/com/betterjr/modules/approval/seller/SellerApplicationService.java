package com.betterjr.modules.approval.seller;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.BaseNodeService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class SellerApplicationService extends BaseNodeService{
	public ScfRequest execute(ScfRequest request){
		request.setCustType("2");
		request = requestService.addRequest(request);
        this.updateAndSendRequestStatus(request.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), RequestLastStatus.REQUEST.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
		return request;
	}

}
