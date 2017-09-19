package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.NumberToCN;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;
import com.betterjr.modules.template.service.ScfContractTemplateService;


@Service("scfElecReceivableRequestService")
@Scope("singleton")
public class ScfElecReceivableRequestService  extends ScfElecAgreeLocalService{

    private static final Logger logger = LoggerFactory.getLogger(ScfElecProtacalLocalService.class);

    
    
    private ScfReceivableRequestService requestService  = null;
    private ScfContractTemplateService contractTemplateService = null;
    

    protected void subInit(){
       this.requestService = SpringContextHolder.getBean(ScfReceivableRequestService.class);
       this.contractTemplateService = SpringContextHolder.getBean(ScfContractTemplateService.class);
       
    }

    @Override
    protected Map<String, Object> findViewModeData() {
        
        
        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        ScfReceivableRequest request = requestService.findOneByRequestNo(requestNo);
        result.put("request", request);
        //封装保理公司默认操作人
        CustOperatorInfo operator =requestService.findDefaultOperatorInfo(request.getFactoryNo());
        if(operator==null){
            operator=new CustOperatorInfo();
        }
        result.put("factoryOperator", operator);
        //封装供应商默认操作人
        CustOperatorInfo custoperator = requestService.findDefaultOperatorInfo(request.getCustNo());
        if(custoperator==null){
            custoperator=new CustOperatorInfo();
        }
        result.put("custoperator", custoperator);
       
        //封装原付款年月日
        
        if(StringUtils.isNoneBlank(request.getEndDate())){
            
            result.put("sourceDateYear", request.getEndDate().substring(0, 4));
            result.put("sourceDateMonthy", request.getEndDate().substring(4, 6));
            result.put("sourceDateDay", request.getEndDate().substring(6, 8));
        }
        
        //付款日期
        if(StringUtils.isNoneBlank(request.getRequestPayDate())){
            
            result.put("payDateYear", request.getRequestPayDate().substring(0, 4));
            result.put("payDateMonthy", request.getRequestPayDate().substring(4, 6));
            result.put("payDateDay", request.getRequestPayDate().substring(6, 8));
        }
        //封装合同签署日期
        if(StringUtils.isNoneBlank(this.elecAgree.getSignDate())){
            
            result.put("sourceAgreementDateYear", this.elecAgree.getSignDate().substring(0, 4));
            result.put("sourceAgreementDateMonthy", this.elecAgree.getSignDate().substring(4, 6));
            result.put("sourceAgreementDateDay", this.elecAgree.getSignDate().substring(6, 8));
        }
        
        //封装金额大写
        result.put("balanceCN", NumberToCN.number2CNMontrayUnit(request.getBalance()));
        result.put("payBalanceCN", NumberToCN.number2CNMontrayUnit(request.getRequestPayBalance()));
        result.put("factoryStamp", request.getFactoryName());
        result.put("custStamp", request.getCustName());
        result.put("elecAgreement", this.elecAgree);
        
        result.put("template", contractTemplateService.findTemplateByType(request.getFactoryNo(), "receivableRequestProtocolModel"+request.getReceivableRequestType(), "1"));
        return result;
    }

    @Override
    protected String getViewModeFile() {
        if(elecAgree.getAgreeType().equals("6")){
            return "receivableRequestProtocolModel1";
        }
        return "receivableRequestProtocolModel2";
    }

    @Override
    public boolean cancelElecAgreement(String anDescribe) {
        // TODO Auto-generated method stub
        return false;
    }

}
