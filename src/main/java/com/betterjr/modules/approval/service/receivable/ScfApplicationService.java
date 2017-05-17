package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.loan.service.ScfRequestTempService;

/**
 * 启动融资方申请子流程，申请临时表状态从草稿改为确认；
 * @author tangzw
 *
 */
@Service
public class ScfApplicationService extends ScfBaseApprovalService{
	@Autowired
	private ScfRequestTempService requestTempService;
	
	public ScfRequestTemp savApplication(Map<String, Object> anContext){
		ScfRequestTemp temp = requestTempService.findRequestTemp(anContext.get("requestNo").toString());
		temp.setBusinStatus("2");
		requestTempService.saveModifyTemp(temp, anContext.get("requestNo").toString());
		return temp;
	}
	

}
