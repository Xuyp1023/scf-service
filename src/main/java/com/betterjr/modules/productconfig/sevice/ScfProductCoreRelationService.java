package com.betterjr.modules.productconfig.sevice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.credit.service.ScfCreditService;
import com.betterjr.modules.product.constant.ProductConstants;
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
	
	/**
	 * 批量保存产品与核心企业的关系
	 * @param anProductCode
	 * @param anTypeIdList 以字符串的方式传入（"type-id,type-id"）多个以逗号隔开
	 * @return
	 */
	public String batchSaveRelation(String anProductCode, String anTypeIdList) {
		BTAssert.notNull(anProductCode, "产品配置失败");
		BTAssert.notNull(anTypeIdList, "产品配置失败");
		
		//删除原有的的关系
		this.deleteByProperty("productCode", anProductCode);
		
		//建立新的关系
		String[] arrNo = anTypeIdList.split(",");
		for (int i = 0; i < arrNo.length; i++) {
			ScfProductCoreRelation relation = new ScfProductCoreRelation();
			relation.setProductCode(anProductCode);
			relation.setCoreCustNo(Long.parseLong(arrNo[i]));
			relation.setCoreCustName(custAccountService.findCustInfo(relation.getCoreCustNo()).getCustName());
			relation.init();
			this.insert(relation);
		}
		return anTypeIdList;
	}

	/**
	 * 分页查询产品关联的核心企业列表，用于产品详情中显示
	 * @param anProductCode
	 * @param anPageNum
	 * @param anPageSize
	 * @param anFlag
	 * @return
	 */
	public Page<ScfProductCoreRelation> queryProductCoreUser(String anProductCode, int anPageNum, int anPageSize, int anFlag) {
		BTAssert.notNull(anProductCode, "查询失败");

		// 根据产品与核心企业关系表 循环出 关联的核心企业
		Map<String, Object> relationMap = QueryTermBuilder.newInstance().put("productCode", anProductCode).build();
		ScfProductConfig product = scfProductConfigService.findProduct(relationMap);
		
		Page<ScfProductCoreRelation> relationsList = this.selectPropertyByPage(relationMap, anPageNum, anPageSize, 1==anFlag);
		for (ScfProductCoreRelation relation : relationsList) {
			Map<String, Object> credictMap = scfCreditService.findCreditSumByCustNo(relation.getCoreCustNo(), product.getFactorNo());
			if(MathExtend.compareToZero(new BigDecimal(credictMap.get("creditBalance").toString()))){
				relation.setCredict("已授信");
			}
		}
		
		return relationsList;
	}
	
	/**
	 * 查询产品关联的核心企业，用于编辑时勾选已有的关系复选框
	 * @param anProductCode
	 * @return
	 */
	public List<ScfProductCoreRelation> findCoreListByProduct(String anProductCode) {
		BTAssert.notNull(anProductCode, "查询失败");
		Map<String, Object> relationMap = QueryTermBuilder.newInstance().put("productCode", anProductCode).build();
		return this.selectByClassProperty(ScfProductCoreRelation.class, relationMap);
	}

	/**
	 * 查询指定产品与指定核心的关系对象
	 * @param anProductCode
	 * @param anCustNo
	 * @return
	 */
	public ScfProductCoreRelation findRelation(String anProductCode, String anCustNo){
		Map<String, Object> map = QueryTermBuilder.newInstance().put("productCode", anProductCode).put("coreCustNo", anCustNo).build();
		List<ScfProductCoreRelation> list = this.selectByClassProperty(ScfProductCoreRelation.class, map);
		if(!Collections3.isEmpty(list)){
			return Collections3.getFirst(list);
		}
		return null;
	}
	
	/**
	 * 查询某个核心企业关联的所有产品
	 * @param anCustNo
	 * @return
	 */
	public List<ScfProductCoreRelation> findRelationByCore(Long anCustNo){
		Map<String, Object> map = QueryTermBuilder.newInstance().put("coreCustNo", anCustNo).build();
		return this.selectByClassProperty(ScfProductCoreRelation.class, map);
	}
	
}
