package com.betterjr.modules.delivery.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.delivery.IDeliveryService;

@Service(interfaceClass=IDeliveryService.class)
public class DeliveryDubboService  implements IDeliveryService{

}
