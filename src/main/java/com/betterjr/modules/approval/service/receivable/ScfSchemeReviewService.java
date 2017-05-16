package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

/**
 * 融资-审核融资方案
 * 
 * @author tangzw
 *
 */
@Service
public class ScfSchemeReviewService extends ScfBaseApprovalService {
	
	public void processPass(Map<String, Object> context) {

	}

	public void processReject(Map<String, Object> context) {

	}

}
