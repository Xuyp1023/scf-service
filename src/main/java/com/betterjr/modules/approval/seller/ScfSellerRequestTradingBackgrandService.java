package com.betterjr.modules.approval.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.approval.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;

@Service
public class ScfSellerRequestTradingBackgrandService extends ScfBaseApprovalService{
    @Autowired
    private ScfRequestSchemeService schemeService;
    @Autowired
    private ScfAgreementService agreementService;
    
	public void processPass(Map<String, Object> anContext){
		String requestNo = anContext.get("requestNo").toString();
		String smsCode = anContext.get("smsCode").toString();
        
        //签署三方协议
         /*if(false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_PROTOCOL, smsCode)){
             return AjaxObject.newError("操作失败：短信验证码错误").toJson();
         }*/
         
        //修改融资方案--核心企业确认状态（待确认）
        //ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        //scheme.setCoreCustAduit("0");
        //schemeService.saveModifyScheme(scheme);
	}

	public void processReject(Map<String, Object> anContext) {
		// TODO Auto-generated method stub
		
	}

}
