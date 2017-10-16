package com.betterjr.modules.template.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.template.dao.ScfContractTemplateMapper;
import com.betterjr.modules.template.entity.ScfContractTemplate;

@Service
public class ScfContractTemplateService extends BaseService<ScfContractTemplateMapper, ScfContractTemplate> {
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;

    @Autowired
    private CustAccountService custAccountService;

    public ScfContractTemplate addTemplate(Map<String, Object> anParam, String anFileList) {

        BTAssert.notNull(anParam, "核心企业编号不允许为空！");
        anParam = Collections3.filterMapEmptyObject(anParam);
        BTAssert.notNull(anParam.get("factorNo"), "核心企业编号不允许为空！");
        BTAssert.notNull(anParam.get("templateType"), "模板类型不允许为空！");
        BTAssert.notNull(anParam.get("templateStatus"), "请选择模板状态！");
        String[] dt = new String[] { "factorNo", "templateType", "templateStatus" };
        anParam = Collections3.filterMap(anParam, dt);
        // 重复检查
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("templateType", anParam.get("templateType"));
        anPropValue.put("factorNo", anParam.get("factorNo"));
        BTAssert.isNull(findTemplate(anPropValue), "该企业自定义模板中已存在此类模板，不允许重复添加！");
        ScfContractTemplate template = new ScfContractTemplate();
        try {
            BeanUtils.populate(template, anParam);
        }
        catch (IllegalAccessException e) {
            BTAssert.notNull(null, "数据异常");
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            BTAssert.notNull(null, "数据异常");
            e.printStackTrace();
        }
        template.setFactorName(custAccountService.queryCustName(template.getFactorNo()));
        this.saveFile(template, anFileList);
        template.initValue();

        this.insert(template);
        return template;
    }

    public ScfContractTemplate findTemplateByType(Long anFactorNo, String anType, String anStatus) {
        BTAssert.notNull(anFactorNo, "核心企业编号不允许为空！");
        BTAssert.notNull(anType, "模板类型不允许为空！");
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("templateType", anType);
        anPropValue.put("factorNo", anFactorNo);
        anPropValue.put("templateStatus", anStatus);

        return findTemplate(anPropValue);
    }

    public ScfContractTemplate findTemplate(Long anId) {
        BTAssert.notNull(anId, "id不能为空！");
        return this.selectByPrimaryKey(anId);
    }

    private ScfContractTemplate findTemplate(Map<String, Object> anPropValue) {
        List<ScfContractTemplate> list = this.selectByClassProperty(ScfContractTemplate.class, anPropValue);
        if (Collections3.isEmpty(list) || list.size() == 0) {
            logger.debug("没有查到模板数据！");
            return null;
        }
        return Collections3.getFirst(list);
    }

    /**
     * 修改融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfContractTemplate saveModifyTemplate(ScfContractTemplate anTemplate, Long anId, String anFileList) {
        BTAssert.notNull(anTemplate, "修改数据失败-模板数据不能为空");
        BTAssert.notNull(anId, "修改数据失败-id不能为空");
        BTAssert.notNull(this.selectByPrimaryKey(anId), "修改数据失败-没找到对应的模板数据");

        if (anFileList.length() > 0) {
            this.saveFile(anTemplate, anFileList);
        }

        anTemplate.setId(anId);
        this.updateByPrimaryKeySelective(anTemplate);
        return anTemplate;
    }

    /**
     * 修改合同模版
     * 
     * @param anMap
     * @param anId
     * @param anFileList
     * @return
     */
    public ScfContractTemplate saveUpdateTemplate(Map<String, Object> anMap, Long anId, String anFileList) {
        BTAssert.notNull(anMap, "修改数据失败-模板数据不能为空");
        BTAssert.notNull(anId, "修改数据失败-id不能为空");
        BTAssert.notNull(anMap.get("templateStatus"), "修改数据失败-没找到对应的模板数据");
        ScfContractTemplate anTemplate = this.selectByPrimaryKey(anId);
        if (anFileList.length() > 0) {
            this.saveFile(anTemplate, anFileList);
        }
        anTemplate.setTemplateStatus(anMap.get("templateStatus").toString());
        this.updateByPrimaryKeySelective(anTemplate);
        return anTemplate;
    }

    /**
     * 保存上存的文件
     * 
     * @param anTemplate
     * @param anFileList
     */
    private void saveFile(ScfContractTemplate anTemplate, String anFileList) {
        Long batchNo = custFileService.updateCustFileItemInfo(anFileList, null);
        anTemplate.setBatchNo(batchNo);

        // 从文件表中查询模板路径保存到模板表中
        CustFileItem item = custFileService.findOneByBatchNo(batchNo);
        anTemplate.setTemplatePath(item.getFilePath());
        anTemplate.setTemplateName(item.getFileName());
    }

    public Page<ScfContractTemplate> queryTemplate(Map anMap, int anFlag, int anPageNum, int anPageSize) {
        String[] dt = new String[] { "factorNo", "templateType", "templateStatue" };
        anMap = Collections3.filterMap(anMap, dt);
        anMap = Collections3.filterMapEmptyObject(anMap);
        Page<ScfContractTemplate> tempPage = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag));
        for (ScfContractTemplate template : tempPage) {
            String factorName = custAccountService.queryCustName(template.getFactorNo());
            if (null != custAccountService.queryCustName(template.getFactorNo())) {
                template.setFactorName(factorName);
            }
        }
        return tempPage;
    }
}
