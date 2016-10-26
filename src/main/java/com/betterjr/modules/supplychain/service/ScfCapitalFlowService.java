package com.betterjr.modules.supplychain.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.supplychain.dao.ScfCapitalFlowMapper;
import com.betterjr.modules.supplychain.entity.ScfCapitalFlow;

import java.util.*;

import org.springframework.stereotype.Service;

/**
 * 供应商付款流水管理，相对于核心企业
 * @author zhoucy
 *
 */
@Service
public class ScfCapitalFlowService extends BaseService<ScfCapitalFlowMapper, ScfCapitalFlow> {

    public List<ScfCapitalFlow> findCapitalFlowBySupplier(Long anSupplierNo,  String anBankAccount){
       Map map = new HashMap();
       map.put("supplierNo", anSupplierNo);
       map.put("suppBankAccount", anBankAccount);
       
       return this.selectByProperty(map);
    }
}
