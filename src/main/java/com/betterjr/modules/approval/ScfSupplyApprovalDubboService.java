package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.approval.supply.ScfSupplyApplicationService;
import com.betterjr.modules.approval.supply.ScfSupplyConfirmLoanService;
import com.betterjr.modules.approval.supply.ScfSupplyConfirmSchemeService;
import com.betterjr.modules.approval.supply.ScfSupplyConfirmTradingBackgrandService;
import com.betterjr.modules.approval.supply.ScfSupplyEndFlowService;
import com.betterjr.modules.approval.supply.ScfSupplyOfferSchemeService;
import com.betterjr.modules.approval.supply.ScfSupplyRequestTradingBackgrandService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSupplyApprovalService.class)
public class ScfSupplyApprovalDubboService implements IScfSupplyApprovalService {
	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(ScfSupplyApprovalDubboService.class);
	
	@Autowired
	private ScfSupplyApplicationService applicationService;
	@Autowired
	private ScfSupplyOfferSchemeService offerSchemeService;
	@Autowired
	private ScfSupplyConfirmSchemeService confirmSchemeService;
	@Autowired
	private ScfSupplyConfirmTradingBackgrandService confirmTradingBackgrandService;
	@Autowired
	private ScfSupplyRequestTradingBackgrandService requestTradingBackgrandService;
	@Autowired
	private ScfSupplyConfirmLoanService confirmLoanService;
	@Autowired
	private ScfSupplyEndFlowService endFlowService;
	@Autowired
	private ScfRequestService requestService;
	
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
	
	@Override
	public void releaseSource(String anRequestNo) {
		ScfRequest request = requestService.findRequestByRequestNo(anRequestNo);
		applicationService.releaseSource(request);
	}

}
