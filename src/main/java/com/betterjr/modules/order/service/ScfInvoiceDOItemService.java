package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.order.dao.ScfInvoiceDOItemMapper;
import com.betterjr.modules.order.entity.ScfInvoiceDOItem;

@Service
public class ScfInvoiceDOItemService extends BaseService<ScfInvoiceDOItemMapper, ScfInvoiceDOItem>  {

    
    public List<ScfInvoiceDOItem> saveAddInvoiceItem(List<ScfInvoiceDOItem> invoiceItem,Long invoiceId){
        
        if(invoiceItem ==null){
            return new ArrayList<>();
        }
        for (ScfInvoiceDOItem scfInvoiceDOItem : invoiceItem) {
            scfInvoiceDOItem.initAdd(invoiceId);
            this.insertSelective(scfInvoiceDOItem);
        }
        return invoiceItem;
    }
    
    /**
     * 查询发票详情信息
     * 
     */
    public List<ScfInvoiceDOItem> queryInvoiceItemsByInvoiceId(Long invoiceId){
        
        BTAssert.notNull(invoiceId, "查询的发票号为空!");
        Map<String,Object> paramMap= QueryTermBuilder.newInstance().put("invoiceId", invoiceId).build();
        return this.selectByClassProperty(ScfInvoiceDOItem.class, paramMap, "id");
        
    }
    

    /**
     * 新增或者修改发票明细，如果明细中带了id 则修改记录，如果明细中没带id 则新增记录
     * @param anInvoiceItemList
     * @param anInvoiceId 
     * @return
     */
    public List<ScfInvoiceDOItem> saveUpdateOrModifyItemList(List<ScfInvoiceDOItem> anInvoiceItemList, Long anInvoiceId) {
        
        if(anInvoiceItemList ==null){
            return new ArrayList<>();
        }
        List<ScfInvoiceDOItem> itemList=new ArrayList<>();
        for (ScfInvoiceDOItem scfInvoiceDOItem : anInvoiceItemList) {
            if (scfInvoiceDOItem.getId()!=null) {
                scfInvoiceDOItem.setInvoiceId(anInvoiceId);
                this.updateByPrimaryKeySelective(scfInvoiceDOItem);
            }else{
                scfInvoiceDOItem.initAdd(anInvoiceId);
                this.insertSelective(scfInvoiceDOItem);
            }
            itemList.add(scfInvoiceDOItem);
        }
        
        return itemList;
    }
}
