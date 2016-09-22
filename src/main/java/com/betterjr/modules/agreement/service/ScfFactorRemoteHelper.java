package com.betterjr.modules.agreement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.agreement.entity.FaceTradeResult;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.remote.entity.RemoteResultInfo;
import com.betterjr.modules.remote.helper.RemoteProxyFactory;

/**
 * 提供调用保理和沃通的远程接口辅助工具类。
 * 
 * @author zhoucy
 *
 */
@Service
@DependsOn("dictService")
public class ScfFactorRemoteHelper extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ScfFactorRemoteHelper.class);

    @Autowired
    private ScfElecAgreeStubService elecAgreeStubService;

    /**
     * 发送获取验证码请求
     * 
     * @param anRequestNo
     *            合同订单号
     * @param anCustNo
     *            客户号
     * @return
     */
    public boolean sendSMS(String anRequestNo, Long anCustNo) {

        return sendSmsInfo(anRequestNo, anCustNo, null);
    }

    /**
     * 发送验证码
     * 
     * @param anRequestNo
     *            合同订单号
     * @param anCustNo
     *            客户号
     * @param anVcode
     *            验证码信息
     * @return
     */
    public boolean sendValidSMS(String anRequestNo, Long anCustNo, String anVcode) {
        if (BetterStringUtils.isBlank(anVcode)) {
            return false;
        }

        return sendSmsInfo(anRequestNo, anCustNo, anVcode);
    }

    /**
     * 发起电子合同签署申请，将电子合同签署信息发送到沃通
     * 
     * @param anElecAgreement
     *            电子合同信息
     * @return
     */
    public boolean signElecAgreement(ScfElecAgreement anElecAgreement) {
        if (anElecAgreement == null) {
            return false;
        }

        ScfRemoteService remoteService = RemoteProxyFactory.createService("wos", ScfRemoteService.class);
        RemoteResultInfo remoteResult;
        FaceTradeResult result;
        boolean rtResult = false;
        try {
            anElecAgreement = findSignerlist(anElecAgreement);
            remoteResult = remoteService.createSignOrder(anElecAgreement);
            result = (FaceTradeResult) remoteResult.getResult();
            rtResult = "1".equals(result.getStatus());
            if (rtResult) {
                anElecAgreement.setSignId(result.getMessageID());
                anElecAgreement.setSignStatus("2");
                anElecAgreement.setDealFlag("1");
            }
            else {
                anElecAgreement.setSignStatus("3");
                anElecAgreement.setDealFlag("2");
            }
        }
        catch (Exception ex) {
            logger.error("CreateSignOrder Has Error", ex);
        }

        return rtResult;
    }

    private boolean sendSmsInfo(String requestNo, Long custNo, String vcode) {
        ScfRemoteService remoteService=RemoteProxyFactory.createService("wos", ScfRemoteService.class);
        RemoteResultInfo remoteResult;
        FaceTradeResult result;
        try {
            if (BetterStringUtils.isBlank(vcode)) {
                remoteResult = remoteService.sendSMS(requestNo, custNo);
            }
            else {
                remoteResult = remoteService.validateSMS(requestNo, custNo, vcode);
            }

            result = (FaceTradeResult) remoteResult.getResult();
            return result.getStatus().equals("1");
        }
        catch (Exception ex) {

            logger.error("sendSmsInfo Has Error", ex);
            return false;
        }
    }
    
    /**
     * 查找签署方的信息
     * 
     * @param anElecAgreement
     * @return
     */
    private ScfElecAgreement findSignerlist(ScfElecAgreement anElecAgreement) {
        anElecAgreement.setSignerlist(this.elecAgreeStubService.findSignerForWosign(anElecAgreement.getAppNo()));
        return anElecAgreement;
    }
}