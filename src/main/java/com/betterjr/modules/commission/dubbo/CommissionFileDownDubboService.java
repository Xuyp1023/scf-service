package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.service.CommissionFileDownService;
import com.betterjr.modules.commissionfile.ICommissionFileDownService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=ICommissionFileDownService.class)
public class CommissionFileDownDubboService implements ICommissionFileDownService {
    
    @Autowired
    private CommissionFileDownService fileDownService;

    @Override
    public String webQueryFileDownList(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {


        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOkWithPage("佣金下载文件查询成功", fileDownService.queryFileDownList(queryMap,anFlag, anPageNum, anPageSize))
                .toJson();
    }

}
