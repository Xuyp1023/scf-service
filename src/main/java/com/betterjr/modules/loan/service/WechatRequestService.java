package com.betterjr.modules.loan.service;

import java.util.HashMap;
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
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.enquiry.entity.ScfEnquiryObject;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.ScfEnquiryObjectService;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.BillRequest;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;

@Service
public class WechatRequestService extends BaseService<ScfRequestMapper, ScfRequest> {
    
    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfAcceptBillService billService;
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private ScfProductService productService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfEnquiryService enquiryService;
    @Autowired
    private ScfEnquiryObjectService enquiryObjectService;
    
    /**
     * 新增融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest saveRequest(ScfRequest anRequest) {
        BTAssert.notNull(anRequest, "新增融资申请失败-anRequest不能为空");
        //anRequest.setRequestType(RequestType.BILL.getCode());
        anRequest.setTradeStatus(RequestTradeStatus.REQUEST.getCode());
        anRequest.setPeriodUnit(1);
        anRequest.setRequestDate(BetterDateUtils.getNumDate());
        anRequest.setTradeStatus(RequestTradeStatus.REQUEST.getCode());
        
        //保存申请
        requestService.addRequest(anRequest);
        // 关联订单
        orderService.saveInfoRequestNo(anRequest.getRequestType(), anRequest.getRequestNo(), anRequest.getOrders());
        // 冻结订单
        orderService.forzenInfos(anRequest.getRequestNo(), null);
        
        if(null != anRequest.getOfferId()){
            //从报价过来的要改变报价状态
            offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "3");
            
            ScfOffer offer = offerService.selectByPrimaryKey(anRequest.getOfferId());
            //从报价过来的要改变报价状态
            Map<String, Object> anMap = new HashMap<String, Object>();
            anMap.put("enquiryNo", offer.getEnquiryNo());
            anMap.put("offerId", offer.getId());
            ScfEnquiryObject enquiryObj = enquiryObjectService.find(anMap);
            if(null != enquiryObj){
                enquiryObj.setBusinStatus("-2");
                enquiryObjectService.saveModify(enquiryObj);
            }
           
            enquiryService.saveUpdateBusinStatus(offer.getEnquiryNo(), "-2");
        }

        return anRequest;
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
