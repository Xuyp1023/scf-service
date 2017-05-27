package com.betterjr.modules.asset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.asset.dao.ScfAssetBasedataMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.entity.ScfAssetBasedata;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.order.service.ScfOrderDOService;
import com.betterjr.modules.order.service.ScfTransportService;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;

@Service
public class ScfAssetBasedataService extends BaseService<ScfAssetBasedataMapper, ScfAssetBasedata> {

    @Autowired
    private ScfCustAgreementService custAgreementService;
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
        Map<String, Object> paramMap=new HashMap<String,Object>();
        paramMap.put("assetId", anAsset.getId());
        List<ScfAssetBasedata> assetBasedata = this.selectByProperty(paramMap);
        fillBasedata(anAsset, assetBasedata);
        logger.info("success to query selectByAssetId"+anAsset.getId());
        return anAsset;
    }

    private void fillBasedata(ScfAsset anAsset, List<ScfAssetBasedata> assetBasedata) {
        BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata is null");
        List<ScfOrderDO> orderList=new ArrayList<ScfOrderDO>();
        List<CustAgreement> agreementList=new ArrayList<CustAgreement>();
        List<ScfAcceptBillDO> billList=new ArrayList<ScfAcceptBillDO>();
        List<ScfTransport> transportList=new ArrayList<ScfTransport>();
        List<ScfInvoiceDO> invoiceList=new ArrayList<ScfInvoiceDO>();
        List<ScfReceivableDO> receivableList=new ArrayList<ScfReceivableDO>();
        for (ScfAssetBasedata scfAssetBasedata : assetBasedata) {
            String type=scfAssetBasedata.getInfoType();
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata type is null");
            String refNo=scfAssetBasedata.getRefNo();
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata refNo is null");
            String version=scfAssetBasedata.getVersion();
            BTAssert.notNull(assetBasedata, "查询资产基础数据 失败-assetBasedata version is null");
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(type)){
               
                ScfOrderDO order=orderService.selectOneWithVersion(refNo, version);
                orderList.add(order);
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_AGREEMENT.equals(type)){
                CustAgreement agreement=new CustAgreement();
                //=custAgreementService.selectOneWithVersion(refNo, version);
                agreementList.add(agreement);
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(type)){
                ScfAcceptBillDO bill=acceptBillService.selectOneWithVersion(refNo, version);
                billList.add(bill);
                continue;
            }
            
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(type)){
                ScfReceivableDO receivable=receivableService.selectOneWithVersion(refNo, version);
                receivableList.add(receivable);
                continue;
            }
            if(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(type)){
                ScfInvoiceDO invoice=invoiceService.selectOneWithVersion(refNo, version);
                invoiceList.add(invoice);
            }
        }
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_ORDER_LIST_KEY, orderList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_BILL_LIST_KEY, billList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_INVOICE_LIST_KEY, invoiceList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_RECEICEABLE_LIST_KEY, receivableList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.SCF_TRANSPORT_LIST_KEY, transportList);
        anAsset.getBasedataMap().put(AssetConstantCollentions.CUST_AGREEMENT_LIST_KEY, agreementList);
    }
}
