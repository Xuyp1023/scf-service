package com.betterjr.modules.acceptbill.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.acceptbill.IScfAcceptBillService;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfAcceptBillService.class)
public class ScfAcceptBillDubboService implements IScfAcceptBillService {

    @Autowired
    private ScfAcceptBillService scfAcceptBillService;

    @Autowired
    private ScfAcceptBillDOService acceptBillService;
    
    @Override
    public String webQueryAcceptBill(final Map<String, Object> anMap, final String anIsOnlyNormal, final String anFlag, final int anPageNum,
            final int anPageSize) {

        final Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("汇票信息查询成功", scfAcceptBillService.queryAcceptBill(anQueryConditionMap, anIsOnlyNormal, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webFindAcceptBillList(final Map<String, Object> anMap, final String anIsOnlyNormal) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("汇票信息查询成功", scfAcceptBillService.findAcceptBillList(queryMap, anIsOnlyNormal)).toJson();
    }

    @Override
    public String webSaveModifyAcceptBill(final Map<String, Object> anMap, final Long anId, final String anFileList, final String anOtherFileList) {

        final ScfAcceptBill anAcceptBill = (ScfAcceptBill) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("汇票信息修改成功", scfAcceptBillService.saveModifyAcceptBill(anAcceptBill, anId, anFileList, anOtherFileList)).toJson();
    }
    
    @Override
    public String webSaveModifyAcceptBillDO(Map<String, Object> anMap, String anFileList, boolean anConfirmFlag) {
      
         ScfAcceptBillDO anAcceptBill = (ScfAcceptBillDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("汇票信息修改成功", acceptBillService.saveModifyAcceptBill(anAcceptBill, anFileList,anConfirmFlag )).toJson();
    }

    @Override
    public String webAddAcceptBill(final Map<String, Object> anMap, final String anFileList, final String anOtherFileList) {

        final ScfAcceptBill anAcceptBill = (ScfAcceptBill) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("汇票信息登记成功", scfAcceptBillService.addAcceptBill(anAcceptBill, anFileList, anOtherFileList)).toJson();
    }

    @Override
    public String webAddAcceptBillDO(Map<String, Object> anMap, String anFileList, boolean anConfirmFlag) {
        
        ScfAcceptBillDO anAcceptBill = (ScfAcceptBillDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("票据登记成功", acceptBillService.addAcceptBill(anAcceptBill, anFileList,anConfirmFlag)).toJson();
   
    }
    
    @Override
    public String webSaveAduitAcceptBill(final Long anId) {

        return AjaxObject.newOk("汇票信息审核", scfAcceptBillService.saveAduitAcceptBill(anId)).toJson();
    }

    @Override
    public String webFindAllFile(final Long anId) {
        return AjaxObject.newOk("汇票信息所有附件查询成功", scfAcceptBillService.findAllFile(anId)).toJson();
    }

    @Override
    public String webQueryFinancedByFactor(final Map<String, Object> anMap, final Long anFactorNo) {
        final Map<String, Object> anBillConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("保理公司查询已融资汇票", scfAcceptBillService.queryFinancedByFactor(anBillConditionMap, anFactorNo)).toJson();
    }

    @Override
    public String webFindAcceptBillDetailsById(final Long anId) {

        return AjaxObject.newOk("汇票信息详情查询", scfAcceptBillService.findAcceptBillDetailsById(anId)).toJson();
    }

    @Override
    public String webSaveSingleFileLink(final Long anId, final Long anFileId) {

        return AjaxObject.newOk("汇票信息详情查询", scfAcceptBillService.saveSingleFileLink(anId, anFileId)).toJson();
    }

    @Override
    public String webFindAcceptBillListByCustNo(final String anCustNo) {
        return AjaxObject.newOk("汇票信息详情查询", scfAcceptBillService.findAcceptBillListByCustNo(anCustNo)).toJson();
    }

    @Override
    public String findAcceptBillStatusById(final Long anBillId) {
        final ScfAcceptBill bill = scfAcceptBillService.findAcceptBillDetailsById(anBillId);
        return bill != null ? bill.getBusinStatus() : null;
    }
    
    @Override
    public String webSaveBillFile(Long anBillId,String anFileTypeName,String anFileMediaId){
        return AjaxObject.newOk("保存票据附件",scfAcceptBillService.saveBillFile(anBillId,anFileTypeName,anFileMediaId)).toJson();
    }

    @Override
    public String webSaveAnnulAcceptBill(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("票据废止成功", acceptBillService.annulBill(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webFindAcceptBillDOByRefNoVersion(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("票据查询成功", acceptBillService.findBill(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webSaveAuditBillDOByRefNoVersion(String anRefNo, String anVersion) {
        
        return AjaxObject.newOk("票据查询成功", acceptBillService.saveAuditBill(anRefNo, anVersion)).toJson();
    }

   

    
}
