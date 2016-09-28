package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.alibaba.dubbo.config.annotation.Reference;

import com.betterjr.common.exception.BytterTradeException;


import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterWebServiceException;


import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
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

import com.betterjr.modules.loan.helper.ScfFactorRemoteHelper;

import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
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
    private ScfOfferService offerService;
    @Autowired
    private ScfEnquiryService enquiryService;
    
    @Reference(interfaceClass = ICustMechLawService.class)
    private ICustMechLawService mechLawService;
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService mechBaseService;
    @Reference(interfaceClass = ICustMechBankAccountService.class)
    private ICustMechBankAccountService mechBankAccountService;

    private ScfFactorRemoteHelper factorRemoteHelper=new ScfFactorRemoteHelper();

    
    public ScfRequest saveStartRequest(ScfRequest anRequest){
        anRequest.setRequestFrom("1");
        anRequest = this.addRequest(anRequest);
        
        if(null != anRequest.getOfferId()){
            //从报价过来的要改变报价状态
            offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "3");
            
            ScfOffer offer = offerService.selectByPrimaryKey(anRequest.getOfferId());
            //从报价过来的要改变报价状态
            enquiryService.saveUpdateBusinStatus(offer.getEnquiryNo(), "-2");
        }

        // 关联订单
        orderService.saveInfoRequestNo(anRequest.getRequestType(), anRequest.getRequestNo(), anRequest.getOrders());
        // 冻结订单
        orderService.forzenInfos(anRequest.getRequestNo(), null);
        
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
        Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
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
        BTAssert.notNull(anRequestNo, " 查询融资申详情失败-requestNo不能为空");
        ScfRequest request = this.selectByPrimaryKey(anRequestNo);
        if (null == request) {
            logger.debug("没查到相关数据！");
            return new ScfRequest();
        }

        this.fillCustName(request);

        // 设置还款计划
        request.setPayPlan(payPlanService.findPayPlanByRequest(anRequestNo));
        
        return request;
    }

    private void fillCustName(ScfRequest request) {
        // 设置相关名称
        request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
        request.setFactorName(custAccountService.queryCustName(request.getFactorNo()));
        ScfProduct product = productService.findProductById(request.getProductId());
        if(null != product){
            request.setProductName(product.getProductName());  
        }
    }

    /**
     * 资方-出具融资方案（生成应收账款转让通知书）
     * 
     * @param anMap
     * @return
     */
    public ScfRequestScheme saveOfferScheme(ScfRequestScheme anScheme) {
        return schemeService.addScheme(anScheme);
    }

    /**
     * 融方-确认融资方案（确认融资金额，期限，利率，）
     * 
     * @param  anRequestNo
     * @param  anAduitStatus
     * @return
     */
    public ScfRequestScheme saveConfirmScheme(String anRequestNo, String anAduitStatus) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(scheme);
        
        // 将融资确认后的融资信息放入到申请表中（修改申请表中的信息）
        ScfRequest request = this.selectByPrimaryKey(anRequestNo);
        request.setApprovedPeriod(scheme.getApprovedPeriod());
        request.setApprovedPeriodUnit(scheme.getApprovedPeriodUnit());
        request.setManagementRatio(scheme.getApprovedManagementRatio());
        request.setApprovedRatio(scheme.getApprovedRatio());
        request.setServicefeeRatio(scheme.getServicefeeRatio());
        request.setApprovedBalance(scheme.getApprovedBalance());
        request.setConfirmBalance(scheme.getApprovedBalance());
        this.saveModifyRequest(request, anRequestNo);
        
        // 修改融资企业确认状态
        scheme.setCustAduit(anAduitStatus);
        return schemeService.saveModifyScheme(scheme);
    }

    /**
     * 资方-发起贸易背景确认（生成应收账款转确认意见书）
     * @param anMap
     * @return
     */
    public ScfRequestScheme saveRequestTradingBackgrand(String anRequestNo) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        scheme.setCoreCustAduit("0");
        return schemeService.saveModifyScheme(scheme);
    }
    
    /**
     * 核心企业
     * 
     * @param anMap
     * @return
     */
    public ScfRequestScheme confirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(scheme);

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

        // ---保存手续费----------------------------------------------
        BigDecimal servicefeeBalance = new BigDecimal(0);
        servicefeeBalance = saveServiceFee(anLoan.getServicefeeBalance(), request);

        // ---保存放款记录------------------------------------------
        anLoan.setInterestBalance(plan.getShouldInterestBalance());
        anLoan.setManagementBalance(plan.getShouldManagementBalance());
        anLoan.setCustNo(request.getCustNo());
        anLoan.setServicefeeBalance(servicefeeBalance);
        anLoan.setFactorNo(request.getFactorNo());
        loanService.addLoan(anLoan);

        return request;
    }

    private BigDecimal saveServiceFee(BigDecimal servicefeeBalance, ScfRequest anRequest) {
        ScfServiceFee serviceFee = new ScfServiceFee();
        serviceFee.setCustNo(anRequest.getCustNo());
        serviceFee.setFactorNo(anRequest.getFactorNo());
        serviceFee.setRequestNo(anRequest.getRequestNo());
        serviceFee.setPayDate(anRequest.getActualDate());

        if (null == servicefeeBalance) {
            // 计算手续费(按千分之收)
            servicefeeBalance = anRequest.getApprovedBalance().multiply(anRequest.getServicefeeRatio()).divide(new BigDecimal(1000));
        }
        
        if(servicefeeBalance.compareTo(BigDecimal.ZERO) < 1){
            return BigDecimal.ZERO;
        }
        
        serviceFee.setBalance(servicefeeBalance);
        serviceFeeService.addServiceFee(serviceFee);
        return servicefeeBalance;
    }

    private ScfPayPlan createPayPlan(BigDecimal anInterestBalance, BigDecimal anManagementBalance, ScfRequest anRequest) {
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
        
        //计算应还
        plan.setShouldPrincipalBalance(anRequest.getLoanBalance());
        if (null == anInterestBalance) {
            plan.setShouldInterestBalance(payPlanService.getFee(anRequest.getRequestNo(), anRequest.getLoanBalance(), 3));
        }
        else {
            plan.setShouldInterestBalance(anInterestBalance);
        }
        
        if(null == anManagementBalance){
            plan.setShouldManagementBalance(payPlanService.getFee(anRequest.getRequestNo(), anRequest.getLoanBalance(), 1));
        }else{
            plan.setShouldManagementBalance(anManagementBalance);
        }
        
        //未还
        plan.setSurplusPrincipalBalance(plan.getShouldPrincipalBalance());
        plan.setSurplusInterestBalance(plan.getShouldInterestBalance());
        plan.setSurplusManagementBalance(plan.getShouldManagementBalance());
        
        //计算应还未还总额
        BigDecimal totalBalance = plan.getShouldPrincipalBalance().add(plan.getShouldInterestBalance()).add(plan.getShouldManagementBalance());
        plan.setShouldTotalBalance(totalBalance);
        plan.setSurplusTotalBalance(totalBalance);
        payPlanService.addPayPlan(plan);
        return plan;
    }
    
    public List<ScfRequest> queryTodoList(){
        
        return null;
    }
    
    /**
     * 查询待批融资   <=150
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     */
    public Page<ScfRequest> queryPendingRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
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
     * 查询还款融资      >150  <190
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     */
    public Page<ScfRequest> queryRepaymentRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
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
     * 查询历史融资  >=190
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     */
    public Page<ScfRequest> queryCompletedRequest(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
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
     * 核心企业融资查询
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     * anBusinStatus - 1：未放款，2：还款中  3.已还款
     */
    public Page<ScfRequest> queryCoreEnterpriseRequest(Map<String, Object> anMap, String anBusinStatus, String anFlag, int anPageNum, int anPageSize) {
        if (BetterStringUtils.isBlank(anBusinStatus)) {
            Page<ScfRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
            for (ScfRequest request : page) {
                fillCustName(request);
                // 设置还款计划
                request.setPayPlan(payPlanService.findPayPlanByRequest(request.getRequestNo()));
            }
            return page;
        }
        else {
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

    public List<ScfRequestCredit> getCreditList(ScfRequest anRequest){
        List<CustAgreement> agreementList = (List)orderService.findRelationInfo(anRequest.getRequestNo(), ScfOrderRelationType.AGGREMENT);
        CustAgreement agreement = Collections3.getFirst(agreementList);
        BTAssert.notNull(agreement, "发起融资背景确认失败：没有找到贸易合同！");
        
        List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
        String type = anRequest.getRequestType();
        if(BetterStringUtils.equals(RequestType.ORDER.getCode(), type) || BetterStringUtils.equals(RequestType.SELLER.getCode(), type)){
            List<ScfOrder> list = (List)orderService.findInfoListByRequest(anRequest.getRequestNo(), RequestType.ORDER.getCode());
            for (ScfOrder order : list) {
                creditList = setInvoice(anRequest, order.getInvoiceList(), order.getBalance(), order.getOrderNo(),agreement);
            }
        }else if(BetterStringUtils.equals(RequestType.BILL.getCode(), type)){
            List<ScfAcceptBill> list = (List)orderService.findInfoListByRequest(anRequest.getRequestNo(), RequestType.BILL.getCode());
            for (ScfAcceptBill bill : list) {
                creditList = setInvoice(anRequest, bill.getInvoiceList(), bill.getBalance(), bill.getBtBillNo() ,agreement);
            }
        }else if(BetterStringUtils.equals(RequestType.RECEIVABLE.getCode(), type)){
            List<ScfReceivable> list = (List)orderService.findInfoListByRequest(anRequest.getRequestNo(), RequestType.RECEIVABLE.getCode());
            for (ScfReceivable receivable : list) {
                creditList = setInvoice(anRequest, receivable.getInvoiceList(), receivable.getBalance(), receivable.getReceivableNo(), agreement);
            }
        }
        return creditList;
    }
    
    /**
     * 设置转主明细相关信息
     * @param request
     * @param anInvoiceList
     * @param anBalance
     * @param anObjectNo
     * @param anAgreement
     * @return
     */
    private List<ScfRequestCredit> setInvoice(ScfRequest request, List<ScfInvoice> anInvoiceList, BigDecimal anBalance, String anObjectNo, CustAgreement anAgreement){
        List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
        for (ScfInvoice invoice : anInvoiceList) {
            ScfRequestCredit credit = new ScfRequestCredit();
            credit.setTransNo(anObjectNo);
            credit.setRequestNo(request.getRequestNo());
            credit.setBalance(anBalance);
            credit.setInvoiceNo(invoice.getInvoiceNo());
            credit.setInvoiceBalance(invoice.getBalance());
            credit.setEndDate(invoice.getInvoiceDate());
            creditList.add(credit);
        }
        return creditList;
    }
    
    /**
     * 设置转让通知书相关信息
     * @param anRequestNo
     * @param anRequest
     * @return
     */
    public ScfRequestNotice getNotice(ScfRequest anRequest) {
        //TODO 合同名称、 银行账号， 保理公司详细地址
        CustMechBankAccount bankAccount = mechBankAccountService.findDefaultBankAccount(anRequest.getFactorNo());
        
        String noticeNo = BetterDateUtils.getDate("yyyyMMdd") + anRequest.getRequestNo();
        ScfRequestNotice noticeRequest = new ScfRequestNotice();
        noticeRequest.setRequestNo(anRequest.getRequestNo());
        noticeRequest.setAgreeName(anRequest.getCustName() + "应收账款转让申请书");
        noticeRequest.setNoticeNo(noticeNo);
        noticeRequest.setBuyer(anRequest.getCoreCustName());
        noticeRequest.setFactorRequestNo(noticeNo);
        noticeRequest.setBankAccount(bankAccount.getBankAcco());
        CustMechBase custBase = mechBaseService.findBaseInfo(anRequest.getCustNo());
        noticeRequest.setFactorAddr(custBase.getAddress());
        noticeRequest.setFactorPost(custBase.getZipCode());
        
        CustMechLaw custMechLaw = mechLawService.findLawInfo(anRequest.getCustNo());
        noticeRequest.setFactorLinkMan(custMechLaw.getName());
        return noticeRequest;
    }

    /**
     * TODO 设置三方协议相关信息
     * @return
     */
    public ScfRequestProtacal getProtacal(ScfRequest anRequest) {
        //负责人默认为法人
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
        protacal.setFirstName(anRequest.getFactorName());
        protacal.setFirstJob(firstJob);
        protacal.setFirstPhone(factorBase.getMobile());
        protacal.setFirstLegal(factorLow.getCustName());
        
        protacal.setSecondAddress(buyBase.getAddress());
        protacal.setSecondFax(buyBase.getFax());
        protacal.setSecondName(anRequest.getCoreCustName());
        protacal.setSecondJob(firstJob);
        protacal.setSecondPhone(buyBase.getMobile());
        protacal.setSecondLegal(buyLow.getCustName());
        
        protacal.setThreeAddress(sellerBase.getAddress());
        protacal.setThreeFax(sellerBase.getFax());
        protacal.setThreeName(anRequest.getCustName());
        protacal.setThreeJob(firstJob);
        protacal.setThreePhone(sellerBase.getMobile());
        protacal.setThreeLegal(sellerLow.getCustName());
        protacal.setRequestNo(anRequest.getRequestNo());
        
        List<CustAgreement> list = orderService.findRelationInfo(anRequest.getRequestNo(), ScfOrderRelationType.AGGREMENT);
        protacal.setProtocalNo(Collections3.getFirst(list).getAgreeNo());
        return protacal;
    }

    /**
     * 设置转让意见确认书相关信息
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
        node.setNodeCustomName(RequestTradeStatus.PAYFINSH.getName());
        node.setSysNodeName(RequestTradeStatus.PAYFINSH.getName());
        node.setSysNodeId(new Long(RequestTradeStatus.PAYFINSH.getCode()));
        node.setId(new Long(RequestTradeStatus.PAYFINSH.getCode()));
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
     * @param anScfRequest
     */
    public Object fillOrderInfo(ScfRequest anScfRequest) {
        //经销商融资不用设置
        if(BetterStringUtils.equals(RequestType.SELLER.getCode(), anScfRequest.getRequestType())){
            return null;
        }
        
        if(BetterStringUtils.equals(RequestType.ORDER.getCode(), anScfRequest.getRequestType())){
            List<ScfOrder> orderList = (List)orderService.findInfoListByRequest(anScfRequest.getRequestNo(), anScfRequest.getRequestType());
            return Collections3.getFirst(orderList);
        }
        else if(BetterStringUtils.equals(RequestType.BILL.getCode(), anScfRequest.getRequestType())){
            List<ScfAcceptBill> orderList = (List)orderService.findInfoListByRequest(anScfRequest.getRequestNo(), anScfRequest.getRequestType());
            return Collections3.getFirst(orderList);
        }else{
            List<ScfReceivable> orderList = (List)orderService.findInfoListByRequest(anScfRequest.getRequestNo(), anScfRequest.getRequestType());
            return Collections3.getFirst(orderList);
        }
       
    }

    /**
     * 查询融资列表，无分页
     */
    public List<ScfRequest> findRequestList(Map<String, Object> anMap) {
        List<ScfRequest> requestList = this.selectByProperty(anMap);
        for(ScfRequest request: requestList) {
            fillCustName(request);
        }
        return requestList;
    }


    /**
     * anBusinStatus - 1：未放款，2：还款中  3.已还款
     */
    public Page querySupplierRequestByCore(Map<String, Object> anQueryConditionMap, String anBusinStatus, String anFlag, int anPageNum,
            int anPageSize) {
        anQueryConditionMap.put("requestType", new String[]{RequestType.ORDER.getCode(), RequestType.BILL.getCode(), RequestType.RECEIVABLE.getCode()});
        return this.queryCoreEnterpriseRequest(anQueryConditionMap, anBusinStatus, anFlag, anPageNum, anPageSize);
    }

    /**
     * anBusinStatus - 1：未放款，2：还款中  3.已还款
     */
    public Page querySellerRequestByCore(Map<String, Object> anQueryConditionMap, String anBusinStatus, String anFlag, int anPageNum,
            int anPageSize) {
        anQueryConditionMap.put("requestType", new String[]{RequestType.SELLER.getCode()});
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
        }
        else {
            logger.warn("sendRequestStatus method not find requestNo " + anRequestNo);
        }

    }
}
