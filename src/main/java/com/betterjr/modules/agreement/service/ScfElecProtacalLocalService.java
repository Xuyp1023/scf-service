package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;

/***
 * 三方协议书
 * @author hubl
 *
 */
@Service("scfElecProtacalLocalService")
@Scope("singleton")
public class ScfElecProtacalLocalService  extends ScfElecAgreeLocalService {
    private static final Logger logger = LoggerFactory.getLogger(ScfElecProtacalLocalService.class);

    private ScfRequestProtacalService requestProtacalService = null;
    private ScfRequestService requestService  = null;

    protected void subInit(){
       this.requestProtacalService = SpringContextHolder.getBean(ScfRequestProtacalService.class);
       this.requestService = SpringContextHolder.getBean(ScfRequestService.class);
    }

    @Override
    protected Map<String, Object> findViewModeData() {
        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        ScfRequestProtacal protacalInfo = requestProtacalService.selectByPrimaryKey(requestNo);
        if(null == protacalInfo) {
            logger.error("Can't get protacal information with request no:"+requestNo);
            throw new BytterTradeException(40001, "无法获取三方协议信息");
        }
        result.put("protacalInfo", protacalInfo);
        //取当前服务器日期作为签署日期
        result.put("signDate", findSignDate());

        return result;
    }

    @Override
    protected String getViewModeFile() {

        return "protacal";
    }

    @Override
    public boolean cancelElecAgreement(String anDescribe) {
        if (elecAgreeService.checkCancelElecAgreement(this.elecAgree.getAppNo())){
             return this.elecAgreeService.saveAndCancelElecAgreement(this.elecAgree.getAppNo(),anDescribe);
        }
        return false;
    }
}
