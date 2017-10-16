package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

/**
 * 融资-签约审核
 * 
 * @author tangzw
 *
 */
@Service
public class ScfSignReviewService extends ScfBaseApprovalService {

    public void processPass(Map<String, Object> anContext) {
        ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
        // this.pushOrderInfo(request);
    }

    public void processReject(Map<String, Object> anContext) {
        ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
    }

}
