package com.betterjr.modules.agreement.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_AGREEMENT_STUB")
public class ScfElecAgreeStub implements BetterjrEntity {
    /**
     * 序号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "序号", comments = "序号")
    private Integer id;

    /**
     * 电子签署合同的申请单号，日期加流水号的形式
     */
    @Column(name = "C_APPLICATIONNO", columnDefinition = "VARCHAR")
    @MetaData(value = "电子签署合同的申请单号", comments = "电子签署合同的申请单号，日期加流水号的形式")
    private String appNo;

    /**
     * 客户编号；对应操作员所在企业的客户号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号；对应操作员所在企业的客户号")
    private Long custNo;

    /**
     * 网络地址
     */
    @Column(name = "C_IPADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "网络地址", comments = "网络地址")
    private String ipaddr;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员编码", comments = "操作员编码")
    private String operCode;

    /**
     * 操作员名字
     */
    @Column(name = "C_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员名字", comments = "操作员名字")
    private String operName;

    /**
     * 操作时间
     */
    @Column(name = "T_OPERTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "操作时间", comments = "操作时间")
    @JsonSerialize(using = CustDateTimeJsonSerializer.class)
    private String operTime;

    /**
     * 状态；0未处理，1成功，2处理失败
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态；0未处理，1成功，2处理失败")
    private String operStatus;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;
    /**
     * 签署顺序
     */
    @Column(name = "N_SEQUENCE", columnDefinition = "INTEGER")
    @MetaData(value = "签署顺序", comments = "签署顺序")
    private Integer signOrder;

    /**
     * 在电子合同服务商那边的签署记录id
     */
    @Column(name = "C_SIGN_SERVICEID", columnDefinition = "VARCHAR")
    @MetaData(value = "在电子合同服务商那边的签署记录id", comments = "在电子合同服务商那边的签署记录id")
    private String signServiceId;

    private static final long serialVersionUID = 1461833243013L;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(final String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(final String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(final String operCode) {
        this.operCode = operCode == null ? null : operCode.trim();
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(final String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(final String operTime) {
        this.operTime = operTime == null ? null : operTime.trim();
    }

    public String getOperStatus() {
        return this.operStatus;
    }

    public void setOperStatus(final String anOperStatus) {
        this.operStatus = anOperStatus;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(final String anRegDate) {
        this.regDate = anRegDate;
    }

    public Integer getSignOrder() {
        return this.signOrder;
    }

    public void setSignOrder(final Integer anSignOrder) {
        this.signOrder = anSignOrder;
    }

    public String getSignServiceId() {
        return this.signServiceId;
    }

    public void setSignServiceId(final String anSignServiceId) {
        this.signServiceId = anSignServiceId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appNo=").append(appNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", ipaddr=").append(ipaddr);
        sb.append(", operCode=").append(operCode);
        sb.append(", operName=").append(operName);
        sb.append(", operTime=").append(operTime);
        sb.append(", operStatus=").append(operStatus);
        sb.append(", regDate=").append(regDate);
        sb.append(", signOrder=").append(signOrder);
        sb.append(", signServiceId=").append(signServiceId);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        final ScfElecAgreeStub other = (ScfElecAgreeStub) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAppNo() == null ? other.getAppNo() == null : this.getAppNo().equals(other.getAppNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()))
                && (this.getOperCode() == null ? other.getOperCode() == null
                        : this.getOperCode().equals(other.getOperCode()))
                && (this.getOperName() == null ? other.getOperName() == null
                        : this.getOperName().equals(other.getOperName()))
                && (this.getOperTime() == null ? other.getOperTime() == null
                        : this.getOperTime().equals(other.getOperTime()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getOperStatus() == null ? other.getOperStatus() == null
                        : this.getOperStatus().equals(other.getOperStatus()))
                && (this.getSignOrder() == null ? other.getSignOrder() == null
                        : this.getSignOrder().equals(other.getSignOrder()))
                && (this.getSignServiceId() == null ? other.getSignServiceId() == null
                        : this.getSignServiceId().equals(other.getSignServiceId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAppNo() == null) ? 0 : getAppNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getIpaddr() == null) ? 0 : getIpaddr().hashCode());
        result = prime * result + ((getOperCode() == null) ? 0 : getOperCode().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getOperStatus() == null) ? 0 : getOperStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getSignOrder() == null) ? 0 : getSignOrder().hashCode());
        result = prime * result + ((getSignServiceId() == null) ? 0 : getSignServiceId().hashCode());

        return result;
    }

    public ScfElecAgreeStub() {

    }

    public ScfElecAgreeStub(final int anOrder, final String anAppNo, final Long anCustNo) {
        this.signOrder = anOrder;
        this.appNo = anAppNo;
        this.custNo = anCustNo;
        this.id = SerialGenerator.getIntValue("ScfElecAgreeStub.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.operStatus = "0";
    }

    public static void updateSignInfo(final ScfElecAgreeStub anStub, final String anStatus) {
        // 获取当前登录操作员信息
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        anStub.operCode = operator.getOperCode();
        anStub.operName = operator.getName();
        anStub.operStatus = anStatus;
        anStub.operTime = BetterDateUtils.getNumDateTime();
    }
}