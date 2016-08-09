package com.betterjr.modules.agreement.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.modules.agreement.dao.ScfRequestOpinionMapper;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.utils.SupplyChainUtil;

/**
 * 核心企业应收账款确认书管理
 * 
 * @author zhoucy
 *
 */
@Service
public class ScfRequestOpinionService extends BaseService<ScfRequestOpinionMapper, ScfRequestOpinion> {

    private static final Logger logger = LoggerFactory.getLogger(ScfRequestOpinionService.class);
    @Autowired
    private FreemarkerService markerService;

    @Autowired
    private ScfRequestNoticeService scfRequestNoticeService;

    @Autowired
    private ScfElecAgreementService elecAgreementService;

    /**
     * 更新核心企业确认书的协议信息
     * 
     * @param anOpinion
     *            核心企业确认书信息
     * @return
     */
    public boolean updateOpinionInfo(ScfRequestOpinion anOpinion, String anSupplier) {
        anOpinion.setAgreeName(anSupplier + "买方确认意见");
        ScfRequestOpinion tmpOpinion = this.selectByPrimaryKey(anOpinion.getRequestNo());
        ScfRequestNotice notice = scfRequestNoticeService.selectByPrimaryKey(anOpinion.getRequestNo());
        if (notice == null) {
            logger.error("updateOpinionInfo the requestNo : " + anOpinion.getRequestNo() + ", not find NoticeInfo");
        }
        else {
            anOpinion.setNoticeNo(notice.getNoticeNo());
        }

        boolean result = true;
        if (tmpOpinion == null) {
            anOpinion.setFactorName(SupplyChainUtil.findFactorNameByNo(anOpinion.getFactorNo()));
            this.insert(anOpinion);
        }
        else if ("0".equals(tmpOpinion.getOpinionStatus())) {
            anOpinion.setFactorName(tmpOpinion.getFactorName());
            this.updateByPrimaryKey(anOpinion);
        }
        else {
            logger.warn("updateOpinionInfo  the request " + anOpinion + ", has used!");
            result = false;
        }

        if (result) {
            ScfElecAgreement elecAgreement = ScfElecAgreement.createByOpinion(anOpinion.getAgreeName(), anOpinion.getConfirmNo(),
                    BigDecimal.ZERO);
            elecAgreement.setSupplierNo(anOpinion.getSupplierNo());
            elecAgreement.setRequestNo(anOpinion.getRequestNo());
            elecAgreement.setBuyerNo(anOpinion.getBuyerNo());
            elecAgreement.setFactorNo(anOpinion.getFactorNo());
            elecAgreement.setSupplier(anSupplier);
            elecAgreementService.addElecAgreementInfo(elecAgreement, Arrays.asList(anOpinion.getBuyerNo()));
        }

        return result;
    }
}