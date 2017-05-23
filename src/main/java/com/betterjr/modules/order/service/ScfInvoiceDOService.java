package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustMechBusinLicenceService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.dao.ScfInvoiceDOMapper;
import com.betterjr.modules.order.entity.ScfInvoiceDO;
import com.betterjr.modules.order.entity.ScfInvoiceDOItem;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfInvoiceDOService extends BaseVersionService<ScfInvoiceDOMapper, ScfInvoiceDO>{

    
    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    @Reference(interfaceClass = ICustMechBusinLicenceService.class)
    private ICustMechBusinLicenceService custMechBusinLicenceService;//获取公司的开户行信息
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    @Autowired
    private ScfInvoiceDOItemService invoiceItemService;
    
    /**
     * 新增发票信息
     * @param anInvoice
     * @param anFileList
     * @param anConfirmFlag true  新增并确认发票状态      false  新增发票信息
     * @return
     */
    public ScfInvoiceDO addInvoice(ScfInvoiceDO anInvoice, String anFileList,boolean anConfirmFlag,List<ScfInvoiceDOItem> anInvoiceItemList) {
        
        BTAssert.notNull(anInvoice,"插入发票为空,操作失败");
        BTAssert.notNull(anInvoice.getInvoiceNo(),"插入发票号码为空,操作失败");
        //通过发票号查找当前发票号是否已经被使用
        List<ScfInvoiceDO> checkInvoiceLists = checkInvoiceNoIsExist(anInvoice.getInvoiceNo());
        BTAssert.notNull(checkInvoiceLists,"当前发票已经登记,操作失败"); 
        if(!Collections3.isEmpty(checkInvoiceLists)){
            BTAssert.notNull(null,"当前发票已经登记,操作失败"); 
        }
        logger.info("Begin to add order"+UserUtils.getOperatorInfo().getName());
        anInvoice.initAddValue(UserUtils.getOperatorInfo(),anConfirmFlag);
        // 操作机构设置为供应商
        anInvoice.setOperOrg(baseService.findBaseInfo(anInvoice.getCustNo()).getOperOrg());
        anInvoice.setCustName(custAccountService.queryCustName(anInvoice.getCustNo()));
        anInvoice.setCoreCustName(custAccountService.queryCustName(anInvoice.getCoreCustNo()));
        //设置纳税人识别号
        anInvoice.setCustTaxpayerNo(custMechBusinLicenceService.findBusinLicenceTaxNo(anInvoice.getCustNo()));
        anInvoice.setCoreCustTaxpayerNo(custMechBusinLicenceService.findBusinLicenceTaxNo(anInvoice.getCoreCustNo()));
        // 保存附件信息
        anInvoice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anInvoice.getBatchNo()));
        //保存发票明细项
        if(anInvoiceItemList !=null && !anInvoiceItemList.isEmpty()){
            invoiceItemService.saveAddInvoiceItem(anInvoiceItemList, anInvoice.getId());
        }
        anInvoice=this.insertVersion(anInvoice);
        logger.info("success to add order"+UserUtils.getOperatorInfo().getName());
        anInvoice.setInvoiceItemList(anInvoiceItemList);
        return anInvoice;
    }
    
    /**
     * 校验当前发票是否存在，如果已经存在不允许再次插入
     * @param anInvoiceNo
     */
   private List<ScfInvoiceDO> checkInvoiceNoIsExist(String anInvoiceNo) {
        
       List<String> businStatusList=new ArrayList<>();
       businStatusList.add(VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE);
       businStatusList.add(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
       businStatusList.add(VersionConstantCollentions.BUSIN_STATUS_USED);
       List<String> docStatusList=new ArrayList<>();
       docStatusList.add(VersionConstantCollentions.DOC_STATUS_DRAFT);
       docStatusList.add(VersionConstantCollentions.DOC_STATUS_CONFIRM);
       Map<String,Object> map=QueryTermBuilder.newInstance()
               .put("invoiceNo", anInvoiceNo)
               .put("businStatus", businStatusList)
               .put("docStatus", docStatusList)
               .put("isLatest", VersionConstantCollentions.IS_LATEST)
               .build();
       List<ScfInvoiceDO> invoiceList = this.selectByClassProperty(ScfInvoiceDO.class, map);
       
       return invoiceList;
    }

/**
    * 发票信息编辑
    * @param anModiInvoice
    * @param anFileList  文件上传批次号
    * @param anConfirmFlag  true: 确认并修改     false: 修改
 * @param anInvoiceItemList 
    * @return
    */
    public ScfInvoiceDO saveModifyInvoice(ScfInvoiceDO anModiInvoice,String anFileList,boolean anConfirmFlag, List<ScfInvoiceDOItem> anInvoiceItemList) {
        
        BTAssert.notNull(anModiInvoice,"发票为空,操作失败");
        BTAssert.notNull(anModiInvoice.getRefNo(),"凭证编号为空,操作失败");
        BTAssert.notNull(anModiInvoice.getVersion(),"发票信息不全,操作失败");
        logger.info("Begin to modify invoice"+UserUtils.getOperatorInfo().getId());
        ScfInvoiceDO invoice = this.selectOneWithVersion(anModiInvoice.getRefNo(),anModiInvoice.getVersion());
        BTAssert.notNull(invoice, "无法获取发票信息");
        //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
        checkOperatorModifyStatus(UserUtils.getOperatorInfo(),invoice);
        List<ScfInvoiceDO> checkInvoiceLists = checkInvoiceNoIsExist(invoice.getInvoiceNo());
        BTAssert.notNull(checkInvoiceLists,"当前发票已经删除,操作失败"); 
        checkInvoiceLists.remove(invoice);
        if(!Collections3.isEmpty(checkInvoiceLists)){
            BTAssert.notNull(null,"当前发票已经登记,操作失败"); 
        }
        // 应收账款信息变更迁移初始化
        anModiInvoice.initModifyValue(invoice);
        if(! invoice.getCustNo().equals(anModiInvoice.getCustNo())){
            anModiInvoice.setCustName(custAccountService.queryCustName(anModiInvoice.getCustNo()));
            // 操作机构设置为供应商
            anModiInvoice.setOperOrg(baseService.findBaseInfo(anModiInvoice.getCustNo()).getOperOrg());
            anModiInvoice.setCustTaxpayerNo(custMechBusinLicenceService.findBusinLicenceTaxNo(anModiInvoice.getCustNo()));
        }
        if(!  invoice.getCoreCustNo().equals(anModiInvoice.getCoreCustNo())){
            anModiInvoice.setCoreCustName(custAccountService.queryCustName(anModiInvoice.getCoreCustNo()));
            anModiInvoice.setCoreCustTaxpayerNo(custMechBusinLicenceService.findBusinLicenceTaxNo(anModiInvoice.getCoreCustNo()));
        }
        // 保存附件信息
        if(StringUtils.isNotBlank(anFileList)){
            anModiInvoice.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anModiInvoice.getBatchNo()));
        }else{
            anModiInvoice.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo("", anModiInvoice.getBatchNo())); 
        }
        // 需要升级版本的修改
        anModiInvoice.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
        anModiInvoice.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_LOCKED);
        anModiInvoice.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        //this.updateByPrimaryKeySelective(anOrder);
        if(anConfirmFlag){
            anModiInvoice.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
        }
        //ScfOrderDO order2 = this.selectOneWithVersion(order.getRefNo(),order.getVersion());
        anModiInvoice=this.updateVersionByPrimaryKeySelective(anModiInvoice,invoice.getRefNo(),invoice.getVersion());
        BTAssert.notNull(anModiInvoice, "修改发票失败");
      //保存发票明细项
        if(anInvoiceItemList !=null && !anInvoiceItemList.isEmpty()){
            invoiceItemService.saveAddInvoiceItem(anInvoiceItemList, anModiInvoice.getId()); 
            anModiInvoice.setInvoiceItemList(anInvoiceItemList);
        }else{
            anModiInvoice.setInvoiceItemList(new ArrayList<ScfInvoiceDOItem>());
        }
        logger.info("success to modify invoice"+UserUtils.getOperatorInfo().getName());
        return anModiInvoice;
    }
    
    /**
     * 作废发票
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfInvoiceDO saveAnnulInvoice(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "发票凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfInvoiceDO invoice = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(invoice, "此发票异常!操作失败");
        invoice=this.annulOperator(UserUtils.getOperatorInfo(), invoice);
        return invoice;
        
    }
    
    /**
     * 查询发票详情
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfInvoiceDO findInvoice(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfInvoiceDO invoice = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(invoice, "此订单异常!操作失败");
        invoice.setInvoiceItemList(invoiceItemService.queryInvoiceItemsByInvoiceId(invoice.getId()));
        return invoice;
    }
    
    /**
     * 发票核准
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfInvoiceDO saveAuditInvoice(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfInvoiceDO invoice = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(invoice, "此订单异常!操作失败");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "获取当前企业失败!操作失败");
        if(! getCustNoList(custInfos).contains(invoice.getCoreCustNo())){
            BTAssert.notNull(invoice, "您没有审核权限!操作失败"); 
        }
        this.auditOperator(UserUtils.getOperatorInfo(), invoice);
        return invoice;
        
    }
    
    /**
     * 将给定的企业集合中提取企业的id
     * @param custInfos
     * @return
     */
    private List<Long> getCustNoList(Collection<CustInfo> custInfos) {
        
        List<Long> custNos=new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getCustNo());
       }
        
        return custNos;
    }
    
    /**
     * 查询未生效的发票信息
     * @param anMap
     * @param anFlag 是否需要查询总的数量
     * @param anPageNum  当前页数
     * @param anPageSize 每页显示的数量
     * @param anIsAudit true  是审核界面的数据来源     false   不是审核界面的数据来源
     * @return
     */
    public Page<ScfInvoiceDO> queryIneffectiveInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean anIsAudit) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        
        if(anIsAudit){
            
            if (! anMap.containsKey("custNo") ||  anMap.get("custNo") ==null || StringUtils.isBlank(anMap.get("custNo").toString())) {
                
                Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
                anMap.put("custNo", getCustNoList(custInfos));
            }
            anMap.put("docStatus", VersionConstantCollentions.DOC_STATUS_CONFIRM);
            
        }else{
            anMap.put("regOperId", UserUtils.getOperatorInfo().getId());
        }
        
        Page<ScfInvoiceDO> invoiceList = this.selectPropertyIneffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        
        return invoiceList;
    }
    
    
    /**
     * 查询已经生效的发票信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @param anIsCust true  供应商来查询   false 不是供应商来查询
     * @return
     */
    public Page<ScfInvoiceDO> queryEffectiveInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean anIsCust) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        
        if(anIsCust){
            
            if (! anMap.containsKey("custNo") ||  anMap.get("custNo") ==null || StringUtils.isBlank(anMap.get("custNo").toString())) {
                anMap.put("custNo", getCustNoList(custInfos));
            }
            anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
            
        }else{
            
            if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                anMap.put("coreCustNo", getCustNoList(custInfos));
            }
            //anMap.put("coreCustNo", getCustNoList(custInfos));
        }
        
        Page<ScfInvoiceDO> invoiceList = this.selectPropertyEffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        
        return invoiceList;
    }

    public Page queryRecycleInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        // 操作员只能查询本机构数据
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());

        if (! anMap.containsKey("custNo") ||  anMap.get("custNo") ==null || StringUtils.isBlank(anMap.get("custNo").toString())) {
            anMap.put("custNo", getCustNoList(custInfos));
        }
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        Page<ScfInvoiceDO> invoiceList = this.selectPropertyByPageWithStatus(anMap, anPageNum, anPageSize,"1".equals(anFlag), "id desc", VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE, VersionConstantCollentions.DOC_STATUS_CONFIRM, VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        
        Page<ScfInvoiceDO> invoiceList2 = this.selectPropertyByPageWithStatus(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc",VersionConstantCollentions.BUSIN_STATUS_USED,VersionConstantCollentions.DOC_STATUS_CONFIRM,VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        invoiceList.addAll(invoiceList2);
        return invoiceList;
    }
    
    /**
     * 已经生效的发票进行废止操作
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfInvoiceDO saveCustAnnulInvoice(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "数据版本为空!操作失败");
        logger.info("begin to saveCustAnnulInvoice invoice"+UserUtils.getOperatorInfo().getName());
        ScfInvoiceDO invoice = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(invoice, "此发票异常!操作失败");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        List<Long> custNoList = getCustNoList(custInfos);
        //bill.getCoreCustNo().
        if(! custNoList.contains(invoice.getCustNo())){
            BTAssert.notNull(null, "你没有相应的权限!操作失败");
        }
        invoice=this.annulEffectiveOperator(invoice);
        logger.info("success to saveCustAnnulInvoice invoice"+UserUtils.getOperatorInfo().getName());
        return invoice;
    }

    public List<ScfInvoiceDO> saveResolveFile(List<Map<String,Object>> listMap) {
        
        return null;
    }
}
