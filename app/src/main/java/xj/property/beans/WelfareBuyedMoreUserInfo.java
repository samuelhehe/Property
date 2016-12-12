package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class WelfareBuyedMoreUserInfo {


    /**
     * status : yes
     * info : {"rowCount":2,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"userId":3177,"nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","emobId":"2011eb792db7b1029341faab3ad65919"},{"userId":434,"nickname":"我是009","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","emobId":"58a4e33fbc97abca4051130ad9b2e2cf"}]}
     */

    private String status;
    private InfoEntity info;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
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
         * rowCount : 2
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
         * pageData : [{"userId":3177,"nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","emobId":"2011eb792db7b1029341faab3ad65919"},{"userId":434,"nickname":"我是009","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","emobId":"58a4e33fbc97abca4051130ad9b2e2cf"}]
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
             * userId : 3177
             * nickname : lei
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt
             * emobId : 2011eb792db7b1029341faab3ad65919
             */

            private int userId;
            private String nickname;
            private String avatar;
            private String emobId;

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public int getUserId() {
                return userId;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public String getEmobId() {
                return emobId;
            }
        }
    }
}
