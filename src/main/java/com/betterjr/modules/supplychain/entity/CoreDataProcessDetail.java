package com.betterjr.modules.supplychain.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.modules.supplychain.data.ScfClientDataDetail;
 
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_PROCESS_DETAIL")
public class CoreDataProcessDetail implements BetterjrEntity {
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
    @Column(name = "C_COREORG", columnDefinition = "VARCHAR")
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
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 拜特内部编号
     */
    @Column(name = "C_BTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "拜特内部编号", comments = "拜特内部编号")
    private String btNo;

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

    /**
     * 处理数量
     */
    @Column(name = "N_COUNT", columnDefinition = "INTEGER")
    @MetaData(value = "处理数量", comments = "处理数量")
    private Integer dataCount;

    /**
     * 描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "描述", comments = "描述")
    private String description;

    private static final long serialVersionUID = 1459331162476L;

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

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getBtNo() {
        return btNo;
    }

    public void setBtNo(String btNo) {
        this.btNo = btNo == null ? null : btNo.trim();
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

    public Integer getDataCount() {
        return this.dataCount;
    }

    public void setDataCount(Integer anDataCount) {
        this.dataCount = anDataCount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id = ").append(id);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", processType=").append(processType);
        sb.append(", workDate=").append(workDate);
        sb.append(", workStatus=").append(workStatus);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", custNo=").append(custNo);
        sb.append(", btNo=").append(btNo);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", dataCount=").append(dataCount);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
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
        CoreDataProcessDetail other = (CoreDataProcessDetail) that;
        return (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getProcessType() == null ? other.getProcessType() == null : this.getProcessType().equals(other.getProcessType()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkDate() == null ? other.getWorkDate() == null : this.getWorkDate().equals(other.getWorkDate()))
                && (this.getWorkStatus() == null ? other.getWorkStatus() == null : this.getWorkStatus().equals(other.getWorkStatus()))
                && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getDataCount() == null ? other.getDataCount() == null : this.getDataCount().equals(other.getDataCount()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getBtNo() == null ? other.getBtNo() == null : this.getBtNo().equals(other.getBtNo()));
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
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getBtNo() == null) ? 0 : getBtNo().hashCode());
        result = prime * result + ((getDataCount() == null) ? 0 : getDataCount().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    public CoreDataProcessDetail() {
    }

    public void putWorkStatus(ScfClientDataDetail anDetail) {
        this.workStatus = anDetail.getWorkStatus();
        this.dataCount = anDetail.getCount();
    }

    public CoreDataProcessDetail(String anOperOrg, String anWorkType, ScfClientDataDetail anDetail) {
        BeanMapper.copy(anDetail, this);
        this.id = SerialGenerator.getLongValue("CustAgreement.id");
        this.processType = anWorkType;
        this.operOrg = anOperOrg;
        this.bankAccount = anDetail.getBankAcc();
        this.btNo = anDetail.getBtCustId();
        this.dataCount = anDetail.getCount();
        if ("1".equals(this.workStatus)){
           this.description ="成功"; 
        }
        else{
            this.description = anDetail.getData();
        }
    }
}