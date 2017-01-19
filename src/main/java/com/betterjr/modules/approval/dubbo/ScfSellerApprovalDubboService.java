package com.betterjr.modules.approval.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.approval.IScfSellerApprovalService;
import com.betterjr.modules.approval.service.seller.ScfSellerApplicationService;
import com.betterjr.modules.approval.service.seller.ScfSellerConfirmLoanService;
import com.betterjr.modules.approval.service.seller.ScfSellerConfirmSchemeService;
import com.betterjr.modules.approval.service.seller.ScfSellerConfirmTradingBackgrandService;
import com.betterjr.modules.approval.service.seller.ScfSellerEndFlowService;
import com.betterjr.modules.approval.service.seller.ScfSellerOfferSchemeService;
import com.betterjr.modules.approval.service.seller.ScfSellerRequestTradingBackgrandService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.service.ScfRequestService;
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
	@Autowired
	private ScfRequestService requestService;
	
	@Reference(interfaceClass = ICustFileService.class)
	private ICustFileService custFileService;
	 
	@Override
	public Map<String, Object> application(Map<String, Object> anContext) {
		ScfRequest request = (ScfRequest)RuleServiceDubboFilterInvoker.getInputObj();
		
		//保存上存的文件
		String fileList = (String)anContext.get("fileList");
		if(BetterStringUtils.isNotBlank(fileList)){
			Long batchNo = custFileService.updateCustFileItemInfo(fileList, null);
			request.setBatchNo(batchNo.intValue());
		}
		
		request= applicationService.saveApplication(request);
		anContext.put("requestNo", request.getRequestNo());
		anContext.put("balance", request.getBalance());
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

	@Override
	public void releaseSource(String anRequestNo) {
		ScfRequest request = requestService.findRequestByRequestNo(anRequestNo);
		applicationService.releaseSource(request);
		requestService.delete(request);
	}
}
