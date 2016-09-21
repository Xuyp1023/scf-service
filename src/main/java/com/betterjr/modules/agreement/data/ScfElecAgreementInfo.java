package com.betterjr.modules.agreement.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.agreement.entity.ScfElecAgreeStub;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;

@ReferClass(ScfElecAgreement.class)
public class ScfElecAgreementInfo extends ScfElecAgreement {
    private static final long serialVersionUID = 1461746080033L;

    private String buyer;

    private String supplier;

    private List<ScfElecAgreeStubInfo> stubInfos;

    // 保理公司名称
    private String factorName;

    //本方的签署状态。和合同的签署状态不同
    private String subscribed;

    private String billNo;
    private String description;
    private BigDecimal confirmBalance;
    private String productName;
    private String billMode;
    private String invoiceDate;
    private String endDate;

    public String getSubscribed() {
        
        return this.subscribed;
    }
 
    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String anBuyer) {
        this.buyer = anBuyer;
    }

    public String getFactorName() {

        return BetterStringUtils.isBlank(this.factorName) ? this.getFactorNo() : this.factorName;
    }

    public void setFactorName(String anFactorName) {
        this.factorName = anFactorName;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String anSupplier) {
        this.supplier = anSupplier;
    }

    public List<ScfElecAgreeStubInfo> getStubInfos() {
        return this.stubInfos;
    }

    public String getBillNo() {

        return this.billNo;
    }

    public void setBillNo(String anBillNo) {

        this.billNo = anBillNo;
    }

    public String getDescription() {

        return this.description;
    }

    public void setDescription(String anDescription) {

        this.description = anDescription;
    }

    public BigDecimal getConfirmBalance() {

        return this.confirmBalance;
    }

    public void setConfirmBalance(BigDecimal anConfirmBalance) {

        this.confirmBalance = anConfirmBalance;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String anProductName) {
        this.productName = anProductName;
    }

    public String getBillMode() {
        return this.billMode;
    }

    public void setBillMode(String anBillMode) {
        this.billMode = anBillMode;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(String anInvoiceDate) {
        this.invoiceDate = anInvoiceDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String anEndDate) {
        this.endDate = anEndDate;
    }

    //    public void addAttachValue(ScfRequest anRequest) {
//        if (anRequest != null) {
//            BeanMapperHelper.invoke(anRequest, this, "billNo:billNo;description:description;confirmBalance:confirmBalance;balance:balance");
//        }
//    }  
    /**
     * 设置合同签署状态，并赋予本方的签署状态
     * @param anStubInfoList 签署方信息列表
     * @param anCustNoList 操作员所对应的客户编号列表
     */
    public void putStubInfos(List<ScfElecAgreeStubInfo> anStubInfoList, Set<Long> anCustNoList) {
        this.stubInfos = anStubInfoList;
        
        //如果客户号和本方的客户号一致，则为本方的操作状态
        for(ScfElecAgreeStub elecAgreeStub : anStubInfoList){
            if (anCustNoList.contains(elecAgreeStub.getCustNo())){
               this.subscribed = elecAgreeStub.getOperStatus();
               break;
            }
        }
    }
 
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(super.toString());
        sb.append(", buyer=").append(buyer);
        sb.append(", supplier=").append(supplier);
        sb.append(", stubInfos=").append(stubInfos.toString());
        sb.append("]");
        return sb.toString();
    }

}
