package com.betterjr.modules.receivable.service;

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
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.receivable.dao.ScfReceivableDOMapper;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfReceivableDOService extends BaseVersionService<ScfReceivableDOMapper, ScfReceivableDO> {

    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    /**
     * 应收账款新增
     * @param anReceivable
     * @param anFileList   上传的附件内容
     * @param anConfirmFlag true 需要确认新增结果  false  只新增当前结果
     * @return
     */
    public ScfReceivableDO addReceivable(ScfReceivableDO anReceivable, String anFileList,boolean anConfirmFlag) {
        
        BTAssert.notNull(anReceivable,"插入订单为空,操作失败");
        logger.info("Begin to add addReceivable"+UserUtils.getOperatorInfo().getName());
        anReceivable.initAddValue(UserUtils.getOperatorInfo(),anConfirmFlag);
        // 操作机构设置为供应商
        anReceivable.setOperOrg(baseService.findBaseInfo(anReceivable.getCustNo()).getOperOrg());
        anReceivable.setCustName(custAccountService.queryCustName(anReceivable.getCustNo()));
        anReceivable.setCoreCustName(custAccountService.queryCustName(anReceivable.getCoreCustNo()));
        // 保存附件信息
        if(StringUtils.isNoneBlank(anFileList)){
            anReceivable.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anReceivable.getBatchNo()));
        }
        anReceivable=this.insertVersion(anReceivable);
        logger.info("success to add addReceivable"+UserUtils.getOperatorInfo().getName());
        return anReceivable;
    }
    
    
    /**
     * 保存编辑应收账款
     * @param anModiReceivable
     * @param anFileList
     * @param anConfirmFlag  true  保存编辑并确认    false  只保存编辑
     * @return
     */
    public ScfReceivableDO saveModifyReceivable(ScfReceivableDO anModiReceivable,String anFileList,boolean anConfirmFlag) {
        
        BTAssert.notNull(anModiReceivable,"编辑保存应收账款为空,操作失败");
        BTAssert.notNull(anModiReceivable.getRefNo(),"凭证编号为空,操作失败");
        BTAssert.notNull(anModiReceivable.getVersion(),"应收账款信息不全,操作失败");
        logger.info("Begin to modify saveModifyOrder");
        ScfReceivableDO receivable = this.selectOneWithVersion(anModiReceivable.getRefNo(),anModiReceivable.getVersion());
        BTAssert.notNull(receivable, "无法获取应收账款信息");
        //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
        checkOperatorModifyStatus(UserUtils.getOperatorInfo(),receivable);
        // 应收账款信息变更迁移初始化
        anModiReceivable.initModifyValue(receivable);
        //anModiOrder.setId(anOrder.getId());
        anModiReceivable.setCustName(custAccountService.queryCustName(anModiReceivable.getCustNo()));
        anModiReceivable.setCoreCustName(custAccountService.queryCustName(anModiReceivable.getCoreCustNo()));
        // 操作机构设置为供应商
        anModiReceivable.setOperOrg(baseService.findBaseInfo(anModiReceivable.getCustNo()).getOperOrg());
        // 保存附件信息
        if(StringUtils.isNotBlank(anFileList)){
            anModiReceivable.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anModiReceivable.getBatchNo()));
        }else{
            anModiReceivable.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo("", anModiReceivable.getBatchNo())); 
        }
        // 初始版本更改 直接返回
        if(receivable.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
                receivable.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_DRAFT)){
            if(anConfirmFlag){
                anModiReceivable.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
            }
             // 数据存盘
            this.updateByPrimaryKeySelective(anModiReceivable);
            return anModiReceivable;
        }
        // 需要升级版本的修改
        anModiReceivable.setIsLatest(VersionConstantCollentions.IS_LATEST);
        anModiReceivable.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        anModiReceivable.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        //this.updateByPrimaryKeySelective(anOrder);
        if(anConfirmFlag){
            anModiReceivable.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
        }
        anModiReceivable=this.updateVersionByPrimaryKeySelective(anModiReceivable,receivable.getRefNo(),receivable.getVersion());
        BTAssert.notNull(anModiReceivable, "修改订单失败");
        logger.info("success to modify saveModifyOrder"+UserUtils.getOperatorInfo().getName());
        return anModiReceivable;
    }
    
    /**
     * 对指定应收账款单据进行废止操作
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfReceivableDO saveAnnulReceivable(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "应收账款凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfReceivableDO receivable = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(receivable, "此应收账款异常!操作失败");
        receivable=this.annulOperator(UserUtils.getOperatorInfo(), receivable);
        return receivable;
        
    }
    
    /**
     * 查找指定应收账款单据
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfReceivableDO findReceivable(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "应收账款凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfReceivableDO receivable = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(receivable, "此应收账款异常!操作失败");
        return receivable;
    }
    
    /**
     * 给订单的单据进行审核，必须是核心企业中的人员才能审核
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfReceivableDO saveAuditReceivable(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "应收账款凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "操作异常为空!操作失败");
        ScfReceivableDO receivable = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(receivable, "此应收账款异常!操作失败");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "获取当前企业失败!操作失败");
        if(! getCustNoList(custInfos).contains(receivable.getCoreCustNo())){
            BTAssert.notNull(receivable, "您没有审核权限!操作失败"); 
        }
        this.auditOperator(UserUtils.getOperatorInfo(), receivable);
        return receivable;
        
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
     * 查询登入界面和核准界面的应收账款信息
     * @param anMap 查询条件
     * @param anFlag 是否需要查询总的数量
     * @param anPageNum 
     * @param anPageSize
     * @param anIsAudit true  核准界面的数据     false  登入界面的数据
     * @return
     */
    public Page<ScfReceivableDO> queryIneffectiveReceivable(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean anIsAudit) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        
        if(anIsAudit){
            if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                
                Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
                anMap.put("coreCustNo", getCustNoList(custInfos));
            }
            anMap.put("docStatus", VersionConstantCollentions.DOC_STATUS_CONFIRM);
            
        }else{
            anMap.put("modiOperId", UserUtils.getOperatorInfo().getId());
        }
        
        Page<ScfReceivableDO> receivableList = this.selectPropertyIneffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo");
        
        return receivableList;
    }
    
    public Page<ScfReceivableDO> queryEffectiveReceivable(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean anIsCust) {
        
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前登录的用户下所有企业信息
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        
        if(anIsCust){
            //供应商查询已经生效的数据
            if (! anMap.containsKey("custNo") ||  anMap.get("custNo") ==null || StringUtils.isBlank(anMap.get("custNo").toString())) {
                anMap.put("custNo", getCustNoList(custInfos));
            }
            //anMap.put("custNo", getCustNoList(custInfos));
            anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
            
        }else{
            //核心企业查询已经生效的数据
            if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                anMap.put("coreCustNo", getCustNoList(custInfos));
            }
        }
        
        Page<ScfReceivableDO> receivableList = this.selectPropertyEffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo");
        
        return receivableList;
    }


    public List<ScfReceivableDO> saveResolveFile(List<Map<String,Object>> listMap) {
        
        return null;
    }
    
}
