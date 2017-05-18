package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfOrderService;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.order.service.ScfOrderDOService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfOrderService.class)
public class ScfOrderDubboService implements IScfOrderService{
    
    @Autowired
    private ScfOrderService scfOrderService;
    
    @Autowired
    private ScfOrderDOService orderService;

    @Override
    public String webSaveModifyOrder(Map<String, Object> anMap, Long anId, String anFileList, String anOtherFileList) {
        ScfOrder anOrder = (ScfOrder) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息编辑成功", scfOrderService.saveModifyOrder(anOrder, anId, anFileList, anOtherFileList)).toJson();
    }
    
    @Override
    public String webSaveModifyOrderDO(Map<String, Object> anMap, String anFileList, boolean confirmFlag) {
        ScfOrderDO anOrder = (ScfOrderDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息编辑成功", orderService.saveModifyOrder(anOrder, anFileList, confirmFlag)).toJson();
    }

    @Override
    public String webQueryOrder(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单信息查询成功", scfOrderService.queryOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webQueryOrderDO (Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单信息查询成功", orderService.queryOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webQueryIneffectiveOrderDO(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,
            boolean anIsAudit) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单信息查询成功", orderService.queryIneffectiveOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize,anIsAudit)).toJson();
   
    }

    @Override
    public String webQueryEffectiveOrderDO(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,
            boolean anIsCust) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单信息查询成功", orderService.queryEffectiveOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize,anIsCust)).toJson();
    }
    
    @Override
    public String webFindOrderDetailsById(Long anId) {
        return AjaxObject.newOk("订单信息查询成功", scfOrderService.findOrderDetailsById(anId)).toJson();
    }
    
    @Override
    public String webFindOrderList(Map<String, Object> anMap, String anIsOnlyNormal) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息查询成功", scfOrderService.findOrderList(queryMap, anIsOnlyNormal)).toJson();
    }
    
    @Override
    public String webFindInfoListByRequest(String anRequestNo, String anRequestType) {
        return AjaxObject.newOk("订单信息查询成功", scfOrderService.findInfoListByRequest(anRequestNo, anRequestType)).toJson();
    }

    @Override
    public String webAddOrder(Map<String, Object> anMap, String anFileList, String anOtherFileList) {
        ScfOrder anOrder = (ScfOrder) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息新增成功", scfOrderService.addOrder(anOrder, anFileList, anOtherFileList)).toJson();
    }
    
    @Override
    public String webAddOrderDO(Map<String, Object> anMap, String anFileList, boolean confirmFlag) {
        ScfOrderDO anOrder = (ScfOrderDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息新增成功", orderService.addOrder(anOrder, anFileList, confirmFlag)).toJson();
    }

    @Override
    public String webCheckCompleteInvoice(String anRequestType, String anIdList) {
        return AjaxObject.newOk("检查关联发票关系成功", scfOrderService.checkCompleteInvoice(anRequestType, anIdList)).toJson();
    }
    
    @Override
    public String webFindRequestBaseInfoFileList(String anRequestNo) {
        return AjaxObject.newOk("查询所有附件成功", scfOrderService.findRequestBaseInfoFileList(anRequestNo)).toJson();
    }
    
    @Override
    public String webCheckInfoCompleted(String anIdList, String anRequestType) {
        return AjaxObject.newOk("资料检查成功", scfOrderService.checkInfoCompleted(anIdList, anRequestType)? "0":"1").toJson();
    }
    
    @Override
    public String webCheckAgreementStatus(Long anAcceptBillId){
        return AjaxObject.newOk("合同状态检查成功", scfOrderService.checkAgreementStatus(anAcceptBillId)).toJson();
    }

    @Override
    public String webFindSubjectMaster(String anId, String anType){
        return AjaxObject.newOk("查询标的物", scfOrderService.getSubjectMaster(anId, anType)).toJson();
    }
    
    @Override
    public String webFindCoreCustNo(String anId, String anType){
        return AjaxObject.newOk("查询标的物", scfOrderService.getCoreCustNoByMaster(anId, anType)).toJson();
    }
    
    @Override
    public String webFindRequestByInfoId(Long anInfoId, String anInfoType){
        return AjaxObject.newOk("根据资料id和资料类型查询融资实体", scfOrderService.findRequestByInfoId(anInfoId, anInfoType)).toJson();
    }

    @Override
    public String webQueryCanAnnulOrder(Map<String, Object> anAnMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject
                .newOkWithPage("订单信息查询成功", orderService.queryCanAnnulBill(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webSaveAnnulOrder(String anRefNo, String anVersion) {
        
        
        return AjaxObject.newOk("订单废止成功", orderService.saveAnnulOrder(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webSaveAuditOrderByRefNoVersion(String anRefNo, String anVersion) {
       
        
        return AjaxObject.newOk("订单审核成功", orderService.saveAuditOrder(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webQueryIneffectiveOrder(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,
            boolean anIsAudit) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单查询成功", orderService.queryIneffectiveOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize, anIsAudit)).toJson();
    }

    @Override
    public String webQueryEffectiveOrder(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,
            boolean anIsCust) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单查询成功", orderService.queryEffectiveOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize, anIsCust)).toJson();
    }

    @Override
    public String webfindOrderDetail(String anRefNo, String anVersion) {
       
        return AjaxObject.newOk("订单查询成功", orderService.findOrder(anRefNo, anVersion)).toJson();
    }

}
