package com.betterjr.modules.acceptbill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillDOMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBillDO;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
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
        
        BTAssert.notNull(anAcceptBill,"票据登记失败，原因：保存数据异常");
        BTAssert.notNull(anAcceptBill.getCoreCustNo(),"票据登记失败，原因：请选择开票单位");
        BTAssert.notNull(anAcceptBill.getSupplierNo(),"票据登记失败，原因：请选择收款人");
        logger.info("Begin to add addAcceptBill"+UserUtils.getOperatorInfo().getName());
        anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
        anAcceptBill.setInvoiceCorp(anAcceptBill.getCoreCustName());
        anAcceptBill.initAddValue(UserUtils.getOperatorInfo(),confirmFlag);
        //操作机构设置为供应商
        anAcceptBill.setOperOrg(baseService.findBaseInfo(anAcceptBill.getSupplierNo()).getOperOrg());
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        this.insertVersion(anAcceptBill);
        logger.info("success to add addAcceptBill"+UserUtils.getOperatorInfo().getName());
        return anAcceptBill;
    }
    
    /**
     * 
     * @param anModiBill  修改的票据对象详情
     * @param anFileList  文件上次批次好
     * @param anConfirmFlag 是否修改加确认 true 标识确认    false 标识只是修改
     * @return
     */
    public ScfAcceptBillDO saveModifyAcceptBill(ScfAcceptBillDO anModiBill,String anFileList,boolean anConfirmFlag) {
        
        BTAssert.notNull(anModiBill,"票据修改失败,原因：对象为空");
        BTAssert.notNull(anModiBill.getRefNo(),"票据修改失败,原因：凭证编号为空");
        BTAssert.notNull(anModiBill.getVersion(),"票据修改失败,原因：票据信息不全");
        logger.info("Begin to modify bill");
        ScfAcceptBillDO bill = this.selectOneWithVersion(anModiBill.getRefNo(),anModiBill.getVersion());
        BTAssert.notNull(bill, "票据修改失败,原因：无法获取票据信息");
        //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
        checkOperatorModifyStatus(UserUtils.getOperatorInfo(),bill);
        // 应收账款信息变更迁移初始化
        //order.setCustName(custAccountService.queryCustName(anModiBill.getCustNo()));
        anModiBill.setCoreCustName(custAccountService.queryCustName(anModiBill.getCoreCustNo()));
        anModiBill.setInvoiceCorp(anModiBill.getCoreCustName());
        anModiBill.initModifyValue(bill,anConfirmFlag);
        // 保存附件信息
        anModiBill.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, bill.getBatchNo()));
        // 初始版本更改 直接返回
        if(bill.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
                bill.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_DRAFT)){
            if(anConfirmFlag){
                anModiBill.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
            }
             // 数据存盘
            this.updateByPrimaryKeySelective(anModiBill);
            return anModiBill;
        }
        // 需要升级版本的修改
        //this.updateByPrimaryKeySelective(anOrder);
        if(anConfirmFlag){
            anModiBill.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
        }
        //ScfOrderDO order2 = this.selectOneWithVersion(order.getRefNo(),order.getVersion());
        anModiBill=this.updateVersionByPrimaryKeySelective(anModiBill,anModiBill.getRefNo(),anModiBill.getVersion());
        BTAssert.notNull(anModiBill, "票据修改失败,原因：数据异常,请检查");
        logger.info("success to modify bill"+UserUtils.getOperatorInfo().getName());
        return anModiBill;
    }
    
    /**
     * 作废当前票据
     * @param refNo
     * @param version
     * @return
     */
    public ScfAcceptBillDO annulBill(String refNo,String version){
        
        BTAssert.notNull(refNo, "凭证单号为空!操作失败");
        BTAssert.notNull(version, "数据版本为空!操作失败");
        logger.info("begin to aunul bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(refNo, version);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        bill=this.annulOperator(UserUtils.getOperatorInfo(), bill);
        logger.info("success to aunul bill"+UserUtils.getOperatorInfo().getName());
        return bill;
    }
    
    /**
     * 查询票据详情
     * @param anRefNo
     * @param anVersion
     * @return
     */
    public ScfAcceptBillDO findBill(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "数据版本为空!操作失败");
        logger.info("begin  to find bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        logger.info("success to find bill"+UserUtils.getOperatorInfo().getName());
        return bill;
    }
    
    
    public ScfAcceptBillDO saveAuditBill(String anRefNo,String anVersion){
        
        BTAssert.notNull(anRefNo, "凭证单号为空!操作失败");
        BTAssert.notNull(anVersion, "数据版本为空!操作失败");
        logger.info("begin to audit bill"+UserUtils.getOperatorInfo().getName());
        ScfAcceptBillDO bill = this.selectOneWithVersion(anRefNo, anVersion);
        BTAssert.notNull(bill, "此票据异常!操作失败");
        this.auditOperator(UserUtils.getOperatorInfo(), bill);
        logger.info("success to audit bill"+UserUtils.getOperatorInfo().getName());
        return bill;
        
    }
    
}
