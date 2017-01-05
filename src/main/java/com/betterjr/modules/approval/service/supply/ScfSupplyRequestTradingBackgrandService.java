package com.betterjr.modules.approval.service.supply;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

@Service
public class ScfSupplyRequestTradingBackgrandService extends ScfBaseApprovalService{
    @Autowired
    private ScfElecAgreementService elecAgreementService;  
    @Autowired
    private ScfSupplierPushService supplierPushService;
    @Autowired
    private ScfRequestSchemeService schemeService;
    @Autowired
    private ScfAgreementService agreementService;
    
	public void processPass(Map<String, Object> anContext){
		String requestNo = anContext.get("requestNo").toString();
		BTAssert.notNull(requestNo, "申请编号不能为空！");
        ScfRequest request = requestService.findRequestByRequestNo(requestNo);
        
        //生成-应收账款转让确认意见确认书
        agreementService.transOpinion(requestService.getOption(request));
        
        //修改融资方案--核心企业确认状态（待确认）
        //ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        //scheme.setCoreCustAduit("0");
        //schemeService.saveModifyScheme(scheme);
        
        this.pushSingInof(request);
        this.updateAndSendRequestStatus(requestNo, RequestTradeStatus.CONFIRM_TRADING.getCode(), RequestLastStatus.APPROVE.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(requestNo));
	}

	public void processReject(Map<String, Object> anContext) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 如果是 微信端的申请 并为 票据融资 则需要发送消息
	 * @param request
	 * @param scheme
	 */
	private void pushSingInof(ScfRequest request) {
        if (BetterStringUtils.equals("2", request.getRequestFrom()) && BetterStringUtils.equals(RequestType.BILL.getCode(), request.getRequestType())) {
            List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(request.getRequestNo(), "1");
            if(false == Collections3.isEmpty(list)){
                supplierPushService.pushSignInfo(Collections3.getFirst(list));
            }
        }
	}

}
