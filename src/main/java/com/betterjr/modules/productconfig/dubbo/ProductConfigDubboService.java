package com.betterjr.modules.productconfig.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.productconfig.IScfProductConfigService;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfAssetDictAttachRelation;
import com.betterjr.modules.productconfig.entity.ScfProductAssetDictRelation;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.sevice.ScfAssetDictAttachRelationService;
import com.betterjr.modules.productconfig.sevice.ScfAssetDictService;
import com.betterjr.modules.productconfig.sevice.ScfProductAssetDictRelationService;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;
import com.betterjr.modules.productconfig.sevice.ScfProductCoreRelationService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfProductConfigService.class)
public class ProductConfigDubboService implements IScfProductConfigService {
	protected final Logger logger = LoggerFactory.getLogger(ProductConfigDubboService.class);

	@Autowired
	private ScfProductConfigService productConfigService;
	@Autowired
	private ScfProductAssetDictRelationService productAssetDictRelationService;
	@Autowired
	private ScfAssetDictAttachRelationService assetDictAttachRelationService;
	@Autowired
	private ScfProductCoreRelationService productCoreRelationService;
	@Autowired
	private ScfAssetDictService assetDictService;

	@Override
	public String webAddProductConfig(Map<String, Object> anMap) {
		logger.debug("保存产品入参：" + anMap);
		ScfProductConfig productConfig = (ScfProductConfig) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk("操作成功", productConfigService.addConfig(productConfig)).toJson();
	}
	
	@Override
	public String webSaveModifyProductConfig(Map<String, Object> anMap, Long anId) {
		logger.debug("修改产品入参：" + anMap);
		ScfProductConfig productConfig = (ScfProductConfig) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk("操作成功", productConfigService.saveModifyConfig(productConfig, anId)).toJson();
	}

	@Override
	public String webQueryProductConfig(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
		logger.debug("分页查询产品列表，入参：" + anMap);
		anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOkWithPage("分页查询产品列表", productConfigService.queryProduct(anMap, anFlag, anPageNum, anPageSize)).toJson();
	}

	@Override
	public String webFindProductConfig(String productCode) {
		logger.debug("查询产品配置：" + productCode);
		Map<String, Object> anMap = new HashMap<String, Object>();
		anMap.put("productCode", productCode);
		return AjaxObject.newOk("操作成功", productConfigService.findProduct(anMap)).toJson();
	}

	@Override
	public String webAddProductAssetDictRelation(String anProductCode, String anAssets) {
		logger.debug("保存产品与资产类型关系：" + anProductCode);
		//ScfProductAssetDictRelation anRelation = (ScfProductAssetDictRelation) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk("操作成功", productAssetDictRelationService.batchSaveRelation(anProductCode, anAssets)).toJson();
	}

	@Override
	public String webAddAssetDictAttachRelation(Map<String, Object> anMap) {
		logger.debug("保存资产类型与附件类型关系：" + anMap);
		ScfAssetDictAttachRelation anRelation = (ScfAssetDictAttachRelation) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk("操作成功", assetDictAttachRelationService.addDict(anRelation)).toJson();
	}

	@Override
	public String webFindAssetDictAttachRelationList(Map<String, Object> anMap) {
		logger.debug("查询资产类型与附件类型关系列表：" + anMap);
		List list = productAssetDictRelationService.selectByClassProperty(ScfProductAssetDictRelation.class, anMap);
		return AjaxObject.newOk("操作成功", list).toJson();
	}

	@Override
	public String webAddProductCoreRelation(String anProductCode, String anCustNo) {
		logger.debug("保存产品与核心企业关系");
		return AjaxObject.newOk("操作成功", productCoreRelationService.batchSaveRelation(anProductCode, anCustNo)).toJson();
	}

	@Override
	public String webFindAssetDict(Long factorNo) {
		logger.debug("查询资产类型列表" + factorNo);
		Map<String, Object> anMap = new HashMap<String, Object>();
		anMap.put("businStatus", 1);
		return AjaxObject.newOk("操作成功", assetDictService.selectByClassProperty(ScfAssetDict.class, anMap)).toJson();
	}
	
	@Override
	public String webQueryAssetDict(Long factorNo, int anFlag, int anPageNum, int anPageSize) {
		logger.debug("查询资产类型列表" + factorNo);
		Map<String, Object> anMap = new HashMap<String, Object>();
		anMap.put("businStatus", 1);
		return AjaxObject.newOkWithPage("操作成功", assetDictService.selectPropertyByPage(ScfAssetDict.class, anMap, anPageNum, anPageSize, 1==anFlag)).toJson();
	}

	@Override
	public String webFindAssetDictByProduct(String anProductCode) {
		return AjaxObject.newOk("操作成功", productConfigService.findAssetDictByProduct(anProductCode)).toJson();
	}
	
	@Override
	public String webQueryCoreByProduct(String anProductCode, int anFlag, int anPageNum, int anPageSize) {
		return AjaxObject.newOkWithPage("操作成功", productConfigService.webFindCoreByProduct(anProductCode, anFlag, anPageNum, anPageSize)).toJson();
	}

}
