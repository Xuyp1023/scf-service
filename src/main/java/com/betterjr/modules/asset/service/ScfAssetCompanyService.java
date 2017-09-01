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
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.dao.ScfAssetCompanyMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
import com.betterjr.modules.asset.entity.ScfAssetBasedata;
import com.betterjr.modules.asset.entity.ScfAssetCompany;

@Service
public class ScfAssetCompanyService extends BaseService<ScfAssetCompanyMapper, ScfAssetCompany> {

    @Autowired
    private CustAccountService custAccountService;
    /**
     * 新增资产对应公司的关系
     */
    
    public ScfAssetCompany addAssetCompany(ScfAssetCompany anAssetCompany){
        
        BTAssert.notNull(anAssetCompany, "新增资产企业 失败-anAssetCompany is null");
        BTAssert.notNull(anAssetCompany.getAssetId(), "新增资产企业 失败-资产Id is null");
        logger.info("Begin to add anAssetCompany");
        
        anAssetCompany.initAdd();
        
        this.insert(anAssetCompany);
        logger.info("success to add anAssetCompany");
        return anAssetCompany;
        
    }

    /**
     * 查询资产企业同资产id和当前客户号
     * @param anAsset
     * @param anCustNo
     * @param anOnOff 
     * @return 返回资产 在原资产基础上添加资产权限和企业map
     */
    public ScfAsset selectByAssetId(ScfAsset anAsset) {
        
        BTAssert.notNull(anAsset, "查询资产 失败-anAsset is null");
        BTAssert.notNull(anAsset.getId(), "查询资产 失败-anAsset.getId is null");
        logger.info("Begin to add selectByAssetId"+anAsset.getId());
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("assetId", anAsset.getId());
        paramMap.put("businStatus", AssetConstantCollentions.ASSET_BUSIN_STATUS_OK);
        List<ScfAssetCompany> assetCompanyList = this.selectByProperty(paramMap);
        
        for (ScfAssetCompany scfAssetCompany : assetCompanyList) {
            
            CustInfo custInfo = custAccountService.selectByPrimaryKey(scfAssetCompany.getCustNo());
            scfAssetCompany.setCustInfo(custInfo);
            if(scfAssetCompany.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY)){
                anAsset.getCustMap().put(AssetConstantCollentions.CUST_INFO_KEY, custInfo);
            }else if(scfAssetCompany.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_DEALER)){
                anAsset.getCustMap().put(AssetConstantCollentions.CUST_INFO_KEY, custInfo);
            }else if(scfAssetCompany.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_CORE)){
                anAsset.getCustMap().put(AssetConstantCollentions.CORE_CUST_INFO_KEY, custInfo);
            }else if(scfAssetCompany.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY)){
                anAsset.getCustMap().put(AssetConstantCollentions.FACTORY_CUST_INFO_KEY, custInfo);
            }else{
                
            }
            
        }
        //anAsset.setCustList(assetCompanyList);
        return anAsset;
        
    }

    /**
     * 通过资产详情插入资产公司表信息
     * @param anAsset
     */
    public void saveAddAssetCompanyByAsset(ScfAsset anAsset) {
        
        Map<String, Object> custMap = anAsset.getCustMap();
        Object custInfo = custMap.get(AssetConstantCollentions.CUST_INFO_KEY);
        if(custInfo instanceof ScfAssetCompany){
            if(UserUtils.supplierUser()){
                saveAddCompanyByCustInfo((ScfAssetCompany)custInfo, anAsset.getId(), AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
            }else{
                saveAddCompanyByCustInfo((ScfAssetCompany)custInfo, anAsset.getId(), AssetConstantCollentions.SCF_ASSET_ROLE_DEALER); 
            }
        }
        Object coreCustInfo = custMap.get(AssetConstantCollentions.CORE_CUST_INFO_KEY);
        if(coreCustInfo instanceof ScfAssetCompany){
            
            saveAddCompanyByCustInfo((ScfAssetCompany)coreCustInfo, anAsset.getId(), AssetConstantCollentions.SCF_ASSET_ROLE_CORE); 
        }
        Object factoryCustInfo = custMap.get(AssetConstantCollentions.FACTORY_CUST_INFO_KEY);
        if(factoryCustInfo instanceof ScfAssetCompany){
            saveAddCompanyByCustInfo((ScfAssetCompany)factoryCustInfo, anAsset.getId(), AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY); 
        }
        
    }
    
    
    private ScfAssetCompany saveAddCompanyByCustInfo(ScfAssetCompany company,Long anAssetId,String anAssetRole){
        
        //ScfAssetCompany company=new ScfAssetCompany();
        company.setAssetId(anAssetId);
        company.setAssetRole(anAssetRole);
        //company.setCustInfo(anCustInfo);
        //company.setCustName(anCustInfo.getCustName());
        //company.setCustNo(anCustInfo.getCustNo());
        company.initAdd();
        this.insert(company);
        return company;
        
    }

    /**
     * 将旧的资产记录信息转移到新的资产记录信息
     * @param anId
     * @param anId2
     */
    public void saveInsertCompanyFromOldToNewWithId(Long anOldId, Long anNewId) {
        
        
        List<ScfAssetCompany> companyList = queryCompanyByAssetId(anOldId);
        
        for (ScfAssetCompany company : companyList) {
            
            if(!(company.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY) || company.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_DEALER))){
                //只插入保理公司和核心企业的企业关系
                if(company.getAssetRole().equals(AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY)){
                    company.setAssetRole(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY); 
                }
                company.setAssetId(anNewId);
                company.initAdd();
                this.insert(company);
            }
        }
        
    }
    
    private List<ScfAssetCompany> queryCompanyByAssetId(Long anAssetId){
        
        Map<String, Object> paramMap=QueryTermBuilder.newInstance()
                .put("assetId", anAssetId)
                .put("businStatus", AssetConstantCollentions.ASSET_BUSIN_STATUS_OK)
                .build();
        List<ScfAssetCompany> companyList = this.selectByProperty(paramMap);
        
        return companyList;
        
    }

    /**
     * 通过asset本事基本的信息插入公司表
     * @param anAsset
     */
    public void saveAddCompanyByAssetBean(ScfAsset anAsset) {
        
        saveAddCustInfoCompanyByAsset(anAsset);
        
        saveAddCoreCustInfoCompanyByAsset(anAsset);
        
        saveAddFactoryCustInfoCompanyByAsset(anAsset);
        
        
    }

    private void saveAddFactoryCustInfoCompanyByAsset(ScfAsset anAsset) {
        
        if(anAsset.getFactorNo()!=null){
            
            CustInfo custInfo = custAccountService.selectByPrimaryKey(anAsset.getFactorNo());
            if(custInfo!=null){
                ScfAssetCompany company=new ScfAssetCompany();
                company.initAdd();
                company.setAssetId(anAsset.getId());
                company.setAssetRole(AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY);
                //company.setBankAccount(anAsset.getCoreCustBankAccount());
                //company.setBankAccountName(anAsset.getCoreCustBankAccountName());
                //company.setBankName(anAsset.getCoreCustBankName());
                company.setCustName(custInfo.getCustName());
                company.setCustNo(anAsset.getFactorNo());
                this.insert(company);
            }
        }
        
    }

    private void saveAddCoreCustInfoCompanyByAsset(ScfAsset anAsset) {
        
        ScfAssetCompany company=new ScfAssetCompany();
        company.initAdd();
        company.setAssetId(anAsset.getId());
        company.setAssetRole(AssetConstantCollentions.SCF_ASSET_ROLE_CORE);
        company.setBankAccount(anAsset.getCoreCustBankAccount());
        company.setBankAccountName(anAsset.getCoreCustBankAccountName());
        company.setBankName(anAsset.getCoreCustBankName());
        company.setCustName(anAsset.getCoreCustName());
        company.setCustNo(anAsset.getCoreCustNo());
        this.insert(company);
        
    }

    private void saveAddCustInfoCompanyByAsset(ScfAsset anAsset) {
        
        ScfAssetCompany company=new ScfAssetCompany();
        company.initAdd();
        company.setAssetId(anAsset.getId());
        company.setAssetRole(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY);
        company.setBankAccount(anAsset.getCustBankAccount());
        company.setBankAccountName(anAsset.getCustBankAccountName());
        company.setBankName(anAsset.getCustBankName());
        company.setCustName(anAsset.getCustName());
        company.setCustNo(anAsset.getCustNo());
        this.insert(company);
        
    }
    
}
