package com.betterjr.modules.agreement.dubbo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.agreement.IScfElecAgreementService;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.entity.ScfRequestOpinion;
import com.betterjr.modules.agreement.entity.ScfRequestProtacal;
import com.betterjr.modules.agreement.service.ScfAgreementService;
import com.betterjr.modules.agreement.service.ScfElecAgreementService;
import com.betterjr.modules.agreement.service.ScfOtherFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

/***
 * dubbo 服务
 * @author hubl
 *
 */
@Service(interfaceClass=IScfElecAgreementService.class)
public class ScfElecAgreementDubboService implements IScfElecAgreementService {

    @Autowired
    private ScfElecAgreementService scfElecAgreementService;
    @Autowired
    private ScfAgreementService scfAgreementService;
    @Autowired
    private ScfOtherFileService scfOtherFileService;
    
    @Override
    public String webQueryElecAgreementByPage(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        return AjaxObject.newOkWithPage("查询用户电子合同分页信息成功",scfElecAgreementService.queryScfElecAgreementList(anParam, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webCancelElecAgreement(String anAppNo) {
        if(scfElecAgreementService.saveAndCancelElecAgreement(anAppNo)){
            return AjaxObject.newOk("取消电子合同的流水号成功").toJson();
        }else{
            return AjaxObject.newError("取消电子合同失败").toJson();
        }
    }

    @Override
    public String webFindElecAgreePage(String anAppNo) {
        return AjaxObject.newOk(scfAgreementService.createOutHtmlInfo(anAppNo)).toJson();
    }

    @Override
    public String webFindValidCode(String anAppNo, String anCustType) {
        if(scfElecAgreementService.saveAndSendSMS(anAppNo)){
            return AjaxObject.newOk("获取签署合同的验证码成功").toJson();
        }else{
            return AjaxObject.newError("获取签署合同的验证码失败，请稍后重试！").toJson();
        }
    }

    @Override
    public String webSendValidCode(String anAppNo, String anCustType, String anVcode) {
        return AjaxObject.newOk(scfAgreementService.sendValidCode(anAppNo, anCustType, anVcode)).toString();
    }
    
    /***
     * 添加电子合同
     * @param anMap  
     *            电子合同信息
     * @param anCustNoList  
     *            电子合同的签约方信息
     */
    public void webAddElecAgreementInfo(Map<String, Object> anMap, List<Long> anCustNoList){
        ScfElecAgreement scfElecAgreement=(ScfElecAgreement) RuleServiceDubboFilterInvoker.getInputObj();
        scfElecAgreementService.addElecAgreementInfo(scfElecAgreement, anCustNoList);
    }
    
    /***
     * 转让通知书
     * @param anRequestNo 申请单号
     * @param anFactorRequestNo 保理商单号
     * @return
     */
    public boolean webTransNotice(Map<String, Object> anMap){
        ScfRequestNotice noticeRequest=(ScfRequestNotice)RuleServiceDubboFilterInvoker.getInputObj();
        return scfAgreementService.transNotice(noticeRequest);
    }
    
    
    public boolean webTransProtacal(Map<String, Object> anMap){
        ScfRequestProtacal protacal=new ScfRequestProtacal();
        protacal.initProtacal(anMap);
        return scfAgreementService.transProtacal(protacal);
    }
    
    
    /***
     * 确认意见书
     * @param anRequestNo 申请单号
     * @param anFactorRequestNo 保理商单号
     * @return
     */
    public boolean webTransOpinion(Map<String, Object> anMap){
        ScfRequestOpinion opinion=(ScfRequestOpinion)RuleServiceDubboFilterInvoker.getInputObj();
        return scfAgreementService.transOpinion(opinion);
    }
    
    /****
     * 根据申请单号，类型查询合同
     * @param anRequestNo
     * @param anSignType
     * @return
     */
    public String webFindElecAgreeByOrderNo(String anRequestNo, String anSignType){
        return AjaxObject.newOk(scfElecAgreementService.findElecAgreeByOrderNo(anRequestNo, anSignType)).toJson();
    }
    
    /***
     * 查找pdf文件
     * @param appNo
     * @return
     */
    public CustFileItem webFindPdfFileInfo(String appNo){
        return scfAgreementService.findPdfFileInfo(appNo);
    }
    
    /****
     * 添加其它资料
     * @param anMap
     * @return
     */
    public String webAddOtherFile(Map<String, Object> anMap){
       if(scfOtherFileService.addOtherFile(anMap)){
           return AjaxObject.newOk("资料添加成功").toJson();
       }else{
           return AjaxObject.newError("资料添加失败").toJson();
       }
    }
    
    /***
     * 根据申请单号查询其它资料信息
     * @param anRequestNo
     * @return
     */
    public String webQueryOtherFile(String anRequestNo){
        return AjaxObject.newOk("查询其它资料",scfOtherFileService.queryOtherFile(anRequestNo)).toJson();
    }
    
    /***
     * 删除其它资料
     * @param anOtherFileId
     * @return
     */
    public String webDelOtherFile(Long anOtherFileId){
        if(scfOtherFileService.delOtherFile(anOtherFileId)){
            return AjaxObject.newOk("资料删除成功").toJson();
        }else{
            return AjaxObject.newError("资料删除失败").toJson();
        }
    }
}
