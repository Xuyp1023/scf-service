package com.betterjr.modules.discount.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.discount.dao.ScfDiscountMapper;
import com.betterjr.modules.discount.entity.ScfDiscount;

@Service
public class ScfDiscountService extends BaseService<ScfDiscountMapper,ScfDiscount>{

    /****
     * 添加贴现
     * @param anDiscount
     * @return
     */
    public ScfDiscount addDiscount(ScfDiscount anDiscount){
        anDiscount.initDefValue(anDiscount);
        this.insert(anDiscount);
        return anDiscount;
    }
    
    public Page<ScfDiscount> queryDiscount(Map<String, Object> anParam, int anPageNum, int anPageSize){
        Map<String, Object> map = new HashMap();
        map.put("businStatus", "1");
        if(BetterStringUtils.isNotBlank((String)anParam.get("factorNo"))){
            map.put("factorNo", anParam.get("factorNo"));
        }
        return this.selectPropertyByPage(map, anPageNum, anPageSize, "1".equals(anParam.get("flag")));
    }
    
    public ScfDiscount queryDiscountById(int discountId){
        return this.selectByPrimaryKey(discountId);
    }
    
}
