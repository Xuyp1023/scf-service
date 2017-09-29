package com.betterjr.modules.supplieroffer.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.supplieroffer.dao.ScfAgreementTemplateMapper;
import com.betterjr.modules.supplieroffer.data.AgreementConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfAgreementTemplate;

@Service
public class ScfAgreementTemplateService extends BaseService<ScfAgreementTemplateMapper, ScfAgreementTemplate> {

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Autowired
    private CustAccountService custAccountService;

    /**
     * 上传合同模版， coreCustNo templateFileId anIsOperator 是否是平台
     * 
     * @param anAgrement
     * @return
     */
    public ScfAgreementTemplate saveAddAgreementTemplate(ScfAgreementTemplate anAgrement, boolean anIsOperator) {

        BTAssert.notNull(anAgrement, "插入合同模版为空,操作失败");
        BTAssert.notNull(anAgrement.getCoreCustNo(), "请选择供应商,操作失败");
        BTAssert.notNull(anAgrement.getTemplateFileId(), "请选择上传的文件,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        logger.info("Begin to add saveAddAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        ScfAgreementTemplate template = findAgreementTemplate(anAgrement.getCoreCustNo());
        BTAssert.isNull(template, "您已有合同模版,请不要重复上传");
        CustFileItem fileItem = custFileDubboService.findOne(anAgrement.getTemplateFileId());
        anAgrement.setTemplateFileName(fileItem.getFileName());
        if (fileItem.getBatchNo() == null || fileItem.getBatchNo().equals(0)) {
            anAgrement.setTemplateBatchNo(
                    custFileDubboService.updateCustFileItemInfo(anAgrement.getTemplateFileId() + "", anAgrement.getTemplateBatchNo()));
        }
        anAgrement.saveAddValue(UserUtils.getOperatorInfo());
        anAgrement.setCoreCustName(custAccountService.queryCustName(anAgrement.getCoreCustNo()));
        anAgrement.setOperOrg(baseService.findBaseInfo(anAgrement.getCoreCustNo()).getOperOrg());
        // 平台默认完成合同模版的所有操作
        if (anIsOperator) {

            anAgrement.saveAllOperatorValue(UserUtils.getOperatorInfo());
        }
        this.insert(anAgrement);
        logger.info("end to add saveAddAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        return anAgrement;
    }

    /**
     * 删除合同模版
     * 
     * @param anId
     * @return
     */
    public ScfAgreementTemplate saveDeleteAgreementTemplate(Long anId) {

        BTAssert.notNull(anId, "删除的模版为空,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        ScfAgreementTemplate template = this.selectByPrimaryKey(anId);
        BTAssert.notNull(template, "删除的模版为空,操作失败");
        logger.info("begin to add saveDeleteAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        checkStatus(template.getOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有当前企业的操作权限！操作失败");
        template.saveDeleteValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(template);

        logger.info("end to  saveDeleteAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        return template;

    }

    /**
     * 激活合同模版信息
     * 
     * @param anId
     * @return
     */
    public ScfAgreementTemplate saveActiveAgreementTemplate(Long anId) {

        BTAssert.notNull(anId, "激活的模版为空,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        ScfAgreementTemplate template = this.selectByPrimaryKey(anId);
        BTAssert.notNull(template, "激活的模版为空,操作失败");
        logger.info("begin to add saveActiveAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        checkStatus(template.getOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有当前企业的操作权限！操作失败");
        checkStatus(template.getBusinStatus(), AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_EFFECTIVE, false, "当前模版尚未制作FTL！操作失败");
        template.setBusinStatus(AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_ACTIVATE);
        this.updateByPrimaryKeySelective(template);
        logger.info("end to add saveActiveAgreementTemplate" + UserUtils.getOperatorInfo().getName());
        return template;

    }

    /**
     * 查询当前企业的各个状态下的合同信息
     * 
     * @param anCoreCustNo
     * @param anBusinStatus
     * @return
     */
    public ScfAgreementTemplate findAgreementTemplateWithStatus(Long anCoreCustNo, String anBusinStatus) {

        BTAssert.notNull(anCoreCustNo, "请选择查询的企业,操作失败");
        BTAssert.notNull(anBusinStatus, "查询条件不全,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        ScfAgreementTemplate template = findAgreementTemplate(anCoreCustNo);
        if (template != null && anBusinStatus.equals(template.getBusinStatus())) {
            return template;
        }

        BTAssert.notNull(null, "未查到符合条件的合同模版");
        return template;

    }

    /**
     * 平台给每个企业线下制作ftl之后上传ftl模版信息
     * 
     * @param anId
     * @param anFileId
     * @return
     */
    public ScfAgreementTemplate saveUploadFtlAgreement(Long anId, Long anFileId) {

        BTAssert.notNull(anId, "请选择合同模版,操作失败");
        BTAssert.notNull(anFileId, "未设置上传的ftl文件,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        logger.info("begin to add saveUploadFtlAgreement" + UserUtils.getOperatorInfo().getName());
        ScfAgreementTemplate template = this.selectByPrimaryKey(anId);
        BTAssert.notNull(template, "未找到合同模版,操作失败");
        checkStatus(template.getBusinStatus(), AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_NOEFFECTIVE, false, "当前模版不符合上传ftl条件！操作失败");
        template.saveFtlUpdateValue(UserUtils.getOperatorInfo());
        template.setFtlFileId(anFileId);
        CustFileItem fileItem = custFileDubboService.findOne(anFileId);
        if (fileItem.getBatchNo() == null || fileItem.getBatchNo().equals(0)) {
            template.setTemplateBatchNo(custFileDubboService.updateCustFileItemInfo(anFileId + "", template.getFtlBatchNo()));
        }

        this.updateByPrimaryKeySelective(template);
        logger.info("end to  saveUploadFtlAgreement" + UserUtils.getOperatorInfo().getName());
        return template;

    }

    /**
     * 平台删除合同模版的ftl文件 去除制作时间和状态
     * 
     * @param anId
     * @return
     */
    public ScfAgreementTemplate saveDeleteFtlAgreement(Long anId) {

        BTAssert.notNull(anId, "请选择合同模版,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        logger.info("begin to add saveDeleteFtlAgreement" + UserUtils.getOperatorInfo().getName());
        ScfAgreementTemplate template = this.selectByPrimaryKey(anId);
        BTAssert.notNull(template, "未找到合同模版,操作失败");
        checkStatus(template.getBusinStatus(), AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_ACTIVATE, true, "当前模版已经激活！操作失败");
        template.saveDeleteFtlValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(template);
        logger.info("end to saveDeleteFtlAgreement" + UserUtils.getOperatorInfo().getName());
        return template;

    }

    /**
     * 查询当前企业可用的合同模版信息 coreCustNo
     * 
     * @param anCoreCustNo
     * @return
     */
    public ScfAgreementTemplate findAgreementTemplate(Long anCoreCustNo) {

        Map<String, Object> paramMap = QueryTermBuilder.newInstance().put("coreCustNo", anCoreCustNo)
                .put("NEbusinStatus", AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_DELETE)
                .put("operOrg", baseService.findBaseInfo(anCoreCustNo).getOperOrg()).build();

        List<ScfAgreementTemplate> list = this.selectByProperty(paramMap);

        if (!Collections3.isEmpty(list)) {

            return Collections3.getFirst(list);
        }
        else {

            return null;
        }

    }

    /**
     * 平台查询所有的合同模版信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfAgreementTemplate> queryAgreementTemplatePage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "coreCustName", "coreCustNo" });
        Collections3.fuzzyMap(anMap, new String[] { "coreCustName" });
        Page<ScfAgreementTemplate> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");

        return page;
    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

}
