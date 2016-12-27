package com.betterjr.modules.approval;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.approval.seller.SellerApplicationService;
import com.betterjr.modules.approval.seller.SellerConfirmLoanService;
import com.betterjr.modules.approval.seller.SellerConfirmSchemeService;
import com.betterjr.modules.approval.seller.SellerConfirmTradingBackgrandService;
import com.betterjr.modules.approval.seller.SellerEndFlowService;
import com.betterjr.modules.approval.seller.SellerOfferSchemeService;
import com.betterjr.modules.approval.seller.SellerRequestTradingBackgrandService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSellerApprovalService.class)
public class SellerApprovalDubboService implements IScfSellerApprovalService {
	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(SellerApprovalDubboService.class);
	@Autowired
	private SellerApplicationService applicationService;
	@Autowired
	private SellerOfferSchemeService sellerOfferSchemeService;
	@Autowired
	private SellerConfirmSchemeService sellerConfirmSchemeService;
	@Autowired
	private SellerConfirmTradingBackgrandService sellerConfirmTradingBackgrandService;
	@Autowired
	private SellerRequestTradingBackgrandService sellerRequestTradingBackgrandService;
	@Autowired
	private SellerConfirmLoanService sellerConfirmLoanServiceService;
	@Autowired
	private SellerEndFlowService sellerEndFlowService;
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
