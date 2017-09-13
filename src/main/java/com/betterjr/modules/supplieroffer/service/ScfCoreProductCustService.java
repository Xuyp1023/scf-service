package com.betterjr.modules.supplieroffer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.productconfig.entity.ScfProductConfig;
import com.betterjr.modules.productconfig.sevice.ScfProductConfigService;
import com.betterjr.modules.supplieroffer.dao.ScfCoreProductCustMapper;
import com.betterjr.modules.supplieroffer.data.CoreProductCustConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfCoreProductCust;

@Service
public class ScfCoreProductCustService extends BaseService<ScfCoreProductCustMapper,ScfCoreProductCust>{

    
    @Autowired
    private ScfProductConfigService productConfigService;
    
    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService relationService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    @Autowired
    private CustAccountService custAccountService;
    
    /**
     * 核心企业查询可用分配给供应商的保理产品
     * @param anCoreCustNo
     * @return
     */
    public List<ScfCoreProductCust> queryProductConfigByCore(Long anCustNo,Long anCoreCustNo){
        BTAssert.notNull(anCustNo, "查询失败");
        BTAssert.notNull(anCoreCustNo, "查询失败");
        //查询当前核心企业所有的保理产品
        List<ScfProductConfig> productConfigList = productConfigService.queryProductKeyAndValue(anCoreCustNo);
        //查询当前核心企业给供应商分配的保理产品（已经生效的）
        Map build = QueryTermBuilder.newInstance()
        .put("coreCustNo", anCoreCustNo)
        .put("custNo", anCustNo)
        .put("businStatus", new String[]{CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_EFFECTIVE,
                CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_USED })
        .build();
        List<ScfCoreProductCust> productList = this.selectByProperty(build);
        synchronizedProductConfigAndProduct(productList,productConfigList,anCustNo,anCoreCustNo);
        return this.selectByProperty(build);
    }
    
    /**
     * 更新融资产品列表
     * @param anCustNo
     * @param anCoreCustNo
     * @param anProductCodes
     * @return
     */
    public List<ScfCoreProductCust> saveAddAndUpdateProduct(Long anCustNo,Long anCoreCustNo,String anProductCodes){
        
        BTAssert.notNull(anCoreCustNo, "修改失败");
        BTAssert.notNull(anCustNo, "修改失败");
        if(!getCurrentUserCustNos().contains(anCoreCustNo)){
            BTAssert.notNull(null, "你没有新增权限");
        }
        
        //将以前的保理产品设置为没有分配
        saveUpdateProduceToFailure(anCustNo, anCoreCustNo);
        List<ScfCoreProductCust> products=new ArrayList<>();
        //重新添加新的保理产品信息
        Map build = QueryTermBuilder.newInstance()
        .put("id", StringUtils.split(anProductCodes, ","))
        .build();
        List<ScfCoreProductCust> temList = this.selectByProperty(build);
        for (ScfCoreProductCust config : temList) {
            config.setBusinStatus(CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_USED);
            this.updateByPrimaryKeySelective(config);
            products.add(config);
        }
        
        return products;
    }
    
    /**
     * 查询核心企业分配给供应商的可用的产品列表
     * @param custNo
     * @param coreCustNo
     * @return
     */
    public List<ScfCoreProductCust> queryCanUseProduct(Long custNo,Long coreCustNo){
        
        Map build = QueryTermBuilder.newInstance()
                .put("coreCustNo", coreCustNo)
                .put("custNo", custNo)
                .put("businStatus", CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_USED)
                .build();
        
        return this.selectByProperty(build);
        
    }
    
    
    /**
     * 同步保理产品和供应商产品信息
     * @param anProductList
     * @param anProductConfigList
     */
    private void synchronizedProductConfigAndProduct(List<ScfCoreProductCust> anProductList, List<ScfProductConfig> anProductConfigList,Long anCustNo,Long anCoreCustNo) {
        
        //返回保理产品id
        List<Long> ids=getProductIds(anProductList);
        for (ScfProductConfig scfProductConfig : anProductConfigList) {
            if(!ids.contains(scfProductConfig.getId())){
                ScfCoreProductCust product=new ScfCoreProductCust();
                product.setCoreCustNo(anCoreCustNo);
                
                product.setCoreCustName(custAccountService.queryCustName(anCoreCustNo));
                product.setCustNo(anCustNo);
                product.setCustName(custAccountService.queryCustName(anCustNo));
                product.setFactoryNo(scfProductConfig.getFactorNo());
                product.setFactoryName(custAccountService.queryCustName(scfProductConfig.getFactorNo()));
                product.setProductCode(scfProductConfig.getProductCode());
                product.setProductId(scfProductConfig.getId());
                product.setProductName(scfProductConfig.getProductName());
                product.setReceivableRequestType(scfProductConfig.getReceivableRequestType());
                product.saveAddValue(UserUtils.getOperatorInfo());
                this.insertSelective(product);
                anProductList.add(product);
            }
        }
        
        //配置产品表和保理产品表同步
        
        //获取到保理产品表id集合
        List<Long> configIds=getProductConfigIds(anProductConfigList);
        for (ScfCoreProductCust product : anProductList) {
            
            if(!configIds.contains(product.getProductId())){
                
                product.setBusinStatus(CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_NOEFFECTIVE);
                this.updateByPrimaryKeySelective(product);
                //anProductList.remove(product);
                //anProductList.listIterator().remove();
                
            }
        }
        
    }

    private List<Long> getProductConfigIds(List<ScfProductConfig> anProductConfigList) {
        List<Long> ids=new ArrayList<>();
        for (ScfProductConfig config : anProductConfigList) {
            ids.add(config.getId());
        }
        
        
        return ids;
    }

    /**
     * 返回保理产品Id
     * @param anProductList
     * @return
     */
    private List<Long> getProductIds(List<ScfCoreProductCust> anProductList) {
       
        List<Long> ids=new ArrayList<>();
        for (ScfCoreProductCust product : anProductList) {
            ids.add(product.getProductId());
        }
        return ids;
    }
    
    /**
     * 获取当前登录用户所在的所有公司id集合
     * @return
     */
    private List<Long> getCurrentUserCustNos(){
        
        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        List<Long> custNos=new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return  custNos;
    }
    
    private void saveUpdateProduceToFailure(Long anCustNo,Long anCoreCustNo){
        
        BTAssert.notNull(anCoreCustNo, "修改失败");
        BTAssert.notNull(anCustNo, "修改失败");
        
        Map build = QueryTermBuilder.newInstance()
                .put("coreCustNo", anCoreCustNo)
                .put("custNo", anCustNo)
                .put("businStatus", CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_USED)
                .build();
        
        List<ScfCoreProductCust> property = this.selectByProperty(build);
        for (ScfCoreProductCust scfCoreProductCust : property) {
            scfCoreProductCust.setBusinStatus(CoreProductCustConstantCollentions.PRODUCT_BUSIN_STATUS_EFFECTIVE);
            this.updateByPrimaryKeySelective(scfCoreProductCust);
        }
        
    }
}
