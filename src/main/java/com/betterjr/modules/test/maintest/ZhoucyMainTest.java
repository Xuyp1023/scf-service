package com.betterjr.modules.test.maintest;

import java.net.URL;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.betterjr.modules.Provider;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.agreement.service.ScfFactorRemoteHelper;
import com.betterjr.modules.supplychain.handler.OpenScfAccountHandlerService;

public class ZhoucyMainTest {

    private static ClassPathXmlApplicationContext context = null;
    public static void testAccountInfo(){
        OpenScfAccountHandlerService handlerService = context.getBean(OpenScfAccountHandlerService.class);
        handlerService.testData();
    }
    
    public static void testQueryProduct(){
        OpenScfAccountHandlerService handlerService = context.getBean(OpenScfAccountHandlerService.class);
        handlerService.testQueryProduct();
        
    }
    
    public static void testWosignCode(){
        ScfAgreementService scfElecAgreementService = context.getBean(ScfAgreementService.class);
        //scfElecAgreementService.saveAndSendSMS("2016092198ac58");
        scfElecAgreementService.sendValidCode("2016092198ac58", "0", "693414");
        
        //ScfFactorRemoteHelper scfAgreementService = context.getBean(ScfFactorRemoteHelper.class);
        //scfAgreementService.sendSMS("2016092198ac58", 102200334L);
    }
    
    public static void main(String[] args) throws Exception {
        URL url = ZhoucyMainTest.class.getClassLoader().getSystemResource("log4j.properties");
        System.out.println(url.toString());
        Log4jConfigurer.initLogging(url.getFile());
        context = new ClassPathXmlApplicationContext(new String[] { "spring-context-scf-dubbo-provider.xml" });
        //测试发送验证码
        testWosignCode();
        //開戶測試
        //testAccountInfo();
        
        //查詢保理產品 
        //testQueryProduct();
        
        context.close();
        System.exit(0);
    }
}
