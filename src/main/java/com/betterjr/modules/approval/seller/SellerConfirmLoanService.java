package com.betterjr.modules.approval.seller;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.BaseNodeService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class SellerConfirmLoanService extends BaseNodeService{
	
	public void processPass(ScfLoan loan){
		BTAssert.notNull(loan, "放款信息不能为空！");
        requestService.saveConfirmLoan(loan);
        this.pushOrderInfo(requestService.findRequestByRequestNo(loan.getRequestNo()));
        this.updateAndSendRequestStatus(loan.getRequestNo(), RequestTradeStatus.FINISH_LOAN.getCode());
	}
	
	public void processReject(Map<String, Object> anContext) {
		//TODO 
	}
	

}
