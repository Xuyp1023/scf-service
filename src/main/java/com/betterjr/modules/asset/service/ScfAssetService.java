package com.betterjr.modules.asset.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.asset.dao.ScfAssetMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.entity.ScfAssetBasedata;
import com.betterjr.modules.asset.entity.ScfAssetCompany;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.service.ContractLedgerService;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.order.service.ScfOrderDOService;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;
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
    /**
     * 新增资产以及相关企业，基础数据信息
     * @param anAsset
     * @return
     */
    public ScfAsset addAsset(ScfAsset anAsset){
        
        BTAssert.notNull(anAsset, "新增资产企业 失败-anAsset is null");
        logger.info("Begin to add anAsset");
        anAsset.initAdd();
        
        //插入资产企业表信息
        addAssetCompany(anAsset);
        
        //插入资产基础数据表信息
        addAssetBasedata(anAsset);
        
        this.insert(anAsset);
        logger.info("success to add anAsset");
        anAsset.setOperationAuth(AssetConstantCollentions.ASSET_OPERATOR_AUTH_MAX);
        
        return anAsset; 
        
    }
    
   /**
    *查询资产信息，
    * @param assetId  资产id
    * @param custNo   当前企业id(用来校验权限)
    * onOff: true 表示需要查询资产企业的详情信息   false 只需要查询一些基础数据信息 企业查询编号和企业名字  true 需要查询企业的全部信息
    * @return   返回的资产全部信息，并且封装好当前企业的操作权限
    */
    public ScfAsset findAssetByid(Long assetId,Long custNo,boolean onOff){
        
        ScfAsset asset=new ScfAsset();
        asset.setId(assetId);
        asset.setBusinStatus(AssetConstantCollentions.ASSET_INFO_CAN_USE);
        asset=this.selectOne(asset);
        //查询资产企业信息
        asset=assetCompanyService.selectByAssetId(asset,custNo,onOff);
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
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page queryFinanceBaseDataList(Long anCustNo,String anDataType,String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anCustNo, "查询可用资产失败!供应商id不存在");
        BTAssert.notNull(anDataType, "查询可用资产失败!请选择要查询的数据");
        if(!getCurrentUserCustNos().contains(anCustNo)){
            BTAssert.notNull(null, "查询可用资产失败!你没有当前企业资产的操作权限");  
        }
        
        Map<String,Object> paramMap = QueryTermBuilder.newInstance()
                .put("custNo", anCustNo)
                .build();
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
}
