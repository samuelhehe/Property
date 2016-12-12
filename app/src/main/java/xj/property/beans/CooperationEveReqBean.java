package xj.property.beans;

/**
 * Created by Administrator on 2015/11/4.
 * v3 2016/03/14
 */
public class CooperationEveReqBean extends  xj.property.netbase.BaseBean {


    public CooperationEveReqBean(String emobIdFrom, String emobIdTo, String detailContent, int cooperationId, int communityId) {
        this.emobIdFrom = emobIdFrom;
        this.emobIdTo = emobIdTo;
        this.detailContent = detailContent;
        this.cooperationId = cooperationId;
        this.communityId = communityId;
    }

    /**
     * emobIdFrom : 3d28814348965f77a098cc646bcce6df
     * emobIdTo : 4531b1aa29ed4d4708c140d7e6ab4347
     * detailContent : 非常好，下次还来
     * cooperationId : 1
     */

    private String emobIdFrom;
    private String emobIdTo;
    private String detailContent;
    private int cooperationId;

    private int communityId;  //// v3 2016/03/14

    /**
     *
     * {
     "communityId": {小区ID},
     "emobIdFrom": "{评论者环信ID}",
     "emobIdTo": "{被评论者环信ID}",
     "cooperationId": {邻居帮ID},
     "detailContent": "{评论内容}"
     }
     *
     *
     */



    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }
    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public void setCooperationId(int cooperationId) {
        this.cooperationId = cooperationId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public int getCooperationId() {
        return cooperationId;
    }
}
