package com.betterjr.modules.approval.service.receivable;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ScfApplicationService extends ScfBaseApprovalService{
	
	
	public ScfRequest savApplication(ScfRequest anRequest){
		anRequest.setCustType(REQUEST_CUST_TYPE_SUPPLY);
		anRequest = requestService.addRequest(anRequest);
        this.updateAndSendRequestStatus(anRequest.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), RequestLastStatus.REQUEST.getCode());
        this.forzenSource(anRequest);
        this.pushOrderInfo(requestService.findRequestByRequestNo(anRequest.getRequestNo()));
		return anRequest;
	}
	

}
