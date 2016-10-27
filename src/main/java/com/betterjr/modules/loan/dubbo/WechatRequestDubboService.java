package com.betterjr.modules.loan.dubbo;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.IScfWechatRequestService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.service.WechatRequestService;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.workflow.IFlowService;
import com.betterjr.modules.workflow.data.FlowCommand;
import com.betterjr.modules.workflow.data.FlowInput;
import com.betterjr.modules.workflow.data.FlowStatus;
import com.betterjr.modules.workflow.data.FlowType;

@Service(interfaceClass = IScfWechatRequestService.class)
public class WechatRequestDubboService implements IScfWechatRequestService {
    protected final Logger logger = LoggerFactory.getLogger(IScfWechatRequestService.class);
    
    @Autowired
    private WechatRequestService wechatRequestService;
    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private CustAccountService custAccountService;
    @Reference(interfaceClass = IFlowService.class)
    private IFlowService flowService;

    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("微信版-新增票据融资申请，入参：" + anMap);
        ScfRequest request = (ScfRequest) RuleServiceDubboFilterInvoker.getInputObj();
        request = wechatRequestService.saveRequest(request);
        return AjaxObject.newOk(this.webFindRequestByNo(request.getRequestNo())).toJson();
    }

    @Override
    public String webQueryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询票据融资申请，入参：" + anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        
        if(UserUtils.supplierUser() || UserUtils.sellerUser()){
            anMap.put("custNo", UserUtils.getDefCustInfo().getCustNo());
        }
        else if(UserUtils.factorUser()){
            anMap.put("factorNo", UserUtils.getDefCustInfo().getCustNo());
        }
        
        return AjaxObject.newOkWithPage("分页查询融资申请成功",
                wechatRequestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webRequestByOffer(String anOfferId) {
        logger.debug("获取票据申请数据，入参：" + anOfferId);
        return AjaxObject.newOk(offerService.selectByPrimaryKey(Long.parseLong(anOfferId))).toJson();
    }
    
    /**
     * 启动流程
     * @param anRequestNo 申请编号
     * @return
     */
    public String startFlow(String anRequestNo){
        // 启动流程
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(request.getRequestNo()));
        input.setMoney(request.getBalance());
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setFinancerOperOrg(UserUtils.getOperatorInfo().getOperOrg());
        input.setType(FlowType.Trade);
        
        CustInfo coreCustInfo = custAccountService.selectByPrimaryKey(request.getCoreCustNo());
        input.setCoreOperOrg(coreCustInfo.getOperOrg());
        flowService.start(input);
        return anRequestNo;
    }
    
    /**
     * 执行流程
     * @param anRequestNo 申请编号
     * @return
     */
    public String execFlow(String anRequestNo){
        // 启动流程
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(request.getRequestNo()));
        input.setMoney(request.getBalance());
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setFinancerOperOrg(UserUtils.getOperatorInfo().getOperOrg());
        input.setType(FlowType.Trade);
        return anRequestNo;
    }
    
    /**
     * 执行流程
     * 
     * @param anRequestNo 申请编号
     * @param money       审批金额
     * @param anDescription      备注
     */
    public void execFlow(String anRequestNo, BigDecimal money, String anDescription) {
        FlowInput input = new FlowInput();
        input.setBusinessId(Long.parseLong(anRequestNo));
        input.setMoney(money);
        input.setOperator(UserUtils.getPrincipal().getId().toString());
        input.setReason(anDescription);
        // 执行流程
        flowService.exec(input);
    }
    
    @Override
    public String updateRequestTradeStauts(String anRequestNo, String anTradeStatus){
        logger.debug("修改票据申请的融资状态，入参：anTradeStatus=" + anTradeStatus);
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        request.setTradeStatus(anTradeStatus);
        return AjaxObject.newOk(requestService.saveModifyRequest(request, anRequestNo)).toJson();
    }
    
    @Override
    public String webFindRequestByNo(String anRequestNo){
        logger.debug("根据申请编号查询融资申请，入参：anRequestNo=" + anRequestNo);
        return AjaxObject.newOk(wechatRequestService.findRequestByNo(anRequestNo)).toJson();
    }
    
    @Override
    public String webFindRequestByBill(String anBillId){
        logger.debug("根据票据id查询融资申请，入参：anBillId=" + anBillId);
        return AjaxObject.newOk(wechatRequestService.findRequestByBill(anBillId)).toJson();
    }


    
}
