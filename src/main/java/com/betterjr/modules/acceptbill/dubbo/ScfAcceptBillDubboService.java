package com.betterjr.modules.acceptbill.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.acceptbill.IScfAcceptBillService;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfAcceptBillService.class)
public class ScfAcceptBillDubboService implements IScfAcceptBillService {

    @Autowired
    private ScfAcceptBillService scfAcceptBillService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Override
    public String webQueryAcceptBill(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        
        Map<String, Object> anQueryConditionMap =  (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("汇票信息查询成功", scfAcceptBillService.queryAcceptBill(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindAcceptBillList(String anCustNo, String anIsOnlyNormal) {
        
        return AjaxObject.newOk("汇票信息查询成功", scfAcceptBillService.findAcceptBillList(anCustNo, anIsOnlyNormal)).toJson();
    }

    @Override
    public String webSaveModifyAcceptBill(Map<String, Object> anMap, Long anId, String anFileList, String anOtherFileList) {
        
        ScfAcceptBill anAcceptBill = (ScfAcceptBill) RuleServiceDubboFilterInvoker.getInputObj();
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        anAcceptBill.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anAcceptBill.getOtherBatchNo()));
        return AjaxObject.newOk("汇票信息修改成功", scfAcceptBillService.saveModifyAcceptBill(anAcceptBill, anId, anFileList)).toJson();
    }
    
    @Override
    public String webAddAcceptBill(Map<String, Object> anMap, String anFileList, String anOtherFileList) {
        
        ScfAcceptBill anAcceptBill = (ScfAcceptBill) RuleServiceDubboFilterInvoker.getInputObj();
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        anAcceptBill.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anAcceptBill.getOtherBatchNo()));
        return AjaxObject.newOk("汇票信息登记成功", scfAcceptBillService.addAcceptBill(anAcceptBill)).toJson();
    }
    
    @Override
    public String webSaveAduitAcceptBill(Long anId) {
        
        return AjaxObject.newOk("汇票信息查询成功", scfAcceptBillService.saveAduitAcceptBill(anId)).toJson();
    }

    @Override
    public String webFindAllFile(Long anId) {
        return AjaxObject.newOk("汇票信息所有附件查询成功", scfAcceptBillService.findAllFile(anId)).toJson();
    }

    @Override
    public String webQueryFinancedByFactor(Map<String, Object> anMap, Long anFactorNo) {
        Map<String, Object> anBillConditionMap =  (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("保理公司查询已融资汇票", scfAcceptBillService.queryFinancedByFactor(anBillConditionMap, anFactorNo)).toJson();
    }
}
