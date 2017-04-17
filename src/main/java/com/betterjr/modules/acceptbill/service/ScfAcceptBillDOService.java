package com.betterjr.modules.acceptbill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillDOMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfAcceptBillDOService extends BaseVersionService<ScfAcceptBillDOMapper, ScfAcceptBillDO> {


    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    
    /**
     * 新增汇票信息
     */
    public ScfAcceptBillDO addAcceptBill(ScfAcceptBillDO anAcceptBill,String anFileList,boolean confirmFlag) {
        
        anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
        anAcceptBill.setInvoiceCorp(anAcceptBill.getCoreCustName());
        anAcceptBill.initAddValue(UserUtils.getOperatorInfo(),confirmFlag);
        //操作机构设置为供应商
        anAcceptBill.setOperOrg(baseService.findBaseInfo(anAcceptBill.getSupplierNo()).getOperOrg());
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        this.insertVersion(anAcceptBill);
        return anAcceptBill;
    }
}
