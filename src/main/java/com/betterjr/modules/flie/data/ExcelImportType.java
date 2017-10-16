package com.betterjr.modules.flie.data;

import java.io.Serializable;

import com.betterjr.modules.flie.annotation.ExcelImportTypeAnno;

/**
 * 文件导入模版级别的设置
 * @author lenovo
 *
 */
public class ExcelImportType implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3052194988428793027L;
    private Integer excelBeginRow;

    public Integer getExcelBeginRow() {
        return excelBeginRow;
    }

    public void setExcelBeginRow(Integer excelBeginRow) {
        this.excelBeginRow = excelBeginRow;
    }

    @SuppressWarnings("all")
    public static ExcelImportType createImportType(Class clazz) {

        ExcelImportType importType = new ExcelImportType();

        if (clazz.isAnnotationPresent(ExcelImportTypeAnno.class)) {

            ExcelImportTypeAnno anno = (ExcelImportTypeAnno) clazz.getAnnotation(ExcelImportTypeAnno.class);

            importType.setExcelBeginRow(anno.excelBeginRow());

        }
        return importType;

    }

}
