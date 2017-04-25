package com.betterjr.modules.productconfig.sevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.productconfig.dao.ScfProductAssetDictRelationMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfProductAssetDictRelation;
import com.betterjr.modules.productconfig.entity.ScfProductCoreRelation;

@Service
public class ScfProductAssetDictRelationService extends BaseService<ScfProductAssetDictRelationMapper, ScfProductAssetDictRelation> {
	
	@Autowired
	private ScfAssetDictService assetDictService;
	
	public ScfProductAssetDictRelation addDict(ScfProductAssetDictRelation anRelation) {
        BTAssert.notNull(anRelation, "产品配置失败:产品资产类型保存失败");
        anRelation.init();
        this.insert(anRelation);
        return this.selectByPrimaryKey(anRelation.getId());
    }
	
	public String batchSaveRelation(String anProductCode, String assets) {
		BTAssert.notNull(anProductCode, "资产清单保存失败");
		BTAssert.notNull(assets, "资产清单保存失败");
		
		//删除前面已保存的关系
		String[] arrNo = assets.split(",");
		for (int i = 0; i < arrNo.length; i++) {
			this.deleteByProperty("productCode", anProductCode);
		}
		
		for (int i = 0; i < arrNo.length; i++) {
			ScfProductAssetDictRelation relation = new ScfProductAssetDictRelation();
			String[] assArr = arrNo[i].split("-");
			relation.setAssestId(Long.parseLong(assArr[0]));
			relation.setAssestType(assArr[1]);
			relation.setProductCode(anProductCode);
			relation.init();
			this.insert(relation);
		}
		return assets;
	}
	
	public ScfProductAssetDictRelation findRelation(String anProductCode, Long assestId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productCode", anProductCode);
		map.put("assestId", assestId);
		List<ScfProductAssetDictRelation> list = this.selectByClassProperty(ScfProductAssetDictRelation.class, map);
		if(!Collections3.isEmpty(list)){
			return Collections3.getFirst(list);
		}
		return null;
	}
	
	public List<ScfAssetDict> queryProductAssetDict(String anProductCode) {
	    BTAssert.notNull(anProductCode, "查询失败");
	    Map<String, Object> anMap = new HashMap<String, Object>();
	    anMap.put("productCode", anProductCode);
	    
		//根据产品与资产关系表 循环出 关联的资产类型
	    List<ScfProductAssetDictRelation> relations = this.selectByClassProperty(ScfProductAssetDictRelation.class, anMap);
	    List assetDictIds = new ArrayList();
	    for (ScfProductAssetDictRelation relation : relations) {
	    	assetDictIds.add(relation.getAssestId());
		}
	    
        return assetDictService.selectByListProperty("id", assetDictIds);
   }
}
