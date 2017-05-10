package com.betterjr.modules.flie.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;

@Service
public class FileDownService {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;   //查询文件详情
    
    @Autowired
    private DataStoreService dataStoreService; //将文件转成输入流
    
    /**
     * 文件导出服务
     * @param anRecordList  导出的数据源文件
     * tempFileId  导出的数据源模版上传后的id
     * fileName 导出的文件名
     * @return
     */
    public CustFileItem uploadCommissionRecordFileis(Map<String,Object> data,Long tempFileId,String fileName) {
        
        logger.info("文件导出服务： 操作人："+UserUtils.getOperatorInfo().getName());
        BTAssert.notNull(data,"导出的数据文件为空");
        //获取佣金记录导出模版文件
        //CustFileItem fileItem = custFileService.findOne(CommissionConstantCollentions.COMMISSION_FILE_DOWN_FILEITEM_FILEID);//文件上次详情
        CustFileItem fileItem = custFileService.findOne(tempFileId);//文件上次详情
        BTAssert.notNull(fileItem,"导出模版为空");
        InputStream is = dataStoreService.loadFromStore(fileItem);//得到文件输入流
        TemplateExportParams params=new TemplateExportParams(is);
        //TemplateExportParams params=new TemplateExportParams("C:\\Users\\xuyp\\Desktop\\佣金数据导出模板.xlsx");
        Workbook book=ExcelExportUtil.exportExcel(params, data);
        BTAssert.notNull(book,"封装模版产生异常,请稍后重试");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            FileOutputStream fos=new FileOutputStream(new File("d:\\789.xlsx"));
            book.write(fos);
            book.write(os);
            fos.close();
//            book.write(os);
        }
        catch (IOException e) {
            logger.info("封装模版产生异常,请稍后重试!"+e.getMessage());
            BTAssert.notNull(book,"封装模版产生异常,请稍后重试!");
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        //String fileName=anRecordList.get(0).getCustName()+anRecordList.get(0).getFileId()+"佣金数据导出."+fileItem.getFileType();
        CustFileItem item = dataStoreService.saveStreamToStore(in, fileItem.getFileType(),fileName );
        logger.info("导出文件生成成功："+UserUtils.getOperatorInfo().getName());
        return item;
    }

}
