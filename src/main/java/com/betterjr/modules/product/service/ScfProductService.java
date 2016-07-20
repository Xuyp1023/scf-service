package com.betterjr.modules.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.product.dao.ScfProductMapper;
import com.betterjr.modules.product.entity.ScfProduct;

@Service
public class ScfProductService extends BaseService<ScfProductMapper, ScfProduct> {

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

}
