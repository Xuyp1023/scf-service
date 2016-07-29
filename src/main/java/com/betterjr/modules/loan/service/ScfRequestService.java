package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfRequest;

@Service
public class ScfRequestService extends BaseService<ScfRequestMapper, ScfRequest> {
   
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private CustAccountService custAccountService;
    
    /**
     * 新增融资申请
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
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfRequest> queryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfRequest> list = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
        for (ScfRequest scfRequest : list) {
            scfRequest.setCoreCustName(custAccountService.queryCustName(scfRequest.getCoreCustNo()));
            scfRequest.setFactorName(custAccountService.queryCustName(scfRequest.getFactorNo()));
        }
        return list;
     }

     /**
      * 查询融资申详情
      * @param requestNo
      * @return
      */
     public ScfRequest findRequestDetail(String requestNo){
         BTAssert.notNull(requestNo, "requestNo不能为空");
         ScfRequest request = this.selectByPrimaryKey(requestNo);
         if(null == request){
             logger.debug("没查到相关数据！");
             return new ScfRequest();
         }
         
         //设置相关名称
         request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
         request.setFactorName(custAccountService.queryCustName(request.getFactorNo()));
         
         //设置还款计划
         Map<String, Object> anPropValue =new HashMap<String, Object>();
         anPropValue.put("requestNo", requestNo);
         request.setPayPlan(payPlanService.findPayPlanByProperty(anPropValue ));
         return request;
     }
     
     public ScfRequest approveRequest(Map<String, Object> anMap){
         String tradeStatus = anMap.get("tradeStatus").toString();
         return null;
     }
     
     /**
      * 资方-无业务审批
      * @param anMap
      * @return
      */
     public ScfRequest nonBusiness(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-提供融资方案
      * @param anMap
      * @return
      */
     public ScfRequest offerScheme(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-确认融资方案（确认融资金额，期限，利率，）
      * @param anMap
      * @return
      */
     public ScfRequest confirmScheme(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-签约（发送融资协议）
      * @param anMap
      * @return
      */
     public ScfRequest signContract(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-确认签约（确认融资协议）
      * @param anMap
      * @return
      */
     public ScfRequest confirmContract(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-放款（收取放款手续费）
      * @param anMap
      * @return
      */
     public ScfRequest loan(Map<String, Object> anMap){
         return null;
     }
     
     /**
      * 资方-确认放款（计算利息，生成还款计划）
      * @param anMap
      * @return
      */
     public ScfRequest confirmloan(Map<String, Object> anMap){
         return null;
     }

}
