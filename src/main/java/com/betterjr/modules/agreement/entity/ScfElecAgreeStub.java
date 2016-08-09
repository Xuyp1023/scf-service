package com.betterjr.modules.agreement.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_AGREEMENT_STUB")
public class ScfElecAgreeStub implements BetterjrEntity {
    /**
     * 序号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="序号", comments = "序号")
    private Integer id;

    /**
     * 电子签署合同的申请单号，日期加流水号的形式
     */
    @Column(name = "C_APPLICATIONNO",  columnDefinition="VARCHAR" )
    @MetaData( value="电子签署合同的申请单号", comments = "电子签署合同的申请单号，日期加流水号的形式")
    private String appNo;

    /**
     * 客户编号；对应操作员所在企业的客户号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号；对应操作员所在企业的客户号")
    private Long custNo;

    /**
     * 网络地址
     */
    @Column(name = "C_IPADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="网络地址", comments = "网络地址")
    private String ipaddr;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员编码", comments = "操作员编码")
    private String operCode;

    /**
     * 操作员名字
     */
    @Column(name = "C_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员名字", comments = "操作员名字")
    private String operName;

    /**
     * 操作时间
     */
    @Column(name = "T_OPERTIME",  columnDefinition="VARCHAR" )
    @MetaData( value="操作时间", comments = "操作时间")
    private String operTime;

    /**
     * 状态；0未处理，1成功，2处理失败
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态；0未处理，1成功，2处理失败")
    private String operStatus;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    private static final long serialVersionUID = 1461833243013L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode == null ? null : operCode.trim();
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime == null ? null : operTime.trim();
    }

    public String getOperStatus() {
        return this.operStatus;
    }

    public void setOperStatus(String anOperStatus) {
        this.operStatus = anOperStatus;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        ScfElecAgreeStub other = (ScfElecAgreeStub) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAppNo() == null ? other.getAppNo() == null : this.getAppNo().equals(other.getAppNo()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()))
            && (this.getOperCode() == null ? other.getOperCode() == null : this.getOperCode().equals(other.getOperCode()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getOperStatus() == null ? other.getOperStatus() == null : this.getOperStatus().equals(other.getOperStatus()));
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
        
       return result;
    }
    
    public ScfElecAgreeStub(){
        
    } 
    
    public ScfElecAgreeStub(String anAppNo, Long anCustNo){
        this.appNo = anAppNo;
        this.custNo = anCustNo;
        this.id = SerialGenerator.getIntValue("ScfElecAgreeStub.id");
        this.regDate = BetterDateUtils.getNumDate();
        this.operStatus = "0";
    }
    
    public static void updateSignInfo(ScfElecAgreeStub anStub, String anStatus) {
        //获取当前登录操作员信息
        CustOperatorInfo operator = UserUtils.getOperatorInfo();
        anStub.operCode = operator.getOperCode();
        anStub.operName = operator.getName();
        anStub.operStatus = anStatus;
        anStub.operTime = BetterDateUtils.getNumDateTime();
    }
}