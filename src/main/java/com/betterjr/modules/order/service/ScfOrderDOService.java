package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.dao.ScfOrderDOMapper;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfOrderDOService extends BaseVersionService<ScfOrderDOMapper, ScfOrderDO> {

    
    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    /**
     * 
     * @param anOrder  订单保存对象
     * @param anFileList 附件列表
     * @param anOtherFileList
     * @param confirmFlag  是否需要确认 true 为新增确认    false 为新增
     * @return
     */
     public ScfOrderDO addOrder(ScfOrderDO anOrder, String anFileList,boolean anConfirmFlag) {
         
         BTAssert.notNull(anOrder,"插入订单为空,操作失败");
         logger.info("Begin to add order"+UserUtils.getOperatorInfo().getName());
         anOrder.initAddValue(UserUtils.getOperatorInfo(),anConfirmFlag);
         // 操作机构设置为供应商
         anOrder.setOperOrg(baseService.findBaseInfo(anOrder.getCustNo()).getOperOrg());
         anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
         anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
         // 保存附件信息
         anOrder.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anOrder.getBatchNo()));
         anOrder=this.insertVersion(anOrder);
         logger.info("success to add order"+UserUtils.getOperatorInfo().getName());
         return anOrder;
     }
     
     /**
      * 订单信息编辑
      */
     public ScfOrderDO saveModifyOrder(ScfOrderDO anModiOrder,String anFileList,boolean anConfirmFlag) {
         
         BTAssert.notNull(anModiOrder,"订单为空,操作失败");
         BTAssert.notNull(anModiOrder.getRefNo(),"凭证编号为空,操作失败");
         BTAssert.notNull(anModiOrder.getVersion(),"订单信息不全,操作失败");
         logger.info("Begin to modify order");
         ScfOrderDO order = this.selectOneWithVersion(anModiOrder.getRefNo(),anModiOrder.getVersion());
         BTAssert.notNull(order, "无法获取订单信息");
         //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
         checkOperatorModifyStatus(UserUtils.getOperatorInfo(),order);
         // 应收账款信息变更迁移初始化
         //anModiOrder.setId(anOrder.getId());
         order.setCustName(custAccountService.queryCustName(anModiOrder.getCustNo()));
         order.setCoreCustName(custAccountService.queryCustName(anModiOrder.getCoreCustNo()));
         order=anModiOrder.initModifyValue(order);
         // 保存附件信息
         order.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo("", order.getBatchNo()));
         // 初始版本更改 直接返回
         if(order.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
                 order.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_DRAFT)){
             if(anConfirmFlag){
                 order.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
             }
              // 数据存盘
             this.updateByPrimaryKeySelective(order);
             return order;
         }
         // 需要升级版本的修改
           order.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
           order.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_LOCKED);
           order.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
         //this.updateByPrimaryKeySelective(anOrder);
         if(anConfirmFlag){
             order.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
         }
         //ScfOrderDO order2 = this.selectOneWithVersion(order.getRefNo(),order.getVersion());
         anModiOrder=this.updateVersionByPrimaryKeySelective(order,order.getRefNo(),order.getVersion());
         BTAssert.notNull(anModiOrder, "修改订单失败");
         logger.info("success to modify order"+UserUtils.getOperatorInfo().getName());
         return anModiOrder;
     }

     /**
      * 作废当前订单
      * @param refNo
      * @param version
      * @return
      */
     public ScfOrderDO annulOrder(String refNo,String version){
         
         BTAssert.notNull(refNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(version, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(refNo, version);
         BTAssert.notNull(order, "此订单异常!操作失败");
         order=this.annulOperator(UserUtils.getOperatorInfo(), order);
         return order;
         
     }
     
     /**
      * 查询当前订单
      * @param refNo
      * @param version
      * @return
      */
     public ScfOrderDO findOrder(String anRefNo,String anVersion){
         
         BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(anVersion, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(anRefNo, anVersion);
         BTAssert.notNull(order, "此订单异常!操作失败");
         return order;
     }
     
     /**
      * 
      * @param anRefNo
      * @param anVersion
      * @return
      */
     public ScfOrderDO saveAuditOrder(String anRefNo,String anVersion){
         
         BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(anVersion, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(anRefNo, anVersion);
         BTAssert.notNull(order, "此订单异常!操作失败");
         this.auditOperator(UserUtils.getOperatorInfo(), order);
         return order;
         
     }
     
    /**
     * 订单信息分页查询
     * @param anMap 查询条件封装
     * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
     * @param anPageNum 当前页数
     * @param anPageSize 每页显示的数量
     * anIsOnlyNormal 1 数据来源与新增
     * @return
     */
     public Page<ScfOrderDO> queryOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
         
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
         
         if(anMap.containsKey("businStatus") && anMap.get("businStatus")!=null &&VersionConstantCollentions.BUSIN_STATUS_USED.equals(anMap.get("businStatus").toString())){
             
             anMap.put("lockedStatus", VersionConstantCollentions.LOCKED_STATUS_LOCKED);
             
         }
         anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         
         Page<ScfOrderDO> anOrderList = this.selectPropertyByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo");

         return anOrderList;
     }
     
     /**
      * 订单信息未生效分页查询
      * 查询是否编辑和确认 废止的订单  必须是当前用户的自己创建修改的
      * 是查询需要审核的订单   必须是自己公司的订单
      * @param anMap 查询条件封装
      * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
      * @param anPageNum 当前页数
      * @param anPageSize 每页显示的数量
      * anIsOnlyNormal 1 数据来源与新增
      * anIsAudit true 是查询需要审核的订单     false 查询是否编辑和确认 废止的订单
      * @return
      */
     public Page<ScfOrderDO> queryIneffectiveOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anIsAudit) {
         
         BTAssert.notNull(anMap, "查询条件为空!操作失败");
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
        
         anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         
         if(anIsAudit){
             Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
             anMap.put("custNo", getCustNoList(custInfos));
         }else{
             anMap.put("modiOperId", UserUtils.getOperatorInfo().getId());
         }
         
         Page<ScfOrderDO> anOrderList = this.selectPropertyIneffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo");
         
         return anOrderList;
     }
     
     /**
      * 订单信息已生效分页查询
      * isCustNo true 核心企业查询已生效 已使用，已过期，已废止的订单 
      * isCustNo false 供应商查询与自己有关的已生效，已使用，已过期，已废止的订单
      * 是查询需要审核的订单   必须是自己公司的订单
      * @param anMap 查询条件封装
      * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
      * @param anPageNum 当前页数
      * @param anPageSize 每页显示的数量
      * anIsOnlyNormal 1 数据来源与新增
      * 
      * @return
      */
     public Page<ScfOrderDO> queryEffectiveOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anIsCust) {
         
         BTAssert.notNull(anMap, "查询条件为空!操作失败");
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
         
         Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
         if(anIsCust){
             anMap.put("custNo", getCustNoList(custInfos));
             anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         }else{
             anMap.put("coreCustNo", getCustNoList(custInfos));
         }
         
         Page<ScfOrderDO> anOrderList = this.selectPropertyEffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo");
         
         return anOrderList;
     }
     
     /**
      * 将给定的企业集合中提取企业的id
      * @param custInfos
      * @return
      */
     private List<Long> getCustNoList(Collection<CustInfo> custInfos) {
         
         List<Long> custNos=new ArrayList<>();
         for (CustInfo custInfo : custInfos) {
             custNos.add(custInfo.getCustNo());
        }
         
         return custNos;
         
     }

}
