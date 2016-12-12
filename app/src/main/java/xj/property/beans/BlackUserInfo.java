package xj.property.beans;

/**
 * Created by n on 2015/5/25.
 * v3 2016/03/04
 * 拉取黑名单生活圈/通讯录
 */
public class BlackUserInfo  {


    /**
     * emobIdTo : {被拉黑方环信ID}
     * nickname : {被拉黑方昵称}
     * avatar : {被拉黑房头像}
     */

    private String emobIdTo;
    private String nickname;
    private String avatar;

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }
}