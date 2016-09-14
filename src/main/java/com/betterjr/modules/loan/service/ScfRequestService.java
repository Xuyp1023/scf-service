package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.entity.ScfServiceFee;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.workflow.data.CustFlowNodeData;

@Service
public class ScfRequestService extends BaseService<ScfRequestMapper, ScfRequest> {

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
    /**
     * 新增融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest addRequest(ScfRequest anRequest) {
        BTAssert.notNull(anRequest, "新增融资申请失败-anRequest不能为空");
        anRequest.init();
        anRequest.setCustName(custAccountService.queryCustName(anRequest.getCustNo()));
        anRequest.setRequestDate(BetterDateUtils.getDate("yyyyMMdd"));
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
            throw new IllegalArgumentException("修改融资申请失败-找不到原数据");
        }

        anRequest.initModify();
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
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
            scfRequest.setCustName(custAccountService.queryCustName(scfRequest.getCustNo()));
            ScfRequestScheme scheme = schemeService.findSchemeDetail2(scfRequest.getRequestNo());
            
            //融资方案上否确认
            if(null != scheme && BetterStringUtils.equals("1", scheme.getCustAduit())){
                scfRequest.setApprovedBalance(scheme.getApprovedBalance());
                scfRequest.setApprovedRatio(scheme.getApprovedRatio());
                scfRequest.setApprovedPeriod(scheme.getApprovedPeriod());
                scfRequest.setApprovedPeriodUnit(scheme.getApprovedPeriodUnit());
            }
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

        // 设置相关名称
        request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
        request.setFactorName(custAccountService.queryCustName(request.getFactorNo()));

        // 设置还款计划
        request.setPayPlan(payPlanService.findPayPlanByRequest(anRequestNo));
        return request;
    }

    /**
     * 资方-出具融资方案
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
     * @param anMap
     * @return
     */
    public ScfRequestScheme saveConfirmScheme(String anRequestNo, String anAduitStatus) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(scheme);

        // 修改融资企业确认状态
        scheme.setCustAduit(anAduitStatus);
        return schemeService.saveModifyScheme(scheme);
    }

    /**
     * 资方-发起贸易背景确认（发送转让协议通知书）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest saveRequestTradingBackgrand(String anRequestNo) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        ScfRequest request = this.selectByPrimaryKey(anRequestNo);
        scheme.setCoreCustAduit("0");
        schemeService.saveModifyScheme(scheme);
        return request;
    }

    /**
     * 核心企业--（确认贸易背景、签署应收账款转让协议）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest confirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        ScfRequestScheme scheme = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(scheme);

        // 修改核心企业确认状态
        scheme.setCoreCustAduit(anAduitStatus);
        schemeService.saveModifyScheme(scheme);
        return findRequestDetail(anRequestNo);
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

        ScfRequestScheme scheme = schemeService.findSchemeDetail2(request.getRequestNo());
        BTAssert.notNull(scheme, "确认放款失败-找不到融资方案");

        // ---修申请表---------------------------------------------
        request.setActualDate(anLoan.getLoanDate());
        request.setEndDate(anLoan.getEndDate());
        request.setConfirmBalance(anLoan.getLoanBalance());
        this.updateByPrimaryKey(request);

        // ---保存还款计划------------------------------------------
        ScfPayPlan plan = createPayPlan(anLoan, request, scheme);

        // ---保存手续费----------------------------------------------
        BigDecimal servicefeeBalance = new BigDecimal(0);
        if (null != anLoan.getServicefeeBalance() || null != scheme.getServicefeeRatio()) {
            servicefeeBalance = saveServiceFee(anLoan, request, scheme);
        }

        // ---保存放款记录------------------------------------------
        anLoan.setInterestBalance(plan.getShouldInterestBalance());
        anLoan.setManagementBalance(plan.getShouldManagementBalance());
        anLoan.setCustNo(request.getCustNo());
        anLoan.setServicefeeBalance(servicefeeBalance);
        anLoan.setFactorNo(request.getFactorNo());
        loanService.addLoan(anLoan);

        return request;
    }

    private BigDecimal saveServiceFee(ScfLoan anLoan, ScfRequest request, ScfRequestScheme scheme) {
        ScfServiceFee serviceFee = new ScfServiceFee();
        serviceFee.setCustNo(request.getCustNo());
        serviceFee.setFactorNo(request.getFactorNo());
        serviceFee.setRequestNo(anLoan.getRequestNo());
        serviceFee.setPayDate(anLoan.getLoanDate());

        BigDecimal servicefeeBalance;
        if (null == anLoan.getServicefeeBalance()) {
            // 计算手续费(按千分之收)
            servicefeeBalance = scheme.getApprovedBalance().multiply(scheme.getServicefeeRatio()).divide(new BigDecimal(1000));
        }
        else {
            servicefeeBalance = anLoan.getServicefeeBalance();
        }
        serviceFee.setBalance(servicefeeBalance);
        serviceFeeService.addServiceFee(serviceFee);
        return servicefeeBalance;
    }

    private ScfPayPlan createPayPlan(ScfLoan anLoan, ScfRequest request, ScfRequestScheme scheme) {
        ScfPayPlan plan = new ScfPayPlan();
        plan.setTerm(1);
        plan.setCustNo(request.getCustNo());
        plan.setCoreCustNo(request.getCoreCustNo());
        plan.setFactorNo(request.getFactorNo());
        plan.setRequestNo(anLoan.getRequestNo());
        plan.setStartDate(anLoan.getLoanDate());
        plan.setPlanDate(anLoan.getEndDate());
        plan.setRatio(scheme.getApprovedRatio());
        plan.setManagementRatio(scheme.getApprovedManagementRatio());
        
        //计算应还
        plan.setShouldPrincipalBalance(anLoan.getLoanBalance());
        if (null == anLoan.getInterestBalance()) {
            plan.setShouldInterestBalance(payPlanService.calculatFee(anLoan.getRequestNo(), scheme.getApprovedBalance(), 3));
        }
        else {
            plan.setShouldInterestBalance(anLoan.getInterestBalance());
        }
        
        if(null == anLoan.getManagementBalance()){
            plan.setShouldManagementBalance(payPlanService.calculatFee(anLoan.getRequestNo(), scheme.getApprovedBalance(), 1));
        }else{
            plan.setShouldManagementBalance(anLoan.getManagementBalance());
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
        for (ScfRequest scfRequest : page) {
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
            // 设置还款计划
            scfRequest.setPayPlan(payPlanService.findPayPlanByRequest(scfRequest.getRequestNo()));
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
        for (ScfRequest scfRequest : page) {
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
            // 设置还款计划
            scfRequest.setPayPlan(payPlanService.findPayPlanByRequest(scfRequest.getRequestNo()));
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
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
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
     */
    public Page<ScfRequest> queryCoreEnterpriseRequest(Map<String, Object> anMap, String anRequestType, String anFlag, int anPageNum, int anPageSize) {
        switch (anRequestType) {
        case "1":
            // 未还款
            return this.queryPendingRequest(anMap, anFlag, anPageNum, anPageSize);
        case "2":
            // 还款中
            return this.queryRepaymentRequest(anMap, anFlag, anPageNum, anPageSize);
        case "3":
            // 已还款
            return this.queryCompletedRequest(anMap, anFlag, anPageNum, anPageSize);
        default:
            return (Page<ScfRequest>) Collections3.union(queryPendingRequest(anMap, anFlag, anPageNum, anPageSize), Collections3.union(queryRepaymentRequest(anMap, anFlag, anPageNum, anPageSize),queryCompletedRequest(anMap, anFlag, anPageNum, anPageSize)));
        }
    }
    
    public ScfRequest approveRequest(Map<String, Object> anMap) {
        return null;
    }

    public List<ScfRequestCredit> getCreditList(ScfRequest request){
        List<CustAgreement> agreementList = (List)orderService.findRelationInfo(request.getRequestNo(), ScfOrderRelationType.AGGREMENT);
        CustAgreement agreement = Collections3.getFirst(agreementList);
        BTAssert.notNull(agreement, "发起融资背景确认失败：没有找到贸易合同！");
        
        String type = request.getRequestType();
        List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
        
        if(BetterStringUtils.equals("1", type) || BetterStringUtils.equals("4", type)){
            List<ScfOrder> list = (List)orderService.findInfoListByRequest(request.getRequestNo(), "1");
            for (ScfOrder order : list) {
                creditList = setInvoice(request, order.getInvoiceList(), order.getBalance(), order.getOrderNo(),agreement);
            }
        }else if(BetterStringUtils.equals("2", type)){
            List<ScfReceivable> list = (List)orderService.findInfoListByRequest(request.getRequestNo(), "2");
            for (ScfReceivable receivable : list) {
                creditList = setInvoice(request, receivable.getInvoiceList(), receivable.getBalance(), receivable.getReceivableNo() ,agreement);
            }
        }else if(BetterStringUtils.equals("3", type)){
            List<ScfAcceptBill> list = (List)orderService.findInfoListByRequest(request.getRequestNo(), "3");
            for (ScfAcceptBill bill : list) {
                creditList = setInvoice(request, bill.getInvoiceList(), bill.getBalance(), bill.getBtBillNo(),agreement);
            }
        }
        return creditList;
    }
    
    /**
     * 设置转主明细相关信息
     * @param request
     * @param invoiceList
     * @param balance
     * @param ObjectNo
     * @param agreement
     * @return
     */
    private List<ScfRequestCredit> setInvoice(ScfRequest request, List<ScfInvoice> invoiceList, BigDecimal balance, String ObjectNo, CustAgreement agreement){
        List<ScfRequestCredit> creditList = new ArrayList<ScfRequestCredit>();
        for (ScfInvoice invoice : invoiceList) {
            ScfRequestCredit credit = new ScfRequestCredit();
            credit.setTransNo(ObjectNo);
            credit.setRequestNo(request.getRequestNo());
            credit.setBalance(balance);
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
     * @param request
     * @return
     */
    public ScfRequestNotice getNotice(ScfRequest request) {
        //TODO 合同名称、 银行账号， 保理公司详细地址
        String noticeNo = BetterDateUtils.getDate("yyyyMMdd") + request.getRequestNo();
        ScfRequestNotice noticeRequest = new ScfRequestNotice();
        noticeRequest.setRequestNo(request.getRequestNo());
        noticeRequest.setAgreeName(request.getCustName() + "应收账款转让申请书");
        noticeRequest.setNoticeNo(noticeNo);
        noticeRequest.setBuyer(request.getFactorName());
        noticeRequest.setFactorRequestNo(noticeNo);
        noticeRequest.setBankAccount("6229887842567285");
        noticeRequest.setFactorAddr("保理公司详细地址");
        noticeRequest.setFactorPost("518100");
        noticeRequest.setFactorLinkMan("保理公司联系人姓名");
        return noticeRequest;
    }

    /**
     * 设置三方协议相关信息
     * @return
     */
    public ScfRequestProtacal getProtacal(ScfRequest request) {
        ScfRequestProtacal protacal = new ScfRequestProtacal();
        protacal.setFirstAddress("地址目前还没有");
        protacal.setFirstFax("fax目前还没有");
        protacal.setFirstName("甲方名称目前没有");
        protacal.setFirstJob("职务目前没有");
        protacal.setFirstPhone("电话目前没有");
        protacal.setFirstLegal("负责人目前没有");
        
        protacal.setSecondAddress("地址目前还没有");
        protacal.setSecondFax("fax目前还没有");
        protacal.setSecondName("甲方名称目前没有");
        protacal.setSecondJob("职务目前没有");
        protacal.setSecondPhone("电话目前没有");
        protacal.setSecondLegal("负责人目前没有");
        
        protacal.setThreeAddress("地址目前还没有");
        protacal.setThreeFax("fax目前还没有");
        protacal.setThreeName("甲方名称目前没有");
        protacal.setThreeJob("职务目前没有");
        protacal.setThreePhone("电话目前没有");
        protacal.setThreeLegal("负责人目前没有");
        return protacal;
    }

    /**
     * 设置转让意见确认书相关信息
     * @param request
     * @return
     */
    public ScfRequestOpinion getOption(ScfRequest request) {
        String noticeNo = BetterDateUtils.getDate("yyyyMMdd") + request.getRequestNo();
        ScfRequestOpinion opinion = new ScfRequestOpinion();
        opinion.setRequestNo(request.getRequestNo());
        opinion.setFactorRequestNo(noticeNo);
        opinion.setConfirmNo(noticeNo);
        opinion.setSupplier(request.getCustName());
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
    }
    
    /**
     * 查询融资列表，无分页
     */
    public List<ScfRequest> findRequestList(Map<String, Object> anMap) {
        List<ScfRequest> requestList = this.selectByProperty(anMap);
        for(ScfRequest scfRequest : requestList) {
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
            scfRequest.setCustName(custAccountService.queryCustName(scfRequest.getCustNo()));
            ScfRequestScheme scheme = schemeService.findSchemeDetail2(scfRequest.getRequestNo());
            //融资方案上否确认
            if(null != scheme && BetterStringUtils.equals("1", scheme.getCustAduit())){
                scfRequest.setApprovedBalance(scheme.getApprovedBalance());
                scfRequest.setApprovedRatio(scheme.getApprovedRatio());
                scfRequest.setApprovedPeriod(scheme.getApprovedPeriod());
                scfRequest.setApprovedPeriodUnit(scheme.getApprovedPeriodUnit());
            }
        }
        return requestList;
    }

}
