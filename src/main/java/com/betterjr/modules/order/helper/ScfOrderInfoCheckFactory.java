package com.betterjr.modules.order.helper;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.order.service.ScfTransportService;
import com.betterjr.modules.receivable.service.ScfReceivableService;

/**
 * 根据infoType不同 返回不同的服务类
 */
public class ScfOrderInfoCheckFactory {

    public static IScfOrderInfoCheckService create(String anInfoType) {

        IScfOrderInfoCheckService orderInfoCheckService = null;

        if (StringUtils.equals(ScfOrderRelationType.AGGREMENT.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder
                    .getBean(ScfCustAgreementService.class);
        } else if (StringUtils.equals(ScfOrderRelationType.ACCEPTBILL.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder.getBean(ScfAcceptBillService.class);
        } else if (StringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder.getBean(ScfInvoiceService.class);
        } else if (StringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder.getBean(ScfReceivableService.class);
        } else if (StringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder.getBean(ScfTransportService.class);
        } else if (StringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anInfoType)) {
            orderInfoCheckService = SpringContextHolder.getBean(ScfOrderService.class);
        }

        BTAssert.notNull(orderInfoCheckService, "获取订单关联资料服务类失败");
        return orderInfoCheckService;

    }
}
