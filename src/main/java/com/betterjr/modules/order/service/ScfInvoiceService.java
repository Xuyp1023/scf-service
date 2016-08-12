package com.betterjr.modules.order.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.order.dao.ScfInvoiceMapper;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;

@Service
public class ScfInvoiceService extends BaseService<ScfInvoiceMapper, ScfInvoice> implements IScfOrderInfoCheckService {

    
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
        //仅显示可用发票
        anMap.put("businStatus", "0");

        Page<ScfInvoice> anInvoiceList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));

        return anInvoiceList;
    }

    /**
     * 变更发票状态
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    private ScfInvoice saveInvoiceStatus(Long anId, String anStatus) {
        ScfInvoice anInvoice = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anInvoice, "无法获取发票信息");
        //检查用户权限
        checkOperator(anInvoice.getOperOrg(), "当前操作员无权限变更发票状态");
        //变更相关状态
        anInvoice.setBusinStatus(anStatus);
        anInvoice.setModiOperId(UserUtils.getOperatorInfo().getId());
        anInvoice.setModiOperName(UserUtils.getOperatorInfo().getName());
        anInvoice.setModiDate(BetterDateUtils.getNumDate());
        anInvoice.setModiTime(BetterDateUtils.getNumTime());
        //数据存盘
        this.updateByPrimaryKeySelective(anInvoice);
        return anInvoice;
    }
    
    /**
     * 检查是否存在相应id、操作机构、业务状态的订单发票
     * @param anId  发票id
     * @param anOperOrg 操作机构
     */
    public void checkInfoExist(Long anId,  String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        List<ScfInvoice> invoiceList = new LinkedList<ScfInvoice>();
        String[] anBusinStatusList = {"1"};
        anMap.put("id", anId);
        anMap.put("operOrg", anOperOrg);
        //查询每个状态数据
        for(int i = 0; i < anBusinStatusList.length; i++) {
            anMap.put("businStatus", anBusinStatusList[i]);
            List<ScfInvoice> tempInvoiceList = this.selectByClassProperty(ScfInvoice.class, anMap);
            invoiceList.addAll(tempInvoiceList);
        }
        if (Collections3.isEmpty(invoiceList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的发票");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的发票");
        }
    }
    
    /**
     * 查询发票信息
     */
    public List<ScfInvoice> findInvoice(Map<String, Object> anMap) {
    	return this.selectByClassProperty(ScfInvoice.class, anMap);
    }

    /**
     * 变更发票状态-失效
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveInvalidInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "0");
    }

    /**
     * 变更发票状态-正常
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveNormalInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "1");
    }

    /**
     * 变更发票状态-过期
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveExpireInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "2");
    }
    
    /**
     * 变更发票状态-冻结
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveForzenInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "3");
    }
    
    /*
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(String anOperOrg, String anMessage) {
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }
}
