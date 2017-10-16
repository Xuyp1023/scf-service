package com.betterjr.modules.approval.service.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.service.ScfRequestSchemeService;

@Service
public class ScfSellerConfirmTradingBackgrandService extends ScfBaseApprovalService {
    @Autowired
    private ScfRequestSchemeService schemeService;
    @Autowired
    private ScfAgreementService agreementService;

    public void processPass(Map<String, Object> anContext) {
        String requestNo = anContext.get("requestNo").toString();
        String smsCode = anContext.get("smsCode").toString();

        ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        BTAssert.notNull(scheme);

        if (false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_PROTOCOL, smsCode)) {
            // throw new RuntimeException("操作失败：短信验证码错误");
        }

        this.updateRequestStatus(requestNo, RequestTradeStatus.CONFIRM_LOAN.getCode(),
                RequestLastStatus.APPROVE.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(requestNo));
    }

    public void processReject(Map<String, Object> anContext) {
        String requestNo = anContext.get("requestNo").toString();

        // 取消签约
        // agreementService.cancelElecAgreement(requestNo, "1", "");

        ScfRequestScheme scheme = schemeService.findSchemeDetail2(requestNo);
        BTAssert.notNull(scheme);

        // 修改核心企业确认状态
        // scheme.setCoreCustAduit("2");
        // schemeService.saveModifyScheme(scheme);
    }

}
