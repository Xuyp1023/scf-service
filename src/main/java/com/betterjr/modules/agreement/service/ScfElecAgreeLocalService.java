package com.betterjr.modules.agreement.service;

import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.sys.service.SysConfigService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 融资合同管理，实现电子合同的签署和拒绝等操作，在服务的WEB端
 * 
 * @author zhoucy
 *
 */
public abstract class ScfElecAgreeLocalService {
    private static final Logger logger = LoggerFactory.getLogger(ScfElecAgreeLocalService.class);
    protected ScfElecAgreementService elecAgreeService;

    protected ScfElecAgreement elecAgree;
    protected FreemarkerService freeMarkerService;
    protected ScfFactorRemoteHelper remoteHelper;

    protected DataStoreService dataStoreService;

    private final String WOSIGN_SIGN_FILE = "signFile";

    protected void init(ScfElecAgreementService anElecAgreeService, ScfElecAgreement anElecAgree) {
        this.elecAgree = anElecAgree;
        this.elecAgreeService = anElecAgreeService;
        this.freeMarkerService = SpringContextHolder.getBean(FreemarkerService.class);
        this.remoteHelper = SpringContextHolder.getBean(ScfFactorRemoteHelper.class);
        this.dataStoreService = SpringContextHolder.getBean(DataStoreService.class);
        subInit();
    }

    public ScfElecAgreement getElecagree() {
        return elecAgree;
    }

    protected void putService(ScfElecAgreementService anElecAgreeService) {

        this.elecAgreeService = anElecAgreeService;
    }

    /**
     * 子类的初始化
     */
    protected abstract void subInit();

    /**
     * 根据上下文的电子合同信息，输出创建PDF和前端页面的数据。
     * 
     * @return
     */
    protected abstract Map<String, Object> findViewModeData();

    // 获得合同签签署日期，如果已经签署，从合同签署信息中取，如果未签署，则取当前日期
    protected String findSignDate() {
        String tmpDate = elecAgree.getSignDate();
        if (BetterStringUtils.isBlank(tmpDate)) {
            tmpDate = BetterDateUtils.getNumDate();
        }
        return BetterDateUtils.formatDispay(tmpDate);
    }

    /**
     * 检查PDF文件是否创建
     * 
     * @return
     */
    public CustFileItem checkPdfCreateStatus(boolean anSave) {
        CustFileItem fileItem = null;
        if (MathExtend.smallValue(this.elecAgree.getBatchNo())) {
            StringBuffer buffer = createOutHtmlInfo();
            ByteArrayOutputStream bOut = new ByteArrayOutputStream(1024 * 1024);
            CustFileUtils.exportPDF(buffer, bOut);
            fileItem = this.dataStoreService.saveStreamToStoreWithBatchNo(new ByteArrayInputStream(bOut.toByteArray()), WOSIGN_SIGN_FILE,
                    this.elecAgree.getAgreeName().concat(".pdf"));
            if (fileItem != null) {
                elecAgreeService.saveSignFileInfo(this.elecAgree, fileItem);
            }
        }

        return fileItem;
    }

    // 发送短信验证码，如果成功，则保存待签署的文件。
    public boolean saveAndSendValidSMS(String anAppNo, String anVcode) {
        CustFileItem fileItem = checkPdfCreateStatus(false);
        if (fileItem == null) {

            throw new BytterTradeException(50000, "创建签名用的PDF失败，请检查！");
        }

        return elecAgreeService.saveAndSendValidSMS(elecAgree, fileItem, anAppNo, anVcode);
    }

    public Page<ScfElecAgreementInfo> findScfElecAgreementList(Map<String, Object> anParam, int anPageNum, int anPageSize) {
     
        return elecAgreeService.queryScfElecAgreementList(anParam, anPageNum, anPageSize);
    }
    
    public boolean saveAndSendSMS(String anAppNo) {
        boolean isok = elecAgreeService.saveAndSendSMS(anAppNo);

        return isok;
    }

    /**
     * 输出HTML文件给前端预览
     * 
     * @return
     */
    public StringBuffer createOutHtmlInfo() {
        Map<String, Object> param = findViewModeData();

        return freeMarkerService.processTemplateByFileNameUnderModule(getViewModeFile(), param, "supplychain");
    }
    

    /**
     * 获得输出的模板文件
     * 
     * @return
     */
    protected abstract String getViewModeFile();

    /***
     * 拒绝的原因
     * @param anDescribe
     * @return
     */
    public abstract boolean cancelElecAgreement(String anDescribe);
}