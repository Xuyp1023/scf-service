package com.betterjr.modules.approval.supply;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.BaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ApplicationService extends BaseApprovalService{
	public ScfRequest application(ScfRequest request){
		request.setCustType(REQUEST_CUST_TYPE_SUPPLY);
		request = requestService.addRequest(request);
        this.updateAndSendRequestStatus(request.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), RequestLastStatus.REQUEST.getCode());
        this.forzenSource(request);
        this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
		return request;
	}

}
