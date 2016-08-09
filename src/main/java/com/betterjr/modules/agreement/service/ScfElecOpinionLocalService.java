package com.betterjr.modules.agreement.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;

/**
 * 融资合同管理之核心企业确认书管理
 * @author zhoucy
 *
 */
@Service("scfElecOpinionLocalService")
@Scope("singleton")
public class ScfElecOpinionLocalService  extends ScfElecAgreeLocalService {
    private static final Logger logger = LoggerFactory.getLogger(ScfElecOpinionLocalService.class);

     private ScfRequestOpinionService requestOpinionService = null;

    protected void subInit(){
       this.requestOpinionService = SpringContextHolder.getBean(ScfRequestOpinionService.class);
    }

    @Override
    protected Map<String, Object> findViewModeData() {
        Map<String, Object> result = new HashMap();
        String requestNo = this.elecAgree.getRequestNo();
        ScfRequestOpinion opinionInfo = requestOpinionService.selectByPrimaryKey(requestNo);
        if(null == opinionInfo) {
            logger.error("Can't get opinion information with request no:"+requestNo);
            throw new BytterTradeException(40001, "无法获取确认书信息");
        }
        result.put("opinionInfo", opinionInfo);
 
        //取当前服务器日期作为签署日期
        result.put("signDate", findSignDate());

        return result;
    }

    @Override
    protected String getViewModeFile() {

        return "buyerConfirm";
    }

    @Override
    public boolean cancelElecAgreement() {
        if (elecAgreeService.checkCancelElecAgreement(this.elecAgree.getAppNo())){
//          ScfRequest tmpReq = this.requestService.selectByPrimaryKey(elecAgree.getRequestNo());
//          tmpReq.cancelRequestValue("核心企业拒绝确认该融资订单！", "4");
//          tmpReq = this.remoteHelper.sendRequestStatus(tmpReq);
//          if ("6".equalsIgnoreCase(tmpReq.getOutStatus())){
             
             return this.elecAgreeService.saveAndCancelElecAgreement(this.elecAgree.getAppNo());
//          }
//          else{
//              return false;
//          }
        }
        return false;
    }
}
