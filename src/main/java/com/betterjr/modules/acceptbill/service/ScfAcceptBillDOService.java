package com.betterjr.modules.acceptbill.service;

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
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillDOMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfAcceptBillDOService extends BaseVersionService<ScfAcceptBillDOMapper, ScfAcceptBillDO> {


    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    
    /**
     * 新增汇票信息
     */
    public ScfAcceptBillDO addAcceptBill(ScfAcceptBillDO anAcceptBill,String anFileList,boolean confirmFlag) {
        
        BTAssert.notNull(anAcceptBill,"票据登记失败，原因：保存数据异常");
        BTAssert.notNull(anAcceptBill.getCoreCustNo(),"票据登记失败，原因：请选择开票单位");
        BTAssert.notNull(anAcceptBill.getSupplierNo(),"票据登记失败，原因：请选择收款人");
        logger.info("Begin to add addAcceptBill"+UserUtils.getOperatorInfo().getName());
        anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
        anAcceptBill.setSupplierName(custAccountService.queryCustName(anAcceptBill.getSupplierNo()));
        anAcceptBill.setInvoiceCorp(anAcceptBill.getCoreCustName());
        anAcceptBill.initAddValue(UserUtils.getOperatorInfo(),confirmFlag);
        //操作机构设置为供应商
        anAcceptBill.setOperOrg(baseService.findBaseInfo(anAcceptBill.getSupplierNo()).getOperOrg());
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        this.insertVersion(anAcceptBill);
        logger.info("success to add addAcceptBill"+UserUtils.getOperatorInfo().getName());
        return anAcceptBill;
    }
    
    /**
     * 
     * @param anModiBill  修改的票据对象详情
     * @param anFileList  文件上次批次好
     * @param anConfirmFlag 是否修改加确认 true 标识确认    false 标识只是修改
     * @return
     */
    public ScfAcceptBillDO saveModifyAcceptBill(ScfAcceptBillDO anModiBill,String anFileList,boolean anConfirmFlag) {
        
        BTAssert.notNull(anModiBill,"票据修改失败,原因：对象为空");
        BTAssert.notNull(anModiBill.getRefNo(),"票据修改失败,原因：凭证编号为空");
        BTAssert.notNull(anModiBill.getVersion(),"票据修改失败,原因：票据信息不全");
        logger.info("Begin to modify bill");
        ScfAcceptBillDO bill = this.selectOneWithVersion(anModiBill.getRefNo(),anModiBill.getVersion());
        BTAssert.notNull(bill, "票据修改失败,原因：无法获取票据信息");
        //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
        checkOperatorModifyStatus(UserUtils.getOperatorInfo(),bill);
        // 应收账款信息变更迁移初始化
        //order.setCustName(custAccountService.queryCustName(anModiBill.getCustNo()));
        anModiBill.setCoreCustName(custAccountService.queryCustName(anModiBill.getCoreCustNo()));
        anModiBill.setInvoiceCorp(anModiBill.getCoreCustName());
        anModiBill.initModifyValue(bill,anConfirmFlag);
        //操作机构设置为供应商
        anModiBill.setOperOrg(baseService.findBaseInfo(anModiBill.getSupplierNo()).getOperOrg());
        // 保存附件信息
        if(StringUtils.isNotBlank(anFileList)){
            anModiBill.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anModiBill.getBatchNo()));
        }else{
            anModiBill.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo("", anModiBill.getBatchNo())); 
        }
        // 初始版本更改 直接返回
        if(bill.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
                bill.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_DRAFT)){
            if(anConfirmFlag){
                anModiBill.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
            }
             // 数据存盘
            this.updateByPrimaryKeySelective(anModiBill);
            return anModiBill;
        }
        // 需要升级版本的修改
        //this.updateByPrimaryKeySelective(anOrder);
        if(anConfirmFlag){
            anModiBill.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
        }
        //ScfOrderDO order2 = this.selectOneWithVersion(order.getRefNo(),order.getVersion());
        anModiBill=this.updateVersionByPrimaryKeySelective(anModiBill,anModiBill.getRefNo(),anModiBill.getVersion());
        BTAssert.notNull(anModiBill, "票据修改失败,原因：数据异常,请检查");
        logger.info("success to modify bill"+UserUtils.getOperatorInfo().getName());
        return anModiBill;
    }
    
    /**
     * 作废当前票据(当前创建者作废)
     * @param refNo
     * @param version
     * @return
     */
    public ScfAcceptBillDO saveAnnulBill(String refNo,String version){
        
        BTAssert.notNull(refNo, "凭证单号为空!操作失败");
        BTAssert.notNull(version, "数据版本为空!操作失败");
        logger.info("begin to aunul bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(refNo, version);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        bill=this.annulOperator(UserUtils.getOperatorInfo(), bill);
        logger.info("success to aunul bill"+UserUtils.getOperatorInfo().getName());
        return bill;
    }
    
    /**
     * 作废当前票据(同一个公司可以作废) 票据回收作废
     * @param refNo
     * @param version
     * @return
     */
    public ScfAcceptBillDO saveCoreCustAnnulBill(String refNo,String version){
        
        BTAssert.notNull(refNo, "凭证单号为空!操作失败");
        BTAssert.notNull(version, "数据版本为空!操作失败");
        logger.info("begin to saveCoreCustAnnulBill bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(refNo, version);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        List<Long> coreCustNoList = getCustNoList(custInfos);
        //bill.getCoreCustNo().
        if(! coreCustNoList.contains(bill.getCoreCustNo())){
            BTAssert.notNull(null, "你没有相应的权限!操作失败");
        }
        bill=this.annulEffectiveOperator(bill);
        logger.info("success to saveCoreCustAnnulBill bill"+UserUtils.getOperatorInfo().getName());
        return bill;
    }
    
    /**
     * 查询票据详情
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfAcceptBillDO findBill(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "数据版本为空!操作失败");
        logger.info("begin  to find bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        logger.info("success to find bill"+UserUtils.getOperatorInfo().getName());
        return bill;
    }
    
    
    /**
     * 票据核准
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfAcceptBillDO saveAuditBill(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "数据版本为空!操作失败");
        logger.info("begin to audit bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        checkStatus(UserUtils.getOperatorInfo().getOperOrg(), bill.getCoreOperOrg(), false, "您没有审核权限!操作失败");
        this.auditOperator(UserUtils.getOperatorInfo(), bill);
        logger.info("success to audit bill"+UserUtils.getOperatorInfo().getName());
        return bill;
        
    }
    
    /**
     * 查询未生效的票据信息
     * @param anMap 查询条件
     * @param anIsOnlyNormal 是否只查询用户登入的数据
     * @param anFlag 是否需要查询总的页数
     * @param anPageNum 当前页数
     * @param anPageSize 每页显示的数量
     * anAuditFlag true 是核准数据的查询  false 是登入列表的查询
     * @return
     */
    public Page<ScfAcceptBillDO> queryIneffectiveBill(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anAuditFlag) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 只查询数据非自动生成的数据来源
        if("1".equals(anIsOnlyNormal)){
            anMap.put("dataSource", "1");
        }
        // 模糊查询
        anMap = Collections3.fuzzyMap(anMap, new String[] { "billNo"});
        
        anMap.put("coreOperOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        if(anAuditFlag){
            
            if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                
                Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
                anMap.put("coreCustNo", getCustNoList(custInfos));
            }
            anMap.put("docStatus", VersionConstantCollentions.DOC_STATUS_CONFIRM);
        }else{
            
            anMap.put("operId", UserUtils.getOperatorInfo().getId());
        }
        
        Page<ScfAcceptBillDO> billList = this.selectPropertyIneffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo desc");
        
        return billList;
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
     * 查询已经生效的票据信息
     * @param anMap 查询条件
     * @param anIsOnlyNormal 数据来源是否只查询用户登入的数据
     * @param anFlag  是否查询总的数据量
     * @param anPageNum 每页显示的数据量
     * @param anPageSize 总的页数
     * @param anIsCust true 是供应商的已经生效的查询    false 是核心企业的已经生效的查询
     * @return
     */
    public Page<ScfAcceptBillDO> queryEffectiveBill(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anIsCust) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 只查询数据非自动生成的数据来源
        if("1".equals(anIsOnlyNormal)){
            anMap.put("dataSource", "1");
        }
        // 模糊查询
        anMap = Collections3.fuzzyMap(anMap, new String[] { "billNo"});
        
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        if(anIsCust){
            
            if (! anMap.containsKey("supplierNo") ||  anMap.get("supplierNo") ==null || StringUtils.isBlank(anMap.get("supplierNo").toString())) {
                anMap.put("supplierNo", getCustNoList(custInfos));
            }
            //anMap.put("supplierNo", getCustNoList(custInfos));
            
        }else{
            
            if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                anMap.put("coreCustNo", getCustNoList(custInfos));
            }
            anMap.put("coreOperOrg", UserUtils.getOperatorInfo().getOperOrg());
            
        }
        
        Page<ScfAcceptBillDO> billList = this.selectPropertyEffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo desc");
        
        return billList;
    }
    
    /**
     * 根据当前公司废止当前单据内容
     * @param anMap 查询条件
     * @param anIsOnlyNormal 1 只查询当前系统登入的数据
     * @param anFlag 是否需要查询总的数量
     * @param anPageNum 当前页数
     * @param anPageSize 每页显示的数据量
     * @return
     */
    
    public Page<ScfAcceptBillDO> queryCanAnnulBill(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 只查询数据非自动生成的数据来源
        if("1".equals(anIsOnlyNormal)){
            anMap.put("dataSource", "1");
        }
        // 模糊查询
        anMap = Collections3.fuzzyMap(anMap, new String[] { "billNo"});
        
        if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
            
            Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
            anMap.put("coreCustNo", getCustNoList(custInfos));
        }
        
        Page<ScfAcceptBillDO> billList = this.selectPropertyCanAunulByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo desc");
        
        return billList;
    }

    public List<ScfAcceptBillDO> saveResolveFile(List<Map<String,Object>> listMap) {
        
        return null;
    }

    
}
