package xj.property.beans;

/**
 * Created by Administrator on 2015/6/10.
 */
public class LifeEvaBean extends xj.property.netbase.BaseBean {

    public LifeEvaBean(int communityId, String emobIdTo,String emobIdFrom,int lifeCircleId ,String detailContent) {
        this.communityId = communityId;
        this.emobIdFrom = emobIdFrom;
        this.emobIdTo = emobIdTo;
        this.detailContent = detailContent;
        this.lifeCircleId = lifeCircleId;
    }

    private int communityId;
    private String emobIdFrom;
    public String emobIdTo;
    public String detailContent;
    public int lifeCircleId;

    /**
     * {
     * "communityId": {小区ID},
     * "emobIdFrom": "{评论者环信ID}",
     * "emobIdTo": "{被评论者环信ID}",
     * "detailContent": "{评论内容}",
     * "lifeCircleId": {生活圈ID}
     * }
     */

    public LifeEvaBean() {
    }



    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }
}
