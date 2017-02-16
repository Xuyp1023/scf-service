package com.betterjr.modules.push.service;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.customer.data.CustRelationData;
import com.betterjr.modules.enquiry.entity.ScfEnquiry;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.push.dao.ScfSupplierPushMapper;
import com.betterjr.modules.push.entity.ScfSupplierPush;
import com.betterjr.modules.push.entity.ScfSupplierPushDetail;

@Service
public class ScfSupplierPushService extends BaseService<ScfSupplierPushMapper, ScfSupplierPush> {

    private static final Logger logger = LoggerFactory.getLogger(ScfSupplierPushService.class);
    
    @Autowired
    private ScfSupplierPushDetailService supplierPushDetailService;
    @Autowired
    private ScfPushCheckService pushCheckService;
    @Autowired
    private ScfEnquiryService scfEnquiryService;
    @Autowired
    private ScfAcceptBillService scfAcceptBillService;
    @Autowired
    private ScfElecAgreementService scfElecAgreementService;
    @Autowired
    private ScfRequestService requestService;
    
    public boolean pushSupplierInfo(Long anBillId){
        boolean bool=false;
        try {
            logger.info("anBillId:"+anBillId);
            // 获取票据信息
            ScfAcceptBill scfAcceptBill=scfAcceptBillService.findAcceptBillDetailsById(anBillId);
            logger.info("scfAcceptBill:"+scfAcceptBill);
            ScfSupplierPush supplierPush=new ScfSupplierPush();
            supplierPush.initDefValue(scfAcceptBill.getCoreCustNo(),scfAcceptBill.getSupplierNo());
            this.insert(supplierPush);
            ScfSupplierPushDetail supplierPushDetail=new ScfSupplierPushDetail();
            supplierPushDetail.initDefValue(scfAcceptBill);
            supplierPushDetail.setRemark("票据信息推送");
            supplierPushDetail.setPushId(supplierPush.getId());
            // 添加发送记录
            String factors = pushCheckService.pushData(scfAcceptBill.getCoreCustNo(),scfAcceptBill.getSupplierNo(),supplierPushDetail);
            supplierPushDetail.setAgencyNo(factors);
            supplierPushDetailService.addPushDetail(supplierPushDetail);
            logger.info("添加推送信息,factors:"+factors);
            // 推送成功后调询价接口
            if(BetterStringUtils.isNotBlank(factors)){
                ScfEnquiry scfEnquiry=new ScfEnquiry();
                scfEnquiry.setCustNo(scfAcceptBill.getCoreCustNo());
                scfEnquiry.setFactors(factors);
                scfEnquiry.setOrders(supplierPushDetail.getOrderId().toString());
                scfEnquiry.setRequestType("2");
                scfEnquiry.setEnquiryMethod("1");
                logger.info("调用询价："+scfEnquiry);
                scfEnquiryService.addEnquiry(scfEnquiry);
            }
        }
        catch (Exception e) {
            logger.error("异常：",e);
            bool=false;
        }
        return bool;
    }
    
    /***
     * 修改放弃原因
     * @param anPushId 推送信息的编号
     * @param anRemarkId 放弃原因的列表 ID
     * @param anRemark   放弃
     * @return
     */
    public boolean saveSupplierInfo(Long anPushId,String anRemarkId,String anRemark){
        logger.info("入参："+anPushId+"，"+anRemarkId+"，"+anRemark);
        boolean bool=false;
        try {
            ScfSupplierPush supplierPush=this.selectByPrimaryKey(anPushId);
            supplierPush.setRemark(anRemark);
            supplierPush.setRemarkId(anRemarkId);
            return this.updateByPrimaryKey(supplierPush)>0;
        }
        catch (Exception e) {
            logger.error("异常：",e);
            bool=false;
        }
        return bool;
    }
    
    public void testPushSign(String anAppNo){
//        pushSignInfo(scfElecAgreementService.findOneElecAgreement(anAppNo));
        pushOrderInfo(requestService.findRequestDetail(anAppNo));
//        pushScfEnquiryInfo(null);
    }
    
    
    /****
     * 签约提醒
     * @param anElecAgrement
     * @return
     */
    public void pushSignInfo(ScfElecAgreement anElecAgrement){
        if(anElecAgrement!=null){
            ScfSupplierPushDetail supplierPushDetail=new ScfSupplierPushDetail();  
            try {    
                supplierPushDetail.initValue(anElecAgrement.getAppNo(), "3");
                if(pushCheckService.signSend(anElecAgrement,supplierPushDetail)){
                    supplierPushDetail.setRemark("签约提醒推送成功");
                }else{
                    supplierPushDetail.setBusinStatus("0");
                    supplierPushDetail.setRemark("签约提醒推送失败");
                }
            }
            catch (Exception e) {
                supplierPushDetail.setBusinStatus("0");
                supplierPushDetail.setRemark("签约提醒推送异常，原因："+e);
            }
            supplierPushDetailService.addPushDetail(supplierPushDetail);// 添加明细
        }
    }
    
    /**
     * 融资申请进度推送
     * @param anRequest
     */
    public void pushOrderInfo(ScfRequest anRequest){
        ScfSupplierPushDetail supplierPushDetail=new ScfSupplierPushDetail();
        try {
            supplierPushDetail.initValue(anRequest.getRequestNo(), "0");
            supplierPushDetail.setSendNo(""+anRequest.getFactorNo());
            supplierPushDetail.setReceiveNo(""+anRequest.getCustNo());
            if(pushCheckService.orderStatusSend(anRequest)){
                supplierPushDetail.setRemark("融资进度推送成功，状态："+anRequest.getTradeStatus());
            }else{
                supplierPushDetail.setBusinStatus("0");
                supplierPushDetail.setRemark("融资进度推送失败，状态："+anRequest.getTradeStatus());
            }
        }
        catch (Exception e) {
            supplierPushDetail.setBusinStatus("0");
            supplierPushDetail.setRemark("融资进度推送异常，原因："+e);
        }
        supplierPushDetailService.addPushDetail(supplierPushDetail);// 添加明细
    }

    /***
     * 推送询价状态通知
     */
    public void pushScfEnquiryInfo(Map<String, String> anMap){  
        ScfSupplierPushDetail supplierPushDetail=new ScfSupplierPushDetail();
        try {
            supplierPushDetail.initValue(anMap.get("enquiryNo"), "4");
            supplierPushDetail.setSendNo(anMap.get("sendCustNo"));
            supplierPushDetail.setReceiveNo(anMap.get("accCustNo"));
            if(pushCheckService.pushEnquirySend(anMap)){
                supplierPushDetail.setRemark("询价通知推送成功!");
            }else{
                supplierPushDetail.setRemark("询价通知推送失败!");
                supplierPushDetail.setBusinStatus("0");
            }
        }
        catch (Exception e) {
            supplierPushDetail.setBusinStatus("0");
            supplierPushDetail.setRemark("询价状态推送异常，原因："+e);
        }
        supplierPushDetailService.addPushDetail(supplierPushDetail);// 添加明细
    }
    
    
}
