package com.betterjr.modules.agreement.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.config.SpringPropertyResourceReader;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterWebServiceException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
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
import com.betterjr.modules.agreement.entity.ScfElecAgreeStub;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.contract.IEsignSingerService;
import com.betterjr.modules.contract.data.ContractStubData;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.product.service.ScfProductService;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.template.entity.ScfContractTemplate;
import com.betterjr.modules.template.service.ScfContractTemplateService;

/***
 * 电子合同管理
 * 
 * @author hubl
 *
 */
@Service
public class ScfElecAgreementService extends BaseService<ScfElecAgreementMapper, ScfElecAgreement> {

    @Autowired
    private CustAccountService custAccoService;
    @Autowired
    private ScfElecAgreeStubService scfElecAgreeStubService;
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;
    @Autowired
    private ScfFactorRemoteHelper remoteHelper;

    @Reference(interfaceClass = IEsignSingerService.class)
    private IEsignSingerService tsignHelper;

    @Autowired
    private ScfProductService productService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfRequestService requestService;
    @Reference(interfaceClass = ICustFileService.class)
    protected ICustFileService fileItemService;

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private ScfContractTemplateService contractTemplateService;

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
    public Page<ScfElecAgreementInfo> queryScfElecAgreementList(final Map<String, Object> anParam, final int anPageNum, int anPageSize) {
        final Map<String, Object> termMap = new HashMap();
        if (BetterStringUtils.isNotBlank((String) anParam.get("GTEregDate")) && BetterStringUtils.isNotBlank((String) anParam.get("LTEregDate"))) {
            termMap.put("GTEregDate", anParam.get("GTEregDate"));
            termMap.put("LTEregDate", anParam.get("LTEregDate"));
        }
        final String signStatus = (String) anParam.get("signStatus");
        anPageSize = MathExtend.defIntBetween(anPageSize, 2, ParamNames.MAX_PAGE_SIZE, 25);
        if (BetterStringUtils.isBlank(signStatus)) {
            termMap.put("signStatus", Arrays.asList("0", "1", "2", "3"));
        }
        else {
            termMap.put("signStatus", signStatus);
        }
        if (UserUtils.coreUser()) {
            // termMap.put("agreeType", Arrays.asList("1", "2"));
            termMap.put("buyerNo", anParam.get("custNo"));
        }
        else if (UserUtils.supplierUser()) {
            // termMap.put("agreeType", Arrays.asList("0"));
            termMap.put("supplierNo", anParam.get("custNo"));
        }
        else if (UserUtils.sellerUser()) {
            // termMap.put("agreeType", Arrays.asList("2"));
            termMap.put("supplierNo", anParam.get("custNo"));
        }
        else if (UserUtils.factorUser()) {
            // termMap.put("agreeType", Arrays.asList("0", "1", "2"));
            termMap.put("factorNo", anParam.get("custNo"));
        }
        final List<Long> custNoList = UserUtils.findCustNoList();
        if (logger.isInfoEnabled()) {
            logger.info("this is findScfElecAgreementList params " + termMap);
        }
        final Page<ScfElecAgreementInfo> elecAgreeList = this.selectPropertyByPage(ScfElecAgreementInfo.class, termMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")));
        final Set<Long> custNoSet = new HashSet(custNoList);
        for (final ScfElecAgreementInfo elecAgree : elecAgreeList) {
            findAgreemtnBill(elecAgree, custNoSet);
        }
        logger.info("this is findScfElecAgreementList result count :" + elecAgreeList.size());
        return elecAgreeList;
    }

    /***
     * 根据requstNo获取产品名称
     * 
     * @param requestNo
     * @return
     */
    public String findProductNameByRequestNo(final String requestNo) {
        try {
            final ScfRequest request = requestService.findRequestByRequestNo(requestNo);
            if (request.getProductId() != null) {
                return productService.findProductById(request.getProductId()).getProductName();
            }
        }
        catch (final Exception e) {
            logger.error("getProductNameByRequestNo 异常：" + e.getMessage());
        }
        return "";
    }

    /****
     * 根据申请单号查询票据信息
     * 
     * @param anRequestNo
     * @return
     */
    public ScfAcceptBill findBillInfoByRequestNo(final String anRequestNo) {
        final List billList = orderService.findInfoListByRequest(anRequestNo, "2");
        ScfAcceptBill scfAcceptBill = null;
        if (billList != null && billList.size() > 0) {
            scfAcceptBill = (ScfAcceptBill) Collections3.getFirst(billList);
        }
        return scfAcceptBill;
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
    public List<ScfElecAgreementInfo> findElecAgreeByOrderNo(final String anRequestNo, final String anSignType) {
        final Map workCondition = new HashMap();
        if (BetterStringUtils.isNotBlank(anSignType)) {
            workCondition.put("agreeType", anSignType);
        }
        final List<Long> custNoList = UserUtils.findCustNoList();
        workCondition.put("requestNo", anRequestNo);
        final List<ScfElecAgreementInfo> elecAgreeList = this.selectByClassProperty(ScfElecAgreementInfo.class, workCondition);
        final Set<Long> custNoSet = new HashSet(custNoList);
        for (final ScfElecAgreementInfo elecAgree : elecAgreeList) {
            findAgreemtnBill(elecAgree, custNoSet);
        }
        return elecAgreeList;
    }

    private boolean saveSignFileInfo(final String anAppNo, final CustFileItem anFileItem, final boolean anSignedFile) {
        final ScfElecAgreement tmpElecAgree = this.selectByPrimaryKey(anAppNo);
        return saveSignFileInfo(tmpElecAgree, anFileItem, anSignedFile);
    }

    public boolean saveSignFileInfo(final ScfElecAgreement tmpElecAgree, final CustFileItem anFileItem) {

        return saveSignFileInfo(tmpElecAgree, anFileItem, false);
    }

    private boolean saveSignFileInfo(final ScfElecAgreement tmpElecAgree, final CustFileItem anFileItem, final boolean anSignedFile) {
        if (tmpElecAgree != null) {
            if (anSignedFile) {
                tmpElecAgree.setSignBatchNo(this.fileItemService.updateCustFileItemInfo(Long.toString(anFileItem.getId()), null));
            }
            else {
                tmpElecAgree.setBatchNo(this.fileItemService.updateCustFileItemInfo(Long.toString(anFileItem.getId()), anFileItem.getBatchNo()));
            }
            final String tmpStatus = this.scfElecAgreeStubService.checkSignStatus(tmpElecAgree.getAppNo());
            tmpElecAgree.fillElecAgreeStatus(tmpStatus);
            this.updateByPrimaryKey(tmpElecAgree);
            // custFileService.webSaveAndUpdateFileItem(anFileItem.getFilePath(),anFileItem.getFileLength(),anFileItem.getFileInfoType(),anFileItem.getFileName());
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
    public boolean saveAndSendValidSMS(final ScfElecAgreement anElecAgree, final CustFileItem fileItem, final String anAppNo, final String anVcode) {
        final ScfElecAgreeStub agreeStub = this.scfElecAgreeStubService.findContract(anAppNo);
        if (agreeStub == null) {
            logger.error("saveAndSendValidSMS not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        final ScfElecAgreement electAgreement = this.selectByPrimaryKey(anAppNo);
        ContractStubData tmpStub = new ContractStubData();
        tmpStub.setCustNo(agreeStub.getCustNo());
        tmpStub.setSequence(agreeStub.getSignOrder());
        tmpStub.setPositionType("1");
        tmpStub.setContractTemplateId(electAgreement.getContractTemplateId());
        tmpStub.setSignFileName(electAgreement.getAgreeName().concat(BetterDateUtils.getNumDateTime()).concat(".pdf"));
        final Long workFileBatchNo = electAgreement.getSignBatchNo();
        byte[] tmpData = null;
        if (MathExtend.smallValue(workFileBatchNo) == false) {
            tmpData = dataStoreService.loadDataFromStoreByBatchNo(workFileBatchNo);
        }
        else {
            tmpData = dataStoreService.loadDataFromStore(fileItem.getId());
        }
        tmpStub = this.tsignHelper.signData(agreeStub.getCustNo(), tmpStub, tmpData, anVcode, Boolean.FALSE);
        final boolean isok = tmpStub.getResult() != null;
        if (isok) {
            agreeStub.setOperStatus(tmpStub.getBusinStatus());
            agreeStub.setSignServiceId(tmpStub.getSignServiceId());
            if (MathExtend.smallValue(fileItem.getId()) == false) {
                saveSignFileInfo(anElecAgree, fileItem, false);
            }
            this.scfElecAgreeStubService.saveElecAgreeStubStatus(agreeStub.getCustNo(), anAppNo, "1", tmpStub.getSignServiceId());

            final CustFileItem tmpFileItem = this.dataStoreService.saveStreamToStoreWithBatchNo(new ByteArrayInputStream(tmpStub.getResult()),
                    "signFile", electAgreement.getAgreeName().concat(".pdf"));
            saveSignFileInfo(anElecAgree, tmpFileItem, true);
        }
        else {
            this.scfElecAgreeStubService.saveElecAgreeStubStatus(agreeStub.getCustNo(), anAppNo, "2");
        }
        return isok;
    }

    public boolean saveAndSendValidSMS_OLD(final ScfElecAgreement anElecAgree, final CustFileItem fileItem, final String anAppNo,
            final String anVcode) {
        final Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0) {
            logger.error("saveAndSendValidSMS not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        final String mode = SpringPropertyResourceReader.getProperty("sys.mode", "prod");
        if (BetterStringUtils.equalsIgnoreCase(mode, "dev")) {
            return true;
        }
        else {
            final boolean result = this.remoteHelper.sendValidSMS(anAppNo, custNo, anVcode);
            this.scfElecAgreeStubService.saveElecAgreeStubStatus(custNo, anAppNo, result ? "1" : "2");
            if (result) {
                saveSignFileInfo(anElecAgree, fileItem, false);
            }
            return result;
        }
    }

    /**
     * 获取短信验证码
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return boolean 获得验证码成功，返回true,否则返回false;
     */
    public boolean saveAndSendSMS(final String anAppNo) {
        final Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("sendSMS not find signed parnter use with anAppNo " + anAppNo);

            return false;
        }
        final ScfElecAgreement electAgreement = this.selectByPrimaryKey(anAppNo);
        if (electAgreement.hasSendSignOrder()) {
            if (this.saveAndSignElecAgreement(electAgreement) == false) {
                logger.error("SendSignOrder find Error ");
                return false;
            }
        }
        else {
            logger.info("this electAgreement appNo" + anAppNo + ", has sended!");
        }

        final Long tmpCustNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);

        final boolean isok = this.tsignHelper.sendSMS(tmpCustNo, Boolean.FALSE);

        return isok;
    }

    public boolean saveAndSendSMS_OLD(final String anAppNo) {
        final Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("sendSMS not find signed parnter use with anAppNo " + anAppNo);

            return false;
        }
        final String mode = SpringPropertyResourceReader.getProperty("sys.mode", "prod");
        if (BetterStringUtils.equalsIgnoreCase(mode, "dev")) {
            return true;
        }
        else {
            final ScfElecAgreement electAgreement = this.selectByPrimaryKey(anAppNo);
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
    }

    /**
     * 发送给沃通做电子签名
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return
     */
    protected boolean saveAndSignElecAgreement(final ScfElecAgreement anElectAgreement) {
        logger.info("create saveAndSignElecAgreement " + anElectAgreement);

        final String mode = SpringPropertyResourceReader.getProperty("sys.mode", "prod");
        if (BetterStringUtils.equalsIgnoreCase(mode, "dev")) {
            return true;
        }
        else {
            final boolean result = this.remoteHelper.signElecAgreement(anElectAgreement);
            if (result) {

                this.updateByPrimaryKey(anElectAgreement);
            }

            return result;
        }
    }

    /**
     * 取消或作废电子合同
     * 
     * @param anAppNo
     * @return
     */
    public boolean checkCancelElecAgreement(final String anAppNo) {
        final Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        final ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                logger.info("permit cancel this agreement " + anAppNo);
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
    public boolean saveAndCancelElecAgreement(final String anAppNo, final String anDescribe) {
        final Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        final ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                elecAgreement.fillElecAgreeStatus("9");
                if (BetterStringUtils.isNotBlank(anDescribe)) {
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
    public void addElecAgreementInfo(final ScfElecAgreement anElecAgree, final String anTempName, final List<Long> anCustNoList) {
        final Map workCondition = new HashMap();
        workCondition.put("agreeType", anElecAgree.getAgreeType());
        workCondition.put("requestNo", anElecAgree.getRequestNo());
        final List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        boolean result = true;
        final ScfContractTemplate contractTemp = contractTemplateService.findTemplateByType(Long.parseLong(anElecAgree.getFactorNo()), anTempName,
                "1");
        BTAssert.notNull(contractTemp);
        anElecAgree.setContractTemplateId(contractTemp.getId());

        if (Collections3.isEmpty(anCustNoList)) {
            throw new BytterWebServiceException(WebServiceErrorCode.E1010);
        }
        if (Collections3.isEmpty(elecAgreeList)) {
            this.insert(anElecAgree);
        }
        else {
            final ScfElecAgreement tmpElecAgree = Collections3.getFirst(elecAgreeList);
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
    public void addElecAgreementInfo(final ScfElecAgreement anElecAgree) {
        final Map workCondition = new HashMap();
        workCondition.put("agreeType", anElecAgree.getAgreeType());
        workCondition.put("requestNo", anElecAgree.getRequestNo());
        final List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        if (Collections3.isEmpty(elecAgreeList)) {
            this.insert(anElecAgree);
        }
        else {
            final ScfElecAgreement tmpElecAgree = Collections3.getFirst(elecAgreeList);
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
    public CustFileItem findPdfFileInfo(final ScfElecAgreement elecAgree) {
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
    public String findElecAgreeByRequestNo(final String anRequestNo, final String anAgreeType) {
        final Map workCondition = new HashMap();
        if (BetterStringUtils.isNotBlank(anAgreeType)) {
            workCondition.put("agreeType", anAgreeType);
        }
        workCondition.put("requestNo", anRequestNo);
        final List<ScfElecAgreementInfo> elecAgreeList = this.selectByClassProperty(ScfElecAgreementInfo.class, workCondition);
        return Collections3.getFirst(elecAgreeList).getAppNo();
    }

    /**
     * 根据订单号，查询需要签署的文件
     * 
     * @param anAppNo
     * @return
     */
    public List<Long> findBatchNo(final String anAppNo) {
        final ScfElecAgreement tmpElecAgree = findOneElecAgreement(anAppNo);
        final List<Long> result = new ArrayList();
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
    public List<Long> findSignedBatchNo(final String anRequestNo) {
        final List<Long> result = new ArrayList();
        for (final ScfElecAgreement tmpElecAgree : this.selectByProperty("requestNo", anRequestNo)) {
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
    public void saveElecAgreementStatus(final String anAppNo, final String anStatus) {
        final ScfElecAgreement tmpElecAgree = this.selectByPrimaryKey(anAppNo);
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
    public ScfElecAgreement findOneElecAgreement(final String anAppNo) {
        if (BetterStringUtils.isNotBlank(anAppNo)) {
            return this.selectByPrimaryKey(anAppNo);
        }
        else {

            return null;
        }
    }

    /***
     * 初始合同票据属性的参数
     * 
     * @param elecAgree
     * @param custNoSet
     * @param anRequestNo
     * @return
     */
    public void findAgreemtnBill(final ScfElecAgreementInfo elecAgree, final Set<Long> custNoSet) {
        elecAgree.putStubInfos(scfElecAgreeStubService.findSignerList(elecAgree.getAppNo(), custAccoService), custNoSet);
        elecAgree.setBuyer(custAccoService.queryCustName(elecAgree.getBuyerNo()));
        elecAgree.setFactorName(DictUtils.getDictLabel("ScfAgencyGroup", elecAgree.getFactorNo()));
        elecAgree.setProductName(findProductNameByRequestNo(elecAgree.getRequestNo()));
        // 加入票据属性
        final ScfAcceptBill bill = findBillInfoByRequestNo(elecAgree.getRequestNo());
        if (bill != null) {
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
    public boolean saveSignedFile(final String anAppNo, final CustFileItem anFileItem) {

        return saveSignFileInfo(anAppNo, anFileItem, true);
    }

    /***
     * 保理合同新增
     * 
     * @param anElecAgreement
     * @param anFileList
     *            附件列表
     * @return
     */
    public ScfElecAgreement addFactorAgreement(final ScfElecAgreement anElecAgreement, final String anFileList) {
        anElecAgreement.initDefValue(custAccoService.queryCustName(anElecAgreement.getSupplierNo()));
        anElecAgreement.setAgreeType("9");
        anElecAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, anElecAgreement.getBatchNo()));
        try {
            this.insert(anElecAgreement);
        }
        catch (final Exception e) {
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
    public ScfElecAgreement updateFactorAgreement(final ScfElecAgreement anElecAgreement, final String anAppNo, final String anFileList) {
        logger.info("update param:" + anElecAgreement);
        final ScfElecAgreement tmpAgreement = this.selectByPrimaryKey(anAppNo);

        // 只更新数据，不能更新状态，而且只有未启用的合同才能更新
        if ("0".equals(tmpAgreement.getSignStatus()) == false) {

            throw new BytterTradeException(40001, "已启用或废止的合同不能修改！");
        }
        final ScfElecAgreement reqAgreement = anElecAgreement;
        reqAgreement.modifyAgreement(tmpAgreement);
        reqAgreement.setSupplier(custAccoService.queryCustName(anElecAgreement.getSupplierNo()));

        reqAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, reqAgreement.getBatchNo()));
        // 保存附件信息

        this.updateByPrimaryKey(reqAgreement);

        return reqAgreement;
    }

    /***
     * 查询保理合同
     * 
     * @param anParam
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfElecAgreementInfo> queryFactorAgreementList(final Map<String, Object> anParam, final int anPageNum, int anPageSize) {
        final Map<String, Object> termMap = new HashMap();
        if (BetterStringUtils.isNotBlank((String) anParam.get("GTEregDate")) && BetterStringUtils.isNotBlank((String) anParam.get("LTEregDate"))) {
            termMap.put("GTEregDate", anParam.get("GTEregDate"));
            termMap.put("LTEregDate", anParam.get("LTEregDate"));
        }
        final String signStatus = (String) anParam.get("signStatus");
        anPageSize = MathExtend.defIntBetween(anPageSize, 2, ParamNames.MAX_PAGE_SIZE, 25);
        if (BetterStringUtils.isBlank(signStatus)) {
            termMap.put("signStatus", Arrays.asList("0", "1", "2", "3", "9"));
        }
        else {
            termMap.put("signStatus", signStatus);
        }
        termMap.put("agreeType", anParam.get("agreeType"));
        termMap.put("factorNo", anParam.get("factorNo"));
        final Page<ScfElecAgreementInfo> elecAgreeList = this.selectPropertyByPage(ScfElecAgreementInfo.class, termMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")));

        return elecAgreeList;
    }

    /***
     * 查询保理合同关联下拉列表
     * 
     * @param anCustNo
     *            客户号
     * @param anFactorNo
     *            保理公司编号
     * @param anCoreCustNo
     *            核心企业编号
     * @param anAgreeType
     *            合同类型
     * @return
     */
    public List<SimpleDataEntity> findFactorAgreement(final Long anCustNo, final Long anFactorNo, final String anAgreeType) {
        final Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("supplierNo", anCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("agreeType", anAgreeType);
        anMap.put("signStatus", Arrays.asList("1"));
        final List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();
        for (final ScfElecAgreement elecAgreement : this.selectByProperty(anMap)) {
            result.add(new SimpleDataEntity(elecAgreement.getAgreeName(), elecAgreement.getAppNo()));
        }
        return result;
    }

    public String updateFactorAgree(final String anAppNo, final String anStatus) {
        final ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        elecAgreement.setSignStatus(anStatus);
        if (this.updateByPrimaryKey(elecAgreement) > 0) {
            return "成功";
        }
        else {
            return "失败";
        }
    }

    /***
     * 查找详情
     * 
     * @param anAppNo
     * @return
     */
    public ScfElecAgreementInfo findElecAgreementInfo(final String anAppNo) {
        final List<Long> custNoList = UserUtils.findCustNoList();
        final Set<Long> custNoSet = new HashSet(custNoList);
        final ScfElecAgreementInfo elecAgree = new ScfElecAgreementInfo();
        BeanMapper.copy(this.selectByPrimaryKey(anAppNo), elecAgree);
        findAgreemtnBill(elecAgree, custNoSet);
        return elecAgree;
    }

    /***
     * 根据供应商客户号查询保理合同信息，返回合同对象
     * 
     * @param anCustNo
     *            客户号
     * @param anFactorNo
     *            保理公司编号
     * @param anCoreCustNo
     *            核心企业编号
     * @param anAgreeType
     *            合同类型
     * @return
     */
    public ScfElecAgreement findFactorAgreementBySupplierNo(final Long anCustNo, final Long anFactorNo, final String anAgreeType) {
        final Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("supplierNo", anCustNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("agreeType", anAgreeType);
        anMap.put("signStatus", Arrays.asList("1"));
        return Collections3.getFirst(this.selectByProperty(anMap));
    }

    public List findBillListByRequestNo(final String anRequestNo) {
        return orderService.findInfoListByRequest(anRequestNo, "2");
    }

    /**
     * 通过融资申请生成电子合同
     * 
     * @param anRequest
     * @return
     */
    public ScfElecAgreement saveAddElecAgreementByReceivableRequest(final ScfReceivableRequest anRequest) {

        // 查询电子合同模版
        ScfContractTemplate template = new ScfContractTemplate();
        String agreeType = "6";
        if ("1".equals(anRequest.getReceivableRequestType())) {

            template = findAgreementTemplate(anRequest.getFactoryNo(), "receivableRequestProtocolModel1");
        }
        else if ("2".equals(anRequest.getReceivableRequestType())) {
            template = findAgreementTemplate(anRequest.getFactoryNo(), "receivableRequestProtocolModel2");
            agreeType = "7";
        }

        BTAssert.notNull(template, "请联系平台上传电子合同模版");
        final ScfElecAgreement agreement = ScfElecAgreement.createByReceivable(anRequest.getCustName() + "应收账款提前回款协议书",
                SequenceFactory.generate("PLAT_ReceivableRequestAgreementCode", "TZBL-ZR#{Date:yy}#{Seq:8}"), anRequest.getBalance(), agreeType);
        agreement.setAgreeStartDate(BetterDateUtils.getNumDate());
        agreement.setBuyerNo(anRequest.getCoreCustNo());
        agreement.setFactorNo(anRequest.getFactoryNo() + "");
        agreement.setRequestNo(anRequest.getRequestNo());
        agreement.setSupplier(anRequest.getCustName());
        agreement.setSupplierNo(anRequest.getCustNo());
        this.addElecAgreementInfo(agreement, template.getTemplateType(),
                Arrays.asList(agreement.getSupplierNo(), Long.parseLong(agreement.getFactorNo())));
        // this.insertSelective(agreement);

        return agreement;

    }

    /**
     * 电子合同状态更新
     * 
     * @param appNo
     * @param anCustNo
     * @param anBusinStatus
     * @return
     */
    public ScfElecAgreement saveUpdateBusinStatus(final String appNo, final Long anCustNo, final String anBusinStatus) {

        if (StringUtils.isBlank(appNo)) {
            BTAssert.notNull(appNo, "没有生成电子合同信息，不能进行签署");
        }

        final ScfElecAgreement agreement = this.selectByPrimaryKey(appNo);
        BTAssert.notNull(agreement, "没有生成电子合同信息，不能进行签署");
        agreement.setSignStatus(anBusinStatus);
        if ("1".equals(anBusinStatus)) {
            agreement.setSignDate(BetterDateUtils.getNumDate());
        }
        this.updateByPrimaryKeySelective(agreement);
        // scfElecAgreeStubService.saveAddInitValueStub(agreement.getAppNo(), anCustNo, "1");

        return agreement;

    }

    /**
     * 查找电子合同模版
     * 
     * @return
     */
    private ScfContractTemplate findAgreementTemplate(final Long factoryNo, final String type) {

        final ScfContractTemplate template = contractTemplateService.findTemplateByType(factoryNo, type, "1");
        if (template != null && template.getId() != null) {
            return template;
        }
        return null;
    }
}
