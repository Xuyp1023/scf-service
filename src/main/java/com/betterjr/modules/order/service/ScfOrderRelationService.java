package com.betterjr.modules.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.order.dao.ScfOrderRelationMapper;
import com.betterjr.modules.order.data.ScfOrderRelationType;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.receivable.entity.ScfReceivable;

@Service
public class ScfOrderRelationService extends BaseService<ScfOrderRelationMapper, ScfOrderRelation> {

    @Autowired
    private ScfOrderService scfOrderService;
    @Autowired
    private ScfTransportService scfTransportService;
    @Autowired
    private ScfInvoiceService scfInvoiceService;
    @Autowired
    private ScfReceivable scfReceivableService;
    @Autowired
    private ScfAcceptBillService scfAcceptBillService;

    /**
     * 订单关联关系保存
     */
    public ScfOrderRelation addOrderRelation(ScfOrderRelation anOrderRelation) {
        logger.info("Begin to add OrderRelation");
        String 
        // 检查相应id是否存在
        scfOrderService.checkOrderExist(anOrderRelation.getOrderId(), "0", UserUtils.getOperatorInfo().getOperOrg());
        if (BetterStringUtils.equals(ScfOrderRelationType.AGGREMENT.getCode(), anOrderRelation.getInfoType())) {
            // 检查并关联合同
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anOrderRelation.getInfoType())) {
            // 检查并关联运输单据
            scfTransportService.checkTransportExist(anOrderRelation.getId(), anOperOrg);
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anOrderRelation.getInfoType())) {
            // 检查并关联发票信息
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.ACCEPTBILL.getCode(), anOrderRelation.getInfoType())) {
            // 检查并关联汇票
        }
        else if (BetterStringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anOrderRelation.getInfoType())) {
            // 检查并关联应收账款
        }

        anOrderRelation.initAddValue();

        return null;
    }
}
