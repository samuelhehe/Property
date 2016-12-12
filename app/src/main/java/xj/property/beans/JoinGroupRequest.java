package xj.property.beans;

/**
 * Created by Administrator on 2015/4/10.
 *
 * v3 2016/03/03
 */
public class JoinGroupRequest extends xj.property.netbase.BaseBean{

    /**
     * {
     "emobUserId": "{要入群的用户环信ID}",
     "communityId": {用户所属小区ID}
     }
     *
     */

    public String emobUserId;

    private int communityId;

    public String getEmobUserId() {
        return emobUserId;
    }

    public void setEmobUserId(String emobUserId) {
        this.emobUserId = emobUserId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }
}