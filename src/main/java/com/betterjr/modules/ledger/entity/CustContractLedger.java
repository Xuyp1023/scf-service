package com.betterjr.modules.ledger.entity;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_contract_ledger_cust")
public class CustContractLedger implements BetterjrEntity {
    @Id
    @Column(name = "l_custNo", columnDefinition = "INTEGER")
    private Long custNo;

    @Id
    @Column(name = "l_contractId", columnDefinition = "INTEGER")
    private Long contractId;

    @Column(name = "c_custName", columnDefinition = "VARCHAR")
    private String custName;

    @Column(name = "c_representative", columnDefinition = "VARCHAR")
    private String representative;

    @Column(name = "c_bankName", columnDefinition = "VARCHAR")
    private String bankName;

    @Column(name = "c_bankAccount", columnDefinition = "VARCHAR")
    private String bankAccount;

    @Column(name = "c_tax_code", columnDefinition = "VARCHAR")
    private String taxCode;

    @Column(name = "c_phone", columnDefinition = "VARCHAR")
    private String phone;

    @Column(name = "c_fax", columnDefinition = "VARCHAR")
    private String fax;

    @Column(name = "c_address", columnDefinition = "VARCHAR")
    private String address;

    @Column(name = "c_email", columnDefinition = "VARCHAR")
    private String email;

    @Column(name = "c_busin_status", columnDefinition = "VARCHAR")
    private String businStatus;

    @Column(name = "c_reg_date", columnDefinition = "VARCHAR")
    private String regDate;

    @Column(name = "c_reg_time", columnDefinition = "VARCHAR")
    private String regTime;

    @Column(name = "c_bankAccountName", columnDefinition = "VARCHAR")
    private String bankAccountName;

    @Column(name = "c_zipCode", columnDefinition = "VARCHAR")
    private String zipCode;

    private static final long serialVersionUID = 1482311479891L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

    public String getBankAccountName() {
        return this.bankAccountName;
    }

    public void setBankAccountName(String anBankAccountName) {
        this.bankAccountName = anBankAccountName;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String anZipCode) {
        this.zipCode = anZipCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", contractId=").append(contractId);
        sb.append(", custName=").append(custName);
        sb.append(", representative=").append(representative);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", taxCode=").append(taxCode);
        sb.append(", phone=").append(phone);
        sb.append(", fax=").append(fax);
        sb.append(", address=").append(address);
        sb.append(", email=").append(email);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", zipCode=").append(zipCode);
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
        CustContractLedger other = (CustContractLedger) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getContractId() == null ? other.getContractId() == null
                        : this.getContractId().equals(other.getContractId()))
                && (this.getCustName() == null ? other.getCustName() == null
                        : this.getCustName().equals(other.getCustName()))
                && (this.getRepresentative() == null ? other.getRepresentative() == null
                        : this.getRepresentative().equals(other.getRepresentative()))
                && (this.getBankName() == null ? other.getBankName() == null
                        : this.getBankName().equals(other.getBankName()))
                && (this.getBankAccount() == null ? other.getBankAccount() == null
                        : this.getBankAccount().equals(other.getBankAccount()))
                && (this.getTaxCode() == null ? other.getTaxCode() == null
                        : this.getTaxCode().equals(other.getTaxCode()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getFax() == null ? other.getFax() == null : this.getFax().equals(other.getFax()))
                && (this.getAddress() == null ? other.getAddress() == null
                        : this.getAddress().equals(other.getAddress()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getRepresentative() == null) ? 0 : getRepresentative().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getTaxCode() == null) ? 0 : getTaxCode().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getFax() == null) ? 0 : getFax().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        return result;
    }

    public void initValue() {
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.businStatus = "1";
    }
}