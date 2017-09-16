package com.betterjr.modules.agreement.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.template.entity.ScfContractTemplate;

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

    protected DataStoreService dataStoreService;

    private final String WOSIGN_SIGN_FILE = "signFile";
    private final String SIGN_TEMPLATE_FILE = "signTemplateFile";

    protected void init(final ScfElecAgreementService anElecAgreeService, final ScfElecAgreement anElecAgree) {
        this.elecAgree = anElecAgree;
        this.elecAgreeService = anElecAgreeService;

        this.freeMarkerService = SpringContextHolder.getBean(FreemarkerService.class);
        this.dataStoreService = SpringContextHolder.getBean(DataStoreService.class);
        subInit();
    }

    public ScfElecAgreement getElecagree() {
        return elecAgree;
    }

    protected void putService(final ScfElecAgreementService anElecAgreeService) {

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
    public CustFileItem checkPdfCreateStatus(final boolean anSave) {
        CustFileItem fileItem = null;
        if (MathExtend.smallValue(this.elecAgree.getBatchNo())) {
            final StringBuffer buffer = createOutHtmlInfo();
            final ByteArrayOutputStream bOut = new ByteArrayOutputStream(1024 * 1024);
            CustFileUtils.exportPDF(buffer, bOut);
            fileItem = this.dataStoreService.saveStreamToStoreWithBatchNo(new ByteArrayInputStream(bOut.toByteArray()), WOSIGN_SIGN_FILE,
                    this.elecAgree.getAgreeName().concat(".pdf"));
            if (fileItem != null && anSave) {
                elecAgreeService.saveSignFileInfo(this.elecAgree, fileItem);
            }
        }
        else if (MathExtend.smallValue(this.elecAgree.getSignBatchNo()) == false) {
            fileItem = new CustFileItem();
        }

        return fileItem;
    }

    // 发送短信验证码，如果成功，则保存待签署的文件。
    public boolean saveAndSendValidSMS(final String anAppNo, final String anVcode) {
        final CustFileItem fileItem = checkPdfCreateStatus(false);
        if (fileItem == null) {

            throw new BytterTradeException(50000, "创建签名用的PDF失败，请检查！");
        }

        return elecAgreeService.saveAndSendValidSMS(elecAgree, fileItem, anAppNo, anVcode);
    }

    public Page<ScfElecAgreementInfo> findScfElecAgreementList(final Map<String, Object> anParam, final int anPageNum, final int anPageSize) {

        return elecAgreeService.queryScfElecAgreementList(anParam, anPageNum, anPageSize);
    }

    public boolean saveAndSendSMS(final String anAppNo) {
        final boolean isok = elecAgreeService.saveAndSendSMS(anAppNo);

        return isok;
    }

    /**
     * 输出HTML文件给前端预览
     * 
     * @return
     */
    public StringBuffer createOutHtmlInfo() {
        final Map<String, Object> param = findViewModeData();

        // 使用保理公司自定义模板
        if (null != param.get("template")) {
            logger.info("生成合同---使用保理公司自定义模板");
            final ScfContractTemplate template = (ScfContractTemplate) param.get("template");
            final InputStream is = dataStoreService.loadFromStoreByBatchNo(template.getBatchNo());
            return freeMarkerService.processTemplateByFactory(getViewModeFile() + "_" + template.getFactorNo(), param, "supplychain", is);
        }

        // 使用系统模板
        logger.info("生成合同---使用系统默认模板");
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
     * 
     * @param anDescribe
     * @return
     */
    public abstract boolean cancelElecAgreement(String anDescribe);
}