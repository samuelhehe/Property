package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class VoteIndexRespInfoBean {


    private int voteId;
    private String voteTitle;
    private String emobId;
    private int createTime;
    private int voteSum;
    private String nickname;
    private String avatar;
    private String grade;
    private String identity;

    private List<VoteOptionsListEntity> options;

    /**
     * {
     "voteId": {投票ID},
     "voteTitle": "{投票标题}",
     "emobId": "{用户环信ID}",
     "createTime": {创建时间},
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主身份}",
     "identity": "{用户牛人身份}",
     "voteSum": {投票次数}
     }
     */
    private List<UsersListEntity> voteDetails;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public static class UsersListEntity {
        /**
         * emobId : 384275f742e3f0ad1fb5b28220af6e00
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof
         */
        private String emobId;
        private String avatar;

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
    }

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

}
