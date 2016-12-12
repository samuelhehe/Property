package xj.property.beans;

/**
 * Created by Administrator on 2015/9/17.
 */
public class WelfareQueryGoingBuy extends  xj.property.netbase.BaseBean {
    /**
     * communityId : 2
     * welfareId : 1
     * emobId : d463b16dfc014466a1e441dd685ba505
     */

    private int communityId;
    private int welfareId;
    private String emobId;

    public WelfareQueryGoingBuy(int communityId, int welfareId, String emobId) {
        this.communityId = communityId;
        this.welfareId = welfareId;
        this.emobId = emobId;
    }

    public WelfareQueryGoingBuy() {

    }


    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setWelfareId(int welfareId) {
        this.welfareId = welfareId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public int getWelfareId() {
        return welfareId;
    }

    public String getEmobId() {
        return emobId;
    }


    /**
     *
     * {
     "communityId": {小区ID},
     "welfareId": {福利ID},
     "emobId": "{用户环信ID}"
     }
     *
     */



}
