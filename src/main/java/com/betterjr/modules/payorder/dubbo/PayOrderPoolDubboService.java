package com.betterjr.modules.payorder.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.payorder.IPayOrderPoolService;

@Service(interfaceClass = IPayOrderPoolService.class)
public class PayOrderPoolDubboService implements IPayOrderPoolService {

}
