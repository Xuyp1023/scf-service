package com.betterjr.modules.supplieroffer.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.supplieroffer.IScfReceivableRequestAgreementService;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestAgreementService;

@Service(interfaceClass = IScfReceivableRequestAgreementService.class)
public class ScfReceivableRequestAgreementDubboService implements IScfReceivableRequestAgreementService {

    @Autowired
    private ScfReceivableRequestAgreementService agreementService;

    @Override
    public String webQueryAgreementWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        return AjaxObject
                .newOkWithPage("查询合同成功", agreementService.queryAgreementWithCore(anMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webQueryAgreementWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        return AjaxObject.newOkWithPage("查询合同成功",
                agreementService.queryAgreementWithSupplier(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryDictFactory() {

        return AjaxObject.newOk("查询成功", agreementService.queryFactory()).toJson();
    }
}
