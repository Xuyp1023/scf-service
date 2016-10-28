package com.betterjr.modules.push.data;

import com.betterjr.common.utils.BetterStringUtils;

public enum RequestTradeStatusType {
    //申请状态；0未处理，1待完善资料，2审批中，3待签约，4已签约，5待录入流水号，6还款中，7已逾期，8已还完，9已失效，X融资失败
//    100 申请，110 出具保理方案 ，120 融资方确认方案 ，130 融资背景确认，140 核心企业确认背景，150 放款确认 ，160 放款完成，170 逾期，180 展期，190 还款完成
    
    PENDING("100", "申请"), BLENDING("110", "出具保理方案"), REPORTING("120", "融资方确认方案"), REJECT_MODIFIY("130", "融资背景确认"), WASTE_SINGLE("140", "核心企业确认背景"), BE_REMOVED("150", "放款确认"), 
    REPORTED("160", "放款完成"), CONFIRMED("170", "已逾期"), FINISHED("180", "展期"),TRADE_FIAL("190","还款完成");

    private final String title;
    private final String value;

    RequestTradeStatusType(String anValue, String anTitle) {
        this.title = anTitle;

        this.value = anValue;
    }

    public String getTitle() {
        return this.title;
    }

    public String getValue() {
        return this.value;
    }

    public static RequestTradeStatusType checking(String anWorkType) {
        try {
            if (BetterStringUtils.isNotBlank(anWorkType)) {
                for (RequestTradeStatusType statusType : RequestTradeStatusType.values()) {
                    if (statusType.value.equals(anWorkType)) {

                        return statusType;
                    }
                }
                return RequestTradeStatusType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
