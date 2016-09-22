package com.betterjr.modules.remote.dubbo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.modules.agreement.IScfElecAgreementService;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.data.DownloadFileInfo;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.utils.CustFileClientUtils;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.document.utils.DownloadFileService;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.remote.entity.SignRequestInfo;
import com.betterjr.modules.rule.annotation.AnnotRuleFunc;
import com.betterjr.modules.rule.entity.RuleFuncType;


/**
 * 供应链融资业务相关接口提供
 * 
 * @author zhoucy
 *
 */
@Service("scfWebService")
@AnnotRuleService("scfWebService")
public class ScfWebServiceDubboService implements IScfWebService{
    private static Logger logger = LoggerFactory.getLogger(ScfWebServiceDubboService.class);
    private static final String TRADE_FALSE = "0";
    private static final String TRADE_TRUE = "1";
    @Autowired
    private IScfRequestService requestService;

    @Autowired
    private ICustRelationService factorRelService;

    @Autowired
    private IAgencyAuthFileGroupService authFileGroupService;

    @Autowired
    private ICustFileService fileService;

    @Autowired
    private IScfElecAgreementService elecAgreementService;

    /**
     * 对应接口：queryDownloadList 供应链融资业务，查询下载的文件列表
     * 
     * @param anMap
     *            需要查询的条件，客户号，在保理的客户号和需要下载的文件类型必须
     * @return
     */
    @AnnotRuleFunc(name = "queryDownloadList", fundType = RuleFuncType.OBJECT)
    public List<DownloadFileInfo> queryDownloadList(Long anCustNo, String anScfId, String anRequestNo, String anBusinType, String anFactorNo) {
        List<Long> fileBatchList = null;
        List<String> fileTypeBusinList = authFileGroupService.composeList(anFactorNo, anBusinType);
        if (BetterStringUtils.isBlank(anRequestNo)) {
            CustRelation factorRel = factorRelService.findOneRelation(anCustNo, Long.parseLong(anFactorNo), anScfId);
            logger.info(factorRel.toString());
            fileBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList);
        }
        else {
            if ("wos".equalsIgnoreCase(anFactorNo)) {
                fileBatchList = elecAgreementService.findBatchNo(anRequestNo);
                if (anCustNo == null){
                    anCustNo =102200334L;
                    anScfId = "102200334";
                }
            }
            else if("cfgy".equalsIgnoreCase(anFactorNo)){
                List<String> fileTypeBusinList02=null;
                CustRelation factorRel = factorRelService.findOneRelation(anCustNo, Long.parseLong(anFactorNo), anScfId);
                fileTypeBusinList = authFileGroupService.composeList(anFactorNo, "01");// 这里是财富共盈处理特殊方式，01代表除发票，合同，票据的其它所有文件
                fileBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList);
                List<Long> billBatchList = requestService.findVoucherBatchNo(anRequestNo); // 根据申请单查询发票，合同，票据附件
                if(billBatchList==null || billBatchList.size()<=0){
                    fileTypeBusinList02 = authFileGroupService.composeList(anFactorNo, "02");
                    billBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList02);
                }else{
                    fileTypeBusinList02 = authFileGroupService.composeList(anFactorNo, "02");
                }
                if(billBatchList!=null){
                    fileBatchList.addAll(billBatchList);
                    fileTypeBusinList.addAll(fileTypeBusinList02);
                }
            }
            else {
                fileBatchList = requestService.findVoucherBatchNo(anRequestNo);
                fileBatchList.addAll(elecAgreementService.findSignedBatchNo(anRequestNo));
            }
        }
        List<DownloadFileInfo> resultList = new ArrayList<DownloadFileInfo>();
        DownloadFileInfo resultFile;
        for (CustFileItem fileItem : fileService.findCustFilesByBatch(fileBatchList, fileTypeBusinList)) {
            logger.info("需要下载的文件是："+fileItem);
            resultFile = CustFileUtils.sendToDownloadFileService(fileItem, anCustNo, anScfId, anBusinType);
            resultList.add(resultFile);
        }

        return resultList;
    }
    
    /**
     * 对应接口：downloadFile 供应链融资业务，需要下载文件的信息，需要在control层做转换
     * 
     * @param anMap
     *            需要查询的条件，文件编号，客户号，在保理的客户号和需要下载的文件类型必须
     * @return
     */
    public DownloadFileInfo downloadFileInfo(String anToken) {

        return DownloadFileService.exactDownloadFile(anToken);
    }
    

    /**
     * 沃通推送文件接口
     */
    @AnnotRuleFunc(name = "PushSignedDoc", fundType = RuleFuncType.OBJECT)
    public String pushSignedDoc(SignRequestInfo input) {
        logger.warn("service input:" + JsonMapper.toNonNullJson(input));
        ScfElecAgreement elecAgreement = this.elecAgreementService.findOneElecAgreement(input.getRequestNo());
        boolean workStatus = false;
        if (elecAgreement != null) {
            workStatus = "1".equals(input.getStatus());
            if (workStatus) {
                KeyAndValueObject tmpFileInfo = FileUtils.findFilePathWithParent(ParamNames.CONTRACT_PATH);
                if (CustFileClientUtils.saveFileStream(tmpFileInfo, new ByteArrayInputStream(Base64.decodeBase64(input.getSignedContent())))) {
                    CustFileItem fileItem = CustFileUtils.createSignDocFileItem(tmpFileInfo, "signedFile",
                            elecAgreement.getAgreeName().concat(".pdf"));
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
    @AnnotRuleFunc(name = "PushValidation", fundType = RuleFuncType.OBJECT)
    public String pushValidation(SignRequestInfo anInput) {
        logger.debug("service input:" + JsonMapper.toNonNullJson(anInput));

        if (this.factorRelService.saveFactorRelationStatus(Long.parseLong(anInput.getCustNo()), anInput.getCustNo(), anInput.getStatus(), "wos")) {
            return "1";
        }
        else {
            return "0";
        }
    }
    
}
