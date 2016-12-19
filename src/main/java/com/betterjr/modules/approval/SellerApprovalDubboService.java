package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.seller.Application;
import com.betterjr.modules.approval.seller.ConfirmLoan;
import com.betterjr.modules.approval.seller.ConfirmScheme;
import com.betterjr.modules.approval.seller.ConfirmTradingBackgrand;
import com.betterjr.modules.approval.seller.EndFlow;
import com.betterjr.modules.approval.seller.OfferScheme;
import com.betterjr.modules.approval.seller.RequestTradingBackgrand;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSellerApprovalService.class)
public class SellerApprovalDubboService implements IScfSellerApprovalService {
	protected final Logger logger = LoggerFactory.getLogger(SellerApprovalDubboService.class);
	private static final int PROCESS_PASS = 1;
	
	@Override
	public Map<String, Object> application(Map<String, Object> anContext) {
		Application application = (Application) NodeFactory.getSellerNode(1);
		BTAssert.notNull(application);
		ScfRequest request = application.execute((ScfRequest)RuleServiceDubboFilterInvoker.getInputObj());
		anContext.put("requestNo", request.getRequestNo());
		return anContext;
	}

	@Override
	public Map<String, Object> offerScheme(Map<String, Object> anContext, int resultType) {
		OfferScheme offerScheme = (OfferScheme) NodeFactory.getSellerNode(2);
		BTAssert.notNull(offerScheme);
		if (PROCESS_PASS == resultType) {
			offerScheme.processPass((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			offerScheme.processReject(anContext);
		}
		return anContext;
	}

	@Override
	public void confirmScheme(Map<String, Object> anContext, int resultType) {
		ConfirmScheme confirmScheme = (ConfirmScheme) NodeFactory.getSellerNode(3);
		BTAssert.notNull(confirmScheme);
		if (PROCESS_PASS == resultType) {
			confirmScheme.processPass(anContext);
		} else {
			confirmScheme.processReject(anContext);
		}
	}

	@Override
	public void requestTradingBackgrand(Map<String, Object> anContext, int resultType) {
		RequestTradingBackgrand requestTradingBackgrand = (RequestTradingBackgrand) NodeFactory.getSellerNode(4);
		BTAssert.notNull(requestTradingBackgrand);
		if (PROCESS_PASS == resultType) {
			requestTradingBackgrand.processPass(anContext);
		} else {
			requestTradingBackgrand.processReject(anContext);
		}
	}

	@Override
	public void confirmTradingBackgrand(Map<String, Object> anContext, int resultType) {
		ConfirmTradingBackgrand confirmTradingBackgrand = (ConfirmTradingBackgrand) NodeFactory.getSellerNode(5);
		BTAssert.notNull(confirmTradingBackgrand);
		if (PROCESS_PASS == resultType) {
			confirmTradingBackgrand.processPass(anContext);
		} else {
			confirmTradingBackgrand.processReject(anContext);
		}
	}
	
	@Override
	public void confirmLoan(Map<String, Object> anContext, int resultType) {
		ScfLoan loan = (ScfLoan) RuleServiceDubboFilterInvoker.getInputObj();
		ConfirmLoan confirmLoan = (ConfirmLoan) NodeFactory.getSellerNode(6);
		BTAssert.notNull(confirmLoan);
		if (PROCESS_PASS == resultType) {
			confirmLoan.processPass(loan);
		} else {
			confirmLoan.processReject(anContext);
		}
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int resultType) {
		EndFlow endFlow = (EndFlow) NodeFactory.getSellerNode(7);
		if (PROCESS_PASS == resultType) {
			endFlow.processCancel(anContext);
		} else {
			endFlow.processEnd(anContext);
		}
	}

}
