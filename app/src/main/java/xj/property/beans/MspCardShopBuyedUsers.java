package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 * v3 2016/03/14
 */
public class MspCardShopBuyedUsers {

    private int page;
    private int limit;

    private List<PageDataEntity> pageData;


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

    public List<PageDataEntity> getPageData() {
        return pageData;
    }

    public void setPageData(List<PageDataEntity> pageData) {
        this.pageData = pageData;
    }




    /**
     * '
     * <p/>
     * "page": {页码},
     * "limit": {页面大小},
     * "data": [{
     * "userId": {用户ID},
     * "emobId": "{用户环信ID}",
     * "nickname": "{用户昵称}",
     * "avatar": "{用户头像}",
     * "grade": "{用户帮主身份}",
     * "identity": "{用户牛人身份}"
     * }]
     */


    public static class PageDataEntity {

        /**
         * "userId": 4,
         * "emobId": "d463b16dfc014466a1e441dd685ba505",
         * "nickname": "浮白",
         * "avatar": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9",
         * "grade": "normal",
         * "identity": "famous"
         */


        private int userId;
        private String username;
        private String emobId;
        private String nickname;
        private String avatar;

        /// v3 2016/03/14
        private String identity;
        private String grade;


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
