package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRequestService.class)
public class RequestDubboService implements IScfRequestService {
    protected final Logger logger = LoggerFactory.getLogger(RequestDubboService.class);

    @Autowired
    private ScfRequestService scfRequestService;

    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("新增融资申请，入参：" + anMap);
        return AjaxObject.newOk(scfRequestService.addRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj())).toJson();
    }

    @Override
    public String webSaveModifyRequest(Map<String, Object> anMap, String anRequestNo) {
        logger.debug("修改融资申请，入参：" + anMap);
        return AjaxObject.newOk(scfRequestService.saveModifyRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj(), anRequestNo)).toJson();
    }
    
    @Override
    public String webQueryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询融资申请列表，入参：" + anMap);
        return AjaxObject.newOkWithPage("融资申请查询成功",
                scfRequestService.queryRequestList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindRequestDetail(Map<String, Object> anMap, String anRequestNo) {
        logger.debug("查询融资申请详情，入参：" + anMap);
        return AjaxObject.newOk(scfRequestService.findRequestDetail(anRequestNo)).toJson();
    }
    

    

}
