package com.betterjr.modules.push.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mapper.CustDecimalJsonSerializer;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.credit.entity.ScfCredit;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.data.CustRelationData;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;
import com.betterjr.modules.param.service.ParamService;
import com.betterjr.modules.push.data.RequestTradeStatusType;
import com.betterjr.modules.push.entity.ScfSupplierPushDetail;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.wechat.dubboclient.CustWeChatDubboClientService;
/***
 * 推送条件检查
 * @author hubl
 *
 */
@Service
public class ScfPushCheckService {
    
    private static final Logger logger = LoggerFactory.getLogger(ScfPushCheckService.class);

    @Reference(interfaceClass=ICustRelationService.class)
    private ICustRelationService custRelationService;
    @Autowired
    private ParamService paramService;
    @Autowired
    private CustAccountService accountService;
    
    @Reference(interfaceClass = INotificationSendService.class)
    public INotificationSendService notificationSendService;
    @Autowired
    private CustOperatorService custOperatorService;
    @Autowired
    private ScfAcceptBillService acceptBillService;
    @Autowired
    private CustWeChatDubboClientService wechatClientService;

    /***
     * 推送数据
     * @param anCoreCustNo 核心企业客户号
     * @param anSupplierCustNo 供应商企业客户号
     */
    public String pushData(Long anCoreCustNo,Long anSupplierCustNo,ScfSupplierPushDetail supplierPushDetail){
       // 得到核心企业设置的机构信息
        billSend(anCoreCustNo,anSupplierCustNo,supplierPushDetail);
        return paramService.queryCoreParam(anCoreCustNo.toString(),supplierPushDetail.getEndDate());
    }
    
//    /***
//     * 推送数据
//     * @param anCoreCustNo 核心企业客户号
//     * @param anSupplierCustNo 供应商企业客户号
//     */
//    public List<CustRelationData> pushData(Long anCoreCustNo,Long anSupplierCustNo,ScfSupplierPushDetail supplierPushDetail){
//        // 得到核心企业与保理机构的关系信息
//        List<CustRelationData> custRelationDataList=custRelationService.webQueryCustRelationData(anCoreCustNo,"2");
//        logger.info("relationDataList:"+custRelationDataList);
//        // 得到核心企业设置的机构信息
//        String agencyCustNo=paramService.queryCoreParam(anCoreCustNo.toString(),supplierPushDetail.getEndDate());
//        if(BetterStringUtils.isNotBlank(agencyCustNo)){
//            custRelationDataList=compareCust(custRelationDataList,agencyCustNo);
//        }else{
//            custRelationDataList=new ArrayList<CustRelationData>();
//        }
//        // 再取供应商关系客户信息
//        List<CustRelationData> supplierRelationDataList=custRelationService.webQueryCustRelationData(anSupplierCustNo,"0");
//        // 判断该供应商接不接收资金方消息推送，允许则可推送有设置的信息
//        if(paramService.checkSupplierParam(anSupplierCustNo.toString())){
//            supplierRelationDataList.addAll(custRelationDataList);
//        }
//        // 微信发送
//        for(CustRelationData relationData:supplierRelationDataList){
//            Long custNo=null;
//            if(BetterStringUtils.equalsIgnoreCase(relationData.getRelateType(), "2")){
//                custNo=relationData.getRelateCustno();
//            }else if(BetterStringUtils.equalsIgnoreCase(relationData.getRelateType(), "0")){
//                custNo=relationData.getCustNo();
//            }
//            if(!billSend(anCoreCustNo,custNo,supplierPushDetail)){
//                supplierRelationDataList=new ArrayList<CustRelationData>();
//                break;
//            }
//        }
//        return supplierRelationDataList;
//    }
    
    /***
     * 比较设置需要推送的机构信息
     * @param custRelationDataList
     * @param agencyCustNo
     * @return
     */
    public List<CustRelationData> compareCust(List<CustRelationData> custRelationDataList,String agencyCustNo){
        List<CustRelationData> relationList=new ArrayList<CustRelationData>();
        String[] agencyArr=agencyCustNo.split(",");
        for(CustRelationData relationData:custRelationDataList){
            if(Arrays.asList(agencyArr).contains(relationData.getRelateCustno())){
                relationList.add(relationData);
            }
        }
        return relationList;
    }
    
    /****
     * 微信票据消息发送
     * @param sendCustNo 发送客户
     * @param targetCustNo 接收客户
     */
    public boolean billSend(Long sendCustNo,Long targetCustNo,ScfSupplierPushDetail supplierPushDetail){
        logger.info("sendCustNo:"+sendCustNo+",targetCustNo:"+targetCustNo+",supplierPushDetail:"+supplierPushDetail);
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(sendCustNo);
        logger.info("sendCustomer:"+sendCustomer);
        supplierPushDetail.setCustName(sendCustomer.getCustName());
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(sendCustNo));
        logger.info("sendOperator:"+sendOperator);
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(targetCustNo));
        logger.info("targetOperator:"+targetOperator);
        if(sendOperator!=null && targetOperator!=null){
            supplierPushDetail.setTragetCustName(accountService.queryCustName(targetCustNo));
            final Builder builder = NotificationModel.newBuilder("商业汇票开立通知", sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("tragetCustName", supplierPushDetail.getTragetCustName());
            builder.addParam("billId", supplierPushDetail.getOrderId());
            builder.addParam("custName", supplierPushDetail.getCustName());
            builder.addParam("acceptor", supplierPushDetail.getAcceptor());
            builder.addParam("billNo", supplierPushDetail.getBillNo());
            builder.addParam("disMoney", supplierPushDetail.getDisMoney());
            builder.addParam("disDate", supplierPushDetail.getDisDate());
            builder.addReceiver(targetCustNo, null);  // 接收人
            logger.info("builder:"+builder);
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("billSend 消息发送标识  bool："+bool);
        }
        logger.info("发送成功");
        return bool;
    }
    
    /****
     * 发送签约提醒
     * @param elecAgreement
     * @return
     */
    public boolean signSend(ScfElecAgreement elecAgreement,ScfSupplierPushDetail anSupplierPushDetail){
        boolean bool=false;
        try {
            final CustInfo sendCustomer = accountService.findCustInfo(Long.parseLong(elecAgreement.getFactorNo()));
            final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(Long.parseLong(elecAgreement.getFactorNo())));
            CustOperatorInfo targetOperator=null;
            CustInfo targetCustomer=null;
            if(BetterStringUtils.equalsIgnoreCase(elecAgreement.getAgreeType(), "0") || BetterStringUtils.equalsIgnoreCase(elecAgreement.getAgreeType(), "6") || BetterStringUtils.equalsIgnoreCase(elecAgreement.getAgreeType(), "7")){
                targetCustomer = accountService.findCustInfo(elecAgreement.getSupplierNo());
                targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(elecAgreement.getSupplierNo()));
            }else if(BetterStringUtils.equalsIgnoreCase(elecAgreement.getAgreeType(), "1")){
                targetCustomer = accountService.findCustInfo(elecAgreement.getBuyerNo());
                targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(elecAgreement.getBuyerNo()));
            }
            if(sendOperator!=null && targetOperator!=null){
                anSupplierPushDetail.setSendNo(elecAgreement.getFactorNo());
                anSupplierPushDetail.setReceiveNo(targetCustomer.getCustNo()+"");
                anSupplierPushDetail.setBeginDate(elecAgreement.getRegDate());
                anSupplierPushDetail.setEndDate(BetterDateUtils.addStrDays(elecAgreement.getRegDate(),3));
                
                final Builder builder = NotificationModel.newBuilder("签约提醒", sendCustomer, sendOperator);
                builder.addParam("appId", wechatClientService.getAppId());
                builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
                builder.addParam("appNo", elecAgreement.getAppNo());
                builder.addParam("supplierName", targetCustomer.getName());
                builder.addParam("factorName", sendCustomer.getCustName());
                builder.addParam("regDate", getDisDate(elecAgreement.getRegDate()));
                builder.addParam("agreeName", elecAgreement.getAgreeName());
                builder.addParam("endDate", getDisDate(BetterDateUtils.addStrDays(elecAgreement.getRegDate(),3)));
                
                builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
                bool=notificationSendService.sendNotification(builder.build());
                logger.info("signSend 消息发送标识  bool："+bool);
            }
        }
        catch (Exception e) {
            logger.error("发送签约提醒异常："+e.getMessage());
        }
        return bool;
    }
    
    /****
     * 融资状态推送
     * @return
     */
    public boolean orderStatusSend(ScfRequest anRequest){
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(anRequest.getFactorNo());
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anRequest.getFactorNo()));
        final CustInfo targetCustomer = accountService.findCustInfo(anRequest.getCustNo());
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anRequest.getCustNo()));
        if(sendOperator!=null && targetOperator!=null){
            final Builder builder = NotificationModel.newBuilder("融资进度提醒", sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("requestNo", anRequest.getRequestNo());
            builder.addParam("supplierName", targetCustomer.getName());
            builder.addParam("factorName", sendCustomer.getCustName());
            builder.addParam("requestDate", getDisDate(anRequest.getRegDate()));
            builder.addParam("balance", getDisMoney(anRequest.getBalance()));
            builder.addParam("tradeStatus", RequestTradeStatusType.checking(anRequest.getTradeStatus()).getTitle());
            builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("orderStatusSend 消息发送标识  bool："+bool);
        }
        return bool;
    }
    
    /****
     * 询价答复通知
     * @param anMap
     * @return
     */
    public boolean pushEnquirySend(Map<String,String> anMap){
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(Long.parseLong(anMap.get("sendCustNo").toString()));
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(Long.parseLong(anMap.get("sendCustNo").toString())));
        final CustInfo targetCustomer = accountService.findCustInfo(Long.parseLong(anMap.get("accCustNo").toString()));
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(Long.parseLong(anMap.get("accCustNo").toString())));
        if(sendOperator!=null && targetOperator!=null){
            final Builder builder = NotificationModel.newBuilder("询价单答复通知", sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("enquiryNo", anMap.get("enquiryNo"));
            builder.addParam("productName",anMap.get("productName"));
            builder.addParam("description", anMap.get("description"));
            String balance=BetterStringUtils.isNotBlank(anMap.get("balance"))?getDisMoney(new BigDecimal(anMap.get("balance").toString())):"";
            builder.addParam("balance",balance);
            String offerTime=BetterStringUtils.isNotBlank(anMap.get("offerTime"))?getDisDate(anMap.get("offerTime").toString()):"";
            builder.addParam("offerTime",offerTime);
            builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("pushEnquirySend 消息发送标识  bool："+bool);
        }
        
        return bool;
    }
    
    /***
     * 查询票据承兑人
     * @param billId
     * @return
     */
    public ScfAcceptBill findBillAcceptor(Long billId){
        Map<String, Object> anMap=new HashMap<String, Object>();
        anMap.put("id", billId);
        ScfAcceptBill acceptBill=Collections3.getFirst(acceptBillService.findAcceptBill(anMap));
        BTAssert.notNull(acceptBill,"未找到票据信息");
        return acceptBill;
    }
    public String getDisDate(final String date) {
        return BetterDateUtils.formatDate(BetterDateUtils.parseDate(date), "yyyy年MM月dd日");
    }
    public String getDisMoney(final BigDecimal money) {
        return "￥"+CustDecimalJsonSerializer.format(money);
    }
    
    
    /****
     * 推送应收账款通知
     * @param anMap
     * @return
     */
    public boolean pushReceivableSend(ScfReceivableDO anReceivableDo,String anType){
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(anReceivableDo.getCoreCustNo());
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anReceivableDo.getCoreCustNo()));
        final CustInfo targetCustomer = accountService.findCustInfo(anReceivableDo.getCustNo());
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anReceivableDo.getCustNo()));
        if(sendOperator!=null && targetOperator!=null){
            final Builder builder = NotificationModel.newBuilder(anType, sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("coreCustName", anReceivableDo.getCoreCustName());
            builder.addParam("receivableNo",anReceivableDo.getReceivableNo());
            builder.addParam("balance",getDisMoney(anReceivableDo.getBalance()));
            builder.addParam("endDate", getDisDate(anReceivableDo.getEndDate()));
            builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("pushReceivableSend 消息发送标识  bool："+bool);
        }
        return bool;
    }
    
    
    /****
     * 发送验证通知信息
     * @param anMap
     * @return
     */
    public boolean pushVerifySend(Map<String,Object> anMap){
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(Long.parseLong(anMap.get("coreCustNo").toString()));
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(Long.parseLong(anMap.get("coreCustNo").toString())));
        final CustInfo targetCustomer = accountService.findCustInfo(Long.parseLong(anMap.get("custNo").toString()));
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(Long.parseLong(anMap.get("custNo").toString())));
        if(sendOperator!=null && targetOperator!=null){
            final Builder builder = NotificationModel.newBuilder("验证通过提醒", sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("coreCustName", anMap.get("coreCustName"));
            builder.addParam("operName",anMap.get("operName"));
            builder.addParam("dateTime",BetterDateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH时mm分"));
            builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("pushVerifySend 消息发送标识  bool："+bool);
        }
        return bool;
    }
    
    
    /****
     * 授信成功通知信息
     * @param anMap
     * @return
     */
    public boolean pushCreditSend(ScfCredit anScfCredit){
        boolean bool=false;
        final CustInfo sendCustomer = accountService.findCustInfo(anScfCredit.getFactorNo());
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anScfCredit.getFactorNo()));
        final CustInfo targetCustomer = accountService.findCustInfo(anScfCredit.getCustNo());
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(anScfCredit.getCustNo()));
        if(sendOperator!=null && targetOperator!=null){
            final Builder builder = NotificationModel.newBuilder("授信成功通知", sendCustomer, sendOperator);
            builder.addParam("appId", wechatClientService.getAppId());
            builder.addParam("wechatUrl", wechatClientService.getWechatUrl());
            builder.addParam("factorName", anScfCredit.getFactorName());
            builder.addParam("creditLimit",getDisMoney(anScfCredit.getCreditLimit()));
            builder.addParam("startDate",getDisDate(anScfCredit.getStartDate()));
            builder.addParam("endDate",getDisDate(anScfCredit.getEndDate()));
            builder.addReceiver(targetCustomer.getCustNo(), null);  // 接收人
            bool=notificationSendService.sendNotification(builder.build());
            logger.info("pushCreditSend 消息发送标识  bool："+bool);
        }
        return bool;
    }
}
