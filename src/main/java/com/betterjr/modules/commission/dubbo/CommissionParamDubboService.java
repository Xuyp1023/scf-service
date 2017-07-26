package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.entity.CommissionParam;
import com.betterjr.modules.commission.service.CommissionParamService;
import com.betterjr.modules.commissionfile.ICommissionParamService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=ICommissionParamService.class)
public class CommissionParamDubboService implements ICommissionParamService {
    
    
    @Autowired
    private CommissionParamService paramService;

    @Override
    public String webSaveAddParam(Map anParamMap) {
        
        CommissionParam param=RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOk("参数配置新增成功", paramService.saveAddParam(param)).toJson();
    }

    @Override
    public String webQueryParamList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {
        
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("参数配置查询成功", paramService.queryParamList(queryMap,anFlag, anPageNum, anPageSize))
                .toJson();
        
    }

    @Override
    public String webSaveUpdateParam(Map anParamMap) {
        
        
        CommissionParam param=RuleServiceDubboFilterInvoker.getInputObj();
        
        return AjaxObject.newOk("参数配置修改成功", paramService.saveUpdateParam(param)).toJson();
        
    }
    
    @Override
    public String webSaveDeleteParam(Long anParamId) {
        
        return AjaxObject.newOk("参数配置删除成功", paramService.saveDeleteParam(anParamId)).toJson();
    }

    @Override
    public String webFindParamByCustNo(Long anCustNo, Long anCoreCustNo) {
        
        return AjaxObject.newOk("参数配置查询成功", paramService.findParamByCustNo(anCustNo, anCoreCustNo)).toJson();
    }


}
