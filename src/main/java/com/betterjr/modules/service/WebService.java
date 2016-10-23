package com.betterjr.modules.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfRequestNoticeService;
import com.betterjr.modules.enquiry.service.ScfEnquiryService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfOrderService;

@Service
public class WebService {

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfAcceptBillService billService;
    @Autowired
    private ScfEnquiryService enquiryService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfAgreementService agreementService;
    @Autowired
    private ScfRequestNoticeService requestNoticeService;
    /**
     * 4.7 融资业务申请; 1.供应商必须已经开户且状态是有效;2.供应商的融资剩余额度必须大于申请融资金额;3.供应商申请融资必须得到核心企业允许(应收账款确认)
     * 
     * @param custId
     *            保理公司客户号
     * @param unitCode
     *            拜特客户号
     * @param requestNo
     *            前海拜特申请单号
     * @param coreCustCode
     *            核心企业客户号（拜特）
     * @param productId
     *            产品ID
     * @param applicationMoney
     *            申请融资金额
     * @return
     */
    public Map<String, Object> businApp(String custId, String unitCode, String requestNo, String coreCustCode, String productId,
            BigDecimal applicationMoney) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        ScfRequest request = new ScfRequest();
        if (BetterStringUtils.isBlank(custId)) {
            return retMap;
        }
        if (BetterStringUtils.isBlank(unitCode)) {
            return retMap;
        }
        if (BetterStringUtils.isBlank(requestNo)) {
            return retMap;
        }
        if (BetterStringUtils.isBlank(coreCustCode)) {
            return retMap;
        }
        if (BetterStringUtils.isBlank(productId)) {
            return retMap;
        }

        // 对字段，requestNo需要输入吗？系统中是不能输入的
        request.setRequestNo(requestNo);
        request.setFactorNo(Long.parseLong(custId));
        request.setCustNo(Long.parseLong(unitCode));
        request.setCoreCustNo(Long.parseLong(coreCustCode));
        request.setProductId(Long.parseLong(productId));
        request.setBalance(applicationMoney);
        requestService.addRequest(request);

        // creditMoney可融资金额 在申请后就能直接得到哪到？
        retMap.put("requestNo", request.getRequestNo());
        retMap.put("loanNo", "");
        retMap.put("applayStatus", request.getTradeStatus());
        retMap.put("creditMoney", request.getApprovedBalance());
        retMap.put("remainMoney", "");
        return retMap;
    }

    /**
     * 4.8 询价或业务申请—汇票信息(业务申请时传递汇票信息); 供应商必须已经开户且状态是有效 供应商的融资剩余额度必须大于申请融资金额 供应商申请融资必须得到核心企业允许(应收账款确认)
     * 
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param businFlag
     *            业务类型 (0或不填写表示融资业务，1表示询价业务，对应是询价业务的询价单号)
     * @return
     */
    public List<Map<String, Object>> businAppDraft(String requestNo, String loanNo, String businFlag) {
        List<Map<String, Object>> billList = new ArrayList<Map<String, Object>>();
        if (BetterStringUtils.isBlank(requestNo)) {
            return billList;
        }
        if (BetterStringUtils.isBlank(loanNo)) {
            return billList;
        }

        String billsId = "";
        if (BetterStringUtils.isBlank(businFlag) || BetterStringUtils.equals("0", businFlag)) {
            ScfRequest request = requestService.findRequestDetail(requestNo);
            billsId = request.getOrders();
        }
        else {
            billsId = enquiryService.findEnquiryByNo(requestNo).getOrders();
        }

        // 只有票据一种业务吗？如果有其它的呢我们这怎么办？
        List<String> idList = BetterStringUtils.splitTrim(billsId);
        for (String billId : idList) {
            ScfAcceptBill bill = billService.selectByPrimaryKey(Long.parseLong(billId));
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("voucherNo", bill.getBillNo());// 汇票编号
            retMap.put("contractNo", bill.getAgreeNo());// 合同编号
            retMap.put("payerName", bill.getBuyer());// 付款方
            retMap.put("payerNo", bill.getBuyerNo());// 付款方客户号
            retMap.put("payerBankName", bill.getBuyerBankName());// 付款方开户行
            retMap.put("payerBankNo", bill.getBuyerBankAccount());// 付款方账号
            retMap.put("payeeName", bill.getSupplier()); // 收款方
            retMap.put("payeeBankName", bill.getSuppBankName());// 收款方开户行
            retMap.put("payeeBankNo", bill.getSuppBankAccount());// 收款方账号
            retMap.put("voucherMoney", bill.getBalance());// 票面金额
            retMap.put("ticketDate", bill.getInvoiceDate());// 出票时间
            retMap.put("acceptanceDate", bill.getEndDate());// 到期时间
            retMap.put("acceptTime", bill.getCashDate());// 承兑时间
            retMap.put("payCompany", bill.getAcceptor());// 付款单位
            retMap.put("paidCompany", bill.getRealBuyer());// 实际付款方
            billList.add(retMap);
        }

        return billList;
    }

    /**
     * 4.9 融资业务申请-发票信息(申请单信息会传递合同信息，合同信息可能为多张)1.供应商必须已经开户且状态是有效 2.供应商的融资剩余额度必须大于申请融资金额 3.供应商申请融资必须得到核心企业允许(应收账款确认)
     * 
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param businFlag
     *            业务类型 (0或不填写表示融资业务，1表示询价业务，对应是询价业务的询价单号)
     * @return
     */
    public List<Map<String, Object>> businAppReceipt(String requestNo, String loanNo, String businFlag) {
        List<Map<String, Object>> billList = new ArrayList<Map<String, Object>>();
        if (BetterStringUtils.isBlank(requestNo)) {
            return billList;
        }
        if (BetterStringUtils.isBlank(loanNo)) {
            return billList;
        }

        String billsId = "";
        if (BetterStringUtils.isBlank(businFlag) || BetterStringUtils.equals("0", businFlag)) {
            ScfRequest request = requestService.findRequestDetail(requestNo);
            billsId = request.getOrders();
        }
        else {
            billsId = enquiryService.findEnquiryByNo(requestNo).getOrders();
        }

        List<String> idList = BetterStringUtils.splitTrim(billsId);
        List<ScfInvoice> list = orderService.findRelationInfo(requestNo, ScfOrderRelationType.INVOICE);
        for (String billId : idList) {
            Map<String, Object> retMap = new HashMap<String, Object>();
            /*
             * ScfAcceptBill bill = billService.selectByPrimaryKey(Long.parseLong(billId)); Map<String, Object> anMap = new HashMap<String, Object>();
             * anMap.put("id", billId); billService.findAcceptBill(anMap ); orderService. retMap.put("invoiceNum", invoice.getInvoiceNo());//发票编号
             * retMap.put("contractNo", "");//合同编号 retMap.put("voucherNo", invoice.get);//汇票编号 retMap.put("invoiceCompany", invoice.get);//开票单位
             * retMap.put("industryType", invoice.get);//行业类别 retMap.put("item", invoice.get);//项目 retMap.put("unit", invoice.get);//单位
             * retMap.put("quantity", invoice.get);//数量 retMap.put("invoiceMoney", invoice.get);//金额
             */
        }

        return billList;
    }

    /**
     * 4.10 询价或业务申请—合同信息(申请单信息会传递合同信息，合同信息可能为多张)
     * 
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param businFlag
     *            业务类型 (0或不填写表示融资业务，1表示询价业务，对应是询价业务的询价单号)
     */
    public List<Map<String, Object>> businAppAgree(String requestNo, String loanNo, String businFlag) {
        List<Map<String, Object>> billList = new ArrayList<Map<String, Object>>();
        if (BetterStringUtils.isBlank(requestNo)) {
            return billList;
        }
        if (BetterStringUtils.isBlank(loanNo)) {
            return billList;
        }

        String billsId = "";
        if (BetterStringUtils.isBlank(businFlag) || BetterStringUtils.equals("0", businFlag)) {
            ScfRequest request = requestService.findRequestDetail(requestNo);
            billsId = request.getOrders();
        }
        else {
            billsId = enquiryService.findEnquiryByNo(requestNo).getOrders();
        }

        Map<String, Object> anMap = new HashMap<String, Object>();
        /*
         * anMap.put("id", billId); billService.findAcceptBill(anMap ); retMap.put("contractNo", );//合同编号 retMap.put("voucherNo", "");//汇票编号
         * retMap.put("agreeName", );//合同名称 retMap.put("supplyCompany", );// 供方 retMap.put("requireCompany", invoice.get);//需方
         * retMap.put("supplyLinkMan", invoice.get);//供方联系人 retMap.put("requireLinkMan", invoice.get);//需方联系人 retMap.put("agreeMoney",
         * invoice.get);//合同金额 retMap.put("supplyGoodsDate", invoice.get);// 供货时间 retMap.put("supplyGoodsPlace", invoice.get);// 供货地点
         * retMap.put("checkType", invoice.get);// 验收方式 retMap.put("supplyGoodsDate", invoice.get);// 提出异议期限 retMap.put("objectionTime",
         * invoice.get);// 供货时间 retMap.put("payeeBank", invoice.get);// 收款银行开户行 retMap.put("payeeBankNo", invoice.get);// 收款银行账号
         * retMap.put("signDate", invoice.get);// 合同签订日期 retMap.put("validStartDate", invoice.get);// 合同起止日期 retMap.put("validEndDate",
         * invoice.get);// 合同终止日期
         */

        return billList;
    }

    /**
     * 4.11 保理公司订单状态同步(当保理公司订单状态更新时，会主动推送到前海拜特平台系统)
     * 
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param applicationMoney
     *            申请融资金额
     * @param applayStatus
     *            申请单状态
     * @param closeReason
     *            状态描述
     * @param fileUrl
     *            附件信息
     * @param cashRequestNo
     *            提现申请单号
     * @param discountMoney
     *            实际融资金额
     * @param paymentMoney
     *            实际放款金额
     * @param paymentTime
     *            放款时间
     * @param rate
     *            利率（%）
     * @param repaymentTime
     *            还款截止日期
     */
    public int notice2Bytter(String requestNo, String loanNo, BigDecimal applicationMoney, String applayStatus, String closeReason, String fileUrl,
            String cashRequestNo, BigDecimal discountMoney, BigDecimal paymentMoney, String paymentTime, BigDecimal rate, String repaymentTime) {

        if (BetterStringUtils.isBlank(requestNo)) {
            return 0;
        }
        if (BetterStringUtils.isBlank(loanNo)) {
            return 0;
        }
        if (BetterStringUtils.isBlank(applayStatus)) {
            return 0;
        }

        ScfRequest anRequest = new ScfRequest();
        anRequest.setRequestNo(requestNo);
        anRequest.setFactorRequestNo(loanNo);

        // 8.待录入 1.资料收集中 2.待确认 3 待提交 4 审批中（大状态） 5 放款中12.还款中 10 交易完成 11 交易关闭
        anRequest.setTradeStatus(applayStatus);

        if (null != applicationMoney) {
            anRequest.setBalance(applicationMoney);
        }
        if (BetterStringUtils.isBlank(closeReason)) {
            anRequest.setDescription(closeReason);
        }
        if (BetterStringUtils.isBlank(fileUrl)) {
            // 这个没有呢
        }
        if (BetterStringUtils.isBlank(cashRequestNo)) {
            // 这个是干嘛用的？
        }
        if (null != discountMoney) {
            anRequest.setConfirmBalance(discountMoney);
        }
        if (null != paymentMoney) {
            anRequest.setLoanBalance(paymentMoney);
        }
        if (BetterStringUtils.isBlank(paymentTime)) {
            anRequest.setActualDate(paymentTime);
        }
        if (null != rate) {
            anRequest.setRatio(rate);
        }
        if (BetterStringUtils.isBlank(repaymentTime)) {
            anRequest.setEndDate(repaymentTime);
        }

        try {
            requestService.saveModifyRequest(anRequest, requestNo);
        }
        catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 4.12拜特订单状态同步 (当拜特方更新订单状态后，需要同步更新保理公司业务订单状态)
     * 
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param status
     *            申请单状态
     * @param updateTime更新时间
     */
    public void notice2Factor(String requestNo, String loanNo, String status, String updateTime) {
        requestService.findRequestDetail(requestNo);
    }

    /**
     * @param requestNo
     *            前海拜特申请单号
     * @param loanNo
     *            保理公司申请编号
     * @param confirmNo
     *            转让通知书编号
     * @param buyerName
     *            买方名称
     * @param agreeName
     *            合同名称
     * @param bankAccount
     *            保理公司银行账号
     * @param factorAddr
     *            保理公司详细地址
     * @param factorPost
     *            保理公司邮政编码
     * @param factorLinkMan
     *            保理公司联系人姓名
     * @param updateTime
     *            更新时间
     */
    public int transNotice2Bytter(String requestNo, String loanNo, String confirmNo, String buyerName, String agreeName, String bankAccount,
            String factorAddr, String factorPost, String factorLinkMan, String updateTime) {
        //验证参数是否为空
        String[] params = new String[] { requestNo, loanNo, confirmNo, buyerName, agreeName, bankAccount, factorAddr, factorPost, factorLinkMan, updateTime };
        for (int i = 0; i < params.length; i++) {
            if(BetterStringUtils.isBlank(params[i])){
                return 0;
            }
        }
        
        try {
            ScfRequest request = requestService.findRequestDetail(requestNo);
            ScfRequestNotice noticeRequest = requestService.getNotice(request);
            noticeRequest.setFactorRequestNo(loanNo);
            noticeRequest.setNoticeNo(confirmNo);
            noticeRequest.setBuyer(buyerName);
            noticeRequest.setAgreeName(agreeName);
            noticeRequest.setBankAccount(bankAccount);
            noticeRequest.setFactorAddr(factorAddr);
            noticeRequest.setFactorPost(factorPost);
            noticeRequest.setFactorLinkMan(factorLinkMan);
            noticeRequest.setNoticeTime(updateTime);
            
            String agreeNo = requestNoticeService.updateTransNotice(noticeRequest, request.getCustName(),request.getApprovedBalance());
            boolean isOktransCredit = agreementService.transCredit(requestService.getCreditList(request),agreeNo);
            if(agreeNo!=null && isOktransCredit){
                // 添加转让明细（因为在转让申请时就添加了 转让明细，如果核心企业不同意，那明细需要删除，但目前没有做删除这步）
                return 1;
            }else{
                return 0;
            }

           
        }
        catch (Exception e) {
            return 0;
        }
       
    }

}
