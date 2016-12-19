package com.betterjr.modules.approval.supply;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.approval.BaseNode;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.push.service.ScfSupplierPushService;

public class OfferScheme extends BaseNode{
    @Autowired
    private ScfSupplierPushService supplierPushService;
    @Autowired
    private ScfElecAgreementService elecAgreementService;
	 
	public void processPass(ScfRequestScheme scheme){
        //保存融资方案
        requestService.saveOfferScheme(scheme);
        this.pushSingInfo(scheme);
        this.updateAndSendRequestStatus(scheme.getRequestNo(), RequestTradeStatus.CONFIRM_SCHEME.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(scheme.getRequestNo()));
	}

	public void processReject(Map<String, Object> anContext) {
		//TODO 
	}
	
	/**
	 * 发送签约提醒
	 * @param scheme
	 */
	private void pushSingInfo(ScfRequestScheme scheme) {
		ScfRequest request = requestService.selectByPrimaryKey(scheme.getRequestNo());
        if (BetterStringUtils.equals("2", request.getRequestFrom())) {
            List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(scheme.getRequestNo(), "0");
            if(false == Collections3.isEmpty(list)){
                supplierPushService.pushSignInfo(Collections3.getFirst(list));
            }
        }
	}
	
}
