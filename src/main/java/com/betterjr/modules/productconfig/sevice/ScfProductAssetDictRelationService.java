package com.betterjr.modules.productconfig.sevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.productconfig.dao.ScfProductAssetDictRelationMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfProductAssetDictRelation;

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
	
	public String batchSaveRelation(String anProductCode, String listIdType) {
		BTAssert.notNull(anProductCode, "资产清单保存失败");
		BTAssert.notNull(listIdType, "资产清单保存失败");
		
		//删除原有的关系
		this.deleteByProperty("productCode", anProductCode);
		
		//建立新的关系
		String[] arrNo = listIdType.split(",");
		for (int i = 0; i < arrNo.length; i++) {
			ScfProductAssetDictRelation relation = new ScfProductAssetDictRelation();
			String[] assArr = arrNo[i].split("-");
			relation.setAssestType(assArr[0]);
			relation.setAssestId(Long.parseLong(assArr[1]));
			relation.setProductCode(anProductCode);
			relation.init();
			this.insert(relation);
		}
		return listIdType;
	}
	
	public ScfProductAssetDictRelation findRelation(String anProductCode, Long assestId){
		Map<String, Object> map = QueryTermBuilder.newInstance().put("productCode", anProductCode).put("assestId", assestId).build();
		List<ScfProductAssetDictRelation> relationlist = this.selectByClassProperty(ScfProductAssetDictRelation.class, map);
		if(!Collections3.isEmpty(relationlist)){
			return Collections3.getFirst(relationlist);
		}
		return null;
	}
	
	public List<ScfAssetDict> queryProductAssetDict(String anProductCode) {
	    BTAssert.notNull(anProductCode, "查询资产清单查询失败");
	    Map<String, Object> anMap = QueryTermBuilder.newInstance().put("productCode", anProductCode).build();
	    
		//根据产品与资产关系表 循环出 关联的资产类型
	    List<ScfProductAssetDictRelation> relations = this.selectByClassProperty(ScfProductAssetDictRelation.class, anMap);
	    List<ScfAssetDict> relationlist = new ArrayList<ScfAssetDict>();
	    for (ScfProductAssetDictRelation relation : relations) {
	    	ScfAssetDict dict = assetDictService.selectByPrimaryKey(relation.getAssestId());
	    	dict.setAssetType(relation.getAssestType());
			relationlist.add(dict);
		}
        return relationlist;
   }
}
