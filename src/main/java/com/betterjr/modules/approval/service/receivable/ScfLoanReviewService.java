package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

/**
 * 融资-发起签约
 * @author tangzw
 *
 */
@Service
public class ScfLoanReviewService extends ScfBaseApprovalService{
	
	public void processPass(Map<String, Object> anContext) {
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		this.updateRequestLastStatus(request, true);
		this.pushOrderInfo(request);
	}

	public void processReject(Map<String, Object> anContext) {
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		this.updateRequestLastStatus(request, true);
	}

}
