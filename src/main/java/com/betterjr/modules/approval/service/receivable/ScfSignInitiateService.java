package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

/**
 * 融资-发起签约
 * 
 * @author tangzw
 *
 */
@Service
public class ScfSignInitiateService extends ScfBaseApprovalService {
	public void processHandle(Map<String, Object> anContext) {
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		//this.pushOrderInfo(request);
	}

}
