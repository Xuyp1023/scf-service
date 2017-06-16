package com.betterjr.modules.approval.service.seller;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ScfSellerConfirmLoanService extends ScfBaseApprovalService{
	
	public void processPass(ScfLoan loan){
		BTAssert.notNull(loan, "放款信息不能为空！");
        //requestService.saveConfirmLoan(loan);
        this.updateRequestStatus(loan.getRequestNo(), RequestTradeStatus.FINISH_LOAN.getCode(), RequestLastStatus.APPROVE.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(loan.getRequestNo()));
	}
	
	public void processReject(Map<String, Object> anContext) {
		//TODO 
	}
	

}
