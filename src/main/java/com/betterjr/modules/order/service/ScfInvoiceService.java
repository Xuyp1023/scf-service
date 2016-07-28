package com.betterjr.modules.order.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.order.dao.ScfInvoiceMapper;
import com.betterjr.modules.order.entity.ScfInvoice;

@Service
public class ScfInvoiceService extends BaseService<ScfInvoiceMapper, ScfInvoice> {

    /**
     * 订单发票信息录入
     */
    public ScfInvoice addInvoice(ScfInvoice anInvoice, String anFileList) {
        logger.info("Begin to add Invoice");
        if (BetterStringUtils.isBlank(anFileList)) {
            logger.error("发票附件信息不能为空");
            throw new BytterTradeException(40001, "发票附件信息不能为空");
        }
        anInvoice.initAddValue();
        // 发票初始状态为正常
        anInvoice.setBusinStatus("1");
        this.insert(anInvoice);
        return anInvoice;
    }

    /**
     * 订单发票信息查询
     */
    public Page<ScfInvoice> queryInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        // 限定操作员查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());

        Page<ScfInvoice> anInvoiceList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));

        return anInvoiceList;
    }

    /**
     * 变更发票状态
     */
    private ScfInvoice saveInvoiceStatus(Long anId, String anStatus) {
        ScfInvoice anInvoice = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anInvoice, "无法获取发票信息");
        anInvoice.setBusinStatus(anStatus);
        this.updateByPrimaryKeySelective(anInvoice);
        return anInvoice;
    }

    /**
     * 变更发票状态-失效
     */
    public ScfInvoice saveInvalidInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "0");
    }

    /**
     * 变更发票状态-正常
     */
    public ScfInvoice saveNormalInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "1");
    }

    /**
     * 变更发票状态-过期
     */
    public ScfInvoice saveExpireInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "2");
    }
    
    /**
     * 变更发票状态-冻结
     */
    public ScfInvoice saveForzenInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "3");
    }
}
