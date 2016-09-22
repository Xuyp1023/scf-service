package com.betterjr.modules.push.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.data.CustRelationData;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;
import com.betterjr.modules.param.service.ParamService;
import com.betterjr.modules.push.entity.ScfSupplierPushDetail;
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

    /***
     * 推送数据
     * @param anCoreCustNo 核心企业客户号
     * @param anSupplierCustNo 供应商企业客户号
     */
    public List<CustRelationData> pushData(Long anCoreCustNo,Long anSupplierCustNo,ScfSupplierPushDetail supplierPushDetail){
        // 得到核心企业与保理机构的关系信息
        List<CustRelationData> custRelationDataList=custRelationService.webQueryCustRelationData(anCoreCustNo,"2");
        logger.info("relationDataList:"+custRelationDataList);
        // 得到核心企业设置的机构信息
        String agencyCustNo=paramService.queryCoreParam(anCoreCustNo.toString(),supplierPushDetail.getEndDate());
        if(BetterStringUtils.isNotBlank(agencyCustNo)){
            custRelationDataList=compareCust(custRelationDataList,agencyCustNo);
        }else{
            custRelationDataList=new ArrayList<CustRelationData>();
        }
        // 再取供应商关系客户信息
        List<CustRelationData> supplierRelationDataList=custRelationService.webQueryCustRelationData(anSupplierCustNo,"0");
        // 判断该供应商接不接收资金方消息推送，允许则可推送有设置的信息
        if(paramService.checkSupplierParam(anSupplierCustNo.toString())){
            supplierRelationDataList.addAll(custRelationDataList);
        }
        // 微信发送
        for(CustRelationData relationData:supplierRelationDataList){
            send(anCoreCustNo,relationData.getRelateCustno(),supplierPushDetail);
        }
        return supplierRelationDataList;
    }
    
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
     * 微信消息发送
     * @param sendCustNo 发送客户
     * @param targetCustNo 接口客户
     */
    public void send(Long sendCustNo,Long targetCustNo,ScfSupplierPushDetail supplierPushDetail){
        final CustInfo sendCustomer = accountService.findCustInfo(sendCustNo);
        supplierPushDetail.setCustName(sendCustomer.getCustName());
        final CustOperatorInfo sendOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(sendCustNo));
        final CustOperatorInfo targetOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(targetCustNo));
        supplierPushDetail.setTragetCustName(accountService.queryCustName(targetCustNo));
        supplierPushDetail.setAcceptor(findBillAcceptor(supplierPushDetail.getOrderId()).getAcceptor());
        supplierPushDetail.setBillNo(findBillAcceptor(supplierPushDetail.getOrderId()).getBillNo());
        final Builder builder = NotificationModel.newBuilder("商业汇票开立通知", sendCustomer, sendOperator);
        builder.addParam("tragetCustName", supplierPushDetail.getTragetCustName());
        builder.addParam("custName", supplierPushDetail.getCustName());
        builder.addParam("acceptor", supplierPushDetail.getAcceptor());
        builder.addParam("billNo", supplierPushDetail.getBillNo());
        builder.addParam("disMoney", supplierPushDetail.getDisMoney());
        builder.addParam("disDate", supplierPushDetail.getDisDate());
        builder.addReceiveOperator(targetOperator.getId()); // 接收人
        notificationSendService.sendNotification(builder.build());
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
}
