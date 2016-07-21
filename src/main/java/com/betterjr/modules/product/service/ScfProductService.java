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
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.product.dao.ScfProductMapper;
import com.betterjr.modules.product.entity.ScfProduct;

@Service
public class ScfProductService extends BaseService<ScfProductMapper, ScfProduct> {

    @Autowired
    private CustAndOperatorRelaService custAndOperatorRelaService;

    @Autowired
    private CustAccountService custAccountService;

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
        // 操作员只能查询所属机构的融资产品信息
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());

        Page<ScfProduct> anProductList = this.selectPropertyByPage(ScfProduct.class, anMap, anPageNum, anPageSize, "1".equals(anFlag));
        for (ScfProduct anProduct : anProductList) {
            anProduct.setCoreName(custAccountService.queryCustName(anProduct.getCoreCustNo()));
            anProduct.setFactorName(custAccountService.queryCustName(anProduct.getFactorNo()));
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

        // 操作员所属机构
        String anOperOrg = UserUtils.getOperatorInfo().getOperOrg();

        // 设置操作员所属保理公司客户号
        Long anFactorNo = Collections3.getFirst(custAndOperatorRelaService.findCustNoList(UserUtils.getOperatorInfo().getId(), anOperOrg));
        anProduct.setFactorNo(anFactorNo);

        // 设置操作员所属保理公司简称
        anProduct.setFactorCorp(Collections3.getFirst(custAccountService.selectByProperty("custNo", anFactorNo)).getShortName());

        // 数据存盘
        this.insert(anProduct);

        return anProduct;
    }

    public ScfProduct saveModifyProduct(ScfProduct anModiProduct) {
        logger.info("Begin to modify Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anModiProduct.getId());
        if (null == anProduct) {
            logger.error("无法获取融资产品信息");
            throw new BytterTradeException(40001, "无法获取融资产品信息");
        }

        // 检查当前操作员是否能修改该融资产品
        CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (BetterStringUtils.equals(operator.getOperOrg(), anProduct.getOperOrg()) == false) {
            logger.warn("当前操作员不能修改该融资产品");
            throw new BytterTradeException(40001, "当前操作员不能修改该融资产品");
        }

        // 不允许修改已上架(businStatus:1)的融资产品
        String status = anProduct.getBusinStatus();
        if (BetterStringUtils.equals(status, "1") == true) {
            logger.warn("当前融资产品已上架,不允许修改");
            throw new BytterTradeException(40001, "当前融资产品已上架,不允许修改");
        }

        // 不允许修改已下架(businStatus:2)的融资产品
        if (BetterStringUtils.equals(status, "2") == true) {
            logger.warn("当前融资产品已下架,不允许修改");
            throw new BytterTradeException(40001, "当前融资产品已下架,不允许修改");
        }

        // 设置修改信息
        anModiProduct.initModifyValue(anProduct);

        // 数据存盘
        this.updateByPrimaryKey(anModiProduct);

        return anModiProduct;
    }

    public int saveDeleteProduct(Long anId) {
        logger.info("Begin to Delete Product");

        ScfProduct anProduct = this.selectByPrimaryKey(anId);
        if (null == anProduct) {
            logger.error("无法获取融资产品信息");
            throw new BytterTradeException(40001, "无法获取融资产品信息");
        }

        // 不允许删除已上架(businStatus:1)的融资产品
        String status = anProduct.getBusinStatus();
        if (BetterStringUtils.equals(status, "1") == true) {
            logger.warn("当前融资产品已上架,不允许删除");
            throw new BytterTradeException(40001, "当前融资产品已上架,不允许删除");
        }

        // 不允许删除已下架(businStatus:2)的融资产品
        if (BetterStringUtils.equals(status, "2") == true) {
            logger.warn("当前融资产品已下架,不允许删除");
            throw new BytterTradeException(40001, "当前融资产品已下架,不允许删除");
        }

        // 数据存盘
        return this.deleteByPrimaryKey(anId);
    }

}
