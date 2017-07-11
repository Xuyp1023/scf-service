package com.betterjr.modules.asset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.asset.dao.ScfAssetBasedataMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.entity.ScfAssetBasedata;
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
public class ScfAssetBasedataService extends BaseService<ScfAssetBasedataMapper, ScfAssetBasedata> {

    @Autowired
    private ContractLedgerService agreementService;
    @Autowired
    private ScfInvoiceDOService invoiceService;
    @Autowired
    private ScfAcceptBillDOService acceptBillService;
    @Autowired
    private ScfReceivableDOService receivableService;
    @Autowired
    private ScfOrderDOService orderService;
    /**
     * 插入资产记录的基础数据关系
     * @param anAssetBasedata
     * @return
     */
    public ScfAssetBasedata addAssetBasedata(ScfAssetBasedata anAssetBasedata){
        
        BTAssert.notNull(anAssetBasedata, "新增资产 失败-anAssetBasedata is null");
        BTAssert.notNull(anAssetBasedata.getAssetId(), "新增资产 失败-资产Id is null");
        logger.info("Begin to add addAssetBasedata");
        
        anAssetBasedata.initAdd();
        
        this.insert(anAssetBasedata);
        logger.info("success to add addAssetBasedata");
        return anAssetBasedata;
        
    }

    /**
     * 根据资产id查询对应的资产基础数据信息
     * @param anAsset
     * @param anOnOff 
     * @return 在原有资产基础上增加基础数据map
     */
    public ScfAsset selectByAssetId(ScfAsset anAsset) {
        BTAssert.notNull(anAsset, "查询资产 失败-anAsset is null");
        BTAssert.notNull(anAsset.getId(), "查询资产 失败-anAsset.getId is null");
        logger.info("Begin to query selectByAssetId"+anAsset.getId());
        
        List<ScfAssetBasedata> assetBasedata = queryBasedataByAssetId(anAsset.getId());
        fillBasedata(anAsset, assetBasedata);
        logger.info("success to query selectByAssetId"+anAsset.getId());
        return anAsset;
    }

    private void fillBasedata(ScfAsset anAsset, List<ScfAssetBasedata> assetBasedata) {
        BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata is null");
        List<ScfOrderDO> orderList=new ArrayList<ScfOrderDO>();
        List<ContractLedger> agreementList=new ArrayList<ContractLedger>();
        List<ScfAcceptBillDO> billList=new ArrayList<ScfAcceptBillDO>();
        List<ScfInvoiceDO> invoiceList=new ArrayList<ScfInvoiceDO>();
        List<ScfReceivableDO> receivableList=new ArrayList<ScfReceivableDO>();
        for (ScfAssetBasedata scfAssetBasedata : assetBasedata) {
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata type is null");
            String type=scfAssetBasedata.getInfoType();
            String refNo=scfAssetBasedata.getRefNo();
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata refNo is null");
            String version=scfAssetBasedata.getVersion();
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata version is null");
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(type)){
               
                ScfOrderDO order=orderService.selectOneWithVersion(refNo, version);
                orderList.add(order);
                anAsset.setOrderList(anAsset.getOrderList()+","+order.getId());
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT.equals(type)){
                ContractLedger agreement=agreementService.selectOneByRefNoAndVersion(refNo, version);
                //=custAgreementService.selectOneWithVersion(refNo, version);
                agreementList.add(agreement);
                anAsset.setAgreementList(anAsset.getAgreementList()+","+agreement.getId());
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(type)){
                ScfAcceptBillDO bill=acceptBillService.selectOneWithVersion(refNo, version);
                billList.add(bill);
                anAsset.setAcceptBillList(anAsset.getAcceptBillList()+","+bill.getId());
                continue;
            }
            
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(type)){
                ScfReceivableDO receivable=receivableService.selectOneWithVersion(refNo, version);
                receivableList.add(receivable);
                anAsset.setReceivableList(anAsset.getReceivableList()+","+receivable.getId());
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(type)){
                ScfInvoiceDO invoice=invoiceService.selectOneWithVersion(refNo, version);
                anAsset.setInvoiceList(anAsset.getInvoiceList()+","+invoice.getId());
                invoiceList.add(invoice);
            }
        }
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_ORDER_LIST_KEY, orderList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_BILL_LIST_KEY, billList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_INVOICE_LIST_KEY, invoiceList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, receivableList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, agreementList);
    }

    /**
     * 通过资产详情插入资产基础数据信息
     * @param anAsset
     */
    public void saveAddAssetBasedataByAsset(ScfAsset anAsset) {
       
        BTAssert.notNull(anAsset, "新增资产出错，资产为空");
        BTAssert.notNull(anAsset.getBasedataMap(), "新增资产出错，资产为空");
        
        //新增订单 类型
        if(anAsset.getBasedataMap() !=null && anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_ORDER_LIST_KEY)!=null){
            
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_ORDER_LIST_KEY);
            if(object instanceof List){
                
                List<ScfOrderDO> orderList=(List<ScfOrderDO>) object;
                saveBaseDataByBaseVersionList(orderList, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER, anAsset.getId());
            }
            
        }
        //票据类型
        if(anAsset.getBasedataMap() !=null && anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_BILL_LIST_KEY)!=null){
            
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_BILL_LIST_KEY);
            if(object instanceof List){
                
                List<ScfAcceptBillDO> billList=(List<ScfAcceptBillDO>) object;
                saveBaseDataByBaseVersionList(billList, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL, anAsset.getId());
            }
            
        }
        //发票类型
        if(anAsset.getBasedataMap() !=null && anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY)!=null){
            
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_INVOICE_LIST_KEY);
            if(object instanceof List){
                
                List<ScfInvoiceDO> invoiceList=(List<ScfInvoiceDO>) object;
                saveBaseDataByBaseVersionList(invoiceList, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE, anAsset.getId());
            }
            
        }
        //应收账款新增
        if(anAsset.getBasedataMap() !=null && anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY)!=null){
            
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY);
            if(object instanceof List){
                
                List<ScfReceivableDO> invoiceList=(List<ScfReceivableDO>) object;
                saveBaseDataByBaseVersionList(invoiceList, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE, anAsset.getId());
            }
            
        }
        
        //合同新增
        if(anAsset.getBasedataMap() !=null && anAsset.getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY)!=null){
            
            Object object = anAsset.getBasedataMap().get(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY);
            if(object instanceof List){
                
                List<ContractLedger> agreementList=(List<ContractLedger>) object;
                //saveBaseDataByBaseVersionList(invoiceList, AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE, anAsset.getId());
                for (ContractLedger agreement : agreementList) {
                    ScfAssetBasedata baseData=new ScfAssetBasedata();
                    baseData.setAssetId(anAsset.getId());
                    baseData.setInfoType(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT);
                    baseData.setRefNo(agreement.getRefNo());
                    baseData.setVersion(agreement.getVersion());
                    addAssetBasedata(baseData);
                }
                
                
            }
            
        }
        
    }
    
    private void saveBaseDataByBaseVersionList(List<? extends BaseVersionEntity> anBaseList,String anInfoType,Long anAssetId){
        
        for (BaseVersionEntity baseVersion : anBaseList) {
            ScfAssetBasedata baseData=new ScfAssetBasedata();
            baseData.setAssetId(anAssetId);
            baseData.setInfoType(anInfoType);
            baseData.setRefNo(baseVersion.getRefNo());
            baseData.setVersion(baseVersion.getVersion());
            addAssetBasedata(baseData);
        }
        
    }

    /**
     * 通过资产id将基础资产初始化到最初的状态
     * @param anAssetId
     */
    public void saveRejectOrBreakAssetByAssetId(Long anAssetId) {
        
        List<ScfAssetBasedata> basedataList = queryBasedataByAssetId(anAssetId);
        for (ScfAssetBasedata basedata : basedataList) {
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(basedata.getInfoType())){
                
                orderService.updateVersionByRefNoVersion(basedata.getRefNo(), basedata.getVersion());
            }else if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT.equals(basedata.getInfoType())){
                
                agreementService.updateVersionByRefNoVersion(basedata.getRefNo(), basedata.getVersion());
                
            }else if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(basedata.getInfoType())){
                
                acceptBillService.updateVersionByRefNoVersion(basedata.getRefNo(), basedata.getVersion());
                
            }else if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(basedata.getInfoType())){
                
                invoiceService.updateVersionByRefNoVersion(basedata.getRefNo(), basedata.getVersion());
                
            }else if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(basedata.getInfoType())){
                
                receivableService.updateVersionByRefNoVersion(basedata.getRefNo(), basedata.getVersion());
                
            }else{
                
                
                
            }
            
            
        }
        
    }
    
    /**
     * 通过资产Id查询到所有的关联的资产信息
     * @param anAssetId
     * @return
     */
    private List<ScfAssetBasedata> queryBasedataByAssetId(Long anAssetId){
        
        Map<String, Object> paramMap=QueryTermBuilder.newInstance()
                    .put("assetId", anAssetId)
                    .put("businStatus", AssetConstantCollentions.ASSET_BUSIN_STATUS_OK)
                    .build();
        List<ScfAssetBasedata> assetBasedata = this.selectByProperty(paramMap);
        
        return assetBasedata;
        
    }

    /**
     * 将旧的资产记录信息转移到新的资产记录信息
     * @param anOldId
     * @param anNewId
     */
    public void saveInsertBaseDataFromOldToNewWithId(Long anOldId, Long anNewId) {
        
        List<ScfAssetBasedata> baseDataList = queryBasedataByAssetId(anOldId);
        for (ScfAssetBasedata baseData : baseDataList) {
            baseData.setAssetId(anNewId);
            baseData.initAdd();
            this.insertSelective(baseData);
        }
        
    }
}
