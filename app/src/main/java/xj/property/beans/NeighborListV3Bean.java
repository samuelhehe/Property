package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2016/3/9 10:22
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class NeighborListV3Bean {

    private int page;
    private int limit;
    private List<NeighborListData> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<NeighborListData> getData() {
        return data;
    }

    public void setData(List<NeighborListData> data) {
        this.data = data;
    }

    public class NeighborListData {
        private int cooperationId;
        private String title;
        private String content;
        private String emobId;
        private int communityId;
        private String nickname;
        private String avatar;
        private String grade;
        private String identity;
        private List<NeighborListUser> users;

        public List<NeighborListUser> getUsers() {
            return users;
        }

        public void setUsers(List<NeighborListUser> users) {
            this.users = users;
        }

        public int getCooperationId() {
            return cooperationId;
        }

        public void setCooperationId(int cooperationId) {
            this.cooperationId = cooperationId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public class NeighborListUser {
            private String emobId;
            private String nickname;
            private String avatar;
            private Long createTime;

            public String getEmobId() {
                return emobId;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public Long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Long createTime) {
                this.createTime = createTime;
            }
        }

    }

}
