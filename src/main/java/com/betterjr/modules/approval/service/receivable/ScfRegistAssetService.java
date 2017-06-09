package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

/**
 * 融资-登记资产
 * 
 * @author tangzw
 *
 */
@Service
public class ScfRegistAssetService extends ScfBaseApprovalService {
	
	public void processHandle(Map<String, Object> anContext) {
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		//this.updateRequestLastStatus(request, true);
		//this.pushOrderInfo(request);
	}

}
