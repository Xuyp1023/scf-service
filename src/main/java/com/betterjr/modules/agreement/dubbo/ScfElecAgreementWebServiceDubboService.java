package com.betterjr.modules.agreement.dubbo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.modules.agreement.IScfElecAgreementService;
import com.betterjr.modules.agreement.IScfElecAgreementWebService;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.document.data.DownloadFileInfo;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.remote.entity.SignRequestInfo;
import com.betterjr.modules.rule.annotation.AnnotRuleFunc;
import com.betterjr.modules.rule.entity.RuleFuncType;

/**
 * 电子合同webservice服务
 * 
 * @author zhoucy
 *
 */
@Service(interfaceClass = IScfElecAgreementWebService.class)

public class ScfElecAgreementWebServiceDubboService implements IScfElecAgreementWebService {
    private static Logger logger = LoggerFactory.getLogger(ScfElecAgreementWebServiceDubboService.class);

    @Reference(interfaceClass = IScfRequestService.class)
    private IScfRequestService requestService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService factorRelService;

    @Reference(interfaceClass = IScfElecAgreementService.class)
    private IScfElecAgreementService elecAgreementService;

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private static final String WOSIGNED_FILE_TYPE = "signedFile";

    /**
     * 沃通推送文件接口
     */

    public String pushSignedDoc(SignRequestInfo input) {
        logger.warn("service input:" + JsonMapper.toNonNullJson(input));
        ScfElecAgreement elecAgreement = this.elecAgreementService.findOneElecAgreement(input.getRequestNo());
        boolean workStatus = false;
        if (elecAgreement != null) {
            workStatus = "1".equals(input.getStatus());
            if (workStatus) {
                InputStream inData = new ByteArrayInputStream(Base64.decodeBase64(input.getSignedContent()));
                CustFileItem fileItem = dataStoreService.saveStreamToStore(inData, WOSIGNED_FILE_TYPE, elecAgreement.getAgreeName().concat(".pdf"));
                if (fileItem != null) {
                    this.elecAgreementService.saveSignedFile(input.getRequestNo(), fileItem);
                }
            }
            else {
                this.elecAgreementService.saveElecAgreementStatus(input.getRequestNo(), "9");
            }

            // 如果是核心企业确认信息，则发送确认状态给到保理公司
            String outStatus = null;
            if ("1".equals(elecAgreement.getAgreeType())) {

                outStatus = workStatus ? "3" : "4";
            }
            else if ("0".equals(elecAgreement.getAgreeType())) {

                outStatus = workStatus ? "5" : "6";
            }
            requestService.updateAndSendRequestStatus(elecAgreement.getRequestNo(), outStatus);
            return "1";
        }
        return "0";
    }

    /**
     * 沃通推送身份验证结果
     */

    public String pushValidation(SignRequestInfo anInput) {
        logger.debug("service input:" + JsonMapper.toNonNullJson(anInput));
        String tmpStatus = anInput.getStatus();
        if ("1".equals(tmpStatus)){
           tmpStatus = "3"; 
        }
        else{
            tmpStatus = "4";
        }
        if (this.factorRelService.saveFactorRelationStatus(Long.parseLong(anInput.getCustNo()), anInput.getCustNo(), tmpStatus, "wos")) {
            return "1";
        }
        else {
            return "0";
        }
    }
 
}
