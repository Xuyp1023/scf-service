package com.betterjr.modules.loan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.BillRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;

@Service
public class ScfBillRequestService extends BaseService<ScfRequestMapper, ScfRequest> {
    
    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfAcceptBillService billService;
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private ScfProductService productService;
    
    /**
     * 新增融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest addBillRequest(ScfRequest anRequest) {
        BTAssert.notNull(anRequest, "新增融资申请失败-anRequest不能为空");
        anRequest.setRequestType(RequestType.BILL.getCode());
        anRequest.setPeriodUnit(1);
        anRequest.setRequestDate(BetterDateUtils.getNumDate());
        anRequest.setTradeStatus(RequestTradeStatus.REQUEST.getCode());
        return requestService.addRequest(anRequest);
    }
    
    /**
     * 查询融资申请列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<BillRequest> queryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfRequest> page = requestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize);
        
        //将融资列表的分页信息拷贝到新的列表中
        Page<BillRequest> billPage = new Page<>(page.getPageNum(), page.getPageSize(), page.isCount());
        billPage.setTotal(page.getTotal());
        billPage.setPageSizeZero(page.getPageSizeZero());
        billPage.setReasonable(page.getReasonable());
        
        //组装
        for (ScfRequest scfRequest : page) {
            BillRequest transRequest = fillInfo(scfRequest);
            transRequest.setRequest(scfRequest);
            billPage.add(transRequest);
        }
        return billPage;
    }

    private BillRequest fillInfo(ScfRequest anRequest) {
        BillRequest transRequest = new BillRequest();
        if(!BetterStringUtils.isBlank(anRequest.getOrders())){
            transRequest.setBill(billService.selectByPrimaryKey(Long.parseLong(anRequest.getOrders())));
        }
        
        ScfPayPlan plan = payPlanService.findPayPlanByRequest(anRequest.getRequestNo());
        if(null != plan){
            transRequest.setPlan(plan);
        }
        
        ScfProduct product = productService.findProductById(anRequest.getProductId());
        if(null != product){
            anRequest.setProductName(product.getProductName());
        }
        transRequest.setRequest(anRequest);
        return transRequest;
    }
    
    /**
     * 根据申请编号查询融资申请
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public BillRequest findRequestByNo(String anRequestNo) {
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        return fillInfo(request);
    }
    
    /**
     * 根据票据id查询融资申请
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public BillRequest findRequestByBill(String anBillsId) {
        List<ScfRequest> list = requestService.selectByClassProperty(ScfRequest.class, "orders", anBillsId);
        return fillInfo(Collections3.getFirst(list));
    }
}
