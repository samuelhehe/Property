package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class TagsWhoTagMeRespV3Bean {


    private int page;
    private int limit;

    private List<DataEntity> data;

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

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }


    public class DataEntity {

        /*
        {"labelId":86,
        "createTime":1458636532,
        "emobIdFrom":"0d0ad29c32e04093854db473d2188924",
        "nickname":"嫦娥一号",
        "avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/c97d2d880dda4db4bb46b5cee9bf6823",
        "grade":"normal",
        "identity":"normal"}


         */

        private int labelId;
        private int createTime;
        private String emobIdFrom;
        private String nickname;
        private String avatar;
        private String grade;
        private String identity;

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public String getEmobIdFrom() {
            return emobIdFrom;
        }

        public void setEmobIdFrom(String emobIdFrom) {
            this.emobIdFrom = emobIdFrom;
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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }


}
