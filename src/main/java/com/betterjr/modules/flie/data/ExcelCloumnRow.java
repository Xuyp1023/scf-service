package com.betterjr.modules.flie.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.betterjr.modules.flie.annotation.ExcelImportAnno;

/**
 * 指定对象获取模版行结构信息封装
 * @author lenovo
 *
 */
public class ExcelCloumnRow implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4538569453281311389L;
    private List<ExcelColumnCell> excelRow = new ArrayList<>();

    public List<ExcelColumnCell> getExcelRow() {
        return excelRow;
    }

    public void setExcelRow(List<ExcelColumnCell> excelRow) {
        this.excelRow = excelRow;
    }

    public static ExcelCloumnRow getExcelRow(Class clazz) {

        ExcelCloumnRow row = new ExcelCloumnRow();

        row.setExcelRow(getImportExcelRow(clazz));

        return row;

    }

    @SuppressWarnings("all")
    /**
     * 通过导入类的注解获取当前类上面注解模版行结构信息
     * @author lenovo
     *
     */
    public static List<ExcelColumnCell> getImportExcelRow(Class clazz) {

        List<ExcelColumnCell> cellList = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(ExcelImportAnno.class)) {

                ExcelImportAnno importAnno = field.getAnnotation(ExcelImportAnno.class);
                ExcelColumnCell cell = importAnnoToExcelCloumnCell(importAnno);
                cell.setCloumnProperties(field.getName());
                cellList.add(cell);
            }

        }
        return cellList;

    }

    private static ExcelColumnCell importAnnoToExcelCloumnCell(ExcelImportAnno anImportAnno) {

        String cloumnChineseName = anImportAnno.cloumnChineseName();// 中文名称

        int cloumnOrder = anImportAnno.cloumnOrder();

        String cloumnType = anImportAnno.cloumnType();

        String must = anImportAnno.isMust();

        String regular = anImportAnno.vailedRegular();

        ExcelColumnCell cell = new ExcelColumnCell();
        cell.setCloumnChineseName(cloumnChineseName);
        cell.setCloumnOrder(cloumnOrder);
        cell.setCloumnType(cloumnType);
        cell.setIsMust(must);
        cell.setVailedRegular(regular);

        return cell;
    }

}
