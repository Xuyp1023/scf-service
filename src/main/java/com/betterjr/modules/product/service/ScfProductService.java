package com.betterjr.modules.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.product.dao.ScfProductMapper;
import com.betterjr.modules.product.entity.ScfProduct;

@Service
public class ScfProductService extends BaseService<ScfProductMapper, ScfProduct> {

    @Autowired
    private CustAccountService custAccountService;

    @Autowired
    private CustAndOperatorRelaService custAndOperatorRelaService;

    /**
     * 融资产品信息分页查询
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfProduct> queryProduct(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        // 状态(0:登记;1:上架;2:下架;)
        Object businStatus = anMap.get("businStatus");
        if (null == businStatus || businStatus.toString().isEmpty()) {
            anMap.put("businStatus", new String[] { "0", "1", "2" });
        }
        // 操作员只能查询所属机构的融资产品信息
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());

        Page<ScfProduct> anProductList = this.selectPropertyByPage(ScfProduct.class, anMap, anPageNum, anPageSize, "1".equals(anFlag));
        for (ScfProduct anProduct : anProductList) {
            anProduct.setCoreName(custAccountService.queryCustName(anProduct.getCoreCustNo()));
            anProduct.setFactorName(custAccountService.queryCustName(anProduct.getFactorNo()));
            anProduct.setRegDate(BetterDateUtils.formatDispDate(anProduct.getRegDate()));
            anProduct.setModiDate(BetterDateUtils.formatDispDate(anProduct.getModiDate()));
            anProduct.setShelvesDate(BetterDateUtils.formatDispDate(anProduct.getShelvesDate()));
            anProduct.setOffLineDate(BetterDateUtils.formatDispDate(anProduct.getOffLineDate()));
        }

        return anProductList;
    }

    /**
     * 融资产品下拉列表查询
     * 
     * @param coreCustNo
     * @param anFactorNo
     * @return
     */
    public List<SimpleDataEntity> queryProductKeyAndValue(Long coreCustNo, Long anFactorNo) {
        List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();
        if (null == coreCustNo || null == anFactorNo) {
            return result;
        }

        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("coreCustNo", coreCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("businStatus", "1");// 状态(0:登记;1:上架;2:下架;)

        for (ScfProduct product : this.selectByProperty(anMap)) {
            result.add(new SimpleDataEntity(product.getProductName(), String.valueOf(product.getId())));
        }

        return result;
    }

    /**
     * 融资产品录入
     * 
     * @param anMap
     * @return
     */
    public ScfProduct addProduct(ScfProduct anProduct) {
        logger.info("Begin to add Product");

        // 初始化信息
        anProduct.initAddValue();

        // 设置操作员所属保理公司客户号
        Long anFactorNo = Collections3
                .getFirst(custAndOperatorRelaService.findCustNoList(UserUtils.getOperatorInfo().getId(), anProduct.getOperOrg()));
        anProduct.setFactorNo(anFactorNo);

        // 设置操作员所属保理公司简称
        anProduct.setFactorCorp(Collections3.getFirst(custAccountService.selectByProperty("custNo", anFactorNo)).getShortName());

        // 数据存盘
        this.insert(anProduct);

        return anProduct;
    }

    /**
     * 融资产品修改
     * 
     * @param anModiProduct
     * @param anId
     * @return
     */
    public ScfProduct saveModifyProduct(ScfProduct anModiProduct, Long anId) {
        logger.info("Begin to modify Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anProduct, "无法获取融资产品信息");

        // 检查当前操作员是否能修改该融资产品
        checkOperator(anProduct.getOperOrg(), "当前操作员不能修改该融资产品");

        // 不允许修改已上架(businStatus:1)的融资产品
        String businStatus = anProduct.getBusinStatus();
        checkStatus(businStatus, "1", true, "当前融资产品已上架,不允许修改");

        // 不允许修改已下架(businStatus:2)的融资产品
        checkStatus(businStatus, "2", true, "当前融资产品已下架,不允许修改");

        // 设置修改信息
        anModiProduct.initModifyValue(anProduct);

        // 数据存盘
        this.updateByPrimaryKey(anModiProduct);

        return anModiProduct;
    }

    /**
     * 融资产品删除
     * 
     * @param anId
     * @return
     */
    public int saveDeleteProduct(Long anId) {
        logger.info("Begin to Delete Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anProduct, "无法获取融资产品信息");

        // 检查当前操作员是否能删除该融资产品
        checkOperator(anProduct.getOperOrg(), "当前操作员不能删除该融资产品");

        // 不允许删除已上架(businStatus:1)的融资产品
        String businStatus = anProduct.getBusinStatus();
        checkStatus(businStatus, "1", true, "当前融资产品已上架,不允许删除");

        // 不允许删除已下架(businStatus:2)的融资产品
        checkStatus(businStatus, "2", true, "当前融资产品已下架,不允许删除");

        // 数据存盘
        return this.deleteByPrimaryKey(anId);
    }

    /**
     * 融资产品上架
     * 
     * @param anId
     * @return
     */
    public ScfProduct saveShelvesProduct(Long anId) {
        logger.info("Begin to Shelves Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anProduct, "无法获取融资产品信息");

        // 检查当前操作员是否能上架该融资产品
        checkOperator(anProduct.getOperOrg(), "当前操作员不能对该融资产品进行上架操作");

        // 已上架(businStatus:1)的融资产品不需要执行此操作
        String anBusinStatus = anProduct.getBusinStatus();
        checkStatus(anBusinStatus, "1", true, "当前融资产品已上架");

        // 已下架(businStatus:2)的融资产品不需要执行此操作
        checkStatus(anBusinStatus, "2", true, "当前融资产品已下架");

        // 设置上架信息
        anProduct.initShelvesValue();

        // 数据存盘
        this.updateByPrimaryKey(anProduct);

        return anProduct;
    }

    /**
     * 融资产品下架
     * 
     * @param anId
     * @return
     */
    public ScfProduct saveOfflineProduct(Long anId) {
        logger.info("Begin to Offline Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anProduct, "无法获取融资产品信息");

        // 检查当前操作员是否能下架该融资产品
        checkOperator(anProduct.getOperOrg(), "当前操作员不能对该融资产品进行下架操作");

        // 未上架(businStatus:0)的融资产品不需要执行此操作
        String businStatus = anProduct.getBusinStatus();
        checkStatus(businStatus, "0", true, "当前融资产品未上架");

        // 已下架(businStatus:2)的融资产品不需要执行此操作
        checkStatus(businStatus, "2", true, "当前融资产品已下架");

        // 设置下架信息
        anProduct.initOfflineValue();

        // 数据存盘
        this.updateByPrimaryKey(anProduct);

        return anProduct;
    }

    private void checkOperator(String anOperOrg, String anMessage) {
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    private void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

}
