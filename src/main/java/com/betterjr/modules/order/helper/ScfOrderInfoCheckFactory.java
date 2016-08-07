package com.betterjr.modules.order.helper;

import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;

/**
 * 根据infoType不同 返回不同的服务类
 */
public class ScfOrderInfoCheckFactory {

    public static IScfOrderInfoCheckService create(String anInfoType) {
        
        IScfOrderInfoCheckService orderInfoCheckService = null;

        if (BetterStringUtils.equals(ScfOrderRelationType.AGGREMENT.getCode(), anInfoType)) {
            
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.ACCEPTBILL.getCode(), anInfoType)) {
            orderInfoCheckService= (IScfOrderInfoCheckService) SpringContextHolder.getBean(ScfAcceptBillService.class);
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anInfoType)) {
            orderInfoCheckService= (IScfOrderInfoCheckService) SpringContextHolder.getBean(ScfAcceptBillService.class);
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anInfoType)) {
            orderInfoCheckService= (IScfOrderInfoCheckService) SpringContextHolder.getBean(ScfAcceptBillService.class);
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anInfoType)) {
            orderInfoCheckService=(IScfOrderInfoCheckService) SpringContextHolder.getBean(ScfAcceptBillService.class);
        }
        BTAssert.isNull(orderInfoCheckService, "获取订单关联资料服务类失败");
        return orderInfoCheckService;

    }
}
