package com.betterjr.modules.agreement.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.service.CustAccountService;
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

    private static final Logger logger = LoggerFactory.getLogger(ScfRequestProtacalService.class);
    
    @Autowired
    private ScfElecAgreementService elecAgreementService;
    @Autowired
    private CustAccountService custAccoService;
    
    /***
     * 更新三方协议
     * @param protacal
     * @return
     */
    public boolean updateProtacalInfo(ScfRequestProtacal protacal,BigDecimal anBalance){
        protacal.setAgreeName("保兑仓业务三方合作协议");
        ScfRequestProtacal tmpProtacal = this.selectByPrimaryKey(protacal.getRequestNo());
        if(tmpProtacal==null){
            protacal.setFirstName(initCustName(protacal.getFirstName(),Long.parseLong(protacal.getFirstNo())));
            protacal.setSecondName(initCustName(protacal.getSecondName(),protacal.getSecondNo()));
            protacal.setThreeName(initCustName(protacal.getThreeName(),protacal.getThreeNo()));
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
    
    public String initCustName(String anCustName,Long anCustNo){
        try {
            if(BetterStringUtils.isBlank(anCustName)){
                anCustName=custAccoService.queryCustName(anCustNo);
            }
        }
        catch (Exception e) {
            logger.error("initCustName erorr:"+e.getMessage());
            anCustName="";
        }
        return anCustName;
    }
}
