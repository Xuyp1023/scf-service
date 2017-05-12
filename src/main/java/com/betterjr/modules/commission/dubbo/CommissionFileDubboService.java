package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.service.CommissionFileService;
import com.betterjr.modules.commissionfile.ICommissionFileService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=ICommissionFileService.class)
public class CommissionFileDubboService implements ICommissionFileService {

    
    @Autowired
    private CommissionFileService  commissionFileService;
    
    @Override
    public String webAddCommissionFile(Map anParamMap) {
        
        CommissionFile file=new CommissionFile();
        try {
            BeanUtils.populate(file, anParamMap);
        }
        catch (Exception e) {
            BTAssert.notNull(null, "新增佣金文件参数出错"+e.getMessage());
        }
        
        return AjaxObject.newOk("佣金文件登记成功", commissionFileService.saveAddCommissionFile(file)).toJson();
    }

    @Override
    public String webQueryFileList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {
       
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("佣金文件查询成功", commissionFileService.queryFileList(queryMap,anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webSaveDeleteFile(String anRefNo) {
        
        return AjaxObject
                .newOk("佣金文件删除成功", commissionFileService.saveDeleteFile(anRefNo))
                .toJson();
    }

    @Override
    public String webSaveResolveFile(String anRefNo) {
        
        CommissionFile resolveFile = commissionFileService.saveResolveFile(anRefNo);
        BTAssert.notNull(resolveFile,"文件解析失败,当前文件不存在或已删除");
        if(resolveFile.getResolveStatus().equals(CommissionConstantCollentions.COMMISSION_RESOLVE_STATUS_FAILURE)){
            return AjaxObject
                    .newError("解析的佣金文件失败"+resolveFile.getShowMessage())
                    .toJson();
        }
        
        return AjaxObject
                .newOk("佣金文件解析成功", resolveFile)
                .toJson();
        
    }

    @Override
    public String webFindTemplateFile() {
        
        return AjaxObject
                .newOk("佣金文件解析成功", commissionFileService.findCommissionFileExportTemplate())
                .toJson();
    }

}
