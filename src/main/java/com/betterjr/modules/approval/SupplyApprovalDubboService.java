package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.approval.supply.Application;
import com.betterjr.modules.approval.supply.ConfirmLoan;
import com.betterjr.modules.approval.supply.ConfirmScheme;
import com.betterjr.modules.approval.supply.ConfirmTradingBackgrand;
import com.betterjr.modules.approval.supply.EndFlow;
import com.betterjr.modules.approval.supply.OfferScheme;
import com.betterjr.modules.approval.supply.RequestTradingBackgrand;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSupplyApprovalService.class)
public class SupplyApprovalDubboService implements IScfSupplyApprovalService {
	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(SupplyApprovalDubboService.class);
	
	@Override
	public Map<String, Object> application(Map<String, Object> anContext) {
		Application application = (Application) NodeFactory.getSupplyNode(1);
		BTAssert.notNull(application);
		ScfRequest request = application.execute((ScfRequest)RuleServiceDubboFilterInvoker.getInputObj());
		anContext.put("requestNo", request.getRequestNo());
		return anContext;
	}

	@Override
	public Map<String, Object> offerScheme(Map<String, Object> anContext, int resultType) {
		OfferScheme offerScheme = (OfferScheme) NodeFactory.getSupplyNode(2);
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
		ConfirmScheme confirmScheme = (ConfirmScheme) NodeFactory.getSupplyNode(3);
		BTAssert.notNull(confirmScheme);
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			confirmScheme.processPass(anContext);
		} else {
			confirmScheme.processReject(anContext);
		}
	}

	@Override
	public void requestTradingBackgrand(Map<String, Object> anContext, int resultType) {
		RequestTradingBackgrand requestTradingBackgrand = (RequestTradingBackgrand) NodeFactory.getSupplyNode(4);
		BTAssert.notNull(requestTradingBackgrand);
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			requestTradingBackgrand.processPass(anContext);
		} else {
			requestTradingBackgrand.processReject(anContext);
		}
	}

	@Override
	public void confirmTradingBackgrand(Map<String, Object> anContext, int resultType) {
		ConfirmTradingBackgrand confirmTradingBackgrand = (ConfirmTradingBackgrand) NodeFactory.getSupplyNode(5);
		BTAssert.notNull(confirmTradingBackgrand);
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			confirmTradingBackgrand.processPass(anContext);
		} else {
			confirmTradingBackgrand.processReject(anContext);
		}
	}

	@Override
	public void confirmLoan(Map<String, Object> anContext, int resultType) {
		ConfirmLoan confirmLoan = (ConfirmLoan) NodeFactory.getSupplyNode(6);
		BTAssert.notNull(confirmLoan);
		if (PROCESS_PASS == resultType) {
			confirmLoan.processPass((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			confirmLoan.processReject(anContext);
		}
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int resultType) {
		EndFlow endFlow = (EndFlow) NodeFactory.getSupplyNode(7);
		BTAssert.notNull(endFlow);
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			endFlow.processCancel(anContext);
		} else {
			endFlow.processEnd(anContext);
		}
	}

}
