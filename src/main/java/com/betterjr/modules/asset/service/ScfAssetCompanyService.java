package com.betterjr.modules.asset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.dao.ScfAssetCompanyMapper;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.asset.entity.ScfAsset;
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
    public ScfAsset selectByAssetId(ScfAsset anAsset, Long anCustNo, boolean anOnOff) {
        
        BTAssert.notNull(anAsset, "查询资产 失败-anAsset is null");
        BTAssert.notNull(anAsset.getId(), "查询资产 失败-anAsset.getId is null");
        logger.info("Begin to add selectByAssetId"+anAsset.getId() +"  企业id:"+anCustNo);
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("assetId", anAsset.getId());
        Page<ScfAssetCompany> assetCompanyList = this.selectPropertyByPage(paramMap, 1, 10, false);
        boolean flag=false;//为true设置到资产，，false不设置到资产
        List<CustInfo> factoryCust=new ArrayList<CustInfo>();
        for (ScfAssetCompany scfAssetCompany : assetCompanyList) {
            
            if(scfAssetCompany.getCustNo().equals(anCustNo)){
                anAsset.setOperationAuth(scfAssetCompany.getOperatorStatus());
                flag=true;
            }
            CustInfo custInfo =new CustInfo();
            if (anOnOff) {
                custInfo = custAccountService.selectByPrimaryKey(scfAssetCompany.getCustNo());
                logger.info("find custinfo success"+scfAssetCompany.getCustNo());
                if(custInfo==null){
                    continue;
                }
                
            }else{
                custInfo.setCustNo(scfAssetCompany.getCustNo());
                custInfo.setCustName(scfAssetCompany.getCustName());
            }
            if(!AssetConstantCollentions.SCF_ASSET_ROLE_FACTORY.equals(scfAssetCompany.getAssetRole())){
                //是供应商，经销商，核心企业
                if(AssetConstantCollentions.SCF_ASSET_ROLE_SUPPLY.equals(scfAssetCompany.getAssetRole())|| AssetConstantCollentions.SCF_ASSET_ROLE_DEALER.equals(scfAssetCompany.getAssetRole())){
                    anAsset.getCustMap().put(AssetConstantCollentions.CUST_INFO_KEY, custInfo);  
                }
                if(AssetConstantCollentions.SCF_ASSET_ROLE_CORE.equals(scfAssetCompany.getAssetRole())){
                    anAsset.getCustMap().put(AssetConstantCollentions.CORE_CUST_INFO_KEY, custInfo);  
                }
            }else{
                //保理公司
                factoryCust.add(custInfo);
            }
        }
        anAsset.getCustMap().put(AssetConstantCollentions.FACTORY_CUST_INFO_KEY, factoryCust);
        
        if (flag) {
            logger.info("success to  selectByAssetId"+anAsset.getId());
            return anAsset;  
        }else{
            logger.info("faile to  selectByAssetId"+anAsset.getId()+" 企业没有权限" +anCustNo);
            anAsset.setCustMap(new HashMap<String,Object>());
            return anAsset;
        }
        
    }
    
    
}
