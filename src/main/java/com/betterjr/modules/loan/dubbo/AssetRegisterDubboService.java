package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IscfAssetRegisterService;
import com.betterjr.modules.loan.entity.ScfAssetCheck;
import com.betterjr.modules.loan.entity.ScfAssetRegister;
import com.betterjr.modules.loan.service.ScfAssetCheckService;
import com.betterjr.modules.loan.service.ScfAssetRegisterService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IscfAssetRegisterService.class)
public class AssetRegisterDubboService implements IscfAssetRegisterService {
	protected final Logger logger = LoggerFactory.getLogger(AssetRegisterDubboService.class);
	
	@Autowired
	private ScfAssetRegisterService assetRegisterService;
	@Autowired
	private ScfAssetCheckService assetCheckService;

	@Override
	public String webAddAssetRegister(Map<String, Object> anMap) {
		ScfAssetRegister register = (ScfAssetRegister) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk(assetRegisterService.addRegister(register)).toJson();
	}
	
	@Override
	public String webFindAssetRegister(String anRequestNo) {
		return AjaxObject.newOk(assetRegisterService.findAssestRegisterByRequestNo(anRequestNo)).toJson();
	}

	@Override
	public String webAddAssetCheck(Map<String, Object> anMap) {
		ScfAssetCheck check = (ScfAssetCheck) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk(assetCheckService.addAssetCheck(check)).toJson();
	}

	@Override
	public String webFindAssetCheck(String anRequestNo) {
		return AjaxObject.newOk(assetCheckService.findAssestCheckByRequestNo(anRequestNo)).toJson();
	}
}
