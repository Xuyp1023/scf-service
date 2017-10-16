package com.betterjr.modules.approval.service.seller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.agreement.data.ScfElecAgreementInfo;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.approval.service.ScfBaseApprovalService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestLastStatus;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;

@Service
public class ScfSellerRequestTradingBackgrandService extends ScfBaseApprovalService {
    @Autowired
    private ScfAgreementService agreementService;
    @Autowired
    private ScfElecAgreementService elecAgreementService;

    public void processPass(Map<String, Object> anContext) {
        String requestNo = anContext.get("requestNo").toString();
        String smsCode = anContext.get("smsCode").toString();
        ScfRequest request = requestService.findRequestByRequestNo(requestNo);

        // 签署三方协议
        if (false == agreementService.sendValidCodeByRequestNo(requestNo, AGREEMENT_TYPE_PROTOCOL, smsCode)) {
            // return AjaxObject.newError("操作失败：短信验证码错误").toJson();
        }

        this.pushSingInof(request);
        this.updateRequestStatus(requestNo, RequestTradeStatus.CONFIRM_TRADING.getCode(),
                RequestLastStatus.APPROVE.getCode());
        this.pushOrderInfo(requestService.findRequestByRequestNo(requestNo));
    }

    public void processReject(Map<String, Object> anContext) {
        // TODO Auto-generated method stub

    }

    /**
     * 如果是 微信端的申请 并为 票据融资 则需要发送消息
     * @param request
     * @param scheme
     */
    private void pushSingInof(ScfRequest request) {
        if (StringUtils.equals("2", request.getRequestFrom())
                && StringUtils.equals(RequestType.BILL.getCode(), request.getRequestType())) {
            List<ScfElecAgreementInfo> list = elecAgreementService.findElecAgreeByOrderNo(request.getRequestNo(), "1");
            if (false == Collections3.isEmpty(list)) {
                supplierPushService.pushSignInfo(Collections3.getFirst(list));
            }
        }
    }

}
