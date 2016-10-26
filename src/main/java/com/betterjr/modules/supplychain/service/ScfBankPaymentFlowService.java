package com.betterjr.modules.supplychain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.supplychain.dao.ScfBankPaymentFlowMapper;
import com.betterjr.modules.supplychain.entity.ScfBankPaymentFlow;

/**
 * 供应商银行资金流水信息管理
 * @author zhoucy
 *
 */
@Service
public class ScfBankPaymentFlowService extends BaseService<ScfBankPaymentFlowMapper, ScfBankPaymentFlow> {
    
    /**
     * 获取供应商的银行账户流资金流水信息
     * @param anSupplierNo 供应商客户编号
     * @param anBankAccount 银行账户
     * @return
     */
    public List<ScfBankPaymentFlow> findCapitalFlowBySupplier(Long anSupplierNo,  String anBankAccount){
        Map map = new HashMap();
        map.put("supplierNo", anSupplierNo);
        map.put("suppBankAccount", anBankAccount);
        List<ScfBankPaymentFlow> result = this.selectByProperty(map);
        
        return result;
     }
    
    /**
     * 保存核心企业上传的供应商银行流水信息。
     * @param anList
     * @param anOperOrg
     * @return
     */
    public boolean saveUploadSupplierData(List<ScfBankPaymentFlow> anList, Long anCoreCustNo) {
        if (Collections3.isEmpty(anList)) {
            return true;
        }
        Set<String> distinctBtNo = ReflectionUtils.listToKeySet(anList, "requestNo");
        Map<String, Object> termMap = new HashMap<String, Object>();
        for (String btNo : distinctBtNo) {
            termMap.put("coreCustNo", anCoreCustNo);
            termMap.put("requestNo", btNo);
            this.deleteByExample(termMap);
            termMap.clear();
        }
        
        for (ScfBankPaymentFlow scfBank : anList) {
            scfBank.fillDefaultValue();
            this.insert(scfBank);
        }
        
        return true;
    }
}
