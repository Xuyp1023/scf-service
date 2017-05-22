package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;

@Service
public class ScfSchemeConfirmService extends ScfBaseApprovalService {

	//@Autowired
	//private ScfAgreementService agreementService;

	/**
	 * 通过-签署应收账款转让通知书
	 * @param anContext
	 */
	public void processPass(Map<String, Object> anContext) {
		String requestNo = anContext.get("requestNo").toString();
		//String smsCode = anContext.get("smsCode").toString();
		BTAssert.notNull(requestNo, "申请编号不能为空！");
		//BTAssert.notNull(smsCode, "短信验证码不能为空！");

		//if (false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_NOTICE, smsCode)) {
			//throw new RuntimeException("操作失败：短信验证码错误");
		//}
		
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		this.updateRequestLastStatus(request, true);
		this.pushOrderInfo(request);
	}

	public void processReject(Map<String, Object> anContext) {
		ScfRequest request = this.getReqtuest(anContext.get("requestNo").toString());
		this.updateRequestLastStatus(request, true);
	}

}
