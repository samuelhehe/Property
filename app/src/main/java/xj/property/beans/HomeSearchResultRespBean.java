package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 *
 * v3 2016/03/18
 */
public class HomeSearchResultRespBean {

    /**
     * "users": {
     "page": {页码},
     "limit": {页面大小},
     "data": [{
     "emobId": "{用户环信ID}",
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主身份}",
     "identity": "{用户牛人身份}"
     }]
     },
     "lifeCircles": {
     "page": {页码},
     "limit": {页面大小},
     "data": [{
     "lifeCircleId": {生活圈ID},
     "communityId": {小区ID},
     "createTime": {发布时间},
     "lifeContent": "{生活圈内容}",
     "praiseSum": {被赞次数},
     "emobId": "{用户环信ID}",
     "type": {生活圈类型ID},
     "typeContent": "{生活圈类型}",
     "photoes": "{生活圈图片}",
     "superPraise": "{当前是否被超级赞}",
     "characterValues": {人品值},
     "avatar": "{用户头像}",
     "nickname": "{用户昵称}",
     "grade": "{用户帮主身份}",
     "identity": "{用户牛人身份}"
     }]
     }
     */

    private UsersEntity users;

    private LifeCirclesEntity lifeCircles;

    public void setUsers(UsersEntity users) {
        this.users = users;
    }

    public UsersEntity getUsers() {
        return users;
    }

    public LifeCirclesEntity getLifeCircles() {
        return lifeCircles;
    }

    public void setLifeCircles(LifeCirclesEntity lifeCircles) {
        this.lifeCircles = lifeCircles;
    }

    public static class UsersEntity {

        private int page;
        private int limit;

        private List<SearchUserResultRespBean> data;

        public List<SearchUserResultRespBean> getPageData() {
            return data;
        }
        public void setPageData(List<SearchUserResultRespBean> data) {
            this.data = data;
        }
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

        public List<SearchUserResultRespBean> getData() {
            return data;
        }

        public void setData(List<SearchUserResultRespBean> data) {
            this.data = data;
        }
    }

    public static class LifeCirclesEntity {
        private int page;
        private int limit;
        private List<SearchLifeCircleResultRespBean> data;

        public List<SearchLifeCircleResultRespBean> getData() {
            return data;
        }

        public void setData(List<SearchLifeCircleResultRespBean> data) {
            this.data = data;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }

}
