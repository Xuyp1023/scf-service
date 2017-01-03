package com.betterjr.modules.approval.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.approval.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;

@Service
public class ScfSellerConfirmSchemeService extends ScfBaseApprovalService {

	@Autowired
	private ScfRequestSchemeService schemeService;
	@Autowired
	private ScfAgreementService agreementService;

	public void processPass(Map<String, Object> anContext){
		String requestNo = anContext.get("requestNo").toString();
		String smsCode = anContext.get("smsCode").toString();
		
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        BTAssert.notNull(scheme);
       
        //if(false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_PROTOCOL, smsCode)){
        	//throw new RuntimeException("操作失败：短信验证码错误");
        //}
	        
		//修改融资方案确认状态
        //scheme.setCustAduit("1");
        //schemeService.saveModifyScheme(scheme);
	}
	
	public void processReject(Map<String, Object> anContext) {
		String requestNo = anContext.get("requestNo").toString();

		//取消签约--并不存在取消的状态。如果打回则是现在不签，而不是拒签。
		//agreementService.cancelElecAgreement(requestNo, AGREEMENT_TYPE_PROTOCOL, "");
		
		ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
		BTAssert.notNull(scheme);
		
		//修改融资方案确认状态
		//scheme.setCustAduit("2");
		//schemeService.saveModifyScheme(scheme);
	}
	

}
