package com.betterjr.modules.loan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfAssetCheckMapper;
import com.betterjr.modules.loan.entity.ScfAssetCheck;
import com.betterjr.modules.loan.entity.ScfRequest;

@Service
public class ScfAssetCheckService extends BaseService<ScfAssetCheckMapper, ScfAssetCheck> {
	
	@Autowired
	private ScfRequestService requestService;
    @Autowired
    private CustAccountService custAccountService;

    public ScfAssetCheck addAssetCheck(ScfAssetCheck anCheck) {
        BTAssert.notNull(anCheck, "保存记录失败-anCheck不能为空");
        
        if(BetterStringUtils.isNotEmpty(anCheck.getRequestNo())){
        	//删除原有的登记
        	ScfAssetCheck oldCheck = findAssestCheckByRequestNo(anCheck.getRequestNo());
        	if(oldCheck != null){
        		this.delete(oldCheck);
        	}
        }
        
        anCheck.init();
        this.insert(anCheck);
        return anCheck;
    }
    
    public ScfAssetCheck findAssestCheck(Long anId){
		BTAssert.notNull(anId, "查询失败anId不能为空");
		ScfAssetCheck check = this.selectByPrimaryKey(anId);
		ScfRequest request = requestService.findRequestByRequestNo(check.getRequestNo());
		check.setCustName(custAccountService.queryCustName(request.getCustNo()));
		return check;
	}
    
    public ScfAssetCheck findAssestCheckByRequestNo(String anRequestNo){
    	BTAssert.notNull(anRequestNo, "查询失败anId不能为空");
    	Map<String, Object> map = QueryTermBuilder.newInstance().put("requestNo", anRequestNo).build();
    	List<ScfAssetCheck> list = this.selectByClassProperty(ScfAssetCheck.class, map);
    	ScfAssetCheck check = null;
    	if(!Collections3.isEmpty(list)){
    		check = list.get(0);
    		ScfRequest request = requestService.findRequestByRequestNo(check.getRequestNo());
    		check.setCustName(custAccountService.queryCustName(request.getCustNo()));
    	}
		return check;
    }
}
