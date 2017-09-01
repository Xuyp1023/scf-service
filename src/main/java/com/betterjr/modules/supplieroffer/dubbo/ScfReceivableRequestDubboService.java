package com.betterjr.modules.supplieroffer.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.supplieroffer.IScfReceivableRequestService;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;


@Service(interfaceClass=IScfReceivableRequestService.class)
public class ScfReceivableRequestDubboService implements IScfReceivableRequestService {

    
    @Autowired
    private ScfReceivableRequestService requestService;
    @Override
    public String webSaveAddRequest(Map<String, Object> anParamMap) {
        
        return AjaxObject.newOk("融资请求新增成功", requestService.saveAddRequest(anParamMap)).toJson();
    }

    @Override
    public String webSaveSubmitRequest(String anRequestNo, String anRequestPayDate, String anDescription) {
        
        return AjaxObject.newOk("融资请求申请成功", requestService.saveSubmitRequest(anRequestNo, anRequestPayDate, anDescription)).toJson();
    }

    @Override
    public String webSaveSupplierSignAgreement(String anRequestNo) {
        
        return AjaxObject.newOk("供应商签署合同成功", requestService.saveSupplierSignAgreement(anRequestNo)).toJson();
    }

    @Override
    public String webSaveSupplierFinishConfirmRequest(String anRequestNo, String anRequestPayDate, String anDescription) {
        
        return AjaxObject.newOk("供应商确认申请成功", requestService.saveSupplierFinishConfirmRequest(anRequestNo, anRequestPayDate, anDescription)).toJson();
    }

    @Override
    public String webSaveAnnulReceivableRequest(String anRequestNo) {
        
        return AjaxObject.newOk("废止申请成功", requestService.saveAnnulReceivableRequest(anRequestNo)).toJson();
    }

    @Override
    public String webSaveCoreSignAgreement(String anRequestNo) {
        
        return AjaxObject.newOk("核心企业签署合同成功", requestService.saveCoreSignAgreement(anRequestNo)).toJson();
    }

    @Override
    public String webSaveCoreFinishPayRequest(String anRequestNo, String anRequestPayDate, String anDescription) {
        
        return AjaxObject.newOk("核心企业完成付款成功", requestService.saveCoreFinishPayRequest(anRequestNo, anRequestPayDate, anDescription)).toJson();
    }

    @Override
    public String webFindOneByRequestNo(String anRequestNo) {
        
        return AjaxObject.newOk("查询申请成功", requestService.findOneByRequestNo(anRequestNo)).toJson();
    }

    @Override
    public String webQueryReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("查询申请成功",requestService.queryReceivableRequestWithSupplier(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryFinishReceivableRequestWithSupplier(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("查询申请成功",requestService.queryFinishReceivableRequestWithSupplier(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("查询申请成功",requestService.queryReceivableRequestWithCore(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webQueryFinishReceivableRequestWithCore(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("查询申请成功",requestService.queryFinishReceivableRequestWithCore(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webSaveAddRequestFour(Map<String, Object> anParamMap) {
        
        return AjaxObject.newOk("申请成功", requestService.saveAddRequestFour(anParamMap)).toJson();
    }

    @Override
    public String webQueryReceivableRequestFour(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize, boolean anIsCust) {
        
        return AjaxObject.newOkWithPage("查询申请成功",requestService.queryReceivableRequestFour(anMap, anFlag, anPageNum, anPageSize,anIsCust)).toJson();
    }

    @Override
    public String webSaveConfirmReceivableRequestFour(String anRequestNo) {
        
        return AjaxObject.newOk("确认成功", requestService.saveConfirmReceivableRequestFour(anRequestNo)).toJson();
    }

    @Override
    public String webSaveRejectReceivableRequestFour(String anRequestNo) {
        
        return AjaxObject.newOk("拒绝成功", requestService.saveRejectReceivableRequestFour(anRequestNo)).toJson();
    }

}
