package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.NumberToCN;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;
import com.betterjr.modules.template.service.ScfContractTemplateService;

@Service("scfElecReceivableRequestService")
@Scope("singleton")
public class ScfElecReceivableRequestService extends ScfElecAgreeLocalService {

    private static final Logger logger = LoggerFactory.getLogger(ScfElecProtacalLocalService.class);

    private ScfReceivableRequestService requestService = null;
    private ScfContractTemplateService contractTemplateService = null;

    @Override
    protected void subInit() {
        this.requestService = SpringContextHolder.getBean(ScfReceivableRequestService.class);
        this.contractTemplateService = SpringContextHolder.getBean(ScfContractTemplateService.class);

    }

    @Override
    protected Map<String, Object> findViewModeData() {

        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        ScfReceivableRequest request = requestService.findOneByRequestNo(requestNo);
        result.put("request", request);
        // 封装保理公司默认操作人
        CustOperatorInfo operator = requestService.findDefaultOperatorInfo(request.getFactoryNo());
        if (operator == null) {
            operator = new CustOperatorInfo();
        }
        result.put("factoryOperator", operator);

        // 封装供应商默认操作人
        CustOperatorInfo custoperator = requestService.findDefaultOperatorInfo(request.getCustNo());
        if (custoperator == null) {
            custoperator = new CustOperatorInfo();
        }
        result.put("custoperator", custoperator);

        // 封装金额大写
        result.put("balanceCN", NumberToCN.number2CNMontrayUnit(request.getBalance()));
        result.put("payBalanceCN", NumberToCN.number2CNMontrayUnit(request.getRequestPayBalance()));

        result.put("template", contractTemplateService.findTemplateByType(request.getFactoryNo(),
                "receivableRequestProtocolModel" + request.getReceivableRequestType(), "1"));
        return result;
    }

    @Override
    protected String getViewModeFile() {
        if (elecAgree.getAgreeType().equals("6")) {
            return "receivableRequestProtocolModel1";
        }
        return "receivableRequestProtocolModel2";
    }

    @Override
    public boolean cancelElecAgreement(String anDescribe) {

        logger.info("will cancel appNo is " + this.elecAgree.getAppNo());
        if (elecAgreeService.checkCancelElecAgreement(this.elecAgree.getAppNo())) {
            requestService.saveAnnulReceivableRequest(this.elecAgree.getRequestNo());
            return this.elecAgreeService.saveAndCancelElecAgreement(this.elecAgree.getAppNo(), anDescribe);
        }
        return false;
    }

}
