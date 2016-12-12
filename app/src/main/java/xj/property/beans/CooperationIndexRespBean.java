package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CooperationIndexRespBean {


    /**
     * status : yes
     * info : {"rowCount":19,"pageSize":10,"num":2,"startRow":10,"next":2,"prev":1,"pageCount":2,"begin":1,"end":2,"first":1,"last":2,"navNum":10,"pageData":[{"cooperationId":1,"title":"修马桶","content":"专业修马桶20年","emobId":"30fbf0be239f5afd52440cf31d98f23e","nickname":"老干妈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS","users":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]}]}
     */

    private String status;
    private InfoEntity info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public static class InfoEntity {

        /**
         * rowCount : 19
         * pageSize : 10
         * num : 2
         * startRow : 10
         * next : 2
         * prev : 1
         * pageCount : 2
         * begin : 1
         * end : 2
         * first : 1
         * last : 2
         * navNum : 10
         * pageData : [{"cooperationId":1,"title":"修马桶","content":"专业修马桶20年","emobId":"30fbf0be239f5afd52440cf31d98f23e","nickname":"老干妈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS","users":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]}]
         */

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

        public List<NeighborListV3Bean.NeighborListData> pageData;

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public void setLast(int last) {
            this.last = last;
        }

        public void setNavNum(int navNum) {
            this.navNum = navNum;
        }


        public int getRowCount() {
            return rowCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getNum() {
            return num;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getNext() {
            return next;
        }

        public int getPrev() {
            return prev;
        }

        public int getPageCount() {
            return pageCount;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }

        public int getFirst() {
            return first;
        }

        public int getLast() {
            return last;
        }

        public int getNavNum() {
            return navNum;
        }


//        public List<CooperationContentBean> getPageData() {
//            return pageData;
//        }
//
//        public void setPageData(List<CooperationContentBean> pageData) {
//            this.pageData = pageData;
//        }

        public List<NeighborListV3Bean.NeighborListData> getPageData() {
            return pageData;
        }

        public void setPageData(List<NeighborListV3Bean.NeighborListData> pageData) {
            this.pageData = pageData;
        }
    }
}
