package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.agreement.entity.FaceTradeResult;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.remote.entity.RemoteResultInfo;
import com.betterjr.modules.remote.helper.RemoteProxyFactory;
import com.betterjr.modules.agreement.utils.SupplyChainUtil;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.product.entity.ScfProduct;
import com.betterjr.modules.product.service.ScfProductService;

/**
 * 提供调用保理和沃通的远程接口辅助工具类。
 *  lipeijie@kukahome.com
 * @author zhoucy
 *
 */
@Service
@DependsOn("dictService")
public class ScfFactorRemoteHelper extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ScfFactorRemoteHelper.class);

    @Autowired
    private ScfElecAgreeStubService elecAgreeStubService;

    @Autowired
    private ScfProductService scfProductService;

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
    

    /**
     * 查询供应链金融保理方的产品信息
     */
    public void queryProductInfo() {
        Map<String, String> agencyGroup = SupplyChainUtil.findFactorCorpInfo();
        RemoteResultInfo<List<ScfProduct>> remoteResult;
        List<ScfProduct> result;
        for (String agencyNo : agencyGroup.keySet()) {

            ScfRemoteService remoteService = RemoteProxyFactory.createService(agencyNo, ScfRemoteService.class);
            remoteResult = remoteService.queryFactorProduct(new HashMap());
            if (remoteResult.isSucess()) {
                result = (List<ScfProduct>) remoteResult.getData();
                for (ScfProduct product : result) {
                    product.setFactorCorp(agencyNo);
                    this.scfProductService.saveOrUpdateProduct(product);
                }
            }
        }
    }

    /**
     * 发送融资申请信息给保理
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest sendRequest(ScfRequest anRequest) {

        return dealFactorAppRequest(anRequest, true);
    }

    /**
     * 发送融资申请的状态给保理。
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest sendRequestStatus(ScfRequest anRequest) {

        return dealFactorAppRequest(anRequest, false);
    }

    protected ScfRequest dealFactorAppRequest(ScfRequest anRequest, boolean anWorkApp) {
        logger.info("dealFactorAppRequest :" + anRequest);
        ScfRemoteService remoteService=RemoteProxyFactory.createService(DictUtils.getDictCode("ScfFactorGroup", String.valueOf(anRequest.getFactorNo())), ScfRemoteService.class);
        RemoteResultInfo remoteResult;
        ScfRequest result;
        try {
            if (anWorkApp) {
                remoteResult = remoteService.appFinancing(anRequest);
                if (remoteResult.isSucess()) {
                    result = (ScfRequest) remoteResult.getResult();
                    logger.info("return dealFactorAppRequest data :" + result);
                    anRequest.setFactorRequestNo(result.getFactorRequestNo());
                    anRequest.setTradeStatus(result.getTradeStatus());
                    anRequest.setCreditMoney(MathExtend.defaultZero(result.getCreditMoney()));
                    anRequest.setRemainMoney(MathExtend.defaultZero(result.getRemainMoney()));
                    logger.info("return dealFactorAppRequest anRequest :" + anRequest);
                }
                else {
                    logger.warn("return dealFactorAppRequest data false " + remoteResult);
                    anRequest.setOutStatus("X");
                }
            }
            else {
                remoteResult = remoteService.sendAppFinancingState(anRequest);
                if (remoteResult.isSucess()) {
                    FaceTradeResult tradeResult = (FaceTradeResult) remoteResult.getResult();
                    if ("1".equals(tradeResult.getStatus())) {
                        anRequest.setTradeStatus(tradeResult.getApplayStatus());
                    }
                    else {
                        logger.error("send request status has error \n" + anRequest);
                    }
                }
                else {
                    logger.error("send request status has error \n" + anRequest);

                    anRequest.setOutStatus("X");
                }
            }
        }
        catch (Exception ex) {

            logger.error("dealFactorAppRequest Has Error!", ex);
        }

        return anRequest;
    }
    
}