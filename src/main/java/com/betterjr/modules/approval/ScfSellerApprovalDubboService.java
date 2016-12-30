package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.approval.seller.ScfSellerApplicationService;
import com.betterjr.modules.approval.seller.ScfSellerConfirmLoanService;
import com.betterjr.modules.approval.seller.ScfSellerConfirmSchemeService;
import com.betterjr.modules.approval.seller.ScfSellerConfirmTradingBackgrandService;
import com.betterjr.modules.approval.seller.ScfSellerEndFlowService;
import com.betterjr.modules.approval.seller.ScfSellerOfferSchemeService;
import com.betterjr.modules.approval.seller.ScfSellerRequestTradingBackgrandService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSellerApprovalService.class)
public class ScfSellerApprovalDubboService implements IScfSellerApprovalService {
	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(ScfSellerApprovalDubboService.class);
	@Autowired
	private ScfSellerApplicationService applicationService;
	@Autowired
	private ScfSellerOfferSchemeService sellerOfferSchemeService;
	@Autowired
	private ScfSellerConfirmSchemeService sellerConfirmSchemeService;
	@Autowired
	private ScfSellerConfirmTradingBackgrandService sellerConfirmTradingBackgrandService;
	@Autowired
	private ScfSellerRequestTradingBackgrandService sellerRequestTradingBackgrandService;
	@Autowired
	private ScfSellerConfirmLoanService sellerConfirmLoanServiceService;
	@Autowired
	private ScfSellerEndFlowService sellerEndFlowService;
	@Override
	public Map<String, Object> application(Map<String, Object> anContext) {
		ScfRequest request = applicationService.execute((ScfRequest)RuleServiceDubboFilterInvoker.getInputObj());
		anContext.put("requestNo", request.getRequestNo());
		return anContext;
	}
	
	@Override
	public Map<String, Object> offerScheme(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerOfferSchemeService.processPass((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			sellerOfferSchemeService.processReject(anContext);
		}
		return anContext;
	}

	@Override
	public void confirmScheme(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerConfirmSchemeService.processPass(anContext);
		} else {
			sellerConfirmSchemeService.processReject(anContext);
		}
	}

	@Override
	public void requestTradingBackgrand(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerRequestTradingBackgrandService.processPass(anContext);
		} else {
			sellerRequestTradingBackgrandService.processReject(anContext);
		}
	}

	@Override
	public void confirmTradingBackgrand(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerConfirmTradingBackgrandService.processPass(anContext);
		} else {
			sellerConfirmTradingBackgrandService.processReject(anContext);
		}
	}
	
	@Override
	public void confirmLoan(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerConfirmLoanServiceService.processPass((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			sellerConfirmLoanServiceService.processReject(anContext);
		}
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int resultType) {
		if (PROCESS_PASS == resultType) {
			sellerEndFlowService.processCancel(anContext);
		} else {
			sellerEndFlowService.processEnd(anContext);
		}
	}

}
