package xj.property.beans;

/**
 * Created by Administrator on 2015/9/17.
 */
public class WelfareGoingBuyInfo {


    /**
     * emobId : {福利消息管理员环信ID}
     * nickname : {福利消息管理员昵称}
     * avatar : {福利消息管理员昵称}
     */

    private String emobId;
    private String nickname;
    private String avatar;

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
