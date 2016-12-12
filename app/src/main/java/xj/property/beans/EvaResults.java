package xj.property.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/5.
 */
public class EvaResults {
    public String status;
    public info info;

    public String getStatus() {
        return status;
    }

    public EvaResults.info getInfo() {
        return info;
    }

    public void setInfo(EvaResults.info info) {
        this.info = info;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class info {

        public int rowCount;
        public int pageSize;
        public int num;
        public int startRow;
        public int next;
        public int prev;
        public int pageCount;
        public int begin;
        public int end;
        public int first;
        public int last;
        public int navNum;
        public ArrayList<EvaBean> pageData;

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

        public ArrayList<EvaBean> getPageData() {
            return pageData;
        }

        public void setPageData(ArrayList<EvaBean> pageData) {
            this.pageData = pageData;
        }
    }


}