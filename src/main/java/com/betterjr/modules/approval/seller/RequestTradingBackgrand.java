package com.betterjr.modules.approval.seller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.approval.BaseNode;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

public class RequestTradingBackgrand extends BaseNode{
	@Autowired
    private ScfRequestService requestService;
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
        ScfRequest request = requestService.findRequestByRequestNo(requestNo);
        
        //生成-应收账款转让确认意见确认书
        agreementService.transOpinion(requestService.getOption(request));
        
        //修改融资方案--核心企业确认状态（待确认）
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        scheme.setCoreCustAduit("0");
        schemeService.saveModifyScheme(scheme);
	}

	public void processReject(Map<String, Object> anContext) {
		// TODO Auto-generated method stub
		
	}



}
