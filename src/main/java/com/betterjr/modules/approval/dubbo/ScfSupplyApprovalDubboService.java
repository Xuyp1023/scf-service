package com.betterjr.modules.approval.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.approval.IScfSupplyApprovalService;
import com.betterjr.modules.approval.service.supply.ScfSupplyApplicationService;
import com.betterjr.modules.approval.service.supply.ScfSupplyConfirmLoanService;
import com.betterjr.modules.approval.service.supply.ScfSupplyConfirmSchemeService;
import com.betterjr.modules.approval.service.supply.ScfSupplyConfirmTradingBackgrandService;
import com.betterjr.modules.approval.service.supply.ScfSupplyEndFlowService;
import com.betterjr.modules.approval.service.supply.ScfSupplyOfferSchemeService;
import com.betterjr.modules.approval.service.supply.ScfSupplyRequestTradingBackgrandService;
import com.betterjr.modules.document.ICustFileService;
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
		
		request= applicationService.savApplication(request);
		anContext.put("requestNo", request.getRequestNo());
		anContext.put("balance", request.getBalance());
		return anContext;
	}

	@Override
	public Map<String, Object> offerScheme(Map<String, Object> anContext, int anResultType) {
		if (PROCESS_PASS == anResultType) {
			offerSchemeService.processPass((ScfRequestScheme) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			offerSchemeService.processReject(anContext);
		}
		return anContext;
	}

	@Override
	public void confirmScheme(Map<String, Object> anContext, int anResultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == anResultType) {
			confirmSchemeService.processPass(anContext);
		} else {
			confirmSchemeService.processReject(anContext);
		}
	}

	@Override
	public void requestTradingBackgrand(Map<String, Object> anContext, int anResultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == anResultType) {
			requestTradingBackgrandService.processPass(anContext);
		} else {
			requestTradingBackgrandService.processReject(anContext);
		}
	}

	@Override
	public void confirmTradingBackgrand(Map<String, Object> anContext, int anResultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == anResultType) {
			confirmTradingBackgrandService.processPass(anContext);
		} else {
			confirmTradingBackgrandService.processReject(anContext);
		}
	}

	@Override
	public void confirmLoan(Map<String, Object> anContext, int anResultType) {
		if (PROCESS_PASS == anResultType) {
			confirmLoanService.processPass((ScfLoan) RuleServiceDubboFilterInvoker.getInputObj());
		} else {
			confirmLoanService.processReject(anContext);
		}
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int anResultType) {
		anContext = RuleServiceDubboFilterInvoker.getInputObj();
		if (PROCESS_PASS == anResultType) {
			endFlowService.processCancel(anContext);
		} else {
			endFlowService.processEnd(anContext);
		}
	}
	
	@Override
	public void releaseSource(String anRequestNo) {
		ScfRequest request = requestService.findRequestByRequestNo(anRequestNo);
		applicationService.releaseSource(request);
		requestService.delete(request);
	}

}
