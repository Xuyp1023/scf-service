package com.betterjr.modules.approval.supply;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.BaseNodeService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ApplicationService extends BaseNodeService{
	public ScfRequest application(ScfRequest request){
		request = requestService.addRequest(request);
        this.updateAndSendRequestStatus(request.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), "1");
        this.forzenSource(request);
        this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
		return request;
	}

}
