package com.betterjr.modules.productconfig.sevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBankAccountService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.productconfig.dao.ScfProductConfigMapper;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.entity.ScfProductCoreRelation;

@Service
public class ScfProductConfigService extends BaseService<ScfProductConfigMapper, ScfProductConfig> {

	 
	@Autowired
	private ScfProductCoreRelationService productCoreRelationService;
	
	@Autowired
	private ScfAssetDictAttachRelationService assetDictAttachRelationService;
	
	@Autowired
	private ScfProductAssetDictRelationService productAssetDictRelationService ;
	
	@Autowired
	private CustAccountService custAccountService;
	
	 @Reference(interfaceClass = ICustMechBankAccountService.class)
    private ICustRelationService custRelationService;

	
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
        return this.selectByPrimaryKey(anConfig.getId());
    }
	
	public ScfProductConfig findProduct(Map<String, Object> anMap) {
		anMap = Collections3.filterMap(anMap, new String[]{"factorNo", "productCode", "businStatus"});
		List<ScfProductConfig> list = this.selectByClassProperty(ScfProductConfig.class, anMap);
		if(Collections3.isEmpty(list)){
			return null;
		}
        return Collections3.getFirst(list);
    }
																	
	public Page<ScfProductConfig> queryProduct(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
		anMap = Collections3.filterMap(anMap, new String[]{"custNo", "coreCustNo", "factorNo", "productCode", "businStatus", "GTEregDate", "LTEregDate"});
		Object custNo = anMap.get("custNo");
		Object coreCustNo = anMap.get("coreCustNo");
		
		//供应商企业查询
		if(custNo != null){
			List<Long> idList =  new ArrayList<Long>();
			//查出该供应商关联的所有核心企业
			List<CustRelation> coreList = custRelationService.queryCoreList(Long.parseLong(custNo.toString()));
			for (CustRelation cust : coreList) {
				idList.add(cust.getCustNo());
			}
			
			//查询产品关联的核心企业
			anMap.put("coreCustNo", idList.toArray());
			List<ScfProductCoreRelation> relationList = productCoreRelationService.selectByListProperty("coreCustNo", idList);
			idList =  new ArrayList<Long>();
			for (ScfProductCoreRelation rel : relationList) {
				idList.add(rel.getId());
			}
			
			//查询产品
			anMap.remove("custNo");
			anMap.put("id", idList.toArray());
		}
		
		//核心企业查询
		else if(coreCustNo != null){
			List<Long> idList =  new ArrayList<Long>();
			List<ScfProductCoreRelation> relationList = productCoreRelationService.selectByProperty("coreCustNo", coreCustNo.toString());
			idList =  new ArrayList<Long>();
			for (ScfProductCoreRelation rel : relationList) {
				idList.add(rel.getId());
			}
			
			//查询产品
			anMap.remove("coreCustNo");
			anMap.put("id", idList.toArray());
		}
		
		return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
    }

	public List<ScfAssetDict> findAssetDictByProduct(String anProductCode) {
		return productAssetDictRelationService.queryProductAssetDict(anProductCode);
	}

	public Page<CustInfo> webFindCoreByProduct(String anProductCode, int anFlag, int anPageNum, int anPageSize) {
		return productCoreRelationService.queryProductCoreUser(anProductCode, anPageNum, anPageSize, anFlag);
	}
	
}