package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ScfManagerFlowService extends ScfBaseApprovalService{
	
	/**
	 * 流程中止-非正常
	 * @param anContext
	 */
	public void processCancel(Map<String, Object> anContext) {
		String requestNo  = anContext.get("requestNo").toString();
		BTAssert.notNull(requestNo);
		ScfRequest request = this.getReqtuest(requestNo);
		//this.releaseSource(request);
		//this.updateAndSendRequestStatus(requestNo, RequestTradeStatus.CLOSED.getCode(), RequestLastStatus.CLOSED.getCode());
		//this.pushOrderInfo(requestService.findRequestByRequestNo(requestNo));
	}

	/**
	 * 流程结束-正常
	 * @param anContext
	 */
	public void processEnd(Map<String, Object> anContext) {
		String requestNo  = anContext.get("requestNo").toString();
		BTAssert.notNull(requestNo);
		//this.updateAndSendRequestStatus(requestNo, RequestTradeStatus.FINISH_LOAN.getCode(), RequestLastStatus.LOAN.getCode());
		//this.pushOrderInfo(request);
	}
}
