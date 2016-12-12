package xj.property.beans;

/**
 * Created by Administrator on 2015/11/4.
 */
public class LookedDetailsReqBean extends  xj.property.netbase.BaseBean {


    /**
     * emobId : c237702dd4bbe4827f633a2d2308f2e2
     * nickname : 谢英亮
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FsLPhnV12GnDml70YgZ4mC-vwEm4
     */

    private String emobId;
    private String nickname;
    private String avatar;
    private int communtityId;


    /*

    {
    "communityId": {用户小区ID},
    "emobId": "{用户环信ID}",
    "nickname": "{用户昵称}",
    "avatar": "{用户头像}"
}
     */

    public int getCommuntityId() {
        return communtityId;
    }

    public void setCommuntityId(int communtityId) {
        this.communtityId = communtityId;
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
