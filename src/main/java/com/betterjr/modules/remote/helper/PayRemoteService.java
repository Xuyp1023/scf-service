package com.betterjr.modules.remote.helper;

import java.util.List;

public abstract class PayRemoteService extends RemoteService {
    private static final long serialVersionUID = 7362543651831413605L;

    public String identifySend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName, String anCallbackUrl) {

        return null;
    }

    public String identifyReceive(String anCryptograph) {

        return null;
    }

    public String resetPasswordSend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName, String anCallbackUrl) {

        return null;
    }

    public String resetPasswordReceive(String anCryptograph) {

        return null;
    }

    public String bindSend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName, String anCallbackUrl) {

        return null;
    }

    public String bindReceive(String anCryptograph) {

        return null;
    }

    public String cancelBindSend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName) {

        return null;
    }

    public String cancelBindReceive(String anCryptograph) {

        return null;
    }

    public String endSignSend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName) {

        return null;
    }

    public String endSignReceive(String anCryptograph) {

        return null;
    }

    public String paySend(String anRequestNo, String anBusinFlag, String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName,
            String anApplAmount, String anFundCode) {

        return null;
    }

    public String payReceive(String anRequestNo, String anBusinFlag, String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName,
            String anApplAmount, String anFundCode) {

        return null;
    }

    public String drawSend(String anRequestNo, String anBusinFlag, String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName,
            String anApplAmount, String anFundCode) {

        return null;
    }

    public String drawReceive(String anRequestNo, String anBusinFlag, String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName,
            String anApplAmount, String anFundCode) {

        return null;
    }

    public String cancelSend(String anRequestNo) {

        return null;
    }

    public String cancelReceive(String anRequestNo) {

        return null;
    }

    public String payBackSend(String anRequestNo) {

        return null;
    }

    public String payBackReceive(String anRequestNo, String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName,
            String anApplAmount) {

        return null;
    }

    public String signSend(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName, String anCallbackUrl) {

        return null;
    }

    public String signReceive(String anCryptograph) {

        return null;
    }

    public String querySign(String anIdentType, String anIdentNo, String anBankAcco, String anBankAccoName) {

        return null;
    }

    public String singleQuery(String anOrderNo) {

        return null;
    }

    public String batchQuery(String anStartTime, String anEndTime) {

        return null;
    }

    public String downLoadSettleFile(String anDate, String anMode) {

        return null;
    }

    public String downLoadPayBackFile(String anDate, String anMode) {

        return null;
    }

    public String makePayBackFile(List anRecords) {

        return null;
    }

    public String makeSavePlanFile(List anRecords) {

        return null;
    }

    public String makeSupervisorBackFile(List anRecords) {

        return null;
    }

    public String makeSupervisorFile(List anRecords) {

        return null;
    }
}
