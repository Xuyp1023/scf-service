package com.betterjr.modules.ledger.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.ledger.IContractLedgerService;
import com.betterjr.modules.ledger.entity.ContractLedger;
import com.betterjr.modules.ledger.entity.CustContractLedger;
import com.betterjr.modules.ledger.service.ContractLedgerService;
import com.betterjr.modules.ledger.utils.ContractLedgerUtils;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass=IContractLedgerService.class)
public class ContractLedgerDubboService implements IContractLedgerService{

    @Autowired
    public ContractLedgerService contractLedgerService;
    
    @Override
    public String webQueryContractLedgerByPage(Map<String, Object> anMap, String anQueryType, int anPageNum, int anPageSize) {
        return AjaxObject.newOkWithPage("分页查询",contractLedgerService.queryContractLedgerByPage(anMap, anQueryType, anPageNum, anPageSize)).toJson();
    }
    
    public String webFindCustInfoByCustNo(Long anCustNo){
        return AjaxObject.newOk("查询客户信息", contractLedgerService.findCustInfoByCustNo(anCustNo)).toJson();
    }
    
    /***
     * 添加合同台账信息
     */
    public String webAddContractLedger(Map<String, Object> anMap, String anFileList){
//        anMap=ContractLedgerUtils.convertDateStr(anMap);
        Map<String,Object> buyerInfoMap=(Map<String,Object>)anMap.get("buyerInfo");
        Map<String,Object> supplierInfoMap=(Map<String,Object>)anMap.get("supplierInfo");
        ContractLedger contractLedger=(ContractLedger)RuleServiceDubboFilterInvoker.getInputObj();
        CustContractLedger buyerCustContractLedger=new CustContractLedger();
        CustContractLedger supplierCustContractLedger=new CustContractLedger();
        BeanMapper.copy(buyerInfoMap, buyerCustContractLedger);
        BeanMapper.copy(supplierInfoMap, supplierCustContractLedger);
        return AjaxObject.newOk("添加合同台账",contractLedgerService.addContractLedger(contractLedger,buyerCustContractLedger,supplierCustContractLedger, anFileList)).toJson();
    }
    
    /***
     * 修改合同台账
     */
    public String webSaveContractLedger(Map<String, Object> anMap, String anFileList){
        Map<String,Object> buyerInfoMap=(Map<String,Object>)anMap.get("buyerInfo");
        Map<String,Object> supplierInfoMap=(Map<String,Object>)anMap.get("supplierInfo");
        ContractLedger contractLedger=(ContractLedger)RuleServiceDubboFilterInvoker.getInputObj();
        CustContractLedger buyerCustContractLedger=new CustContractLedger();
        CustContractLedger supplierCustContractLedger=new CustContractLedger();
        BeanMapper.copy(buyerInfoMap, buyerCustContractLedger);
        BeanMapper.copy(supplierInfoMap, supplierCustContractLedger);
        return AjaxObject.newOk("修改合同台账",contractLedgerService.saveContractLedger(contractLedger,buyerCustContractLedger,supplierCustContractLedger, anFileList)).toJson();
    }
    
    /***
     * 根据客户号，合同号查询合同客户信息
     * @param anCustNo
     * @return
     */
    public String webFindCustInfoByCustNoAndContractId(Long anCustNo,Long anContractId){
        return AjaxObject.newOk("查询合同台账客户信息",contractLedgerService.findCustInfoByCustNoAndContractId(anCustNo, anContractId)).toJson();
    }
    
    /***
     * 根据合同编号查询合同信息
     * @param anContractId
     * @return
     */
    public String webFindContractLedgerByContractId(Long anContractId){
        return AjaxObject.newOk("查询合同信息",contractLedgerService.findContractLedgerByContractId(anContractId)).toJson();
    }
    
    /**
     * 根据批次号获取合同附件列表
     * 
     * @param anParam
     * @return
     */
    public String webFindCustFileItems(Long anContractId) {
        return AjaxObject.newOk("查询附件信息",contractLedgerService.findCustFileItems(anContractId)).toJson();
    }
    
    /***
     * 修改合同状态
     * @param anContractId
     * @param anStatus
     */
    public String webSaveContractLedgerStatus(Long anContractId,String anStatus){
        if(contractLedgerService.saveContractLedgerStatus(anContractId, anStatus)){
            return AjaxObject.newOk("状态变更成功").toJson();
        }else{
            return AjaxObject.newOk("状态变更失败").toJson();
        }
    }
    
    /***
     * 查询合同台账记录
     * @param anContractId
     * @return
     */
    public String webFindContractLedgerRecode(Long anContractId){
        return AjaxObject.newOk("查询合同台账记录",contractLedgerService.findContractLedgerRecode(anContractId)).toJson();
    }
    
    
}
