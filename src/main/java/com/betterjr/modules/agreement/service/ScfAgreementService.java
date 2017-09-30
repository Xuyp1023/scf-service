package com.betterjr.modules.agreement.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;

/****
 * 电子合同中间service
 * 
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
    @Autowired
    private ScfRequestCreditService requestCreditService;
    @Autowired
    private ScfRequestProtacalService requestProtacalService;
    @Autowired
    private ScfElecAgreementService scfElecAgreementService;

    /***
     * 生成通知书的静态页面成功
     * 
     * @param appNo
     * @return
     */
    public String createOutHtmlInfo(final String appNo) {
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.createOutHtmlInfo().toString();
    }

    /***
     * 生成通知书的静态页面成功
     * 
     * @param appNo
     * @return
     */
    public String createOutHtmlInfoByRequestNo(final String anRequestNo, final String anAgreeType) {
        final String appNo = scfElecAgreementService.findElecAgreeByRequestNo(anRequestNo, anAgreeType);
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.createOutHtmlInfo().toString();
    }

    /****
     * 拒绝签署
     * 
     * @param anAppNo
     * @param anDescribe
     * @return
     */
    public boolean cancelElecAgreement(final String anAppNo, final String anDescribe) {
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(anAppNo);
        return localService.cancelElecAgreement(anDescribe);
    }

    /****
     * 拒绝签署
     * 
     * @param anAppNo
     * @param anDescribe
     * @return
     */
    public boolean cancelElecAgreement(final String anRequestNo, final String anAgreeType, final String anDescribe) {
        final String appNo = scfElecAgreementService.findElecAgreeByRequestNo(anRequestNo, anAgreeType);
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.cancelElecAgreement(anDescribe);
    }

    /****
     * 发送并验证签署合同的验证码
     * 
     * @param appNo
     * @param custType
     * @param vCode
     * @return
     */
    public String sendValidCode(final String appNo, final String custType, final String vCode) {
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        final boolean isSuccess = localService.saveAndSendValidSMS(appNo, vCode);
        if (isSuccess) {
            return AjaxObject.newOk("发送并验证签署合同的验证码成功", isSuccess).toJson();
        }
        else {
            return AjaxObject.newError("发送并验证签署合同的验证码失败，请稍后重试！").toJson();
        }
    }

    /****
     * 发送并验证签署合同的验证码
     * 
     * @param appNo
     * @param custType
     * @param vCode
     * @return
     */
    public boolean sendValidCodeByRequestNo(final String anRequestNo, final String anAgreeType, final String vCode) {
        final String appNo = scfElecAgreementService.findElecAgreeByRequestNo(anRequestNo, anAgreeType);
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.saveAndSendValidSMS(appNo, vCode);
    }

    /***
     * 转让通知书
     * 
     * @return
     */
    public String transNotice(final ScfRequestNotice anNoticeRequest) {

        logger.info("转让通知书：" + anNoticeRequest);
        if (BetterStringUtils.isBlank(anNoticeRequest.getFactorPost())) {
            anNoticeRequest.setFactorPost("000000");
        }
        BTAssert.notNull(anNoticeRequest.getBankAccount(), "银行账户不能为空");
        final ScfRequest request = requestService.findRequestDetail(anNoticeRequest.getRequestNo());
        anNoticeRequest.fillInfo(request);
        String agreeNo = "";
        try {
            agreeNo = requestNoticeService.updateTransNotice(anNoticeRequest, request.getCustName(), request.getApprovedBalance());
        }
        catch (final Exception e) {
            logger.error("转让通知书生成失败，原因：" + e.getMessage());
            throw new BytterTradeException("转让通知书生成失败");
        }
        return agreeNo;
    }

    /***
     * 买方确认意见书信息
     * 
     * @param anOpinion
     * @return
     */
    public boolean transOpinion(final ScfRequestOpinion anOpinion) {
        logger.info("意见确认书：" + anOpinion);
        final ScfRequest request = requestService.findRequestDetail(anOpinion.getRequestNo());
        anOpinion.fillInfo(request);
        boolean bool = false;
        try {
            bool = requestOpinionService.updateOpinionInfo(anOpinion, request.getCustName(), request.getApprovedBalance());
        }
        catch (final Exception e) {
            logger.error("意见确认书生成失败，原因：" + e.getMessage());
            throw new BytterTradeException("意见确认书生成失败");
        }
        return bool;
    }

    /***
     * 应收账款转让书
     */
    public boolean transCredit(final List<ScfRequestCredit> list, final String anAgreeNo) {
        logger.info("应收账款转让书：" + list);
        final ScfRequestCredit credit = Collections3.getFirst(list);
        BTAssert.notNull(credit, "应收账款明细不能为空");
        final String requestNo = credit.getRequestNo();
        final ScfRequest request = requestService.findRequestDetail(requestNo);
        // 根据申请单号查询对应的转让书编号
        final ScfRequestNotice notice = requestNoticeService.findTransNotice(requestNo);
        BTAssert.notNull(notice, "申请单：" + requestNo + "的转让通知书不存在");
        if (requestNoticeService.allowUpdate(requestNo) && requestCreditService.updateCreditList(request, notice.getNoticeNo(), list, anAgreeNo)) {
            return true;
        }
        return false;
    }

    /***
     * 三方协议签署
     * 
     * @return
     */
    public boolean transProtacal(final ScfRequestProtacal protacal) {
        logger.info("三方协议书：" + protacal);
        final ScfRequest request = requestService.findRequestDetail(protacal.getRequestNo());
        protacal.initProtacal();
        BTAssert.notNull(request, "三方协议申请单不存在");
        boolean bool = false;
        try {
            bool = requestProtacalService.updateProtacalInfo(protacal, request.getApprovedBalance());
        }
        catch (final Exception e) {
            logger.error("三方协议书生成失败，原因：" + e.getMessage());
            throw new BytterTradeException("三方协议书生成失败");
        }
        return bool;
    }

    public CustFileItem findPdfFileInfo(final String appNo) {
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        CustFileItem custFileItem = scfElecAgreementService.findPdfFileInfo(localService.getElecagree());
        if (custFileItem == null) {
            custFileItem = localService.checkPdfCreateStatus(false);
        }
        return custFileItem;
    }

    /**
     * 创建输出显示内容，带上类型信息
     * 
     * @param appNo
     * @return
     */
    public Map<String, Object> createOutHtmlInfoWithType(final String appNo, final boolean anCreate) {
        final ScfElecAgreeLocalService localService = ScfElecAgreementFactory.create(appNo);
        return localService.createOutHtmlInfoWithType(anCreate);
    }

}
