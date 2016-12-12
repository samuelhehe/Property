package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class VoteDetailsRespBean {


    /**
    "voteId": {投票ID},
        "voteTitle": "{投票标题}",
        "emobId": "{投票发起人环信ID}",
        "createTime": {投票创建时间},
        "updateTime": {投票修改时间},
        "communityId": {小区ID},
        "status": "{投票状态}",
        "nickname": "{投票发起人昵称}",
        "avatar": "{投票发起人头像}",
        "grade": "{投票发起人帮主身份}",
        "identity": "{投票发起人牛人身份}",
        "voteSum": {被投次数},
        "options": [{
            "content": "{投票选项}",
            "percent": {被投百分比},
            "percentText": "{被投百分比}",
            "voted": {是否已投},
            "count": {被投次数}
        }],
        "voteDetails": [{
            "voteOptionsId": {被投选项的ID},
            "emobId": "{投票人环信ID}",
            "avatar": "{投票人头像}"
        }],
        "comments": [{
            "emobIdFrom": "{评论者环信ID}",
            "emobIdTo": "{被评论者环信ID}",
            "chatContent": "{评论内容}",
            "createTime": {评论时间},
            "nicknameFrom": "{评论者昵称}",
            "avatarFrom": "{评论者头像}",
            "gradeFrom": "{评论者帮主身份}",
            "identityFrom": "{评论者牛人身份}",
            "nicknameTo": "{被评论者昵称}",
            "avatarTo": "{被评论者头像}"
        }]


     */
    private int voteId;
    private String voteTitle;
    private String emobId;
    private int updateTime;
    private int createTime;
    private int communityId;
    private int voteSum;
    private int voted; //// {是否已投：1->投了，0->没投},
    private String nickname;
    private String status;
    private String avatar;

    private String grade;
    private String identity;



    private List<VoteOptionsListEntity> options;
    private List<UsersListEntity> voteDetails;
    private List<VoteChatListEntity> comments;

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setVoteSum(int voteSum) {
        this.voteSum = voteSum;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public int getVoteId() {
        return voteId;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public String getEmobId() {
        return emobId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getVoteSum() {
        return voteSum;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }



    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<VoteOptionsListEntity> getOptions() {
        return options;
    }

    public void setOptions(List<VoteOptionsListEntity> options) {
        this.options = options;
    }

    public List<UsersListEntity> getVoteDetails() {
        return voteDetails;
    }

    public void setVoteDetails(List<UsersListEntity> voteDetails) {
        this.voteDetails = voteDetails;
    }

    public List<VoteChatListEntity> getComments() {
        return comments;
    }

    public void setComments(List<VoteChatListEntity> comments) {
        this.comments = comments;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }


    public static class UsersListEntity {

        private String emobId;
        private String avatar;
        private int voteOptionsId;

        /**
         * "voteOptionsId": {被投选项的ID},
         "emobId": "{投票人环信ID}",
         "avatar": "{投票人头像}"
         *
         */

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getVoteOptionsId() {
            return voteOptionsId;
        }

        public void setVoteOptionsId(int voteOptionsId) {
            this.voteOptionsId = voteOptionsId;
        }
    }

    public static class VoteChatListEntity {
        /**
         * emobIdFrom : 384275f742e3f0ad1fb5b28220af6e00
         * emobIdTo : 9f8445aa682e68962713f458b0285ebd
         * chatContent : ceshi1
         * createTime : 1448289000
         * nicknameFrom : 我是6号
         * avatarFrom : http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof
         */

        private String emobIdFrom;
        private String emobIdTo;
        private String chatContent;
        private int createTime;
        private String nicknameFrom;
        private String nicknameTo;
        private String avatarFrom;

        private String gradeFrom;///评论者帮主身份
        private String identityFrom;///评论者帮主身份
        private String avatarTo;///被评论者头像

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

        public String getAvatarTo() {
            return avatarTo;
        }

        public void setAvatarTo(String avatarTo) {
            this.avatarTo = avatarTo;
        }

        public void setEmobIdFrom(String emobIdFrom) {
            this.emobIdFrom = emobIdFrom;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public void setChatContent(String chatContent) {
            this.chatContent = chatContent;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setNicknameFrom(String nicknameFrom) {
            this.nicknameFrom = nicknameFrom;
        }

        public void setAvatarFrom(String avatarFrom) {
            this.avatarFrom = avatarFrom;
        }

        public String getEmobIdFrom() {
            return emobIdFrom;
        }

        public String getEmobIdTo() {
            return emobIdTo;
        }

        public String getChatContent() {
            return chatContent;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getNicknameFrom() {
            return nicknameFrom;
        }

        public String getAvatarFrom() {
            return avatarFrom;
        }

        public String getNicknameTo() {
            return nicknameTo;
        }

        public void setNicknameTo(String nicknameTo) {
            this.nicknameTo = nicknameTo;
        }

    }
}
