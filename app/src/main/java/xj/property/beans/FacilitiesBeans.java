package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/1.
 */
public class FacilitiesBeans {
    private Info info;
    private String status;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  static class Info implements  XJ{


    private int rowCount;
    private int pageSize;
    private int num;
    private int startRow;
    private int next;
    private int prev;
    private int pageCount;
    private int begin;
    private int end;
    private int first;
    private int last;
    private int navNum;
    private List<FacilitiesBean> pageData;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getNavNum() {
        return navNum;
    }

    public void setNavNum(int navNum) {
        this.navNum = navNum;
    }

    public List<FacilitiesBean> getPageData() {
        return pageData;
    }

    public void setPageData(List<FacilitiesBean> pageData) {
        this.pageData = pageData;
    }

    }
}
