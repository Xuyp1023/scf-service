package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.entity.ScfRequestCredit;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;

/**
 * 融资合同的转让协议书管理
 * 
 * @author zhoucy
 *
 */
@Service("scfElecNoticeLocalService")
@Scope("singleton")
public class ScfElecNoticeLocalService extends ScfElecAgreeLocalService {
    private static final Logger logger = LoggerFactory.getLogger(ScfElecNoticeLocalService.class);
    private ScfRequestNoticeService requestNoticeService = null;
    private ScfRequestService requestService = null;
    private ScfRequestCreditService requestCreditService;

    protected void subInit() {
        this.requestNoticeService = SpringContextHolder.getBean(ScfRequestNoticeService.class);
        this.requestService = SpringContextHolder.getBean(ScfRequestService.class);
    }

    @Override
    public Map<String, Object> findViewModeData() {
        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        ScfRequestNotice noticeInfo = requestNoticeService.selectByPrimaryKey(requestNo);
        if (null == noticeInfo) {
            logger.error("Can't get notice information with request no:" + requestNo);
            throw new BytterTradeException(40001, "无法获取通知书信息");
        }
        result.put("noticeInfo", noticeInfo);
        List<ScfRequestCredit> reqCredits = requestCreditService.findByRequestNo(requestNo);
        // 获取应收账款信息
        if (Collections3.isEmpty(reqCredits)) {
            logger.error("Can't get credit information with request no:" + requestNo);
            throw new BytterTradeException(40001, "无法获取通知书中的应收账款信息");
        }
        result.put("creditInfos", reqCredits);
        result.put("signDate", findSignDate());

        return result;
    }

    @Override
    protected String getViewModeFile() {

        return "billTransNotice";
    }

    @Override
    public boolean cancelElecAgreement() {
        logger.info("will cancel appNo is " + this.elecAgree.getAppNo());
        if (elecAgreeService.checkCancelElecAgreement(this.elecAgree.getAppNo())) {
            ScfRequest tmpReq = this.requestService.selectByPrimaryKey(elecAgree.getRequestNo());
//            tmpReq.cancelRequestValue("供应商取消转让协议！", "6");
//            tmpReq = this.remoteHelper.sendRequestStatus(tmpReq);
            if ("6".equalsIgnoreCase(tmpReq.getTradeStatus())) {
                this.requestService.addRequest(tmpReq);
                return this.elecAgreeService.saveAndCancelElecAgreement(this.elecAgree.getAppNo());
            }
            else {
                return false;
            }
        }
        return false;
    }

}