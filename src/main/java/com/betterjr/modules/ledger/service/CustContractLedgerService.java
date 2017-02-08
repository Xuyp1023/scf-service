package com.betterjr.modules.ledger.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.customer.ICustRelationConfigService;
import com.betterjr.modules.customer.data.FactorBusinessRequestData;
import com.betterjr.modules.ledger.dao.CustContractLedgerMapper;
import com.betterjr.modules.ledger.entity.CustContractLedger;

/****
 * 贸易合同台账关联的客户基本信息
 * @author hubl
 *
 */
@Service
public class CustContractLedgerService extends BaseService<CustContractLedgerMapper,CustContractLedger>{
    
    private static final Logger logger=LoggerFactory.getLogger(CustContractLedgerService.class);
    

    @Reference(interfaceClass=ICustRelationConfigService.class)
    private ICustRelationConfigService custRelationConfigService;

    /***
     * 查询合同客户基础信息
     * @param anCustNo
     * @return
     */
    public CustContractLedger initCustContractLedger(Long anCustNo){
        List<CustContractLedger> custContractractList=this.selectByProperty("custNo", anCustNo,"regDate DESC");
        if(custContractractList!=null && custContractractList.size()>0){
            return Collections3.getFirst(custContractractList);
        }else{
            FactorBusinessRequestData businessData=custRelationConfigService.findBusinessCustInfo(anCustNo);
            logger.info("businessData:"+businessData);
            CustContractLedger custContractLedger=new CustContractLedger();
            BeanMapper.copy(businessData, custContractLedger);
            custContractLedger.initValue();
            custContractLedger.setRepresentative(businessData.getOperName());
            custContractLedger.setContractId(0l);
            addCustContractLedger(custContractLedger); // 添加初始的关系信息
            return custContractLedger;
        }
    }
    
    public void addCustContractLedger(CustContractLedger custContractLedger){
        this.insert(custContractLedger);
    }
    
    // 只有添加的时候才能更新合同编号信息
    public boolean svaeCustContractLedger(CustContractLedger anCustContractLedger){
        // 首先通过客户号与合同编号查询信息是否存在，如果不存在，则做添加操作，否则做修改操作
        CustContractLedger custContractLedger=findCustContractByCustNoAndContractId(anCustContractLedger.getCustNo(),anCustContractLedger.getContractId());
        if(custContractLedger==null){
            addCustContractLedger(anCustContractLedger);
            return true;
        }else{
            Map<String,Object> anMap=new HashMap<String,Object>();
            anMap.put("custNo", anCustContractLedger.getCustNo());
            anMap.put("contractId", anCustContractLedger.getContractId());
            return this.updateByExampleSelective(anCustContractLedger, anMap)>0;
        }
    }
    
    /***
     * 根据客户号，合同号查询合同台账信息
     * @param anCustNo
     * @param anContractId
     * @return
     */
    public CustContractLedger findCustContractByCustNoAndContractId(Long anCustNo,Long anContractId){
        Map<String,Object> anMap=new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        anMap.put("contractId", anContractId);
        return Collections3.getFirst(this.selectByProperty(anMap));
    }
    
}
