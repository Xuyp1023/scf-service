package com.betterjr.modules.supplychain.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.mq.annotation.RocketMQListener;
import com.betterjr.common.mq.message.MQMessage;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.account.entity.SaleAccoRequestInfo;
import com.betterjr.modules.agreement.service.ScfFactorRemoteHelper;
import com.betterjr.modules.customer.ICustMechBankAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustMechBusinLicenceService;
import com.betterjr.modules.customer.ICustMechLawService;
import com.betterjr.modules.customer.ICustOpenAccountService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.constants.CustomerConstants;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.notification.constants.NotificationConstants;
import com.betterjr.modules.operator.dubbo.IOperatorService;
import com.betterjr.modules.param.entity.FactorParam;

/**
 * @author zhoucy
 *
 */
@Service
public class OpenScfAccountHandlerService {
    private final static Logger logger = LoggerFactory.getLogger(OpenScfAccountHandlerService.class);

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService relationService;

    //银行账户信息，取默认银行账户
    @Reference(interfaceClass = ICustMechBankAccountService.class)
    private ICustMechBankAccountService bankAccountService;

    //机构法人信息
    @Reference(interfaceClass = ICustMechLawService.class)
    private ICustMechLawService mechLawService;

    //机构基本信息
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService mechBaseService;

    //机构营业执照信息
    @Reference(interfaceClass = ICustMechBusinLicenceService.class)
    private ICustMechBusinLicenceService businLicenceService;
    
    //经办人信息
    @Reference(interfaceClass = IOperatorService.class)
    private IOperatorService operatorService;

    @Reference(interfaceClass = ICustOpenAccountService.class)
    private ICustOpenAccountService openAccountService;
    
    @Autowired
    private ScfFactorRemoteHelper factorRemoteHelper; 
    /**
     * 处理开通业务调用远端的接口
     */
    @RocketMQListener(topic = CustomerConstants.CUSTOMER_OPEN_SCF_ACCOUNT , consumer = "betterConsumer")
    public void processNotification(final Object anMessage){
       MQMessage message = (MQMessage) anMessage;
       logger.info("processNotification :" + message);
       CustRelation custRelation = (CustRelation) message.getObject();
       logger.info("processNotification custRelation :" + custRelation);
       FactorParam param = DictUtils.loadObject("FactorParam", String.valueOf(custRelation.getRelateCustno()), FactorParam.class);
       if(param == null || (param.getRemoting() == 0)){
          logger.warn("not find FactorParam " + custRelation.getRelateCustname()); 
          return;
       }
       
       //CustRelation custRelation = this.relationService.findByRelationId(12L);
       SaleAccoRequestInfo accoRequest = createOpenAccountRequestData(custRelation);
       factorRemoteHelper.dealAccoRequest(accoRequest, custRelation, null);
       
    }
    
    public void testData(){
        if (this.relationService == null){
            logger.warn("relationService is null");
            return ;
        }
        CustRelation custRelation = this.relationService.findByRelationId(3110L);
        SaleAccoRequestInfo accoRequest = createOpenAccountRequestData(custRelation);
        factorRemoteHelper.dealAccoRequest(accoRequest, custRelation, null);
    }

    public void testQueryProduct(){
        factorRemoteHelper.queryProductInfo();
    }
    
    private SaleAccoRequestInfo createOpenAccountRequestData(CustRelation custRelation){
        Map<String, Object> data = openAccountService.findOpenTempAccountInfo(custRelation.getCustNo());
        data.put("contEmail", data.remove("operEmail"));
        data.put("bankAccount", data.remove("bankAcco"));
        data.put("certValidDate", data.remove("validDate"));
        data.put("contName", data.remove("operName"));
        data.put("busiLicence", data.remove("orgCode"));
        data.put("contIdentNo", data.remove("operIdentno"));
        data.put("contIdentType", data.remove("operIdenttype"));
        data.put("contMobileNo", data.remove("operMobile"));
        data.put("bankAcountName", data.remove("bankAccoName"));
        data.put("cityNo", data.remove("bankCityno"));
        
        SaleAccoRequestInfo accountRequest = BeanMapper.map(data, SaleAccoRequestInfo.class);
        accountRequest.setRequestNo(SerialGenerator.getAppSerialNo(18));
        accountRequest.setBusinFlag("08");
        accountRequest.setAgencyNo(custRelation.getRelateCustCorp());
        accountRequest.setCustType( relationService.checkCoreCustomer(custRelation.getCustNo(), custRelation.getRelateCustCorp()));
        logger.info("accountRequest data :"+accountRequest);
        
        return accountRequest;
    }
    
    private SaleAccoRequestInfo findAccoRequestInfo(CustRelation anRelation){
        
        return null;
    }
}
