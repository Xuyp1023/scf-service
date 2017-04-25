package com.betterjr.modules.productconfig.sevice;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.productconfig.dao.ScfAssetDictMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;

@Service
public class ScfAssetDictService extends BaseService<ScfAssetDictMapper, ScfAssetDict> {
	
	public ScfAssetDict addDict(ScfAssetDict anDict) {
        BTAssert.notNull(anDict, "产品配置失败");
        anDict.init();
        this.insert(anDict);
        return this.selectByPrimaryKey(anDict.getId());
    }
}
