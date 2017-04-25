package com.betterjr.modules.productconfig.sevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.productconfig.dao.ScfProductCoreRelationMapper;
import com.betterjr.modules.productconfig.entity.ScfProductCoreRelation;

@Service
public class ScfProductCoreRelationService extends BaseService<ScfProductCoreRelationMapper, ScfProductCoreRelation> {
	@Autowired
	private CustAccountService custAccountService;

	public ScfProductCoreRelation addRelation(ScfProductCoreRelation anDict) {
		BTAssert.notNull(anDict, "产品配置失败");
		anDict.init();
		this.insert(anDict);
		return this.selectByPrimaryKey(anDict.getId());
	}
	
	public String batchSaveRelation(String anProductCode, String anCustNo) {
		BTAssert.notNull(anProductCode, "产品配置失败");
		BTAssert.notNull(anCustNo, "产品配置失败");
		
		//删除前面已保存的关系
		this.deleteByProperty("productCode", anProductCode);
		
		//保存新的关系
		String[] arrNo = anCustNo.split(",");
		for (int i = 0; i < arrNo.length; i++) {
			ScfProductCoreRelation relation = new ScfProductCoreRelation();
			relation.setProductCode(anProductCode);
			relation.setCoreCustNo(Long.parseLong(arrNo[i]));
			relation.setCoreCustName(custAccountService.findCustInfo(relation.getCoreCustNo()).getCustName());
			relation.init();
			this.insert(relation);
		}
		return anCustNo;
	}

	public Page<CustInfo> queryProductCoreUser(String anProductCode, int anPageNum, int anPageSize, int anFlag) {
		BTAssert.notNull(anProductCode, "查询失败");

		// 根据产品与核心企业关系表 循环出 关联的核心企业
		Map<String, Object> relationMap = new HashMap<String, Object>();
		relationMap.put("productCode", anProductCode);
		List<ScfProductCoreRelation> relations = this.selectByClassProperty(ScfProductCoreRelation.class, relationMap);
		StringBuffer coreIds = new StringBuffer();
		for (ScfProductCoreRelation relation : relations) {
			coreIds.append(relation.getCoreCustNo());
		}
		
		Map<String, Object> coreMap = new HashMap<String, Object>();
		coreMap.put("custNo", coreIds.toString().split(","));
		return custAccountService.selectPropertyByPage(coreMap, anPageNum, anPageSize, 1==anFlag);
	}

	public ScfProductCoreRelation findRelation(String anProductCode, String anCustNo){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productCode", anProductCode);
		map.put("coreCustNo", anCustNo);
		List<ScfProductCoreRelation> list = this.selectByClassProperty(ScfProductCoreRelation.class, map);
		if(!Collections3.isEmpty(list)){
			return Collections3.getFirst(list);
		}
		return null;
	}
}
