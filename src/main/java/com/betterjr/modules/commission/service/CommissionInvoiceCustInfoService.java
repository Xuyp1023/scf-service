package com.betterjr.modules.commission.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.betterjr.mapper.pagehelper.Page;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionInvoiceCustInfoMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionInvoiceCustInfo;
import com.betterjr.modules.commission.entity.CommissionParam;
import com.betterjr.modules.customer.ICustMechBaseService;

@Service
public class CommissionInvoiceCustInfoService extends BaseService<CommissionInvoiceCustInfoMapper, CommissionInvoiceCustInfo> {

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    @Autowired
    private CustAccountService custAccountService;
    
    /**
     * 新增发票抬头信息
     * @param anCustInfo
     * @return
     */
    public CommissionInvoiceCustInfo saveAddInvoiceCustInfo(CommissionInvoiceCustInfo anCustInfo){
        
        anCustInfo.initAddValue(UserUtils.getOperatorInfo());
        vailedAddCustInfo(anCustInfo);
        anCustInfo.setCustName(custAccountService.queryCustName(anCustInfo.getCustNo()));
        if(!anCustInfo.getCoreCustNo().equals(anCustInfo.getCustNo())){
            
            anCustInfo.setCoreCustName(custAccountService.queryCustName(anCustInfo.getCoreCustNo()));
        }
        logger.info("Begin to add saveAddInvoiceCustInfo 数据为："+anCustInfo+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        this.insertSelective(anCustInfo);
        return anCustInfo;
    }
    
    /**
     * 查询企业发票生效的发票抬头
     * 
     * 默认并且生效的发票抬头信息
     * @param anCustNo
     * @param anCoreCustNo
     * @return
     */
    public CommissionInvoiceCustInfo findInvoiceCustInfoEffectiveByCustNo(Long anCustNo, Long anCoreCustNo){
        
        Map map = QueryTermBuilder.newInstance()
                .put("custNo", anCustNo)
                .put("coreCustNo", anCoreCustNo)
                .put("isLatest", CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_IS_LAEST_OK)
                .put("businStatus", CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK)
                .build();
        
        List<CommissionInvoiceCustInfo> list = this.selectByProperty(map);
        //BTAssert.notNull(list, "你没有默认生效的发票抬头信息，请新建");
        //if(Collections3.isEmpty(list)) BTAssert.notNull(null, "你没有默认生效的发票抬头信息，请新建");
       
        return Collections3.getFirst(list);
        
    }
    
    
    /**
     * 发票抬头信息查询
     * 查询所有生效的发票抬头信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionInvoiceCustInfo> queryInvoiceCustInfoList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap, "查询发票抬头失败！条件为空");  
        BTAssert.notNull(anMap.get("custNo"), "查询发票抬头失败！条件为空");  
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap.put("NEcoreCustNo", anMap.get("custNo"));
        anMap.put("businStatus", CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK);
        Page<CommissionInvoiceCustInfo> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        
        return page;
        
    }
    
    /**
     * 发票抬头信息修改
     * 需要id和修改的数据
     * @param anInvoiceInfo
     * @return
     */
    public CommissionInvoiceCustInfo saveUpdateInvoiceCustInfo(CommissionInvoiceCustInfo anInvoiceInfo){
        
        BTAssert.notNull(anInvoiceInfo, "修改发票抬头失败！条件为空");  
        BTAssert.notNull(anInvoiceInfo.getId(), "修改发票抬头失败！条件为空");  
        logger.info("Begin to saveUpdateInvoiceCustInfo 数据为："+anInvoiceInfo+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        vailedAddCustInfo(anInvoiceInfo);
        CommissionInvoiceCustInfo custInfo = this.selectByPrimaryKey(anInvoiceInfo.getId());
        BTAssert.notNull(custInfo, "修改发票抬头失败！没有查询到修改的记录");  
        if(custInfo.getBusinStatus().equals(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER)){
            BTAssert.notNull(null, "修改发票抬头失败！当前抬头已经失效"); 
        }
        if(!getCurrentUserCustNos().contains(custInfo.getCustNo())){
            
            BTAssert.notNull(null, "修改发票抬头失败！你没有当前参数的操作权限"); 
        }
        
        custInfo.setBusinStatus(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER);
        custInfo.setIsLatest(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_IS_LAEST_FAILER);
        this.updateByPrimaryKeySelective(custInfo);
        anInvoiceInfo.initAddValue(UserUtils.getOperatorInfo());
        anInvoiceInfo.setCustName(custAccountService.queryCustName(anInvoiceInfo.getCustNo()));
        if(!anInvoiceInfo.getCoreCustNo().equals(anInvoiceInfo.getCustNo())){
            
            anInvoiceInfo.setCoreCustName(custAccountService.queryCustName(anInvoiceInfo.getCoreCustNo()));
        }
        this.insert(anInvoiceInfo);
        
        logger.info("end to saveUpdateInvoiceCustInfo 数据为："+anInvoiceInfo+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return anInvoiceInfo;
        
    }
    
    /**
     * 获取当前登录用户所在的所有公司id集合
     * @return
     */
    private Collection<Long> getCurrentUserCustNos(){
        
        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询发票抬头失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询发票抬头失败!获取当前企业失败");
        Collection<Long> custNos=new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return  custNos;
    }
    
    
    /**
     * 初始化插入抬头信息校验数据是否符合要求
     * 必须包括
     * custNo
     * coreCustNo
     * coreInfoType   发票类型
     * isLatest   是否默认
     * 
     * 如果是企业类型  需要校验
     * 地址,银行等信息
     * 如果是默认  需要判断数据库中不存在默认的信息才允许插入
     * 
     * @param anCustInfo
     */
    private void vailedAddCustInfo(CommissionInvoiceCustInfo anCustInfo){
        
        BTAssert.notNull(anCustInfo, "新增发票抬头失败！数据为空");
        BTAssert.notNull(anCustInfo.getCustNo(), "新增发票抬头失败！数据为空");
        BTAssert.notNull(anCustInfo.getCoreCustNo(), "新增发票抬头失败！数据为空");
        BTAssert.notNull(anCustInfo.getId(), "新增发票抬头失败！数据为空");
        
        if(StringUtils.isBlank(anCustInfo.getCoreInfoType())){
            BTAssert.notNull(null, "新增发票抬头失败！请选择发票抬头类型");
        }
        if(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_CUSTTYPE_ENTERPRISE.equals(anCustInfo.getCoreInfoType())){
            
            BTAssert.notNull(anCustInfo.getCoreAddress(), "新增发票抬头失败！请输入发票抬头地址");
            BTAssert.notNull(anCustInfo.getCoreBank(), "新增发票抬头失败！请输入发票抬头开户银行");
            BTAssert.notNull(anCustInfo.getCoreBankAccount(), "新增发票抬头失败！请输入发票抬头银行账户");
            BTAssert.notNull(anCustInfo.getCorePhone(), "新增发票抬头失败！请输入发票抬头电话");
            BTAssert.notNull(anCustInfo.getCoreTaxPayerNo(), "新增发票抬头失败！请输入发票抬头纳税人人识别号");
        }
        
        if(StringUtils.isBlank(anCustInfo.getIsLatest())){
            BTAssert.notNull(null, "新增发票抬头失败！请选择发票抬头是否默认");
        }
        if(CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_IS_LAEST_OK.equals(anCustInfo.getIsLatest())){
            
            
            Map map = QueryTermBuilder.newInstance()
            .put("custNo", anCustInfo.getCustNo())
            .put("coreCustNo", anCustInfo.getCoreCustNo())
            .put("isLatest", CommissionConstantCollentions.COMMISSION_INVOICE_CUSTINFO_IS_LAEST_OK)
            .put("businStatus", CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK)
            .put("NEid", anCustInfo.getId())
            .build();
            
            List<CommissionInvoiceCustInfo> list = this.selectByProperty(map);
            if(!Collections3.isEmpty(list)){
                BTAssert.notNull(null, "新增发票抬头失败！只能有一个默认的发票抬头");
            }
        }
        
        
    }
    
    
    
    public CommissionInvoiceCustInfo saveDeleteCustInfo(Long  anCustInfoId){
        
        BTAssert.notNull(anCustInfoId, "删除佣金发票抬头失败！数据为空"); 
      
        CommissionInvoiceCustInfo custInfo = this.selectByPrimaryKey(anCustInfoId);
        BTAssert.notNull(custInfo, "删除佣金发票抬头失败！未查询到参数配置信息"); 
        logger.info("Begin to add saveDeleteCustInfo 数据为："+custInfo+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        
        if(!getCurrentUserCustNos().contains(custInfo.getCustNo())){
          
            BTAssert.notNull(null, "删除佣金参数失败！你没有当前参数的操作权限"); 
        }
        custInfo.setBusinStatus(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER);
        this.updateByPrimaryKeySelective(custInfo);
        
        logger.info("end to add saveDeleteCustInfo 数据为："+custInfo+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return custInfo;
        
    }
}
