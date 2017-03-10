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
        this.saveFile(anTemplate, anFileList);
		
        anTemplate.initValue();
        this.insert(anTemplate);
        return anTemplate;
    }
    
    public ScfContractTemplate findTemplateByType(Long anFactorNo, String anType){
        BTAssert.notNull(anFactorNo, "核心企业编号不允许为空！");
        BTAssert.notNull(anType, "模板类型不允许为空！");
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("templateType", anType);
        anPropValue.put("factorNo", anFactorNo);
        
        List<ScfContractTemplate> list = this.selectByClassProperty(ScfContractTemplate.class, anPropValue);
        if (Collections3.isEmpty(list) || list.size() == 0) {
            logger.debug("没有查到模板数据！");
            return null;
        }

        return Collections3.getFirst(list);
    }
    
    public ScfContractTemplate findTemplate(Long anId){
        BTAssert.notNull(anId, "id不能为空！");
        return this.selectByPrimaryKey(anId);
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
        
        this.saveFile(anTemplate, anFileList);
           
        this.updateByPrimaryKeySelective(anTemplate);
        return anTemplate;
    }

	private void saveFile(ScfContractTemplate anTemplate, String anFileList) {
		//保存上存的文件
		Long batchNo = custFileService.updateCustFileItemInfo(anFileList, null);
		anTemplate.setBatchNo(batchNo);
		
		//从文件表中查询模板路径保存到模板表中
        CustFileItem item = custFileService.findOneByBatchNo(batchNo);
        anTemplate.setTemplatePath(item.getFilePath());
        anTemplate.setTemplateName(item.getFileName());
	}

    public Page<ScfContractTemplate> queryTemplate(Map anMap, int anFlag, int anPageNum, int anPageSize) {
    	System.out.println(anMap);
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
