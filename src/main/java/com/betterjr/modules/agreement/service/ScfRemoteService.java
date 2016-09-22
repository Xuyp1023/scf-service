package com.betterjr.modules.agreement.service;

import com.betterjr.common.annotation.AnnonRemoteMethod;
import com.betterjr.common.data.BusinClassType;
import com.betterjr.modules.agreement.entity.FaceTradeResult;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.remote.entity.RemoteResultInfo;

/***
 * 电子合同发送短信
 * @author hubl
 *
 */
public interface ScfRemoteService {
    /**
     * 
     * 融资申请
     * 
     * @param 融资申请信息
     * @return 融资申请结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "007", businClass = BusinClassType.SCF_APP, custType = "X")
    public RemoteResultInfo<ScfRequest> appFinancing(ScfRequest anRequest);
    
    /**
     * 
     * 发送订单状态信息
     * 
     * @param 订单状态信息列表
     * @return 订单状态发送结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "008", businClass = BusinClassType.SCF_STATE, custType = "X")
    public RemoteResultInfo<FaceTradeResult> sendAppFinancingState(ScfRequest anRequest);

    /**
     * 
     *  发送短信-电子合同签署
     * 
     * @param userid & orderNumber
     * @return 短信发送结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "008", businClass = BusinClassType.SCF_STATE, custType = "X")
    public RemoteResultInfo<FaceTradeResult> sendSMS(String requestNo, Long custNo);
    
    /**
     * 
     *   验证短信-电子合同签署
     * 
     * @param userid & orderNumber
     * @return 短信  验证结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "008", businClass = BusinClassType.SCF_STATE, custType = "X")
    public RemoteResultInfo<FaceTradeResult> validateSMS(String requestNo, Long custNo, String vcode);
    
    /**
     * 
     *    创建 签署-电子合同签署
     * 
     * @param 创建 签署参数
     * @return 操作结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "008", businClass = BusinClassType.SCF_STATE, custType = "X")
    public RemoteResultInfo<FaceTradeResult> createSignOrder(ScfElecAgreement anElecAgreement);

}
