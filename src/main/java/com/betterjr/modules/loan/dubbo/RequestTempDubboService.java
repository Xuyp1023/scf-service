package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.loan.IScfRequestTempService;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.loan.service.ScfRequestTempService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfRequestTempService.class)
public class RequestTempDubboService implements IScfRequestTempService {

	@Autowired
	private ScfRequestTempService requestTempService;
	
	@Override
	public String webAddRequestTemp(Map<String, Object> anMap) {
		ScfRequestTemp requestTemp = (ScfRequestTemp) RuleServiceDubboFilterInvoker.getInputObj();
		
		return AjaxObject.newOk(requestTempService.addRequestTemp(requestTemp,anMap)).toJson();
	}

	@Override
	public String webSaveModifyRequestTemp(Map<String, Object> anMap, String anRequestNo) {
		ScfRequestTemp requestTemp = (ScfRequestTemp) RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOk(requestTempService.saveModifyTemp(requestTemp, anRequestNo,anMap)).toJson();
	}

	@Override
	public String webQueryRequestTempList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
		anMap = RuleServiceDubboFilterInvoker.getInputObj();
		return AjaxObject.newOkWithPage("操作成功", requestTempService.queryTempList(anMap, anFlag, anPageNum, anPageSize)).toJson();
	}

}
