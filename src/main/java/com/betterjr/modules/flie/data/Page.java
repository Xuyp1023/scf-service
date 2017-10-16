package com.betterjr.modules.flie.data;

import java.util.List;
import java.util.Map;

public class Page {

    private List<Map> list;
    private int currentPage;
    private int totalPage;
    private int pageSize;

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

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int anPageSize) {
        this.pageSize = anPageSize;
    }

}
