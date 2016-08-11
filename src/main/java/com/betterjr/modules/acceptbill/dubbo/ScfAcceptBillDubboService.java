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
    public String webQueryAcceptBill(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        Map<String, Object> anQueryConditionMap =  (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("汇票信息查询成功", scfAcceptBillService.queryAcceptBill(anQueryConditionMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveModifyAcceptBill(Map<String, Object> anMap, Long anId, String anFileList) {
        
        ScfAcceptBill anAcceptBill = (ScfAcceptBill) RuleServiceDubboFilterInvoker.getInputObj();
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        return AjaxObject.newOk("汇票信息修改成功", scfAcceptBillService.saveModifyAcceptBill(anAcceptBill, anId, anFileList)).toJson();
    }
}
