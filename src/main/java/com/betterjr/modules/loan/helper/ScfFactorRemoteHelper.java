package com.betterjr.modules.loan.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.agreement.entity.FaceTradeResult;
import com.betterjr.modules.agreement.service.ScfRemoteService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.remote.entity.RemoteResultInfo;
import com.betterjr.modules.remote.utils.RemoteInvokeUtils;

/**
 * 提供调用保理和沃通的远程接口辅助工具类。
 * 
 * @author zhoucy
 *
 */
public class ScfFactorRemoteHelper {
    private static final Logger logger = LoggerFactory.getLogger(ScfFactorRemoteHelper.class);


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
        String faceNo=anRequest.getFactorNo().toString();
        ScfRemoteService remoteService = RemoteInvokeUtils.createService(faceNo, ScfRemoteService.class);
        @SuppressWarnings("rawtypes")
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
