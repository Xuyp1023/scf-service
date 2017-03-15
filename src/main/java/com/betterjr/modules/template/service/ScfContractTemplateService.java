package com.betterjr.modules.template.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ScfContractTemplateService extends BaseService<ScfContractTemplateMapper, ScfContractTemplate>{
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;
    
    @Autowired
    private CustAccountService custAccountService;
    
    public ScfContractTemplate addTemplate(ScfContractTemplate anTemplate, String anFileList){
    	//重复检查
    	Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("templateType", anTemplate.getTemplateType());
        anPropValue.put("factorNo", anTemplate.getFactorNo());
    	BTAssert.isNull(findTemplate(anPropValue), "该企业自定义模板中已存在此类模板，不允许重复添加！");
    	
        this.saveFile(anTemplate, anFileList);
        anTemplate.initValue();
        this.insert(anTemplate);
        return anTemplate;
    }
    
    public ScfContractTemplate findTemplateByType(Long anFactorNo, String anType, String anStatus){
        BTAssert.notNull(anFactorNo, "核心企业编号不允许为空！");
        BTAssert.notNull(anType, "模板类型不允许为空！");
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("templateType", anType);
        anPropValue.put("factorNo", anFactorNo);
        anPropValue.put("templateStatus", anStatus);
        
        return findTemplate(anPropValue);
    }
    
    public ScfContractTemplate findTemplate(Long anId){
        BTAssert.notNull(anId, "id不能为空！");
        return this.selectByPrimaryKey(anId);
    }
    
    private ScfContractTemplate findTemplate(Map<String, Object> anPropValue){
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
        
        if(anFileList.length() > 0){
        	this.saveFile(anTemplate, anFileList);
        }
        
        anTemplate.setId(anId);
        this.updateByPrimaryKeySelective(anTemplate);
        return anTemplate;
    }

    /**
     * 保存上存的文件
     * @param anTemplate
     * @param anFileList
     */
	private void saveFile(ScfContractTemplate anTemplate, String anFileList) {
		Long batchNo = custFileService.updateCustFileItemInfo(anFileList, null);
		anTemplate.setBatchNo(batchNo);
		
		//从文件表中查询模板路径保存到模板表中
        CustFileItem item = custFileService.findOneByBatchNo(batchNo);
        anTemplate.setTemplatePath(item.getFilePath());
        anTemplate.setTemplateName(item.getFileName());
	}

    public Page<ScfContractTemplate> queryTemplate(Map anMap, int anFlag, int anPageNum, int anPageSize) {
    	String[] dt = new String[]{"factorNo", "templateType", "templateStatue"};
    	anMap = Collections3.filterMap(anMap, dt);
    	Page<ScfContractTemplate> tempPage = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
    	for (ScfContractTemplate template : tempPage) {
    		String factorName = custAccountService.queryCustName(template.getFactorNo());
    		if(null != custAccountService.queryCustName(template.getFactorNo())){
    			template.setFactorName(factorName);
    		}
		}
        return tempPage;
	}
}
