package com.betterjr.modules.agreement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterWebServiceException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.dao.ScfElecAgreementMapper;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.product.service.ScfProductService;

/***
 * 电子合同管理
 * @author hubl
 *
 */
@Service
public class ScfElecAgreementService extends BaseService<ScfElecAgreementMapper, ScfElecAgreement>{
    
    @Autowired
    private CustAccountService custAccoService;
    @Autowired
    private ScfElecAgreeStubService scfElecAgreeStubService;
    @Reference(interfaceClass=ICustFileService.class)
    private ICustFileService custFileService;
    @Autowired
    private ScfFactorRemoteHelper remoteHelper;
    @Autowired
    private ScfProductService productService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfRequestService requestService; 
    @Reference(interfaceClass=ICustFileService.class)
    protected ICustFileService fileItemService;
    /**
     * 查找供应商的电子合同信息
     * 
     * @param anGTEsignDate
     *            起始日期
     * @param anLTEsignDate
     *            截止日期
     * @param anSignStatus
     *            签约状态
     * @param anPageNum
     *            页码
     * @param anPageSize
     *            每页大小
     * @param anCountFlag
     *            是否需要统计
     * @return
     */
    public Page<ScfElecAgreementInfo> queryScfElecAgreementList(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        Map<String, Object> termMap = new HashMap();
        if(BetterStringUtils.isNotBlank((String)anParam.get("GTEregDate")) && BetterStringUtils.isNotBlank((String)anParam.get("LTEregDate"))){
            termMap.put("GTEregDate", anParam.get("GTEregDate"));
            termMap.put("LTEregDate", anParam.get("LTEregDate"));
        }
        String signStatus = (String) anParam.get("signStatus");
        anPageSize = MathExtend.defIntBetween(anPageSize, 2, ParamNames.MAX_PAGE_SIZE, 25);
        if (BetterStringUtils.isBlank(signStatus)) {
            termMap.put("signStatus", Arrays.asList("0","1", "2","3"));
        }
        else {
            termMap.put("signStatus", signStatus);
        }
        if(UserUtils.coreUser()){
            termMap.put("agreeType", Arrays.asList("1","2"));
            termMap.put("buyerNo", anParam.get("custNo"));
        }else if(UserUtils.supplierUser()){
            termMap.put("agreeType", Arrays.asList("0"));
            termMap.put("supplierNo", anParam.get("custNo"));
        }else if(UserUtils.sellerUser()){
            termMap.put("agreeType", Arrays.asList("2"));
            termMap.put("supplierNo", anParam.get("custNo"));
        }
        List<Long> custNoList = UserUtils.findCustNoList();
        if (logger.isInfoEnabled()){
            logger.info("this is findScfElecAgreementList params " + termMap);
        }
        Page<ScfElecAgreementInfo> elecAgreeList = this.selectPropertyByPage(ScfElecAgreementInfo.class, termMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")));
        Set<Long> custNoSet = new HashSet(custNoList);
        for (ScfElecAgreementInfo elecAgree : elecAgreeList) {
            initAgreemtnBill(elecAgree, custNoSet);
        }
        logger.info("this is findScfElecAgreementList result count :" + elecAgreeList.size());
        return elecAgreeList;
    }
    
    /***
     * 根据requstNo获取产品名称
     * @param requestNo
     * @return
     */
    public String getProductNameByRequestNo(String requestNo){
        try {
            ScfRequest request=requestService.findRequestDetail(requestNo);
            return productService.findProductById(request.getProductId()).getProductName();            
        }
        catch (Exception e) {
            logger.error("getProductNameByRequestNo 异常："+e.getMessage());
        }
        return "";
    }
    
    /****
     * 根据申请单号查询票据信息
     * @param anRequestNo
     * @return
     */
    public ScfAcceptBill findBillInfoByRequestNo(String anRequestNo){
        List billList=orderService.findInfoListByRequest(anRequestNo, "2");
        return (ScfAcceptBill)Collections3.getFirst(billList);
    }
    
    
    /**
     * 根据融资申请订单号和电子合同类型，获得不同合同信息
     * 
     * @param anRequestNo
     *            融资申请业务订单号
     * @param anSignType
     *            电子合同类型
     * @return
     */
    public List<ScfElecAgreementInfo> findElecAgreeByOrderNo(String anRequestNo, String anSignType) {
        Map workCondition = new HashMap();
        if(BetterStringUtils.isNotBlank(anSignType)){
            workCondition.put("agreeType", anSignType);
        }
        List<Long> custNoList = UserUtils.findCustNoList();
        workCondition.put("requestNo", anRequestNo);
        List<ScfElecAgreementInfo> elecAgreeList=this.selectByClassProperty(ScfElecAgreementInfo.class, workCondition);
        Set<Long> custNoSet = new HashSet(custNoList);
        for (ScfElecAgreementInfo elecAgree : elecAgreeList) {
            initAgreemtnBill(elecAgree,custNoSet);
        }
        return elecAgreeList;
    }
    
    private boolean saveSignFileInfo(String anAppNo, CustFileItem anFileItem, boolean anSignedFile) {
        ScfElecAgreement tmpElecAgree = this.selectByPrimaryKey(anAppNo);
        return saveSignFileInfo(tmpElecAgree, anFileItem, anSignedFile);
    }
    
    public boolean saveSignFileInfo(ScfElecAgreement tmpElecAgree, CustFileItem anFileItem){
        
        return saveSignFileInfo(tmpElecAgree, anFileItem, false);
    }
    
    private boolean saveSignFileInfo(ScfElecAgreement tmpElecAgree, CustFileItem anFileItem, boolean anSignedFile) {
        if (tmpElecAgree != null) {
            String tmpStatus = null;
            if (anSignedFile) {
                tmpElecAgree.setSignBatchNo(anFileItem.getBatchNo());
                tmpStatus = "1";
            }
            else {
                tmpElecAgree.setBatchNo(anFileItem.getBatchNo());
                tmpStatus = "2";
            }
            tmpElecAgree.fillElecAgreeStatus(tmpStatus);
            this.updateByPrimaryKey(tmpElecAgree);
            //custFileService.webSaveAndUpdateFileItem(anFileItem.getFilePath(),anFileItem.getFileLength(),anFileItem.getFileInfoType(),anFileItem.getFileName());
        }

        return true;
    }
    
    /**
     * 发送短信验证码
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @param anVcode
     *            沃通发出的验证码
     * @return
     */
    public boolean saveAndSendValidSMS(ScfElecAgreement anElecAgree, CustFileItem fileItem, String anAppNo, String anVcode) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0) {
            logger.error("saveAndSendValidSMS not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        boolean result = this.remoteHelper.sendValidSMS(anAppNo, custNo, anVcode);
        this.scfElecAgreeStubService.saveElecAgreeStubStatus(custNo, anAppNo, result ? "1" : "2");
        if  (result){
            saveSignFileInfo(anElecAgree, fileItem, false);
        }

        return result;
    }
    
    /**
     * 获取短信验证码
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return boolean 获得验证码成功，返回true,否则返回false;
     */
    public boolean saveAndSendSMS(String anAppNo) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("sendSMS not find signed parnter use with anAppNo " + anAppNo);

            return false;
        }
        ScfElecAgreement electAgreement = this.selectByPrimaryKey(anAppNo);
        if (electAgreement.hasSendSignOrder()) {
            if (this.saveAndSignElecAgreement(electAgreement) == false) {
                logger.error("SendSignOrder find Error ");
                return false;
            }
        }
        else {
            logger.info("this electAgreement appNo" + anAppNo + ", has sended!");
        }

        return this.remoteHelper.sendSMS(anAppNo, custNo);
    }
    
    /**
     * 发送给沃通做电子签名
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return
     */
    protected boolean saveAndSignElecAgreement(ScfElecAgreement anElectAgreement) {
        logger.info("create saveAndSignElecAgreement " + anElectAgreement);
        boolean result = this.remoteHelper.signElecAgreement(anElectAgreement);
        if (result) {

            this.updateByPrimaryKey(anElectAgreement);
        }

        return result;
    }
    
    /**
     * 取消或作废电子合同
     * 
     * @param anAppNo
     * @return
     */
    public boolean checkCancelElecAgreement(String anAppNo) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                logger.info("permit cancel this agreement " + anAppNo );
                return true;
            }
        }
        return false;
    }
    
    /**
     * 取消或作废电子合同
     * 
     * @param anAppNo
     * @return
     */
    public boolean saveAndCancelElecAgreement(String anAppNo,String anDescribe) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                elecAgreement.fillElecAgreeStatus("9");
                if(BetterStringUtils.isNotBlank(anDescribe)){
                    elecAgreement.setDes(anDescribe);
                }
                this.updateByPrimaryKey(elecAgreement);
                this.scfElecAgreeStubService.saveElecAgreeStubStatus(custNo, anAppNo, "9");
                return true;
            }
        }
        return false;
    }
    
    /**
     * 增加电子合同信息
     * 
     * @param anElecAgree
     *            电子合同信息
     * @param anCustList
     *            电子合同的签约方信息
     */
    public void addElecAgreementInfo(ScfElecAgreement anElecAgree, List<Long> anCustNoList) {
        Map workCondition = new HashMap();
        workCondition.put("agreeType", anElecAgree.getAgreeType());
        workCondition.put("requestNo", anElecAgree.getRequestNo());
        List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        boolean result = true;
        if (Collections3.isEmpty(anCustNoList)) {
            throw new BytterWebServiceException(WebServiceErrorCode.E1010);
        }
        if (Collections3.isEmpty(elecAgreeList)) {
            this.insert(anElecAgree);
        }
        else {
            ScfElecAgreement tmpElecAgree = Collections3.getFirst(elecAgreeList);
            // 只能更新未处理的合同协议
            if ("0".equals(tmpElecAgree.getSignStatus())) {
                tmpElecAgree.updateInfo(anElecAgree);
                this.updateByPrimaryKey(tmpElecAgree);
            }
            else {
                result = false;
            }
        }
        if (result) {
            this.scfElecAgreeStubService.saveScfElecAgreeStub(anElecAgree.getAppNo(), anCustNoList);
        }
        else {
            throw new BytterWebServiceException(WebServiceErrorCode.E1008);
        }
    }
    

    /**
     * 增加电子合同信息
     * 
     * @param anElecAgree
     *            电子合同信息
     * @param anCustList
     *            电子合同的签约方信息
     */
    public void addElecAgreementInfo(ScfElecAgreement anElecAgree) {
        Map workCondition = new HashMap();
        workCondition.put("agreeType", anElecAgree.getAgreeType());
        workCondition.put("requestNo", anElecAgree.getRequestNo());
        List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        if (Collections3.isEmpty(elecAgreeList)) {
            this.insert(anElecAgree);
        }
        else {
            ScfElecAgreement tmpElecAgree = Collections3.getFirst(elecAgreeList);
            // 只能更新未处理的合同协议
            if ("0".equals(tmpElecAgree.getSignStatus())) {
                tmpElecAgree.updateInfo(anElecAgree);
                this.updateByPrimaryKey(tmpElecAgree);
            }
        }
    }
    


    /**
     * 获得创建的PDF文件信息
     * 
     * @return
     */
    public CustFileItem findPdfFileInfo(ScfElecAgreement elecAgree) {
        CustFileItem fileItem = null;
        if (MathExtend.smallValue(elecAgree.getSignBatchNo()) == false) {

            fileItem = fileItemService.findOneByBatchNo(elecAgree.getSignBatchNo());
        }
        else if (MathExtend.smallValue(elecAgree.getBatchNo()) == false) {

            fileItem = fileItemService.findOneByBatchNo(elecAgree.getBatchNo());
        }
        return fileItem;
    };
    

    /**
     * 根据请求单号查询电子合同
     * 
     * @param anRequestNo
     *            融资申请业务订单号
     * @param anSignType
     *            电子合同类型
     * @return
     */
    public String findElecAgreeByRequestNo(String anRequestNo, String anAgreeType) {
        Map workCondition = new HashMap();
        if(BetterStringUtils.isNotBlank(anAgreeType)){
            workCondition.put("agreeType", anAgreeType);
        }
        workCondition.put("requestNo", anRequestNo);
        List<ScfElecAgreementInfo> elecAgreeList=this.selectByClassProperty(ScfElecAgreementInfo.class, workCondition);
        return Collections3.getFirst(elecAgreeList).getAppNo();
    }
    

    
    /**
     * 根据订单号，查询需要签署的文件
     * 
     * @param anAppNo
     * @return
     */
    public List<Long> findBatchNo(String anAppNo) {
        ScfElecAgreement tmpElecAgree = findOneElecAgreement(anAppNo);
        List<Long> result = new ArrayList();
        if (tmpElecAgree != null) {
            if (tmpElecAgree.getBatchNo() != null && tmpElecAgree.getBatchNo() > 10L) {

                result.add(tmpElecAgree.getBatchNo());
            }
        }

        return result;
    }

    /**
     * 获得签名的文件信息
     * 
     * @param anAppNo
     * @return
     */
    public List<Long> findSignedBatchNo(String anRequestNo) {
        List<Long> result = new ArrayList();
        for (ScfElecAgreement tmpElecAgree : this.selectByProperty("requestNo", anRequestNo)) {
            if (tmpElecAgree.getSignBatchNo() != null && tmpElecAgree.getSignBatchNo() > 10L) {

                result.add(tmpElecAgree.getSignBatchNo());
            }
        }

        return result;

    }
    
    /**
     * 更新电子合同的状态
     * 
     * @param anAppNo
     * @param anStatus
     */
    public void saveElecAgreementStatus(String anAppNo, String anStatus) {
        ScfElecAgreement tmpElecAgree = this.selectByPrimaryKey(anAppNo);
        if (tmpElecAgree != null) {
            tmpElecAgree.fillElecAgreeStatus(anStatus);
            this.updateByPrimaryKey(tmpElecAgree);
        }
        else {
            throw new BytterWebServiceException(WebServiceErrorCode.E1004);
        }
    }
    
    /**
     * 根据主键找到电子合同协议信息
     * 
     * @param anAppNo
     * @return
     */
    public ScfElecAgreement findOneElecAgreement(String anAppNo) {
        if (BetterStringUtils.isNotBlank(anAppNo)) {
            return this.selectByPrimaryKey(anAppNo);
        }
        else {

            return null;
        }
    }
    
    /***
     * 初始合同票据属性的参数
     * @param elecAgree
     * @param custNoSet
     * @param anRequestNo
     * @return
     */
    public void initAgreemtnBill(ScfElecAgreementInfo elecAgree,Set<Long> custNoSet){
        elecAgree.putStubInfos(scfElecAgreeStubService.findSignerList(elecAgree.getAppNo(),custAccoService), custNoSet);
        elecAgree.setBuyer(custAccoService.queryCustName(elecAgree.getBuyerNo()));
        elecAgree.setFactorName(DictUtils.getDictLabel("ScfAgencyGroup", elecAgree.getFactorNo()));
        elecAgree.setProductName(getProductNameByRequestNo(elecAgree.getRequestNo()));
        // 加入票据属性
        ScfAcceptBill bill=findBillInfoByRequestNo(elecAgree.getRequestNo());
        if(bill!=null){
            elecAgree.setBillMode(bill.getBillMode());
            elecAgree.setBillNo(bill.getBillNo());
            elecAgree.setInvoiceDate(bill.getInvoiceDate());
            elecAgree.setEndDate(bill.getEndDate());
        }
    }
    
    
    /**
     * 更新已经签署的电子文件信息
     * 
     * @param anAppNo
     *            签署的电子文件订单号信息
     * @param anFileItem
     *            文件信息
     * @return
     */
    public boolean saveSignedFile(String anAppNo, CustFileItem anFileItem) {

        return saveSignFileInfo(anAppNo, anFileItem, true);
    }
    
    /***
     * 保理合同新增
     * @param anElecAgreement
     * @param anFileList 附件列表
     * @return
     */
    public ScfElecAgreement addFactorAgreement(ScfElecAgreement anElecAgreement,String anFileList){
        anElecAgreement.initDefValue(custAccoService.queryCustName(anElecAgreement.getSupplierNo()));
        anElecAgreement.setAgreeType("3");
        anElecAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, anElecAgreement.getBatchNo()));
        try {
            this.insert(anElecAgreement);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return anElecAgreement;
    }
    
    /**
     * 更新客户合同信息
     * 
     * @param anParam
     * @param fileList
     * @return 更新后的客户合同信息
     */
    public ScfElecAgreement updateFactorAgreement(ScfElecAgreement anElecAgreement, String anAppNo, String anFileList) {
        logger.info("update param:" + anElecAgreement);
        ScfElecAgreement tmpAgreement = this.selectByPrimaryKey(anAppNo);

        // 只更新数据，不能更新状态，而且只有未启用的合同才能更新
        if ("0".equals(tmpAgreement.getSignStatus()) == false) {

            throw new BytterTradeException(40001, "已启用或废止的合同不能修改！");
        }
        ScfElecAgreement reqAgreement = anElecAgreement;
        reqAgreement.modifyAgreement(tmpAgreement);
        reqAgreement.setSupplier(custAccoService.queryCustName(anElecAgreement.getSupplierNo()));

        reqAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, reqAgreement.getBatchNo()));
        // 保存附件信息

        this.updateByPrimaryKey(reqAgreement);

        return reqAgreement;
    }
    
    /***
     * 查询保理合同
     * @param anParam
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfElecAgreementInfo> queryFactorAgreementList(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        Map<String, Object> termMap = new HashMap();
        if(BetterStringUtils.isNotBlank((String)anParam.get("GTEregDate")) && BetterStringUtils.isNotBlank((String)anParam.get("LTEregDate"))){
            termMap.put("GTEregDate", anParam.get("GTEregDate"));
            termMap.put("LTEregDate", anParam.get("LTEregDate"));
        }
        String signStatus = (String) anParam.get("signStatus");
        anPageSize = MathExtend.defIntBetween(anPageSize, 2, ParamNames.MAX_PAGE_SIZE, 25);
        if (BetterStringUtils.isBlank(signStatus)) {
            termMap.put("signStatus", Arrays.asList("0","1", "2","3", "9"));
        }
        else {
            termMap.put("signStatus", signStatus);
        }
        termMap.put("agreeType", anParam.get("agreeType"));
        termMap.put("factorNo", anParam.get("factorNo"));
        Page<ScfElecAgreementInfo> elecAgreeList = this.selectPropertyByPage(ScfElecAgreementInfo.class, termMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")));
        
        return elecAgreeList;
    }
    
    /***
     * 查询保理合同关联下拉列表
     * @param anCustNo 客户号 
     * @param anFactorNo 保理公司编号
     * @param anCoreCustNo 核心企业编号
     * @param anAgreeType 合同类型
     * @return
     */
    public List<SimpleDataEntity> findFactorAgreement(Long anCustNo,Long anFactorNo,String anAgreeType){
        Map<String,Object> anMap=new HashMap<String,Object>();
        anMap.put("supplierNo", anCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("agreeType", anAgreeType);
        anMap.put("signStatus", Arrays.asList("1"));
        List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();
        for(ScfElecAgreement elecAgreement:this.selectByProperty(anMap)){
            result.add(new SimpleDataEntity(elecAgreement.getAgreeName(), elecAgreement.getAppNo()));
        }
        return result;
    }
    
    public String updateFactorAgree(String anAppNo,String anStatus){
        ScfElecAgreement elecAgreement=this.selectByPrimaryKey(anAppNo);
        elecAgreement.setSignStatus(anStatus);
        if(this.updateByPrimaryKey(elecAgreement)>0){
            return "成功";
        }else{
            return "失败";
        }
    }
    
    /***
     * 查找详情
     * @param anAppNo
     * @return
     */
    public ScfElecAgreementInfo findElecAgreementInfo(String anAppNo){
        List<Long> custNoList = UserUtils.findCustNoList();
        Set<Long> custNoSet = new HashSet(custNoList);
        ScfElecAgreementInfo elecAgree=new ScfElecAgreementInfo();
        BeanMapper.copy(this.selectByPrimaryKey(anAppNo),elecAgree);
        initAgreemtnBill(elecAgree, custNoSet);
        System.out.println("elecAgree:"+elecAgree);
        return elecAgree;
    }
}
