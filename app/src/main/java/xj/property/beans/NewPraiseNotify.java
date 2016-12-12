package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/6/11.
 * v3 2016/03/04
 */
public class NewPraiseNotify implements XJ {
    public int contentSum;
    public int praiseSum;
    public int lifeCircleId;
    /**
     * {
     "lifeCircleId": {生活圈ID},
     "praiseSum": {被赞数量},
     "contentSum": {被评论数量},
     "users": [{
     "nickname": "{点赞或评论的用户的昵称}",
     "avatar": "{点赞或评论的用户的头像}"
     }
     */
    private List<UserSimpleInfo> users;

    public String content4Show; //// 这两个字段是为了适配BaseAdapter使用的,
    public String avatar4Show;

    public String getAvatar4Show() {
        return avatar4Show;
    }

    public void setAvatar4Show(String avatar4Show) {
        this.avatar4Show = avatar4Show;
    }

    public String getContent4Show() {
        return content4Show;
    }

    public void setContent4Show(String content4Show) {
        this.content4Show = content4Show;
    }

    public List<UserSimpleInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserSimpleInfo> users) {
        this.users = users;
        if (!users.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(users.get(0).getNickname());
            if (users.size() > 1)
                stringBuilder.append("等" + users.size() + "人");
//            stringBuilder.append("对你");
            if (contentSum > 0)
                stringBuilder.append("评论了你的生活圈");
            else
                stringBuilder.append("赞了你的人品");
            content4Show = stringBuilder.toString();
            avatar4Show = users.get(0).getAvatar();
        }
    }


    public int getContentSum() {
        return contentSum;
    }

    public static class UserSimpleInfo {
        String nickname;
        String avatar;


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

    public void setContentSum(int contentSum) {
        this.contentSum = contentSum;
    }

    public int getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    @Override
    public String toString() {
        return "NewPraiseNotify{" +
                "contentSum=" + contentSum +
                ", praiseSum=" + praiseSum +
                ", lifeCircleId=" + lifeCircleId +
                ", users=" + users +
                ", content4Show='" + content4Show + '\'' +
                ", avatar4Show='" + avatar4Show + '\'' +
                '}';
    }
}
