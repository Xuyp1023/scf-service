package com.betterjr.modules.order.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.order.IScfOrderService;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfOrderService.class)
public class ScfOrderDubboService implements IScfOrderService{
    
    @Autowired
    private ScfOrderService scfOrderService;

    @Override
    public String webSaveModifyOrder(Map<String, Object> anMap, Long anId, String anFileList, String anOtherFileList) {
        ScfOrder anOrder = (ScfOrder) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("订单信息编辑成功", scfOrderService.saveModifyOrder(anOrder, anId, anFileList, anOtherFileList)).toJson();
    }

    @Override
    public String webQueryOrder(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("订单信息查询成功", scfOrderService.queryOrder(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
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
    public String webFindRequestByInfoId(Long anInfoId, String anInfoType){
        return AjaxObject.newOk("根据资料id和资料类型查询融资实体", scfOrderService.findRequestByInfoId(anInfoId, anInfoType)).toJson();
    }
}
