package com.betterjr.modules.loan.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestTempMapper;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;

@Service
public class ScfRequestTempService extends BaseService<ScfRequestTempMapper, ScfRequestTemp> {

	@Autowired
	private ScfProductConfigService scfProductConfigService;
	@Autowired
	private CustAccountService custAccountService;

	public ScfRequestTemp addRequestTemp(ScfRequestTemp anRequest) {
		BTAssert.notNull(anRequest, "保存失败，anRequest不能为空");
		anRequest.init();
		this.insert(anRequest);
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
		temp.setFactorName(custAccountService.findCustInfo(temp.getFactorNo()).getCustName());
		temp.setProductName(scfProductConfigService.findProductByCode(temp.getProductCode()).getProductName());
	}

}
