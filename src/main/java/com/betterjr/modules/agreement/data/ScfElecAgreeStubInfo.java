package com.betterjr.modules.agreement.data;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.agreement.entity.ScfElecAgreeStub;

@ReferClass(ScfElecAgreeStub.class)
public class ScfElecAgreeStubInfo extends ScfElecAgreeStub {
    private static final long serialVersionUID = -5721824329491867243L;

    private String custName;

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public ScfElecAgreeStubInfo() {

    }

    public ScfElecAgreeStubInfo(ScfElecAgreeStub anStub, String anCustName) {
        BeanMapper.copy(anStub, this);
        this.custName = anCustName;
    }
}
