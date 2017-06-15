package com.betterjr.modules.flie.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import com.betterjr.modules.flie.exception.ImportExcelException;
import com.betterjr.modules.order.entity.ScfOrderDO;



public class ExcelImportUtils {
	
	
	/**
	 * 获取导入文件整体信息
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("all")
	public static ExcelImportType getImportType(Class clazz){
		
		return ExcelImportType.createImportType(clazz);
		
	}
	
	/**
	 * 获取导入文件每一个数据行的信息
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("all")
	public static ExcelCloumnRow getImportRow(Class clazz){
		
		return ExcelCloumnRow.getExcelRow(clazz);
	}
	
	public static <T> List<T> importObj(Class<T> t,String filePath) throws Exception{
		
		if(StringUtils.isBlank(filePath)){
			throw new ImportExcelException("请选择文件路径,解析的文件不能为空");
		}
		
		if(!(filePath.endsWith("xls")|| filePath.endsWith("xlsx") )){
			throw new ImportExcelException("解析的文件必须为excel类型");
		}
		
		String fileType=filePath.endsWith("xls")?"xls":"xlsx";
		FileInputStream fis=new FileInputStream(new File(filePath));
		return importObj(t, fis, fileType);
		
	}
	
	public static <T> List<T> importObj(Class<T> t,InputStream is,String fileType) throws Exception{
		
		Iterator<Row> iterator = ExcelUtils.parseFile(is, fileType);//获取导入文件的数据
		
		ExcelImportType importType = getImportType(t);//获取整个模版的配置信息
		
		ExcelCloumnRow row = getImportRow(t);//获取每行记录的信息
		
		List<T> tList=new ArrayList<T>();
		try{
		 List<Map<String, Object>> lists = createObjs(iterator,importType,row);
		 
		 for (Map<String, Object> mapData : lists) {
			 T newInstance = t.newInstance();
			 BeanUtils.populate(newInstance, mapData);
			 tList.add(newInstance);
		 }
		 
		}finally{
			is.close();
		}
		
		return tList;
		
	}

	private static List<Map<String,Object>> createObjs(Iterator<Row> iterator, ExcelImportType importType, ExcelCloumnRow excelRow) throws ImportExcelException {
		
		List<Map<String,Object>> lists=new ArrayList<>();
		int i=importType.getExcelBeginRow();
		while(iterator.hasNext()){
			
			Row row = iterator.next();
			if(row.getRowNum() <importType.getExcelBeginRow()){
				continue;
			}
			i++;
			try{
				Map<String,Object> map=createObj(row,excelRow);
				lists.add(map);
			}catch(Exception e){
				throw new ImportExcelException("第"+i+"行出现错误：错误内容为"+e.getMessage());
			}
			
		}
		
		return lists;
	}

	private static Map<String,Object> createObj(Row row, ExcelCloumnRow excelRow) throws ImportExcelException {
		
		List<ExcelColumnCell> cells = excelRow.getExcelRow();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			
			for (ExcelColumnCell cell : cells) {
				String chineseName = cell.getCloumnChineseName();//中文名称
				Integer order = cell.getCloumnOrder();//排序的位置
				String properties = cell.getCloumnProperties();//属性名称
				String type = cell.getCloumnType();//属性的类型 0 字符串   1 数字
				String isMust = cell.getIsMust();//是否必须   1 必须  0 不必须
				String regular = cell.getVailedRegular();//正则表达式
				String cellValue = ExcelUtils.getStringCellValue(row.getCell(order));
				
				//当前值是必须的，如果为空，或者当前校验规则不为空需要通过校验规则，如果是数字类型，还需要通过数字校验
				if(AnnoConstantCollection.IS_MUST_YES.equals(isMust) ){
					if(StringUtils.isBlank(cellValue)){
						throw new ImportExcelException(chineseName+" 默认不能 为空,请在导入文件中加入此值");
					}
					
					if(StringUtils.isNoneBlank(regular)){
						if(!matchValue(cellValue, regular)){
							throw new ImportExcelException(chineseName+" 的值不符合正则要求"+regular+",请注意");
						}
					}
					
					if(AnnoConstantCollection.COLUMN_TYPE_NUMBER.equals(type)){
	                    if( ! isNumber(cellValue)){
	                        throw new ImportExcelException(chineseName+" 的值只能为数字,请注意");
	                    }
	                }
					
				}else{
				    
				    //说明当前值不是必须的   如果是当前值不为空，如果是数字类型，，通过是否是数字判断，如果不是数字类型通过正则判断
				    if(StringUtils.isNoneBlank(cellValue)){
				        if(AnnoConstantCollection.COLUMN_TYPE_NUMBER.equals(type)){
		                    if( ! isNumber(cellValue)){
		                        throw new ImportExcelException(chineseName+" 的值只能为数字,请注意");
		                    }
		                }else{
                            if(StringUtils.isNoneBlank(regular)){
                                if(!matchValue(cellValue, regular)){
                                    throw new ImportExcelException(chineseName+" 的值不符合正则要求"+regular+",请注意");
                                }
                            }
                        }
				        
				    }else{
				        //解决如果是数字类型但是excel中没有值的情况无法转换的情况
				        if(AnnoConstantCollection.COLUMN_TYPE_NUMBER.equals(type)){
				            cellValue="0";
                        }
				        
				        
				    }
				    
				}
				
				map.put(properties, cellValue);
			}
			
		}catch(Exception e){
			throw new ImportExcelException(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 判断值必须为数字类型，如果为数字返回true
	 */
	private static boolean isNumber(String value){
		
	    String regular="([1-9]+[0-9]*|0)(\\.[\\d]+)?";
		Pattern pattern = Pattern.compile(regular); 
		   Matcher isNum = pattern.matcher(value);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}
	
	public static boolean matchValue(String value,String regular){
		
		   Pattern pattern = Pattern.compile(regular); 
		   Matcher isNum = pattern.matcher(value);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}

	@Test
	public void run1(){
	    
	    System.out.println("123:"+isDecimal("123"));  
	    System.out.println("0.123:"+isDecimal("0.123"));  
	    System.out.println(".123:"+isDecimal(".123"));  
	    System.out.println("1.23:"+isDecimal("1.23"));  
	    
	}
	
	@Test
    public void run3(){
        
        try {
            String filePath="C:\\Users\\xuyp\\Desktop\\采购订单导入模板数据.xlsx";
            List<ScfOrderDO> importObj = ExcelImportUtils.importObj(ScfOrderDO.class, filePath);
            System.out.println(importObj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
	
	
	public static boolean isDecimal(String str){  
	    return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();  
	   }  
}
