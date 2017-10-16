package com.betterjr.modules.payorder.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.payorder.IPayOrderPoolRecordService;

@Service(interfaceClass = IPayOrderPoolRecordService.class)
public class PayOrderPoolRecordDubboService implements IPayOrderPoolRecordService {

}
