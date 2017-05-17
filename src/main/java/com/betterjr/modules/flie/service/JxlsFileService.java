package com.betterjr.modules.flie.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.betterjr.modules.flie.data.Page;
/***
 * jxls 服务处理类
 * @author hubl
 *
 */
@Service
public class JxlsFileService {

protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;   //查询文件详情
    
    @Autowired
    private DataStoreService dataStoreService; //将文件转成输入流
    
    public CustFileItem transformXLSFile(List resultList,Map<String,Object> anMap,Long anTempFileId,Integer fristPageSize, Integer pageSize,String fileName) throws IOException, ParsePropertyException, InvalidFormatException{
        logger.info("文件导出服务： 操作人："+UserUtils.getOperatorInfo().getName());
        BTAssert.notNull(resultList,"导出的数据文件为空");
        CustFileItem fileItem = custFileService.findOne(anTempFileId);//文件上次详情
        BTAssert.notNull(fileItem,"导出模版为空");
        String fileType=fileItem.getFileType();
        InputStream is = dataStoreService.loadFromStore(fileItem);//得到文件输入流
        
        List<Page> objects =transPageList(resultList,pageSize,fristPageSize);
        List<String> sheetNames = objects.stream().map(page->page.getSheetName()).collect(Collectors.toList());
        
        XLSTransformer transformer = new XLSTransformer();
        Workbook workBook = transformer.transformMultipleSheetsList(is , objects, sheetNames, "page", anMap, 0);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workBook.write(os);
        }
        catch (IOException e) {
            logger.info("封装模版产生异常,请稍后重试!"+e.getMessage());
            BTAssert.notNull(workBook,"封装模版产生异常,请稍后重试!");
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        if(!fileName.contains(".") || !(fileName.endsWith("xls") || fileName.endsWith("xlsx")) ){
            fileName=fileName+"."+fileType;
        }
        CustFileItem item = dataStoreService.saveStreamToStore(in, fileItem.getFileType(),fileName);
        logger.info("导出文件生成成功："+UserUtils.getOperatorInfo().getName());
        is.close();
        os.flush();
        os.close();
        
        return item;
    }
    
    /***
     * 将列表转化成page
     * @param resultList
     * @param pageSize
     * @param fristPageSize
     * @return
     */
    public List<Page> transPageList(List resultList,int pageSize,int fristPageSize){
        int total=resultList.size();
        boolean flag = false;
        int totalPage = 0;
        if (flag) {
            totalPage = (total + pageSize - 1) / pageSize;
        } else {
            int surplusTotal = total - fristPageSize;
            if (surplusTotal > 0) {
                totalPage = (surplusTotal + pageSize - 1) / pageSize + 1;
            } else {
                totalPage = 1;
            }
        }
        List<Page> objects = new ArrayList<Page>();
        for (int pageIndex = 0; pageIndex < totalPage; pageIndex++) {
            Page page = new Page();
            int sheetNum=pageIndex+1;
            page.setSheetName("第" + sheetNum +"页");
            page.setTotalPage(totalPage);
            page.setCurrentPage(sheetNum);
            
            if (!flag && pageIndex == 0) {
                if(total>fristPageSize){
                    page.setList(resultList.subList(0, fristPageSize));
                    page.setPageSize(fristPageSize);
                }else{
                    page.setList(resultList);
                    page.setPageSize(resultList.size());
                    for(int s=0;s<fristPageSize-total;s++){
                        page.getList().add(new HashMap());
                    }
                }
            }  else if (pageIndex == totalPage - 1) {
                List list = new ArrayList();
                List subList=resultList.subList(pageIndex * pageSize, total);
                list.addAll(subList);
                for(int m=0;m<pageSize-subList.size();m++){
                    list.add(new HashMap());
                }
                page.setPageSize(pageSize);
                page.setList(list);
            } else {
                page.setPageSize(pageIndex * pageSize + pageSize - pageIndex * pageSize);
                page.setList(resultList.subList(pageIndex * pageSize, pageIndex * pageSize + pageSize));
            }
            objects.add(page);
        }
        return objects;
    }
    
}
