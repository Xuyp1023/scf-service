package com.betterjr.modules.agreement.service;

import java.util.List;
import java.util.Map;

import com.betterjr.common.annotation.AnnonRemoteMethod;
import com.betterjr.common.data.BusinClassType;
import com.betterjr.modules.account.entity.SaleAccoRequestInfo;
import com.betterjr.modules.agreement.entity.FaceTradeResult;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.product.entity.ScfProduct;
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

    /**
     * 
     * 查询保理产品信息
     * 
     * @param 查询条件
     *            ，使用Map作为入参
     * @return 查询结果通知，返回是一个列表
     * @throws AccessException
     *             抛出访问异常信息
     */
    public RemoteResultInfo<List<ScfProduct>> queryFactorProduct(Map anMap);
    
    /**
     * 
     * 查询机构客户资料
     * 
     * @param 查询条件
     *            ，使用Map作为入参
     * @return 查询结果通知，返回是一个列表
     * @throws AccessException
     *             抛出访问异常信息
     */
    public RemoteResultInfo<FaceTradeResult> queryMechAcco(CustRelation anMap);

    /**
     * 
     * 机构开户
     * 
     * @param 开户申请信息
     * @return 开户结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "01", businClass = BusinClassType.SALE_ACCOUNT, custType = "0")
    public RemoteResultInfo<FaceTradeResult> mechOpenAcco(SaleAccoRequestInfo anAccoRequest);
 
    /**
     * 
     * 修改机构客户资料
     * 
     * @param 申请信息
     * @return 修改结果通知
     * @throws AccessException
     *             抛出访问异常信息
     */
    @AnnonRemoteMethod(businFlag = "03", businClass = BusinClassType.SALE_ACCOUNT, custType = "0")
    public RemoteResultInfo<FaceTradeResult> mechModifyAcco(SaleAccoRequestInfo anAccoRequest);

}
