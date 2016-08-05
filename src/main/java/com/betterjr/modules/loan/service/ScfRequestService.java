package com.betterjr.modules.loan.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfLoan;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.entity.ScfServiceFee;

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

    /**
     * 新增融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest addRequest(ScfRequest anRequest) {
        BTAssert.notNull(anRequest, "anRequest不能为空");
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
        BTAssert.notNull(anRequest, "anRequest不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anRequest.getCustNo());
        map.put("requestNo", anRequestNo);
        if (Collections3.isEmpty(selectByClassProperty(ScfRequest.class, map))) {
            throw new IllegalArgumentException("找不到原数据");
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
        BTAssert.notNull(anRequestNo, "requestNo不能为空");
        ScfRequest request = this.selectByPrimaryKey(anRequestNo);
        if (null == request) {
            logger.debug("没查到相关数据！");
            return new ScfRequest();
        }

        // 设置相关名称
        request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
        request.setFactorName(custAccountService.queryCustName(request.getFactorNo()));

        // 设置还款计划
        Map<String, Object> propValue = new HashMap<String, Object>();
        propValue.put("requestNo", anRequestNo);
        request.setPayPlan(payPlanService.findPayPlanByProperty(propValue));
        return request;
    }

    public ScfRequest approveRequest(Map<String, Object> anMap) {
        return null;
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
        ScfRequest request = this.selectByPrimaryKey(anRequestNo);

        // 将核心企业的审批状态改为"需要确认0"
        ScfRequestScheme approved = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(approved, "无审批记录！");
        approved.setCoreCustAduit("0");
        schemeService.saveModifyScheme(approved);

        // 1:票据;2:应收款;3:经销商
        if (BetterStringUtils.equals("3", request.getRequestType())) {
            // TODO 发送《三方协议》

        }
        else {
            // TODO 发送《应收账款转让确认书》
        }

        return request;
    }

    /**
     * 核心企业--（确认贸易背景、签署应收账款转让协议）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest confirmTradingBackgrand(String anRequestNo, String anAduitStatus) {
        ScfRequestScheme approved = schemeService.findSchemeDetail2(anRequestNo);
        BTAssert.notNull(approved);

        // 修改核心企业确认状态
        approved.setCoreCustAduit(anAduitStatus);
        schemeService.saveModifyScheme(approved);

        // 修改融资申请中的核心企业确认状态
        ScfRequest request = new ScfRequest();
        request.setAduit(anAduitStatus);
        request = saveModifyRequest(request, anRequestNo);

        // 企业确认状态，0未确认，1已确认，2否决
        if (BetterStringUtils.equals("2", anAduitStatus)) {
            return findRequestDetail(anRequestNo);
        }

        // TODO 保存 应收账款转让协议 相关数据

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
        BTAssert.notNull(request, "找不到融资申请单");

        ScfRequestScheme scheme = schemeService.findSchemeDetail2(request.getRequestNo());
        BTAssert.notNull(scheme, "找不到融资方案");

        // ---修申请表---------------------------------------------
        request.setActualDate(anLoan.getLoanDate());
        request.setEndDate(anLoan.getEndDate());
        request.setConfirmBalance(anLoan.getLoanBalance());
        this.updateByPrimaryKey(request);

        // TODO 如果是经销商融资 放的金额是减掉了保证金吗？
        // ---保存还款计划------------------------------------------
        ScfPayPlan plan = new ScfPayPlan();
        plan.setCustNo(request.getCustNo());
        plan.setCoreCustNo(request.getCoreCustNo());
        plan.setFactorNo(request.getFactorNo());
        plan.setRequestNo(anLoan.getRequestNo());
        plan.setPlanDate(anLoan.getEndDate());
        plan.setShouldPrincipalBalance(anLoan.getLoanBalance());
        if (null == anLoan.getInterestBalance()) {
            plan.setShouldInterestBalance(payPlanService.calculatFee(anLoan.getRequestNo(), scheme.getApprovedBalance(), 3));
            plan.setShouldManagementBalance(payPlanService.calculatFee(anLoan.getRequestNo(), scheme.getApprovedBalance(), 1));
        }
        else {
            plan.setShouldInterestBalance(anLoan.getInterestBalance());
            plan.setShouldManagementBalance(anLoan.getManagementBalance());
        }
        payPlanService.addPayPlan(plan);

        // ---保存手续费----------------------------------------------
        BigDecimal servicefeeBalance = new BigDecimal(0);
        if (null != anLoan.getServicefeeBalance() || null != scheme.getServicefeeRatio()) {
            ScfServiceFee serviceFee = new ScfServiceFee();
            serviceFee.setCustNo(request.getCustNo());
            serviceFee.setFactorNo(request.getFactorNo());
            serviceFee.setRequestNo(anLoan.getRequestNo());
            serviceFee.setPayDate(anLoan.getLoanDate());

            if (null == anLoan.getServicefeeBalance()) {
                // 计算手续费(按千分之收)
                servicefeeBalance = scheme.getApprovedBalance().multiply(scheme.getServicefeeRatio()).divide(new BigDecimal(1000));
            }
            else {
                servicefeeBalance = anLoan.getServicefeeBalance();
            }
            serviceFee.setBalance(servicefeeBalance);
            serviceFeeService.addServiceFee(serviceFee);
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

}
