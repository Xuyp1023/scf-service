package com.betterjr.modules.supplychain.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.mq.annotation.RocketMQListener;
import com.betterjr.modules.customer.constants.CustomerConstants;

@Service
public class ScfBusinRequestHanderService {
    private final static Logger logger = LoggerFactory.getLogger(ScfBusinRequestHanderService.class);
    
    @RocketMQListener(topic = CustomerConstants.CUSTOMER_OPEN_SCF_ACCOUNT , consumer = "betterConsumer")
    public void processBusinRequest(final Object anMessage){
        
    }
}
