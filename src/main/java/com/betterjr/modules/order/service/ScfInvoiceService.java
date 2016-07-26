package com.betterjr.modules.order.service;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.order.dao.ScfInvoiceMapper;
import com.betterjr.modules.order.entity.ScfInvoice;

@Service
public class ScfInvoiceService extends BaseService<ScfInvoiceMapper, ScfInvoice> {
    
    /**
     * 订单发票信息录入
     */
    public ScfInvoice addInvoice(ScfInvoice anInvoice, String anFileList) {
        logger.info("Begin to add Invoice");
        if(BetterStringUtils.isBlank(anFileList)) {
            logger.error("发票附件信息不能为空");
            throw new BytterTradeException(40001, "发票附件信息不能为空");
        }
        anInvoice.initAddValue();
        //发票初始状态为正常
        anInvoice.setBusinStatus("1");
        this.insert(anInvoice);
        return anInvoice;
    }

}
