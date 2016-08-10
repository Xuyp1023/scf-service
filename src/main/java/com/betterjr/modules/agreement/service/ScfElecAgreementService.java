package com.betterjr.modules.agreement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.WebServiceErrorCode;
import com.betterjr.common.exception.BytterWebServiceException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.dao.ScfElecAgreementMapper;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;

/***
 * 电子合同管理
 * @author hubl
 *
 */
@Service
public class ScfElecAgreementService extends BaseService<ScfElecAgreementMapper, ScfElecAgreement>{
    
    @Autowired
    private CustAccountService custAccoService;
    @Autowired
    private ScfElecAgreeStubService scfElecAgreeStubService;
    @Reference(interfaceClass=ICustFileService.class)
    private ICustFileService custFileService;
    
    /**
     * 查找供应商的电子合同信息
     * 
     * @param anGTEsignDate
     *            起始日期
     * @param anLTEsignDate
     *            截止日期
     * @param anSignStatus
     *            签约状态
     * @param anPageNum
     *            页码
     * @param anPageSize
     *            每页大小
     * @param anCountFlag
     *            是否需要统计
     * @return
     */
    public Page<ScfElecAgreementInfo> queryScfElecAgreementList(String anAgreeType, Map<String, Object> anParam, int anPageNum, int anPageSize) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("GTEregDate", anParam.get("GTEregDate"));
        termMap.put("LTEregDate", anParam.get("LTEregDate"));
        String signStatus = (String) anParam.get("signStatus");
        anPageSize = MathExtend.defIntBetween(anPageSize, 2, ParamNames.MAX_PAGE_SIZE, 25);
        if (BetterStringUtils.isBlank(signStatus)) {
            termMap.put("signStatus", Arrays.asList("0", "2", "3"));
        }
        else {
            termMap.put("signStatus", signStatus);
        }
        termMap.put("agreeType", anAgreeType);
        List<Long> custNoList = UserUtils.findCustNoList();
        if (Collections3.isEmpty(custNoList)) {
            custNoList = new ArrayList<Long>();
            // throw new BytterValidException("没有找到操作员相关的客户资料，请重新登陆！");
        }
        if ("0".equals(anAgreeType)) {
            termMap.put("supplierNo", custNoList);
        }
        else if ("1".equals(anAgreeType)) {
            termMap.put("buyerNo", anParam.get("custNo"));
        }
        
        if (logger.isInfoEnabled()){
            logger.info("this is findScfElecAgreementList params " + termMap);
        }
        Page<ScfElecAgreementInfo> elecAgreeList = this.selectPropertyByPage(ScfElecAgreementInfo.class, termMap, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")));
        Set<Long> custNoSet = new HashSet(custNoList);
        for (ScfElecAgreementInfo elecAgree : elecAgreeList) {
            elecAgree.putStubInfos(scfElecAgreeStubService.findSignerList(elecAgree.getAppNo(),custAccoService), custNoSet);
            elecAgree.setBuyer(custAccoService.queryCustName(elecAgree.getBuyerNo()));
            elecAgree.setFactorName(DictUtils.getDictLabel("ScfAgencyGroup", elecAgree.getFactorNo()));
        }
        logger.info("this is findScfElecAgreementList result count :" + elecAgreeList.size());
        return elecAgreeList;
    }
    
    
    /**
     * 根据融资申请订单号和电子合同类型，获得不同合同信息
     * 
     * @param anRequestNo
     *            融资申请业务订单号
     * @param anSignType
     *            电子合同类型
     * @return
     */
    public ScfElecAgreement findOneElecAgreeByOrderNo(String anRequestNo, String anSignType) {
        Map workCondition = new HashMap();
        workCondition.put("agreeType", anSignType);
        workCondition.put("requestNo", anRequestNo);
        List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        ScfElecAgreement tmpElecAgree = null;
        if (Collections3.isEmpty(elecAgreeList) == false) {
            tmpElecAgree = Collections3.getFirst(elecAgreeList);
        }

        return tmpElecAgree;
    }
    
    public boolean saveSignFileInfo(String anAppNo, CustFileItem anFileItem, boolean anSignedFile) {
        ScfElecAgreement tmpElecAgree = this.selectByPrimaryKey(anAppNo);
        return saveSignFileInfo(tmpElecAgree, anFileItem, anSignedFile);
    }
    
    public boolean saveSignFileInfo(ScfElecAgreement tmpElecAgree, CustFileItem anFileItem, boolean anSignedFile) {
        if (tmpElecAgree != null) {
            String tmpStatus = null;
            if (anSignedFile) {
                tmpElecAgree.setSignBatchNo(anFileItem.getBatchNo());
                tmpStatus = "1";
            }
            else {
                tmpElecAgree.setBatchNo(anFileItem.getBatchNo());
                tmpStatus = "2";
            }
            tmpElecAgree.fillElecAgreeStatus(tmpStatus);
            this.updateByPrimaryKey(tmpElecAgree);
            custFileService.webSaveAndUpdateFileItem(anFileItem.getFilePath(),anFileItem.getFileLength(),anFileItem.getFileInfoType(),anFileItem.getFileName());
        }

        return true;
    }
    
    /**
     * 发送短信验证码
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @param anVcode
     *            沃通发出的验证码
     * @return
     */
    public boolean saveAndSendValidSMS(ScfElecAgreement anElecAgree, CustFileItem fileItem, String anAppNo, String anVcode) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0) {
            logger.error("saveAndSendValidSMS not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
//        boolean result = this.remoteHelper.sendValidSMS(anAppNo, custNo, anVcode);
        boolean result=false;
        this.scfElecAgreeStubService.saveElecAgreeStubStatus(custNo, anAppNo, result ? "1" : "2");
        if  (result){
            saveSignFileInfo(anElecAgree, fileItem, false);
        }

        return result;
    }
    
    /**
     * 获取短信验证码
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return boolean 获得验证码成功，返回true,否则返回false;
     */
    public boolean saveAndSendSMS(String anAppNo) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("sendSMS not find signed parnter use with anAppNo " + anAppNo);

            return false;
        }
        ScfElecAgreement electAgreement = this.selectByPrimaryKey(anAppNo);
        if (electAgreement.hasSendSignOrder()) {
            if (this.saveAndSignElecAgreement(electAgreement) == false) {
                logger.error("SendSignOrder find Error ");
                return false;
            }
        }
        else {
            logger.info("this electAgreement appNo" + anAppNo + ", has sended!");
        }

//        return this.remoteHelper.sendSMS(anAppNo, custNo);
        return false;
    }
    
    /**
     * 发送给沃通做电子签名
     * 
     * @param anAppNo
     *            电子合同流水号对应C_APPLICATIONNO
     * @return
     */
    protected boolean saveAndSignElecAgreement(ScfElecAgreement anElectAgreement) {
        logger.info("create saveAndSignElecAgreement " + anElectAgreement);
//        boolean result = this.remoteHelper.signElecAgreement(anElectAgreement);
        boolean result=false;
        if (result) {

            this.updateByPrimaryKey(anElectAgreement);
        }

        return result;
    }
    
    /**
     * 取消或作废电子合同
     * 
     * @param anAppNo
     * @return
     */
    public boolean checkCancelElecAgreement(String anAppNo) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                logger.info("permit cancel this agreement " + anAppNo );
                return true;
            }
        }
        return false;
    }
    
    /**
     * 取消或作废电子合同
     * 
     * @param anAppNo
     * @return
     */
    public boolean saveAndCancelElecAgreement(String anAppNo) {
        Long custNo = this.scfElecAgreeStubService.findContractCustNo(anAppNo);
        if (custNo <= 0L) {
            logger.error("saveAndCancelElecAgreement not find signed parnter use with anAppNo " + anAppNo);
            return false;
        }
        ScfElecAgreement elecAgreement = this.selectByPrimaryKey(anAppNo);
        if (elecAgreement != null) {

            // 未签署的协议才可以作废
            if ("1".equalsIgnoreCase(elecAgreement.getSignStatus()) == false) {
                elecAgreement.fillElecAgreeStatus("9");
                this.updateByPrimaryKey(elecAgreement);
                this.scfElecAgreeStubService.saveElecAgreeStubStatus(custNo, anAppNo, "9");
                return true;
            }
        }
        return false;
    }
    
    /**
     * 增加电子合同信息
     * 
     * @param anElecAgree
     *            电子合同信息
     * @param anCustList
     *            电子合同的签约方信息
     */
    public void addElecAgreementInfo(ScfElecAgreement anElecAgree, List<Long> anCustNoList) {
        Map workCondition = new HashMap();
        workCondition.put("agreeType", anElecAgree.getAgreeType());
        workCondition.put("requestNo", anElecAgree.getRequestNo());
        List<ScfElecAgreement> elecAgreeList = this.selectByProperty(workCondition);
        boolean result = true;
        if (Collections3.isEmpty(anCustNoList)) {
            throw new BytterWebServiceException(WebServiceErrorCode.E1010);
        }
        if (Collections3.isEmpty(elecAgreeList)) {
            this.insert(anElecAgree);
        }
        else {
            ScfElecAgreement tmpElecAgree = Collections3.getFirst(elecAgreeList);
            // 只能更新未处理的合同协议
            if ("0".equals(tmpElecAgree.getSignStatus())) {
                tmpElecAgree.updateInfo(anElecAgree);
                this.updateByPrimaryKey(tmpElecAgree);
            }
            else {
                result = false;
            }
        }
        if (result) {
            this.scfElecAgreeStubService.saveScfElecAgreeStub(anElecAgree.getAppNo(), anCustNoList);
        }
        else {
            throw new BytterWebServiceException(WebServiceErrorCode.E1008);
        }
    }

}