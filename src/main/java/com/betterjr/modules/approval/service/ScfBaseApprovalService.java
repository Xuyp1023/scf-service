package com.betterjr.modules.approval.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.credit.entity.ScfCreditInfo;
import com.betterjr.modules.credit.service.ScfCreditDetailService;
import com.betterjr.modules.enquiry.entity.ScfOffer;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

@Service
public class ScfBaseApprovalService {

    /**
     * 应收账款转让通知书/ 应收账款转让确认书/ 三方协议
     */
    public static final String AGREEMENT_TYPE_NOTICE = "0";
    public static final String AGREEMENT_TYPE_CONFIRMATION = "1";
    public static final String AGREEMENT_TYPE_PROTOCOL = "2";

    /**
     * 客户类型
     */
    public static final String REQUEST_CUST_TYPE_SUPPLY = "1";
    public static final String REQUEST_CUST_TYPE_SELLER = "2";

    @Autowired
    public ScfRequestService requestService;
    @Autowired
    public ScfOrderService orderService;
    @Autowired
    public ScfOfferService offerService;
    @Autowired
    public ScfCreditDetailService creditDetailService;
    @Autowired
    public ScfSupplierPushService supplierPushService;
    @Autowired
    private ScfEnquiryService enquiryService;

    @Autowired
    private ScfAssetService assetService;

    /**
     * 修改项目状态
     * 
     * @param anContext
     * @return
     */
    public void updateRequestStatus(String anRequestNo, String anStatus, String anLastStatus) {
        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        if (request != null) {
            request.setTradeStatus(anStatus);
            request.setLastStatus(anLastStatus);
            requestService.saveModifyRequest(request, anRequestNo);
        }
    }

    /**
     * 修改项目状态
     * 
     * @param anContext
     * @return
     */
    public ScfRequest updateRequestLastStatus(String anRequestNo, String anLastStatus) {

        ScfRequest request = requestService.selectByPrimaryKey(anRequestNo);
        // 基础资产驳回
        // assetService.saveRejectOrBreakAsset(Long.parseLong(request.getOrders()));
        if (request != null) {
            request.setLastStatus(anLastStatus);
            requestService.saveModifyRequest(request, anRequestNo);
        }
        return request;
    }

    /**
     * 流程作废
     * @param anRequestNo
     * @param anLastStatus
     */
    public void updateAnnulAsset(String anRequestNo, String anLastStatus) {

        ScfRequest request = updateRequestLastStatus(anRequestNo, anLastStatus);

        // 基础资产驳回
        assetService.saveRejectOrBreakAsset(Long.parseLong(request.getOrders()));

    }

    /**
     * 冻结资源
     * 
     * @param anRequest
     */
    public void forzenSource(ScfRequest anRequest) {
        if (null != anRequest.getOfferId()) {
            // 从报价过来的要改变报价状态
            offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "3");

            ScfOffer offer = offerService.selectByPrimaryKey(anRequest.getOfferId());
            // 从报价过来的要改变报价状态
            enquiryService.saveUpdateBusinStatus(offer.getEnquiryNo(), "-2");
        }

        // 关联订单
        orderService.saveInfoRequestNo(anRequest.getRequestType(), anRequest.getRequestNo(), anRequest.getOrders());
        // 冻结订单
        orderService.forzenInfos(anRequest.getRequestNo(), null);

        // 当融资流程启动时,冻结授信额度
        ScfCreditInfo anCreditInfo = new ScfCreditInfo();
        anCreditInfo.setBusinFlag(anRequest.getRequestType());
        anCreditInfo.setBalance(anRequest.getBalance());
        anCreditInfo.setBusinId(Long.valueOf(anRequest.getRequestNo()));
        anCreditInfo.setCoreCustNo(anRequest.getCoreCustNo());
        anCreditInfo.setCustNo(anRequest.getCustNo());
        anCreditInfo.setFactorNo(anRequest.getFactorNo());
        anCreditInfo.setCreditMode(anRequest.getCreditMode());
        anCreditInfo.setRequestNo(anRequest.getRequestNo());
        anCreditInfo.setDescription(anRequest.getDescription());
        creditDetailService.saveFreezeCredit(anCreditInfo);
    }

    /**
     * 释放资源
     * 
     * @param anRequest
     */
    public void releaseSource(ScfRequest anRequest) {
        // 当融资流程终止时 ，解除订单关联
        orderService.unForzenInfoes(anRequest.getRequestNo(), null);

        // 当融资流程终止时 ，将使用的报价改为可用状态
        if (null != anRequest.getOfferId()) {
            offerService.saveUpdateTradeStatus(anRequest.getOfferId(), "1");
        }

        // 修申请单状态为关闭
        anRequest.setTradeStatus(RequestTradeStatus.CLOSED.getCode());

        // 当融资流程终止时,对冻结的授信额度进行解冻
        ScfCreditInfo anCreditInfo = new ScfCreditInfo();
        anCreditInfo.setBusinFlag(anRequest.getRequestType());
        anCreditInfo.setBalance(anRequest.getBalance());
        anCreditInfo.setBusinId(Long.valueOf(anRequest.getRequestNo()));
        anCreditInfo.setCoreCustNo(anRequest.getCoreCustNo());
        anCreditInfo.setCustNo(anRequest.getCustNo());
        anCreditInfo.setFactorNo(anRequest.getFactorNo());
        anCreditInfo.setCreditMode(anRequest.getCreditMode());
        anCreditInfo.setRequestNo(anRequest.getRequestNo());
        anCreditInfo.setDescription(anRequest.getDescription());
        creditDetailService.saveUnfreezeCredit(anCreditInfo);
    }

    /**
     * 融资申请进度推送-发送微信提醒
     * 
     * @param anRequest
     */
    public void pushOrderInfo(ScfRequest anRequest) {
        // 如果是微信发起的流程---发送微信提醒
        // if (BetterStringUtils.equals("2", anRequest.getRequestFrom())) {
        supplierPushService.pushOrderInfo(anRequest);
        // }
    }

    public ScfRequest getReqtuest(String anRequestNo) {
        return requestService.findRequestByRequestNo(anRequestNo);
    }
}
