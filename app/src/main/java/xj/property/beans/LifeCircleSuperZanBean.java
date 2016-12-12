package xj.property.beans;

/**
 * Created by che on 2015/9/28.
 *
 * v3 2016/03/16
 */
public class LifeCircleSuperZanBean extends xj.property.netbase.BaseBean {

    private Integer lifeCircleId;
    private Integer activityId;
    private Integer communityId;
    private String type;
    private String emobIdTo;
    private String emobIdFrom;

    //// 生活圈
    public static final String type_superzan_lifeCircle= "lifeCircle";
    //// 活动
    public static final String type_superzan_activity= "activity";

    /**
     *
     * {
     "communityId": {小区ID},
     "emobIdFrom": "{点赞人环信ID}",
     "emobIdTo": "{被赞人环信ID}",
     "type": "{点赞类型：lifeCircle->生活圈超级赞，activity->活动/话题超级赞}",
     "lifeCircleId": {生活圈ID，当type为lifeCircle时需要传递该字段},
     "activityId": {活动/话题ID，当type为activity时需要传递该字段}
     }
     *
     */

    public Integer getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(Integer lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }


    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }
}
