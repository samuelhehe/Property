package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2015/12/11 18:04
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitmentFinishCompanyInfo {

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
    private List<FitmentFinishCompanyData> pageData;

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

    public List<FitmentFinishCompanyData> getPageData() {
        return pageData;
    }

    public void setPageData(List<FitmentFinishCompanyData> pageData) {
        this.pageData = pageData;
    }
}
