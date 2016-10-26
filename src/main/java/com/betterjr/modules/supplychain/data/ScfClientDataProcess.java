package com.betterjr.modules.supplychain.data;

import java.util.*;

import com.betterjr.common.utils.BetterDateUtils;

public class ScfClientDataProcess implements java.io.Serializable {
    private static final long serialVersionUID = 1365428114416891818L;

    // 类型
    private String workType;

    // 状态
    private String workStatus;

    // 处理结果数据
    private Map<String, List<ScfClientDataDetail>> data = new HashMap();

    // 开始日期
    private String startDate;

    // 处理 日期
    private String workDate;

    // 截止日期
    private String endDate;

    public ScfClientDataProcess() {

    }

    public Map<String, List<ScfClientDataDetail>> getData() {
        return this.data;
    }

    public void setData(Map<String, List<ScfClientDataDetail>> anData) {
        this.data = anData;
    }

    public List<ScfClientDataDetail> findData(String anWorkType) {
        List result = this.data.get(anWorkType);
        if (result == null) {
            result = new ArrayList();
        }
        
        return result;
    }

    public void addData(String anWorkType, ScfClientDataDetail anClientData) {

        List workList = data.get(anWorkType);
        if (workList == null) {
            workList = new ArrayList();
            data.put(anWorkType, workList);
        }
        workList.add(anClientData);
    }

    public void addData(ScfClientDataDetail anClientData) {
        addData("data", anClientData);
    }

    public static ScfClientDataProcess createInstance(String anWorkType) {
        ScfClientDataProcess dataProcess = new ScfClientDataProcess();
        dataProcess.initDefValue(anWorkType);
        return dataProcess;
    }

    public void initDefValue(String anWorkType) {
        this.workType = anWorkType;
        this.workStatus = "0";
        this.workDate = BetterDateUtils.formatNumberDate(new Date());
        this.startDate = BetterDateUtils.formatNumberDate(BetterDateUtils.addYears(new Date(), -6));
        this.endDate = BetterDateUtils.formatNumberDate(BetterDateUtils.addDays(new Date(), 1));
    }

    public String getWorkType() {
        return this.workType;
    }

    public void setWorkType(String anWorkType) {
        this.workType = anWorkType;
    }

    public String getWorkStatus() {
        return this.workStatus;
    }

    public void setWorkStatus(String anWorkStatus) {
        this.workStatus = anWorkStatus;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String anStartDate) {
        this.startDate = anStartDate;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String anWorkDate) {
        this.workDate = anWorkDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("workType=").append(workType);
        sb.append(", workDate=").append(workDate);
        sb.append(", workStatus=").append(workStatus);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append("]");
        return sb.toString();
    }

}
