package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/9/28.
 */
public class InviteNeighborBean {


    /**
     * status : yes
     * info : {"neighbors":{"rowCount":3,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"registNickname":"不知不觉","registEmobId":"2","registAvatar":"http://...","phone":"15874859658"},{"registNickname":"天天向上","registEmobId":"3","registAvatar":"http://...","phone":"15874851151"},{"phone":"15874852222"},{"phone":"15874852223"}]},"message":"您邀请的不知不觉，天天向上已注册，您的人品值增加了40"}
     */

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public static class InfoEntity {
        /**
         * neighbors : {"rowCount":3,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"registNickname":"不知不觉","registEmobId":"2","registAvatar":"http://...","phone":"15874859658"},{"registNickname":"天天向上","registEmobId":"3","registAvatar":"http://...","phone":"15874851151"},{"phone":"15874852222"},{"phone":"15874852223"}]}
         * message : 您邀请的不知不觉，天天向上已注册，您的人品值增加了40
         */

        private NeighborsEntity neighbors;
        private String message;

        public void setNeighbors(NeighborsEntity neighbors) {
            this.neighbors = neighbors;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public NeighborsEntity getNeighbors() {
            return neighbors;
        }

        public String getMessage() {
            return message;
        }

        public static class NeighborsEntity {
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
             * pageData : [{"registNickname":"不知不觉","registEmobId":"2","registAvatar":"http://...","phone":"15874859658"},{"registNickname":"天天向上","registEmobId":"3","registAvatar":"http://...","phone":"15874851151"},{"phone":"15874852222"},{"phone":"15874852223"}]
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
                 * registNickname : 不知不觉
                 * registEmobId : 2
                 * registAvatar : http://...
                 * phone : 15874859658
                 */

                private String registNickname;
                private String registEmobId;
                private String registAvatar;
                private String phone;

                public void setRegistNickname(String registNickname) {
                    this.registNickname = registNickname;
                }

                public void setRegistEmobId(String registEmobId) {
                    this.registEmobId = registEmobId;
                }

                public void setRegistAvatar(String registAvatar) {
                    this.registAvatar = registAvatar;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getRegistNickname() {
                    return registNickname;
                }

                public String getRegistEmobId() {
                    return registEmobId;
                }

                public String getRegistAvatar() {
                    return registAvatar;
                }

                public String getPhone() {
                    return phone;
                }
            }
        }
    }
}
