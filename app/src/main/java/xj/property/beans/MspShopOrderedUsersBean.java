package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MspShopOrderedUsersBean {


    /**
     * status : yes
     * info : {"rowCount":4,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"userId":405,"username":"18600113752","emobId":"4531b1aa29ed4d4708c140d7e6ab4347","nickname":"北海","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlRLqmevHypVsCWlfbEfQ7UyKhXJ"},{"userId":406,"username":"18734900006","emobId":"384275f742e3f0ad1fb5b28220af6e00","nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof"},{"userId":409,"username":"13811794912","emobId":"3429a32511ff13d12c2003cc0441dca7","nickname":"亮仔Q糖","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqdAkIGlJMfz0PFIhVjVxInuvz9X"},{"userId":412,"username":"15810079859","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"}]}
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
         * rowCount : 4
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
         * pageData : [{"userId":405,"username":"18600113752","emobId":"4531b1aa29ed4d4708c140d7e6ab4347","nickname":"北海","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlRLqmevHypVsCWlfbEfQ7UyKhXJ"},{"userId":406,"username":"18734900006","emobId":"384275f742e3f0ad1fb5b28220af6e00","nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof"},{"userId":409,"username":"13811794912","emobId":"3429a32511ff13d12c2003cc0441dca7","nickname":"亮仔Q糖","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqdAkIGlJMfz0PFIhVjVxInuvz9X"},{"userId":412,"username":"15810079859","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"}]
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
             * userId : 405
             * username : 18600113752
             * emobId : 4531b1aa29ed4d4708c140d7e6ab4347
             * nickname : 北海
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FlRLqmevHypVsCWlfbEfQ7UyKhXJ
             */

            private int userId;
            private String username;
            private String emobId;
            private String nickname;
            private String avatar;

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getUserId() {
                return userId;
            }

            public String getUsername() {
                return username;
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
        }
    }
}
