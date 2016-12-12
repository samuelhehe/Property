package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 * v3 2016/03/15
 */
public class HasVoteMoreNeighborRespBean {

    /*
    {
            "voteId": {投票ID},
            "voteDetailId": {投票操作ID},
            "voteOptionsId": {投票选项ID},
            "emobId": "{投票操作用户环信ID}",
            "createTime": {投票时间},
            "status": "{投票操作状态}",
            "avatar": "{投票操作用户头像}"
        }

     */

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

    public List<PageDataEntity> getData() {
        return data;
    }

    public void setData(List<PageDataEntity> data) {
        this.data = data;
    }

    private int page;
    private int limit;

    private List<PageDataEntity> data;


    public static class PageDataEntity {
        /**
         * createTime : 1456999472
         * emobId : d463b16dfc014466a1e441dd685ba505
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
         * nickname : 浮白
         * grade : normal
         * identity : famous
         */

        private int createTime;
        private String emobId;
        private String avatar;
        private String nickname;
        private String grade;
        private String identity;

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public String getGrade() {
            return grade;
        }

        public String getIdentity() {
            return identity;
        }


        /**
         * {
         "createTime": {投票时间},
         "emobId": "{投票操作用户环信ID}",
         "avatar": "{投票操作用户头像}",
         "nickname": "{投票操作用户昵称}",
         "grade": "{投票操作用户帮主身份}",
         "identity": "{投票操作用户牛人身份}"
         }
         *
         *
         */




    }
}
