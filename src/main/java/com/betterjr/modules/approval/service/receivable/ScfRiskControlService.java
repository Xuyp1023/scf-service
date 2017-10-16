package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

/**
 * 融资-风控审批
 * 
 * @author tangzw
 *
 */
@Service
public class ScfRiskControlService extends ScfBaseApprovalService {

    public void processPass(Map<String, Object> anContext) {
        ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
        // this.pushOrderInfo(request);
    }

    public void processReject(Map<String, Object> anContext) {
        ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
    }

}
