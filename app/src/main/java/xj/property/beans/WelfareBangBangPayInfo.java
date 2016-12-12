package xj.property.beans;

/**
 * Created by Administrator on 2015/9/18.
 */
public class WelfareBangBangPayInfo  extends  xj.property.netbase.BaseBean{


    private int bonuscoin; ///帮帮币数量
    private int welfareId; /// 福利Id
    private int communityId; /// 小区ID
    private String emobId; /// emobid


//    {
//            "welfareId": 93,
//            "communityId": 2,
//            "emobId": "d463b16dfc014466a1e441dd685ba505",
//            "bonuscoin": 698
//    }

    public int getBonuscoin() {
        return bonuscoin;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public int getWelfareId() {
        return welfareId;
    }

    public void setWelfareId(int welfareId) {
        this.welfareId = welfareId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


}
