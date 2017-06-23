package com.betterjr.modules.approval.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.approval.IScfReceApprovalFlowDubboService;
import com.betterjr.modules.approval.service.receivable.ScfAcceptService;
import com.betterjr.modules.approval.service.receivable.ScfApplicationReviewService;
import com.betterjr.modules.approval.service.receivable.ScfApplicationService;
import com.betterjr.modules.approval.service.receivable.ScfConfirmTradingBackgrandService;
import com.betterjr.modules.approval.service.receivable.ScfLoanReviewService;
import com.betterjr.modules.approval.service.receivable.ScfManagerFlowService;
import com.betterjr.modules.approval.service.receivable.ScfRegistAssetService;
import com.betterjr.modules.approval.service.receivable.ScfRiskControlService;
import com.betterjr.modules.approval.service.receivable.ScfSchemeConfirmService;
import com.betterjr.modules.approval.service.receivable.ScfSchemeReplyService;
import com.betterjr.modules.approval.service.receivable.ScfSchemeReviewService;
import com.betterjr.modules.approval.service.receivable.ScfSignInitiateService;
import com.betterjr.modules.approval.service.receivable.ScfSignReviewService;
import com.betterjr.modules.approval.service.receivable.ScfSignService;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

/**
 * 
 * 应收账款融资审批流程管理
 * @author tangzw
 *
 */
@Service(interfaceClass = IScfReceApprovalFlowDubboService.class)
public class ScfReceApprovalDubboService implements IScfReceApprovalFlowDubboService {

	private static final int PROCESS_PASS = 1;
	protected final Logger logger = LoggerFactory.getLogger(ScfSellerApprovalDubboService.class);
	
	
	@Autowired
	private ScfApplicationService applicationService;
	@Autowired
	private ScfApplicationReviewService applicationReviewService;
	@Autowired
	private ScfAcceptService acceptService;
	@Autowired
	private ScfConfirmTradingBackgrandService confirmTradingBackgrandService;
	@Autowired
	private ScfLoanReviewService loanReviewService;
	@Autowired
	private ScfRegistAssetService registAssetService;
	@Autowired
	private ScfRiskControlService riskControlService;
	@Autowired
	private ScfSchemeConfirmService schemeConfirmService;
	@Autowired
	private ScfSchemeReplyService schemeReplyService;
	@Autowired
	private ScfSchemeReviewService schemeReviewService;
	@Autowired
	private ScfSignInitiateService signInitiateService;
	@Autowired
	private ScfSignReviewService signReviewService;
	@Autowired
	private ScfSignService signService;
	@Autowired
	private ScfManagerFlowService managerFlowService;
	
	@Override
	public Map<String, Object> application(Map<String, Object> anContext){
		ScfRequestTemp anTemp = applicationService.savApplication(anContext);
		
		applicationService.savRequest(anTemp);
		return anContext;
	}
	
	@Override
	public Map<String, Object> applicationReview(Map<String, Object> anContext, int anResultType){
		if (PROCESS_PASS == anResultType) {
			applicationReviewService.processPass(anContext);
		} else {
			applicationReviewService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> applictionAccept(Map<String, Object> anContext){
		acceptService.processHandle(anContext);
		return anContext;
	}
	
	@Override
	public Map<String, Object> riskControl(Map<String, Object> anContext, int anResultType){
		if (PROCESS_PASS == anResultType) {
			riskControlService.processPass(anContext);
		} else {
			riskControlService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> schemeReply(Map<String, Object> anContext){
		schemeReplyService.processHandle(anContext);
		return anContext;
	}
	
	@Override
	public Map<String, Object> schemeReview(Map<String, Object> anContext, int anResultType){
		if (PROCESS_PASS == anResultType) {
			schemeReviewService.processPass(anContext);
		} else {
			schemeReviewService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> schemeConfirm(Map<String, Object> anContext, int anResultType){
		if (PROCESS_PASS == anResultType) {
			schemeConfirmService.processPass(anContext);
		} else {
			schemeConfirmService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> confirmTradingBackgrand(Map<String, Object> anContext, int anResultType){
		if (PROCESS_PASS == anResultType) {
			confirmTradingBackgrandService.processPass(anContext);
		} else {
			confirmTradingBackgrandService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> signInitiate(Map<String, Object> anContext) {
		signInitiateService.processHandle(anContext);
		return anContext;
	}
	
	@Override
	public Map<String, Object> signReview(Map<String, Object> anContext, int anResultType) {
		if (PROCESS_PASS == anResultType) {
			signReviewService.processPass(anContext);
		} else {
			signReviewService.processReject(anContext);
		}
		return anContext;
	}
	
	@Override
	public Map<String, Object> sign(Map<String, Object> anContext) {
		signService.processHandle(anContext);
		return anContext;
	}
	
	@Override
	public Map<String, Object> registerAsset(Map<String, Object> anContext) {
		registAssetService.processHandle(anContext);
		return anContext;
	}
	
	@Override
	public Map<String, Object> loanReview(Map<String, Object> anContext, int anResultType) {
		if (PROCESS_PASS == anResultType) {
			loanReviewService.processPass(anContext);
		} else {
			loanReviewService.processReject(anContext);
		}
		return anContext;
	}

	@Override
	public void endFlow(Map<String, Object> anContext, int anResultType) {
		if (PROCESS_PASS == anResultType) {
			managerFlowService.processCancel(anContext);
		} else {
			managerFlowService.processEnd(anContext);
		}
	}
	
}
