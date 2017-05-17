package com.betterjr.modules;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;







import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class JxlsTest {

    
    public void exportPayslip() throws Exception{

        String  xlsFileName = "d:/excel/testMultipleSheets.xls";
        transformXLSFile2(getList() ,xlsFileName, 2, 3);
//        if (payslipList.size() <= 3000){
//            xlsFileName = "c:"+File.separator+costcenterCode+".xls";
//            transformXLSFile(payslipList ,xlsFileName );
//        } else {
//            for (int i =0;i<payslipList.size()/3000;i++){
//                xlsFileName = "c:"+File.separator+costcenterCode+"_"+new Integer(i+1).toString()+".xls";
//                int k = ((i+1)*3000 < payslipList.size()) ? (i+1)*3000 : payslipList.size();
//                List resultList = payslipList.subList(i*3000,k);
//                transformXLSFile(resultList ,xlsFileName );
//            }
//        }
        
    }
    
    public static List getList(){
        List suplyAreaList = new ArrayList();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("user", "test");
        map.put("name", "name");
        map.put("address", "深圳福田");
        
        suplyAreaList.add(map);
        
        map=new HashMap<String, Object>();
        map.put("user", "test1");
        map.put("name", "测试");
        map.put("address", "深圳南山");
        suplyAreaList.add(map);
        

        map=new HashMap<String, Object>();
        map.put("user", "test2");
        map.put("name", "测试2");
        map.put("address", "深圳罗湖");
        suplyAreaList.add(map);
        

        map=new HashMap<String, Object>();
        map.put("user", "test3");
        map.put("name", "测试3");
        map.put("address", "深圳罗湖3");
        suplyAreaList.add(map);
        
        
        map=new HashMap<String, Object>();
        map.put("user", "test4");
        map.put("name", "测试4");
        map.put("address", "深圳罗湖4");
        suplyAreaList.add(map);
        
        
        map=new HashMap<String, Object>();
        map.put("user", "test5");
        map.put("name", "测试5");
        map.put("address", "深圳罗湖5");
        suplyAreaList.add(map);
        
        
        map=new HashMap<String, Object>();
        map.put("user", "test6");
        map.put("name", "测试6");
        map.put("address", "深圳罗湖6");
        suplyAreaList.add(map);

        map=new HashMap<String, Object>();
        map.put("user", "test7");
        map.put("name", "测试7");
        map.put("address", "深圳罗湖7");
        suplyAreaList.add(map);
        

        map=new HashMap<String, Object>();
        map.put("user", "test8");
        map.put("name", "测试8");
        map.put("address", "深圳罗湖8");
        suplyAreaList.add(map);
        

        map=new HashMap<String, Object>();
        map.put("user", "test9");
        map.put("name", "测试9");
        map.put("address", "深圳罗湖9");
        suplyAreaList.add(map);
        return suplyAreaList;
    }
    
    public static class Page {
        private List<Map> list;
        private int currentPage;
        private int totalPage;
        
        private String sheetName;
        
        public List<Map> getList() {
            return this.list;
        }
        public void setList(List<Map> anList) {
            this.list = anList;
        }
        public int getCurrentPage() {
            return this.currentPage;
        }
        public void setCurrentPage(int anCurrentPage) {
            this.currentPage = anCurrentPage;
        }
        public int getTotalPage() {
            return this.totalPage;
        }
        public void setTotalPage(int anTotalPage) {
            this.totalPage = anTotalPage;
        }
        public String getSheetName() {
            return this.sheetName;
        }
        public void setSheetName(String anSheetName) {
            this.sheetName = anSheetName;
        }
        
    }
    
    private void transformXLSFile2(List resultList,String fileName, Integer fristPageSize, Integer pageSize) throws IOException, InvalidFormatException{

        Map<String,Object> param=new HashMap<String, Object>();
        param.put("title", "标题");
        param.put("total", "11");
        InputStream is = new BufferedInputStream(new FileInputStream("d:/excel/multipleSheets.xls"));  
        XLSTransformer transformer = new XLSTransformer();

        int total = resultList.size();
        boolean flag = false;
        if (pageSize == null) {
            flag = true;
            pageSize = fristPageSize;
        }
        
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
        List<Page> objects = new ArrayList<JxlsTest.Page>();
        for (int pageIndex = 0; pageIndex < totalPage; pageIndex++) {
            Page page = new Page();
            page.setSheetName("Sheet" + pageIndex);
            page.setTotalPage(totalPage);
            page.setCurrentPage(pageIndex);
            if (!flag && pageIndex == 0) {
                page.setList(resultList.subList(0, fristPageSize));
            }  else if (pageIndex == totalPage - 1) {
                page.setList(resultList.subList(pageIndex * pageSize, total));
            } else {
                page.setList(resultList.subList(pageIndex * pageSize, pageIndex * pageSize + pageSize));
            }
            objects.add(page);
        }
        List<String> sheetNames = objects.stream().map(page->page.getSheetName()).collect(Collectors.toList());
        Workbook workBook =  transformer.transformMultipleSheetsList(is , objects, sheetNames, "page", new HashMap(), 0);

        saveWorkbook(workBook, fileName);
    }
  
    private void transformXLSFile(List resultList,String fileName) throws IOException, ParsePropertyException, InvalidFormatException{
        XLSTransformer transformer = new XLSTransformer();
        Workbook resultWorkBook = null;

        Map<String,Object> param=new HashMap<String, Object>();
        param.put("title", "标题");
        param.put("total", "11");
        for (int i = 0;i<resultList.size()/3;i++){
            List<List> objects = new ArrayList<List>(); 
            List list = new ArrayList();
            List sheetNames = new ArrayList();
            InputStream is = new BufferedInputStream(new FileInputStream("d:/excel/multipleSheets.xls"));  
            int k = ((i+1)*3 < resultList.size()) ? (i+1)*3 : resultList.size();
            list = resultList.subList(i*3,k);
            
            for (int j=0;j<list.size();j++){
                Map payslip = (Map)list.get(j);
                String sheetName = payslip.get("user").toString();
                sheetNames.add(sheetName);
            }
            
            objects.add(list);
            
            Workbook workBook =  transformer.transformMultipleSheetsList(is , objects, sheetNames, "list", new HashMap(), 0);
           if (i == 0){
                resultWorkBook = workBook;
            } else {
                for (int h = 0;h < workBook.getNumberOfSheets();h++){
                    Sheet newSheet = workBook.createSheet( (String) sheetNames.get(i) );
                    Sheet hssfSheet = workBook.getSheetAt(h);
                    Util.copySheets(newSheet, hssfSheet);
                    Util.copyPageSetup(newSheet, hssfSheet);
                    Util.copyPrintSetup(newSheet, hssfSheet);
                }
            }
            resultWorkBook = workBook;
            saveWorkbook(resultWorkBook, fileName);
        }
    }
    
    private void saveWorkbook(Workbook resultWorkbook, String fileName) throws IOException{
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));
        resultWorkbook.write(os);
        os.flush();
        os.close();
    }
    
    
    private static void testJxls() throws ParsePropertyException, InvalidFormatException, IOException{
        List suplyAreaList = new ArrayList();    
        
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("user", "test");
        map.put("name", "name");
        map.put("address", "深圳福田");
        map.put("sheetName", "福田");
        
        suplyAreaList.add(map);
        
        map=new HashMap<String, Object>();
        map.put("user", "test1");
        map.put("name", "测试");
        map.put("address", "深圳南山");
        map.put("sheetName", "南山");
        suplyAreaList.add(map);
        

        map=new HashMap<String, Object>();
        map.put("user", "test2");
        map.put("name", "测试2");
        map.put("address", "深圳罗湖");
        map.put("sheetName", "罗湖");
        suplyAreaList.add(map);
        
        
        String templateDir = "d:/excel/multipleSheets.xls";  
        String targetDir="d:/excel/testMultipleSheets.xls";  
        List sheetNames = new ArrayList();  
        for(int i=0;i<suplyAreaList.size();i++){  
            Map sa = (Map)suplyAreaList.get(i);  
            sheetNames.add(sa.get("sheetName"));
        }  
        InputStream is = new BufferedInputStream(new FileInputStream(templateDir));  
        XLSTransformer transformer = new XLSTransformer();  
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("title", "标题");
        param.put("total", "11");
        Workbook resultWorkBook = transformer.transformMultipleSheetsList(is, suplyAreaList, sheetNames, "map", param, 0);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(targetDir));  
        resultWorkBook.write(os);
        is.close();
        os.close();
    }
    
    
    public static void test1() throws Exception {  

        String templateDir = "d:/excel/multipleSheets.xls";  
        String targetDir="d:/excel/testMultipleSheets.xls"; 
          
        Map beanParams = new HashMap();  
        beanParams.put("reports", getList());  
        XLSTransformer former = new XLSTransformer();  
        former.transformXLS(templateDir, beanParams, targetDir);  
    } 
    
    public static void test2() throws ParsePropertyException, InvalidFormatException, IOException {  

        //获取Excel模板文件  

        String filePath = "d:/excel/multipleSheets.xls";   
        String targetDir="d:/excel/testMultipleSheets.xls"; 

        System.out.println("excel template file:" + filePath);  

        FileInputStream is = new FileInputStream(filePath);  

        //创建测试数据  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  

        Map<String, Object> map1 = new HashMap<String, Object>();  

        map1.put("name", "电视");  

        map1.put("price", "3000");  

        map1.put("desc", "3D电视机");  

        
        Map<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("name", "空调");  
        map2.put("price", "2000");  
        map2.put("desc", "变频空调");  
        
        list.add(map1);  
        list.add(map2);  
        List objects = new ArrayList<List>();  
        objects.add(list);  
        objects.add(list);  
        objects.add(list);  
        objects.add(list);  
        //sheet的名称  
        List<String> listSheetNames = new ArrayList<String>();  
        listSheetNames.add("1");  
        listSheetNames.add("2");  
        listSheetNames.add("3");  
        listSheetNames.add("4");  
        //调用引擎生成excel报表  
        XLSTransformer transformer = new XLSTransformer();  

        Workbook workbook = transformer.transformMultipleSheetsList(is, objects, listSheetNames, "list", new HashMap(), 0);  

        workbook.write(new FileOutputStream(targetDir));  

      }
    
    public static void main(String[] args) {
        try {
//            testJxls();
            JxlsTest test=new JxlsTest();
            test.exportPayslip();
//            test1();
//            test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
