package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class CooperationRespBean {


    /**
     * status : yes
     * info : {"rowCount":10,"pageSize":10,"num":1,"startRow":0,"next":1,"prev":1,"pageCount":1,"begin":1,"end":1,"first":1,"last":1,"navNum":10,"pageData":[{"cooperationId":1,"title":"修马桶","content":"专业修马桶20年","emobId":"30fbf0be239f5afd52440cf31d98f23e","nickname":"老干妈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS","users":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH"},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4"}]},{"cooperationId":14,"title":"做小吃","content":"专业小吃摊100年，500年传统工艺","emobId":"4531b1aa29ed4d4708c140d7e6ab4347","nickname":"北海","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpWcRgfmSTDWNovJ6IRC5Q52aylp"},{"cooperationId":15,"title":"品尝美食","content":"专业美食评鉴大家，吃遍大江南北","emobId":"384275f742e3f0ad1fb5b28220af6e00","nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof"},{"cooperationId":16,"title":"做衣服","content":"宫廷手艺，纯手工，给您帝皇般的感觉","emobId":"3429a32511ff13d12c2003cc0441dca7","nickname":"亮仔Q糖","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqdAkIGlJMfz0PFIhVjVxInuvz9X"},{"cooperationId":17,"title":"修家电","content":"十年老店，您值得信任","emobId":"9a86c7273e9e3f7ae3fb1fc24c0a2a2a","nickname":"TONNY ","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fg3kGkc11xodG6Rr56theADzSaM2"},{"cooperationId":18,"title":"修房顶","content":"专业人士，为了您的房顶不漏","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":19,"title":"打扫卫生","content":"58同城特约小时功，您家庭的清洁大使","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":20,"title":"照顾小孩","content":"不管您您应为什么原因不能陪孩子，我一定给他最好的关爱","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":21,"title":"牙科医生","content":"专注牙医30年，祖传技术值得信赖！","emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"},{"cooperationId":22,"title":"牙科医生","content":"专注牙医30年，祖传技术值得信赖！","emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"}]}
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
         * rowCount : 10
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
         * pageData : [{"cooperationId":1,"title":"修马桶","content":"专业修马桶20年","emobId":"30fbf0be239f5afd52440cf31d98f23e","nickname":"老干妈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS","users":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH"},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4"}]},{"cooperationId":14,"title":"做小吃","content":"专业小吃摊100年，500年传统工艺","emobId":"4531b1aa29ed4d4708c140d7e6ab4347","nickname":"北海","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpWcRgfmSTDWNovJ6IRC5Q52aylp"},{"cooperationId":15,"title":"品尝美食","content":"专业美食评鉴大家，吃遍大江南北","emobId":"384275f742e3f0ad1fb5b28220af6e00","nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof"},{"cooperationId":16,"title":"做衣服","content":"宫廷手艺，纯手工，给您帝皇般的感觉","emobId":"3429a32511ff13d12c2003cc0441dca7","nickname":"亮仔Q糖","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqdAkIGlJMfz0PFIhVjVxInuvz9X"},{"cooperationId":17,"title":"修家电","content":"十年老店，您值得信任","emobId":"9a86c7273e9e3f7ae3fb1fc24c0a2a2a","nickname":"TONNY ","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fg3kGkc11xodG6Rr56theADzSaM2"},{"cooperationId":18,"title":"修房顶","content":"专业人士，为了您的房顶不漏","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":19,"title":"打扫卫生","content":"58同城特约小时功，您家庭的清洁大使","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":20,"title":"照顾小孩","content":"不管您您应为什么原因不能陪孩子，我一定给他最好的关爱","emobId":"2c6df6239e0107cbc2ba6617f3ebef9e","nickname":"刘宇峰","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl8MWlP5hzCpijgyr_Ljcb_nxIbx"},{"cooperationId":21,"title":"牙科医生","content":"专注牙医30年，祖传技术值得信赖！","emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"},{"cooperationId":22,"title":"牙科医生","content":"专注牙医30年，祖传技术值得信赖！","emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"}]
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
             * cooperationId : 1
             * title : 修马桶
             * content : 专业修马桶20年
             * emobId : 30fbf0be239f5afd52440cf31d98f23e
             * nickname : 老干妈
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS
             * users : [{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH"},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4"}]
             */

            private int cooperationId;
            private String title;
            private String content;
            private String emobId;
            private String nickname;
            private String avatar;

            private List<UsersEntity> users;

            public void setCooperationId(int cooperationId) {
                this.cooperationId = cooperationId;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setContent(String content) {
                this.content = content;
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

            public void setUsers(List<UsersEntity> users) {
                this.users = users;
            }

            public int getCooperationId() {
                return cooperationId;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
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

            public List<UsersEntity> getUsers() {
                return users;
            }

            public static class UsersEntity {
                /**
                 * emobId : aad8e70c725f5362c28852b281297e86
                 * nickname : 我是008
                 * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH
                 */

                private String emobId;
                private String nickname;
                private String avatar;

                public void setEmobId(String emobId) {
                    this.emobId = emobId;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
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
}
