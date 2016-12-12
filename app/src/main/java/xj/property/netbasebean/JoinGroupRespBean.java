package xj.property.netbasebean;

/**
 * Created by Administrator on 2016/3/3.
 * v3 2016/03/03
 */
public class JoinGroupRespBean {
    /**
     * activityMemberId : 5399726
     * emobGroupId : 168209969693327808
     * emobUserId : fcb6adf78bef4ee4940d2af8ee7905f9
     * createTime : 1456732775
     * communityId : 2
     */

    private int activityMemberId;
    private String emobGroupId;
    private String emobUserId;
    private int createTime;
    private int communityId;

    public void setActivityMemberId(int activityMemberId) {
        this.activityMemberId = activityMemberId;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public void setEmobUserId(String emobUserId) {
        this.emobUserId = emobUserId;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getActivityMemberId() {
        return activityMemberId;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public String getEmobUserId() {
        return emobUserId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getCommunityId() {
        return communityId;
    }


    /*

    {
        "activityMemberId": {群组成员数据ID},
        "emobGroupId": "{环信群组ID}",
        "emobUserId": "{群主成员环信ID}",
        "createTime": {创建时间},
        "communityId": {小区ID}
    }
     */



}
