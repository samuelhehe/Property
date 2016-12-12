package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ProviderDetailsVisitMoreRespBean {


    /**
     * status : yes
     * info : {"rowCount":3,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]}
     */

    private String status;

    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity {
        /**
         * rowCount : 3
         * pageSize : 10
         * num : 1
         * startRow : 0
         * next : 1
         * prev : 1
         * pageCount : 1
         * begin : 1
         * end : 1
         * first : 1
         * last : 1
         * navNum : 10
         * pageData : [{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]
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
        private List<PageDataEntity> pageData;

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

        public void setPageData(List<PageDataEntity> pageData) {
            this.pageData = pageData;
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

        public List<PageDataEntity> getPageData() {
            return pageData;
        }

        public static class PageDataEntity {
            /**
             * emobId : aad8e70c725f5362c28852b281297e86
             * nickname : 我是008
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH
             * createTime : 1446544107
             */

            private String emobId;
            private String nickname;
            private String avatar;
            /// 2015/11/23
            private int timestemp;

            private int createTime;

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public String getEmobId() {
                return emobId;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public int getCreateTime() {
                return createTime;
            }

            public int getTimestemp() {
                return timestemp;
            }

            public void setTimestemp(int timestemp) {
                this.timestemp = timestemp;
            }
        }
    }
}
