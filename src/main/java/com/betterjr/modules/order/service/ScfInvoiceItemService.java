package com.betterjr.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.order.dao.ScfInvoiceItemMapper;
import com.betterjr.modules.order.entity.ScfInvoiceItem;

@Service
public class ScfInvoiceItemService extends BaseService<ScfInvoiceItemMapper, ScfInvoiceItem> {

    /**
     * 发票详情录入
     */
    public ScfInvoiceItem addInvoiceItem(ScfInvoiceItem anInvoiceItem) {
        logger.info("Begin to add InvoiceItem");
        anInvoiceItem.initAddValue();
        this.insert(anInvoiceItem);
        return anInvoiceItem;
    }

    /**
     * 删除发票详情
     */
    public int saveDeleteInvoiceItem(Long anId) {
        logger.info("Begin to delete InvoiceItem");
        // 加载发票详情
        ScfInvoiceItem anInvoiceItem = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anInvoiceItem, "不存在对应发票详情");
        return this.deleteByPrimaryKey(anId);
    }

    /**
     * 保存发票详情至发票
     */
    public void saveInvoiceItemRelation(Long anInvoiceId, String... anInvoiceItemIds) {
        for (String anInvoiceItemId : anInvoiceItemIds) {
            ScfInvoiceItem anInvoiceItem = this.selectByPrimaryKey(Long.valueOf(anInvoiceItemId));
            BTAssert.notNull(anInvoiceItem, "不存在对应发票详情");
            anInvoiceItem.setInvoiceId(anInvoiceId);
            this.updateByPrimaryKeySelective(anInvoiceItem);
        }
    }

    /**
     * 查询发票下的所有发票详情
     */
    public List<ScfInvoiceItem> findItemsByInvoiceId(Long anInvoiceId) {
        return this.selectByClassProperty(ScfInvoiceItem.class, "invoiceId", anInvoiceId);
    }

}
