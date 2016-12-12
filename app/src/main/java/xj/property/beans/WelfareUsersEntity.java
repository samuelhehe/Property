package xj.property.beans;

/**
 * Created by Administrator on 2015/9/16.
 */
public class WelfareUsersEntity {

    /**
     * userId : 3177
     * emobId : 2011eb792db7b1029341faab3ad65919
     * nickname : lei
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt
     */

    private int userId;
    private String emobId;
    private String nickname;
    private String avatar;

    public void setUserId(int userId) {
        this.userId = userId;
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
