package com.betterjr.modules.flie.data;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.betterjr.common.utils.BTAssert;

public class ExcelUtils {

	/**
	 * 获取单元格的值，将其他所有格式转换成字符串类型并返回； 单元格内容为空则返回""。
	 * 
	 * @param cell
	 * @return
	 */
	public static String getStringCellValue(Cell cell) {
		if (null == cell)
			return "";
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date d = cell.getDateCellValue();
				DateFormat formater = new SimpleDateFormat("yyyyMMdd");
				return formater.format(d);
			} else {
				strCell = new HSSFDataFormatter().formatCellValue(cell);
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (StringUtils.isEmpty(strCell)) {
			return "";
		}

		return strCell.replaceAll("[　 ]", " ").trim();
	}

	/**
	 * excel文件预处理
	 * 
	 * @param file
	 * @param maxCount
	 * @return
	 * @throws IOException
	 * @ApiDocMethod
	 * @ApiCode
	 */
	public static Iterator<Row> parseFile(InputStream is,String fileType)
			throws IOException {

		Workbook wb = checkFileType(is,fileType);
		BTAssert.notNull(null,"文件格式错误!请重新上传");
		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return null;
		}
		int totals = sheet.getLastRowNum(); // 总行数
		if (totals < 1) {
		    BTAssert.notNull(null,"您上传的文件中无数据！");
		}
		return sheet.rowIterator();
	}

	/**
	 * 检查上传文件类型是否为excel
	 * 
	 * @param item
	 * @param wb
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @ApiDocMethod
	 * @ApiCode
	 */
	private static Workbook checkFileType(InputStream is,String fileType)
			throws IOException {
		
		if (fileType.equals("xls")) {
			return new HSSFWorkbook(is);
		} else if (fileType.equals("xlsx")) {
			return new XSSFWorkbook(is);
		}
		return null;
	}
}
