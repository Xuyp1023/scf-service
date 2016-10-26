package com.betterjr.modules.supplychain.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_PROCESS_LOG")
public class CoreDataProcessInfo implements BetterjrEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 数据类型;0：供应商资料，1：供应商银行账户，2：供应商资金流水，3：供应商票据信息，4：供应商应收款信息，5供应商订单信息，6托收账户余额信息
     */
    @Column(name = "C_PROCESS_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "数据类型;0：供应商资料", comments = "数据类型;0：供应商资料，1：供应商银行账户，2：供应商资金流水，3：供应商票据信息，4：供应商应收款信息，5供应商订单信息，6托收账户余额信息")
    private String processType;

    /**
     * 处理日期
     */
    @Column(name = "D_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "处理日期", comments = "处理日期")
    private String workDate;

    /**
     * 状态，0失败，1成功
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0失败，1成功")
    private String workStatus;

    /**
     * 银行账户
     */
    @Column(name = "C_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "银行账户", comments = "银行账户")
    private String bankAccount;
 
    /**
     * 开始日期
     */
    @Column(name = "D_STARTDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "开始日期", comments = "开始日期")
    private String startDate;

    /**
     * 结束日期
     */
    @Column(name = "D_ENDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "结束日期", comments = "结束日期")
    private String endDate;

    private static final long serialVersionUID = 1459331162475L;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType == null ? null : processType.trim();
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate == null ? null : workDate.trim();
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus == null ? null : workStatus.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }
 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id=").append(id);
        sb.append(",operOrg=").append(operOrg);
        sb.append(", processType=").append(processType);
        sb.append(", workDate=").append(workDate);
        sb.append(", workStatus=").append(workStatus);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append("]");
        return sb.toString();
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CoreDataProcessInfo other = (CoreDataProcessInfo) that;
        return (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getProcessType() == null ? other.getProcessType() == null : this.getProcessType().equals(other.getProcessType()))
                && (this.getWorkDate() == null ? other.getWorkDate() == null : this.getWorkDate().equals(other.getWorkDate()))
                && (this.getWorkStatus() == null ? other.getWorkStatus() == null : this.getWorkStatus().equals(other.getWorkStatus()))
                && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
                && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getProcessType() == null) ? 0 : getProcessType().hashCode());
        result = prime * result + ((getWorkDate() == null) ? 0 : getWorkDate().hashCode());
        result = prime * result + ((getWorkStatus() == null) ? 0 : getWorkStatus().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    public static CoreDataProcessInfo switchNext(CoreDataProcessInfo anProcess) {
        CoreDataProcessInfo process = BeanMapper.map(anProcess, CoreDataProcessInfo.class);
        if (BetterStringUtils.isBlank(anProcess.endDate)){
            anProcess.endDate = BetterDateUtils.getNumDate();
        }
        process.startDate = anProcess.endDate;
        process.workDate = BetterDateUtils.addStrDays(process.startDate, 1);
        process.endDate = null;
        process.setWorkStatus("1");
        //process.id = SerialGenerator.getLongValue("CoreDataProcessInfo.id");
        return process;
    }
}