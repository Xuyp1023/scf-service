package com.betterjr.modules.approval.service.supply;

import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ScfSupplyApplicationService extends ScfBaseApprovalService{
	
	
	public ScfRequest savApplication(ScfRequest anRequest){
		anRequest.setCustType(REQUEST_CUST_TYPE_SUPPLY);
		anRequest = requestService.addRequest(anRequest);//新增融资t_scf_request并且 填充各个名称信息
		//设置当前融资的状态
        this.updateAndSendRequestStatus(anRequest.getRequestNo(), RequestTradeStatus.OFFER_SCHEME.getCode(), RequestLastStatus.REQUEST.getCode());
        //关联数据来源(订单，应收账单等信息，并且设置订单状态，公司授信等信息)
        this.forzenSource(anRequest);
        this.pushOrderInfo(requestService.findRequestByRequestNo(anRequest.getRequestNo()));
		return anRequest;
	}

}
