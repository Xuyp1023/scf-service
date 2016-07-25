package com.betterjr.modules.param.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.param.IScfFactorParamService;
import com.betterjr.modules.param.entity.FactorParam;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfFactorParamService.class)
public class FactorParamDubboService implements IScfFactorParamService{
    protected final Logger logger = LoggerFactory.getLogger(FactorParamDubboService.class);
    
    public String webSaveFactorParam(Map<String, Object> anMap){
        logger.debug("保存保理公司参数,入参:"+anMap);
        
        FactorParam param = (FactorParam) RuleServiceDubboFilterInvoker.getInputObj();
        DictUtils.saveObject("FactorParam", param.getCustNo()+"", "保理公司参数", param);
        return AjaxObject.newOk("保理公司参数保存成功").toJson();
    }
    
    public String webLoadFactorParam(String custNo){
        logger.debug("查询保理公司参数,入参custNo:" + custNo);
        
        FactorParam param = DictUtils.loadObject("FactorParam", custNo, FactorParam.class);
        return AjaxObject.newOk("理公司参数查询成功", param).toJson();
    }
}
