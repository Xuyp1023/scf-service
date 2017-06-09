package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterWebServiceException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfFactorRemoteHelper;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.credit.service.ScfCreditService;
import com.betterjr.modules.customer.ICustMechBankAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustMechLawService;
import com.betterjr.modules.customer.entity.CustMechBankAccount;
import com.betterjr.modules.customer.entity.CustMechBase;
import com.betterjr.modules.customer.entity.CustMechLaw;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.entity.ScfServiceFee;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.template.entity.ScfContractTemplate;
import com.betterjr.modules.template.service.ScfContractTemplateService;
import com.betterjr.modules.workflow.data.CustFlowNodeData;

@Service
public class ScfRequestService extends BaseService<ScfRequestMapper, ScfRequest> {

	@Autowired
	private ScfAcceptBillService scfAcceptService;

	@Autowired
	private ScfPayPlanService payPlanService;
	@Autowired
	private CustAccountService custAccountService;
	@Autowired
	private ScfRequestSchemeService schemeService;
	@Autowired
	private ScfLoanService loanService;
	@Autowired
	private ScfServiceFeeService serviceFeeService;
	@Autowired
	private ScfOrderService orderService;

	@Autowired
	private ScfProductService productService;
	
	@Autowired
	private ScfProductConfigService productConfigService;
	
	@Autowired
	private ScfOfferService offerService;
	@Autowired
	private ScfEnquiryService enquiryService;
	@Autowired
	private ScfServiceFeeService feeService;
	@Autowired
	private ScfCreditDetailService creditDetailService;
	@Autowired
	private ScfCreditService scfCreditService;
	@Autowired
	private ScfAgreementService agreementService;
	@Autowired
	private ScfContractTemplateService contractTemplateService;

	@Reference(interfaceClass = ICustMechLawService.class)
	private ICustMechLawService mechLawService;
	@Reference(interfaceClass = ICustMechBaseService.class)
	private ICustMechBaseService mechBaseService;
	@Reference(interfaceClass = ICustMechBankAccountService.class)
	private ICustMechBankAccountService mechBankAccountService;

	@Autowired
	private ScfFactorRemoteHelper factorRemoteHelper;

	public ScfRequest saveStartRequest(ScfRequest anRequest) {
		// 检查核心企业 在 该保理公司的授信情况
		ScfCredit coreCredit = scfCreditService.findCredit(anRequest.getCoreCustNo(), anRequest.getCoreCustNo(),
				anRequest.getFactorNo(), anRequest.getCreditMode());
		BTAssert.notNull(coreCredit, "无法获取核心企业授信记录");

		anRequest.setRequestFrom("1");
		anRequest.setFlowStatus("1");
		anRequest = this.addRequest(anRequest);

		if (null != anRequest.getOfferId()) {
			// 从报价过来的要改变报价状态
			offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "3");

			ScfOffer offer = offerService.selectByPrimaryKey(anRequest.getOfferId());
			// 从报价过来的要改变报价状态
			enquiryService.saveUpdateBusinStatus(offer.getEnquiryNo(), "-2");
		}

		// 关联订单
		orderService.saveInfoRequestNo(anRequest.getRequestType(), anRequest.getRequestNo(), anRequest.getOrders());
		// 冻结订单
		orderService.forzenInfos(anRequest.getRequestNo(), null);

		// 当融资流程启动时,冻结授信额度 add by Liusq 2016-10-20
		ScfCreditInfo anCreditInfo = new ScfCreditInfo();
		anCreditInfo.setBusinFlag(anRequest.getRequestType());
		anCreditInfo.setBalance(anRequest.getBalance());
		anCreditInfo.setBusinId(Long.valueOf(anRequest.getRequestNo()));
		anCreditInfo.setCoreCustNo(anRequest.getCoreCustNo());
		anCreditInfo.setCustNo(anRequest.getCustNo());
		anCreditInfo.setFactorNo(anRequest.getFactorNo());
		anCreditInfo.setCreditMode(anRequest.getCreditMode());
		anCreditInfo.setRequestNo(anRequest.getRequestNo());
		anCreditInfo.setDescription(anRequest.getDescription());
		creditDetailService.saveFreezeCredit(anCreditInfo);

		return anRequest;
	}

	/**
	 * 新增融资申请
	 * 
	 * @param anRequest
	 * @return
	 */
	public ScfRequest addRequest(ScfRequest anRequest) {
		BTAssert.notNull(anRequest, "新增融资申请失败-anRequest不能为空");
		anRequest.init(anRequest);
		anRequest.setCustName(custAccountService.queryCustName(anRequest.getCustNo()));
		anRequest.setRequestDate(BetterDateUtils.getNumDate());
		this.insert(anRequest);

		fillCustName(anRequest);
		return anRequest;
	}

	/**
	 * 修改融资申请
	 * 
	 * @param anRequest
	 * @return
	 */
	public ScfRequest saveModifyRequest(ScfRequest anRequest, String anRequestNo) {
		BTAssert.notNull(anRequest, "修改融资申请失败-anRequest不能为空");

		if (Collections3.isEmpty(selectByProperty("requestNo", anRequestNo))) {
			throw new BytterTradeException(40001, "修改融资申请失败-找不到原数据");
		}

		anRequest.initModify(anRequest);
		anRequest.setRequestNo(anRequestNo);
		this.updateByPrimaryKeySelective(anRequest);
		return anRequest;
	}

	public ScfRequest saveModifyRequest(ScfRequest anRequest, Map<String, Object> anValues) {
		BTAssert.notNull(anRequest, "修改融资申请失败-anRequest不能为空");
		anRequest.initModify(anRequest);
		this.updateByExampleSelective(anRequest, anRequest.getRequestNo(), anValues);
		return anRequest;
	}

	/**
	 * 修改融资申请
	 * 
	 * @param anRequest
	 * @return
	 */
	public ScfRequest saveAutoModifyRequest(ScfRequest anRequest, String anRequestNo) {
		BTAssert.notNull(anRequest, "修改融资申请失败-anRequest不能为空");

		if (Collections3.isEmpty(selectByProperty("requestNo", anRequestNo))) {
			throw new BytterTradeException(40001, "修改融资申请失败-找不到原数据");
		}

		anRequest.initModify(anRequest);
		anRequest.setRequestNo(anRequestNo);
		this.updateByPrimaryKeySelective(anRequest);
		return anRequest;
	}

	/**
	 * 查询融资申请列表
	 * 
	 * @param anMap
	 * @param anFlag
	 * @param anPageNum
	 * @param anPageSize
	 * @return
	 */
	public Page<ScfRequest> queryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
		Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag,
				"regDate desc,regTime desc");
		for (ScfRequest scfRequest : page) {
			fillCustName(scfRequest);
		}
		return page;
	}

	/**
	 * 查询融资申详情
	 * 
	 * @param anRequestNo
	 * @return
	 */
	public ScfRequest findRequestDetail(String anRequestNo) {
		ScfRequest request = this.selectByPrimaryKey(anRequestNo);
		BTAssert.notNull(request, " 查询融资申详情失败-requestNo不能为空");
		if (null == request) {
			logger.debug("没查到相关数据！");
			return new ScfRequest();
		}

		this.fillCustName(request);

		// 设置还款计划
		request.setPayPlan(payPlanService.findPayPlanByRequest(anRequestNo));

		return request;
	}

	/**
	 * 根据申请单号查询申请单信息
	 * 
	 * @param anRequestNo
	 * @return
	 */
	public ScfRequest findRequestByRequestNo(String anRequestNo) {
		ScfRequest request = this.selectByPrimaryKey(anRequestNo);
		if (null == request) {
			logger.debug("没查到相关数据！");
			return new ScfRequest();
		}
		return request;
	}

	public void fillCustName(ScfRequest request) {
		// 设置相关名称
		request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
		request.setFactorName(custAccountService.queryCustName(request.getFactorNo()));
		ScfProduct product = productService.findProductById(request.getProductId());
		if (null != product) {
			request.setProductName(product.getProductName());
		}
		
		if(BetterStringUtils.isBlank(request.getProductName())){
			ScfProductConfig productConfig = productConfigService.selectByPrimaryKey(request.getProductId());
			if (null != productConfig) {
				request.setProductName(productConfig.getProductName());
			}
		}

		ScfServiceFee serviceFee = feeService.findServiceFeeByType(request.getRequestNo(), "1");
		if (null != serviceFee) {
			request.setServicefeeBalance(serviceFee.getBalance());
		}
	}

	/**
	 * 资方-出具融资方案（生成应收账款转让通知书/保兑仓业务三方合作协议）
	 * 
	 * @param anMap
	 * @return
	 */
	public ScfRequestScheme saveOfferScheme(ScfRequestScheme anScheme) {
		// 将融资确认后的融资信息放入到申请表中（修改申请表中的信息）
		ScfRequest request = this.selectByPrimaryKey(anScheme.getRequestNo());
		request.setApprovedPeriod(anScheme.getApprovedPeriod());
		request.setApprovedPeriodUnit(anScheme.getApprovedPeriodUnit());
		request.setManagementRatio(anScheme.getApprovedManagementRatio());
		request.setApprovedRatio(anScheme.getApprovedRatio());
		request.setServicefeeRatio(anScheme.getServicefeeRatio());
		request.setApprovedBalance(anScheme.getApprovedBalance());
		request.setConfirmBalance(anScheme.getApprovedBalance());
		this.saveModifyRequest(request, anScheme.getRequestNo());

		// 1,订单，2:票据;3:应收款;4:经销商
		if (BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType())) {
			// 生成-保兑仓业务三方合作协议
			agreementService.transProtacal(this.getProtacal(request));
		} else {
			// 生成-应收帐款转让通知书
			ScfRequestNotice noticeRequest = this.getNotice(request);
			String appNo = agreementService.transNotice(noticeRequest);

			// 添加转让明细（因为在转让申请时就添加了 转让明细，如果核心企业不同意，那明细需要删除，但目前没有做删除这步）
			agreementService.transCredit(this.getCreditList(request), appNo);
		}

		return schemeService.addScheme(anScheme);
	}

	/**
	 * 融资方-确认融资方案（确认融资金额，期限，利率，经销商签署-三方协议书）
	 * 
	 * @param anRequestNo
	 * @param anAduitStatus
	 * @return
	 */
	public ScfRequestScheme saveConfirmScheme(String anRequestNo, String anApprovalResult, String anSmsCode) {
		ScfRequest request = this.findRequestDetail(anRequestNo);
		ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
		BTAssert.notNull(scheme);
		scheme.setCustAduit(anApprovalResult);

		// 电子合同类型，0：应收账款债权转让通知书，1：买方确认意见，2三方协议书
		String agreeType = BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType()) ? "2" : "0";
		if (BetterStringUtils.equals("0", anApprovalResult)) {
			// if(false ==
			// agreementService.sendValidCodeByRequestNo(anRequestNo, agreeType,
			// anSmsCode)){
			// return AjaxObject.newError("操作失败：短信验证码错误").toJson();
			// }
		} else {
			// 取消签约
			agreementService.cancelElecAgreement(anRequestNo, agreeType, "");
		}

		return schemeService.saveModifyScheme(scheme);
	}

	/**
	 * 资方-发起贸易背景确认（生成-应收账款转让确认意见确认书 / 三方协议书）
	 * 
	 * @param anMap
	 * @return
	 */
	public ScfRequestScheme saveRequestTradingBackgrand(String anRequestNo, String anApprovalResult, String anSmsCode) {
		ScfRequest request = this.findRequestDetail(anRequestNo);
		ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
		if (BetterStringUtils.equals("0", anApprovalResult) == true) {
			// 1,订单，2:票据;3:应收款;4:经销商
			if (BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType())) {

				// 签署三方协议
				// if(false ==
				// agreementService.sendValidCodeByRequestNo(anRequestNo, "2",
				// anSmsCode)){
				// return AjaxObject.newError("操作失败：短信验证码错误").toJson();
				// }
			} else {
				// 生成-应收账款转让确认意见确认书
				agreementService.transOpinion(this.getOption(request));
			}

			// 修改融资方案--核心企业确认状态（待确认）
			scheme.setCoreCustAduit("0");
		}

		return schemeService.saveModifyScheme(scheme);
	}

	/**
	 * 核心企业-确认贸易背景（签署-应收账款转让确认意见书/三方协议书）
	 * 
	 * @param anMap
	 * @return
	 */
	public ScfRequestScheme confirmTradingBackgrand(String anRequestNo, String anApprovalResult, String anSmsCode) {
		ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
		BTAssert.notNull(scheme);

		ScfRequest request = this.findRequestDetail(anRequestNo);
		// 电子合同类型，0：应收账款债权转让通知书，1：买方确认意见，2三方协议书
		String agreeType = BetterStringUtils.equals(RequestType.SELLER.getCode(), request.getRequestType()) ? "2" : "1";
		if (BetterStringUtils.equals(anApprovalResult, "0")) {
			// 签署协议
			// if(false ==
			// agreementService.sendValidCodeByRequestNo(anRequestNo, agreeType,
			// anSmsCode)){
			// return AjaxObject.newError("操作失败：短信验证码错误").toJson();
			// }
		} else {
			// 取消签约
			agreementService.cancelElecAgreement(anRequestNo, agreeType, "");
		}

		// 修改核心企业确认状态
		scheme.setCoreCustAduit("1");
		return schemeService.saveModifyScheme(scheme);
	}

	/**
	 * 资方-确认放款（收取放款手续费,计算利息，生成还款计划）
	 * 
	 * @param anMap
	 * @return
	 */
	public ScfRequest saveConfirmLoan(ScfLoan anLoan) {
		ScfRequest request = this.selectByPrimaryKey(anLoan.getRequestNo());
		BTAssert.notNull(request, "确认放款失败-找不到融资申请单");

		// ---修申请表---------------------------------------------
		request.setActualDate(anLoan.getLoanDate());
		request.setEndDate(anLoan.getEndDate());
		request.setConfirmBalance(anLoan.getLoanBalance());
		request.setLoanBalance(anLoan.getLoanBalance());
		this.saveModifyRequest(request, request.getRequestNo());

		// ---保存还款计划------------------------------------------
		ScfPayPlan plan = createPayPlan(anLoan.getInterestBalance(), anLoan.getManagementBalance(), request);
		payPlanService.addPayPlan(plan);

		// ---保存手续费----------------------------------------------
		BigDecimal servicefeeBalance = saveServiceFee(anLoan.getServicefeeBalance(), request);

		// ---保存放款记录------------------------------------------
		anLoan.setInterestBalance(plan.getShouldInterestBalance());
		anLoan.setManagementBalance(plan.getShouldManagementBalance());
		anLoan.setCustNo(request.getCustNo());
		anLoan.setServicefeeBalance(servicefeeBalance);
		anLoan.setFactorNo(request.getFactorNo());
		loanService.addLoan(anLoan);

		// 占用授信额度
		ScfCreditInfo anCreditInfo = new ScfCreditInfo();
		anCreditInfo.setBalance(request.getLoanBalance());
		anCreditInfo.setBusinId(anLoan.getId());
		anCreditInfo.setCoreCustNo(request.getCoreCustNo());
		anCreditInfo.setCustNo(request.getCustNo());
		anCreditInfo.setFactorNo(request.getFactorNo());
		anCreditInfo.setRequestNo(request.getRequestNo());
		anCreditInfo.setDescription(request.getDescription());
		// 微信
		if (BetterStringUtils.equals("2", request.getRequestFrom())) {
			anCreditInfo.setBusinFlag("2");// 业务类型(1:应收账款融资;2:应收账款票据质押融资;3:预付款融资;)
			anCreditInfo.setCreditMode("1");// 授信方式(1:信用授信(循环);2:信用授信(一次性);3:担保信用(循环);4:担保授信(一次性);)
		} else {// PC端
			anCreditInfo.setBusinFlag(request.getRequestType());
			anCreditInfo.setCreditMode(request.getCreditMode());
		}
		creditDetailService.saveOccupyCredit(anCreditInfo);

		return request;
	}

	private BigDecimal saveServiceFee(BigDecimal servicefeeBalance, ScfRequest anRequest) {
		if (null == servicefeeBalance) {
			// 计算手续费(按千分之收)
			servicefeeBalance = anRequest.getApprovedBalance().multiply(anRequest.getServicefeeRatio())
					.divide(new BigDecimal(1000));
		}

		if (servicefeeBalance.compareTo(BigDecimal.ZERO) < 1) {
			return BigDecimal.ZERO;
		}

		ScfServiceFee serviceFee = new ScfServiceFee();
		serviceFee.setCustNo(anRequest.getCustNo());
		serviceFee.setFactorNo(anRequest.getFactorNo());
		serviceFee.setRequestNo(anRequest.getRequestNo());
		serviceFee.setPayDate(anRequest.getActualDate());
		serviceFee.setBalance(servicefeeBalance);
		serviceFee.setFeeType("1");
		serviceFeeService.addServiceFee(serviceFee);
		return servicefeeBalance;
	}

	private ScfPayPlan createPayPlan(BigDecimal anInterestBalance, BigDecimal anManagementBalance,
			ScfRequest anRequest) {
		ScfPayPlan plan = new ScfPayPlan();
		plan.setTerm(1);
		plan.setCustNo(anRequest.getCustNo());
		plan.setCoreCustNo(anRequest.getCoreCustNo());
		plan.setFactorNo(anRequest.getFactorNo());
		plan.setRequestNo(anRequest.getRequestNo());
		plan.setStartDate(anRequest.getActualDate());
		plan.setPlanDate(anRequest.getEndDate());
		plan.setRatio(anRequest.getApprovedRatio());
		plan.setManagementRatio(anRequest.getManagementRatio());

		// 计算应还
		plan.setShouldPrincipalBalance(MathExtend.subtract(anRequest.getLoanBalance(), anRequest.getBondBalance()));
		if (null == anInterestBalance) {
			plan.setShouldInterestBalance(payPlanService.getFee(anRequest.getFactorNo(), anRequest.getLoanBalance(),
					anRequest.getApprovedRatio(), anRequest.getActualDate(), anRequest.getEndDate()));
		} else {
			plan.setShouldInterestBalance(anInterestBalance);
		}

		if (null == anManagementBalance) {
			plan.setShouldManagementBalance(payPlanService.getFee(anRequest.getFactorNo(), anRequest.getLoanBalance(),
					anRequest.getManagementRatio(), anRequest.getActualDate(), anRequest.getEndDate()));
		} else {
			plan.setShouldManagementBalance(anManagementBalance);
		}

		// 未还
		plan.setSurplusPrincipalBalance(plan.getShouldPrincipalBalance());
		plan.setSurplusInterestBalance(plan.getShouldInterestBalance());
		plan.setSurplusManagementBalance(plan.getShouldManagementBalance());

		// 计算应还未还总额
		BigDecimal totalBalance = plan.getShouldPrincipalBalance().add(plan.getShouldInterestBalance())
				.add(plan.getShouldManagementBalance());
		plan.setShouldTotalBalance(totalBalance);
		plan.setSurplusTotalBalance(totalBalance);
		return plan;
	}

	/**
	 * 查询待批融资 <=150 出具保理方案 - 110 融资方确认方案 - 120 发起融资背景确认 - 130 核心企业确认背景 - 140
	 * 放款确认 - 150 完成融资 - 160 放款完成 - 170
	 */
	public Page<ScfRequest> queryPendingRequest(Map<String, Object> anMap, String anFlag, int anPageNum,
			int anPageSize) {
		// 放款前的状态
		anMap.put("LTEtradeStatus", "150");
		Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
		for (ScfRequest request : page) {
			fillCustName(request);
			// 设置还款计划
			request.setPayPlan(payPlanService.findPayPlanByRequest(request.getRequestNo()));
		}
		return page;
	}

	/**
	 * 查询还款融资 >150 <190 出具保理方案 - 110 融资方确认方案 - 120 发起融资背景确认 - 130 核心企业确认背景 - 140
	 * 放款确认 - 150 完成融资 - 160 放款完成 - 170
	 */
	public Page<ScfRequest> queryRepaymentRequest(Map<String, Object> anMap, String anFlag, int anPageNum,
			int anPageSize) {
		// 放款后结束前的状态
		anMap.put("GTtradeStatus", "150");
		anMap.put("LTtradeStatus", "190");
		Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
		for (ScfRequest request : page) {
			fillCustName(request);
			// 设置还款计划
			request.setPayPlan(payPlanService.findPayPlanByRequest(request.getRequestNo()));
		}
		return page;
	}

	/**
	 * 查询历史融资 >=190 出具保理方案 - 110 融资方确认方案 - 120 发起融资背景确认 - 130 核心企业确认背景 - 140
	 * 放款确认 - 150 完成融资 - 160 放款完成 - 170
	 */
	public Page<ScfRequest> queryCompletedRequest(Map<String, Object> anMap, String anFlag, int anPageNum,
			int anPageSize) {
		// 放款后结束前的状态
		anMap.put("GTEtradeStatus", "190");
		Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
		for (ScfRequest scfRequest : page) {
			fillCustName(scfRequest);
			// 设置还款计划
			scfRequest.setPayPlan(payPlanService.findPayPlanByRequest(scfRequest.getRequestNo()));
		}
		return page;
	}

	/**
	 * 核心企业融资查询 出具保理方案 - 110 融资方确认方案 - 120 发起融资背景确认 - 130 核心企业确认背景 - 140 放款确认 -
	 * 150 完成融资 - 160 放款完成 - 170 anBusinStatus - 1：未放款，2：还款中 3.已还款
	 */
	public Page<ScfRequest> queryCoreEnterpriseRequest(Map<String, Object> anMap, String anBusinStatus, String anFlag,
			int anPageNum, int anPageSize) {
		anMap = Collections3.fuzzyMap(anMap, new String[] { "coreCustNo", "custNo", "factorNo" });
		if (BetterStringUtils.isBlank(anBusinStatus)) {
			Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
			for (ScfRequest request : page) {
				fillCustName(request);
				// 设置还款计划
				request.setPayPlan(payPlanService.findPayPlanByRequest(request.getRequestNo()));
			}
			return page;
		} else {
			switch (anBusinStatus) {
			case "1":
				// 未放款
				return this.queryPendingRequest(anMap, anFlag, anPageNum, anPageSize);
			case "2":
				// 还款中
				return this.queryRepaymentRequest(anMap, anFlag, anPageNum, anPageSize);
			case "3":
				// 已还款
				return this.queryCompletedRequest(anMap, anFlag, anPageNum, anPageSize);
			default:
				Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
				for (ScfRequest request : page) {
					fillCustName(request);
					// 设置还款计划
					request.setPayPlan(payPlanService.findPayPlanByRequest(request.getRequestNo()));
				}
				return page;
			}
		}
	}

	public ScfRequest approveRequest(Map<String, Object> anMap) {
		return null;
	}

	public List<ScfRequestCredit> getCreditList(ScfRequest anRequest) {
		List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
		String type = anRequest.getRequestType();
		if (BetterStringUtils.equals(RequestType.ORDER.getCode(), type)
				|| BetterStringUtils.equals(RequestType.SELLER.getCode(), type)) {
			List<ScfOrder> list = (List) orderService.findInfoListByRequest(anRequest.getRequestNo(),
					RequestType.ORDER.getCode());
			for (ScfOrder order : list) {
				creditList.addAll(setInvoice(anRequest, order.getInvoiceList(), order.getBalance(), order.getOrderNo(),
						order.getEndDate(), order.getAgreementList()));
			}
		} else if (BetterStringUtils.equals(RequestType.BILL.getCode(), type)) {
			List<ScfAcceptBill> list = (List) orderService.findInfoListByRequest(anRequest.getRequestNo(),
					RequestType.BILL.getCode());
			for (ScfAcceptBill bill : list) {
				creditList.addAll(setInvoice(anRequest, bill.getInvoiceList(), bill.getBalance(), bill.getBillNo(),
						bill.getEndDate(), bill.getAgreementList()));
			}
		} else if (BetterStringUtils.equals(RequestType.RECEIVABLE.getCode(), type)) {
			List<ScfReceivable> list = (List) orderService.findInfoListByRequest(anRequest.getRequestNo(),
					RequestType.RECEIVABLE.getCode());
			for (ScfReceivable receivable : list) {
				creditList.addAll(setInvoice(anRequest, receivable.getInvoiceList(), receivable.getBalance(),
						receivable.getReceivableNo(), receivable.getEndDate(), receivable.getAgreementList()));
			}
		}
		return creditList;
	}

	/**
	 * 设置转主明细相关信息
	 * 
	 * @param request
	 * @param anInvoiceList
	 * @param anBalance
	 * @param anObjectNo
	 * @param anAgreement
	 * @return
	 */
	private List<ScfRequestCredit> setInvoice(ScfRequest request, List<ScfInvoice> anInvoiceList, BigDecimal anBalance,
			String anObjectNo, String anEndDate, List<CustAgreement> custAgreementList) {
		CustAgreement custAgreement = Collections3.getFirst(custAgreementList);// 暂时都取第一个贸易合同信息
		List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
		for (ScfInvoice invoice : anInvoiceList) {
			ScfRequestCredit credit = new ScfRequestCredit();
			credit.setTransNo(anObjectNo);
			credit.setRequestNo(request.getRequestNo());
			credit.setBalance(anBalance);
			credit.setInvoiceNo(invoice.getInvoiceNo());
			credit.setInvoiceBalance(invoice.getBalance());
			credit.setEndDate(anEndDate);
			if (custAgreement != null) {
				credit.setAgreeNo(custAgreement.getAgreeNo());
			}
			creditList.add(credit);
		}
		return creditList;
	}

	/**
	 * 设置转让通知书相关信息
	 * 
	 * @param anRequestNo
	 * @param anRequest
	 * @return
	 */
	public ScfRequestNotice getNotice(ScfRequest anRequest) {
		// TODO 合同名称、 银行账号， 保理公司详细地址
		CustMechBankAccount bankAccount = mechBankAccountService.findDefaultBankAccount(anRequest.getFactorNo());

		String noticeNo = BetterDateUtils.getDate("yyyyMMdd") + anRequest.getRequestNo();
		ScfRequestNotice noticeRequest = new ScfRequestNotice();
		noticeRequest.setRequestNo(anRequest.getRequestNo());
		noticeRequest.setAgreeName(anRequest.getCustName() + "应收账款债权转让通知书");
		// noticeRequest.setNoticeNo(noticeNo);
		noticeRequest.setBuyer(anRequest.getCoreCustName());
		noticeRequest.setFactorRequestNo(noticeNo);
		noticeRequest.setBankAccount(bankAccount.getBankAcco());
		CustMechBase custBase = mechBaseService.findBaseInfo(anRequest.getFactorNo());
		noticeRequest.setFactorAddr(custBase.getAddress());
		noticeRequest.setFactorPost(custBase.getZipCode());

		// CustMechLaw custMechLaw =
		// mechLawService.findLawInfo(anRequest.getCustNo());
		// noticeRequest.setFactorLinkMan(custMechLaw.getName());
		return noticeRequest;
	}

	/**
	 * TODO 设置三方协议相关信息
	 * 
	 * @return
	 */
	public ScfRequestProtacal getProtacal(ScfRequest anRequest) {
		// 负责人默认为法人
		String firstJob = "法人";

		ScfRequestProtacal protacal = new ScfRequestProtacal();
		CustMechLaw factorLow = mechLawService.findLawInfo(anRequest.getFactorNo());
		CustMechLaw buyLow = mechLawService.findLawInfo(anRequest.getCoreCustNo());
		CustMechLaw sellerLow = mechLawService.findLawInfo(anRequest.getCustNo());

		CustMechBase factorBase = mechBaseService.findBaseInfo(anRequest.getFactorNo());
		CustMechBase buyBase = mechBaseService.findBaseInfo(anRequest.getCoreCustNo());
		CustMechBase sellerBase = mechBaseService.findBaseInfo(anRequest.getCustNo());
		protacal.setFirstAddress(factorBase.getAddress());
		protacal.setFirstFax(factorBase.getFax());
		protacal.setFirstNo(anRequest.getFactorNo().toString());
		protacal.setFirstName(anRequest.getFactorName());
		protacal.setFirstJob(firstJob);
		protacal.setFirstPhone(factorBase.getPhone());
		protacal.setFirstLegal(factorLow.getCustName());

		protacal.setSecondAddress(buyBase.getAddress());
		protacal.setSecondFax(buyBase.getFax());
		protacal.setSecondNo(anRequest.getCoreCustNo());
		protacal.setSecondName(anRequest.getCoreCustName());
		protacal.setSecondJob(firstJob);
		protacal.setSecondPhone(buyBase.getPhone());
		protacal.setSecondLegal(buyLow.getCustName());

		protacal.setThreeAddress(sellerBase.getAddress());
		protacal.setThreeFax(sellerBase.getFax());
		protacal.setThreeNo(anRequest.getCustNo());
		protacal.setThreeName(anRequest.getCustName());
		protacal.setThreeJob(firstJob);
		protacal.setThreePhone(sellerBase.getPhone());
		protacal.setThreeLegal(sellerLow.getCustName());
		protacal.setRequestNo(anRequest.getRequestNo());

		List<CustAgreement> list = orderService.findRelationInfo(anRequest.getRequestNo(),
				ScfOrderRelationType.AGGREMENT);
		protacal.setProtocalNo(Collections3.getFirst(list).getAgreeNo());
		return protacal;
	}

	/**
	 * 设置转让意见确认书相关信息
	 * 
	 * @param anRequest
	 * @return
	 */
	public ScfRequestOpinion getOption(ScfRequest anRequest) {
		String noticeNo = BetterDateUtils.getNumDate() + anRequest.getRequestNo();
		ScfRequestOpinion opinion = new ScfRequestOpinion();
		opinion.setRequestNo(anRequest.getRequestNo());
		opinion.setFactorRequestNo(noticeNo);
		opinion.setConfirmNo(noticeNo);
		opinion.setSupplier(anRequest.getCustName());
		return opinion;
	}

	public void getDefaultNode(List<CustFlowNodeData> list) {
		CustFlowNodeData node = new CustFlowNodeData();
		node = list.get(list.size() - 1);
		node.setNodeCustomName(RequestTradeStatus.FINISH_LOAN.getName());

		node = new CustFlowNodeData();
		node.setNodeCustomName(RequestTradeStatus.OVERDUE.getName());
		node.setSysNodeName(RequestTradeStatus.OVERDUE.getName());
		node.setSysNodeId(new Long(RequestTradeStatus.OVERDUE.getCode()));
		node.setId(new Long(RequestTradeStatus.OVERDUE.getCode()));
		list.add(node);

		node = new CustFlowNodeData();
		node.setNodeCustomName(RequestTradeStatus.EXTENSION.getName());
		node.setSysNodeName(RequestTradeStatus.EXTENSION.getName());
		node.setSysNodeId(new Long(RequestTradeStatus.EXTENSION.getCode()));
		node.setId(new Long(RequestTradeStatus.EXTENSION.getCode()));
		list.add(node);

		node = new CustFlowNodeData();
		node.setNodeCustomName(RequestTradeStatus.CLEAN.getName());
		node.setSysNodeName(RequestTradeStatus.CLEAN.getName());
		node.setSysNodeId(new Long(RequestTradeStatus.CLEAN.getCode()));
		node.setId(new Long(RequestTradeStatus.CLEAN.getCode()));
		list.add(node);

		node = new CustFlowNodeData();
		node.setNodeCustomName(RequestTradeStatus.CLOSED.getName());
		node.setSysNodeName(RequestTradeStatus.CLOSED.getName());
		node.setSysNodeId(new Long(RequestTradeStatus.CLOSED.getCode()));
		node.setId(new Long(RequestTradeStatus.CLOSED.getCode()));
		list.add(node);
	}

	/**
	 * 填充订单信息
	 * 
	 * @param anScfRequest
	 */
	public Object fillOrderInfo(ScfRequest anScfRequest) {
		// 经销商融资不用设置
		if (BetterStringUtils.equals(RequestType.SELLER.getCode(), anScfRequest.getRequestType())) {
			return null;
		}

		if (BetterStringUtils.equals(RequestType.ORDER.getCode(), anScfRequest.getRequestType())) {
			List<ScfOrder> orderList = (List) orderService.findInfoListByRequest(anScfRequest.getRequestNo(),
					anScfRequest.getRequestType());
			return Collections3.getFirst(orderList);
		} else if (BetterStringUtils.equals(RequestType.BILL.getCode(), anScfRequest.getRequestType())) {
			List<ScfAcceptBill> orderList = (List) orderService.findInfoListByRequest(anScfRequest.getRequestNo(),
					anScfRequest.getRequestType());
			return Collections3.getFirst(orderList);
		} else {
			List<ScfReceivable> orderList = (List) orderService.findInfoListByRequest(anScfRequest.getRequestNo(),
					anScfRequest.getRequestType());
			return Collections3.getFirst(orderList);
		}

	}

	/**
	 * 查询融资列表，无分页
	 */
	public List<ScfRequest> findRequestList(Map<String, Object> anMap) {
		List<ScfRequest> requestList = this.selectByProperty(anMap);
		for (ScfRequest request : requestList) {
			fillCustName(request);
		}
		return requestList;
	}

	/**
	 * anBusinStatus - 1：未放款，2：还款中 3.已还款
	 */
	public Page querySupplierRequestByCore(Map<String, Object> anQueryConditionMap, String anBusinStatus, String anFlag,
			int anPageNum, int anPageSize) {
		anQueryConditionMap.put("requestType", new String[] { RequestType.ORDER.getCode(), RequestType.BILL.getCode(),
				RequestType.RECEIVABLE.getCode() });
		return this.queryCoreEnterpriseRequest(anQueryConditionMap, anBusinStatus, anFlag, anPageNum, anPageSize);
	}

	/**
	 * anBusinStatus - 1：未放款，2：还款中 3.已还款
	 */
	public Page querySellerRequestByCore(Map<String, Object> anQueryConditionMap, String anBusinStatus, String anFlag,
			int anPageNum, int anPageSize) {
		anQueryConditionMap.put("requestType", new String[] { RequestType.SELLER.getCode() });
		return this.queryCoreEnterpriseRequest(anQueryConditionMap, anBusinStatus, anFlag, anPageNum, anPageSize);

	}

	public List<Long> findVoucherBatchNo(String anRequest) {
		ScfRequest request = this.selectByPrimaryKey(anRequest);
		if (request != null) {
			return scfAcceptService.findBillRelBatchNo(Long.parseLong(request.getBillId()));
		}
		throw new BytterWebServiceException(WebServiceErrorCode.E1004);
	}

	/**
	 * 发送申请单状态给保理公司
	 * 
	 * @param anRequestNo
	 *            申请单编号
	 * @param anStatus
	 *            申请单状态
	 */
	public void updateAndSendRequestStatus(String anRequestNo, String anStatus) {
		ScfRequest request = this.selectByPrimaryKey(anRequestNo);
		if (request != null) {
			request.setOutStatus(anStatus);
			this.factorRemoteHelper.sendRequestStatus(request);
			this.updateByPrimaryKey(request);
		} else {
			logger.warn("sendRequestStatus method not find requestNo " + anRequestNo);
		}

	}

	/**
	 * 查询申请列表
	 * 
	 * @param anMap
	 * @param anFlag
	 * @param anPageNum
	 * @param anPageSize
	 * @return
	 */
	private Page<ScfRequest> selectRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
		if (null != anMap.get("lastStatus") && BetterStringUtils.isNotEmpty(anMap.get("lastStatus").toString())) {
			anMap.put("lastStatus", anMap.get("lastStatus").toString().split(","));
		}
		Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
		for (ScfRequest scfRequest : page) {
			this.fillCustName(scfRequest);
			// 如果已放款，设置还款计划
			if (Integer.parseInt(scfRequest.getLastStatus()) > 3) {
				scfRequest.setPayPlan(payPlanService.findPayPlanByRequest(scfRequest.getRequestNo()));
			}
		}
		return page;
	}

	public Page<ScfRequest> queryRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
		String[] queryTerm = new String[] { "lastStatus", "custNo", "factorNo", "coreCustNo", "GTErequestDate",
				"LTErequestDate", "GTEactualDate", "LTEactualDate", "requestType", "custType" };
		anMap = Collections3.filterMap(anMap, queryTerm);
		if (UserUtils.supplierUser() || UserUtils.sellerUser()) {
			anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
		} else if (UserUtils.coreUser()) {
			anMap.put("coreCustNo", UserUtils.getDefCustInfo().getCustNo());
		} else if (UserUtils.factorUser()) {
			if (null == anMap.get("factorNo")) {
				anMap.put("factorNo", UserUtils.getDefCustInfo().getCustNo());
			}
		} else {
			return null;
		}
		return this.selectRequest(anMap, anFlag, anPageNum, anPageSize);
	}

	public ScfContractTemplate getFactoryContactTemplate(String requestNo, String tempType) {
		// 获取融资信息
		ScfRequest request = findRequestDetail(requestNo);
		if (null == request) {
			logger.error("Can't get request information with request no:" + requestNo);
			throw new BytterTradeException(40001, "无法获取融资信息");
		}
		return contractTemplateService.findTemplateByType(request.getFactorNo(), tempType, "1");
	}
	
	/**
	 * 2.3.1-资方-出具融资方案
	 * 
	 * @param anMap
	 * @return
	 */
	public ScfRequestScheme saveScheme(ScfRequestScheme anScheme) {
		// 将融资确认后的融资信息放入到申请表中（修改申请表中的信息）
		ScfRequest request = this.selectByPrimaryKey(anScheme.getRequestNo());
		request.setApprovedPeriod(anScheme.getApprovedPeriod());
		request.setApprovedPeriodUnit(anScheme.getApprovedPeriodUnit());
		request.setApprovedRatio(anScheme.getApprovedRatio());
		request.setApprovedBalance(anScheme.getApprovedBalance());
		request.setConfirmBalance(anScheme.getApprovedBalance());
		this.saveModifyRequest(request, anScheme.getRequestNo());
		return schemeService.addScheme(anScheme);
	}

}
