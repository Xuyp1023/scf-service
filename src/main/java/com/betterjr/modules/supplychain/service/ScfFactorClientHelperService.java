package com.betterjr.modules.supplychain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.client.data.ScfClientDataParentFace;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustMechBankAccount;
import com.betterjr.modules.supplychain.data.ScfClientDataDetail;
import com.betterjr.modules.supplychain.data.ScfClientDataInfo;
import com.betterjr.modules.supplychain.entity.CustCoreCorpInfo;
import com.betterjr.modules.supplychain.entity.CustTempEnrollInfo;
import com.betterjr.modules.supplychain.entity.ScfSupplierBank;

@Service
public class ScfFactorClientHelperService {

    private static final Logger logger = LoggerFactory.getLogger(ScfFactorClientHelperService.class);

    @Autowired
    private ScfSupplierBankService supplierBankService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService relationService;

    @Autowired
    private SupplyAccoRequestService supplyAccoService;

    private Map<String, ScfClientDataInfo> classMapService = new LinkedCaseInsensitiveMap();

    @Autowired
    private CustCoreCorpService coreCorpService;

    public Map<String, ScfClientDataInfo> getClassMapService() {

        return this.classMapService;
    }

    public void setClassMapService(final Map<String, ScfClientDataInfo> anClassMapService) {

        this.classMapService = anClassMapService;
    }

    @PostConstruct
    public void initClassMapService() {
        this.classMapService.put("CoreSupplierInfo", new ScfClientDataInfo("CoreSupplierInfo", "coreEnterpriseService", "btNo", "supplier", "btNo"));
        this.classMapService.put("ScfAcceptBill",
                new ScfClientDataInfo("ScfAcceptBill", "scfAcceptBillService", "billNo", "supplierBill", "suppBankAccount"));
        this.classMapService.put("ScfBankPaymentFlow",
                new ScfClientDataInfo("ScfBankPaymentFlow", "scfBankPaymentFlowService", "btInnerId", "supplierPay", "suppBankAccount"));
        this.classMapService.put("ScfCapitalFlow",
                new ScfClientDataInfo("ScfCapitalFlow", "scfCapitalFlowService", "btInnerId", "supplierCapital", "suppBankAccount"));
        this.classMapService.put("ScfSupplierBank", new ScfClientDataInfo("ScfSupplierBank", "scfSupplierBankService", "btNo", "supplierBank", ""));
        this.classMapService.put("ScfRelation", new ScfClientDataInfo("ScfRelation", "scfRelationService", "btNo", "supplierRela", "btNo"));
        this.classMapService.put("CustTempEnrollInfo",
                new ScfClientDataInfo("CustTempEnrollInfo", "supplyAccoRequestService", "btNo", "supplierReg", "btNo"));

        this.classMapService.put("CustCoreCorpInfo",
                new ScfClientDataInfo("CustCoreCorpInfo", "CustCoreCorpService", "corpNo", "supplierReg", "corpNo"));
    }

    /**
     * 产生数字证书信息时，根据客户的组织机构证书信息和银行账户更新供应链金融相关信息
     *
     * @param anCoreCustNo
     *            核心企业编码
     * @param anBankAccount
     *            银行账户
     * @param anOperOrg
     *            企业唯一标示
     */
    public void saveCustomerOperOrg(final Long anCoreCustNo, final String anBankAccount, final String anOperOrg) {
        // 首先保存供应商银行账户信息，建立供应商银行账户信息和操作员所在机构的关系，客户银行账户信息中保存了资金系统的客户号和银行账户，兼顾的多方的处理。
        final Set<String> workBtNoList = new HashSet();
        final Set<String> workBankAccountList = new HashSet();
        final Set<ScfSupplierBank> tmpBankSet = supplierBankService.saveCustOperOrg(anCoreCustNo, anBankAccount, anOperOrg);
        ScfSupplierBank tmpBankAccountInfo = null;
        for (final ScfSupplierBank suppBank : tmpBankSet) {
            workBtNoList.add(suppBank.getBtNo());
            workBankAccountList.add(suppBank.getBankAccount());
            if (suppBank.getBankAccount().equalsIgnoreCase(anBankAccount)) {
                tmpBankAccountInfo = suppBank;
            }
        }
        BaseService workService = null;
        final Map termMap = new HashMap();
        Object obj = null;
        List<ScfClientDataParentFace> workList;
        for (final ScfClientDataInfo serviceInfo : this.classMapService.values()) {
            termMap.put("coreCustNo", anCoreCustNo);
            obj = serviceInfo.findCondition(workBtNoList, workBankAccountList);
            if (obj == null) {
                continue;
            }
            termMap.put(serviceInfo.getFilterField(), obj);
            workService = (BaseService) SpringContextHolder.getBean(serviceInfo.getWorkService());
            workList = workService.selectByProperty(termMap);
            for (final ScfClientDataParentFace workFace : workList) {
                if (BetterStringUtils.defShortString(workFace.getOperOrg())) {
                    workFace.setOperOrg(anOperOrg);
                    workService.updateByPrimaryKey(workFace);
                }
                else {
                    logger.warn("更新数据的OperOrg已经存在，不能更新 ：存在的OperOrg:" + workFace.getOperOrg());
                }
            }

            termMap.clear();
        }

        // 然后准备开户的默认信息，如果注册，则从注册信息中取，如果没有注册，则从供应商信息中获得

        final CustTempEnrollInfo custTemp = this.supplyAccoService.findByBankAccount(tmpBankAccountInfo);

        // 表示客户不是通过注册来使用的。
        Map tmpMap = null;
        if (custTemp != null) {
            tmpMap = custTemp.converToTmpDataMap();
        }
        else if (tmpBankAccountInfo != null) {
            tmpMap = tmpBankAccountInfo.converToTmpDataMap();
        }/*
        if (tmpMap != null) {
            String data = BTObjectUtils.fastSerializeStr(tmpMap);
            this.tmpDataService.saveCustTempDataByOperOrg(data, anOperOrg);
        }*/

    }

    /**
     * 客户开户时，写入客户客户号信息
     *
     * @param anCustNo
     *            客户开户时，平台分配的客户号
     * @param anBankAccount
     *            银行账户信息
     * @param anOperOrg
     *            客户操作员账户
     */
    public void saveCustomerCustNo(final Long anCustNo, final String anBankAccount, final String anOperOrg) {
        final List<ScfSupplierBank> suppBankList = this.supplierBankService.findCustByBankAccount(anOperOrg, anBankAccount);
        if (Collections3.isEmpty(suppBankList)) {
            logger.warn("不是供应商的银行账户信息，不能更新客户号, anBankAccount =" + anBankAccount + ", anOperOrg = " + anOperOrg);
        }
        else {
            BaseService workService = null;
            List<ScfClientDataParentFace> workList;
            for (final ScfClientDataInfo serviceInfo : this.classMapService.values()) {
                workService = (BaseService) SpringContextHolder.getBean(serviceInfo.getWorkService());
                workList = workService.selectByProperty("operOrg", anOperOrg);
                for (final ScfClientDataParentFace workFace : workList) {
                    if (MathExtend.smallValue(workFace.getCustNo())) {
                        workFace.setCustNo(anCustNo);
                        workService.updateByPrimaryKey(workFace);
                    }
                }
            }
        }
    }

    public void saveCoreCorpData(final String anWorkType, final ScfClientDataDetail anWorkTypeData, final List anDataList, final String anOperOrg) {
        coreCorpService.saveCoreCorpList(anDataList, anOperOrg);
    }

    /**
     * 保存客户端上传来的数据
     *
     * @param anDataList
     *            数据列表
     * @param anCoreCustNo
     *            核心企业客户号
     */
    public void saveCustomerData(final String anWorkType, final ScfClientDataDetail anWorkTypeData, final List<ScfClientDataParentFace> anDataList, final String anCoreOperOrg) {
        // 空就不处理了.
        if (Collections3.isEmpty(anDataList)) {

            return;
        }
        final String myClass = Collections3.getFirst(anDataList).getClass().getSimpleName();
        final ScfClientDataInfo serviceInfo = classMapService.get(myClass);
        final BaseService workService = (BaseService) SpringContextHolder.getBean(serviceInfo.getWorkService());
        logger.info("saveCustomerData work Service is " + workService);
        ScfSupplierBank suppBank;
        final List<ScfClientDataParentFace> resultData = new ArrayList();
        for (final ScfClientDataParentFace clientData : anDataList) {
            //clientData.setCoreCustNo(anCoreCustNo);
            clientData.fillDefaultValue();
            // 表示基础账户信息，可以通过供应商和核心企业关系来取数据
            if (BetterStringUtils.isNotBlank(clientData.getBtNo()) || BetterStringUtils.isNotBlank(clientData.getBankAccount())) {
                if (BetterStringUtils.isNotBlank(clientData.getBtNo())) {
                    suppBank = this.supplierBankService.findCustNoByBtCustNo(anCoreOperOrg, clientData.getBtNo());
                } // 表示可以通过银行信息获得客户编号
                else {
                    suppBank = this.supplierBankService.findScfBankByBankAccount(anCoreOperOrg, clientData.getBankAccount());
                }
                if (suppBank != null) {
                    clientData.setCustNo(suppBank.getCustNo());
                    clientData.setOperOrg(suppBank.getOperOrg());
                }//通过从注册的表中获取已经开户的客户信息
                else{
                    String tmpAccoName = anWorkTypeData.getBankAccName();
                    String tmpAcco = anWorkTypeData.getBankAcc();
                    CustTempEnrollInfo tempEnroll = supplyAccoService.findUnRegisterAccount(tmpAccoName, tmpAcco, false);
                    if (tempEnroll == null){
                        tmpAcco = clientData.getBankAccount();
                        tmpAccoName = clientData.findBankAccountName();
                        tempEnroll = supplyAccoService.findUnRegisterAccount(tmpAccoName, tmpAcco, false);
                    }
                    if (tempEnroll != null){
                        clientData.setCustNo(tempEnroll.getCustNo());
                        clientData.setOperOrg(tempEnroll.getOperOrg());
                    } else {
                        final CustMechBankAccount bankAccount = supplyAccoService.findCustMechBankAccount(tmpAccoName, tmpAcco);
                        if (bankAccount != null) {
                            clientData.setCustNo(bankAccount.getCustNo());
                            clientData.setOperOrg(bankAccount.getOperOrg());
                        }
                    }
                }
            } // 其它情况不能处理，不属于处理的范围
            else {
                logger.error("find Has Data not dispather, " + clientData);
                continue;
            }
            resultData.add(clientData);
            // logger.info("work Client Data is " + clientData);
        }

        saveUploadSupplierData(workService, resultData, serviceInfo, anCoreOperOrg);
        if (workService instanceof CoreEnterpriseService) {
            saveUploadSupplierRelation(resultData, anCoreOperOrg);

            // XXX remove coreCustNo
            supplyAccoService.saveUnRegisterAccount(resultData);
        }
    }

    private void saveUploadSupplierRelation(final List anDataList, final String anCoreOperOrg){
        //final String coreCustName = SupplyChainUtil.findCoreNameByCustNo(anCoreCustNo);
        for (final Object data: anDataList){
            final Map workValue = BeanMapper.map(data, HashMap.class);

            final String coreCorpId = (String) workValue.get("corpId");

            if (BetterStringUtils.isBlank(coreCorpId)) {
                logger.info("资金系统 供应商信息 corpId 为空:" + workValue);
                continue;
            }

            final CustCoreCorpInfo coreCorpInfo = coreCorpService.findByCorpNo(anCoreOperOrg, coreCorpId);
            if (coreCorpInfo == null) {
                logger.info("资金系统 供应商信息 对应核心企业 为空:" + workValue);
                continue;
            }

            relationService.saveAndCheckCust(workValue, coreCorpInfo.getCustName() , coreCorpInfo.getCustNo());
        }
    }

    /**
     * 保存核心企业上传上来的供应商银行账户账户数据；处理逻辑，该表主要由核心企业的数据同步，因此；<BR>
     * 每次处理都是删除后追加，先简单处理，以后实现归档的操作。即删除前作归档操作。 <BR>
     * 删除的逻辑是根据核心企业号和数据表中的唯一主键来删除。
     *
     * @param anList
     *            数据列表
     * @return 处理成功返回true， 处理失败返回false
     */
    protected boolean saveUploadSupplierData(final BaseService anService, final List anList, final ScfClientDataInfo anServiceInfo, final String anCoreOperOrg) {
        // 没数据，直接返回
        if (Collections3.isEmpty(anList)) {

            return true;
        }

        if (anService instanceof ScfAcceptBillService) {
            final ScfAcceptBillService accetBillService = (ScfAcceptBillService) anService;
            return accetBillService.saveUploadSupplierData(anList, anCoreOperOrg);
        }
        else {
            final Set<String> distinctKey = ReflectionUtils.listToKeySet(anList, anServiceInfo.getKeyField());
            final Map<String, Object> termMap = new HashMap<String, Object>();
            for (final String btNo : distinctKey) {
                termMap.put("coreOperOrg", anCoreOperOrg);
                termMap.put(anServiceInfo.getKeyField(), btNo);
                anService.deleteByExample(termMap);
                termMap.clear();
            }

            for (final Object scfObj : anList) {

                anService.insert(scfObj);
            }
            return true;
        }
    }

    /**
     * 检查是否需要会写获取核心企业的上、下游客户信息
     * @param anMap 开户时提供的参数
     */
    public void checkTempAcco(final Map anMap){
        logger.info("checkTempAcco request value :"+anMap);
        supplyAccoService.addSupplyAccountFromOpenAcco(anMap);
    }
}