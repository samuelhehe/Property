package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CooperationContentBean {

    /**
     * cooperationId : 1
     * title : 修马桶
     * content : 专业修马桶20年
     * emobId : 30fbf0be239f5afd52440cf31d98f23e
     * nickname : 老干妈
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FhrDJbgH7ejMBOnBdvvPVEE58eJS
     * users : [{"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH","createTime":1446544107},{"emobId":"c237702dd4bbe4827f633a2d2308f2e2","nickname":"谢英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4","createTime":1446543978},{"emobId":"98b0ce078094640917cdb2f59f5ff572"}]
     */

    private int cooperationId;
    private String title;
    private String content;
    private String emobId;
    private String nickname;
    private String avatar;

    /// 2015/11/19 添加帮内头衔字段
    private String grade;

    private List<UsersEntity> users;

    public void setCooperationId(int cooperationId) {
        this.cooperationId = cooperationId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public int getCooperationId() {
        return cooperationId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public List<UsersEntity> getUsers() {
        return users;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static class UsersEntity {
        /**
         * emobId : aad8e70c725f5362c28852b281297e86
         * nickname : 我是008
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH
         * createTime : 1446544107
         */

        private String emobId;
        private String nickname;
        private String avatar;
        private int createTime;

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
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

        public int getCreateTime() {
            return createTime;
        }
    }

}
