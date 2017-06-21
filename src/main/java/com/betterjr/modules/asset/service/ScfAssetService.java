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
import com.betterjr.modules.ledger.service.ContractLedgerService;
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

@Service
public class ScfAssetService extends BaseService<ScfAssetMapper, ScfAsset> {

    @Autowired
    private ScfAssetCompanyService assetCompanyService;
    
    @Autowired
    private ScfAssetBasedataService assetBasedataService;
    
    @Autowired
    private ScfReceivableDOService receivableService;//引收账款
    
    @Autowired
    private ScfInvoiceDOService invoiceService;//发票
    
    @Autowired
    private ScfOrderDOService orderService;//订单
    
    @Autowired 
    private ScfAcceptBillDOService billService;//票据
    
    @Autowired
    private ContractLedgerService contractLedgerService;//合同
    
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
     * @param anAsset
     * @return
     */
    public ScfAsset saveAddAsset(Map<String,Object> anAssetMap){
        
        BTAssert.notNull(anAssetMap, "新增资产企业 失败-资产 is null");
        anAssetMap=Collections3.filterMap(anAssetMap, new String[]{"businTypeId","productCode",
                "sourceUseType","custNo","coreCustNo","factorNo","orderList",
                "invoiceList","agreementList","receivableList","acceptBillList",
                "statementFileList","goodsFileList","othersFileList","id"});
        //将map转成资产对象
        ScfAsset asset=convertBeanFromMap(anAssetMap);
        asset.initAdd();
        //封装资产数据中的基础信息
        asset=packageAssetFromMap(asset,anAssetMap);
        logger.info("Begin to add anAsset");
        
        //插入资产企业表信息
        assetCompanyService.saveAddAssetCompanyByAsset(asset);
        //-------------------20170615
        //插入资产基础数据表信息
        //addAssetBasedata(asset);
        assetBasedataService.saveAddAssetBasedataByAsset(asset);
        
        this.insert(asset);
        logger.info("success to add anAsset");
        //asset.setOperationAuth(AssetConstantCollentions.ASSET_OPERATOR_AUTH_MAX);
        
        return asset; 
        
    }
    
  
    /**
     * 封装资产数据中的基础信息
     * @param anAsset
     * @param anAssetMap
     * @return
     */
   private ScfAsset packageAssetFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {
        
       BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
       BTAssert.notNull(anAsset, "资产初始化 失败-资产 is null");
       BTAssert.notNull(anAssetMap.get("custNo"),"资产初始化 失败-请选择供应商");
       BTAssert.notNull(anAssetMap.get("coreCustNo"),"资产初始化 失败-请选择核心企业");
       BTAssert.notNull(anAssetMap.get("factorNo"),"资产初始化 失败-请选择核心企业");
       //将企业信息列表封装到资产中
       packageAssetCompanyInfoFromMap(anAsset,anAssetMap);
       //将基础资产信息封装到资产中
       packageAssetBaseDataInfoFromMap(anAsset,anAssetMap);
       //封装资产总金额
       paceageAssetBalanceFromMap(anAsset,anAssetMap);
       //封装资产附件各个批次号
       packageAssetFileFromMap(anAsset,anAssetMap);
        return anAsset;
    }

   /**
    * 
    * 封装文件资产附件信息
    * @param anAsset
    * @param anAssetMap
    */
   
   private void packageAssetFileFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {
        
       //对账单附件列表
       if(anAssetMap.containsKey("statementFileList") &&anAssetMap.get("statementFileList") !=null ){
           
           anAsset.setStatementBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anAssetMap.get("statementFileList").toString(), anAsset.getStatementBatchNo()));
           
       }
       
     //商品出库单附件列表
       if(anAssetMap.containsKey("goodsFileList") &&anAssetMap.get("goodsFileList") !=null ){
           
           anAsset.setGoodsBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anAssetMap.get("goodsFileList").toString(), anAsset.getGoodsBatchNo()));
           
       }
       
       //其他附件列表
       if(anAssetMap.containsKey("othersFileList") &&anAssetMap.get("othersFileList") !=null ){
           
           anAsset.setOthersBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anAssetMap.get("othersFileList").toString(), anAsset.getOthersBatchNo()));
           
       }
        
    }


   /**
    * 封装资产的总金额
    * @param anAsset
    * @param anAssetMap
    */
   private ScfAsset paceageAssetBalanceFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {
        
       //获取主体资产类型
       String AssetType = findProductMainAssetType(anAssetMap);
       BigDecimal totalBalance=new BigDecimal(0);
       if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(AssetType)){
           Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_BILL_LIST_KEY);
           if(object instanceof List){
               List<ScfAcceptBillDO> billList=(List<ScfAcceptBillDO>) object;
               for (ScfAcceptBillDO bill : billList) {
                   totalBalance= MathExtend.add(totalBalance, bill.getBalance());
               }
           }
       }
       if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT.equals(AssetType)){
           Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
           if(object instanceof List){
               List<ContractLedger> agreeList=(List<ContractLedger>) object;
               for (ContractLedger agree : agreeList) {
                   totalBalance= MathExtend.add(totalBalance, new BigDecimal(agree.getBalance()));
               }
           }
       }
       
       if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(AssetType)){
           Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
           if(object instanceof List){
               List<ScfInvoiceDO> invoiceList=(List<ScfInvoiceDO>) object;
               for (ScfInvoiceDO invoice : invoiceList) {
                   totalBalance= MathExtend.add(totalBalance, invoice.getBalance());
               }
           }
       }
       
       if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(AssetType)){
           Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_ORDER_LIST_KEY);
           if(object instanceof List){
               List<ScfOrderDO> orderList=(List<ScfOrderDO>) object;
               for (ScfOrderDO order : orderList) {
                   totalBalance= MathExtend.add(totalBalance, order.getBalance());
               }
           }
       }
       
       if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(AssetType)){
           Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
           if(object instanceof List){
               List<ScfReceivableDO> receivableList=(List<ScfReceivableDO>) object;
               for (ScfReceivableDO receivable : receivableList) {
                   totalBalance= MathExtend.add(totalBalance, receivable.getBalance());
               }
           }
       }
       
       anAsset.setBalance(totalBalance);
       return anAsset;
        
    }


/**
    * 将基础资产信息封装到资产中秋
    * @param anAsset
    * @param anAssetMap
    */
   private ScfAsset packageAssetBaseDataInfoFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {
       
       BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
       BTAssert.notNull(anAsset, "资产初始化 失败-资产 is null");
       
       if(anAssetMap.containsKey(AssetConstantCollentions.SCF_ORDER_LIST_KEY)){
           List<ScfOrderDO> orderList=orderService.queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.SCF_ORDER_LIST_KEY).toString());
           anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_ORDER_LIST_KEY, orderList);
       }
       if(anAssetMap.containsKey(AssetConstantCollentions.SCF_INVOICE_LIST_KEY)){
           List<ScfInvoiceDO> invoiceList=invoiceService.queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY).toString());
           anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_INVOICE_LIST_KEY, invoiceList);
       }
       if(anAssetMap.containsKey(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY)){
           List<ScfReceivableDO> receivableList=receivableService.queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY).toString());
           anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, receivableList);
       }
       if(anAssetMap.containsKey(AssetConstantCollentions.SCF_BILL_LIST_KEY)){
           List<ScfAcceptBillDO> billList=billService.queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.SCF_BILL_LIST_KEY).toString());
           anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_BILL_LIST_KEY, billList);
       }
       if(anAssetMap.containsKey(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY)){
           List<ContractLedger> agreementList=contractLedgerService.queryBaseVersionObjectByids(anAssetMap.get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY).toString());
           anAsset.getBasedataMap().put(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, agreementList);
       }
      
       return anAsset;
    }


   /**
    * 通过保理产品编号查询主资产的类型
    * @param anAsset
    * @param anAssetMap
    */
    private String findProductMainAssetType(Map<String, Object> anAssetMap) {
        BTAssert.notNull(anAssetMap.get("productCode"),"资产初始化 失败-请选择保理产品");
           List<ScfAssetDict> productAssetDictList = productAssetService.queryProductAssetDict(anAssetMap.get("productCode").toString());
           BTAssert.notNull(productAssetDictList,"资产初始化 失败-请先配置当前保理产品");
           for (ScfAssetDict scfAssetDict : productAssetDictList) {
            if(scfAssetDict.getAssetType().equals("1")){
                logger.info("融资核心资产类型为:"+scfAssetDict); 
                if("1".equals(scfAssetDict.getDictType())){
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL;
                }else if("2".equals(scfAssetDict.getDictType())){
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE;
                }else if("3".equals(scfAssetDict.getDictType())){
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT;
                }else if("4".equals(scfAssetDict.getDictType())){
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE;
                }else if("5".equals(scfAssetDict.getDictType())){
                    return AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER;
                }else{
                    BTAssert.notNull(null,"资产初始化 失败-当前保理产品主体资产不符合要求(只能是订单,应收应付账款,合同，发票，汇票)");
                    return "";
                }
            }
        }
           
        BTAssert.notNull(null,"资产初始化 失败-当前保理产品请选择主体资产");
        return "";
    }


   /**
    * 将企业信息列表封装到资产中
    * @param anAsset
    * @param anAssetMap
    * @return
    */
   private ScfAsset packageAssetCompanyInfoFromMap(ScfAsset anAsset, Map<String, Object> anAssetMap) {
        
       if(!getCurrentUserCustNos().contains(anAsset.getCustNo())){
           BTAssert.notNull(null, "资产初始化 失败-你不是当前供应商的用户!没有权限");
       }
       //Map<String, Object> custMap = anAsset.getCustMap();
       //封装供应商信息
       CustInfo custInfo=findCustInfoById(anAsset.getCustNo());
       if(anAsset.getCustType().equals(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY)){
           
           packageAssetCompanyFromCustInfo(anAsset,custInfo,AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
       }else{
           packageAssetCompanyFromCustInfo(anAsset,custInfo,AssetConstantCollentions.SCF_ASSET_ROLE_DEALER);
           
       }
       //封装核心企业信息
       CustInfo coreCustInfo=findCustInfoById(anAsset.getCoreCustNo());
       packageAssetCompanyFromCustInfo(anAsset,coreCustInfo,AssetConstantCollentions.SCF_ASSET_ROLE_CORE);
       //封装保理公司信息
       CustInfo factorCustInfo=findCustInfoById(anAsset.getFactorNo());
       packageAssetCompanyFromCustInfo(anAsset,factorCustInfo,AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY);
       
       return anAsset;
        
    }


   private ScfAsset packageAssetCompanyFromCustInfo(ScfAsset anAsset, CustInfo anCustInfo, String anScfAssetRoleSupply) {
    
       ScfAssetCompany company=new ScfAssetCompany();
       company.setAssetId(anAsset.getId());
       company.setBusinStatus(AssetConstantCollentions.ASSET_BUSIN_STATUS_OK);
       company.setAssetRole(anScfAssetRoleSupply);
       company.setCustInfo(anCustInfo);
       company.setCustNo(anCustInfo.getCustNo());
       company.setCustName(anCustInfo.getCustName());
       if(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY.equals(anScfAssetRoleSupply) ||AssetConstantCollentions.SCF_ASSET_ROLE_DEALER.equals(anScfAssetRoleSupply)){
           
           anAsset.setCustName(anCustInfo.getCustName());
           anAsset.getCustMap().put(AssetConstantCollentions.CUST_INFO_KEY, company);
       }else if(AssetConstantCollentions.SCF_ASSET_ROLE_CORE.equals(anScfAssetRoleSupply)){
           //company.setCustName(anCustInfo.getCustName());
           anAsset.setCoreCustName(anCustInfo.getCustName());
           anAsset.getCustMap().put(AssetConstantCollentions.CORE_CUST_INFO_KEY, company);
       }else if(AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY.equals(anScfAssetRoleSupply)){
           //company.setCustName(anCustInfo.getCustName());
           //anAsset.setCoreCustName(anCustInfo.getCustName());
           anAsset.getCustMap().put(AssetConstantCollentions.FACTORY_CUST_INFO_KEY, company);
       }else{
           
       }
       
       
       return  anAsset;
    
}


/**
    * 通过公司id查找公司详情
    * @param anCustNo
    * @return
    */
    private CustInfo findCustInfoById(Long anCustNo) {
        
        return custAccountService.findCustInfo(anCustNo);
    }


/**
    * 将map中的数据转换成资产对象
    * @param anAssetMap
    * @return
    */
   private ScfAsset convertBeanFromMap(Map<String, Object> anAssetMap) {
        
       BTAssert.notNull(anAssetMap, "资产初始化 失败-资产 is null");
       ScfAsset asset=new ScfAsset();
       try {
        BeanUtils.populate(asset, anAssetMap);
        verificationAssetProperties(asset);
        if(UserUtils.supplierUser()){
            asset.setCustType(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY); 
        }else{
            asset.setCustType(AssetConstantCollentions.SCF_ASSET_ROLE_DEALER);
        }
        //区分是新增还是修改资产
        saveModifyAsset(asset);
    }
    catch (IllegalAccessException | InvocationTargetException e) {
        logger.info("map-------->asset失败"+e.getMessage());
        BTAssert.notNull(null, "资产初始化 失败-数据不符合要求"+e.getMessage());
    }
        return asset;
    }
   
   private void verificationAssetProperties(ScfAsset asset){
       
       BTAssert.notNull(asset, "资产初始化 失败-资产 is null");
       BTAssert.hasLength(asset.getBusinTypeId(),"资产初始化 失败-业务类型is null");
       BTAssert.hasLength(asset.getProductCode(),"资产初始化 失败-保理产品is null");
       BTAssert.hasLength(asset.getSourceUseType(),"资产初始化 失败-请指定业务类型(融资/询价)");
       BTAssert.notNull(asset.getCustNo(),"资产初始化 失败-请选择供应商");
       BTAssert.notNull(asset.getCoreCustNo(),"资产初始化 失败-请选择核心企业");
       BTAssert.notNull(asset.getFactorNo(),"资产初始化 失败-请选择保理企业");
       
   }
   

   /**
    *查询资产信息，
    * @param assetId  资产id
    * @param custNo   当前企业id(用来校验权限)
    * onOff: true 表示需要查询资产企业的详情信息   false 只需要查询一些基础数据信息 企业查询编号和企业名字  true 需要查询企业的全部信息
    * @return   返回的资产全部信息，并且封装好当前企业的操作权限
    */
    public ScfAsset findAssetByid(Long assetId){
        
        ScfAsset asset=this.selectByPrimaryKey(assetId);
        //查询资产企业信息
        asset=assetCompanyService.selectByAssetId(asset);
        if (asset.getCustMap().isEmpty()) {
            BTAssert.notNull(null, "查询资产企业 失败-该公司没有权限 is null");
        }
        //查询订单详细信息
        asset=assetBasedataService.selectByAssetId(asset);
        return asset;
        
    }


    /**
     * 根据资产信息插入资产基础数据表信息
     * @param anAsset
     */
    private void addAssetBasedata(ScfAsset anAsset) {
        
        Map<String, Object> basedataMap = anAsset.getBasedataMap();
        BTAssert.notNull(basedataMap, "新增资产企业 失败-订单列表basedataMap is null");
        logger.info("success to add addAssetBasedata");
        //插入订单
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_ORDER_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER);
        logger.info("success to add addAssetBasedata 订单");
        //插入发票
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_INVOICE_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE);
        logger.info("success to add addAssetBasedata 发票");
        //插入合同
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT);
        logger.info("success to add addAssetBasedata 合同");
        //插入引收账款
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE);
        logger.info("success to add addAssetBasedata 引收账款");
        //插入运输单据
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_TRANSPORT_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_TRANSPORT);
        logger.info("success to add addAssetBasedata 运输单据");
        //插入票据
        addAssetBasedataDetail(basedataMap, AssetConstantCollentions.SCF_BILL_LIST_KEY, anAsset.getId(),AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL);
        logger.info("success to add addAssetBasedata 票据");
    }
    
    private void addAssetBasedataDetail(Map<String, Object> basedataMap,String key,Long assetId,String infoType){
        Object object = basedataMap.get(key);
        if(object !=null){
            List<BaseVersionEntity> orders=(List<BaseVersionEntity>) object;
            for (BaseVersionEntity scfOrder : orders) {
                BTAssert.notNull(scfOrder.getRefNo(), "新增资产企业 失败-订单列表refno is null");
                BTAssert.notNull(scfOrder.getVersion(), "新增资产企业 失败-订单列表version is null");
                ScfAssetBasedata assetBasedata=new ScfAssetBasedata();
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
     * @param anAsset
     */
    private void addAssetCompany(ScfAsset anAsset) {
        logger.info("Begin to add addAssetCompany");
        //插入资产企业中的供应商/经销商
        CustInfo cust= getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.CUST_INFO_KEY).get(0);//企业信息
        BTAssert.notNull(cust, "新增资产企业 失败-addAssetCompany 供应商/经销商 is null");
        Integer operatorStatu=getOperator(anAsset.getCustMap(),AssetConstantCollentions.CUST_INFO_STATUS);//操作权限
        ScfAssetCompany anAssetCompany=getAssetCompany(cust,anAsset.getCustType(),operatorStatu,anAsset.getId());
        assetCompanyService.addAssetCompany(anAssetCompany);
        logger.info("success to add addAssetCompany 供应商/经销商");
        anAsset.setCustNo(cust.getCustNo());
        anAsset.setCustName(cust.getCustName());
        //插入资产企业中的核心企业
        CustInfo coreCust= getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.CORE_CUST_INFO_KEY).get(0);
        BTAssert.notNull(coreCust, "新增资产企业 失败-addAssetCompany 核心企业 is null");
        Integer coreOperatorStatu=getOperator(anAsset.getCustMap(),AssetConstantCollentions.CORE_CUST_INFO_STATUS);
        ScfAssetCompany coreAssetCompany=getAssetCompany(coreCust,AssetConstantCollentions.SCF_ASSET_ROLE_CORE,coreOperatorStatu,anAsset.getId());
        assetCompanyService.addAssetCompany(coreAssetCompany);
        anAsset.setCoreCustNo(coreCust.getCustNo());
        anAsset.setCoreCustName(coreCust.getCustName());
        logger.info("success to add addAssetCompany 核心企业");
        //插入资产企业中的保理公司
        List<CustInfo> factoryCust= getCustInfo(anAsset.getCustMap(), AssetConstantCollentions.FACTORY_CUST_INFO_KEY);
        BTAssert.notNull(factoryCust, "新增资产企业 失败-addAssetCompany 保理公司 is null");
        Integer factoryOperatorStatu=getOperator(anAsset.getCustMap(),AssetConstantCollentions.FACTORY_CUST_INFO_STATUS);
        for (CustInfo custInfo : factoryCust) {
            ScfAssetCompany factoryAssetCompany=getAssetCompany(custInfo,AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY,factoryOperatorStatu,anAsset.getId());
            assetCompanyService.addAssetCompany(factoryAssetCompany);
        }
        logger.info("success to add addAssetCompany");
    }
    
    /**
     * 获取当前对象的操作权限
     */
    private Integer getOperator(Map<String, Object> anCustMap, String key) {
        
        BTAssert.notNull(anCustMap, "新增资产企业 失败-企业列表anCustMap is null");
        Integer status=1;
        Object object = anCustMap.get(key);
        try{
            status=Integer.parseInt(object.toString());
        }catch(Exception e){
            return status;
        }
        return status;
    }

    /**
     * 初始化资产企业表
     * @param custInfo  企业信息
     * @param assetRole  企业担任的角色
     * @param operatorStatus 企业权限 1 读 2 写 4 删除
     * @param assetId   资产id
     * @return
     */
    public ScfAssetCompany getAssetCompany(CustInfo custInfo,String assetRole,Integer operatorStatus,Long assetId){
        
        ScfAssetCompany assetCompany=new ScfAssetCompany();
        assetCompany.setAssetId(assetId);
        assetCompany.setAssetRole(assetRole);
        assetCompany.setOperatorStatus(operatorStatus);
        assetCompany.setCustNo(custInfo.getCustNo());
        assetCompany.setCustName(custInfo.getCustName());
        return assetCompany;
        
    }
    
    
    /**
     * 从资产表中获取到相关企业的数据
     * @param custMap
     * @param key
     * @return
     */
    public List<CustInfo> getCustInfo(Map<String, Object> custMap,String key){
        
        BTAssert.notNull(custMap, "新增资产企业 失败-企业列表custMap is null");
        Object object = custMap.get(key);
        BTAssert.notNull(object, "新增资产企业 失败-企业列表当前key is null"+key);
        List<CustInfo> list=new ArrayList<>();
        if(key.equals(AssetConstantCollentions.CUST_INFO_KEY) || key.equals(AssetConstantCollentions.CORE_CUST_INFO_KEY)){
            //如果是供应商和核心企业
            CustInfo custinfo=(CustInfo) object;
            list.add(custinfo);
        }else{
            list=(List<CustInfo>) object;
        }
        return list;
    }
    
    /**
     * 查找符合条件的资产基础数据
     * @param anCustNo  供应商id
     * @param anDataType  资产类型   关联的基础数据的类型1订单2票据3应收账款4发票5贸易合同6运输单单据类型
     * @param anFlag
     * @param anFlag 
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page queryFinanceBaseDataList(Long anCustNo,Long anCoreCustNo,String anDataType,String anIds, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anCustNo, "查询可用资产失败!供应商id不存在");
        BTAssert.notNull(anCoreCustNo, "查询可用资产失败!核心企业id不存在");
        BTAssert.notNull(anDataType, "查询可用资产失败!请选择要查询的数据");
        if(!getCurrentUserCustNos().contains(anCustNo)){
            BTAssert.notNull(null, "查询可用资产失败!你没有当前企业资产的操作权限");  
        }
        
        Map<String,Object> paramMap = QueryTermBuilder.newInstance()
                .put("custNo", anCustNo)
                .put("coreCustNo", anCoreCustNo)
                .build();
        if(StringUtils.isNoneBlank(anIds)){
            
            List<Long> idList=convertStringToList(anIds);
            paramMap.put("NEid", idList);
            
        }
        if(anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER)){
            Page<ScfOrderDO> orderPage = orderService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
            return orderPage;
        }else if(anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL)){
            Page<ScfAcceptBillDO> billPage = billService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
            return billPage;
        }else if(anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE)){
            Page<ScfInvoiceDO> invoicePage = invoiceService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
            return invoicePage;
        }else if(anDataType.equals(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE)){
            Page<ScfReceivableDO> receivablePage = receivableService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
            return receivablePage;
        }else{
            Page<ContractLedger> agreementPage = contractLedgerService.selectCanUsePageWithVersion(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
            return agreementPage;
        }
        
    }
    
    private List<Long> convertStringToList(String anIds) {
        
        List<Long> idList=new ArrayList<Long>();
        if(StringUtils.isNotBlank(anIds)){
            
            if(anIds.contains(",")){
                
                String[] strs = anIds.split(",");
                for (String string : strs) {
                    if(StringUtils.isNoneBlank(string)){
                        try{
                            
                            idList.add(Long.parseLong(string));
                        }catch(Exception e){
                            
                        }
                        
                    }
                }
                
            }else{
                idList.add(Long.parseLong(anIds));  
            }
            
        }
        return idList;
    }


    /**
     * 获取当前登录用户所在的所有公司id集合
     * @return
     */
    private Collection<Long> getCurrentUserCustNos(){
        
        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        Collection<Long> custNos=new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return  custNos;
    }
    
    /**
     * 资产修改
     * @param anAsset
     * @return
     */
    private ScfAsset saveModifyAsset(ScfAsset anAsset){
        
        if(anAsset !=null && anAsset.getId() !=null){
            ScfAsset asset = this.selectByPrimaryKey(anAsset.getId());
            BTAssert.notNull(asset, "修改资产 失败-未找到资产信息");
            logger.info("Begin to saveModifyAsset anAsset="+asset+"  当前操作用户为:"+UserUtils.getOperatorInfo().getName());
            if(!getCurrentUserCustNos().contains(asset.getCustNo())){
                BTAssert.notNull(null, "修改资产 失败-原资产公司成员!没有权限");
            }
            checkModifyStatus(asset);
            asset.initModifyValue(UserUtils.getOperatorInfo());
            this.updateByPrimaryKeySelective(asset);
            anAsset.setPrefixId(asset.getId());
            anAsset.setId(null);
            logger.info("end to saveModifyAsset anAsset="+asset+"  当前操作用户为:"+UserUtils.getOperatorInfo().getName());
        }
        return anAsset;
    }
    
    /**
     * 资产确认提交，走融资流程
     * @param anAssetId
     * @return
     */
    public  synchronized ScfAsset saveConfirmAsset(Long anAssetId){
        
        //查找资产信息
        ScfAsset asset = findAssetByid(anAssetId);
        //校验资产状态
        checkConfirmStatus(asset);
        //校验当前公司是否有权限
        checkCurrentCompanyPermission(asset);
        //校验资产包含的所有基础资料的状态
        checkBaseDataStatusAndUpdateBaseData(asset);
        //更新资产的状态
        asset.setBusinStatus(AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE);
        this.updateByPrimaryKeySelective(asset);
        
        return  asset;
    }
    
    /**
     * 资产作废
     * @param anAssetId
     * @return
     */
    public ScfAsset saveAnnulAsset(Long anAssetId){
        
        //查找资产信息
        ScfAsset asset = findAssetByid(anAssetId);
        //校验资产状态
        checkAnnulStatus(asset);
        //校验当前公司是否有权限
        checkCurrentCompanyPermission(asset);
        //更新资产的状态
        
        asset.initAnnulAsset(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(asset);
        
        return  asset;
    }
    
    private void checkAnnulStatus(ScfAsset anAsset) {
        
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true, "当前资产已经废止,不能进行废止"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true, "当前资产已经转让,不能进行废止"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, true, "当前资产已经在融资流程中,不能进行废止"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true, "当前资产已经过期,不能进行废止"); 
      
        
    }


    /**
     * 校验基础资产的状态和修改基础资产的数据记录
     * @param anAsset
     */
    private void checkBaseDataStatusAndUpdateBaseData(ScfAsset anAsset) {
        
        BTAssert.notNull(anAsset, "修改资产 失败-未找到资产信息");
        BTAssert.notNull(anAsset.getBasedataMap(), "修改资产 失败-未找到资产信息");
        Object orderObj = anAsset.getBasedataMap().get(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER);
        Object agreementObj = anAsset.getBasedataMap().get(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT);
        Object billObj = anAsset.getBasedataMap().get(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL);
        Object invoiceObj = anAsset.getBasedataMap().get(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE);
        Object receivableObj = anAsset.getBasedataMap().get(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE);
        //更新订单
        if(orderObj !=null && orderObj instanceof List){
            
            List<ScfOrderDO> orderList=(List<ScfOrderDO>) orderObj;
            for (ScfOrderDO order : orderList) {
                //校验单据的状态
                order.checkFinanceStatus();
                orderService.updateBaseAssetStatus(order.getRefNo(), order.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED, order.getDocStatus());
            }
            
        }
        
        //更新票据
        if(billObj !=null && billObj instanceof List){
            
            List<ScfAcceptBillDO> billList=(List<ScfAcceptBillDO>) billObj;
            for (ScfAcceptBillDO bill : billList) {
                //校验单据的状态
                bill.checkFinanceStatus();
                billService.updateBaseAssetStatus(bill.getRefNo(), bill.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED, bill.getDocStatus());
            }
            
        }
        //更新发票
        if(invoiceObj !=null && invoiceObj instanceof List){
            
            List<ScfInvoiceDO> invoiceList=(List<ScfInvoiceDO>) invoiceObj;
            for (ScfInvoiceDO invoice : invoiceList) {
                //校验单据的状态
                invoice.checkFinanceStatus();
                invoiceService.updateBaseAssetStatus(invoice.getRefNo(), invoice.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED, invoice.getDocStatus());
            }
            
        }
        //更新应收应付账款
        if(receivableObj !=null && receivableObj instanceof List){
            
            List<ScfReceivableDO> receivableList=(List<ScfReceivableDO>) receivableObj;
            for (ScfReceivableDO receivable : receivableList) {
                //校验单据的状态
                receivable.checkFinanceStatus();
                invoiceService.updateBaseAssetStatus(receivable.getRefNo(), receivable.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED, receivable.getDocStatus());
            }
            
        }
        
        //更新贸易合同的状态agreementObj
        if(agreementObj !=null && agreementObj instanceof List){
            
            List<ContractLedger> agreementList=(List<ContractLedger>) agreementObj;
            for (ContractLedger agreement : agreementList) {
                //校验单据的状态
                agreement.checkFinanceStatus();
                contractLedgerService.updateBaseAssetStatus(agreement.getRefNo(), agreement.getVersion(),
                        VersionConstantCollentions.BUSIN_STATUS_USED, VersionConstantCollentions.LOCKED_STATUS_LOCKED);
            }
            
        }
        
    }


    /**
     * 校验当前公司是否有权限
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
        boolean flag=false;
        if(custInfo instanceof CustInfo){
            
            CustInfo cust=(CustInfo) custInfo;
            if(custNos.contains(cust.getCustNo())){
                flag=true;
            }
        }
        
        if(coreCustInfo instanceof CustInfo){
            
            CustInfo coreCust=(CustInfo) coreCustInfo;
            if(custNos.contains(coreCust.getCustNo())){
                flag=true;
            }
        }
        
        if(factoryCustInfo instanceof CustInfo){
            
            CustInfo factoryCust=(CustInfo) factoryCustInfo;
            if(custNos.contains(factoryCust.getCustNo())){
                flag=true;
            }
        }
        
        if(!flag){
            
            BTAssert.notNull(anAsset, "校验公司权限失败!你没有当前资产的操作权限"); 
        }
        
    }


    private void checkConfirmStatus(ScfAsset anAsset) {
        
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true, "当前资产已经废止,不能进行融资"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true, "当前资产已经转让,不能进行融资"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, true, "当前资产已经在融资流程中,不能进行融资"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true, "当前资产已经废止,不能进行融资"); 
      
        
    }


    /**
     * 校验当前资产是否符合修改条件
     * @param anAsset
     */
    private void checkModifyStatus(ScfAsset anAsset){
        
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ANNUL, true, "当前资产已经废止,不能修改"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_ASSIGNMENT, true, "当前资产已经转让,不能修改"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_EFFECTIVE, true, "当前资产已经在融资流程中,不能修改"); 
        checkStatus(anAsset.getBusinStatus(), AssetConstantCollentions.ASSET_INFO_BUSIN_STATUS_NOCAN_USE, true, "当前资产已经废止,不能修改"); 
        
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
    
    
}
