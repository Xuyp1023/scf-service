package com.betterjr.modules.approval.service.receivable;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestTemp;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.loan.service.ScfRequestTempService;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;

/**
 * 启动融资方申请子流程，申请临时表状态从草稿改为确认；
 * @author tangzw
 *
 */
@Service
public class ScfApplicationService extends ScfBaseApprovalService{
	@Autowired
	private ScfRequestTempService requestTempService;
	
	@Autowired
	private ScfRequestService requestService;
	@Autowired
	private ScfProductConfigService productConfigService;
	@Autowired
    private ScfAssetService assetService;
	
	public ScfRequestTemp savApplication(Map<String, Object> anContext){
		ScfRequestTemp temp = requestTempService.findRequestTemp(anContext.get("requestNo").toString());
		temp.setBusinStatus("2");
		requestTempService.saveModifyTemp(temp, anContext.get("requestNo").toString());
		return temp;
	}
	
	/**
	 * 将临时表数据保存到申请表中
	 * @param anTemp
	 * @return
	 */
	public ScfRequest savRequest(ScfRequestTemp anTemp){
	    assetService.saveConfirmAsset(Long.parseLong(anTemp.getOrders()));
		ScfRequest request = new ScfRequest();
		request.setRequestNo(anTemp.getRequestNo());
		request.setLoanNo(anTemp.getRequestNo());
		request.setBalance(anTemp.getBalance());
		request.setPeriod(anTemp.getPeriod());
		request.setPeriodUnit(anTemp.getPeriodUnit());
		request.setCustNo(anTemp.getCustNo());
		request.setCustName(anTemp.getCustName());
		request.setCoreCustNo(anTemp.getCoreCustNo());
		request.setFactorNo(anTemp.getFactorNo());
		request.setProductCode(anTemp.getProductCode());
		request.setDescription(anTemp.getDescription());
		request.setSuppBankAccount(anTemp.getSuppBankAccount());
		request.setOrders(anTemp.getOrders());
		ScfProductConfig product = productConfigService.findProductByCode(request.getProductCode());
		request.setProductId(product.getId());
		requestService.insert(request);
		requestService.fillCustName(request);
		return request;
	}
	

}
