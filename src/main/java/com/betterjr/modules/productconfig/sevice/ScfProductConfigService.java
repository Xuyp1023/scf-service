package com.betterjr.modules.productconfig.sevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.product.constant.ProductConstants;
import com.betterjr.modules.productconfig.dao.ScfProductConfigMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.entity.ScfProductCoreRelation;
import com.betterjr.modules.supplieroffer.service.ScfCoreProductCustService;

@Service
public class ScfProductConfigService extends BaseService<ScfProductConfigMapper, ScfProductConfig> {

    @Autowired
    private ScfProductCoreRelationService productCoreRelationService;

    @Autowired
    private ScfProductAssetDictRelationService productAssetDictRelationService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService custRelationService;

    @Autowired
    private ScfCoreProductCustService productCustService;

    public ScfProductConfig addConfig(ScfProductConfig anConfig) {
        BTAssert.notNull(anConfig, "产品配置失败");
        anConfig.init();
        this.insert(anConfig);
        return this.selectByPrimaryKey(anConfig.getId());
    }

    public ScfProductConfig saveModifyConfig(ScfProductConfig anConfig, Long anId) {
        BTAssert.notNull(anConfig, "产品修改失败");
        BTAssert.notNull(anId, "产品修改失败");
        anConfig.setId(anId);
        anConfig.initModify();
        this.updateByPrimaryKeySelective(anConfig);
        anConfig = this.selectByPrimaryKey(anConfig.getId());
        if (StringUtils.isNoneBlank(anConfig.getBusinStatus()) && "2".equals(anConfig.getBusinStatus())) {

            productCustService.saveAnnulProductByCode(anConfig.getProductCode());

        }
        return anConfig;
    }

    public ScfProductConfig findProduct(Map<String, Object> anMap) {
        anMap = Collections3.filterMap(anMap, new String[] { "factorNo", "productCode", "businStatus" });
        List<ScfProductConfig> list = this.selectByClassProperty(ScfProductConfig.class, anMap);
        if (Collections3.isEmpty(list)) {
            return null;
        }
        return Collections3.getFirst(list);
    }

    public ScfProductConfig findProductByCode(String anProductCode) {
        BTAssert.notNull(anProductCode, "查询失败anProductCode不能为空");
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("productCode", anProductCode);
        List<ScfProductConfig> list = this.selectByClassProperty(ScfProductConfig.class, anMap);
        if (Collections3.isEmpty(list)) {
            return null;
        }
        return Collections3.getFirst(list);
    }

    /**
     * 根据产品的编号查询保理产品信息
     * @param anProductCodes
     * @return
     */
    public List<ScfProductConfig> queryProductListByCodes(String anProductCodes) {
        BTAssert.notNull(anProductCodes, "查询失败anProductCode不能为空");
        Map<String, Object> anMap = new HashMap<String, Object>();
        String[] split = StringUtils.split(anProductCodes, ",");
        anMap.put("productCode", split);
        List<ScfProductConfig> list = this.selectByClassProperty(ScfProductConfig.class, anMap);

        return list;
    }

    /**
     * 分页查询产品
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfProductConfig> queryProduct(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "coreCustNo", "factorNo", "productCode",
                "businStatus", "GTEregDate", "LTEregDate" });

        if (null != anMap.get("businStatus")) {
            anMap.put("businStatus", anMap.get("businStatus").toString().split(","));
        }

        // 保理公司查询
        if (UserUtils.factorUser()) {
            return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
        }

        // 其它公司查询
        List<String> productCodeList = getProductCode(anMap);
        if (productCodeList.size() == 0) {
            return new Page<>(anPageNum, anPageSize);
        }

        // 输入条件有指定productCode时
        Object productCode = anMap.get("productCode");
        if (productCode != null) {
            if (productCodeList.contains(productCode.toString())) {
                anMap.put("productCode", productCode.toString());
            } else {
                return new Page<>(anPageNum, anPageSize);
            }
        } else {
            anMap.put("productCode", productCodeList.toArray());
        }

        anMap.remove("coreCustNo");
        anMap.remove("custNo");
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    private List<String> getProductCode(Map<String, Object> anMap) {
        Object custNo = anMap.get("custNo");
        String coreCustNo = anMap.get("coreCustNo") == null ? "" : anMap.get("coreCustNo").toString();
        List<String> productCodeList = new ArrayList<String>();

        // 供应商企业查询
        if (UserUtils.supplierUser() || UserUtils.sellerUser()) {
            List<Long> noList = new ArrayList<Long>();
            if (!StringUtils.isEmpty(coreCustNo)) {
                noList.add(Long.parseLong(coreCustNo));
            } else {
                // 查出该供应商关联的所有核心企业
                List<CustRelation> coreList = custRelationService.queryCoreList(Long.parseLong(custNo.toString()));
                for (CustRelation cust : coreList) {
                    noList.add(cust.getRelateCustno());
                }
            }

            // 查询产品关联的核心企业
            List<ScfProductCoreRelation> relationList = productCoreRelationService.selectByListProperty("coreCustNo",
                    noList);
            if (Collections3.isEmpty(relationList)) {
                return productCodeList;
            }

            for (ScfProductCoreRelation rel : relationList) {
                productCodeList.add(rel.getProductCode());
            }
        }

        // 核心企业查询
        else if (UserUtils.coreUser()) {
            List<ScfProductCoreRelation> relationList = productCoreRelationService.selectByProperty("coreCustNo",
                    coreCustNo);
            if (Collections3.isEmpty(relationList)) {
                return productCodeList;
            }

            for (ScfProductCoreRelation rel : relationList) {
                productCodeList.add(rel.getProductCode());
            }
        }

        return productCodeList;
    }

    public List<ScfAssetDict> findAssetDictByProduct(String anProductCode) {
        return productAssetDictRelationService.queryProductAssetDict(anProductCode);
    }

    public Page<ScfProductCoreRelation> queryCoreByProduct(String anProductCode, int anFlag, int anPageNum,
            int anPageSize) {
        return productCoreRelationService.queryProductCoreUser(anProductCode, anPageNum, anPageSize, anFlag);
    }

    public List<ScfProductCoreRelation> findCoreByProductList(String anProductCode) {
        return productCoreRelationService.findCoreListByProduct(anProductCode);
    }

    /**
     * 删除产品
     * @param anId
     * @return
     */
    public int delProductConfig(Long anId) {
        BTAssert.notNull(anId, "产品删除失败");
        ScfProductConfig product = this.selectByPrimaryKey(anId);
        Map<String, Object> anMap = QueryTermBuilder.newInstance().put("productCode", product.getProductCode()).build();
        productCoreRelationService.deleteByExample(anMap);
        productAssetDictRelationService.deleteByExample(anMap);
        return this.delete(product);
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

        // 查询核心企业关联的产品
        List<ScfProductCoreRelation> relationList = productCoreRelationService.findRelationByCore(coreCustNo);
        ArrayList<String> codeList = new ArrayList<String>();
        for (ScfProductCoreRelation relation : relationList) {
            codeList.add(relation.getProductCode());
        }

        // 查询保指定理公司的产品
        Map<String, Object> anMap = QueryTermBuilder.newInstance().put("productCode", codeList.toArray())
                .put("factorNo", anFactorNo).put("businStatus", ProductConstants.PRO_STATUS_SHELVES).build();

        List<ScfProductConfig> list = this.selectByProperty(anMap);
        for (ScfProductConfig product : list) {
            result.add(new SimpleDataEntity(product.getProductName(), String.valueOf(product.getProductCode())));
        }
        return result;
    }

    public List<ScfProductConfig> queryProductKeyAndValue(Long coreCustNo) {
        List<ScfProductConfig> result = new ArrayList<ScfProductConfig>();
        if (null == coreCustNo) {
            return result;
        }

        // 查询核心企业关联的产品
        List<ScfProductCoreRelation> relationList = productCoreRelationService.findRelationByCore(coreCustNo);
        ArrayList<String> codeList = new ArrayList<String>();
        for (ScfProductCoreRelation relation : relationList) {
            codeList.add(relation.getProductCode());
        }

        // 查询保指定理公司的产品
        Map<String, Object> anMap = QueryTermBuilder.newInstance().put("productCode", codeList.toArray())
                .put("factorNo", custRelationService.queryNoInsideFactoryByCore(coreCustNo))
                .put("businStatus", ProductConstants.PRO_STATUS_SHELVES).build();

        List<ScfProductConfig> list = this.selectByProperty(anMap);

        return list;
    }

    /**
     * 根据产品配置查询所有的配置信息
     * @param productCode
     * @return
     */
    public ScfProductConfig findProductConfigById(String productCode) {

        ScfProductConfig productConfig = findProductByCode(productCode);

        List<ScfAssetDict> assetDict = productAssetDictRelationService
                .queryProductAssetDict(productConfig.getProductCode());
        productConfig.setAssetDictList(assetDict);

        return productConfig;

    }
}