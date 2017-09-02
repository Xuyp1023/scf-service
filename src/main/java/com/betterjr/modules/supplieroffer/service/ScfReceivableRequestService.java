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
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.service.ContractLedgerService;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;
import com.betterjr.modules.supplieroffer.dao.ScfReceivableRequestMapper;
import com.betterjr.modules.supplieroffer.data.ReceivableRequestConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.entity.ScfSupplierOffer;
import com.betterjr.modules.version.constant.VersionConstantCollentions;

@Service
public class ScfReceivableRequestService extends BaseService<ScfReceivableRequestMapper, ScfReceivableRequest>{

    
    @Autowired
    private ScfAssetService assetService;
    
    @Autowired
    private ScfReceivableRequestAgreementService agreementService;
    
    @Autowired
    private ScfSupplierOfferService offerService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    @Autowired
    private CustAccountService custAccountService;
    
    @Autowired
    private ScfReceivableDOService receivableService;
    
    @Autowired
    private ScfInvoiceDOService invoiceService;
    
    @Autowired
    private ContractLedgerService contractLedgerService;//合同
    
    /*
     * 模式一应收账款申请
     * 融资资产新增
     * 
     * receivableId 存放的是应收账款id
     */
    public ScfReceivableRequest saveAddRequest(Map<String,Object> anMap){
        
        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");
        
        ScfAsset asset = assetService.saveAddAssetNew(anMap);
        //将资产信息封装到融资中去
        ScfReceivableRequest request=convertAssetToReceviableRequest(asset);
        request.setReceivableRequestType("1");
        request.saveAddValue();
        fillRequestRaxInfo(request,BetterDateUtils.getNumDate());
        //插入电子合同信息
        agreementService.saveAddCoreAgreementByRequest(request);
        agreementService.saveAddPlatAgreementByRequest(request);
        this.insert(request);
        return request;
        
    }
    
    /**
     * 校验是否是可用的应付账款信息
     * @param receivableId
     * @return
     */
    public ScfReceivableDO checkVerifyReceivable(Long receivableId){
        
        BTAssert.notNull(receivableId, "请选择应付账款,操作失败");
        ScfReceivableDO receivable = receivableService.selectByPrimaryKey(receivableId) ;
        BTAssert.notNull(receivable, "请选择应付账款,操作失败");
        checkStatus(receivable.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "请选择审核生效的应付账款进行申请");
        checkStatus(receivable.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "当前应付账款已经进行申请");
        String agreeNo = receivable.getAgreeNo();
        String invoiceNos = receivable.getInvoiceNos();
        if(StringUtils.isBlank(agreeNo)){
            BTAssert.notNull(receivable, "请应付账款关联贸易合同");
        }
        if(StringUtils.isBlank(invoiceNos)){
            BTAssert.notNull(receivable, "请应付账款关联发票信息");
        }
        checkReceivableContainInvoices(invoiceNos);
        
        //根据贸易合同编号查询贸易合同
        ContractLedger agreement = contractLedgerService.selectOneByAgreeNo(agreeNo);
        BTAssert.notNull(agreement, "应收账款的贸易合同未找到!操作失败");
        checkStatus(agreement.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "请核准贸易合同");
        checkStatus(receivable.getCustNo()+"", agreement.getCustNo()+"", false, "应收账款和贸易合同对应的企业不一致");
        //查找利率
        ScfSupplierOffer offer = offerService.findOffer(receivable.getCustNo(), receivable.getCoreCustNo());
        BTAssert.notNull(offer, "请通知核心企业设置融资利率,操作失败");
        
        ScfSupplierOffer platOffer = offerService.findOffer(receivable.getCustNo(), findPlatCustInfo());
        BTAssert.notNull(platOffer, "请通知平台设置服务费利率,操作失败");
        return  receivable;
        
    }
    
    private void checkReceivableContainInvoices(String invoiceIds){
        
        
        List<ScfInvoiceDO> invoiceList = invoiceService.queryReceivableList(invoiceIds);
        //当一条记录都没有找到
        if(Collections3.isEmpty(invoiceList)){
            BTAssert.notNull(null, "当前应收账款对应的发票未找到!"); 
        }
        
        //当未找全所有的发票信息
        if(invoiceIds.split(",").length != invoiceList.size()){
            BTAssert.notNull(null, "当前应收账款对应的发票还有未找到!"); 
            
        }
        
        for (ScfInvoiceDO invoiceDo : invoiceList) {
            checkStatus(invoiceDo.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "对应的发票不是生效状态");
            checkStatus(invoiceDo.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, false, "对应的发票已经用于融资申请");
            
        }
        
    }
    
    
    
    /**
     * 供应商提交申请融资信息  并且生成电子合同
     * @param anRequestNo
     * @param anRequestPayDate
     * @return
     */
    public ScfReceivableRequest saveSubmitRequest(String anRequestNo,String anRequestPayDate,String anDescription){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if(!getCurrentUserCustNos().contains(request.getCustNo())){
            BTAssert.notNull(null, "你没有当前申请的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE, false, "当前申请已不能进行再次申请");
        ScfAsset asset = assetService.saveConfirmAsset(request.getAssetId());
        request.setAsset(asset);
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST);
        //封装应付款钱信息
        fillRequestRaxInfo(request,anRequestPayDate);
        request.setDescription(anDescription);
        
        this.updateByPrimaryKeySelective(request);
        return request;
        
    }
    
    /**
     * 供应商签署合同（供应商和平台的两份合同）
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveSupplierSignAgreement(String anRequestNo){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST, false, "请在合同提交之后签署合同信息");
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT);
        //处理供应商电子合同和平台电子合同
        agreementService.saveSupplierSignAgreement(request);
        this.updateByPrimaryKeySelective(request);
        return request;
        
        
    }
    
    /**
     * 供应商签署合同之后提交信息给核心企业进行确认
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    public ScfReceivableRequest saveSupplierFinishConfirmRequest(String anRequestNo,String anRequestPayDate,String anDescription){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if(!getCurrentUserCustNos().contains(request.getCustNo())){
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT, false, "请先签署合同，在进行提交");
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE);
        //封装应付款钱信息
        fillRequestRaxInfo(request,anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getCoreCustNo());
        this.updateByPrimaryKeySelective(request);
        return request;
        
    }
    
    /**
     * 融资废止操作
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveAnnulReceivableRequest(String anRequestNo){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if(!getCurrentUserCustNos().contains(request.getCustNo())){
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT, true, "核心企业已经签署合同，进行付款操作,无法作废");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM, true, "核心企业已经付款,无法作废");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_END, true, "该融资已经结束，无法废止");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_ANNUL, true, "该融资已经废止");
        //更新资产包状态信息
        assetService.saveRejectOrBreakAsset(request.getAssetId());
        //更新合同的状态
        agreementService.saveAnnulAgreement(request);
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_ANNUL);
        this.updateByPrimaryKeySelective(request);
        return request;
    }
    
    /**
     * 核心企业签署合同
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveCoreSignAgreement(String anRequestNo){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE, false, "电子合同只能签署一次!");
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT);
        //处理供应商电子合同和平台电子合同
        agreementService.saveCoreSignAgreement(request);
        this.updateByPrimaryKeySelective(request);
        return request;
        
        
    }
    
    /**
     * 核心企业完成付款操作
     * @param anRequestNo
     * @param anRequestPayDate
     * @param anDescription
     * @return
     */
    public ScfReceivableRequest saveCoreFinishPayRequest(String anRequestNo,String anRequestPayDate,String anDescription){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        if(!getCurrentUserCustNos().contains(request.getCoreCustNo())){
            BTAssert.notNull(null, "你没有当前处理的权限,操作失败");
        }
        checkStatus(request.getBusinStatus(), ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT, false, "请先签署合同，再进行付款");
        request.setBusinStatus(ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_END);
        //封装应付款钱信息
        fillRequestRaxInfo(request,anRequestPayDate);
        request.setDescription(anDescription);
        request.setOwnCompany(request.getCoreCustNo());
        this.updateByPrimaryKeySelective(request);
        return request;
        
    }
    
    //--------------查询----------------------------------
    
    /**
     * 查询单条融资信息
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest findOneByRequestNo(String anRequestNo){
        
        BTAssert.notNull(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        request.setAsset(assetService.findAssetByid(request.getAssetId()));
        request.setCoreAgreement(agreementService.selectByPrimaryKey(request.getCoreAgreementId()));
        request.setPlatAgreement(agreementService.selectByPrimaryKey(request.getPlatAgreementId()));
        return request;
    }
    
    /**
     * 供应商查询还有那些融资申请可以再次提交
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap,"查询条件为空,操作失败");
        if(!anMap.containsKey("custNo")){
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap=Collections3.filterMapEmptyObject(anMap);
        anMap=Collections3.filterMap(anMap, new String[]{"custNo","coreCustNo","requestNo","GTEregDate","LTEregDate","receivableRequestType"});
        anMap=Collections3.fuzzyMap(anMap, new String[]{"requestNo"});
        anMap.put("businStatus", new String[]{ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUBMIT_REQUEST
                ,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_SUPPLIER_SIGN_AGREEMENT
                ,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE
                ,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT
                ,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_PAY_CONFIRM
                ,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_END});
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }
    
    /**
     * 供应商查询已经完结的融资信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryFinishReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap,"查询条件为空,操作失败");
        if(!anMap.containsKey("custNo")){
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap=Collections3.filterMapEmptyObject(anMap);
        anMap=Collections3.filterMap(anMap, new String[]{"custNo","coreCustNo","GTEendDate","LTEendDate","receivableRequestType"});
        anMap.put("businStatus", ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_END);
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }
    
    /**
     * 核心企业查询可以提交的融资信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap,"查询条件为空,操作失败");
        if(!anMap.containsKey("coreCustNo")){
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap=Collections3.filterMapEmptyObject(anMap);
        anMap=Collections3.filterMap(anMap, new String[]{"custNo","coreCustNo","requestNo","GTEregDate","LTEregDate","receivableRequestType"});
        anMap=Collections3.fuzzyMap(anMap, new String[]{"requestNo"});
        anMap.put("businStatus", new String[]{ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_TRANSFER_AGREEMENT_CORE,ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_CORE_SIGN_AGREEMENT});
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }
    
    /**
     * 核心企业查询已经融资结束的所有的申请信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequest> queryFinishReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap,"查询条件为空,操作失败");
        if(!anMap.containsKey("coreCustNo")){
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap=Collections3.filterMapEmptyObject(anMap);
        anMap=Collections3.filterMap(anMap, new String[]{"custNo","coreCustNo","GTEendDate","LTEendDate","receivableRequestType"});
        anMap.put("businStatus", ReceivableRequestConstantCollentions.OFFER_BUSIN_STATUS_REQUEST_END);
        anMap.put("receivableRequestType", "1");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
    }
    
    /**
     * 计算金额，等信息
     * @param anRequest
     * @param anRequestPayDate  计划付款日期
     */
    private void fillRequestRaxInfo(ScfReceivableRequest anRequest, String anRequestPayDate) {
        
        
        
        if(!anRequestPayDate.replaceAll("-", "").equals(anRequest.getRequestPayDate())){
            
            BigDecimal balance = anRequest.getBalance();//融资总金额
            int dayLength = daysBetween(anRequestPayDate.replaceAll("-", ""), anRequest.getEndDate());
            if(dayLength<=0){
                BTAssert.notNull(null, "当前时间已经超过应收账款的结算时间，操作失败"); 
            }
            double dayDiv=dayLength/365;
            //计算供应商应付核心企业的钱
            BigDecimal payBalance = balance.multiply(anRequest.getCustCoreRate()).multiply(new BigDecimal(dayLength));
            payBalance=MathExtend.divide(payBalance, new BigDecimal(36500));
            anRequest.setRequestPayBalance(balance.subtract(payBalance));
            //计算供应商应付平台的钱
            BigDecimal payPlatBalance = balance.multiply(anRequest.getCustOpatRate()).multiply(new BigDecimal(dayLength));
            payPlatBalance=MathExtend.divide(payPlatBalance, new BigDecimal(36500));
            anRequest.setRequestPayPlatBalance(payPlatBalance);
            anRequest.setRequestPayDate(anRequestPayDate.replaceAll("-", ""));
        }
        
    }
    
    /**
     * 计算日期相差的天数
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    private int daysBetween(String smdate,String bdate) {
        
        try{
            
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
            Calendar cal = Calendar.getInstance();    
            cal.setTime(sdf.parse(smdate));    
            long time1 = cal.getTimeInMillis();                 
            cal.setTime(sdf.parse(bdate));    
            long time2 = cal.getTimeInMillis();         
            long between_days=(time2-time1)/(1000*3600*24);  
            
            return Integer.parseInt(String.valueOf(between_days));     
        }catch(ParseException ex){
            
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
     * @return
     */
    private List<Long> getCurrentUserCustNos(){
        
        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        List<Long> custNos=new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return  custNos;
    }

    /**
     * 将资产信息封装到融资信息中去
     * @param anAsset
     * @return
     */
    private ScfReceivableRequest convertAssetToReceviableRequest(ScfAsset anAsset) {
        ScfReceivableRequest request=new ScfReceivableRequest();
        request=convertAssetToReceviableRequest(request, anAsset);
        ScfSupplierOffer offer = offerService.findOffer(anAsset.getCustNo(), anAsset.getCoreCustNo());
        BTAssert.notNull(offer, "请通知核心企业设置融资利率,操作失败");
        request.setCustCoreRate(offer.getCoreCustRate());
        
        ScfSupplierOffer platOffer = offerService.findOffer(anAsset.getCustNo(), findPlatCustInfo());
        BTAssert.notNull(platOffer, "请通知平台设置服务费利率,操作失败");
        request.setCustOpatRate(platOffer.getCoreCustRate());
        
        return request;
    }
    private ScfReceivableRequest convertAssetToReceviableRequest(ScfReceivableRequest request,ScfAsset anAsset){
        
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
     * @return
     */
    public Long findPlatCustInfo(){
        
        return custMechBaseService.findPlatCustNo();
        
    }
    
    //  模式四   新增融资申请
    
    public ScfReceivableRequest saveAddRequestFour(Map<String,Object> anMap){
        
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
        ScfReceivableRequest request=new ScfReceivableRequest();
        if(!anMap.containsKey("requestPayDate")){
            anMap.put("requestPayDate", BetterDateUtils.getNumDate());
        }
        anMap=Collections3.filterMap(anMap, new String[]{"description","custBankName","custBankAccount","custBankAccountName","requestBalance","custNo","coreCustNo","factoryNo","orderList",
                "invoiceList","agreementList","receivableList","acceptBillList","requestPayDate"});
        try {
            BeanUtils.populate(request, anMap);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            
            e.printStackTrace();
        }
        request.saveAddValue();
        request.setReceivableRequestType("4");
        ScfAsset asset=assetService.saveAddAssetFour(anMap);
        request.setAsset(asset);
        request.setAssetId(asset.getId());
        request.setBalance(asset.getBalance());
        request.setCoreCustName(custAccountService.queryCustName(request.getCoreCustNo()));
        request.setCustName(custAccountService.queryCustName(request.getCustNo()));
        //request.setFactoryNo(Long.parseLong(anMap.get("factoryNo").toString()));
        request.setFactoryName(custAccountService.queryCustName(request.getFactoryNo()));
        request.setEndDate(request.getRequestPayDate());
        this.insertSelective(request);
        return request;
        
    }
    
    /**
     * 查询第四种模式的应收账款申请
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @param isCust true :供应商查询    false  核心企业查询
     * @return
     */
    public Page<ScfReceivableRequest> queryReceivableRequestFour(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean isCust){
        
        BTAssert.notNull(anMap, "查询失败，条件为空");
        anMap=Collections3.filterMapEmptyObject(anMap);
        anMap=Collections3.filterMap(anMap, new String[]{"custNo","factoryNo","coreCustNo","GTERegDate","LTERegDate","businStatus"});
        if(isCust){
            if(!anMap.containsKey("custNo")){
                anMap.put("custNo", getCurrentUserCustNos());   
            }
        }else{
            if(!anMap.containsKey("coreCustNo")){
                anMap.put("coreCustNo", getCurrentUserCustNos());   
            }
           
        }
        anMap.put("receivableRequestType", "4");
        Page<ScfReceivableRequest> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "requestNo desc");
        return page;
        
    }
    
    /**
     * 核心企业确认
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveConfirmReceivableRequestFour(String anRequestNo){
        
        BTAssert.hasText(anRequestNo, "确认失败，查询为空");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        checkStatus(request.getBusinStatus(), "0", false, "已经确认过的申请不能重复确认");
        if(!getCurrentUserCustNos().contains(request.getCoreCustNo())){
            BTAssert.notNull(null,"你没有当前单据的操作权限");
        }
        request.setBusinStatus("1");
        this.updateByPrimaryKey(request);
        return request;
    }
    
    /**
     * 核心企业拒绝
     * @param anRequestNo
     * @return
     */
    public ScfReceivableRequest saveRejectReceivableRequestFour(String anRequestNo){
        
        BTAssert.hasText(anRequestNo, "拒绝失败，查询为空");
        ScfReceivableRequest request = this.selectByPrimaryKey(anRequestNo);
        checkStatus(request.getBusinStatus(), "0", false, "已经确认过的申请不能拒绝");
        if(!getCurrentUserCustNos().contains(request.getCoreCustNo())){
            BTAssert.notNull(null,"你没有当前单据的操作权限");
        }
        request.setBusinStatus("2");
        assetService.saveRejectOrBreakAsset(request.getAssetId());
        this.updateByPrimaryKey(request);
        return request;
    }
    
    /**
     * 模式2新增融资申请
     * @param anMap
     * @return
     */
    public ScfReceivableRequest saveAddRequestTwo(Map<String,Object> anMap){
        
        BTAssert.notNull(anMap, "融资信息为空,操作失败");
        BTAssert.notNull(anMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");
        
        ScfAsset asset = assetService.saveAddAssetNew(anMap);
        //将资产信息封装到融资中去
        ScfReceivableRequest request=new ScfReceivableRequest();
        request=convertAssetToReceviableRequest(request,asset);
        //设置利率
        
        request.setReceivableRequestType("2");
        request.saveAddValue();
        fillRequestRaxInfo(request,BetterDateUtils.getNumDate());
        //插入电子合同信息
        agreementService.saveAddCoreAgreementByRequest(request);
        agreementService.saveAddPlatAgreementByRequest(request);
        this.insert(request);
        return request;
        
    }
    
}
