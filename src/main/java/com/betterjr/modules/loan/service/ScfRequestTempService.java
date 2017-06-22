package com.betterjr.modules.loan.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.loan.dao.ScfRequestTempMapper;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;

@Service
public class ScfRequestTempService extends BaseService<ScfRequestTempMapper, ScfRequestTemp> {

	@Autowired
	private ScfProductConfigService scfProductConfigService;
	@Autowired
	private CustAccountService custAccountService;
	
	@Autowired
    private ScfAssetService assetService;

	public ScfRequestTemp addRequestTemp(ScfRequestTemp anRequest, Map<String, Object> anMap) {
		BTAssert.notNull(anRequest, "保存失败，anRequest不能为空");
		ScfAsset asset = assetService.saveAddAsset(anMap);
		anRequest.setOrders(asset.getId()+"");
		anRequest.init();
		this.insert(anRequest);
		setName(anRequest);
		return anRequest;
	}

	public ScfRequestTemp saveModifyTemp(ScfRequestTemp anRequest, String anRequestNo, Map<String, Object> anMap) {
		BTAssert.notNull(anRequest, "修改失败，anRequest不能为空");
		BTAssert.notNull(anRequestNo, "修改失败，申请编号不能为空");
		anRequest.setRequestNo(anRequestNo);
		if(StringUtils.isNoneBlank(anRequest.getOrders())){
		    anMap.put("id", anRequest.getOrders());
		}
		ScfAsset asset = assetService.saveAddAsset(anMap);
		anRequest.setOrders(asset.getId()+"");
		anRequest.initModify("1");
		this.updateByPrimaryKeySelective(anRequest);
		anRequest  = this.selectByPrimaryKey(anRequest.getRequestNo());
		setName(anRequest);
		return anRequest;
	}
	
	public ScfRequestTemp saveModifyTemp(ScfRequestTemp anRequest, String anRequestNo) {
	    BTAssert.notNull(anRequest, "修改失败，anRequest不能为空");
	    BTAssert.notNull(anRequestNo, "修改失败，申请编号不能为空");
	    anRequest.setRequestNo(anRequestNo);
	    anRequest.initModify();
	    this.updateByPrimaryKeySelective(anRequest);
	    anRequest  = this.selectByPrimaryKey(anRequest.getRequestNo());
	    setName(anRequest);
	    return anRequest;
	}

	public Page<ScfRequestTemp> queryTempList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
		anMap.put("businStatus", new String[]{"1"});
		Page<ScfRequestTemp> list = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
		for (ScfRequestTemp temp : list) {
			setName(temp);
		}
		return list;
	}
	
	public ScfRequestTemp findRequestTemp(String anRequestNo) {
		BTAssert.notNull(anRequestNo, "查询失败,anRequestNo不能为空");
		ScfRequestTemp temp = this.selectByPrimaryKey(anRequestNo);
		setName(temp);
		return temp;
	}
	
	private void setName(ScfRequestTemp temp) {
		temp.setCustName(custAccountService.findCustInfo(temp.getCustNo()).getCustName());
		temp.setCoreCustName(custAccountService.findCustInfo(temp.getCoreCustNo()).getCustName());
		temp.setFactorName(custAccountService.findCustInfo(temp.getFactorNo()).getCustName());
		temp.setProductName(scfProductConfigService.findProductByCode(temp.getProductCode()).getProductName());
	}

}
