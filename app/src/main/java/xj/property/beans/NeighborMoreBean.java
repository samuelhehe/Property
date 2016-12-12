package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/9 14:31
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class NeighborMoreBean {

    private int page;
    private int limit;
    private List<NeighborMoreUser> data;

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

    public List<NeighborMoreUser> getData() {
        return data;
    }

    public void setData(List<NeighborMoreUser> data) {
        this.data = data;
    }

    public class NeighborMoreUser {


        /**
         *
         * {
         "emobId": "fcb6adf78bef4ee4940d2af8ee7905f9",
         "nickname": "天昭",
         "avatar": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9",
         "createTime": 1457434983
         }
         *
         */
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
