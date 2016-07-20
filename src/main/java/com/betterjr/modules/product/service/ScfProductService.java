package com.betterjr.modules.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.BaseService;
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

}
