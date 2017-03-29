package com.betterjr.modules.approval.service.seller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;

@Service
public class ScfSellerOfferSchemeService extends ScfBaseApprovalService{
    @Autowired
    private ScfElecAgreementService elecAgreementService;
	 
    public void processPass(ScfRequestScheme scheme){
        //保存融资方案
        requestService.saveOfferScheme(scheme);
        this.updateAndSendRequestStatus(scheme.getRequestNo(), RequestTradeStatus.CONFIRM_SCHEME.getCode(), RequestLastStatus.APPROVE.getCode());
        this.pushSingInfo(scheme);
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
        //if (BetterStringUtils.equals("2", request.getRequestFrom())) {
            List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(scheme.getRequestNo(), "0");
            if(false == Collections3.isEmpty(list)){
                supplierPushService.pushSignInfo(Collections3.getFirst(list));
            }
       // }
	}
	
}
