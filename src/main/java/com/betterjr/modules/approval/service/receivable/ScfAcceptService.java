package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

/**
 * 融资-受理
 * 
 * @author tangzw
 *
 */
@Service
public class ScfAcceptService extends ScfBaseApprovalService {
	public ScfRequest saveApplication(ScfRequest anRequest) {
		anRequest.setCustType(REQUEST_CUST_TYPE_SUPPLY);
		anRequest = requestService.addRequest(anRequest);
		this.updateAndSendRequestStatus(anRequest.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(),
				RequestLastStatus.REQUEST.getCode());
		this.forzenSource(anRequest);
		this.pushOrderInfo(requestService.findRequestByRequestNo(anRequest.getRequestNo()));
		return anRequest;
	}

	public void processHandle(Map<String, Object> anContext) {

	}

}
