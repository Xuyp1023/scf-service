package com.betterjr.modules.payorder.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.payorder.IPayOrderFileService;
import com.betterjr.modules.payorder.service.PayOrderFileService;

/**
 * 
 * @ClassName: PayOrderFileDubboService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:41:30 
 *
 */
@Service(interfaceClass = IPayOrderFileService.class)
public class PayOrderFileDubboService implements IPayOrderFileService {
    
    @Autowired
    private PayOrderFileService fileService;

    @Override
    public String webSaveAddFile(String anRequestPayDate, String anFlag) {
        
        return AjaxObject.newOk("付款文件新增成功", fileService.saveAddFile(anRequestPayDate, anFlag)).toJson();
    }

    @Override
    public String webQueryFileList(String anRequestPayDate, String anbusinStatus, String anInfoType) {
        
        return AjaxObject.newOk("付款文件查询成功", fileService.queryFileList(anRequestPayDate, anbusinStatus, anInfoType)).toJson();
    }
    
    @Override
    public String webQueryFileListByMap(Map<String, Object> anMap) {
        
        return AjaxObject.newOk("付款文件查询成功", fileService.queryFileListByMap(anMap)).toJson();
    }

    @Override
    public String webSaveResolveFile(Long anFileItemId, Long anSourceFileId) {
        
        return AjaxObject.newOk("付款文件查询成功", fileService.saveResolveFile(anFileItemId, anSourceFileId)).toJson();
    }

    @Override
    public String webFindFileDetailByPrimaryKey(Long anid) {
        
        return AjaxObject.newOk("付款文件查询成功", fileService.findFileDetailByPrimaryKey(anid)).toJson();
    }

    @Override
    public String webSaveAuditFileByPrimaryKey(Long anid) {
        
        return AjaxObject.newOk("付款文件审核成功", fileService.saveAuditFileByPrimaryKey(anid)).toJson();
    }

    @Override
    public String webSaveDeleteFileByPrimaryKey(Long anid) {
        
        return AjaxObject.newOk("付款文件删除成功", fileService.saveDeleteFileByPrimaryKey(anid)).toJson();
    }

    @Override
    public String webQueryFilePage(Map<String, Object> anAnMap, String anFlag, int anPageNum, int anPageSize) {
        
        return AjaxObject.newOkWithPage("付款文件查询成功", fileService.queryFilePage(anAnMap, anFlag, anPageNum, anPageSize)).toJson();
    }

   

}
