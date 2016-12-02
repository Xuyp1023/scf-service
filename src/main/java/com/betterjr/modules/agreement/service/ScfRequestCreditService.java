package com.betterjr.modules.agreement.service;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.agreement.dao.ScfRequestCreditMapper;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.loan.entity.ScfRequest;

import java.util.*;

/**
 * 应收账款转让通知书-应收账款管理
 * @author zhoucy
 *
 */
@Service
public class ScfRequestCreditService extends BaseService<ScfRequestCreditMapper, ScfRequestCredit>{

    /**
     * 根据订单号，查询转让协议中的明细内容
     * @param anRequestNo
     * @return
     */
    public List<ScfRequestCredit> findByRequestNo(String anRequestNo){
        
        return this.selectByProperty("requestNo", anRequestNo);
    }
    
    public boolean updateCreditList(ScfRequest anRequest,String anNoticNo, List<ScfRequestCredit> anList,String anAgreeNo){
        this.deleteByProperty("requestNo", anRequest.getRequestNo());
        for(ScfRequestCredit credit : anList){
           BTAssert.notNull(credit.getInvoiceNo(),"请完善发票信息");
           credit.fillInfo(anRequest);
           credit.setConfirmNo(anNoticNo);
           credit.setAgreeNo(anAgreeNo);
           this.insert(credit);
        }
        return true;
    } 
}
