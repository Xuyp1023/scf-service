package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.loan.service.ScfRequestTempService;

/**
 * 融资批复，
 * 审批通过将临时数据复制到融资申请表；
 * 审批不通过将临时表数据状态改为草稿，打回修改；
 * 
 * @author tangzw
 *
 */
@Service
public class ScfApplicationReviewService extends ScfBaseApprovalService {
	@Autowired
	private ScfRequestTempService requestTempService;
	
	@Autowired
	private ScfRequestService requestService;

	public void processPass(Map<String, Object> anContext) {
		// 获取临时表数据
		ScfRequestTemp requestTemp = requestTempService.findRequestTemp(anContext.get("requestNo").toString());

		// 临时表复制到申请表
		ScfRequest request = new ScfRequest();
		request.setRequestNo(requestTemp.getRequestNo());
		request.setBalance(requestTemp.getBalance());
		request.setPeriod(requestTemp.getPeriod());
		request.setPeriodUnit(requestTemp.getPeriodUnit());
		request.setCustNo(requestTemp.getCustNo());
		request.setCoreCustNo(requestTemp.getCoreCustNo());
		request.setFactorNo(requestTemp.getFactorNo());
		request.setDescription(requestTemp.getDescription());
		request.setSuppBankAccount(requestTemp.getSuppBankAccount());
		request.setProductCode(requestTemp.getProductCode());
		request.setOrders(requestTemp.getOrders());
		request.setCustType(REQUEST_CUST_TYPE_SUPPLY);
		requestService.addRequest(request);
	}

	public void processReject(Map<String, Object> anContext) {
		// 获取临时表数据
		ScfRequestTemp requestTemp = requestTempService.findRequestTemp(anContext.get("requestNo").toString());
		
		//打回修改，将临时表状态改为草稿
		requestTemp.setBusinStatus("1");
		requestTempService.saveModifyTemp(requestTemp, anContext.get("requestNo").toString());
	}
}
