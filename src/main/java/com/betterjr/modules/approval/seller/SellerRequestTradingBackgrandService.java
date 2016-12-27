package com.betterjr.modules.approval.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.approval.BaseNodeService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;

@Service
public class SellerRequestTradingBackgrandService extends BaseNodeService{
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
