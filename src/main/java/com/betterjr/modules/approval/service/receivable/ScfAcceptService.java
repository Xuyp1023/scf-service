package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;

/**
 * 融资-受理:
 * 1.启动审批流程；
 * 2.冻结资产相关数据；
 * 
 * @author tangzw
 *
 */
@Service
public class ScfAcceptService extends ScfBaseApprovalService {

    public void processHandle(Map<String, Object> anContext) {
        ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
        this.updateRequestLastStatus(request.getRequestNo(), RequestLastStatus.APPROVE.getCode());
        // this.forzenSource(request);
        // this.pushOrderInfo(requestService.findRequestByRequestNo(request.getRequestNo()));
    }

}
