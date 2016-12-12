package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SendSMSReqBean extends  BaseBean {


    /**
     * communityName : {小区名称}
     * nickname : {用户昵称}
     * emobId : {用户环信ID}
     */

    private String communityName;
    private String nickname;
    private String emobId;

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmobId() {
        return emobId;
    }
}
