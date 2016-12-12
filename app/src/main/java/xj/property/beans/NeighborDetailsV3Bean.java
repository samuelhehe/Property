package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/9 11:39
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class NeighborDetailsV3Bean {

    private int cooperationId;
    private String title;
    private String content;
    private String emobId;
    private Long createTime;
    private int communityId;
    private String nickname;
    private String avatar;
    private String grade;
    private String identity;
    private List<NeighborDetailsUserV3Bean> users;
    private List<NeighborDetailsCooperationV3Bean> cooperationDetails;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public List<NeighborDetailsUserV3Bean> getUsers() {
        return users;
    }

    public void setUsers(List<NeighborDetailsUserV3Bean> users) {
        this.users = users;
    }

    public List<NeighborDetailsCooperationV3Bean> getCooperationDetails() {
        return cooperationDetails;
    }

    public void setCooperationDetails(List<NeighborDetailsCooperationV3Bean> cooperationDetails) {
        this.cooperationDetails = cooperationDetails;
    }

    public class NeighborDetailsUserV3Bean {

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

    public class NeighborDetailsCooperationV3Bean {
        private String emobIdFrom;
        private String detailContent;
        private String nicknameFrom;
        private String avatarFrom;
        private String gradeFrom;
        private String identityFrom;
        private String emobIdTo;
        private String nicknameTo;

        public String getEmobIdFrom() {
            return emobIdFrom;
        }

        public void setEmobIdFrom(String emobIdFrom) {
            this.emobIdFrom = emobIdFrom;
        }

        public String getDetailContent() {
            return detailContent;
        }

        public void setDetailContent(String detailContent) {
            this.detailContent = detailContent;
        }

        public String getNicknameFrom() {
            return nicknameFrom;
        }

        public void setNicknameFrom(String nicknameFrom) {
            this.nicknameFrom = nicknameFrom;
        }

        public String getAvatarFrom() {
            return avatarFrom;
        }

        public void setAvatarFrom(String avatarFrom) {
            this.avatarFrom = avatarFrom;
        }

        public String getGradeFrom() {
            return gradeFrom;
        }

        public void setGradeFrom(String gradeFrom) {
            this.gradeFrom = gradeFrom;
        }

        public String getIdentityFrom() {
            return identityFrom;
        }

        public void setIdentityFrom(String identityFrom) {
            this.identityFrom = identityFrom;
        }

        public String getEmobIdTo() {
            return emobIdTo;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public String getNicknameTo() {
            return nicknameTo;
        }

        public void setNicknameTo(String nicknameTo) {
            this.nicknameTo = nicknameTo;
        }
    }

}
