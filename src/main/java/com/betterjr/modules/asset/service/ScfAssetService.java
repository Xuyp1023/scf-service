package com.betterjr.modules.asset.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.dao.ScfAssetMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.entity.ScfAssetBasedata;
import com.betterjr.modules.asset.entity.ScfAssetCompany;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.entity.CustContractLedger;
import com.betterjr.modules.ledger.service.ContractLedgerService;
import com.betterjr.modules.ledger.service.CustContractLedgerService;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.order.service.ScfOrderDOService;
import com.betterjr.modules.productconfig.entity.ScfAssetDict;
import com.betterjr.modules.productconfig.sevice.ScfProductAssetDictRelationService;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.entity.BaseVersionEntity;
import com.betterjr.modules.version.entity.BetterjrBaseEntity;

@Service
public class ScfAssetService extends BaseService<ScfAssetMapper, ScfAsset> {

    @Autowired
    private ScfAssetCompanyService assetCompanyService;

    @Autowired
    private ScfAssetBasedataService assetBasedataService;

    @Autowired
    private ScfReceivableDOService receivableService;// 引收账款

    @Autowired
    private ScfInvoiceDOService invoiceService;// 发票

    @Autowired
    private ScfOrderDOService orderService;// 订单

    @Autowired
    private ScfAcceptBillDOService billService;// 票据

    @Autowired
    private ContractLedgerService contractLedgerService;// 合同

    @Autowired
    private CustContractLedgerService custLedgerService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Autowired
    private CustAccountService custAccountService;

    @Autowired
    private ScfProductAssetDictRelationService productAssetService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    /**
     * 新增资产以及相关企业，基础数据信息
     * 
     * @param anAsset
     * @return
     */
    public ScfAsset saveAddAsset(Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "新增资产企业 失败-资产 is null");
        anAssetMap = Collections3.filterMap(anAssetMap,
                new String[] { "businTypeId", "productCode", "sourceUseType", "custNo", "coreCustNo", "factorNo",
                        "orderList", "invoiceList", "agreementList", "receivableList", "acceptBillList",
                        "statementFileList", "goodsFileList", "othersFileList", "id" });
        // 将map转成资产对象
        ScfAsset asset = convertBeanFromMap(anAssetMap);
        asset.initAdd();
        // 封装资产数据中的基础信息
        asset = packageAssetFromMap(asset, anAssetMap);
        logger.info("Begin to add anAsset");

        // 插入资产企业表信息
        assetCompanyService.saveAddAssetCompanyByAsset(asset);
        // -------------------20170615
        // 插入资产基础数据表信息
        // addAssetBasedata(asset);
        assetBasedataService.saveAddAssetBasedataByAsset(asset);

        this.insert(asset);
        logger.info("success to add anAsset");
        // asset.setOperationAuth(AssetConstantCollentions.ASSET_OPERATOR_AUTH_MAX);

        return asset;

    }

    /**
     * 模式4新增资产信息
     * 
     * @param anMap
     * @return
     */
    public ScfAsset saveAddAssetFour(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "新增资产企业 失败-资产 is null");
        anMap = Collections3.filterMap(anMap,
                new String[] { "description", "custBankName", "custBankAccount", "custBankAccountName", "custNo",
                        "coreCustNo", "factoryNo", "orderList", "invoiceList", "agreementList", "receivableList",
                        "acceptBillList" });
        ScfAsset asset = new ScfAsset();
        try {
            BeanUtils.populate(asset, anMap);
        }
        catch (IllegalAccessException | InvocationTargetException e) {

            e.printStackTrace();
        }
        asset.initAdd();
        if (anMap.containsKey("factoryNo")) {

            asset.setFactorNo(Long.parseLong(anMap.get("factoryNo").toString()));
        }
        asset.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE);
        // 封装基础数据信息
        covernAssetByMapBaseDataList(asset, anMap, true);
        // 封装企业信息 并且插入企业信息
        convernAssetCompany(asset);
        this.insertSelective(asset);
        // 查询完整的资产信息
        asset = findAssetByid(asset.getId());
        return asset;
    }

    /**
     * 封装企业信息并且插入到数据库
     * 
     * @param anAsset
     */
    private void convernAssetCompany(ScfAsset anAsset) {

        CustInfo custInfo = custAccountService.findCustInfo(anAsset.getCustNo());
        packageCustInfoToAssetCompany(anAsset, custInfo, AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
        CustInfo coreCustInfo = custAccountService.findCustInfo(anAsset.getCoreCustNo());
        packageCustInfoToAssetCompany(anAsset, coreCustInfo, AssetConstantCollentions.SCF_ASSET_ROLE_CORE);
        CustInfo factoryInfo = custAccountService.findCustInfo(anAsset.getFactorNo());
        packageCustInfoToAssetCompany(anAsset, factoryInfo, AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY);
    }

    private void packageCustInfoToAssetCompany(ScfAsset anAsset, CustInfo custInfo, String companyType) {

        if (custInfo == null) {
            return;
        }
        ScfAssetCompany company = new ScfAssetCompany();
        company.setAssetId(anAsset.getId());
        company.setAssetRole(companyType);
        if (company.equals(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY)
                || company.equals(AssetConstantCollentions.SCF_ASSET_ROLE_DEALER)) {
            company.setBankAccount(anAsset.getCustBankAccount());
            company.setBankAccountName(anAsset.getCustBankAccountName());
            company.setBankName(anAsset.getCustBankName());
        } else if (company.equals(AssetConstantCollentions.SCF_ASSET_ROLE_CORE)) {
            company.setBankAccount(anAsset.getCoreCustBankAccount());
            company.setBankAccountName(anAsset.getCoreCustBankAccountName());
            company.setBankName(anAsset.getCoreCustBankName());
        } else {

        }
        company.setCustName(custInfo.getCustName());
        company.setCustNo(custInfo.getCustNo());
        company.initAdd();
        assetCompanyService.insertSelective(company);

    }

    /**
     * 封装基础资产信息并且插入到数据库中去
     * 
     * @param anAsset
     * @param anMap
     */
    private void covernAssetByMapBaseDataList(ScfAsset anAsset, Map<String, Object> anMap, boolean flag) {

        // 将基础数据封装到 getBasedataMap
        // 查询所有的发票信息
        List<ScfInvoiceDO> invoiceList = invoiceService
                .queryBaseVersionObjectByids(anMap.get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY).toString());
        packageBaseVersionEntityListToAsset(anAsset, invoiceList,
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE, flag);
        // 查询所有的应付账款信息
        List<ScfReceivableDO> receivableList = receivableService
                .queryBaseVersionObjectByids(anMap.get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY).toString());
        packageBaseVersionEntityListToAsset(anAsset, receivableList,
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE, flag);
        for (ScfReceivableDO receivable : receivableList) {
            anAsset.setBalance(MathExtend.add(anAsset.getBalance(), receivable.getSurplusBalance()));
        }
        // 查询所有的贸易合同信息 并且插入贸易合同信息
        List<ContractLedger> agreementList = contractLedgerService
                .queryBaseVersionObjectByids(anMap.get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY).toString());
        packageBaseVersionEntityListToAsset(anAsset, agreementList,
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT, flag);
    }

    private void packageBaseVersionEntityListToAsset(ScfAsset anAsset,
            List<? extends BetterjrBaseEntity> baseVersionList, String baseInfoType, boolean flag) {

        for (BetterjrBaseEntity baseVersion : baseVersionList) {
            checkStatus(baseVersion.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false,
                    "单据不符合要求,请选择生效的单据");
            packageBaseVersionEntityToAsset(anAsset, baseVersion, baseInfoType, flag);
        }

    }

    /**
     * 应收账款融资新增资产明细信息
     * 
     * @param anAssetMap
     * @return
     */
    public ScfAsset saveAddAssetNew(Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "新增资产企业 失败-资产 is null");
        BTAssert.notNull(anAssetMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");

        List<ScfReceivableDO> receivableList = queryReceivableList(anAssetMap);
        ScfAsset asset = new ScfAsset();
        try {
            BeanUtils.populate(asset, anAssetMap);
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        asset.initAdd();
        for (ScfReceivableDO receivable : receivableList) {
            // 创建asset对象和公司，基础数据等对象信息 并且插入企业表和基础数据表
            asset = createAndBuilderAsset(asset, receivable);
        }

        this.insert(asset);
        // 查询完整的资产信息
        ScfAsset findAssetByid = findAssetByid(asset.getId());
        // ------------
        return findAssetByid;

    }

    /**
     * 创建asset对象和公司，基础数据等对象信息 根据单条的应收应付账款信息封装到资产信息中去
     * 
     * @param anAsset
     * @param anReceivable
     * @return
     */
    private ScfAsset createAndBuilderAsset(ScfAsset anAsset, ScfReceivableDO anReceivable) {

        // 处理发票信息
        packageInvoiceToAsset(anAsset, anReceivable);
        // 处理贸易合同
        packageAgreementToAsset(anAsset, anReceivable);
        // 将应收账款信息封装到资产中 （并且插入公司表记录）并且将应收账款信息插入到资产基础数据表中去
        packageReceivableToAsset(anAsset, anReceivable);
        return anAsset;
    }

    /**
     * 将应收账款信息封装到资产中 （并且插入公司表记录）
     * 
     * @param anAsset
     * @param anReceivable
     */
    private void packageReceivableToAsset(ScfAsset anAsset, ScfReceivableDO anReceivable) {

        anAsset.setBalance(MathExtend.add(anAsset.getBalance(), anReceivable.getSurplusBalance()));
        anAsset.setCoreCustNo(anReceivable.getCoreCustNo());
        anAsset.setCoreCustName(anReceivable.getCoreCustName());
        anAsset.setCustNo(anReceivable.getCustNo());
        anAsset.setCustName(anReceivable.getCustName());
        anAsset.setEndDate(anReceivable.getEndDate());
        anAsset.setFactorNo(anReceivable.getCoreCustNo());
        assetCompanyService.saveAddCompanyByAssetBean(anAsset);
        insertReceivableToBaseData(anReceivable, anAsset.getId());

    }

    /**
     * 封装应收账款信息进入资产管理中
     * 
     * @param anReceivable
     * @param anId
     */
    private void insertReceivableToBaseData(ScfReceivableDO anReceivable, Long anId) {

        ScfAssetBasedata baseData = new ScfAssetBasedata();
        baseData.setAssetId(anId);
        baseData.setInfoId(anReceivable.getId());
        baseData.setInfoType(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE);
        baseData.setRefNo(anReceivable.getRefNo());
        baseData.setVersion(anReceivable.getVersion());
        baseData.initAdd();
        assetBasedataService.insertSelective(baseData);

    }

    /**
     * 将贸易合同信息存入到数据库中 将银行信息封装到asset中
     * 
     * @param anAsset
     * @param anReceivable
     */
    private void packageAgreementToAsset(ScfAsset anAsset, ScfReceivableDO anReceivable) {

        String agreeNo = anReceivable.getAgreeNo();
        if (StringUtils.isBlank(agreeNo)) {
            BTAssert.notNull(null, "应收账款的贸易合同号为空!操作失败");
        }
        // 根据贸易合同编号查询贸易合同
        ContractLedger agreement = contractLedgerService.selectOneByAgreeNo(agreeNo);
        BTAssert.notNull(agreement, "应收账款的贸易合同未找到!操作失败");
        checkStatus(agreement.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false, "请核准贸易合同");
        checkStatus(anReceivable.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false,
                "应付账款不是生效状态，无法进行申请");
        checkStatus(anReceivable.getCustNo() + "", agreement.getCustNo() + "", false, "应收账款和贸易合同对应的企业不一致");
        CustContractLedger custAgreement = custLedgerService
                .findCustContractByCustNoAndContractId(anReceivable.getCustNo(), agreement.getId());
        CustContractLedger coreCustAgreement = custLedgerService
                .findCustContractByCustNoAndContractId(anReceivable.getCoreCustNo(), agreement.getId());
        // 将合同插入到资产数据表中
        packageBaseVersionEntityToAsset(anAsset, agreement, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT,
                false);
        // 封装银行信息到资产中 1表示是供应商 2 表示是核心企业
        packageCustContractToAsset(anAsset, custAgreement, "1");
        packageCustContractToAsset(anAsset, coreCustAgreement, "2");
    }

    /**
     * 封装银行信息到资产中
     * 
     * @param anAsset
     * @param anCustAgreement
     * @param anType
     *            1表示是供应商 2 表示是核心企业
     */
    private void packageCustContractToAsset(ScfAsset anAsset, CustContractLedger anCustAgreement, String anType) {

        if ("1".equals(anType)) {
            // 供应商封装银行信息

            // 封装银行名称
            if (StringUtils.isNoneBlank(anAsset.getCustBankName())) {
                if (!anAsset.getCustBankName().equals(anCustAgreement.getBankName())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCustBankName(anCustAgreement.getBankName());
            }

            // 封装银行开户行
            if (StringUtils.isNoneBlank(anAsset.getCustBankAccountName())) {
                if (!anAsset.getCustBankAccountName().equals(anCustAgreement.getBankAccountName())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCustBankAccountName(anCustAgreement.getBankAccountName());
            }

            // 封装银行账户信息
            if (StringUtils.isNoneBlank(anAsset.getCustBankAccount())) {
                if (!anAsset.getCustBankAccount().equals(anCustAgreement.getBankAccount())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCustBankAccount(anCustAgreement.getBankAccount());
            }
        } else {

            // 核心企业封装
            // 封装银行名称
            if (StringUtils.isNoneBlank(anAsset.getCoreCustBankName())) {
                if (!anAsset.getCoreCustBankName().equals(anCustAgreement.getBankName())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCoreCustBankName(anCustAgreement.getBankName());
            }

            // 封装银行开户行
            if (StringUtils.isNoneBlank(anAsset.getCoreCustBankAccountName())) {
                if (!anAsset.getCoreCustBankAccountName().equals(anCustAgreement.getBankAccountName())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCoreCustBankAccountName(anCustAgreement.getBankAccountName());
            }

            // 封装银行账户信息
            if (StringUtils.isNoneBlank(anAsset.getCoreCustBankAccount())) {
                if (!anAsset.getCoreCustBankAccount().equals(anCustAgreement.getBankAccount())) {
                    BTAssert.notNull(null, "应收账款的贸易合同号对应的银行信息不对等!操作失败");
                }
            } else {
                anAsset.setCoreCustBankAccount(anCustAgreement.getBankAccount());
            }
        }

    }

    /**
     * 将发票信息插入到数据库中
     * 
     * @param anAsset
     * @param anReceivable
     */
    private void packageInvoiceToAsset(ScfAsset anAsset, ScfReceivableDO anReceivable) {

        // 查询发票列表
        if (StringUtils.isNoneBlank(anReceivable.getInvoiceNos())) {

            List<ScfInvoiceDO> invoiceList = invoiceService.queryReceivableList(anReceivable.getInvoiceNos());
            // 当一条记录都没有找到
            if (Collections3.isEmpty(invoiceList)) {
                BTAssert.notNull(null, "当前应收账款对应的发票未找到!");
            }

            // 当未找全所有的发票信息
            if (anReceivable.getInvoiceNos().split(",").length != invoiceList.size()) {
                BTAssert.notNull(null, "当前应收账款对应的发票还有未找到!");

            }

            for (ScfInvoiceDO invoiceDo : invoiceList) {
                if (!invoiceDo.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE)) {
                    BTAssert.notNull(null, "发票号码不可用来使用,请先核准" + invoiceDo.getInvoiceNo());

                }
                // 将发票信息存入到asset中
                packageBaseVersionEntityToAsset(anAsset, invoiceDo,
                        AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE, false);
            }

        }
    }

    /**
     * 将基础数据资产封装进资产中 并且插入到数据库中区
     * 
     * @param anAsset
     * @param baseVersion
     *            基础数据信息
     * @param baseInfoType
     *            基础数据类型
     */
    private void packageBaseVersionEntityToAsset(ScfAsset anAsset, BetterjrBaseEntity baseVersion, String baseInfoType,
            boolean flag) {

        ScfAssetBasedata baseData = new ScfAssetBasedata();
        baseData.setAssetId(anAsset.getId());
        baseData.initAdd();
        baseData.setInfoType(baseInfoType);
        baseData.setRefNo(baseVersion.getRefNo());
        baseData.setVersion(baseVersion.getVersion());
        baseData.setInfoId(baseVersion.getId());
        assetBasedataService.insertSelective(baseData);
        lockedAssetBasedata(baseVersion, baseInfoType, flag);

    }

    /**
     * 锁定基础资产信息
     * 
     * @param anBaseVersion
     * @param anFlag
     */
    private void lockedAssetBasedata(BetterjrBaseEntity anBaseVersion, String baseInfoType, boolean anFlag) {
        if (anFlag) {

            anBaseVersion.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_USED);
            anBaseVersion.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_LOCKED);
            if (baseInfoType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER)) {
                orderService.updateByPrimaryKeySelective((ScfOrderDO) anBaseVersion);

            } else if (baseInfoType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL)) {
                billService.updateByPrimaryKeySelective((ScfAcceptBillDO) anBaseVersion);
            } else if (baseInfoType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE)) {
                invoiceService.updateByPrimaryKeySelective((ScfInvoiceDO) anBaseVersion);
            } else if (baseInfoType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT)) {
                contractLedgerService.updateByPrimaryKeySelective((ContractLedger) anBaseVersion);
            } else if (baseInfoType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)) {
                receivableService.updateByPrimaryKeySelective((ScfReceivableDO) anBaseVersion);
            } else {

            }
        }

    }

    /**
     * 根据条件查询到用于融资的应收账款列表
     * 
     * @param anAssetMap
     * @return
     */
    private List<ScfReceivableDO> queryReceivableList(Map<String, Object> anAssetMap) {

        List<ScfReceivableDO> receivableList = new ArrayList<>();
        Object receivableId = anAssetMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY);
        ScfReceivableDO receivableDO = receivableService.selectByPrimaryKey(Long.parseLong(receivableId.toString()));
        BTAssert.notNull(receivableDO, "当前应收账款对应的发票未找到!");
        checkStatus(receivableDO.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false,
                "当前应收账款状态不是审核生效!");
        checkStatus(receivableDO.getIsLatest(), VersionConstantCollentions.IS_LATEST, false, "当前应收账款已不是最新版本!");
        checkStatus(receivableDO.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true,
                "当前应收账款已经在进行融资!");
        receivableList.add(receivableDO);
        return receivableList;
    }

    /**
     * 封装资产数据中的基础信息
     * 
     * @param anAsset
     * @param anAssetMap
     * @return
     */
    private ScfAsset packageAssetFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
        BTAssert.notNull(anAsset, "资产初始化 失败-资产 is null");
        BTAssert.notNull(anAssetMap.get("custNo"), "资产初始化 失败-请选择供应商");
        BTAssert.notNull(anAssetMap.get("coreCustNo"), "资产初始化 失败-请选择核心企业");
        BTAssert.notNull(anAssetMap.get("factorNo"), "资产初始化 失败-请选择核心企业");
        // 将企业信息列表封装到资产中
        packageAssetCompanyInfoFromMap(anAsset, anAssetMap);
        // 将基础资产信息封装到资产中
        packageAssetBaseDataInfoFromMap(anAsset, anAssetMap);
        // 封装资产总金额
        paceageAssetBalanceFromMap(anAsset, anAssetMap);
        // 封装资产附件各个批次号
        packageAssetFileFromMap(anAsset, anAssetMap);
        return anAsset;
    }

    /**
     * 
     * 封装文件资产附件信息
     * 
     * @param anAsset
     * @param anAssetMap
     */

    private void packageAssetFileFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {

        // 对账单附件列表
        if (anAssetMap.containsKey("statementFileList") && anAssetMap.get("statementFileList") != null) {

            anAsset.setStatementBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(
                    anAssetMap.get("statementFileList").toString(), anAsset.getStatementBatchNo()));

        }

        // 商品出库单附件列表
        if (anAssetMap.containsKey("goodsFileList") && anAssetMap.get("goodsFileList") != null) {

            anAsset.setGoodsBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(
                    anAssetMap.get("goodsFileList").toString(), anAsset.getGoodsBatchNo()));

        }

        // 其他附件列表
        if (anAssetMap.containsKey("othersFileList") && anAssetMap.get("othersFileList") != null) {

            anAsset.setOthersBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(
                    anAssetMap.get("othersFileList").toString(), anAsset.getOthersBatchNo()));

        }

    }

    /**
     * 封装资产的总金额
     * 
     * @param anAsset
     * @param anAssetMap
     */
    private ScfAsset paceageAssetBalanceFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {

        // 获取主体资产类型
        String AssetType = findProductMainAssetType(anAssetMap);
        BigDecimal totalBalance = new BigDecimal(0);
        if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(AssetType)) {
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_BILL_LIST_KEY);
            if (object instanceof List) {
                List<ScfAcceptBillDO> billList = (List<ScfAcceptBillDO>) object;
                for (ScfAcceptBillDO bill : billList) {
                    totalBalance = MathExtend.add(totalBalance, bill.getBalance());
                }
            }
        }
        if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT.equals(AssetType)) {
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
            if (object instanceof List) {
                List<ContractLedger> agreeList = (List<ContractLedger>) object;
                for (ContractLedger agree : agreeList) {
                    totalBalance = MathExtend.add(totalBalance, new BigDecimal(agree.getBalance()));
                }
            }
        }

        if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(AssetType)) {
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
            if (object instanceof List) {
                List<ScfInvoiceDO> invoiceList = (List<ScfInvoiceDO>) object;
                for (ScfInvoiceDO invoice : invoiceList) {
                    totalBalance = MathExtend.add(totalBalance, invoice.getBalance());
                }
            }
        }

        if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(AssetType)) {
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_ORDER_LIST_KEY);
            if (object instanceof List) {
                List<ScfOrderDO> orderList = (List<ScfOrderDO>) object;
                for (ScfOrderDO order : orderList) {
                    totalBalance = MathExtend.add(totalBalance, order.getBalance());
                }
            }
        }

        if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(AssetType)) {
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
            if (object instanceof List) {
                List<ScfReceivableDO> receivableList = (List<ScfReceivableDO>) object;
                for (ScfReceivableDO receivable : receivableList) {
                    totalBalance = MathExtend.add(totalBalance, receivable.getBalance());
                }
            }
        }

        anAsset.setBalance(totalBalance);
        return anAsset;

    }

    /**
     * 将基础资产信息封装到资产中秋
     * 
     * @param anAsset
     * @param anAssetMap
     */
    private ScfAsset packageAssetBaseDataInfoFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
        BTAssert.notNull(anAsset, "资产初始化 失败-资产 is null");

        if (anAssetMap.containsKey(AssetConstantCollentions.SCF_ORDER_LIST_KEY)) {
            List<ScfOrderDO> orderList = orderService.queryBaseVersionObjectByids(
                    anAssetMap.get(AssetConstantCollentions.SCF_ORDER_LIST_KEY).toString());
            anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_ORDER_LIST_KEY, orderList);
        }
        if (anAssetMap.containsKey(AssetConstantCollentions.SCF_INVOICE_LIST_KEY)) {
            List<ScfInvoiceDO> invoiceList = invoiceService.queryBaseVersionObjectByids(
                    anAssetMap.get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY).toString());
            anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_INVOICE_LIST_KEY, invoiceList);
        }
        if (anAssetMap.containsKey(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY)) {
            List<ScfReceivableDO> receivableList = receivableService.queryBaseVersionObjectByids(
                    anAssetMap.get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY).toString());
            anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, receivableList);
        }
        if (anAssetMap.containsKey(AssetConstantCollentions.SCF_BILL_LIST_KEY)) {
            List<ScfAcceptBillDO> billList = billService
                    .queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.SCF_BILL_LIST_KEY).toString());
            anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_BILL_LIST_KEY, billList);
        }
        if (anAssetMap.containsKey(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY)) {
            List<ContractLedger> agreementList = contractLedgerService.queryBaseVersionObjectByids(
                    anAssetMap.get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY).toString());
            anAsset.getBasedataMap().put(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, agreementList);
        }

        return anAsset;
    }

    /**
     * 通过保理产品编号查询主资产的类型
     * 
     * @param anAsset
     * @param anAssetMap
     */
    private String findProductMainAssetType(Map<String, Object> anAssetMap) {
        BTAssert.notNull(anAssetMap.get("productCode"), "资产初始化 失败-请选择保理产品");
        List<ScfAssetDict> productAssetDictList = productAssetService
                .queryProductAssetDict(anAssetMap.get("productCode").toString());
        BTAssert.notNull(productAssetDictList, "资产初始化 失败-请先配置当前保理产品");
        for (ScfAssetDict scfAssetDict : productAssetDictList) {
            if (scfAssetDict.getAssetType().equals("1")) {
                logger.info("融资核心资产类型为:" + scfAssetDict);
                if ("1".equals(scfAssetDict.getDictType())) {
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL;
                } else if ("2".equals(scfAssetDict.getDictType())) {
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE;
                } else if ("3".equals(scfAssetDict.getDictType())) {
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT;
                } else if ("4".equals(scfAssetDict.getDictType())) {
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE;
                } else if ("5".equals(scfAssetDict.getDictType())) {
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER;
                } else {
                    BTAssert.notNull(null, "资产初始化 失败-当前保理产品主体资产不符合要求(只能是订单,应收应付账款,合同，发票，汇票)");
                    return "";
                }
            }
        }

        BTAssert.notNull(null, "资产初始化 失败-当前保理产品请选择主体资产");
        return "";
    }

    /**
     * 将企业信息列表封装到资产中
     * 
     * @param anAsset
     * @param anAssetMap
     * @return
     */
    private ScfAsset packageAssetCompanyInfoFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {

        if (!getCurrentUserCustNos().contains(anAsset.getCustNo())) {
            BTAssert.notNull(null, "资产初始化 失败-你不是当前供应商的用户!没有权限");
        }
        // Map<String, Object> custMap = anAsset.getCustMap();
        // 封装供应商信息
        CustInfo custInfo = findCustInfoById(anAsset.getCustNo());
        if (anAsset.getCustType().equals(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY)) {

            packageAssetCompanyFromCustInfo(anAsset, custInfo, AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
        } else {
            packageAssetCompanyFromCustInfo(anAsset, custInfo, AssetConstantCollentions.SCF_ASSET_ROLE_DEALER);

        }
        // 封装核心企业信息
        CustInfo coreCustInfo = findCustInfoById(anAsset.getCoreCustNo());
        packageAssetCompanyFromCustInfo(anAsset, coreCustInfo, AssetConstantCollentions.SCF_ASSET_ROLE_CORE);
        // 封装保理公司信息
        CustInfo factorCustInfo = findCustInfoById(anAsset.getFactorNo());
        packageAssetCompanyFromCustInfo(anAsset, factorCustInfo, AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY);

        return anAsset;

    }

    private ScfAsset packageAssetCompanyFromCustInfo(ScfAsset anAsset, CustInfo anCustInfo,
            String anScfAssetRoleSupply) {

        ScfAssetCompany company = new ScfAssetCompany();
        company.setAssetId(anAsset.getId());
        company.setBusinStatus(AssetConstantCollentions.ASSET_BUSIN_STATUS_OK);
        company.setAssetRole(anScfAssetRoleSupply);
        company.setCustInfo(anCustInfo);
        company.setCustNo(anCustInfo.getCustNo());
        company.setCustName(anCustInfo.getCustName());
        if (AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY.equals(anScfAssetRoleSupply)
                || AssetConstantCollentions.SCF_ASSET_ROLE_DEALER.equals(anScfAssetRoleSupply)) {

            anAsset.setCustName(anCustInfo.getCustName());
            anAsset.getCustMap().put(AssetConstantCollentions.CUST_INFO_KEY, company);
        } else if (AssetConstantCollentions.SCF_ASSET_ROLE_CORE.equals(anScfAssetRoleSupply)) {
            // company.setCustName(anCustInfo.getCustName());
            anAsset.setCoreCustName(anCustInfo.getCustName());
            anAsset.getCustMap().put(AssetConstantCollentions.CORE_CUST_INFO_KEY, company);
        } else if (AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY.equals(anScfAssetRoleSupply)) {
            // company.setCustName(anCustInfo.getCustName());
            // anAsset.setCoreCustName(anCustInfo.getCustName());
            anAsset.getCustMap().put(AssetConstantCollentions.FACTORY_CUST_INFO_KEY, company);
        } else {

        }

        return anAsset;

    }

    /**
     * 通过公司id查找公司详情
     * 
     * @param anCustNo
     * @return
     */
    private CustInfo findCustInfoById(Long anCustNo) {

        return custAccountService.findCustInfo(anCustNo);
    }

    /**
     * 将map中的数据转换成资产对象
     * 
     * @param anAssetMap
     * @return
     */
    private ScfAsset convertBeanFromMap(Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
        ScfAsset asset = new ScfAsset();
        try {
            BeanUtils.populate(asset, anAssetMap);
            verificationAssetProperties(asset);
            if (UserUtils.supplierUser()) {
                asset.setCustType(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
            } else {
                asset.setCustType(AssetConstantCollentions.SCF_ASSET_ROLE_DEALER);
            }
            // 区分是新增还是修改资产
            saveModifyAsset(asset);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            logger.info("map-------->asset失败" + e.getMessage());
            BTAssert.notNull(null, "资产初始化 失败-数据不符合要求" + e.getMessage());
        }
        return asset;
    }

    private void verificationAssetProperties(ScfAsset asset) {

        BTAssert.notNull(asset, "资产初始化 失败-资产 is null");
        BTAssert.hasLength(asset.getBusinTypeId(), "资产初始化 失败-业务类型is null");
        BTAssert.hasLength(asset.getProductCode(), "资产初始化 失败-保理产品is null");
        BTAssert.hasLength(asset.getSourceUseType(), "资产初始化 失败-请指定业务类型(融资/询价)");
        BTAssert.notNull(asset.getCustNo(), "资产初始化 失败-请选择供应商");
        BTAssert.notNull(asset.getCoreCustNo(), "资产初始化 失败-请选择核心企业");
        BTAssert.notNull(asset.getFactorNo(), "资产初始化 失败-请选择保理企业");

    }

    /**
     * 查询资产信息，
     * 
     * @param assetId
     *            资产id
     * @param custNo
     *            当前企业id(用来校验权限) onOff: true 表示需要查询资产企业的详情信息 false 只需要查询一些基础数据信息 企业查询编号和企业名字 true 需要查询企业的全部信息
     * @return 返回的资产全部信息，并且封装好当前企业的操作权限
     */
    public ScfAsset findAssetByid(Long assetId) {

        ScfAsset asset = this.selectByPrimaryKey(assetId);
        // 查询资产企业信息
        asset = assetCompanyService.selectByAssetId(asset);
        if (asset.getCustMap().isEmpty()) {
            BTAssert.notNull(null, "查询资产企业 失败-该公司没有权限 is null");
        }
        // 查询订单详细信息
        asset = assetBasedataService.selectByAssetId(asset);
        return asset;

    }

    /**
     * 根据资产信息插入资产基础数据表信息
     * 
     * @param anAsset
     */
    private void addAssetBasedata(ScfAsset anAsset) {

        Map<String, Object> basedataMap = anAsset.getBasedataMap();
        BTAssert.notNull(basedataMap, "新增资产企业 失败-订单列表basedataMap is null");
        logger.info("success to add addAssetBasedata");
        // 插入订单
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_ORDER_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER);
        logger.info("success to add addAssetBasedata 订单");
        // 插入发票
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_INVOICE_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE);
        logger.info("success to add addAssetBasedata 发票");
        // 插入合同
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT);
        logger.info("success to add addAssetBasedata 合同");
        // 插入引收账款
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE);
        logger.info("success to add addAssetBasedata 引收账款");
        // 插入运输单据
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_TRANSPORT_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_TRANSPORT);
        logger.info("success to add addAssetBasedata 运输单据");
        // 插入票据
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_BILL_LIST_KEY, anAsset.getId(),
                AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL);
        logger.info("success to add addAssetBasedata 票据");
    }

    private void addAssetBasedataDetail(Map<String, Object> basedataMap, String key, Long assetId, String infoType) {
        Object object = basedataMap.get(key);
        if (object != null) {
            List<BaseVersionEntity> orders = (List<BaseVersionEntity>) object;
            for (BaseVersionEntity scfOrder : orders) {
                BTAssert.notNull(scfOrder.getRefNo(), "新增资产企业 失败-订单列表refno is null");
                BTAssert.notNull(scfOrder.getVersion(), "新增资产企业 失败-订单列表version is null");
                ScfAssetBasedata assetBasedata = new ScfAssetBasedata();
                assetBasedata.setAssetId(assetId);
                assetBasedata.setInfoType(infoType);
                assetBasedata.setRefNo(scfOrder.getRefNo());
                assetBasedata.setVersion(scfOrder.getVersion());
                assetBasedataService.addAssetBasedata(assetBasedata);
            }
        }
    }

    /**
     * 根据资产信息插入资产企业表信息
     * 
     * @param anAsset
     */
    private void addAssetCompany(ScfAsset anAsset) {
        logger.info("Begin to add addAssetCompany");
        // 插入资产企业中的供应商/经销商
        CustInfo cust = getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.CUST_INFO_KEY).get(0);// 企业信息
        BTAssert.notNull(cust, "新增资产企业 失败-addAssetCompany 供应商/经销商 is null");
        Integer operatorStatu = getOperator(anAsset.getCustMap(), AssetConstantCollentions.CUST_INFO_STATUS);// 操作权限
        ScfAssetCompany anAssetCompany = getAssetCompany(cust, anAsset.getCustType(), operatorStatu, anAsset.getId());
        assetCompanyService.addAssetCompany(anAssetCompany);
        logger.info("success to add addAssetCompany 供应商/经销商");
        anAsset.setCustNo(cust.getCustNo());
        anAsset.setCustName(cust.getCustName());
        // 插入资产企业中的核心企业
        CustInfo coreCust = getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.CORE_CUST_INFO_KEY).get(0);
        BTAssert.notNull(coreCust, "新增资产企业 失败-addAssetCompany 核心企业 is null");
        Integer coreOperatorStatu = getOperator(anAsset.getCustMap(), AssetConstantCollentions.CORE_CUST_INFO_STATUS);
        ScfAssetCompany coreAssetCompany = getAssetCompany(coreCust, AssetConstantCollentions.SCF_ASSET_ROLE_CORE,
                coreOperatorStatu, anAsset.getId());
        assetCompanyService.addAssetCompany(coreAssetCompany);
        anAsset.setCoreCustNo(coreCust.getCustNo());
        anAsset.setCoreCustName(coreCust.getCustName());
        logger.info("success to add addAssetCompany 核心企业");
        // 插入资产企业中的保理公司
        List<CustInfo> factoryCust = getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.FACTORY_CUST_INFO_KEY);
        BTAssert.notNull(factoryCust, "新增资产企业 失败-addAssetCompany 保理公司 is null");
        Integer factoryOperatorStatu = getOperator(anAsset.getCustMap(),
                AssetConstantCollentions.FACTORY_CUST_INFO_STATUS);
        for (CustInfo custInfo : factoryCust) {
            ScfAssetCompany factoryAssetCompany = getAssetCompany(custInfo,
                    AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY, factoryOperatorStatu, anAsset.getId());
            assetCompanyService.addAssetCompany(factoryAssetCompany);
        }
        logger.info("success to add addAssetCompany");
    }

    /**
     * 获取当前对象的操作权限
     */
    private Integer getOperator(Map<String, Object> anCustMap, String key) {

        BTAssert.notNull(anCustMap, "新增资产企业 失败-企业列表anCustMap is null");
        Integer status = 1;
        Object object = anCustMap.get(key);
        try {
            status = Integer.parseInt(object.toString());
        }
        catch (Exception e) {
            return status;
        }
        return status;
    }

    /**
     * 初始化资产企业表
     * 
     * @param custInfo
     *            企业信息
     * @param assetRole
     *            企业担任的角色
     * @param operatorStatus
     *            企业权限 1 读 2 写 4 删除
     * @param assetId
     *            资产id
     * @return
     */
    public ScfAssetCompany getAssetCompany(CustInfo custInfo, String assetRole, Integer operatorStatus, Long assetId) {

        ScfAssetCompany assetCompany = new ScfAssetCompany();
        assetCompany.setAssetId(assetId);
        assetCompany.setAssetRole(assetRole);
        assetCompany.setOperatorStatus(operatorStatus);
        assetCompany.setCustNo(custInfo.getCustNo());
        assetCompany.setCustName(custInfo.getCustName());
        return assetCompany;

    }

    /**
     * 从资产表中获取到相关企业的数据
     * 
     * @param custMap
     * @param key
     * @return
     */
    public List<CustInfo> getCustInfo(Map<String, Object> custMap, String key) {

        BTAssert.notNull(custMap, "新增资产企业 失败-企业列表custMap is null");
        Object object = custMap.get(key);
        BTAssert.notNull(object, "新增资产企业 失败-企业列表当前key is null" + key);
        List<CustInfo> list = new ArrayList<>();
        if (key.equals(AssetConstantCollentions.CUST_INFO_KEY)
                || key.equals(AssetConstantCollentions.CORE_CUST_INFO_KEY)) {
            // 如果是供应商和核心企业
            CustInfo custinfo = (CustInfo) object;
            list.add(custinfo);
        } else {
            list = (List<CustInfo>) object;
        }
        return list;
    }

    /**
     * 查找符合条件的资产基础数据
     * 
     * @param anCustNo
     *            供应商id
     * @param anDataType
     *            资产类型 关联的基础数据的类型1订单2票据3应收账款4发票5贸易合同6运输单单据类型
     * @param anFlag
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page queryFinanceBaseDataList(Long anCustNo, Long anCoreCustNo, String anDataType, String anIds,
            String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anCustNo, "查询可用资产失败!供应商id不存在");
        BTAssert.notNull(anCoreCustNo, "查询可用资产失败!核心企业id不存在");
        BTAssert.notNull(anDataType, "查询可用资产失败!请选择要查询的数据");
        if (!getCurrentUserCustNos().contains(anCustNo)) {
            BTAssert.notNull(null, "查询可用资产失败!你没有当前企业资产的操作权限");
        }

        Map<String, Object> paramMap = QueryTermBuilder.newInstance().put("custNo", anCustNo)
                .put("coreCustNo", anCoreCustNo).build();
        if (StringUtils.isNoneBlank(anIds) && !"null".equals(anIds) && !"undefined".equals(anIds)
                && !"undefined,".equals(anIds)) {

            List<Long> idList = convertStringToList(anIds);
            paramMap.put("NEid", idList);

        }
        if (anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER)) {
            Page<ScfOrderDO> orderPage = orderService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize,
                    "1".equals(anFlag), "id desc");
            return orderPage;
        } else if (anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL)) {
            Page<ScfAcceptBillDO> billPage = billService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize,
                    "1".equals(anFlag), "id desc");
            return billPage;
        } else if (anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE)) {
            Page<ScfInvoiceDO> invoicePage = invoiceService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize,
                    "1".equals(anFlag), "id desc");
            return invoicePage;
        } else if (anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)) {
            Page<ScfReceivableDO> receivablePage = receivableService.selectCanUsePageWithVersion(paramMap, anPageNum,
                    anPageSize, "1".equals(anFlag), "id desc");
            return receivablePage;
        } else {
            Page<ContractLedger> agreementPage = contractLedgerService.selectCanUsePageWithVersion(paramMap, anPageNum,
                    anPageSize, "1".equals(anFlag), "id desc");
            return agreementPage;
        }

    }

    private List<Long> convertStringToList(String anIds) {

        List<Long> idList = new ArrayList<Long>();
        if (StringUtils.isNotBlank(anIds)) {

            if (anIds.contains(",")) {

                String[] strs = anIds.split(",");
                for (String string : strs) {
                    if (StringUtils.isNoneBlank(string)) {
                        try {

                            idList.add(Long.parseLong(string));
                        }
                        catch (Exception e) {

                        }

                    }
                }

            } else {
                idList.add(Long.parseLong(anIds));
            }

        }
        return idList;
    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * 
     * @return
     */
    private Collection<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        Collection<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

    /**
     * 资产修改
     * 
     * @param anAsset
     * @return
     */
    private ScfAsset saveModifyAsset(ScfAsset anAsset) {

        if (anAsset != null && anAsset.getId() != null) {
            ScfAsset asset = this.selectByPrimaryKey(anAsset.getId());
            BTAssert.notNull(asset, "修改资产 失败-未找到资产信息");
            logger.info(
                    "Begin to saveModifyAsset anAsset=" + asset + "  当前操作用户为:" + UserUtils.getOperatorInfo().getName());
            if (!getCurrentUserCustNos().contains(asset.getCustNo())) {
                BTAssert.notNull(null, "修改资产 失败-原资产公司成员!没有权限");
            }
            checkModifyStatus(asset);
            asset.initModifyValue(UserUtils.getOperatorInfo());
            this.updateByPrimaryKeySelective(asset);
            anAsset.setPrefixId(asset.getId());
            anAsset.setId(null);
            logger.info(
                    "end to saveModifyAsset anAsset=" + asset + "  当前操作用户为:" + UserUtils.getOperatorInfo().getName());
        }
        return anAsset;
    }

    /**
     * 资产确认提交，走融资流程
     * 
     * @param anAssetId
     * @return
     */
    public synchronized ScfAsset saveConfirmAsset(Long anAssetId) {

        BTAssert.notNull(anAssetId, "资产确认失败,请选择要确认的资产");
        // 查找资产信息
        ScfAsset asset = findAssetByid(anAssetId);
        BTAssert.notNull(asset, "资产确认失败,请选择要确认的资产");
        logger.info("saveConfirmAsset begin:..资产确认提交，走融资流程 Asset=" + asset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        // 校验资产状态
        checkConfirmStatus(asset);
        // 校验当前公司是否有权限
        checkCurrentCompanyPermission(asset);
        // 校验资产包含的所有基础资料的状态
        checkBaseDataStatusAndUpdateBaseData(asset);
        // 更新资产的状态
        asset.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE);
        this.updateByPrimaryKeySelective(asset);

        logger.info("saveConfirmAsset end:..资产确认提交，走融资流程 Asset=" + asset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        return asset;
    }

    /**
     * 资产作废
     * 
     * @param anAssetId
     * @return
     */
    public ScfAsset saveAnnulAsset(Long anAssetId) {

        BTAssert.notNull(anAssetId, "资产作废失败,请选择要作废的资产");
        // 查找资产信息
        ScfAsset asset = findAssetByid(anAssetId);
        BTAssert.notNull(asset, "资产作废失败,请选择要作废的资产");
        logger.info("saveAnnulAsset begin:..资产作废资产信息 Asset=" + asset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        // 校验资产状态
        checkAnnulStatus(asset);
        // 校验当前公司是否有权限
        checkCurrentCompanyPermission(asset);

        asset.initAnnulAsset(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(asset);

        logger.info(
                "saveAnnulAsset end:..资产作废资产信息 Asset=" + asset + "  当前操作用户为:" + UserUtils.getOperatorInfo().getName());
        return asset;
    }

    /**
     * 资产融资过程中驳回中止资产信息
     * 
     * @param anAssetId
     * @return
     */
    public ScfAsset saveRejectOrBreakAsset(Long anAssetId) {

        BTAssert.notNull(anAssetId, "资产驳回失败,请选择要驳回的资产");
        // 查找资产信息
        ScfAsset asset = findAssetByid(anAssetId);
        BTAssert.notNull(asset, "资产驳回失败,请选择要驳回的资产");
        logger.info("saveRejectOrBreakAsset begin:..资产融资过程中驳回中止资产信息 Asset=" + asset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        // 校验资产状态
        checkAnnulStatus(asset);
        // 校验当前公司是否有权限
        checkCurrentCompanyPermission(asset);
        // 更新资产的状态(将现在的资产设置为不是最新状态，重新升级资产的版本)
        assetBasedataService.saveRejectOrBreakAssetByAssetId(anAssetId);

        asset.initRejectOrBreakAsset(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(asset);
        logger.info("saveRejectOrBreakAsset end:..资产融资过程中驳回中止资产信息 Asset=" + asset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        return asset;
    }

    private void checkAnnulStatus(ScfAsset anAsset) {

        // checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true,
        // "当前资产已经废止,不能进行废止");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true,
                "当前资产已经转让,不能进行废止");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, false,
                "当前资产尚未在融资过程中,不能进行驳回");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true,
                "当前资产已经过期,不能进行废止");

    }

    /**
     * 校验基础资产的状态和修改基础资产的数据记录
     * 
     * @param anAsset
     */
    private void checkBaseDataStatusAndUpdateBaseData(ScfAsset anAsset) {

        BTAssert.notNull(anAsset, "修改资产 失败-未找到资产信息");
        BTAssert.notNull(anAsset.getBasedataMap(), "修改资产 失败-未找到资产信息");
        Object orderObj = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_ORDER_LIST_KEY);
        Object agreementObj = anAsset.getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
        Object billObj = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_BILL_LIST_KEY);
        Object invoiceObj = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
        Object receivableObj = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
        // 更新订单
        if (orderObj != null && orderObj instanceof List) {

            List<ScfOrderDO> orderList = (List<ScfOrderDO>) orderObj;
            for (ScfOrderDO order : orderList) {
                // 校验单据的状态
                order.checkFinanceStatus();
                orderService.updateBaseAssetStatus(order.getRefNo(), order.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED,
                        order.getDocStatus());
            }

        }

        // 更新票据
        if (billObj != null && billObj instanceof List) {

            List<ScfAcceptBillDO> billList = (List<ScfAcceptBillDO>) billObj;
            for (ScfAcceptBillDO bill : billList) {
                // 校验单据的状态
                bill.checkFinanceStatus();
                billService.updateBaseAssetStatus(bill.getRefNo(), bill.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED,
                        bill.getDocStatus());
            }

        }
        // 更新发票
        if (invoiceObj != null && invoiceObj instanceof List) {

            List<ScfInvoiceDO> invoiceList = (List<ScfInvoiceDO>) invoiceObj;
            for (ScfInvoiceDO invoice : invoiceList) {
                // 校验单据的状态
                invoice.checkFinanceStatus();
                invoiceService.updateBaseAssetStatus(invoice.getRefNo(), invoice.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED,
                        invoice.getDocStatus());
            }

        }
        // 更新应收应付账款
        if (receivableObj != null && receivableObj instanceof List) {

            List<ScfReceivableDO> receivableList = (List<ScfReceivableDO>) receivableObj;
            for (ScfReceivableDO receivable : receivableList) {
                // 校验单据的状态
                receivable.checkFinanceStatus();
                receivableService.updateBaseAssetStatus(receivable.getId(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED,
                        receivable.getDocStatus());
            }

        }

        // 更新贸易合同的状态agreementObj
        if (agreementObj != null && agreementObj instanceof List) {

            List<ContractLedger> agreementList = (List<ContractLedger>) agreementObj;
            for (ContractLedger agreement : agreementList) {
                // 校验单据的状态
                agreement.checkReceivableFinanceStatus();
                contractLedgerService.updateBaseAssetStatus(agreement.getRefNo(), agreement.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED);
            }

        }

    }

    /**
     * 校验当前公司是否有权限
     * 
     * @param anAsset
     */
    private void checkCurrentCompanyPermission(ScfAsset anAsset) {

        BTAssert.notNull(anAsset, "修改资产 失败-未找到资产信息");
        BTAssert.notNull(anAsset.getCustMap(), "修改资产 失败-未找到资产信息");
        Map<String, Object> custMap = anAsset.getCustMap();
        Object custInfo = custMap.get(AssetConstantCollentions.CUST_INFO_KEY);
        Object coreCustInfo = custMap.get(AssetConstantCollentions.CORE_CUST_INFO_KEY);
        Object factoryCustInfo = custMap.get(AssetConstantCollentions.FACTORY_CUST_INFO_KEY);
        Collection<Long> custNos = getCurrentUserCustNos();
        boolean flag = false;
        if (custInfo instanceof CustInfo) {

            CustInfo cust = (CustInfo) custInfo;
            if (custNos.contains(cust.getCustNo())) {
                flag = true;
            }
        }

        if (coreCustInfo instanceof CustInfo) {

            CustInfo coreCust = (CustInfo) coreCustInfo;
            if (custNos.contains(coreCust.getCustNo())) {
                flag = true;
            }
        }

        if (factoryCustInfo instanceof CustInfo) {

            CustInfo factoryCust = (CustInfo) factoryCustInfo;
            if (custNos.contains(factoryCust.getCustNo())) {
                flag = true;
            }
        }

        if (!flag) {

            BTAssert.notNull(anAsset, "校验公司权限失败!你没有当前资产的操作权限");
        }

    }

    private void checkConfirmStatus(ScfAsset anAsset) {

        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true,
                "当前资产已经废止,不能进行融资");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true,
                "当前资产已经转让,不能进行融资");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, true,
                "当前资产已经在融资流程中,不能进行融资");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true,
                "当前资产已经废止,不能进行融资");

    }

    /**
     * 校验当前资产是否符合修改条件
     * 
     * @param anAsset
     */
    private void checkModifyStatus(ScfAsset anAsset) {

        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true,
                "当前资产已经废止,不能修改");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true,
                "当前资产已经转让,不能修改");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, true,
                "当前资产已经在融资流程中,不能修改");
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true,
                "当前资产已经废止,不能修改");

    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (StringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    public ScfAsset saveAssetToOne(Long anAssetId) {

        BTAssert.notNull(anAssetId, "修改资产 失败-未找到资产信息");
        ScfAsset scfAsset = this.selectByPrimaryKey(anAssetId);
        BTAssert.notNull(scfAsset, "修改资产 失败-未找到资产信息");
        scfAsset.setSuffixId(123213123l);
        this.updateByPrimaryKeySelective(scfAsset);
        return scfAsset;
    }

    /**
     * 融资结束，修改资产相关信息
     * 
     * @param anAssetId
     * @return
     */
    public ScfAsset saveAssignmentAssetToFactory(Long anAssetId) {

        BTAssert.notNull(anAssetId, "资产转移出错，请传递资产信息");
        // 查找当前资产信息
        ScfAsset oldAsset = this.findAssetByid(anAssetId);
        BTAssert.notNull(oldAsset, "资产转移出错，没有找到相关资产信息");
        logger.info("saveAssignmentAssetToFactory begin:..资产转让：资产详细信息 oldAsset=" + oldAsset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());

        // 校验当前公司是否有权限
        checkCurrentCompanyPermission(oldAsset);

        // 将原来的资产信息状态修改为已转让
        oldAsset.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT);

        // 新增新的资产信息(拥有者变成保理公司)
        ScfAsset newAsset = new ScfAsset();
        try {
            BeanUtils.copyProperties(newAsset, oldAsset);
        }
        catch (Exception e) {
            BTAssert.notNull(null, "资产转移出错，请查询当前资产详细信息");
            logger.info("资产转移出错，请查询当前资产详细信息 anAsset=" + oldAsset + "  当前操作用户为:" + UserUtils.getOperatorInfo().getName()
                    + " 错误信息为:" + e.getMessage());
        }
        BTAssert.notNull(newAsset, "资产转移出错，请查询当前资产详细信息");
        newAsset.initAdd();
        Object object = newAsset.getCustMap().get(AssetConstantCollentions.FACTORY_CUST_INFO_KEY);
        if (object != null && object instanceof CustInfo) {

            CustInfo factoryCustInfo = (CustInfo) object;
            newAsset.setCustNo(factoryCustInfo.getCustNo());
            newAsset.setCustName(factoryCustInfo.getCustName());
        }
        newAsset.setPrefixId(oldAsset.getId());
        oldAsset.setSuffixId(newAsset.getId());
        // 修改资产基础数据的状态（变成已经转让的状态）
        saveBaseDataStatusToAssignment(oldAsset.getBasedataMap());
        // 新增资产公司记录
        assetCompanyService.saveInsertCompanyFromOldToNewWithId(oldAsset.getId(), newAsset.getId());
        // 新增资产基础数据记录
        assetBasedataService.saveInsertBaseDataFromOldToNewWithId(oldAsset.getId(), newAsset.getId());
        // 回写数据
        this.insertSelective(newAsset);
        this.updateByPrimaryKeySelective(oldAsset);
        logger.info("saveAssignmentAssetToFactory begin:..资产转让：资产详细信息 newAsset=" + newAsset + "  当前操作用户为:"
                + UserUtils.getOperatorInfo().getName());
        // 返回最新的资产信息
        return newAsset;

    }

    /**
     * 将资产的基础数据信息的状态改成已经转让
     * 
     * @param anBasedataMap
     */
    private void saveBaseDataStatusToAssignment(Map<String, Object> anBasedataMap) {

        if (anBasedataMap != null) {

            // 保存订单状态转变
            Object orderList = anBasedataMap.get(AssetConstantCollentions.SCF_ORDER_LIST_KEY);
            saveOrderListToAssignment(orderList);

            // 票据
            Object billList = anBasedataMap.get(AssetConstantCollentions.SCF_BILL_LIST_KEY);
            saveBillListToAssignment(billList);

            // 发票
            Object invoiceList = anBasedataMap.get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
            saveInvoiceListToAssignment(invoiceList);

            // 应收应付账款
            Object receivableList = anBasedataMap.get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
            saveReceivableListToAssignment(receivableList);

            // 合同
            Object agreementList = anBasedataMap.get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
            saveAgreementListToAssignment(agreementList);

        }

    }

    private void saveAgreementListToAssignment(Object object) {

        if (object != null && object instanceof List) {
            List<ContractLedger> agreementList = (List<ContractLedger>) object;
            for (ContractLedger agreement : agreementList) {
                agreement.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                agreement.setBusinVersionStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                contractLedgerService.updateByPrimaryKeySelective(agreement);
            }
        }

    }

    private void saveReceivableListToAssignment(Object object) {

        if (object != null && object instanceof List) {
            List<ScfReceivableDO> receivableList = (List<ScfReceivableDO>) object;
            for (ScfReceivableDO receivable : receivableList) {
                receivable.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                receivableService.updateByPrimaryKeySelective(receivable);
            }
        }

    }

    private void saveInvoiceListToAssignment(Object object) {

        if (object != null && object instanceof List) {
            List<ScfInvoiceDO> invoiceList = (List<ScfInvoiceDO>) object;
            for (ScfInvoiceDO invoice : invoiceList) {
                invoice.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                invoiceService.updateByPrimaryKeySelective(invoice);
            }
        }

    }

    private void saveBillListToAssignment(Object object) {

        if (object != null && object instanceof List) {
            List<ScfAcceptBillDO> billList = (List<ScfAcceptBillDO>) object;
            for (ScfAcceptBillDO bill : billList) {
                bill.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                billService.updateByPrimaryKeySelective(bill);
            }
        }

    }

    private void saveOrderListToAssignment(Object object) {
        if (object != null && object instanceof List) {
            List<ScfOrderDO> orderList = (List<ScfOrderDO>) object;
            for (ScfOrderDO order : orderList) {
                order.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_TRANSFER);
                orderService.updateByPrimaryKeySelective(order);
            }
        }
    }

    /**
     * 整合模式申请总的入口
     * 
     * @param anAssetMap
     * @return
     */
    public ScfAsset saveAddAssetNewTotal(Map<String, Object> anAssetMap) {

        BTAssert.notNull(anAssetMap, "新增资产企业 失败-资产 is null");
        BTAssert.notNull(anAssetMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_BY_RECEIVABLEID_KEY), "请选择应收账款!");

        List<ScfReceivableDO> receivableList = queryReceivableList(anAssetMap);
        ScfAsset asset = new ScfAsset();
        try {
            BeanUtils.populate(asset, anAssetMap);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        boolean isRequestAsset = true;
        boolean isLockedAsset = true;
        if (anAssetMap.containsKey(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_REQUEST_ASSET) && "false"
                .equals(anAssetMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_REQUEST_ASSET).toString())) {
            isRequestAsset = false;
        }

        if (anAssetMap.containsKey(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_LOCKED_ASSET) && "false"
                .equals(anAssetMap.get(AssetConstantCollentions.RECEIVABLE_REQUEST_IS_LOCKED_ASSET).toString())) {
            isLockedAsset = false;
        }

        asset.initAdd();
        for (ScfReceivableDO receivable : receivableList) {
            // 创建asset对象和公司，基础数据等对象信息 并且插入企业表和基础数据表
            asset = createAndBuilderAssetTotal(asset, receivable, isRequestAsset, isLockedAsset);
        }

        this.insert(asset);
        // 查询完整的资产信息
        ScfAsset findAssetByid = findAssetByid(asset.getId());
        // ------------
        return findAssetByid;

    }

    /**
     * 
     * @param anAsset
     * @param anReceivable
     * @param isRequestAsset
     *            是否必须要
     * @param isLockedAsset
     *            是否锁定资产
     * @return
     */
    private ScfAsset createAndBuilderAssetTotal(ScfAsset anAsset, ScfReceivableDO anReceivable, boolean isRequestAsset,
            boolean isLockedAsset) {

        // 处理发票信息
        packageInvoiceToAssetRequsetLocket(anAsset, anReceivable, isRequestAsset, isLockedAsset);
        // 处理贸易合同
        packageAgreementToAssetRequsetLocket(anAsset, anReceivable, isRequestAsset, isLockedAsset);
        // 将应收账款信息封装到资产中 （并且插入公司表记录）并且将应收账款信息插入到资产基础数据表中去
        packageReceivableToAsset(anAsset, anReceivable);
        return anAsset;
    }

    /**
     * 封装贸易合同信息
     * 
     * @param anAsset
     * @param anReceivable
     * @param isRequestAsset
     *            是否必须要
     * @param isLockedAsset
     *            是否锁定资产
     */
    private void packageAgreementToAssetRequsetLocket(ScfAsset anAsset, ScfReceivableDO anReceivable,
            boolean anIsRequestAsset, boolean anIsLockedAsset) {

        if (anIsRequestAsset) {

            String agreeNo = anReceivable.getAgreeNo();
            if (StringUtils.isBlank(agreeNo)) {
                BTAssert.notNull(null, "应收账款的贸易合同号为空!操作失败");
            }
            // 根据贸易合同编号查询贸易合同
            ContractLedger agreement = contractLedgerService.selectOneByAgreeNo(agreeNo);
            BTAssert.notNull(agreement, "应收账款的贸易合同未找到!操作失败");
            checkStatus(agreement.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false,
                    "请核准贸易合同");
            checkStatus(anReceivable.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, false,
                    "应付账款不是生效状态，无法进行申请");
            checkStatus(anReceivable.getCustNo() + "", agreement.getCustNo() + "", false, "应收账款和贸易合同对应的企业不一致");
            // 将合同插入到资产数据表中
            packageBaseVersionEntityToAsset(anAsset, agreement,
                    AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT, anIsLockedAsset);

        } else {

            if (StringUtils.isNotBlank(anReceivable.getAgreeNo())) {
                ContractLedger agreement = contractLedgerService.selectOneByAgreeNo(anReceivable.getAgreeNo());
                if (agreement != null
                        && agreement.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE)
                        && agreement.getLockedStatus().equals(VersionConstantCollentions.LOCKED_STATUS_INlOCKED)) {
                    // 将合同插入到资产数据表中
                    packageBaseVersionEntityToAsset(anAsset, agreement,
                            AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT, anIsLockedAsset);

                }

            }

        }

    }

    /**
     * 关联发票信息
     * 
     * @param anAsset
     * @param anReceivable
     * @param isRequestAsset
     * @param isLockedAsset
     */
    private void packageInvoiceToAssetRequsetLocket(ScfAsset anAsset, ScfReceivableDO anReceivable,
            boolean isRequestAsset, boolean isLockedAsset) {

        // 查询发票列表
        if (StringUtils.isNoneBlank(anReceivable.getInvoiceNos())) {

            List<ScfInvoiceDO> invoiceList = invoiceService.queryReceivableList(anReceivable.getInvoiceNos());
            // 当一条记录都没有找到
            if (Collections3.isEmpty(invoiceList)) {
                if (isRequestAsset) {
                    BTAssert.notNull(null, "当前应收账款对应的发票未找到!");
                }
            }

            // 当未找全所有的发票信息
            if (anReceivable.getInvoiceNos().split(",").length != invoiceList.size()) {
                if (isRequestAsset) {
                    BTAssert.notNull(null, "当前应收账款对应的发票还有未找到!");
                }
            }

            for (ScfInvoiceDO invoiceDo : invoiceList) {

                if (isRequestAsset) {

                    if (!invoiceDo.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE)) {
                        BTAssert.notNull(null, "发票号码不可用来使用,请先核准" + invoiceDo.getInvoiceNo());

                    }

                }

                // 将发票信息存入到asset中
                packageBaseVersionEntityToAsset(anAsset, invoiceDo,
                        AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE, isLockedAsset);
            }

        } else {
            if (isRequestAsset) {
                BTAssert.notNull(null, "请应收账款关联发票信息");
            }
        }

    }

    /**
     * 更新上传附件信息
     * 
     * @param anAssetId
     * @param anGoodsBatchNo
     *            文件id,文件id,.....
     * @param anString
     *            1://贸易合同 2发票
     */
    public void saveUpdateAssetBatchNo(Long anAssetId, String anGoodsBatchNo, String anString) {

        ScfAsset asset = this.selectByPrimaryKey(anAssetId);
        if ("1".equals(anString)) {
            asset.setGoodsBatchNo(custFileDubboService.updateCustFileItemInfo(anGoodsBatchNo, asset.getGoodsBatchNo()));

        } else if ("2".equals(anString)) {

            asset.setStatementBatchNo(
                    custFileDubboService.updateCustFileItemInfo(anGoodsBatchNo, asset.getStatementBatchNo()));
        } else {

        }
        this.updateByPrimaryKeySelective(asset);
        // 保存附件信息
    }

    public ScfAsset findAssetByReceivableRefNoWithVersion(String anRefNo, String anVersion) {

        Map map = QueryTermBuilder.newInstance().put("refNo", anRefNo).put("version", anVersion)
                .put("infoType", AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)
                .put("businStatus", AssetConstantCollentions.ASSET_BUSIN_STATUS_OK).build();
        List<ScfAssetBasedata> list = assetBasedataService.selectByProperty(map, "id Desc");
        if (!Collections3.isEmpty(list)) {
            return this.selectByPrimaryKey(Collections3.getFirst(list).getAssetId());
        }
        return new ScfAsset();
    }

}
