package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionInvoiceMapper;
import com.betterjr.modules.commission.entity.CommissionInvoice;
import com.betterjr.modules.commission.entity.CommissionInvoiceCustInfo;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;

@Service
public class CommissionInvoiceService extends BaseService<CommissionInvoiceMapper, CommissionInvoice>{

    
    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    @Autowired
    private CommissionInvoiceRecordService recordService;
    
    @Autowired
    private CommissionInvoiceCustInfoService custInfoService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;
    
    
    /**
     * 发票新增索取
     * 
     * @param anCustNo  平台Id
     * @param anCoreCustNo   核心企业Id
     * @param anMonthlyIds    月对账单Id集合
     * @param anInvoiceType    发票类型   0 普通发票  1 专用发票
     * @return
     */
    public CommissionInvoice saveDemandInvoice(Long anCustNo,Long anCoreCustNo,String anMonthlyIds,String anInvoiceType){
        
        BTAssert.notNull(anCustNo, "索取发票失败！数据不全");
        BTAssert.notNull(anCoreCustNo, "索取发票失败！请选择对账企业");
        BTAssert.notNull(anMonthlyIds, "索取发票失败！请选择月账单");
        BTAssert.notNull(anInvoiceType, "索取发票失败！请选择发票类型");
        CommissionInvoice invoice=new CommissionInvoice();
        invoice.initAddValue(UserUtils.getOperatorInfo());
        logger.info("Begin to add saveDemandInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        //封装发票抬头
        fillInvoiceByCustNoCoreCustNo(anCustNo, anCoreCustNo, invoice);
        invoice.setInvoiceType(anInvoiceType);
        //将月账单中的信息封装到发票中去
        recordService.fillDataToInvoice(invoice,anMonthlyIds);
        //封装税额和金额等信息
        fillInvoiceRaxInfo(invoice);
        
        this.insert(invoice);
        
        logger.info("end to add saveDemandInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return invoice;
        
        
    }
    
    /**
     * 索取发票之后提交发票信息   是发票正式生效
     * @param anInvoiceId
     * @param anInvoiceContent
     * @param anDescription
     * @return
     */
    public CommissionInvoice saveInvoiceEffective(Long anInvoiceId,String anInvoiceContent,String anDescription){
        
        BTAssert.notNull(anInvoiceId, "提交发票失败！数据不全");
        BTAssert.notNull(anInvoiceContent, "提交发票失败！发票内容为空");
        
        CommissionInvoice invoice = this.selectByPrimaryKey(anInvoiceId);
        BTAssert.notNull(invoice, "提交发票失败！数据不全");
        if(!invoice.getBusinStatus().equals("0")){
            BTAssert.notNull(null, "提交发票失败！当前票据已经失效");
        }
        if(!UserUtils.getOperOrg().equals(invoice.getOperOrg())){
            BTAssert.notNull(null, "提交发票失败！你没有相应的权限"); 
        }
        logger.info("begin to add saveInvoiceEffective 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        //更新月账单状态
        recordService.saveUpdateRecord(anInvoiceId,invoice.getInvoiceType());
        
        invoice.setInvoiceContent(anInvoiceContent);
        invoice.setDescription(anDescription);
        invoice.setBusinStatus("1");
        this.updateByPrimaryKeySelective(invoice);
        
        logger.info("end to add saveInvoiceEffective 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return invoice;
    }
    
    /**
     * 将未确认的票据作废
     * @param anInvoiceId
     * @return
     */
    public CommissionInvoice saveAnnulInvoice(Long anInvoiceId){
        
        BTAssert.notNull(anInvoiceId, "提交发票失败！数据不全");
        CommissionInvoice invoice = this.selectByPrimaryKey(anInvoiceId);
        BTAssert.notNull(invoice, "提交发票失败！发票不存在");
        if(!invoice.getBusinStatus().equals("1")){
            BTAssert.notNull(null, "提交发票失败！当前票据已经失效");
        }
        if(!UserUtils.getOperOrg().equals(invoice.getOperOrg())){
            BTAssert.notNull(null, "提交发票失败！你没有相应的权限"); 
        }
        logger.info("begin to saveAnnulInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        //将月账单释放
        recordService.saveAnnulRecord(anInvoiceId);
        
        invoice.setBusinStatus("4");
        this.updateByPrimaryKeySelective(invoice);
        
        logger.info("end to saveAnnulInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return invoice;
        
    }
    
    /**
     * 带开票的发票变成开票中的发票
     * @param anInvoiceId
     * @return
     */
    public CommissionInvoice saveConfirmInvoice(Long anInvoiceId){
        
        BTAssert.notNull(anInvoiceId, "提交发票失败！数据不全");
        CommissionInvoice invoice = this.selectByPrimaryKey(anInvoiceId);
        BTAssert.notNull(invoice, "提交发票失败！发票不存在");
        if(!invoice.getBusinStatus().equals("1")){
            BTAssert.notNull(null, "提交发票失败！当前票据已经失效");
        }
        if(!UserUtils.getOperOrg().equals(invoice.getOperOrg())){
            BTAssert.notNull(null, "提交发票失败！你没有相应的权限"); 
        }
        
        logger.info("begin to saveConfirmInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        invoice.initConfirmValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(invoice);
        
        logger.info("end to saveConfirmInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        return invoice;
    }
    
    /**
     * 发票维护详细信息
     * id
     * invoiceNo
     * invoiceCode
     * invoiceDate
     * description
     * drawer  出票人
     * @param anInvoice
     * @param anFileList
     * @return
     */
    public CommissionInvoice saveAuditInvoice(CommissionInvoice anInvoice,String anFileList){
        
        BTAssert.notNull(anInvoice, "提交发票失败！发票内容为空"); 
        BTAssert.notNull(anInvoice.getId(), "提交发票失败！发票内容为空");
        CommissionInvoice invoice = this.selectByPrimaryKey(anInvoice.getId());
        BTAssert.notNull(invoice, "提交发票失败！未查询到发票信息");
        logger.info("begin to saveAuditInvoice 数据为："+anInvoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
        if(StringUtils.isBlank(anFileList)){
            BTAssert.notNull(null, "请上传发票附件！！");  
            
        }
        if(!invoice.getBusinStatus().equals("2")){
            BTAssert.notNull(null, "提交发票失败！当前票据已经失效");
        }
        if(!UserUtils.getOperOrg().equals(invoice.getOperOrg())){
            BTAssert.notNull(null, "提交发票失败！你没有相应的权限"); 
        }
        invoice.initAuditValue(UserUtils.getOperatorInfo(),anInvoice);
        // 保存附件信息
        invoice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, invoice.getBatchNo()));
       this.updateByPrimaryKeySelective(invoice);
       
       logger.info("end to saveAuditInvoice 数据为："+invoice+" 操作用户为："+UserUtils.getOperatorInfo().getName());
       return invoice;
    }
    
    
    /**
     * 查询发票信息
     * businStatus  状态
     * GTEregDate   申请日期
     * LTEregDate   申请日期
     * custNo    平台id
     * coreCustNo   发票抬头企业
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @param anIsConfirm   是否是用来确认页面的查询  true  是确认页面的查询       false  发票申请页面的查询
     * @return
     */
    public Page<CommissionInvoice> queryCommissionInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean anIsConfirm){
        
        BTAssert.notNull(anMap, "查询发票失败！条件为空");  
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        
        if(!anMap.containsKey("businStatus") || StringUtils.isBlank(anMap.get("businStatus").toString())){
            
            if(anIsConfirm){
                //确认查询，不查询已经作废的发票
                anMap.put("businStatus", new String[]{"1","2","3"});
            }else{
                anMap.put("businStatus", new String[]{"1","2","3","4"});
                
            }
            
        }
        
        anMap.put("operOrg", UserUtils.getOperOrg());
        Page<CommissionInvoice> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id Desc");
        
        return  page;
    }
    
    /**
     * 查询发票详情信息
     * @param anInvoiceId
     * @return
     */
    public CommissionInvoice findCommissionInvoiceById(Long anInvoiceId){
        
        BTAssert.notNull(anInvoiceId, "查询发票失败！条件为空");  
        CommissionInvoice invoice = this.selectByPrimaryKey(anInvoiceId);
        BTAssert.notNull(invoice, "查询发票失败！未查询到相关发票信息");  
        fillInvoiceCustInfo(invoice);
        return invoice;
        
    }
    
    /**
     * 封装税额和金额等信息
     * @param anInvoice
     */
    private void fillInvoiceRaxInfo(CommissionInvoice anInvoice) {
       
        BigDecimal balance = anInvoice.getBalance();
        BigDecimal taxRate = anInvoice.getTaxRate();
        BigDecimal rate = MathExtend.add(MathExtend.divide(taxRate,new BigDecimal(100)), new BigDecimal(1));
        BigDecimal interestBalance = MathExtend.divide(balance,rate);
        anInvoice.setInterestBalance(interestBalance);
        anInvoice.setTaxBalance(MathExtend.subtract(balance,interestBalance));
    }


    /**
     * 通过平台id和核心企业id封装发票的抬头 和企业详细等信息
     * @param anCustNo
     * @param anCoreCustNo
     * @param invoice
     */
    private void fillInvoiceByCustNoCoreCustNo(Long anCustNo, Long anCoreCustNo, CommissionInvoice invoice) {
        invoice.setCustNo(anCustNo);
        invoice.setCoreCustNo(anCoreCustNo);
        //封装发票抬头
        fillInvoiceCustInfo(invoice);
        
        invoice.setCustName(custAccountService.queryCustName(invoice.getCustNo()));
        invoice.setCoreCustName(custAccountService.queryCustName(invoice.getCoreCustNo()));
        invoice.setOperOrg(baseService.findBaseInfo(invoice.getCustNo()).getOperOrg());
    }

    /**
     * 封装发票抬头信息
     * @param invoice
     */
    private void fillInvoiceCustInfo(CommissionInvoice invoice) {
        
        BTAssert.notNull(invoice, "查询发票失败！条件为空");  
        BTAssert.notNull(invoice.getCustNo(), "查询发票失败！条件为空");  
        BTAssert.notNull(invoice.getCoreCustNo(), "查询发票失败！条件为空");  
        //核心企业的发票抬头
        CommissionInvoiceCustInfo coreCustInfo = new CommissionInvoiceCustInfo();
        if(invoice.getCoreCustInvoiceId()==null){
            
            coreCustInfo = custInfoService.findInvoiceCustInfoEffectiveByCustNo(invoice.getCustNo(), invoice.getCoreCustNo());
            BTAssert.notNull(coreCustInfo, "当前委托企业没有默认生效的发票抬头信息，请新建");
        }else{
            coreCustInfo = custInfoService.selectByPrimaryKey(invoice.getCoreCustInvoiceId());
            BTAssert.notNull(coreCustInfo, "票据抬头缺失,请重新开票");
        }
        invoice.setCoreCustInvoiceInfo(coreCustInfo);
        invoice.setCoreCustInvoiceId(coreCustInfo.getId());
        //平台的发票抬头
        CommissionInvoiceCustInfo custInfo =  new CommissionInvoiceCustInfo();
        if(invoice.getCustInvoiceId() == null){
            
            custInfo  = custInfoService.findInvoiceCustInfoEffectiveByCustNo(invoice.getCustNo(), invoice.getCustNo());
            BTAssert.notNull(custInfo, "当前开票方没有默认生效的发票抬头信息，请新建");
        }else{
            custInfo =  custInfoService.selectByPrimaryKey(invoice.getCustInvoiceId());
            BTAssert.notNull(custInfo, "票据抬头缺失,请重新开票");
        }
        invoice.setCustInvoiceInfo(custInfo);
        invoice.setCustInvoiceId(custInfo.getId());
    }
    
    
    
}
