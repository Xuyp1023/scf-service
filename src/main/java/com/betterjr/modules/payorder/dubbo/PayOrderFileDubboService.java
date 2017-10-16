package com.betterjr.modules.payorder.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.payorder.IPayOrderFileService;

@Service(interfaceClass = IPayOrderFileService.class)
public class PayOrderFileDubboService implements IPayOrderFileService {

}
