package xj.property.beans;

import java.util.List;

import xj.property.cache.ShopContact;


/**
 * Created by Administrator on 2015/3/16.
 */
public class OrderRepairHistoryBean implements XJ {
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

    public static class Info implements XJ {
        private String rowCount;
        private String pageSize;
        private String num;
        private String startRow;
        private String next;
        private String prev;
        private String pageCount;
        private String begin;
        private String end;
        private String first;
        private String last;
        private String navNum;
        private List<HistoryBean> pageData;

        public List<HistoryBean> getPageData() {
            return pageData;
        }

        public void setPageData(List<HistoryBean> pageData) {
            this.pageData = pageData;
        }

        public String getRowCount() {
            return rowCount;
        }

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getStartRow() {
            return startRow;
        }

        public void setStartRow(String startRow) {
            this.startRow = startRow;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

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
    }


}
