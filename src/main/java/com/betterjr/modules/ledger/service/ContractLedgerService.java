package com.betterjr.modules.ledger.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.mapper.pagehelper.PageHelper;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.ledger.dao.ContractLedgerMapper;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.entity.CustContractLedger;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.wechat.ICustWeChatService;

/***
 * 合同台账服务类
 * @author hubl
 *
 */
@Service
public class ContractLedgerService  extends BaseService<ContractLedgerMapper, ContractLedger>{
    
    private static Logger logger=LoggerFactory.getLogger(ContractLedgerService.class);
    
    @Reference(interfaceClass=ICustFileService.class)
    private ICustFileService custFileService;
    @Autowired
    private CustContractLedgerService custContractLedgerService;
    @Autowired
    private ContractLedgerRecodeService contractLedgerRecodeService;
    @Reference(interfaceClass=ICustWeChatService.class)
    private ICustWeChatService custWeChatService;
    
    /****
     * 分页查询合同台账信息
     * @param anMap 入参
     * @param anQueryType 业务查询类型 (1：登记查询-审核查询，2查询所有)
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ContractLedger> queryContractLedgerByPage(Map<String,Object> anMap,String anQueryType, int anPageNum, int anPageSize){
        Map<String,Object> map=setCustNoParam();
        map.put("operOrg", UserUtils.findOperOrg());
        if(BetterStringUtils.isNotBlank((String)anMap.get("businStatus"))){
            map.put("businStatus", anMap.get("businStatus"));
        }
        if(BetterStringUtils.equals("1", anQueryType)){
            map.put("businStatus", "0");
        }else if(BetterStringUtils.equals("2", anQueryType)){
            map.put("businStatus", new String[]{"0","1","2"});
        }
        if(BetterStringUtils.isNotBlank((String)anMap.get("GTEsignDate"))){
            map.put("GTEsignDate", anMap.get("GTEsignDate"));
            map.put("LTEsignDate", anMap.get("LTEsignDate"));
        }
        logger.info("分页查询合同台账信息 queryContractLedgerByPage-anMap:"+map);
        String flag=(String)anMap.get("flag");
        map.put("isLatest", VersionConstantCollentions.IS_LATEST);
        return this.selectPropertyByPage(map, anPageNum, anPageSize, "1".equals(flag),"id desc");
    }
    
    /***
     * 添加合同台账
     * @param anContractLedger 合同台账对象
     * @param anFileList 附件列表
     * @return
     */
    public ContractLedger addContractLedger(ContractLedger anContractLedger,CustContractLedger anBuyerCustContractLedger,CustContractLedger anSupplierCustContractLedger,String anFileList){
        anContractLedger.initValue();   
        logger.info("anContractLedger:"+anContractLedger);
        logger.info("anBuyerCustContractLedger:"+anBuyerCustContractLedger);
        logger.info("anSupplierCustContractLedger:"+anSupplierCustContractLedger);
        if(BetterStringUtils.isNotBlank(anFileList)){
            anContractLedger.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, anContractLedger.getBatchNo()));
        }
        this.insert(anContractLedger);
        // 修改签署方的信息
        anBuyerCustContractLedger.setContractId(anContractLedger.getId());
        anSupplierCustContractLedger.setContractId(anContractLedger.getId());
        custContractLedgerService.addCustContractLedger(anBuyerCustContractLedger);
        custContractLedgerService.addCustContractLedger(anSupplierCustContractLedger);
        // 添加台账记录
        addContractLedgerRecode(anContractLedger.getId(),anContractLedger.getBusinStatus());
        return anContractLedger;
    }
    
    /***
     * 根据客户号查询基本的客户信息
     * @param anCustNo
     * @return
     */
    public CustContractLedger findCustInfoByCustNo(Long anCustNo){
        CustContractLedger custContractLedger=new CustContractLedger();
        if(anCustNo!=null){
            custContractLedger=custContractLedgerService.initCustContractLedger(anCustNo);
        }else{
            custContractLedger=custContractLedgerService.initCustContractLedger(UserUtils.getDefCustInfo().getCustNo());
        }
        return custContractLedger;
    }
    
    /***
     * 根据客户号，合同号查询合同和客户信息
     * @param anCustNo
     * @return
     */
    public CustContractLedger findCustInfoByCustNoAndContractId(Long anCustNo,Long anContractId){
        CustContractLedger custContractLedger = custContractLedgerService.findCustContractByCustNoAndContractId(anCustNo, anContractId); 
        if(custContractLedger==null){
            custContractLedger=findCustInfoByCustNo(anCustNo);
        }
        
        return custContractLedger;
    }
    
    /***
     * 根据合同编号查询合同信息
     * @param anContractId
     * @return
     */
    public ContractLedger findContractLedgerByContractId(Long anContractId){
        return this.selectByPrimaryKey(anContractId);
    }
    
    /**
     * 根据批次号获取合同附件列表
     * 
     * @param anParam
     * @return
     */
    public List<CustFileItem> findCustFileItems(Long anContractId) {
        ContractLedger contractLedger=this.selectByPrimaryKey(anContractId);
        if (null == contractLedger) {
            throw new BytterTradeException(40001, "无法获取合同信息！");
        }
        return custFileService.findCustFiles(contractLedger.getBatchNo());
    }
    
    /***
     * 修改合同信息
     * @param anContractLedger 合同信息
     * @param anBuyerCustContractLedger 买方客户合同
     * @param anSupplierCustContractLedger 卖方客户合同
     * @param anFileList 文件列表
     * @return
     */
    public ContractLedger saveContractLedger(ContractLedger anContractLedger,CustContractLedger anBuyerCustContractLedger,CustContractLedger anSupplierCustContractLedger,String anFileList){
        ContractLedger contractLedger=this.selectByPrimaryKey(anContractLedger.getId());
        if ("0".equals(contractLedger.getBusinStatus()) == false) {
            throw new BytterTradeException(40001, "已生效或废止的合同不能修改！");
        }
        if (VersionConstantCollentions.IS_NOT_LATEST.equals(contractLedger.getIsLatest())) {
            throw new BytterTradeException(40001, "当前合同已不是最新版本不能修改！");
        }
        ContractLedger reqContractLedger=anContractLedger;
        reqContractLedger.modifyContractLedger(contractLedger);
        reqContractLedger.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, reqContractLedger.getBatchNo()));
        //this.updateByPrimaryKey(reqContractLedger);
        this.insertSelective(reqContractLedger);
        contractLedger.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
        this.updateByPrimaryKey(contractLedger);
        // 修改买方，卖方的客户信息
        anBuyerCustContractLedger.setContractId(reqContractLedger.getId());
        anSupplierCustContractLedger.setContractId(reqContractLedger.getId());
        anBuyerCustContractLedger.initValue();
        anSupplierCustContractLedger.initValue();
        boolean buyerFlag=custContractLedgerService.svaeCustContractLedger(anBuyerCustContractLedger);
        boolean supplierFlag=custContractLedgerService.svaeCustContractLedger(anSupplierCustContractLedger);
        logger.info("buyerFlag："+buyerFlag+"，supplierFlag："+supplierFlag);
        // 添加台账记录
        addContractLedgerRecode(reqContractLedger.getId(),anContractLedger.getBusinStatus());
        return reqContractLedger;
    }
    
    /***
     * 修改合同状态
     * @param anContractId
     * @param anStatus
     */
    public boolean saveContractLedgerStatus(Long anContractId,String anStatus){
        boolean bool=false;
        ContractLedger contractLedger=this.selectByPrimaryKey(anContractId);
        if (null == contractLedger) {
            throw new BytterTradeException("未查找到合同信息");
        }
        contractLedger.initModiDateValue();
        contractLedger.setBusinStatus(anStatus);
        if(VersionConstantCollentions.LOCKED_STATUS_LOCKED.equals(contractLedger.getLockedStatus())){
            throw new BytterTradeException(40001, "当前单据已经用于融资，不能修改！");
        }
        contractLedger.setBusinVersionStatus(anStatus);
        bool=this.updateByPrimaryKey(contractLedger)>0;
        if(bool){
            // 添加修改状态的记录
            addContractLedgerRecode(anContractId,anStatus);
        }
        return bool;
    }

    // 添加台账记录
    public void addContractLedgerRecode(Long anContractId,String anStatus){
        contractLedgerRecodeService.addContractLedgerRecode(anContractId,anStatus);
    }
    
    /***
     * 查询审核记录
     * @param anContractId
     * @return
     */
    public Map<String,Object> findContractLedgerRecode(Long anContractId){
        return contractLedgerRecodeService.findContractLedgerRecode(anContractId);
    }
    
    /***
     * 设置客户号查询参数
     * @param anMap
     * @return
     */
    public Map<String,Object> setCustNoParam(){
        Map<String,Object> map=new HashMap<String,Object>();
        if(UserUtils.supplierUser()){
            map.put("supplierNo", UserUtils.getDefCustInfo().getCustNo());
        }else if(UserUtils.sellerUser()){
            // 获取当前登录操作员信息
            map.put("buyerNo",UserUtils.getDefCustInfo().getCustNo());
        }
        return map;
    }
    
    /***
     * 添加合同台账
     * @param anContractLedger 合同台账对象
     * @return
     */
    public ContractLedger addContractLedger(ContractLedger anContractLedger){
        anContractLedger.initValue();
        logger.info("anContractLedger:"+anContractLedger);
        this.insert(anContractLedger);
        // 添加台账记录
        addContractLedgerRecode(anContractLedger.getId(),anContractLedger.getBusinStatus());
        return anContractLedger;
    }
    
    /***
     * 保存合同台账附件
     * @param id 合同台账id
     * @param fileTypeName 文件类型名称
     * @param fileMediaId  文件
     * @return
     */
    public CustFileItem saveContractLedgerFile(Long anId,String anFileTypeName,String anFileMediaId){
        ContractLedger contractLedger=this.selectByPrimaryKey(anId);
        CustFileItem fileItem = (CustFileItem)custWeChatService.fileUpload(anFileTypeName, anFileMediaId);
        logger.info("custFileItem:"+fileItem);
        if(fileItem!=null){
            fileItem.setBatchNo(custFileService.updateCustFileItemInfo(fileItem.getId().toString(), contractLedger.getBatchNo()));
            contractLedger.setBatchNo(fileItem.getBatchNo());
            this.updateByPrimaryKey(contractLedger);
        }else{
            fileItem=new CustFileItem();
        }
        return fileItem;
    }
    
    /***
     * 修改合同信息-微信端
     * @param anContractLedger 合同信息
     * @return
     */
    public ContractLedger saveContractLedger(ContractLedger anContractLedger){
        ContractLedger contractLedger=this.selectByPrimaryKey(anContractLedger.getId());
        if ("0".equals(contractLedger.getBusinStatus()) == false) {
            throw new BytterTradeException(40001, "已生效或废止的合同不能修改！");
        }
        ContractLedger reqContractLedger=anContractLedger;
        reqContractLedger.modifyContractLedger(contractLedger);
        this.updateByPrimaryKey(reqContractLedger);
        // 添加台账记录
        addContractLedgerRecode(anContractLedger.getId(),anContractLedger.getBusinStatus());
        return reqContractLedger;
    }
    
    /***
     * 根据合同id查询文件信息
     * @param anContractId
     * @return
     */
    public ContractLedger findFileByContractId(Long anContractId){
        ContractLedger contractLedger=this.selectByPrimaryKey(anContractId);
        contractLedger.setCustFileList(custFileService.findCustFiles(contractLedger.getBatchNo()));
        return contractLedger;
    }
    
    /***
     * 根据id 删除合同台账
     * @param anContractId
     * @return
     */
    public boolean deleteContractById(Long anContractId){
        return this.deleteByPrimaryKey(anContractId)>0;
    }
    
    /**
     * 微信，删除附件
     */
    public boolean deleteContractFile(Long anFileId) {
        CustFileItem anFile = custFileService.findOne(anFileId);
        return custFileService.deleteFileItem(anFile.getId(),anFile.getBatchNo());
    }
    
    public Page<ContractLedger> selectCanUsePageWithVersion (Map<String, Object> paramMap, int anPageNum, int anPageSize, boolean anFlag, String anOrderDesc) {
        paramMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        paramMap.put("businStatus",VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE);
        paramMap.put("lockedStatus",VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        PageHelper.startPage(anPageNum, anPageSize, anFlag);
        return (Page) this.selectByProperty(paramMap, anOrderDesc);
    }

    
    
    
}
