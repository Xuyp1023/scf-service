package com.betterjr.modules.approval.supply;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.approval.BaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;

@Service
public class ConfirmSchemeService extends BaseApprovalService {

	@Autowired
	private ScfRequestSchemeService schemeService;
	@Autowired
	private ScfAgreementService agreementService;

	/**
	 * 通过-签署应收账款转让通知书
	 * @param anContext
	 */
	public void processPass(Map<String, Object> anContext) {
		String requestNo = anContext.get("requestNo").toString();
		String smsCode = anContext.get("smsCode").toString();
		BTAssert.notNull(requestNo, "申请编号不能为空！");
		BTAssert.notNull(smsCode, "短信验证码不能为空！");

		ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
		BTAssert.notNull(scheme, "找不到对应的融资方案！");
		//if (false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_NOTICE, smsCode)) {
		//	throw new RuntimeException("操作失败：短信验证码错误");
		//}

		// 修改融资方案确认状态
		scheme.setCustAduit("1");
		schemeService.saveModifyScheme(scheme);
		
		this.updateAndSendRequestStatus(requestNo, RequestTradeStatus.REQUEST_TRADING.getCode(), RequestLastStatus.APPROVE.getCode());
		this.pushOrderInfo(requestService.findRequestByRequestNo(requestNo));
	}

	public void processReject(Map<String, Object> anContext) {
		String requestNo = anContext.get("requestNo").toString();
		BTAssert.notNull(requestNo, "申请编号不能为空！");
		ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
		BTAssert.notNull(scheme, "找不到对应的融资方案！");

		// 修改融资方案确认状态
		scheme.setCustAduit("2");
		schemeService.saveModifyScheme(scheme);
	}

}
