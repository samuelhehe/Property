package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/16.
 */
public class GeneralZanReqBean  extends  BaseBean {
    /**
     * emobIdFrom : fcb6adf78bef4ee4940d2af8ee7905f9
     * emobIdTo : d463b16dfc014466a1e441dd685ba505
     * type : lifeCircle
     * lifeCircleId : 1
     */

       /*

    {
    "emobIdFrom": "{点赞人环信ID}",
    "emobIdTo": "{被赞人环信ID}",
    "type": "{点赞类型：lifeCircle->生活圈标识赞，activity->活动/话题标识赞}",
    "lifeCircleId": {生活圈ID，当type为lifeCircle时需要传递该字段},
    "activityId": {活动/话题ID，当type为activity时需要传递该字段}
}

     */

    //// 生活圈
    public static final String type_superzan_lifeCircle= "lifeCircle";
    //// 活动
    public static final String type_superzan_activity= "activity";

    private String emobIdFrom;
    private String emobIdTo;
    private String type;
    private int lifeCircleId;
    private int activityId;

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getType() {
        return type;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }


    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
