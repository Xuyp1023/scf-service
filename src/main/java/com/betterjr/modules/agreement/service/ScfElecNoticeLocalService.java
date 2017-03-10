package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.template.service.ScfContractTemplateService;

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
    private ScfContractTemplateService contractTemplateService = null;

    protected void subInit() {
        this.requestNoticeService = SpringContextHolder.getBean(ScfRequestNoticeService.class);
        this.requestService = SpringContextHolder.getBean(ScfRequestService.class);
        this.requestCreditService = SpringContextHolder.getBean(ScfRequestCreditService.class);
        this.contractTemplateService = SpringContextHolder.getBean(ScfContractTemplateService.class);
    }

    @Override
    public Map<String, Object> findViewModeData() {
        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        logger.info("notice requestNo:"+requestNo);
        ScfRequestNotice noticeInfo = requestNoticeService.selectByPrimaryKey(requestNo);
        if (null == noticeInfo) {
            logger.error("Can't get notice information with request no:" + requestNo);
            throw new BytterTradeException(40001, "无法获取通知书信息");
        }
        noticeInfo=requestNoticeService.findFactorAgreement(noticeInfo);
        result.put("noticeInfo", noticeInfo);
        Map<String, Object> reqCredits = requestCreditService.findByRequestNo(requestNo);
        // 获取应收账款信息
        if (Collections3.isEmpty(reqCredits)) {
            logger.error("Can't get credit information with request no:" + requestNo);
            throw new BytterTradeException(40001, "无法获取通知书中的应收账款信息");
        }
        result.put("creditInfos", reqCredits);
        result.put("signDate", findSignDate());
        
        // 查询发票信息
        Map<String, Object> billMap=requestNoticeService.findBillListByRequestNo(requestNo);
        logger.info("notice billMap:"+billMap);
        result.put("billMap", billMap);

        //获取保理公司自定义模板
        result.put("template", requestService.getFactoryContactTemplate(requestNo, getViewModeFile()));
        
        return result;
    }

    @Override
    protected String getViewModeFile() {

        return "billTransNotice";
    }

    @Override
    public boolean cancelElecAgreement(String anDescribe) {
        logger.info("will cancel appNo is " + this.elecAgree.getAppNo());
        if (elecAgreeService.checkCancelElecAgreement(this.elecAgree.getAppNo())) {
            return this.elecAgreeService.saveAndCancelElecAgreement(this.elecAgree.getAppNo(),anDescribe);
        }
        return false;
    }

}