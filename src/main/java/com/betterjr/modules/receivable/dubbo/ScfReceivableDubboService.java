package com.betterjr.modules.receivable.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.receivable.IScfReceivableService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.receivable.entity.ScfReceivableDO;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;
import com.betterjr.modules.receivable.service.ScfReceivableService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfReceivableService.class)
public class ScfReceivableDubboService implements IScfReceivableService {

    @Autowired
    private ScfReceivableService scfReceivableService;

    @Autowired
    private ScfReceivableDOService receivableService;

    @Override
    public String webSaveModifyReceivable(Map<String, Object> anMap, Long anId, String anFileList,
            String anOtherFileList) {
        ScfReceivable anReceivable = (ScfReceivable) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("应收账款信息编辑成功",
                        scfReceivableService.saveModifyReceivable(anReceivable, anId, anFileList, anOtherFileList))
                .toJson();
    }

    @Override
    public String webQueryReceivable(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum,
            int anPageSize) {
        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("应收账款查询成功", scfReceivableService.queryReceivable(anQueryConditionMap,
                anIsOnlyNormal, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFindReceivableList(Map<String, Object> anMap, String anIsOnlyNormal) {
        Map<String, Object> queryMap = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("应收账款查询成功", scfReceivableService.findReceivableList(queryMap, anIsOnlyNormal)).toJson();
    }

    @Override
    public String webFindReceivableDetailsById(Long anId) {
        return AjaxObject.newOk("应收账款详情查询成功", scfReceivableService.findReceivableDetailsById(anId)).toJson();
    }

    @Override
    public String webAddReceivable(Map<String, Object> anMap, String anFileList, String anOtherFileList) {
        ScfReceivable anReceivable = (ScfReceivable) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("应收账款新增成功", scfReceivableService.addReceivable(anReceivable, anFileList, anOtherFileList))
                .toJson();
    }

    @Override
    public String webSaveAduitReceivable(Long anId) {

        return AjaxObject.newOk("应收账款审核成功", scfReceivableService.saveAduitReceivable(anId)).toJson();
    }

    @Override
    public String webAddReceivableDO(Map<String, Object> anAnMap, String anFileList, boolean anConfirmFlag) {

        ScfReceivableDO receivable = (ScfReceivableDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("应收账款新增成功", receivableService.addReceivable(receivable, anFileList, anConfirmFlag))
                .toJson();
    }

    @Override
    public String webSaveModifyReceivableDO(Map<String, Object> anAnMap, String anFileList, boolean anConfirmFlag) {

        ScfReceivableDO receivable = (ScfReceivableDO) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject
                .newOk("应收账款修改成功", receivableService.saveModifyReceivable(receivable, anFileList, anConfirmFlag))
                .toJson();

    }

    @Override
    public String webSaveAnnulReceivable(String anRefNo, String anVersion) {
        //
        return AjaxObject.newOk("应收账款废止成功", receivableService.saveAnnulReceivable(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webSaveAuditReceivableByRefNoVersion(String anRefNo, String anVersion, Map<String, Object> anAnMap) {

        return AjaxObject.newOk("应收账款审核成功", receivableService.saveAuditReceivable(anRefNo, anVersion, anAnMap))
                .toJson();
    }

    @Override
    public String webFindReceivableByRefNoVersion(String anRefNo, String anVersion) {

        return AjaxObject.newOk("应收账款查询成功", receivableService.findReceivable(anRefNo, anVersion)).toJson();
    }

    @Override
    public String webQueryIneffectiveReceivable(Map<String, Object> anAnMap, String anFlag, int anPageNum,
            int anPageSize, boolean anIsAudit) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("应收账款查询成功", receivableService.queryIneffectiveReceivable(anQueryConditionMap,
                anFlag, anPageNum, anPageSize, anIsAudit)).toJson();

    }

    @Override
    public String webQueryEffectiveReceivable(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize,
            boolean anIsCust) {

        Map<String, Object> anQueryConditionMap = (Map<String, Object>) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("应收账款查询成功", receivableService.queryEffectiveReceivable(anQueryConditionMap,
                anFlag, anPageNum, anPageSize, anIsCust)).toJson();

    }

}
