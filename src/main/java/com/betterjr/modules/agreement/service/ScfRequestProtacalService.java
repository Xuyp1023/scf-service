package com.betterjr.modules.agreement.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.agreement.dao.ScfRequestProtacalMapper;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
/***
 * 三方协议
 * @author hubl
 *
 */
@Service
public class ScfRequestProtacalService extends BaseService<ScfRequestProtacalMapper, ScfRequestProtacal> {

    @Autowired
    private ScfElecAgreementService elecAgreementService;
    
    /***
     * 更新三方协议
     * @param protacal
     * @return
     */
    public boolean updateProtacalInfo(ScfRequestProtacal protacal,BigDecimal anBalance){
        protacal.setAgreeName("保兑仓业务三方合作协议");
        ScfRequestProtacal tmpProtacal = this.selectByPrimaryKey(protacal.getRequestNo());
        if(tmpProtacal==null){
            this.insert(protacal);
        }else if("0".equals(tmpProtacal.getBusinStatus())){
            this.updateByPrimaryKey(protacal);
        }else{
            throw new BytterTradeException("此申请单协议已签署");
        }
        ScfElecAgreement elecAgreement = ScfElecAgreement.createByProtacal(protacal.getAgreeName(), protacal.getProtocalNo(),anBalance);
        elecAgreement.setRequestNo(protacal.getRequestNo());
        elecAgreementService.addElecAgreementInfo(elecAgreement);
        elecAgreement.setSupplierNo(protacal.getThreeNo());
        elecAgreement.setRequestNo(protacal.getRequestNo());
        elecAgreement.setBuyerNo(protacal.getSecondNo());
        elecAgreement.setFactorNo(protacal.getFirstNo());
        elecAgreement.setSupplier(protacal.getThreeName());
        elecAgreementService.addElecAgreementInfo(elecAgreement, Arrays.asList(protacal.getThreeNo()));
        return true;
    }
}
