package com.betterjr.modules.agreement.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.dao.ScfRequestOpinionMapper;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.utils.SupplyChainUtil;
import com.betterjr.modules.operator.dubbo.IOperatorService;
import com.betterjr.modules.push.service.ScfSupplierPushService;

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

    @Autowired
    private ScfSupplierPushService pushService;

    @Autowired
    private CustAccountService custAccountService;

    @Reference(interfaceClass = IOperatorService.class)
    private IOperatorService operatorService;

    /**
     * 更新核心企业确认书的协议信息
     * 
     * @param anOpinion
     *            核心企业确认书信息
     * @return
     */
    public boolean updateOpinionInfo(final ScfRequestOpinion anOpinion, final String anSupplier, final BigDecimal anBalance) {
        anOpinion.setAgreeName(anSupplier + "应收账款债权转让通知确认");
        final ScfRequestOpinion tmpOpinion = this.selectByPrimaryKey(anOpinion.getRequestNo());
        final ScfRequestNotice notice = scfRequestNoticeService.selectByPrimaryKey(anOpinion.getRequestNo());
        if (notice == null) {
            logger.error("updateOpinionInfo the requestNo : " + anOpinion.getRequestNo() + ", not find NoticeInfo");
        }
        else {
            anOpinion.setNoticeNo(notice.getNoticeNo());
        }

        // 设置企业经办人信息
        final CustInfo custInfo = custAccountService.findCustInfo(anOpinion.getBuyerNo());// 查询企业基本信息
        CustOperatorInfo custOperator = operatorService.findCustClerkMan(custInfo.getOperOrg(), "1");
        if (custOperator == null) {
            custOperator = operatorService.findCustClerkMan(custInfo.getOperOrg(), "0");
        }
        anOpinion.setLinkName(custOperator.getName());
        anOpinion.setEmail(custOperator.getEmail());
        anOpinion.setPhone(custOperator.getMobileNo());
        anOpinion.setFactorName(SupplyChainUtil.findFactorNameByNo(anOpinion.getFactorNo()));

        boolean result = true;
        if (tmpOpinion == null) {
            this.insert(anOpinion);
        }
        else if ("0".equals(tmpOpinion.getOpinionStatus())) {
            this.updateByPrimaryKey(anOpinion);
        }
        else {
            logger.warn("updateOpinionInfo  the request " + anOpinion + ", has used!");
            result = false;
        }

        if (result) {
            final ScfElecAgreement elecAgreement = ScfElecAgreement.createByOpinion(anOpinion.getAgreeName(), anOpinion.getConfirmNo(), anBalance);
            elecAgreement.setSupplierNo(anOpinion.getSupplierNo());
            elecAgreement.setRequestNo(anOpinion.getRequestNo());
            elecAgreement.setBuyerNo(anOpinion.getBuyerNo());
            elecAgreement.setFactorNo(anOpinion.getFactorNo());
            elecAgreement.setSupplier(anSupplier);
            elecAgreementService.addElecAgreementInfo(elecAgreement, "buyerConfirm",
                    Arrays.asList(anOpinion.getBuyerNo(), Long.parseLong(elecAgreement.getFactorNo())));
        }

        return result;
    }

    public String queryCustName(final Long anCustNo) {
        return custAccountService.queryCustName(anCustNo);
    }
}