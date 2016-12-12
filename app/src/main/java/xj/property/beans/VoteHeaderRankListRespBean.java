package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class VoteHeaderRankListRespBean {


    /**
     * status : yes
     * info : {"page":{"rowCount":1,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"emobId":"a0027fcac74d3126e22fa2a18430059d","score":10,"nickname":"IMSuper5","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fr68rY3RCVTQkmtqOCZiECwVcY4S","rank":1}]},"MyElectedEmobId":"a0027fcac74d3126e22fa2a18430059d"}
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
         * page : {"rowCount":1,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"emobId":"a0027fcac74d3126e22fa2a18430059d","score":10,"nickname":"IMSuper5","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fr68rY3RCVTQkmtqOCZiECwVcY4S","rank":1}]}
         * MyElectedEmobId : a0027fcac74d3126e22fa2a18430059d
         */

        private PageEntity page;
        private String MyElectedEmobId;

        public void setPage(PageEntity page) {
            this.page = page;
        }

        public void setMyElectedEmobId(String MyElectedEmobId) {
            this.MyElectedEmobId = MyElectedEmobId;
        }

        public PageEntity getPage() {
            return page;
        }

        public String getMyElectedEmobId() {
            return MyElectedEmobId;
        }

        public static class PageEntity {
            /**
             * rowCount : 1
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
             * pageData : [{"emobId":"a0027fcac74d3126e22fa2a18430059d","score":10,"nickname":"IMSuper5","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fr68rY3RCVTQkmtqOCZiECwVcY4S","rank":1}]
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
                 * emobId : a0027fcac74d3126e22fa2a18430059d
                 * score : 10
                 * nickname : IMSuper5
                 * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fr68rY3RCVTQkmtqOCZiECwVcY4S
                 * rank : 1
                 */

                private String emobId;
                private int score;
                private String nickname;
                private String avatar;
                private int rank;

                public void setEmobId(String emobId) {
                    this.emobId = emobId;
                }

                public void setScore(int score) {
                    this.score = score;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public void setRank(int rank) {
                    this.rank = rank;
                }

                public String getEmobId() {
                    return emobId;
                }

                public int getScore() {
                    return score;
                }

                public String getNickname() {
                    return nickname;
                }

                public String getAvatar() {
                    return avatar;
                }

                public int getRank() {
                    return rank;
                }
            }
        }
    }
}
