package com.betterjr.modules.agreement.data;
 

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.agreement.entity.CustAgreement;

@ReferClass(CustAgreement.class)
public class ScfSupplierAgreement extends CustAgreement implements  BaseRemoteEntity {
 
    private static final long serialVersionUID = 2510026195159706675L;
    
    /**
     * 汇票编号
     */
    private String billNo;

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String anBillNo) {
        this.billNo = anBillNo;
    }

    public static ScfSupplierAgreement createInstance(CustAgreement anAgree, String anBillNo){
        ScfSupplierAgreement scfAgree = BeanMapper.map(anAgree, ScfSupplierAgreement.class);
        scfAgree.billNo = anBillNo;
        return scfAgree;
    }
}
