package com.betterjr.modules.agreement.entity;

import java.util.Map;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SCF_REQUEST_PROTACAL")
public class ScfRequestProtacal implements BetterjrEntity {
    @Id
    @Column(name = "C_REQUESTNO",  columnDefinition="VARCHAR" )
    private String requestNo;

    @Column(name = "D_BEGIN_DATE",  columnDefinition="VARCHAR" )
    private String beginDate;

    @Column(name = "D_END_DATE",  columnDefinition="VARCHAR" )
    private String endDate;

    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    private String regDate;

    @Column(name = "C_PROTOCALNO",  columnDefinition="VARCHAR" )
    private String protocalNo;

    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;

    @Column(name = "C_PAYMENT",  columnDefinition="VARCHAR" )
    private String payment;

    @Column(name = "C_FIRST_NAME",  columnDefinition="VARCHAR" )
    private String firstName;

    @Column(name = "C_FIRST_ADDRESS",  columnDefinition="VARCHAR" )
    private String firstAddress;

    @Column(name = "C_FIRST_PHONE",  columnDefinition="VARCHAR" )
    private String firstPhone;

    @Column(name = "C_FIRST_FAX",  columnDefinition="VARCHAR" )
    private String firstFax;

    @Column(name = "C_FIRST_JOB",  columnDefinition="VARCHAR" )
    private String firstJob;

    @Column(name = "C_FIRST_LEGAL",  columnDefinition="VARCHAR" )
    private String firstLegal;

    @Column(name = "C_SECOND_NAME",  columnDefinition="VARCHAR" )
    private String secondName;

    @Column(name = "C_SECOND_ADDRESS",  columnDefinition="VARCHAR" )
    private String secondAddress;

    @Column(name = "C_SECOND_PHONE",  columnDefinition="VARCHAR" )
    private String secondPhone;

    @Column(name = "C_SECOND_FAX",  columnDefinition="VARCHAR" )
    private String secondFax;

    @Column(name = "C_SECOND_JOB",  columnDefinition="VARCHAR" )
    private String secondJob;

    @Column(name = "C_SECOND_LEGAL",  columnDefinition="VARCHAR" )
    private String secondLegal;

    @Column(name = "C_THREE_NAME",  columnDefinition="VARCHAR" )
    private String threeName;

    @Column(name = "C_THREE_ADDRESS",  columnDefinition="VARCHAR" )
    private String threeAddress;

    @Column(name = "C_THREE_PHONE",  columnDefinition="VARCHAR" )
    private String threePhone;

    @Column(name = "C_THREE_FAX",  columnDefinition="VARCHAR" )
    private String threeFax;

    @Column(name = "C_THREE_JOB",  columnDefinition="VARCHAR" )
    private String threeJob;

    @Column(name = "C_THREE_LEGAL",  columnDefinition="VARCHAR" )
    private String threeLegal;

    @Column(name = "C_AGREENAME",  columnDefinition="VARCHAR" )
    private String agreeName;
    
    @Column(name = "L_FIRSTNO",  columnDefinition="VARCHAR" )
    private String firstNo;

    @Column(name = "L_SECONDNO",  columnDefinition="Long" )
    private Long secondNo;

    @Column(name = "L_THREENO",  columnDefinition="Long" )
    private Long threeNo;

    private static final long serialVersionUID = 2746923015924615585L;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate == null ? null : beginDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getProtocalNo() {
        return protocalNo;
    }

    public void setProtocalNo(String protocalNo) {
        this.protocalNo = protocalNo == null ? null : protocalNo.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress == null ? null : firstAddress.trim();
    }

    public String getFirstPhone() {
        return firstPhone;
    }

    public void setFirstPhone(String firstPhone) {
        this.firstPhone = firstPhone == null ? null : firstPhone.trim();
    }

    public String getFirstFax() {
        return firstFax;
    }

    public void setFirstFax(String firstFax) {
        this.firstFax = firstFax == null ? null : firstFax.trim();
    }

    public String getFirstJob() {
        return firstJob;
    }

    public void setFirstJob(String firstJob) {
        this.firstJob = firstJob == null ? null : firstJob.trim();
    }

    public String getFirstLegal() {
        return firstLegal;
    }

    public void setFirstLegal(String firstLegal) {
        this.firstLegal = firstLegal == null ? null : firstLegal.trim();
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName == null ? null : secondName.trim();
    }

    public String getSecondAddress() {
        return secondAddress;
    }

    public void setSecondAddress(String secondAddress) {
        this.secondAddress = secondAddress == null ? null : secondAddress.trim();
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone == null ? null : secondPhone.trim();
    }

    public String getSecondFax() {
        return secondFax;
    }

    public void setSecondFax(String secondFax) {
        this.secondFax = secondFax == null ? null : secondFax.trim();
    }

    public String getSecondJob() {
        return secondJob;
    }

    public void setSecondJob(String secondJob) {
        this.secondJob = secondJob == null ? null : secondJob.trim();
    }

    public String getSecondLegal() {
        return secondLegal;
    }

    public void setSecondLegal(String secondLegal) {
        this.secondLegal = secondLegal == null ? null : secondLegal.trim();
    }

    public String getThreeName() {
        return threeName;
    }

    public void setThreeName(String threeName) {
        this.threeName = threeName == null ? null : threeName.trim();
    }

    public String getThreeAddress() {
        return threeAddress;
    }

    public void setThreeAddress(String threeAddress) {
        this.threeAddress = threeAddress == null ? null : threeAddress.trim();
    }

    public String getThreePhone() {
        return threePhone;
    }

    public void setThreePhone(String threePhone) {
        this.threePhone = threePhone == null ? null : threePhone.trim();
    }

    public String getThreeFax() {
        return threeFax;
    }

    public void setThreeFax(String threeFax) {
        this.threeFax = threeFax == null ? null : threeFax.trim();
    }

    public String getThreeJob() {
        return threeJob;
    }

    public void setThreeJob(String threeJob) {
        this.threeJob = threeJob == null ? null : threeJob.trim();
    }

    public String getThreeLegal() {
        return threeLegal;
    }

    public void setThreeLegal(String threeLegal) {
        this.threeLegal = threeLegal == null ? null : threeLegal.trim();
    }

    public String getAgreeName() {
        return this.agreeName;
    }

    public void setAgreeName(String anAgreeName) {
        this.agreeName = anAgreeName;
    }

    public String getFirstNo() {
        return this.firstNo;
    }

    public void setFirstNo(String anFirstNo) {
        this.firstNo = anFirstNo;
    }

    public Long getSecondNo() {
        return this.secondNo;
    }

    public void setSecondNo(Long anSecondNo) {
        this.secondNo = anSecondNo;
    }

    public Long getThreeNo() {
        return this.threeNo;
    }

    public void setThreeNo(Long anThreeNo) {
        this.threeNo = anThreeNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", requestNo=").append(requestNo);
        sb.append(", beginDate=").append(beginDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", regDate=").append(regDate);
        sb.append(", protocalNo=").append(protocalNo);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", payment=").append(payment);
        sb.append(", firstName=").append(firstName);
        sb.append(", firstAddress=").append(firstAddress);
        sb.append(", firstPhone=").append(firstPhone);
        sb.append(", firstFax=").append(firstFax);
        sb.append(", firstJob=").append(firstJob);
        sb.append(", firstLegal=").append(firstLegal);
        sb.append(", secondName=").append(secondName);
        sb.append(", secondAddress=").append(secondAddress);
        sb.append(", secondPhone=").append(secondPhone);
        sb.append(", secondFax=").append(secondFax);
        sb.append(", secondJob=").append(secondJob);
        sb.append(", secondLegal=").append(secondLegal);
        sb.append(", threeName=").append(threeName);
        sb.append(", threeAddress=").append(threeAddress);
        sb.append(", threePhone=").append(threePhone);
        sb.append(", threeFax=").append(threeFax);
        sb.append(", threeJob=").append(threeJob);
        sb.append(", threeLegal=").append(threeLegal);
        sb.append(", agreeName=").append(agreeName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        ScfRequestProtacal other = (ScfRequestProtacal) that;
        return (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
            && (this.getBeginDate() == null ? other.getBeginDate() == null : this.getBeginDate().equals(other.getBeginDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getProtocalNo() == null ? other.getProtocalNo() == null : this.getProtocalNo().equals(other.getProtocalNo()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getPayment() == null ? other.getPayment() == null : this.getPayment().equals(other.getPayment()))
            && (this.getFirstName() == null ? other.getFirstName() == null : this.getFirstName().equals(other.getFirstName()))
            && (this.getFirstAddress() == null ? other.getFirstAddress() == null : this.getFirstAddress().equals(other.getFirstAddress()))
            && (this.getFirstPhone() == null ? other.getFirstPhone() == null : this.getFirstPhone().equals(other.getFirstPhone()))
            && (this.getFirstFax() == null ? other.getFirstFax() == null : this.getFirstFax().equals(other.getFirstFax()))
            && (this.getFirstJob() == null ? other.getFirstJob() == null : this.getFirstJob().equals(other.getFirstJob()))
            && (this.getFirstLegal() == null ? other.getFirstLegal() == null : this.getFirstLegal().equals(other.getFirstLegal()))
            && (this.getSecondName() == null ? other.getSecondName() == null : this.getSecondName().equals(other.getSecondName()))
            && (this.getSecondAddress() == null ? other.getSecondAddress() == null : this.getSecondAddress().equals(other.getSecondAddress()))
            && (this.getSecondPhone() == null ? other.getSecondPhone() == null : this.getSecondPhone().equals(other.getSecondPhone()))
            && (this.getSecondFax() == null ? other.getSecondFax() == null : this.getSecondFax().equals(other.getSecondFax()))
            && (this.getSecondJob() == null ? other.getSecondJob() == null : this.getSecondJob().equals(other.getSecondJob()))
            && (this.getSecondLegal() == null ? other.getSecondLegal() == null : this.getSecondLegal().equals(other.getSecondLegal()))
            && (this.getThreeName() == null ? other.getThreeName() == null : this.getThreeName().equals(other.getThreeName()))
            && (this.getThreeAddress() == null ? other.getThreeAddress() == null : this.getThreeAddress().equals(other.getThreeAddress()))
            && (this.getThreePhone() == null ? other.getThreePhone() == null : this.getThreePhone().equals(other.getThreePhone()))
            && (this.getThreeFax() == null ? other.getThreeFax() == null : this.getThreeFax().equals(other.getThreeFax()))
            && (this.getThreeJob() == null ? other.getThreeJob() == null : this.getThreeJob().equals(other.getThreeJob()))
            && (this.getAgreeName() == null ? other.getAgreeName() == null : this.getAgreeName().equals(other.getAgreeName()))
            && (this.getThreeLegal() == null ? other.getThreeLegal() == null : this.getThreeLegal().equals(other.getThreeLegal()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getBeginDate() == null) ? 0 : getBeginDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getProtocalNo() == null) ? 0 : getProtocalNo().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getPayment() == null) ? 0 : getPayment().hashCode());
        result = prime * result + ((getFirstName() == null) ? 0 : getFirstName().hashCode());
        result = prime * result + ((getFirstAddress() == null) ? 0 : getFirstAddress().hashCode());
        result = prime * result + ((getFirstPhone() == null) ? 0 : getFirstPhone().hashCode());
        result = prime * result + ((getFirstFax() == null) ? 0 : getFirstFax().hashCode());
        result = prime * result + ((getFirstJob() == null) ? 0 : getFirstJob().hashCode());
        result = prime * result + ((getFirstLegal() == null) ? 0 : getFirstLegal().hashCode());
        result = prime * result + ((getSecondName() == null) ? 0 : getSecondName().hashCode());
        result = prime * result + ((getSecondAddress() == null) ? 0 : getSecondAddress().hashCode());
        result = prime * result + ((getSecondPhone() == null) ? 0 : getSecondPhone().hashCode());
        result = prime * result + ((getSecondFax() == null) ? 0 : getSecondFax().hashCode());
        result = prime * result + ((getSecondJob() == null) ? 0 : getSecondJob().hashCode());
        result = prime * result + ((getSecondLegal() == null) ? 0 : getSecondLegal().hashCode());
        result = prime * result + ((getThreeName() == null) ? 0 : getThreeName().hashCode());
        result = prime * result + ((getThreeAddress() == null) ? 0 : getThreeAddress().hashCode());
        result = prime * result + ((getThreePhone() == null) ? 0 : getThreePhone().hashCode());
        result = prime * result + ((getThreeFax() == null) ? 0 : getThreeFax().hashCode());
        result = prime * result + ((getThreeJob() == null) ? 0 : getThreeJob().hashCode());
        result = prime * result + ((getThreeLegal() == null) ? 0 : getThreeLegal().hashCode());
        result = prime * result + ((getAgreeName() == null) ? 0 : getAgreeName().hashCode());
        return result;
    }
    
    public void initProtacal(){
        this.businStatus="0";
        this.regDate = BetterDateUtils.getNumDate();
    }
}