package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2016/3/9 18:06
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PageDataEntityV3Bean {

    private int page;
    private int limit;
    private List<PageDataEntityUserV3Bean> data;

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

    public List<PageDataEntityUserV3Bean> getData() {
        return data;
    }

    public void setData(List<PageDataEntityUserV3Bean> data) {
        this.data = data;
    }

    public class PageDataEntityUserV3Bean{
        private int userId;
        private String emobId;
        private String nickname;
        private String avatar;

        /**
         *"userId": {用户ID},
         "emobId": "{用户环信ID}",
         "nickname": "{用户昵称}",
         "avatar": "{用户头像}"
         *
         */

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

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
    }
}
