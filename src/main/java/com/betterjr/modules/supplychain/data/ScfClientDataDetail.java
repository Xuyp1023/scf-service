package com.betterjr.modules.supplychain.data;
 
public class ScfClientDataDetail implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = -1863720109216478785L;

    // 银行账户
    private String bankAcc;

    // 账户名称
    private String bankAccName;

    // 拜特资金管理系统中客户号
    private String btCustId;

    // 开始日期
    private String startDate;

    private String workDate;
    // 截止日期
    private String endDate;

    // 银行流水方向
    private String workFlag;

    // 处理状态
    private String workStatus;

    // 结果数据
    private String data;

    // 结果数量
    private int count;

    public String getBankAcc() {
        return this.bankAcc;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String anWorkDate) {
        this.workDate = anWorkDate;
    }

    public void initDefValue(ScfClientDataProcess anProcess) {
        this.workStatus = "0";
        this.workDate = anProcess.getWorkDate();
        this.startDate = anProcess.getStartDate();
        this.endDate = anProcess.getEndDate();
    }

    public void setBankAcc(String anBankAcc) {
        this.bankAcc = anBankAcc;
    }

    public String getBankAccName() {
        return this.bankAccName;
    }

    public void setBankAccName(String anBankAccName) {
        this.bankAccName = anBankAccName;
    }

    public String getBtCustId() {
        return this.btCustId;
    }

    public void setBtCustId(String anBtCustId) {
        this.btCustId = anBtCustId;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String anStartDate) {
        this.startDate = anStartDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    public String getWorkFlag() {
        return this.workFlag;
    }

    public void setWorkFlag(String anWorkFlag) {
        this.workFlag = anWorkFlag;
    }

    public String getWorkStatus() {
        return this.workStatus;
    }

    public void setWorkStatus(String anWorkStatus) {
        this.workStatus = anWorkStatus;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String anData) {
        this.data = anData;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int anCount) {
        this.count = anCount;
    }

    public boolean hasValid(){
       return "1".equals(this.workStatus); 
    }
    
    public void putDataValue(String anData) {
        this.data = anData;
        if ((this.data != null) && (this.data.trim().length() > 10)) {
            this.workStatus = "1";
        }
        else {
            this.workStatus = "0";
        }
    }

    public ScfClientDataDetail clone() {
        try {
            return (ScfClientDataDetail) super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" bankAcc=").append(bankAcc);
        sb.append(", bankAccName=").append(bankAccName);
        sb.append(", btCustId=").append(btCustId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", workFlag=").append(workFlag);
        sb.append(", workStatus=").append(workStatus);
        sb.append(", data=").append(data);
        sb.append(", count=").append(count);
        sb.append("]");
        return sb.toString();
    }
}
