package com.betterjr.modules.productconfig.sevice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.credit.service.ScfCreditService;
import com.betterjr.modules.productconfig.dao.ScfProductCoreRelationMapper;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.entity.ScfProductCoreRelation;

@Service
public class ScfProductCoreRelationService extends BaseService<ScfProductCoreRelationMapper, ScfProductCoreRelation> {
	@Autowired
	private CustAccountService custAccountService;
	@Autowired
	private ScfCreditService scfCreditService ;
	@Autowired
	private ScfProductConfigService scfProductConfigService ;

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

	public Page<ScfProductCoreRelation> queryProductCoreUser(String anProductCode, int anPageNum, int anPageSize, int anFlag) {
		BTAssert.notNull(anProductCode, "查询失败");

		// 根据产品与核心企业关系表 循环出 关联的核心企业
		Map<String, Object> relationMap = new HashMap<String, Object>();
		relationMap.put("productCode", anProductCode);
		
		ScfProductConfig product = scfProductConfigService.findProduct(relationMap);
		
		Page<ScfProductCoreRelation> relations = this.selectPropertyByPage(relationMap, anPageNum, anPageSize, 1==anFlag);
		//List<Long> coreIds = new ArrayList<Long>();
		for (ScfProductCoreRelation relation : relations) {
			//coreIds.add(relation.getCoreCustNo());
			Map<String, Object> credictMap = scfCreditService.findCreditSumByCustNo(relation.getCoreCustNo(), product.getFactorNo());
			if(MathExtend.compareToZero(new BigDecimal(credictMap.get("creditBalance").toString()))){
				relation.setCredict("已授信");
			}
		}
		
		return relations;
		/*Map<String, Object> coreMap = new HashMap<String, Object>();
		coreMap.put("custNo", coreIds.toArray());*/
		//return custAccountService.selectPropertyByPage(coreMap, anPageNum, anPageSize, 1==anFlag);
	}
	
	public List<ScfProductCoreRelation> findCoreByProductList(String anProductCode) {
		BTAssert.notNull(anProductCode, "查询失败");

		// 根据产品与核心企业关系表 循环出 关联的核心企业
		Map<String, Object> relationMap = new HashMap<String, Object>();
		relationMap.put("productCode", anProductCode);
		return this.selectByClassProperty(ScfProductCoreRelation.class, relationMap);
	}

	public ScfProductCoreRelation findRelation(String anProductCode, String anCustNo){
		Map<String, Object> map = QueryTermBuilder.newInstance().put("productCode", anProductCode).put("coreCustNo", anCustNo).build();
		List<ScfProductCoreRelation> list = this.selectByClassProperty(ScfProductCoreRelation.class, map);
		if(!Collections3.isEmpty(list)){
			return Collections3.getFirst(list);
		}
		return null;
	}
	
	public List<ScfProductCoreRelation> findRelationByCore(Long anCustNo){
		Map<String, Object> map = QueryTermBuilder.newInstance().put("coreCustNo", anCustNo).build();
		return this.selectByClassProperty(ScfProductCoreRelation.class, map);
	}
	
}
