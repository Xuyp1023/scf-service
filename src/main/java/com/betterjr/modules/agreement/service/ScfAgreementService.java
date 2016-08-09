package com.betterjr.modules.agreement.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;

/****
 * 电子合同中间service
 * @author hubl
 *
 */
@Service
public class ScfAgreementService {
    private static final Logger logger = LoggerFactory.getLogger(ScfAgreementService.class);
    @Autowired
    private ScfRequestNoticeService requestNoticeService;
    @Autowired
    private ScfRequestOpinionService requestOpinionService;
    @Autowired
    private ScfRequestService requestService;
    
    /***
     * 生成通知书的静态页面成功
     * @param appNo
     * @return
     */
    public String createOutHtmlInfo(String appNo){
        ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.createOutHtmlInfo().toString();
    }
    
    /****
     * 发送并验证签署合同的验证码
     * @param appNo
     * @param custType
     * @param vCode
     * @return
     */
    public String sendValidCode(String appNo, String custType, String vCode){
        ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        boolean isSuccess = localService.saveAndSendValidSMS(appNo, vCode);
        if (isSuccess) {
            return AjaxObject.newOk("发送并验证签署合同的验证码成功", isSuccess).toJson();
        }
        else {
            return AjaxObject.newError("发送并验证签署合同的验证码失败，请稍后重试！").toJson();
        }
    }
    
    /***
     * 转让通知书
     * 
     * @return
     */
    public boolean transNotice(ScfRequestNotice anNoticeRequest){
        logger.info("转让通知书=================================："+anNoticeRequest);
        ScfRequest request = requestService.findRequestDetail(anNoticeRequest.getRequestNo());
        anNoticeRequest.fillInfo(request);
        return requestNoticeService.updateTransNotice(anNoticeRequest,request.getCustName());
    }
    
    /***
     * 买方确认意见书信息
     * @param anOpinion
     * @return
     */
    public boolean transOpinion(ScfRequestOpinion anOpinion){
        logger.info("意见确认书=================================："+anOpinion);
        ScfRequest request = requestService.findRequestDetail(anOpinion.getRequestNo());
        anOpinion.fillInfo(request);
        return requestOpinionService.updateOpinionInfo(anOpinion,request.getCustName());
    }
    
}
