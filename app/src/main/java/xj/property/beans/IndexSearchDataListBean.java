package xj.property.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiangyu on 2015/4/1.
 */
public class IndexSearchDataListBean implements XJ {
    private String status;
    private Info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class Info implements XJ{
    private String begin;
    private String end;
    private String first;
    private String last;
    private String navNum;
    private String next;
    private String num;
    private String pageCount;
    private String pageSize;
    private String prev;
    private String rowCount;
    private String startRow;

    public ArrayList<FacilitiesBean> pageData;

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getNavNum() {
        return navNum;
    }

    public void setNavNum(String navNum) {
        this.navNum = navNum;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getRowCount() {
        return rowCount;
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    public String getStartRow() {
        return startRow;
    }

    public void setStartRow(String startRow) {
        this.startRow = startRow;
    }

        public ArrayList<FacilitiesBean> getPageData() {
            return pageData;
        }

        public void setPageData(ArrayList<FacilitiesBean> pageData) {
            this.pageData = pageData;
        }
    }



}
