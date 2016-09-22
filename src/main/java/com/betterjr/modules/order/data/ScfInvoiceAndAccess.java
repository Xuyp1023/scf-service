package com.betterjr.modules.order.data;

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.order.entity.ScfInvoice;

@ReferClass(ScfInvoice.class)
public class ScfInvoiceAndAccess extends ScfInvoice implements  BaseRemoteEntity {
    private static final long serialVersionUID = 2216709871930966583L;

    private Long itemId;

    private String itemName;

    private String fileType;

    /**
     * 合同编号
     */
    private String agreeNo;
    
    private Long custNo;

    public String getAgreeNo() {
        return this.agreeNo;
    }

    public void setAgreeNo(String anAgreeNo) {
        this.agreeNo = anAgreeNo;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long anItemId) {
        this.itemId = anItemId;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String anItemName) {
        this.itemName = anItemName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String anFileType) {
        this.fileType = anFileType;
    }

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(super.toString());
        sb.append(", itemId=").append(itemId);
        sb.append(", itemName=").append(itemName);
        sb.append(", fileType=").append(fileType);
        sb.append("]");
        return sb.toString();
    }
    
    public void initFileItemValue(CustFileItem anItem){
       if (anItem != null){
         this.itemName = anItem.getFileName();
         this.itemId = anItem.getId();
         this.fileType = anItem.getFileType();         
       }
    }
}
