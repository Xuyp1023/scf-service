package com.betterjr.modules.delivery.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.delivery.IDeliveryStatementService;

@Service(interfaceClass=IDeliveryStatementService.class)
public class DeliveryStatementDubboService implements IDeliveryStatementService{

}
