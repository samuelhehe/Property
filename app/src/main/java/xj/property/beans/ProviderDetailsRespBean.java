package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ProviderDetailsRespBean {


    /**
     * status : yes
     * info : {"cooperationId":1,"title":"修马桶","content":"专业修马桶20年","emobId":"30fbf0be239f5afd52440cf31d98f23e","contentSum":9,"nickname":"老干妈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS","users":[{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}],"cooperationDetails":[{"emobIdFrom":"30fbf0be239f5afd52440cf31d98f23e","nickFrom":"老干妈","emobIdTo":"4531b1aa29ed4d4708c140d7e6ab4347","nickTo":"北海","detailContent":"很好很强大"},{"emobIdFrom":"58a4e33fbc97abca4051130ad9b2e2cf","nickFrom":"我是009","emobIdTo":"30fbf0be239f5afd52440cf31d98f23e","nickTo":"老干妈","detailContent":"不错不错"},{"emobIdFrom":"3d28814348965f77a098cc646bcce6df","nickFrom":"不是静芳","emobIdTo":"4531b1aa29ed4d4708c140d7e6ab4347","nickTo":"北海","detailContent":"非常好，下次还来"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来23"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"},{"emobIdFrom":"351971f409262aadafca554166621f06","nickFrom":"一方","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"},{"emobIdFrom":"efa35a16749a4303e5c56f5e8abfa143","nickFrom":"我的名字就这么长","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"}]}
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
         * cooperationId : 1
         * title : 修马桶
         * content : 专业修马桶20年
         * emobId : 30fbf0be239f5afd52440cf31d98f23e
         * contentSum : 9
         * nickname : 老干妈
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS
         * users : [{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]
         * cooperationDetails : [{"emobIdFrom":"30fbf0be239f5afd52440cf31d98f23e","nickFrom":"老干妈","emobIdTo":"4531b1aa29ed4d4708c140d7e6ab4347","nickTo":"北海","detailContent":"很好很强大"},{"emobIdFrom":"58a4e33fbc97abca4051130ad9b2e2cf","nickFrom":"我是009","emobIdTo":"30fbf0be239f5afd52440cf31d98f23e","nickTo":"老干妈","detailContent":"不错不错"},{"emobIdFrom":"3d28814348965f77a098cc646bcce6df","nickFrom":"不是静芳","emobIdTo":"4531b1aa29ed4d4708c140d7e6ab4347","nickTo":"北海","detailContent":"非常好，下次还来"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来23"},{"emobIdFrom":"d11504bf81a4949e8a348e9f783f68f7","nickFrom":"maxwell","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"},{"emobIdFrom":"351971f409262aadafca554166621f06","nickFrom":"一方","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"},{"emobIdFrom":"efa35a16749a4303e5c56f5e8abfa143","nickFrom":"我的名字就这么长","emobIdTo":"c37ce94085bbea46562a3ffaa17dc5a4","nickTo":"吧唧吧唧","detailContent":"非常好，下次还来234"}]
         */

        private int cooperationId;
        private String title;
        private String content;
        private String emobId;
        private int contentSum;
        private String nickname;
        private String avatar;

        /// 2015/11/19 添加帮内头衔字段
        private String grade;


        private int createTime;

        private List<UsersEntity> users;
        private List<CooperationDetailsEntity> cooperationDetails;

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

        public void setContentSum(int contentSum) {
            this.contentSum = contentSum;
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

        public void setCooperationDetails(List<CooperationDetailsEntity> cooperationDetails) {
            this.cooperationDetails = cooperationDetails;
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

        public int getContentSum() {
            return contentSum;
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

        public List<CooperationDetailsEntity> getCooperationDetails() {
            return cooperationDetails;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public static class UsersEntity {
            /**
             * emobId : aad8e70c725f5362c28852b281297e86
             * nickname : 我是008
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH
             * createTime : 1446544107
             */

            private String emobId;
            private String nickname;
            private String avatar;

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
        }

        public static class CooperationDetailsEntity {
            /**
             * emobIdFrom : 30fbf0be239f5afd52440cf31d98f23e
             * nickFrom : 老干妈
             * emobIdTo : 4531b1aa29ed4d4708c140d7e6ab4347
             * nickTo : 北海
             * avatar: xxX
             * detailContent : 很好很强大
             */

            private String emobIdFrom;
            private String nickFrom;
            private String emobIdTo;
            private String nickTo;
            private String detailContent;
            private String avatar;

            /// 2015/11/19 添加帮内头衔字段
            private String grade;


            public void setEmobIdFrom(String emobIdFrom) {
                this.emobIdFrom = emobIdFrom;
            }

            public void setNickFrom(String nickFrom) {
                this.nickFrom = nickFrom;
            }

            public void setEmobIdTo(String emobIdTo) {
                this.emobIdTo = emobIdTo;
            }

            public void setNickTo(String nickTo) {
                this.nickTo = nickTo;
            }

            public void setDetailContent(String detailContent) {
                this.detailContent = detailContent;
            }

            public String getEmobIdFrom() {
                return emobIdFrom;
            }

            public String getNickFrom() {
                return nickFrom;
            }

            public String getEmobIdTo() {
                return emobIdTo;
            }

            public String getNickTo() {
                return nickTo;
            }

            public String getDetailContent() {
                return detailContent;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }
        }
    }
}
