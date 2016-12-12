package xj.property.beans;

/**
 * Created by Administrator on 2015/9/25.
 */
public class InvitatePhone  extends  BaseBean{


    /**
     * phone : 15811078116
     * communityName : 狮子城
     *
     * add 2015/11/16
     * type :  home 首页/owner 帮主中邀请
     *
     */

    private String phone;

    private String communityName;

    private String type ;


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
