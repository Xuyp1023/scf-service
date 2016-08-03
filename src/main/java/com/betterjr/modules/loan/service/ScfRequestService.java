package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestApproved;

@Service
public class ScfRequestService extends BaseService<ScfRequestMapper, ScfRequest> {

    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private CustAccountService custAccountService;
    @Autowired
    private ScfRequestApprovedService approvedService;

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
        BTAssert.notNull(selectByClassProperty(ScfRequest.class, map), "原始数据不存在");

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
     * 资方-无业务审批
     * 
     * @param anMap
     * @return
     */
    public ScfRequest nonBusiness(Map<String, Object> anMap) {
        return null;
    }

    /**
     * 资方-出具融资方案
     * 
     * @param anMap
     * @return
     */
    public ScfRequestApproved offerScheme(ScfRequestApproved anApproved) {
        anApproved.setCustAduit("0");
        approvedService.addApproved(anApproved);
        return anApproved;
    }

    /**
     * 融方-确认融资方案（确认融资金额，期限，利率，）
     * 
     * @param anMap
     * @return
     */
    public ScfRequestApproved confirmScheme(String anRequestNo, String anAduit) {
        ScfRequestApproved approved = approvedService.findApprovedDetail2(anRequestNo);
        BTAssert.notNull(approved);
        
        //修改融资企业确认状态
        approved.setCustAduit(anAduit);
        approvedService.saveModifyApproved(approved);
        return approved;
    }
   
    /**
     * 资方-发起贸易背景确认（发送转让协议通知书）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest webRequestTradingBackgrand(String anRequestNo) {
        ScfRequest request =  this.selectByPrimaryKey(anRequestNo);
       
        //将核心企业的审批状态改为"需要确认0"
        ScfRequestApproved approved = approvedService.findApprovedDetail2(anRequestNo);
        BTAssert.notNull(approved, "无审批记录！");
        approved.setCoreCustAduit("0");
        approvedService.saveModifyApproved(approved);
        
        // 1:票据;2:应收款;3:经销商
        if (BetterStringUtils.equals("3", request.getRequestType())) {
            //TODO 发送《三方协议》

        }else{
            //TODO 发送《应收账款转让确认书》 
        }
        
        
        return request;
    }

    /**
     * 核心企业--（确认贸易背景、签署应收账款转让协议）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest confirmTradingBackgrand(String anRequestNo, String anAduit) {
        ScfRequestApproved approved = approvedService.findApprovedDetail2(anRequestNo);
        BTAssert.notNull(approved);
        
        //修改核心企业确认状态
        approved.setCoreCustAduit(anAduit);
        approvedService.saveModifyApproved(approved);
        
        //修改融资申请中的核心企业确认状态
        ScfRequest request = new ScfRequest();
        request.setAduit(anAduit);
        request = saveModifyRequest(request, anRequestNo);
        
        //企业确认状态，0未确认，1已确认，2否决
        if(BetterStringUtils.equals("2", anAduit)){
            return findRequestDetail(anRequestNo);
        }
        
        //TODO 保存 应收账款转让协议 相关数据
        
        return findRequestDetail(anRequestNo);
    }
    
    /**
     * 资方-放款（收取放款手续费）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest loan(Map<String, Object> anMap) {
        return null;
    }

    /**
     * 资方-确认放款（计算利息，生成还款计划）
     * 
     * @param anMap
     * @return
     */
    public ScfRequest confirmLoan(Map<String, Object> anMap) {
        return null;
    }

}
