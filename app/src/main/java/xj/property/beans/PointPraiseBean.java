package xj.property.beans;

/**
 * Created by che on 2015/6/10.
 * 点赞bean
 */
public class PointPraiseBean extends xj.property.netbase.BaseBean {
    /**
     * emobIdFrom : fcb6adf78bef4ee4940d2af8ee7905f9
     * emobIdTo : d463b16dfc014466a1e441dd685ba505
     * praiseType : circle
     * lifeCircleId : 1
     * communityId : 2
     */


    /**
     * {
     "emobIdFrom": "{点赞人的环信ID}",
     "emobIdTo": "{被赞人的环信ID}",
     "praiseType": "{点赞类型：circle->赞生活圈,comment->赞评论}",
     "lifeCircleId": {生活圈ID},
     "lifeCircleDetailId": {生活圈评论ID},
     "communityId": {小区ID}
     }
     */


    public static final String praise_type_circle="circle"; /// 赞生活圈
    public static final String praise_type_comment="comment";/// 赞评论

    private String emobIdFrom;
    private String emobIdTo;
    private String praiseType;
    private int lifeCircleId;
    private int communityId;

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setPraiseType(String praiseType) {
        this.praiseType = praiseType;
    }

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getPraiseType() {
        return praiseType;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public int getCommunityId() {
        return communityId;
    }




}
