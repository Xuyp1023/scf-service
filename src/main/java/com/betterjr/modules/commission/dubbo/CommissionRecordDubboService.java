package com.betterjr.modules.commission.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.commissionfile.ICommissionRecordService;

@Service(interfaceClass=ICommissionRecordService.class)
public class CommissionRecordDubboService implements ICommissionRecordService{

}
