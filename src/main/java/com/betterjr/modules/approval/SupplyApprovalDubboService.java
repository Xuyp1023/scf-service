package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.approval.supply.ApplicationService;
import com.betterjr.modules.approval.supply.ConfirmLoanService;
import com.betterjr.modules.approval.supply.ConfirmSchemeService;
import com.betterjr.modules.approval.supply.ConfirmTradingBackgrandService;
import com.betterjr.modules.approval.supply.EndFlowService;
import com.betterjr.modules.approval.supply.OfferSchemeService;
import com.betterjr.modules.approval.supply.RequestTradingBackgrandService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSupplyApprovalService.class)
public class SupplyApprovalDubboService implements IScfSupplyApprovalService {
	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(SupplyApprovalDubboService.class);
	
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private OfferSchemeService offerSchemeService;
	@Autowired
	private ConfirmSchemeService confirmSchemeService;
	@Autowired
	private ConfirmTradingBackgrandService confirmTradingBackgrandService;
	@Autowired
	private RequestTradingBackgrandService requestTradingBackgrandService;
	@Autowired
	private ConfirmLoanService confirmLoanService;
	@Autowired
	private EndFlowService endFlowService;
	
	@Override
	public Map<String, Object> application(Map<String, Object> anContext) {
		ScfRequest request = applicationService.application((ScfRequest)RuleServiceDubboFilterInvoker.getInputObj());
		anContext.put("requestNo", request.getRequestNo());
		return anContext;
	}

	@Override
	public Map<String, Object> offerScheme(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			offerSchemeService.processPass((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			offerSchemeService.processReject(anContext);
		}
		return anContext;
	}

	@Override
	public void confirmScheme(Map<String, Object> anContext, int resultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			confirmSchemeService.processPass(anContext);
		} else {
			confirmSchemeService.processReject(anContext);
		}
	}

	@Override
	public void requestTradingBackgrand(Map<String, Object> anContext, int resultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			requestTradingBackgrandService.processPass(anContext);
		} else {
			requestTradingBackgrandService.processReject(anContext);
		}
	}

	@Override
	public void confirmTradingBackgrand(Map<String, Object> anContext, int resultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			confirmTradingBackgrandService.processPass(anContext);
		} else {
			confirmTradingBackgrandService.processReject(anContext);
		}
	}

	@Override
	public void confirmLoan(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			confirmLoanService.processPass((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			confirmLoanService.processReject(anContext);
		}
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int resultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == resultType) {
			endFlowService.processCancel(anContext);
		} else {
			endFlowService.processEnd(anContext);
		}
	}

}
