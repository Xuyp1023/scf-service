package com.betterjr.modules.productconfig.sevice;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.productconfig.dao.ScfAssetDictAttachRelationMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDictAttachRelation;

@Service
public class ScfAssetDictAttachRelationService
		extends BaseService<ScfAssetDictAttachRelationMapper, ScfAssetDictAttachRelation> {

	public ScfAssetDictAttachRelation addDict(ScfAssetDictAttachRelation anRelation) {
		BTAssert.notNull(anRelation, "产品配置失败");
		anRelation.init();
		this.insert(anRelation);
		return this.selectByPrimaryKey(anRelation.getId());
	}

	public List<ScfAssetDictAttachRelation> queryProductCoreRelation(Map<String, Object> anMap) {
		BTAssert.notNull(anMap.get("productCode"), "产品查询失败");
		return this.selectByClassProperty(ScfAssetDictAttachRelation.class, anMap);
	}
}
