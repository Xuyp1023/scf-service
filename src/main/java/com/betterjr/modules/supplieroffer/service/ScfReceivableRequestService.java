package com.betterjr.modules.supplieroffer.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.agreement.data.ScfElecAgreeStubInfo;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.customer.ICustMechBankAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustMechBankAccount;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.service.ContractLedgerService;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;
import com.betterjr.modules.push.service.ScfSupplierPushService;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;
import com.betterjr.modules.supplieroffer.dao.ScfReceivableRequestMapper;
import com.betterjr.modules.supplieroffer.data.OfferConstantCollentions;
import com.betterjr.modules.supplieroffer.data.ReceivableRequestConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfCoreProductCust;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.entity.ScfSupplierOffer;
import com.betterjr.modules.version.constant.VersionConstantCollentions;

@Service
public class ScfReceivableRequestService extends BaseService<ScfReceivableRequestMapper, ScfReceivableRequest> {

    @Autowired
    private ScfAssetService assetService;

    @Autowired
    private ScfReceivableRequestAgreementService agreementService;

    @Autowired
    private ScfSupplierOfferService offerService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService relationService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Reference(interfaceClass = ICustMechBankAccountService.class)
    private ICustMechBankAccountService custMechBankAccountService;

    @Autowired
    private CustAccountService custAccountService;

    @Autowired
    private ScfReceivableDOService receivableService;

    @Autowired
    private ScfInvoiceDOService invoiceService;

    @Autowired
    private ContractLedgerService contractLedgerService;// 合同

    @Autowired
    private ScfProductConfigService productConfigService;

    @Autowired
    private ScfCoreProductCustService productService;

    @Autowired
    private ScfElecAgreementService elecAgreementService;
    @Autowired
    private CustOperatorService operatorService;

    @Autowired
    private ScfSupplierPushService supplierPushService;
    
    @Autowired
    private ScfReceivableRequestLogService logService;

    /*
     * 模式一应收账款申请 融资资产新增
     * 
     * receivableId 存放的是应收账款id
     */
    public ScfReceivableRequest saveAddRequest(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");

        ScfAsset asset = assetService.saveAddAssetNew(anMap);
        // 将资产信息封装到融资中去
        ScfReceivableRequest request = convertAssetToReceviableRequest(asset);
        request.setReceivableRequestType("1");
        request.saveAddValue();
        fillRequestRaxInfo(request, BetterDateUtils.getNumDate());
        // 插入电子合同信息
        agreementService.saveAddCoreAgreementByRequest(request, "1");
        agreementService.saveAddPlatAgreementByRequest(request);
        this.insert(request);
        return request;

    }

    /**
     * 校验是否是可用的应付账款信息
     * 
     * @param receivableId
     * @return
     */
    public ScfReceivableDO checkVerifyReceivable(Long receivableId) {

        BTAssert.notNull(receivableId, "请选择应付账款,操作失败");
        ScfReceivableDO receivable = receivableService.selectByPrimaryKey(receivableId);
        BTAssert.notNull(receivable, "请选择应付账款,操作失败");
        checkStatus(receivable.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "请选择审核生效的应付账款进行申请");
        checkStatus(receivable.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "当前应付账款已经进行申请");
        String agreeNo = receivable.getAgreeNo();
        String invoiceNos = receivable.getInvoiceNos();
        if (StringUtils.isBlank(agreeNo)) {
            BTAssert.notNull(receivable, "请应付账款关联贸易合同");
        }
        if (StringUtils.isBlank(invoiceNos)) {
            BTAssert.notNull(receivable, "请应付账款关联发票信息");
        }
        checkReceivableContainInvoices(invoiceNos);

        // 根据贸易合同编号查询贸易合同
        ContractLedger agreement = contractLedgerService.selectOneByAgreeNo(agreeNo);
        BTAssert.notNull(agreement, "应收账款的贸易合同未找到!操作失败");
        checkStatus(agreement.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "请核准贸易合同");
        checkStatus(receivable.getCustNo() + "", agreement.getCustNo() + "", false, "应收账款和贸易合同对应的企业不一致");
        // 查找利率
        ScfSupplierOffer offer = offerService.findOffer(receivable.getCustNo(), receivable.getCoreCustNo());
        BTAssert.notNull(offer, "请通知核心企业设置融资利率,操作失败");

        ScfSupplierOffer platOffer = offerService.findOffer(receivable.getCustNo(), findPlatCustInfo());
        BTAssert.notNull(platOffer, "请通知平台设置服务费利率,操作失败");
        return receivable;

    }

    private void checkReceivableContainInvoices(String invoiceIds) {

        List<ScfInvoiceDO> invoiceList = invoiceService.queryReceivableList(invoiceIds);
        // 当一条记录都没有找到
        if (Collections3.isEmpty(invoiceList)) {
            BTAssert.notNull(null, "当前应收账款对应的发票未找到!");
        }

        // 当未找全所有的发票信息
        if (invoiceIds.split(",").length != invoiceList.size()) {
            BTAssert.notNull(null, "当前应收账款对应的发票还有未找到!");

        }

        for (ScfInvoiceDO invoiceDo : invoiceList) {
            checkStatus(invoiceDo.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "对应的发票不是生效状态");
            checkStatus(invoiceDo.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "对应的发票已经用于融资申请");

        }

    }

    /**
     * 供应商提交申请融资信息 并且生成电子合同
     * 
     * @param anRequestNo
     * @param anRequestPayDate
     * @return
     */
    public ScfReceivableRequest saveSubmitRequest(String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCustNo())) {
            BTAssert.notNull(null, "你没有当前申请的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE, false, "当前申请已不能进行再次申请");
        ScfAsset asset = assetService.saveConfirmAsset(request.getAssetId());
        request.setAsset(asset);
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST);
        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);

        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 供应商签署合同（供应商和平台的两份合同）
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveSupplierSignAgreement(String anRequestNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_NOEFFECTIVE, true, "当前申请尚未生效");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED, true, "当前申请已经完结");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_ANNUL, true, "当前申请已经完结");
        request.setSupplierSignFlag(ReceivableRequestConstantCollentions.SIGN_AGREEMENT_FLAG_YES);
        // 处理供应商电子合同和平台电子合同
        // agreementService.saveSupplierSignAgreement(request);
        //request.setElecAgreement(elecAgreementService.saveUpdateBusinStatus(request.getAgreementAppNo(), request.getCustNo(), "2"));
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 供应商签署合同之后提交信息给核心企业进行确认
     * 
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    public ScfReceivableRequest saveSupplierFinishConfirmRequest(String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCustNo())) {
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT, false, "请先签署合同，在进行提交");

        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE);
        if ("2".equals(request.getReceivableRequestType())) {

            request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_CORE_CONFIRM);

        }
        
        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getCoreCustNo());
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 融资废止操作
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveAnnulReceivableRequest(String anRequestNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCustNo())) {
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_CONFIRM_PAY, true, "资金方已经付款,无法作废");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED, true, "该融资已经结束，无法废止");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_ANNUL, true, "该融资已经废止");
        // agreementService.saveAnnulAgreement(request);
        // 更新合同的状态
        elecAgreementService.saveUpdateBusinStatus(request.getAgreementAppNo(), request.getCustNo(), "9");

        // 更新资产包状态信息
        assetService.saveRejectOrBreakAsset(request.getAssetId());
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_ANNUL);
        this.updateByPrimaryKeySelective(request);
        return request;
    }

    /**
     * 核心企业签署合同
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveCoreSignAgreement(String anRequestNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.findOneByRequestNo(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT, false, "合同只能签署一次");
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN);
        request.setCoreSignFlag(ReceivableRequestConstantCollentions.SIGN_AGREEMENT_FLAG_YES);
        request.setFactorySignFlag(ReceivableRequestConstantCollentions.SIGN_AGREEMENT_FLAG_YES);
        // 处理供应商电子合同和平台电子合同
        // agreementService.saveCoreSignAgreement(request);
        // elecAgreementService.saveUpdateBusinStatus(request.getAgreementAppNo(), request.getCoreCustNo(), "1");
        sendReceivableTopic(anRequestNo);
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 发送微信通知给供应商签署合同
     * 
     * @param anRequest
     */
    private void sendReceivableTopic(String anRequestNo) {

        if (StringUtils.isNoneBlank(anRequestNo)) {

            ScfReceivableRequest request = findOneByRequestNo(anRequestNo);
            if (request != null && request.getElecAgreement() != null) {

                ScfElecAgreementInfo agreement = (ScfElecAgreementInfo) request.getElecAgreement();
                if (!Collections3.isEmpty(agreement.getStubInfos())) {

                    for (ScfElecAgreeStubInfo info : agreement.getStubInfos()) {

                        if (info.getCustNo().equals(request.getCustNo()) && "0".equals(info.getOperStatus())) {
                            supplierPushService.pushSignInfo(agreement);

                        }

                    }

                }

            }

        }

    }

    /**
     * 核心企业完成付款操作
     * 
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    public ScfReceivableRequest saveCoreFinishPayRequest(String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.findOneByRequestNo(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCoreCustNo())) {
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN, false, "请先签署合同，再进行付款");
        if (request.getElecAgreement() == null || !"1".equals(request.getElecAgreement().getSignStatus())) {
            // 发送微信通知
            sendReceivableTopic(anRequestNo);
            BTAssert.notNull(null, "供应商尚未签署电子合同!");
        }
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED);
        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getCoreCustNo());
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    // --------------查询----------------------------------

    /**
     * 查询单条融资信息
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest findOneByRequestNo(String anRequestNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        request.setAsset(assetService.findAssetByid(request.getAssetId()));
        if (request.getCoreAgreementId() != null) {

            request.setCoreAgreement(agreementService.selectByPrimaryKey(request.getCoreAgreementId()));
        }
        if (request.getPlatAgreementId() != null) {

            request.setPlatAgreement(agreementService.selectByPrimaryKey(request.getPlatAgreementId()));
        }
        if (StringUtils.isNoneBlank(request.getAgreementAppNo())) {

            request.setElecAgreement(elecAgreementService.findElecAgreementInfo(request.getAgreementAppNo()));

        }
        return request;
    }

    /**
     * 供应商查询还有那些融资申请可以再次提交
     * 融资申请模式改变
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    @Deprecated
    public Page<ScfReceivableRequest> queryReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("custNo")) {
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "requestNo", "GTEregDate", "LTEregDate", "receivableRequestType" });
        anMap = Collections3.fuzzyMap(anMap, new String[] { "requestNo" });
        anMap.put("businStatus",
                new String[] { ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_FACTORY_SIGN_AGREEMENT });
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 供应商查询已经完结的融资信息
     * 模式一和模式二流程合并
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    @Deprecated
    public Page<ScfReceivableRequest> queryFinishReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("custNo")) {
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "GTEregDate", "LTEregDate", "GTEendDate", "LTEendDate", "receivableRequestType" });
        anMap.put("businStatus", ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_REQUEST_END);
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 核心企业查询可以提交的融资信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("coreCustNo")) {
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "requestNo", "GTEregDate", "LTEregDate", "receivableRequestType" });
        anMap = Collections3.fuzzyMap(anMap, new String[] { "requestNo" });
        anMap.put("businStatus", new String[] { ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT,
                ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN,
                ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_CONFIRM_PAY});
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 核心企业查询已经融资结束的所有的申请信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryFinishReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("coreCustNo")) {
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "GTEregDate", "LTEregDate", "GTEendDate", "LTEendDate", "receivableRequestType" });
        anMap.put("businStatus", ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED);
        // anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 计算金额，等信息
     * 
     * @param anRequest
     * @param anRequestPayDate
     *            计划付款日期
     */
    private void fillRequestRaxInfo(ScfReceivableRequest anRequest, String anRequestPayDate) {

        if (!anRequestPayDate.replaceAll("-", "").equals(anRequest.getRequestPayDate())) {

            BigDecimal balance = anRequest.getBalance();// 融资总金额
            int dayLength = daysBetween(anRequestPayDate.replaceAll("-", ""), anRequest.getEndDate());
            if (dayLength <= 0) {
                BTAssert.notNull(null, "当前时间已经超过应收账款的结算时间，操作失败");
            }
            double dayDiv = dayLength / 365;
            // 计算供应商应付核心企业的钱
            BigDecimal payBalance = balance.multiply(anRequest.getCustCoreRate()).multiply(new BigDecimal(dayLength));
            payBalance = MathExtend.divide(payBalance, new BigDecimal(36500));
            anRequest.setRequestPayBalance(balance.subtract(payBalance));
            // 计算供应商应付平台的钱
            BigDecimal payPlatBalance = balance.multiply(anRequest.getCustOpatRate()).multiply(new BigDecimal(dayLength));
            payPlatBalance = MathExtend.divide(payPlatBalance, new BigDecimal(36500));
            anRequest.setRequestPayPlatBalance(payPlatBalance);
            anRequest.setRequestPayDate(anRequestPayDate.replaceAll("-", ""));
            BigDecimal depositBalance = anRequest.getRequestPayBalance().multiply(new BigDecimal(100));
            anRequest.setDepositRate(MathExtend.divide(depositBalance, balance));
        }

    }

    /**
     * 计算日期相差的天数
     * 
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    private int daysBetween(String smdate, String bdate) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        }
        catch (ParseException ex) {

            return 0;
        }
    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * 
     * @return
     */
    private List<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        List<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

    /**
     * 将资产信息封装到融资信息中去
     * 
     * @param anAsset
     * @return
     */
    private ScfReceivableRequest convertAssetToReceviableRequest(ScfAsset anAsset) {
        ScfReceivableRequest request = new ScfReceivableRequest();
        request = convertAssetToReceviableRequest(request, anAsset);

        packageCustOpatRate(request, "1");

        // 封装服务费率
        packageCustOpatRate(request, "2");

        return request;
    }

    /**
     * 封装利率
     * 
     * @param request
     * @param requestType
     *            1 表示封装资金方利率 2 封装平台
     */
    private void packageCustOpatRate(ScfReceivableRequest request, String requestType) {

        if ("1".equals(requestType) && request.getFactoryNo() != null) {

            ScfSupplierOffer offer = offerService.findOffer(request.getCustNo(), request.getFactoryNo());
            BTAssert.notNull(offer, "请通知资金方设置融资利率,操作失败");
            request.setCustCoreRate(offer.getCoreCustRate());
        }
        else if ("2".equals(requestType)) {

            ScfSupplierOffer platOffer = offerService.findOffer(request.getCustNo(), findPlatCustInfo());
            BTAssert.notNull(platOffer, "请通知平台设置服务费利率,操作失败");
            request.setCustOpatRate(platOffer.getCoreCustRate());

        }

    }

    /**
     * 平台设置利率
     * 
     * @param request
     */
    private void packagePaltRateToRequest(ScfReceivableRequest request) {

        ScfSupplierOffer platOffer = offerService.findOffer(request.getCustNo(), findPlatCustInfo());
        BTAssert.notNull(platOffer, "请通知平台设置服务费利率,操作失败");
        request.setCustOpatRate(platOffer.getCoreCustRate());

    }

    /**
     * 将资产信息封装到申请中
     * 
     * @param request
     * @param anAsset
     * @return
     */
    private ScfReceivableRequest convertAssetToReceviableRequest(ScfReceivableRequest request, ScfAsset anAsset) {

        request.setAssetId(anAsset.getId());
        request.setBalance(anAsset.getBalance());
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE);
        request.setCoreCustName(anAsset.getCoreCustName());
        request.setCoreCustNo(anAsset.getCoreCustNo());
        request.setCustBankAccount(anAsset.getCustBankAccount());
        request.setCustBankAccountName(anAsset.getCustBankAccountName());
        request.setCustBankName(anAsset.getCustBankName());
        request.setCustName(anAsset.getCustName());
        request.setCustNo(anAsset.getCustNo());
        request.setOperOrg(baseService.findBaseInfo(anAsset.getCustNo()).getOperOrg());
        request.setOwnCompany(anAsset.getCustNo());
        request.setEndDate(anAsset.getEndDate());
        request.setAsset(anAsset);
        return request;

    }

    /**
     * 查询平台的企业信息
     * 
     * @return
     */
    public Long findPlatCustInfo() {

        return custMechBaseService.findPlatCustNo();

    }

    // 模式四 新增融资申请

    public ScfReceivableRequest saveAddRequestFour(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "新增申请失败!资料不存在");
        BTAssert.notNull(anMap.get("custNo"), "新增申请失败!客户为空");
        BTAssert.notNull(anMap.get("coreCustNo"), "新增申请失败!核心企业为空");
        BTAssert.notNull(anMap.get("custBankName"), "新增申请失败!银行帐号信息不全");
        BTAssert.notNull(anMap.get("custBankAccount"), "新增申请失败!银行帐号信息不全");
        BTAssert.notNull(anMap.get("custBankAccountName"), "新增申请失败!银行帐号信息不全");
        BTAssert.notNull(anMap.get("factoryNo"), "新增申请失败!请选择保理银行");
        BTAssert.notNull(anMap.get("invoiceList"), "新增申请失败!请选择发票");
        BTAssert.notNull(anMap.get("agreementList"), "新增申请失败!请选择合同");
        BTAssert.notNull(anMap.get("receivableList"), "新增申请失败!请选择应收账款");
        BTAssert.notNull(anMap.get("requestBalance"), "新增申请失败!填写申请金额");
        ScfReceivableRequest request = new ScfReceivableRequest();
        if (!anMap.containsKey("requestPayDate")) {
            anMap.put("requestPayDate", BetterDateUtils.getNumDate());
        }
        anMap = Collections3
                .filterMap(anMap,
                        new String[] { "description", "custBankName", "custBankAccount", "custBankAccountName", "requestBalance", "custNo",
                                "coreCustNo", "factoryNo", "orderList", "invoiceList", "agreementList", "receivableList", "acceptBillList",
                                "requestPayDate" });
        try {
            BeanUtils.populate(request, anMap);
        }
        catch (IllegalAccessException | InvocationTargetException e) {

            e.printStackTrace();
        }
        request.saveAddValue();
        request.setReceivableRequestType("4");
        ScfAsset asset = assetService.saveAddAssetFour(anMap);
        request.setAsset(asset);
        request.setAssetId(asset.getId());
        request.setBalance(asset.getBalance());
        request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
        request.setCustName(custAccountService.queryCustName(request.getCustNo()));
        // request.setFactoryNo(Long.parseLong(anMap.get("factoryNo").toString()));
        request.setFactoryName(custAccountService.queryCustName(request.getFactoryNo()));
        request.setEndDate(request.getRequestPayDate());
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.insertSelective(request);
        return request;

    }

    /**
     * 查询第四种模式的应收账款申请
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @param isCust
     *            true :供应商查询 false 核心企业查询
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestFour(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,
            boolean isCust) {

        BTAssert.notNull(anMap, "查询失败，条件为空");
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "factoryNo", "coreCustNo", "GTERegDate", "LTERegDate", "businStatus" });
        if (isCust) {
            if (!anMap.containsKey("custNo")) {
                anMap.put("custNo", getCurrentUserCustNos());
            }
        }
        else {
            if (!anMap.containsKey("coreCustNo")) {
                anMap.put("coreCustNo", getCurrentUserCustNos());
            }

        }
        anMap.put("receivableRequestType", "4");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;

    }

    /**
     * 核心企业确认
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveConfirmReceivableRequestFour(String anRequestNo) {

        BTAssert.hasText(anRequestNo, "确认失败，查询为空");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        checkStatus(request.getBusinStatus(), "0", false, "已经确认过的申请不能重复确认");
        if (!getCurrentUserCustNos().contains(request.getCoreCustNo())) {
            BTAssert.notNull(null, "你没有当前单据的操作权限");
        }
        request.setBusinStatus("1");
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKey(request);
        return request;
    }

    /**
     * 核心企业拒绝
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveRejectReceivableRequestFour(String anRequestNo) {

        BTAssert.hasText(anRequestNo, "拒绝失败，查询为空");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        checkStatus(request.getBusinStatus(), "0", false, "已经确认过的申请不能拒绝");
        if (!getCurrentUserCustNos().contains(request.getCoreCustNo())) {
            BTAssert.notNull(null, "你没有当前单据的操作权限");
        }
        request.setBusinStatus("2");
        assetService.saveRejectOrBreakAsset(request.getAssetId());
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKey(request);
        return request;
    }

    /**
     * 模式2新增融资申请
     * 
     * @param anMap
     * @return
     */
    public ScfReceivableRequest saveAddRequestTwo(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");

        ScfAsset asset = assetService.saveAddAssetNew(anMap);
        // 将资产信息封装到融资中去
        ScfReceivableRequest request = new ScfReceivableRequest();
        request = convertAssetToReceviableRequest(request, asset);
        // 设置利率
        /*
         * List<SimpleDataEntity> factoryList = queryCanFacotyList(request); //查询保理公司对供应商利率最高的值 covertFacotyHightestRate(request,factoryList);
         */

        // 设置最低利率
        covertFacotyLowestRate(request);

        // 封装平台对供应商的利率
        packagePaltRateToRequest(request);
        request.setReceivableRequestType("2");
        request.saveAddValue();
        fillRequestRaxInfo(request, BetterDateUtils.getNumDate());
        // 插入电子合同信息
        agreementService.saveAddCoreAgreementByRequest(request, "2");
        agreementService.saveAddPlatAgreementByRequest(request);
        this.insert(request);
        return request;

    }

    /**
     * 模式2 供应商提交申请
     * 
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @param factoryNo
     * @return
     */
    public ScfReceivableRequest saveSubmitRequestTwo(String anRequestNo, String anRequestPayDate, String anDescription, Long factoryNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        BTAssert.notNull(factoryNo, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCustNo())) {
            BTAssert.notNull(null, "你没有当前申请的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE, false, "当前申请已不能进行再次申请");
        ScfAsset asset = assetService.saveConfirmAsset(request.getAssetId());
        request.setAsset(asset);
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST);
        if (request.getFactoryNo() != factoryNo) {
            ScfSupplierOffer offer = offerService.findOffer(request.getCustNo(), request.getFactoryNo());
            if (offer == null || offer.getCoreCustRate() == null) {
                BTAssert.notNull(null, "当前保理公司没有设置利率，查询失败");
            }
            request.setCustCoreRate(offer.getCoreCustRate());
            request.setFactoryNo(offer.getCoreCustNo());
            request.setFactoryName(offer.getCoreCustName());
            request.setRequestPayDate("");
        }
        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);

        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 查询能够用于保理的结算中心
     * 
     * @return
     */
    public List<SimpleDataEntity> queryCanFacotyList(ScfReceivableRequest request) {

        // 查询能够查询的保理公司列表
        List<SimpleDataEntity> factoryList = relationService.queryFactoryByCore(request.getCoreCustNo());
        for (SimpleDataEntity simpleDataEntity : factoryList) {
            ScfSupplierOffer offer = offerService.findOffer(request.getCustNo(), Long.parseLong(simpleDataEntity.getValue()));
            if (offer == null || offer.getCoreCustRate() == null) {
                factoryList.remove(simpleDataEntity);
            }
        }

        if (Collections3.isEmpty(factoryList)) {
            BTAssert.notNull(null, "核心企业尚未提供结算中心用于申请");
        }

        return factoryList;

    }

    /**
     * 第五步 核心企业确认申请行为
     * 流程更改，此方法作废:供应商提交直接到资金方签署合同
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    @Deprecated
    public ScfReceivableRequest saveCoreConfrimPayRequest(String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCoreCustNo())) {
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT, false, "请先签署合同，再进行付款");
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_CORE_CONFIRM);
        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getCoreCustNo());
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 第六部结算中心签署合同
     * 
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveFactorySignAgreement(String anRequestNo) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.findOneByRequestNo(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT, false, "电子合同只能签署一次!");
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN);
        request.setFactorySignFlag(ReceivableRequestConstantCollentions.SIGN_AGREEMENT_FLAG_YES);
        // 处理供应商电子合同和平台电子合同
        // agreementService.saveFactorySignAgreement(request);
        // elecAgreementService.saveUpdateBusinStatus(request.getAgreementAppNo(), request.getFactoryNo(), "1");
        sendReceivableTopic(anRequestNo);
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 模式2 结算中心确认付款并完成申请流程
     * 
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    public ScfReceivableRequest saveFactoryConfrimPayRequest(String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.findOneByRequestNo(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getFactoryNo())) {
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN, false,
                "请先签署合同，再进行付款");
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED);

        if (request.getElecAgreement() == null || !"1".equals(request.getElecAgreement().getSignStatus())) {
            // 发送微信通知
            sendReceivableTopic(anRequestNo);
            BTAssert.notNull(null, "供应商尚未签署电子合同!");
        }
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getFactoryNo());
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        return request;

    }

    /**
     * 模式2 供应商查询还有那些融资申请可以再次提交
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestTwoWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("custNo")) {
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "requestNo", "GTEregDate", "LTEregDate", "receivableRequestType" });
        anMap = Collections3.fuzzyMap(anMap, new String[] { "requestNo" });
        anMap.put("businStatus",
                new String[] { ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT,
                        ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN,
                        ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_CONFIRM_PAY});
        // anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 模式 二供应商查询已经完结的融资信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryTwoFinishReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("custNo")) {
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "GTEregDate", "LTEregDate", "GTEendDate", "LTEendDate", "receivableRequestType" });
        anMap.put("businStatus", ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED);
        // anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 模式二核心企业查询可以提交的融资信息
     * 模式二流程修改
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    @Deprecated
    public Page<ScfReceivableRequest> queryTwoReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("coreCustNo")) {
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "requestNo", "GTEregDate", "LTEregDate", "receivableRequestType" });
        anMap = Collections3.fuzzyMap(anMap, new String[] { "requestNo" });
        anMap.put("businStatus",
                new String[] { ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_FACTORY_SIGN_AGREEMENT,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_FACTORY_PAY_CONFIRM,
                        ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_REQUEST_END });
        anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 核心企业查询已经融资结束的所有的申请信息
     * 融资模式流程修改
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    @Deprecated
    public Page<ScfReceivableRequest> queryTwoFinishReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("coreCustNo")) {
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "GTEregDate", "LTEregDate", "GTEendDate", "LTEendDate", "receivableRequestType" });
        anMap.put("businStatus", ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_REQUEST_END);
        anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 模式 2保理公司查询可以申请的申请
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryTwoReceivableRequestWithFactory(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("factoryNo")) {
            anMap.put("factoryNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "coreCustNo", "requestNo", "GTEregDate", "LTEregDate", "factoryNo" });
        anMap = Collections3.fuzzyMap(anMap, new String[] { "requestNo" });
        anMap.put("businStatus",
                new String[] { ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT,
                        ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_SIGN,
                        ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FACTORY_CONFIRM_PAY });
        anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 模式2 保理公司查询已经融资结束的所有的申请信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryTwoFinishReceivableRequestWithFactory(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("factoryNo")) {
            anMap.put("factoryNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "custNo", "coreCustNo", "GTEregDate", "LTEregDate", "GTEendDate", "LTEendDate", "factoryNo" });
        anMap.put("businStatus", ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED);
        anMap.put("receivableRequestType", "2");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }

    /**
     * 封装最高的利率结算中心
     * 
     * @param anRequest
     * @param anFactoryList
     */
    private void covertFacotyHightestRate(ScfReceivableRequest anRequest, List<SimpleDataEntity> anFactoryList) {

        BTAssert.notEmpty(anFactoryList, "核心企业尚未提供结算中心用于申请");
        List<Long> factoryNo = new ArrayList<>();
        for (SimpleDataEntity entity : anFactoryList) {
            factoryNo.add(Long.parseLong(entity.getValue()));
        }
        Map build = QueryTermBuilder.newInstance().put("coreCustNo", factoryNo).put("custNo", anRequest.getCustNo())
                .put("businStatus", OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE).build();
        List<ScfSupplierOffer> offerList = offerService.selectByProperty(build, "coreCustRate Desc");
        BTAssert.notNull(offerList, "核心企业尚未提供结算中心用于申请");
        anRequest.setCustCoreRate(Collections3.getFirst(offerList).getCoreCustRate());
        anRequest.setFactoryNo(Collections3.getFirst(offerList).getCoreCustNo());
        anRequest.setFactoryName(Collections3.getFirst(offerList).getCoreCustName());

    }

    /**
     * 设置最低利率
     * 
     * @param anRequest
     */
    private void covertFacotyLowestRate(ScfReceivableRequest anRequest) {

        List<ScfSupplierOffer> factoryList = offerService.queryAllFactoryByCustNo(anRequest.getCustNo());
        BTAssert.notNull(factoryList, "没有查到相应的资金方设置融资申请利率！");
        BTAssert.notEmpty(factoryList, "没有查到相应的资金方设置融资申请利率！");
        anRequest.setCustCoreRate(Collections3.getFirst(factoryList).getCoreCustRate());
        anRequest.setFactoryNo(Collections3.getFirst(factoryList).getCoreCustNo());
        anRequest.setFactoryName(Collections3.getFirst(factoryList).getCoreCustName());

    }

    /**
     * 整合模式申请总的入口
     * 
     * @param anMap
     * @return
     * 
     *         receivableId 应收账款id
     * 
     *         isRequestAsset 是否必须 false 不锁定 isLockedAsset 是否锁定 true 锁定
     */
    public ScfReceivableRequest saveAddRequestTotal(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");
        anMap.put(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_REQUEST_ASSET, false);
        anMap.put(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_LOCKED_ASSET, false);
        ScfAsset asset = assetService.saveAddAssetNewTotal(anMap);
        // 将资产信息封装到融资中去
        ScfReceivableRequest request = convertAssetToReceviableRequest(asset);
        // request.setReceivableRequestType("1");

        request.saveAddValue();
        // fillRequestRaxInfo(request,BetterDateUtils.getNumDate());
        // 插入电子合同信息
        // agreementService.saveAddCoreAgreementByRequest(request,"1");
        // agreementService.saveAddPlatAgreementByRequest(request);
        
        //插入日志
        logService.saveAddRequestLog(request);
        
        this.insert(request);
        return request;

    }

    /**
     * 供应商确认提交融资申请
     * 
     * @param anMap
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     *            wechaMarker 微信标记 goodsBatchNo 合同附件 statementBatchNo 发票附件
     * @return
     */
    public ScfReceivableRequest saveSubmitRequestTotal(Map<String, Object> anMap, String anRequestNo, String anRequestPayDate, String anDescription) {

        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get("requestProductCode"), "请选择保理产品");
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = findOneByRequestNo(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if (!getCurrentUserCustNos().contains(request.getCustNo())) {
            BTAssert.notNull(null, "你没有当前申请的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_NOEFFECTIVE, false, "当前申请已不能进行再次申请");
        ScfProductConfig config = productConfigService.findProductConfigById(anMap.get("requestProductCode").toString());
        BTAssert.notNull(config, "没有查询到产品配置信息");
        // 判断是否微信来源，如果微信会上传贸易合同和发票附件信息
        //if (anMap.containsKey("wechaMarker") && "1".equals(anMap.get("wechaMarker").toString())) {
        convertProdectConfigToRequestAndAsset(request, config, anMap);
        request.setBusinStatus(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_SUPPLIER_SUBMIT);
        /*
         * 整合模式一和模式2的状态
         * if ("2".equals(config.getReceivableRequestType())) {

            request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TWO_CORE_CONFIRM);

        }
         */
       /* }
        else {

            convertProdectConfigToRequestAndAsset(request, config, null);
            request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST);
        }*/
        // 设置银行账户信息
        CustMechBankAccount account = custMechBankAccountService.findCustMechBankAccount(anMap.get("custBankAccount").toString());
        BTAssert.notNull(account, "没有查到供应商银行帐号");
        request.setCustBankAccount(account.getBankAcco());
        request.setCustBankAccountName(account.getBankAccoName());
        request.setCustBankName(account.getBankName());
        // 插入电子合同信息
        ScfElecAgreement agreement = elecAgreementService.saveAddElecAgreementByReceivableRequest(request);
        request.setAgreementAppNo(agreement.getAppNo());
        request.setElecAgreement(agreement);

        ScfAsset asset = assetService.saveConfirmAsset(request.getAssetId());
        request.setAsset(asset);

        // 封装应付款钱信息
        fillRequestRaxInfo(request, anRequestPayDate);
        request.setDescription(anDescription);

        //插入日志
        logService.saveAddRequestLog(request);
        
        this.updateByPrimaryKeySelective(request);
        
        return request;

    }

    /**
     * 融资申请选择可以使用的保理产品列表
     * 
     * @param requestNo
     * @return
     */
    public List<ScfCoreProductCust> queryProductByRequestNo(String requestNo) {

        BTAssert.notNull(requestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = findOneByRequestNo(requestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        List<ScfCoreProductCust> productDescs = new ArrayList<>();
        List<ScfCoreProductCust> products = productService.queryCanUseProduct(request.getCustNo(), request.getCoreCustNo());
        for (ScfCoreProductCust product : products) {
            if (queryCheckRequestAndCode(request, product.getProductCode())) {
                productDescs.add(product);
            }
        }

        return productDescs;
    }

    public boolean queryCheckRequestAndCode(ScfReceivableRequest request, String requestProductCode) {

        if (request.getAsset() == null || request.getAsset().getId() == null) {

            request.setAsset(assetService.findAssetByid(request.getAssetId()));

        }
        ScfProductConfig config = productConfigService.findProductConfigById(requestProductCode);
        BTAssert.notNull(config, "没有查询到产品配置信息");
        boolean falg = true;
        for (ScfAssetDict assetDict : config.getAssetDictList()) {
            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)) {
                if (!request.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY)) {
                    falg = false;
                    return falg;
                }
                else {

                    List object = (List) request.getAsset().getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
                    if (Collections3.isEmpty(object)) {
                        return false;
                    }
                }
            }

            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE)) {
                if (!request.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.SCF_INVOICE_LIST_KEY)) {
                    falg = false;
                    return falg;
                }
                else {

                    List object = (List) request.getAsset().getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
                    if (Collections3.isEmpty(object)) {
                        return false;
                    }
                }
            }

            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT)) {
                if (!request.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY)) {
                    falg = false;
                    return falg;
                }
                else {
                    List object = (List) request.getAsset().getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
                    if (Collections3.isEmpty(object)) {
                        return false;
                    }
                }
            }
        }

        return falg;

    }

    /**
     * 校验当前申请和保理产品是否成功匹配
     * 
     * @param requestNo
     *            融资申请id
     * @param requestProductCode
     *            保理产品
     * @return
     */
    public boolean queryCheckRequestAndCode(String requestNo, String requestProductCode) {

        BTAssert.notNull(requestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = findOneByRequestNo(requestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        return queryCheckRequestAndCode(request, requestProductCode);

    }

    /**
     *
     * 将产品配置信息信息封装到融资请求中去
     * 
     * 
     */
    private void convertProdectConfigToRequest(ScfReceivableRequest anRequest, ScfProductConfig anConfig) {

        anRequest.setFactoryNo(anConfig.getFactorNo());
        anRequest.setFactoryName(custAccountService.queryCustName(anConfig.getFactorNo()));
        anRequest.setRequestProductCode(anConfig.getProductCode());
        anRequest.setReceivableRequestType(anConfig.getReceivableRequestType());

        packageCustOpatRate(anRequest, "1");

    }

    /**
     *
     * 将产品配置信息信息封装到融资请求中去
     * 
     * 
     */
    private void convertProdectConfigToRequestAndAsset(ScfReceivableRequest anRequest, ScfProductConfig anConfig, Map<String, Object> anMap) {

        convertProdectConfigToRequest(anRequest, anConfig);
        packageAndCheckAsset(anRequest, anConfig, anMap);

    }

    /**
     * 封装和检查资产信息
     * 
     * @param anRequest
     * @param anConfig
     * @param anMap
     */
    private void packageAndCheckAsset(ScfReceivableRequest anRequest, ScfProductConfig anConfig, Map<String, Object> anMap) {

        for (ScfAssetDict assetDict : anConfig.getAssetDictList()) {
            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)) {
                if (!anRequest.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY)) {
                    BTAssert.notNull(null, "当前融资产品需要应收账款");
                }
                else {

                    if (Collections3.isEmpty((List) anRequest.getAsset().getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY))) {
                        BTAssert.notNull(null, "当前融资产品需要应收账款");
                    }
                }
            }

            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE)) {

                if (anMap != null && anMap.containsKey("wechaMarker") && "1".equals(anMap.get("wechaMarker").toString())) {

                    // 资产上传发票附件
                    if (!anMap.containsKey("statementBatchNo") || StringUtils.isBlank(anMap.get("statementBatchNo").toString())) {
                        BTAssert.notNull(null, "当前融资产品需要商业发票");
                    }

                }
                else {

                    if (!anRequest.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.SCF_INVOICE_LIST_KEY)) {
                        BTAssert.notNull(null, "当前融资产品需要商业发票");
                    }
                    else {

                        if (Collections3.isEmpty((List) anRequest.getAsset().getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY))) {
                            BTAssert.notNull(null, "当前融资产品需要商业发票");
                        }
                    }
                }
            }

            if (assetDict.getAssetInfoType().equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT)) {

                if (anMap != null && anMap.containsKey("wechaMarker") && "1".equals(anMap.get("wechaMarker").toString())) {

                    // 资产上传发票附件
                    if (!anMap.containsKey("goodsBatchNo") || StringUtils.isBlank(anMap.get("goodsBatchNo").toString())) {
                        BTAssert.notNull(null, "当前融资产品需要贸易合同");
                    }

                }
                else {

                    if (!anRequest.getAsset().getBasedataMap().containsKey(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY)) {
                        BTAssert.notNull(null, "当前融资产品需要贸易合同");
                    }
                    else {

                        if (Collections3
                                .isEmpty((List) anRequest.getAsset().getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY))) {
                            BTAssert.notNull(null, "当前融资产品需要贸易合同");
                        }
                    }
                }

            }
        }

        // 更新附件信息
        if (anMap != null && anMap.containsKey("wechaMarker") && "1".equals(anMap.get("wechaMarker").toString())) {

            String goodsBatchNo = "";
            // 贸易合同
            if (anMap.containsKey("goodsBatchNo") && StringUtils.isNotBlank(anMap.get("goodsBatchNo").toString())) {

                goodsBatchNo = anMap.get("goodsBatchNo").toString();
                assetService.saveUpdateAssetBatchNo(anRequest.getAssetId(), goodsBatchNo, "1");
            }
            String statementBatchNo = "";
            // 发票
            if (anMap.containsKey("statementBatchNo") && StringUtils.isNotBlank(anMap.get("statementBatchNo").toString())) {

                statementBatchNo = anMap.get("statementBatchNo").toString();
                assetService.saveUpdateAssetBatchNo(anRequest.getAssetId(), statementBatchNo, "2");
            }

        }

    }

    public CustOperatorInfo findDefaultOperatorInfo(Long custNo) {

        return operatorService.findDefaultOperator(baseService.findBaseInfo(custNo).getOperOrg());

    }

    /**
     * 通过应付账款的编号和版本查询
     * 
     * @param anRefNo
     * @param version
     * @return
     */
    public ScfReceivableRequest findRequestByReceivableId(String anRefNo, String version) {

        // 查询资产
        ScfAsset asset = assetService.findAssetByReceivableRefNoWithVersion(anRefNo, version);
        BTAssert.notNull(asset, "未找到资产信息");
        // 查询融资详情
        Map anMap = QueryTermBuilder.newInstance().put("assetId", asset.getId()).build();
        List<ScfReceivableRequest> list = this.selectByProperty(anMap, "requestNo Desc");
        if (!Collections3.hasEmptyObject(list)) {
            return findOneByRequestNo(Collections3.getFirst(list).getRequestNo());
        }
        return new ScfReceivableRequest();

    }

    public void saveUpdateSupplierSignByAppNo(String anAppNo) {
        
        Map paramMap = QueryTermBuilder.newInstance()
                .put("agreementAppNo", anAppNo)
                .build();
        
        List<ScfReceivableRequest> list = this.selectByProperty(paramMap);
        if(!Collections3.isEmpty(list)){
            ScfReceivableRequest request = list.get(0);
            request.setSupplierSignFlag(ReceivableRequestConstantCollentions.SIGN_AGREEMENT_FLAG_YES);
            this.updateByPrimaryKeySelective(request);
        }
        
        
    }

}
